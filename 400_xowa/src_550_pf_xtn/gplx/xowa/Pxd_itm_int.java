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
class Pxd_itm_int extends Pxd_itm_base {
	@Override public byte Tkn_tid() {return Pxd_itm_.TypeId_int;}
	@Override public int Eval_idx() {return eval_idx;} private int eval_idx = 99;
	public int Val() {return val;} public Pxd_itm_int Val_(int v) {val = v; return this;} private int val;
	public int Digits() {return digits;} private int digits;
	@Override public void Time_ini(DateAdpBldr bldr) {
		if (this.Seg_idx() == Pxd_itm_base.Seg_idx_skip) return;
		bldr.Seg_set(this.Seg_idx(), val);
	}
	static final int Digits_year = 4, Month_max = 12;
	@Override public void Eval(Pxd_parser state) {
		if (this.Seg_idx() != Pxd_itm_base.Seg_idx_null) return;						// has seg_idx; already eval'd by something else
		switch (digits) {
			case Digits_year:															// 4 digits; assume year
				switch (this.Data_idx()) {
					case 0: Eval_year_at_pos_0(state); break;							// year at pos 0; EX: 2001-02-03
					case 1: state.Err_set(Pf_xtn_time_log.Invalid_year_mid); return;	// year at pos 1; invalid; EX: 02-2003-03
					case 2: Eval_year_at_pos_2(state); break;							// year at pos 2; EX: 02-03-2001
				}
				state.Seg_idxs_(this, DateAdp_.SegIdx_year);
				break;
			default:
				if (val > Month_max) {													// value is not a month; assume day; DATE:2013-03-15
					switch (this.Data_idx()) {
						case 0:															// > 12 in slot 0
							if (Match_sym(state, true, Pxd_itm_.TypeId_dot))			// next sym is dot; assume m.d.y; EX: 22.5.70
								Eval_day_at_pos_0(state); 
							else														// next sym is not dot; assume y-m-d; EX: 70-5-22
								Eval_month_at_pos_0(state); 
							break;	
						case 1: Eval_month_at_pos_1(state); break;						// > 12 in slot 1; assume m.d; EX: 5.22
						case 2:															// > 12 in slot 2
							if (Match_sym(state, false, Pxd_itm_.TypeId_dot))			// prv sym is dot; assume d.m.y; EX: 22.5.70
								Eval_dmy_at_y(state); 
							else														// prv sym is not dot; assume m-d-y; EX: 22.5.70
								Eval_month_at_pos_2(state);
							break;
						case 4:
							Eval_unknown_at_pos_4(state); break;
					}
				}
				else {																	// value is either day or month;
					switch (this.Data_idx()) {
						case 0: Eval_unknown_at_pos_0(state); break;
						case 3: Eval_unknown_at_pos_3(state); break;
						case 4: Eval_unknown_at_pos_4(state); break;
					}
				}
				break;
		}
	}
	void Eval_unknown_at_pos_3(Pxd_parser state) {	// int at pos 4
		if (	state.Seg_idxs_chk(DateAdp_.SegIdx_year, DateAdp_.SegIdx_month, DateAdp_.SegIdx_day)	// check that ymd is set
			&&	Match_sym(state, false, Pxd_itm_.TypeId_dash))											// check that preceding symbol is "-"
			Pxd_itm_int_.Hour_err(state, this);															// mark as hour; handles strange fmts like November 2, 1991-06; DATE:2013-06-19
	}
	void Eval_unknown_at_pos_4(Pxd_parser state) {
		if (	state.Seg_idxs_chk(DateAdp_.SegIdx_year
				, DateAdp_.SegIdx_month, DateAdp_.SegIdx_day, DateAdp_.SegIdx_hour)						// check that ymdh is set
			&&	Match_sym(state, false, Pxd_itm_.TypeId_dash))											// check that preceding symbol is "-"
			state.Seg_idxs_(this, Pxd_itm_base.Seg_idx_skip);											// mark as ignore; handles strange fmts like November 2, 1991-06-19; DATE:2013-06-19
	}
	boolean Match_sym(Pxd_parser state, boolean fwd, int sym_tid) {
		int sym_idx = this.Ary_idx() + (fwd ? 1 : -1);
		Pxd_itm[] sym_tkns = state.Tkns();
		if (sym_idx < 0 || sym_idx > sym_tkns.length) return false;	// NOTE: was Data_ary_len; DATE:2013-06-19
		return sym_tkns[sym_idx].Tkn_tid() == sym_tid;
	}
	void Eval_month_at_pos_0(Pxd_parser state) {
		Pxd_itm[] data_ary = state.Data_ary();
		if (state.Data_ary_len() < 2) return;
		Pxd_itm_int itm_1 = Pxd_itm_int_.CastOrNull(data_ary[1]);
		if (itm_1 != null) {
			if (Pxd_itm_int_.Month_err(state, itm_1)) return;
		}
		if (state.Data_ary_len() > 2) {
			Pxd_itm_int itm_2 = Pxd_itm_int_.CastOrNull(data_ary[2]);
			if (itm_2 != null) {
				if (Pxd_itm_int_.Day_err(state, itm_2)) return;		
			}
		}
		Pxd_itm_int_.Year_err(state, this);
	}
	void Eval_day_at_pos_0(Pxd_parser state) {	// eval 1 as month; 2 as year, 0 as day
		Pxd_itm[] data_ary = state.Data_ary();
		if (state.Data_ary_len() < 2) return;
		Pxd_itm_int itm_1 = Pxd_itm_int_.CastOrNull(data_ary[1]);
		if (itm_1 != null) {
			if (Pxd_itm_int_.Month_err(state, itm_1)) return;
		}
		if (state.Data_ary_len() > 2) {
			Pxd_itm_int itm_2 = Pxd_itm_int_.CastOrNull(data_ary[2]);
			if (itm_2 != null) {
				if (Pxd_itm_int_.Year_err(state, itm_2)) return;		
			}
		}
		Pxd_itm_int_.Day_err(state, this);
	}
	void Eval_dmy_at_y(Pxd_parser state) {	// dmy format; cur is y (slot 2) 
		Pxd_itm[] data_ary = state.Data_ary();
		if (state.Data_ary_len() < 3) return;	// since proc starts at y, assume at least d-m-y (not m-y) 
		Pxd_itm_int itm_1 = Pxd_itm_int_.CastOrNull(data_ary[1]);
		if (itm_1 != null) {
			if (Pxd_itm_int_.Month_err(state, itm_1)) return;
		}
		Pxd_itm_int itm_0 = Pxd_itm_int_.CastOrNull(data_ary[0]);
		if (itm_0 != null) {
			if (Pxd_itm_int_.Year_err(state, itm_0)) return;		
		}
		Pxd_itm_int_.Year_err(state, this);
	}
	void Eval_month_at_pos_1(Pxd_parser state) {
		Pxd_itm[] data_ary = state.Data_ary();
		Pxd_itm_int itm_0 = Pxd_itm_int_.CastOrNull(data_ary[0]);
		if (itm_0 != null) {
			if (Pxd_itm_int_.Month_err(state, itm_0)) return;
		}
		if (state.Data_ary_len() > 2) {
			Pxd_itm_int itm_2 = Pxd_itm_int_.CastOrNull(data_ary[2]);
			if (itm_2 != null) {
				if (Pxd_itm_int_.Year_err(state, itm_2)) return;		
			}
		}
		Pxd_itm_int_.Day_err(state, this);
	}
	void Eval_month_at_pos_2(Pxd_parser state) {
		Pxd_itm[] data_ary = state.Data_ary();
		Pxd_itm_int itm_0 = Pxd_itm_int_.CastOrNull(data_ary[0]);
		if (itm_0 != null) {
			if (Pxd_itm_int_.Month_err(state, itm_0)) return;
		}
		Pxd_itm_int itm_1 = Pxd_itm_int_.CastOrNull(data_ary[1]);
		if (itm_1 != null) {
			if (Pxd_itm_int_.Day_err(state, itm_1)) return;		
		}
		Pxd_itm_int_.Year_err(state, this);
	}
	void Eval_unknown_at_pos_0(Pxd_parser state) {	// NOTE: assumes dmy format
		Pxd_itm[] data_ary = state.Data_ary();
		if (state.Data_ary_len() < 2) {state.Err_set(Pf_xtn_time_log.Invalid_year, ByteAryFmtrArg_.int_(val)); return;}
		Pxd_itm_int itm_1 = Pxd_itm_int_.CastOrNull(data_ary[1]);
		if (itm_1 != null) {				// if 1st itm to right is number, parse it as month
			if (Pxd_itm_int_.Month_err(state, itm_1)) return;
		}
		if (state.Data_ary_len() > 2) {
			Pxd_itm_int itm_2 = Pxd_itm_int_.CastOrNull(data_ary[2]);
			if (itm_2 != null) {				// if 2nd itm to right is number, assume it as year
				if (Pxd_itm_int_.Year_err(state, itm_2)) return;		
			}
		}
		Pxd_itm_int_.Day_err(state, this);	// parse current as day (dmy format)
	}
	void Eval_year_at_pos_0(Pxd_parser state) {
		Pxd_itm[] data_ary = state.Data_ary();
		if (state.Data_ary_len() < 2) return;
		Pxd_itm_int itm_1 = Pxd_itm_int_.CastOrNull(data_ary[1]);
		if (itm_1 != null) {
			if (Pxd_itm_int_.Month_err(state, itm_1)) return;
		}
		if (state.Data_ary_len() < 3) return;
		Pxd_itm_int itm_2 = Pxd_itm_int_.CastOrNull(data_ary[2]);
		if (itm_2 != null) {
			if (Pxd_itm_int_.Day_err(state, itm_2)) return;		
		}
	}
	void Eval_year_at_pos_2(Pxd_parser state) {
		Pxd_itm[] data_ary = state.Data_ary();
		Pxd_itm_int itm_0 = Pxd_itm_int_.CastOrNull(data_ary[0]);
		Pxd_itm_int itm_1 = Pxd_itm_int_.CastOrNull(data_ary[1]);
		if (itm_0 == null || itm_1 == null) {return;} // trie: fail
		if (itm_1.Val() > 13) {
			if (Pxd_itm_int_.Month_err(state, itm_0)) return;
			if (Pxd_itm_int_.Day_err(state, itm_1)) return;						
		}
		else {
			if (Pxd_itm_int_.Day_err(state, itm_0)) return;
			if (Pxd_itm_int_.Month_err(state, itm_1)) return;			
		}
	}
	public Pxd_itm_int(int ary_idx, int digits, int val) {
		this.Ctor(ary_idx); this.digits = digits; this.val = val;
		switch (digits) {
			case 4:					// assume year
				eval_idx = 50;
				break;
			case 2:
			case 1:
			default:
				if (val > 12) 		// either day or year; not month
					eval_idx = 60;
				else
					eval_idx = 70; // day, year, or month
				break;
		}
	}
}
class Pxd_itm_int_ {
	public static Pxd_itm_int CastOrNull(Pxd_itm itm) {return itm.Tkn_tid() == Pxd_itm_.TypeId_int ? (Pxd_itm_int)itm : null; }
	public static Pxd_itm_int GetNearest(Pxd_itm[] tkns, int tkn_idx, boolean fwd) {
		int adj = 1, end = tkns.length;
		if (!fwd) {
			adj = -1;
			end = -1;
		}
		for (int i = tkn_idx + adj; i != end; i += adj) {
			Pxd_itm itm = tkns[i];
			if (itm.Tkn_tid() == Pxd_itm_.TypeId_int) {
				Pxd_itm_int itm_int = (Pxd_itm_int)itm;
				return itm_int.Seg_idx() == -1 ? itm_int : null;
			} 
		}
		return null;
	}
	public static boolean Year_err(Pxd_parser state, Pxd_itm_int itm) {
		int val = itm.Val();
		switch (itm.Digits()) {
			case 1:
			case 2:
				itm.Val_(val + (val > 69 ? 1900 : 2000));	// ASSUME that 70 refers to 1970 and 69 refers to 2069
				state.Seg_idxs_(itm, DateAdp_.SegIdx_year);
				return false;
			case 3:	// NOTE: 3 digit numbers are valid years; MW relies on PHP time parse which always zero-pad numbers; EX.WP: Battle of the Catalaunian Plains; {{#time:Y|June 20, 451}}
			case 4:
				state.Seg_idxs_(itm, DateAdp_.SegIdx_year);
				return false;
		}
		state.Err_set(Pf_xtn_time_log.Invalid_year, ByteAryFmtrArg_.int_(val));
		return true;
	}
	public static boolean Month_err(Pxd_parser state, Pxd_itm_int itm) {
		int val = itm.Val();
		switch (itm.Digits()) {
			case 1:
			case 2:
				if (val > 0 && val < 13) {
					state.Seg_idxs_(itm, DateAdp_.SegIdx_month);
					return false;
				}
				break;
		}
		state.Err_set(Pf_xtn_time_log.Invalid_month, ByteAryFmtrArg_.int_(val));
		return true;
	}
	public static boolean Day_err(Pxd_parser state, Pxd_itm_int itm) {
		int val = itm.Val();
		switch (itm.Digits()) {
			case 1:
			case 2:
				if (val > -1 && val < 32) { 
					state.Seg_idxs_(itm, DateAdp_.SegIdx_day);
					return false;
				}
				break;
		}
		state.Err_set(Pf_xtn_time_log.Invalid_day, ByteAryFmtrArg_.int_(val));
		return true;
	}
	public static boolean Hour_err(Pxd_parser state, Pxd_itm_int itm) {
		int val = itm.Val();
		switch (itm.Digits()) {
			case 1:
			case 2:
				if (val > -1 && val < 25) { 
					state.Seg_idxs_(itm, DateAdp_.SegIdx_hour);
					return false;
				}
				break;
		}
		state.Err_set(Pf_xtn_time_log.Invalid_hour, ByteAryFmtrArg_.int_(val));
		return true;
	}
	public static boolean Min_err(Pxd_parser state, Pxd_itm_int itm) {
		int val = itm.Val();
		switch (itm.Digits()) {
			case 1:
			case 2:
				if (val > -1 && val < 60) { 
					state.Seg_idxs_(itm, DateAdp_.SegIdx_minute);
					return false;
				}
				break;
		}
		state.Err_set(Pf_xtn_time_log.Invalid_minute, ByteAryFmtrArg_.int_(val));
		return true;
	}
	public static boolean Sec_err(Pxd_parser state, Pxd_itm_int itm) {
		int val = itm.Val();
		switch (itm.Digits()) {
			case 1:
			case 2:
				if (val > -1 && val < 60) { 
					state.Seg_idxs_(itm, DateAdp_.SegIdx_second);
					return false;
				}
				break;
		}
		state.Err_set(Pf_xtn_time_log.Invalid_second, ByteAryFmtrArg_.int_(val));
		return true;
	}
}