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
class Xop_eq_lxr implements Xop_lxr {//20111222
	public Xop_eq_lxr(boolean tmpl_mode) {this.tmpl_mode = tmpl_mode;}	boolean tmpl_mode;
	public byte Lxr_tid() {return Xop_lxr_.Tid_eq;}
	public void Init_by_wiki(Xow_wiki wiki, ByteTrieMgr_fast core_trie) {core_trie.Add(Byte_ascii.Eq, this);}
	public void Init_by_lang(Xol_lang lang, ByteTrieMgr_fast core_trie) {}
	public int Make_tkn(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int src_len, int bgn_pos, int cur_pos) {
		if (tmpl_mode) {
			Xop_tkn_itm owner = ctx.Stack_get_last();									// beginning of "is == part of a hdr tkn sequence?"; DATE:2014-02-09
			if (owner != null && owner.Tkn_tid() == Xop_tkn_itm_.Tid_tmpl_curly_bgn) {	// inside curly
				if (	cur_pos < src_len												// bounds check
					&&	src[cur_pos] == Byte_ascii.Eq									// next char is =; i.e.: "=="
					) {
					int rhs = Byte_ary_finder.Find_fwd_while(src, cur_pos, src_len, Byte_ascii.Eq);	// find all consecutive "="
					int prv_pos = bgn_pos - 1;
					boolean hdr_like = false;
					if (prv_pos > -1 && src[prv_pos] == Byte_ascii.NewLine)				// is prv char \n; EX: "\n==="
						hdr_like = true;
					else {
						int nxt_pos = rhs + 1;
						if (	nxt_pos < src_len										// bounds check
							&&	src[nxt_pos] == Byte_ascii.NewLine						// is nxt char \n; EX: "===\n"
							)
							hdr_like = true;
					}
					if (hdr_like)														// ignore hdr tkn;
						return ctx.LxrMake_txt_(rhs);
				}
			}
			ctx.Subs_add(root, tkn_mkr.Eq(bgn_pos, cur_pos, 1));
			return cur_pos;
		}
		// wiki_mode; chk if hdr exists
		int stack_pos = ctx.Stack_idx_typ(Xop_tkn_itm_.Tid_hdr);
		if (stack_pos == Xop_ctx.Stack_not_found) {	// no hdr; make eq_tkn and return;
			ctx.Subs_add(root, tkn_mkr.Eq(bgn_pos, cur_pos, 1));
			return cur_pos;
		}
		// hdr exists; see if "=" begins hdr_end, or is just text; EX: "== a =ab\n"; NOTE: "==a==\s\t\n" is also a valid hdr
		int eq_len = 1, ws_bgn = -1; byte loop = State_loop;
		while (true) {
			if (cur_pos == src_len)			{loop = State_stop_eos; break;}
			byte b = src[cur_pos];
			switch (b) {
				case Byte_ascii.Eq:	
					if (ws_bgn == -1)		eq_len++;							// increase eq_len
					else					loop = State_stop_non_hdr;			// = after space; EX: "==a= ="
					break;
				case Byte_ascii.NewLine:	loop = State_stop_nl; break;
				case Byte_ascii.Tab:
				case Byte_ascii.Space:		if (ws_bgn == -1) ws_bgn = cur_pos; break;
				default:					loop = State_stop_non_hdr; break;
			}
			if (loop != State_loop) break;
			cur_pos++;
		}
		// = is for hdr; create hdr tkn
		switch (loop) {
			case State_stop_eos:
			case State_stop_nl:
				return ctx.Hdr().Make_tkn_end(ctx, tkn_mkr, root, src, src_len, bgn_pos, cur_pos, stack_pos, eq_len, ws_bgn);
		}
		// = is just text; create = tkn and any other ws tkns; NOTE: also create ws tkns if scanned; EX: "== a ===  bad"; create "===" and " "; position at "b"
		int end_pos = cur_pos;
		if (ws_bgn != -1) end_pos = ws_bgn;
		ctx.Subs_add(root, tkn_mkr.Eq(bgn_pos, end_pos, eq_len));
		if (ws_bgn != -1) ctx.Subs_add(root, tkn_mkr.Space(root, ws_bgn, cur_pos));
		return cur_pos;
	}	private static final byte State_loop = 0, State_stop_eos = 1, State_stop_nl = 2, State_stop_non_hdr = 3;
}
