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
package gplx.xowa.langs.vnts; import gplx.*; import gplx.xowa.*; import gplx.xowa.langs.*;
public class Xol_vnt_mgr implements GfoInvkAble {
	private OrderedHash vnts = OrderedHash_.new_bry_();
	public Xol_vnt_mgr(Xol_lang lang) {this.lang = lang;}
	public Xol_lang Lang() {return lang;} private Xol_lang lang;
	public boolean Enabled() {return enabled;} private boolean enabled = false;
	public Xol_vnt_itm Get_or_new(byte[] key) {
		Xol_vnt_itm rv = (Xol_vnt_itm)vnts.Fetch(key);
		if (rv == null) {			
			rv = new Xol_vnt_itm(this, key);
			vnts.Add(key, rv);
			enabled = true;	// mark enabled if any vnts have been added
		}
		return rv;
	}
	public void Convert_ttl_init() {
		int vnts_len = vnts.Count();
		tmp_converter_ary_len = vnts_len;
		tmp_converter_ary = new Xol_vnt_converter[vnts_len];
		for (int i = 0; i < vnts_len; i++) {
			Xol_vnt_itm itm = (Xol_vnt_itm)vnts.FetchAt(i);
			tmp_converter_ary[i] = itm.Converter();
		}
	}
	private Xol_vnt_converter[] tmp_converter_ary; int tmp_converter_ary_len; private OrderedHash tmp_page_list = OrderedHash_.new_bry_();
	public Xodb_page Convert_ttl(Xow_wiki wiki, ByteAryBfr tmp_bfr, Xow_ns ns, byte[] ttl_bry) {	// REF.MW:LanguageConverter.php|findVariantLink
		int converted = Convert_ttl_convert(tmp_bfr, ns, ttl_bry);			// convert ttl for each vnt
		if (converted == 0) return Xodb_page.Null;							// ttl_bry has no conversions; exit;
		wiki.Db_mgr().Load_mgr().Load_by_ttls(Cancelable_.Never, tmp_page_list, true, 0, converted);
		for (int i = 0; i < converted; i++) {
			Xodb_page page = (Xodb_page)tmp_page_list.FetchAt(i);
			if (page.Exists()) return page;									// return 1st found page
		}
		return Xodb_page.Null;
	}
	private int Convert_ttl_convert(ByteAryBfr tmp_bfr, Xow_ns ns, byte[] ttl_bry) {
		tmp_page_list.Clear();
		int rv = 0;
		for (int i = 0; i < tmp_converter_ary_len; i++) {				// convert ttl for each variant
			Xol_vnt_converter converter = tmp_converter_ary[i];
			tmp_bfr.Clear();
			if (!converter.Convert_text(tmp_bfr, ttl_bry)) continue;	// ttl is not converted for variant; ignore
			Xodb_page page = new Xodb_page();
			page.Ttl_(ns, tmp_bfr.XtoAryAndClear());
			byte[] converted_ttl = page.Ttl_w_ns();
			if (tmp_page_list.Has(converted_ttl)) continue;
			tmp_page_list.Add(converted_ttl, page);
			++rv;
		}
		return rv;
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_get))					return Get_or_new(m.ReadBry("v"));
		else if	(ctx.Match(k, Invk_init_end))				Convert_ttl_init();
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}	private static final String Invk_get = "get", Invk_init_end = "init_end";
}
