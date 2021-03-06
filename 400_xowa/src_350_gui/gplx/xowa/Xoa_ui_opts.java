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
public class Xoa_ui_opts implements Gfo_usr_dlg_ui_opt, GfoInvkAble {
	public Xoa_ui_opts(Xoa_app app) {}
	public boolean Warn_enabled() {return warn_enabled;} private boolean warn_enabled = true;
	public boolean Note_enabled() {return note_enabled;} private boolean note_enabled = true;
	public Xol_font_info Font() {return font;} private Xol_font_info font = new Xol_font_info("Arial", 8, gplx.gfui.FontStyleAdp_.Plain);
	public ByteAryFmtr Search_box_fmtr() {return search_box_fmtr;} private ByteAryFmtr search_box_fmtr = ByteAryFmtr.new_("Special:Allpages?from=", "search");
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_warn_enabled_))			warn_enabled = m.ReadYn("v");
		else if	(ctx.Match(k, Invk_note_enabled_))			note_enabled = m.ReadYn("v");
		else if	(ctx.Match(k, Invk_font))					return font;
		else if	(ctx.Match(k, Invk_search_box_fmt_))		search_box_fmtr.Fmt_(m.ReadBry("v"));
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}
	private static final String Invk_warn_enabled_ = "warn_enabled_", Invk_note_enabled_ = "note_enabled_", Invk_search_box_fmt_ = "search_box_fmt_", Invk_font = "font";
}
