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
package gplx.xowa.xtns; import gplx.*; import gplx.xowa.*;
import gplx.xowa.html.*;
public abstract class Xox_mgr_base implements Xox_mgr {
	public Xox_mgr_base() {
		this.enabled = Enabled_default();
	}
	public abstract byte[] Xtn_key();
	public abstract Xox_mgr Clone_new();
	public boolean Enabled() {return enabled;} private boolean enabled;
	@gplx.Virtual public boolean Enabled_default() {return true;}
	public void Enabled_y_() {enabled = true;} public void Enabled_n_() {enabled = false;}	// TEST:
	@gplx.Virtual public void Xtn_ctor_by_app(Xoa_app app) {}
	@gplx.Virtual public void Xtn_ctor_by_wiki(Xow_wiki wiki) {}
	@gplx.Virtual public void Xtn_init_by_wiki(Xow_wiki wiki) {}
	@gplx.Virtual public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_enabled))			return Yn.X_to_str(enabled);
		else if	(ctx.Match(k, Invk_enabled_))			enabled = m.ReadYn("v");
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}
	private static final String Invk_enabled = "enabled", Invk_enabled_ = "enabled_";
	public static void Xtn_write_escape(Xoa_app app, ByteAryBfr bfr, byte[] src, Xop_xnde_tkn xnde) {Xtn_write_escape(app, bfr, src, xnde.Src_bgn(), xnde.Src_end());}
	public static void Xtn_write_escape(Xoa_app app, ByteAryBfr bfr, byte[] src)					{Xtn_write_escape(app, bfr, src, 0, src.length);}
	public static void Xtn_write_escape(Xoa_app app, ByteAryBfr bfr, byte[] src, int bgn, int end)	{Xoh_html_wtr_escaper.Escape(app, bfr, src, bgn, end, true, false);}
	public static void Xtn_write_unsupported(Xoa_app app, Xop_ctx ctx, ByteAryBfr bfr, byte[] src, Xop_xnde_tkn xnde, boolean parse_content) {
		bfr.Add(Xowa_not_implemented);
		Xox_mgr_base.Xtn_write_escape(app, bfr, src, xnde.Tag_open_bgn(), xnde.Tag_open_end());
		if (xnde.CloseMode() != Xop_xnde_tkn.CloseMode_pair) return;	// inline node
		if (parse_content)
			bfr.Add(ctx.Wiki().Parser().Parse_fragment_to_html(ctx, ByteAry_.Mid(src, xnde.Tag_open_end(), xnde.Tag_close_bgn())));
		else
			Xox_mgr_base.Xtn_write_escape(app, bfr, src, xnde.Tag_open_end(), xnde.Tag_close_bgn());
		Xox_mgr_base.Xtn_write_escape(app, bfr, src, xnde.Tag_close_bgn(), xnde.Tag_close_end());
	}
	private static final byte[] Xowa_not_implemented = ByteAry_.new_ascii_("XOWA does not support this extension: ");
}
