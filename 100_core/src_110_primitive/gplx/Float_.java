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
public class Float_ {
	public static final float NaN = Float.NaN;;					
	public static boolean IsNaN(float v) {return Float.isNaN(v);}		
	public static int RoundUp(float val) {
		int rv = (int)val;
		return (rv == val) ? rv : rv + 1;
	}
	public static float Div(int val, int divisor) {return (float)val / (float)divisor;}
	public static float Div(long val, long divisor) {return (float)val / (float)divisor;}
	public static String XtoStr(float v) {
				int v_int = (int)v;
		return v - v_int == 0 ? Int_.XtoStr(v_int) : Float.toString(v);
			}
	public static float cast_double_(double v) {return (float)v;}
	public static float cast_(Object obj) {try {return (Float)obj;} catch(Exception exc) {throw Err_.type_mismatch_exc_(exc, float.class, obj);}}
	public static float read_(Object o) {String s = String_.as_(o); return s != null ? Float_.parse_(s) : Float_.cast_(o);}
	public static float parse_(String raw) {try {return Float.parseFloat(raw);} catch(Exception exc) {throw Err_.parse_type_exc_(exc, float.class, raw);}} 
	public static float parseOr_(String raw, float v) {
		if (raw == null || raw.length() == 0) return v;										
		try {return Float.parseFloat(raw);} catch(Exception e) {Err_.Noop(e); return v;}	
	}
}
