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
package gplx;
import org.junit.*;
public class ByteTrieMgr_slim_tst {
	@Before public void init() {
	}	private ByteTrieMgr_slim trie;
	private void ini_setup1() {
		trie = ByteTrieMgr_slim.cs_();
		run_Add("a"		,	1);
		run_Add("abc"	,	123);
	}
	@Test  public void Fetch() {
		ini_setup1();
		tst_MatchAtCur("a"		, 1);
		tst_MatchAtCur("abc"	, 123);
		tst_MatchAtCur("ab"		, 1);
		tst_MatchAtCur("abcde"	, 123);
		tst_MatchAtCur(" a"		, null);
	}
	@Test  public void Bos() {
		ini_setup1();
		tst_Match("bc", Byte_ascii.Ltr_a, -1, 123);
	}
	@Test  public void MatchAtCurExact() {
		ini_setup1();
		tst_MatchAtCurExact("a", 1);
		tst_MatchAtCurExact("ab", null);
		tst_MatchAtCurExact("abc", 123);
	}
	private void ini_setup2() {
		trie = ByteTrieMgr_slim.cs_();
		run_Add("a"	,	1);
		run_Add("b"	,	2);
	}
	@Test  public void Match_2() {
		ini_setup2();
		tst_MatchAtCur("a", 1);
		tst_MatchAtCur("b", 2);
	}
	private void ini_setup_caseAny() {
		trie = ByteTrieMgr_slim.ci_();
		run_Add("a"	,	1);
		run_Add("b"	,	2);
	}
	@Test  public void CaseAny() {
		ini_setup_caseAny();
		tst_MatchAtCur("a", 1);
		tst_MatchAtCur("A", 1);
	}
	@Test  public void Del() {
		ini_setup1();
		trie.Del(ByteAry_.new_ascii_("a"));	// delete "a"; "abc" still remains;
		tst_MatchAtCur("a"		, null);
		tst_MatchAtCur("abc"	, 123);

		trie.Del(ByteAry_.new_ascii_("abc"));
		tst_MatchAtCur("abc"	, null);
	}

	private void run_Add(String k, int val) {trie.Add(ByteAry_.new_ascii_(k), val);}
	private void tst_Match(String srcStr, byte b, int bgn_pos, int expd) {
		byte[] src = ByteAry_.new_ascii_(srcStr);
		Object actl = trie.Match(b, src, bgn_pos, src.length);
		Tfds.Eq(expd, actl);
	}
	private void tst_MatchAtCur(String srcStr, Object expd) {
		byte[] src = ByteAry_.new_ascii_(srcStr);
		Object actl = trie.Match(src[0], src, 0, src.length);
		Tfds.Eq(expd, actl);
	}
	private void tst_MatchAtCurExact(String srcStr, Object expd) {
		byte[] src = ByteAry_.new_ascii_(srcStr);
		Object actl = trie.MatchAtCurExact(src, 0, src.length);
		Tfds.Eq(expd, actl);
	}
}
