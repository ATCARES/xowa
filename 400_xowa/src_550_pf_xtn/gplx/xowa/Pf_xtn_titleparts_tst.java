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
public class Pf_xtn_titleparts_tst {
	Xop_fxt fxt = new Xop_fxt();
	@Before public void init()				{fxt.Reset();}
	@Test  public void Len1()				{fxt.tst_Parse_tmpl_str_test("{{#titleparts:a/b/c/d|1}}"			, "{{test}}", "A");}
	@Test  public void Len2()				{fxt.tst_Parse_tmpl_str_test("{{#titleparts:a/b/c/d|2}}"			, "{{test}}", "A/b");}
	@Test  public void Len2_Bgn2()			{fxt.tst_Parse_tmpl_str_test("{{#titleparts:a/b/c/d|2|2}}"			, "{{test}}", "b/c");}
	@Test  public void LenNeg1()			{fxt.tst_Parse_tmpl_str_test("{{#titleparts:a/b/c/d|-1}}"			, "{{test}}", "A/b/c");}
	@Test  public void BgnNeg1()			{fxt.tst_Parse_tmpl_str_test("{{#titleparts:a/b/c/d||-1}}"			, "{{test}}", "d");}
	@Test  public void LenNeg1Bgn2()		{fxt.tst_Parse_tmpl_str_test("{{#titleparts:a/b/c/d|-1|2}}"			, "{{test}}", "b/c");}

	@Test  public void Exc_len0()			{fxt.tst_Parse_tmpl_str_test("{{#titleparts:a/b/c/d|0}}"			, "{{test}}", "A/b/c/d");}
	@Test  public void Exc_lenNeg4()		{fxt.tst_Parse_tmpl_str_test("{{#titleparts:a/b/c/d|-4}}"			, "{{test}}", "");}
	@Test  public void Exc_lenNeg5()		{fxt.tst_Parse_tmpl_str_test("{{#titleparts:a/b/c/d|-5}}"			, "{{test}}", "");}
	@Test  public void Exc_no_dlm()			{fxt.tst_Parse_tmpl_str_test("{{#titleparts:abcd|1}}"				, "{{test}}", "Abcd");}
	@Test  public void Exc_parts0()			{fxt.tst_Parse_tmpl_str_test("{{#titleparts:abcd|0}}"				, "{{test}}", "Abcd");}
	@Test  public void Exc_many_segs()		{fxt.tst_Parse_tmpl_str_test("{{#titleparts:a/b|1|4}}"				, "{{test}}", "");}	// PURPOSE: bgn=4, len=1 requested; only bgn=1,2 available

	@Test  public void Exc_bad_len()		{fxt.ini_Log_(Pf_xtn_titleparts_log.Len_is_invalid).tst_Parse_tmpl_str_test("{{#titleparts:a/b/c/d|bad}}"		, "{{test}}", "A/b/c/d");}
	@Test  public void Exc_bad_bgn()		{fxt.ini_Log_(Pf_xtn_titleparts_log.Bgn_is_invalid).tst_Parse_tmpl_str_test("{{#titleparts:a/b/c/d|1|bad}}"		, "{{test}}", "A");}
	@Test  public void Exc_1()				{fxt.tst_Parse_tmpl_str_test("{{#titleparts:50|1|1}}"				, "{{test}}", "50");}
	@Test  public void Exc_2()				{fxt.tst_Parse_tmpl_str_test("{{#titleparts:50|1|2}}"				, "{{test}}", "");}

	@Test  public void Lnki()				{fxt.tst_Parse_tmpl_str_test("{{#titleparts:[[a|b]]|1}}"			, "{{test}}", "[[a|b]]");}	// PURPOSE: invalid title was not being rendered
	@Test  public void Invalid_table() {// PURPOSE: Template:Taxobox/showtaxon calls #titleparts on table fragments; do not remove new lines; EX.WP:Owl
		fxt.ini_defn_clear();
		fxt.ini_defn_add("!", "|");
		fxt.ini_defn_add("!!", "||");
		fxt.tst_Parse_tmpl_str_test
		(	String_.Concat_lines_nl_skipLast
		(	"{|"
		,	"|-"
		,	"{{#titleparts:"
		,	"{{!}} Order: {{!!}} '''Strigiformes'''"
		,	"{{!}}-|1}}"
		,	"|}"
		)
		,	"{{test}}"
		,	String_.Concat_lines_nl_skipLast
		(	""	// NOTE: don't know why this new line is necessary, but hopefully it is benign
		,	"{|"
		,	"|-"
		,	"| Order: || '''Strigiformes'''"
		,	"|-"
		,	"|}"
		)
		);
	}
	@Test  public void Invalid_xml() {// PURPOSE: handle calls like {{#titleparts:a<span id='b'>c</span>d|1}}; EX.WP:Triceratops
		fxt.tst_Parse_tmpl_str_test
		(	"{{#titleparts:a<span id='b'>c</span>d/e|1}}"
		,	"{{test}}"
		,	"a<span id='b'>c</span>d/e"
		);
	}
}