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
import gplx.xowa.html.*;
public class Xoh_lnki_wtr {
	public Xoh_lnki_wtr(Xow_wiki wiki, Xoh_html_wtr html_wtr) {
		this.wiki = wiki; this.html_wtr = html_wtr; bfr_mkr = wiki.Utl_bry_bfr_mkr();
		this.cfg = wiki.Html_mgr();
	}	Xow_html_mgr cfg; boolean lnki_title_enabled;
	Xow_wiki wiki; Xoh_html_wtr html_wtr; ByteAryFmtrArg_html_fmtr media_alt_fmtr = new ByteAryFmtrArg_html_fmtr(), caption_fmtr = new ByteAryFmtrArg_html_fmtr(); Bry_bfr_mkr bfr_mkr; Xoa_url tmp_url = new Xoa_url();
	Xoa_page page;
	public void Write_or_queue(Xoa_page page, Xoh_opts opts, ByteAryBfr bfr, byte[] src, Xop_lnki_tkn lnki, int depth) {
		this.page = page;
		Xof_xfer_itm xfer_itm = this.Lnki_eval(page, lnki, queue_add_ref);
		this.Write_media(bfr, src, opts, lnki, depth, xfer_itm);
	}	BoolRef queue_add_ref = BoolRef.false_();
	public void Page_bgn(Xoa_page page) {
		cfg_alt_defaults_to_caption = page.Wiki().App().User().Wiki().Html_mgr().Imgs_mgr().Alt_defaults_to_caption().Val();
	}	private boolean cfg_alt_defaults_to_caption = true;
	public Xof_xfer_itm Lnki_eval(Xoa_page page, Xop_lnki_tkn lnki, BoolRef queue_add_ref) {return Lnki_eval(page.File_queue(), lnki.Ttl().Page_url(), lnki.Lnki_type(), lnki.Width().Val(), lnki.Height().Val(), lnki.Upright(), lnki.Thumbtime(), lnki.NmsId() == Xow_ns_.Id_media, queue_add_ref);}
	public Xof_xfer_itm Lnki_eval(Xof_xfer_queue queue, byte[] lnki_ttl, byte lnki_type, int lnki_w, int lnki_h, double lnki_upright, int lnki_seek, boolean lnki_is_media_ns, BoolRef queue_add_ref) {
		queue_add_ref.Val_false();
		tmp_xfer_itm.Clear().Atrs_by_ttl(lnki_ttl, ByteAry_.Empty).Atrs_by_lnki(lnki_type, lnki_w, lnki_h, lnki_upright, lnki_seek);
		boolean found = wiki.File_mgr().Find_meta(tmp_xfer_itm);
		boolean file_queue_add = File_queue_add(wiki, tmp_xfer_itm, lnki_is_media_ns, found);
		Xof_xfer_itm rv = tmp_xfer_itm;
		if (file_queue_add) {
			queue_add_ref.Val_true();
			return Queue_add_manual(queue,  tmp_xfer_itm);
		}
		return rv;
	}	Xof_xfer_itm tmp_xfer_itm = new Xof_xfer_itm();
	public static Xof_xfer_itm Queue_add_manual(Xof_xfer_queue queue, Xof_xfer_itm xfer_itm) {
		int elem_id = queue.Elem_id().Val_add();
		Xof_xfer_itm rv = xfer_itm.Clone().Html_dynamic_atrs_(elem_id, Xof_xfer_itm.Html_dynamic_tid_img);
		queue.Add(rv);
		return rv;
	}
	private static boolean File_queue_add(Xow_wiki wiki, Xof_xfer_itm xfer_itm, boolean lnki_is_media_ns, boolean found) {
		if (!wiki.File_mgr().Cfg_download().Enabled()) return false;
//			if (xfer_itm.Meta_itm() == null) return false;		// occurs when repos are missing; // DELETE: caused Redownload_missing to fail; no reason why missing shouldn't return a default repo; DATE:2013-01-26
		if (lnki_is_media_ns) return false;
		switch (wiki.File_mgr().Cfg_download().Redownload()) {
			case Xof_cfg_download.Redownload_none:
				if (found) return false;
				if (!found && xfer_itm.Meta_itm().Orig_exists() == Xof_meta_itm.Exists_n) return false;	// not found, and orig_exists is n; do not download again (NOTE: even if current lnki is thumb, don't bother looking for thumb if orig is missing)
				break;
			case Xof_cfg_download.Redownload_missing:
				if (found) return false;
				break;
			case Xof_cfg_download.Redownload_all:
				break;
		}
		return true;
	}
	Xop_link_parser tmp_link_parser = new Xop_link_parser();
	Xohp_title_wkr anchor_title_wkr = new Xohp_title_wkr();
	void Write_media(ByteAryBfr bfr, byte[] src, Xoh_opts opts, Xop_lnki_tkn lnki, int depth, Xof_xfer_itm xfer_itm) {
		lnki_title_enabled = html_wtr.Hctx().Lnki_title();
		int elem_id = xfer_itm.Html_dynamic_id();
		int div_width = xfer_itm.Html_w();
		if (div_width < 1) div_width = wiki.Html_mgr().Img_thumb_width();
		int lnki_halign = lnki.HAlign();
		if (lnki_halign == Xop_lnki_halign.Null) lnki_halign = wiki.Lang().Img_thumb_halign_default();	// if halign is not supplied, then default to align for language
		byte[] lnki_halign_bry = Xop_lnki_halign.Html_names[lnki_halign];
		byte[] lnki_href = wiki.App().Href_parser().Build_to_bry(lnki.Ttl(), wiki);
		byte[] html_orig_src = xfer_itm.Html_orig_src();
		byte[] html_view_src = xfer_itm.Html_view_src();
		byte[] lnki_alt_text = Alt_text(src, lnki, depth);
		byte[] content = ByteAry_.Empty;
		byte[] lnki_ttl = lnki.Ttl().Page_txt();
		if (cfg.Img_suppress_missing_src()							// option to suppress src when file is missing
			&& !xfer_itm.Html_pass()								// file is missing
			&& !xfer_itm.Ext().Id_is_media()) {						// file is media; never suppress; src needs to be available for "click" on play; note that most media will be missing (not downloaded)
			html_orig_src = html_view_src = ByteAry_.Empty;			// null out src
		}

		if (lnki.NmsId() == Xow_ns_.Id_media) {	// REF.MW:Linker.php|makeMediaLinkObj; NOTE: regardless of ext (ogg vs jpeg) and literal status (Media vs :Media), [[Media]] links are always rendered the same way; see Beethoven; EX: [[:Media:De-Ludwig_van_Beethoven.ogg|listen]]); [[File:Beethoven 3.jpg|The [[Media:BeethovenWithLyreGuitar( W. J. Mahler - 1804).jpg|complete painting]]...]]
			cfg.Lnki_full_media().Bld_bfr_many(bfr, html_view_src, lnki.Ttl().Page_txt(), Caption(src, lnki, Xoh_opts.root_(), depth, html_orig_src));
			return;
		}
		if (xfer_itm.Ext().Id_is_media()) {
			if		(xfer_itm.Ext().Id() == Xof_ext_.Id_ogv || xfer_itm.Html_pass()			// NOTE: xfer_itm.Html_pass() checks for video .ogg files (ext = .ogg and thumb is available); EX: WWI;
					|| (xfer_itm.Ext().Id_is_ogg() && xfer_itm.Meta_itm().State_new())) {	// NOTE: State_new() will always assume that ogg is video; needed for 1st load and dynamic updates
				xfer_itm.Html_dynamic_tid_(Xof_xfer_itm.Html_dynamic_tid_vid);
				if (Xop_lnki_type.Id_is_thumb_like(lnki.Lnki_type())) {
					content = Video(src, opts, lnki, xfer_itm, depth, elem_id, true, lnki_href, html_view_src, html_orig_src, lnki_alt_text);
				}
				else {
					content = Video(src, opts, lnki, xfer_itm, depth, elem_id, false, lnki_href, html_view_src, html_orig_src, lnki_alt_text);
					cfg.Plain().Bld_bfr_many(bfr, content);
					return;
				}
			}
			else if	(xfer_itm.Ext().Id_is_audio()) {
				content = Audio(src, opts, lnki, depth, elem_id, lnki_href, html_orig_src, lnki_alt_text);
				if (lnki.Media_icon())
					cfg.Lnki_thumb_core().Bld_bfr_many(bfr, div_width, lnki_halign_bry, content, elem_id);
				else
					cfg.Plain().Bld_bfr_many(bfr, content);
				return;
			}
			cfg.Lnki_thumb_core().Bld_bfr_many(bfr, div_width, lnki_halign_bry, content, elem_id);
		}
		else {	// image
			if (lnki_halign == Xop_lnki_halign.Center) bfr.Add(Bry_div_bgn_center);
			byte lnki_img_type = lnki.Lnki_type();
			ByteAryBfr tmp_bfr = bfr_mkr.Get_k004();
			byte[] anchor_title = lnki_title_enabled ? Make_anchor_title(tmp_bfr, src, lnki, lnki_ttl, anchor_title_wkr) : ByteAry_.Empty;
			if (lnki_img_type == Xop_lnki_type.Id_thumb) {	// is "thumb"
				if (bfr.Bry_len() > 0) bfr.Add_byte_nl();
				content = Image_thumb(src, opts, lnki, xfer_itm, depth, elem_id, lnki_href, html_view_src, html_orig_src, lnki_alt_text, lnki_ttl, anchor_title);
				cfg.Lnki_thumb_core().Bld_bfr_many(bfr, div_width, lnki_halign_bry, content, elem_id);
			}
			else {
				if (	cfg_alt_defaults_to_caption 
					&& 	ByteAry_.Len_eq_0(lnki_alt_text)	// NOTE: if no alt, always use caption; DATE:2013-07-22
					&& 	!lnki.Alt_exists()) {				// unless blank alt exists; EX: [[File:A.png|a|alt=]] should have alt of "", not "a" 
					Caption(src, lnki, Xoh_opts.lnki_alt_(), depth, html_orig_src).XferAry(tmp_bfr, 0);
					lnki_alt_text = tmp_bfr.XtoAryAndClear();
				}
//					if (lnki_img_type == Xop_lnki_type.Id_none) bfr.Add(Bry_div_float_none).Add_byte_nl();
				switch (lnki.HAlign()) {
					case Xop_lnki_halign.Left:		bfr.Add(Bry_div_float_left).Add_byte_nl();	break;
					case Xop_lnki_halign.Right:		bfr.Add(Bry_div_float_right).Add_byte_nl();	break;
					case Xop_lnki_halign.None:		bfr.Add(Bry_div_float_none).Add_byte_nl();	break;
				}
				if (lnki.Link_tkn() == Arg_nde_tkn.Null)						
					cfg.Lnki_full_image().Bld_bfr_many(bfr, elem_id, lnki_href, html_view_src, xfer_itm.Html_w(), xfer_itm.Html_h(), lnki_alt_text, lnki_ttl, Xow_html_mgr.Bry_anchor_class_image, Xow_html_mgr.Bry_anchor_rel_blank, anchor_title, Xow_html_mgr.Bry_img_class_none);
				else {
					Arg_itm_tkn link_tkn = lnki.Link_tkn().Val_tkn();
					byte[] link_ref = link_tkn.Dat_to_bry(src);
					byte[] link_ref_new = tmp_link_parser.Parse(tmp_bfr, tmp_url, wiki, link_ref, lnki_href);
					link_ref = link_ref_new == null ? lnki_href: link_ref_new;	// if parse fails, then assign to lnki_href; EX:link={{{1}}}
					lnki_ttl = ByteAry_.Coalesce(lnki_ttl, tmp_link_parser.Html_xowa_ttl());
					cfg.Lnki_full_image().Bld_bfr_many(bfr, elem_id, link_ref, html_view_src, xfer_itm.Html_w(), xfer_itm.Html_h(), lnki_alt_text, lnki_ttl, tmp_link_parser.Html_anchor_cls(), tmp_link_parser.Html_anchor_rel(), anchor_title, Xow_html_mgr.Bry_img_class_none);
				}
				switch (lnki.HAlign()) {
					case Xop_lnki_halign.Left:
					case Xop_lnki_halign.Right:
					case Xop_lnki_halign.None:	bfr.Add(Bry_div_end); break;
				}
			}
			if (lnki_halign == Xop_lnki_halign.Center) bfr.Add(Bry_div_end);
			tmp_bfr.Mkr_rls();
		}
	}
	private static byte[] Make_anchor_title(ByteAryBfr bfr, byte[] src, Xop_lnki_tkn lnki, byte[] lnki_ttl, Xohp_title_wkr anchor_title_wkr) {
		switch (lnki.Lnki_type()) {
			case Xop_lnki_type.Id_thumb:		// If the image is a thumb, do not add a title / alt, even if a caption is available
				return ByteAry_.Empty;
			case Xop_lnki_type.Id_frameless:	// If the image is frameless, add the caption as a title / alt. If no caption is available, do not add a title / alt
				break;
		}
		Xop_tkn_itm anchor_title_tkn = lnki.Caption_tkn();
		if (anchor_title_tkn == Xop_tkn_null.Null_tkn) return lnki_ttl; // no caption; return; (do not use lnki)
		bfr.Add(Anchor_title);
		anchor_title_wkr.Set(src, anchor_title_tkn).XferAry(bfr, 0);
		bfr.Add_byte(Byte_ascii.Quote);
		return bfr.XtoAryAndClear();
	}
	byte[] Video(byte[] src, Xoh_opts opts, Xop_lnki_tkn lnki, Xof_xfer_itm xfer_itm, int depth, int elem_id, boolean lnki_thumb, byte[] lnki_href, byte[] html_view_src, byte[] html_orig_src, byte[] lnki_alt_text) {
		int thumb_w = xfer_itm.Html_w();
		ByteAryBfr tmp_bfr = bfr_mkr.Get_k004();
		int play_btn_width = thumb_w; if (play_btn_width < 1) play_btn_width = wiki.Html_mgr().Img_thumb_width();
		if (lnki_thumb)
			cfg.Lnki_thumb_file_video().Bld_bfr_many(tmp_bfr, Play_btn(elem_id, play_btn_width, play_btn_width, html_orig_src, lnki.Ttl().Page_txt()), Img_thumb(lnki, xfer_itm, depth, elem_id, lnki_href, html_view_src, lnki_alt_text), Caption_div(src, lnki, depth, html_orig_src, lnki_href), Alt_html(src, lnki, depth));
		else
			cfg.Lnki_thumb_file_video().Bld_bfr_many(tmp_bfr, Play_btn(elem_id, play_btn_width, play_btn_width, html_orig_src, lnki.Ttl().Page_txt()), Img_thumb(lnki, xfer_itm, depth, elem_id, lnki_href, html_view_src, lnki_alt_text), ByteAry_.Empty, ByteAry_.Empty);
		return tmp_bfr.Mkr_rls().XtoAryAndClear();
	}
	byte[] Image_thumb(byte[] src, Xoh_opts opts, Xop_lnki_tkn lnki, Xof_xfer_itm xfer_itm, int depth, int elem_id, byte[] lnki_href, byte[] html_view_src, byte[] html_orig_src, byte[] lnki_alt_text, byte[] lnki_ttl, byte[] anchor_title) {
		byte[] lnki_alt_html = Alt_html(src, lnki, depth);
		ByteAryBfr tmp_bfr = bfr_mkr.Get_k004();
		byte[] lnki_class = xfer_itm.Html_pass()
			? Xow_html_mgr.Bry_img_class_thumbimage
			: Xow_html_mgr.Bry_img_class_none;
		cfg.Lnki_full_image().Bld_bfr_many(tmp_bfr, elem_id, lnki_href, html_view_src, xfer_itm.Html_w(), xfer_itm.Html_h(), lnki_alt_text, lnki_ttl, Xow_html_mgr.Bry_anchor_class_image, Xow_html_mgr.Bry_anchor_rel_blank, anchor_title, lnki_class);
		byte[] thumb = tmp_bfr.XtoAryAndClear();
		if (!wiki.Html_mgr().Imgs_mgr().Alt_in_caption().Val()) lnki_alt_html = ByteAry_.Empty;
		cfg.Lnki_thumb_file_image().Bld_bfr_many(tmp_bfr, thumb, Caption_div(src, lnki, depth, html_orig_src, lnki_href), lnki_alt_html);
		return tmp_bfr.Mkr_rls().XtoAryAndClear();
	}	static final byte[] Anchor_title = ByteAry_.new_utf8_(" title=\"");
	byte[] Audio(byte[] src, Xoh_opts opts, Xop_lnki_tkn lnki, int depth, int elem_id, byte[] lnki_href, byte[] html_orig_src, byte[] lnki_alt_text) {
		byte[] info_btn = ByteAry_.Empty;
		ByteAryBfr tmp_bfr = bfr_mkr.Get_k004();
		if (lnki.Media_icon()) {
			cfg.Lnki_thumb_part_info_btn().Bld_bfr_many(tmp_bfr, wiki.Html_mgr().Img_media_info_btn(), lnki_href);
			info_btn = tmp_bfr.XtoAryAndClear();
		}
		int play_btn_width = lnki.Width().Val(); if (play_btn_width < 1) play_btn_width = wiki.Html_mgr().Img_thumb_width();	// if no width set width to default thumb width
		cfg.Lnki_thumb_file_audio().Bld_bfr_many(tmp_bfr, Play_btn(elem_id, play_btn_width, Play_btn_max_width, html_orig_src, lnki.Ttl().Page_txt()), info_btn, Caption_div(src, lnki, depth, html_orig_src, lnki_href), Alt_html(src, lnki, depth));
		return tmp_bfr.Mkr_rls().XtoAryAndClear();
	}
	byte[] Img_thumb(Xop_lnki_tkn lnki, Xof_xfer_itm xfer_itm, int depth, int elem_id, byte[] lnki_href, byte[] html_view_src, byte[] alt) {
		ByteAryBfr tmp_bfr = bfr_mkr.Get_k004();
		cfg.Lnki_thumb_part_image().Bld_bfr_many(tmp_bfr, elem_id, Bry_class_internal, lnki_href, lnki.Ttl().Page_txt(), html_view_src, xfer_itm.Html_w(), xfer_itm.Html_h(), alt);
		return tmp_bfr.Mkr_rls().XtoAryAndClear();
	}
	public static final byte[] Bry_class_internal = ByteAry_.new_ascii_("image");
	byte[] Alt_text(byte[] src, Xop_lnki_tkn lnki, int depth) {
		if (!lnki.Alt_exists()) return ByteAry_.Empty;
		media_alt_fmtr.Set(html_wtr, Xoh_opts.lnki_alt_(), src, lnki.Alt_tkn().Val_tkn(), depth, cfg.Plain());
		ByteAryBfr tmp_bfr = bfr_mkr.Get_k004();
		media_alt_fmtr.XferAry(tmp_bfr, 0);
		return tmp_bfr.Mkr_rls().XtoAryAndClear(); 
	}
	byte[] Alt_html(byte[] src, Xop_lnki_tkn lnki, int depth) {
		if (!lnki.Alt_exists()) return ByteAry_.Empty;
		media_alt_fmtr.Set(html_wtr, Xoh_opts.root_(), src, lnki.Alt_tkn().Val_tkn(), depth, cfg.Lnki_thumb_part_alt());
		ByteAryBfr tmp_bfr = bfr_mkr.Get_k004();
		media_alt_fmtr.XferAry(tmp_bfr, 0);
		return tmp_bfr.Mkr_rls().XtoAryAndClear(); 
	}
	byte[] Caption_div(byte[] src, Xop_lnki_tkn lnki, int depth, byte[] html_orig_src, byte[] lnki_href) {
		ByteAryFmtrArg caption = Caption(src, lnki, Xoh_opts.root_(), depth, html_orig_src);
		ByteAryBfr tmp_bfr = bfr_mkr.Get_k004();
		byte[] magnify_btn = ByteAry_.Empty;
		if (lnki.Media_icon()) {
			cfg.Lnki_thumb_part_magnfiy_btn().Bld_bfr_many(tmp_bfr, wiki.Html_mgr().Img_thumb_magnify(), lnki_href, wiki.Msg_mgr().Val_by_id(Xol_msg_itm_.Id_file_enlarge));
			magnify_btn = tmp_bfr.XtoAryAndClear();
		}
		cfg.Lnki_thumb_part_caption().Bld_bfr_many(tmp_bfr, magnify_btn, caption);
		return tmp_bfr.Mkr_rls().XtoAryAndClear();				
	}
	ByteAryFmtrArg Caption(byte[] src, Xop_lnki_tkn lnki, Xoh_opts opts, int depth, byte[] html_orig_src) {
		return lnki.Caption_exists()
			? caption_fmtr.Set(html_wtr, opts, src, lnki.Caption_val_tkn(), depth, cfg.Plain())
			: ByteAryFmtrArg_.Null;
	}
	byte[] Play_btn(int elem_id, int width, int max_width, byte[] html_orig_src, byte[] lnki_href) {
		ByteAryBfr tmp_bfr = bfr_mkr.Get_k004();
		cfg.Lnki_thumb_part_play_btn().Bld_bfr_many(tmp_bfr, elem_id, wiki.Html_mgr().Img_media_play_btn(), width - 2, max_width, html_orig_src, lnki_href);	// NOTE: -2 is fudge factor else play btn will jut out over video thumb; see Earth and ISS video
		return tmp_bfr.Mkr_rls().XtoAryAndClear();				
	}
	static final int Play_btn_max_width = 1024;
	private static final byte[] Bry_div_bgn_center = ByteAry_.new_ascii_("<div class=\"center\">"), Bry_div_end = ByteAry_.new_ascii_("</div>")
		, Bry_div_float_none = ByteAry_.new_ascii_("<div class=\"floatnone\">"), Bry_div_float_left = ByteAry_.new_ascii_("<div class=\"floatleft\">"), Bry_div_float_right = ByteAry_.new_ascii_("<div class=\"floatright\">");
	public static byte[] Lnki_cls_visited(gplx.xowa.users.history.Xou_history_mgr history_mgr, byte[] wiki_key, byte[] page_ttl) {
		return history_mgr.Has(wiki_key, page_ttl) ? Lnki_cls_visited_bry : ByteAry_.Empty;
	}	static final byte[] Lnki_cls_visited_bry = ByteAry_.new_ascii_(" class=\"xowa-visited\"");
}