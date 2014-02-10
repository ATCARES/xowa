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
public class Xoh_html_wtr_tst {
	private Xop_fxt fxt = new Xop_fxt();
	@Test  public void Hr_basic()					{fxt.Test_parse_page_wiki_str("----"				, "<hr/>");}
	@Test  public void Hr_extended()				{fxt.Test_parse_page_wiki_str("--------"			, "<hr/>");}
	@Test  public void Lnki_basic()					{fxt.Test_parse_page_wiki_str("[[a]]"				, "<a href=\"/wiki/A\">a</a>");}
	@Test  public void Lnki_caption()				{fxt.Test_parse_page_wiki_str("[[a|b]]"				, "<a href=\"/wiki/A\">b</a>");}
	@Test  public void Lnki_caption_fmt()			{fxt.Test_parse_page_wiki_str("[[a|''b'']]"			, "<a href=\"/wiki/A\"><i>b</i></a>");}
	@Test  public void Lnki_tail_trg()				{fxt.Test_parse_page_wiki_str("[[a]]b"				, "<a href=\"/wiki/A\">ab</a>");}
	@Test  public void Lnki_tail_caption()			{fxt.Test_parse_page_wiki_str("[[a|b]]c"			, "<a href=\"/wiki/A\">bc</a>");}
	@Test   public void Lnki_title() {
		fxt.Hctx().Lnki_title_(true);
		fxt.Test_parse_page_wiki_str("[[a|b]]", "<a href=\"/wiki/A\" title=\"A\">b</a>");
		fxt.Hctx().Lnki_title_(false);
	}
	@Test   public void Lnki_title_page_text() {
		fxt.Hctx().Lnki_title_(true);
		fxt.Test_parse_page_wiki_str("[[a_b]]", "<a href=\"/wiki/A_b\" title=\"A b\">a_b</a>");
		fxt.Hctx().Lnki_title_(false);
	}
	@Test  public void Lnki_category()				{fxt.Test_parse_page_wiki_str("[[Category:A]]"		, "");}	// NOTE: Category does not get written in main page bfr
	@Test  public void Lnki_category_force()		{fxt.Test_parse_page_wiki_str("[[:Category:A]]"		, "<a href=\"/wiki/Category:A\">Category:A</a>");}
	@Test  public void Lnki_matches_page()			{fxt.Test_parse_page_wiki_str("[[test page|t1]]", "<b>t1</b>");}	// NOTE: "Test page" is hardcoded to be the test page name
	@Test  public void Lnki_matches_page_but_has_anchor()	{fxt.Test_parse_page_wiki_str("[[Test page#a|test 1]]", "<a href=\"/wiki/Test_page#a\">test 1</a>");}	// NOTE: "Test page" is hardcoded to be the test page name
	@Test  public void Lnki_anchor()				{fxt.Test_parse_page_wiki_str("[[A#b]]"				, "<a href=\"/wiki/A#b\">A#b</a>");}
//		@Test  public void Img_invalid_wnt_char() {
//			fxt.Test_parse_page_wiki_str
//			(	"[[File:A*b.png]]"
//			,	"<div class=\"floatnone\"><a href=\"File:A.png\" class=\"image\"><img alt=\"\" src=\"\" width=\"20\" height=\"30\" /></a></div>"
//			);
//		}
//		@Test  public void Img_alt() {	// FUTURE: enable; WHEN: after fixing xnde to handle bad xnde; EX: France
//			fxt.Test_parse_page_wiki_str("[[File:A.png|none|9x8px|alt=a<b>b</b>\"c\"d]]", Xop_fxt.html_img_none("File:A.png", "ab&quot;c&quot;d"));
//		}
	@Test  public void Url_encode()					{fxt.Test_parse_page_wiki_str("[[a;@$!*(),/ _^b|z]]"		, "<a href=\"/wiki/a;@$!*(),/__%5Eb\">z</a>");}
	@Test  public void Apos_i()						{fxt.Test_parse_page_wiki_str("''a''"						, "<i>a</i>");}
	@Test  public void Apos_b()						{fxt.Test_parse_page_wiki_str("'''a'''"						, "<b>a</b>");}
	@Test  public void Apos_ib()					{fxt.Test_parse_page_wiki_str("'''''a'''''"					, "<i><b>a</b></i>");}
	@Test  public void Html_ent()					{fxt.Test_parse_page_wiki_str("&#33;"						, "!");}
	@Test  public void Html_ref()					{fxt.Test_parse_page_wiki_str("&gt;"						, "&gt;");}
	@Test  public void Lnke_basic()					{fxt.Test_parse_page_wiki_str("[irc://a]"					, "<a href=\"irc://a\" class=\"external text\" rel=\"nofollow\">[1]</a>");}
	@Test  public void Lnke_autonumber()			{fxt.Test_parse_page_wiki_str("[irc://a] [irc://b]"			, "<a href=\"irc://a\" class=\"external text\" rel=\"nofollow\">[1]</a> <a href=\"irc://b\" class=\"external text\" rel=\"nofollow\">[2]</a>");}
	@Test  public void Lnke_caption()				{fxt.Test_parse_page_wiki_str("[irc://a b]"					, "<a href=\"irc://a\" class=\"external text\" rel=\"nofollow\">b</a>");}
	@Test  public void Lnke_caption_fmt()			{fxt.Test_parse_page_wiki_str("[irc://a ''b'']"				, "<a href=\"irc://a\" class=\"external text\" rel=\"nofollow\"><i>b</i></a>");}
	@Test  public void Lnke_xowa()	{
		String img = "<img src=\"file:///mem/xowa/user/test_user/app/img/xowa/protocol.png\"/>";
		fxt.Wiki().Sys_cfg().Xowa_proto_enabled_(true);
		fxt.Test_parse_page_wiki_str("[xowa-cmd:\"a\" z]"			, "<a href=\"xowa-cmd:a\">z" + img + "</a>");
		fxt.Test_parse_page_wiki_str("[xowa-cmd:\"a.b('c_d');\" z]"	, "<a href=\"xowa-cmd:a.b('c_d');\">z" + img + "</a>");
		fxt.Test_parse_page_wiki_str("[xowa-cmd:*\"a\"b*c\"* z]"		, "<a href=\"xowa-cmd:a%22b%2Ac\">z" + img + "</a>");
		fxt.Wiki().Sys_cfg().Xowa_proto_enabled_(false);
		fxt.Test_parse_page_wiki_str("[xowa-cmd:\"a\" b]"			, "[xowa-cmd:&quot;a&quot; b]");	// protocol is disabled: literalize String (i.e.: don't make it an anchor)
	}
	@Test  public void List_1_itm()	{
		fxt.Test_parse_page_wiki_str("*a", String_.Concat_lines_nl_skipLast
			( "<ul>"
			, "  <li>a"
			, "  </li>"
			, "</ul>"
			));
	}
	@Test  public void List_2_itms()	{
		fxt.Test_parse_page_wiki_str("*a\n*b", String_.Concat_lines_nl_skipLast
			( "<ul>"
			, "  <li>a"
			, "  </li>"
			, "  <li>b"
			, "  </li>"
			, "</ul>"
			));
	}
	@Test  public void List_nest_ul()	{
		fxt.Test_parse_page_wiki_str("*a\n**b", String_.Concat_lines_nl_skipLast
			( "<ul>"
			, "  <li>a"
			, "    <ul>"
			, "      <li>b"
			, "      </li>"
			, "    </ul>"
			, "  </li>"
			, "</ul>"
			));
	}
	@Test  public void List_dt_dd()	{
		fxt.Test_parse_page_wiki_str(";a:b", String_.Concat_lines_nl_skipLast
			( "<dl>"
			, "  <dt>a"
			, "  </dt>"
			, "  <dd>b"
			, "  </dd>"
			, "</dl>"
			));
	}
	@Test  public void List_dd_nest2()	{
		fxt.Test_parse_page_wiki_str("::a", String_.Concat_lines_nl_skipLast
			( "<dl>"
			, "  <dd>"
			, "    <dl>"
			, "      <dd>a"
			, "      </dd>"
			, "    </dl>"
			, "  </dd>"
			, "</dl>"
			));
	}
	@Test  public void Tblw_basic() {
		fxt.Test_parse_page_wiki_str("{|\n|+a\n!b||c\n|-\n|d||e\n|}", String_.Concat_lines_nl
			( "<table>"
			, "  <caption>a"
			, "  </caption>"
			, "  <tr>"
			, "    <th>b"
			, "    </th>"
			, "    <th>c"
			, "    </th>"
			, "  </tr>"
			, "  <tr>"
			, "    <td>d"
			, "    </td>"
			, "    <td>e"
			, "    </td>"
			, "  </tr>"
			, "</table>"
			));
	}
	@Test  public void Tblw_atrs() {
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"{|style='z'"
			,	"|+a"
			,	"!style='y'|b||style='x'|c"
			,	"|-style='w'"
			,	"|style='v'|d||style='u'|e"
			,	"|}"
			), String_.Concat_lines_nl
			(	"<table style='z'>"
			,	"  <caption>a"
			,	"  </caption>"
			,	"  <tr>"
			,	"    <th style='y'>b"
			,	"    </th>"
			,	"    <th style='x'>c"
			,	"    </th>"
			,	"  </tr>"
			,	"  <tr style='w'>"
			,	"    <td style='v'>d"
			,	"    </td>"
			,	"    <td style='u'>e"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			));
	}
	@Test  public void Tblw_exc_tb_tr_tb() {	// PURPOSE: if <tr><table>, ignore <table>; DATE:2014-02-02
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			( "{|"
			, "|-"
			, "{|"
			, "|}"
			, "|}"), String_.Concat_lines_nl_skipLast
			( "<table>"
			, "  <tr>"
			, "  </tr>"
			, "</table>"
			, ""
			));
	}
	@Test  public void Tblw_exc_tb_tc_tb() {
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			( "{|"
			, 	"|+"
			, 	"{|"
			,	"|}"
			, "|}"), String_.Concat_lines_nl_skipLast
			( "<table>"
			, "  <caption>"
			, "  </caption>"
			, "  <tr>"
			, "    <td>"
			, "      <table>"
			, "        <tr>"
			, "          <td>"
			, "          </td>"
			, "        </tr>"
			, "      </table>"
			, "    </td>"
			, "  </tr>"
			, "</table>"
			, ""
			));
	}
	@Test  public void Para_hdr_list() {
		fxt.Ctx().Para().Enabled_y_();
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			( "==a=="
			, ""
			, "*b"
			, "*c"
			), String_.Concat_lines_nl_skipLast
			( "<h2>a</h2>"
			, ""
			, "<ul>"
			, "  <li>b"
			, "  </li>"
			, "  <li>c"
			, "  </li>"
			, "</ul>"
			));
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void Para_nl_is_space() {
		fxt.Ctx().Para().Enabled_y_();
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			( "a"
			, "b"
			), String_.Concat_lines_nl_skipLast
			( "<p>a"
			, "b"
			, "</p>"
			, ""
			));
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void Para_nl_2_2() {
		fxt.Ctx().Para().Enabled_y_();
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			( "a"
			, ""
			, "b"
			, ""
			, "c"
			), String_.Concat_lines_nl_skipLast
			( "<p>a"
			, "</p>"
			, ""
			, "<p>b"
			, "</p>"
			, ""
			, "<p>c"
			, "</p>"
			, ""
			));
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void Div_2() {	// WP:[[Air]]#Density of air
		fxt.Ctx().Para().Enabled_y_();
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl_skipLast
			(	"<div>a</div>"
			,	""
			,	"<div>b</div>"
			), String_.Concat_lines_nl_skipLast
			(	"<div>a</div><div>b</div>"	// FUTURE: should be \n between divs; WHEN: rework para_parser
			));
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void Tblw() {
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl
			(	"{|"
			,	"|-"
			,	"|a"
			,	"|b"
			,	"|-"
			,	"|c"
			,	"|d"
			,	"|}"
			)
			, String_.Concat_lines_nl
			( "<table>"
			, "  <tr>"
			, "    <td>a"
			, "    </td>"
			, "    <td>b"
			, "    </td>"
			, "  </tr>"
			, "  <tr>"
			, "    <td>c"
			, "    </td>"
			, "    <td>d"
			, "    </td>"
			, "  </tr>"
			, "</table>"
			, ""
			));
	}
	@Test  public void Tblw_lnki_bang() {
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl
			(	"{|"
			,	"|-"
			,	"|[[a|!]]"
			,	"|}"
			)
			, String_.Concat_lines_nl
			( "<table>"
			, "  <tr>"
			, "    <td><a href=\"/wiki/A\">!</a>"
			, "    </td>"
			, "  </tr>"
			, "</table>"
			, ""
			));
	}
	@Test  public void Tr_inside_tblw_td() {	// WP:[[Earth]]
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl
			(	"{|"
			,	"|-"
			,	"<tr><td>a</td></tr>"
			,	"|}"
			)
			, String_.Concat_lines_nl
			(	"<table>"
			,	"  <tr>"
			,	"    <td>a"
			,	"    </td>"
			,	"  </tr>"
			,	"</table>"
			, ""
			));
	}
	@Test  public void Tblw_tr_with_newlines() {// WP:[[John Adams]] Infobox Officeholder
		fxt.Test_parse_page_wiki_str(String_.Concat_lines_nl
			(	"{|"
			,	"|-"
			,	""
			,	""
			,	""
			,	"|a"
			,	"|}"
			)
			, String_.Concat_lines_nl
			( "<table>"
			, "  <tr>"
			, "    <td>a"
			, "    </td>"
			, "  </tr>"
			, "</table>"
			, ""
			));
	}
	@Test  public void Bang_doesnt_force_tbl() {
		fxt.Ctx().Para().Enabled_y_();
		fxt.Test_parse_page_wiki_str("a! b! c", "<p>a! b! c\n</p>\n");
		fxt.Ctx().Para().Enabled_n_();
	}
	@Test  public void Err_nlOnly() {
		fxt.Test_parse_page_wiki_str("{{\n}}", "{{\n}}");	// NOTE: was  {{}}
	}
	@Test  public void Xnde_inline() {
		fxt.Test_parse_page_wiki_str("<div/>", "<div></div>");
	}
	@Test  public void Xnde_id_encode() { // PURPOSE: id should be url-encoded; DATE: 2013-11-13;
		fxt.Test_parse_page_wiki_str("<div id='a*'></div>", "<div id='a.2A'></div>");
		fxt.Test_parse_page_wiki_str("<div id='a b'></div>", "<div id='a_b'></div>");
	}
	@Test  public void Math() {
		fxt.App().File_mgr().Math_mgr().Renderer_is_mathjax_(false);
		fxt.Test_parse_page_all_str("<math>x + y</math>", "<img id='xowa_math_img_0' src='' width='' height=''/><span id='xowa_math_txt_0'>x + y</span>");	// latex has img
		fxt.App().File_mgr().Math_mgr().Renderer_is_mathjax_(true);
		fxt.Test_parse_page_all_str("<math>x + y</math>", "<span id='xowa_math_txt_0'>x + y</span>");	// mathjax has no img
		fxt.App().File_mgr().Math_mgr().Renderer_is_mathjax_(false);
	}
	@Test  public void Amp_ncr_should_not_be_rendered_as_bytes() {	// PURPOSE: &#160; should be rendered as &#160; not as literal bytes {192,160}; DATE:2013-12-09
		fxt.Test_parse_page_wiki_str("a&#160;b", "a&#160;b");
	}

	//		@Test  public void Fix_PositionAbsolute_stripped() {
//			fxt.Test_parse_page_wiki_str("<span style=\"position:absolute;\"></span>", "<span style=\";\"></span>");
//		}
//		@Test  public void Xnde_nl()	{
//			fxt.Test_parse_page_wiki_str("<div id='a'\nclass='b'>c</div>", String_.Concat_lines_nl_skipLast
//					( "<div id='a' class='b'>c</div>"
//					));
//		}
//		@Test  public void Tblw()	{
//			fxt.Test_parse_page_wiki_str("{|\n|}", String_.Concat_lines_nl
//				( "<table>"
//				, "  <tr>"
//				, "    <td>a"
//				, "    </td>"
//				, "    <td>b"
//				, "    </td>"
//				, "  </tr>"
//				, "</table>"
//				));
//		}
}			
