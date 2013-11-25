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
package gplx.xowa.bldrs.files; import gplx.*; import gplx.xowa.*; import gplx.xowa.bldrs.*;
import org.junit.*;
import gplx.stores.*; import gplx.xowa.files.*;
public class Xob_xfer_temp_itm_tst {
	private Xob_xfer_temp_itm_fxt fxt = new Xob_xfer_temp_itm_fxt();
	@Before public void init() {fxt.Reset();}
	@Test   public void Pass()				{fxt.Test_pass().Test_itm_chk_fail_id_none();}
	@Test   public void Missing_orig()		{fxt.Test_fail(Xob_xfer_temp_itm.Chk_tid_orig_page_id_is_null		, KeyVal_.new_(Xob_orig_regy_tbl.Fld_orig_page_id, null));}
	@Test   public void File_is_audio()		{fxt.Test_fail(Xob_xfer_temp_itm.Chk_tid_orig_media_type_is_audio	, KeyVal_.new_(Xob_orig_regy_tbl.Fld_orig_media_type, Xof_media_type.Name_audio));}
	@Test   public void File_is_midi()		{fxt.Test_fail(Xob_xfer_temp_itm.Chk_tid_orig_media_type_is_audio	, KeyVal_.new_(Xob_lnki_regy_tbl.Fld_lnki_ext, Xof_ext_.Id_mid));}
	@Test   public void Ogg_to_ogv() {
		fxt.Test_bgn
		(	KeyVal_.new_(Xob_lnki_regy_tbl.Fld_lnki_ext			, Xof_ext_.Id_ogg)
		,	KeyVal_.new_(Xob_orig_regy_tbl.Fld_orig_media_type	, Xof_media_type.Name_video)
		);
		fxt.Test_lnki_ext_id(Xof_ext_.Id_ogv);						// confirm ext is ogv
		fxt.Test_itm_chk_fail_id(Xob_xfer_temp_itm.Chk_tid_none);	// confirm no err
	}
	@Test   public void Redirect_src_is_empty() {	// orig_cmd sets all direct files to have "orig_join" == "lnki_ttl"
		fxt.Test_bgn
		(	KeyVal_.new_(Xob_orig_regy_tbl.Fld_orig_file_ttl	, "A.png")
		,	KeyVal_.new_(Xob_orig_regy_tbl.Fld_lnki_ttl			, "A.png")
		);
		fxt.Test_lnki_redirect_src("");								// confirm redirect_src set to ""
	}
	@Test   public void Redirect_src_has_val() {	// orig_cmd sets all redirect files to have "orig_join" = redirect and "lnki_ttl" as orig; EX: A.png redirects to B.png; orig_join will be B.png (the actual image) and redirect_src will be A.png (the original lnki)
		fxt.Test_bgn
		(	KeyVal_.new_(Xob_orig_regy_tbl.Fld_orig_file_ttl	, "B.png")
		,	KeyVal_.new_(Xob_orig_regy_tbl.Fld_lnki_ttl			, "A.png")
		);
		fxt.Test_lnki_redirect_src("A.png");							// confirm redirect_src set to ""
	}
	@Test   public void Redirect_should_take_trg_ext() {// if "A.png" redirects to "B.jpg", ext_id should be ".jpg" (the actual file) not ".png (lnki_ext_id)
		fxt.Test_bgn
		(	KeyVal_.new_(Xob_orig_regy_tbl.Fld_orig_file_ttl	, "B.jpg")
		,	KeyVal_.new_(Xob_orig_regy_tbl.Fld_lnki_ttl			, "A.png")
		,	KeyVal_.new_(Xob_lnki_regy_tbl.Fld_lnki_ext			, Xof_ext_.Id_png)	// .png b/c A.png
		);
		fxt.Test_lnki_ext_id(Xof_ext_.Id_jpg);						// confirm ext changed to .jpg
	}
	@Test   public void Thumbtime_should_only_apply_to_video() {// PURPOSE: one image actually had a thumbtime defined; EX: General_Dynamics_F-16_Fighting_Falcon; [[File:Crash.arp.600pix.jpg|thumb|thumbtime=2]]
		fxt.Test_bgn
		(	KeyVal_.new_(Xob_lnki_regy_tbl.Fld_lnki_thumbtime, (double)3)	// define thumbtime; note that file is image
		);
		fxt.Test_lnki_thumbtime(Xop_lnki_tkn.Thumbtime_null);			// confirm thumbtime is set to null
	}
}
class Xob_xfer_temp_itm_fxt {
	private Xob_xfer_temp_itm itm = new Xob_xfer_temp_itm();
	private Xof_img_size img_size = new Xof_img_size();
	private DataRdr_mem rdr;
	private GfoNde nde;
	public static String[] Flds = new String[] 
	{ Xob_lnki_regy_tbl.Fld_lnki_ext
	, Xob_orig_regy_tbl.Fld_orig_media_type
	, Xob_lnki_regy_tbl.Fld_lnki_id
	, Xob_orig_regy_tbl.Fld_orig_repo
	, Xob_orig_regy_tbl.Fld_orig_file_ttl
	, Xob_orig_regy_tbl.Fld_lnki_ttl
	, Xob_lnki_regy_tbl.Fld_lnki_type
	, Xob_lnki_regy_tbl.Fld_lnki_w
	, Xob_lnki_regy_tbl.Fld_lnki_h
	, Xob_lnki_regy_tbl.Fld_lnki_count
	, Xob_lnki_regy_tbl.Fld_lnki_page_id
	, Xob_orig_regy_tbl.Fld_orig_w
	, Xob_orig_regy_tbl.Fld_orig_h
	, Xob_orig_regy_tbl.Fld_orig_page_id
	, Xob_lnki_regy_tbl.Fld_lnki_upright
	, Xob_lnki_regy_tbl.Fld_lnki_thumbtime
	}
	;
	public void Reset() {
		itm.Clear();
		img_size.Clear();
	}
	public Xob_xfer_temp_itm_fxt Init_rdr_image() {
		GfoFldList flds = GfoFldList_.str_(Flds);
		nde = GfoNde_.vals_(flds, Object_.Ary
		( Xof_ext_.Id_jpg, Xof_media_type.Name_bitmap, 1, Xof_repo_itm.Repo_remote
		, "A.png", "A.png", Xop_lnki_type.Id_thumb, 220, 200, 1, 2, 440, 400, 3, (double)-1, (double)-1
		));
		GfoNdeList subs = GfoNdeList_.new_();
		subs.Add(nde);
		GfoNde root = GfoNde_.root_(nde);
		rdr = DataRdr_mem.new_(root, flds, subs);
		rdr.MoveNextPeer();
		return this;
	}
	public Xob_xfer_temp_itm_fxt Init_rdr(String key, Object val) {
		nde.Write(key, val);
		return this;
	}
	public Xob_xfer_temp_itm_fxt Test_pass() {return Test_bgn();}
	public Xob_xfer_temp_itm_fxt Test_fail(byte fail_tid, KeyVal... kvs) {
		Test_bgn(kvs);
		this.Test_itm_chk_fail_id(fail_tid);
		return this;
	}
	public Xob_xfer_temp_itm_fxt Test_bgn(KeyVal... kvs) {
		Init_rdr_image();
		int len = kvs.length;
		for (int i = 0; i < len; i++) {
			KeyVal kv = kvs[i];
			Init_rdr(kv.Key(), kv.Val());
		}
		this.Exec_load();
		this.Exec_chk();
		return this;
	}
	public Xob_xfer_temp_itm_fxt Test_atr(String key, Object val) {
		Tfds.Eq(rdr.Read(key), val);
		return this;
	}
	public Xob_xfer_temp_itm_fxt Exec_load() {
		itm.Load(rdr);
		return this;
	}
	public Xob_xfer_temp_itm_fxt Exec_chk() {
		itm.Chk(img_size);
		return this;
	}
	public Xob_xfer_temp_itm_fxt Test_lnki_ext_id(int expd) {Tfds.Eq(expd, itm.Lnki_ext()); return this;}
	public Xob_xfer_temp_itm_fxt Test_lnki_thumbtime(double expd) {Tfds.Eq(expd, itm.Lnki_thumbtime()); return this;}
	public Xob_xfer_temp_itm_fxt Test_lnki_redirect_src(String expd) {Tfds.Eq(expd, itm.Redirect_src()); return this;}
	public Xob_xfer_temp_itm_fxt Test_itm_chk_fail_id_none() {return Test_itm_chk_fail_id(Xob_xfer_temp_itm.Chk_tid_none);}
	public Xob_xfer_temp_itm_fxt Test_itm_chk_fail_id(byte expd) {
		Tfds.Eq(expd, itm.Chk_tid());
		return this;
	}
}