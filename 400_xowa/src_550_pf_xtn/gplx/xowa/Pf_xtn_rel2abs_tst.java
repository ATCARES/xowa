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
public class Pf_xtn_rel2abs_tst {
	private Xop_fxt fxt = new Xop_fxt();
	@Before public void init()					{fxt.Reset();}
	@Test   public void Slash_lvl3()			{fxt.Test_parse_tmpl_str_test("{{#rel2abs:/d|a/b/c}}"				, "{{test}}"			, "a/b/c/d");}
	@Test   public void Cur_lvl1()				{fxt.Test_parse_tmpl_str_test("{{#rel2abs:./d|a}}"					, "{{test}}"			, "a/d");}
	@Test   public void Cur_lvl2()				{fxt.Test_parse_tmpl_str_test("{{#rel2abs:./d|a/b}}"					, "{{test}}"			, "a/b/d");}
	@Test   public void Cur_lvl3()				{fxt.Test_parse_tmpl_str_test("{{#rel2abs:./d|a/b/c}}"				, "{{test}}"			, "a/b/c/d");}
	@Test   public void Owner_lvl3()			{fxt.Test_parse_tmpl_str_test("{{#rel2abs:../d|a/b/c}}"				, "{{test}}"			, "a/b/d");}
	@Test   public void Owner_cur_lvl3()		{fxt.Test_parse_tmpl_str_test("{{#rel2abs:../.|a/b/c}}"				, "{{test}}"			, "a/b");}
	@Test   public void Text_lvl3()				{fxt.Test_parse_tmpl_str_test("{{#rel2abs:d|a/b/c}}"					, "{{test}}"			, "d");}
	@Test   public void Slash_mult()			{fxt.Test_parse_tmpl_str_test("{{#rel2abs:/d//e|a/b/c}}"				, "{{test}}"			, "a/b/c/d/e");}
	@Test   public void Slash_cur_mult()		{fxt.Test_parse_tmpl_str_test("{{#rel2abs:/d/./e|a/b/c}}"			, "{{test}}"			, "a/b/c/d/e");}
	@Test   public void Qry_ends_w_slash()		{fxt.Test_parse_tmpl_str_test("{{#rel2abs:/d/|a/b/c}}"				, "{{test}}"			, "a/b/c/d");}
	@Test   public void Qry_is_empty()			{fxt.Test_parse_tmpl_str_test("{{#rel2abs:|a/b/c}}"					, "{{test}}"			, "a/b/c");}
	@Test   public void Qry_is_dot()			{fxt.Test_parse_tmpl_str_test("{{#rel2abs:.|a/b/c}}"					, "{{test}}"			, "a/b/c");}
	@Test   public void DotDot_mult2()			{fxt.Test_parse_tmpl_str_test("{{#rel2abs:/../..|a/b/c}}"			, "{{test}}"			, "a");}
	@Test   public void DotDot_mult3()			{fxt.Test_parse_tmpl_str_test("{{#rel2abs:/../../..|a/b/c}}"			, "{{test}}"			, "");}
	@Test   public void Src_is_empty()			{fxt.Test_parse_tmpl_str_test("{{#rel2abs:/d|}}"						, "{{test}}"			, "Test page/d");}
	@Test   public void Err_owner()				{fxt.Test_parse_tmpl_str_test("{{#rel2abs:..}}"						, "{{test}}"			, "");}	// PURPOSE.fix: should not fail
	@Test   public void Err_owner_2()			{fxt.Test_parse_tmpl_str_test("{{#rel2abs:/../../b|a}}"				, "{{test}}"			, "");}	// PURPOSE.fix: should not fail
	@Test   public void Ns_should_be_included_for_cur_page()	{// PURPOSE.fix: current title was not returning ns; EX: de.wikipedia.org/wiki/Hilfe:Vorlagenprogrammierung#Funktion_rel2abs 
		fxt.Page_ttl_("Help:A");	// set page to title with namespace
		fxt.Test_parse_tmpl_str_test("{{#rel2abs:.}}"				, "{{test}}"			, "Help:A");
	}
	@Test   public void Owner_lvl0()	{// PURPOSE.fix: old rel2abs was producing "/c"; EX: de.wikipedia.org/wiki/Hilfe:Vorlagenprogrammierung#Funktion_rel2abs
		fxt.Test_parse_tmpl_str_test("{{#rel2abs:../c|a}}"	, "{{test}}"			, "c");
	}	
	@Test   public void Rel2abs_slash() {
		fxt.Page_ttl_("Page_1");
		fxt.Init_defn_clear();
		fxt.Init_defn_add("test"				, "{{/B}}");
		fxt.Init_page_create("Page_1/B"		, "Page_1/B text");
		fxt.Test_parse_tmpl_str("{{test}}"	, "Page_1/B text");
		fxt.Init_defn_clear();
	}
	@Test   public void Rel2abs_dot() {
		fxt.Page_ttl_("Page_1/A");
		fxt.Init_defn_clear();
		fxt.Init_defn_add("test"				, "{{../C}}");
		fxt.Init_page_create("Page_1/C"		, "Page_1/C text");
		fxt.Test_parse_tmpl_str("{{test}}"	, "Page_1/C text");
		fxt.Init_defn_clear();
	}
	@Test   public void Rel2abs_ttl()		{
		Tst_rel2abs_ttl("a../b", true);
		Tst_rel2abs_ttl("a../[b", false);
	}
	private void Tst_rel2abs_ttl(String raw, boolean expd) {Tfds.Eq(expd, Pf_xtn_rel2abs.Rel2abs_ttl(ByteAry_.new_ascii_(raw), 0, String_.Len(raw)));}
}
