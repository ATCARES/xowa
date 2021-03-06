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
package gplx.xowa; import gplx.*;
public class Xol_msg_mgr implements GfoInvkAble {
	public Xol_msg_mgr(GfoInvkAble owner, boolean owner_is_lang) {
		this.owner = owner; this.owner_is_lang = owner_is_lang;
		this.Clear();
	}	GfoInvkAble owner; boolean owner_is_lang;
	public void Clear() {
		if (owner_is_lang)
			itms = Ary_new();
		else
			itms = new Xol_msg_itm[Xol_msg_itm_.Id__max];
		hash = Hash_new(itms);
		itms_max = itms_id_next = Xol_msg_itm_.Id__max;
	}
	public int Itms_max() {return itms_max;} private Xol_msg_itm[] itms; int itms_max = Xol_msg_itm_.Id__max; int itms_id_next = Xol_msg_itm_.Id__max;
	public Xol_msg_itm Itm_by_id_or_null(int id) {return id < itms_max ? itms[id] : null;}
	public Xol_msg_itm Itm_by_key_or_null(byte[] key) {return (Xol_msg_itm)hash.Fetch(key);}
	public Xol_msg_itm Itms_new(byte[] msg_key) {
		Xol_msg_itm rv = new Xol_msg_itm(itms_id_next++, msg_key);
		Itms_reg(rv);
		return rv;
	}
	public Xol_msg_itm Itm_by_key_or_new(String key, String val) {return Itm_by_key_or_new(key, val, false);}
	public Xol_msg_itm Itm_by_key_or_new(String key, String val, boolean has_fmt_arg) {	// TEST:
		Xol_msg_itm rv = Itm_by_key_or_new(ByteAry_.new_utf8_(key));
		rv.Atrs_set(ByteAry_.new_utf8_(val), has_fmt_arg, false);
		return rv;
	}
	public Xol_msg_itm Itm_by_key_or_new(byte[] key) {
		Object o = hash.Fetch(key);
		Xol_msg_itm rv = null;
		if (o == null) { // key not found; likely not a system_id; generate a custom one
			rv = new Xol_msg_itm(itms_id_next++, key);
			Itms_reg(rv);
		}
		else {
			rv = (Xol_msg_itm)o;
		}
		return rv;
	}	HashAdp hash;
	public byte[] Val_by_id(int id) {	// NOTE: Val_by_id needs to exist on lang (not wiki_msg_mgr); {{#time}} can pass in lang, and will need to call lang's msg_mgr directly
		Xol_msg_itm itm = Itm_by_id_or_null(id);
		return itm == null ? null : itm.Val();
	}
	public byte[] Val_by_id(Xow_wiki wiki, int id) {	// NOTE: Val_by_id needs to exist on lang (not wiki_msg_mgr); {{#time}} can pass in lang, and will need to call lang's msg_mgr directly
		Xol_msg_itm itm = Itm_by_id_or_null(id);
		if (itm == null) return null;
		byte[] rv = itm.Val();
		if (itm.Has_tmpl_txt()) rv = Xop_parser_.Parse_fragment(wiki, rv);
		return rv;
	}
	public byte[] Val_by_id_args(int id, ByteAryBfr bfr, Object... args) {
		Xol_msg_itm itm = Itm_by_id_or_null(id); if (itm == null) return null;
		return itm.Fmt(bfr, args);
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_lang))					return owner;
		else if	(ctx.Match(k, Invk_load_text))				Xol_lang_srl.Load_messages(this, m.ReadBry("v"));
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}	private static final String Invk_lang = Xol_lang_srl.Invk_lang, Invk_load_text = Xol_lang_srl.Invk_load_text;
	private void Itms_reg(Xol_msg_itm itm) {
		int id = itm.Id();
		if (id >= itms_max) {
			int new_max = (id + 1) * 2;  
			itms = (Xol_msg_itm[])Array_.Expand(itms, new Xol_msg_itm[new_max], itms_max);
			itms_max = new_max;
		}
		itms[id] = itm;
		hash.Add(itm.Key(), itm);
	}
	private static Xol_msg_itm[] Ary_new() {
		Xol_msg_itm[] rv = new Xol_msg_itm[Xol_msg_itm_.Id__max];
		for (int i = 0; i < Xol_msg_itm_.Id__max; i++)
			rv[i] = Xol_msg_itm_.new_(i);
		return rv;
	}
	private static HashAdp Hash_new(Xol_msg_itm[] ary) {
		HashAdp rv = Hash_adp_bry.ci_();
		for (int i = 0; i < Xol_msg_itm_.Id__max; i++) {
			Xol_msg_itm itm = ary[i]; if (itm == null) continue;	// NOTE: can be null when msg_mgr is owned by wiki
			rv.Add(itm.Key(), itm);
		}
		return rv;
	}
	static final String GRP_KEY = "xowa.lang.msg_mgr";
}
