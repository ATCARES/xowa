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
public class Xohp_title_wkr implements ByteAryFmtrArg {
	public Xohp_title_wkr Set(byte[] src, Xop_tkn_itm tkn) {this.src = src; this.tkn = tkn; return this;}
	public void XferAry(ByteAryBfr bfr, int idx) {
		Bld_recurse(bfr, tkn);
	}
	public void Bld_recurse(ByteAryBfr bfr, Xop_tkn_itm tkn) {
		switch (tkn.Tkn_tid()) {
			case Xop_tkn_itm_.Tid_newLine: case Xop_tkn_itm_.Tid_space: case Xop_tkn_itm_.Tid_txt:	// NOTE: atomic tkns which will have no subs
				Write_atr_text(bfr, src, tkn.Src_bgn(), tkn.Src_end());
				break;
			case Xop_tkn_itm_.Tid_arg_nde:		// caption tkns have no subs; just a key and a val; write val
				Arg_nde_tkn arg_nde = (Arg_nde_tkn)tkn;
				Bld_recurse(bfr, arg_nde.Val_tkn());
				break;
			default:	// all other tkns, just iterate over subs for txt tkns
				int len = tkn.Subs_len();
				for (int i = 0; i < len; i++) {
					Xop_tkn_itm sub = tkn.Subs_get(i);
					Bld_recurse(bfr, sub);
				}
				break;
		}
	}
	public static void Write_atr_text(ByteAryBfr bfr, byte[] src, int bgn, int end) {
		for (int i = bgn; i < end; i++) {
			byte b = src[i];
			switch (b) {
				case Byte_ascii.NewLine: case Byte_ascii.CarriageReturn: case Byte_ascii.Tab:		// NOTE: escape ws so that it renders correctly in tool tips
				case Byte_ascii.Quote: case Byte_ascii.Lt: case Byte_ascii.Gt: case Byte_ascii.Amp:	// NOTE: escape possible javascript injection characters
					bfr.Add(Escape_bgn);
					bfr.Add_int_variable(b);
					bfr.Add_byte(Byte_ascii.Semic);
					break;
				default: bfr.Add_byte(b); break;
			}
		}
	}
	byte[] src; Xop_tkn_itm tkn;
	public static final byte[] Escape_bgn = ByteAry_.new_ascii_("&#");
}
