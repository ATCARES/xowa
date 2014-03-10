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
import gplx.xowa.xtns.refs.*;
public class Xop_tkn_mkr {
	Xop_space_tkn space_tkn_immutable = new Xop_space_tkn(true, -1, -1);
	public Xop_root_tkn Root(byte[] raw)													{return new Xop_root_tkn().Root_src_(raw);}
	public Xop_txt_tkn Txt(int bgn, int end)												{return new Xop_txt_tkn(bgn, end);}
//		public Xop_space_tkn Space(Xop_tkn_grp grp, int bgn, int end)							{grp.Subs_src_pos_(grp.Subs_len(), bgn, end); return space_tkn_immutable;}
	public Xop_space_tkn Space(Xop_tkn_grp grp, int bgn, int end)							{Xop_space_tkn rv = new Xop_space_tkn(false, bgn, end); grp.Subs_src_pos_(grp.Subs_len(), bgn, end); return rv;}
	public Xop_space_tkn Space_mutable(int bgn, int end)									{return new Xop_space_tkn(false, bgn, end);}
	public Xop_apos_tkn Apos(int bgn, int end
		, int aposLen, int typ, int cmd, int lit_apos, byte cur_tkn_tid)					{return new Xop_apos_tkn(bgn, end, aposLen, typ, cmd, lit_apos, cur_tkn_tid);}
	public Xop_tkn_itm HtmlRef(int bgn, int end, Xop_amp_trie_itm itm)						{return new Xop_html_ref_tkn(bgn, end, itm);}
	public Xop_tkn_itm HtmlNcr(int bgn, int end, int val_int, byte[] val_bry)				{return new Xop_html_ncr_tkn(bgn, end, val_int, val_bry);}
	public Xop_tkn_itm HtmlNcr(int bgn, int end, int val_int)								{return new Xop_html_ncr_tkn(bgn, end, val_int, gplx.intl.Utf8_.EncodeCharAsAry(val_int));}
	public Xop_nl_tkn NewLine(int bgn, int end, byte nl_typ, int nl_len)					{return new Xop_nl_tkn(bgn, end, nl_typ, nl_len);}
	public Xop_lnki_tkn Lnki(int bgn, int end)												{return (Xop_lnki_tkn)new Xop_lnki_tkn().Tkn_ini_pos(false, bgn, end);}
	public Xop_list_tkn List_bgn(int bgn, int end, byte listType, int symLen)				{return Xop_list_tkn.bgn_(bgn, end, listType, symLen);}
	public Xop_list_tkn List_end(int pos, byte listType)									{return Xop_list_tkn.end_(pos, listType);}
	public Xop_tkn_itm Pipe(int bgn, int end)												{return new Xop_pipe_tkn(bgn, end);}
	public Xop_tkn_itm Colon(int bgn, int end)												{return new Xop_colon_tkn(bgn, end);}
	public Xop_eq_tkn Eq(int bgn, int end, int eq_len)										{return new Xop_eq_tkn(bgn, end, eq_len);}
	public Xot_invk_tkn Tmpl_invk(int bgn, int end)											{return new Xot_invk_tkn(bgn, end);}
	public Arg_nde_tkn ArgNde(int arg_idx, int bgn)											{return new Arg_nde_tkn(arg_idx, bgn);}
	public Arg_itm_tkn ArgItm(int bgn, int end)												{return new Arg_itm_tkn_base(bgn, end);}
	public Xop_xnde_tkn Xnde(int bgn, int end)												{return (Xop_xnde_tkn)Xop_xnde_tkn.new_().Tkn_ini_pos(false, bgn, end);}
	public Xop_hdr_tkn Hdr(int bgn, int end, int hdr_len)									{return new Xop_hdr_tkn(bgn, end, hdr_len);}
	public Xop_hr_tkn Hr(int bgn, int end, int hr_len)										{return new Xop_hr_tkn(bgn, end, hr_len);}
	public Xop_tab_tkn Tab(int bgn, int end)												{return new Xop_tab_tkn(bgn, end);}
	public Xop_curly_bgn_tkn Tmpl_curly_bgn(int bgn, int end)								{return new Xop_curly_bgn_tkn(bgn, end);}
	public Xop_tkn_itm Brack_bgn(int bgn, int end)											{return new Xop_brack_bgn_tkn(bgn, end);}
	public Xop_tkn_itm Brack_end(int bgn, int end)											{return new Xop_brack_end_tkn(bgn, end);}
	public Xop_lnke_tkn Lnke(int bgn, int end, byte[] protocol, byte proto_tid, byte lnke_typ, int lnk_bgn, int lnk_end) {
		return new Xop_lnke_tkn(bgn, end, protocol, proto_tid, lnke_typ, lnk_bgn, lnk_end);
	}
	public Xop_tblw_tb_tkn Tblw_tb(int bgn, int end, boolean tblw_xml)							{return new Xop_tblw_tb_tkn(bgn, end, tblw_xml);}
	public Xop_tblw_tr_tkn Tblw_tr(int bgn, int end, boolean tblw_xml)							{return new Xop_tblw_tr_tkn(bgn, end, tblw_xml);}
	public Xop_tblw_td_tkn Tblw_td(int bgn, int end, boolean tblw_xml)							{return new Xop_tblw_td_tkn(bgn, end, tblw_xml);}
	public Xop_tblw_th_tkn Tblw_th(int bgn, int end, boolean tblw_xml)							{return new Xop_tblw_th_tkn(bgn, end, tblw_xml);}
	public Xop_tblw_tc_tkn Tblw_tc(int bgn, int end, boolean tblw_xml)							{return new Xop_tblw_tc_tkn(bgn, end, tblw_xml);}
	public Xot_prm_tkn Tmpl_prm(int bgn, int end)											{return new Xot_prm_tkn(bgn, end);}
	public Xop_para_tkn Para(int pos)														{return new Xop_para_tkn(pos);}
	public Xop_pre_tkn Para_pre_bgn(int pos)												{return new Xop_pre_tkn(pos, pos, Xop_pre_tkn.Pre_tid_bgn, null);}
	public Xop_pre_tkn Para_pre_end(int pos, Xop_tkn_itm bgn)								{return new Xop_pre_tkn(pos, pos, Xop_pre_tkn.Pre_tid_end, bgn);}
	public Xop_ignore_tkn Ignore(int bgn, int end, byte ignore_type)						{return new Xop_ignore_tkn(bgn, end, ignore_type);}
	public Xop_bry_tkn Bry(int bgn, int end, byte[] bry)									{return new Xop_bry_tkn(bgn, end, bry);}
	public Xop_under_tkn Under(int bgn, int end, int v)										{return new Xop_under_tkn(bgn, end, v);}
	public gplx.xowa.xtns.xowa_cmds.Xop_xowa_cmd Xnde_xowa_cmd()							{return new gplx.xowa.xtns.xowa_cmds.Xop_xowa_cmd();}
	public gplx.xowa.xtns.poems.Poem_nde Xnde_poem()										{return new gplx.xowa.xtns.poems.Poem_nde();}
	public Ref_nde Xnde_ref()															{return new Ref_nde();}
	public References_nde Xnde_references()												{return new References_nde();}
	public gplx.xowa.xtns.gallery.Gallery_nde Xnde_gallery()								{return new gplx.xowa.xtns.gallery.Gallery_nde();}
	public gplx.xowa.xtns.imageMap.Xop_imageMap_xnde Xnde_imageMap()						{return new gplx.xowa.xtns.imageMap.Xop_imageMap_xnde();}
	public gplx.xowa.xtns.hiero.Hiero_nde Xnde_hiero()									{return new gplx.xowa.xtns.hiero.Hiero_nde();}
	public gplx.xowa.xtns.proofreadPage.Pp_pages_nde Xnde_pages()							{return new gplx.xowa.xtns.proofreadPage.Pp_pages_nde();}
	public gplx.xowa.xtns.proofreadPage.Pp_pagelist_nde Xnde_pagelist()						{return new gplx.xowa.xtns.proofreadPage.Pp_pagelist_nde();}
	public gplx.xowa.xtns.proofreadPage.Pp_pagequality_nde Xnde_pagequality()				{return new gplx.xowa.xtns.proofreadPage.Pp_pagequality_nde();}
	public gplx.xowa.xtns.lst.Lst_section_nde Xnde_section()								{return new gplx.xowa.xtns.lst.Lst_section_nde();}
	public gplx.xowa.xtns.categoryList.Xtn_categorylist_nde Xnde_categoryList()				{return new gplx.xowa.xtns.categoryList.Xtn_categorylist_nde();}
	public gplx.xowa.xtns.dynamicPageList.Dpl_xnde Xnde_dynamicPageList()					{return new gplx.xowa.xtns.dynamicPageList.Dpl_xnde();}
	public gplx.xowa.xtns.syntaxHighlight.Xtn_syntaxHighlight_nde Xnde_syntaxHighlight()	{return new gplx.xowa.xtns.syntaxHighlight.Xtn_syntaxHighlight_nde();}
	public gplx.xowa.xtns.templateData.Xtn_templateData_nde Xnde_templateData()				{return new gplx.xowa.xtns.templateData.Xtn_templateData_nde();}
	public gplx.xowa.xtns.rss.Rss_xnde Xnde_rss()											{return new gplx.xowa.xtns.rss.Rss_xnde();}
	public gplx.xowa.xtns.listings.Listing_xnde Xnde_listing(int tag_id)					{return new gplx.xowa.xtns.listings.Listing_xnde(tag_id);}
	public gplx.xowa.xtns.scores.Score_xnde Xnde_score()									{return new gplx.xowa.xtns.scores.Score_xnde();}
	public gplx.xowa.xtns.inputBox.Xtn_inputbox_nde Xnde_inputbox()							{return new gplx.xowa.xtns.inputBox.Xtn_inputbox_nde();}
	public gplx.xowa.xtns.translates.Xop_translate_xnde Xnde_translate()					{return new gplx.xowa.xtns.translates.Xop_translate_xnde();}
	public gplx.xowa.xtns.translates.Xop_languages_xnde Xnde_languages()					{return new gplx.xowa.xtns.translates.Xop_languages_xnde();}
	public gplx.xowa.xtns.translates.Xop_tvar_tkn Tvar(int tkn_bgn, int tkn_end, int key_bgn, int key_end, int txt_bgn, int txt_end) {return new gplx.xowa.xtns.translates.Xop_tvar_tkn(tkn_bgn, tkn_end, key_bgn, key_end, txt_bgn, txt_end);}
	public gplx.xowa.langs.vnts.Xop_vnt_tkn Vnt(int bgn_lhs, int bgn_rhs)					{return new gplx.xowa.langs.vnts.Xop_vnt_tkn(bgn_lhs, bgn_rhs);}
	public gplx.xowa.langs.vnts.Xop_vnt_eqgt_tkn Vnt_eqgt(int bgn, int end)					{return new gplx.xowa.langs.vnts.Xop_vnt_eqgt_tkn(bgn, end);}

//		public void Clear() {
//			space_tkns_len = txt_tkns_len = 0;
//		}
//		public Xop_txt_tkn Txt(int bgn, int end) {
//			Xop_txt_tkn rv = null;
//			if (txt_tkns_len < txt_tkns_max) {
//				rv = txt_tkns[txt_tkns_len];
//				if (rv == null) {
//					rv = new Xop_txt_tkn(bgn, end);
//					txt_tkns[txt_tkns_len] = rv;
//				}
//				else {
//					rv.Reset();
//					rv.Src_rng_(bgn, end);
//				}
//				txt_tkns_len++;
//			}
//			else {
//				rv = new Xop_txt_tkn(bgn, end);
//				Txt_tkns_add(rv);
//			}
//			return rv;
////			return new Xop_txt_tkn(bgn, end);
//		}
//		public Xop_space_tkn Space(int bgn, int end)											{
//			Xop_space_tkn rv = null;
//			if (space_tkns_len < space_tkns_max) {
//				rv = space_tkns[space_tkns_len];
//				if (rv == null) {
//					rv = new Xop_space_tkn(bgn, end);
//					space_tkns[space_tkns_len] = rv;
//				}
//				else {
//					rv.Reset();
//					rv.Src_rng_(bgn, end);
//				}
//				space_tkns_len++;
//			}
//			else {
//				rv = new Xop_space_tkn(bgn, end);
//				Space_tkns_add(rv);
//			}
//			return rv;
////			return new Xop_space_tkn(bgn, end);
//		}
//		private void Txt_tkns_add(Xop_txt_tkn sub) {
//			int new_len = txt_tkns_len + 1;
//			if (new_len > txt_tkns_max) {
//				txt_tkns_max = new_len * 2;
//				txt_tkns = Resize(txt_tkns, txt_tkns_len, txt_tkns_max);
//			}
//			txt_tkns[txt_tkns_len] = sub;
//			txt_tkns_len = new_len;
//		}	private Xop_txt_tkn[] txt_tkns = new Xop_txt_tkn[0]; int txt_tkns_len, txt_tkns_max;
//		Xop_txt_tkn[] Resize(Xop_txt_tkn[] src, int cur_len, int new_len) {
//			Xop_txt_tkn[] rv = new Xop_txt_tkn[new_len];
//			for (int i = 0; i < cur_len; i++)
//				rv[i] = src[i];
//			return rv;
//		}
//		private void Space_tkns_add(Xop_space_tkn sub) {
//			int new_len = space_tkns_len + 1;
//			if (new_len > space_tkns_max) {
//				space_tkns_max = new_len * 2;
//				space_tkns = Resize(space_tkns, space_tkns_len, space_tkns_max);
//			}
//			space_tkns[space_tkns_len] = sub;
//			space_tkns_len = new_len;
//		}	private Xop_space_tkn[] space_tkns = new Xop_space_tkn[0]; int space_tkns_len, space_tkns_max;
//		Xop_space_tkn[] Resize(Xop_space_tkn[] src, int cur_len, int new_len) {
//			Xop_space_tkn[] rv = new Xop_space_tkn[new_len];
//			for (int i = 0; i < cur_len; i++)
//				rv[i] = src[i];
//			return rv;
//		}
}
