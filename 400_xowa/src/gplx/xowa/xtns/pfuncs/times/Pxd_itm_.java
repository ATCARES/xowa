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
package gplx.xowa.xtns.pfuncs.times; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.pfuncs.*;
interface Pxd_itm {
	byte Tkn_tid();
	int Ary_idx();
	int Seg_idx();
	int Eval_idx();
	int Data_idx(); void Data_idx_(int v);
	void Eval(Pxd_parser state);
	void Time_ini(DateAdpBldr bldr);
}
class Pxd_itm_ {
	public static final int
	  TypeId_null				= 0
	, TypeId_int				= 1
	, TypeId_int_dmy_14			= 2
	, TypeId_int_hms_6			= 3
	, TypeId_month_name			= 4
	, TypeId_unit				= 5
	, TypeId_ago 				= 6
	, TypeId_day_suffix			= 7	// 1st, 2nd
	, TypeId_day_relative		= 8	// today, tomorrow, yesterday
	, TypeId_time_relative		= 9 // now
	, TypeId_dash 				= Byte_ascii.Dash
	, TypeId_dot 				= Byte_ascii.Dot
	, TypeId_slash 				= Byte_ascii.Slash
	, TypeId_colon 				= Byte_ascii.Colon
	, TypeId_ws					= 98
	, TypeId_sym				= 99
	;	
}
abstract class Pxd_itm_base implements Pxd_itm {
	public abstract byte Tkn_tid();
	public int Ary_idx() {return ary_idx;} private int ary_idx;
	public int Seg_idx() {return seg_idx;} private int seg_idx = Seg_idx_null;
	public void Seg_idx_(int v) {seg_idx = v;}
	public int Data_idx() {return data_idx;} public void Data_idx_(int v) {data_idx = v;} private int data_idx;
	public abstract int Eval_idx();
	@gplx.Virtual public void Eval(Pxd_parser state) {}
	@gplx.Virtual public void Time_ini(DateAdpBldr bldr) {}
	public void Ctor(int ary_idx) {this.ary_idx = ary_idx;}
	public static final int Seg_idx_null = -1, Seg_idx_skip = -2;
}
interface Pxd_itm_prototype {
	Pxd_itm MakeNew(int ary_idx);
}
class DateAdpBldr {
	public DateAdp Date() {
		if (dirty) {	
			if (date == null) date = DateAdp_.seg_(seg_ary);	// date not set and seg_ary is dirty; make date = seg_ary;
			return date;
		}
		else
			return DateAdp_.Now();	// not dirtied; default to now;
	}
	public DateAdpBldr Date_(DateAdp v) {date = v; return this;} DateAdp date = null;
	public DateAdpBldr Seg_set(int idx, int val) {
		if (date == null) seg_ary[idx] = val;
		else {
			seg_ary = date.XtoSegAry();
			seg_ary[idx] = val;
			date = DateAdp_.seg_(seg_ary);
		}
		dirty = true;
		return this;
	}
	public DateAdp Bld() {
		return date == null ? DateAdp_.seg_(seg_ary) : date;
	}
	public DateAdpBldr(int... seg_ary) {this.seg_ary = seg_ary;}
	int[] seg_ary = new int[DateAdp_.SegIdx__max]; boolean dirty = false;
}
class Pft_func_time_log {
	private static final Gfo_msg_grp owner = Gfo_msg_grp_.new_(Xoa_app_.Nde, "time_parser");
	public static final Gfo_msg_itm
		  Invalid_day					= Gfo_msg_itm_.new_warn_(owner, "Invalid day: ~{0}")
		, Invalid_month					= Gfo_msg_itm_.new_warn_(owner, "Invalid month: ~{0}")
		, Invalid_year					= Gfo_msg_itm_.new_warn_(owner, "Invalid year: ~{0}")
		, Invalid_year_mid				= Gfo_msg_itm_.new_warn_(owner, "Invalid date: 4 digit year must be either yyyy-##-## or ##-##-yyyy")
		, Invalid_hour					= Gfo_msg_itm_.new_warn_(owner, "Invalid hour: ~{0}")
		, Invalid_minute				= Gfo_msg_itm_.new_warn_(owner, "Invalid minute: ~{0}")
		, Invalid_second				= Gfo_msg_itm_.new_warn_(owner, "Invalid second: ~{0}")
		, Invalid_date					= Gfo_msg_itm_.new_warn_(owner, "Invalid date: ~{0}")
		;
}
