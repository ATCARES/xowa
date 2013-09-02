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
class Pxd_itm_colon extends Pxd_itm_base {
	@Override public byte Tkn_tid() {return Pxd_itm_.TypeId_colon;}
	@Override public int Eval_idx() {return 20;}
	@Override public void Eval(Pxd_parser state) {
		state.Colon_count++;
		int colon_count = state.Colon_count;
		Pxd_itm[] tkns = state.Tkns();
		Pxd_itm_int itm_int = null;
		switch (colon_count) {
			case 1:				
				itm_int = Pxd_itm_int_.GetNearest(tkns, this.Ary_idx(), false);
				if (itm_int == null) {state.Err_set(Pf_xtn_time_log.Invalid_hour, ByteAryFmtrArg_.bry_("null")); return;}
				if (Pxd_itm_int_.Hour_err(state, itm_int)) return;
				itm_int = Pxd_itm_int_.GetNearest(tkns, this.Ary_idx(), true);
				if (Pxd_itm_int_.Min_err(state, itm_int)) return;
				break;
			case 2:
				state.Colon_count++;
				itm_int = Pxd_itm_int_.GetNearest(tkns, this.Ary_idx(), true);
				if (Pxd_itm_int_.Sec_err(state, itm_int)) return;
				break;
		} 
	}
	public Pxd_itm_colon(int ary_idx) {this.Ctor(ary_idx);}
}
class Pxd_itm_null extends Pxd_itm_base {
	@Override public byte Tkn_tid() {return Pxd_itm_.TypeId_null;}
	@Override public int Eval_idx() {return 99;}
	public static final Pxd_itm_null _ = new Pxd_itm_null(); 
}
class Pxd_itm_dash extends Pxd_itm_base {
	@Override public byte Tkn_tid() {return Pxd_itm_.TypeId_dash;}
	@Override public int Eval_idx() {return 99;}
	public Pxd_itm_dash(int ary_idx) {this.Ctor(ary_idx);}
}
class Pxd_itm_dot extends Pxd_itm_base {
	@Override public byte Tkn_tid() {return Pxd_itm_.TypeId_dot;}
	@Override public int Eval_idx() {return 99;}
	public Pxd_itm_dot(int ary_idx) {this.Ctor(ary_idx);}
}
class Pxd_itm_ws extends Pxd_itm_base {
	@Override public byte Tkn_tid() {return Pxd_itm_.TypeId_ws;}
	@Override public int Eval_idx() {return 99;}
	public Pxd_itm_ws(int ary_idx) {this.Ctor(ary_idx);}
}
class Pxd_itm_slash extends Pxd_itm_base {
	@Override public byte Tkn_tid() {return Pxd_itm_.TypeId_slash;}
	@Override public int Eval_idx() {return 99;}
	public Pxd_itm_slash(int ary_idx) {this.Ctor(ary_idx);}
}
class Pxd_itm_sym extends Pxd_itm_base {
	@Override public byte Tkn_tid() {return Pxd_itm_.TypeId_sym;} 
	@Override public int Eval_idx() {return 99;}
	@Override public void Time_ini(DateAdpBldr bldr) {}
	public Pxd_itm_sym(int ary_idx, int val) {this.Ctor(ary_idx);}
}
class Pxd_itm_int_dmy_14 extends Pxd_itm_base {
	@Override public byte Tkn_tid() {return Pxd_itm_.TypeId_int_dmy_14;}
	@Override public int Eval_idx() {return eval_idx;} private int eval_idx = 20;
	@Override public void Time_ini(DateAdpBldr bldr) {
		bldr.Seg_set(DateAdp_.SegIdx_year	, ByteAry_.XtoIntByPos(src,  0,  4, 0));
		bldr.Seg_set(DateAdp_.SegIdx_month	, ByteAry_.XtoIntByPos(src,  4,  6, 0));
		if (digits > 6) {
			bldr.Seg_set(DateAdp_.SegIdx_day	, ByteAry_.XtoIntByPos(src,  6,  8, 0));
			if (digits > 8) {
				bldr.Seg_set(DateAdp_.SegIdx_hour	, ByteAry_.XtoIntByPos(src,  8, 10, 0));
				if (digits > 10) {
					bldr.Seg_set(DateAdp_.SegIdx_minute	, ByteAry_.XtoIntByPos(src, 10, 12, 0));
					if (digits > 12)
						bldr.Seg_set(DateAdp_.SegIdx_second	, ByteAry_.XtoIntByPos(src, 12, 14, 0));
				}
			}
		}
	}
	public Pxd_itm_int_dmy_14(int ary_idx, byte[] src, int digits) {this.Ctor(ary_idx); this.src = src; this.digits = digits;} private byte[] src; int digits;
}
class Pxd_itm_int_mhs_6 extends Pxd_itm_base {
	@Override public byte Tkn_tid() {return Pxd_itm_.TypeId_int_hms_6;}
	@Override public int Eval_idx() {return eval_idx;} private int eval_idx = 20;
	@Override public void Time_ini(DateAdpBldr bldr) {
		bldr.Seg_set(DateAdp_.SegIdx_hour	, ByteAry_.XtoIntByPos(src, 0,  2, 0));
		bldr.Seg_set(DateAdp_.SegIdx_minute	, ByteAry_.XtoIntByPos(src, 2,  4, 0));
		bldr.Seg_set(DateAdp_.SegIdx_second	, ByteAry_.XtoIntByPos(src, 4,  6, 0));
	}
	public Pxd_itm_int_mhs_6(int ary_idx, byte[] src) {this.Ctor(ary_idx); this.src = src;} private byte[] src;
}
class Pxd_itm_sorter implements gplx.lists.ComparerAble {
	public int compare(Object lhsObj, Object rhsObj) {
		Pxd_itm lhs = (Pxd_itm)lhsObj;
		Pxd_itm rhs = (Pxd_itm)rhsObj;
		return Int_.Compare(lhs.Eval_idx(), rhs.Eval_idx());
	}
	public static final Pxd_itm_sorter _ = new Pxd_itm_sorter();
	public static Pxd_itm[] XtoAryAndSort(Pxd_itm[] src, int src_len) {
		Pxd_itm[] rv = new Pxd_itm[src_len];
		for (int i = 0; i < src_len; i++)
			rv[i] = src[i];
		Array_.Sort(rv, Pxd_itm_sorter._);
		return rv;
	}
}