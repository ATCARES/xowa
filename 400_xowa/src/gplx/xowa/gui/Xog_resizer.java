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
package gplx.xowa.gui; import gplx.*; import gplx.xowa.*;
import gplx.gfui.*;
public class Xog_resizer {
	public void Exec_win_resize(Xoa_app app, int main_w, int main_h) {
		Xog_layout layout = app.Gui_mgr().Layout();
		Xog_win main_win = app.Gui_mgr().Main_win();
		GfuiBtn go_bwd_btn = main_win.Go_bwd_btn(), go_fwd_btn = main_win.Go_fwd_btn(), url_exec_btn = main_win.Url_exec_btn(), search_exec_btn = main_win.Search_exec_btn(), find_fwd_btn = main_win.Find_fwd_btn(), find_bwd_btn = main_win.Find_bwd_btn(), find_close_btn = main_win.Find_close_btn();
		GfuiTextBox url_box = main_win.Url_box(), search_box = main_win.Search_box(), find_box = main_win.Find_box(), prog_box = main_win.Prog_box(), note_box = main_win.Info_box();
		Gfui_html html_box = main_win.Html_box();
		int txt_dim = layout.Box_height_calc(app.Gui_mgr().Kit(), url_box);
		int btn_dim = 25; //txt_dim  + 1;
		if (txt_dim < 25) {
			txt_margin_v = (25 - txt_dim) / 2;
			txt_dim = 25;
		}
		else
			txt_margin_v = 0;
		int txt_dif = 0, btn_dif = 0;
		int bar_dim = btn_dim > txt_dim ? btn_dim : txt_dim;
		if (txt_dim < 25) 	{txt_dif = (25 - txt_dim) / 2;}
		else				{btn_dif = (txt_dim - 25) / 2;}
		int menu_bar_adj = app.Gui_mgr().Menu_mgr().Window_mnu_mgr().Get_or_new("main_win").Enabled() ? 20 : 0;
		Exec_win_resize_elem(layout.Go_bwd_btn()		, go_bwd_btn			, new Rect_ref(0, 0, btn_dim							, btn_dim				), null, Xog_resizer.Layout_init);
		Exec_win_resize_elem(layout.Go_fwd_btn()		, go_fwd_btn			, new Rect_ref(0, 0, btn_dim							, btn_dim				), go_bwd_btn, Xog_resizer.Layout_right_top);
		Exec_win_resize_elem(layout.Url_box()			, url_box				, new Rect_ref(0, 0, main_w - (btn_dim * 5) - 200		, txt_dim				), go_fwd_btn, Xog_resizer.Layout_right_top);
		Exec_win_resize_elem(layout.Url_exec_btn()		, url_exec_btn			, new Rect_ref(0, 0, btn_dim							, btn_dim				), url_box, Xog_resizer.Layout_right_top);
		Exec_win_resize_elem(layout.Search_box()		, search_box			, new Rect_ref(0, 0, 190								, txt_dim				), url_exec_btn, Xog_resizer.Layout_right_top);
		Exec_win_resize_elem(layout.Search_exec_btn()	, search_exec_btn		, new Rect_ref(0, 0, btn_dim							, btn_dim				), search_box, Xog_resizer.Layout_right_top);
		Exec_win_resize_elem(layout.Html_box()			, html_box				, new Rect_ref(0, 0, main_w								, main_h + -(bar_dim * 2) + 4 - menu_bar_adj), go_bwd_btn, Xog_resizer.Layout_below_left);	// -40:btn_dim(url bar) + btn_dim (find box)
		html_box.Y_(bar_dim);
		Exec_win_resize_elem(layout.Find_close_btn()	, find_close_btn		, new Rect_ref(0, 0, btn_dim							, btn_dim				), html_box, Xog_resizer.Layout_below_left);
		Exec_win_resize_elem(layout.Find_box()			, find_box				, new Rect_ref(0, 0, 102								, txt_dim				), find_close_btn, Xog_resizer.Layout_right_top);
		find_box.Y_(html_box.Y_max());
		Exec_win_resize_elem(layout.Find_fwd_btn()		, find_fwd_btn			, new Rect_ref(0, 0, btn_dim							, btn_dim				), find_box, Xog_resizer.Layout_right_top);
		Exec_win_resize_elem(layout.Find_bwd_btn()		, find_bwd_btn			, new Rect_ref(0, 0, btn_dim							, btn_dim				), find_fwd_btn, Xog_resizer.Layout_right_top);
		Exec_win_resize_elem(layout.Prog_box()			, prog_box				, new Rect_ref(0, 0, (main_w - 102 - 200 - (bar_dim * 3)), txt_dim				), find_bwd_btn, Xog_resizer.Layout_right_top);	// -200=200(find_box) - 200 (note_box)
		Exec_win_resize_elem(layout.Note_box()			, note_box				, new Rect_ref(0, 0, 200								, txt_dim				), prog_box, Xog_resizer.Layout_right_top);	// -200=200(find_box)
		if (txt_dif > 0) {
			GfuiElem_.Y_adj(txt_dif, url_box, search_box);
			GfuiElem_.Y_adj(txt_dif, find_box, prog_box, note_box);
		}
		if (btn_dif > 0) {
			GfuiElem_.Y_adj(btn_dif, go_bwd_btn, go_fwd_btn, url_exec_btn, find_close_btn, search_exec_btn);
			GfuiElem_.Y_adj(btn_dif, find_fwd_btn, find_bwd_btn);			
		}
	}
	private static void Exec_win_resize_elem(Xog_layout_box box, GfuiElem cur_elem, Rect_ref cur_elem_rect, GfuiElem prv_elem, byte layout) {
		if (ClassAdp_.Eq_typeSafe(cur_elem, GfuiTextBox.class)) {
			try {
				GfuiTextBox cur_box = (GfuiTextBox)cur_elem;
				cur_box.Margins_set(0, txt_margin_v, 0, txt_margin_v);
			}	catch (Exception e) {Err_.Noop(e);}
		}
		if (box.Mode() == Xog_layout_box.Mode_abs)	// absolute mode; set dimensions manually
			cur_elem_rect.X_(box.X_abs()).Y_(box.Y_abs()).W_(box.W_abs()).H_(box.H_abs());
		else {
			box.Adj_size(cur_elem_rect);
			if (prv_elem != null)	// null for 1st elem (which doesn't have preceding elem)
				Set_pos_by_prv(layout, cur_elem_rect, Rect_ref.rectAdp_(prv_elem.Rect()));
			box.Adj_pos(cur_elem_rect);
		}
		cur_elem.Rect_set(cur_elem_rect.XtoRectAdp());
		if (ClassAdp_.Eq_typeSafe(cur_elem, GfuiBtn.class)) {
			GfuiBtn cur_btn = (GfuiBtn)cur_elem;
			cur_btn.Btn_img_(cur_btn.Btn_img());
		}
	}
	private static void Set_pos_by_prv(byte layout, Rect_ref cur_elem_rect, Rect_ref prv_elem_rect) {
		switch (layout) {
			case Xog_resizer.Layout_right_top:
				cur_elem_rect.X_(prv_elem_rect.X_max());
				cur_elem_rect.Y_(prv_elem_rect.Y());
				break;
			case Xog_resizer.Layout_right_bot:
				cur_elem_rect.X_(prv_elem_rect.X_max());
				cur_elem_rect.Y_(prv_elem_rect.Y_max() - cur_elem_rect.H());	// cur_elem.Height
				break;
			case Xog_resizer.Layout_below_left:
				cur_elem_rect.X_(prv_elem_rect.X());
				cur_elem_rect.Y_(prv_elem_rect.Y_max());
				break;
		}
	}
	public static final byte Layout_init = 0, Layout_right_top = 1, Layout_right_bot = 2, Layout_below_left = 3;
	private static int txt_margin_v = 0;
}