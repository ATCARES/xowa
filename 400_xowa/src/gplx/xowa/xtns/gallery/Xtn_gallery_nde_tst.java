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
package gplx.xowa.xtns.gallery; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import org.junit.*;
public class Xtn_gallery_nde_tst {
	Xop_fxt fxt = new Xop_fxt(); String raw_src;
	@Before public void init() {fxt.Reset();}
	@Test  public void Lnki_no_caption() {
		fxt.tst_Parse_page_wiki("<gallery>File:A.png</gallery>"
				,	fxt.tkn_xnde_().Xnde_tagId_(Xop_xnde_tag_.Tid_gallery).Xnde_data_
				(	new_chkr_gallery_mgr().Expd_subs_
					(	new_chkr_gallery_itm().Expd_lnki_("File:A.png")
					)
				));
	}
	@Test  public void Lnki_1() {
		fxt.tst_Parse_page_wiki("<gallery>File:A.png|b</gallery>"
				,	fxt.tkn_xnde_().Xnde_tagId_(Xop_xnde_tag_.Tid_gallery).Xnde_data_
				(	new_chkr_gallery_mgr().Expd_subs_
					(	new_chkr_gallery_itm().Expd_lnki_("File:A.png")
					)
				));
	}
	@Test  public void Lnki_3() {
		fxt.tst_Parse_page_wiki("<gallery>File:A.png|a\nFile:B.png|b\nFile:C.png|c</gallery>"
				,	fxt.tkn_xnde_().Xnde_tagId_(Xop_xnde_tag_.Tid_gallery).Xnde_data_
				(	new_chkr_gallery_mgr().Expd_subs_
					(	new_chkr_gallery_itm().Expd_lnki_("File:A.png")
					,	new_chkr_gallery_itm().Expd_lnki_("File:B.png")
					,	new_chkr_gallery_itm().Expd_lnki_("File:C.png")
					)
				));
	}
	@Test  public void Ignore_newLines() {
		fxt.tst_Parse_page_wiki("<gallery>\n\n\nFile:A.png|a\n\n\nFile:B.png|b\n\n\n</gallery>"
				,	fxt.tkn_xnde_().Xnde_tagId_(Xop_xnde_tag_.Tid_gallery).Xnde_data_
				(	new_chkr_gallery_mgr().Expd_subs_
					(	new_chkr_gallery_itm().Expd_lnki_("File:A.png")
					,	new_chkr_gallery_itm().Expd_lnki_("File:B.png")
					)
				));
	}
	@Test  public void Only_first_pipe() {
		fxt.tst_Parse_page_wiki("<gallery>File:A.png|File:B.png|cc</gallery>"
				,	fxt.tkn_xnde_().Xnde_tagId_(Xop_xnde_tag_.Tid_gallery).Xnde_data_
				(	new_chkr_gallery_mgr().Expd_subs_
					(	new_chkr_gallery_itm().Expd_lnki_("File:A.png")
					)
				));
	}
	@Test  public void Invalid_lnki() {
		fxt.tst_Parse_page_wiki("<gallery>A.png|cc</gallery>"
				,	fxt.tkn_xnde_().Xnde_tagId_(Xop_xnde_tag_.Tid_gallery).Xnde_data_
				(	new_chkr_gallery_mgr().Expd_subs_
					(	new_chkr_gallery_itm().Expd_lnki_("A.png")
					)
				));
	}
	@Test  public void File_only_trailing_nl() {
		fxt.tst_Parse_page_wiki("<gallery>File:A.png\n</gallery>"
				,	fxt.tkn_xnde_().Xnde_tagId_(Xop_xnde_tag_.Tid_gallery).Xnde_data_
				(	new_chkr_gallery_mgr().Expd_subs_
					(	new_chkr_gallery_itm().Expd_lnki_("File:A.png").Expd_subs_()
					)
				));
	}
	@Test  public void Invalid_curly() {
		raw_src = "a\n";			
		fxt.ini_Log_(Xop_ttl_log.Invalid_char).tst_Parse_page_wiki("<gallery>File:A.png|" + raw_src + "}}</gallery>"	// NOTE: }} is ignored since it is not a valid title
				,	fxt.tkn_xnde_().Xnde_tagId_(Xop_xnde_tag_.Tid_gallery).Xnde_data_
				(	new_chkr_gallery_mgr().Expd_subs_
					(	new_chkr_gallery_itm().Expd_lnki_("File:A.png").Expd_subs_
						(	fxt.tkn_txt_().Raw_src_(raw_src).Src_rng_(0, 1).Raw_("a")
						)
					)
				));
	}
	@Test  public void Caption() {
		raw_src = "a<br/>c";
		fxt.tst_Parse_page_wiki("<gallery>File:A.png|" + raw_src + "</gallery>"
				,	fxt.tkn_xnde_().Xnde_tagId_(Xop_xnde_tag_.Tid_gallery).Xnde_data_
				(	new_chkr_gallery_mgr().Expd_subs_
					(	new_chkr_gallery_itm().Expd_lnki_("File:A.png").Expd_subs_
						(	fxt.tkn_txt_().Raw_src_(raw_src).Src_rng_(0, 1).Raw_("a")
						,	fxt.tkn_xnde_().Raw_src_(raw_src).Src_rng_(1, 6).Raw_("<br/>")
						,	fxt.tkn_txt_().Raw_src_(raw_src).Src_rng_(6, 7).Raw_("c")
						)
					)
				));
	}
	@Test  public void Xnde_atr() {
		raw_src = "<center>a<br/>b</center>";
		fxt.tst_Parse_page_wiki(String_.Concat_lines_nl_skipLast
			(	"<gallery perrow=3>"
			,	"File:A.jpg|" + raw_src
			,	"</gallery>"
			) ,	fxt.tkn_xnde_().Xnde_tagId_(Xop_xnde_tag_.Tid_gallery).Xnde_data_
				(	new_chkr_gallery_mgr().Expd_subs_
					(	new_chkr_gallery_itm().Expd_lnki_("File:A.jpg").Expd_subs_
						(	fxt.tkn_xnde_().Raw_src_(raw_src).Raw_("<center>a<br/>b</center>").Subs_
							(	fxt.tkn_txt_().Raw_src_(raw_src).Raw_("a")
							,	fxt.tkn_xnde_().Raw_src_(raw_src).Raw_("<br/>")
							,	fxt.tkn_txt_().Raw_src_(raw_src).Raw_("b")
							)
						)
					)
				));
	}
	@Test  public void Err_pre() {	// PURPOSE: leading ws was failing; EX.WP: Vlaardingen; "\nA.jpg| <center>Visbank</center>\n"
		raw_src = " <center>a</center>";
		fxt.tst_Parse_page_wiki(String_.Concat_lines_nl_skipLast
			(	"<gallery>"
			,	"File:A.jpg|" + raw_src
			,	"</gallery>"
			) ,	fxt.tkn_xnde_().Xnde_tagId_(Xop_xnde_tag_.Tid_gallery).Xnde_data_
				(	new_chkr_gallery_mgr().Expd_subs_
					(	new_chkr_gallery_itm().Expd_lnki_("File:A.jpg").Expd_subs_
						(	fxt.tkn_space_()
						,	fxt.tkn_xnde_().Raw_src_(raw_src).Raw_("<center>a</center>"))
					)
				));
	}
	@Test  public void Err_comment() {	// PURPOSE: comment was being rendered; EX.WP: Perpetual motion; <!-- removed A.jpg|bcde -->
		raw_src = "b";
		fxt.ini_Log_(Xop_ttl_log.Comment_eos).tst_Parse_page_wiki(String_.Concat_lines_nl_skipLast
			(	"<gallery>"
			,	"<!-- deleted A.jpg|" + raw_src
			,	"</gallery>"
			) ,	fxt.tkn_xnde_().Xnde_tagId_(Xop_xnde_tag_.Tid_gallery).Xnde_data_
				(	new_chkr_gallery_mgr().Expd_subs_()
				)
			);
	}
	void Init_html() {
		Io_mgr._.InitEngine_mem();	// clear out mem files
		Io_url rootDir = Io_url_.mem_dir_("mem").GenSubDir_nest(Xoa_app_.App_name);
		fxt.App().Fsys_mgr().Temp_dir_(rootDir.OwnerDir().GenSubDir("tmp"));
//			fxt.Wiki().FileMgr_
//			( new Xof_mgr(fxt.App(), rootDir.GenSubDir("fit"), rootDir.GenSubDir("raw")
//				, new Xofx_repo("mem/wiki/c0/")
//				, new Xofx_repo("mem/wiki/en/")
//				));
	}
	@Test  public void Html() {
		Init_html();
		fxt.tst_Parse_page_wiki_str("<gallery perrow=2 widths=200px heights=300px>File:A.png|a<br/>c</gallery>", String_.Concat_lines_nl_skipLast
			(	"<ul class=\"gallery\" style=\"max-width:486px; _width:486px;\">"
			,	"  <li class=\"gallerybox\" style=\"width:235px;\">"
			,	"    <div style=\"width:235px;\">"
			,	"      <div class=\"thumb\" style=\"width:230px;\">"
			,	"        <div id=\"xowa_file_gallery_div_0\" style=\"margin:15px auto;\">"
			,	"          <a href=\"/wiki/File:A.png\" class=\"image\">"
			,	"            <img id=\"xowa_file_img_0\" alt=\"A.png\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/200px.png\" width=\"200\" height=\"300\" />"
			,	"          </a>"
			,	"        </div>"
			,	"      </div>"
			,	"      <div class=\"gallerytext\">a<br/>c"
			,	"      </div>"
			,	"    </div>"
			,	"  </li>"
			,	"</ul>"
			));
	}
	@Test  public void Tmpl() {
		fxt.ini_defn_clear();
		fxt.ini_defn_add("test_tmpl", "b");
		Init_html();
		fxt.tst_Parse_page_wiki_str("<gallery>File:A.png|a{{test_tmpl}}c</gallery>", String_.Concat_lines_nl_skipLast
			(	"<ul class=\"gallery\" style=\"max-width:652px; _width:652px;\">"
			,	"  <li class=\"gallerybox\" style=\"width:155px;\">"
			,	"    <div style=\"width:155px;\">"
			,	"      <div class=\"thumb\" style=\"width:150px;\">"
			,	"        <div id=\"xowa_file_gallery_div_0\" style=\"margin:15px auto;\">"
			,	"          <a href=\"/wiki/File:A.png\" class=\"image\">"
			,	"            <img id=\"xowa_file_img_0\" alt=\"A.png\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/120px.png\" width=\"120\" height=\"120\" />"
			,	"          </a>"
			,	"        </div>"
			,	"      </div>"
			,	"      <div class=\"gallerytext\">abc"
			,	"      </div>"
			,	"    </div>"
			,	"  </li>"
			,	"</ul>"
			));
		fxt.ini_defn_clear();
	}
	@Test  public void Item_defaults_to_120() {
		Init_html();
		fxt.tst_Parse_page_wiki_str("<gallery perrow=3>File:A.png|a<br/>c</gallery>", String_.Concat_lines_nl_skipLast
			(	"<ul class=\"gallery\" style=\"max-width:489px; _width:489px;\">"
			,	"  <li class=\"gallerybox\" style=\"width:155px;\">"
			,	"    <div style=\"width:155px;\">"
			,	"      <div class=\"thumb\" style=\"width:150px;\">"
			,	"        <div id=\"xowa_file_gallery_div_0\" style=\"margin:15px auto;\">"
			,	"          <a href=\"/wiki/File:A.png\" class=\"image\">"
			,	"            <img id=\"xowa_file_img_0\" alt=\"A.png\" src=\"file:///mem/wiki/repo/trg/thumb/7/0/A.png/120px.png\" width=\"120\" height=\"120\" />"
			,	"          </a>"
			,	"        </div>"
			,	"      </div>"
			,	"      <div class=\"gallerytext\">a<br/>c"
			,	"      </div>"
			,	"    </div>"
			,	"  </li>"
			,	"</ul>"
			));
	}
	private Xtn_gallery_mgr_data_chkr new_chkr_gallery_mgr()	{return new Xtn_gallery_mgr_data_chkr();}
	private Xtn_gallery_itm_data_chkr new_chkr_gallery_itm()	{return new Xtn_gallery_itm_data_chkr();}

}
class Xtn_gallery_mgr_data_chkr implements Tst_chkr {
	public Class<?> TypeOf() {return Xtn_gallery_nde.class;}
	public Xtn_gallery_itm_data_chkr[] Expd_subs() {return expd_subs;} public Xtn_gallery_mgr_data_chkr Expd_subs_(Xtn_gallery_itm_data_chkr... v) {expd_subs = v; return this;} Xtn_gallery_itm_data_chkr[] expd_subs = null;
	public int Chk(Tst_mgr mgr, String path, Object actl_obj) {
		Xtn_gallery_nde actl = (Xtn_gallery_nde)actl_obj;
		int rv = 0;
		rv += Chk_basic(mgr, path, actl, rv);
		rv += Chk_subs(mgr, path, actl, rv);
		return rv;
	}
	public int Chk_basic(Tst_mgr mgr, String path, Xtn_gallery_nde actl, int err) {
		return err;
	}
	public int Chk_subs(Tst_mgr mgr, String path, Xtn_gallery_nde actl, int err) {
		if (expd_subs != null) {
			int actl_subs_len = actl.Itms_len();
			Xtn_gallery_itm_data[] actl_subs = new Xtn_gallery_itm_data[actl_subs_len];  
			for (int i = 0; i < actl_subs_len; i++)
				actl_subs[i] = actl.Itms_get(i);
			return mgr.Tst_sub_ary(expd_subs, actl_subs, path, err);
		}
		return err;
	}
}
class Xtn_gallery_itm_data_chkr implements Tst_chkr {
	public Class<?> TypeOf() {return Xtn_gallery_itm_data.class;}
	public Xtn_gallery_itm_data_chkr Expd_lnki_(String v) {expd_lnki = Xoa_ttl_chkr.new_(v); return this;}  Xoa_ttl_chkr expd_lnki;
	public Xtn_gallery_itm_data_chkr Expd_subs_(Tst_chkr... v) {expd_subs = v; return this;} Tst_chkr[] expd_subs = null;
	public int Chk(Tst_mgr mgr, String path, Object actl_obj) {
		Xtn_gallery_itm_data actl = (Xtn_gallery_itm_data)actl_obj;
		int err = 0;
		err += mgr.Tst_sub_obj(expd_lnki, actl.Lnki(), path, err);
		if (expd_subs != null) {
			int actl_subs_len = actl.Html_root() == null ? 0 : actl.Html_root().Subs_len();
			Xop_tkn_itm[] actl_subs = new Xop_tkn_itm[actl_subs_len];  
			for (int i = 0; i < actl_subs_len; i++)
				actl_subs[i] = actl.Html_root().Subs_get(i);
			return mgr.Tst_sub_ary(expd_subs, actl_subs, path, err) + err;
		}
		return err;
	}
}