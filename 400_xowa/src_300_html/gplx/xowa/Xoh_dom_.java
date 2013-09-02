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
import gplx.xowa.html.*;
class Xoh_dom_ {
	private static final byte[] Lt_bry = ByteAry_.new_ascii_("<"), Space_bry = ByteAry_.new_ascii_(" ");
	public static byte[] Query_val_by_where(Xoh_find rv, byte[] src, byte[] where_nde, byte[] where_key, byte[] where_val, byte[] query_key, int bgn) {
		int src_len = src.length;		
		where_nde = ByteAry_.Add(Lt_bry, where_nde, Space_bry);
		while (true) {
			boolean where_val_found = Select_tag(rv, src, where_nde, where_key, bgn, src_len);
			if (where_val_found) {
				int tag_bgn = rv.Tag_bgn();
				int tag_end = rv.Tag_end();
				boolean where_val_match = ByteAry_.Match(src, rv.Val_bgn(), rv.Val_end(), where_val);
				if (where_val_match) {
					boolean query_val_found = Find_atr_val_in_tag(rv, src, query_key, tag_bgn, tag_end);
					if (query_val_found) {
						return ByteAry_.Mid(src, rv.Val_bgn(), rv.Val_end());
					}
					else
						return null;
				}
				else
					bgn = tag_end + 1;
			}
			else
				break;
		}
		return null;
	}
	public static boolean Select_tag(Xoh_find rv, byte[] src, byte[] nde, byte[] key, int rng_bgn, int rng_end) {
		int tag_bgn = ByteAry_.FindFwd(src, nde, 		   rng_bgn, rng_end); 					if (tag_bgn == ByteAry_.NotFound) return false;
		int tag_end = ByteAry_.FindFwd(src, Byte_ascii.Gt, tag_bgn, rng_end); 					if (tag_end == ByteAry_.NotFound) return false;
		int key_bgn = ByteAry_.FindFwd(src, key, tag_bgn, tag_end);								if (key_bgn == ByteAry_.NotFound) return false;
		int key_end = key_bgn + key.length;
		int val_bgn = ByteAry_.FindFwd(src, Byte_ascii.Quote, key_end, tag_end);				if (val_bgn == ByteAry_.NotFound) return false;
		++val_bgn;
		int val_end = ByteAry_.FindFwd(src, Byte_ascii.Quote, val_bgn, tag_end);				if (val_end == ByteAry_.NotFound) return false;
		rv.Set_all(tag_bgn, tag_end, key_bgn, key_end, val_bgn, val_end);
		return true;
	}
	public static boolean Find_atr_val_in_tag(Xoh_find rv, byte[] src, byte[] key, int tag_bgn, int tag_end) {
		int key_bgn = ByteAry_.FindFwd(src, key, tag_bgn, tag_end);								if (key_bgn == ByteAry_.NotFound) return false;
		int key_end = key_bgn + key.length;
		int val_bgn = ByteAry_.FindFwd(src, Byte_ascii.Quote, key_end, tag_end);				if (val_bgn == ByteAry_.NotFound) return false;
		++val_bgn;
		int val_end = ByteAry_.FindFwd(src, Byte_ascii.Quote, val_bgn, tag_end);				if (val_end == ByteAry_.NotFound) return false;
		rv.Set_all(tag_bgn, tag_end, key_bgn, key_end, val_bgn, val_end);
		return true;
	}
	public static String Title_by_href(Url_encoder encoder, ByteAryBfr bfr, byte[] href_dec, byte[] html_src) {
		int slash_pos = ByteAry_.FindBwd(href_dec, Byte_ascii.Slash);
		encoder.Encode(bfr, href_dec, slash_pos + 1, href_dec.length);
		byte[] name_enc = bfr.XtoAryAndClear();
		bfr.Add_mid(href_dec, 0, slash_pos + Int_.Const_dlm_len);	// include trailing slash			
		bfr.Add(name_enc);
		byte[] href_enc = bfr.XtoAryAndClear();			
		byte[] xowa_title = Xoh_dom_.Query_val_by_where(dom_find, html_src, Xoh_html_tag.Nde_a_bry, Xoh_html_tag.Nde_href_bry, href_enc, Xoh_html_tag.Nde_xowa_title_bry, 0);
		return String_.new_utf8_(xowa_title);
	}	static final Xoh_find dom_find = new Xoh_find(); 
}
class Xoh_find {
	public int Tag_bgn() {return tag_bgn;} public Xoh_find Tag_bgn_(int v) {tag_bgn = v; return this;} private int tag_bgn;
	public int Tag_end() {return tag_end;} public Xoh_find Tag_end_(int v) {tag_end = v; return this;} private int tag_end;
	public int Key_bgn() {return key_bgn;} public Xoh_find Key_bgn_(int v) {key_bgn = v; return this;} private int key_bgn;
	public int Key_end() {return key_end;} public Xoh_find Key_end_(int v) {key_end = v; return this;} private int key_end;
	public int Val_bgn() {return val_bgn;} public Xoh_find Val_bgn_(int v) {val_bgn = v; return this;} private int val_bgn;
	public int Val_end() {return val_end;} public Xoh_find Val_end_(int v) {val_end = v; return this;} private int val_end;
	public void Set_all(int tag_bgn, int tag_end, int key_bgn, int key_end, int val_bgn, int val_end) {
		this.tag_bgn = tag_bgn; this.tag_end = tag_end; this.key_bgn = key_bgn; this.key_end = key_end; this.val_bgn = val_bgn; this.val_end = val_end;
	}
}
