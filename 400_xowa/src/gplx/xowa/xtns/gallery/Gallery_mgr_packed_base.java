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
package gplx.xowa.xtns.gallery; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.xowa.files.*; import gplx.xowa.html.modules.*;
class Gallery_mgr_packed_base extends Gallery_mgr_base {
	@Override public byte Tid() {return Gallery_mgr_base_.Packed_tid;}
	@Override public byte[] Tid_bry() {return Gallery_mgr_base_.Packed_bry;}
	@Override public void Init(int itms_per_row, int itm_default_w, int itm_default_h) {
		this.itms_per_row = 0;	// Does not support per row option.
		this.itm_default_w = itm_default_w;
		this.itm_default_h = itm_default_h;
	}
	@Override public void Itms_per_row_(int v) {}
	@Override public void Get_modules(Xoh_module_list modules) {modules.Add(Module_packed);}
	@Override public int Get_thumb_padding()				{return 0;}
	@Override public int Get_gb_padding()				{return 2;}
	@Override public int Get_vpad(int itm_h, int thm_h)	{
		return (int)((this.Get_thumb_padding() + itm_h - thm_h / Scale_factor) / 2);
	}
	@Override public int Get_thumb_div_width(int thm_w) {
		if (thm_w < Scale_factor_x_60)
			thm_w = Scale_factor_x_60;	// Require at least 60px wide, so caption is wide enough to work.
		return (int)(thm_w / Scale_factor) + this.Get_thumb_padding();
	}
	@Override public int Get_gb_width(int thm_w, int thm_h) {
		int val = thm_w == -1 ? (int)(itm_default_w * Scale_factor) : thm_w;
		return Get_thumb_div_width(val) + this.Get_gb_padding();
	}
	@Override public void Get_thumb_size(Xop_lnki_tkn lnki, Xof_ext ext) {
		Get_thumb_size_static(lnki, ext, itm_default_w, itm_default_h);
	}
	@Override public void Adjust_image_parameters(Xof_xfer_itm xfer_itm) {
		int w = (int)(xfer_itm.Html_w() / Scale_factor);
		int h = (int)(xfer_itm.Html_h() / Scale_factor);
		xfer_itm.Init_xfer_html_size(w, h);
	}
	public static final double Scale_factor = 1.5d;	// We artificially have 1.5 the resolution neccessary so that we can scale it up by that much on the client side, without worrying about requesting a new image.
	private static final int Scale_factor_x_60 = (int)(Scale_factor * 60);
	public static void Get_thumb_size_static(Xop_lnki_tkn lnki, Xof_ext ext, int itm_default_w, int itm_default_h) {
		int w;
		if (ext.Id_is_audio())
			w = itm_default_w;
		else
			w = (itm_default_h) * 10 + 100;	// We want the width not to be the constraining factor, so use random big number.
		lnki.Lnki_w_((int)(Scale_factor * w));
		lnki.Lnki_h_((int)(Scale_factor * itm_default_h));
	}
	public static final Xoh_module_itm Module_packed = new Xoh_module_itm("mediawiki.page.gallery").Scripts_init("\n  xowa_gallery_packed = true;");
}
class Gallery_mgr_packed_overlay extends Gallery_mgr_packed_base {
	@Override public byte[] Tid_bry() {return Gallery_mgr_base_.Packed_overlay_bry;}
	@Override public void Wrap_gallery_text(ByteAryBfr bfr, byte[] gallery_text, int thm_w, int thm_h) {
		if (gallery_text.length == 0) return; // If we have no text, do not output anything to avoid ugly white overlay.
		int img_w = this.Get_gb_width(thm_w, thm_h) - this.Get_thumb_padding() - this.Get_gb_padding();
		int caption_w = (img_w - 20);
		bfr	.Add(Wrap_gallery_text_0).Add_int_variable(caption_w)
			.Add(Wrap_gallery_text_1).Add(gallery_text)
			.Add(Wrap_gallery_text_2)
			;
	}
	private static final byte[] 
	  Wrap_gallery_text_0 = ByteAry_.new_ascii_("\n      <div class=\"gallerytextwrapper\" style=\"width: ")
	, Wrap_gallery_text_1 = ByteAry_.new_ascii_("px\"><div class=\"gallerytext\">\n") // NOTE: The newline after <div class="gallerytext"> is needed to accommodate htmltidy
	, Wrap_gallery_text_2 = ByteAry_.new_ascii_("\n      </div></div>")	// NOTE: 2nd </div> is not part of MW, but needed to close div
	;
}
class Gallery_mgr_packed_hover extends Gallery_mgr_packed_overlay {		@Override public byte[] Tid_bry() {return Gallery_mgr_base_.Packed_hover_bry;}
}
