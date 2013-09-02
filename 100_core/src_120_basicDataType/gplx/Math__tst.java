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
import org.junit.*;
public class Math__tst {
	@Test  public void Abs() {
		tst_Abs(1, 1);
		tst_Abs(-1, 1);
		tst_Abs(0, 0);
	}	void tst_Abs(int val, int expd) {Tfds.Eq(expd, Math_.Abs(val));}
	@Test  public void Log10() {
		tst_Log10(0, Int_.MinValue);
		tst_Log10(9, 0);
		tst_Log10(10, 1);
		tst_Log10(99, 1);
		tst_Log10(100, 2);
	}	void tst_Log10(int val, int expd) {Tfds.Eq(expd, Math_.Log10(val));}
	@Test  public void Min() {
		tst_Min(0, 1, 0);
		tst_Min(1, 0, 0);
		tst_Min(0, 0, 0);
	}	void tst_Min(int val0, int val1, int expd) {Tfds.Eq(expd, Math_.Min(val0, val1));}
	@Test  public void Pow() {
		tst_Pow(2, 0, 1);
		tst_Pow(2, 1, 2);
		tst_Pow(2, 2, 4);
	}	void tst_Pow(int val, int exponent, double expd) {Tfds.Eq(expd, Math_.Pow(val, exponent));}
	@Test  public void Mult() {
		tst_Mult(100, .01f, 1);
	}	void tst_Mult(int val, float multiplier, int expd) {Tfds.Eq(expd, Int_.Mult(val, multiplier));}		
	@Test  public void Base2Ary() {
		tst_Base2Ary(  1, 256, 1);
		tst_Base2Ary(  2, 256, 2);
		tst_Base2Ary(  3, 256, 1, 2);
		tst_Base2Ary(  4, 256, 4);
		tst_Base2Ary(  5, 256, 1, 4);
		tst_Base2Ary(  6, 256, 2, 4);
		tst_Base2Ary(511, 256, 1, 2, 4, 8, 16, 32, 64, 128, 256);
	}	void tst_Base2Ary(int v, int max, int... expd) {Tfds.Eq_ary(expd, Math_.Base2Ary(v, max));}
	@Test  public void Round() {
		tst_Round(1.5		, 0, 2);
		tst_Round(2.5		, 0, 3);
		tst_Round(2.123		, 2, 2.12);
		tst_Round(21.1		, -1, 20);
	}	void tst_Round(double v, int places, double expd) {Tfds.Eq(expd, Math_.Round(v, places));}
}
