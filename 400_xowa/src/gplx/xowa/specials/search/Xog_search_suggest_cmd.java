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
package gplx.xowa.specials.search; import gplx.*; import gplx.xowa.*; import gplx.xowa.specials.*;
class Xog_search_suggest_cmd implements GfoInvkAble, Cancelable {
	public Xog_search_suggest_cmd(Xoa_app app, Xog_search_suggest_mgr mgr) {
		this.app = app; this.mgr = mgr;
	}	private Xoa_app app; Xog_search_suggest_mgr mgr; ByteAryBfr tmp_bfr = ByteAryBfr.reset_(255); ListAdp rslts_1 = ListAdp_.new_(), rslts_2 = ListAdp_.new_();
	public void Init(Xow_wiki wiki, byte[] search_bry, byte[] cbk_func, int max_results, byte search_mode, int all_pages_extend, int all_pages_min) {
		this.wiki = wiki; this.search_bry = search_bry; this.cbk_func = cbk_func; this.max_results = max_results;
		this.search_mode_all_pages = search_mode == Xog_search_suggest_mgr.Tid_search_mode_all_pages; this.all_pages_extend = all_pages_extend; this.all_pages_min = all_pages_min;
		searcher = new Xosrh_page_mgr();
	}	private Xow_wiki wiki; byte[] search_bry, cbk_func; Xosrh_page_mgr searcher; int max_results, all_pages_extend, all_pages_min;
	boolean search_mode_all_pages;
	public byte[] Search_bry() {return search_bry;}
	public boolean Canceled() {return canceled;}
	public void Cancel() {this.canceled = true;} private boolean canceled;
	public void Cancel_reset() {this.canceled = false;}
	public boolean Working() {return working;} public void Working_(boolean v) {working = v;} private boolean working;
	public ListAdp Results() {return rslts_2;}
	public void Search() {
		try {	// NOTE: must handle any errors in async mode
			canceled = false;
			working = true;
			if (search_mode_all_pages) {
				rslts_2.Clear();
//					int browse_len = max_results + all_pages_extend;
				Xodb_page rslt_nxt = new Xodb_page();
				Xodb_page rslt_prv = new Xodb_page();
				Xoa_ttl search_ttl = Xoa_ttl.parse_(wiki, search_bry); if (search_ttl == null) return;
				byte[] search_ttl_bry = search_ttl.Page_db();
				ListAdp page_list = ListAdp_.new_();
				wiki.Db_mgr().Load_mgr().Load_ttls_starting_with(this, page_list, rslt_nxt, rslt_prv, IntRef.zero_(), wiki.Ns_mgr().Ns_main(), search_ttl_bry, max_results, all_pages_min, all_pages_extend, true, false);
				Xodb_page[] page_ary = (Xodb_page[])page_list.XtoAryAndClear(Xodb_page.class);
				int idx = 0, page_ary_len = page_ary.length;
				for (int i = 0; i < page_ary_len; i++) {
					Xodb_page page = page_ary[i];
					if (page != null) {
						if (!ByteAry_.HasAtBgn(page.Ttl_wo_ns(), search_ttl_bry)) continue;	// look-ahead may return other titles that don't begin with search; ignore
						if (page.Text_len() > all_pages_min) {
							rslts_2.Add(page);
							idx++;
						}
					}
					if (idx == max_results) break;
				}
			}
			else {
				Xosrh_rslt_grp rv = searcher.Itms_per_page_(max_results).Search(tmp_bfr, wiki, search_bry, 0, searcher, this);
				if (canceled) {working = false; return;}
				rslts_1.Clear();
				int len = rv.Itms_len();
				for (int i = 0; i < len; i++)
					rslts_1.Add(rv.Itms_get_at(i));
				if (canceled) {working = false; return;}
				rslts_1.SortBy(Xodb_page_sorter.EnyLenDsc);
				if (canceled) {working = false; return;}
				if (len > max_results) len = max_results;
				rslts_2.Clear();
				for (int i = 0; i < len; i++)
					rslts_2.Add(rslts_1.FetchAt(i));
				if (canceled) {working = false; return;}
				rslts_2.SortBy(Xodb_page_sorter.IdAsc);
				wiki.Db_mgr().Load_mgr().Load_by_ids(this, rslts_2, 0, len);
				rslts_2.SortBy(Xodb_page_sorter.TitleAsc);
				if (canceled) {working = false; return;}
			}
			GfoInvkAble_.InvkCmd(app.Gui_mgr().Kit().New_cmd_sync(mgr), Xog_search_suggest_mgr.Invk_notify);
			working = false;
		} 
		catch(Exception e) {
			app.Usr_dlg().Prog_many("", "", "error during search: ~{0}", Err_.Message_gplx_brief(e));
		}
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_search))		Search();
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}	public static final String Invk_search = "search";
}
