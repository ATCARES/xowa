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
package gplx.xowa.dbs; import gplx.*; import gplx.xowa.*;
import gplx.xowa.bldrs.imports.ctgs.*; import gplx.xowa.ctgs.*; import gplx.xowa.specials.search.*;
public interface Xodb_load_mgr {
	void Load_init			(Xow_wiki wiki);
	void Load_page			(Xodb_page rv, Xow_ns ns, boolean timestamp_enabled);
	boolean Load_ttl			(Xodb_page rv, Xow_ns ns, byte[] ttl);
	boolean Load_ttl_by_id		(Xodb_page rv, int id);
	void Load_ttls_by_ids	(Cancelable cancelable, ListAdp list, int bgn, int end);
	void Load_search		(Cancelable cancelable, ListAdp rv, byte[] search, int results_max);
	boolean Load_ctg_v1		(Xoctg_view_ctg rv, byte[] ttl);
	boolean Load_ctg_v2		(Xoctg_data_ctg rv, byte[] ttl);
	void Load_ctg_v2a		(Xoctg_view_ctg rv, Xoctg_url url_ctg, byte[] ttl_bry, int limit);
	void Load_ttls_starting_with(Cancelable cancelable, ListAdp rslt_list, Xodb_page rslt_nxt, Xodb_page rslt_prv, IntRef rslt_count, Xow_ns ns, byte[] key, int max_results, int min_page_len, int browse_len, boolean include_redirects, boolean fetch_prv_item);
	int Load_ctg_count		(byte[] ttl);
	byte[] Find_random_ttl(Xow_ns ns);
	void Clear();	// TEST:helper function
	byte[] Load_qid(byte[] wiki_alias, byte[] ns_num, byte[] ttl);
	int Load_pid(byte[] lang_key, byte[] pid_name);
	Xodb_page[] Load_ctg_list(byte[][] ctg_ttls);
}
