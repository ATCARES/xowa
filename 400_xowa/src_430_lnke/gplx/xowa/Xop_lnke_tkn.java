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
public class Xop_lnke_tkn extends Xop_tkn_itm_base {//20111222
	public static final byte Lnke_typ_null = 0, Lnke_typ_brack = 1, Lnke_typ_text = 2, Lnke_typ_brack_dangling = 3;
	@Override public byte Tkn_tid() {return Xop_tkn_itm_.Tid_lnke;}
	public boolean Lnke_relative() {return lnke_relative;} public Xop_lnke_tkn Lnke_relative_(boolean v) {lnke_relative = v; return this;} private boolean lnke_relative;
	public byte Lnke_typ() {return lnke_typ;} public Xop_lnke_tkn Lnke_typ_(byte v) {lnke_typ = v; return this;} private byte lnke_typ = Lnke_typ_null;
	public byte[] Lnke_site() {return lnke_site;} public Xop_lnke_tkn Lnke_site_(byte[] v) {lnke_site = v; return this;} private byte[] lnke_site;
	public byte[] Lnke_xwiki_wiki() {return lnke_xwiki_wiki;} private byte[] lnke_xwiki_wiki;
	public byte[] Lnke_xwiki_page() {return lnke_xwiki_page;} private byte[] lnke_xwiki_page;
	public Gfo_url_arg[] Lnke_xwiki_qargs() {return lnke_xwiki_qargs;} Gfo_url_arg[] lnke_xwiki_qargs;
	public void Lnke_xwiki_(byte[] wiki, byte[] page, Gfo_url_arg[] args) {this.lnke_xwiki_wiki = wiki; this.lnke_xwiki_page = page; this.lnke_xwiki_qargs = args;}
	public int Lnke_bgn() {return lnke_bgn;} private int lnke_bgn;
	public int Lnke_end() {return lnke_end;} private int lnke_end;
	public Xop_lnke_tkn Lnke_rng_(int bgn, int end) {lnke_bgn = bgn; lnke_end = end; return this;}
	public byte[] Protocol() {return protocol;} private byte[] protocol;
	public byte Proto_tid() {return proto_tid;} private byte proto_tid;
	public Xop_lnke_tkn Subs_add_ary(Xop_tkn_itm... ary) {for (Xop_tkn_itm itm : ary) super.Subs_add(itm); return this;}

	public Xop_lnke_tkn(int bgn, int end, byte[] protocol, byte proto_tid, byte lnke_typ, int lnke_bgn, int lnke_end) {
		this.Tkn_ini_pos(false, bgn, end); this.protocol = protocol; this.proto_tid = proto_tid; this.lnke_typ = lnke_typ; this.lnke_bgn = lnke_bgn; this.lnke_end = lnke_end;
	}	Xop_lnke_tkn() {}
}
class Xop_lnke_end_lxr implements Xop_lxr {//20111222
	public byte Lxr_tid() {return Xop_lxr_.Tid_lnke_end;}
	public void Init_by_wiki(Xow_wiki wiki, ByteTrieMgr_fast core_trie) {core_trie.Add(Byte_ascii.Brack_end, this);}
	public void Init_by_lang(Xol_lang lang, ByteTrieMgr_fast core_trie) {}
	public int Make_tkn(Xop_ctx ctx, Xop_tkn_mkr tkn_mkr, Xop_root_tkn root, byte[] src, int src_len, int bgn_pos, int cur_pos) {return ctx.Lnke().MakeTkn_end(ctx, tkn_mkr, root, src, src_len, bgn_pos, cur_pos);}
	public static final Xop_lnke_end_lxr _ = new Xop_lnke_end_lxr(); Xop_lnke_end_lxr() {}
}
