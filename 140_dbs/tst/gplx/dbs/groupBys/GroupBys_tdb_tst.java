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
package gplx.dbs.groupBys; import gplx.*; import gplx.dbs.*;
import org.junit.*;
public class GroupBys_tdb_tst extends GroupBys_base_tst {
	@Override protected Db_provider provider_() {return Db_provider_fxt.Tdb("130_dbs_group_bys.dsv");}
	@Test  public void GroupBy_1fld() {super.GroupBy_1fld_hook();}
	@Test  public void GroupBy_2fld() {super.GroupBy_2fld_hook();}
	@Test  public void Min() {super.MinMax_hook(true);}
	@Test  public void Max() {super.MinMax_hook(false);}
	@Test  public void Count() {super.Count_hook();}
	@Test  public void Sum() {super.Sum_hook();}
	// Avg, CountDistinct
}