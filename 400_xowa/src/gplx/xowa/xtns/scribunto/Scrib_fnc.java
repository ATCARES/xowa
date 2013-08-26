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
public class Scrib_fnc {
	public Scrib_fnc(String key, int id) {this.key = key; this.id = id;}
	public String Key() {return key;} private String key;
	public int Id() {return id;} private int id;
	@Override public String toString() {return key + ":" + id;}
	public static Scrib_fnc cast_or_null_(Object o) {	// NOTE: maxStringLength and maxPatternLength return d:INF; ignore these
		return ClassAdp_.ClassOf_obj(o) == Scrib_fnc.class ? (Scrib_fnc)o : null;
	}
}
