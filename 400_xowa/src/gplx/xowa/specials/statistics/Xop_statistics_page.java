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
package gplx.xowa.specials.statistics; import gplx.*; import gplx.xowa.*; import gplx.xowa.specials.*;
public class Xop_statistics_page implements Xows_page {
	private Xop_statistics_stats_page_grp stats_page = new Xop_statistics_stats_page_grp();
//		private Xop_statistics_stats_wiki_grp stats_wiki = new Xop_statistics_stats_wiki_grp();
	private Xop_statistics_stats_ns_grp stats_ns = new Xop_statistics_stats_ns_grp();
	public void Special_gen(Xoa_url calling_url, Xoa_page page, Xow_wiki wiki, Xoa_ttl ttl) {
		byte[] html = Build_html(wiki);
		page.Html_restricted_n_();	// [[Special:]] pages allow all HTML
		page.Data_raw_(html);
	}
	public byte[] Build_html(Xow_wiki wiki) {
		ByteAryBfr tmp_bfr = wiki.Utl_bry_bfr_mkr().Get_m001();
		stats_page.Wiki_(wiki);
//			stats_wiki.Wiki_(wiki);
		stats_ns.Wiki_(wiki);
		fmtr_all.Bld_bfr_many(tmp_bfr, stats_page, stats_ns);
		return tmp_bfr.Mkr_rls().XtoAryAndClear();
	}
	private ByteAryFmtr fmtr_all = ByteAryFmtr.new_(String_.Concat_lines_nl_skipLast
	(	"<div id=\"mw-content-text\">"
	,	"<table class=\"wikitable mw-statistics-table\">~{page_stats}~{ns_stats}"
	,	"</table>"
	,	"</div>"
	), "page_stats", "ns_stats");
}
class Xop_statistics_stats_page_grp implements ByteAryFmtrArg {
	public void Wiki_(Xow_wiki v) {this.wiki = v;} private Xow_wiki wiki;
	public void XferAry(ByteAryBfr bfr, int idx) {			
		byte[] lbl_header_pages = wiki.Msg_mgr().Val_by_id(Xol_msg_itm_.Id_statistics_header_pages);
		byte[] lbl_articles = wiki.Msg_mgr().Val_by_id(Xol_msg_itm_.Id_statistics_articles);
		byte[] lbl_pages = wiki.Msg_mgr().Val_by_id(Xol_msg_itm_.Id_statistics_pages);
		byte[] lbl_pages_desc = wiki.Msg_mgr().Val_by_id(Xol_msg_itm_.Id_statistics_pages_desc);
		fmtr_page.Bld_bfr_many(bfr, lbl_header_pages, lbl_articles, lbl_pages, lbl_pages_desc , wiki.Lang().Num_fmt_mgr().Fmt(wiki.Stats().NumArticles()), wiki.Lang().Num_fmt_mgr().Fmt(wiki.Stats().NumPages()));
	}
	private ByteAryFmtr fmtr_page = ByteAryFmtr.new_(String_.Concat_lines_nl_skipLast
	(	""
	,	"  <tr>"
	,	"    <th colspan=\"2\">~{lbl_header_pages}</th>"
	,	"  </tr>"
	,	"  <tr class=\"mw-statistics-articles\">"
	,	"    <td>"
	,	"      ~{lbl_articles}"
	,	"    </td>"
	,	"    <td class=\"mw-statistics-numbers\" style='text-align:right'>~{page_count_main}</td>"
	,	"  </tr>"
	,	"  <tr class=\"mw-statistics-pages\">"
	,	"    <td>~{lbl_pages}<br />"
	,	"      <small class=\"mw-statistic-desc\"> ~{lbl_pages_desc}</small>"
	,	"    </td>"
	,	"    <td class=\"mw-statistics-numbers\" style='text-align:right'>~{page_count_all}</td>"
	,	"  </tr>"
	), "lbl_header_pages", "lbl_articles", "lbl_pages", "lbl_pages_desc", "page_count_main", "page_count_all");
}
class Xop_statistics_stats_ns_grp implements ByteAryFmtrArg {
	private Xop_statistics_stats_ns_itm ns_itm_fmtr = new Xop_statistics_stats_ns_itm();
	public void Wiki_(Xow_wiki v) {this.wiki = v; ns_itm_fmtr.Wiki_(v);} private Xow_wiki wiki;
	public void XferAry(ByteAryBfr bfr, int idx) {
		byte[] lbl_header_ns = wiki.Msg_mgr().Val_by_id(Xol_msg_itm_.Id_statistics_header_ns);
		fmtr_ns_grp.Bld_bfr_many(bfr, lbl_header_ns, ns_itm_fmtr);
	}
	private ByteAryFmtr fmtr_ns_grp = ByteAryFmtr.new_(String_.Concat_lines_nl_skipLast
	(	""
	,	"  <tr>"
	,	"    <th colspan=\"2\">~{lbl_header_ns}</th>"
	,	"  </tr>~{ns_itms}"
	), "lbl_header_ns", "ns_itms");
}
class Xop_statistics_stats_ns_itm implements ByteAryFmtrArg {
	public void Wiki_(Xow_wiki v) {this.wiki = v;} private Xow_wiki wiki;
	public void XferAry(ByteAryBfr bfr, int idx) {
		Xow_ns_mgr ns_mgr = wiki.Ns_mgr();
		int ns_len = ns_mgr.Count();
		for (int i = 0; i < ns_len; i++) {
			Xow_ns ns = ns_mgr.Id_get_at(i);
			if (ns.Is_meta()) continue;
			if (ns.Count() == 0) continue;
			byte[] ns_name = ns.Id_main() ? wiki.Msg_mgr().Val_by_id(Xol_msg_itm_.Id_ns_blankns) : ns.Name_txt();
			fmtr_ns_itm.Bld_bfr_many(bfr, ns_name, wiki.Lang().Num_fmt_mgr().Fmt(ns.Count()));
		}
	}
	private ByteAryFmtr fmtr_ns_itm = ByteAryFmtr.new_(String_.Concat_lines_nl_skipLast
	(	""
	,	"  <tr>"
	,	"    <td>~{ns_name}</td>"
	,	"    <td style='text-align:right'>~{ns_count}</td>"
	,	"  </tr>"
	), "ns_name", "ns_count");
}
class Xop_statistics_stats_wiki_grp implements ByteAryFmtrArg {
	public void Wiki_(Xow_wiki v) {this.wiki = v;} private Xow_wiki wiki;
	public void XferAry(ByteAryBfr bfr, int idx) {
		fmtr_wiki.Bld_bfr_many(bfr, wiki.Db_mgr().Tid_name(), wiki.Fsys_mgr().Root_dir().Raw(), Byte_.XtoStr(wiki.Db_mgr().Category_version()), wiki.Maint_mgr().Wiki_dump_date().XtoStr_fmt_iso_8561());
	}
	private ByteAryFmtr fmtr_wiki = ByteAryFmtr.new_(String_.Concat_lines_nl_skipLast
	(	""	
	,	"  <tr>"
	,	"    <th colspan=\"2\">Wiki statistics</th>"
	,	"  </tr>"
	,	"  <tr>"
	,	"    <td>Wiki format</td>"
	,	"    <td>~{wiki_format}</td>"
	,	"  </tr>"
	,	"  <tr>"
	,	"    <td>Wiki location</td>"
	,	"    <td>~{wiki_url}</td>"
	,	"  </tr>"
	,	"  <tr>"
	,	"    <td>Category level</td>"
	,	"    <td>~{ctg_version}</td>"
	,	"  </tr>"
	,	"  <tr>"
	,	"    <td>Last page updated on</td>"
	,	"    <td>~{page_modified_max}</td>"
	,	"  </tr>"
	), "wiki_format", "wiki_url", "ctg_version", "page_modified_max");
}
