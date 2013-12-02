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
package gplx.xowa.parsers.lnkis; import gplx.*; import gplx.xowa.*; import gplx.xowa.parsers.*;
import gplx.xowa.dbs.tbls.*;
public class Xop_lnki_logger_redlinks_wkr implements GfoInvkAble {
	private Xow_wiki wiki; private Xog_win win; private ListAdp lnki_list; private boolean log_enabled; private Gfo_usr_dlg usr_dlg;
	private int request_idx;
	private Xop_lnki_logger_redlinks_mgr redlinks_mgr;
	public Xop_lnki_logger_redlinks_wkr(Xog_win win) {
		this.win = win; 
		Xoa_page page = win.Page();
		this.wiki = page.Wiki();
		redlinks_mgr = wiki.Ctx().Tab().Redlinks_mgr();
		this.lnki_list = redlinks_mgr.Lnki_list(); this.log_enabled = redlinks_mgr.Log_enabled(); this.usr_dlg = redlinks_mgr.Usr_dlg();
		this.request_idx = redlinks_mgr.Request_idx();
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_run)) Redlink();
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}	public static final String Invk_run = "run";
	public void Redlink() {
		ListAdp work_list = ListAdp_.new_();
		OrderedHash page_hash = OrderedHash_.new_bry_();
		page_hash.Clear(); // NOTE: do not clear in Page_bgn, else will fail b/c of threading; EX: Open Page -> Preview -> Save; DATE:2013-11-17
		work_list.Clear();
		int len = lnki_list.Count();
		if (log_enabled) usr_dlg.Log_many("", "", "redlink.redlink_bgn: page=~{0} total_links=~{1}", String_.new_utf8_(wiki.Ctx().Page().Page_ttl().Raw()), len);
		for (int i = 0; i < len; i++) {	// make a copy of list else thread issues
			if (win.Gui_wtr().Canceled()) return;
			if (redlinks_mgr.Request_idx() != request_idx) return;
			work_list.Add(lnki_list.FetchAt(i));
		}
		for (int i = 0; i < len; i++) {
			if (win.Gui_wtr().Canceled()) return;
			if (redlinks_mgr.Request_idx() != request_idx) return;
			Xop_lnki_tkn lnki = (Xop_lnki_tkn)work_list.FetchAt(i);
			Xoa_ttl ttl = lnki.Ttl();
			Xodb_page page = new Xodb_page().Ttl_(ttl);
			byte[] full_txt = ttl.Full_db();
			if (!page_hash.Has(full_txt))
				page_hash.Add(full_txt, page);
		}
		int page_len = page_hash.Count();
		for (int i = 0; i < page_len; i += Batch_size) {
			if (win.Gui_wtr().Canceled()) return;
			if (redlinks_mgr.Request_idx() != request_idx) return;
			int end = i + Batch_size;
			if (end > page_len) end = page_len;
			wiki.Db_mgr().Load_mgr().Load_by_ttls(win.Gui_wtr(), page_hash, Xodb_page_tbl.Load_idx_flds_only_y, i, end);
		}
		int redlink_count = 0;
		ByteAryBfr bfr = null;
		for (int j = 0; j < len; j++) {
			Xop_lnki_tkn lnki = (Xop_lnki_tkn)work_list.FetchAt(j);
			byte[] full_txt = lnki.Ttl().Full_db();
			Xodb_page page = (Xodb_page)page_hash.Fetch(full_txt);
			if (page == null) continue;	// pages shouldn't be null, but just in case
			if (!page.Exists()) {					
				if (log_enabled) {
					if (bfr == null) bfr = ByteAryBfr.new_();
					bfr.Add_int_variable(lnki.Html_id()).Add_byte_pipe().Add(Xop_tkn_.Lnki_bgn).Add(full_txt).Add(Xop_tkn_.Lnki_end).Add_byte(Byte_ascii.Semic).Add_byte_space();
				}
				if (win.Gui_wtr().Canceled()) return;
				if (redlinks_mgr.Request_idx() != request_idx) return;
				win.Gui_wtr().Html_elem_atr_set_append(Xop_lnki_logger_redlinks_mgr.Lnki_id_prefix + Int_.XtoStr(lnki.Html_id()), "class", " new");
				++redlink_count;
			}
		}
		if (log_enabled)
			usr_dlg.Log_many("", "", "redlink.redlink_end: redlinks_run=~{0} links=~{1}", redlink_count, bfr == null ? String_.Empty : bfr.XtoStrAndClear());
	}
        public static final Xop_lnki_logger_redlinks_wkr Null = new Xop_lnki_logger_redlinks_wkr();  Xop_lnki_logger_redlinks_wkr() {}
	private static final int Batch_size = 32;
}
