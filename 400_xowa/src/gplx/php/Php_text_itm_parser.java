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
package gplx.php; import gplx.*;
public class Php_text_itm_parser {
	public static final byte Rslt_orig = 0, Rslt_dirty = 1, Rslt_fmt = 2;
	public byte[] Parse_as_bry(ListAdp list, byte[] raw, ByteRef rslt_ref, ByteAryBfr tmp_bfr) {
		Parse(list, raw, rslt_ref);
		byte[] rv = raw;
		switch (rslt_ref.Val()) {
			case Rslt_orig: break;
			case Rslt_dirty:
			case Rslt_fmt:
				tmp_bfr.Clear();
				int list_len = list.Count();
				for (int i = 0; i < list_len; i++) {
					Php_text_itm itm = (Php_text_itm)list.FetchAt(i);
					itm.Bld(tmp_bfr, raw);
				}
				rv = tmp_bfr.XtoAryAndClear();
				break;
		}
		return rv;
	}
	public void Parse(ListAdp rv, byte[] raw) {
		Parse(rv, raw, ByteRef.zero_());
	}
	public void Parse(ListAdp rv, byte[] raw, ByteRef rslt) {
		rv.Clear();
		int raw_len = raw.length; int raw_last = raw_len - 1; 
		int txt_bgn = -1;
		byte rslt_val = Rslt_orig;
		for (int i = 0; i < raw_len; i++) {
			byte b = raw[i];
			switch (b) {
				case Byte_ascii.Backslash:
					if (txt_bgn != -1) {rv.Add(new Php_text_itm_text(txt_bgn, i)); txt_bgn = -1; rslt_val = Rslt_dirty;}
					if (i == raw_last) throw Err_mgr._.fmt_auto_(GRP_KEY, "backslash_is_last_char", String_.new_utf8_(raw));
					int next_pos = i + 1;
					byte next_char = raw[next_pos];
					switch (next_char) {
						case Byte_ascii.Ltr_N:
						case Byte_ascii.Ltr_n:	next_char = Byte_ascii.NewLine; break;
						case Byte_ascii.Ltr_T:
						case Byte_ascii.Ltr_t:	next_char = Byte_ascii.Tab; break;
						case Byte_ascii.Ltr_R:
						case Byte_ascii.Ltr_r:	next_char = Byte_ascii.CarriageReturn; break;					
						case Byte_ascii.Ltr_U:
						case Byte_ascii.Ltr_u:	{	// EX: "\u007C"
							rslt_val = Rslt_dirty;
							Parse_utf16(rv, raw, next_pos + 1, raw_len);	// +1 to skip u
							i = next_pos + 4;	// +4 to skip utf16 seq; EX: \u007C; +4 for 007C
							continue;
						}
						case Byte_ascii.Ltr_X:
						case Byte_ascii.Ltr_x:	{	// EX: "\xc2"
							rslt_val = Rslt_dirty;
							byte[] literal = ByteAry_.Add(CONST_utf_prefix, ByteAry_.Mid(raw, next_pos + 1, next_pos + 3));
							rv.Add(new Php_text_itm_utf16(i, i + 4, literal));
							i = next_pos + 2;	// +2 to skip rest; EX: \xc2; +2 for c2
							continue;
						}
					}
					rv.Add(new Php_text_itm_escaped(i, next_pos, next_char)); rslt_val = Rslt_dirty;
					i = next_pos;
					break;
				case Byte_ascii.Dollar:
					if (txt_bgn != -1) {rv.Add(new Php_text_itm_text(txt_bgn, i)); txt_bgn = -1;}
					if (i == raw_last) {
						//throw Err_mgr._.fmt_auto_(GRP_KEY, "dollar_is_last_char", String_.new_utf8_(raw));
					}
					int int_end = Find_fwd_non_int(raw, i + 1, raw_len);	// +1 to search after $
					int int_val = ByteAry_.X_to_int_or(raw, i + 1, int_end, -1); // +1 to search after $
					if (int_val == -1) {
						rv.Add(new Php_text_itm_text(i, i + 1)); 
						continue;
					}
					//throw Err_mgr._.fmt_auto_(GRP_KEY, "invalid_arg", String_.new_utf8_(raw));
					rv.Add(new Php_text_itm_arg(i, int_end, int_val));
					rslt_val = Rslt_fmt;
					i = int_end - 1;	// -1 b/c i++ in for loop 
					break;
				default:
					if (txt_bgn == -1) txt_bgn = i;
					break;
			}
		}	
		if (txt_bgn != -1) {rv.Add(new Php_text_itm_text(txt_bgn, raw_len)); txt_bgn = -1; rslt_val = Rslt_dirty;}
		rslt.Val_(rslt_val);
	}	static final byte[] CONST_utf_prefix = ByteAry_.new_ascii_("\\u00");
	private void Parse_utf16(ListAdp rv, byte[] src, int bgn, int src_len) {
		int end = bgn + 4;
		if (end >= src_len) throw Err_mgr._.fmt_auto_(GRP_KEY, "utf16_parse", String_.new_utf8_(src));
		int v = Int_.Xto_int_hex(src, bgn, end);	// +2; skip "\" + "u"
		byte[] literal = gplx.intl.Utf16_.Encode_int_to_bry(v);
		rv.Add(new Php_text_itm_utf16(bgn, end, literal));
	}
	static final String GRP_KEY = "xowa.php.quote_text_parser";
	public static int Find_fwd_non_int(byte[] src, int bgn, int end) {
		for (int i = bgn; i < end; i++) {
			byte b = src[i];
			switch (b) {
				case Byte_ascii.Num_0: case Byte_ascii.Num_1: case Byte_ascii.Num_2: case Byte_ascii.Num_3: case Byte_ascii.Num_4:
				case Byte_ascii.Num_5: case Byte_ascii.Num_6: case Byte_ascii.Num_7: case Byte_ascii.Num_8: case Byte_ascii.Num_9:
					break;
				default:
					return i;
			}
		}
		return end;
	}
}
