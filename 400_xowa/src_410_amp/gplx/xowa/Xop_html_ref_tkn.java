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
public class Xop_html_ref_tkn extends Xop_tkn_itm_base {
	@Override public byte Tkn_tid() {return Xop_tkn_itm_.Tid_html_ref;}
	public int HtmlRef_val() {return htmlRef_itm.Char_int();}
	public Xop_amp_trie_itm HtmlRef_itm() {return htmlRef_itm;} private Xop_amp_trie_itm htmlRef_itm;
	public Xop_html_ref_tkn(int bgn, int end, Xop_amp_trie_itm htmlRef_itm) {
		this.htmlRef_itm = htmlRef_itm;
		this.Tkn_ini_pos(false, bgn, end);
	}
}
