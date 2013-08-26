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
package gplx.xowa.bldrs.imports; import gplx.*; import gplx.xowa.*; import gplx.xowa.bldrs.*;
import gplx.xowa.xtns.wdatas.*; import gplx.xowa.dbs.*; import gplx.xowa.xtns.wdatas.imports.*;
public class Xob_init_sql extends Xob_init_base {
	public Xob_init_sql(Xob_bldr bldr, Xow_wiki wiki) {this.Ctor(bldr, wiki);}
	@Override public String Cmd_key() {return KEY;} public static final String KEY = "import.sql.init";
	@Override public void Cmd_ini_wdata(Xob_bldr bldr, Xow_wiki wiki) {
		bldr.Cmd_mgr().Add_cmd(wiki, Xob_wdata_qid_sql.KEY);
		bldr.Cmd_mgr().Add_cmd(wiki, Xob_wdata_pid_sql.KEY);
	}
	@Override public void Cmd_run_end(Xow_wiki wiki) {
		if (Xodb_mgr_sql.Find_core_url(wiki) != null) throw wiki.App().Bldr().Usr_dlg().Fail_many("", "", "directory must not contain any sqlite3 files: ~{0}", wiki.Fsys_mgr().Root_dir().Raw());
		String ns_map = wiki.App().Setup_mgr().Dump_mgr().Db_ns_map();
		Xodb_mgr_sql db_mgr = wiki.Db_mgr_create_as_sql();
		db_mgr.Init_make(ns_map);	// NOTE: must Init after Xob_siteinfo_parser b/c Xob_siteinfo_parser will create new ns itms
	}
}