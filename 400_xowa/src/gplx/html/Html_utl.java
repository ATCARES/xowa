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
package gplx.html; import gplx.*;
public class Html_utl {
	public static byte[] Escape_for_atr_val_as_bry(String s, byte quote_byte) {
		ByteAryBfr tmp_bfr = null;
		if (s == null) return null;
		byte[] bry = ByteAry_.new_utf8_(s);
		boolean dirty = false;
		int len = bry.length;
		for (int i = 0; i < len; i++) {
			byte b = bry[i];
			if (b == quote_byte) {
				if (!dirty) {
					tmp_bfr = ByteAryBfr.reset_(256);
					tmp_bfr.Add_mid(bry, 0, i);
					dirty = true;
				}
				tmp_bfr.Add_byte(Byte_ascii.Backslash).Add_byte(quote_byte);
			}
			else {
				if (dirty)
					tmp_bfr.Add_byte(b);
			}
		}
		return dirty ? tmp_bfr.XtoAryAndClear() : bry;
	}
	public static String Escape_html_as_str(String v) {return String_.new_utf8_(Escape_html_as_bry(ByteAry_.new_utf8_(v)));}
	public static byte[] Escape_html_as_bry(byte[] bry) {return Escape_html_as_bry(bry, true, true, true, true);}
	public static byte[] Escape_html_as_bry(byte[] bry, boolean escape_lt, boolean escape_gt, boolean escape_amp, boolean escape_quote) {
		ByteAryBfr tmp_bfr = null;
		if (bry == null) return null;
		boolean dirty = false; byte[] escaped = null;
		int len = bry.length;
		for (int i = 0; i < len; i++) {
			byte b = bry[i];
			switch (b) {
				case Byte_ascii.Lt: 	if (escape_lt)		escaped = Html_consts.Lt; break;
				case Byte_ascii.Gt: 	if (escape_gt)		escaped = Html_consts.Gt; break;
				case Byte_ascii.Amp:	if (escape_amp)		escaped = Html_consts.Amp; break;
				case Byte_ascii.Quote:	if (escape_quote)	escaped = Html_consts.Quote; break;
				default:
					if (dirty)
						tmp_bfr.Add_byte(b);
					break;
			}
			if (escaped != null) {
				if (!dirty) {
					tmp_bfr = ByteAryBfr.reset_(256);
					tmp_bfr.Add_mid(bry, 0, i);
					dirty = true;
				}
				tmp_bfr.Add(escaped);
				escaped = null;
			}
		}
		return dirty ? tmp_bfr.XtoAryAndClear() : bry;
	}
	public static void Escape_html_to_bfr_lt(ByteAryBfr bfr, byte[] bry) {Escape_html_to_bfr(bfr, bry, 0, bry.length, true, false, false, false);}
	public static void Escape_html_to_bfr(ByteAryBfr bfr, byte[] bry, int bgn, int end, boolean escape_lt, boolean escape_gt, boolean escape_amp, boolean escape_quote) {
		for (int i = bgn; i < end; i++) {
			byte b = bry[i];
			switch (b) {
				case Byte_ascii.Lt: 	if (escape_lt)		{bfr.Add(Html_consts.Lt); continue;} break;
				case Byte_ascii.Gt: 	if (escape_gt)		{bfr.Add(Html_consts.Gt); continue;} break;
				case Byte_ascii.Amp:	if (escape_amp)		{bfr.Add(Html_consts.Amp); continue;} break;
				case Byte_ascii.Quote:	if (escape_quote)	{bfr.Add(Html_consts.Quote); continue;} break;
				default: break;
			}
			bfr.Add_byte(b);
		}
	}
	public static byte[] Del_comments(ByteAryBfr bfr, byte[] src) {return Del_comments(bfr, src, 0, src.length);}
	public static byte[] Del_comments(ByteAryBfr bfr, byte[] src, int pos, int end) {
		while (true) {
			if (pos >= end) break;
			int comm_bgn = Byte_ary_finder.Find_fwd(src, Html_consts.Comm_bgn, pos);											// look for <!--
			if (comm_bgn == Byte_ary_finder.Not_found) {																			// not found; consume rest
				bfr.Add_mid(src, pos, end);
				break;
			}
			int comm_end = Byte_ary_finder.Find_fwd(src, Html_consts.Comm_end, comm_bgn + Html_consts.Comm_bgn_len);		// look for -->
			if (comm_end == Byte_ary_finder.Not_found) {																			// not found; consume rest
				bfr.Add_mid(src, pos, end);
				break;
			}
			bfr.Add_mid(src, pos, comm_bgn);																						// add everything between pos and comm_bgn
			pos = comm_end + Html_consts.Comm_end_len;																			// reposition pos after comm_end
		}
		return bfr.XtoAryAndClear();
	}
}
