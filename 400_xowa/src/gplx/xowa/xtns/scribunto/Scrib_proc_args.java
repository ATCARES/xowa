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
public class Scrib_proc_args {		
	private KeyVal[] ary; private int ary_len;
	public Scrib_proc_args(KeyVal[] v) {Init(v);}
	public int Len() {return ary_len;}
	public KeyVal[] Ary() {return ary;}
	public String	Pull_str(int i)					{Object rv = Get_or_fail(i); return String_.cast_(rv);}
	public byte[]	Pull_bry(int i)					{Object rv = Get_or_fail(i); return ByteAry_.new_utf8_(String_.cast_(rv));}
	public int		Pull_int(int i)					{Object rv = Get_or_fail(i);
		try {return Int_.coerce_(rv);} // coerce to handle "1" and 1; will still fail if "abc" is passed
		catch (Exception e) {
			Err_.Noop(e);
			throw Err_.new_fmt_("bad argument; int expected; idx={0} len={1}", i, ary_len);
		}
	}	
	public String	Cast_str_or_null(int i)			{Object rv = Get_or_null(i); return rv == null ? null			: String_.cast_		(rv);}
	public byte[]	Cast_bry_or_null(int i)			{Object rv = Get_or_null(i); return rv == null ? null			: ByteAry_.new_utf8_(String_.cast_	(rv));}	// NOTE: cast is deliberate; Scrib call checkType whi
	public byte[]	Cast_bry_or_empty(int i)		{Object rv = Get_or_null(i); return rv == null ? ByteAry_.Empty : ByteAry_.new_utf8_(String_.cast_	(rv));}
	public byte[]	Cast_bry_or(int i, byte[] or)	{Object rv = Get_or_null(i); return rv == null ? or				: ByteAry_.new_utf8_(String_.cast_	(rv));}
	public Object	Cast_obj_or_null(int i)			{return Get_or_null(i);}
	public boolean		Cast_bool_or_y(int i)			{Object rv = Get_or_null(i); return rv == null ? Bool_.Y		: Bool_.cast_(rv);}
	public boolean		Cast_bool_or_n(int i)			{Object rv = Get_or_null(i); return rv == null ? Bool_.N		: Bool_.cast_(rv);}
	public int		Cast_int_or(int i, int or)		{Object rv = Get_or_null(i); return rv == null ? or				: Int_.coerce_(rv);}	// coerce to handle "1" and 1;
	public String	Form_str_or_null(int i)			{Object rv = Get_or_null(i); return rv == null ? null			: Object_.XtoStr_OrNull(rv);}	// NOTE: Modules can throw exceptions in which return value is nothing; do not fail; return ""; EX: -logy; DATE:2013-10-14
	public byte[]	Form_bry_or_null(int i)			{Object rv = Get_or_null(i); return rv == null ? null			: ByteAry_.new_utf8_(Object_.XtoStr_OrNull(rv));}
	public KeyVal[] Pull_kv_ary(int i) {
		Object rv = Get_or_fail(i);
		return (KeyVal[])rv;
	}
	public byte[][]	Cast_params_as_bry_ary_or_empty(int params_idx)	{
		if (params_idx < 0 || params_idx >= ary_len) return ByteAry_.Ary_empty;
		int rv_len = ary_len - params_idx;
		byte[][] rv = new byte[rv_len][];
		for (int i = 0; i < rv_len; i++) {
			KeyVal kv = ary[i + params_idx];
			rv[i] = ByteAry_.new_utf8_(String_.cast_(kv.Val()));
		}
		return rv;
	}
	public byte[] Extract_qry_args(Xow_wiki wiki, int idx) {
		Object qry_args_obj = Cast_obj_or_null(idx);
		if (qry_args_obj == null) return ByteAry_.Empty;
		Class<?> qry_args_cls = ClassAdp_.ClassOf_obj(qry_args_obj);
		if		(qry_args_cls == String.class)
			return ByteAry_.new_utf8_((String)qry_args_obj);
		else if (qry_args_cls == KeyVal[].class) {
			ByteAryBfr bfr = wiki.Utl_bry_bfr_mkr().Get_b128();
			KeyVal[] kvs = (KeyVal[])qry_args_obj;
			int len = kvs.length;
			for (int i = 0; i < len; i++) {
				KeyVal kv = kvs[i];
				if (i != 0) bfr.Add_byte(Byte_ascii.Amp);
				bfr.Add_str(kv.Key());
				bfr.Add_byte(Byte_ascii.Eq);
				bfr.Add_str(kv.Val_to_str_or_empty());
			}
			return bfr.Mkr_rls().XtoAryAndClear();
		}
		else {
			wiki.App().Gui_wtr().Warn_many("", "", "unknown type for GetUrl query args: ~{0}", ClassAdp_.NameOf_type(qry_args_cls));
			return ByteAry_.Empty;
		}
	}
	private void Init(KeyVal[] v) {
		int v_len = v.length;
		if (v_len == 0) {
			ary = KeyVal_.Ary_empty;
			ary_len = 0;
			return;
		}
		int v_max = -1;
		for (int i = 0; i < v_len; i++) {
			KeyVal kv = v[i];
			int idx = Int_.cast_(kv.Key_as_obj());
			if (v_max < idx) v_max = idx;
		}
		this.ary_len = v_max;
		if (v_max == v_len) {		// keys are in sequential order; EX: [1:a,2:b,3:c]
			this.ary = v;
		}
		else {						// keys are not in sequential order, or there are gaps; EX: [1:a,3:c]
			ary = new KeyVal[ary_len];
			for (int i = 0; i < v_len; i++) {
				KeyVal kv = v[i];
				int idx = Int_.cast_(kv.Key_as_obj());
				ary[idx - ListAdp_.Base1] = kv;
			}
		}
	}
	private Object Get_or_null(int i) {
		if (i < 0 || i >= ary_len) return null;
		KeyVal kv = ary[i];
		return kv == null ? null : kv.Val();
	}
	private Object Get_or_fail(int i) {
		if (i < 0 || i >= ary_len) throw Err_.new_fmt_("bad argument: nil; idx={0} len={1}", i, ary_len);
		KeyVal kv = ary[i];
		Object rv = kv == null ? null : kv.Val();
		if (rv == null) throw Err_.new_fmt_("scrib arg is null; idx={0} len={1}", i, ary_len);
		return rv;
	}
}
