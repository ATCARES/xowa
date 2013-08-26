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
public class Xop_subst_tst {
	Xop_fxt fxt = new Xop_fxt(); 
	@Before public void init() {
		fxt.Reset();
		fxt.ini_defn_clear();
		fxt.ini_defn_add("mwo_print", "{{{1}}}");
		fxt.ini_defn_add("!", "|");
	}
	@Test  public void Wiki_txt_subst()				{fxt.tst_Parse_tmpl_str_test("{{{1}}}"								, "{{subst:test|a}}"			, "a");}
	@Test  public void Wiki_txt_subst_ws()			{fxt.tst_Parse_tmpl_str_test("{{{1}}}"								, "{{ subst:test|a}}"			, "a");}
	@Test  public void Wiki_txt_safesubst()			{fxt.tst_Parse_tmpl_str_test("{{{1}}}"								, "{{safesubst:test|a}}"		, "a");}
	@Test  public void Tmpl_txt_subst_empty()		{fxt.tst_Parse_tmpl_str_test("{{subst:}}"							, "{{test}}"					, "{{subst:}}");}
	@Test  public void Tmpl_txt_safesubst()			{fxt.tst_Parse_tmpl_str_test("{{safesubst:mwo_print|a}}"			, "{{test}}"					, "a");}
	@Test  public void Tmpl_prm_subst()				{fxt.tst_Parse_tmpl_str_test("{{{{{1|subst:}}}mwo_print|a}}"		, "{{test}}"					, "{{subst:mwo_print|a}}");}
	@Test  public void Tmpl_prm_subst_ws()			{fxt.tst_Parse_tmpl_str_test("{{{{{1| subst:}}}mwo_print|a}}"		, "{{test}}"					, "{{ subst:mwo_print|a}}");}
	@Test  public void Tmpl_prm_safesubst()			{fxt.tst_Parse_tmpl_str_test("{{{{{1|safesubst:}}}mwo_print|a}}"	, "{{test}}"					, "a");}
	@Test  public void Tmpl_prm_safesubst_empty()	{fxt.tst_Parse_tmpl_str_test("{{{{{|safesubst:}}}mwo_print|a}}"		, "{{test}}"					, "a");}
	@Test  public void Tmpl_txt_subst_pf()			{fxt.tst_Parse_tmpl_str_test("{{subst:#expr:0}}"					, "{{test}}"					, "0");}
	@Test  public void Tmpl_txt_safesubst_prm()		{fxt.tst_Parse_tmpl_str_test("{{{{{|safesubst:}}}#if:{{{1|}}}{{{{{|safesubst:}}}!}}c1|c2}}"	, "{{test}}"					, "c2");}
	@Test  public void Exc_tmpl_prm_safesubst_ns()	{fxt.tst_Parse_tmpl_str_test("{{{{{|safesubst}}}:NAMESPACE}}"		, "{{test}}"					, "");}
	@Test  public void Unreferenced() {	// PURPOSE: if subst, but in tmpl stage, do not actually subst; EX.WP:Unreferenced; DATE:2013-01-31
		fxt.ini_defn_clear();
		fxt.ini_defn_add("substcheck", "SUBST");
		fxt.ini_defn_add("ifsubst", String_.Concat_lines_nl
			(	"{{ {{{|safesubst:}}}#ifeq:{{ {{{|safesubst:}}}NAMESPACE}}|{{NAMESPACE}}"
			,	" |{{{no|{{{2|}}}}}}"
			,	" |{{{yes|{{{1|}}}}}}"
			,	"}}"
			));
		fxt.tst_Parse_tmpl_str_test("{{ {{{|safesubst:}}}ifsubst |yes|<includeonly>{{subst:substcheck}}</includeonly>}}", "{{test}}", "{{subst:substcheck}}");
	}
	// NOTE: these are actually not good tests; MW does subst just before save; it doesn't do subst on load; in this case, the tests are testing load (which will noop); they need to test save (which xowa doesn't do)
//		@Test  public void Tmpl_txt_subst()				{fxt.tst_Parse_tmpl_str_test("{{subst:mwo_print|a}}"				, "{{test}}"					, "a");}
//		@Test  public void Tmpl_txt_subst_prm()			{fxt.tst_Parse_tmpl_str_test("{{subst:mwo_print|{{{1}}}}}"			, "{{test|a}}"					, "a");}

	//@Test  public void Tmpl_txt_safesubst_prm()		{fxt.tst_Parse_tmpl_str_test("{{{{{|safesubst:}}}ns:Category}}"	, "{{test}}"					, "Category");}
	//@Test  public void Tmpl_txt_subst_immed()		{fxt.tst_Parse_tmpl_str_test("{{mwo_print{{subst:!}}a}}"			, "{{test}}"					, "a");}
}