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
package gplx.xowa.specials.allPages; import gplx.*; import gplx.xowa.*; import gplx.xowa.specials.*;
import gplx.xowa.html.*;
import gplx.xowa.wikis.*;
public class Xows_page_allpages implements GfoInvkAble, ByteAryFmtrArg, Xows_page {
	public Xows_page_allpages(Xow_wiki wiki) {
		this.wiki = wiki;
		html_itm_fmtr = new Xos_pagelist_html_itm_fmtr(this, wiki);
	}	private Xos_pagelist_html_itm_fmtr html_itm_fmtr;
	public Xow_wiki Wiki() {return wiki;} private Xow_wiki wiki;
	public ByteAryFmtr Html_all() {return html_all;} ByteAryFmtr html_all = ByteAryFmtr.new_(String_.Concat_lines_nl
		(	"<table class=\"mw-allpages-table-form\">"
		,	"  <tr>"
		,	"    <td class=\"mw-allpages-nav\">"
		,	"~{anchor_prv} |"
		,	"~{anchor_nxt}"
		,	"    </td>"
		,	"  </tr>"
		,	"</table>"
		,	"<table class=\"mw-allpages-table-chunk\">"
		,	"<tbody>~{grps}"
		,	"</tbody>"
		,	"</table>"
		,	"<hr/>"
		,	"<div class=\"mw-allpages-nav\">"
		,	"~{anchor_prv} |"
		,	"~{anchor_nxt}"
		,	"</div>"
		),	"grps", "anchor_prv", "anchor_nxt");
	public ByteAryFmtr Html_list_grp() {return html_list_grp;} ByteAryFmtr html_list_grp = ByteAryFmtr.new_(String_.Concat_lines_nl_skipLast
		(	""
		,	"  <tr>~{itms}"
		,	"  </tr>"
		),	"itms");
	public ByteAryFmtr Html_list_itm_normal() {return html_list_itm_normal;} ByteAryFmtr html_list_itm_normal = ByteAryFmtr.new_(String_.Concat_lines_nl_skipLast
		(	""
		,	"    <td style=\"width:~{itm_pct}%\"><a~{lnki_atr_cls} href=\"~{lnki_href}\" title=\"~{lnki_title}\">~{lnki_text}</a></td>"
		),	"itm_pct", "lnki_href", "lnki_title", "lnki_text", "lnki_atr_cls");
	public ByteAryFmtr Html_list_itm_redirect() {return html_list_itm_redirect;} ByteAryFmtr html_list_itm_redirect = ByteAryFmtr.new_(String_.Concat_lines_nl_skipLast
		(	""
		,	"    <td style=\"width:~{itm_pct}%\"><div class=\"allpagesredirect\"><a href=\"~{lnki_href}\" class=\"mw-redirect\" title=\"~{lnki_title}\">~{lnki_text}</a></div></td>"
		),	"itm_pct", "lnki_href", "lnki_title", "lnki_text");
	public ByteAryFmtr Html_list_end() {return html_list_end;} ByteAryFmtr html_list_end = ByteAryFmtr.new_(String_.Concat_lines_nl_skipLast
		(	"<a href=\"/wiki/Special:AllPages?from=~{arg_from}~{args__rest}\" title=\"Special:AllPages\">~{lbl_text}</a>"
		),	"arg_from", "args__rest", "lbl_text");
	public int Itms_per_page() {return itms_per_page;} private int itms_per_page = 345;
	public void Itms_per_page_(int v) {itms_per_page = v; rslt_list_ttls = new Xodb_page[v];}
	public int Itms_per_grp() {return itms_per_grp;} private int itms_per_grp = 3;
	public boolean List_include_redirects() {return show_redirects;} private boolean show_redirects = true;
	public Xodb_page Rslt_prv() {return rslt_prv;} public void Rslt_prv_(Xodb_page v) {rslt_prv = v;} private Xodb_page rslt_prv;
	public Xodb_page Rslt_nxt() {return rslt_nxt;} public void Rslt_nxt_(Xodb_page v) {rslt_nxt = v;} private Xodb_page rslt_nxt;
	public int Rslt_list_len() {return rslt_list_len;} public void Rslt_list_len_(int v) {rslt_list_len = v;} private int rslt_list_len;
	public Xodb_page[] Rslt_list_ttls() {return rslt_list_ttls;} private Xodb_page[] rslt_list_ttls;
	public void Special_gen(Xoa_url url, Xoa_page page, Xow_wiki wiki, Xoa_ttl ttl) {
		wiki.Ctx().Tab().Display_ttl_(wiki.Msg_mgr().Val_by_id(Xol_msg_itm_.Id_sp_allpages_hdr));
		url.Page_bry_(ByteAry_.Add(ByteAry_.new_ascii_("Special:"), ttl.Page_txt_wo_qargs()));	// HACK: need to re-set Page b/c href_parser does not eliminate qargs; DATE:2013-02-08
//			url.Page_bry_(ttl.Page_txt_wo_qargs());	// HACK: need to re-set Page b/c href_parser does not eliminate qargs; DATE:2013-02-08
		if (wiki.Domain_tid() == Xow_wiki_domain_.Tid_home) {wiki.App().Usr_dlg().Prog_many(GRP_KEY, "home.invalid", "AllPages not implemented for home wiki"); return;}
		if (rslt_list_ttls == null) this.Itms_per_page_(itms_per_page);
		boolean found = Build_data(url); if (!found) return;
		Build_html(page);
	}
	public boolean Build_data(Xoa_url url) {
		init_ns = wiki.Ns_mgr().Ns_main();
		arg_hash.Load(url);
		byte[] from_val = arg_hash.Get_val_bry_or(Bry_arg_from, null); if (from_val == null) return false;
		from_val = wiki.App().Url_converter_id().Decode(from_val);
		int ns_val = arg_hash.Get_val_int_or(Bry_arg_ns, init_ns.Id()); init_ns = wiki.Ns_mgr().Ids_get_or_null(ns_val);
		boolean hide_redirects_val = arg_hash.Get_val_int_or(Bry_arg_hideredirects, 0) == 1; 
		for (int i = 0; i < itms_per_page; i++)
			rslt_list_ttls[i] = null;
		rslt_list_len = 0;
		rslt_nxt = rslt_prv = null;
		Xoa_ttl from_ttl = Xoa_ttl.parse_(wiki, from_val); if (from_ttl == null) return false;
		if (!from_ttl.Ns().Id_main()) {	// ns specified in title
			init_ns = from_ttl.Ns();
			arg_hash.Set_val_by_int(Bry_arg_ns, init_ns.Id());
			arg_hash.Set_val_by_bry(Bry_arg_from, from_ttl.Page_db());
			arg_hash.Save(url);
		}
//			wiki.Hive_mgr().Special_allpages_query(this, init_ns, from_ttl.Page_db(), itms_per_page, !hide_redirects_val);
		IntRef rslt_len = IntRef.new_(rslt_list_len);
		Xodb_page rslt_nxt2 = new Xodb_page();
		Xodb_page rslt_prv2 = new Xodb_page();
		int all_pages_min = 0;// no minimum for all pages
		ListAdp rslt_list = ListAdp_.new_();
		wiki.Db_mgr().Load_mgr().Load_ttls_for_all_pages(Cancelable_.Never, rslt_list, rslt_nxt2, rslt_prv2, rslt_len, init_ns, from_ttl.Page_db(), itms_per_page, all_pages_min, itms_per_page, !hide_redirects_val, true);
		rslt_list_len = rslt_len.Val();
		for (int i = 0; i < rslt_list_len; i++)
			rslt_list_ttls[i] = (Xodb_page)rslt_list.FetchAt(i);
		rslt_nxt = rslt_nxt2;
		rslt_prv = rslt_prv2;
		return true;
	}	private Xoa_url_arg_hash arg_hash = new Xoa_url_arg_hash();
	private static final byte[] Bry_arg_from = ByteAry_.new_ascii_("from"), Bry_arg_ns = ByteAry_.new_ascii_("namespace"), Bry_arg_hideredirects = ByteAry_.new_ascii_("hideredirects");
	public Xow_ns Init_ns() {return init_ns;} private Xow_ns init_ns;
	public void Build_html(Xoa_page page) {
		ByteAryBfr tmp_bfr = wiki.Utl_bry_bfr_mkr().Get_m001();
		byte[] anchor_prv = Build_html_end(tmp_bfr, rslt_prv, false);
		byte[] anchor_nxt = Build_html_end(tmp_bfr, rslt_nxt, true);
		html_all.Bld_bfr_many(tmp_bfr, this, anchor_prv, anchor_nxt);
		page.Data_raw_(tmp_bfr.XtoAryAndClear());
		tmp_bfr.Mkr_rls();
		page.Html_data().Restricted_n_();
	}
	byte[] Build_html_end(ByteAryBfr bfr, Xodb_page itm, boolean fwd) {
		Xoa_ttl ttl = Xows_page_allpages.ttl_(wiki, init_ns, itm); if (ttl == null) return ByteAry_.Empty;	// occurs when range is empty; EX: Module:A in simplewikibooks
		int msg_id = fwd ? Xol_msg_itm_.Id_sp_allpages_fwd : Xol_msg_itm_.Id_sp_allpages_bwd;
		Xol_msg_itm msg_itm = wiki.Lang().Msg_mgr().Itm_by_id_or_null(msg_id);
		ByteAryBfr tmp_bfr = wiki.Utl_bry_bfr_mkr().Get_b512().Mkr_rls();
		byte[] lbl_text = msg_itm.Fmt(tmp_bfr, ttl.Full_txt());
		byte[] args__rest = arg_hash.Concat(tmp_bfr, Bry_arg_ns, Bry_arg_hideredirects);
		byte[] arg_from = wiki.App().Url_converter_id().Encode(ttl.Page_txt_wo_qargs());
		return html_list_end.Bld_bry_many(bfr, arg_from, args__rest, lbl_text);
	}
	public static Xoa_ttl ttl_(Xow_wiki wiki, Xow_ns ns, Xodb_page itm) {
		byte[] ttl_bry = itm.Ttl_wo_ns();
		if (!ns.Id_main()) ttl_bry = ByteAry_.Add(ns.Name_db_w_colon(), ttl_bry);
		return Xoa_ttl.parse_(wiki, ttl_bry);
	}
	public void XferAry(ByteAryBfr bfr, int idx) {
		int len = rslt_list_ttls.length;
		html_itm_fmtr.XferAry_bgn();
		for (int i = 0; i < len; i += itms_per_grp) {
			html_itm_fmtr.Itm_idx_(i);
			html_list_grp.Bld_bfr_many(bfr, html_itm_fmtr);
		}
		html_itm_fmtr.XferAry_end();
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_html_all_)) 					html_all.Fmt_(m.ReadBry("v"));
		else if	(ctx.Match(k, Invk_html_list_grp_)) 			html_list_grp.Fmt_(m.ReadBry("v"));
		else if	(ctx.Match(k, Invk_html_list_itm_normal_)) 		html_list_itm_normal.Fmt_(m.ReadBry("v"));
		else if	(ctx.Match(k, Invk_html_list_itm_redirect_)) 	html_list_itm_redirect.Fmt_(m.ReadBry("v"));
		else if	(ctx.Match(k, Invk_itms_per_page_)) 			Itms_per_page_(m.ReadInt("v"));
		else if	(ctx.Match(k, Invk_itms_per_grp_)) 				itms_per_grp = m.ReadInt("v");
		else if	(ctx.Match(k, Invk_show_redirects_)) 			show_redirects = m.ReadYn("v");
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}
	public static final String Invk_html_all_ = "html_all_", Invk_html_list_grp_ = "html_list_grp_", Invk_html_list_itm_normal_ = "html_list_itm_normal_", Invk_html_list_itm_redirect_ = "html_list_itm_redirect_"
	, Invk_itms_per_page_ = "itms_per_page_", Invk_itms_per_grp_ = "itms_per_grp_", Invk_show_redirects_ = "show_redirects_";
	public static final String GRP_KEY = "xowa.special.allpages";
}
class Xos_pagelist_html_itm_fmtr implements ByteAryFmtrArg {
	public Xos_pagelist_html_itm_fmtr(Xows_page_allpages mgr, Xow_wiki wiki) {
		this.mgr = mgr; this.wiki = wiki; this.href_parser = wiki.App().Href_parser(); this.wiki_key = wiki.Domain_bry();
		this.itm_normal = mgr.Html_list_itm_normal(); this.itm_redirect = mgr.Html_list_itm_redirect();
		history_mgr = wiki.App().User().History_mgr();
	}	private Xows_page_allpages mgr; Xow_wiki wiki; Xoh_href_parser href_parser; ByteAryFmtr itm_normal, itm_redirect; byte[] wiki_key; gplx.xowa.users.history.Xou_history_mgr history_mgr;
	public void Itm_idx_(int v) {itm_idx = v;} private int itm_idx;
	public void XferAry_bgn() {
		itms_per_grp = mgr.Itms_per_grp();
		ttls = mgr.Rslt_list_ttls();
		ttls_len = ttls.length;
		itm_pct = 100 / itms_per_grp;
		init_ns = mgr.Init_ns();
	} 	int itms_per_grp, ttls_len, itm_pct; Xodb_page[] ttls; Xow_ns init_ns;
	public void XferAry_end() {ttls = null;}
	public void XferAry(ByteAryBfr bfr, int idx) {
		int itm_end = itm_idx + itms_per_grp;
		for (int i = itm_idx; i < itm_end; i++) {
			if (i >= ttls_len) break;	// handle odd number of itms; EX: interval=3; items=4; proc gets called on 0 (0-2) and 3 (3-5); ArrayIndex for 4, 5 
			Xodb_page ttl_itm = ttls[i];
			if (ttl_itm == null) break; // ttl_itm can be null at bgn or end of title list; EX: list=A-Z; count=5; key=Z; itms=X,Y,Z,null,null
			Xoa_ttl ttl = Xows_page_allpages.ttl_(wiki, init_ns, ttl_itm);
			byte[] href = href_parser.Build_to_bry(ttl, wiki);
			byte[] title = Xoh_html_wtr.Ttl_to_title(ttl.Full_txt());
			byte[] cls = Xoh_lnki_file_wtr.Lnki_cls_visited(history_mgr, wiki_key, ttl.Page_txt());	// NOTE: must be ttl.Page_txt() in order to match Xou_history_mgr.Add
			ByteAryFmtr fmtr = ttl_itm.Type_redirect() ? itm_redirect : itm_normal;
			fmtr.Bld_bfr_many(bfr, itm_pct, href, title, ttl.Full_txt(), cls);
		}
	}
}
