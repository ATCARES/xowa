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
package gplx.xowa.html; import gplx.*; import gplx.xowa.*;
public class ByteAryFmtrArg_html_fmtr implements ByteAryFmtrArg {
	private ByteAryFmtr fmtr; private Xoh_html_wtr wtr; private Xop_ctx ctx; private Xoh_opts opts; private byte[] src; private Xop_tkn_itm tkn; private int depth;
	public ByteAryFmtrArg_html_fmtr Set(Xoh_html_wtr wtr, Xop_ctx ctx, Xoh_opts opts, byte[] src, Xop_tkn_itm tkn, int depth, ByteAryFmtr fmtr) {this.wtr = wtr; this.ctx = ctx; this.opts = opts; this.src = src; this.tkn = tkn; this.depth = depth; this.fmtr = fmtr; return this;}
	public void XferAry(ByteAryBfr trg, int idx) {
		ByteAryBfr tmp_bfr = ByteAryBfr.new_();
		wtr.Write_tkn(ctx, opts, tmp_bfr, src, depth + 1, null, Xoh_html_wtr.Sub_idx_null, tkn);
		byte[] bry = tmp_bfr.XtoAryAndClear();
		if (bry.length == 0) return;
		fmtr.Bld_bfr_many(trg, bry);
	}
}
