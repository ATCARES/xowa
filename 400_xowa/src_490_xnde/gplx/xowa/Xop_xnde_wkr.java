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
import gplx.xowa.apps.*; import gplx.xowa.xtns.*;
public class Xop_xnde_wkr implements Xop_ctx_wkr {
	public void Ctor_ctx(Xop_ctx ctx) {this.ctx = ctx; this.tkn_mkr = ctx.Tkn_mkr();}
	public Xob_xnde_wkr File_wkr() {return file_wkr;} public Xop_xnde_wkr File_wkr_(Xob_xnde_wkr v) {file_wkr = v; return this;} private Xob_xnde_wkr file_wkr;
	public void Page_bgn(Xop_ctx ctx) {this.src = ctx.Src(); this.root = ctx.Root(); this.src_len = src.length;} private Xop_ctx ctx; Xop_tkn_mkr tkn_mkr; byte[] src; int src_len; Xop_root_tkn root;
	public void Page_end(Xop_ctx ctx, Xop_root_tkn root, byte[] src, int src_len) {}
	public void AutoClose(Xop_ctx ctx, byte[] src, int src_len, int bgn_pos, int cur_pos, Xop_tkn_itm tkn) {
		Xop_xnde_tkn xnde = (Xop_xnde_tkn)tkn;
		xnde.Src_end_(src_len);
		xnde.Subs_move(root);
		ctx.Msg_log().Add_itm_none(Xop_xnde_log.Dangling_xnde, src, xnde.Src_bgn(), xnde.Name_end());	// NOTE: xnde.Src_bgn to start at <; xnde.Name_end b/c xnde.Src_end is -1
	}
	public int MakeTkn(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int src_len, int bgn_pos, int cur_pos) {
		if (cur_pos == src_len) return ctx.LxrMake_txt_(src_len);					// "<" is last char in page; strange, but don't raise error;

		// find >
		byte cur_byt = src[cur_pos];
		boolean end_tag_exists = false;
		if (cur_byt == Byte_ascii.Slash) { // "</" encountered (note that < enters this frame)
			++cur_pos;
			if (cur_pos == src_len) return ctx.LxrMake_txt_(src_len);				// "</" are last chars on page; strange, but don't raise error;
			cur_byt = src[cur_pos];
			end_tag_exists = true;
		}
		ByteTrieMgr_slim tag_trie = ctx.App().Xnde_tag_regy().XndeNames(ctx.Parse_tid());
		Object tag_obj = tag_trie.Match(cur_byt, src, cur_pos, src_len);	// NOTE:tag_obj can be null in wiki_tmpl mode; EX: "<ul" is not a valid tag in wiki_tmpl, but is valid in wiki_main
		int atrs_bgn_pos = tag_trie.Match_pos();
		int tag_end_pos = atrs_bgn_pos - 1;
		if (tag_obj != null) {
			if (atrs_bgn_pos >= src_len) return ctx.LxrMake_txt_(atrs_bgn_pos);	// truncated tag; EX: "<br"
			switch (src[atrs_bgn_pos]) {	// NOTE: not sure about rules; Preprocessor_DOM.php calls preg_match on $elementsRegex which seems to break on word boundaries; $elementsRegex = "~($xmlishRegex)(?:\s|\/>|>)|(!--)~iA";
				case Byte_ascii.Tab: case Byte_ascii.NewLine: case Byte_ascii.CarriageReturn: case Byte_ascii.Space:
					++atrs_bgn_pos;	// set bgn_pos to be after ws
					break;
				case Byte_ascii.Slash: case Byte_ascii.Gt:
					++atrs_bgn_pos;	// set bgn_pos to be after ws
					break;
				case Byte_ascii.Backslash:
					++tag_end_pos;
					break;
				case Byte_ascii.Dollar:// handles <br$2>;
				default:	// allow all other symbols by defaults 
					break;
				case Byte_ascii.Ltr_A: case Byte_ascii.Ltr_B: case Byte_ascii.Ltr_C: case Byte_ascii.Ltr_D: case Byte_ascii.Ltr_E:
				case Byte_ascii.Ltr_F: case Byte_ascii.Ltr_G: case Byte_ascii.Ltr_H: case Byte_ascii.Ltr_I: case Byte_ascii.Ltr_J:
				case Byte_ascii.Ltr_K: case Byte_ascii.Ltr_L: case Byte_ascii.Ltr_M: case Byte_ascii.Ltr_N: case Byte_ascii.Ltr_O:
				case Byte_ascii.Ltr_P: case Byte_ascii.Ltr_Q: case Byte_ascii.Ltr_R: case Byte_ascii.Ltr_S: case Byte_ascii.Ltr_T:
				case Byte_ascii.Ltr_U: case Byte_ascii.Ltr_V: case Byte_ascii.Ltr_W: case Byte_ascii.Ltr_X: case Byte_ascii.Ltr_Y: case Byte_ascii.Ltr_Z:
				case Byte_ascii.Ltr_a: case Byte_ascii.Ltr_b: case Byte_ascii.Ltr_c: case Byte_ascii.Ltr_d: case Byte_ascii.Ltr_e:
				case Byte_ascii.Ltr_f: case Byte_ascii.Ltr_g: case Byte_ascii.Ltr_h: case Byte_ascii.Ltr_i: case Byte_ascii.Ltr_j:
				case Byte_ascii.Ltr_k: case Byte_ascii.Ltr_l: case Byte_ascii.Ltr_m: case Byte_ascii.Ltr_n: case Byte_ascii.Ltr_o:
				case Byte_ascii.Ltr_p: case Byte_ascii.Ltr_q: case Byte_ascii.Ltr_r: case Byte_ascii.Ltr_s: case Byte_ascii.Ltr_t:
				case Byte_ascii.Ltr_u: case Byte_ascii.Ltr_v: case Byte_ascii.Ltr_w: case Byte_ascii.Ltr_x: case Byte_ascii.Ltr_y: case Byte_ascii.Ltr_z:
				case Byte_ascii.Num_0: case Byte_ascii.Num_1: case Byte_ascii.Num_2: case Byte_ascii.Num_3: case Byte_ascii.Num_4:
				case Byte_ascii.Num_5: case Byte_ascii.Num_6: case Byte_ascii.Num_7: case Byte_ascii.Num_8: case Byte_ascii.Num_9:
					tag_obj = null;
					break;
			}
		}
		boolean ctx_cur_tid_is_tblw_atr_owner = false;
		switch (ctx.Cur_tkn_tid()) {
			case Xop_tkn_itm_.Tid_tblw_tb: case Xop_tkn_itm_.Tid_tblw_tr: case Xop_tkn_itm_.Tid_tblw_th:
				ctx_cur_tid_is_tblw_atr_owner = true;
				break;
		}
//			int tag_idx = -1;
//			if (tag_obj == null) {	// not a known xml tag; EX: "<abcd>"; "if 5 < 7 then"
//				if (ctx.Parse_tid() == Xop_parser_.Parse_tid_page_wiki) {
//					int xowa_tag_end = cur_pos + Pf_tag.Bry_tag_hdr_len;
//					if (ByteAry_.Eq(Pf_tag.Bry_tag_hdr, src, cur_pos, xowa_tag_end)) {	// tag bgns with xowa_tag_
//						tag_idx = ByteAry_.XtoIntByPos(src, xowa_tag_end, tag_end_pos, -1);
//						ctx.Subs_add(tkn_mkr.Bry(bgn_pos - 1, tag_end_pos + 1, ByteAry_.Empty));	// add tkn for <xowa_tag_0>
//						bgn_pos = tag_end_pos + 2;	// +2: 1 after > and 1 for <
//						cur_pos = Find_gt_pos(ctx, src, bgn_pos, src_len);
//						Tfds.Write(tag_idx);
//						/*
//						set the tag for the xtn nde (EX: ref)
//						set xowa_tag flag to true so xtn can do adjustment at end for </xowa_tag_>
//						*/
//					}
//					if (ctx_cur_tid_is_tblw_atr_owner)			// <unknown_tag is occurring inside tblw element (EX: {| style='margin:1em<f'); just add to txt tkn
//						return ctx.LxrMake_txt_(cur_pos);
//					else {										// <unknown_tag is occurring anyhwere else; escape < to &lt; and resume from character just after it;
//						ctx.Subs_add(tkn_mkr.Bry(cur_pos - 1, cur_pos, Bry_escape_lt));
//						return cur_pos;
//					}
//				}
//				else {
//					if (ctx_cur_tid_is_tblw_atr_owner) Xop_tblw_wkr.Atrs_close(ctx, src, root);
//					return ctx.LxrMake_txt_(cur_pos);
//				}
//			}
		if (tag_obj == null) {	// not a known xml tag; EX: "<abcd>"; "if 5 < 7 then"
			if (ctx.Parse_tid() == Xop_parser_.Parse_tid_page_wiki) {
				if (ctx_cur_tid_is_tblw_atr_owner)			// <unknown_tag is occurring inside tblw element (EX: {| style='margin:1em<f'); just add to txt tkn
					return ctx.LxrMake_txt_(cur_pos);
				else {										// <unknown_tag is occurring anyhwere else; escape < to &lt; and resume from character just after it;
					ctx.Subs_add(tkn_mkr.Bry(cur_pos - 1, cur_pos, Bry_escape_lt));
					return cur_pos;
				}
			}
			else {
				if (ctx_cur_tid_is_tblw_atr_owner) Xop_tblw_wkr.Atrs_close(ctx, src, root);
				return ctx.LxrMake_txt_(cur_pos);
			}
		}
		Xop_xnde_tag tag = (Xop_xnde_tag)tag_obj;
		int gt_pos = -1;	// find closing >; NOTE: MW does not ignore > inside quotes; EX: <div id="a>b">abc</div> -> <div id="a>
		for (int i = cur_pos; i < src_len; i++) {
			byte b = src[i];
			switch (b) {
				case Byte_ascii.Lt:	// < encountered; may be inner node inside tag which is legal in wikitext; EX: "<ul style=<nowiki>#</nowiki>FFFFFF>"
					int valid_inner_xnde_gt = ctx.App().Xatr_parser().Xnde_find_gt_find(src, i + 1, src_len);
					if (valid_inner_xnde_gt != String_.NotFound) {
						i = valid_inner_xnde_gt;
					}
					break;
				case Byte_ascii.Gt:
					gt_pos = i;
					i = src_len;
					break;
			}
		}
		if (gt_pos == -1) {return ctx.LxrMake_log_(Xop_xnde_log.Eos_while_closing_tag, bgn_pos, cur_pos);}
		boolean force_xtn_for_nowiki = false;
		int end_pos = gt_pos + 1;
		switch (ctx.Parse_tid()) {					// NOTE: special logic to handle <*include*>; SEE: NOTE_1 below
			case Xop_parser_.Parse_tid_page_wiki:	// NOTE: ignore if (a) wiki and (b) <noinclude> or <onlyinclude>
				switch (tag.Id()) {
					case Xop_xnde_tag_.Tid_noinclude:
					case Xop_xnde_tag_.Tid_onlyinclude:
						ctx.Subs_add(tkn_mkr.Ignore(bgn_pos, end_pos, Xop_ignore_tkn.Ignore_tid_include_wiki));
						return end_pos;
					case Xop_xnde_tag_.Tid_includeonly:	// NOTE: includeonly tag should be ignored, but inner content should be visible; see test
					case Xop_xnde_tag_.Tid_nowiki:
						force_xtn_for_nowiki = true;
						ctx_cur_tid_is_tblw_atr_owner = false;
						break;
				}
				break;
			case Xop_parser_.Parse_tid_tmpl:			// NOTE: ignore if (a) tmpl and (b) <includeonly>
				switch (tag.Id()) {
					case Xop_xnde_tag_.Tid_includeonly:
						ctx.Subs_add(tkn_mkr.Ignore(bgn_pos, end_pos, Xop_ignore_tkn.Ignore_tid_include_tmpl));
						return end_pos;
					case Xop_xnde_tag_.Tid_noinclude:
						return Make_noinclude(ctx, tkn_mkr, root, src, src_len, bgn_pos, gt_pos, tag, atrs_bgn_pos, src[atrs_bgn_pos - 1]);
					case Xop_xnde_tag_.Tid_nowiki:
						force_xtn_for_nowiki = true;
						break;
				}
				break;
			case Xop_parser_.Parse_tid_page_tmpl:		// NOTE: added late; SEE:comment test for "a <!-<noinclude></noinclude>- b -->c"
				switch (tag.Id()) {
					case Xop_xnde_tag_.Tid_noinclude:
						ctx.Subs_add(tkn_mkr.Ignore(bgn_pos, end_pos, Xop_ignore_tkn.Ignore_tid_include_tmpl));
						return end_pos;
					case Xop_xnde_tag_.Tid_nowiki:		// NOTE: if encountered in page_tmpl stage, mark nowiki as xtn; added for nowiki_xnde_frag; DATE:2013-01-27
						force_xtn_for_nowiki = true;
						break;
				}
				break;
		}
		if (ctx_cur_tid_is_tblw_atr_owner) Xop_tblw_wkr.Atrs_close(ctx, src, root);	// < found inside tblw; close off tblw attributes; EX: |- id='abcd' <td>a</td> (which is valid wikitext; NOTE: must happen after <nowiki>
		if (end_tag_exists)
			return Make_xtag_end(ctx, tkn_mkr, root, src, src_len, bgn_pos, gt_pos, tag);
		else
			return Make_xtag_bgn(ctx, tkn_mkr, root, src, src_len, bgn_pos, gt_pos, tag, atrs_bgn_pos, src[tag_end_pos], force_xtn_for_nowiki);
	}
	int Make_noinclude(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int src_len, int bgn_pos, int gtPos, Xop_xnde_tag tag, int tag_end_pos, byte tag_end_byte) {
		if (tag_end_byte == Byte_ascii.Slash) {	// inline
			boolean valid = true;
			for (int i = tag_end_pos; i < gtPos; i++) {
				switch (src[i]) {
					case Byte_ascii.Space: case Byte_ascii.Tab: case Byte_ascii.NewLine: break;
					case Byte_ascii.Slash: break;
					default: valid = false; break;
				}
			}
			if (valid) {
				ctx.Subs_add(tkn_mkr.Ignore(bgn_pos, gtPos, Xop_ignore_tkn.Ignore_tid_include_tmpl));
				return gtPos + Apps_app_mgr.Adj_next_char;
			}
			else {
				return ctx.LxrMake_txt_(gtPos);
			}
		}
		int end_rhs = -1, findPos = gtPos;
		byte[] end_bry = Xop_xnde_tag_.Tag_noinclude.XtnEndTag(); int end_bry_len = end_bry.length;
		while (true) {
			int end_lhs = ByteAry_.FindFwd(src, end_bry, findPos);
			if (end_lhs == -1 || (end_lhs + end_bry_len) == src_len) break;	// nothing found or EOS;
			findPos = end_lhs;
			for (int i = end_lhs + end_bry_len; i < src_len; i++) {
				switch (src[i]) {
					case Byte_ascii.Space: case Byte_ascii.Tab: case Byte_ascii.NewLine: break;
					case Byte_ascii.Slash: break;
					case Byte_ascii.Gt: end_rhs = i + 1; i = src_len; break;	// +1 to place after Gt
					default:			findPos = i    ; i = src_len; break;
				}
			}
			if (end_rhs != -1) break;
		}
		if (end_rhs == -1) end_rhs = src_len;
		ctx.Subs_add(tkn_mkr.Ignore(bgn_pos, end_rhs, Xop_ignore_tkn.Ignore_tid_include_tmpl));
		return end_rhs;
	}
	int Make_xtag_bgn(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int src_len, int bgn_pos, int gtPos, Xop_xnde_tag tag, int tag_end_pos, byte tag_end_byte, boolean force_xtn_for_nowiki) {
		boolean inline = false;
		int open_tag_end = gtPos + Apps_app_mgr.Adj_next_char, atrs_bgn = -1, atrs_end = -1;
		// calc (a) inline; (b) atrs
		switch (tag_end_byte) {	// look at last char of tag; EX: for b, following are registered: "b/","b>","b\s","b\n","b\t"
			case Byte_ascii.Slash:	// "/" EX: "<br/"; // NOTE: <pre/a>, <pre//> are allowed
				inline = true;		
				break;
			case Byte_ascii.Backslash:	// allow <br\>; EX:w:Mosquito
				if (tag.Inline_by_backslash())
					src[tag_end_pos] = Byte_ascii.Slash;	
				break;
			case Byte_ascii.Gt:		// ">" "normal" tag; noop
				break;
			default:				// "\s", "\n", "\t"
				atrs_bgn = tag_end_pos;		// set atrs_bgn to first char after ws; EX: "<a\shref='b/>" atrs_bgn = pos(h)
				atrs_end = gtPos;			// set atrs_end to gtPos;				EX: "<a\shref='b/>" atrs_end = pos(>)
				if (src[gtPos - 1] == Byte_ascii.Slash) {	// adjust if inline
					--atrs_end;
					inline = true;
				}
				break;
		}
		Xop_xatr_itm[] atrs = null;
		if (ctx.Parse_tid() == Xop_parser_.Parse_tid_page_wiki) {
			atrs = ctx.App().Xatr_parser().Parse(ctx.Msg_log(), src, atrs_bgn, atrs_end);
		}
		if (tag.Xtn() || (tag.XtnTmpl() )		// tag is xtnTmpl and parse type is template (not wiki)
			|| (force_xtn_for_nowiki && !inline))	 {
			return Make_xnde_xtn(tag, bgn_pos, gtPos + 1, atrs_bgn, atrs_end, atrs, inline);	// find end tag and do not parse anything inbetween
		}
		if (tag.Restricted()) {
			Xoa_page page = ctx.Page();
			if (!page.Allow_all_html() && page.Wiki().Wiki_tid() != Xow_wiki_type_.Tid_home) {
				int end_pos = gtPos + 1;
				ctx.Subs_add(tkn_mkr.Bry(bgn_pos, end_pos, ByteAry_.Add(gplx.html.Html_entities.Lt, tag.Name_bry(), gplx.html.Html_entities.Gt)));
				return end_pos;
			}
		}
		int prv_acs = ctx.Stack_idx_typ_tblw(Xop_tkn_itm_.Tid_xnde);
		Xop_xnde_tkn prv_xnde = prv_acs == -1 ? null : (Xop_xnde_tkn)ctx.Stack_get(prv_acs); //(Xop_xnde_tkn)ctx.Stack_get_typ(Xop_tkn_itm_.Tid_xnde);
		int prv_xnde_tagId = prv_xnde == null ? Xop_tkn_itm_.Tid_null : prv_xnde.Tag().Id();

		boolean tag_ignore = false;
		int tagId = tag.Id();
		if (tagId == Xop_xnde_tag_.Tid_table || tag.TblSub()) {							// tbl tag; EX: <table>,<tr>,<td>,<th>
			Tblw_bgn(ctx, tkn_mkr, root, src, src_len, bgn_pos, gtPos + 1, tagId, atrs_bgn, atrs_end);
			return gtPos + 1;
		}
		else if (prv_xnde_tagId == Xop_xnde_tag_.Tid_p && tagId == Xop_xnde_tag_.Tid_p) {
			ctx.Msg_log().Add_itm_none(Xop_xnde_log.Auto_closing_section, src, bgn_pos, bgn_pos);
			EndTag(ctx, root, prv_xnde, src, src_len, bgn_pos - 1, bgn_pos - 1, tagId, true, tag);
		}
		else if (tagId == prv_xnde_tagId && tag.Repeat_ends()) {	// EX: "<code>a<code>b" -> "<code>a</code>b"
			EndTag(ctx, root, prv_xnde, src, src_len, bgn_pos - 1, bgn_pos - 1, tagId, true, tag);
			return gtPos + 1;
		}
		else if (tagId == prv_xnde_tagId && tag.Repeat_mids()) {	// EX: "<li>a<li>b" -> "<li>a</li><li>b"
			EndTag(ctx, root, prv_xnde, src, src_len, bgn_pos - 1, bgn_pos - 1, tagId, true, tag);
		}
		else if (!tag.Nest() && TagStack_has(ctx, tagId)) return ctx.LxrMake_log_(Xop_xnde_log.Invalid_nest, bgn_pos, gtPos);
		else if (tag.InlineOnly()) inline = true; // <br></br> not allowed; convert <br> to <br/> </br> will be escaped
		else if (tag.NoInline() && inline) {
			Xop_xnde_tkn xnde_inline = Xnde_bgn(ctx, tkn_mkr, root, tag, Xop_xnde_tkn.CloseMode_open, bgn_pos, open_tag_end, atrs_bgn, atrs_end, atrs);
			EndTag(ctx, root, xnde_inline, src, src_len, bgn_pos, gtPos, tagId, false, tag);
			ctx.Msg_log().Add_itm_none(Xop_xnde_log.No_inline, src, bgn_pos, gtPos);
			return gtPos + Int_.Const_position_after_char;
		}
		else if (tagId == Xop_xnde_tag_.Tid_li) {
			int subs_len = ctx.Root().Subs_len();		// ignore redundant <li>; EX: "* <li>a</li>\n" -> "* a"; EX: http://it.wikipedia.org/wiki/Milano#Bibliographie
			for (int i = subs_len - 1; i > -1; i--) {	// iterate backwards
				Xop_tkn_itm sub = ctx.Root().Subs_get_or_null(i);
				switch (sub.Tkn_tid()) {
					case Xop_tkn_itm_.Tid_space:
					case Xop_tkn_itm_.Tid_tab:		// ignore ws
						continue;
					case Xop_tkn_itm_.Tid_list:		// handle "* <li>", "# <li>", ": <li>", "; <li>"
						tag_ignore = true;
						break;
					default:						// stop at anything else
						break;
				}
				i = -1;
			}
			if (!TagStack_has_list(ctx)) {				// <li> encountered, but no <ul> or <ol> in stack; automatically add; EX.WP.FR:France; [[File:A.png|thumb|\n<li>a</li>\n]]
				Xop_xnde_tkn xnde_ul = Xnde_bgn(ctx, tkn_mkr, root, Xop_xnde_tag_.Tag_ul, Xop_xnde_tkn.CloseMode_open, bgn_pos, bgn_pos, -1, -1, atrs);
				ctx.Stack_add(xnde_ul);
			}
		}
		Xop_xnde_tkn xnde = null;
		xnde = Xnde_bgn(ctx, tkn_mkr, root, tag, inline ? Xop_xnde_tkn.CloseMode_inline : Xop_xnde_tkn.CloseMode_open, bgn_pos, open_tag_end, atrs_bgn, atrs_end, atrs);
		if (!inline && tag.BgnNdeMode() != Xop_xnde_tag_.BgnNdeMode_inline) {
			ctx.Stack_add(xnde);
		}
		if (tag_ignore)
			xnde.Tag_visible_(false);
		if (tag.Empty_ignored()) ctx.Empty_ignored_y_();
		return open_tag_end;
	}
	boolean TagStack_has(Xop_ctx ctx, int tagId) {
		int acs_end = ctx.Stack_len() - 1;
		if (acs_end == -1) return false;
		for (int i = acs_end; i > -1; i--) {
			Xop_tkn_itm tkn = ctx.Stack_get(i);
			switch (tkn.Tkn_tid()) {
				case Xop_tkn_itm_.Tid_tblw_td:
				case Xop_tkn_itm_.Tid_tblw_th:
				case Xop_tkn_itm_.Tid_tblw_tc:
					return false;
				case Xop_tkn_itm_.Tid_xnde:
					Xop_xnde_tkn xnde_tkn = (Xop_xnde_tkn)tkn;
					if (xnde_tkn.Tag().Id() == tagId) return true;
					break;
			}
		}
		return false;
	}
	boolean TagStack_has_list(Xop_ctx ctx) {
		int acs_end = ctx.Stack_len() - 1;
		if (acs_end == -1)
			return false;
		else {
			Xop_tkn_itm tkn = ctx.Stack_get(acs_end);
			switch (tkn.Tkn_tid()) {
				case Xop_tkn_itm_.Tid_list:
					return true;
				case Xop_tkn_itm_.Tid_xnde:
					Xop_xnde_tkn xnde_tkn = (Xop_xnde_tkn)tkn;
					switch (xnde_tkn.Tag().Id()) {
						case Xop_xnde_tag_.Tid_ul:
						case Xop_xnde_tag_.Tid_ol:
							return true;
					}
					break;
			}
		}
		return false;
	}
	void Tblw_bgn(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int src_len, int bgn_pos, int cur_pos, int tagId, int atrs_bgn, int atrs_end) {
		byte wlxr_type = 0;
		switch (tagId) {
			case Xop_xnde_tag_.Tid_table:	wlxr_type = Xop_tblw_wkr.Tblw_type_tb; break;
			case Xop_xnde_tag_.Tid_tr:		wlxr_type = Xop_tblw_wkr.Tblw_type_tr; break;
			case Xop_xnde_tag_.Tid_td:		wlxr_type = Xop_tblw_wkr.Tblw_type_td; break;
			case Xop_xnde_tag_.Tid_th:		wlxr_type = Xop_tblw_wkr.Tblw_type_th; break;
			case Xop_xnde_tag_.Tid_caption:	wlxr_type = Xop_tblw_wkr.Tblw_type_tc; break;
		}
		ctx.Tblw().MakeTkn_bgn(ctx, tkn_mkr, root, src, src_len, bgn_pos, cur_pos, wlxr_type, false, true, atrs_bgn, atrs_end);
	}
	void Tblw_end(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int src_len, int bgn_pos, int cur_pos, int tagId) {
		int typeId = 0;
		byte wlxr_type = 0;
		switch (tagId) {
			case Xop_xnde_tag_.Tid_table:	typeId = Xop_tkn_itm_.Tid_tblw_tb; wlxr_type = Xop_tblw_wkr.Tblw_type_tb; break;
			case Xop_xnde_tag_.Tid_tr:		typeId = Xop_tkn_itm_.Tid_tblw_tr; wlxr_type = Xop_tblw_wkr.Tblw_type_tr; break;
			case Xop_xnde_tag_.Tid_td:		typeId = Xop_tkn_itm_.Tid_tblw_td; wlxr_type = Xop_tblw_wkr.Tblw_type_td; break;
			case Xop_xnde_tag_.Tid_th:		typeId = Xop_tkn_itm_.Tid_tblw_th; wlxr_type = Xop_tblw_wkr.Tblw_type_th; break;
			case Xop_xnde_tag_.Tid_caption:	typeId = Xop_tkn_itm_.Tid_tblw_tc; wlxr_type = Xop_tblw_wkr.Tblw_type_tc; break;
		}
		Xop_tblw_tkn prv_tkn = ctx.Stack_get_tblw();
		int prv_tkn_typeId = prv_tkn == null ? -1 : prv_tkn.Tkn_tid();
		ctx.Tblw().MakeTkn_end_tblw(ctx, tkn_mkr, root, src, src_len, bgn_pos, cur_pos, typeId, wlxr_type, prv_tkn, prv_tkn_typeId, true);
		ctx.Para().Process_nl_sect_end(cur_pos);
	}		
	int Make_xtag_end(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int src_len, int bgn_pos, int cur_pos, Xop_xnde_tag endTag) {
		int endTagId = endTag.Id();
		cur_pos = Xop_lxr_.Find_fwd_while_non_ws(src, cur_pos, src_len) + 1;
		int acs_pos = ctx.Stack_idx_typ_tblw(Xop_tkn_itm_.Tid_xnde);
		Xop_xnde_tkn bgnNde = (Xop_xnde_tkn)ctx.Stack_get(acs_pos);
		int bgnTagId = bgnNde == null ? -1 : bgnNde.Tag().Id();

		int endNdeMode = endTag.EndNdeMode();
		switch (bgnTagId) {
			case Xop_xnde_tag_.Tid_sub:
				if (endTagId == Xop_xnde_tag_.Tid_sup) {endTagId = Xop_xnde_tag_.Tid_sub; ctx.Msg_log().Add_itm_none(Xop_xnde_log.Sub_sup_swapped, src, bgn_pos, cur_pos);}
				break;
			case Xop_xnde_tag_.Tid_sup:
				if (endTagId == Xop_xnde_tag_.Tid_sub) {endTagId = Xop_xnde_tag_.Tid_sup; ctx.Msg_log().Add_itm_none(Xop_xnde_log.Sub_sup_swapped, src, bgn_pos, cur_pos);}
				break;
		}
		if (endTagId == Xop_xnde_tag_.Tid_table || endTag.TblSub()) {
			Tblw_end(ctx, tkn_mkr, root, src, src_len, bgn_pos, cur_pos, endTagId);
			return cur_pos;
		}
		if (endTag.Empty_ignored() && ctx.Empty_ignored()		// emulate TidyHtml logic for pruning empty tags; EX: "<li> </li>" -> "")
			&& bgnNde != null) {								// bgnNde will be null if only endNde; EX:WP:Sukhoi Su-47; "* </li>" 
			ctx.Empty_ignore(bgnNde.Tkn_sub_idx());
			EndTag(ctx, root, bgnNde, src, src_len, bgn_pos, cur_pos, endTagId, true, endTag);
			return cur_pos;
		}
		switch (endNdeMode) {
			case Xop_xnde_tag_.EndNdeMode_inline:	// PATCH.WP: allows </br>, </br/> and many other variants
				Xnde_bgn(ctx, tkn_mkr, root, endTag, Xop_xnde_tkn.CloseMode_inline, bgn_pos, cur_pos, Int_.MinValue, Int_.MinValue, null);	// NOTE: atrs is null b/c </br> will never have atrs
				return cur_pos;
			case Xop_xnde_tag_.EndNdeMode_escape:	// handle </hr>
				ctx.Lxr_make_(false);
				ctx.Msg_log().Add_itm_none(Xop_xnde_log.Escaped_xnde, src, bgn_pos, cur_pos - 1);
				return cur_pos;
		}
		if (acs_pos != -1) {	// something found
			if		(bgnTagId == endTagId) { 						// endNde matches bgnNde; normal
				EndTag(ctx, root, bgnNde, src, src_len, bgn_pos, cur_pos, endTagId, true, endTag);
				return cur_pos;
			}
			if (TagStack_has(ctx, endTagId)) {	// endTag has bgnTag somewhere in stack;					
				int end = ctx.Stack_len() - 1;
				for (int i = end; i > -1; i--) {	// iterate stack and close all nodes until bgnNde that matches endNde
					Xop_tkn_itm tkn = ctx.Stack_get(i);
					if (tkn.Tkn_tid() == Xop_tkn_itm_.Tid_xnde) {
						Xop_xnde_tkn xnde_tkn = (Xop_xnde_tkn)tkn;
						EndTag(ctx, root, xnde_tkn, src, src_len, bgn_pos, bgn_pos, xnde_tkn.Tag().Id(), false, endTag);
						ctx.Stack_pop_idx(i);
						if (xnde_tkn.Tag().Id() == endTagId) {
							xnde_tkn.Src_end_(cur_pos);
							return cur_pos;
						}
						else
							ctx.Msg_log().Add_itm_none(Xop_xnde_log.Auto_closing_section, src, bgnNde.Src_bgn(), bgnNde.Name_end());
					}
					else
						ctx.Stack_autoClose(tkn, bgn_pos, cur_pos);
				}
			}
		}
		ctx.Subs_add(tkn_mkr.Ignore(bgn_pos, cur_pos, Xop_ignore_tkn.Ignore_tid_xnde_dangling));
		ctx.Para().Process_xml_block(endTag.Block_close(), cur_pos);

		ctx.Msg_log().Add_itm_none(Xop_xnde_log.Escaped_xnde, src, bgn_pos, cur_pos - 1);
		return cur_pos;
	}
	void EndTag(Xop_ctx ctx, Xop_root_tkn root, Xop_xnde_tkn bgn_nde, byte[] src, int src_len, int bgn_pos, int cur_pos, int tagId, boolean pop, Xop_xnde_tag endTag) {
		bgn_nde.Src_end_(cur_pos);
		bgn_nde.CloseMode_(Xop_xnde_tkn.CloseMode_pair);
		bgn_nde.Tag_close_rng_(bgn_pos, cur_pos);
		if (pop)
			ctx.Stack_pop_til(ctx.Stack_idx_typ(Xop_tkn_itm_.Tid_xnde), false, cur_pos, cur_pos);
		bgn_nde.Subs_move(root);	// NOTE: Subs_move must go after Stack_pop_til, b/c Stack_pop_til adds tkns; see Xnde_td_list
		ctx.Para().Process_xml_block(endTag.Block_close(), cur_pos);
	}
	Xop_xnde_tkn Xnde_bgn(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, Xop_xnde_tag tag, byte closeMode, int bgn_pos, int cur_pos, int atrs_bgn, int atrs_end, Xop_xatr_itm[] atrs) {
		Xop_xnde_tkn xnde = tkn_mkr.Xnde(bgn_pos, cur_pos).CloseMode_(closeMode);
		int xndeBgn = bgn_pos + 1;
		xnde.Name_rng_(xndeBgn, xndeBgn + tag.NameLen());
		xnde.Tag_(tag);
		xnde.Tag_open_rng_(bgn_pos, cur_pos);
		if (atrs_bgn > 0) {
			xnde.Atrs_rng_(atrs_bgn, atrs_end);
			xnde.Atrs_ary_(atrs);
		}
		ctx.Subs_add(xnde);
		ctx.Para().Process_xml_block(tag.Block_open(), cur_pos);
		return xnde;
	}
	int Find_endTag_pos(int find_bgn) {
		int rv = find_bgn;
		boolean found = false, loop = true;
		while (loop) {
			if (rv == src_len) break;
			byte b = src[rv];
			switch (b) {
				case Byte_ascii.Space:
				case Byte_ascii.NewLine:
				case Byte_ascii.Tab:
					++rv;
					break;
				case Byte_ascii.Gt:
					found = true;
					loop = false;
					++rv; // add 1 to position after >
					break;
				default:
					loop = false;
					break;
			}
		}
		return found ? rv : ByteAry_.NotFound;
	}
	int Make_xnde_xtn(Xop_xnde_tag tag, int open_bgn, int open_end, int atrs_bgn, int atrs_end, Xop_xatr_itm[] atrs, boolean inline) {
		// NOTE: find endTag that exactly matches bgnTag; must be case sensitive;
		int xnde_end = open_end;
		Xop_xnde_tkn xnde = null;
		if (inline) {
			xnde = Xnde_bgn(ctx, tkn_mkr, root, tag, Xop_xnde_tkn.CloseMode_inline, open_bgn, open_end, atrs_bgn, atrs_end, atrs);
			xnde.Tag_close_rng_(open_end, open_end);	// NOTE: inline tag, so set TagClose to open_end; should noop
		}
		else {
			byte[] close_ary = tag.XtnEndTag_tmp();			// get tmp bry (so as not to new)
			int close_ary_len = close_ary.length;
			int src_offset = open_bgn - 1;					// open bgn to start at <; -2 to ignore </ ; +1 to include <
			for (int i = 2; i < close_ary_len; i++)			// 2 to ignore </
				close_ary[i] = src[src_offset + i];

			int close_bgn = ByteAry_.FindFwd(src, close_ary, open_end);
			if (close_bgn == ByteAry_.NotFound) {// NOTE: end_tag not found; try again, but with case_insensitive; EX: w:Ehrenfest_paradox
				xtn_end_tag_trie.Clear();
				xtn_end_tag_trie.Add(close_ary, close_ary);
				for (int i = open_end; i < src_len; i++) {
					Object o = xtn_end_tag_trie.MatchAtCur(src, i, src_len);
					if (o != null) {
						close_bgn = i;
						break;
					}
				}
				if (close_bgn == ByteAry_.NotFound)
					return ctx.LxrMake_log_(Xop_xnde_log.Xtn_end_not_found, open_bgn, open_end);
			}
			int close_end = Find_endTag_pos(close_bgn + close_ary.length);
			if (close_end == ByteAry_.NotFound) return ctx.LxrMake_log_(Xop_xnde_log.Xtn_end_not_found, open_bgn, open_end);
			xnde_end = close_end;

			xnde = New_xnde_pair(tag, open_bgn, open_end, close_bgn, close_end);
			xnde.Atrs_rng_(atrs_bgn, atrs_end);
			xnde.Atrs_ary_(atrs);
			if (close_bgn - open_end > 0)
				xnde.Subs_add(tkn_mkr.Txt(open_end, close_bgn));
		}
		if (tag.Id() == Xop_xnde_tag_.Tid_pre)				// NOTE: if pre is already in effect, end it; EX: b:Knowing Knoppix/Other applications
			ctx.Para().Cur_mode_end();
		else {
			switch (tag.Id()) {
				case Xop_xnde_tag_.Tid_gallery:				// NOTE: must cancel for gallery; EX.WP: Mary, Queen of Scots; " <gallery>"; 
					ctx.Para().Pre_disable();
					break;
				case Xop_xnde_tag_.Tid_nowiki:				// NOTE: nowiki should not affect pre (not sure); DATE:2013-01-21
					break;
			}
		}
		switch (ctx.Parse_tid()) {
			case Xop_parser_.Parse_tid_page_tmpl: {
				Xop_xnde_xtn xnde_data = null;
				switch (tag.Id()) {
					case Xop_xnde_tag_.Tid_xowa_cmd:				xnde_data = tkn_mkr.Xowa_cmd_nde(); break;
				}
				if (xnde_data != null) { 
					xnde_data.Xtn_compile(ctx, ctx.Wiki(), tkn_mkr, src, xnde);
					xnde.Xnde_data_(xnde_data);
				}
				break;
			}
			case Xop_parser_.Parse_tid_page_wiki: {
				Xop_xnde_xtn xnde_data = null;
				switch (tag.Id()) {
					case Xop_xnde_tag_.Tid_xowa_cmd:				xnde_data = tkn_mkr.Xowa_cmd_nde(); break;
					case Xop_xnde_tag_.Tid_poem:					xnde_data = tkn_mkr.Poem_nde(); break;
//						case Xop_xnde_tag_.Tid_nowiki:					xnde_data = new Xtn_nowiki_nde(); break;
					case Xop_xnde_tag_.Tid_math:					if (file_wkr != null) file_wkr.Wkr_run(ctx, xnde); break;
					case Xop_xnde_tag_.Tid_ref:						xnde_data = tkn_mkr.Ref_nde(); break;
					case Xop_xnde_tag_.Tid_references:				xnde_data = tkn_mkr.References_nde(); break;
					case Xop_xnde_tag_.Tid_gallery:					xnde_data = tkn_mkr.Gallery_nde(); break;
					case Xop_xnde_tag_.Tid_imageMap:				xnde_data = tkn_mkr.ImageMap_nde(); break;
					case Xop_xnde_tag_.Tid_hiero:					xnde_data = tkn_mkr.Hiero_nde(); break;
					case Xop_xnde_tag_.Tid_inputBox:				xnde_data = tkn_mkr.Inputbox_nde(); break;
					case Xop_xnde_tag_.Tid_pages:					xnde_data = tkn_mkr.Pages_nde(); break;
					case Xop_xnde_tag_.Tid_section:					xnde_data = tkn_mkr.Section_nde(); break;
					case Xop_xnde_tag_.Tid_categoryList:			xnde_data = tkn_mkr.CategoryList_nde(); break;
					case Xop_xnde_tag_.Tid_dynamicPageList:			xnde_data = tkn_mkr.DynamicPageList_nde(); break;
					case Xop_xnde_tag_.Tid_syntaxHighlight:			xnde_data = tkn_mkr.SyntaxHighlight(); break;
					case Xop_xnde_tag_.Tid_score:					xnde_data = tkn_mkr.Score_nde(); break;
					case Xop_xnde_tag_.Tid_translate:				xnde_data = tkn_mkr.Translate(); break;
					case Xop_xnde_tag_.Tid_languages:				xnde_data = tkn_mkr.Languages(); break;
				}
				if (xnde_data != null) { 
					xnde_data.Xtn_compile(ctx, ctx.Wiki(), tkn_mkr, src, xnde);
					xnde.Xnde_data_(xnde_data);
				}
				break;
			}
		}
		return xnde_end;
	}	private ByteTrieMgr_slim xtn_end_tag_trie = ByteTrieMgr_slim.ci_();
	Xop_xnde_tkn New_xnde_pair(Xop_xnde_tag tag, int open_bgn, int open_end, int close_bgn, int close_end) {
		Xop_xnde_tkn rv = tkn_mkr.Xnde(open_bgn, close_end).Tag_(tag).Tag_open_rng_(open_bgn, open_end).Tag_close_rng_(close_bgn, close_end).CloseMode_(Xop_xnde_tkn.CloseMode_pair);
		int name_bgn = open_bgn + 1;
		rv.Name_rng_(name_bgn, name_bgn + tag.NameLen());
		ctx.Subs_add(rv);
		return rv;
	}
//		public static ByteAryBfr TEMP_TIMELINE = ByteAryBfr.new_();
	public static final byte[] Bry_escape_lt = ByteAry_.new_ascii_("&lt;"), Bry_escape_gt = ByteAry_.new_ascii_("&gt;"), Bry_escape_quote = ByteAry_.new_ascii_("&quot;"), Bry_escape_amp = ByteAry_.new_ascii_("&amp;"), Bry_escape_brack_bgn = ByteAry_.new_ascii_("&#91;"), Bry_apos = new byte[] {Byte_ascii.Apos};
	public static final byte[] Bry_lt = new byte[] {Byte_ascii.Lt}, Bry_brack_bgn = new byte[] {Byte_ascii.Brack_bgn}, Bry_pipe = new byte[] {Byte_ascii.Pipe};
	public static int Find_gt_pos(Xop_ctx ctx, byte[] src, int cur_pos, int src_len) {	// UNUSED
		int gt_pos = -1;	// find closing >
		for (int i = cur_pos; i < src_len; i++) {
			byte b = src[i];
			switch (b) {
				case Byte_ascii.Lt:	// < encountered; may be inner node inside tag which is legal in wikitext; EX: "<ul style=<nowiki>#</nowiki>FFFFFF>"
					int valid_inner_xnde_gt = ctx.App().Xatr_parser().Xnde_find_gt_find(src, i + 1, src_len);
					if (valid_inner_xnde_gt != String_.NotFound) {
						i = valid_inner_xnde_gt;
					}
					break;
				case Byte_ascii.Gt:
					gt_pos = i;
					i = src_len;
					break;
			}
		}
		return gt_pos;
	}
}
//	class Xtn_nowiki_nde : Xop_xnde_xtn {
//		public Xop_root_tkn Xtn_root() {return null;}
//		public byte[] Swap() {return swap;} private byte[] swap;
//		public void Xtn_compile(Xop_ctx ctx, Xow_wiki wiki, Xop_tkn_mkr tkn_mkr, byte[] src, Xop_xnde_tkn xnde) {
//			swap = ByteAry_.Replace(ByteAry_.Mid(src, xnde.Tag_open_end(), xnde.Tag_close_bgn()), ByteAry_.new_ascii_("<"), ByteAry_.new_ascii_("&lt;"));
//		}
//	}
/*
NOTE_1: special logic for <*include*>
cannot process like regular xnde tag b/c cannot auto-close tags on tmpl
  EX: <includeonly>{{subst:</includeonly><includeonly>substcheck}}</includeonly>
      1st </io> would autoclose {{subst:
Since the basic intent is to "hide" the tags in certain modes, then basically create ignore_tkn and exit
*/