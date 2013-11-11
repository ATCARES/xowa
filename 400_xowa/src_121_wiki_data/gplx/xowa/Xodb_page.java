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
public class Xodb_page implements Xobl_data_itm {
	public Xodb_page() {this.Clear();}
	public int Id() {return id;} public Xodb_page Id_(int v) {id = v; id_val = null; return this;} private int id;
	public IntVal Id_val() {if (id_val == null) id_val = IntVal.new_(id); return id_val;} IntVal id_val;
	public int Ns_id() {return ns_id;} public Xodb_page Ns_id_(int v) {ns_id = v; return this;} private int ns_id;
	public byte[] Ttl_wo_ns() {return ttl_wo_ns;} public Xodb_page Ttl_wo_ns_(byte[] v) {ttl_wo_ns = v; return this;} private byte[] ttl_wo_ns;
	public boolean Type_redirect() {return type_redirect;} public Xodb_page Type_redirect_(boolean v) {type_redirect = v; return this;} private boolean type_redirect;
	public int Text_len() {return text_len;} public Xodb_page Text_len_(int v) {text_len = v; return this;} private int text_len;
	public byte[] Text() {return text;} public Xodb_page Text_(byte[] v) {text = v; if (v != null) text_len = v.length; return this;} private byte[] text;
	public boolean Exists() {return exists;} public Xodb_page Exists_(boolean v) {exists = v; return this;} private boolean exists;
	public int Db_file_idx() {return db_file_idx;} public Xodb_page Db_file_idx_(int v) {db_file_idx = v; return this;} private int db_file_idx;
	public int Db_row_idx() {return db_row_idx;} public Xodb_page Db_row_idx_(int v) {db_row_idx = v; return this;} private int db_row_idx;
	public DateAdp Modified_on() {return modified_on;} public Xodb_page Modified_on_(DateAdp v) {modified_on = v; return this;} DateAdp modified_on;
	public Xow_ns Ns() {return ns;} private Xow_ns ns;
	public Object Xtn() {return xtn;} public Xodb_page Xtn_(Object v) {this.xtn = v; return this;} Object xtn;
	public byte[] Ttl_w_ns() {return ttl_w_ns;} private byte[] ttl_w_ns;
	public Xodb_page Ttl_(Xow_ns ns, byte[] ttl_wo_ns) {
		this.ns = ns;
		ns_id = ns.Id();
		this.ttl_wo_ns = ttl_wo_ns;
		this.ttl_w_ns = ns.Gen_ttl(ttl_wo_ns);
		return this;
	}
	public Xodb_page Ttl_(Xoa_ttl ttl) {
		ttl_w_ns = ttl.Full_txt();
		ttl_wo_ns = ttl.Page_db();
		ns = ttl.Ns();
		ns_id = ns.Id();
		return this;
	}
	public Xodb_page Ttl_(byte[] v, Xow_ns_mgr ns_mgr) {
		ttl_w_ns = v;
		Object o = ns_mgr.Trie_match_colon(v, 0, v.length, colon_pos_tmp);
		if (o == null)	{
			ns = ns_mgr.Ns_main();
			ttl_wo_ns = v;
		}
		else			{
			ns = (Xow_ns)o;
			ttl_wo_ns = ByteAry_.Mid(v, colon_pos_tmp.Val() + 1, v.length);
		}
		ns_id = ns.Id();
		return this;
	}	static final IntRef colon_pos_tmp = IntRef.neg1_();
	public void Clear() {
		id = Id_null; text_len = 0;	// text_len should be 0 b/c text defaults to 0;
		db_file_idx = db_row_idx = 0; // default to 0, b/c some tests do not set and will fail at -1
		ns_id = Int_.MinValue;
		ttl_w_ns = ttl_wo_ns = null; text = ByteAry_.Empty;	// default to Ary_empty for entries that have <text />
		ns = null;
		type_redirect = exists = false;
		modified_on = DateAdp_.MinValue;
		id_val = null;
	}
	public void Copy(Xodb_page orig) {
		this.id = orig.id;
		this.text_len = orig.text_len;
		this.db_file_idx = orig.db_file_idx;
		this.db_row_idx = orig.db_row_idx;
		this.ns_id = orig.ns_id;
		this.ttl_w_ns = orig.ttl_w_ns;
		this.ttl_wo_ns = orig.ttl_wo_ns;
		this.text = orig.text;
		this.ns = orig.ns;
		this.type_redirect = orig.type_redirect;
		this.exists = orig.exists;
		this.modified_on = orig.modified_on;
		this.id_val = null;
	}
	public Xodb_page Set_all_(int id, int db_file_idx, int db_row_idx, boolean redirect, int text_len, byte[] ttl_wo_ns) {
		this.id = id; this.db_file_idx = db_file_idx; this.db_row_idx = db_row_idx; this.type_redirect = redirect; this.text_len = text_len; this.ttl_wo_ns = ttl_wo_ns;
		id_val = null;
		return this;
	}
	public void Srl_save(ByteAryBfr bfr) {Xodb_page_.Txt_id_save(bfr, this);}
	public static Xodb_page tmp_() {return new Xodb_page();}
	public static Xodb_page srch_(int id, int text_len) {return new Xodb_page().Id_(id).Text_len_(text_len);}
	public static final Xodb_page[] Ary_empty = new Xodb_page[0];
	public static final int Timestamp_null = 0;
	public static final int Id_null = -1;
}
