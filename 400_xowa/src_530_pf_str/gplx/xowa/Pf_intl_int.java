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
import gplx.php.*;
class Pf_intl_int extends Pf_func_base {
	@Override public boolean Func_require_colon_arg() {return true;}
	@Override public void Func_evaluate(Xop_ctx ctx, byte[] src, Xot_invk caller, Xot_invk self, ByteAryBfr bfr) {
		byte[] msg_key = Eval_argx(ctx, src, caller, self);
		Xow_wiki wiki = ctx.Wiki();
		Xol_lang page_lang = ctx.Page().Lang();
		byte[][] args_ary = ByteAry_.Ary_empty;
		int args_len = self.Args_len();
		if (args_len > 0) {
			args_ary = new byte[args_len][];
			for (int i = 0; i < args_len; i++)
				args_ary[i] = Pf_func_.EvalArgOrEmptyAry(ctx, src, caller, self, self.Args_len(), i);
		}
		byte[] msg_val = Pf_msg_mgr.Get_msg_by_key(wiki, page_lang, msg_key, args_ary);
		bfr.Add(msg_val);
	}	static ByteAryFmtr tmp_fmtr = ByteAryFmtr.tmp_(); static ByteAryBfr tmp_bfr = ByteAryBfr.reset_(256); 
	@Override public int Id() {return Xol_kwd_grp_.Id_i18n_int;}
	@Override public Pf_func New(int id, byte[] name) {return new Pf_intl_int().Name_(name);}
}
