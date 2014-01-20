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
import gplx.xowa.xtns.lst.*;
public class Pp_pages_nde implements Xop_xnde_xtn, Xop_xnde_atr_parser {
	public Xop_root_tkn Xtn_root() {return xtn_root;} private Xop_root_tkn xtn_root;
	public boolean Xtn_literal() {return xtn_literal;} private boolean xtn_literal = false;
	private byte[] index_ttl_bry, bgn_page_bry, end_page_bry, bgn_sect_bry, end_sect_bry;		
	private int step_int;
	private byte[] include, exclude, step_bry;//, onlysection, header;
	private int ns_index_id = Int_.MinValue, ns_page_id = Int_.MinValue;
	private Xow_ns ns_page;
	private Xoa_ttl index_ttl;
	private Xoa_app app; private Xow_wiki wiki; private Xop_ctx ctx; private Gfo_usr_dlg usr_dlg;
	private byte[] src; private Xop_xnde_tkn xnde_tkn;
	private Xoa_ttl cur_page_ttl;
	public void Xatr_parse(Xow_wiki wiki, byte[] src, Xop_xatr_itm xatr, Object xatr_key_obj) {
		if (xatr_key_obj == null) return;
		ByteVal xatr_key = (ByteVal)xatr_key_obj;
		switch (xatr_key.Val()) {
			case Xatr_index_ttl:	index_ttl_bry	= xatr.Val_as_bry(src); break;
			case Xatr_bgn_page:		bgn_page_bry	= xatr.Val_as_bry(src); break;
			case Xatr_end_page:		end_page_bry	= xatr.Val_as_bry(src); break;
			case Xatr_bgn_sect:		bgn_sect_bry	= xatr.Val_as_bry(src); break;
			case Xatr_end_sect:		end_sect_bry	= xatr.Val_as_bry(src); break;
			case Xatr_include:		include			= xatr.Val_as_bry(src); break;
			case Xatr_exclude:		exclude			= xatr.Val_as_bry(src); break;
			case Xatr_step:			step_bry		= xatr.Val_as_bry(src); break;
//				case Xatr_onlysection:	onlysection		= xatr.Val_as_bry(src); break;
//				case Xatr_header:		header			= xatr.Val_as_bry(src); break;
		}			
	}
	public void Xtn_compile(Xow_wiki wiki, Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, Xop_xnde_tkn xnde) {
		if (!Init_vars(wiki, ctx, src, xnde)) return;
		Xoa_ttl[] ttl_ary = Get_ttls(); if (ttl_ary == Ary_empty) return;
		Bld_root(ttl_ary);
	}
	private boolean Init_vars(Xow_wiki wiki, Xop_ctx ctx, byte[] src, Xop_xnde_tkn xnde) {
		this.wiki = wiki; this.ctx = ctx; app = wiki.App(); usr_dlg = app.Usr_dlg();
		this.src = src; this.xnde_tkn = xnde; cur_page_ttl = ctx.Page().Page_ttl();
		Xop_xatr_itm.Xatr_parse(app, this, xtn_atrs, wiki, src, xnde);
		ByteAryBfr tmp_bfr = wiki.Utl_bry_bfr_mkr().Get_b128();
		ByteTrieMgr_slim amp_trie = wiki.App().Amp_trie();
		index_ttl_bry = Xop_amp_wkr.Parse(tmp_bfr, amp_trie, index_ttl_bry);
		bgn_page_bry = Xop_amp_wkr.Parse(tmp_bfr, amp_trie, bgn_page_bry);
		end_page_bry = Xop_amp_wkr.Parse(tmp_bfr, amp_trie, end_page_bry);
		tmp_bfr.Mkr_rls();
		Xowc_xtn_pages cfg_pages = wiki.Cfg_parser().Xtns().Itm_pages();
		if (cfg_pages.Init_needed()) cfg_pages.Init(wiki.Ns_mgr());
		ns_index_id = cfg_pages.Ns_index_id(); if (ns_index_id == Int_.MinValue) return Fail_msg("wiki does not have an Index ns");
		ns_page_id  = cfg_pages.Ns_page_id();  if (ns_page_id  == Int_.MinValue) return Fail_msg("wiki does not have a Page ns");	// occurs when <pages> used in a wiki without a "Page:" ns; EX: de.w:Help:Buchfunktion/Feedback
		index_ttl = Xoa_ttl.parse_(wiki, ns_index_id, index_ttl_bry); if (index_ttl == null) return Fail_args("index title is not valid: index={0}", String_.new_utf8_(index_ttl_bry));
		ns_page = wiki.Ns_mgr().Get_by_id(ns_page_id);
		return true;
	}
	private Xoa_ttl[] Get_ttls() {
		Xoa_ttl[] rv = Get_ttls_from_index_wikitext();
		if (rv == Ary_empty) {	// [[Index:*]] does not have any [[Page:*]] links
			rv = Get_ttls_from_xnde_args();
			if (rv == Ary_empty) {
				Fail_msg("no index ttls found");					
				return Ary_empty;
			}
		}
		return rv;
	}
	private Xoa_ttl[] Get_ttls_from_index_wikitext() {
		Xoa_page index_page = wiki.Data_mgr().Get_page(index_ttl, false);
		if (ctx.Wiki().View_data().Lst_recursed()) return Ary_empty;
		ctx.Wiki().View_data().Lst_recursed_(true);
		Xop_root_tkn index_root = wiki.Parser().Parse_recurse(ctx, index_page.Data_raw(), true);
		ctx.Wiki().View_data().Lst_recursed_(false);
		ListAdp found = ListAdp_.new_();
		Get_ttls_from_index_wikitext_recur(index_root, index_root.Subs_len(), found);
		ListAdp rv = ListAdp_.new_();
		Xoa_ttl bgn_page_ttl = Get_ttls_from_index_wikitext__ttl(bgn_page_bry), end_page_ttl = Get_ttls_from_index_wikitext__ttl(end_page_bry);
		boolean add = bgn_page_ttl == Xoa_ttl.Null;	// if from is missing, default to bgn; EX: <pages index=A to="A/5"/>
		int found_len = found.Count();
		for (int i = 0; i < found_len; i++) {	// REF.MW:ProofreadPageRenderer|renderPages
			Xoa_ttl found_ttl = (Xoa_ttl)found.FetchAt(i);
			if (found_ttl.Eq_page_db(bgn_page_ttl)) add = true;
			if (add) rv.Add(found_ttl);
			if (	end_page_ttl != Xoa_ttl.Null		// if to is missing default to end;
				&&	found_ttl.Eq_page_db(end_page_ttl)
				)									add = false;
		}
		return rv.Count() == 0 ? Ary_empty : (Xoa_ttl[])rv.XtoAry(Xoa_ttl.class);
	}
	private Xoa_ttl Get_ttls_from_index_wikitext__ttl(byte[] bry) {
		return bry == null ? Xoa_ttl.Null : Xoa_ttl.parse_(wiki, ns_page_id, bry);
	}
	private void Get_ttls_from_index_wikitext_recur(Xop_tkn_itm_base owner, int owner_len, ListAdp found) {
		for (int i = 0; i < owner_len; i++) {
			Xop_tkn_itm sub = owner.Subs_get(i);
			if (sub.Tkn_tid() == Xop_tkn_itm_.Tid_lnki) {
				Xop_lnki_tkn sub_as_lnki = (Xop_lnki_tkn)sub;
				if (sub_as_lnki.Ns_id() == ns_page_id) {
					Xoa_ttl sub_ttl = sub_as_lnki.Ttl();
					found.Add(sub_ttl);
				}
			}
			int sub_subs_len = sub.Subs_len();
			if (sub_subs_len > 0)
				Get_ttls_from_index_wikitext_recur((Xop_tkn_itm_base)sub, sub_subs_len, found);
		}
	}
	private Xoa_ttl[] Get_ttls_from_xnde_args() {
		if (!Chk_step()) return Ary_empty;
		ListAdp list = ListAdp_.new_();
		list = Get_ttls_from_xnde_args__include(list);	if (list == null) return Ary_empty;
		list = Get_ttls_from_xnde_args__rng(list);		if (list == null) return Ary_empty;
		list = Get_ttls_from_xnde_args__exclude(list);	if (list == null) return Ary_empty;
		if (include != null || exclude != null)	// sort if include / exclude specified; will skip sort if only range specified
			list.Sort();
		return Get_ttls_from_xnde_args__ttls(list);
	}
	private ListAdp Get_ttls_from_xnde_args__include(ListAdp list) {
		if (ByteAry_.Len_eq_0(include)) return list;	// include is blank; exit early;
		int[] include_pages = Int_ary_.Parse_list_or(include, null);
		if (include_pages == null) {
			Fail_args("pages node does not have a valid 'include': include={0}", String_.new_utf8_(include));
			return null;
		}
		int include_pages_len = include_pages.length;
		for (int i = 0; i < include_pages_len; i++)
			list.Add(IntVal.new_(include_pages[i]));
		return list;
	}
	private ListAdp Get_ttls_from_xnde_args__rng(ListAdp list) {
		if (bgn_page_bry == null && end_page_bry == null) return list;	// from and to are blank; exit early
		NumberParser num_parser = wiki.App().Utl_num_parser();
		int bgn_page_int = 0;	// NOTE: default to 0 (1st page)
		if (ByteAry_.Len_gt_0(bgn_page_bry)) {
			num_parser.Parse(bgn_page_bry);
			if (num_parser.HasErr()) {
				Fail_args("pages node does not have a valid 'from': from={0}", String_.new_utf8_(bgn_page_bry));
				return null;
			}
			else
				bgn_page_int = num_parser.AsInt();
		}
		int end_page_int = 0;	
		if (ByteAry_.Len_eq_0(end_page_bry)) 
			end_page_int = Get_max_page_idx(wiki, index_ttl);
		else {
			num_parser.Parse(end_page_bry);
			if (num_parser.HasErr()) {
				Fail_args("pages node does not have a valid 'to': to={0}", String_.new_utf8_(bgn_page_bry));
				return null;
			}
			else
				end_page_int = num_parser.AsInt();
		}
		if (bgn_page_int > end_page_int) {
			Fail_args("from must be less than to: from={0} to={0}", bgn_page_int, end_page_int);
			return null;
		}
		int rng_len = end_page_int - bgn_page_int + ListAdp_.Base1;	// EX: from=3 to=4 has len of 2
		for (int i = 0; i < rng_len; i ++)
			list.Add(IntVal.new_(i + bgn_page_int));
		return list;
	}
	private int Get_max_page_idx(Xow_wiki wiki, Xoa_ttl index_ttl) {
		ListAdp rslt = ListAdp_.new_();
		IntRef rslt_count = IntRef.zero_();
		wiki.Db_mgr().Load_mgr().Load_ttls_for_all_pages(Cancelable_.Never, rslt, tmp_page, tmp_page, rslt_count, ns_page, index_ttl.Page_db(), Int_.MaxValue, 0, Int_.MaxValue, false, false);
		int len = rslt_count.Val();
		int page_leaf_max = 0;
		for (int i = 0; i < len; i++) {
			Xodb_page page = (Xodb_page)rslt.FetchAt(i);
			Xoa_ttl page_ttl = Xoa_ttl.parse_(wiki, ns_page_id, page.Ttl_wo_ns());		if (page_ttl == null) continue;					// page_ttl is not valid; should never happen;
			byte[] page_ttl_leaf = page_ttl.Leaf_txt();									if (page_ttl_leaf == null) continue;			// page is not leaf; should not happen
			int page_leaf_val = ByteAry_.XtoIntOr(page_ttl_leaf, Int_.MinValue);		if (page_leaf_val == Int_.MinValue) continue;	// leaf is not int; ignore
			if (page_leaf_val > page_leaf_max) page_leaf_max = page_leaf_val;
		}
		return page_leaf_max;
	}	private static Xodb_page tmp_page = new Xodb_page();	// tmp_page passed to Load_ttls_for_all_pages; values are never looked at, so use static instance
	private ListAdp Get_ttls_from_xnde_args__exclude(ListAdp list) {
		if (ByteAry_.Len_eq_0(exclude)) return list;	// exclude is blank; exit early;
		int[] exclude_pages = Int_ary_.Parse_list_or(exclude, null);
		if (exclude_pages == null) {
			Fail_args("pages node does not have a valid 'exclude': exclude={0}", String_.new_utf8_(exclude));
			return null;
		}
		HashAdp exclude_pages_hash = HashAdp_.new_();
		int exclude_pages_len = exclude_pages.length;
		for (int i = 0; i < exclude_pages_len; i++) {
			IntVal exclude_page = IntVal.new_(exclude_pages[i]);
			if (!exclude_pages_hash.Has(exclude_page))
				exclude_pages_hash.Add(exclude_page, exclude_page);
		}
		ListAdp new_list = ListAdp_.new_();
		int list_len = list.Count();
		for (int i = 0; i < list_len; i++) {
			IntVal page = (IntVal)list.FetchAt(i);
			if (exclude_pages_hash.Has(page)) continue;
			new_list.Add(page);
		}
		return new_list;
	}
	private Xoa_ttl[] Get_ttls_from_xnde_args__ttls(ListAdp list) {
		ByteAryBfr ttl_bfr = app.Utl_bry_bfr_mkr().Get_b512();
		int list_len = list.Count();
		Xoa_ttl[] rv = new Xoa_ttl[(list_len / step_int) + ((list_len % step_int == 0) ? 0 : 1)];
		int rv_idx = 0;
		for (int i = 0; i < list_len; i += step_int) {
			IntVal page = (IntVal)list.FetchAt(i);
			ttl_bfr.Add(ns_page.Name_db_w_colon())		// EX: 'Page:'
				.Add(index_ttl_bry)						// EX: 'File.djvu'
				.Add_byte(Byte_ascii.Slash)				// EX: '/'
				.Add_int_variable(page.Val());			// EX: '123'
			rv[rv_idx++] = Xoa_ttl.parse_(wiki, ttl_bfr.XtoAryAndClear());
		}
		ttl_bfr.Mkr_rls(); 
		return rv;
	}
	private boolean Chk_step() {
		if (step_bry == null) {
			step_int = 1;
			return true;
		}
		step_int = ByteAry_.XtoIntOr(step_bry, Int_.MinValue);
		if (step_int < 1 || step_int > 1000) {
			Fail_args("pages node does not have a valid 'step': step={0}", String_.new_utf8_(step_bry));
			return false;
		}
		return true;
	}
	private void Bld_root(Xoa_ttl[] ary) {
		Hash_adp_bry lst_page_regy = ctx.Lst_page_regy(); if (lst_page_regy == null) lst_page_regy = new Hash_adp_bry(true);	// SEE:NOTE:page_regy; DATE:2014-01-01
		int ary_len = ary.length;
		Xoa_ttl bgn_page_ttl = bgn_page_bry == null ? null : ary[0];
		Xoa_ttl end_page_ttl = end_page_bry == null ? null : ary[ary_len - 1];
		
		ByteAryBfr tmp_bfr = app.Utl_bry_bfr_mkr().Get_m001();
		Lst_pfunc_wkr lst_pfunc_wkr = new Lst_pfunc_wkr();
		for (int i = 0; i < ary_len; i++) {
			Xoa_ttl ttl = ary[i];
			byte[] ttl_page_db = ttl.Page_db();
			if (lst_page_regy.Get_by_bry(ttl_page_db) == null)	// check if page was already added; avoids recursive <page> calls which will overflow stack; DATE:2014-01-01
				lst_page_regy.Add(ttl_page_db, ttl_page_db);
			else
				continue;
			byte[] cur_sect_bgn = Lst_pfunc_wkr.Null_arg, cur_sect_end = Lst_pfunc_wkr.Null_arg;
			if		(ttl.Eq_page_db(bgn_page_ttl)) {
				if	(bgn_sect_bry != null)
					cur_sect_bgn = bgn_sect_bry;
			}
			else if	(ttl.Eq_page_db(end_page_ttl)) {
				if	(end_sect_bry != null)
					cur_sect_end = end_sect_bry;
			}
			lst_pfunc_wkr.Init_include(ttl.Full_db(), cur_sect_bgn, cur_sect_end).Exec(tmp_bfr, ctx);
		}			
		byte[] wikitext = tmp_bfr.XtoAry();
		xtn_root = Bld_root(tmp_bfr, lst_page_regy, wikitext);
		tmp_bfr.Mkr_rls();
	}
	private Xop_root_tkn Bld_root(ByteAryBfr page_bfr, Hash_adp_bry lst_page_regy, byte[] wikitext) {
		Xop_ctx tmp_ctx = Xop_ctx.new_sub_page_(wiki, ctx, lst_page_regy);
		Xop_root_tkn rv = tmp_ctx.Tkn_mkr().Root(wikitext);
		tmp_ctx.Parse_tid_(Xop_parser_.Parse_tid_tmpl);
		tmp_ctx.Page().Page_ttl_(ctx.Page().Page_ttl());	// NOTE: must set tmp_ctx.Ttl to ctx.Ttl; EX: Flatland and First World; DATE:2013-04-29
		Xot_defn_tmpl tmp_tmpl = wiki.Parser().Parse_tmpl(tmp_ctx, tmp_ctx.Tkn_mkr(), wiki.Ns_mgr().Ns_template(), ByteAry_.Empty, wikitext); 
		page_bfr.Clear();
		tmp_tmpl.Tmpl_evaluate(tmp_ctx, Xot_invk_temp.PageIsCaller, page_bfr);
		byte[] compiled = page_bfr.XtoAryAndClear();
		wiki.Parser().Parse_page_all(rv, tmp_ctx, ctx.Tkn_mkr(), compiled, -1);
		rv.Root_src_(rv.Root_src());
		return rv;
	}
	private static Hash_adp_bry xtn_atrs = new Hash_adp_bry(false)	// NOTE: these do not seem to be i18n'd; no ProofreadPage.magic.php; ProofreadPage.i18n.php only has messages; ProofreadPage.body.php refers to names literally
	.Add_str_obj("index"		, ByteVal.new_(Pp_pages_nde.Xatr_index_ttl))
	.Add_str_obj("from"			, ByteVal.new_(Pp_pages_nde.Xatr_bgn_page))
	.Add_str_obj("to"			, ByteVal.new_(Pp_pages_nde.Xatr_end_page))
	.Add_str_obj("fromsection"	, ByteVal.new_(Pp_pages_nde.Xatr_bgn_sect))
	.Add_str_obj("tosection"	, ByteVal.new_(Pp_pages_nde.Xatr_end_sect))
	.Add_str_obj("include"		, ByteVal.new_(Pp_pages_nde.Xatr_include))
	.Add_str_obj("exclude"		, ByteVal.new_(Pp_pages_nde.Xatr_exclude))
	.Add_str_obj("onlysection"	, ByteVal.new_(Pp_pages_nde.Xatr_onlysection))
	.Add_str_obj("step"			, ByteVal.new_(Pp_pages_nde.Xatr_step))
	.Add_str_obj("header"		, ByteVal.new_(Pp_pages_nde.Xatr_header))
	;
	public static final byte
	  Xatr_index_ttl	= 0
	, Xatr_bgn_page		= 1
	, Xatr_end_page		= 2
	, Xatr_bgn_sect		= 3
	, Xatr_end_sect		= 4
	, Xatr_include		= 5
	, Xatr_exclude		= 6
	, Xatr_onlysection	= 7
	, Xatr_step			= 8
	, Xatr_header		= 9
	;

