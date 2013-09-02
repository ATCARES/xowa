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
public class Xoa_css_img_downloader_tst {		
	@Before public void init() {fxt.Clear();} private Xoa_css_img_downloader_fxt fxt = new Xoa_css_img_downloader_fxt();
	@Test  public void Basic() {
		fxt.Test_css_convert
		(	"x {url(\"//site/a.jpg\")} y {url(\"//site/b.jpg\")}"
		, 	"x {url(\"site/a.jpg\")} y {url(\"site/b.jpg\")}"
		,	"site/a.jpg"
		,	"site/b.jpg"
		);
	}
	@Test  public void Unquoted() {
		fxt.Test_css_convert
		(	"x {url(//site/a.jpg)}"
		, 	"x {url(\"site/a.jpg\")}"
		,	"site/a.jpg"
		);
	}
	@Test  public void Http() {
		fxt.Test_css_convert
		(	"x {url(http://site/a.jpg)}"
		, 	"x {url(\"site/a.jpg\")}"
		,	"site/a.jpg"
		);
	}
	@Test  public void Base64() {
		fxt.Test_css_convert
		(	"x {url(\"//site/a.jpg\")} y {url(\"data:image/png;base64,BASE64DATA;ABC=\")} z {}"
		, 	"x {url(\"site/a.jpg\")} y {url(\"data:image/png;base64,BASE64DATA;ABC=\")} z {}"
		,	"site/a.jpg"
		);
	}
	@Test  public void Exc_missing_quote() {
		fxt.Test_css_convert
		(	"x {url(\"//site/a.jpg\")} y {url(\"//site/b.jpg} z {}"
		, 	"x {url(\"site/a.jpg\")} y {url(\"//site/b.jpg} z {}"
		,	"site/a.jpg"
		);
	}
	@Test  public void Exc_empty() {
		fxt.Test_css_convert
		(	"x {url(\"//site/a.jpg\")} y {url(\"\"} z {}"
		, 	"x {url(\"site/a.jpg\")} y {url(\"\"} z {}"
		,	"site/a.jpg"
		);
	}
	@Test  public void Exc_name_only() {
		fxt.Test_css_convert
		(	"x {url(\"//site/a.jpg\")} y {url(\"b.jpg\"} z {}"
		, 	"x {url(\"site/a.jpg\")} y {url(\"b.jpg\"} z {}"
		,	"site/a.jpg"
		);
	}
	@Test  public void Repeat() {// PURPOSE.fix: exact same item was being added literally
		fxt.Test_css_convert
		(	"x {url(\"//site/a.jpg?a=b\")} y {url(\"//site/a.jpg?a=b\"}"
		, 	"x {url(\"site/a.jpg\")} y {url(\"site/a.jpg\"}"
		,	"site/a.jpg"
		);
	}
	@Test  public void Clean_basic() 				{fxt.Test_clean_img_url("//site/a.jpg"							, "site/a.jpg");}
	@Test  public void Clean_query() 				{fxt.Test_clean_img_url("//site/a.jpg?key=val"					, "site/a.jpg");}
	@Test  public void Clean_dir() 					{fxt.Test_clean_img_url("//site/a/b/c.jpg?key=val"				, "site/a/b/c.jpg");}
	@Test  public void Clean_exc_site_only() 		{fxt.Test_clean_img_url("//site"								, null);}
	@Test  public void Clean_exc_site_only_2() 		{fxt.Test_clean_img_url("//site/"								, null);}
	@Test  public void Import_url() {
		Io_mgr._.InitEngine_mem();
		Io_mgr._.SaveFilStr("mem/www/b.css", "imported_css");
		fxt.Test_css_convert
		(	"x @import url(\"mem/www/b.css\") screen; z"
		, 	String_.Concat_lines_nl
		(	"x "
		,	"/*XOWA:mem/www/b.css*/"
		,	"imported_css"
		,	""
		,	" z"
		)
		);
	}
	@Test  public void Import_url_relative() {
		Io_mgr._.InitEngine_mem();
		Io_mgr._.SaveFilStr("mem/en.wikipedia.org/www/b.css", "imported_css");
		fxt.Test_css_convert
		(	"x @import url(\"/www/b.css\") screen; z"
		, 	String_.Concat_lines_nl
		(	"x "
		,	"/*XOWA:mem/en.wikipedia.org/www/b.css*/"
		,	"imported_css"
		,	""
		,	" z"
		)
		);
	}
}
class Xoa_css_img_downloader_fxt {
	public void Clear() {
		downloader = new Xoa_css_img_downloader();
		downloader.Ctor(Gfo_usr_dlg_xowa.test_(), new Xof_download_wkr_test(), ByteAry_.Empty);
	}	Xoa_css_img_downloader downloader;
	public void Test_css_convert(String raw, String expd, String... expd_img_ary) {
		ListAdp actl_img_list = ListAdp_.new_();
		byte[] actl_bry = downloader.Convert_to_local_urls(ByteAry_.new_ascii_("mem/en.wikipedia.org"), ByteAry_.new_utf8_(raw), actl_img_list);
		Tfds.Eq_str_lines(expd, String_.new_utf8_(actl_bry));
		Tfds.Eq_ary_str(expd_img_ary, actl_img_list.XtoStrAry());
	}
	public void Test_clean_img_url(String raw_str, String expd) {
		byte[] raw = ByteAry_.new_ascii_(raw_str);
		byte[] actl = downloader.Clean_img_url(raw, raw.length);
		Tfds.Eq(expd, actl == null ? null : String_.new_ascii_(actl));
	}
}
