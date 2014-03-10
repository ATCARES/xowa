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
package gplx.xowa.xtns.hiero; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.xowa.wikis.*;
public class Hiero_xtn_mgr extends Xox_mgr_base {
	@Override public boolean Enabled_default() {return true;}
	@Override public byte[] Xtn_key() {return Xtn_key_static;} public static final byte[] Xtn_key_static = ByteAry_.new_ascii_("hiero");
	@Override public Xox_mgr Clone_new() {return new Hiero_xtn_mgr();}
	@Override public void Xtn_init_by_wiki(Xow_wiki wiki) {
		if (!Enabled()) return;
		this.Reset(wiki);
	}
	public void Clear() {
	}
	private void Reset(Xow_wiki wiki) {
	}
}
