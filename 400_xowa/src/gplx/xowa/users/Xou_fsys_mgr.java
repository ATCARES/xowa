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
import gplx.xowa.wikis.*;
public class Xou_fsys_mgr implements GfoInvkAble {
	Io_url app_root_dir;  Io_url wiki_root_dir; Xoa_app app; Xou_user usr; Io_url cur_root;
	public Xou_fsys_mgr(Xoa_app app, Xou_user usr, Io_url user_dir) {
		this.app = app; this.usr = usr;
		this.cur_root = user_dir;
		app_root_dir = cur_root.GenSubDir("app");
		app_temp_dir = app_root_dir.GenSubDir("tmp");
		app_data_history_fil = app_root_dir.GenSubFil_nest("data", "history", "page_history.csv");
		wiki_root_dir = cur_root.GenSubDir("wiki");
		home_wiki_dir = wiki_root_dir.GenSubDir_nest(Xow_wiki_domain_.Key_home_str);
	}
	public Io_url Root_dir()				{return cur_root;}
	public Io_url Wiki_root_dir()			{return wiki_root_dir;}
	public Io_url Wiki_html_dir(String wiki){return wiki_root_dir.GenSubDir_nest(wiki, "html");}
	public Io_url App_data_history_fil()	{return app_data_history_fil;} Io_url app_data_history_fil;
	public Io_url App_temp_dir()			{return app_temp_dir;} Io_url app_temp_dir;
	public Io_url App_img_dir()				{return app_root_dir.GenSubDir_nest("img");}
	public Io_url App_data_dir()			{return app_root_dir.GenSubDir_nest("data");}
	public Io_url App_data_cfg_dir()		{return app_root_dir.GenSubDir_nest("data", "cfg");}
	public Io_url App_data_cfg_custom_fil()	{return app_root_dir.GenSubFil_nest("data", "cfg", Xoa_gfs_mgr.Cfg_user_custom_file);}
	public Io_url Home_wiki_dir()			{return home_wiki_dir;} Io_url home_wiki_dir;
	public static final String Name_user_system_cfg = "user_system_cfg.gfs";
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_root_dir))		return cur_root;
		else	return GfoInvkAble_.Rv_unhandled;
	}	private static final String Invk_root_dir = "root_dir";
}
