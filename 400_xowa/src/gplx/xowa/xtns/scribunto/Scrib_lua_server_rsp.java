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
package gplx.xowa.xtns.scribunto; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.php.*;
class Scrib_lua_server_rsp {
	Php_srl_parser parser = new Php_srl_parser();
	public Scrib_lua_server_rsp() {
		arg_keys.Add("op"			, ByteVal.new_(Arg_op));
		arg_keys.Add("values"		, ByteVal.new_(Arg_values));
		arg_keys.Add("id"			, ByteVal.new_(Arg_id));
		arg_keys.Add("args"			, ByteVal.new_(Arg_args));
	}	HashAdp arg_keys = HashAdp_.new_(); static final byte Arg_op = 0, Arg_values = 1, Arg_id = 2, Arg_args = 3;
	public String Op() {return op;} private String op;
	public String Call_id() {return call_id;} private String call_id;
	public KeyVal[] Rslt_ary() {return rslt_ary;} KeyVal[] rslt_ary;
	public KeyVal[] Values() {return values;} KeyVal[] values;
	public KeyVal[] Call_args() {return call_args;} KeyVal[] call_args;
	public String Extract(byte[] rsp) {
		try {
			op = call_id = null;
			rslt_ary = values = call_args = null;
			KeyVal[] root_ary = parser.Parse_as_kvs(rsp);
			rslt_ary = (KeyVal[])root_ary[0].Val();
			int len = rslt_ary.length;
			for (int i = 0; i < len; i++) {
				KeyVal kv = rslt_ary[i];
				String kv_key = kv.Key();
				ByteVal bv = (ByteVal)arg_keys.Fetch(kv_key);
				if	(bv != null) {
					switch (bv.Val()) {
						case Arg_op:		op = kv.Val_to_str_or_empty(); break;
						case Arg_values: 	values = (KeyVal[])kv.Val(); break;
						case Arg_id:		call_id = kv.Val_to_str_or_empty(); break;
						case Arg_args:		call_args = (KeyVal[])kv.Val(); break;
					}
				}
			}
			return op;
		}
		catch (Exception e) {
			throw Xow_xtn_scribunto.err_(e, "failed to extract data: {0} {1}", Err_.Message_gplx_brief(e), String_.new_utf8_(rsp));
		}
	}
}