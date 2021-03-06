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
class Xop_comm_lxr implements Xop_lxr {
	public byte Lxr_tid() {return Xop_lxr_.Tid_comment;}
	public void Init_by_wiki(Xow_wiki wiki, ByteTrieMgr_fast core_trie) {core_trie.Add(Bgn_ary, this);}
	public void Init_by_lang(Xol_lang lang, ByteTrieMgr_fast core_trie) {}
	public int Make_tkn(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int src_len, int bgn_pos, int cur_pos) {
		cur_pos = Byte_ary_finder.Find_fwd(src, End_ary, cur_pos, src_len);		// search for "-->"
		if (cur_pos == Byte_ary_finder.Not_found) {								// "-->" not found
			ctx.Msg_log().Add_itm_none(Xop_comment_log.Eos, src, bgn_pos, cur_pos);
			cur_pos = src_len;													// gobble up rest of content
		}
		else
			cur_pos += End_len;
		cur_pos = Trim_ws_if_entire_line_is_commment(ctx, tkn_mkr, root, src, src_len, cur_pos);
		ctx.Subs_add(root, tkn_mkr.Ignore(bgn_pos, cur_pos, Xop_ignore_tkn.Ignore_tid_comment));
		return cur_pos;
	}
	private static int Trim_ws_if_entire_line_is_commment(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int src_len, int cur_pos) {// REF.MW:Preprocessor_DOM.php|preprocessToXml|handle comments; DATE:2014-02-24
		int nl_lhs = -1;
		int subs_len = root.Subs_len();
		for (int i = subs_len - 1; i > -1; i--) {			// look bwd for "\n"
			Xop_tkn_itm sub = root.Subs_get(i);
			switch (sub.Tkn_tid()) {
				case Xop_tkn_itm_.Tid_space: case Xop_tkn_itm_.Tid_tab:
					break;
				case Xop_tkn_itm_.Tid_ignore:
					Xop_ignore_tkn sub_as_ignore = (Xop_ignore_tkn)sub;
					if (sub_as_ignore.Ignore_type() != Xop_ignore_tkn.Ignore_tid_comment)
						i = -1;
					break;
				case Xop_tkn_itm_.Tid_newLine:				// new_line found; anything afterwards is a \s or a \t; SEE.WIKT:coincidence
					nl_lhs = i;
					break;
				default:
					i = -1;
					break;
			}
		}
		if (nl_lhs == -1) return cur_pos;					// non ws tkns found before \n; exit now; EX: \n\sa<!--
		boolean loop = true;
		int nl_rhs = -1, loop_pos = cur_pos;
		while (loop) {										// look fwd for \n
			if (loop_pos == src_len) break;
			switch (src[loop_pos++]) {
				case Byte_ascii.Space:
				case Byte_ascii.Tab:
					break;
				case Byte_ascii.NewLine:
					loop = false;
					nl_rhs = loop_pos;
					break;
				default:
					loop = false;
					break;
			}
		}
		if (nl_rhs == -1) return cur_pos;					// non ws tkns found before \n; exit now; EX: -->a\n
		for (int i = nl_lhs + 1; i < subs_len; i++) {		// entire line is ws; trim everything from nl_lhs + 1 to nl_rhs; do not trim nl_lhs
			Xop_tkn_itm sub_tkn = root.Subs_get(i);
			sub_tkn.Ignore_y_grp_(ctx, root, i);
		}
		ctx.Subs_add(root, tkn_mkr.NewLine(nl_rhs - 1, nl_rhs, Xop_nl_tkn.Tid_char, 1).Ignore_y_()); // add tkn for nl_rhs, but mark as ignore; needed for multiple comment nls; EX: "<!-- -->\n<!-- -->\n;"; DATE:2014-02-24
		return nl_rhs;
	}
	public static final byte[] Bgn_ary = new byte[] {60, 33, 45, 45}, /*<!--*/ End_ary = new byte[] {45, 45, 62}; /*-->*/
	private static final int End_len = End_ary.length;
	public static final Xop_comm_lxr _ = new Xop_comm_lxr(); Xop_comm_lxr() {}

}
