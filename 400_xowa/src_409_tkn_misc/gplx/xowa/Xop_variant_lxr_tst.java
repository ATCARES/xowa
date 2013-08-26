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
import org.junit.*;
public class Xop_variant_lxr_tst {
	@Test  public void Disabled() {
		Xop_fxt fxt = new Xop_fxt();
		fxt.tst_Parse_page_wiki_str("a-{b}-c", "a-{b}-c");
	}
	@Test  public void Enabled() {
		Xoa_app app = Xoa_app_fxt.app_();
		Xol_lang lang = new Xol_lang(app, ByteAry_.new_ascii_("zh"));
		lang.Variants_enabled_(true);
		Xow_wiki wiki = Xoa_app_fxt.wiki_(app, "zh.wikipedia.org", lang);
		Xop_fxt fxt = new Xop_fxt(app, wiki);
		fxt.tst_Parse_page_wiki_str("a-{b}-c", "ac");
	}
}