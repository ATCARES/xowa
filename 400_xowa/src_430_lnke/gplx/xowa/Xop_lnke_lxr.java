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
import gplx.xowa.net.*;
class Xop_lnke_lxr implements Xop_lxr {
	Xop_lnke_lxr(byte lnke_typ, byte[] protocol, byte tid) {this.lnke_typ = lnke_typ; this.protocol = protocol; this.tid = tid;} private byte lnke_typ; byte[] protocol; byte tid;
	public byte Lxr_tid() {return Xop_lxr_.Tid_lnke_bgn;}
	public void Init_by_wiki(Xow_wiki wiki, ByteTrieMgr_fast core_trie) {
		String[] ary = Xoo_protocol_itm.Protocol_str_ary();
		int ary_len = ary.length;
		for (int i = 0; i < ary_len; i++)
			Ctor_lxr_add(core_trie, ary[i], Byte_.int_(i));	// NOTE: i = tid; this requires that protocol_ary is in same order as Tid;
		core_trie.Add(Bry_relative_1, new Xop_lnke_lxr(Xop_lnke_tkn.Lnke_typ_brack, Xoa_consts.Url_relative_prefix, Xoo_protocol_itm.Tid_relative_1));
		core_trie.Add(Bry_relative_2, new Xop_lnke_lxr(Xop_lnke_tkn.Lnke_typ_brack, Xoa_consts.Url_relative_prefix, Xoo_protocol_itm.Tid_relative_2));
		Ctor_lxr_add(core_trie, "xowa-cmd", Xoo_protocol_itm.Tid_xowa);
	}	private static final byte[] Bry_relative_1 = ByteAry_.new_ascii_("[//"), Bry_relative_2 = ByteAry_.new_ascii_("[[//");
	public void Init_by_lang(Xol_lang lang, ByteTrieMgr_fast core_trie) {}
	private void Ctor_lxr_add(ByteTrieMgr_fast core_trie, String itm, byte tid) {
		byte[] protocol_ary = ByteAry_.new_ascii_(itm);
		core_trie.Add(protocol_ary						, new Xop_lnke_lxr(Xop_lnke_tkn.Lnke_typ_text, protocol_ary, tid));
		core_trie.Add(ByteAry_.new_ascii_("[" + itm)	, new Xop_lnke_lxr(Xop_lnke_tkn.Lnke_typ_brack, protocol_ary, tid));
	}
	public int Make_tkn(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int src_len, int bgn_pos, int cur_pos) {
		if (this.tid == Xoo_protocol_itm.Tid_xowa && !ctx.Wiki().Sys_cfg().Xowa_proto_enabled()) return ctx.LxrMake_txt_(cur_pos);
		return ctx.Lnke().MakeTkn_bgn(ctx, tkn_mkr, root, src, src_len, bgn_pos, cur_pos, protocol, tid, lnke_typ);
	}
	public static final Xop_lnke_lxr _ = new Xop_lnke_lxr(); Xop_lnke_lxr() {}
}
