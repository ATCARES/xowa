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
package gplx.xowa.users.prefs; import gplx.*; import gplx.xowa.*; import gplx.xowa.users.*;
import gplx.html.*;
class Prefs_trg_mgr {
	private Xoa_url_arg_hash arg_hash = new Xoa_url_arg_hash();
	public byte[] Trg_type() {return trg_type;} private byte[] trg_type;
	public byte[] Trg_val() {return trg_val;} private byte[] trg_val;
	public void Init(Xoa_url url) {
		arg_hash.Load(url);
		trg_type = arg_hash.Get_val_bry_or(Prefs_trg_mgr.Arg_option_trg_type_bry, null);
		trg_val = arg_hash.Get_val_bry_or(Prefs_trg_mgr.Arg_option_trg_val_bry, null);
	}
	public static final byte[] Arg_option_trg_type_bry = ByteAry_.new_ascii_("option_trg_type"), Arg_option_trg_val_bry = ByteAry_.new_ascii_("option_trg_val");
}
public class Prefs_mgr implements GfoInvkAble {
	public Prefs_mgr(Xoa_app app) {
		this.app = app;
		atrs_hash = new Hash_adp_bry(true);
		atrs_hash.Add(Bry_prop, ByteVal.new_(Tid_prop));
		atrs_hash.Add(Bry_prop_get, ByteVal.new_(Tid_prop_get));
		atrs_hash.Add(Bry_prop_set, ByteVal.new_(Tid_prop_set));
		html_wtr = new Prefs_html_wtr(this);
	}	private Xoa_app app; private Hash_adp_bry atrs_hash; private Html_parser html_rdr = new Html_parser(); private Prefs_html_wtr html_wtr;
	private Prefs_trg_mgr option_trgs_mgr = new Prefs_trg_mgr();
	public void Html_box_(Xog_box_html v) {this.html_box = v;} private Xog_box_html html_box;
	public void Launch() {html_box = app.Gui_mgr().Main_win().Win_adp().Html_box();}		
	public byte[] Props_get(byte[] src) {
//			option_trgs_mgr.Init(app.Gui_mgr().Main_win().Page().Url());
		if (props_get_fmtr == null) props_get_fmtr = ByteAryFmtr.keys_().Eval_mgr_(app.Gfs_mgr().Eval_mgr());
		src = this.Parse_wikitext_to_html(src);
		props_get_fmtr.Fmt_(src);
		ByteAryBfr bfr = ByteAryBfr.new_();
		try {src = props_get_fmtr.Fmt_(src).Bld_bry_none(bfr);}
		catch (Exception e) {src = ByteAry_.Add(src, ByteAry_.new_utf8_(Err_.Message_gplx_brief(e)));}
		Html_nde[] hndes = html_rdr.Parse_as_ary(src);
		hndes = Html_selecter.Select(src, hndes, atrs_hash);
		int pos = 0;
		int len = hndes.length;
		for (int i = 0; i < len; i++) {
			Html_nde hnde = hndes[i];
			bfr.Add_mid(src, pos, hnde.Tag_lhs_bgn());
			html_wtr.Write(bfr, src, hnde, i, option_trgs_mgr.Trg_type(), option_trgs_mgr.Trg_val());
			pos = hnde.Tag_rhs_end();
		}
		bfr.Add_mid(src, pos, src.length);
		return bfr.XtoAryAndClear();
	}	private ByteAryFmtr props_get_fmtr; 
	private void Props_set_and_reload() {
		Xoa_page page = app.Gui_mgr().Main_win().Page();
		Props_set(page.Data_raw());
		page.Wiki().ParsePage_root(page, true);	// reparse in order to save new values to root; needed for history and going back / fwd; DATE:2014-02-07
		app.Usr_dlg().Prog_direct("options saved (" + DateAdp_.Now().XtoStr_fmt("HH:mm:ss") + ")");
	}
	public void Props_set(byte[] src) {
		src = ByteAry_.Replace(src, ByteAry_.new_ascii_("<xowa_cmd>"), ByteAry_.new_ascii_("&lt;xowa_cmd>"));
		src = ByteAry_.Replace(src, ByteAry_.new_ascii_("</xowa_cmd>"), ByteAry_.new_ascii_("&lt;/xowa_cmd>"));
		src = this.Parse_wikitext_to_html(src);	
		Html_nde[] hndes = html_rdr.Parse_as_ary(src);
		hndes = Html_selecter.Select(src, hndes, atrs_hash);
		int len = hndes.length;
		ByteAryBfr cmd_bfr = ByteAryBfr.reset_(255);
		for (int i = 0; i < len; i++) {
			Html_nde hnde = hndes[i];
			Props_set_by_hnde(cmd_bfr, src, hnde, i);
		}
		app.Cfg_mgr().Db_save_txt();
	}
	private void Props_set_by_hnde(ByteAryBfr cmd_bfr, byte[] src, Html_nde hnde, int i) {
		byte[] eval_code = hnde.Atrs_val_by_key_bry(Bry_prop);
		if 	(eval_code == null) eval_code = hnde.Atrs_val_by_key_bry(Bry_prop_set);
		String hnde_val = null;
		String hnde_key = "xowa_prop_" + Int_.XtoStr(i);
		switch (Prefs_mgr.Elem_tid_tid_of(hnde)) {
			case Elem_tid_input_text:
			case Elem_tid_textarea: 
			case Elem_tid_input_xowa_io:
			case Elem_tid_select: 			hnde_val = html_box.Html_elem_atr_get_str(hnde_key, gplx.gfui.Gfui_html.Atr_value); break;
			case Elem_tid_input_checkbox:	hnde_val = html_box.Html_elem_atr_get_bool(hnde_key, "checked") ? "y" : "n"; break;
		}			
		byte[] get_cmd = Props_get(eval_code); 
		Object get_val = Eval_run(ByteAry_.Add(get_cmd, Byte_ascii.Semic));
		if (String_.Eq(Object_.XtoStr_OrNullStr(get_val), hnde_val)) return;
		try		{app.Cfg_mgr().Set_by_app(String_.new_utf8_(get_cmd), hnde_val);}
		catch (Exception e) {app.Usr_dlg().Warn_many("", "", "pref update failed: code=~{0} err=~{1}", String_.new_utf8_(eval_code), Err_.Message_gplx_brief(e));}
	}
	Object Eval_run(byte[] cmd) {
		try {return Eval(cmd);}
		catch (Exception e) {Err_.Noop(e); return null;}		
	}
	byte[] Parse_wikitext_to_html(byte[] src) {
		Xow_wiki wiki = app.User().Wiki();		
		Xop_root_tkn root = new Xop_root_tkn();
		Xop_ctx ctx = wiki.Ctx();
		ctx.Page().Clear();
		wiki.Parser().Parse_page_all(root, ctx, ctx.Tkn_mkr(), src, 0);
		return root.Data_mid();			
	}
	public Object Eval(byte[] code) {return app.Gfs_mgr().Run_str(String_.new_ascii_(code));}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_exec_get))		return Props_get(m.ReadBry("v"));
		else if	(ctx.Match(k, Invk_save))			Props_set_and_reload();
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}	private static final String Invk_exec_get = "exec_get", Invk_save = "save";
	private static final byte Tid_prop = 0, Tid_prop_get = 1, Tid_prop_set = 2;
	public static final byte[] Bry_prop = ByteAry_.new_ascii_("xowa_prop"), Bry_prop_get = ByteAry_.new_ascii_("xowa_prop_get"), Bry_prop_set = ByteAry_.new_ascii_("xowa_prop_set"), Bry_id = ByteAry_.new_ascii_("id");
	public static byte Elem_tid_tid_of(Html_nde hnde) {
		byte[] elem_name = ByteAry_.Mid(hnde.Src(), hnde.Name_bgn(), hnde.Name_end());
		if		(ByteAry_.Eq(elem_name, Nde_textarea)) 			return Elem_tid_textarea;
		else if	(ByteAry_.Eq(elem_name, Nde_select)) 			return Elem_tid_select;
		else if	(ByteAry_.Eq(elem_name, Nde_input)) {
			byte[] input_type = hnde.Atrs_val_by_key_bry(Input_type);
			if 		(input_type == null) 						return Elem_tid_input_text;// treat <input /> as <input type='text'/>
			if		(ByteAry_.Eq(input_type, Type_text))		return Elem_tid_input_text;
			else if	(ByteAry_.Eq(input_type, Type_checkbox))	return Elem_tid_input_checkbox;
			else if	(ByteAry_.Eq(input_type, Type_combo))		return Elem_tid_input_combo;
			else if	(ByteAry_.Eq(input_type, Type_xowa_io))		return Elem_tid_input_xowa_io;
			else 												return Elem_tid_null;
		}
		else													return Elem_tid_null;
	}	static final byte[] Input_type = ByteAry_.new_ascii_("type"), Nde_input = ByteAry_.new_ascii_("input"), Nde_textarea = ByteAry_.new_ascii_("textarea"), Nde_select = ByteAry_.new_ascii_("select"), Type_text = ByteAry_.new_ascii_("text"), Type_checkbox = ByteAry_.new_ascii_("checkbox"), Type_combo = ByteAry_.new_ascii_("xowa_combo"), Type_xowa_io = ByteAry_.new_ascii_("xowa_io");
	public static final byte Elem_tid_null = 0, Elem_tid_input_text = 1, Elem_tid_textarea = 2, Elem_tid_input_checkbox = 3, Elem_tid_select = 4, Elem_tid_input_combo = 5, Elem_tid_input_xowa_io = 6;	
}
