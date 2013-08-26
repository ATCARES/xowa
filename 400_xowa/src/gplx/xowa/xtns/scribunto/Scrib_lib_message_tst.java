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
package gplx.xowa.xtns.scribunto; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import org.junit.*;
public class Scrib_lib_message_tst {
	@Before public void init() {
		fxt.Clear();
		fxt.Init_page("{{#invoke:Mod_0|Func_0}}");
		lib = fxt.Engine().Lib_message();
	}	Scrib_pf_invoke_fxt fxt = new Scrib_pf_invoke_fxt(); Scrib_lib lib;
	@Test   public void ToStr() {
		fxt.Test_lib_proc(lib, Scrib_lib_message.Invk_toString, Object_.Ary("parse"				, keys_ary("sun"))							, "Sun");
		fxt.Test_lib_proc(lib, Scrib_lib_message.Invk_toString, Object_.Ary("parse"				, keys_ary("sunx"))							, "&lt;sunx&gt;");
		fxt.Test_lib_proc(lib, Scrib_lib_message.Invk_toString, Object_.Ary("parseAsBlock"		, keys_ary("sun"))							, "<p>Sun</p>");
		fxt.Test_lib_proc(lib, Scrib_lib_message.Invk_toString, Object_.Ary("escaped"			, keys_ary("pfunc_expr_invalid_argument"))	, "Invalid argument for ~{0}: &lt; -1 or &gt; 1");
		fxt.Test_lib_proc(lib, Scrib_lib_message.Invk_toString, Object_.Ary("parse"				, keys_ary_arg("redirectedfrom", "A"))		, "(Redirected from A)");
		Xol_lang lang = fxt.Parser_fxt().Wiki().App().Lang_mgr().Get_by_key_or_new(ByteAry_.new_ascii_("fr"));
		Init_msg(lang, "sun", "dim");
		fxt.Test_lib_proc(lib, Scrib_lib_message.Invk_toString, Object_.Ary("parse"				, keys_ary_lang("sun", "fr"))				, "dim");
	}
	@Test  public void ToStr_rawMessage() {
		fxt.Test_lib_proc(lib, Scrib_lib_message.Invk_toString, Object_.Ary("parse"				, Scrib_kv_utl.flat_many_("rawMessage", "$1", "params", KeyVal_.Ary(KeyVal_.int_(1, "abc")))), "abc");
	}
	@Test   public void Check() {
		fxt.Test_lib_proc(lib, Scrib_lib_message.Invk_check, Object_.Ary("exists"				, keys_ary("sun"))							, "true");
		fxt.Test_lib_proc(lib, Scrib_lib_message.Invk_check, Object_.Ary("exists"				, keys_ary("sunx"))							, "false");
		fxt.Test_lib_proc(lib, Scrib_lib_message.Invk_check, Object_.Ary("isBlank"				, keys_ary("sun"))							, "false");
		Init_msg("blank", "");			
		fxt.Test_lib_proc(lib, Scrib_lib_message.Invk_check, Object_.Ary("isBlank"				, keys_ary("blank"))						, "true");
		Init_msg("disabled", "-");			
		fxt.Test_lib_proc(lib, Scrib_lib_message.Invk_check, Object_.Ary("isDisabled"			, keys_ary("sun"))							, "false");
		fxt.Test_lib_proc(lib, Scrib_lib_message.Invk_check, Object_.Ary("isDisabled"			, keys_ary("blank"))						, "true");
		fxt.Test_lib_proc(lib, Scrib_lib_message.Invk_check, Object_.Ary("isDisabled"			, keys_ary("disabled"))						, "true");
		fxt.Test_lib_proc(lib, Scrib_lib_message.Invk_check, Object_.Ary("isBlank"				, keys_ary("disabled"))						, "false");
	}
	@Test  public void Init_message_for_lang() {
		fxt.Test_lib_proc(lib, Scrib_lib_message.Invk_init_message_for_lang, Object_.Ary_empty						, "lang=en");
	}
	void Init_msg(String key, String val) {Init_msg(fxt.Engine().Wiki().Lang(), key, val);}
	void Init_msg(Xol_lang lang, String key, String val) {
		lang.Msg_mgr().Itm_by_key_or_new(ByteAry_.new_ascii_(key)).Atrs_set(ByteAry_.new_ascii_(val), false, false);
	}
	KeyVal[] keys_ary(String msg_key) {return keys_ary(msg_key, null, null);}
	KeyVal[] keys_ary_arg(String msg_key, String arg) {return keys_ary(msg_key, null, arg);}
	KeyVal[] keys_ary_lang(String msg_key, String lang) {return keys_ary(msg_key, lang, null);}
	KeyVal[] keys_ary(String msg_key, String lang, String arg) {
		boolean arg_exists = arg != null;
		boolean lang_exists = lang != null;
		int idx = 0;
		KeyVal[] rv = new KeyVal[1 + (arg_exists ? 1 : 0) + (lang_exists ? 1 : 0)];
		rv[0] = KeyVal_.new_("keys", KeyVal_.Ary(KeyVal_.int_(1, msg_key)));
		if (arg_exists)
			rv[++idx] = KeyVal_.new_("params", KeyVal_.Ary(KeyVal_.int_(1, arg)));
		if (lang_exists)
			rv[++idx] = KeyVal_.new_("lang", lang);
		return rv;
	}
}	