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
package gplx.thread_cmds; import gplx.*;
import gplx.gfui.*;
public class Gfo_thread_cmd_unzip implements Gfo_thread_cmd {
	public Gfo_thread_cmd_unzip Init(Gfo_usr_dlg usr_dlg, Gfui_kit kit, ProcessAdp bzip2_process, ProcessAdp zip_process, Io_url src, Io_url trg) {
		this.src = src; this.trg = trg; this.kit = kit; this.usr_dlg = usr_dlg;
		this.bzip2_process = bzip2_process;
		this.zip_process = zip_process;
		return this;
	}	Io_url src, trg; ProcessAdp bzip2_process, zip_process, active_process; Gfui_kit kit; Gfo_usr_dlg usr_dlg; String src_ext = ".bz2";
	public GfoInvkAble Owner() {return owner;} public Gfo_thread_cmd_unzip Owner_(GfoInvkAble v) {owner = v; return this;} GfoInvkAble owner;
	public void Cmd_init() {}
	@gplx.Virtual public String Async_key() {return KEY;}
	public Gfo_thread_cmd Async_next_cmd() {return next_cmd;} public void Async_next_cmd_(Gfo_thread_cmd v) {next_cmd = v;} Gfo_thread_cmd next_cmd;
	public ByteAryFmtr_eval_mgr Url_eval_mgr() {return url_eval_mgr;} public Gfo_thread_cmd_unzip Url_eval_mgr_(ByteAryFmtr_eval_mgr v) {url_eval_mgr = v; return this;} ByteAryFmtr_eval_mgr url_eval_mgr;
	public int Async_sleep_interval()	{return Gfo_thread_cmd_.Async_sleep_interval_1_second;}
	public boolean Async_prog_enabled()	{return true;}
	public void Async_prog_run(int async_sleep_sum) {
		String size_str = " please wait...";
		if (trg.Type_fil()) size_str = gplx.ios.Io_size_.Xto_str(Io_mgr._.QueryFil(trg).Size());
		usr_dlg.Prog_many(GRP_KEY, "unzip", "unzipping: ~{0}", size_str);
	}
	@gplx.Virtual public byte Async_init() {
		if (!Io_mgr._.ExistsFil(src)) {
			kit.Ask_ok(GRP_KEY, "source_missing", "Source file does not exist: '~{0}'", src.Raw());
			return Gfo_thread_cmd_.Init_cancel_step;
		}
		trg_is_dir = trg.Type_dir();
		if (delete_trg_if_exists
			&&	(( trg_is_dir && Io_mgr._.ExistsDir(trg))
				||	(!trg_is_dir && Io_mgr._.ExistsFil(trg)))
			) {
			int rslt = kit.Ask_yes_no_cancel(GRP_KEY, "target_exists", "Target file already exists: '~{0}'.\nDo you want to delete it?", trg.Raw());
			switch (rslt) {
				case Gfui_dlg_msg_.Btn_yes:		if (trg_is_dir) Io_mgr._.DeleteDirDeep(trg); else Io_mgr._.DeleteFil(trg); break;
				case Gfui_dlg_msg_.Btn_no:		return Gfo_thread_cmd_.Init_cancel_step;
				case Gfui_dlg_msg_.Btn_cancel:	return Gfo_thread_cmd_.Init_cancel_all;
			}
		}
		return Gfo_thread_cmd_.Init_ok;
	}
	public boolean Async_running() {return active_process.Exit_code() == ProcessAdp.Exit_init;}
	public void Async_run() {
		active_process =  String_.Eq(src_ext, ".bz2") ? bzip2_process : zip_process;
		usr_dlg.Prog_many(GRP_KEY, "bgn", "unzipping");
		Io_mgr._.CreateDirIfAbsent(trg.OwnerDir());
		active_process.Run_mode_(ProcessAdp.Run_mode_async);
		Io_url unzip_dir = trg_is_dir ? trg : trg.OwnerDir();
		active_process.Run(src, trg, unzip_dir.Xto_api());
	}
	public boolean Async_term() {
		if (rename_dir) {
			Io_url[] dirs = Io_mgr._.QueryDir_args(trg).DirOnly_().Recur_(false).ExecAsUrlAry();
			if (dirs.length != 1) {
				kit.Ask_ok(GRP_KEY, "rename.fail", "expected only one directory: dir=~{0} actual=~{1}", trg.Raw(), dirs.length);
				return false;
			}
			Io_url zip_dir = dirs[0];
			Io_mgr._.MoveDirDeep(zip_dir, trg);
		}
		switch (term_cmd_for_src) {
			case Term_cmd_for_src_noop: break;
			case Term_cmd_for_src_delete: Io_mgr._.DeleteFil(src); break;
			case Term_cmd_for_src_move:
				if (term_cmd_for_src_url == Io_url_.Null) throw Err_mgr._.fmt_(GRP_KEY, "url_missing", "move specified, but no url");
				Io_mgr._.MoveFil_args(src, term_cmd_for_src_url, true).Exec();
				break;
			default: throw Err_mgr._.unhandled_(term_cmd_for_src);
		}
		usr_dlg.Prog_many(GRP_KEY, "done", "");
		return true;
	}
	public static final byte Term_cmd_for_src_noop = 0, Term_cmd_for_src_delete = 1, Term_cmd_for_src_move = 2;
	boolean rename_dir = false, trg_is_dir = false, delete_trg_if_exists = true;
	public byte Term_cmd_for_src() {return term_cmd_for_src;} public void Term_cmd_for_src_(byte v) {term_cmd_for_src = v;} private byte term_cmd_for_src = Term_cmd_for_src_delete;
	public Io_url Term_cmd_for_src_url() {return term_cmd_for_src_url;} public void Term_cmd_for_src_url_(Io_url v) {this.term_cmd_for_src_url = v;} Io_url term_cmd_for_src_url = Io_url_.Null;
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_owner))					return owner;
		else if	(ctx.Match(k, Invk_src_))					src = ByteAryFmtr_eval_mgr_.Eval_url(url_eval_mgr, m.ReadBry("v"));
		else if	(ctx.Match(k, Invk_src_ext_))				src_ext = m.ReadStr("v");
		else if	(ctx.Match(k, Invk_trg_))					trg = ByteAryFmtr_eval_mgr_.Eval_url(url_eval_mgr, m.ReadBry("v"));
		else if	(ctx.Match(k, Invk_rename_dir_))			rename_dir = m.ReadYn("v");
		else if	(ctx.Match(k, Invk_delete_trg_if_exists_))	delete_trg_if_exists = m.ReadYn("v");
		else if	(ctx.Match(k, Invk_term_cmd_for_src_))		term_cmd_for_src = Term_cmd_for_src_parse_(m.ReadStr("v"));
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}	private static final String Invk_owner = "owner", Invk_src_ = "src_", Invk_trg_ = "trg_", Invk_rename_dir_ = "rename_dir_", Invk_delete_trg_if_exists_ = "delete_trg_if_exists_", Invk_term_cmd_for_src_ = "term_cmd_for_src_", Invk_src_ext_ = "src_ext_";
	private static byte Term_cmd_for_src_parse_(String s) {
		if 		(String_.Eq(s, "noop")) 		return Term_cmd_for_src_noop;
		else if (String_.Eq(s, "delete")) 		return Term_cmd_for_src_delete;
		else if (String_.Eq(s, "move")) 		return Term_cmd_for_src_move;
		else									throw Err_mgr._.unhandled_(s);
	}
	static final String GRP_KEY = "xowa.thread.file.unzip";
	public static final String KEY = "file.unzip";
}
