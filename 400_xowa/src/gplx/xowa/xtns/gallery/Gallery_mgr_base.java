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
import gplx.html.*; import gplx.xowa.html.*; import gplx.xowa.html.modules.*;
import gplx.xowa.files.*;
public abstract class Gallery_mgr_base {
	public abstract byte Tid();
	public abstract byte[] Tid_bry();
	@gplx.Virtual public boolean Tid_is_packed() {return false;}
	public int Itm_default_w() {return itm_default_w;} protected int itm_default_w;
	public int Itm_default_h() {return itm_default_h;} protected int itm_default_h;
	public int Itms_per_row() {return itms_per_row;} @gplx.Virtual public void Itms_per_row_(int v) {this.itms_per_row = v;} protected int itms_per_row;		
	@gplx.Virtual public int Get_thumb_padding() {return 30;}	// REF.MW: getThumbPadding; How much padding such the thumb have between image and inner div that that contains the border. This is both for verical and horizontal padding. (However, it is cut in half in the vertical direction).
	@gplx.Virtual public int Get_gb_padding() {return 5;}	// REF.MW: getGBPadding; GB stands for gallerybox (as in the <li class="gallerybox"> element)
	@gplx.Virtual public int Get_gb_borders() {return 8;}	// REF.MW: getGBBorders; Get how much extra space the borders around the image takes up. For this mode, it is 2px borders on each side + 2px implied padding on each side from the stylesheet, giving us 2*2+2*2 = 8.
	@gplx.Virtual public int Get_all_padding() {return this.Get_thumb_padding() + this.Get_gb_padding() + this.Get_gb_borders();} // REF.MW: getAllPadding; How many pixels of whitespace surround the thumbnail.
	@gplx.Virtual public int Get_vpad(int itm_h, int thm_h) {	// REF.MW: getVPad; Get vertical padding for a thumbnail; Generally this is the total height minus how high the thumb is.
		return (this.Get_thumb_padding() + itm_h - thm_h) / 2;
	}
	@gplx.Virtual public int Get_thumb_div_width(int thm_w) {	// REF.MW: getThumbDivWidth; Get the width of the inner div that contains the thumbnail in question. This is the div with the class of "thumb".
		return itm_default_w + this.Get_thumb_padding();
	}
	@gplx.Virtual public int Get_gb_width(int thm_w, int thm_h) {// REF.MW: getGBWidth; Width of gallerybox <li>. Generally is the width of the image, plus padding on image plus padding on gallerybox.s
		return itm_default_w + this.Get_thumb_padding() + this.Get_gb_padding();
	}
	@gplx.Virtual public void Get_modules(Xoh_module_list modules) {} // REF.MW: getModules; Get a list of modules to include in the page.
	@gplx.Virtual public void Init(int itms_per_row, int itm_default_w, int itm_default_h) {
		this.itms_per_row = itms_per_row;
		this.itm_default_w = itm_default_w;
		this.itm_default_h = itm_default_h;
	}
	@gplx.Virtual public void Wrap_gallery_text(ByteAryBfr bfr, byte[] gallery_text, int thm_w, int thm_h) {
		bfr	.Add(Wrap_gallery_text_bgn)
			.Add(gallery_text)
			.Add(Wrap_gallery_text_end)
			;
	}
	public void Write_html(ByteAryBfr bfr, Xow_wiki wiki, Xoa_page page, Xop_ctx ctx, byte[] src, Gallery_xnde xnde) {
		ByteAryBfr tmp_bfr = wiki.Utl_bry_bfr_mkr().Get_b512();			
		byte[] box_style = xnde.Atr_style();
		if (itms_per_row > 0) {
			int max_width = itms_per_row * (itm_default_w + this.Get_all_padding());
			box_style = Fmt_and_add(tmp_bfr, box_style_max_width_fmtr, box_style, max_width);
		}
		byte[] box_cls = Fmt_and_add(tmp_bfr, box_cls_fmtr, xnde.Atr_cls(), this.Tid_bry());
		this.Get_modules(page.Html_data().Modules());
		byte[] gallery_ul_id = tmp_bfr.Add(box_id_prefix_bry).Add_int_variable(page.File_queue().Elem_id().Val_add()).XtoAryAndClear();
		Box_hdr_write(bfr, wiki.App().Html_mgr().Whitelist_mgr(), src, gallery_ul_id, box_cls, box_style, xnde.Atrs_other());
		byte[] box_caption = xnde.Atr_caption();
		if (box_caption != Gallery_xnde.Null_bry)
			box_caption_fmtr.Bld_bfr_many(bfr, box_caption);

		Xoa_app app = wiki.App(); Xoh_html_wtr html_wtr = wiki.Html_wtr();
		int itm_len = xnde.Itms_len();
		for (int i = 0; i < itm_len; i++) {
			Write_html_itm(bfr, tmp_bfr, app, wiki, page, ctx, html_wtr, src, xnde, gallery_ul_id, i, null) ;
		}
		bfr.Add(box_html_end_bry);
		tmp_bfr.Mkr_rls();
	}	private static final byte[] box_id_prefix_bry = ByteAry_.new_ascii_("xowa_gallery_ul_"), itm_id_prefix_bry = ByteAry_.new_ascii_("xowa_gallery_li_");
	public static byte File_found_mode = Bool_.__byte;
	public void Write_html_itm(ByteAryBfr bfr, ByteAryBfr tmp_bfr, Xoa_app app, Xow_wiki wiki, Xoa_page page, Xop_ctx ctx, Xoh_html_wtr html_wtr, byte[] src, Gallery_xnde xnde, byte[] gallery_ul_id, int i, Xof_xfer_itm xfer_itm) {
		Gallery_itm itm = (Gallery_itm)xnde.Itms_get_at(i);
		Xoa_ttl ttl = itm.Ttl();
		byte[] itm_caption = itm.Caption_bry(); if (itm_caption == null) itm_caption = ByteAry_.Empty;

		Xop_lnki_tkn lnki = itm.Lnki_tkn();
		int lnki_w_orig = lnki.Lnki_w(), lnki_h_orig = lnki.Lnki_h(); // store orig lnki_w / lnki_w
		this.Get_thumb_size(lnki, itm.Ext());	// packed=expand by 1.5;
		if (xfer_itm == null) {	// will be null on 1st pass
			xfer_itm = html_wtr.Lnki_wtr().File_wtr().Lnki_eval(ctx, page, lnki, html_wtr.Queue_add_ref())
			.Gallery_mgr_h_(xnde.Itm_h_or_default())
			.Html_elem_tid_(Xof_html_elem.Tid_gallery_v2)
			;
		}
		byte[] gallery_li_id = tmp_bfr.Add(itm_id_prefix_bry).Add_int_variable(xfer_itm.Html_uid()).XtoAryAndClear();
		byte[] itm_html = ByteAry_.Empty;
		int html_w_expand = xfer_itm.Html_w();
		int html_h_expand = xfer_itm.Html_h();
		boolean file_found = xfer_itm.File_found();
		if (File_found_mode != Bool_.__byte)
			file_found = File_found_mode == Bool_.Y_byte;
		if (	!ttl.Ns().Id_file()
			||	!file_found
			) {	// itm is not a file, or is not found; write text
			itm_html = itm_missing_fmtr.Bld_bry_many(tmp_bfr, this.Get_thumb_padding() + itm_default_h, ttl.Page_txt());
			itm.Html_prepare(wiki, ctx, src, xnde, xfer_itm, gallery_li_id, i);
			xfer_itm.Html_img_wkr_(itm);
			lnki.Lnki_w_(lnki_w_orig).Lnki_h_(lnki_h_orig);
			html_w_expand = lnki_w_orig; html_h_expand = lnki_h_orig;	// reset lnki_w_orig / lnki_h_orig else large captions
		}
		else {
			byte[] alt = itm.Alt_bgn() == ByteAry_.NotFound && ByteAry_.Len_eq_0(itm_caption)	//	if ( $alt == '' && $text == '' )  $imageParameters['alt'] = $nt->getText();
				? itm.Ttl().Page_txt()
				: Xoh_html_wtr_escaper.Escape(app, tmp_bfr, ByteAry_.Mid(src, itm.Alt_bgn(), itm.Alt_end()))
				;
			Xoa_ttl href_ttl = itm.Link_bgn() == ByteAry_.NotFound
				? ttl
				: Xoa_ttl.parse_(wiki, ByteAry_.Mid(src, itm.Link_bgn(), itm.Link_end()))
				;
			if (href_ttl == null) href_ttl = ttl;	// occurs when link is invalid; EX: A.png|link=<invalid>
			this.Adjust_image_parameters(xfer_itm);	// trad=noop; packed=reduce by 1.5
			int html_w_normal = xfer_itm.Html_w();
			int html_h_normal = xfer_itm.Html_h();
			xfer_itm.Init_xfer_for_gallery(html_w_normal, html_h_normal, html_w_expand);// NOTE: file_w should be set to expanded width so js can resize if gallery
			itm_div0_fmtr.Bld_bfr_many(tmp_bfr, this.Get_thumb_div_width(html_w_expand));
			itm_div1_fmtr.Bld_bfr_many(tmp_bfr, this.Get_vpad(itm_default_h, html_h_expand));	// <div style="margin:~{vpad}px auto;">
			wiki.Html_wtr().Lnki_wtr().Write_file(tmp_bfr, ctx, Xoh_html_wtr_ctx.Basic, src, lnki, xfer_itm, alt);
			tmp_bfr.Add(itm_divs_end_bry);
			itm_html = tmp_bfr.XtoAryAndClear();
		}

		byte[] show_filenames_link = ByteAry_.Empty;
		if (xnde.Show_filename()) {
			wiki.Html_wtr().Lnki_wtr().Write_plain_by_bry
			( tmp_bfr, src, lnki
			, ByteAry_.Limit(ttl.Page_txt(), 25)	// 25 is defined by captionLength in DefaultSettings.php					
			);										// MW:passes know,noclasses which isn't relevant to XO
		}
		int itm_div_width = this.Get_gb_width(html_w_expand, html_h_expand);
		itm_li_bgn_fmtr.Bld_bfr_many(bfr, gallery_li_id, itm_div_width);
		bfr.Add(itm_html);
		Xop_parser_.Parse_to_html(tmp_bfr, wiki, page, true, itm_caption);
		itm_caption = tmp_bfr.XtoAryAndClear();
		itm_caption = tmp_bfr.Add(show_filenames_link).Add(itm_caption).XtoAryAndClear();
		Wrap_gallery_text(bfr, itm_caption, html_w_expand, html_h_expand);
		bfr.Add(itm_li_end_bry);
	}
	private static final byte[] 
	  Wrap_gallery_text_bgn = ByteAry_.new_ascii_("\n      <div class=\"gallerytext\">") // NOTE: The newline after <div class="gallerytext"> is needed to accommodate htmltidy
	, Wrap_gallery_text_end = ByteAry_.new_ascii_("\n      </div>")
	;
	@gplx.Virtual public void Get_thumb_size(Xop_lnki_tkn lnki, Xof_ext ext) { // REF.MW: getThumbParams; Get the transform parameters for a thumbnail.
		lnki.Lnki_w_(itm_default_w);
		lnki.Lnki_h_(itm_default_h);
	}
	@gplx.Virtual public void Adjust_image_parameters(Xof_xfer_itm xfer_itm) {	// REF.MW: Adjust the image parameters for a thumbnail. Used by a subclass to insert extra high resolution images.
	}
	private static final ByteAryFmtr
	  box_style_max_width_fmtr		= ByteAryFmtr.new_(	"max-width:~{max_width}px;_width:~{max_width}px;", "max_width")				// id=xowa_gallery_ul_1
	, box_cls_fmtr					= ByteAryFmtr.new_(	"gallery mw-gallery-~{mode}", "mode")
	, box_caption_fmtr				= ByteAryFmtr.new_(	"\n  <li class='gallerycaption'>~{caption}</li>", "caption")
	, itm_li_bgn_fmtr				= ByteAryFmtr.new_(	"\n  <li id=\"~{id}\" class=\"gallerybox\" style=\"width: ~{width}px\">"	// id=xowa_gallery_li_1
													  + "\n    <div style=\"width: ~{width}px\">", "id", "width")
	, itm_div0_fmtr					= ByteAryFmtr.new_(	"\n      <div class=\"thumb\" style=\"width: ~{width}px;\">", "width")
	, itm_missing_fmtr				= ByteAryFmtr.new_(	"\n      <div class=\"thumb\" style=\"height: ~{height}px;\">~{ttl_text}</div>", "height", "ttl_text")
	, itm_div1_fmtr					= ByteAryFmtr.new_(	"\n        <div style=\"margin:~{vpad}px auto;\">\n          ", "vpad")
	;

