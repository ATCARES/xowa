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
public class Xop_xnde_include_tst {
	Xop_fxt fxt = new Xop_fxt();
	@Before public void init()							{fxt.Reset();}
	@Test  public void Tmpl_includeonly()				{fxt.tst_Parse_tmpl_str_test("a<includeonly>b</includeonly>c"						, "{{test}}", "abc");}
	@Test  public void Tmpl_noinclude()					{fxt.tst_Parse_tmpl_str_test("a<noinclude>b</noinclude>c"							, "{{test}}", "ac");}
	@Test  public void Tmpl_onlyinclude()				{fxt.tst_Parse_tmpl_str_test("a<onlyinclude>b</onlyinclude>c"						, "{{test}}", "b");}
	@Test  public void Tmpl_onlyinclude_nest()			{fxt.tst_Parse_tmpl_str_test("{{#ifeq:y|y|a<onlyinclude>b</onlyinclude>c|n}}"		, "{{test}}", "b");}	// PURPOSE: check that onlyinclude handles (a) inside {{#if}} function (old engine did not); and (b) that abc are correctly added together
	@Test  public void Tmpl_onlyinclude_page() {// PURPOSE: handle scenario similar to {{FA Number}} where # of articles is buried in page between onlyinclude tags; added noinclude as additional stress test
		fxt.ini_page_create("Transclude_1", "<noinclude>a<onlyinclude>b</onlyinclude>c</noinclude>d");
		fxt.tst_Parse_tmpl_str_test("{{:Transclude_1}}"		, "{{test}}", "b");
	}
	@Test  public void Tmpl_onlyinclude_page2() {	// PURPOSE: handle scenario similar to PS3 wherein onlyinclude was being skipped (somewhat correctly) but following text (<pre>) was also included
		fxt.ini_page_create("Transclude_2", "a<onlyinclude>b<includeonly>c</includeonly>d</onlyinclude>e<pre>f</pre>g");
		fxt.tst_Parse_tmpl_str_test("{{:Transclude_2}}"		, "{{test}}", "bcd");
	}

	@Test  public void Wiki_includeonly()	{fxt.tst_Parse_page_all_str("a<includeonly>b</includeonly>c"								, "ac");}
	@Test  public void Wiki_noinclude()		{fxt.tst_Parse_page_all_str("a<noinclude>b</noinclude>c"									, "abc");}
	@Test  public void Wiki_onlyinclude()	{fxt.tst_Parse_page_all_str("a<onlyinclude>b</onlyinclude>c"								, "abc");}
	@Test  public void Wiki_oi_io()			{fxt.tst_Parse_page_all_str("a<onlyinclude>b<includeonly>c</includeonly>d</onlyinclude>e"	, "abde");}
	@Test  public void Wiki_oi_io_tblw() {
		fxt.tst_Parse_page_all_str(String_.Concat_lines_nl_skipLast
			(	"<onlyinclude>"
			,	"{|"
			,	"|-"
			,	"|a<includeonly>"
			,	"|}</includeonly></onlyinclude>"
			,	"|-"
			,	"|b"
			,	"|}"
			),	String_.Concat_lines_nl_skipLast
			(	"<table>"
			,	"  <tr>"
			,	"    <td>a"
			,	"    </td>"
			,	"  </tr>"
			,	"  <tr>"
			,	"    <td>b"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			,	""
			));
	}

