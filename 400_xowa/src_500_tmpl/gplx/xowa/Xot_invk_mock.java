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
public class Xot_invk_mock implements Xot_invk {
	public Xot_invk_mock(int idx_adj) {this.idx_adj = idx_adj;} private int idx_adj; // SEE NOTE_1:
	public boolean Root_frame() {return false;}
	public int Src_bgn() {return -1;}
	public int Src_end() {return -1;}
	public Arg_nde_tkn Name_tkn() {return Arg_nde_tkn.Null;}
	public int Args_len() {return args.Count() + idx_adj;} OrderedHash args = OrderedHash_.new_bry_();
	public Arg_nde_tkn Args_get_by_idx(int i) {return (Arg_nde_tkn)args.FetchAt(i - idx_adj);}
	public Arg_nde_tkn Args_eval_by_idx(byte[] src, int idx) {// DUPE:MW_ARG_RETRIEVE
		int cur = 0, list_len = args.Count(); 
		if (idx >= list_len) return null;
		for (int i = 0; i < list_len; i++) {	// iterate over list to find nth *non-keyd* arg; SEE:NOTE_1
			Arg_nde_tkn nde = (Arg_nde_tkn)args.FetchAt(i);
			if (nde.KeyTkn_exists()) {
				int key_int = ByteAry_.XtoIntOr(nde.Key_tkn().Dat_ary(), -1);
				if (key_int == -1)
					continue;
				else {	// key is numeric
					if (key_int + idx_adj - 1 == idx) {
						return nde;						
					}
					else {
						continue;
					}
				}
			}
			if ((cur + idx_adj) == idx) return nde;
			else ++cur;
		}
		return Args_get_by_key(src, ByteAry_.XtoStrBytesByInt(idx + 1, 1));
	}
	public Arg_nde_tkn Args_get_by_key(byte[] src, byte[] key) {return (Arg_nde_tkn)args.Fetch(key);}
	public static Xot_invk_mock new_(KeyVal... args) {return new_(1, args);}
	public static Xot_invk_mock new_(int idx_adj, KeyVal... args) {
		Xot_invk_mock rv = new Xot_invk_mock(idx_adj);
		int len = args.length;
		for (int i = 0; i < len; i++) {
			KeyVal arg = args[i];
			Object arg_key_obj = arg.Key_as_obj();
			String arg_key = arg.Key();
			boolean arg_key_is_int = ClassAdp_.Eq_typeSafe(arg_key_obj, Int_.ClassOf);
			byte[] arg_key_bry = ByteAry_.new_utf8_(arg_key);
//				if (!rv.args.Has(arg_key_bry))	// ignore duplicates; EX:{{Template1|key1=a|key2=b|key1=c}}
			rv.args.AddReplace(arg_key_bry, new Arg_nde_tkn_mock(arg_key_is_int, arg_key, arg.Val_to_str_or_empty()));
		}
		return rv;
	}
	public static final Xot_invk_mock Null = new Xot_invk_mock(1);
}
class Arg_nde_tkn_mock extends Arg_nde_tkn {	public Arg_nde_tkn_mock(boolean arg_key_is_int, String k, String v) {
		this.key_exists = !arg_key_is_int;
		if (key_exists)
			this.Key_tkn_(new Arg_itm_tkn_mock(k));
		this.Val_tkn_(new Arg_itm_tkn_mock(v));
	}
	@Override public boolean KeyTkn_exists() {return key_exists;} private boolean key_exists;
}
class Arg_itm_tkn_mock extends Arg_itm_tkn_base {
	public Arg_itm_tkn_mock(String v) {
		byte[] dat_ary = ByteAry_.new_utf8_(v);
		this.Subs_add(new Xop_bry_tkn(-1, -1, dat_ary));
		this.Dat_ary_(dat_ary);
		this.val = v;
	}	String val;
	@Override public boolean Tmpl_evaluate(Xop_ctx ctx, byte[] src, Xot_invk caller, ByteAryBfr bfr) {bfr.Add_str(val); return true;}
}
/*
NOTE_1: Xot_invk_mock is being used as a container for two functions
(1) As a substitute for an Invk_tkn; EX: {{#invoke:Mod|Func|arg_1|arg_2}}
. in this case, idx_adj is 1 b/c args will always be 1-based
. EX: Eval_by_idx(1) should return "arg_1". This would be list[0]
. said another way, requested_idx - idx_adj = list_idx; or 1 - 1 = 0
. Hence, 1 is the idx_adj; 
(2) As a substitute for Xot_defn_tmpl_.CopyNew; which occurs in ExpandTemplate
. in this case, idx_adj is 0 b/c args are 0-based
. note that Xot_defn_tmpl_ creates a temporary Object, and its args are 0 based (it doesn't emulate the list[0] for the func_name
*/