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
package gplx.xowa; import gplx.*;
public class Xop_parser {
	Xop_parser(ByteTrieMgr_fast tmpl_trie, ByteTrieMgr_fast wiki_trie) {this.tmpl_trie = tmpl_trie; this.wiki_trie = wiki_trie;} ByteTrieMgr_fast tmpl_trie; ByteTrieMgr_fast wiki_trie;
	public Xop_root_tkn Parse_recurse(Xop_ctx old_ctx, byte[] src, boolean doc_bgn_pos) {return Parse_recurse(old_ctx, Xop_ctx.new_sub_(old_ctx.Wiki()), src, doc_bgn_pos);}
	public Xop_root_tkn Parse_recurse(Xop_ctx old_ctx, Xop_ctx new_ctx, byte[] src, boolean doc_bgn_pos) {
		new_ctx.Para().Enabled_n_();
		new_ctx.Page_(old_ctx.Page());
		Xop_tkn_mkr tkn_mkr = new_ctx.Tkn_mkr();
		Xop_root_tkn root = tkn_mkr.Root(src);
		Parse_page_all(root, new_ctx, tkn_mkr, src, doc_bgn_pos ? Xop_parser_.Doc_bgn_bos : Xop_parser_.Doc_bgn_char_0);
		return root;
	}
	public byte[] Parse_fragment_to_html(Xop_ctx ctx, byte[] fragment) {
		Xow_wiki wiki = ctx.Wiki();
		Xop_root_tkn fragment_root = Parse_recurse(ctx, fragment, true);
		ByteAryBfr tmp_bfr = wiki.Utl_bry_bfr_mkr().Get_b512();
		wiki.Html_wtr().Write_all(ctx, fragment_root, fragment_root.Data_mid(), tmp_bfr);	// NOTE: must pass fragment_root.Data_mid() (not fragment); DATE:2013-07-21
		return tmp_bfr.Mkr_rls().XtoAryAndClear();
	}
	public Xot_defn_tmpl Parse_tmpl(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xow_ns ns, byte[] name, byte[] src) {
		Xot_defn_tmpl rv = new Xot_defn_tmpl();
		Parse_tmpl(rv, ctx, tkn_mkr, ns, name, src); 
		return rv;
	}
	public void Parse_tmpl(Xot_defn_tmpl tmpl, Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xow_ns ns, byte[] name, byte[] src) {
		Xop_root_tkn root = tkn_mkr.Root(src);
		Parse(root, ctx, tkn_mkr, src, Xop_parser_.Parse_tid_tmpl, tmpl_trie, Xop_parser_.Doc_bgn_bos);
		tmpl_props.OnlyInclude_exists = false; int subs_len = root.Subs_len();
		for (int i = 0; i < subs_len; i++)
			root.Subs_get(i).Tmpl_compile(ctx, src, tmpl_props);
		boolean only_include_chk = ByteAry_.FindFwd(src, ByteAry_.new_ascii_("onlyinclude"), 0, src.length) != ByteAry_.NotFound;
		if (only_include_chk) tmpl_props.OnlyInclude_exists = true;
		tmpl.Init_by_new(ns, name, src, root, tmpl_props.OnlyInclude_exists);
	}	Xot_compile_data tmpl_props = new Xot_compile_data();
	public void Parse_page_all_clear(Xop_root_tkn root, Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, byte[] src) {
		ctx.Page().Clear(); ctx.App().Msg_log().Clear();
		Parse_page_all(root, ctx, tkn_mkr, src, Xop_parser_.Doc_bgn_bos);
	}
	public void Parse_page_all(Xop_root_tkn root, Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, byte[] src, int doc_bgn_pos) {
		ctx.Parse_tid_(Xop_parser_.Parse_tid_page_tmpl);
		root.Reset();
		byte[] mid_bry = (ctx.Page().Page_ttl().Ns().Id() == gplx.xowa.xtns.scribunto.Scrib_pf_invoke.Ns_id_module) ? src : Parse_page_tmpl(root, ctx, tkn_mkr, src);	// HACK: do not parse anything if Module; else embedded template names will be called; EX: Module:Routelist_row has comment -- Date passed to the module via the "established" parameter of {{routelist row}}; DATE:2013-04-04
//			byte[] mid_bry = Parse_page_tmpl(root, ctx, tkn_mkr, src);
		root.Data_mid_(mid_bry);
		root.Reset();
		Parse_page_wiki(root, ctx, tkn_mkr, mid_bry, doc_bgn_pos);
	}
	public byte[] Parse_page_tmpl(Xop_root_tkn root, Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, byte[] src) {
		Parse(root, ctx, tkn_mkr, src, Xop_parser_.Parse_tid_page_tmpl, tmpl_trie, Xop_parser_.Doc_bgn_bos);
		int subs_len = root.Subs_len();
		for (int i = 0; i < subs_len; i++)
			root.Subs_get(i).Tmpl_compile(ctx, src, tmpl_props);
		return Xot_tmpl_wtr._.Write_all(ctx, root, src);	// NOTE: generate new src since most callers will use it;
	}
	public void Parse_page_wiki(Xop_root_tkn root, Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, byte[] src, int doc_bgn_pos) {
		root.Root_src_(src);	// always set latest src; needed for Parse_all wherein src will first be raw and then parsed tmpl
		Parse(root, ctx, tkn_mkr, src, Xop_parser_.Parse_tid_page_wiki, wiki_trie, doc_bgn_pos);
	}
	void Parse(Xop_root_tkn root, Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, byte[] src, byte parse_type, ByteTrieMgr_fast trie, int doc_bgn_pos) {
		int len = src.length; if (len == 0) return;	// nothing to parse;
		ctx.Parse_tid_(parse_type);
		int pos = doc_bgn_pos;
		byte b = doc_bgn_pos == -1 ? Byte_ascii.NewLine : src[0];	// simulate newLine at bgn of src; needed for lxrs which rely on \n (EX: "=a=")
		int txt_bgn = 0; Xop_tkn_itm txt_tkn = null;
		ctx.Page_bgn(root, src);
		while (true) {
			Object o = trie.Match(b, src, pos, len);
			if (o == null)				// no lxr found; char is txt; increment pos
				pos++;
			else {						// lxr found
				Xop_lxr lxr = (Xop_lxr)o;
				if (txt_bgn != pos)		// chars exist between pos and txt_bgn; make txt_tkn; see NOTE_1
					txt_tkn = Txt_add(ctx, tkn_mkr, root, txt_tkn, txt_bgn, pos);
				ctx.Lxr_make_(true);
				pos = lxr.MakeTkn(ctx, tkn_mkr, root, src, len, pos, trie.Match_pos());
				if (ctx.Lxr_make()) {txt_bgn = pos; txt_tkn = null;}	// reset txt_tkn
			}
			if (pos == len) break;
			b = src[pos];
		}
		if (txt_bgn != pos) txt_tkn = Txt_add(ctx, tkn_mkr, root, txt_tkn, txt_bgn, pos);
		ctx.Page_end(src, len);
	}
	private static Xop_tkn_itm Txt_add(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, Xop_tkn_itm tkn, int txt_bgn, int pos) {
		if (pos == Xop_parser_.Doc_bgn_bos) return null;	// don't make txt_tkn for Bos_pos
		if (tkn == null) {									// no existing txt_tkn; create new one
			tkn = tkn_mkr.Txt(txt_bgn, pos);
			ctx.Subs_add(tkn);
		}
		else												// existing txt_tkn; happens for false matches; EX: abc[[\nef[[a]]; see NOTE_1
			tkn.Src_end_(pos);
		return tkn;
	}
	public static Xop_parser new_(Xow_wiki wiki)				{return new Xop_parser(Xop_parser_.Tmpl_trie(wiki), Xop_parser_.Main_trie(wiki));}
	public static Xop_parser anchorencode_(Xow_wiki wiki)		{return new Xop_parser(Xop_parser_.Tmpl_trie(wiki), Xop_parser_.Anchorencode_trie(wiki));}
	public static Xop_parser poem_(Xow_wiki wiki)				{return new Xop_parser(Xop_parser_.Tmpl_trie(wiki), Xop_parser_.Poem_trie(wiki));}
}
/*
NOTE_1
abc[[\nef[[a]]
<BOS>	: txt_bgn = 0; txt_tkn = null;
abc		: increment pos
[[\n	: lnki lxr
		: (1): txt_tkn == null, so create txt_tkn with (0, 3)
		: (2): lxr.MakeTkn() entered for lnki; however \n exits lnki
		: (3): note that ctx.Lxr_make == false, so txt_bgn/txt_tkn is not reset
ef		: still just text; increment pos
[[a]]	: lnki entered
		: (1): txt_tkn != null; set end to 8
		: (2): lxr.MakeTkn() entered and lnki made
		: (3): note that ctx.Lxr_make == true, so txt_bgn = 13 and txt_tkn = null
*/