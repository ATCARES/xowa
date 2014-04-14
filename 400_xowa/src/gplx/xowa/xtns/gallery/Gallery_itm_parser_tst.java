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
package gplx.xowa.xtns.gallery; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import org.junit.*;
public class Gallery_itm_parser_tst {
	@Before public void init() {fxt.Init();} private Gallery_itm_parser_fxt fxt = new Gallery_itm_parser_fxt();
	@Test   public void All()				{fxt.Test_parse("File:A.png|a|alt=b|link=c"		, fxt.Expd("File:A.png", "a" , "b" , "c"));}
	@Test   public void Ttl_only()			{fxt.Test_parse("File:A.png"					, fxt.Expd("File:A.png", null, null, null));}
	@Test   public void Ttl_url_encoded()	{fxt.Test_parse("File:A%28b%29.png"				, fxt.Expd("File:A(b).png"));}			// PURPOSE: handle url-encoded sequences; DATE:2014-01-01
	@Test   public void Caption_only()		{fxt.Test_parse("File:A.png|a"					, fxt.Expd("File:A.png", "a" , null, null));}
	@Test   public void Caption_many()		{fxt.Test_parse("File:A.png|a|b"				, fxt.Expd("File:A.png", "a|b"));}		// NOTE: pipe becomes part of caption (i.e.: doesn't separate into caption / alt)
	@Test   public void Alt_only()			{fxt.Test_parse("File:A.png|alt=a"				, fxt.Expd("File:A.png", null, "a" , null));}
	@Test   public void Link_only()			{fxt.Test_parse("File:A.png|link=a"				, fxt.Expd("File:A.png", null, null, "a"));}
	@Test   public void Caption_alt()		{fxt.Test_parse("File:A.png|a|alt=b"			, fxt.Expd("File:A.png", "a" , "b"));}
	@Test   public void Alt_caption()		{fxt.Test_parse("File:A.png|alt=a|b"			, fxt.Expd("File:A.png", "b" , "a"));}
	@Test   public void Alt_blank()			{fxt.Test_parse("File:A.png|alt=|b"				, fxt.Expd("File:A.png", "b" , ""));}
	@Test   public void Alt_invalid()		{fxt.Test_parse("File:A.png|alta=b"				, fxt.Expd("File:A.png", "alta=b"));}	// NOTE: invalid alt becomes caption
	@Test   public void Ws()				{fxt.Test_parse("File:A.png| alt = b | c"		, fxt.Expd("File:A.png", "c" , "b"));}
	@Test   public void Ws_caption_many()	{fxt.Test_parse("File:A.png| a | b | c "		, fxt.Expd("File:A.png", "a | b | c"));}
	@Test   public void Page_pdf()			{fxt.Test_parse("File:A.pdf|page=1 "			, fxt.Expd("File:A.pdf", null, null, null, 1));}	// pdf parses page=1
	@Test   public void Page_png()			{fxt.Test_parse("File:A.png|page=1 "			, fxt.Expd("File:A.png", "page=1", null, null));}	// non-pdf treats page=1 as caption
	@Test   public void Page_invalid()		{fxt.Test_parse("|page=1");}	// check that null title doesn't fail; DATE:2014-03-21
	@Test   public void Skip_blank()		{fxt.Test_parse("");}
	@Test   public void Skip_empty()		{fxt.Test_parse("|File:A.png");}
	@Test   public void Skip_ws()			{fxt.Test_parse(" |File:A.png");}
	@Test   public void Skip_anchor()		{fxt.Test_parse("#a");}	// PURPOSE: anchor-like ttl should not render; ar.d:جَبَّارَة; DATE:2014-03-18			
	@Test   public void Many() {
		fxt.Test_parse("File:A.png\nFile:B.png"					, fxt.Expd("File:A.png"), fxt.Expd("File:B.png"));
	}
	@Test   public void Many_nl() {
		fxt.Test_parse("File:A.png\n\n\nFile:B.png"				, fxt.Expd("File:A.png"), fxt.Expd("File:B.png"));
	}
	@Test   public void Many_nl_w_tab() {
		fxt.Test_parse("File:A.png\n \t \nFile:B.png"			, fxt.Expd("File:A.png"), fxt.Expd("File:B.png"));
	}
	@Test   public void Many_invalid() {
		fxt.Test_parse("File:A.png\n<invalid>\nFile:B.png"		, fxt.Expd("File:A.png"), fxt.Expd("File:B.png"));
	}
	@Test   public void Caption_complicated() {
		fxt.Test_parse("File:A.png|alt=a|b[[c|d]]e ", fxt.Expd("File:A.png", "b[[c|d]]e", "a"));
	}
	@Test   public void Alt_magic_word_has_arg() {	// PURPOSE: img_alt magic_word is of form "alt=$1"; make sure =$1 is stripped for purpose of parser; DATE:2013-09-12
		fxt.Init_kwd_set(Xol_kwd_grp_.Id_img_alt, "alt=$1");
		fxt.Test_parse("File:A.png|alt=a|b", fxt.Expd("File:A.png", "b", "a"));
	}
	@Test   public void Link_null() {	// PURPOSE: null link causes page to fail; EX: ru.w:Гянджа; <gallery>Datei:A.png|link= |</gallery>; DATE:2014-04-11
		fxt.Test_parse("File:A.png|link = |b", fxt.Expd("File:A.png", "b", null, null));
	}
	@Test   public void Caption_empty() {	// PURPOSE: check that empty ws doesn't break caption (based on Link_null); DATE:2014-04-11
		fxt.Test_parse("File:A.png|  |  | ", fxt.Expd("File:A.png", null, null, null));
	}
}
class Gallery_itm_parser_fxt {
	private Xoa_app app; private Xow_wiki wiki;
	private Gallery_itm_parser parser;
	public Gallery_itm_parser_fxt Init() {
		this.app = Xoa_app_fxt.app_();
		this.wiki = Xoa_app_fxt.wiki_tst_(app);
		parser = new Gallery_itm_parser();
		parser.Init_by_wiki(wiki);
		return this;
	}
	public String[] Expd(String ttl)													{return new String[] {ttl, null, null, null, null};}
	public String[] Expd(String ttl, String caption)									{return new String[] {ttl, caption, null, null, null};}
	public String[] Expd(String ttl, String caption, String alt)						{return new String[] {ttl, caption, alt, null, null};}
	public String[] Expd(String ttl, String caption, String alt, String link)			{return new String[] {ttl, caption, alt, link, null};}
	public String[] Expd(String ttl, String caption, String alt, String link, int page)	{return new String[] {ttl, caption, alt, link, Int_.XtoStr(page)};}
	public void Init_kwd_set(int kwd_id, String kwd_val) {
		wiki.Lang().Kwd_mgr().Get_or_new(kwd_id).Itms()[0].Bry_set(ByteAry_.new_ascii_(kwd_val));
		parser.Init_by_wiki(wiki);
	}
	public void Test_parse(String raw, String[]... expd) {
		ListAdp actl = ListAdp_.new_();
		byte[] src = ByteAry_.new_ascii_(raw);
		parser.Parse_all(actl, Gallery_mgr_base_.New_by_mode(Gallery_mgr_base_.Traditional_tid), new Gallery_xnde(), src, 0, src.length);
		Tfds.Eq_ary(Ary_flatten(expd), Ary_flatten(X_to_str_ary(src, actl)));
	}
	private String[] Ary_flatten(String[][] src_ary) {
		int trg_len = 0;
		int src_len = src_ary.length;
		for (int i = 0; i < src_len; i++) {
			String[] itm = src_ary[i];
			if (itm != null) trg_len += itm.length;
		}
		String[] trg_ary = new String[trg_len];
		trg_len = 0;
		for (int i = 0; i < src_len; i++) {
			String[] itm = src_ary[i];
			if (itm == null) continue;
			int itm_len = itm.length;
			for (int j = 0; j < itm_len; j++)
				trg_ary[trg_len++] = itm[j];
		}
		return trg_ary;
	}
	private String[][] X_to_str_ary(byte[] src, ListAdp list) {
		int len = list.Count();
		String[][] rv = new String[len][];
		for (int i = 0; i < len; i++) {
			Gallery_itm itm = (Gallery_itm)list.FetchAt(i);
			String[] ary = new String[5];
			rv[i] = ary;
			ary[0] = String_.new_utf8_(itm.Ttl().Full_txt());
			ary[2] = X_to_str_ary_itm(src, itm.Alt_bgn(), itm.Alt_end());
			ary[3] = X_to_str_ary_itm(src, itm.Link_bgn(), itm.Link_end());
			ary[4] = X_to_str_ary_itm(src, itm.Page_bgn(), itm.Page_end());
			byte[] caption = itm.Caption_bry();
			ary[1] =  caption == null ? null : String_.new_utf8_(caption);
		}
		return rv;
	}
	private String X_to_str_ary_itm(byte[] src, int bgn, int end) {
		return bgn == ByteAry_.NotFound && end == ByteAry_.NotFound ? null : String_.new_utf8_(src, bgn, end);
	}
}