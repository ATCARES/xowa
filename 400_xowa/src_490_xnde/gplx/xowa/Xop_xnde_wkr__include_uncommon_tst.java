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
public class Xop_xnde_wkr__include_uncommon_tst {
	private Xop_fxt fxt = new Xop_fxt();
	@Before public void init()							{fxt.Reset();}
	@Test  public void Ex_Tmpl_io_oi()		{		// PURPOSE: <includeonly> not parsing internals; EX.WP: [[Template:MONTHNAME]]
		fxt.Test_parse_tmpl_str_test("<includeonly>{{#if:{{{1}}}|a|b}}</includeonly><noinclude>c</noinclude>", "{{test|1}}", "a");
	}
	@Test  public void Ex_Tmpl_io_subst()		{	// PURPOSE: <includeonly> and @gplx.Internal protected subst; EX.WP: [[Template:Dubious]]
		fxt.Init_defn_clear();
		fxt.Init_defn_add("mwo_print", "{{{1}}}");
		fxt.Init_defn_add("substcheck", "SUBST");
		fxt.Test_parse_tmpl_str_test(String_.Concat_lines_nl_skipLast
			(	"{{mwo_print"
			,	"|<includeonly>{{subst:</includeonly><includeonly>substcheck}}</includeonly>"
			,	"}}"
			), "{{test}}"
			,	"{{subst:substcheck}}\n"
			);
		fxt.Reset();
		fxt.Test_parse_tmpl_str_test(String_.Concat_lines_nl_skipLast
			(	"{{mwo_print"
			,	"|<includeonly>{{safesubst:</includeonly><includeonly>substcheck}}</includeonly>"
			,	"}}"
			), "{{test}}"
			,	"SUBST\n");
		fxt.Init_defn_clear();
	}
	@Test  public void Ex_Tmpl_noinclude_prm_1() {	// PURPOSE: <noinclude> should not process @gplx.Internal protected tkns; EX.WP: [[Template:See]]
		fxt.Init_defn_clear();
		fxt.Init_defn_add("mwo_print", "{{{1}}}{{{2}}}");
		fxt.Test_parse_tmpl_str_test
			(	"{{mwo_print|{{{1<noinclude>|not_seen</noinclude>}}}|{{{2}}}}}"
			,	"{{test|a|b}}"
			,	"ab"
			);
		fxt.Init_defn_clear();
	}
	@Test  public void Ex_Tmpl_noinclude_prm_2()	{	// PURPOSE: <noinclude> should not process default tkn;
		fxt.Test_parse_tmpl_str_test
			(	"{{#if: {{{x|<noinclude>y</noinclude>}}} | visible | hidden}}"	// {{#if: {{{x|<noinclude>y</noinclude>}}} -> {{#if: {{{x|}} -> hidden
			,	"{{test}}"
			,	"hidden"
			);
	}
	@Test  public void Ex_Tmpl_noinclude2() {	// PURPOSE: <noinclude> should be separate from tkns {{convert|50|km|0|abbr=on}}
		fxt.Init_defn_clear();
		fxt.Init_defn_add("mwo_print", "{{{1}}}{{{2}}}");
		fxt.Test_parse_tmpl_str_test
			(	"{{mwo_print<noinclude>{{{?}}}</noinclude>|a|b}}"
			,	"{{test}}"
			,	"ab"
			);
		fxt.Init_defn_clear();
	}
	@Test  public void Exception_incompleteTag_matchNext() {	// PURPOSE: "</noinclude" should not be matched;
		fxt.Test_parse_tmpl_str_test
			(	"a<noinclude>b</noinclude c<noinclude>d</noinclude>e"
			,	"{{test}}"
			,	"ae"
			);
	}
	@Test  public void Exception_noCloseTag() {
		fxt.Test_parse_tmpl_str_test
			(	"a<noinclude>bcde"
			,	"{{test}}"
			,	"a"
			);
	}
	@Test  public void Exception_inline() {
		fxt.Test_parse_tmpl_str_test
			(	"a<noinclude/>bcde"
			,	"{{test}}"
			,	"abcde"
			);
	}
	@Test  public void Exception_inline_2() {
		fxt.Test_parse_tmpl_str_test
			(	"a<noinclude/a/>bcde"
			,	"{{test}}"
			,	"a<noinclude/a/>bcde"
			);
	}
	@Test  public void Defect_onlyinclude_inside_template() {	// PURPOSE: was eating up next template; EX.WP:Wikipedia:Featured_articles
		fxt.Test_parse_page_all_str
			(	"{{formatnum: <onlyinclude>1</onlyinclude>}} {{formatnum:2}}"
			,	"1 2"
			);
	}
	@Test  public void Only_include_preserves_nl() {	// PURPOSE: given "a\n<onlyinclude>{|\n", "{|" should be table; EX:en.w:Wikipedia:Reference_desk
		fxt.Test_parse_page_all_str(String_.Concat_lines_nl
			(	"a"
			,	"<onlyinclude>==b==</onlyinclude>"
			,	"c"
			)
//			,	"{{test}}"
			,	String_.Concat_lines_nl
			(	"a"
			,	""
			,	"<h2>b</h2>"
			,	"c"
			));
	}
	@Test  public void Only_include_interprets_template() {	// PURPOSE: <oi> should interpret templates
		fxt.Init_defn_clear();
		fxt.Init_defn_add("test", "see_me");
		fxt.Test_parse_page_all_str(String_.Concat_lines_nl
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
	@Test  public void Include_only_in_template_name() {// PURPOSE: includeonly in tmpl_name should be ignored; EX:de.w:Wikipedia:Projektdiskussion; DATE:2014-01-24
		fxt.Init_defn_clear();
		fxt.Init_defn_add("test", "abc");
		fxt.Test_parse_page_all_str("{{<includeonly></includeonly>test}}", "abc");
	}
	@Test  public void Include_only_in_transcluded_page() {// PURPOSE: include only int transcluded page should be ignored; EX:de.w:Wikipedia:Projektdiskussion; DATE:2014-01-24
		fxt.Init_page_create("page", "abc");	// create page in main ns
		fxt.Test_parse_page_all_str("{{<includeonly>safesubst:</includeonly>page}}", "abc");	// will become {{safesubst:page}} which should then transclude page
	}
	@Test  public void Hdr() {	// PURPOSE: includeonly should be evaluated during template parse; EX: es.b:Billar/T�cnica/Clases_de_puentes; DATE:2014-02-12
		fxt.Test_parse_page_all_str("=<includeonly>=</includeonly>A=<includeonly>=</includeonly>", "<h1>A</h1>\n");
	}
//		@Test  public void Noinclude_nested() {	// PURPOSE: nested noincludes don't work; th.w:ISO_3166-1;DATE:2014-04-06
//			fxt.Init_defn_clear();
//			fxt.Init_defn_add("test", "a<noinclude>b<noinclude>c</noinclude>d</noinclude>e");
//			fxt.Test_parse_page_all_str("{{test}}", "ae");
//		}

//		@Test  public void Wiki_includeonly_ignore() {fxt.Test_parse_wiki_text("[[a<includeonly>b</includeonly>c]]", "[[ac]]");}	// FUTURE: ttl parses by idx, and ignores includeonly: WHEN: upon encountering; may need to redo in other parsers?
//		@Test  public void Defect_noinclude_inside_main() {		// PURPOSE: <onlyinclude> inside main was not returning content; EX.WP:Wikipedia:Featured_articles
//			fxt.Init_defn_clear();
//			fxt.Init_defn_add("Test_tmpl", "{{:Test_page}}");
//			fxt.Data_create("Test_page", "a{{#expr:<onlyinclude>1</onlyinclude>}}c");
//			fxt.Test_parse_page_all_str
//				(	"{{Test_tmpl}}"
//				,	"1"
//				);
//			fxt.Init_defn_clear();
//		}
}
