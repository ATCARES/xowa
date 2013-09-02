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
package gplx.xowa.xtns.wdatas; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
public class Wdata_prop_itm_base_ {
	public static final Wdata_prop_itm_base[] Ary_empty = new Wdata_prop_itm_base[0];
	public static final byte Val_tid_unknown = 0, Val_tid_string = 1, Val_tid_entity = 2, Val_tid_time = 3, Val_tid_globecoordinate = 4;
	public static final String Val_str_string = "string", Val_str_entity = "wikibase-entityid", Val_str_time = "time", Val_str_globecoordinate = "globecoordinate";
	public static final byte[] Val_bry_string = bry_(Val_str_string), Val_bry_entity = bry_(Val_str_entity), Val_bry_time = bry_(Val_str_time), Val_bry_globecoordinate = bry_(Val_str_globecoordinate);
	public static byte Val_tid_parse(byte[] src, int bgn, int end) {
		Object bval_obj = Val_tid_regy.Get_by_mid(src, bgn, end);
		if	(bval_obj == null) return Val_tid_unknown;		
		return ((ByteVal)bval_obj).Val();
	}	private static final Hash_adp_bry Val_tid_regy = new Hash_adp_bry(false).Add_bry_byteVal(Val_bry_string, Wdata_prop_itm_base_.Val_tid_string).Add_bry_byteVal(Val_bry_entity, Wdata_prop_itm_base_.Val_tid_entity).Add_bry_byteVal(Val_bry_time, Wdata_prop_itm_base_.Val_tid_time).Add_bry_byteVal(Val_bry_globecoordinate, Wdata_prop_itm_base_.Val_tid_globecoordinate);
	public static String Val_tid_to_string(byte tid) {
		switch (tid) {
			case Val_tid_string				: return Val_str_string;
			case Val_tid_entity				: return Val_str_entity;
			case Val_tid_time				: return Val_str_time;
			case Val_tid_globecoordinate	: return Val_str_globecoordinate;
			default							: return "unknown";
		} 
	}
	public static final byte Snak_tid_novalue = 0, Snak_tid_value = 1;
	public static byte Snak_tid_parse(byte[] v) {
		Object bval_obj = Snak_tid_regy.Get_by_bry(v);
		if	(bval_obj == null) throw Err_.new_fmt_("unknown snak type_id: ~{0}", String_.new_utf8_(v));
		return ((ByteVal)bval_obj).Val();
	}	private static Hash_adp_bry Snak_tid_regy = new Hash_adp_bry(false).Add_bry_byteVal(Wdata_doc_consts.Val_prop_novalue_bry, Snak_tid_novalue).Add_str_byteVal(Wdata_doc_consts.Val_prop_value_str, Snak_tid_value);
	public static String Snak_tid_string(byte v) {
		switch (v) {
			case Snak_tid_value:	return Wdata_doc_consts.Val_prop_value_str;
			case Snak_tid_novalue:	return Wdata_doc_consts.Val_prop_novalue_str;
			default: 				return "unknown";
		}
	}
	public static byte[] Snak_tid_bry(byte v) {
		switch (v) {
			case Snak_tid_value:	return Wdata_doc_consts.Val_prop_value_bry;
			case Snak_tid_novalue:	return Wdata_doc_consts.Val_prop_novalue_bry;
			default: 				return null;
		}
	}
	public static final byte Rank_normal = 1;
	public static String Rank_string(byte v) {
		switch (v) {
			case Rank_normal: 	return "normal";
			default: 			return "unknown"; 
		}
	}
	public static final String Prop_type_statement = "statement";
	private static byte[] bry_(String s) {return ByteAry_.new_ascii_(s);}
}
