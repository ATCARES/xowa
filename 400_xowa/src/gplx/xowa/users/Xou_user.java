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
package gplx.xowa.users; import gplx.*; import gplx.xowa.*;
import gplx.xowa.users.dbs.*;
import gplx.xowa.wikis.*; import gplx.xowa.users.history.*; import gplx.xowa.xtns.scribunto.*;
public class Xou_user implements GfoInvkAble {
	public Xou_user(Xoa_app app, Io_url user_dir) {
		this.app = app; this.key_str = user_dir.NameOnly(); key_bry = ByteAry_.new_utf8_(key_str);
		fsys_mgr = new Xou_fsys_mgr(app, this, user_dir);
		prefs_mgr = new gplx.xowa.users.prefs.Prefs_mgr(app);
		cfg_mgr = new Xou_cfg(this);
		session_mgr = new Xou_session(this);
		history_mgr = new Xou_history_mgr(this);
		db_mgr = new Xou_db_mgr(app);
	}
	public Xoa_app App() {return app;} private Xoa_app app;
	public String Key_str() {return key_str;} private String key_str;
	public byte[] Key_bry() {return key_bry;} private byte[] key_bry;
	public Xol_lang Lang() {if (lang == null) {lang = app.Lang_mgr().Get_by_key_or_new(app.Sys_cfg().Lang()); lang.Init_by_load();} return lang;} private Xol_lang lang;
	public Xou_fsys_mgr Fsys_mgr() {return fsys_mgr;} private Xou_fsys_mgr fsys_mgr;
	public Xow_wiki Default_wiki() {return default_wiki_key == null ? app.Wiki_mgr().Get_at(0) : app.Wiki_mgr().Get_by_key_or_make(default_wiki_key);} private byte[] default_wiki_key = ByteAry_.new_ascii_("en.wikipedia.org"); 
	public Xow_wiki Wiki() {if (wiki == null) wiki = Xou_user_.new_or_create_(this, app); return wiki;} private Xow_wiki wiki;
	public Xou_history_mgr History_mgr() {return history_mgr;} private Xou_history_mgr history_mgr;
	public Xou_cfg Cfg_mgr() {return cfg_mgr;} private Xou_cfg cfg_mgr;
	public Xou_session Session_mgr() {return session_mgr;} private Xou_session session_mgr;
	public Xou_db_mgr Db_mgr() {return db_mgr;} private Xou_db_mgr db_mgr;
	public gplx.xowa.users.prefs.Prefs_mgr Prefs_mgr() {return prefs_mgr;} gplx.xowa.users.prefs.Prefs_mgr prefs_mgr;
	public Xow_msg_mgr Msg_mgr() {
		if (msg_mgr == null)
			msg_mgr = new Xow_msg_mgr(this.Wiki(), this.Lang());	// NOTE: must call this.Lang() not this.lang, else nullRef exception when using "app.shell.fetch_page"; DATE:2013-04-12
		return msg_mgr;} private Xow_msg_mgr msg_mgr;
	public void Init_by_app() {
		Io_url user_system_cfg = fsys_mgr.App_data_cfg_dir().GenSubFil(Xou_fsys_mgr.Name_user_system_cfg);
		if (!Io_mgr._.ExistsFil(user_system_cfg)) Xou_user_.User_system_cfg_make(app.Usr_dlg(), user_system_cfg);
		if (!Env_.Mode_testing())
			db_mgr.App_init();
	}
	public void App_term() {
		session_mgr.Window_mgr().Save_window(app.Gui_mgr().Main_win().Win());
		history_mgr.Save(app);
		db_mgr.App_term();
	}
	public void Bookmarks_add(Xoa_page page) {
		ByteAryBfr tmp_bfr = wiki.Utl_bry_bfr_mkr().Get_k004();
		bookmarks_add_fmtr.Bld_bfr_many(tmp_bfr, page.Wiki().Domain_bry(), page.Ttl().Full_txt());
		byte[] new_entry = tmp_bfr.Mkr_rls().XtoAryAndClear();
		Xoa_ttl bookmarks_ttl = Xoa_ttl.parse_(wiki, Bry_data_bookmarks);
		Xoa_page bookmarks_page = wiki.Data_mgr().Get_page(bookmarks_ttl, false);
		byte[] new_data = ByteAry_.Add(bookmarks_page.Data_raw(), new_entry);
		wiki.Db_mgr().Save_mgr().Data_update(bookmarks_page, new_data);
	}	private ByteAryFmtr bookmarks_add_fmtr = ByteAryFmtr.new_("* [[~{wiki_key}:~{page_name}]]\n", "wiki_key", "page_name"); byte[] Bry_data_bookmarks = ByteAry_.new_utf8_("Data:Bookmarks");
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {			
		if		(ctx.Match(k, Invk_available_from_bulk))		Available_from_bulk(m.ReadBry("v"));
		else if	(ctx.Match(k, Invk_available_from_fsys))		Available_from_fsys();
		else if	(ctx.Match(k, Invk_msgs))						return this.Msg_mgr();
		else if	(ctx.Match(k, Invk_lang))						return lang;
		else if	(ctx.Match(k, Invk_default_wiki_))				default_wiki_key = m.ReadBry("v");
		else if	(ctx.Match(k, Invk_bookmarks_add_fmt_))			bookmarks_add_fmtr.Fmt_(m.ReadBry("v"));
		else if	(ctx.Match(k, Invk_name))						return key_str;
		else if	(ctx.Match(k, Invk_wiki))						return this.Wiki();	// NOTE: mass parse relies on this being this.Wiki(), not wiki
		else if	(ctx.Match(k, Invk_history))					return history_mgr;
		else if	(ctx.Match(k, Invk_fsys))						return fsys_mgr;
		else if	(ctx.Match(k, Invk_prefs))						return prefs_mgr;
		else if	(ctx.Match(k, Invk_cfg))						return cfg_mgr;
		else if	(ctx.Match(k, Invk_session))					return session_mgr;
		else return GfoInvkAble_.Rv_unhandled;
		return this;
	}	public static final String Invk_available_from_fsys = "available_from_fsys", Invk_available_from_bulk = "available_from_bulk", Invk_default_wiki_ = "default_wiki_", Invk_bookmarks_add_fmt_ = "bookmarks_add_fmt_", Invk_name = "name", Invk_wiki = "wiki", Invk_history = "history", Invk_fsys = "fsys", Invk_lang = "lang", Invk_msgs = "msgs", Invk_prefs = "prefs", Invk_cfg = "cfg", Invk_session = "session";
	public static final String Key_xowa_user = "anonymous";
	public void Available_from_fsys() {
		Io_url bookmarks_dir = fsys_mgr.Home_wiki_dir().GenSubDir_nest("wiki", "home", "ns", "730");	// NOTE: putting bookmark check here (instead of at init) b/c Init runs before xowa.gfs, and Bookmarks needs xowa.gfs to run first
		if (!Io_mgr._.ExistsDir(bookmarks_dir)) Xou_user_.Bookmarks_make(app, this.Wiki());

		Io_url[] dirs = Io_mgr._.QueryDir_args(app.Fsys_mgr().Wiki_dir()).Recur_(false).DirOnly_().ExecAsUrlAry();
		Xow_wiki usr_wiki = Wiki();
		int dirs_len = dirs.length;
		for (int i = 0; i < dirs_len; i++) {
			Io_url dir = dirs[i];
			String name = dir.NameOnly();
			if (String_.Eq(name, gplx.xowa.bldrs.imports.Xobc_core_batch.Dir_dump)
//					|| !Io_mgr._.ExistsDir(dir.GenSubFil_nest("ns"))
				) continue;
			byte[] dir_name_as_bry = ByteAry_.new_utf8_(name);
			Available_add(usr_wiki, dir_name_as_bry);
			app.Setup_mgr().Maint_mgr().Wiki_mgr().Add(dir_name_as_bry);
		}
	}
	private void Available_from_bulk(byte[] raw) {
		byte[][] wikis = ByteAry_.Split(raw, Byte_ascii.NewLine);
		Xow_wiki usr_wiki = Wiki();
		int wikis_len = wikis.length;
		for (int i = 0; i < wikis_len; i++)
			Available_add(usr_wiki, wikis[i]);
	}
	private void Available_add(Xow_wiki usr_wiki, byte[] wiki_name) {usr_wiki.Xwiki_mgr().Add_full(wiki_name, wiki_name);}
}
class Xou_user_ {
	public static Xow_wiki new_or_create_(Xou_user user, Xoa_app app) {
		Io_url wiki_dir = user.Fsys_mgr().Home_wiki_dir().GenSubDir_nest("wiki", Xow_wiki_domain_.Key_home_str);
		Xol_lang lang = app.Lang_mgr().Get_by_key_or_new(app.Lang_mgr().Default_lang());
		lang.Init_by_load();	// NOTE: lang.Load() must occur before new Xow_wiki b/c wiki will create parsers based on lang
		Xow_wiki rv = new Xow_wiki(app, wiki_dir, ns_home_(), lang);
		app.Wiki_mgr().Add(rv);
		rv.Sys_cfg().Xowa_cmd_enabled_(true);
		rv.Sys_cfg().Xowa_proto_enabled_(true);
		return rv;
	}
	public static void User_system_cfg_make(Gfo_usr_dlg usr_dlg, Io_url cfg_fil) {
		usr_dlg.Log_many(GRP_KEY, "user_system_cfg.create", "creating user_system_cfg.gfs: ~{0}", cfg_fil.Raw());
		Io_mgr._.SaveFilStr(cfg_fil, User_system_cfg_text);
	}
	public static void Bookmarks_make(Xoa_app app, Xow_wiki home_wiki) {
		app.Usr_dlg().Log_many(GRP_KEY, "bookmarks.create", "creating bookmarks page");
		home_wiki.Db_mgr().Save_mgr().Data_create(Xoa_ttl.parse_(home_wiki, ByteAry_.new_ascii_("Data:Bookmarks")), ByteAry_.new_ascii_(Bookmarks_text));
	}
	public static final String User_system_cfg_text = String_.Concat_lines_nl
		(	"app.scripts.txns.get('user.prefs.general').version_('" + Xoa_app_.Version + "').bgn();"
		,	"app.files.download.enabled_('n');"
		,	"app.files.math.enabled_('y');"
		,	"app.files.math.renderer_('mathjax');"
		,	"app.scripts.txns.get('user.prefs.general').end();\n"
		);
	public static final String Bookmarks_text = String_.Concat_lines_nl
		(	"Bookmarks are added automatically to the bottom of the page. All other text is not modified."
		,	""
		,	"Please delete bookmarks by editing this page."
		);
	private static Xow_ns_mgr ns_home_() {
		Xow_ns_mgr rv = new Xow_ns_mgr();
		rv = rv.Add_new(-2, "Media").Add_new(-1, "Special").Add_new(0, "").Add_new(1, "Talk").Add_new(2, "User").Add_new(3, "User talk").Add_new(4, "Wikipedia").Add_new(5, "Wikipedia talk")
			.Add_new(6, "File").Add_new(7, "File talk").Add_new(8, "MediaWiki").Add_new(9, "MediaWiki talk").Add_new(10, "Template").Add_new(11, "Template talk")
			.Add_new(12, "Help").Add_new(13, "Help talk").Add_new(14, "Category").Add_new(15, "Category talk").Add_new(100, "Portal").Add_new(101, "Portal talk")
			.Add_new(gplx.xowa.xtns.wdatas.Wdata_wiki_mgr.Ns_property, gplx.xowa.xtns.wdatas.Wdata_wiki_mgr.Ns_property_name)
			.Add_new(730, "Data").Add_new(731, "Data talk")
			.Add_new(Scrib_xtn_mgr.Ns_id_module, Scrib_xtn_mgr.Ns_name_module).Add_new(Scrib_xtn_mgr.Ns_id_module_talk, Scrib_xtn_mgr.Ns_name_module_talk)
			.Add_defaults()
			;
		rv.Init();
		return rv;
	}
	static final String GRP_KEY = "xowa.user_";
}
// mv -f $LUA_INSTALL_FILE $LUA_FILE
// chmod 774 $LUA_FILE
// app.user.cfg.setup.lua_installed_();