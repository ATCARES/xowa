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
public class Pf_name_tst {
	Xop_fxt fxt = new Xop_fxt();
	@Before public void init()					{fxt.Reset();}
	@Test  public void Ttl_page_txt()			{fxt.Page_ttl_("a b"); fxt.tst_Parse_tmpl_str_test("{{PAGENAME}}"								, "{{test}}", "A b");}
	@Test  public void Ttl_page_txt_empty()		{fxt.Page_ttl_("a b"); fxt.tst_Parse_tmpl_str_test("{{PAGENAME:}}"								, "{{test}}", "");}
	@Test  public void Ttl_page_txt_empty_2()	{fxt.Page_ttl_("a b"); fxt.tst_Parse_tmpl_str_test("{{PAGENAME:{{{a|}}}}}"						, "{{test}}", "");}	// PURPOSE.fix: {{PAGENAME:{{{nom|}}}}} should return ""; pt.wikipedia.org/wiki/Nicholas Kratzer; DATE:2013-02-20
	@Test  public void Ttl_page_txt_arg()		{fxt.Page_ttl_("a b"); fxt.tst_Parse_tmpl_str_test("{{PAGENAME:c d}}"							, "{{test}}", "C d");}
	@Test  public void Ttl_page_txt_amp()		{fxt.Page_ttl_("a b"); fxt.tst_Parse_tmpl_str_test("{{PAGENAME:c & d}}"							, "{{test}}", "C & d");}
	@Test  public void Ttl_page_url()			{fxt.Page_ttl_("a b"); fxt.tst_Parse_tmpl_str_test("{{PAGENAMEE}}"								, "{{test}}", "A_b");}
	@Test  public void Ttl_full_txt()			{fxt.Page_ttl_("Help:a b"); fxt.tst_Parse_tmpl_str_test("{{FULLPAGENAME}}"						, "{{test}}", "Help:A b");}
	@Test  public void Ttl_full_url()			{fxt.Page_ttl_("Help:a b"); fxt.tst_Parse_tmpl_str_test("{{FULLPAGENAMEE}}"						, "{{test}}", "Help:A_b");}
	@Test  public void Ttl_leaf_txt()			{fxt.Page_ttl_("a b/c/d e"); fxt.tst_Parse_tmpl_str_test("{{SUBPAGENAME}}"						, "{{test}}", "d e");}
	@Test  public void Ttl_leaf_url()			{fxt.Page_ttl_("a b/c/d e"); fxt.tst_Parse_tmpl_str_test("{{SUBPAGENAMEE}}"						, "{{test}}", "d_e");}
	@Test  public void Ttl_base_txt()			{fxt.Page_ttl_("a b/c/d e"); fxt.tst_Parse_tmpl_str_test("{{BASEPAGENAME}}"						, "{{test}}", "A b/c");}
	@Test  public void Ttl_base_url()			{fxt.Page_ttl_("a b/c/d e"); fxt.tst_Parse_tmpl_str_test("{{BASEPAGENAMEE}}"					, "{{test}}", "A_b/c");}
	@Test  public void Ttl_subj_txt()			{fxt.Page_ttl_("Help talk:a b"); fxt.tst_Parse_tmpl_str_test("{{SUBJECTPAGENAME}}"				, "{{test}}", "Help:A b");}
	@Test  public void Ttl_subj_url()			{fxt.Page_ttl_("Help talk:a b"); fxt.tst_Parse_tmpl_str_test("{{SUBJECTPAGENAMEE}}"				, "{{test}}", "Help:A_b");}
	@Test  public void Ttl_talk_txt()			{fxt.Page_ttl_("Help:a b"); fxt.tst_Parse_tmpl_str_test("{{TALKPAGENAME}}"						, "{{test}}", "Help talk:A b");}
	@Test  public void Ttl_talk_url()			{fxt.Page_ttl_("Help:a b"); fxt.tst_Parse_tmpl_str_test("{{TALKPAGENAMEE}}"						, "{{test}}", "Help_talk:A_b");}
	@Test  public void Ns_num()					{fxt.Page_ttl_("Help:A"); fxt.tst_Parse_tmpl_str_test("{{NAMESPACENUMBER}}"						, "{{test}}", "12");}
	@Test  public void Ns_txt()					{fxt.Page_ttl_("Help talk:a b"); fxt.tst_Parse_tmpl_str_test("{{NAMESPACE}}"					, "{{test}}", "Help talk");}
	@Test  public void Ns_url()					{fxt.Page_ttl_("Help talk:a b"); fxt.tst_Parse_tmpl_str_test("{{NAMESPACEE}}"					, "{{test}}", "Help_talk");}
	@Test  public void Ns_subj_txt()			{fxt.Page_ttl_("Help talk:a b"); fxt.tst_Parse_tmpl_str_test("{{SUBJECTSPACE}}"					, "{{test}}", "Help");}
	@Test  public void Ns_subj_url()			{fxt.Page_ttl_("Help talk:a b"); fxt.tst_Parse_tmpl_str_test("{{SUBJECTSPACEE}}"				, "{{test}}", "Help");}
	@Test  public void Ns_talk_txt()			{fxt.Page_ttl_("Help:a b"); fxt.tst_Parse_tmpl_str_test("{{TALKSPACE}}"							, "{{test}}", "Help talk");}
	@Test  public void Ns_talk_url()			{fxt.Page_ttl_("Help:a b"); fxt.tst_Parse_tmpl_str_test("{{TALKSPACEE}}"						, "{{test}}", "Help_talk");}
	@Test  public void Exc_empty2()				{fxt.Page_ttl_("Test"); fxt.tst_Parse_tmpl_str_test("{{NAMESPACE:<noinclude>x</noinclude>}}"	, "{{test}}", "");} // {{NMS:<ni>x</ni>}} -> {{NMS:}}
}
