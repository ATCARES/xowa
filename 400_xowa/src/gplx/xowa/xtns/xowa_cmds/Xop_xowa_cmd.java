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
package gplx.xowa.xtns.xowa_cmds; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
import gplx.xowa.html.*;
public class Xop_xowa_cmd implements Xox_xnde {
	public Xop_root_tkn Xtn_root() {return xtn_root;} private Xop_root_tkn xtn_root;
	public void Xtn_parse(Xow_wiki wiki, Xop_ctx ctx, Xop_root_tkn root, byte[] src, Xop_xnde_tkn xnde) {
		int itm_bgn = xnde.Tag_open_end(), itm_end = xnde.Tag_close_bgn();
		if (itm_bgn == src.length)	return;  // NOTE: handle inline where there is no content to parse; EX: <xowa_cmd/>
		if (src[itm_bgn] 		== Byte_ascii.NewLine) ++itm_bgn;	// ignore 1st \n; 
		if (src[itm_end - 1] 	== Byte_ascii.NewLine) --itm_end;	// ignore last \n;
		byte[] raw = ByteAry_.Mid(src, itm_bgn, itm_end);
		byte[] xtn_src = raw;
		if (wiki.Sys_cfg().Xowa_cmd_enabled()) {	// only exec if enabled for wiki
			Object rslt = wiki.App().Gfs_mgr().Run_str(String_.new_utf8_(raw));
			xtn_src = ByteAry_.new_utf8_(Object_.XtoStr_OrNullStr(rslt));
		}
		Xop_ctx sub_ctx = Xop_ctx.new_sub_(wiki);
		xtn_root = wiki.Parser().Parse_recurse(sub_ctx, xtn_src, true);
	}
	public void Xtn_write(Xoa_app app, Xoh_html_wtr html_wtr, Xoh_html_wtr_ctx opts, Xop_ctx ctx, ByteAryBfr bfr, byte[] src, Xop_xnde_tkn xnde) {
		html_wtr.Write_tkn(bfr, ctx, opts, xtn_root.Root_src(), xnde, Xoh_html_wtr.Sub_idx_null, xtn_root);
	}
}
