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
package gplx.xowa.users; import gplx.*; import gplx.xowa.*;
public class Xou_cfg implements GfoInvkAble {
	public Xou_cfg(Xou_user user) {this.user = user; pages_mgr = new Xouc_pages_mgr(this); startup_mgr = new Xouc_startup_mgr(this); setup_mgr = new Xouc_setup_mgr(user);}
	public Xou_user User() {return user;} private Xou_user user;
	public Xouc_pages_mgr Pages_mgr() {return pages_mgr;} private Xouc_pages_mgr pages_mgr;
	public Xouc_startup_mgr Startup_mgr() {return startup_mgr;} private Xouc_startup_mgr startup_mgr;
	public Xouc_setup_mgr Setup_mgr() {return setup_mgr;} private Xouc_setup_mgr setup_mgr;
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {			
		if		(ctx.Match(k, Invk_pages))			return pages_mgr;
		else if	(ctx.Match(k, Invk_startup))		return startup_mgr;
		else if	(ctx.Match(k, Invk_setup))			return setup_mgr;
		return this;
	}	public static final String Invk_pages = "pages", Invk_startup = "startup", Invk_setup = "setup";
}