	public static final Xoa_ttl[] Ary_empty = null;
	private String Fail_msg_suffix() {
		return String_.Format(" ttl={0} src={1}", String_.new_utf8_(cur_page_ttl.Full_db()), String_.new_utf8_(src, xnde_tkn.Src_bgn(), xnde_tkn.Src_end()));
	}
	private String Fail_msg_basic(String msg) {return msg + ";" + Fail_msg_suffix();}
	private String Fail_msg_custom(String fmt, Object... args) {return String_.Format(fmt, args) + Fail_msg_suffix();}
	private boolean Fail_msg(String msg) {
		xtn_literal = true;
		usr_dlg.Warn_many("", "", String_.Replace(Fail_msg_basic(msg), "\n", ""));
		return false;
	}
	private boolean Fail_args(String fmt, Object... args) {
		xtn_literal = true;
		usr_dlg.Warn_many("", "", String_.Replace(Fail_msg_custom(fmt, args), "\n", ""));
		return false;
	}
}

/*
NOTE:page_regy
. original implmentation was following
in Xop_ctx
	public Hash_adp_bry			Lst_page_regy()		{if (lst_page_regy == null) lst_page_regy = new Hash_adp_bry(true); return lst_page_regy;} 
in Pp_pages_nde
	Hash_adp_bry lst_page_regy = ctx.Lst_page_regy();
. current implementation is following
in Xop_ctx
	public Hash_adp_bry			Lst_page_regy()		{return lst_page_regy;} 
in Pp_pages_nde
	Hash_adp_bry lst_page_regy = ctx.Lst_page_regy();
	if (lst_page_regy == null) lst_page_regy = new Hash_adp_bry(true);
. note that this only skips transcluded <pages> within a given <pages> call, not across the entire page
EX: Page:A/1 has the following text
<pages index="A" from=1 to=3 />
<pages index="B" from=1 to=1 />
text
<pages index="B" from=1 to=1 />
. original implementation would correctly include <pages index="A" from=1 to=3 /> only once, but would also include <pages index="B" from=1 to=1 /> once
. current implmentation would include <pages index="B" from=1 to=1 /> twice
. also, side-effect of only having Lst_page_regy only be non-null on sub_ctx, which means nothing needs to be cleared on main_ctx
*/