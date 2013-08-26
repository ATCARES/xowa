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
package gplx.xowa; import gplx.*;
public class Xof_xfer_queue {
	ListAdp list = ListAdp_.new_(); OrderedHash dirty = OrderedHash_.new_(); ByteAryRef dirty_key = ByteAryRef.null_();		
	public IntRef Elem_id() {return elem_id;} IntRef elem_id = IntRef.neg1_();
	public int Count() {return list.Count();}
	public void Clear() {
		dirty.Clear();
		list.Clear();
		elem_id.Val_neg1_();
	}
	public void Add(Xof_xfer_itm xfer_itm) {list.Add(xfer_itm);}
	public void Add_dirty_if_new(Xof_meta_mgr meta_mgr) {
		byte[] meta_mgr_key = meta_mgr.Wiki().Key_bry();
		if (!dirty.Has(dirty_key.Val_(meta_mgr_key)))
			dirty.AddReplace(meta_mgr_key, meta_mgr);	// only add if new
	}
	public void Exec(Xog_win_wtr wtr, Xow_wiki wiki) {
		Xof_meta_mgr meta_mgr = null;
		int xfer_len = list.Count();
		for (int i = 0; i < xfer_len; i++) {
			if (wiki.App().Gui_wtr().Canceled()) break;
			Xof_xfer_itm xfer_itm = (Xof_xfer_itm)list.FetchAt(i);
			meta_mgr = xfer_itm.Meta_itm().Owner_fil().Owner_mgr();
			Add_dirty_if_new(meta_mgr); // only add if new
			String queue_msg = wtr.Prog_many(GRP_KEY, "download.bgn", "downloading ~{0} of ~{1}: ~{2};", i + ListAdp_.Base1, xfer_len, xfer_itm.Ttl());
			wiki.App().File_mgr().Download_mgr().Download_wkr().Download_xrg().Prog_fmt_hdr_(queue_msg);
			wiki.File_mgr().Repo_mgr().Xfer_by_meta(xfer_itm, this);
			xfer_itm.Atrs_by_meta(xfer_itm.Meta_itm(), xfer_itm.Meta_itm().Repo_itm(wiki), wiki.Html_mgr().Img_thumb_width());
			xfer_itm.Atrs_calc_for_html();
			if (ByteAry_.Len_gt_0(xfer_itm.Html_view_src())								// only update images that have been found; otherwise "Undefined" shows up in image box
				&& xfer_itm.Html_dynamic_tid() != Xof_xfer_itm.Html_dynamic_tid_none) {	// skip updates when downloading orig on File page (there won't be any frame to update)
				String file_img_id = "xowa_file_img_" + xfer_itm.Html_dynamic_id();
				wtr.Html_img_update(file_img_id, String_.new_utf8_(xfer_itm.Html_view_src()), xfer_itm.Html_w(), xfer_itm.Html_h());
				if (xfer_itm.Lnki_type() == Xop_lnki_type.Id_thumb) {
					wtr.Html_atr_set(file_img_id, "class", gplx.xowa.html.Xow_html_mgr.Str_img_class_thumbimage);
					wtr.Html_atr_set("xowa_file_div_" + xfer_itm.Html_dynamic_id(), "style", "width:" + xfer_itm.Html_w() + "px;");
				}
				if (xfer_itm.Html_dynamic_tid() == Xof_xfer_itm.Html_dynamic_tid_gallery) {
					int vpad = ((gplx.xowa.xtns.gallery.Xtn_gallery_dynamic_data)xfer_itm.Misc).Calc_vpad(xfer_itm.Html_h());
					wtr.Html_atr_set("xowa_file_gallery_div_" + xfer_itm.Html_dynamic_id(), "style", "margin:" + vpad + "px auto;");
				}
				else if (xfer_itm.Html_dynamic_tid() == Xof_xfer_itm.Html_dynamic_tid_vid) {
					wtr.Html_atr_set("xowa_file_play_" + xfer_itm.Html_dynamic_id(), "style", "width:" + xfer_itm.Html_w() + "px;max-width:" + (xfer_itm.Html_w() - 2) + "px;");
					wtr.Html_atr_set("xowa_file_play_" + xfer_itm.Html_dynamic_id(), "href", String_.new_utf8_(xfer_itm.Html_orig_src()));
				}
			}
		}
		for (int i = 0; i < dirty.Count(); i++) {
			meta_mgr = (Xof_meta_mgr)dirty.FetchAt(i);
			meta_mgr.Save(true);
		}
		this.Clear();
	}
	static final String GRP_KEY = "xowa.xfer.queue";
}