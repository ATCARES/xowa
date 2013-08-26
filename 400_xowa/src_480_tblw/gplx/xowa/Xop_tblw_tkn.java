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
package gplx.xowa; import gplx.*;
public interface Xop_tblw_tkn extends Xop_tkn_itm {
	int Tag_id();
	int Atrs_bgn();
	int Atrs_end();
	void Atrs_rng_set(int bgn, int end);
	Xop_xatr_itm[] Atrs_ary(); Xop_tblw_tkn Atrs_ary_(Xop_xatr_itm[] v);
	boolean Tblw_xml();
}