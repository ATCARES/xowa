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
package gplx.gfui; import gplx.*;
import org.junit.*;
public class IptBndMgr_tst {
	@Before public void setup() {
		fx = new IptBndMgr_fx();
	}	IptBndMgr_fx fx;
	@Test  public void Add() {
		fx.ini_Clear().run_Add("key.a").tst_Exec_same("key.a").tst_Exec_none("key.b");
		fx.ini_Clear().run_Add("key.ctrl+key.a").tst_Exec_same("key.ctrl+key.a").tst_Exec_none("key.ctrl").tst_Exec_none("key.a");
		fx.ini_Clear().run_Add("key.a|key.b").tst_Exec_same("key.a").tst_Exec_same("key.b").tst_Exec_none("key.c");
		fx.ini_Clear().run_Add("mouse.left").tst_Exec_same("mouse.left");
		fx.ini_Clear().run_Add("key.a,key.b")
			.tst_Exec_none("key.a").tst_Exec_same("key.b")
			.tst_Exec_none("key.a").tst_Exec_none("key.c").tst_Exec_none("key.a").tst_Exec_same("key.b")
			.tst_Exec_none("key.a").tst_Exec_none("key.a").tst_Exec_none("key.b");
	}
	class IptBndMgr_fx {
		public IptBndMgr Under() {return under;} IptBndMgr under = IptBndMgr.new_();
		public IptBndMgr_fx ini_Clear() {under.Clear(); return this;}
		public IptBndMgr_fx run_Add(String raw) {
			IptArg[] args = IptArg_.parse_ary_(raw);
			ListAdp list = ListAdp_.new_();
			for (IptArg arg : args)
				list.Add(arg);

			IptBnd_mok bnd = new IptBnd_mok(output).Key_(raw).Ipts_(list).EventTypes_(IptEventType_.default_(args));
			under.Add(bnd);
			return this;
		}
		public IptBndMgr_fx tst_Exec_none(String key) {return tst_Exec(key, "");}
		public IptBndMgr_fx tst_Exec_same(String key) {return tst_Exec(key, key);}
		public IptBndMgr_fx tst_Exec(String key, String expd) {
			output.Clear();
			IptArg[] args = IptArg_.parse_ary_(key);
			for (IptArg arg : args) {
				IptEventData evData = IptEventData.new_(null, IptArg_.EventType_default(arg), arg, null, null);
				under.Process(evData);
			}
			Tfds.Eq(expd, output.XtoStr());
			return this;
		}
		String_bldr output = String_bldr_.new_();
            public IptBndMgr_fx() {}
	}
	class IptBnd_mok implements IptBnd {
		public String Key() {return key;} public IptBnd_mok Key_(String v) {key = v; return this;} private String key;
		public ListAdp Ipts() {return args;} public IptBnd_mok Ipts_(ListAdp v) {args = v; return this;} ListAdp args;
		public IptEventType EventTypes() {return eventTypes;} public IptBnd_mok EventTypes_(IptEventType v) {eventTypes = v; return this;} IptEventType eventTypes;
		public Object Srl(GfoMsg owner) {return this;}
		public void Exec(IptEventData iptData) {
			output.Add(iptData.EventArg().Key());
		}
		public IptBnd_mok(String_bldr v) {output = v;} String_bldr output;
	}
}
