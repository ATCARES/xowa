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
package gplx.xowa.users.history; import gplx.*; import gplx.xowa.*; import gplx.xowa.users.*;
import org.junit.*;
public class Xou_history_mgr_tst {
	Xou_history_mgr_fxt fxt = new Xou_history_mgr_fxt();
	@Before public void init() {fxt.Clear();}
	@Test  public void Archive() {
		Tfds.Now_enabled_y_();
		fxt.Invk(Xou_history_mgr.Invk_current_itms_max_, 4).Invk(Xou_history_mgr.Invk_current_itms_reset_, 2);
		fxt.Add_many("A", "B", "C", "D", "E").Save();
		fxt.List_tst("E", "D");
		fxt.Fil_tst("mem/xowa/user/test_user/app/data/history/20010101_000500.000.csv", String_.Concat_lines_nl
			(	"20010101 000200.000|20010101 000200.000|1|en.wikipedia.org|C"
			,	"20010101 000100.000|20010101 000100.000|1|en.wikipedia.org|B"
			,	"20010101 000000.000|20010101 000000.000|1|en.wikipedia.org|A"
			));
	}
	@Test   public void Normalize() {
		fxt.Clear();
		fxt.Add_many("Category:A_B", "Category:A B", "Category:a B", "Category:_A B_");
		fxt.List_tst("Category:A B");
	}
}
class Xou_history_mgr_fxt {
	Xoa_app app; Xow_wiki wiki;
	Xou_history_mgr under;
	public void Clear() {
		if (app == null) {
			app = Xoa_app_fxt.app_();
			wiki = Xoa_app_fxt.wiki_tst_(app);
			under = app.User().History_mgr();
		}
		Io_mgr._.DeleteDirDeep(Io_url_.new_dir_("mem/xowa/user/test_user/app/data/history/"));
		under.Clear();
	}
	public Xou_history_mgr_fxt Add_many(String... ary) {
		int ary_len = ary.length;
		for (int i = 0; i < ary_len; i++) {
			String itm = ary[i];
			byte[] itm_bry = ByteAry_.new_utf8_(itm);
			Xoa_ttl ttl = Xoa_ttl.parse_(wiki, itm_bry);
			Xoa_page page = new Xoa_page(wiki, ttl);
			page.Url_(Xoa_url.new_(wiki.Key_bry(), itm_bry));  // set url b/c history_mgr.Add uses url
			under.Add(page);
		}
		return this;
	}
	public Xou_history_mgr_fxt List_tst(String... expd) {
		int actl_len = under.Count();
		String[] actl = new String[actl_len];
		for (int i = 0; i < actl_len; i++) {
			Xou_history_itm itm = under.Get_at(i);
			actl[i] = String_.new_utf8_(itm.Page());
		}
		Tfds.Eq_ary_str(expd, actl);
		return this;
	}
	public Xou_history_mgr_fxt Invk(String key, Object v) {GfoInvkAble_.InvkCmd_val(under, key, v); return this;}
	public Xou_history_mgr_fxt Save() {under.Save(app); return this;}
	public Xou_history_mgr_fxt Fil_tst(String expd_url, String expd) {
		String actl = Io_mgr._.LoadFilStr(expd_url);
		Tfds.Eq_str_lines(expd, actl);
		return this;
	}
}
