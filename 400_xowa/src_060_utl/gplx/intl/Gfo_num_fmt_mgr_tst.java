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
package gplx.intl; import gplx.*;
import org.junit.*;
public class Gfo_num_fmt_mgr_tst {
	Gfo_num_fmt_mgr mgr = new Gfo_num_fmt_mgr();
	@Before public void init() {mgr.Clear();}
	@Test  public void Outliers() {
		ini_(".", dat_(",", 3));
		tst_Fmt("1234a1234"							, "1,234a1,234");
		tst_Fmt("1234abc1234"						, "1,234abc1,234");
		tst_Fmt("1234,1234"							, "1,234,1,234");
		tst_Fmt("1234.1234"							, "1,234.1234");
		tst_Fmt("1234."								, "1,234.");
		tst_Fmt("1234.1234.1234.1234"				, "1,234.1234.1234.1234");
		tst_Fmt("-1234567"							, "-1,234,567");
		tst_Fmt("1,234,567"							, "1,234,567");
	}
	@Test  public void English() {
		ini_(".", dat_(",", 3));
		tst_Fmt("123"								, "123");
		tst_Fmt("1234"								, "1,234");
		tst_Fmt("12345678"							, "12,345,678");
		tst_Fmt("12345678901234567890"				, "12,345,678,901,234,567,890");
		tst_Raw("1,234.12"							, "1234.12");
	}
	@Test  public void French() {
		ini_(",", dat_(" ", 3));
		tst_Fmt("123"								, "123");
		tst_Fmt("1234"								, "1 234");
		tst_Fmt("12345678"							, "12 345 678");
		tst_Fmt("12345678901234567890"				, "12 345 678 901 234 567 890");
		tst_Fmt("1234,5678"							, "1 234,5678");
	}
	@Test  public void Croatia() {
		ini_(",", dat_(".", 3), dat_(",", 3));
		tst_Fmt("123"								, "123");
		tst_Fmt("1234"								, "1.234");
		tst_Fmt("12345678"							, "12,345.678");
		tst_Fmt("12345678901234567890"				, "12,345.678,901.234,567.890");
	}
	@Test  public void Mexico() {
		ini_(".", dat_(",", 3, false), dat_("'", 3, false), dat_(",", 3));
		tst_Fmt("123"								, "123");
		tst_Fmt("1234"								, "1,234");
		tst_Fmt("12345678"							, "12'345,678");
		tst_Fmt("12345678901234567890"				, "12,345,678,901,234'567,890");
		tst_Raw("12'345,678.90"						, "12345678.90");
	}
	@Test  public void China() {
		ini_(".", dat_(",", 4));
		tst_Fmt("123"								, "123");
		tst_Fmt("1234"								, "1234");
		tst_Fmt("12345678"							, "1234,5678");
		tst_Fmt("12345678901234567890"				, "1234,5678,9012,3456,7890");
	}
	@Test  public void Hindi() {
		ini_(".", dat_(",", 3, false), dat_(",", 2));
		tst_Fmt("123"								, "123");
		tst_Fmt("1234"								, "1,234");
		tst_Fmt("12345678"							, "1,23,45,678");
		tst_Fmt("12345678901234567890"				, "1,23,45,67,89,01,23,45,67,890");
	}
	@Test  public void India() {
		ini_(".", dat_(",", 3), dat_(",", 2), dat_(",", 2));
		tst_Fmt("123"								, "123");
		tst_Fmt("1234"								, "1,234");
		tst_Fmt("12345678"							, "1,23,45,678");
		tst_Fmt("12345678901234567890"				, "1,23,456,78,90,123,45,67,890");
	}
	@Test  public void MiddleDot() {
		ini_("·", dat_("·", 3));
		tst_Fmt("123"								, "123");
		tst_Fmt("1234"								, "1·234");
		tst_Fmt("12345678"							, "12·345·678");
		tst_Fmt("12345678901234567890"				, "12·345·678·901·234·567·890");
		tst_Fmt("1234·5678"							, "1·234·5678");
		tst_Raw("1234·5678"							, "1234.5678");
	}
	Gfo_num_fmt_grp dat_(String dlm, int digits)				{return new Gfo_num_fmt_grp(ByteAry_.new_utf8_(dlm), digits, true);}
	Gfo_num_fmt_grp dat_(String dlm, int digits, boolean repeat)	{return new Gfo_num_fmt_grp(ByteAry_.new_utf8_(dlm), digits, repeat);}
	void tst_Fmt(String val, String expd) {Tfds.Eq(expd, String_.new_utf8_(mgr.Fmt(ByteAry_.new_utf8_(val))));}
	void tst_Raw(String val, String expd) {Tfds.Eq(expd, String_.new_utf8_(mgr.Raw(ByteAry_.new_utf8_(val))));}
	void ini_(String dec_dlm, Gfo_num_fmt_grp... ary) {
		mgr.Dec_dlm_(ByteAry_.new_utf8_(dec_dlm));
		int ary_len = ary.length;
		for (int i = 0; i < ary_len; i++)
			mgr.Grps_add(ary[i]);
	}
}
/*
'france'  			' 3#' ',0%' 						// 1 234 567,89
'spain'				'.3#' "'0%" 						// 1.234.567'89
'germany'			'.3#' ",0%" 						// 1.234.567,89
'italy'				''3#' ",0%" 						// 1'234'567,89
'en-us' 			',3#' '.0%'							// 1,234,567.89
'en-sa' 			',3#' '\u00120%'					// 1,234,567·89
'croatia' 			',3#*' '.3#*' ',0%'					// 1,234.567,890.123,45
'china'				',4$'								// 123,4567.89
'mexico'			',3#*' "'3#" ',3#'					// 1'234,567.89
'hindi'				",2#*" ',3#'						// 1,23,45,678.9
'india'				',2#*' ',2#*' ',3#*'				// 1,245,67,89,012
*/