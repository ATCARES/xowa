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
package gplx.xowa; import gplx.*;
import org.junit.*;
public class Xof_xfer_queue_html_wmf_api_tst {
	Xof_xfer_queue_html_fxt fxt = new Xof_xfer_queue_html_fxt();
	@Before public void init()		{fxt.Clear(true); fxt.Src_commons_repo().Wmf_api_(true); fxt.Src_en_wiki_repo().Wmf_api_(true);}
	@Test  public void Thumb() {
		fxt	.ini_page_api("en_wiki", "A.png", "", 2200, 2000);
		fxt	.Lnki_thumb_("A.png", 220)
			.Src(	fxt.img_("mem/src/en.wikipedia.org/thumb/7/70/A.png/220px-A.png", 220, 200))
			.Trg(	fxt.img_("mem/trg/en.wikipedia.org/fit/7/0/A.png/220px.png", 220, 200)
				,	fxt.reg_("mem/xowa/file/#meta/en.wikipedia.org/7/70.csv", "A.png|y||2?2200,2000|1?220,200")
				)
			.tst();
	}
	@Test  public void Redirect() {
		fxt	.ini_page_api("en_wiki", "B.png", "A.png", 2200, 2000);
		fxt	.Lnki_thumb_("B.png", 220)
			.Src(	fxt.img_("mem/src/en.wikipedia.org/thumb/7/70/A.png/220px-A.png", 220, 200))
			.Trg(	fxt.img_("mem/trg/en.wikipedia.org/fit/7/0/A.png/220px.png", 220, 200)
				,	fxt.reg_("mem/xowa/file/#meta/en.wikipedia.org/5/57.csv", "B.png|y|A.png|2?2200,2000|1?220,200")
				)
			.tst();
	}
	@Test  public void Svg_thumb_can_be_bigger_than_orig__download() {// PURPOSE: svg thumbs allowed to exceed orig in size; EX: w:Portal:Music; [[File:Treble a.svg|left|160px]]
		fxt	.ini_page_api("en_wiki", "A.svg", "", 110, 100);															// put orig of 110,100 on server
		fxt	.Lnki_thumb_("A.svg", 220)																					// request 220
			.Src(	fxt.img_("mem/src/en.wikipedia.org/thumb/7/75/A.svg/220px-A.svg.png", 220, 200)						// thumb = 220
				)
			.Trg(	fxt.img_("mem/trg/en.wikipedia.org/fit/7/5/A.svg/220px.png"		, 220, 200)							// thumb = 220
				,	fxt.reg_("mem/xowa/file/#meta/en.wikipedia.org/7/75.csv"		, "A.svg|y||2?110,100|1?220,200")
			)
			.tst();
		fxt	.Lnki_thumb_("A.svg", 220)																	
		.Html_src_("file:///mem/trg/en.wikipedia.org/fit/7/5/A.svg/220px.png")
		.Html_size_(220, 200)
		.tst();
	}
	@Test  public void Pdf() {// PURPOSE: main page always assumes size of 800x600; if actual size does not scale to 800x600, don't redownload; [[File:Physical world.pdf|thumb]]
		fxt.ini_page_api("en_wiki", "A.pdf", "", 6600, 5100);
		fxt	.ini_page_create_commons			("File:A.pdf");
		fxt	.Lnki_thumb_("A.pdf", 800, 600)
			.Src(	fxt.img_("mem/src/en.wikipedia.org/e/ef/A.pdf", 6600, 5100)
				,	fxt.img_("mem/src/en.wikipedia.org/thumb/e/ef/A.pdf/page1-777px-A.pdf.jpg", 777, 600)
				)
			.Trg(	fxt.img_("mem/trg/en.wikipedia.org/fit/e/f/A.pdf/777px.jpg", 777, 600)
				,	fxt.reg_("mem/xowa/file/#meta/en.wikipedia.org/e/ef.csv", "A.pdf|y||2?6600,5100|1?777,600")
				)
			.tst();
		fxt	.Lnki_thumb_("A.pdf", 800, 600)
			.Html_src_("file:///mem/trg/en.wikipedia.org/fit/e/f/A.pdf/777px.jpg")
			.Html_size_(777, 600)
			.tst();
	}
	@Test  public void Upright_defect() {	// PURPOSE.fix: upright not working;  EX: w:Beethoven; [[File:Rudolf-habsburg-olmuetz.jpg|thumb|upright|]]
		fxt	.ini_page_api("en_wiki", "A.png", "", 1378, 1829);
		fxt	.Lnki_("A.png", true, 220, -1, 1, Xop_lnki_tkn.Thumbtime_null)
			.Src(	fxt.img_("mem/src/en.wikipedia.org/thumb/7/70/A.png/170px-A.png", 170, 226))
			.Trg(	fxt.img_("mem/trg/en.wikipedia.org/fit/7/0/A.png/170px.png", 170, 226)
				,	fxt.reg_("mem/xowa/file/#meta/en.wikipedia.org/7/70.csv"		, "A.png|y||2?1378,1829|1?170,226")
				)
			.tst();
	}
	@Test  public void Height_only() {	// PURPOSE.fix: height only was still using old infer-size code; EX:w:[[File:Upper and Middle Manhattan.jpg|x120px]]; DATE:2012-12-27
		fxt	.ini_page_api("en_wiki", "A.png", "", 12591, 1847);
		fxt	.Lnki_("A.png", false, -1, 130, Xop_lnki_tkn.Upright_null, Xop_lnki_tkn.Thumbtime_null)
			.Src(	fxt.img_("mem/src/en.wikipedia.org/thumb/7/70/A.png/887px-A.png", 887, 130))
			.Trg(	fxt.img_("mem/trg/en.wikipedia.org/fit/7/0/A.png/887px.png", 887, 130)
				,	fxt.reg_("mem/xowa/file/#meta/en.wikipedia.org/7/70.csv"		, "A.png|y||2?12591,1847|1?887,130")
				)
			.tst();
	}
	@Test  public void Width_only_height_ignored() {// PURPOSE.fix: if height is not specified, do not recalc; needed when true scaled height is 150x151 but WM has 150x158; defect would discard 150x158; EX:[[File:Tokage_2011-07-15.jpg|150px]] simple.wikipedia.org/wiki/2011_Pacific_typhoon_season; DATE:2013-06-03
		fxt	.ini_page_api("en_wiki", "A.png", "", 4884, 4932);
		fxt	.Lnki_("A.png", true, 150, -1, Xop_lnki_tkn.Upright_null, Xop_lnki_tkn.Thumbtime_null)
			.Src(	fxt.img_("mem/src/en.wikipedia.org/thumb/7/70/A.png/150px-A.png", 150, 158))
			.Trg(	fxt.img_("mem/trg/en.wikipedia.org/fit/7/0/A.png/150px.png", 150, 158)
				,	fxt.reg_("mem/xowa/file/#meta/en.wikipedia.org/7/70.csv"		, "A.png|y||2?4884,4932|1?150,158")
				)
			.tst();
	}
	@Test  public void Missing_was_not_being_marked() {	// PURPOSE.fix: missing image was not showing up as repo=x in meta; DATE:2013-01-10
		fxt	.Lnki_("A.png", false, -1, 130, Xop_lnki_tkn.Upright_null, Xop_lnki_tkn.Thumbtime_null)
			.Src()
			.Trg(	fxt.reg_("mem/xowa/file/#meta/en.wikipedia.org/7/70.csv"		, "A.png|x||0?0,0|")
				)
			.tst();
	}
	@Test  public void Missing_was_not_redownloaded() {	// PURPOSE.fix: missing image was not being redownloaded; DATE:2013-01-26
		fxt.save_(fxt.reg_("mem/xowa/file/#meta/en.wikipedia.org/7/70.csv", "A.png|x||0?0,0|"));			// mark file as missing
		fxt	.ini_page_api("en_wiki", "A.png", "", 220, 200);
		fxt.En_wiki().File_mgr().Cfg_download().Redownload_(Xof_cfg_download.Redownload_missing);			// redownload for missing
		fxt	.Lnki_orig_("A.png")
			.Src(	fxt.img_("mem/src/en.wikipedia.org/7/70/A.png", 220, 200))
			.Trg(	fxt.reg_("mem/xowa/file/#meta/en.wikipedia.org/7/70.csv"		, "A.png|x||1?220,200|")// check that file shows up
				)
			.tst();
		fxt.En_wiki().File_mgr().Cfg_download().Redownload_(Xof_cfg_download.Redownload_none);				// redownload back to none (for other tests)
	}
	@Test  public void Error_should_not_abort() {	// PURPOSE: API sometimes returns xml but no <iinfo> node; try to download anyway
		fxt	.ini_page_api("commons", "A.png", "", 2200, 2000, false);
		fxt	.Lnki_thumb_("A.png", 220)
			.Src(	fxt.img_("mem/src/commons.wikimedia.org/thumb/7/70/A.png/220px-A.png", 220, 200))
			.Trg(	fxt.reg_("mem/xowa/file/#meta/en.wikipedia.org/7/70.csv", "A.png|0||0?0,0|1?220,200")
				)
			.tst();
	}
	@Test   public void Tilde() {
		fxt	.ini_page_api("en_wiki", "A~.png", "", 2200, 2000);
		fxt	.Lnki_thumb_("A~.png", 220)
		.Src(	fxt.img_("mem/src/en.wikipedia.org/thumb/a/a5/A%7E.png/220px-A%7E.png", 220, 200))
		.Trg(	fxt.img_("mem/trg/en.wikipedia.org/fit/a/5/A~.png/220px.png", 220, 200)
				,	fxt.reg_("mem/xowa/file/#meta/en.wikipedia.org/a/a5.csv", "A~~.png|y||2?2200,2000|1?220,200")	// NOTE: tildes are doubled in meta file
				)
				.tst();
	}
	@Test  public void Missing_from_1st_repo() {	// PURPOSE: WMF now requires that API goes to image's actual repo (used to accept http://en.wikipedia.org and return back http://commons.wikimedia.org) DATE:2013-03-11
		fxt	.ini_page_api("commons", "A.png", "B.png", 2200, 2000);	// put the redirect in commons wiki
		fxt	.Lnki_thumb_("A.png", 220)
			.Src(	fxt.img_("mem/src/commons.wikimedia.org/thumb/5/57/B.png/220px-B.png", 220, 200))
			.Trg(	fxt.reg_("mem/xowa/file/#meta/en.wikipedia.org/7/70.csv", "A.png|0|B.png|2?2200,2000|1?220,200")
				)
			.tst();
	}
	@Test  public void Ogg_audio() {	// PURPOSE: ogg is audio; (a) do not download thumb; (b) get from correct wiki;  DATE:2013-08-03
		fxt	.ini_page_create_commons("File:A.ogg");
		fxt	.ini_page_api("commons", "A.ogg", "", 0, 0);
		fxt	.Lnki_("A.ogg", false, -1, -1, 1, Xop_lnki_tkn.Thumbtime_null)
			.Src(	fxt.img_("mem/src/commons.wikimedia.org/4/42/A.ogg", 0, 0))
			.Trg(	fxt.img_("mem/trg/commons.wikimedia.org/raw/4/2/A.ogg", 0, 0)
				,	fxt.reg_("mem/xowa/file/#meta/en.wikipedia.org/4/42.csv"		, "A.ogg|0||1?0,0|0?0,0")
				)
			.tst();
	}
}
// Tfds.Write_bry(Xof_xfer_itm.Md5_calc(ByteAry_.new_ascii_("A~.png")));
