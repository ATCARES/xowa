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
package gplx;
public class ErrMsgWtr {
	public String Message_gplx(Exception thrown) {
		Err err = Err.convert_(thrown);	// convert thrown to Err to make rest of class easier
		Err[] innerAry = InnerAsAry(err);
		StringBldr sb = StringBldr.new_();
		WriteError(innerAry, sb, 0);
		WriteInner(innerAry, sb);
		WriteStack(innerAry, sb);
		return sb.XtoStr();
	}
	public String Message_gplx_brief(Exception thrown) {
		Err err = Err.convert_(thrown);	// convert thrown to Err to make rest of proc easier
		StringBldr sb = StringBldr.new_();
		sb.Add(err.Hdr());
		if (err.Args().Count() > 0) sb.Add(" --");
		for (Object kvo : err.Args()) {
			KeyVal kv = (KeyVal)kvo;
			String key = kv.Key(), val = kv.Val_to_str_or_empty();
			sb.Add_fmt(" {0}='{1}'", key, val);
		}
		sb.Add_fmt(" [{0}]", err.Key());
		return sb.XtoStr();
	}
	void WriteInner(Err[] errAry, StringBldr sb) {
		int len = Array_.Len(errAry); if (len <= 1) return;	// no inners; return;
		for (int i = 1; i < len; i++)
			WriteError(errAry, sb, i);
	}
	void WriteError(Err[] errAry, StringBldr sb, int i) {
		Err err = errAry[i];
		String msg = err.Hdr();
		String typ = String_.Eq(err.Key(), "") ? "" : String_.Concat(" <", err.Key(), ">");
		boolean onlyOne = errAry.length == 1;
		String idxStr = onlyOne ? "" : Int_.XtoStr(i);
		sb.Add(idxStr).Add("\t").Add(msg).Add(typ).Add_line();	// ex: "	@count must be > 0 <gplx.arg>"
		WriteKeyValAry(sb, err.Args());
		sb.Add("\t").Add(err.Proc().SignatureRaw()).Add_line();
//			WriteKeyValAry(sb, err.ProcArgs());
	}
	void WriteKeyValAry(StringBldr sb, ListAdp ary) {
		// calc keyMax for valIndentLen
		int keyMax = 0;
		for (Object o : ary) {
			KeyVal kv = (KeyVal)o;
			int keyLen = String_.Len(kv.Key());
			if (keyLen > keyMax) keyMax = keyLen + 1; // +1 to guarantee one space between key and val
		}
		if (keyMax < 8)keyMax = 8;	// separate by at least 8 chars
		for (Object o : ary) {
			KeyVal kv = (KeyVal)o;
			String key = kv.Key(); int keyLen = String_.Len(key);
			String valIndent = String_.Repeat(" ", keyMax - keyLen);
			sb.Add("\t\t@").Add(key).Add(valIndent).Add(kv.Val_to_str_or_empty()).Add_line();
		}
	}
	void WriteStack(Err[] errAry, StringBldr sb) {
		if (Env_.Mode_testing()) return; // only write stack when not testing
		int len = Array_.Len(errAry); if (len == 0) return;	// shouldn't happen, but don't want to throw err
		Err first = errAry[0];
		boolean onlyOne = len == 1;
		sb.Add_line(String_.Repeat("-", 80));
		ListAdp tmp = ListAdp_.new_();
		OrderedHash callStack = first.CallStack(); int callStackCount = callStack.Count();
		for (int i = 0; i < callStackCount ; i++) {
			ErrProcData proc = (ErrProcData)callStack.FetchAt(i);
			// get procIndex
			int idx = -1;
			for (int j = 0; j < len; j++) {
				ErrProcData comp = errAry[j].Proc();
				if (String_.Eq(proc.Raw(), comp.Raw())) {idx = j; break;}
			}
			String idxStr = onlyOne ? "" : Int_.XtoStr(idx);
			String hdr = idx == -1 ? "\t" : idxStr + "\t";
			String ideAddressSpr = String_.CrLf + "\t\t";
			String ideAddress = String_.Eq(proc.IdeAddress(), "") ? "" : ideAddressSpr + proc.IdeAddress();	// NOTE: ideAddress will be blank in compiled mode
			String msg = String_.Concat(hdr, proc.SignatureRaw(), ideAddress);
			tmp.Add(msg);
		}
		tmp.Reverse();
		for (Object o : tmp)
			sb.Add_line((String)o);
	}
	static Err[] InnerAsAry(Err err) {
		ListAdp errAry = ListAdp_.new_();
		Err cur = Err_.as_(err);
		while (cur != null) {
			errAry.Add(cur);
			cur = cur.Inner();
		}
		return (Err[])errAry.XtoAry(Err.class);
	}
	public static final ErrMsgWtr _ = new ErrMsgWtr(); ErrMsgWtr() {}
}