	// @Test  public void Wiki_includeonly_ignore() {fxt.tst_Parse_wiki_text("[[a<includeonly>b</includeonly>c]]", "[[ac]]");}	// FUTURE: ttl parses by idx, and ignores includeonly: WHEN: upon encountering; may need to redo in other parsers?
	@Test  public void Ex_Tmpl_io_oi()		{		// PURPOSE: <includeonly> not parsing internals; EX.WP: [[Template:MONTHNAME]]
		fxt.tst_Parse_tmpl_str_test("<includeonly>{{#if:{{{1}}}|a|b}}</includeonly><noinclude>c</noinclude>", "{{test|1}}", "a");
	}
	@Test  public void Ex_Tmpl_io_subst()		{	// PURPOSE: <includeonly> and @gplx.Internal protected subst; EX.WP: [[Template:Dubious]]
		fxt.ini_defn_clear();
		fxt.ini_defn_add("mwo_print", "{{{1}}}");
		fxt.ini_defn_add("substcheck", "SUBST");
		fxt.tst_Parse_tmpl_str_test(String_.Concat_lines_nl_skipLast
			(	"{{mwo_print"
			,	"|<includeonly>{{subst:</includeonly><includeonly>substcheck}}</includeonly>"
			,	"}}"
			), "{{test}}"
			,	"{{subst:substcheck}}\n"
			);
		fxt.Reset();
		fxt.tst_Parse_tmpl_str_test(String_.Concat_lines_nl_skipLast
			(	"{{mwo_print"
			,	"|<includeonly>{{safesubst:</includeonly><includeonly>substcheck}}</includeonly>"
			,	"}}"
			), "{{test}}"
			,	"SUBST\n");
		fxt.ini_defn_clear();
	}
	@Test  public void Ex_Tmpl_noinclude_prm_1() {	// PURPOSE: <noinclude> should not process @gplx.Internal protected tkns; EX.WP: [[Template:See]]
		fxt.ini_defn_clear();
		fxt.ini_defn_add("mwo_print", "{{{1}}}{{{2}}}");
		fxt.tst_Parse_tmpl_str_test
			(	"{{mwo_print|{{{1<noinclude>|not_seen</noinclude>}}}|{{{2}}}}}"
			,	"{{test|a|b}}"
			,	"ab"
			);
		fxt.ini_defn_clear();
	}
	@Test  public void Ex_Tmpl_noinclude_prm_2()	{	// PURPOSE: <noinclude> should not process default tkn;
		fxt.tst_Parse_tmpl_str_test
			(	"{{#if: {{{x|<noinclude>y</noinclude>}}} | visible | hidden}}"	// {{#if: {{{x|<noinclude>y</noinclude>}}} -> {{#if: {{{x|}} -> hidden
			,	"{{test}}"
			,	"hidden"
			);
	}
	@Test  public void Ex_Tmpl_noinclude2() {	// PURPOSE: <noinclude> should be separate from tkns {{convert|50|km|0|abbr=on}}
		fxt.ini_defn_clear();
		fxt.ini_defn_add("mwo_print", "{{{1}}}{{{2}}}");
		fxt.tst_Parse_tmpl_str_test
			(	"{{mwo_print<noinclude>{{{?}}}</noinclude>|a|b}}"
			,	"{{test}}"
			,	"ab"
			);
		fxt.ini_defn_clear();
	}
	@Test  public void Exception_incompleteTag_matchNext() {	// PURPOSE: "</noinclude" should not be matched;
		fxt.tst_Parse_tmpl_str_test
			(	"a<noinclude>b</noinclude c<noinclude>d</noinclude>e"
			,	"{{test}}"
			,	"ae"
			);
	}
	@Test  public void Exception_noCloseTag() {
		fxt.tst_Parse_tmpl_str_test
			(	"a<noinclude>bcde"
			,	"{{test}}"
			,	"a"
			);
	}
	@Test  public void Exception_inline() {
		fxt.tst_Parse_tmpl_str_test
			(	"a<noinclude/>bcde"
			,	"{{test}}"
			,	"abcde"
			);
	}
	@Test  public void Exception_inline_2() {
		fxt.tst_Parse_tmpl_str_test
			(	"a<noinclude/a/>bcde"
			,	"{{test}}"
			,	"a<noinclude/a/>bcde"
			);
	}
	@Test  public void Defect_onlyinclude_inside_template() {	// PURPOSE: was eating up next template; EX.WP:Wikipedia:Featured_articles
		fxt.tst_Parse_page_all_str
			(	"{{formatnum: <onlyinclude>1</onlyinclude>}} {{formatnum:2}}"
			,	"1 2"
			);
	}
	@Test  public void Only_include_preserves_nl() {	// PURPOSE: given "a\n<onlyinclude>{|\n", "{|" should be table; EX:en.w:Wikipedia:Reference_desk
		fxt.tst_Parse_page_all_str(String_.Concat_lines_nl
			(	"a"
			,	"<onlyinclude>==b==</onlyinclude>"
			,	"c"
			)
//			,	"{{test}}"
			,	String_.Concat_lines_nl
			(	"a"
			,	""
			,	"<h2>b</h2>"
			,	""
			,	"c"
			));
	}
	@Test  public void Only_include_interprets_template() {	// PURPOSE: <oi> should interpret templates
		fxt.ini_defn_clear();
		fxt.ini_defn_add("test", "see_me");
		fxt.tst_Parse_page_all_str(String_.Concat_lines_nl
			(	"a"
			,	"<onlyinclude>{{test}}</onlyinclude>"
			,	"c"
			)
			,	String_.Concat_lines_nl
			(	"a"
			,	"see_me"
			,	"c"
			));
	}

//		@Test  public void Defect_noinclude_inside_main() {		// PURPOSE: <onlyinclude> inside main was not returning content; EX.WP:Wikipedia:Featured_articles
//			fxt.ini_defn_clear();
//			fxt.ini_defn_add("Test_tmpl", "{{:Test_page}}");
//			fxt.Data_create("Test_page", "a{{#expr:<onlyinclude>1</onlyinclude>}}c");
//			fxt.tst_Parse_page_all_str
//				(	"{{Test_tmpl}}"
//				,	"1"
//				);
//			fxt.ini_defn_clear();
//		}
}
/*
<includeonly>-({{{1}}}={{{1}}}round-5)-({{{1}}}={{{1}}}round-4)-({{{1}}}={{{1}}}round-3)-({{{1}}}={{{1}}}round-2)-({{{1}}}={{{1}}}round-1)</includeonly><noinclude>
{{pp-template}}Called by {{lt|precision/0}}</noinclude>

==includeonly==
main: a<includeonly>b</includeonly>c<br/>
tmpl: {{mwo_include_only|a|b|c}}

==noinclude==
main: a<noinclude>b</noinclude>c<br/>
tmpl: {{mwo_no_include|a|b|c}}

==onlyinclude==
main: a<onlyinclude>b</onlyinclude>c<br/>
tmpl: {{mwo_only_include|a|b|c}}
*/