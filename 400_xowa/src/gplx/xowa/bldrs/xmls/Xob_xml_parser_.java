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
package gplx.xowa.bldrs.xmls; import gplx.*; import gplx.xowa.*; import gplx.xowa.bldrs.*;
public class Xob_xml_parser_ {
	public static ByteTrieMgr_fast trie_() {
		ByteTrieMgr_fast rv = ByteTrieMgr_fast.cs_();
		trie_add(rv, Bry_page_bgn, Id_page_bgn); trie_add(rv, Bry_page_bgn_frag, Id_page_bgn_frag); trie_add(rv, Bry_page_end, Id_page_end);
		trie_add(rv, Bry_id_bgn, Id_id_bgn); trie_add(rv, Bry_id_bgn_frag, Id_id_bgn_frag); trie_add(rv, Bry_id_end, Id_id_end);
		trie_add(rv, Bry_title_bgn, Id_title_bgn); trie_add(rv, Bry_title_bgn_frag, Id_title_bgn_frag); trie_add(rv, Bry_title_end, Id_title_end);
		trie_add(rv, Bry_timestamp_bgn, Id_timestamp_bgn); trie_add(rv, Bry_timestamp_bgn_frag, Id_timestamp_bgn_frag); trie_add(rv, Bry_timestamp_end, Id_timestamp_end);
		trie_add(rv, Bry_text_bgn, Id_text_bgn); trie_add(rv, Bry_text_bgn_frag, Id_text_bgn_frag); trie_add(rv, Bry_text_end, Id_text_end);
		trie_add(rv, Bry_amp, Id_amp, Byte_ascii.Amp); trie_add(rv, Bry_quot, Id_quot, Byte_ascii.Quote); trie_add(rv, Bry_gt, Id_gt, Byte_ascii.Gt); trie_add(rv, Bry_lt, Id_lt, Byte_ascii.Lt);
		trie_add(rv, Bry_tab, Id_tab, Bry_tab_ent); trie_add(rv, Bry_cr_nl, Id_cr_nl, Byte_ascii.NewLine); trie_add(rv, Bry_cr, Id_cr, Byte_ascii.NewLine);
		return rv;
	}
	public static final byte[]
		  Bry_page_bgn = ByteAry_.new_ascii_("<page>"), Bry_page_bgn_frag = ByteAry_.new_ascii_("<page"), Bry_page_end = ByteAry_.new_ascii_("</page>")
		, Bry_title_bgn = ByteAry_.new_ascii_("<title>"), Bry_title_bgn_frag = ByteAry_.new_ascii_("<title"), Bry_title_end = ByteAry_.new_ascii_("</title>")
		, Bry_id_bgn = ByteAry_.new_ascii_("<id>"), Bry_id_bgn_frag = ByteAry_.new_ascii_("<id"), Bry_id_end = ByteAry_.new_ascii_("</id>")
		, Bry_redirect_bgn = ByteAry_.new_ascii_("<redirect>"), Bry_redirect_bgn_frag = ByteAry_.new_ascii_("<redirect"), Bry_redirect_end = ByteAry_.new_ascii_("</redirect>")
		, Bry_revision_bgn = ByteAry_.new_ascii_("<revision>"), Bry_revision_bgn_frag = ByteAry_.new_ascii_("<revision"), Bry_revision_end = ByteAry_.new_ascii_("</revision>")
		, Bry_timestamp_bgn = ByteAry_.new_ascii_("<timestamp>"), Bry_timestamp_bgn_frag = ByteAry_.new_ascii_("<timestamp"), Bry_timestamp_end = ByteAry_.new_ascii_("</timestamp>")
		, Bry_contributor_bgn = ByteAry_.new_ascii_("<contributor>"), Bry_contributor_bgn_frag = ByteAry_.new_ascii_("<contributor"), Bry_contributor_end = ByteAry_.new_ascii_("</contributor>")
		, Bry_username_bgn = ByteAry_.new_ascii_("<username>"), Bry_username_bgn_frag = ByteAry_.new_ascii_("<username"), Bry_username_end = ByteAry_.new_ascii_("</username>")
		, Bry_minor_bgn = ByteAry_.new_ascii_("<minor>"), Bry_minor_bgn_frag = ByteAry_.new_ascii_("<minor"), Bry_minor_end = ByteAry_.new_ascii_("</minor>")
		, Bry_comment_bgn = ByteAry_.new_ascii_("<comment>"), Bry_comment_bgn_frag = ByteAry_.new_ascii_("<comment"), Bry_comment_end = ByteAry_.new_ascii_("</comment>")
		, Bry_text_bgn = ByteAry_.new_ascii_("<text>"), Bry_text_bgn_frag = ByteAry_.new_ascii_("<text"), Bry_text_end = ByteAry_.new_ascii_("</text>")
		, Bry_amp = ByteAry_.new_ascii_("&amp;"), Bry_quot = ByteAry_.new_ascii_("&quot;") , Bry_gt = ByteAry_.new_ascii_("&gt;"), Bry_lt = ByteAry_.new_ascii_("&lt;")
		, Bry_tab_ent = ByteAry_.new_ascii_("&#09;"), Bry_tab = ByteAry_.new_ascii_("\t"), Bry_cr_nl = ByteAry_.new_ascii_("\r\n"), Bry_cr = ByteAry_.new_ascii_("\r")			
		;
	public static final byte
		  Id_page_bgn = 0, Id_page_bgn_frag = 1, Id_page_end = 2
		, Id_title_bgn = 3, Id_title_bgn_frag = 4, Id_title_end = 5
		, Id_id_bgn = 6, Id_id_bgn_frag = 7, Id_id_end = 8
		, Id_redirect_bgn = 9, Id_redirect_bgn_frag = 10, Id_redirect_end = 11
		, Id_revision_bgn = 12, Id_revision_bgn_frag = 13, Id_revision_end = 14
		, Id_timestamp_bgn = 15, Id_timestamp_bgn_frag = 16, Id_timestamp_end = 17
		, Id_contributor_bgn = 18, Id_contributor_bgn_frag = 19, Id_contributor_end = 20
		, Id_username_bgn = 21, Id_username_bgn_frag = 22, Id_username_end = 23
		, Id_minor_bgn = 24, Id_minor_bgn_frag = 25, Id_minor_end = 26
		, Id_comment_bgn = 27, Id_comment_bgn_frag = 28, Id_comment_end = 29
		, Id_text_bgn = 30, Id_text_bgn_frag = 31, Id_text_end = 32
		, Id_amp = 33, Id_quot = 34, Id_gt = 35, Id_lt = 36
		, Id_tab = 37, Id_cr_nl = 38, Id_cr = 39
		;
	private static void trie_add(ByteTrieMgr_fast rv, byte[] hook, byte id)						{rv.Add(hook, new Xob_xml_parser_itm(hook, id, Byte_.Zero	, ByteAry_.Empty));}
	private static void trie_add(ByteTrieMgr_fast rv, byte[] hook, byte id, byte subst_byte)	{rv.Add(hook, new Xob_xml_parser_itm(hook, id, subst_byte	, ByteAry_.Empty));}
	private static void trie_add(ByteTrieMgr_fast rv, byte[] hook, byte id, byte[] subst_ary)	{rv.Add(hook, new Xob_xml_parser_itm(hook, id, Byte_.Zero	, subst_ary));}
}
class Xob_xml_parser_itm {
	public Xob_xml_parser_itm(byte[] hook, byte tid, byte subst_byte, byte[] subst_ary) {this.hook = hook; this.hook_len = hook.length; this.tid = tid; this.subst_byte = subst_byte; this.subst_ary = subst_ary;}
	public byte Tid() {return tid;} private byte tid;
	public byte[] Hook() {return hook;} private byte[] hook;
	public int Hook_len() {return hook_len;} private int hook_len;
	public byte Subst_byte() {return subst_byte;} private byte subst_byte;
	public byte[] Subst_ary() {return subst_ary;} private byte[] subst_ary;
}
