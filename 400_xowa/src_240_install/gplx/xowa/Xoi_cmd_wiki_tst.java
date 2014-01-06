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
import org.junit.*; import gplx.xowa.wikis.*;
public class Xoi_cmd_wiki_tst {
	@Test  public void Run() {
//			Bld_import_list(Wikis);
//			Bld_cfg_files(Wikis);	// NOTE: remember to carry over the wikisource / page / index commands from the existing xowa_build_cfg.gfs; also, only run the xowa_build_cfg.gfs once; DATE:2013-10-15
	}
	public void Bld_import_list(String... ary) {
		int ary_len = ary.length;
		ByteAryBfr bfr = ByteAryBfr.reset_(255);
		Time_fmtr_arg time_fmtr = new Time_fmtr_arg();
		Xob_dump_file dump_file = new Xob_dump_file();
		for (int i = 0; i < ary_len; i++) {
			String itm = ary[i];
			dump_file.Ctor(itm, "latest", Xob_bz2_file.Key_pages_articles);
			int count = 0;
			while (count++ < 10) {
				dump_file.Server_url_(Xob_dump_file_.Server_wmf);
				if (dump_file.Connect()) break;
				Tfds.WriteText(String_.Format("retrying: {0} {1}\n", count, dump_file.File_modified()));
				ThreadAdp_.Sleep(2000);	// wait for connection to reset
			}
			if (count == 10) {
				Tfds.WriteText(String_.Format("failed: {0}\n", dump_file.File_url()));
				continue;
			}
			else
				Tfds.WriteText(String_.Format("passed: {0}\n", itm));
			bfr.Add_str(itm).Add_byte_pipe();
			bfr.Add_str(dump_file.File_url()).Add_byte_pipe();
			bfr.Add(Xow_wiki_domain_.Key_by_tid(dump_file.Wiki_type().Tid())).Add_byte_pipe();
//				Xol_lang_itm lang_itm = Xol_lang_itm_.Get_by_key(wiki_type.Lang_key());
//				if (lang_itm == null) lang_itm = Xol_lang_itm_.Get_by_key(Xol_lang_.Key_en);	// commons, species, meta, etc will have no lang
//				bfr.Add(lang_itm.Local_name()).Add_byte_pipe();
//				bfr.Add(lang_itm.Canonical_name()).Add_byte_pipe();
			long src_size = dump_file.File_len();
			bfr.Add_long_variable(src_size).Add_byte_pipe();
			bfr.Add_str(gplx.ios.Io_size_.Xto_str(src_size)).Add_byte_pipe();
			time_fmtr.Seconds_(Math_.Div_safe_as_long(src_size, 1000000)).XferAry(bfr, 0);
			bfr.Add_byte_pipe();
			bfr.Add_str(dump_file.File_modified().XtoStr_fmt_yyyy_MM_dd_HH_mm());
			bfr.Add_byte_pipe();
//				bfr.Add_str(String_.ConcatWith_any(",", (Object[])dump_file.Dump_available_dates()));
//				bfr.Add_byte_pipe();
			bfr.Add_str(dump_file.Dump_date());
			bfr.Add_byte_nl();
		}
		Io_mgr._.SaveFilStr("C:\\temp.txt", bfr.XtoStr());
	}
	public void Bld_cfg_files(String... ary) {
		ByteAryBfr bfr = ByteAryBfr.reset_(255);
		gplx.xowa.bldrs.wiki_cfgs.Xoi_wiki_props_api api = new gplx.xowa.bldrs.wiki_cfgs.Xoi_wiki_props_api();
		gplx.xowa.bldrs.wiki_cfgs.Xoi_wiki_props_wiki wiki = new gplx.xowa.bldrs.wiki_cfgs.Xoi_wiki_props_wiki();
		int ary_len = ary.length;
		for (int i = 0; i < ary_len; i++) {
			String wiki_domain = ary[i];
			try {
				byte[] xml = api.Exec_api(api.Api_src(wiki_domain));
				wiki.Wiki_domain_(ByteAry_.new_ascii_(wiki_domain));
				api.Parse(wiki, String_.new_utf8_(xml));
				api.Build_cfg(bfr, wiki);
			}
			catch (Exception e) {
				ConsoleAdp._.WriteLine(Err_.Message_gplx_brief(e));
			}
		}
		bfr.Add_str("app.wiki_cfg_bldr.run;").Add_byte_nl();
		Io_mgr._.SaveFilStr("C:\\xowa_build_cfg.gfs", bfr.XtoStr());
	}
	public static String[] Wikis = new String[]
{ "simple.wikipedia.org"
};
//, "simple.wiktionary.org"
//, "simple.wikibooks.org"
//, "en.wikipedia.org"
//, "en.wiktionary.org"
//, "en.wikisource.org"
//, "en.wikibooks.org"
//, "en.wikiversity.org"
//, "en.wikinews.org"
//, "en.wikivoyage.org"
//, "commons.wikimedia.org"
//, "species.wikimedia.org"
//, "meta.wikimedia.org"
//, "incubator.wikimedia.org"
//, "www.wikidata.org"
//, "www.mediawiki.org"
//, "wikimediafoundation.org"
//, "de.wikipedia.org"
//, "de.wiktionary.org"
//, "de.wikisource.org"
//, "de.wikibooks.org"
//, "de.wikiversity.org"
//, "de.wikinews.org"
//, "de.wikivoyage.org"
//, "es.wikipedia.org"
//, "es.wiktionary.org"
//, "es.wikisource.org"
//, "es.wikibooks.org"
//, "es.wikiversity.org"
//, "es.wikinews.org"
//, "fr.wikipedia.org"
//, "fr.wiktionary.org"
//, "fr.wikisource.org"
//, "fr.wikibooks.org"
//, "fr.wikiversity.org"
//, "fr.wikinews.org"
//, "fr.wikivoyage.org"
//, "it.wikipedia.org"
//, "it.wiktionary.org"
//, "it.wikisource.org"
//, "it.wikibooks.org"
//, "it.wikiversity.org"
//, "it.wikinews.org"
//, "it.wikivoyage.org"
//, "ja.wikipedia.org"
//, "ja.wiktionary.org"
//, "ja.wikisource.org"
//, "ja.wikibooks.org"
//, "ja.wikiversity.org"
//, "ja.wikinews.org"
//, "nl.wikipedia.org"
//, "nl.wiktionary.org"
//, "nl.wikisource.org"
//, "nl.wikibooks.org"
//, "nl.wikinews.org"
//, "nl.wikivoyage.org"
//, "pl.wikipedia.org"
//, "pl.wiktionary.org"
//, "pl.wikisource.org"
//, "pl.wikibooks.org"
//, "pl.wikinews.org"
//, "pt.wikipedia.org"
//, "pt.wiktionary.org"
//, "pt.wikisource.org"
//, "pt.wikibooks.org"
//, "pt.wikiversity.org"
//, "pt.wikinews.org"
//, "ru.wikipedia.org"
//, "ru.wiktionary.org"
//, "ru.wikisource.org"
//, "ru.wikibooks.org"
//, "ru.wikiversity.org"
//, "ru.wikinews.org"
//, "ru.wikivoyage.org"
//, "ar.wikipedia.org"
//, "ar.wiktionary.org"
//, "ar.wikisource.org"
//, "ar.wikibooks.org"
//, "ar.wikinews.org"
//, "ca.wikipedia.org"
//, "ca.wiktionary.org"
//, "ca.wikisource.org"
//, "ca.wikinews.org"
//, "cs.wikipedia.org"
//, "cs.wiktionary.org"
//, "cs.wikisource.org"
//, "cs.wikibooks.org"
//, "cs.wikiversity.org"
//, "cs.wikinews.org"
//, "da.wikipedia.org"
//, "da.wiktionary.org"
//, "da.wikisource.org"
//, "da.wikibooks.org"
//, "eo.wikipedia.org"
//, "eo.wiktionary.org"
//, "eo.wikibooks.org"
//, "fa.wikipedia.org"
//, "fa.wiktionary.org"
//, "fa.wikisource.org"
//, "fa.wikibooks.org"
//, "fa.wikinews.org"
//, "fi.wikipedia.org"
//, "fi.wiktionary.org"
//, "fi.wikisource.org"
//, "fi.wikibooks.org"
//, "fi.wikiversity.org"
//, "fi.wikinews.org"
//, "hu.wikipedia.org"
//, "hu.wiktionary.org"
//, "hu.wikinews.org"
//, "id.wikipedia.org"
//, "id.wiktionary.org"
//, "id.wikisource.org"
//, "kk.wikipedia.org"
//, "kk.wiktionary.org"
//, "kk.wikibooks.org"
//, "ko.wikipedia.org"
//, "ko.wiktionary.org"
//, "ko.wikisource.org"
//, "ko.wikibooks.org"
//, "ko.wikinews.org"
//, "lt.wikipedia.org"
//, "lt.wiktionary.org"
//, "lt.wikisource.org"
//, "lt.wikibooks.org"
//, "no.wikipedia.org"
//, "no.wiktionary.org"
//, "no.wikibooks.org"
//, "no.wikinews.org"
//, "ro.wikipedia.org"
//, "ro.wiktionary.org"
//, "ro.wikisource.org"
//, "ro.wikibooks.org"
//, "ro.wikinews.org"
//, "sk.wikipedia.org"
//, "sk.wiktionary.org"
//, "sk.wikisource.org"
//, "sk.wikibooks.org"
//, "sr.wikipedia.org"
//, "sr.wiktionary.org"
//, "sr.wikisource.org"
//, "sr.wikibooks.org"
//, "sr.wikinews.org"
//, "sv.wikipedia.org"
//, "sv.wiktionary.org"
//, "sv.wikisource.org"
//, "sv.wikibooks.org"
//, "sv.wikiversity.org"
//, "sv.wikinews.org"
//, "sv.wikivoyage.org"
//, "tr.wikipedia.org"
//, "tr.wiktionary.org"
//, "tr.wikisource.org"
//, "tr.wikibooks.org"
//, "tr.wikinews.org"
//, "uk.wikipedia.org"
//, "uk.wiktionary.org"
//, "uk.wikisource.org"
//, "uk.wikibooks.org"
//, "uk.wikinews.org"
//, "vi.wikipedia.org"
//, "vi.wiktionary.org"
//, "vi.wikisource.org"
//, "vi.wikibooks.org"
//, "zh.wikipedia.org"
//, "zh.wiktionary.org"
//, "zh.wikisource.org"
//, "zh.wikibooks.org"
//, "zh.wikinews.org"
//, "bg.wikipedia.org"
//, "bg.wiktionary.org"
//, "bg.wikibooks.org"
//, "bg.wikinews.org"
//, "el.wikipedia.org"
//, "el.wiktionary.org"
//, "el.wikisource.org"
//, "el.wikibooks.org"
//, "el.wikiversity.org"
//, "el.wikinews.org"
//, "et.wikipedia.org"
//, "et.wiktionary.org"
//, "et.wikibooks.org"
//, "eu.wikipedia.org"
//, "eu.wiktionary.org"
//, "eu.wikibooks.org"
//, "gl.wikipedia.org"
//, "gl.wiktionary.org"
//, "gl.wikisource.org"
//, "gl.wikibooks.org"
//, "he.wikipedia.org"
//, "he.wiktionary.org"
//, "he.wikisource.org"
//, "he.wikibooks.org"
//, "he.wikinews.org"
//, "hr.wikipedia.org"
//, "hr.wiktionary.org"
//, "hr.wikisource.org"
//, "hr.wikibooks.org"
//, "ms.wikipedia.org"
//, "ms.wiktionary.org"
//, "ms.wikibooks.org"
//, "nn.wikipedia.org"
//, "nn.wiktionary.org"
//, "sh.wikipedia.org"
//, "sh.wiktionary.org"
//, "sl.wikipedia.org"
//, "sl.wiktionary.org"
//, "sl.wikisource.org"
//, "sl.wikibooks.org"
//, "th.wikipedia.org"
//, "th.wiktionary.org"
//, "th.wikisource.org"
//, "th.wikibooks.org"
//, "th.wikinews.org"
//, "vo.wikipedia.org"
//, "vo.wiktionary.org"
//, "vo.wikibooks.org"
//, "hi.wikipedia.org"
//, "hi.wiktionary.org"
//, "hi.wikibooks.org"
//, "ia.wikipedia.org"
//, "ia.wiktionary.org"
//, "ia.wikibooks.org"
//, "la.wikipedia.org"
//, "la.wiktionary.org"
//, "la.wikisource.org"
//, "la.wikibooks.org"
//, "aa.wiktionary.org"
//, "ab.wikipedia.org"
//, "ab.wiktionary.org"
//, "ace.wikipedia.org"
//, "af.wikipedia.org"
//, "af.wiktionary.org"
//, "ak.wiktionary.org"
//, "af.wikibooks.org"
//, "als.wikipedia.org"
//, "als.wiktionary.org"
//, "als.wikibooks.org"
//, "am.wikipedia.org"
//, "am.wiktionary.org"
//, "an.wikipedia.org"
//, "an.wiktionary.org"
//, "ang.wikipedia.org"
//, "ang.wiktionary.org"
//, "ang.wikibooks.org"
//, "arc.wikipedia.org"
//, "as.wikipedia.org"
//, "as.wiktionary.org"
//, "ast.wikipedia.org"
//, "ast.wiktionary.org"
//, "ast.wikibooks.org"
//, "av.wikipedia.org"
//, "av.wiktionary.org"
//, "ay.wikipedia.org"
//, "ay.wiktionary.org"
//, "az.wikipedia.org"
//, "az.wiktionary.org"
//, "az.wikibooks.org"
//, "ba.wikipedia.org"
//, "bar.wikipedia.org"
//, "bcl.wikipedia.org"
//, "be.wikipedia.org"
//, "be.wiktionary.org"
//, "be.wikibooks.org"
//, "bh.wikipedia.org"
//, "bh.wiktionary.org"
//, "bi.wikipedia.org"
//, "bi.wiktionary.org"
//, "bm.wikipedia.org"
//, "bm.wiktionary.org"
//, "bn.wikipedia.org"
//, "bn.wiktionary.org"
//, "bn.wikisource.org"
//, "bn.wikibooks.org"
//, "bo.wikipedia.org"
//, "bo.wiktionary.org"
//, "bpy.wikipedia.org"
//, "br.wikipedia.org"
//, "br.wiktionary.org"
//, "bs.wikipedia.org"
//, "bs.wiktionary.org"
//, "bs.wikisource.org"
//, "bs.wikibooks.org"
//, "bs.wikinews.org"
//, "bug.wikipedia.org"
//, "bxr.wikipedia.org"
//, "cdo.wikipedia.org"
//, "ce.wikipedia.org"
//, "ceb.wikipedia.org"
//, "ch.wikipedia.org"
//, "ch.wiktionary.org"
//, "chr.wikipedia.org"
//, "chr.wiktionary.org"
//, "chy.wikipedia.org"
//, "co.wikipedia.org"
//, "co.wiktionary.org"
//, "co.wikibooks.org"
//, "cr.wikipedia.org"
//, "cr.wiktionary.org"
//, "csb.wikipedia.org"
//, "csb.wiktionary.org"
//, "cu.wikipedia.org"
//, "cv.wikipedia.org"
//, "cv.wikibooks.org"
//, "cy.wikipedia.org"
//, "cy.wiktionary.org"
//, "cy.wikisource.org"
//, "cy.wikibooks.org"
//, "dv.wikipedia.org"
//, "dv.wiktionary.org"
//, "dz.wikipedia.org"
//, "dz.wiktionary.org"
//, "ext.wikipedia.org"
//, "ff.wikipedia.org"
//, "fj.wikipedia.org"
//, "fj.wiktionary.org"
//, "fo.wikipedia.org"
//, "fo.wiktionary.org"
//, "frp.wikipedia.org"
//, "frr.wikipedia.org"
//, "fur.wikipedia.org"
//, "fy.wikipedia.org"
//, "fy.wiktionary.org"
//, "fy.wikibooks.org"
//, "ga.wikipedia.org"
//, "ga.wiktionary.org"
//, "gd.wikipedia.org"
//, "gd.wiktionary.org"
//, "gn.wikipedia.org"
//, "gn.wiktionary.org"
//, "got.wikipedia.org"
//, "gu.wikipedia.org"
//, "gu.wiktionary.org"
//, "gv.wikipedia.org"
//, "gv.wiktionary.org"
//, "ha.wikipedia.org"
//, "ha.wiktionary.org"
//, "hak.wikipedia.org"
//, "haw.wikipedia.org"
//, "hsb.wikipedia.org"
//, "ht.wikipedia.org"
//, "hy.wikipedia.org"
//, "hy.wiktionary.org"
//, "hy.wikisource.org"
//, "hy.wikibooks.org"
//, "ie.wikipedia.org"
//, "ie.wiktionary.org"
//, "ie.wikibooks.org"
//, "ig.wikipedia.org"
//, "ik.wikipedia.org"
//, "ik.wiktionary.org"
//, "ilo.wikipedia.org"
//, "io.wikipedia.org"
//, "io.wiktionary.org"
//, "is.wikipedia.org"
//, "is.wiktionary.org"
//, "is.wikisource.org"
//, "is.wikibooks.org"
//, "iu.wikipedia.org"
//, "iu.wiktionary.org"
//, "jbo.wikipedia.org"
//, "jbo.wiktionary.org"
//, "jv.wikipedia.org"
//, "jv.wiktionary.org"
//, "ka.wikipedia.org"
//, "ka.wiktionary.org"
//, "ka.wikibooks.org"
//, "kg.wikipedia.org"
//, "ki.wikipedia.org"
//, "kl.wikipedia.org"
//, "kl.wiktionary.org"
//, "km.wikipedia.org"
//, "km.wiktionary.org"
//, "km.wikibooks.org"
//, "kn.wikipedia.org"
//, "kn.wiktionary.org"
//, "kn.wikibooks.org"
//, "ks.wikipedia.org"
//, "ks.wiktionary.org"
//, "ksh.wikipedia.org"
//, "ku.wikipedia.org"
//, "ku.wiktionary.org"
//, "ku.wikibooks.org"
//, "kv.wikipedia.org"
//, "kw.wikipedia.org"
//, "kw.wiktionary.org"
//, "ky.wikipedia.org"
//, "ky.wiktionary.org"
//, "ky.wikibooks.org"
//, "lad.wikipedia.org"
//, "lb.wikipedia.org"
//, "lb.wiktionary.org"
//, "lg.wikipedia.org"
//, "li.wikipedia.org"
//, "li.wiktionary.org"
//, "li.wikisource.org"
//, "lij.wikipedia.org"
//, "lmo.wikipedia.org"
//, "ln.wikipedia.org"
//, "ln.wiktionary.org"
//, "lo.wikipedia.org"
//, "lo.wiktionary.org"
//, "lv.wikipedia.org"
//, "lv.wiktionary.org"
//, "lv.wikibooks.org"
//, "mg.wikipedia.org"
//, "mg.wiktionary.org"
//, "mg.wikibooks.org"
//, "mh.wikipedia.org"
//, "mh.wiktionary.org"
//, "mi.wikipedia.org"
//, "mi.wiktionary.org"
//, "mk.wikipedia.org"
//, "mk.wiktionary.org"
//, "mk.wikibooks.org"
//, "ml.wikipedia.org"
//, "ml.wiktionary.org"
//, "ml.wikisource.org"
//, "ml.wikibooks.org"
//, "mn.wikipedia.org"
//, "mn.wiktionary.org"
//, "mo.wiktionary.org"
//, "mr.wikipedia.org"
//, "mr.wiktionary.org"
//, "mr.wikibooks.org"
//, "mt.wikipedia.org"
//, "mt.wiktionary.org"
//, "my.wikipedia.org"
//, "my.wiktionary.org"
//, "na.wikipedia.org"
//, "na.wiktionary.org"
//, "na.wikibooks.org"
//, "nah.wikipedia.org"
//, "nah.wiktionary.org"
//, "nap.wikipedia.org"
//, "nds.wikipedia.org"
//, "nds.wiktionary.org"
//, "nds.wikibooks.org"
//, "ne.wikipedia.org"
//, "ne.wiktionary.org"
//, "ne.wikibooks.org"
//, "new.wikipedia.org"
//, "nrm.wikipedia.org"
//, "nv.wikipedia.org"
//, "ny.wikipedia.org"
//, "oc.wikipedia.org"
//, "oc.wiktionary.org"
//, "oc.wikibooks.org"
//, "om.wikipedia.org"
//, "om.wiktionary.org"
//, "or.wikipedia.org"
//, "or.wiktionary.org"
//, "os.wikipedia.org"
//, "pa.wikipedia.org"
//, "pa.wiktionary.org"
//, "pa.wikibooks.org"
//, "pag.wikipedia.org"
//, "pam.wikipedia.org"
//, "pap.wikipedia.org"
//, "pdc.wikipedia.org"
//, "pi.wikipedia.org"
//, "pi.wiktionary.org"
//, "pih.wikipedia.org"
//, "pms.wikipedia.org"
//, "ps.wikipedia.org"
//, "ps.wiktionary.org"
//, "ps.wikibooks.org"
//, "qu.wikipedia.org"
//, "qu.wiktionary.org"
//, "qu.wikibooks.org"
//, "rm.wikipedia.org"
//, "rm.wiktionary.org"
//, "rmy.wikipedia.org"
//, "rn.wikipedia.org"
//, "rn.wiktionary.org"
//, "rw.wikipedia.org"
//, "rw.wiktionary.org"
//, "sa.wikipedia.org"
//, "sa.wiktionary.org"
//, "sc.wikipedia.org"
//, "sc.wiktionary.org"
//, "scn.wikipedia.org"
//, "scn.wiktionary.org"
//, "sco.wikipedia.org"
//, "sd.wikipedia.org"
//, "sd.wiktionary.org"
//, "sd.wikinews.org"
//, "se.wikipedia.org"
//, "sg.wikipedia.org"
//, "sg.wiktionary.org"
//, "si.wikipedia.org"
//, "si.wiktionary.org"
//, "sm.wikipedia.org"
//, "sm.wiktionary.org"
//, "sn.wikipedia.org"
//, "sn.wiktionary.org"
//, "so.wikipedia.org"
//, "so.wiktionary.org"
//, "sq.wikipedia.org"
//, "sq.wiktionary.org"
//, "sq.wikibooks.org"
//, "sq.wikinews.org"
//, "ss.wikipedia.org"
//, "ss.wiktionary.org"
//, "st.wikipedia.org"
//, "st.wiktionary.org"
//, "su.wikipedia.org"
//, "su.wiktionary.org"
//, "su.wikibooks.org"
//, "sw.wikipedia.org"
//, "sw.wiktionary.org"
//, "ta.wikipedia.org"
//, "ta.wiktionary.org"
//, "ta.wikibooks.org"
//, "ta.wikinews.org"
//, "te.wikipedia.org"
//, "te.wiktionary.org"
//, "te.wikibooks.org"
//, "tet.wikipedia.org"
//, "tg.wikipedia.org"
//, "tg.wiktionary.org"
//, "tg.wikibooks.org"
//, "ti.wikipedia.org"
//, "ti.wiktionary.org"
//, "tk.wikipedia.org"
//, "tk.wiktionary.org"
//, "tk.wikibooks.org"
//, "tl.wikipedia.org"
//, "tl.wiktionary.org"
//, "tl.wikibooks.org"
//, "tn.wikipedia.org"
//, "tn.wiktionary.org"
//, "to.wikipedia.org"
//, "to.wiktionary.org"
//, "tpi.wikipedia.org"
//, "tpi.wiktionary.org"
//, "ts.wikipedia.org"
//, "ts.wiktionary.org"
//, "tt.wikipedia.org"
//, "tt.wiktionary.org"
//, "tt.wikibooks.org"
//, "tum.wikipedia.org"
//, "tw.wikipedia.org"
//, "tw.wiktionary.org"
//, "ty.wikipedia.org"
//, "udm.wikipedia.org"
//, "ug.wikipedia.org"
//, "ug.wiktionary.org"
//, "ur.wikipedia.org"
//, "ur.wiktionary.org"
//, "ur.wikibooks.org"
//, "uz.wikipedia.org"
//, "uz.wiktionary.org"
//, "uz.wikibooks.org"
//, "ve.wikipedia.org"
//, "vec.wikipedia.org"
//, "vls.wikipedia.org"
//, "wa.wikipedia.org"
//, "wa.wiktionary.org"
//, "war.wikipedia.org"
//, "wo.wikipedia.org"
//, "wo.wiktionary.org"
//, "wuu.wikipedia.org"
//, "xal.wikipedia.org"
//, "xh.wikipedia.org"
//, "xh.wiktionary.org"
//, "yi.wikipedia.org"
//, "yi.wiktionary.org"
//, "yi.wikisource.org"
//, "yo.wikipedia.org"
//, "yo.wiktionary.org"
//, "za.wikipedia.org"
//, "za.wiktionary.org"
//, "zea.wikipedia.org"
//, "zu.wikipedia.org"
//, "zu.wiktionary.org"
//, "pl.wikiquote.org"
//, "en.wikiquote.org"
//, "it.wikiquote.org"
//, "ru.wikiquote.org"
//, "fr.wikiquote.org"
//, "de.wikiquote.org"
//, "pt.wikiquote.org"
//, "es.wikiquote.org"
//, "cs.wikiquote.org"
//, "sk.wikiquote.org"
//, "bg.wikiquote.org"
//, "bs.wikiquote.org"
//, "tr.wikiquote.org"
//, "sl.wikiquote.org"
//, "he.wikiquote.org"
//, "uk.wikiquote.org"
//, "lt.wikiquote.org"
//, "eo.wikiquote.org"
//, "el.wikiquote.org"
//, "id.wikiquote.org"
//, "zh.wikiquote.org"
//, "fa.wikiquote.org"
//, "hu.wikiquote.org"
//, "fi.wikiquote.org"
//, "sv.wikiquote.org"
//, "nl.wikiquote.org"
//, "li.wikiquote.org"
//, "ca.wikiquote.org"
//, "no.wikiquote.org"
//, "nn.wikiquote.org"
//, "hr.wikiquote.org"
//, "ja.wikiquote.org"
//, "az.wikiquote.org"
//, "hy.wikiquote.org"
//, "simple.wikiquote.org"
//, "ar.wikiquote.org"
//, "et.wikiquote.org"
//, "ko.wikiquote.org"
//, "ml.wikiquote.org"
//, "cy.wikiquote.org"
//, "ka.wikiquote.org"
//, "gl.wikiquote.org"
//, "sr.wikiquote.org"
//, "ro.wikiquote.org"
//, "ku.wikiquote.org"
//, "th.wikiquote.org"
//, "te.wikiquote.org"
//, "is.wikiquote.org"
//, "eu.wikiquote.org"
//, "da.wikiquote.org"
//, "af.wikiquote.org"
//, "vi.wikiquote.org"
//, "sq.wikiquote.org"
//, "ta.wikiquote.org"
//, "hi.wikiquote.org"
//, "la.wikiquote.org"
//, "be.wikiquote.org"
//, "br.wikiquote.org"
//, "mr.wikiquote.org"
//, "ast.wikiquote.org"
//, "uz.wikiquote.org"
//, "ang.wikiquote.org"
//, "ur.wikiquote.org"
//, "gu.wikiquote.org"
//, "su.wikiquote.org"
//, "lb.wikiquote.org"
//, "kn.wikiquote.org"
//, "wo.wikiquote.org"
//, "ky.wikiquote.org"
//, "kk.wikiquote.org"
//, "tt.wikiquote.org"
//, "am.wikiquote.org"
//, "co.wikiquote.org"
//, "qu.wikiquote.org"
//, "ug.wikiquote.org"
//, "bm.wikiquote.org"
//, "kw.wikiquote.org"
//, "ga.wikiquote.org"
//, "tk.wikiquote.org"
//, "vo.wikiquote.org"
//, "na.wikiquote.org"
//, "za.wikiquote.org"
//, "als.wikiquote.org"
//, "ks.wikiquote.org"
//, "cr.wikiquote.org"
//, "nds.wikiquote.org"
//, "kr.wikiquote.org"
//, "zh-classical.wikipedia.org"
//, "zh-min-nan.wiktionary.org"
//, "zh-min-nan.wikipedia.org"
//, "zh-yue.wikipedia.org"
//, "bat-smg.wikipedia.org"
//, "be-x-old.wikipedia.org"
//, "cbk-zam.wikipedia.org"
//, "fiu-vro.wikipedia.org"
//, "map-bms.wikipedia.org"
//, "roa-rup.wikipedia.org"
//, "roa-rup.wiktionary.org"
//};
}
//, "als.wikisource.org"
//, "als.wikinews.org"
//, "nds.wikinews.org"
//, "ba.wiktionary.org"
//, "tokipona.wikibooks.org"
