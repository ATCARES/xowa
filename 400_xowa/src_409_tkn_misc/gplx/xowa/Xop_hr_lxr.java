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
public class Xop_hr_lxr implements Xop_lxr {
	public byte Lxr_tid() {return Xop_lxr_.Tid_hr;}
	public void Ctor_lxr(Xow_wiki wiki, ByteTrieMgr_fast parse_trie) {parse_trie.Add(Hook_ary, this);} static final byte[] Hook_ary = new byte[] {Byte_ascii.NewLine, Byte_ascii.Dash, Byte_ascii.Dash, Byte_ascii.Dash, Byte_ascii.Dash};
	public int MakeTkn(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int src_len, int bgn_pos, int cur_pos) {
		ctx.Para().Process_nl_sect_bgn(bgn_pos, cur_pos, Xop_nl_tkn.Tid_hr);
		int nl_adj = -1;	// -1 to ignore nl at bgn for hr_len
		if (bgn_pos == Xop_parser_.Doc_bgn_bos) {
			bgn_pos = 0;	// do not allow -1 pos
			nl_adj = 0;		// no nl at bgn, so nl_adj = 0
		}
		ctx.Apos().EndFrame(ctx, src, bgn_pos);
		ctx.CloseOpenItms(ctx, tkn_mkr, root, src, src_len, bgn_pos, cur_pos);		// close open items
		cur_pos = Xop_lxr_.Find_fwd_while(src, src_len, cur_pos, Hook_byt);			// get all consecutive dashes
		int hr_len = cur_pos - bgn_pos + nl_adj;
		ctx.Subs_add(tkn_mkr.Hr(bgn_pos, cur_pos, hr_len));
		if (hr_len > 4) ctx.Msg_log().Add_itm_none(Xop_hr_log.Len_5_or_more, src, bgn_pos, cur_pos);
		return cur_pos;
	}	private static final byte Hook_byt = Byte_ascii.Dash;
	public static final int Hr_len = 4;
	public static final Xop_hr_lxr _ = new Xop_hr_lxr(); Xop_hr_lxr() {}
}
