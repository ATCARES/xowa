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
package gplx.xowa.html; import gplx.*; import gplx.xowa.*;
public class Xohp_ctg_grp_mgr {
	final ByteAryFmtr grp_fmtr = ByteAryFmtr.new_(String_.Concat_lines_nl_skipLast
	(	"<div id=\"catlinks\" class=\"catlinks\">"
	,	"  <div id=\"mw-normal-catlinks\" class=\"mw-normal-catlinks\">"
	,	"    ~{grp_lbl}"
	,	"    <ul>~{grp_itms}"
	,	"    </ul>"
	,	"  </div>"
	,	"</div>"
	),	"grp_lbl", "grp_itms")
	;
	final ByteAryFmtr itm_fmtr = ByteAryFmtr.new_(String_.Concat_lines_nl_skipLast
	(	""
	,	"      <li>"
	,	"        <a href=\"~{itm_href}\" class=\"internal\" title=\"~{itm_title}\">~{itm_text}</a>"	
	,	"      </li>"
	), 	"itm_href", "itm_title", "itm_text"
	);
	Xoh_ctg_itm_fmtr itm_mgr = new Xoh_ctg_itm_fmtr();
	public void Bld(ByteAryBfr bfr, Xoa_page page, int ctgs_len) {
		byte[] categories_lbl = page.Wiki().Msg_mgr().Val_by_id(Xol_msg_itm_.Id_ctg_tbl_hdr);
		itm_mgr.Set(page, itm_fmtr);
		grp_fmtr.Bld_bfr_many(bfr, categories_lbl, itm_mgr);
	}
}
class Xoh_ctg_itm_fmtr implements ByteAryFmtrArg {
	public void Set(Xoa_page page, ByteAryFmtr itm_fmtr) {this.page = page; this.itm_fmtr = itm_fmtr;} private Xoa_page page; ByteAryFmtr itm_fmtr;
	public void XferAry(ByteAryBfr bfr, int idx) {
		int ctgs_len = page.Category_list().length;
		ByteAryBfr tmp_bfr = page.Wiki().App().Utl_bry_bfr_mkr().Get_b128();
		ByteAryBfr tmp_href = page.Wiki().App().Utl_bry_bfr_mkr().Get_b128();
		byte[] ctg_prefix = page.Wiki().Ns_mgr().Ns_category().Name_db_w_colon();
		for (int i = 0; i < ctgs_len; i++) {
			byte[] page_name = page.Category_list()[i];
			tmp_bfr.Add(ctg_prefix).Add(page_name);
			page.Wiki().App().Href_parser().Build_to_bfr(tmp_bfr.XtoAryAndClear(), page.Wiki(), tmp_href);
			itm_fmtr.Bld_bfr(bfr, tmp_href.XtoAryAndClear(), page_name, page_name);
		}
		tmp_bfr.Mkr_rls();
		tmp_href.Mkr_rls();
	}	
}
