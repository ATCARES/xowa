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
public class Xop_xnde_wkr__basic_tst {
	private Xop_fxt fxt = new Xop_fxt();
	@After public void term() {fxt.Init_para_n_();}
	@Test  public void Escape_lt() {	// PURPOSE: some templates have unknown tags; EX.WP:PHP
		fxt.Init_para_y_();
		fxt.Test_parse_page_wiki_str("a<code><?</code>b", String_.Concat_lines_nl_skipLast
			(	"<p>a<code>&lt;?</code>b"
			,	"</p>"
			,	""
			));
		fxt.Init_para_n_();
	}
	@Test  public void Inline() {
		fxt.Test_parse_page_wiki("<ref/>"	, fxt.tkn_xnde_(0, 6).CloseMode_(Xop_xnde_tkn.CloseMode_inline).Name_rng_(1, 4));
	}
	@Test  public void Pair() {
		fxt.Test_parse_page_wiki("<div></div>", fxt.tkn_xnde_(0, 11).CloseMode_(Xop_xnde_tkn.CloseMode_pair).Name_rng_(1, 4));
	}
	@Test  public void Pair_text() {
		fxt.Test_parse_page_wiki("<div>b</div>", fxt.tkn_xnde_(0, 12).Subs_(fxt.tkn_txt_(5, 6)));
	}
	@Test  public void Deep1_pair1() {
		fxt.Test_parse_page_wiki("<div><div></div></div>", fxt.tkn_xnde_(0, 22).Name_rng_(1, 4)
			.Subs_(fxt.tkn_xnde_(5, 16).Name_rng_(6, 9)));
	}
	@Test  public void Deep1_inline1() {
		fxt.Test_parse_page_wiki("<div><ref/></div>", fxt.tkn_xnde_(0, 17).Name_rng_(1, 4)
			.Subs_(fxt.tkn_xnde_(5, 11).Name_rng_(6, 9)) );
	}
	@Test  public void Deep1_pair2() {
		fxt.Test_parse_page_wiki("<div><div></div><div></div></div>", fxt.tkn_xnde_(0, 33).Name_rng_(1, 4)
			.Subs_
			(	fxt.tkn_xnde_( 5, 16).Name_rng_( 6,  9)
			,	fxt.tkn_xnde_(16, 27).Name_rng_(17, 20)
			));
	}
	@Test  public void Deep2_pair1() {
		fxt.Test_parse_page_wiki("<div><div><div></div></div></div>", fxt.tkn_xnde_(0, 33).Name_rng_(1, 4)
			.Subs_
			(	fxt.tkn_xnde_( 5, 27).Name_rng_( 6,  9)
			.Subs_
			(	fxt.tkn_xnde_(10, 21).Name_rng_(11, 14))
			));
	}
	@Test  public void Slash() {// b/c mw allows unquoted attributes
		fxt.Test_parse_page_wiki("<ref / >a</ref>", fxt.tkn_xnde_(0, 15).Atrs_rng_(5, 7).Subs_(fxt.tkn_txt_(8, 9)));
		fxt.Test_parse_page_wiki("<ref name=a/b/>", fxt.tkn_xnde_(0, 15).Atrs_rng_(5, 13));
	}
	@Test  public void Escaped() {
		fxt.Init_log_(Xop_xnde_log.Escaped_xnde).Test_parse_page_wiki("<div></span></div>", fxt.tkn_xnde_(0, 18).Subs_(fxt.tkn_ignore_(5, 12, Xop_ignore_tkn.Ignore_tid_xnde_dangling)));
	}
	@Test  public void Nest() {// REVISIT: 2nd <b> should be converted to </b>; other </b> ignored; WHEN: with example
		fxt.Init_log_(Xop_xnde_log.Invalid_nest, Xop_xnde_log.Escaped_xnde).Test_parse_page_wiki("a<b>b<b>c</b>d</b>e"
			, fxt.tkn_txt_	( 0,  1)
			, fxt.tkn_xnde_	( 1, 13).Subs_(fxt.tkn_txt_(4, 9))
			, fxt.tkn_txt_	(13, 14)
			, fxt.tkn_ignore_(14, 18, Xop_ignore_tkn.Ignore_tid_xnde_dangling)
			, fxt.tkn_txt_	(18, 19)
			);
	}
	@Test  public void Xtn() {
		fxt.Test_parse_page_wiki("<math><div></math>", fxt.tkn_xnde_(0, 18).Subs_(fxt.tkn_txt_(6, 11)));	// NOTE: no dangling nde b/c .Xtn skips
	}
	@Test  public void Xtn_ref() {
		fxt.Test_parse_page_wiki("<ref name=\"a\">b</ref>", fxt.tkn_xnde_(0, 21).Name_rng_(1, 4).Atrs_rng_(5, 13).Subs_(fxt.tkn_txt_(14, 15)));
	}
	@Test  public void Lnki() {
		fxt.Test_parse_page_wiki("[[Image:a|b<br/>d]]"
			, fxt.tkn_lnki_().Ns_id_(Xow_ns_.Id_file).Trg_tkn_(fxt.tkn_arg_nde_().Val_tkn_(fxt.tkn_arg_itm_(fxt.tkn_txt_(2, 7), fxt.tkn_colon_(7), fxt.tkn_txt_(8, 9))))
			.Caption_tkn_(fxt.tkn_arg_nde_(10, 17).Val_tkn_(fxt.tkn_arg_itm_(fxt.tkn_txt_(10, 11), fxt.tkn_xnde_(11, 16), fxt.tkn_txt_(16, 17))))
			);
	}
	@Test  public void Br_converted_to_reguar_br() {
		fxt.Test_parse_page_wiki("</br>a"	, fxt.tkn_xnde_(0, 5), fxt.tkn_txt_(5, 6));
		fxt.Test_parse_page_wiki("<br/>a"	, fxt.tkn_xnde_(0, 5), fxt.tkn_txt_(5, 6));
		fxt.Test_parse_page_wiki("</br/>a"	, fxt.tkn_xnde_(0, 6), fxt.tkn_txt_(6, 7));
	}
	@Test  public void CaseSensitivity() {
		fxt.Test_parse_page_wiki("<DiV></dIv>", fxt.tkn_xnde_(0, 11).CloseMode_(Xop_xnde_tkn.CloseMode_pair).Name_rng_(1, 4));
	}
	@Test  public void CaseSensitivity_xtn_1() {
		fxt.Test_parse_page_wiki_str
			(	"<Inputbox>a</Inputbox>b<inputbox>c</inputbox>"
			,	"b"
			);
	}
	@Test   public void CaseSensitivity_xtn_2() {	// PURPOSE: xtn end_tag may not match bgn_tag; EX: w:Ehrenfest_paradox; <References></references>
		fxt.Test_parse_page_all_str("a<ref name=b /><References><ref name=b>c</ref></references>", String_.Concat_lines_nl
		(	"a<sup id=\"cite_ref-b_0-0\" class=\"reference\"><a href=\"#cite_note-b-0\">[1]</a></sup><ol class=\"references\">"
		,	"<li id=\"cite_note-b-0\"><span class=\"mw-cite-backlink\"><a href=\"#cite_ref-b_0-0\">^</a></span> <span class=\"reference-text\">c</span></li>"
		,	"</ol>"
		));
	}
	@Test   public void CaseSensitivity_xtn_3() {// PURPOSE: xtn xnde must do case-insensitive match DATE:2013-12-02
		fxt.Test_parse_page_all_str
			(	"<matH>a</math> b <math>c</matH>"	// <matH> should match </math> not </matH>
			,	"<span id='xowa_math_txt_0'>a</span> b <span id='xowa_math_txt_0'>c</span>"
			);
	}
	@Test  public void Whitelist() {
		fxt.Test_parse_page_all_str("<span onload='alert()'></span>", "<span></span>");
	}
	@Test  public void Whitelist_pre() { // PURPOSE: <pre style="overflow:auto">a</pre> somehow becoming <prestyle="overflow:auto">a</pre>; Template:Infobox_country; ISSUE: old xatr code being triggered; PURPOSE:(2) style being stripped when it shouldn't be
		fxt.Test_parse_page_all_str("<pre style=\"overflow:auto\">a</pre>", "<pre style=\"overflow:auto\">a</pre>");
	}
	@Test  public void Whitelist_style() {
		fxt.Test_parse_page_all_str("<div style=\"url(bad)\"></div>", "<div></div>");
	}
	@Test  public void Script() { // PURPOSE: nested script should (a) write attributes; (b) write close tag; DATE:2014-01-24
		fxt.Test_parse_page_all_str("<code><script src='a'>b</script></code>", "<code>&lt;script src='a'>b&lt;/script></code>");
	}
	@Test   public void Script_in_syntaxhighlight() {
		fxt.Test_parse_page_all_str("<syntaxhighlight><script>alert('fail');</script></syntaxhighlight>", "<pre style=\"overflow:auto;\">&lt;script&gt;alert('fail');&lt;/script&gt;</pre>");
	}
	@Test   public void Script_in_math() {
		fxt.App().File_mgr().Math_mgr().Renderer_is_mathjax_(false);
		fxt.Test_parse_page_all_str("<math><script>alert('fail');</script></math>", "<img id='xowa_math_img_0' src='' width='' height=''/><span id='xowa_math_txt_0'>&lt;script>alert('fail');</script></span>");
		fxt.App().File_mgr().Math_mgr().Renderer_is_mathjax_(true);
	}
	@Test  public void Html5_time() {// PURPOSE: HTML5; should output self (i.e.: must be whitelisted)
		fxt.Test_parse_page_wiki_str("<time class=\"dtstart\" datetime=\"2010-10-10\">10 October 2010</time>", "<time class=\"dtstart\" datetime=\"2010-10-10\">10 October 2010</time>");
	}
	@Test  public void Html5_bdi() {// PURPOSE: HTML5; should output self (i.e.: must be whitelisted); DATE:2013-12-07
		fxt.Test_parse_page_wiki_str("<bdi lang=\"en\">a</bdi>", "<bdi lang=\"en\">a</bdi>");
	}
	@Test  public void Html5_mark() {// PURPOSE: HTML5; should output self (i.e.: must be whitelisted); DATE:2014-01-03
		fxt.Test_parse_page_wiki_str("<mark lang=\"en\">a</mark>", "<mark lang=\"en\">a</mark>");
	}
	@Test  public void Html5_mark_span() {// PURPOSE: </span> should close <mark> tag; EX: zh.wikipedia.org/wiki/异体字; DATE:2014-01-03
		fxt.Test_parse_page_wiki_str("<mark>a</span>", "<mark>a</mark>");
	}
	@Test  public void Html5_wbr() {// PURPOSE: HTML5; should output self (i.e.: must be whitelisted); DATE:2014-01-03
		fxt.Test_parse_page_wiki_str("a<wbr>b<wbr>c", "a<wbr></wbr>b<wbr></wbr>c");
	}
	@Test  public void Html5_bdo() {// PURPOSE: HTML5; should output self (i.e.: must be whitelisted); DATE:2014-01-03
		fxt.Test_parse_page_wiki_str("<bdo>a</bdo>", "<bdo>a</bdo>");
	}
	@Test  public void Pre_always_parsed() {	// PURPOSE: pre should not interpret templates; DATE:2014-04-10
		fxt.Init_defn_clear();
		fxt.Init_defn_add("a", "a");
		fxt.Init_defn_add("test", "<pre>{{a}}</pre>");
		fxt.Test_parse_page_all_str("{{test}}", "<pre>{{a}}</pre>");
		fxt.Init_defn_clear();
	}
}
