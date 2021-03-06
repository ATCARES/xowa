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
package gplx.gfs; import gplx.*;
class Gfs_parser_ctx {
	public ByteTrieMgr_fast Trie() {return trie;} ByteTrieMgr_fast trie;
	public Gfs_nde Root() {return root;} Gfs_nde root = new Gfs_nde();
	public byte[] Src() {return src;} private byte[] src;
	public int Src_len() {return src_len;} private int src_len;
	public byte Prv_lxr() {return prv_lxr;} public Gfs_parser_ctx Prv_lxr_(byte v) {prv_lxr = v; return this;} private byte prv_lxr;
	public Gfs_nde Cur_nde() {return cur_nde;} Gfs_nde cur_nde;
	public int Nxt_pos() {return nxt_pos;} private int nxt_pos;
	public Gfs_lxr Nxt_lxr() {return nxt_lxr;} Gfs_lxr nxt_lxr;
	public ByteAryBfr Tmp_bfr() {return tmp_bfr;} private ByteAryBfr tmp_bfr = ByteAryBfr.new_();
	public void Process_eos() {}
	public void Process_lxr(int nxt_pos, Gfs_lxr nxt_lxr)	{this.nxt_pos = nxt_pos; this.nxt_lxr = nxt_lxr;}
	public void Process_null(int cur_pos)					{this.nxt_pos = cur_pos; this.nxt_lxr = null;}
	public void Init(ByteTrieMgr_fast trie, byte[] src, int src_len) {
		this.trie = trie; this.src = src; this.src_len = src_len;
		cur_nde = root;
		Stack_add();
	}
	public void Hold_word(int bgn, int end) {
		cur_idf_bgn = bgn;
		cur_idf_end = end;
	}	int cur_idf_bgn = -1, cur_idf_end = -1;
	private void Held_word_clear() {cur_idf_bgn = -1; cur_idf_end = -1;}

