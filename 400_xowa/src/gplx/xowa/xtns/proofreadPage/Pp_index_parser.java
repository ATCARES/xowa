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
package gplx.xowa.xtns.proofreadPage; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.xowa.parsers.logs.*;
class Pp_index_parser {
	public static Pp_index_page Parse(Xow_wiki wiki, Xop_ctx ctx, Xoa_ttl index_ttl, int ns_page_id) {
		byte[] src = wiki.Cache_mgr().Page_cache().Get_or_load_as_src(index_ttl);
		if (src == null) return Pp_index_page.Null;
		if (ctx.Wiki().View_data().Pages_recursed()) return Pp_index_page.Null;
		ctx.Wiki().View_data().Pages_recursed_(true);
		Xop_parser sub_parser = new Xop_parser(wiki.Parser().Tmpl_lxr_mgr(), wiki.Parser().Wiki_lxr_mgr());
		Xop_ctx sub_ctx = Xop_ctx.new_sub_(wiki);
		Xop_tkn_mkr tkn_mkr = sub_ctx.Tkn_mkr();
		Xop_root_tkn index_root = tkn_mkr.Root(src);
		byte[] mid_text = sub_parser.Parse_page_tmpl(index_root, sub_ctx, tkn_mkr, src);
		Pp_index_page rv = new Pp_index_page();
		Inspect_tmpl(rv, src, index_root, index_root.Subs_len(), ns_page_id, 1);
		sub_parser.Parse_page_wiki(index_root, sub_ctx, tkn_mkr, mid_text, Xop_parser_.Doc_bgn_bos);
		ctx.Wiki().View_data().Pages_recursed_(false);
		rv.Src_(mid_text);
		Inspect_wiki(rv, src, index_root, index_root.Subs_len(), ns_page_id, 1);
		return rv;
	}
	private static void Inspect_tmpl(Pp_index_page rv, byte[] src, Xop_tkn_itm_base owner, int owner_len, int ns_page_id, int depth) {
		for (int i = 0; i < owner_len; i++) {
			Xop_tkn_itm sub = owner.Subs_get(i);
			int sub_tid = sub.Tkn_tid();
			switch (sub_tid) {
				case Xop_tkn_itm_.Tid_tmpl_invk: {
					if (depth == 1) { // NOTE: only look at tmpls directly beneath root; note that this should be fine b/c [[Index:]] pages have a constrained form-fields GUI; ProofreadPage takes the form fields, and builds a template from it; DATE:2014-01-25
						Xot_invk_tkn invk = (Xot_invk_tkn)sub;
						int args_len = invk.Args_len();
						for (int j = 0; j < args_len; j++) {
							Arg_nde_tkn nde_tkn = invk.Args_get_by_idx(j);
							byte[] key = Get_bry(src, nde_tkn.Key_tkn());
							byte[] val = Get_bry(src, nde_tkn.Val_tkn());
							rv.Invk_args().Add(new Pp_index_arg(key, val));
						}
					}
					break;
				}
			}
			int sub_subs_len = sub.Subs_len();
			if (sub_subs_len > 0)
				Inspect_tmpl(rv, src, (Xop_tkn_itm_base)sub, sub_subs_len, ns_page_id, depth + 1);
		}
	}
	private static void Inspect_wiki(Pp_index_page rv, byte[] src, Xop_tkn_itm_base owner, int owner_len, int ns_page_id, int depth) {
		for (int i = 0; i < owner_len; i++) {
			Xop_tkn_itm sub = owner.Subs_get(i);
			int sub_tid = sub.Tkn_tid();
			switch (sub_tid) {
				case Xop_tkn_itm_.Tid_lnki: {
					Xop_lnki_tkn lnki = (Xop_lnki_tkn)sub;
					int sub_ns_id = lnki.Ns_id();
					if		(sub_ns_id == ns_page_id)		rv.Page_ttls().Add(lnki.Ttl());
					else if	(sub_ns_id == Xow_ns_.Id_main)	rv.Main_lnkis().Add(lnki);
					break;
				}
				case Xop_tkn_itm_.Tid_xnde: {
					Xop_xnde_tkn xnde = (Xop_xnde_tkn)sub;
					if (xnde.Tag().Id() == Xop_xnde_tag_.Tid_pagelist)
						rv.Pagelist_xndes().Add(xnde);
					break;
				}
			}
			int sub_subs_len = sub.Subs_len();
			if (sub_subs_len > 0)
				Inspect_wiki(rv, src, (Xop_tkn_itm_base)sub, sub_subs_len, ns_page_id, depth + 1);
		}
	}
	private static byte[] Get_bry(byte[] src, Arg_itm_tkn itm) {
		return ByteAry_.Mid(src, itm.Dat_bgn(), itm.Dat_end());
	}
}
class Pp_index_page {
	public Pp_index_page() {}
	public byte[] Src() {return src;} public Pp_index_page Src_(byte[] v) {src = v; return this;} private byte[] src;
	public ListAdp		Pagelist_xndes()	{return pagelist_xndes;} private ListAdp pagelist_xndes = ListAdp_.new_();
	public ListAdp		Page_ttls()			{return page_ttls;} private ListAdp page_ttls = ListAdp_.new_();
	public ListAdp		Main_lnkis()		{return main_lnkis;} private ListAdp main_lnkis = ListAdp_.new_();
	public ListAdp		Invk_args()			{return invk_args;} private ListAdp invk_args = ListAdp_.new_();
	public Xoa_ttl[] Get_ttls_rng(Xow_wiki wiki, int ns_page_id, byte[] bgn_page_bry, byte[] end_page_bry, IntRef bgn_page_ref, IntRef end_page_ref) {
		int list_len = page_ttls.Count(); if (list_len == 0) return Pp_pages_nde.Ttls_null;
		ListAdp rv = ListAdp_.new_();
		Xoa_ttl bgn_page_ttl = new_ttl_(wiki, ns_page_id, bgn_page_bry), end_page_ttl = new_ttl_(wiki, ns_page_id, end_page_bry);
		boolean add = bgn_page_ttl == Xoa_ttl.Null;		// if from is missing, default to bgn; EX: <pages index=A to="A/5"/>
		for (int i = 0; i < list_len; i++) {			// REF.MW:ProofreadPageRenderer|renderPages
			Xoa_ttl ttl = (Xoa_ttl)page_ttls.FetchAt(i);
			if (	ttl.Eq_page_db(bgn_page_ttl))	{add = Bool_.Y; bgn_page_ref.Val_(i);}
			if (add) rv.Add(ttl);
			if (	end_page_ttl != Xoa_ttl.Null		// if to is missing default to end;
				&&	ttl.Eq_page_db(end_page_ttl)
				)									{add = Bool_.N; end_page_ref.Val_(i);}
		}
		if (rv.Count() == 0) return Pp_pages_nde.Ttls_null;
		return (Xoa_ttl[])rv.XtoAry(Xoa_ttl.class);
	}
	private static Xoa_ttl new_ttl_(Xow_wiki wiki, int ns_page_id, byte[] bry) {return bry == null ? Xoa_ttl.Null : Xoa_ttl.parse_(wiki, ns_page_id, bry);}
	public static final Pp_index_page Null = new Pp_index_page();
}
class Pp_index_arg {
	public Pp_index_arg(byte[] key, byte[] val) {this.key = key; this.val = val;}
	public byte[] Key() {return key;} private byte[] key;
	public byte[] Val() {return val;} private byte[] val;
}
