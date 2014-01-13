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
package gplx.xowa.xtns.scribunto; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.texts.*;
class Scrib_lib_ustring implements Scrib_lib {
	public Scrib_lib_ustring(Scrib_engine engine) {this.engine = engine; gsub_mgr = new Scrib_lib_ustring_gsub_mgr(engine, regx_converter);} Scrib_engine engine; Scrib_lib_ustring_gsub_mgr gsub_mgr;
	public Scrib_mod Mod() {return mod;} Scrib_mod mod;
	public int String_len_max() {return string_len_max;} public Scrib_lib_ustring String_len_max_(int v) {string_len_max = v; return this;} private int string_len_max = Xoa_page.Page_len_max;
	public int Pattern_len_max() {return pattern_len_max;} public Scrib_lib_ustring Pattern_len_max_(int v) {pattern_len_max = v; return this;} private int pattern_len_max = 10000;
	public Scrib_lua_regx_converter Regx_converter() {return regx_converter;} Scrib_lua_regx_converter regx_converter = new Scrib_lua_regx_converter();
	public Scrib_mod Register(Scrib_engine engine, Io_url script_dir) {
		mod = engine.RegisterInterface(script_dir.GenSubFil("mw.ustring.lua"), this
			, String_.Ary(Invk_find, Invk_match, Invk_gmatch_init, Invk_gmatch_callback, Invk_gsub)
			, KeyVal_.new_("stringLengthLimit", string_len_max)
			, KeyVal_.new_("patternLengthLimit", pattern_len_max)
			);
		return mod;
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_find))						return Find((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_match))						return Match((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_gsub))						return Gsub((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_gmatch_init))				return Gmatch_init((KeyVal[])m.CastObj("v"));
		else if	(ctx.Match(k, Invk_gmatch_callback))			return Gmatch_callback((KeyVal[])m.CastObj("v"));
		else	return GfoInvkAble_.Rv_unhandled;
	}
	public static final String Invk_find = "find", Invk_match = "match", Invk_gmatch_init = "gmatch_init", Invk_gmatch_callback = "gmatch_callback", Invk_gsub = "gsub";
	public KeyVal[] Find(KeyVal[] values) {
		String text = Scrib_kv_utl.Val_to_str(values, 0);
		String regx = Scrib_kv_utl.Val_to_str(values, 1);
		int bgn = Scrib_kv_utl.Val_to_int_or(values, 2, 1);
		bgn = Bgn_adjust(text, bgn);
		boolean plain = Scrib_kv_utl.Val_to_bool_or(values, 3, false);
		if (plain) {
			int pos = String_.FindFwd(text, regx, bgn);
			boolean found = pos != ByteAry_.NotFound;
			if (!found) return KeyVal_.Ary_empty;
			return Scrib_kv_utl.base1_many_(pos + Scrib_lib_ustring.Base1, pos + Scrib_lib_ustring.Base1 + String_.Len(regx) - Scrib_lib_ustring.End_adj);
		}
		else
			regx = regx_converter.Parse(ByteAry_.new_utf8_(regx), false);
		RegxAdp regx_adp = Scrib_lib_ustring.RegxAdp_new_(engine.Ctx(), regx);
		RegxMatch[] regx_rslts = regx_adp.Match_all(text, bgn);
		int len = regx_rslts.length;
		if (len == 0) return KeyVal_.Ary_empty;
		ListAdp tmp_list = ListAdp_.new_();
		for (int i = 0; i < len; i++) {
			RegxMatch rslt = regx_rslts[i];
			tmp_list.Add(rslt.Find_bgn() + Scrib_lib_ustring.Base1);
			tmp_list.Add(rslt.Find_end() + Scrib_lib_ustring.Base1 - Scrib_lib_ustring.End_adj);
			AddCapturesFromMatch(tmp_list, rslt, text, false);
		}
		return Scrib_kv_utl.base1_list_(tmp_list);
	}
	private int Bgn_adjust(String text, int bgn) {
//			if (bgn == 0) return 0;
		if (bgn > 0) bgn -= Scrib_lib_ustring.Base1;
		int text_len = String_.Len(text);
		if		(bgn < 0)			// negative number means search from rear of String
			bgn += text_len;		// NOTE: PHP has extra + 1 for Base 1
		else if (bgn > text_len)	// bgn > text_len; confine to text_len; NOTE: PHP has extra + 1 for Base 1
			bgn = text_len;			// NOTE: PHP has extra + 1 for Base 1
		return bgn;
	}
	public KeyVal[] Match(KeyVal[] values) {
		String text = Scrib_kv_utl.Val_to_str(values, 0);
		String regx = regx_converter.Parse(Scrib_kv_utl.Val_to_bry(values, 1), false);
		int bgn = Scrib_kv_utl.Val_to_int_or(values, 2, 1);
		bgn = Bgn_adjust(text, bgn);
		RegxAdp regx_adp = Scrib_lib_ustring.RegxAdp_new_(engine.Ctx(), regx);
		RegxMatch[] regx_rslts = regx_adp.Match_all(text, bgn);
		int len = regx_rslts.length;
		ListAdp tmp_list = ListAdp_.new_();
		for (int i = 0; i < len; i++) {
			RegxMatch rslt = regx_rslts[i];
			AddCapturesFromMatch(tmp_list, rslt, text, true);
		}
		return Scrib_kv_utl.base1_list_(tmp_list);
	}
	public KeyVal[] Gsub(KeyVal[] values) {return gsub_mgr.Exec(values);}
	public KeyVal[] Gmatch_init(KeyVal[] values) {
		// String text = Scrib_kv_utl.Val_to_str(values, 0);
		byte[] regx = Scrib_kv_utl.Val_to_bry(values, 1);
		String pcre = regx_converter.Parse(regx, true);
		ListAdp captures = regx_converter.Grps();
		return Scrib_kv_utl.base1_many_(pcre, Scrib_kv_utl.base1_list_(captures));
	}
	public KeyVal[] Gmatch_callback(KeyVal[] values) {
		String text = Scrib_kv_utl.Val_to_str_force(values, 0);	// NOTE: UstringLibrary.php!ustringGmatchCallback calls preg_match directly; $s can be any type, and php casts automatically; 
		String regx = Scrib_kv_utl.Val_to_str(values, 1);
		// byte[] capt = Scrib_kv_utl.Val_to_bry(values, 2);	// TODO.4: not sure what this does
		int pos = Scrib_kv_utl.Val_to_int(values, 3);
		RegxAdp regx_adp = Scrib_lib_ustring.RegxAdp_new_(engine.Ctx(), regx);
		RegxMatch[] regx_rslts = regx_adp.Match_all(text, pos);
		int len = regx_rslts.length;
		if (len == 0) return Scrib_kv_utl.base1_many_(pos, KeyVal_.Ary_empty);
		RegxMatch rslt = regx_rslts[0];	// NOTE: take only 1st result
		ListAdp tmp_list = ListAdp_.new_();
		AddCapturesFromMatch(tmp_list, rslt, text, false);
		return Scrib_kv_utl.base1_many_(rslt.Find_end(), Scrib_kv_utl.base1_list_(tmp_list));
	}
	private void AddCapturesFromMatch(ListAdp tmp_list, RegxMatch rslt, String text, boolean op_is_match) {// NOTE: this matches behavior in UstringLibrary.php!addCapturesFromMatch
		RegxGroup[] grps = rslt.Groups();
		int grps_len = grps.length;
		if (grps_len > 0) {
			for (int j = 0; j < grps_len; j++) {
				RegxGroup grp = grps[j];
				if (grp.End() - grp.Bgn() == 0)	// empty grp; return pos only; EX: "()"
					tmp_list.Add(Int_.XtoStr(grp.Bgn() + Scrib_lib_ustring.Base1));	// NOTE: always return as String; callers expect String, and may do operations like len(result), which will fail if int; DATE:2013-12-20
				else
					tmp_list.Add(grp.Val());
			}
		}
		else if (op_is_match)	// if op_is_match, and no captures, extract find_txt; note that UstringLibrary.php says "$arr[] = $m[0][0];" which means get the 1st match; EX: "aaaa", "a" will have four matches; get 1st
			tmp_list.Add(String_.Mid(text, rslt.Find_bgn(), rslt.Find_end()));
	}
	public static RegxAdp RegxAdp_new_(Xop_ctx ctx, String regx) {
		RegxAdp rv = RegxAdp_.new_(regx);
		if (rv.Pattern_is_invalid()) {
			ctx.App().Usr_dlg().Warn_many("", "", "regx is invalid: regx=~{0} page=~{1}", regx, String_.new_utf8_(ctx.Page().Page_ttl().Page_db()));
		}
		return rv;
	}
	static final int Base1 = 1, End_adj = 1;
}
class Scrib_lib_ustring_gsub_mgr {
	public Scrib_lib_ustring_gsub_mgr(Scrib_engine engine, Scrib_lua_regx_converter regx_converter) {this.engine = engine; this.regx_converter = regx_converter;} Scrib_engine engine; Scrib_lua_regx_converter regx_converter;
	private byte tmp_repl_tid = Repl_tid_null; private byte[] tmp_repl_bry = null;
	private HashAdp repl_hash = null; private Scrib_fnc repl_func = null; private ByteAryBfr tmp_bfr;
	int repl_count = 0;
	public KeyVal[] Exec(KeyVal[] values) {
		Object text_obj = Scrib_kv_utl.Val_to_obj(values, 0);
		String text = String_.as_(text_obj);
		if (text == null) text = Object_.XtoStr_OrEmpty(text_obj);
		String regx = Scrib_kv_utl.Val_to_str(values, 1);
		if (values.length == 2) return Scrib_kv_utl.base1_obj_(text);	// if no replace arg, return self; EX:enwikt:'orse; DATE:2013-10-13
		Object repl_obj = values[2].Val();
		regx = regx_converter.Parse(ByteAry_.new_utf8_(regx), false);
		int limit = Scrib_kv_utl.Val_to_int_or(values, 3, -1);
		repl_count = 0;
		Identify_repl(repl_obj);
		String repl = Exec_repl(tmp_repl_tid, tmp_repl_bry, text, regx, limit);
		return Scrib_kv_utl.base1_many_(repl, repl_count);
	}
	private void Identify_repl(Object repl_obj) {
		Class<?> repl_type = repl_obj.getClass();
		if		(Object_.Eq(repl_type, String.class)) {
			tmp_repl_tid = Repl_tid_string;
			tmp_repl_bry = ByteAry_.new_utf8_((String)repl_obj);
		}
		else if	(Object_.Eq(repl_type, KeyVal[].class)) {
			tmp_repl_tid = Repl_tid_table;
			KeyVal[] repl_tbl = (KeyVal[])repl_obj;
			if (repl_hash == null) repl_hash = HashAdp_.new_();
			int repl_tbl_len = repl_tbl.length;
			for (int i = 0; i < repl_tbl_len; i++) {
				KeyVal repl_itm = repl_tbl[i];
				String repl_itm_val = repl_itm.Val_to_str_or_empty();
				repl_hash.Add(repl_itm.Key(), ByteAry_.new_utf8_(repl_itm_val));
			}
		}
		else if	(Object_.Eq(repl_type, Scrib_fnc.class)) {
			tmp_repl_tid = Repl_tid_luacbk;
			repl_func = (Scrib_fnc)repl_obj;
		}
		else throw Err_.unhandled(ClassAdp_.NameOf_type(repl_type));
	}
	private String Exec_repl(byte repl_tid, byte[] repl_bry, String text, String regx, int limit) {
		RegxAdp regx_mgr = Scrib_lib_ustring.RegxAdp_new_(engine.Ctx(), regx);
		RegxMatch[] rslts = regx_mgr.Match_all(text, 0);
		if (rslts.length == 0) return text;	// PHP: If matches are found, the new subject will be returned, otherwise subject will be returned unchanged.; http://php.net/manual/en/function.preg-replace-callback.php
		if (tmp_bfr == null) tmp_bfr = ByteAryBfr.new_();
		int len = rslts.length;
		int pos = 0;
		for (int i = 0; i < len; i++) {
			if (limit > -1 && repl_count == limit) break;
			RegxMatch rslt = rslts[i];
			tmp_bfr.Add_str(String_.Mid(text, pos, rslt.Find_bgn()));	// NOTE: regx returns char pos (not bry); must add as String, not bry; DATE:2013-07-17
			Exec_repl_itm(repl_tid, repl_bry, text, rslt);
			pos = rslt.Find_end();
			++repl_count;
		}
		int text_len = String_.Len(text);
		if (pos < text_len)
			tmp_bfr.Add_str(String_.Mid(text, pos, text_len));			// NOTE: regx returns char pos (not bry); must add as String, not bry; DATE:2013-07-17
		return tmp_bfr.XtoStrAndClear();
	}
	private void Exec_repl_itm(byte repl_tid, byte[] repl_bry, String text, RegxMatch rslt) {
		switch (repl_tid) {
			case Repl_tid_string:
				int len = repl_bry.length;
				for (int i = 0; i < len; i++) {
					byte b = repl_bry[i];
					switch (b) {
						case Byte_ascii.Percent: {
							++i;
							if (i == len)	// % at end of stream; just add %;
								tmp_bfr.Add_byte(b);							
							else {
								b = repl_bry[i];
								switch (b) {
									case Byte_ascii.Num_0: case Byte_ascii.Num_1: case Byte_ascii.Num_2: case Byte_ascii.Num_3: case Byte_ascii.Num_4:
									case Byte_ascii.Num_5: case Byte_ascii.Num_6: case Byte_ascii.Num_7: case Byte_ascii.Num_8: case Byte_ascii.Num_9:
										int idx = b - Byte_ascii.Num_0 - ListAdp_.Base1;
										if (idx < rslt.Groups().length) {	// retrieve numbered capture; TODO: support more than 9 captures
											RegxGroup grp = rslt.Groups()[idx];
											tmp_bfr.Add_str(String_.Mid(text, grp.Bgn(), grp.End()));	// NOTE: grp.Bgn() / .End() is for String pos (bry pos will fail for utf8 strings)
										}
										else {
											tmp_bfr.Add_byte(Byte_ascii.Percent);
											tmp_bfr.Add_byte(b);
										}
										break;
									case Byte_ascii.Percent:
										tmp_bfr.Add_byte(Byte_ascii.Percent);
										break;
									default:	// not a number; add literal
										tmp_bfr.Add_byte(Byte_ascii.Percent);
										tmp_bfr.Add_byte(b);	
										break;
								}
							}
							break;
						}
						default:
							tmp_bfr.Add_byte(b);
							break;
					}
				}
				break;
			case Repl_tid_table: {
				String find_str = String_.Mid(text, rslt.Find_bgn(), rslt.Find_end());	// NOTE: rslt.Bgn() / .End() is for String pos (bry pos will fail for utf8 strings)
				Object actl_repl_obj = repl_hash.Fetch(find_str);
				if (actl_repl_obj != null)
					tmp_bfr.Add((byte[])actl_repl_obj);
				break;
			}
			case Repl_tid_luacbk: {
				KeyVal[] luacbk_args = null;
				RegxGroup[] grps = rslt.Groups();
				int grps_len = grps.length;
				if (grps_len == 0) {	// no match; use original String
					String find_str = String_.Mid(text, rslt.Find_bgn(), rslt.Find_end());
					luacbk_args = Scrib_kv_utl.base1_obj_(find_str);
				}
				else {					// match; build ary of matches; (see UStringLibrary.php)
					luacbk_args = new KeyVal[grps_len];
					for (int i = 0; i < grps_len; i++) {
						RegxGroup grp = grps[i];
						String find_str = String_.Mid(text, grp.Bgn(), grp.End());
						luacbk_args[i] = KeyVal_.int_(i + Scrib_interpreter.Base_1, find_str);
					}
				}
				KeyVal[] rslts = engine.Interpreter().CallFunction(repl_func.Id(), luacbk_args);
				tmp_bfr.Add_str(Scrib_kv_utl.Val_to_str(rslts, 0));
				break;
			}
			default: throw Err_.unhandled(repl_tid);
		}
	}
	static final byte Repl_tid_null = 0, Repl_tid_string = 1, Repl_tid_table = 2, Repl_tid_luacbk = 3;
}
