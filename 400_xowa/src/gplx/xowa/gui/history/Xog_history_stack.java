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
package gplx.xowa.gui.history; import gplx.*; import gplx.xowa.*; import gplx.xowa.gui.*;
public class Xog_history_stack {
	private ListAdp list = ListAdp_.new_();
	public int Stack_pos() {return list_pos;} private int list_pos = 0;
	public int Count() {return list.Count();}
	public void Clear() {list.Clear(); list_pos = 0;}
	public Xog_history_itm Cur_itm() {return list.Count() == 0 ? Xog_history_itm.Null : (Xog_history_itm)list.FetchAt(list_pos);}
	public Xog_history_itm Add(Xoa_page page) {
		byte[] wiki_key = page.Wiki().Domain_bry();
		byte[] page_key = page.Ttl().Full_url();	// get page_name only (no anchor; no query args)
		byte[] anch_key = page.Url().Anchor_bry();
		byte[] qarg_key = page.Url().Args_all_as_bry();
		byte[] redirect_force = page.Url().Redirect_force() ? Bool_.Y_bry : Bool_.N_bry;
		byte[] key = Xog_history_itm.Build_key(wiki_key, page_key, anch_key, qarg_key, redirect_force);
		Xog_history_itm cur_itm = this.Cur_itm(); 
		if (	cur_itm != Xog_history_itm.Null
			&&	ByteAry_.Eq(wiki_key, cur_itm.Wiki_key())	// do not add if last itm is same;
			&&	ByteAry_.Eq(page_key, cur_itm.Page_key())
			&&	ByteAry_.Eq(anch_key, cur_itm.Anch_key())
			&&	ByteAry_.Eq(qarg_key, cur_itm.Qarg_key())
			&&	ByteAry_.Eq(redirect_force, cur_itm.Redirect_force())
			)
			return Xog_history_itm.Null;
		Xog_history_itm itm = new Xog_history_itm(key, wiki_key, page_key, anch_key, qarg_key, redirect_force, page.Html_data().Bmk_pos());
		itm.Display_ttl_(page.Wiki().Ctx().Tab().Display_ttl());
		Del_all_from(list_pos + 1);
		list.Add(itm);
		list_pos = list.Count() - 1;
		return itm;
	}
	public void Update_html_doc_pos(Xoa_page page, byte nav_type) {
		Xog_history_itm itm = Get_recent(page, nav_type);
		if (itm != null) itm.Html_doc_pos_(page.Html_data().Bmk_pos());
	}
	private Xog_history_itm Get_recent(Xoa_page page, byte nav_type) {
		int pos = -1;
		switch (nav_type) {
			case Xog_history_stack.Nav_fwd:			pos = list_pos - 1; break;
			case Xog_history_stack.Nav_bwd:			pos = list_pos + 1; break;
			case Xog_history_stack.Nav_by_anchor:	pos = list_pos; break;
		}
		return (pos < 0 || pos >= list.Count())
			? null
			: (Xog_history_itm)list.FetchAt(pos)
			;
	}
	public Xog_history_itm Go_bwd() {
		if (list.Count() == 0) return Xog_history_itm.Null;
		--list_pos;
		if (list_pos < 0) list_pos = 0; 
		return this.Cur_itm();
	}
	public Xog_history_itm Go_fwd() {
		int list_count = list.Count();
		if (list_count == 0) return Xog_history_itm.Null;
		++list_pos;
		if (list_pos == list_count) list_pos = list_count - 1;
		return this.Cur_itm();
	}
	private void Del_all_from(int from) {
		int list_count = list.Count();
		if (from <= list_count - 1)
			list.Del_range(from, list_count - 1);
	}
	public static final byte Nav_fwd = 1, Nav_bwd = 2, Nav_by_anchor = 3;
}
