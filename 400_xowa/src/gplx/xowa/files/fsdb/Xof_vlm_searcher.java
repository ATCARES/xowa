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
//namespace gplx.xowa.files.fsdb {
//	using gplx.dbs; using gplx.fsdb; using gplx.xowa.files;
//	public class Xof_vlm_searcher {
//		private byte[] tmp_bfr = new byte[4096]; private int flush = Io_mgr.Len_mb;
//		public void Search(Db_provider provider, OrderedHash hash, int len) {
//			for (int i = 0; i < len; i += Sqlite_engine_.Stmt_arg_max) {
//				int grp_end = Sqlite_engine_.Stmt_arg_max; if (i + grp_end > len) grp_end = len;
//				Search__grp(i, grp_end, provider, hash, len);
//			}
//		}
//		private void Search__grp(int grp_bgn, int grp_end, Db_provider provider, OrderedHash hash, int len) {
//			Search__find_itm(provider, hash, len, grp_bgn, grp_end);
//			Search__find_img(provider, hash, len, grp_bgn, grp_end);
//			Search__load_bin(provider, hash, len, grp_bgn, grp_end);
//		}
//		private void Search__find_itm(Db_provider provider, OrderedHash hash, int len, int grp_bgn, int grp_end) {
//			OrderedHash tmp_hash = OrderedHash_.new_bry_();
//			for (int i = grp_bgn; i < grp_end; i++) {
//				Xof_fsdb_itm itm = (Xof_fsdb_itm)hash.FetchAt(i);
//				tmp_hash.Add(itm.Ttl(), itm);
//			}
//			byte[] prv_name = ByteAry_.Empty; Int_2_ref tmp_size = new Int_2_ref(); 
//			DataRdr rdr = DataRdr_.Null;
//			try {
//				rdr = Search__find_itm_rdr(provider, tmp_hash, len);
//				while (rdr.MoveNextPeer()) {
//					byte[] cur_name = rdr.ReadBryByStr("fil_name");
//					if (ByteAry_.Eq(cur_name, prv_name)) continue;	// name already seen; ignore; EX: A.png in both dir_0 and dir_1; only look at dir_0
//					Xof_fsdb_itm cur_itm = (Xof_fsdb_itm)tmp_hash.Fetch(cur_name); if (cur_itm == null) continue;	// shouldn't happen, but just in case, ignore;
//					Xof_xfer_itm_.Calc_xfer_size(tmp_size, gplx.xowa.Xof_img_mgr.Thumb_w_img_const, rdr.ReadInt("thm_width"), rdr.ReadInt("thm_height"), cur_itm.Lnki_w(), cur_itm.Lnki_h(), cur_itm.Lnki_type() == Xop_lnki_type.Id_thumb, cur_itm.Lnki_upright());
//					cur_itm.Db_dir_id_(rdr.ReadInt("thm_owner")).Db_fil_id_(rdr.ReadInt("thm_owner"));
//					cur_itm.Reg_w_(tmp_size.Val_0()).Reg_h_(tmp_size.Val_1()).Db_status_(Xof_fsdb_itm.Db_status_fil_found);
//				}
//			} finally {rdr.Rls();}
//		}
//		private void Search__find_img(Db_provider provider, OrderedHash hash, int len, int grp_bgn, int grp_end) {
//			OrderedHash tmp_hash = OrderedHash_.new_();
//			for (int i = grp_bgn; i < grp_end; i++) {
//				Xof_fsdb_itm itm = (Xof_fsdb_itm)hash.FetchAt(i);
//				tmp_hash.Add(new Int_2_ref(itm.Db_fil_id(), itm.Reg_w()), itm);
//			}
//			DataRdr rdr = DataRdr_.Null;
//			try {
//				rdr = Search__find_img_rdr(provider, hash, len);
//				Int_2_ref tmp_id = new Int_2_ref();
//				while (rdr.MoveNextPeer()) {
//					int img_owner = rdr.ReadInt(Fsdb_xtn_thm_tbl.Fld_thm_owner);
//					int img_w = rdr.ReadInt(Fsdb_xtn_thm_tbl.Fld_thm_width);
//					Xof_fsdb_itm itm = (Xof_fsdb_itm)tmp_hash.Fetch(tmp_id.Val_all_(img_owner, img_w)); if (itm == null) continue; // shouldn't happen, but just in case, ignore;
//					itm.Db_xtn_id_(rdr.ReadInt(Fsdb_xtn_thm_tbl.Fld_thm_id));
//					itm.Db_status_(Xof_fsdb_itm.Db_status_img_found);
//				}
//			} finally {rdr.Rls();}
//		}
//		private void Search__load_bin(Db_provider provider, OrderedHash hash, int len, int grp_bgn, int grp_end) {
//			OrderedHash tmp_hash = OrderedHash_.new_(); 
//			for (int i = grp_bgn; i < grp_end; i++) {
//				Xof_fsdb_itm itm = (Xof_fsdb_itm)hash.FetchAt(i);
//				tmp_hash.Add(IntRef.new_(itm.Db_xtn_id()), itm);
//			}
//			IntRef tmp_id = IntRef.zero_();
//			DataRdr rdr = DataRdr_.Null;
//			try {
//				rdr = Search__load_bin_rdr(provider, tmp_hash, len);
//				while (rdr.MoveNextPeer()) {
//					int id = rdr.ReadInt(Fsdb_bin_tbl.Fld_bin_owner_id);
//					Xof_fsdb_itm itm = (Xof_fsdb_itm)tmp_hash.Fetch(tmp_id.Val_(id)); if (itm == null) continue; // shouldn't happen, but just in case, ignore;
//					Fsdb_bin_tbl.Select_to_fsys__stream(rdr, itm.Actl_url(), tmp_bfr, flush);
//					itm.Db_status_(Xof_fsdb_itm.Db_status_bin_found);
//				}
//			} finally {rdr.Rls();}
//		}
//		private DataRdr Search__find_itm_rdr(Db_provider p, OrderedHash ttl_hash, int len) {
//			Object[] names = new Object[len];
//			for (int i = 0; i < len; i++) {
//				Xof_fsdb_itm itm = (Xof_fsdb_itm)ttl_hash.FetchAt(i);
//				names[i] = String_.new_utf8_(itm.Ttl());
//			}
//			Db_qry qry = Db_qry_select.new_().Cols_all_()
//					.From_(Fsdb_xtn_thm_tbl.Tbl_name, "i")
//					.	Join_(Fsdb_fil_tbl.Tbl_name, "f", Sql_join_itm.new_(Fsdb_fil_tbl.Fld_fil_id, "i", Fsdb_xtn_thm_tbl.Fld_thm_owner))
//					.Where_(Db_crt_.in_(Fsdb_fil_tbl.Fld_fil_name, names))
//					;
//			return p.Exec_qry_as_rdr(qry);
//		}	
//		private DataRdr Search__find_img_rdr(Db_provider p, OrderedHash hash, int len) {
//			Object[][] ary = new Object[len][];
//			for (int i = 0; i < len; i++) {
//				Xof_fsdb_itm itm = (Xof_fsdb_itm)hash.FetchAt(i);
//				Object[] vals = new Object[2];
//				vals[0] = itm.Db_fil_id();
//				vals[1] = itm.Reg_w();
//				ary[i] = vals;
//			}
//			Db_obj_ary_crt crt = Db_obj_ary_crt.new_by_type(ClassAdp_.Tid_int, Fsdb_xtn_thm_tbl.Fld_thm_owner, Fsdb_xtn_thm_tbl.Fld_thm_width);
//			crt.Vals_(ary);
//			Db_qry qry = Db_qry_select.new_().Cols_all_()
//					.From_(Fsdb_xtn_thm_tbl.Tbl_name, "i")
//					.Where_(crt)
//					;
//			return p.Exec_qry_as_rdr(qry);
//		}	
//		private DataRdr Search__load_bin_rdr(Db_provider p, OrderedHash ttl_hash, int len) {
//			Object[] names = new Object[len];
//			for (int i = 0; i < len; i++) {
//				Xof_fsdb_itm itm = (Xof_fsdb_itm)ttl_hash.FetchAt(i);
//				names[i] = itm.Db_xtn_id();
//			}
//			Db_qry qry = Db_qry_select.new_().Cols_all_()
//					.From_(Fsdb_bin_tbl.Tbl_name)
//					.Where_(Db_crt_.in_(Fsdb_bin_tbl.Fld_bin_owner_id, names))
//					;
//			return p.Exec_qry_as_rdr(qry);
//		}
//	}
//}
