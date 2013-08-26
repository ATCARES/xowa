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
package gplx.xowa.dbs.tbls; import gplx.*; import gplx.xowa.*; import gplx.xowa.dbs.*;
import gplx.dbs.*;
public class Xodb_tbl_xowa_cfg {
	public Xodb_tbl_xowa_cfg Provider_(Db_provider provider) {this.provider = provider; return this;} Db_provider provider;
	DataRdr Select(String grp) {
		Db_qry qry = Db_qry_.select_cols_(Tbl_name, Db_crt_.eq_(Fld_cfg_grp, grp), Fld_cfg_key, Fld_cfg_val);
		return provider.Exec_qry_as_rdr(qry);
	}
	public int Select_val_as_int(String grp, String key) {return Int_.parse_(Select_val(grp, key));}
	public String Select_val(String grp, String key) {
		Db_qry_select qry = Db_qry_.select_val_(Tbl_name, Fld_cfg_val, Where_grp_key(grp, key));
		return (String)qry.ExecRdr_val(provider);
	}
	public KeyVal[] Select_kvs(String grp, String match_key, StringRef match_val) {
		DataRdr rdr = DataRdr_.Null;
		try {
			rdr = this.Select(grp);
			while (rdr.MoveNextPeer()) {
				String key = rdr.ReadStr(Fld_cfg_key);
				String val = rdr.ReadStr(Fld_cfg_val);
				if (String_.Eq(key, match_key)) match_val.Val_(val);
				KeyVal kv = KeyVal_.new_(key, val);
				tmp_list.Add(kv);
			}
			return (KeyVal[])tmp_list.XtoAry(KeyVal.class);
		}
		finally {rdr.Rls(); tmp_list.Clear();}		
	}	ListAdp tmp_list = ListAdp_.new_();
	public void Delete(String grp, String key) {
		Db_stmt stmt = Db_stmt_.Null;
		try {
			stmt = Db_stmt_.new_delete_(provider, Tbl_name, String_.Ary(Fld_cfg_grp, Fld_cfg_key));
			stmt.Val_str_(grp).Val_str_(key).Exec_delete();
		}	finally {stmt.Rls();}		
	}
	public void Insert_byte(String grp, String key, byte val)			{Insert_str(grp, key, Byte_.XtoStr(val));}
	public void Insert_str_by_bry(String grp, String key, byte[] val)	{Insert_str(grp, key, String_.new_utf8_(val));}
	public void Insert_str(String grp, String key, String val)			{Insert_str(provider, grp, key, val);}
	public static void Insert_str(Db_provider p, String grp, String key, String val) {
		Db_qry qry = Db_qry_.insert_(Tbl_name)
			.Arg_(Fld_cfg_grp     , grp)
			.Arg_(Fld_cfg_key     , key)
			.Arg_(Fld_cfg_val     , val)
		; 
		p.Exec_qry(qry);
	}
	public void Update(String grp, String key, int val) {Update(grp, key, Int_.XtoStr(val));}
	public void Update(String grp, String key, String val) {
		Db_qry qry = Db_qry_.update_common_(Tbl_name, Where_grp_key(grp, key), KeyVal_.new_(Fld_cfg_val, val));
		provider.Exec_qry(qry);
	}
	gplx.criterias.Criteria Where_grp_key(String grp, String key) {return Db_crt_.eqMany_(KeyVal_.new_(Fld_cfg_grp, grp), KeyVal_.new_(Fld_cfg_key, key));}
	public static final String Tbl_name = "xowa_cfg", Fld_cfg_grp = "cfg_grp", Fld_cfg_key = "cfg_key", Fld_cfg_val = "cfg_val";
}
