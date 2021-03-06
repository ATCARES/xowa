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
import org.junit.*;
import gplx.xowa.langs.casings.*;
public class Xop_lnki_wkr_uncommon_tst {		
	@Before public void init() {fxt.Reset(); fxt.Init_para_n_();} private Xop_fxt fxt = new Xop_fxt();
	@Test  public void Double_bracket() {	// PURPOSE: handle [[[[A]]]] constructions; EX:ru.w:Италия; DATE:2014-02-04
		fxt.Test_parse_page_all_str("[[[[Test_1]]]]"		, "[[<a href=\"/wiki/Test_1\">Test_1</a>]]");
	}
}

