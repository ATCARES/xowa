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
package gplx.xowa.bldrs.imports.ctgs; import gplx.*; import gplx.xowa.*; import gplx.xowa.bldrs.*; import gplx.xowa.bldrs.imports.*;
import org.junit.*;
public class Uca_trie_tst {		
	@Before public void init() {fxt.Clear();} private Xobc_base_fxt fxt = new Xobc_base_fxt();
	@Test  public void Basic() {
		Uca_trie_fxt fxt = new Uca_trie_fxt();
		fxt.Clear();
		fxt.Init_trie_itm("a", ByteAry_.ints_(10, 11));
		fxt.Init_trie_itm("b", ByteAry_.ints_(20, 21));
		fxt.Init_trie_itm("c", ByteAry_.ints_(30, 31));
		fxt.Test_decode(ByteAry_.ints_(10, 11), "a");
		fxt.Test_decode(ByteAry_.ints_(10, 11, 20, 21, 30, 31), "abc");
	}
}
class Uca_trie_fxt {
	public void Clear() {
		if (trie == null) {
			trie = new Uca_trie();
			bfr = ByteAryBfr.new_();
		}
		trie.Clear();
	}	Uca_trie trie; ByteAryBfr bfr;
	public void Init_trie_itm(String charAsStr, byte[] uca) {trie.Init_itm(gplx.intl.Utf16_.Decode_to_int(ByteAry_.new_utf8_(charAsStr), 0), uca);}
	public void Test_decode(byte[] bry, String expd) {
		trie.Decode(bfr, bry, 0, bry.length);
		Tfds.Eq(expd, bfr.XtoStrAndClear());
	}
}
