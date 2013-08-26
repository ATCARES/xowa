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
public class Pf_intl_int_tst {
	Xop_fxt fxt = new Xop_fxt();
	@Before public void init() {fxt.Reset();}
	@Test  public void Month_en()				{fxt.tst_Parse_tmpl_str_test("{{int:january}}"								, "{{test}}"	, "January");}
	@Test  public void Month_en_upper()			{fxt.tst_Parse_tmpl_str_test("{{int:JANUARY}}"								, "{{test}}"	, "January");}
	@Test  public void Unknown()				{fxt.tst_Parse_tmpl_str_test("{{int:unknown_msg}}"							, "{{test}}"	, "<unknown_msg>");}
	@Test  public void Fmt()					{fxt.tst_Parse_tmpl_str_test("{{int:pfunc_expr_unrecognised_word|1a}}"		, "{{test}}"	, "Expression error: Unrecognised word \"1a\"");}
	@Test  public void Lang_specified()			{fxt.tst_Parse_tmpl_str_test("{{int:January/en}}"							, "{{test}}"	, "January");}
	@Test  public void Lang_specified_by_page() {
		Xol_lang en_lang = fxt.Wiki().Lang();
		Xol_lang fr_lang = fxt.App().Lang_mgr().Get_by_key_or_new(ByteAry_.new_ascii_("fr"));
		fr_lang.Load(fxt.App());
		fxt.Ctx().Page().Lang_(fr_lang); 
		fxt.tst_Parse_tmpl_str_test("{{int:Lang}}", "{{test}}"	, "fr");
		fxt.Ctx().Page().Lang_(en_lang);	// NOTE: must reset back to en_lang, else rest of tests will look up under fr
	}
	@Test  public void Tmpl_txt() {
		Xol_lang lang = fxt.Ctx().Page().Lang();
		byte[] tst_msg_key = ByteAry_.new_ascii_("tst_msg");
		Xol_msg_itm tst_msg_itm = lang.Msg_mgr().Itm_by_key_or_new(tst_msg_key);
		tst_msg_itm.Atrs_set(ByteAry_.new_ascii_("{{#expr:1}}"), false, true);
		fxt.tst_Parse_tmpl_str_test("{{int:tst_msg}}"							, "{{test}}"	, "1");
	}
	@Test  public void Mediawiki_precedes_gfs() {
		Xol_lang lang = fxt.Ctx().Page().Lang();
		byte[] tst_msg_key = ByteAry_.new_ascii_("precedes");
		Xol_msg_itm tst_msg_itm = lang.Msg_mgr().Itm_by_key_or_new(tst_msg_key);
		tst_msg_itm.Atrs_set(ByteAry_.new_ascii_("precedes_from_gfs"), true, false);
		fxt.Data_create("MediaWiki:precedes", "precedes_from_mw");
		fxt.tst_Parse_tmpl_str_test("{{int:precedes}}"							, "{{test}}"	, "precedes_from_mw");
	}
	@Test  public void Convert_php() {
		fxt.Data_create("MediaWiki:test_msg", "a\\\\b\\$c$1e");
		fxt.tst_Parse_tmpl_str_test("{{int:test_msg|d}}"						, "{{test}}"	, "a\\b$cde");
	}
	@Test  public void Err_fmt_failed() {
		Xol_lang lang = fxt.Ctx().Page().Lang();
		byte[] tst_msg_key = ByteAry_.new_ascii_("tst_msg");
		Xol_msg_itm tst_msg_itm = lang.Msg_mgr().Itm_by_key_or_new(tst_msg_key);
		tst_msg_itm.Atrs_set(ByteAry_.new_ascii_("a~{0}b"), true, false);
		fxt.tst_Parse_tmpl_str_test("{{int:tst_msg}}"							, "{{test}}"	, "a$1b");
	}
}
