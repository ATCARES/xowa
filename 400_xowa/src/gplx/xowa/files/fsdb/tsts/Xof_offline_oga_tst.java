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
package gplx.xowa.files.fsdb.tsts; import gplx.*; import gplx.xowa.*; import gplx.xowa.files.*; import gplx.xowa.files.fsdb.*;
import org.junit.*;
import gplx.fsdb.*;
public class Xof_offline_oga_tst {
	@Before public void init() {if (fxt.Db_skip()) return; fxt.Reset();} private Xof_file_fxt fxt = new Xof_file_fxt();
	@After public void term() {if (fxt.Db_skip()) return; fxt.Rls();}
	@Test   public void Orig_page() {	// .oga is on page [[File:A.oga]]; do not qry or get bin; (since file is not "viewable" immediately)
		if (fxt.Db_skip()) return;
		fxt.Init_qry_xowa(Xof_fsdb_arg_init_qry.new_().Init_commons_file("A.oga"));
		fxt.Init_bin_fsdb(Xof_fsdb_arg_init_bin.new_().Init_commons_file("A.oga"));
		fxt.Exec_get(Xof_fsdb_arg_exec_get.new_().Init_orig("A.oga").Rslt_reg_missing_reg().Rslt_qry_noop_());
		fxt.Test_fsys_exists_n("mem/root/common/orig/4/f/A.oga");
		fxt.Test_regy_pass("A.oga");
	}
	@Test   public void Orig_app() {	// .oga is clicked; get file
		if (fxt.Db_skip()) return;
		fxt.Init_qry_xowa(Xof_fsdb_arg_init_qry.new_().Init_commons_file("A.oga"));
		fxt.Init_bin_fsdb(Xof_fsdb_arg_init_bin.new_().Init_commons_file("A.oga"));
		fxt.Exec_get(Xof_fsdb_arg_exec_get.new_().Init_orig("A.oga").Exec_tid_(Xof_exec_tid.Tid_viewer_app).Rslt_reg_missing_reg().Rslt_qry_mock_().Rslt_bin_fsdb_());
		fxt.Test_fsys_exists_y("mem/root/common/orig/4/f/A.oga");
		fxt.Test_regy_pass("A.oga");
	}
}
