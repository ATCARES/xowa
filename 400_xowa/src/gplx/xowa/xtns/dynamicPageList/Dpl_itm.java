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
package gplx.xowa.xtns.dynamicPageList; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
class Dpl_itm {
	public ListAdp Ctg_includes() {return ctg_includes;} private ListAdp ctg_includes;
	public ListAdp Ctg_excludes() {return ctg_excludes;} private ListAdp ctg_excludes;
	public int Count() {return count;} private int count = Int_.MinValue;
	public int Offset() {return offset;} private int offset = Int_.MinValue;
	public boolean No_follow() {return no_follow;} private boolean no_follow;
	public boolean Suppress_errors() {return suppress_errors;} private boolean suppress_errors;
	public boolean Show_ns() {return show_ns;} private boolean show_ns;
	public byte Sort_ascending() {return sort_ascending;} private byte sort_ascending = Bool_.__byte;
	public int Ns_filter() {return ns_filter;} private int ns_filter = Ns_filter_null;
	public boolean Gallery_filesize() {return gallery_filesize;} private boolean gallery_filesize;
	public boolean Gallery_filename() {return gallery_filename;} private boolean gallery_filename;
	public int Gallery_imgs_per_row() {return gallery_imgs_per_row;} private int gallery_imgs_per_row;
	public int Gallery_img_w() {return gallery_img_w;} private int gallery_img_w;
	public int Gallery_img_h() {return gallery_img_h;} private int gallery_img_h;
	public byte[] Gallery_caption() {return gallery_caption;} private byte[] gallery_caption;
	public byte Redirects_mode() {return redirects_mode;} private byte redirects_mode = Dpl_redirect.Tid_unknown;
	public byte Sort_tid() {return sort_tid;} private byte sort_tid = Dpl_sort.Tid_categoryadd;
	public byte Quality_pages() {return quality_pages;} private byte quality_pages;
	public byte Stable_pages() {return stable_pages;} private byte stable_pages;
	public void Parse(Xow_wiki wiki, byte[] page_ttl, byte[] src, Xop_xnde_tkn xnde) {	// parse kvs in node; EX:<dpl>category=abc\nredirects=y\n</dpl>
		int content_bgn = xnde.Tag_open_end(), content_end = xnde.Tag_close_bgn();
		int pos = content_bgn;
		int fld_bgn = content_bgn;
		byte key_id = 0;
		Gfo_usr_dlg usr_dlg = wiki.App().Usr_dlg();
		boolean ws_bgn_chk = true; int ws_bgn_idx = -1, ws_end_idx = -1;
		while (true) {										// iterate over content
			boolean done = pos >= content_end;
			byte b = done ? Dlm_row : src[pos];				// get cur byte
			switch (b) {
				case Byte_ascii.Space: case Byte_ascii.Tab:
					if	(ws_bgn_chk) ws_bgn_idx = pos;										// definite ws at bgn; set ws_bgn_idx, and keep setting until text reached; handles mixed sequence of \s\n\t where last tkn should be ws_bgn_idx
					else			{if (ws_end_idx == -1) ws_end_idx = pos;};				// possible ws at end; may be overriden later; see AdjustWsForTxtTkn
					break;
				case Dlm_fld: {								// dlm is fld; EX: "=" in "category="
					if (ws_bgn_idx != -1) fld_bgn = ws_bgn_idx + 1;	// +1 to position after last known ws
					int fld_end = ws_end_idx == -1 ? pos : ws_end_idx;
					key_id = Dpl_itm_keys.Parse(src, fld_bgn, fld_end, Dpl_itm_keys.Key_null);
					if (key_id == Dpl_itm_keys.Key_null) {	// unknown key; warn and set pos to end of line; EX: "unknown=";
						usr_dlg.Warn_many("", "", "unknown_key: page=~{0} key=~{1}", String_.new_utf8_(page_ttl), String_.new_utf8_(src, fld_bgn, fld_end));
						fld_bgn = ByteAry_.FindFwd(src, Byte_ascii.NewLine);
						if (fld_bgn == ByteAry_.NotFound) break; 
					}
					else {									// known key; set pos to val_bgn
						fld_bgn = pos + Int_.Const_dlm_len;
					}
					ws_bgn_chk = true; ws_bgn_idx = ws_end_idx = -1;
					break;
				}
				case Dlm_row: {								// dlm is nl; EX: "\n" in "category=abc\n"
					if (fld_bgn != pos) {					// ignores blank lines
						if (ws_bgn_idx != -1) fld_bgn = ws_bgn_idx + 1;	// +1 to position after last known ws
						int fld_end = ws_end_idx == -1 ? pos : ws_end_idx;
						byte[] val = ByteAry_.Mid(src, fld_bgn, fld_end);
						Parse_cmd(wiki, key_id, val);
					}
					fld_bgn = pos + Int_.Const_dlm_len;
					ws_bgn_chk = true; ws_bgn_idx = ws_end_idx = -1;
					break;
				}
				default:	// text token
					if (ws_bgn_chk) ws_bgn_chk = false; else ws_end_idx = -1;		// INLINE: AdjustWsForTxtTkn
					break;
			}
			if (done) break;
			++pos;
		}
	}
	private static final byte Dlm_fld = Byte_ascii.Eq, Dlm_row = Byte_ascii.NewLine;
	public void Parse_cmd(Xow_wiki wiki, byte key_id, byte[] val) {
		switch (key_id) {
			case Dpl_itm_keys.Key_category: 			if (ctg_includes == null) ctg_includes = ListAdp_.new_(); ctg_includes.Add(Xoa_ttl.Replace_spaces(val)); break;
			case Dpl_itm_keys.Key_notcategory:		 	if (ctg_excludes == null) ctg_excludes = ListAdp_.new_(); ctg_excludes.Add(Xoa_ttl.Replace_spaces(val)); break;
			case Dpl_itm_keys.Key_ns:		 			{Xow_ns ns = (Xow_ns)wiki.Ns_mgr().Trie_match_exact(val, 0, val.length); ns_filter = ns == null ? Xow_ns_.Id_main : ns.Id(); break;}
			case Dpl_itm_keys.Key_order:				sort_ascending = Dpl_sort.Parse_as_bool_byte(val); break;
			case Dpl_itm_keys.Key_suppresserrors:		suppress_errors = Dpl_itm_keys.Parse_as_bool(val, false); break;
			case Dpl_itm_keys.Key_nofollow:				no_follow = Dpl_itm_keys.Parse_as_bool(val, true); break;	// NOTE: default to true; allows passing nofollow=nofollow; MW: if ('false' != $arg)
			case Dpl_itm_keys.Key_shownamespace:		show_ns = Dpl_itm_keys.Parse_as_bool(val, true); break; // NOTE: default to true;
			case Dpl_itm_keys.Key_redirects:			redirects_mode = Dpl_redirect.Parse(val); break;
			case Dpl_itm_keys.Key_stablepages:			stable_pages = Dpl_stable_tid.Parse(val); break;
			case Dpl_itm_keys.Key_qualitypages:			quality_pages = Dpl_redirect.Parse(val); break;
			case Dpl_itm_keys.Key_addfirstcategorydate:	Parse_ctg_date(val); break;
			case Dpl_itm_keys.Key_count:				count = ByteAry_.XtoIntOr(val, Int_.MinValue); break;
			case Dpl_itm_keys.Key_offset:				offset = ByteAry_.XtoIntOr(val, Int_.MinValue); break;
			case Dpl_itm_keys.Key_imagesperow:			gallery_imgs_per_row = ByteAry_.XtoIntOr(val, Int_.MinValue); break;
			case Dpl_itm_keys.Key_imagewidth:			gallery_img_w = ByteAry_.XtoIntOr(val, Int_.MinValue); break;
			case Dpl_itm_keys.Key_imageheight:			gallery_img_h = ByteAry_.XtoIntOr(val, Int_.MinValue); break;
			case Dpl_itm_keys.Key_gallerycaption:		gallery_caption = val; break;	// FUTURE: parse for {{int:}}?
			case Dpl_itm_keys.Key_galleryshowfilesize:	gallery_filesize = Dpl_itm_keys.Parse_as_bool(val, true); break;
			case Dpl_itm_keys.Key_galleryshowfilename:	gallery_filename = Dpl_itm_keys.Parse_as_bool(val, true); break;
			case Dpl_itm_keys.Key_ordermethod:			sort_tid = Dpl_sort.Parse(val); break;
		}
	}
	private void Parse_ctg_date(byte[] val) {
		//			byte val_key = Keys_get_or(val, Dpl_itm_keys.Key_false);
		//			if (val_key == Dpl_itm_keys.Key_true)
		//				ctg_date = true;
		//			else {
		//				if (val.length == 8) { 	// HACK: preg_match( '/^(?:[ymd]{2,3}|ISO 8601)$/'
		//					ctg_date = true;
		//					ctg_date_fmt = val;
		//					if (ctg_date_fmt.length == 2) {
		//						ctg_date_strip = true;
		//						ctg_date_fmt = ByteAry_.Add(ctg_date_fmt, new byte[] {Byte_ascii.Ltr_y});
		//					}
		//				}
		//				else
		//					ctg_date = false;
		//			}
	}
	public static final int Ns_filter_null = Int_.MinValue;
	// boolean ctg_date = false, ctg_date_strip = false;
	// byte[] ns_include = null;
	// byte[] ctg_date_fmt;
}
class Dpl_stable_tid {
	public static final byte Tid_null = 0, Tid_include = 1, Tid_only = 2, Tid_exclude = 3;
	public static byte Parse(byte[] bry) {
		byte key = Dpl_itm_keys.Parse(bry, Dpl_redirect.Tid_exclude);	// NOTE: exclude is default value.
		switch (key) {
			case Dpl_itm_keys.Key_exclude: 			return Tid_exclude;
			case Dpl_itm_keys.Key_include: 			return Tid_include;
			case Dpl_itm_keys.Key_only: 			return Tid_only;
			default:								throw Err_mgr._.unhandled_(key);
		}
	}
}