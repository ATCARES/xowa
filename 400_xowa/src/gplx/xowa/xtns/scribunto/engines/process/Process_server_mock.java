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
package gplx.xowa.xtns.scribunto.engines.process; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*; import gplx.xowa.xtns.scribunto.*; import gplx.xowa.xtns.scribunto.engines.*;
public class Process_server_mock implements Scrib_server {
	ListAdp rsps = ListAdp_.new_(); int rsps_idx = 0;
	public void Init(String... process_args) {}
	public int Server_timeout() {return server_timeout;} public Scrib_server Server_timeout_(int v) {server_timeout = v; return this;} private int server_timeout = 8000;
	public int Server_timeout_polling() {return server_timeout_polling;} public Scrib_server Server_timeout_polling_(int v) {server_timeout_polling = v; return this;} private int server_timeout_polling = 1;
	public int Server_timeout_busy_wait() {return server_timeout_busy_wait;} public Scrib_server Server_timeout_busy_wait_(int v) {server_timeout_busy_wait = v; return this;} private int server_timeout_busy_wait = 250;
	public byte[] Server_comm(byte[] cmd, Object[] cmd_objs) {
		Server_send(cmd, cmd_objs);
		return Server_recv();
	}
	public void Server_send(byte[] cmd, Object[] cmd_objs) {
		this.cmd_objs = cmd_objs;
		log_rcvd.Add(String_.new_utf8_(cmd));
	}	Object[] cmd_objs;
	public byte[] Server_recv() {
		Process_server_mock_rcvd rcvd = (Process_server_mock_rcvd)rsps.FetchAt(rsps_idx++);
		String rv = rcvd.Bld(cmd_objs);
		log_sent.Add(rv);
		return ByteAry_.new_utf8_(rv);
	}
	public void Term() {}
	public void Clear() {rsps.Clear(); rsps_idx = 0; log_rcvd.Clear(); log_sent.Clear();}
	public boolean Print_key() {return print_key;} public Process_server_mock Print_key_(boolean v) {print_key = v; return this;} private boolean print_key;
	public void Prep_add(String v) {rsps.Add(new Process_server_mock_rcvd_str(v));}
	public void Prep_add_dynamic_val() {rsps.Add(new Process_server_mock_rcvd_val(print_key));}
	public ListAdp Log_rcvd() {return log_rcvd;} ListAdp log_rcvd = ListAdp_.new_();
	public ListAdp Log_sent() {return log_sent;} ListAdp log_sent = ListAdp_.new_();
}
interface Process_server_mock_rcvd {
	String Bld(Object[] cmd_obs); 
}
class Process_server_mock_rcvd_str implements Process_server_mock_rcvd {
	public Process_server_mock_rcvd_str(String rcvd) {this.rcvd = rcvd;} private String rcvd;
	public String Bld(Object[] cmd_obs) {return rcvd;}
}
class Process_server_mock_rcvd_val implements Process_server_mock_rcvd {
	public Process_server_mock_rcvd_val(boolean print_key) {this.print_key = print_key;} private boolean print_key;
	public String Bld(Object[] cmd_objs) {
		ByteAryBfr tmp_bfr = ByteAryBfr.new_();
		Bld_recursive(tmp_bfr, 0, (KeyVal[])cmd_objs[5]);
		byte[] values_str = tmp_bfr.XtoAryAndClear();
		tmp_bfr.Add(Bry_rv_bgn).Add_int_variable(values_str.length).Add(Bry_rv_mid).Add(values_str).Add(Bry_rv_end);
		return tmp_bfr.XtoStrAndClear();
	}
	private void Bld_recursive(ByteAryBfr bfr, int depth, KeyVal[] ary) {
		int len = ary.length;
		for (int i = 0; i < len; i++) {
			if (i != 0) bfr.Add_byte(Byte_ascii.Semic);
			KeyVal kv = ary[i];
			Object kv_val = kv.Val();
			if (kv_val == null) {
				bfr.Add(gplx.json.Json_itm_.Const_null);
				continue;
			}
			Class<?> kv_val_type = kv_val.getClass();
			boolean kv_val_is_array = ClassAdp_.Eq(kv_val_type, KeyVal[].class);
			if (print_key && !kv_val_is_array)
				bfr.Add_str(kv.Key()).Add_byte(Byte_ascii.Colon);
			if		(ClassAdp_.Eq(kv_val_type, Bool_.ClassOf))
				bfr.Add(Bool_.cast_(kv_val) ? gplx.json.Json_itm_.Const_true : gplx.json.Json_itm_.Const_false);
			else if	(kv_val_is_array) {
				KeyVal[] sub = (KeyVal[])kv_val;
				if (sub.length == 0) {bfr.Add_byte(Byte_ascii.Curly_bgn).Add_byte(Byte_ascii.Curly_end);}
				else {
					bfr.Add_byte_nl();
					bfr.Add_byte_repeat(Byte_ascii.Space, (depth + 1) * 2);
					Bld_recursive(bfr, depth + 1, (KeyVal[])kv_val);
				}
			}
			else
				bfr.Add_str(kv.Val_to_str_or_empty());
		}
	}

	private static final byte[] Bry_rv_bgn = ByteAry_.new_ascii_("a:3:{s:2:\"op\";s:6:\"return\";s:7:\"nvalues\";i:1;s:6:\"values\";a:1:{i:1;s:"), Bry_rv_mid = ByteAry_.new_ascii_(":\""), Bry_rv_end = ByteAry_.new_ascii_("\";}}");
}