	public Gfs_nde Make_nde(int tkn_bgn, int tkn_end) {	// "abc."; "abc("; "abc;"; "abc{"
		Gfs_nde nde = new Gfs_nde().Name_rng_(cur_idf_bgn, cur_idf_end);
		this.Held_word_clear();
		cur_nde.Subs_add(nde);
		cur_nde = nde;
		return nde;
	}
	public void Make_atr_by_idf()								{Make_atr(cur_idf_bgn, cur_idf_end); Held_word_clear();}
	public void Make_atr_by_bry(int bgn, int end, byte[] bry)	{Make_atr(bgn, end).Name_(bry);}
	public Gfs_nde Make_atr(int bgn, int end) {
		Gfs_nde nde = new Gfs_nde().Name_rng_(bgn, end);
		cur_nde.Atrs_add(nde);
		return nde;
	}
	public void Cur_nde_from_stack() {cur_nde = (Gfs_nde)nodes.FetchAtLast();}
	public void Stack_add() {nodes.Add(cur_nde);} ListAdp nodes = ListAdp_.new_();
	public void Stack_pop(int pos) {
		if (nodes.Count() < 2) err_mgr.Fail_nde_stack_empty(this, pos);	// NOTE: need at least 2 items; 1 to pop and 1 to set as current
		ListAdp_.DelAt_last(nodes);
		Cur_nde_from_stack();
	}
	public Gfs_err_mgr Err_mgr() {return err_mgr;} Gfs_err_mgr err_mgr = new Gfs_err_mgr();
}
class Gfs_err_mgr {
	public void Fail_eos(Gfs_parser_ctx ctx) {Fail(ctx, Fail_msg_eos, ctx.Src_len());}
	public void Fail_unknown_char(Gfs_parser_ctx ctx, int pos, byte c) {Fail(ctx, Fail_msg_unknown_char, pos, KeyVal_.new_("char", Char_.XtoStr((char)c)));}
	public void Fail_nde_stack_empty(Gfs_parser_ctx ctx, int pos) {Fail(ctx, Fail_msg_nde_stack_empty, pos);}
	public void Fail_invalid_lxr(Gfs_parser_ctx ctx, int pos, byte cur_lxr, byte c) {
		Fail(ctx, Fail_msg_invalid_lxr, pos, KeyVal_.new_("char", Char_.XtoStr((char)c)), KeyVal_.new_("cur_lxr", Gfs_lxr_.Tid__name(cur_lxr)), KeyVal_.new_("prv_lxr", Gfs_lxr_.Tid__name(ctx.Prv_lxr())));
	}
	private void Fail(Gfs_parser_ctx ctx, String msg, int pos, KeyVal... args) {
		byte[] src = ctx.Src(); int src_len = ctx.Src_len(); 
		Fail_args_standard(src, src_len, pos);
		int len = args.length;
		for (int i = 0; i < len; i++) {
			KeyVal arg = args[i];
			tmp_fail_args.Add(arg.Key(), arg.Val_to_str_or_empty());
		}
		throw Err_.new_(Fail_msg(msg, tmp_fail_args));
	}
	private void Fail_args_standard(byte[] src, int src_len, int pos) {
		tmp_fail_args.Add("excerpt_bgn", Fail_excerpt_bgn(src, src_len, pos));		
		tmp_fail_args.Add("excerpt_end", Fail_excerpt_end(src, src_len, pos));		
		tmp_fail_args.Add("pos"	, pos);		
	}
	public static final String Fail_msg_invalid_lxr = "invalid character", Fail_msg_unknown_char = "unknown char", Fail_msg_eos = "end of stream", Fail_msg_nde_stack_empty = "node stack empty";
	String Fail_msg(String type, KeyValList fail_args) {
		tmp_fail_bfr.Add_str(type).Add_byte(Byte_ascii.Colon);
		int len = fail_args.Count();
		for (int i = 0; i < len; i++) {
			tmp_fail_bfr.Add_byte(Byte_ascii.Space);
			KeyVal kv = fail_args.GetAt(i);
			tmp_fail_bfr.Add_str(kv.Key());
			tmp_fail_bfr.Add_byte(Byte_ascii.Eq).Add_byte(Byte_ascii.Apos);
			tmp_fail_bfr.Add_str(kv.Val_to_str_or_empty()).Add_byte(Byte_ascii.Apos);
		}
		return tmp_fail_bfr.XtoStrAndClear();
	}
	ByteAryBfr tmp_fail_bfr = ByteAryBfr.reset_(255);
	KeyValList tmp_fail_args = new KeyValList();
	private static int excerpt_len = 50;
	String Fail_excerpt_bgn(byte[] src, int src_len, int pos) {
		int bgn = pos - excerpt_len; if (bgn < 0) bgn = 0;
		Fail_excerpt_rng(tmp_fail_bfr, src, bgn, pos);
		return tmp_fail_bfr.XtoStrAndClear();
	}
	String Fail_excerpt_end(byte[] src, int src_len, int pos) {
		int end = pos + excerpt_len; if (end > src_len) end = src_len;
		Fail_excerpt_rng(tmp_fail_bfr, src, pos, end);
		return tmp_fail_bfr.XtoStrAndClear();
	}
	private static void Fail_excerpt_rng(ByteAryBfr bfr, byte[] src, int bgn, int end) {
		for (int i = bgn; i < end; i++) {
			byte b = src[i];
			switch (b) {
				case Byte_ascii.Tab: 			bfr.Add(Esc_tab); break;
				case Byte_ascii.NewLine:		bfr.Add(Esc_nl); break;
				case Byte_ascii.CarriageReturn: bfr.Add(Esc_cr); break;
				default:						bfr.Add_byte(b); break;
			}
		}
	}	static final byte[] Esc_nl = ByteAry_.new_ascii_("\\n"), Esc_cr = ByteAry_.new_ascii_("\\r"), Esc_tab = ByteAry_.new_ascii_("\\t");
}
