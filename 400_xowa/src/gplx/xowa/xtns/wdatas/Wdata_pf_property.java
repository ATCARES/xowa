/*
XOWA: the extensible offline wiki application
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
package gplx.xowa.xtns.wdatas; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.json.*;
public class Wdata_pf_property extends Pf_func_base {
	@Override public int Id() {return Xol_kwd_grp_.Id_property;}
	@Override public Pf_func New(int id, byte[] name) {return new Wdata_pf_property().Name_(name);}
	@Override public void Func_evaluate(Xop_ctx ctx, byte[] src, Xot_invk caller, Xot_invk self, ByteAryBfr bfr) {// {{#property:pNumber|}}
		Xoa_app app = ctx.App();
		Wdata_wiki_mgr wdata_mgr = app.Wiki_mgr().Wdata_mgr();
		Xow_wiki wiki = ctx.Wiki();
		Xoa_ttl ttl = ctx.Page().Page_ttl();

		Wdata_pf_property_data data = new Wdata_pf_property_data();
		data.Parse(ctx, src, caller, self, this);
		Wdata_doc prop_doc = wdata_mgr.Pages_get(wiki, ttl, data); if (prop_doc == null) {Print_empty(app.Usr_dlg(), "wdata_not_found", "wdata page not found: ~{0} ~{1}", wiki.Key_str(), ttl.Page_db_as_str()); return;}
		int pid = data.Id_int();
		if (pid == Wdata_wiki_mgr.Pid_null)
			pid = wdata_mgr.Pids_get(wiki.Wdata_wiki_lang(), data.Id());
		if (pid == Wdata_wiki_mgr.Pid_null) {Print_self(app.Usr_dlg(), bfr, src, self, "prop_not_found", "prop not found: ~{0} ~{1} ~{2}", wiki.Key_str(), ttl.Page_db_as_str(), data.Id()); return;}
		Wdata_prop_grp prop_grp = prop_doc.Prop_get(pid); if (prop_grp == null) {Print_empty(app.Usr_dlg(), "prop_not_found", "prop not found: ~{0} ~{1} ~{2}", wiki.Key_str(), ttl.Page_db_as_str(), pid); return;}
		wdata_mgr.Resolve_to_bfr(bfr, prop_grp, ctx.Page().Lang().Key_bry());
	}
	public static int Parse_pid(NumberParser num_parser, byte[] bry) {
		int bry_len = bry.length;
		if (bry_len < 2) return Wdata_wiki_mgr.Pid_null;	// must have at least 2 chars; p#
		byte b_0 = bry[0];
		if (b_0 != Byte_ascii.Ltr_p && b_0 != Byte_ascii.Ltr_P)	return Wdata_wiki_mgr.Pid_null;
		num_parser.Parse(bry, 1, bry_len);
		return num_parser.HasErr() ? Wdata_wiki_mgr.Pid_null : num_parser.AsInt();
	}
	public static void Print_self(Gfo_usr_dlg usr_dlg, ByteAryBfr bfr, byte[] src, Xot_invk self, String warn_cls, String warn_fmt, Object... args) {
		bfr.Add_mid(src, self.Src_bgn(), self.Src_end());
		usr_dlg.Warn_many(GRP_KEY, warn_cls, warn_fmt, args);
	}
	public static void Print_empty(Gfo_usr_dlg usr_dlg, String warn_cls, String warn_fmt, Object... args) {
		usr_dlg.Warn_many(GRP_KEY, warn_cls, warn_fmt, args);
	}
	private static final String GRP_KEY = "xowa.xtns.wdata.property";
}
class Wdata_pf_property_data {
	public byte[] Of() {return of;} private byte[] of;
	public byte[] Q() {return q;} private byte[] q;
	public byte[] Id() {return id;} private byte[] id;
	public int Id_int() {return id_int;} private int id_int;
	public void Parse(Xop_ctx ctx, byte[] src, Xot_invk caller, Xot_invk self, Wdata_pf_property pfunc) {
		id = pfunc.Eval_argx(ctx, src, caller, self);
		id_int = Wdata_pf_property.Parse_pid(ctx.App().Utl_num_parser(), id);
		if (id_int == Wdata_wiki_mgr.Pid_null) {}	// named; TODO: get pid from pid_regy
		int args_len = self.Args_len();
		ByteAryBfr tmp_bfr = ctx.Wiki().Utl_bry_bfr_mkr().Get_b512();
		for (int i = 0; i < args_len; i++) {
			Arg_nde_tkn nde = self.Args_get_by_idx(i);
			Arg_itm_tkn nde_key = nde.Key_tkn();
			Object o = Atr_keys.Get_by_mid(src, nde_key.Src_bgn(), nde_key.Src_end());
			if (o == null) {ctx.App().Usr_dlg().Warn_many("", "", "unknown key for property: ~{0} ~{1}", String_.new_utf8_(ctx.Page().Page_ttl().Full_txt()), String_.new_utf8_(src, self.Src_bgn(), self.Src_end())); continue;}
			nde.Val_tkn().Tmpl_evaluate(ctx, src, self, tmp_bfr);
			byte[] val = tmp_bfr.XtoAryAndClear();
			byte key_tid = ((ByteVal)o).Val(); 
			switch (key_tid) {
				case Atr_of_id: of = val; break;
				case Atr_q_id:  q = val; break;
				default: throw Err_.unhandled(key_tid);
			}
		}
		tmp_bfr.Mkr_rls();	
	}
	static final byte Atr_of_id = 1, Atr_q_id = 2;
	private static final byte[] Atr_of_bry = ByteAry_.new_ascii_("of"), Atr_q_bry = ByteAry_.new_ascii_("q");
	private static final Hash_adp_bry Atr_keys = new Hash_adp_bry(false).Add_bry_byteVal(Atr_of_bry, Atr_of_id).Add_bry_byteVal(Atr_q_bry, Atr_q_id);
} 