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
package gplx.xowa; import gplx.*;
public class Xoa_gfs_mgr implements GfoInvkAble, GfoInvkRootWkr {
	public Xoa_gfs_mgr(Xoa_app app) {
		this.app = app;
		GfsCore._.AddCmd(app, Invk_app);
		eval_mgr = new Xoa_app_eval(app);
	}	private Xoa_app app;
	public Xoa_app_eval Eval_mgr() {return eval_mgr;} private Xoa_app_eval eval_mgr;
	public static boolean Fail_if_unhandled = false;
	public static final String Cfg_user_custom_file = "user_custom_cfg.gfs";
	private void Run_url_by_type(String type) {
		Io_url app_data_dir = app.User().Fsys_mgr().App_data_dir();
		Io_url url = null;
		if		(String_.Eq(type, "user_system_cfg"))	url = app_data_dir.GenSubFil_nest("cfg", "user_system_cfg.gfs");
		else if	(String_.Eq(type, "user_custom_cfg"))	url = app_data_dir.GenSubFil_nest("cfg", Cfg_user_custom_file);
		else if	(String_.Eq(type, "xowa_user_cfg"))		url = app_data_dir.GenSubFil_nest("cfg", "xowa_user_cfg.gfs");
		else if	(String_.Eq(type, "xowa"))				url = app.Fsys_mgr().Root_dir().GenSubFil("xowa.gfs");
		else											throw Err_mgr._.fmt_(GRP_KEY, "invalid_gfs_type", "invalid gfs type: ~{0}", type);
		Run_url(url);
	}
	public GfoMsg Parse_root_msg(String v) {return gplx.gfs.Gfs_msg_bldr._.ParseToMsg(v);}
	public void Run_url(Io_url url) {
		Run_url_for(GfsCore._.Root(), url);
		app.Log_wtr().Log_msg_to_session_fmt("gfs_ran: ~{0}", url.Raw());
	}
	public void Run_url_for(GfoInvkAble invk, Io_url url) {
		String raw = Io_mgr._.LoadFilStr_args(url).MissingIgnored_().Exec(); if (String_.Len_eq_0(raw)) return;
		Run_str_for(invk, raw);
	}
	public Object Run_str(String raw) {return Run_str_for(GfsCore._.Root(), raw);}
	public Object Run_str_for(GfoInvkAble invk, String raw) {return Run_str_for(invk, Parse_root_msg(raw));}
	public Object Run_str_for(GfoInvkAble invk, GfoMsg root_msg) {
		try {
			int sub_msgs_len = root_msg.Subs_count();
			GfsCtx ctx = GfsCtx.new_().Fail_if_unhandled_(Fail_if_unhandled).Usr_dlg_(app.Usr_dlg());
			Object rv = null;
			for (int i = 0; i < sub_msgs_len; i++) {
				GfoMsg sub_msg = root_msg.Subs_getAt(i);
				rv = GfsCore._.ExecOne_to(ctx, invk, sub_msg);
			}
			return rv;
		} catch (Exception e) {
			app.Usr_dlg().Warn_many("", "", "error while executing script: err=~{0}", Err_.Message_gplx(e));
			return GfoInvkAble_.Rv_error;
		}
	}
	public byte[] Build_prop_set_to_bry(ByteAryBfr bfr, byte[] prop, byte[] val) {Build_prop_set(bfr, prop, val); return bfr.XtoAryAndClear();}
	public void Build_prop_set(ByteAryBfr bfr, byte[] prop, byte[] val) {
		fmtr_eval_set.Bld_bfr_many(bfr, prop, Xoa_gfs_mgr.Cfg_save_escape(val));
	}	private ByteAryFmtr fmtr_eval_set = ByteAryFmtr.new_("~{prop}_('~{val}');\n", "prop", "val");
	public byte[] Build_prop_get_to_bry(ByteAryBfr bfr, byte[] prop) {Build_prop_get(bfr, prop); return bfr.XtoAryAndClear();}
	public void Build_prop_get(ByteAryBfr bfr, byte[] prop) {
		fmtr_eval_get.Bld_bfr_many(bfr, prop);
	}	private ByteAryFmtr fmtr_eval_get = ByteAryFmtr.new_("~{prop};\n", "prop");
	static final String GRP_KEY = "Xoa_gfs_mgr";
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_run_file_by_type))		Run_url_by_type(m.ReadStr("v"));
		else if	(ctx.Match(k, Invk_fail_if_unhandled_))		{Fail_if_unhandled = m.ReadYn("v"); ctx.Fail_if_unhandled_(Fail_if_unhandled);}
		else if	(ctx.Match(k, Invk_txns))					{return GfoInvkAble_.Null;}	// FUTURE: handle version for upgrades
		else return GfoInvkAble_.Rv_unhandled;
		return this;
	}	private static final String Invk_run_file_by_type = "run_file_by_type", Invk_fail_if_unhandled_ = "fail_if_unhandled_", Invk_txns = "txns";
	public static final String Invk_app = "app";
	public static void Msg_parser_init() {
		GfsCore._.MsgParser_(gplx.gfs.Gfs_msg_bldr._);
	}
	public static byte[] Cfg_save_escape(String v) {return Cfg_save_escape(ByteAry_.new_ascii_(v));}
	public static byte[] Cfg_save_escape(byte[] v) {
		return Byte_ary_finder.Find_fwd(v, Byte_ascii.Apos) == ByteAry_.NotFound ? v : ByteAry_.Replace(v, Bry_apos_1, Bry_apos_2);
	}	static final byte[] Bry_apos_1 = ByteAry_.new_ascii_("'"), Bry_apos_2 = ByteAry_.new_ascii_("''");
	public static String Build_code(String... ary) {
		int len = ary.length;
		for (int i = 0; i < len; i++) {
			if (i != 0) Build_code_bfr.Add_byte(Byte_ascii.Dot);
			Build_code_bfr.Add_str(ary[i]);				
		}
		return Build_code_bfr.XtoStrAndClear();
	}	static final ByteAryBfr Build_code_bfr = ByteAryBfr.new_();
}
