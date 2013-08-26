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
public class Xoh_portal_mgr implements GfoInvkAble {
	public Xoh_portal_mgr(Xoa_app app) {wikis = new Xohp_wikis_mgr(app);}
	public Xohp_wikis_mgr Wikis() {return wikis;} private Xohp_wikis_mgr wikis;
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_wikis))				return wikis;
		else return GfoInvkAble_.Rv_unhandled;
	}	private static final String Invk_wikis = "wikis";
}
