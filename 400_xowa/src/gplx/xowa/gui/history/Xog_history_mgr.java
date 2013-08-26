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
package gplx.xowa.gui.history; import gplx.*; import gplx.xowa.*; import gplx.xowa.gui.*;
public class Xog_history_mgr {
	private OrderedHash hash = OrderedHash_.new_bry_();
	private Xog_history_stack itm_stack = new Xog_history_stack();
	public int Count() {return hash.Count();}
	public void Add(Xoa_page page) {
		itm_stack.Add(page);
		byte[] page_key = Build_page_key(page);
		if (!hash.Has(page_key))
			hash.Add(page_key, page);
	}
	public void Update_html_doc_pos(Xoa_page page) {
		itm_stack.Update_html_doc_pos(page);
	}
	public Xoa_page Cur_page(Xow_wiki wiki) {return Get_or_fetch(wiki, itm_stack.Cur_itm());}
	public Xoa_page Go_bwd(Xow_wiki wiki) {return Go_by_dir(wiki, true);}
	public Xoa_page Go_fwd(Xow_wiki wiki) {return Go_by_dir(wiki, false);}
	public Xoa_page Go_by_dir(Xow_wiki wiki, boolean bwd) {
		Xog_history_itm itm = bwd ? itm_stack.Go_bwd() : itm_stack.Go_fwd();
		if (itm == Xog_history_itm.Null) return Xoa_page.Null;
		Xoa_page rv = Get_or_fetch(wiki, itm);
		byte[] anch_key = itm.Anch_key();
		rv.Url().Anchor_bry_(anch_key); // must override anchor as it may be different for cached page
		rv.DocPos_(itm.Html_doc_pos());
		wiki.Ctx().Tab().Display_ttl_(itm.Display_ttl());
		return rv;
	}
	private Xoa_page Get_or_fetch(Xow_wiki wiki, Xog_history_itm itm) {
		byte[] page_key = Build_page_key(itm.Wiki_key(), itm.Page_key());
		Xoa_page rv = (Xoa_page)hash.Fetch(page_key);
		if (rv != null) return rv;
		Xoa_ttl ttl = Xoa_ttl.parse_(wiki, itm.Page_key());
		return wiki.Data_mgr().Get_page(ttl, false);
	}
	private static byte[] Build_page_key(Xoa_page page) {return Build_page_key(page.Wiki().Domain_bry(), page.Page_ttl().Page_db());}
	private static byte[] Build_page_key(byte[] wiki_key, byte[] page_key) {return ByteAry_.Add_w_dlm(Byte_ascii.Pipe, wiki_key, page_key);}
}
