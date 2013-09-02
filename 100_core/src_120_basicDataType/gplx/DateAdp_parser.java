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
package gplx;
public class DateAdp_parser {
	public int[] Parse_iso8651_like(String raw_str) {Parse_iso8651_like(tmp_rv, raw_str); return tmp_rv;} int[] tmp_rv = new int[7];
	public void Parse_iso8651_like(int[] rv, String raw_str) {
		byte[] raw_bry = ByteAry_.new_utf8_(raw_str);
		Parse_iso8651_like(rv, raw_bry, 0, raw_bry.length);
	}
	public void Parse_iso8651_like(int[] rv, byte[] src, int bgn, int end) {
		/*	1981-04-05T14:30:30.000-05:00	ISO 8601: http://en.wikipedia.org/wiki/ISO_8601
			1981.04.05 14.30.30.000-05.00	ISO 8601 loose
			99991231_235959.999				gplx
			yyyy-MM-ddTHH:mm:ss.fffzzzz		Format String	*/
		int rv_len = rv.length;
		for (int i = 0; i < rv_len; i++)
			rv[i] = 0;	// .Clear
		int pos = bgn, rv_idx = 0, int_len = 0, max_len = max_lens[rv_idx];
		while (true) {
			int int_val = -1;
			byte b = pos < end ? src[pos] : Byte_ascii.Nil;
			switch (b) {
				case Byte_ascii.Num_0: case Byte_ascii.Num_1: case Byte_ascii.Num_2: case Byte_ascii.Num_3: case Byte_ascii.Num_4:
				case Byte_ascii.Num_5: case Byte_ascii.Num_6: case Byte_ascii.Num_7: case Byte_ascii.Num_8: case Byte_ascii.Num_9:
					int_val = b - Byte_ascii.Num_0;	// convert ascii to val; EX: 49 -> 1
					int_len = int_bldr.Add(int_val);
					break;
			}
			if (	(int_val == -1 && int_len > 0)	// char is not number && number exists (ignore consecutive delimiters: EX: "1981  01")
				||	int_len == max_len) {			// max_len reached; necessary for gplxFormat
				rv[rv_idx++] = int_bldr.BldAndClear();
				if (rv_idx == 7) break;				// past frac; break;
				int_len = 0;
				max_len = max_lens[rv_idx];
			}
			if (pos == end) break;
			++pos;
		}
	}		
	int[] max_lens = new int[] {4/*y*/,2/*M*/,2/*d*/,2/*H*/,2/*m*/,2/*s*/,3/*S*/,0};
	IntBldr int_bldr = IntBldr.new_(4);
	public static DateAdp_parser new_() {return new DateAdp_parser();} DateAdp_parser() {}
}
class IntBldr {
	public int Add(char c) {
		if (idx > digitsLen - 1) throw Err_.missing_idx_(idx, digitsLen);
		digits[idx++] = XbyChar(c);
		return idx;
	}
	public int Add(int i) {
		if (idx > digitsLen - 1) throw Err_.missing_idx_(idx, digitsLen);
		digits[idx++] = i;
		return idx;
	}
	public int Parse(String raw) {
		ParseStr(raw);
		try {return Bld();}
		catch (Exception exc) {throw Err_.parse_type_exc_(exc, int.class, raw);}
	}
	public boolean ParseTry(String raw) {
		ParseStr(raw);
		for (int i = 0; i < idx; i++)
			if (digits[i] < 0) return false;
		return true;
	}
	public int Bld() {
		int rv = 0, exponent = 1;
		for (int i = idx - 1; i > -1; i--) {
			int digit = digits[i];
			if (digit < 0) throw Err_.new_("invalid char").Add("char", (char)-digits[i]).Add("ascii", -digits[i]);
			rv += digit * exponent;
			exponent *= 10;
		}
		return sign * rv;
	}
	public int BldAndClear() {
		int rv = Bld();
		this.Clear();
		return rv;
	}
	public void Clear() {idx = 0; sign = 1;}
	void ParseStr(String raw) {
		this.Clear();
		int rawLength = String_.Len(raw);
		for (int i = 0; i < rawLength; i++) {
			char c = String_.CharAt(raw, i);
			if (i == 0 && c == '-')
				sign = -1;
			else
				Add(c);
		}
	}
	int XbyChar(char c) {
		if (c == '0')		return 0; else if (c == '1')	return 1; else if (c == '2')	return 2; else if (c == '3')	return 3; else if (c == '4')	return 4;
		else if (c == '5')	return 5; else if (c == '6')	return 6; else if (c == '7')	return 7; else if (c == '8')	return 8; else if (c == '9')	return 9;
		else				return -(int)c;		// error handling: don't throw error; store ascii value in digit and throw if needed
	}
	int[] digits; int idx, digitsLen; int sign = 1;
	public static IntBldr new_(int digitsMax) {
		IntBldr rv = new IntBldr();
		rv.digits = new int[digitsMax];
		rv.digitsLen = digitsMax;
		return rv;
	}
}
