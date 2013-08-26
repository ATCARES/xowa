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
package gplx.xowa.xtns.refs; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
public class Xtn_ref_grp {
	public Xtn_ref_grp(byte[] grp_name) {this.grp_name = grp_name;}
	public byte[] Grp_name() {return grp_name;} private byte[] grp_name;
	public int Grp_seal() {
		if (lst_idx == lsts.Count())	// NOTE: lst_idx == lsts.Count() if there are no itms in lst; need to add placeholder list anyway; <references> nodes has a List_index, which will call lsts.FetchAt(); see Empty_group_before_ref
			lsts.Add(new Xtn_ref_lst(grp_name));
		return lst_idx++;
	}	int lst_idx;
	public int Lsts_len() {return lsts.Count();}
	public Xtn_ref_lst Lsts_get_at(int i) {return i < lsts.Count() ? (Xtn_ref_lst)lsts.FetchAt(i) : null;}	// NOTE: null can be returned; see Infobox planet; w:Mars
	public void Lsts_add(byte[] itm_name, byte[] follow, Xtn_ref_nde itm) {
		Xtn_ref_lst lst = null;
		int lsts_len = lsts.Count();
		if (lst_idx >= lsts_len) {
			lst = new Xtn_ref_lst(grp_name);
			lsts.Add(lst);
		}
		else
			lst = (Xtn_ref_lst)lsts.FetchAt(lsts_len - 1);
		lst.Itms_add(itm_name, follow, itm);
	}
	public void Lsts_clear() {
		int lsts_len = lsts.Count();
		for (int i = 0; i < lsts_len; i++) {
			Xtn_ref_lst lst = (Xtn_ref_lst)lsts.FetchAt(i);
			lst.Itms_clear();
		}
		lsts.Clear();
		lst_idx = 0;
	}
	ListAdp lsts = ListAdp_.new_();
}	