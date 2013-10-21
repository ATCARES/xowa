/*
XOWA: the XOWA Offline Wiki Application
Copyright (C) 2012 gnosygnu@gmail.com

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU Affero General Public License as
published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU Affero General Public License for more details.

You should have received a copy of the GNU Affero General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package gplx.xowa.bldrs.oimgs; import gplx.*; import gplx.xowa.*; import gplx.xowa.bldrs.*;
import gplx.dbs.*; import gplx.xowa.dbs.*; import gplx.xowa.files.*;
public class Xob_xfer_temp_cmd_orig extends Xob_itm_basic_base implements Xob_cmd {
	private byte[] ext_rules_key = ByteAry_.Empty;
	public Xob_xfer_temp_cmd_orig(Xob_bldr bldr, Xow_wiki wiki) {this.Cmd_init(bldr, wiki);}
	public String Cmd_key() {return KEY_oimg;} public static final String KEY_oimg = "oimg.xfer_temp_orig";
	public void Cmd_ini(Xob_bldr bldr) {}
	public void Cmd_bgn(Xob_bldr bldr) {
		Db_provider provider = Xodb_db_file.init__oimg_lnki(wiki).Provider();
		Db_stmt trg_stmt = Xob_xfer_temp_tbl.Insert_stmt(provider);

		provider.Txn_mgr().Txn_bgn_if_none();
		DataRdr rdr = provider.Exec_sql_as_rdr(Sql_select);
		long[] ext_maxs = Calc_ext_max();
		while (rdr.MoveNextPeer()) {
			String orig_media_type = rdr.ReadStrOr(Xob_orig_regy_tbl.Fld_oor_orig_media_type, "");	// convert nulls to ""
			byte orig_media_type_tid = Xof_media_type.Xto_byte(orig_media_type);
			byte lnki_ext = rdr.ReadByte(Xob_lnki_regy_tbl.Fld_olr_lnki_ext);
			int lnki_id = rdr.ReadInt(Xob_lnki_regy_tbl.Fld_olr_lnki_id);
			byte orig_repo = rdr.ReadByte(Xob_orig_regy_tbl.Fld_oor_orig_repo);
			int orig_page_id = rdr.ReadIntOr(Xob_orig_regy_tbl.Fld_oor_orig_page_id, -1);
			if (orig_page_id == -1) continue;	// no orig found; ignore
			String join_ttl = rdr.ReadStr(Xob_orig_regy_tbl.Fld_oor_orig_join_ttl);
			String redirect_src = rdr.ReadStr(Xob_orig_regy_tbl.Fld_oor_lnki_ttl);
			if (String_.Eq(join_ttl, redirect_src))	// lnki_ttl is same as redirect_src; not a redirect
				redirect_src = "";
			int orig_w = rdr.ReadIntOr(Xob_orig_regy_tbl.Fld_oor_orig_w, -1);
			int orig_h = rdr.ReadIntOr(Xob_orig_regy_tbl.Fld_oor_orig_h, -1);
			if (   orig_media_type_tid == Xof_media_type.Tid_video	// media_type is "VIDEO"
				&& lnki_ext == Xof_ext_.Id_ogg						// ext is ".ogg"
				)
				lnki_ext = Xof_ext_.Id_ogv;							// some .ogg files are "VIDEO"; manually override lnki_ext type
			int orig_size = rdr.ReadIntOr(Xob_orig_regy_tbl.Fld_oor_orig_size, -1);
			if (orig_size > ext_maxs[lnki_ext]) continue;
			Xob_xfer_temp_tbl.Insert(trg_stmt, lnki_id, orig_repo, orig_page_id, join_ttl, redirect_src, lnki_ext, Xop_lnki_type.Id_none, orig_media_type
			, Bool_.Y									// orig is y
			, orig_w, orig_h
			, orig_w, orig_h							// file_w, file_h is same as orig_w,orig_h; i.e.: make same file_w as orig_w
			, Xof_img_size.Null, Xof_img_size.Null		// html_w, html_h is -1; i.e.: will not be displayed in page at specific size (this matches logic in Xob_xfer_temp_cmd_thumb)
			, Xop_lnki_tkn.Thumbtime_null, 0);
		}
		provider.Txn_mgr().Txn_end_all();
	}
	private long[] Calc_ext_max() {
		Xoft_rule_grp ext_rules = wiki.App().File_mgr().Ext_rules().Get_or_new(ext_rules_key);
		long[] rv = new long[Xof_ext_.Id__max];
		for (int i = 0; i < Xof_ext_.Id__max; i++) {
			byte[] ext = Xof_ext_.get_by_id(i);
			Xoft_rule_itm ext_rule = ext_rules.Get_or_null(ext);
			long max = ext_rule == null ? 0 : ext_rule.Make_max();
			rv[i] = max;
		}
		return rv;
	}
	public void Cmd_run() {}
	public void Cmd_end() {}
	public void Cmd_print() {}
	private static final String
		Sql_select = String_.Concat_lines_nl
	(	"SELECT  DISTINCT"
	,   "        olr_lnki_id"
	,	",       olr_lnki_ttl"
	,	",       olr_lnki_ext"
	,	",       oor_orig_repo"
	,	",       oor_orig_page_id"
	,	",       oor_orig_join_id"
	,	",       oor_orig_join_ttl"
	,	",       oor_lnki_ttl"
	,	",       oor_orig_size"
	,	",       oor_orig_w"
	,	",       oor_orig_h"
	,	",       oor_orig_bits"
	,	",       oor_orig_media_type"
	,	"FROM    oimg_lnki_regy l"
	,	"        JOIN oimg_orig_regy f ON f.oor_lnki_ttl = l.olr_lnki_ttl"
	,	"ORDER BY f.oor_orig_join_ttl DESC"
	);
	@Override public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_ext_rules_))			ext_rules_key = m.ReadBry("v");
		else	return super.Invk (ctx, ikey, k, m);
		return this;
	}	private static final String Invk_ext_rules_ = "ext_rules_";
}
class Xof_media_type {
	public static final byte Tid_null = 0, Tid_audio = 1, Tid_bitmap = 2, Tid_drawing = 2, Tid_office = 3, Tid_video = 4;
	public static final String Name_null = "", Name_audio = "AUDIO", Name_bitmap = "BITMAP", Name_drawing = "DRAWING", Name_office = "OFFICE", Name_video = "VIDEO";
	public static byte Xto_byte(String v) {
		if		(String_.Eq(v, Name_audio))		return Tid_audio;
		else if	(String_.Eq(v, Name_bitmap))	return Tid_bitmap;
		else if	(String_.Eq(v, Name_drawing))	return Tid_drawing;
		else if	(String_.Eq(v, Name_office))	return Tid_office;
		else if	(String_.Eq(v, Name_video))		return Tid_video;
		else									return Tid_null;
	}
}