	private static final byte[] 
	  itm_li_end_bry	= ByteAry_.new_ascii_		  ( "\n    </div>"
													  + "\n  </li>")
	, box_html_end_bry	= ByteAry_.new_ascii_		  ( "\n</ul>")
	, itm_divs_end_bry	= ByteAry_.new_ascii_		  ( "\n        </div>\n      </div>")
	;
	private static byte[] Fmt_and_add(ByteAryBfr tmp_bfr, ByteAryFmtr fmtr, byte[] trailer, Object... fmtr_args) {
		fmtr.Bld_bfr_many(tmp_bfr, fmtr_args);
		if (ByteAry_.Len_gt_0(trailer)) {
			tmp_bfr.Add_byte_space();
			tmp_bfr.Add(trailer);
		}
		return tmp_bfr.XtoAryAndClear();
	}
	private static void Box_hdr_write(ByteAryBfr bfr, Xop_xatr_whitelist_mgr whitelist_mgr, byte[] src, byte[] gallery_ul_uid, byte[] cls, byte[] style, ListAdp xatr_list) {
		bfr.Add_byte(Byte_ascii.Lt).Add(Html_consts.Ul_tag_bry);
		Html_wtr.Write_atr(bfr, Html_atrs.Id_bry, gallery_ul_uid);
		Html_wtr.Write_atr(bfr, Html_atrs.Cls_bry, cls);
		Html_wtr.Write_atr(bfr, Html_atrs.Style_bry, style);
		if (xatr_list != null) {
			int len = xatr_list.Count();
			for (int i = 0; i < len; i++) {
				Xop_xatr_itm xatr = (Xop_xatr_itm)xatr_list.FetchAt(i);
				if (!whitelist_mgr.Chk(Xop_xnde_tag_.Tid_ul, src, xatr)) continue;
				byte[] key = xatr.Key_bry();
				byte[] val = xatr.Val_as_bry(src);
				Html_wtr.Write_atr(bfr, key, val);
			}
		}
		bfr.Add_byte(Byte_ascii.Gt);
	}
}
class Gallery_mgr_traditional extends Gallery_mgr_base {
	@Override public byte Tid() {return Gallery_mgr_base_.Traditional_tid;}		
	@Override public byte[] Tid_bry() {return Gallery_mgr_base_.Traditional_bry;}
}
class Gallery_mgr_nolines extends Gallery_mgr_base {
	@Override public byte Tid() {return Gallery_mgr_base_.Nolines_tid;}
	@Override public byte[] Tid_bry() {return Gallery_mgr_base_.Nolines_bry;}
	@Override public int Get_thumb_padding()				{return 0;}
	@Override public int Get_gb_padding()				{return 4;} // This accounts for extra space between <li> elements.
	@Override public int Get_vpad(int itm_h, int thm_h)	{return 0;}
}
