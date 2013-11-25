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
package gplx.xowa.files.fsdb.caches; import gplx.*; import gplx.xowa.*; import gplx.xowa.files.*; import gplx.xowa.files.fsdb.*;
import gplx.dbs.*;
class Cache_fil_tbl {
	private Db_provider provider;
	private Db_stmt select_stmt;
	private Db_stmt_bldr stmt_bldr = new Db_stmt_bldr(Tbl_name, String_.Ary(Fld_uid), Fld_dir_id, Fld_fil_name, Fld_fil_is_orig, Fld_fil_w, Fld_fil_h, Fld_fil_thumbtime, Fld_fil_ext, Fld_fil_size, Fld_cache_time);
	public void Db_init(Db_provider p) {this.provider = p;}
	public void Db_when_new() {
		Sqlite_engine_.Tbl_create(provider, Tbl_name, Tbl_sql);
		Sqlite_engine_.Idx_create(provider, Idx_ttl);
	}
	public void Db_save(Cache_fil_itm itm) {
		if (stmt_bldr == null) stmt_bldr.Init(provider);
		Db_stmt stmt = stmt_bldr.Get(itm.Cmd_mode());
		switch (itm.Cmd_mode()) {
			case Db_cmd_mode.Create:	stmt.Clear().Val_int_(itm.Uid()); Db_save_modify(stmt, itm); stmt.Exec_insert(); break;
			case Db_cmd_mode.Update:	stmt.Clear();					  Db_save_modify(stmt, itm); stmt.Exec_update(); break;
			case Db_cmd_mode.Delete:	stmt.Clear().Val_int_(itm.Uid()); stmt.Exec_delete();	break;
			case Db_cmd_mode.Ignore:	break;
			default:					throw Err_.unhandled(itm.Cmd_mode());
		}
		itm.Cmd_mode_(Db_cmd_mode.Ignore);
	}
	private void Db_save_modify(Db_stmt stmt, Cache_fil_itm itm) {
		stmt.Val_int_(itm.Dir_id())
			.Val_str_(itm.Fil_name())
			.Val_byte_by_bool_(itm.Fil_is_orig())
			.Val_int_(itm.Fil_w())
			.Val_int_(itm.Fil_h())
			.Val_int_(itm.Fil_thumbtime())
			.Val_int_(itm.Fil_ext())
			.Val_long_(itm.Fil_size())
			.Val_long_(itm.Cache_time())
			;
	}
	public void Db_term() {
		if (select_stmt != null) select_stmt.Rls();
		if (stmt_bldr != null) stmt_bldr.Rls();
	}
	public Cache_fil_itm Select(int dir_id, String fil_name, boolean fil_is_orig, int fil_w, int fil_h, int fil_thumbtime) {
		if (select_stmt == null) select_stmt = Db_stmt_.new_select_(provider, Tbl_name, String_.Ary(Fld_dir_id, Fld_fil_name, Fld_fil_is_orig, Fld_fil_w, Fld_fil_h, Fld_fil_thumbtime));
		DataRdr rdr = DataRdr_.Null;
		try {
			rdr = select_stmt.Clear()
			.Val_int_(dir_id)
			.Val_str_(fil_name)
			.Val_byte_by_bool_(fil_is_orig)
			.Val_int_(fil_w)
			.Val_int_(fil_h)
			.Val_int_(fil_thumbtime)
			.Exec_select();
			if (rdr.MoveNextPeer())
				return new Cache_fil_itm().Init_by_load(rdr);
			else
				return Cache_fil_itm.Null;
		}
		catch (Exception e) {select_stmt = null; throw Err_.err_(e, "stmt failed");}
		finally {rdr.Rls();}
	}
	private static final String Tbl_sql = String_.Concat_lines_nl
	( "CREATE TABLE cache_fil"
	, "( uid               integer       NOT NULL        PRIMARY KEY       AUTOINCREMENT"
	, ", dir_id            integer"
	, ", fil_name          varchar(255)"
	, ", fil_is_orig       tinyint"
	, ", fil_w             integer"
	, ", fil_h             integer"
	, ", fil_thumbtime     integer"
	, ", fil_ext           integer"
	, ", fil_size          bigint"
	, ", cache_time        bigint"
	, ");"
	);
	public static final String Tbl_name = "cache_fil"
	, Fld_uid = "uid", Fld_dir_id = "dir_id", Fld_fil_name = "fil_name", Fld_fil_is_orig = "fil_is_orig"
	, Fld_fil_w = "fil_w", Fld_fil_h = "fil_h", Fld_fil_thumbtime = "fil_thumbtime", Fld_fil_ext = "fil_ext", Fld_fil_size = "fil_size", Fld_cache_time = "cache_time"
	;
	private static final Db_idx_itm
		Idx_ttl     		= Db_idx_itm.sql_("CREATE INDEX IF NOT EXISTS cache_fil__fil ON fsdb (fil_name, fil_is_orig, fil_w, fil_h, fil_thumbtime, cache_time, uid);")
	;
}
