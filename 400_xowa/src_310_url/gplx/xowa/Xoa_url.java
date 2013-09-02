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
public class Xoa_url {
	public Gfo_url_arg[] Args() {return args;} public Xoa_url Args_(Gfo_url_arg[] v) {args = v; return this;} Gfo_url_arg[] args = Gfo_url_arg.Ary_empty;
	public byte[] Raw() {return raw;} public Xoa_url Raw_(byte[] v) {raw = v; return this;} private byte[] raw = ByteAry_.Empty;
	public boolean Protocol_is_relative() {return protocol_is_relative;} public Xoa_url Protocol_is_relative_(boolean v) {protocol_is_relative = v; return this;} private boolean protocol_is_relative;
	public byte Protocol_tid() {return protocol_tid;} public Xoa_url Protocol_tid_(byte v) {protocol_tid = v; return this;} private byte protocol_tid;
	public byte[] Protocol_bry() {return protocol_bry;} public Xoa_url Protocol_bry_(byte[] v) {protocol_bry = v; return this;} private byte[] protocol_bry;
	public byte[] Lang_bry() {return lang_bry;} public Xoa_url Lang_bry_(byte[] v) {lang_bry = v; return this;} private byte[] lang_bry;
	public byte[] Wiki_bry() {return wiki_bry;} public Xoa_url Wiki_bry_(byte[] v) {wiki_bry = v; return this;} private byte[] wiki_bry;
	public Xow_wiki Wiki() {return wiki;} public Xoa_url Wiki_(Xow_wiki v) {wiki = v; return this;} private Xow_wiki wiki;
	public byte[] Page_bry() {return page_bry;} public Xoa_url Page_bry_(byte[] v) {page_bry = v; return this;} private byte[] page_bry;
	int Page_bgn(int raw_len) {
		int wiki_pos = ByteAry_.FindFwd(raw, Xoh_href_parser.Href_wiki_bry, 0, raw_len);	 // look for /wiki/
		return wiki_pos == ByteAry_.NotFound ? ByteAry_.NotFound : wiki_pos + Xoh_href_parser.Href_wiki_bry.length;
	}
	public byte[] Page_full() {
		int raw_len = raw.length;
		int page_bgn = Page_bgn(raw_len);
		if (page_bgn == ByteAry_.NotFound)	// no /wiki/ found; return page
			return page_bry == null ? ByteAry_.Empty : page_bry;	// guard against null ref
		else
			return ByteAry_.Mid(raw, page_bgn, raw_len);
	}
	public byte[] Anchor_bry() {return anchor_bry;} public Xoa_url Anchor_bry_(byte[] v) {anchor_bry = v; return this;} private byte[] anchor_bry;
	public byte[] Use_lang() {return use_lang;} public Xoa_url Use_lang_(byte[] v) {use_lang = v; return this;} private byte[] use_lang;
	public boolean Redirect_force() {return redirect_force;} public Xoa_url Redirect_force_(boolean v) {redirect_force = v; return this;} private boolean redirect_force;
	public boolean Search_fulltext() {return search_fulltext;} public Xoa_url Search_fulltext_(boolean v) {search_fulltext = v; return this;} private boolean search_fulltext;
	public boolean Action_is_edit() {return action_is_edit;} public Xoa_url Action_is_edit_(boolean v) {action_is_edit = v; return this;} private boolean action_is_edit;
	public byte Err() {return err;} public Xoa_url Err_(byte v) {err = v; return this;} private byte err;
	public byte[][] Segs_ary() {return segs_ary;} public Xoa_url Segs_ary_(byte[][] v) {segs_ary = v; return this;} private byte[][] segs_ary;
	public boolean Eq_page(Xoa_url comp) {return ByteAry_.Eq(wiki_bry, comp.wiki_bry) && ByteAry_.Eq(page_bry, comp.page_bry) && redirect_force == comp.Redirect_force();}
	public void Init(byte[] raw) {
		this.raw = raw;
		segs_ary = null;
		lang_bry = wiki_bry = page_bry = anchor_bry = use_lang = null;
		err = 0;
		protocol_is_relative = false;
		redirect_force = false;
		action_is_edit = false;
	}
	public void Args_fill(OrderedHash trg_args) {
		int trg_len = trg_args.Count();
		for (int i = 0; i < trg_len; i++) {
			Gfo_url_arg trg_arg = (Gfo_url_arg)trg_args.FetchAt(i);
			trg_arg.Val_bry_(null);
		}
		int src_len = args.length;
		for (int i = 0; i < src_len; i++) {
			Gfo_url_arg src_arg = args[i];
			Gfo_url_arg trg_arg = (Gfo_url_arg)trg_args.Fetch(src_arg.Key_bry());
			if (trg_arg != null) trg_arg.Val_bry_(src_arg.Val_bry());
		}
	}
	public String Anchor_str() {return anchor_bry == null ? null : String_.new_utf8_(anchor_bry);}
	public static Xoa_url new_(byte[] wiki, byte[] page) {
		Xoa_url rv = new Xoa_url();
		rv.Wiki_bry_(wiki);
		rv.Page_bry_(page);
		return rv;
	}
	public String To_str() {
		ByteAryBfr bfr = ByteAryBfr.new_();
		bfr.Add_str("wiki=").Add(wiki_bry).Add_byte_nl();
		bfr.Add_str("page=").Add(page_bry).Add_byte_nl();
		bfr.Add_str("anchor=").Add(anchor_bry).Add_byte_nl();
		int args_len = args.length;
		for (int i = 0; i < args_len; i++) {
			Gfo_url_arg arg = args[i];
			bfr.Add_str("  [").Add_int_variable(i).Add_str("]: ").Add(arg.Key_bry()).Add_byte(Byte_ascii.Eq).Add(arg.Val_bry()).Add_byte_nl();
		}
		return bfr.XtoStrAndClear();
	}
}
