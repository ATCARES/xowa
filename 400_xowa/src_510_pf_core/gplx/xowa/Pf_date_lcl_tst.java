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
import org.junit.*;
public class Pf_date_lcl_tst {
	Xop_fxt fxt = new Xop_fxt();
	@Before	public void setup()						{fxt.Reset(); Tfds.Now_set(DateAdp_.new_(2012, 1, 2, 3, 4, 5, 6));}
	@After public void teardown()				{Tfds.Now_enabled_n_();}
	@Test  public void Lcl_year()					{fxt.tst_Parse_tmpl_str_test("{{LOCALYEAR}}"			, "{{test}}", "2012");}
	@Test  public void Lcl_month_int()				{fxt.tst_Parse_tmpl_str_test("{{LOCALMONTH1}}"			, "{{test}}", "1");}
	@Test  public void Lcl_month_int_len2()			{fxt.tst_Parse_tmpl_str_test("{{LOCALMONTH}}"			, "{{test}}", "01");}
	@Test  public void Lcl_day_int()				{fxt.tst_Parse_tmpl_str_test("{{LOCALDAY}}"				, "{{test}}", "2");}
	@Test  public void Lcl_day_int_len2()			{fxt.tst_Parse_tmpl_str_test("{{LOCALDAY2}}"			, "{{test}}", "02");}
	@Test  public void Lcl_day_hour_len2()			{fxt.tst_Parse_tmpl_str_test("{{LOCALHOUR}}"			, "{{test}}", "03");}
	@Test  public void Lcl_dow_int()				{fxt.tst_Parse_tmpl_str_test("{{LOCALDOW}}"				, "{{test}}", "1");}
	@Test  public void Lcl_week_int()				{fxt.tst_Parse_tmpl_str_test("{{LOCALWEEK}}"			, "{{test}}", "1");}
	@Test  public void Lcl_month_name()				{fxt.tst_Parse_tmpl_str_test("{{LOCALMONTHNAME}}"		, "{{test}}", "January");}
	@Test  public void Lcl_month_gen()				{fxt.tst_Parse_tmpl_str_test("{{LOCALMONTHNAMEGEN}}"	, "{{test}}", "January");}
	@Test  public void Lcl_day_name()				{fxt.tst_Parse_tmpl_str_test("{{LOCALDAYNAME}}"			, "{{test}}", "Monday");}
	@Test  public void Lcl_time()					{fxt.tst_Parse_tmpl_str_test("{{LOCALTIME}}"			, "{{test}}", "03:04");}
	@Test  public void Lcl_timestamp()				{fxt.tst_Parse_tmpl_str_test("{{LOCALTIMESTAMP}}"		, "{{test}}", "20120102030405");}
}