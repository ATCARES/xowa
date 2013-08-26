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
public class Xow_ns {
	public Xow_ns(int id, byte case_match, byte[] name, boolean alias) {
		this.id = id; this.case_match = case_match; this.alias = alias;
		Name_bry_(name);
	}
	public void Name_bry_(byte[] v) {
		if (id == Xow_ns_.Id_main) {	// NOTE: Main will never prefix titles; EX: "Test" vs "Category:Test"
			this.name_str			= "";
			this.name_bry			= ByteAry_.Empty;
			this.name_db_w_colon	= ByteAry_.Empty;
		}
		else {
			this.name_bry			= v;
			this.name_db_w_colon	= ByteAry_.Add(v, Byte_ascii.Colon);
			this.name_str			= String_.new_utf8_(v);
		}
		this.num_str = Int_.XtoStr_PadBgn(id, 3);
		this.num_bry = ByteAry_.new_ascii_(num_str);
		this.name_enc = Xoa_url_encoder._.Encode(name_bry);
		this.name_txt = ByteAry_.Replace(name_enc, Byte_ascii.Underline, Byte_ascii.Space);
		name_txt_w_colon = ByteAry_.Replace(name_db_w_colon, Byte_ascii.Underline, Byte_ascii.Space);
	}
	public byte[]	Name_bry()				{return name_bry;} private byte[] name_bry;
	public String	Name_str()				{return name_str;} private String name_str;
	public byte[]	Name_txt()				{return name_txt;} private byte[] name_txt;
	public byte[]	Name_enc()				{return name_enc;} private byte[] name_enc;
	public byte[]	Name_db_w_colon()		{return name_db_w_colon;} private byte[] name_db_w_colon;
	public byte[]	Name_txt_w_colon()		{return name_txt_w_colon;} private byte[] name_txt_w_colon;		// PERF: for Xoa_ttl
	public String	Num_str()				{return num_str;} private String num_str;
	public byte[]	Num_bry()				{return num_bry;} private byte[] num_bry;
	public int		Id()					{return id;} private int id;
	public int		Id_subj_id()			{if (id < 0) return id; return Id_talk() ? id - 1	: id;}					// // id< 0: special/media return themself; REF.MW: Namespace.php|getSubject
	public int		Id_talk_id()			{if (id < 0) return id; return Id_talk() ? id		: id + 1;}				// REF.MW: Namespace.php|getTalk
	public int		Id_alt_id()				{if (id < 0) return id; return Id_talk() ? Id_subj_id() : Id_talk_id();}	// REF.MW: Namespace.php|getTalk
	public boolean		Id_subj()				{return !Id_talk();}								// REF.MW: Namespace.php|isMain
	public boolean		Id_talk()				{return id > Xow_ns_.Id_main && id % 2 == 1;}		// REF.MW: Namespace.php|isTalk
	public boolean		Id_file()				{return id == Xow_ns_.Id_file;}
	public boolean		Id_file_or_media()		{return id == Xow_ns_.Id_file || id == Xow_ns_.Id_media;}
	public boolean		Id_tmpl()				{return id == Xow_ns_.Id_template;}
	public boolean		Id_ctg()				{return id == Xow_ns_.Id_category;}
	public boolean		Id_main()				{return id == Xow_ns_.Id_main;}
	public int		Ord()					{return ord;} public void Ord_(int v) {this.ord = v;} private int ord;
	public int		Ord_subj_id()			{if (id < 0) return ord; return Id_talk() ? ord - 1 : ord;}  // id< 0: special/media return themself
	public int		Ord_talk_id()			{if (id < 0) return ord; return Id_talk() ? ord : ord + 1;}
	public byte		Case_match()			{return case_match;} public void Case_match_(byte v) {case_match = v;} private byte case_match;
	public boolean		Is_subpage_enabled()	{return id > Xow_ns_.Id_special;}								// ASSUME: only Special, Media does not have subpages
	public boolean		Is_gender_aware()		{return id == Xow_ns_.Id_user || id == Xow_ns_.Id_user_talk;}	// ASSUME: only User, User_talk are gender aware
	public boolean		Is_capitalized()		{return false;}													// ASSUME: always false (?)
	public boolean		Is_content()			{return id == Xow_ns_.Id_main;}									// ASSUME: only Main
	public boolean		Is_includable()			{return true;}													// ASSUME: always true (be transcluded?)
	public boolean		Is_movable()			{return id > Xow_ns_.Id_special;}								// ASSUME: only Special, Media cannot move (be renamed?)
	public boolean		Alias()					{return alias;} private boolean alias;
	public int		Count()					{return count;} public Xow_ns Count_(int v) {count = v; return this;} private int count;
	public byte[]	Gen_ttl(byte[] page)	{return id == Xow_ns_.Id_main ? page : ByteAry_.Add(name_db_w_colon, page);}
	public boolean Exists() {return exists;} public Xow_ns Exists_(boolean v) {exists = v; return this;} private boolean exists;
	public int Bldr_file_idx() {return bldr_file_idx;} public void Bldr_file_idx_(int v) {bldr_file_idx = v;} private int bldr_file_idx = Bldr_file_idx_heap;
	public static final int Bldr_file_idx_heap = -1;
}