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
public class Swing_kit extends Gfui_kit_base {
	private ByteAryFmtr ask_fmtr = ByteAryFmtr.new_(); private ByteAryBfr ask_bfr = ByteAryBfr.new_();
	@Override public byte Tid() {return Gfui_kit_.Swing_tid;}
	@Override public String Key() {return "swing";}
	@Override public GxwElemFactory_base Factory() {return factory;} private GxwElemFactory_cls_lang factory = new GxwElemFactory_cls_lang();
	@Override public void Ask_ok(String grp_key, String msg_key, String fmt, Object... args) {GfuiEnv_.ShowMsg(ask_fmtr.Bld_str_many(ask_bfr, fmt, args));}
	@Override public void Kit_run() {GfuiEnv_.Run(this.Main_win());}
	@Override public void Kit_term() {this.Kit_term_cbk().Invk(); GfuiEnv_.Exit();}
	@Override public ImageAdp New_img_load(Io_url url) {return ImageAdp_.file_(url);}
	@Override protected Gxw_html New_html_impl() {return new Mem_html();}
	@Override protected GxwElem New_btn_impl() {return factory.control_();}
        public static final Swing_kit _ = new Swing_kit(); Swing_kit() {}
}
