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
package gplx.xowa; import gplx.*;
class Xop_nl_lxr implements Xop_lxr {
	public byte Lxr_tid() {return Xop_lxr_.Tid_nl;}
	public void Init_by_wiki(Xow_wiki wiki, ByteTrieMgr_fast core_trie) {core_trie.Add(Byte_ascii.NewLine, this);}
	public void Init_by_lang(Xol_lang lang, ByteTrieMgr_fast core_trie) {}
	public int Make_tkn(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int src_len, int bgn_pos, int cur_pos) {
		if (bgn_pos == Xop_parser_.Doc_bgn_bos) return ctx.LxrMake_txt_(cur_pos); // simulated nl at beginning of every parse
		int trim_category_pos = Trim_category(ctx, src, cur_pos, src_len);
		if (trim_category_pos != -1) return trim_category_pos;
		Xop_tkn_itm last_tkn = ctx.Stack_get_last();		// BLOCK:invalid_ttl_check
		if (	!ctx.Tid_is_image_map()
			&&	last_tkn != null
			&&	last_tkn.Tkn_tid() == Xop_tkn_itm_.Tid_lnki) {
			Xop_lnki_tkn lnki = (Xop_lnki_tkn)last_tkn;
			if (	lnki.Pipe_count_is_zero()) {	// always invalid
				ctx.Stack_pop_last();
				return Xop_lnki_wkr_.Invalidate_lnki(ctx, src, root, lnki, bgn_pos);
			}
		}

		ctx.Apos().EndFrame(ctx, root, src, bgn_pos, true);	// NOTE: frame should at end at bgn_pos (before \n) not after; else, will create tkn at (5,5), while tkn_mkr.Space creates one at (4,5); DATE:2013-10-31
		ctx.Tblw().Cell_pipe_seen_(false);	// flip off "|" in tblw seq; EX: "| a\n||" needs to flip off "|" else "||" will be seen as style dlm"; NOTE: not covered by test?

		Xop_para_wkr para_wkr = ctx.Para();
		switch (ctx.Cur_tkn_tid()) {
			case Xop_tkn_itm_.Tid_hdr:		// last tkn was hdr; close it; EX: \n==a==\nb; "\n" should close 2nd "=="; DATE:2014-02-17
				int acs_pos = ctx.Stack_idx_typ(Xop_tkn_itm_.Tid_hdr);
				ctx.Stack_pop_til(root, src, acs_pos, true, bgn_pos, cur_pos);
				para_wkr.Process_block__bgn_n__end_y(Xop_xnde_tag_.Tag_h2);
				break;
			case Xop_tkn_itm_.Tid_list:		// close list
				Xop_list_wkr_.Close_list_if_present(ctx, root, src, bgn_pos, cur_pos);
				para_wkr.Process_block__bgn_n__end_y(Xop_xnde_tag_.Tag_li);
				break;
			case Xop_tkn_itm_.Tid_lnke:		// close lnke
				if (ctx.Stack_idx_typ(Xop_tkn_itm_.Tid_tmpl_invk) == -1) // only close if no tmpl; MWR: [[SHA-2]]; * {{cite journal|title=Proposed 
					ctx.Stack_pop_til(root, src, ctx.Stack_idx_typ(Xop_tkn_itm_.Tid_lnke), true, bgn_pos, cur_pos);
				break;
			case Xop_tkn_itm_.Tid_lnki:		// NOTE: \n in caption or other multipart lnki; don't call para_wkr.Process
				Xop_tkn_itm nl_tkn = tkn_mkr.Space(root, bgn_pos, cur_pos);	// convert \n to \s. may result in multiple \s, but rely on htmlViewer to suppress; EX: w:Schwarzschild_radius; and the stellar [[Velocity dispersion|velocity\ndispersion]];
				ctx.Subs_add(root, nl_tkn);
				return cur_pos;
			// case Xop_tkn_itm_.Tid_tblw_tc: case Xop_tkn_itm_.Tid_tblw_td:	// STUB: tc/td should not have attributes
			case Xop_tkn_itm_.Tid_tblw_tb: case Xop_tkn_itm_.Tid_tblw_tr: case Xop_tkn_itm_.Tid_tblw_th:	// nl should close previous tblw's atrs range; EX {{Infobox planet}} and |-\n<tr>
				Xop_tblw_wkr.Atrs_close(ctx, src, root);
				break;
		}
		if (	ctx.Parse_tid() == Xop_parser_.Parse_tid_page_wiki			// parse_mode is wiki
			&&	para_wkr.Enabled()											// check that para is enabled
			)
			para_wkr.Process_nl(ctx, root, src, bgn_pos, cur_pos);
		else {																// parse mode is tmpl, or para is disabled; for latter, adding \n for pretty-print
			Xop_nl_tkn nl_tkn = tkn_mkr.NewLine(bgn_pos, cur_pos, Xop_nl_tkn.Tid_char, 1);
			ctx.Subs_add(root, nl_tkn);
		}
		return cur_pos;
	}
	private static int Trim_category(Xop_ctx ctx, byte[] src, int cur_pos, int src_len) {
		for (int i = cur_pos; i < src_len; i++) {
			byte b = src[i];
			switch (b) {
				case Byte_ascii.Space: case Byte_ascii.Tab: case Byte_ascii.NewLine: case Byte_ascii.CarriageReturn:	// ignore ws
					break;
				case Byte_ascii.Brack_bgn: // [
					if (	ByteAry_.Eq_itm(src, src_len, i + 1, Byte_ascii.Brack_bgn)	// [[
						&&	i + 2 < src_len) {	
						int ttl_bgn = Byte_ary_finder.Find_fwd_while(src, i + 2, src_len, Byte_ascii.Space);
						ByteTrieMgr_slim ctg_trie = ctx.Wiki().Ns_mgr().Category_trie();
						Object ctg_ns = ctg_trie.MatchAtCur(src, ttl_bgn, src_len);
						if (ctg_ns != null	// "[[Category" found
							&& ByteAry_.Eq_itm(src, src_len, ctg_trie.Match_pos(), Byte_ascii.Colon)) {	// check that next char is :
							return i;// return pos of 1st [
						}
						return -1;
					}
					break;
				default:	// non-ws; return not found
					return -1;
			}
		}
		return -1;
	}
	public static final Xop_nl_lxr _ = new Xop_nl_lxr(); Xop_nl_lxr() {}
}
