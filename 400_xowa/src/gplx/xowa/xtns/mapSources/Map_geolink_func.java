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
package gplx.xowa.xtns.mapSources; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
public class Map_geolink_func extends Pf_func_base {
	@Override public int Id() {return Xol_kwd_grp_.Id_mapSources_geoLink;}
	@Override public Pf_func New(int id, byte[] name) {return new Map_geolink_func().Name_(name);}
	@Override public void Func_evaluate(Xop_ctx ctx, byte[] src, Xot_invk caller, Xot_invk self, ByteAryBfr bfr) {
		byte[] mer_x_val = null, mer_y_val = null, mer_x_pos = null, mer_x_neg = null, mer_y_pos = null, mer_y_neg = null;
		int prec = 4;
		int args_len = self.Args_len();
		ByteAryBfr tmp_bfr = ctx.Wiki().Utl_bry_bfr_mkr().Get_b128();
		try {
			byte[] pattern = Eval_argx(ctx, src, caller, self);
			for (int i = 0; i < args_len; i++) {
				Arg_nde_tkn arg = self.Args_get_by_idx(i);				
				byte[] key = Pf_func_.Eval_tkn(tmp_bfr, ctx, src, caller, arg.Key_tkn());
				Object key_tid_obj = Key_hash.Fetch(key);
				if (key_tid_obj != null) {
					byte[] val = Pf_func_.Eval_tkn(tmp_bfr, ctx, src, caller, arg.Val_tkn());
					switch (((ByteVal)key_tid_obj).Val()) {
						case Key_tid_lat_val:	mer_x_val = val; break;
						case Key_tid_long_val:	mer_y_val = val; break;
						case Key_tid_lat_pos:	mer_x_pos = val; break;
						case Key_tid_lat_neg:	mer_x_neg = val; break;
						case Key_tid_long_pos:	mer_y_pos = val; break;
						case Key_tid_long_min:	mer_y_neg = val; break;
						case Key_tid_prec:		prec = ByteAry_.X_to_int_or(val, prec); break;
					}
				}
			}
			Map_math mer_x_math = new Map_math();
			Map_math mer_y_math = new Map_math();
			boolean mer_x_pass = mer_x_math.Ctor(mer_x_val, prec, Map_math.Dir_lat_bry, 2);
			boolean mer_y_pass = mer_y_math.Ctor(mer_y_val, prec, Map_math.Dir_long_bry, 2);
			if (!mer_x_pass) {
				mer_x_math.Fail(ctx, src, self, bfr, this.Name());
				return;
			}
			if (!mer_y_pass) {
				mer_y_math.Fail(ctx, src, self, bfr, this.Name());
				return;
			}
			Object[] args = new Object[6];
			args[0] = X_to_coord(tmp_bfr, mer_x_math, mer_x_pass, mer_x_math.Coord_dir_ns(), Bry_arg_0_fail);
			args[1] = X_to_coord(tmp_bfr, mer_y_math, mer_y_pass, mer_y_math.Coord_dir_ew(), Bry_arg_1_fail);
			args[2] = X_to_dms(mer_x_math, mer_x_pass, mer_x_pos, mer_x_neg);
			args[3] = X_to_dms(mer_y_math, mer_y_pass, mer_y_pos, mer_y_neg);
			args[4] = X_to_dec(tmp_bfr, mer_x_math, mer_x_pass);
			args[5] = X_to_dec(tmp_bfr, mer_y_math, mer_y_pass);
			bfr.Add(Xol_msg_itm_.eval_(tmp_bfr, tmp_msg_itm, pattern, args));
		} finally {tmp_bfr.Mkr_rls();}
	}
	private static final Xol_msg_itm tmp_msg_itm = new Xol_msg_itm(-1, ByteAry_.Empty);
	private static byte[] X_to_coord(ByteAryBfr bfr, Map_math math, boolean pass, byte[] dir, byte[] or) {
		return pass
			? bfr.Add_double(Math_.Abs_double(math.Dec())).Add_byte(Byte_ascii.Underline).Add(dir).XtoAryAndClear()
			: or
			;
	}
	private static byte[] X_to_dms(Map_math math, boolean pass, byte[] pos, byte[] neg) {
		return pass
			? math.Get_dms(pos, neg)
			: ByteAry_.Empty		// mapsources-math-incorrect-input
			;
	}
	private static byte[] X_to_dec(ByteAryBfr bfr, Map_math math, boolean pass) {
		return pass
			? bfr.Add_double(math.Dec()).XtoAryAndClear()
			: Bry_arg_5_fail
			;
	}
	private static final byte[] 
	  Bry_arg_0_fail = ByteAry_.new_ascii_("0_N")
	, Bry_arg_1_fail = ByteAry_.new_ascii_("0_E")
	, Bry_arg_5_fail = ByteAry_.new_ascii_("0")
	;
/*
	return wfMsgReplaceArgs( $pattern, $args );
*/
        public static final Map_geolink_func _ = new Map_geolink_func(); Map_geolink_func() {}
	private static final byte Key_tid_lat_val = 1, Key_tid_long_val = 2, Key_tid_lat_pos = 3, Key_tid_lat_neg = 4, Key_tid_long_pos = 5, Key_tid_long_min = 6, Key_tid_prec = 7;
	private static final Hash_adp_bry Key_hash = Hash_adp_bry.cs_()
	.Add_str_byte("lat"			, Key_tid_lat_val)
	.Add_str_byte("long"		, Key_tid_long_val)
	.Add_str_byte("plusLat"		, Key_tid_lat_pos)
	.Add_str_byte("minusLat"	, Key_tid_lat_neg)
	.Add_str_byte("plusLong"	, Key_tid_long_pos)
	.Add_str_byte("minusLong"	, Key_tid_long_min)
	.Add_str_byte("precision"	, Key_tid_prec)
	;
}
