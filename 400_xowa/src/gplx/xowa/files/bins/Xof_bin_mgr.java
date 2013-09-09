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
package gplx.xowa.files.bins; import gplx.*; import gplx.xowa.*; import gplx.xowa.files.*;
import gplx.xowa.files.fsdb.*; import gplx.xowa.files.cnvs.*;
public class Xof_bin_mgr {
	private Xof_bin_wkr[] wkrs = null; private int wkrs_len;
	private Xof_repo_mgr repo_mgr; private Xof_url_bldr url_bldr = new Xof_url_bldr();
	private StringRef resize_warning = StringRef.null_(); private Int_2_ref tmp_size = new Int_2_ref();
	public Xof_bin_mgr(Xof_repo_mgr repo_mgr) {this.repo_mgr = repo_mgr;}
	public void Resizer_(Xof_img_wkr_resize_img v) {resizer = v;} private Xof_img_wkr_resize_img resizer;
	public void Wkrs_(Xof_bin_wkr... wkrs) {this.wkrs = wkrs; wkrs_len = wkrs.length;}
	public boolean Find(Xof_fsdb_itm itm) {
		boolean rv = false;
		boolean itm_is_thumb = itm.Lnki_type_is_thumb();
		if (itm_is_thumb) {
			Io_url trg = Get_url(itm, Xof_repo_itm.Mode_thumb);
			itm.Html_url_abs_(trg);
			for (int i = 0; i < wkrs_len; i++) {
				Xof_bin_wkr wkr = wkrs[i];
				rv = wkr.Get_bin(itm, trg, Bool_.Y, itm.Html_w());		// get thumb's bin
				if (rv) {												// thumb's bin exists;
					itm.Rslt_bin_(wkr.Tid());
					return true;
				}
				Io_url src = Get_url(itm, Xof_repo_itm.Mode_orig);		// make src
				rv = wkr.Get_bin(itm, src, Bool_.N, itm.Orig_w());		// thumb missing; get orig;
				if (!rv) continue;										// nothing found; continue;
				rv = Resize(itm, itm_is_thumb, src, trg);
				if (!rv) continue;
				itm.Rslt_bin_(wkr.Tid());
				return true;
			}
		}
		else {	// orig
			Io_url trg = null;
			if (itm.Lnki_ext().Orig_is_not_image())
				trg = Get_url(itm, Xof_repo_itm.Mode_thumb);
			else
				trg = Get_url(itm, Xof_repo_itm.Mode_orig);
			itm.Html_url_abs_(trg);
			for (int i = 0; i < wkrs_len; i++) {
				Xof_bin_wkr wkr = wkrs[i];
				rv = wkr.Get_bin(itm, trg, Bool_.N, itm.Html_w());
				if (!rv) continue;	// orig not found; continue;
				if (itm.Lnki_ext().Id_is_svg()) {
					itm.Lnki_w_(itm.Html_w());
					Io_url trg_png = Get_url(itm, Xof_repo_itm.Mode_thumb);
					rv = Resize(itm, itm_is_thumb, trg, trg_png);
					if (!rv) continue;
				}
				itm.Rslt_bin_(wkr.Tid());
				return true;
			}
		}
		return false;
	}
	private boolean Resize(Xof_fsdb_itm itm, boolean itm_is_thumb, Io_url src, Io_url trg) {
		boolean limit_size = !itm.Lnki_ext().Id_is_svg();// || (itm.Ext().Id_is_svg() && caller_is_file_page);
		Xof_xfer_itm_.Calc_xfer_size(tmp_size, gplx.xowa.Xof_img_mgr.Thumb_w_img_const, itm.Orig_w(), itm.Orig_h(), itm.Lnki_w(), itm.Lnki_h(), itm_is_thumb, itm.Lnki_upright(), limit_size);
		boolean rv = resizer.Exec(src, trg, tmp_size.Val_0(), tmp_size.Val_1(), itm.Lnki_ext().Id(), resize_warning);
		itm.Rslt_cnv_(rv ? Xof_cnv_wkr_.Tid_y : Xof_cnv_wkr_.Tid_n);
		return rv;
	}
	private Io_url Get_url(Xof_fsdb_itm itm, byte mode) {
		Xof_repo_itm repo = repo_mgr.Get_by(itm.Orig_wiki());
		return url_bldr.Set_trg_file_(mode, repo, itm.Lnki_ttl(), itm.Lnki_md5(), itm.Lnki_ext(), itm.Html_w(), itm.Lnki_thumbtime()).Xto_url();
	}
}
