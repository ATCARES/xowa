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
public class Xop_tab_tkn extends Xop_tkn_itm_base {
	public Xop_tab_tkn(int bgn, int end) {this.Tkn_ini_pos(false, bgn, end);}
	@Override public byte Tkn_tid() {return Xop_tkn_itm_.Tid_tab;}
	public static final byte[] Bry_tab_ent = ByteAry_.new_ascii_("&#09;");
}
class Xop_tab_lxr implements Xop_lxr {
	public byte Lxr_tid() {return Xop_lxr_.Tid_tab;}
	public void Init_by_wiki(Xow_wiki wiki, ByteTrieMgr_fast core_trie) {
		core_trie.Add(Byte_ascii.Tab, this);
		core_trie.Add(Xop_tab_tkn.Bry_tab_ent, this);
	}	
	public void Init_by_lang(Xol_lang lang, ByteTrieMgr_fast core_trie) {}
	public int Make_tkn(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int src_len, int bgn_pos, int cur_pos) {
		cur_pos = Byte_ary_finder.Find_fwd_while(src, cur_pos, src_len, Byte_ascii.Tab);
		src[bgn_pos] = Byte_ascii.Tab; // HACK: SEE:NOTE_1:tabs
		for (int i = bgn_pos + 1; i < cur_pos; i++)	
			src[i] = Byte_ascii.Space;
		ctx.Subs_add(root, tkn_mkr.Tab(bgn_pos, cur_pos));
		return cur_pos;
	}
	public static final Xop_tab_lxr _ = new Xop_tab_lxr();
}
/*
NOTE_1:tabs
. tabs exist in wikimedia source; note that tabs (\t) are not a meaningful HTML character
. xowa uses tabs for delimiters in its xowa files
. in order to maintain some semblance of fidelity, "\t" was replaced with &#09;
. unfortunately, "\t" is generally trimmed as whitespace throughout mediawiki; "&#09;" is not
. so, as a HACK, replace "&#09;" with "\t\s\s\s\s";
.. note that all 5 chars of "&#09;" must be replaced; hence "\t\s\s\s\s"
.. note that they all need to be ws in order to be trimmed out
.. note that shrinking the src[] would be (a) memory-expensive (b) complexity-expensive (many functions assume a static src size)
.. note that "\t\t\t\t\t" was the 1st attempt, but this resulted in exponential growth of "\t"s with each save (1 -> 5 -> 25 -> 125). "\t\s\s\s\s" is less worse with its linear growth (1 -> 5 -> 10)
. TODO: swap out the "&#09;" at point of file-read;
*/