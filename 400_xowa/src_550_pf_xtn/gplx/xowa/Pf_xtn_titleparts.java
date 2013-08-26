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
class Pf_xtn_titleparts extends Pf_func_base {
	@Override public boolean Func_require_colon_arg() {return true;}
	@Override public void Func_evaluate(Xop_ctx ctx, byte[] src, Xot_invk caller, Xot_invk self, ByteAryBfr bb) {// REF.MW:ParserFunctions_body.php
		// get argx
		int args_len = self.Args_len();
		byte[] argx = Eval_argx(ctx, src, caller, self); if (argx == null) return; // no argx; return empty
		Xoa_ttl argx_as_ttl = Xoa_ttl.new_(ctx.Wiki(), ctx.App().Msg_log_null(), argx, argx, 0, argx.length); // transform to title in order to upper first, replace _, etc..
		if (argx_as_ttl == null)	{bb.Add(argx); return;}	// NOTE: argx_as_ttl will be null if invalid, such as [[a|b]]; EX.WP:owl and {{taxobox/showtaxon|Dinosauria}}
		else						argx = argx_as_ttl.Full_txt();

		// get parts_len
		byte[] parts_len_ary = Pf_func_.EvalArgOrEmptyAry(ctx, src, caller, self, args_len, 0);
		int parts_len = parts_len_ary == ByteAry_.Empty ? Int_.MinValue : ByteAry_.XtoIntOr(parts_len_ary, Int_.MaxValue);
		if (parts_len == Int_.MaxValue) {// len is not an int; EX: "a";
			ctx.Msg_log().Add_itm_none(Pf_xtn_titleparts_log.Len_is_invalid, src, caller.Src_bgn(), caller.Src_end());
			bb.Add(argx);
			return;
		}

		// get parts_bgn
		byte[] parts_bgn_arg = Pf_func_.EvalArgOrEmptyAry(ctx, src, caller, self, args_len, 1);
		int parts_bgn = parts_bgn_arg == ByteAry_.Empty ? 0 : ByteAry_.XtoIntOr(parts_bgn_arg, Int_.MinValue);
		if (parts_bgn == Int_.MinValue) {// parts_bgn is not an int; EX: "a"
			ctx.Msg_log().Add_itm_none(Pf_xtn_titleparts_log.Bgn_is_invalid, src, caller.Src_bgn(), caller.Src_end());
			parts_bgn = 0;	// NOTE: do not return
		}
		else if (parts_bgn > 0) parts_bgn -= ListAdp_.Base1;	// adjust for base1
		bb.Add(TitleParts(argx, parts_len, parts_bgn));
	}
	byte[] TitleParts(byte[] src, int parts_len, int parts_bgn) {
		// find dlm positions; EX: ab/cde/f/ will have -1,2,6,8
		int src_len = src.length; int dlms_ary_len = 1;	// 1 b/c dlms_ary[0] will always be -1
		for (int i = 0; i < src_len; i++) {
			if (src[i] == Byte_ascii.Slash) dlms_ary[dlms_ary_len++] = i;
		}
		dlms_ary[dlms_ary_len] = src_len;	// put src_len into last dlms_ary; makes dlms_ary[] logic easier

		// adjust parts_len for negative/null
		if		(parts_len == Int_.MinValue) parts_len = dlms_ary_len;					// no parts_len; default to dlms_ary_len
		else if (parts_len < 0)	{														// neg parts_len; shorten parts now and default to rest of String; EX: a/b/c|-1 -> makes String a/b/c and get 2 parts
			dlms_ary_len += parts_len;
			parts_len = dlms_ary_len;
			if (parts_len < 1) return ByteAry_.Empty;									// NOTE: if zerod'd b/c of neg length, return empty; contrast with line below; EX: a/b/c|-4
		}		
		if (parts_len == 0) return src;													// if no dlms, return orig

		// calc idxs for extraction
		int bgn_idx = parts_bgn > -1 ? parts_bgn : parts_bgn + dlms_ary_len;			// negative parts_bgn means calc from end of dlms_ary_len; EX a/b/c|1|-1 means start from 2
		int end_idx = bgn_idx + parts_len;
		if (bgn_idx > dlms_ary_len) return ByteAry_.Empty;								// if bgn > len, return ""; EX: {{#titleparts:a/b|1|3}} should return ""
		int bgn_pos = dlms_ary[bgn_idx] + 1; // +1 to start after slash
		int end_pos = end_idx > dlms_ary_len ? dlms_ary[dlms_ary_len] : dlms_ary[end_idx];
		if (end_pos < bgn_pos) return ByteAry_.Empty;
		return ByteAry_.Mid(src, bgn_pos, end_pos);	
	}	static int[] dlms_ary = new IntAryBldr(255).Set(0, -1).XtoIntAry();	// set pos0 to -1; makes +1 logic easier
	@Override public int Id() {return Xol_kwd_grp_.Id_xtn_titleparts;}
	@Override public Pf_func New(int id, byte[] name) {return new Pf_xtn_titleparts().Name_(name);}
}