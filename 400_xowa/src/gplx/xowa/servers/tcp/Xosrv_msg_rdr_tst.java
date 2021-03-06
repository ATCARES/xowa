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
package gplx.xowa.servers.tcp; import gplx.*; import gplx.xowa.*; import gplx.xowa.servers.*;
import org.junit.*;
import gplx.ios.*;
public class Xosrv_msg_rdr_tst {
	@Before public void init() {fxt.Clear();} private Xosrv_msg_rdr_fxt fxt = new Xosrv_msg_rdr_fxt();
	@Test  public void Parse() {
		String raw = "0|0000000045|0000000091|cmd_0|id_0|sender_0|recipient_0|date_0|text_0";
		Xosrv_msg msg = fxt.Test_parse_msg(raw, "cmd_0", "id_0", "sender_0", "recipient_0", "date_0", "text_0");
		fxt.Test_print(msg, raw);
	}
	@Test   public void Err_header_is_invalid() 			{fxt.Test_parse_err("abcde", "header is invalid");}
	@Test   public void Err_checksum_failed() 				{fxt.Test_parse_err("0|0000000000|0000000000|", "checksum failed");}
	@Test   public void Err_cmd_missing() 					{fxt.Test_parse_err("0|0000000001|0000000003|a", "pipe not found for cmd_name");}
}
class Xosrv_msg_rdr_fxt {
	public Xosrv_msg_rdr_fxt Clear() {
		if (msg_rdr == null) {
			msg_rdr_stream = new IoStream_mock();
			msg_rdr = new Xosrv_msg_rdr(ByteAry_.Empty, msg_rdr_stream);
		}
		msg_rdr_stream.Reset();
		return this;
	}	private Xosrv_msg_rdr msg_rdr; private IoStream_mock msg_rdr_stream;
	public Xosrv_msg Test_parse_msg(String raw, String expd_cmd, String expd_id, String expd_sender, String expd_recipient, String expd_date, String expd_text) {
		byte[] raw_bry = ByteAry_.new_ascii_(raw);
		msg_rdr_stream.Data_bry_(raw_bry).Read_limit_(raw_bry.length);
		Xosrv_msg msg = msg_rdr.Read();
		Tfds.Eq(String_.new_ascii_(msg.Cmd_name())		, expd_cmd);
		Tfds.Eq(String_.new_ascii_(msg.Msg_id())		, expd_id);
		Tfds.Eq(String_.new_ascii_(msg.Sender())		, expd_sender);
		Tfds.Eq(String_.new_ascii_(msg.Recipient())		, expd_recipient);
		Tfds.Eq(String_.new_ascii_(msg.Msg_date())		, expd_date);
		Tfds.Eq(String_.new_ascii_(msg.Msg_text())		, expd_text);
		return msg;
	}
	public void Test_parse_err(String raw, String expd_err) {
		byte[] raw_bry = ByteAry_.new_ascii_(raw);
		msg_rdr_stream.Data_bry_(raw_bry).Read_limit_(raw_bry.length);
		Xosrv_msg msg = msg_rdr.Read();
		String msg_text = String_.new_ascii_(msg.Msg_text());
		Tfds.Eq_true(String_.HasAtBgn(msg_text, expd_err), msg_text);
	}
	public void Test_print(Xosrv_msg msg, String expd) {
		ByteAryBfr bfr = ByteAryBfr.new_();
		msg.Print(bfr);
		Tfds.Eq(expd, bfr.XtoStrAndClear());
	}
}
