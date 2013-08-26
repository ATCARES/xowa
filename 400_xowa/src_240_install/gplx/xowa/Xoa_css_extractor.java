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
import gplx.ios.*;
public class Xoa_css_extractor {	
	public IoEngine_xrg_downloadFil Download_xrg() {return download_xrg;} private IoEngine_xrg_downloadFil download_xrg = Io_mgr._.DownloadFil_args("", Io_url_.Null);	
	public Xoa_css_extractor Wiki_domain_(byte[] v) {wiki_domain = v; return this;} private byte[] wiki_domain; 
	public Xoa_css_extractor Usr_dlg_(Gfo_usr_dlg v) {usr_dlg = v; return this;} private Gfo_usr_dlg usr_dlg;
	public Xoa_css_extractor Failover_dir_(Io_url v) {failover_dir = v; return this;} private Io_url failover_dir;
	public Xoa_css_extractor Wiki_html_dir_(Io_url v) {wiki_html_dir = v; return this;} private Io_url wiki_html_dir;
	public Xoa_css_extractor Mainpage_url_(String v) {mainpage_url = v; return this;} private String mainpage_url;
	public Xoa_css_extractor Protocol_prefix_(String v) {protocol_prefix = v; return this;} private String protocol_prefix = "http:";
	public Xoa_css_extractor Css_stylesheet_common_src_fmt_(String v) {css_stylesheet_common_src_fmt = v; return this;} private String css_stylesheet_common_src_fmt = "http://bits.wikimedia.org/{0}/load.php?debug=true&modules=ext.gadget.Edittools,WmfProjectStatusHelper,collapsibleTables,site|ext.rtlcite,wikihiero|ext.uls.nojs|mediawiki.legacy.commonPrint,shared|mw.PopUpMediaTransform|skins.vector&only=styles&skin=vector&*";
	public Xoa_css_extractor Page_fetcher_(Xow_page_fetcher v) {page_fetcher = v; return this;} private Xow_page_fetcher page_fetcher;
	public Xoa_css_extractor Css_img_downloader_(Xoa_css_img_downloader v) {this.css_img_downloader = v; return this;} private Xoa_css_img_downloader css_img_downloader;
	public Xoa_css_extractor Css_stylesheet_common_download_(boolean v) {css_stylesheet_common_download = v; return this;} private boolean css_stylesheet_common_download;
	public Xoa_css_extractor Url_encoder_(Url_encoder v) {url_encoder = v; return this;} Url_encoder url_encoder;
	private byte[] mainpage_bry; private boolean lang_is_ltr = true;
	public void App_init(Xoa_app app) {
		this.usr_dlg = app.Usr_dlg();
		Xof_download_wkr download_wkr = app.File_mgr().Download_mgr().Download_wkr();
		this.download_xrg = download_wkr.Download_xrg();
		css_img_downloader = new Xoa_css_img_downloader().Ctor(usr_dlg, download_wkr, ByteAry_.new_utf8_(protocol_prefix));
		failover_dir = app.Fsys_mgr().Bin_any_dir().GenSubDir_nest("html", "xowa", "import");
		url_encoder = app.Url_converter_url();
	}
	public void Chk(Xow_wiki wiki, Io_url wiki_html_dir) {
		Io_url url_css_common = wiki_html_dir.GenSubFil("xowa_common.css");
		Io_url url_css_wiki   = wiki_html_dir.GenSubFil("xowa_wiki.css");
		Xoh_wiki_article wiki_article = wiki.Html_mgr().Output_mgr();
		wiki_article.Css_common_bry_(url_css_common).Css_wiki_bry_(url_css_wiki);
		if (wiki.Wiki_tid() == Xow_wiki_type_.Tid_home || Env_.Mode_testing()) return;		// NOTE: do not download if xowa; also needed for TEST
		if (Io_mgr._.ExistsFil(url_css_wiki)) return;				// css file exists; nothing to generate
		wiki.App().Usr_dlg().Log_many("", "", "generating css for '~{0}'", wiki.Domain_str());
		this.Install(wiki, wiki_html_dir);
	}
	public void Install(Xow_wiki wiki, Io_url wiki_html_dir) {
		css_stylesheet_common_download = wiki.App().Setup_mgr().Dump_mgr().Css_commons_download();
		this.wiki_domain = wiki.Domain_bry();
		mainpage_url = "http://" + wiki.Key_str();	// NOTE: cannot reuse protocol_prefix b/c "//" needs to be added manually; protocol_prefix is used for logo and images which have form of "//domain/image.png"
		if (page_fetcher == null) page_fetcher = new Xow_page_fetcher_wiki();
		((Xow_page_fetcher_wiki)page_fetcher).Wiki_(wiki);
		this.wiki_html_dir = wiki_html_dir;
		this.lang_is_ltr = wiki.Lang().Dir_ltr();
		mainpage_bry = Mainpage_download_bry();
		Logo_setup();
		Css_stylesheet_common_setup();
		Css_scrape_setup();
		Css_stylesheet_wiki_setup();
	}
	public void Css_stylesheet_wiki_setup() {
		boolean css_stylesheet_wiki_missing = true;
		Io_url trg_fil = wiki_html_dir.GenSubFil(Css_wiki_name);
		if (Io_mgr._.ExistsFil(trg_fil)) return;	// don't download if already there
		css_stylesheet_wiki_missing = !Css_stylesheet_wiki_generate(trg_fil);
		if (css_stylesheet_wiki_missing)
			Failover(trg_fil);
		else 
			css_img_downloader.Chk(wiki_domain, trg_fil);
	}
	private boolean Css_stylesheet_wiki_generate(Io_url trg_fil) {
		ByteAryBfr bfr = ByteAryBfr.new_();
		Css_stylesheet_wiki_generate_section(bfr, Ttl_common_css);
		Css_stylesheet_wiki_generate_section(bfr, Ttl_vector_css);
		byte[] bry = bfr.XtoAryAndClear();
		bry = ByteAry_.Replace(bry, gplx.xowa.bldrs.xmls.Xob_xml_parser_.Bry_tab_ent, gplx.xowa.bldrs.xmls.Xob_xml_parser_.Bry_tab);
		Io_mgr._.SaveFilByt(trg_fil, bry);
		return true;
	}	static final byte[] Ttl_common_css = ByteAry_.new_ascii_("Common.css"), Ttl_vector_css = ByteAry_.new_ascii_("Vector.css");
	private boolean Css_stylesheet_wiki_generate_section(ByteAryBfr bfr, byte[] ttl) {
		byte[] page = page_fetcher.Fetch(Xow_ns_.Id_mediaWiki, ttl);
		if (page == null) return false;
		if (bfr.Bry_len() != 0) bfr.Add_byte_nl().Add_byte_nl();	// add "\n\n" between sections; !=0 checks against first
		Css_stylesheet_wiki_section_hdr.Bld_bfr_many(bfr, ttl);		// add "/*XOWA:MediaWiki:Common.css*/\n"
		bfr.Add(page);												// add page
		return true;
	}	static final ByteAryFmtr Css_stylesheet_wiki_section_hdr = ByteAryFmtr.new_("/*XOWA:MediaWiki:~{ttl}*/\n", "ttl");
	public void Css_stylesheet_common_setup() {
		if (css_stylesheet_common_download)
			Css_stylesheet_common_download();
		else
			Css_stylesheet_common_copy();
	}
	private void Css_stylesheet_common_copy() {
		Io_url trg_fil = wiki_html_dir.GenSubFil(Css_common_name);
		Io_mgr._.CopyFil(Css_stylesheet_common_failover_url(), trg_fil, true);
		css_img_downloader.Chk(wiki_domain, trg_fil);
	}
	private void Css_stylesheet_common_download() {
		boolean css_stylesheet_common_missing = true;
		Io_url trg_fil = wiki_html_dir.GenSubFil(Css_common_name);
		css_stylesheet_common_missing = !Css_stylesheet_common_download(trg_fil);
		if (css_stylesheet_common_missing)
			Io_mgr._.CopyFil(Css_stylesheet_common_failover_url(), trg_fil, true);
		else 
			css_img_downloader.Chk(wiki_domain, trg_fil);
	}
	private Io_url Css_stylesheet_common_failover_url() {return failover_dir.GenSubFil(lang_is_ltr ? Css_common_name_ltr : Css_common_name_rtl);}
	private boolean Css_stylesheet_common_download(Io_url trg_fil) {
		String src_fil = String_.Format(css_stylesheet_common_src_fmt, String_.new_utf8_(wiki_domain));
		String log_msg = usr_dlg.Prog_many("", "", "downloading css common: '~{0}'", src_fil);
		boolean rv = download_xrg.Prog_fmt_hdr_(log_msg).Src_(src_fil).Trg_(trg_fil).Exec();
		if (!rv)
			usr_dlg.Warn_many("", "", "failed to download css_common: src_url=~{0};", src_fil);
		return rv;
	}
	public void Logo_setup() {
		boolean logo_missing = true;
		Io_url trg_fil = wiki_html_dir.GenSubFil("logo.png");
		if (Io_mgr._.ExistsFil(trg_fil)) return;	// don't download if already there
		logo_missing = !Logo_download(trg_fil);
		if (logo_missing)
			Failover(trg_fil);
	}
	private boolean Logo_download(Io_url trg_fil) {
		String src_fil = Logo_find_src();
		if (src_fil == null) {
			usr_dlg.Warn_many("", "", "failed to extract logo: trg_fil=~{0};", trg_fil.Raw());
			return false;
		}
		String log_msg = usr_dlg.Prog_many("", "", "downloading logo: '~{0}'", src_fil);
		boolean rv = download_xrg.Prog_fmt_hdr_(log_msg).Src_(src_fil).Trg_(trg_fil).Exec();
		if (!rv)
			usr_dlg.Warn_many("", "", "failed to download logo: src_url=~{0};", src_fil);
		return rv;
	}
	private String Logo_find_src() {
		if (mainpage_bry == null) return null;
		int main_page_html_len = mainpage_bry.length;
		int logo_bgn = ByteAry_.FindFwd(mainpage_bry, Logo_find_bgn, 0); 									if (logo_bgn == ByteAry_.NotFound) return null;
		logo_bgn += Logo_find_bgn.length;
		logo_bgn = ByteAry_.FindFwd(mainpage_bry, Logo_find_end, logo_bgn);	 								if (logo_bgn == ByteAry_.NotFound) return null;
		logo_bgn += Logo_find_end.length;
		int logo_end = ByteAry_.FindFwd(mainpage_bry, Byte_ascii.Paren_end, logo_bgn, main_page_html_len);	if (logo_bgn == ByteAry_.NotFound) return null;
		byte[] logo_bry = ByteAry_.Mid(mainpage_bry, logo_bgn, logo_end);
		return protocol_prefix + String_.new_utf8_(logo_bry);
	}
	private static final byte[] Logo_find_bgn = ByteAry_.new_ascii_("<div id=\"p-logo\""), Logo_find_end = ByteAry_.new_ascii_("background-image: url(");
	public boolean Mainpage_download() {
		mainpage_bry = Mainpage_download_bry();
		return mainpage_bry != null;
	}
	private byte[] Mainpage_download_bry() {
		String log_msg = usr_dlg.Prog_many("", "main_page.download", "downloading main page for '~{0}'", wiki_domain);
		byte[] main_page_html = download_xrg.Prog_fmt_hdr_(log_msg).Exec_as_bry(mainpage_url);
		if (main_page_html == null) usr_dlg.Warn_many("", "", "failed to download main_page: src_url=~{0};", mainpage_url);
		return main_page_html;
	}
	private void Failover(Io_url trg_fil) {
		usr_dlg.Note_many("", "", "copying failover file: trg_fil=~{0};", trg_fil.Raw());
		Io_mgr._.CopyFil(failover_dir.GenSubFil(trg_fil.NameAndExt()), trg_fil, true);		
	}
	public void Css_scrape_setup() {			
		Io_url trg_fil = wiki_html_dir.GenSubFil(Css_common_name);
		if (Io_mgr._.ExistsFil(trg_fil)) return;	// don't download if already there
		byte[] css_url = Css_scrape();
		if (css_url == null)
			Failover(trg_fil);
		else {
			Io_mgr._.SaveFilByt(trg_fil, css_url);
			css_img_downloader.Chk(wiki_domain, trg_fil);
		}
	}
	private byte[] Css_scrape() {
		if (mainpage_bry == null) return null;
		String[] css_urls = Css_scrape_urls(mainpage_bry);		if (css_urls.length == 0) return null;
		return Css_scrape_download(css_urls);
	}
	private String[] Css_scrape_urls(byte[] raw) {
		ListAdp rv = ListAdp_.new_();
		int raw_len = raw.length;
		int prv_pos = 0; 
		int css_find_bgn_len = Css_find_bgn.length;
		byte[] protocol_prefix_bry = ByteAry_.new_utf8_(protocol_prefix);
		while (true) {
			int url_bgn = ByteAry_.FindFwd(raw, Css_find_bgn, prv_pos); 				if (url_bgn == ByteAry_.NotFound) break;	// nothing left; stop
			url_bgn += css_find_bgn_len;
			int url_end = ByteAry_.FindFwd(raw, Byte_ascii.Quote, url_bgn, raw_len); 	if (url_end == ByteAry_.NotFound) {usr_dlg.Warn_many("", "main_page.css_parse", "could not find css; pos='~{0}' text='~{1}'", url_bgn, String_.new_utf8_len_safe_(raw, url_bgn, url_bgn + 32)); break;}
			byte[] css_url_bry = ByteAry_.Mid(raw, url_bgn, url_end);
			css_url_bry = ByteAry_.Replace(css_url_bry, Css_amp_find, Css_amp_repl);	// &amp; -> &
			css_url_bry = url_encoder.Decode(css_url_bry);								// %2C ->		%7C -> |
			css_url_bry = ByteAry_.Add(protocol_prefix_bry, css_url_bry);
			rv.Add(String_.new_utf8_(css_url_bry));
			prv_pos = url_end;
		}
		return rv.XtoStrAry();
	}	private static final byte[] Css_find_bgn = ByteAry_.new_ascii_("<link rel=\"stylesheet\" href=\""), Css_amp_find = ByteAry_.new_ascii_("&amp;"), Css_amp_repl = ByteAry_.new_ascii_("&");
	private byte[] Css_scrape_download(String[] css_urls) {
		int css_urls_len = css_urls.length;
		ByteAryBfr tmp_bfr = ByteAryBfr.new_();
		for (int i = 0; i < css_urls_len; i++) {
			String css_url = css_urls[i];
			usr_dlg.Prog_many("", "main_page.css_download", "downloading css for '~{0}'", css_url);
			download_xrg.Prog_fmt_hdr_(css_url);
			byte[] css_bry = download_xrg.Exec_as_bry(css_url); if (css_bry == null) continue;	// css not found; continue
			tmp_bfr.Add(Xoa_css_img_downloader.Bry_comment_bgn).Add_str(css_url).Add(Xoa_css_img_downloader.Bry_comment_end).Add_byte_nl();
			tmp_bfr.Add(css_bry).Add_byte_nl().Add_byte_nl();
		}
		return tmp_bfr.XtoAryAndClear();
	}
	public static final String Css_common_name = "xowa_common.css", Css_wiki_name = "xowa_wiki.css", Css_common_name_ltr = "xowa_common_ltr.css", Css_common_name_rtl = "xowa_common_rtl.css";
}