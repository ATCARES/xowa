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
package gplx.fsdb; import gplx.*;
import gplx.dbs.*;
public class Fsdb_mnt_mgr {
	private Db_provider provider;
	private Fsdb_db_abc_mgr[] ary; int ary_len = 0;
	public Fsdb_mnt_mgr() {}
	private int insert_db = 0;
	public void Init(Io_url cur_dir) {
		Fsdb_mnt_itm[] mnts = Db_load_or_make(cur_dir);
		ary_len = mnts.length;
		ary = new Fsdb_db_abc_mgr[ary_len];
		for (int i = 0; i < ary_len; i++) {
			Fsdb_mnt_itm itm = mnts[i];
			Io_url abc_url = cur_dir.GenSubFil_nest(itm.Url(), "fsdb.abc.sqlite3");
			ary[i] = new Fsdb_db_abc_mgr().Init(abc_url.OwnerDir());
		}
		insert_db = Fsdb_cfg_tbl.Select_as_int(provider, "core", "mnt.insert_idx");
	}
	private Fsdb_mnt_itm[] Db_load_or_make(Io_url cur_dir) {
		BoolRef created = BoolRef.false_();
		provider = Sqlite_engine_.Provider_load_or_make_(cur_dir.GenSubFil("file.core.sqlite3"), created);
		if (created.Val()) {
			Fsdb_mnt_tbl.Create_table(provider);
			Fsdb_mnt_tbl.Insert(provider, 0, "fsdb.main", "fsdb.main");
			Fsdb_mnt_tbl.Insert(provider, 1, "fsdb.upgrade_00", "fsdb.upgrade_00");
			Fsdb_mnt_tbl.Insert(provider, 2, "fsdb.user", "fsdb.user");
			Fsdb_cfg_tbl.Create_table(provider);
			Fsdb_cfg_tbl.Insert(provider, "core", "mnt.insert_idx", "2");
		}
		return Fsdb_mnt_tbl.Select_all(provider);
	}
	public Fsdb_db_bin_fil Bin_db_get(int mnt_id, int bin_db_id) {
		return ary[mnt_id].Bin_mgr().Get_at(bin_db_id);
	}
	public Fsdb_fil_itm Fil_select_bin(byte[] dir, byte[] fil, boolean is_thumb, int width, int thumbtime) {
		for (int i = 0; i < ary_len; i++) {
			Fsdb_fil_itm rv = ary[i].Fil_select_bin(dir, fil, is_thumb, width, thumbtime);
			if (rv != Fsdb_fil_itm.Null) {
				rv.Mnt_id_(i);
				return rv;
			}
		}
		return Fsdb_fil_itm.Null;
	}
	public Fsdb_xtn_thm_itm Thm_select_bin(byte[] dir, byte[] fil, int width, int thumbtime) {
		for (int i = 0; i < ary_len; i++) {
			Fsdb_xtn_thm_itm rv = ary[i].Thm_select_bin(dir, fil, width, thumbtime);
			if (rv != Fsdb_xtn_thm_itm.Null) {
				rv.Mnt_id_(i);
				return rv;
			}
		}
		return Fsdb_xtn_thm_itm.Null;
	}
	public void Fil_insert(Fsdb_fil_itm rv    , byte[] dir, byte[] fil, int ext_id, DateAdp modified, String hash, long bin_len, gplx.ios.Io_stream_rdr bin_rdr) {
		ary[insert_db].Fil_insert(rv, dir, fil, ext_id, modified, hash, bin_len, bin_rdr);
	}
	public void Thm_insert(Fsdb_xtn_thm_itm rv, byte[] dir, byte[] fil, int ext_id, int w, int h, int thumbtime, DateAdp modified, String hash, long bin_len, gplx.ios.Io_stream_rdr bin_rdr) {
		ary[insert_db].Thm_insert(rv, dir, fil, ext_id, w, h, thumbtime, modified, hash, bin_len, bin_rdr);
	}
	public void Img_insert(Fsdb_xtn_img_itm rv, byte[] dir, byte[] fil, int ext_id, DateAdp modified, String hash, long bin_len, gplx.ios.Io_stream_rdr bin_rdr, int img_w, int img_h) {
		ary[insert_db].Img_insert(rv, dir, fil, ext_id, modified, hash, bin_len, bin_rdr, img_w, img_h);
	}
	public void Bin_db_max_(long v) {
		for (int i = 0; i < ary_len; i++)
			ary[i].Bin_mgr().Db_bin_max_(v);
	}
	public void Commit() {
		for (int i = 0; i < ary_len; i++)
			ary[i].Commit();
	}
	public void Rls() {
		for (int i = 0; i < ary_len; i++)
			ary[i].Rls();
	}
}