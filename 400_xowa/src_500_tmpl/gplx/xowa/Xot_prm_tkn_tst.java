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
public class Xot_prm_tkn_tst {
	Xop_fxt fxt = new Xop_fxt();
	@Before public void init() {fxt.Reset();}
	@Test  public void Idx_1()							{fxt.tst_Parse_tmpl_str_test("{{{1}}}"							, "{{test|a|b}}"					, "a");}
	@Test  public void Idx_2()							{fxt.tst_Parse_tmpl_str_test("{{{2}}}"							, "{{test|a|b}}"					, "b");}
	@Test  public void Idx_3_nil()						{fxt.tst_Parse_tmpl_str_test("{{{3}}}"							, "{{test|a|b}}"					, "{{{3}}}");}
	@Test  public void Idx_3_dflt()						{fxt.tst_Parse_tmpl_str_test("{{{3|c}}}"						, "{{test|a|b}}"					, "c");}
	@Test  public void Idx_3_dflt_len0()				{fxt.tst_Parse_tmpl_str_test("{{{1|}}}"							, "{{test}}"						, "");}
	@Test  public void Idx_1_and_2()					{fxt.tst_Parse_tmpl_str_test("({{{1}}} {{{2}}})"				, "{{test|a|b}}"					, "(a b)");}
	@Test  public void Idx_2_len0()						{fxt.tst_Parse_tmpl_str_test("{{{1}}}"							, "{{test||b}}"						, "");}// should not fail
	@Test  public void Key()							{fxt.tst_Parse_tmpl_str_test("{{{k1}}}"							, "{{test|k1=a|k2=b}}"				, "a");}
	@Test  public void Key_nil()						{fxt.tst_Parse_tmpl_str_test("{{{k3|c}}}"						, "{{test|k1=a|k2=b}}"				, "c");}
	@Test  public void Key_exact()						{fxt.tst_Parse_tmpl_str_test("{{{k|}}}{{{k2|}}}"				, "{{test|k=a}}"					, "a");}	// only {{{k}}} matched
	@Test  public void Var()							{fxt.tst_Parse_tmpl_str_test("{{{1|-{{PAGENAME}}-}}}"			, "{{test}}"						, "-Test page-");}
	@Test  public void Newline_bgn()					{fxt.tst_Parse_tmpl_str_test("{{{1}}} {{{2}}}"					, "{{test|a|\nb}}"					, "a \nb");}
	@Test  public void Newline_end()					{fxt.tst_Parse_tmpl_str_test("{{{1}}} {{{2}}}"					, "{{test|a|b\n}}"					, "a b\n");}
	@Test  public void Exc_lkp_nil()					{fxt.tst_Parse_tmpl_str_test("{{{}}}"							, "{{test|a|b}}"					, "{{{}}}");}
	@Test  public void Exc_lkp_and_args1()				{fxt.tst_Parse_tmpl_str_test("{{{|}}}"							, "{{test|a|b}}"					, "");}
	@Test  public void Exc_lkp_nil_args1_txt()			{fxt.tst_Parse_tmpl_str_test("{{{|a}}}"							, "{{test|a|b}}"					, "a");}
	@Test  public void Ws_idx()							{fxt.tst_Parse_tmpl_str_test("{{{ 1 }}}"						, "{{test|a|b}}"					, "a");}
	@Test  public void Ws_idx_nil()						{fxt.tst_Parse_tmpl_str_test("{{{ 1 }}}"						, "{{test}}"						, "{{{ 1 }}}");}
	@Test  public void Ws_key()							{fxt.tst_Parse_tmpl_str_test("{{{ k1 }}}"						, "{{test|k1=a|k2=b}}"				, "a");}
	@Test  public void Ws_dflt()						{fxt.tst_Parse_tmpl_str_test("{{{1| a }}}"						, "{{test}}"						, " a ");}
	@Test  public void Dflt_multiple()					{fxt.tst_Parse_tmpl_str_test("{{{1|a|b}}}"						, "{{test}}"						, "a");}
	@Test  public void Keyd_not_idxd()					{fxt.tst_Parse_tmpl_str_test("{{{1}}}{{{2}}}"					, "{{test|a|key=b}}"				, "a{{{2}}}");}
	@Test  public void Keyd_not_idxd_ints()				{fxt.tst_Parse_tmpl_str_test("{{{1}}}{{{2}}}"					, "{{test|1=a|2=b}}"				, "ab");}
	@Test  public void Recurse_1()						{fxt.tst_Parse_tmpl_str_test("{{{1{{{2|}}}|}}}"					, "{{test|a}}"						, "a");}	// used in {{See}} to test if argument 2 is last
	@Test  public void Recurse_2()						{fxt.tst_Parse_tmpl_str_test("{{{1{{{2|}}}|}}}"					, "{{test|a|b}}"					, "");}
	@Test  public void Keyd_int()						{fxt.tst_Parse_tmpl_str_test("{{{1}}}{{{2}}}"					, "{{test|2=a|b}}"					, "ba");}
	@Test  public void Keyd_int2()						{fxt.tst_Parse_tmpl_str_test("{{{1}}}{{{2}}}"					, "{{test|2=a|1=b}}"				, "ba");}
	@Test  public void Keyd_int3()						{fxt.tst_Parse_tmpl_str_test("{{{12}}}"							, "{{test|12=a}}"					, "a");}
	@Test  public void Equal_ignored()					{fxt.tst_Parse_tmpl_str_test("{{{1|b=c}}}"						, "{{test}}"						, "b=c");}
	@Test  public void Unresolved()						{fxt.tst_Parse_tmpl_str_test(""									, "{{{a|b}}}"						, "b");}
	@Test  public void Six_ltr()						{fxt.tst_Parse_tmpl_str_test("{{{{{{1}}}}}}"					, "{{test|a}}"						, "{{{a}}}");}
	@Test  public void Six_num()						{fxt.tst_Parse_tmpl_str_test("{{{{{{1}}}}}}"					, "{{test|1}}"						, "1");}
}
/*
*/
