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
import gplx.gfui.*; import gplx.xowa.gui.*; import gplx.xowa.gui.history.*; import gplx.xowa.xtns.math.*; import gplx.xowa.files.*;
public class Xog_win implements GfoInvkAble, GfoEvObj {
	public Xog_win(Xoa_app app, Xoa_gui_mgr mgr) {this.app = app; js_cbk = new Xoa_xowa_exec(app);}
	public GfuiWin			Win() {return win;} GfuiWin win;
	public Gfui_html		Html_box() {return html_box;} Gfui_html html_box;
	public GfuiTextBox		Find_box() {return find_box;} GfuiTextBox find_box;
	public GfuiTextBox		Url_box() {return url_box;} GfuiTextBox url_box;
	public GfuiTextBox		Search_box() {return search_box;} GfuiTextBox search_box;
	public GfuiBtn			Go_bwd_btn() {return go_bwd_btn;} GfuiBtn go_bwd_btn;
	public GfuiBtn			Go_fwd_btn() {return go_fwd_btn;} GfuiBtn go_fwd_btn;
	public GfuiBtn			Url_exec_btn() {return url_exec_btn;} GfuiBtn url_exec_btn;
	public GfuiBtn			Find_close_btn() {return find_close_btn;} GfuiBtn find_close_btn;
	public GfuiBtn			Search_exec_btn() {return search_exec_btn;} GfuiBtn search_exec_btn;
	public GfuiBtn			Find_fwd_btn() {return find_fwd_btn;} GfuiBtn find_fwd_btn;
	public GfuiBtn			Find_bwd_btn() {return find_bwd_btn;} GfuiBtn find_bwd_btn;
	public GfuiTextBox		Prog_box() {return prog_box;} GfuiTextBox prog_box;
	public GfuiTextBox		Info_box() {return note_box;} GfuiTextBox note_box;
	public GfoEvMgr			EvMgr() {if (evMgr == null) evMgr = GfoEvMgr.new_(this); return evMgr;} GfoEvMgr evMgr;
	public Xoa_app			App() {return app;} private Xoa_app app;
	public Xoa_page			Page() {return page;} public void Page_(Xoa_page page) {this.page = page;} private Xoa_page page;
	public Xog_history_mgr	History_mgr() {return history_mgr;} private Xog_history_mgr history_mgr = new Xog_history_mgr();
	public Xog_win_wtr		Gui_wtr() {return gui_wtr;} public void Gui_wtr_(Xog_win_wtr v) {gui_wtr = v;} private Xog_win_wtr gui_wtr = Xog_win_wtr_null._;
	public Xoa_xowa_exec	Js_cbk() {return js_cbk;} private Xoa_xowa_exec js_cbk;
	public Xog_resizer Resizer() {return resizer;} private Xog_resizer resizer = new Xog_resizer();
	public void Refresh_win_size() {
		if (win != null)	// NOTE: will be null when html box adjustment pref is set and application is starting
			resizer.Exec_win_resize(app, win.Width(), win.Height());
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_link_click))								Exec_link_click();
		else if	(ctx.Match(k, Invk_link_print))								Exec_link_print();
		else if	(ctx.Match(k, Gfui_html.Evt_link_hover))					Exec_link_hover(m.ReadStr("v"));
		else if	(ctx.Match(k, Gfui_html.Evt_location_changed))				Exec_link_clicked(m.ReadStr("v"));
		else if	(ctx.Match(k, Gfui_html.Evt_location_changing))				Exec_navigate(m.ReadStr("v"));
		else if (ctx.Match(k, Gfui_html.Evt_win_resized))					Refresh_win_size();
		else if (ctx.Match(k, Invk_page_refresh))							Exec_page_refresh();
		else if	(ctx.Match(k, Invk_page_reload_imgs))						Exec_page_reload_imgs();
		else if	(ctx.Match(k, Invk_page_view_read))							Exec_page_view(Xoh_wiki_article.Tid_view_read);
		else if	(ctx.Match(k, Invk_page_view_edit))							Exec_page_view(Xoh_wiki_article.Tid_view_edit);
		else if	(ctx.Match(k, Invk_page_view_html))							Exec_page_view(Xoh_wiki_article.Tid_view_html);
		else if (ctx.Match(k, Invk_page_edit_save))							Exec_page_edit_save(false);
		else if (ctx.Match(k, Invk_page_edit_save_draft))					Exec_page_edit_save(true);
		else if (ctx.Match(k, Invk_page_edit_rename))						Exec_page_edit_rename();
		else if	(ctx.Match(k, Invk_page_edit_focus_box)) 					{html_box.Focus(); html_box.Html_elem_focus(Id_xowa_edit_data_box);}
		else if	(ctx.Match(k, Invk_page_edit_focus_first)) 					{html_box.Focus(); html_box.Html_elem_focus(Html_id_first_heading);}
		else if (ctx.Match(k, Invk_page_edit_preview))						Exec_page_edit_preview();
		else if	(ctx.Match(k, Invk_page_dbg_html))							Exec_page_dbg(Xoh_wiki_article.Tid_view_html);
		else if	(ctx.Match(k, Invk_page_dbg_wiki))							Exec_page_dbg(Xoh_wiki_article.Tid_view_edit);
		else if	(ctx.Match(k, Invk_page_goto))								Exec_url_exec(m.ReadStr("v"));
		else if	(ctx.Match(k, Invk_page_goto_recent))						Exec_url_exec(app.User().History_mgr().Get_at_last(app));
		else if	(ctx.Match(k, Invk_history_bwd))							{Exec_page_stack(history_mgr.Go_bwd(page.Wiki()));}
		else if	(ctx.Match(k, Invk_history_fwd))							{Exec_page_stack(history_mgr.Go_fwd(page.Wiki()));}
		else if	(ctx.Match(k, Invk_html_box_select_by_id))					Exec_html_box_select_by_id(m.ReadStrOr("v", null));
		else if (ctx.Match(k, Invk_html_box_focus_previous_or_firstHeading))Exec_html_box_focus_previous_or_firstHeading();
		else if	(ctx.Match(k, Invk_html_box_focus))							Exec_html_box_focus();
		else if	(ctx.Match(k, Invk_prog_box_focus))							prog_box.Focus();
		else if (ctx.Match(k, Invk_prog_box_log))							app.Gui_mgr().Show_prog();
		else if	(ctx.Match(k, Invk_note_box_focus))							note_box.Focus();
		else if	(ctx.Match(k, Invk_url_box_exec))							Exec_url_exec(url_box.Text());
		else if	(ctx.Match(k, Invk_url_box_focus))							url_box.Focus_select_all();
		else if (ctx.Match(k, Invk_url_box_paste_and_go))					Exec_url_box_paste_and_go();
		else if	(ctx.Match(k, Invk_find_box_focus))							app.Gui_mgr().Layout().Find_show();
		else if	(ctx.Match(k, Invk_find_box_hide))							app.Gui_mgr().Layout().Find_close();
		else if	(ctx.Match(k, Invk_find_box_find))							{Exec_find_box_find(); gui_wtr.Prog_direct("");}
		else if	(ctx.Match(k, Invk_find_box_dir_fwd))						{find_box_dir_fwd = Bool_.Y; Exec_find_box_find(); gui_wtr.Prog_direct("find_box dir_fwd=" + Yn.XtoStr(find_box_dir_fwd));}
		else if	(ctx.Match(k, Invk_find_box_dir_bwd))						{find_box_dir_fwd = Bool_.N; Exec_find_box_find(); gui_wtr.Prog_direct("find_box dir_fwd=" + Yn.XtoStr(find_box_dir_fwd));}
		else if	(ctx.Match(k, Invk_find_box_case_toggle))					{find_box_case_match = !find_box_case_match; gui_wtr.Prog_direct("find_box case_match=" + Yn.XtoStr(find_box_case_match));}
		else if	(ctx.Match(k, Invk_find_box_wrap_toggle))					{find_box_wrap_find = !find_box_wrap_find; gui_wtr.Prog_direct("find_box wrap=" + Yn.XtoStr(find_box_wrap_find));}
		else if	(ctx.Match(k, Invk_app_bookmarks_add))						Exec_bookmarks_add();
		else if	(ctx.Match(k, Invk_app_exit))								Exec_exit();
		else if	(ctx.Match(k, Invk_app_exec_cfg))							Exec_app_exec_cfg();
		else if	(ctx.Match(k, Invk_shortcuts))								return redirect_mgr;
		else if	(ctx.Match(k, Invk_eval))									Exec_eval(m.ReadStr("cmd"));
		else if	(ctx.Match(k, Invk_warn_clear_all))							gui_wtr.Info_box_clear(true);
		else if	(ctx.Match(k, Invk_warn_clear_color))						gui_wtr.Info_box_clear(false);
		else if	(ctx.Match(k, Invk_cancel_wait))							Exec_cancel_wait();
		else if	(ctx.Match(k, Invk_cancel_restart))							Exec_cancel_restart();
		else if	(ctx.Match(k, Invk_search))									Exec_search();
		else if	(ctx.Match(k, Invk_search_box_focus ))						search_box.Focus_select_all();
		else if	(ctx.Match(k, Invk_url_box))								return url_box;
		else if	(ctx.Match(k, Invk_options_save))							Exec_options_save();
		else if	(ctx.Match(k, Invk_context_copy))							Exec_context_copy();
		else if	(ctx.Match(k, Invk_context_find))							Exec_context_find();
		else if	(ctx.Match(k, Invk_window_font_changed))					Exec_window_font_changed((Xol_font_info)m.CastObj("font"));
		else																return GfoInvkAble_.Rv_unhandled;
		return this;
	}	Ipt_bnd_redirect_mgr redirect_mgr = new Ipt_bnd_redirect_mgr();
	public static final String 
		  Invk_link_click = "link_click", Invk_link_print = "link_print"
		, Invk_page_refresh = "page_refresh", Invk_page_reload_imgs = "page_reload_imgs"
		, Invk_page_view_edit = "page_view_edit", Invk_page_view_read = "page_view_read", Invk_page_view_html = "page_view_html", Invk_page_goto = "page_goto", Invk_page_goto_recent = "page_goto_recent"
		, Invk_page_edit_save = "page_edit_save", Invk_page_edit_save_draft = "page_edit_save_draft", Invk_page_edit_preview = "page_edit_preview", Invk_page_edit_rename = "page_edit_rename", Invk_page_dbg_wiki = "page_dbg_wiki", Invk_page_dbg_html = "page_dbg_html"
		, Invk_history_fwd = "history_fwd", Invk_history_bwd = "history_bwd"
		, Invk_html_box_select_by_id = "html_box_select_by_id", Invk_page_edit_focus_box = "Invk_page_edit_focus_box", Invk_page_edit_focus_first = "Invk_page_edit_focus_first", Invk_html_box_focus_previous_or_firstHeading = "html_box_focus_previous_or_firstHeading", Invk_html_box_focus = "html_box_focus", Invk_prog_box_focus = "prog_box_focus"
		, Invk_url_box_exec = "url_box_exec", Invk_url_box_focus = "url_box_focus", Invk_url_box_paste_and_go = "url_box_paste_and_go"
		, Invk_find_box_focus = "find_box_focus", Invk_find_box_hide = "find_box_hide", Invk_find_box_find = "Exec_find_box_find", Invk_find_box_dir_fwd = "find_box_dir_fwd", Invk_find_box_dir_bwd = "find_box_dir_bwd", Invk_find_box_case_toggle = "find_box_case_toggle", Invk_find_box_wrap_toggle = "find_box_wrap_toggle", Invk_note_box_focus = "note_box_focus"
		, Invk_app_exit = "app_exit", Invk_app_exec_cfg = "app_exec_cfg"
		, Invk_app_bookmarks_add = "app_bookmarks_add"
		, Invk_shortcuts = "shortcuts"
		, Invk_eval = "eval"
		, Invk_warn_clear_all = "warn_clear_all", Invk_warn_clear_color = "warn_clear_color"
		, Invk_cancel_wait = "cancel_wait", Invk_cancel_restart = "cancel_restart"
		, Invk_url_box = "url_box"
		, Invk_prog_box_log = "prog_box_log"
		, Invk_search = "search"
		, Invk_search_box_focus = "search_box_focus"
		, Invk_options_save = "options_save"
		, Invk_context_copy = "context.copy", Invk_context_find = "context.find"
		, Invk_window_font_changed = "winow_font_changed"
		;
	public void Exec_bookmarks_add() {
		app.User().Bookmarks_add(page); gui_wtr.Prog_many(GRP_KEY, "app_bookmarks_add", "bookmark added: ~{0}", String_.new_utf8_(page.Page_ttl().Full_txt_raw()));
	}
	private void Exec_options_save() {
		String font_name = html_box.Html_elem_atr_get_str("opt_window_font_name", Gfui_html.Atr_value);
		String font_size = html_box.Html_elem_atr_get_str("opt_window_font_size", Gfui_html.Atr_value);
		String css_xtn = html_box.Html_elem_atr_get_str("opt_html_css_xtn", Gfui_html.Atr_value);
		app.Gui_mgr().Html_mgr().Css_xtn_(ByteAry_.new_utf8_(css_xtn));
		Io_url user_system_cfg = app.User().Fsys_mgr().App_data_cfg_dir().GenSubFil(gplx.xowa.users.Xou_fsys_mgr.Name_user_system_cfg);
		String user_system_cfg_text = Io_mgr._.LoadFilStr(user_system_cfg);
		user_system_cfg_text = Update_or_append(user_system_cfg_text, "app.gui.win_opts.font.name_", String_.Format("app.gui.win_opts.font.name_('{0}').size_('{1}');\n", font_name, font_size));
		user_system_cfg_text = Update_or_append(user_system_cfg_text, "app.gui.html.css_xtn_", String_.Format("app.gui.html.css_xtn_('{0}');\n", css_xtn));
		Io_mgr._.SaveFilStr(user_system_cfg, user_system_cfg_text);
		Exec_page_refresh();
	}
	private void Exec_link_clicked(String anchor_raw) {
		String url = url_box.Text();
		int pos = String_.FindFwd(url, gplx.xowa.html.Xoh_html_tag.Const_anchor);
		if (pos != ByteAry_.NotFound) url = String_.Mid(url, 0, pos);
		String anchor_str = Xog_win_utl_.Parse_evt_location_changing(anchor_raw);
		byte[] anchor_bry = ByteAry_.new_utf8_(anchor_str);
		if (anchor_str != null) {							// link has anchor
			url_box.Text_(url + "#" + anchor_str);			// update url box
			page.DocPos_(Xog_history_itm.Html_doc_pos_toc);	// HACK: anchor clicked; set docPos of curentPage to TOC (so back will go back to TOC)
			history_mgr.Update_html_doc_pos(page);			// HACK: update history_mgr; note that this must occur before setting Anchor (since Anchor will generate a new history itm)
			page.Url().Anchor_bry_(anchor_bry);				// update url
		}
		history_mgr.Add(page);
		app.User().History_mgr().Add(page.Url(), ByteAry_.Add_w_dlm(Byte_ascii.Hash, page.Url().Page_bry(), anchor_bry));
	}
	String Update_or_append(String body, String find_text, String append_text) {
		int bgn_pos = String_.FindFwd(body, find_text);
		if (bgn_pos != String_.NotFound) {	// text found; delete entire line
			int end_pos = String_.FindFwd(body, ";\n", bgn_pos);
			body = String_.Mid(body, 0, bgn_pos) + String_.Mid(body, end_pos + 2, String_.Len(body));
		}
		body += append_text;
		return body;
	}
	public boolean Exec_html_box_select_by_id(String id) {
		if (id == null) return false;			
		boolean rv = html_box.Html_elem_scroll_into_view(app.Url_converter_id().Encode_str(id));
//			if (rv)
//				app.User().History_mgr().Add(page);
		return rv;
	}
	private void Exec_eval(String s) {
		String snippet = html_box.Html_elem_atr_get_str(s, Gfui_html.Atr_innerHTML);
		app.Gfs_mgr().Run_str(snippet);
	}
	private void Exec_link_click() {
		if (page.Wiki().Gui_mgr().Cfg_browser().Content_editable()) {	// NOTE: Link_click is needed for content_editable is enabled; Link_click will be handled by SwtBrowser location changed
			String href = html_box.Html_active_atr_get_str(Gfui_html.Atr_href, null); if (String_.Len_eq_0(href)) return; // NOTE: href can be null for images; EX: [[File:Loudspeaker.svg|11px|link=|alt=play]]; link= basically means don't link to image
			Exec_navigate(href);
		}
	}
	private void Exec_link_print() {
		String href = String_.Replace(String_.new_utf8_(app.Url_converter_href().Decode(ByteAry_.new_utf8_(html_box.Html_active_atr_get_str(Gfui_html.Atr_href, "")))), "_", " ");
		String title = html_box.Html_active_atr_get_str(Gfui_html.Atr_title, "");
		String val = String_.Trim(href + " " + title);
		if (String_.Eq(val, prog_box.Text())) return;
		gui_wtr.Prog_direct(val);
	}
	private void Exec_html_box_focus() {
		html_box.Focus();
		if (cur_view_tid != Xoh_wiki_article.Tid_view_read) html_box.Html_elem_focus(Id_xowa_edit_data_box);
	}
	private void Exec_link_hover(String href) {
		if (href == null || String_.Len(href) == 0 || String_.Eq(href, "file:///")) {
			gui_wtr.Prog_direct("");	// clear out previous entry
			return;
		}
		app.Href_parser().Parse(tmp_href, href, page.Wiki(), page.Page_ttl().Page_url());
		ByteAryBfr tmp = app.Utl_bry_bfr_mkr().Get_b512();
		tmp_href.Print_to_bfr(tmp, page.Wiki().Gui_mgr().Cfg_browser().Link_hover_full());
		gui_wtr.Prog_direct(tmp.XtoStrAndClear());
		tmp.Mkr_rls();
	}
	private void Exec_html_box_focus_previous_or_firstHeading() {
		String html_doc_pos = page.DocPos();
		if (html_doc_pos == null) {
			String auto_focus_id = app.Gui_mgr().Html_mgr().Auto_focus_id();
			if (String_.Len_eq_0(auto_focus_id)) return;		// don't focus anything
			if (String_.Eq(auto_focus_id, " first_anchor"))	// NOTE: HTML 4/5 do not allow space as id; XOWA using space here to create a unique_key that will never collide with any id
				html_box.Html_doc_body_focus();	// NOTE: will focus body if content-editable, or first_anchor otherwise
			else
				html_box.Html_elem_focus(auto_focus_id);
		}
		else if (String_.Eq(html_doc_pos, Xog_history_itm.Html_doc_pos_toc))	// NOTE: special case to handle TOC clicks; DATE:2013-07-17
			this.Exec_html_box_select_by_id("toc");
		else
			html_box.Html_window_vpos_(html_doc_pos);
	}
	public void Exec_page_refresh() {
		page.DocPos_(html_box.Html_window_vpos());
		Exec_html_box_show_text(page);
		if (page.Url().Anchor_str() == null)
			GfoInvkAble_.InvkCmd(async_cmd, Invk_html_box_focus_previous_or_firstHeading);
		else
			GfoInvkAble_.InvkCmd_val(async_cmd, Invk_html_box_select_by_id, page.Url().Anchor_str());
		Exec_reload_imgs();
	}
	private void Exec_search() {
		Exec_url_exec(app.Gui_mgr().Win_opts().Search_box_fmtr().Bld_str_many(search_box.Text()));
	}
	private void Exec_page_reload_imgs() {
		if (page.Url().Anchor_str() != null) GfoInvkAble_.InvkCmd_val(async_cmd, Invk_html_box_select_by_id, page.Url().Anchor_str());
		if (gui_wtr.Canceled()) {gui_wtr.Prog_none(GRP_KEY, "imgs.done", ""); app.Log_wtr().Queue_enabled_(false); return;}
		int xfer_len = 0;
		xfer_len = page.File_queue().Count();
		if (xfer_len > 0){
			gui_wtr.Prog_one(GRP_KEY, "imgs.download", "downloading images: ~{0}", xfer_len);
			page.File_queue().Exec(Xof_exec_tid.Tid_wiki_page, gui_wtr, page.Wiki());
		}
		xfer_len = page.File_math().Count();
		if (xfer_len > 0){
			gui_wtr.Prog_one(GRP_KEY, "imgs.math", "generating math: ~{0}", xfer_len);
			for (int i = 0; i < xfer_len; i++) {
				if (gui_wtr.Canceled()) {gui_wtr.Prog_none(GRP_KEY, "imgs.done", ""); app.Log_wtr().Queue_enabled_(false); return;}
				Xof_math_itm itm = (Xof_math_itm)page.File_math().FetchAt(i);
				String queue_msg = gui_wtr.Prog_many(GRP_KEY, "imgs.math.msg", "generating math ~{0} of ~{1}: ~{2}", i + ListAdp_.Base1, xfer_len, String_.new_utf8_(itm.Math()));
				app.File_mgr().Math_mgr().MakePng(itm.Math(), itm.Hash(), itm.Png_url(), queue_msg);
				SizeAdp size = app.File_mgr().Img_mgr().Wkr_query_img_size().Exec(itm.Png_url());
				gui_wtr.Html_img_update("xowa_math_img_" + itm.Id(), itm.Png_url().To_http_file_str(), size.Width(), size.Height());
				gui_wtr.Html_elem_delete("xowa_math_txt_" + itm.Id());
//					gui_wtr.Html_atr_set("xowa_math_txt_" + itm.Id(), "style", "display:none");
			}
			page.File_math().Clear();			
		}
		page.Html_cmd_mgr().Exec(app, gui_wtr, page);
		page.Wiki().Ctx().Tab().Redlinks_mgr().Redlink(page.Wiki(), this);
		gui_wtr.Prog_none(GRP_KEY, "imgs.done", "");
		app.Log_wtr().Queue_enabled_(false);
	}
	private void Exec_navigate(String href) {
		Xoa_url url = Xto_url(href);
		if (url != null) Exec_url_exec(url);
	}
	public void Exec_exit() {
		if (!app.Term_cbk()) return; // NOTE: exit called by keyboard shortcut, or exit link; must call Term_cbk manually; Alt-F4/X button will call Term_cbk in closing event
		app.Gui_mgr().Kit().Kit_term();
	}
	private void Exec_page_edit_save(boolean draft) {
		if (cur_view_tid != Xoh_wiki_article.Tid_view_edit) return;
		byte[] new_text = Eval_elem_value_edit_box_bry();
//			new_text = ByteAry_.Replace(new_text, Xob_xml_parser_.Bry_tab, Xob_xml_parser_.Bry_tab_ent);	// DATE:2013-04-26; commented out; no need to actually force tabs from edit page to show up as &09;
		if (page.Page_tid() == Xoa_page.Tid_create) {
			page.Wiki().Db_mgr().Save_mgr().Data_create(page.Page_ttl(), new_text);
			page.Page_tid_normal_();	// set to normal so that next save does not try to create
		}
		else
			page.Wiki().Db_mgr().Save_mgr().Data_update(page, new_text);
		page.Data_raw_(new_text);
		page.Wiki().ParsePage_root(page, true);			// refresh html
		if (page.Page_ttl().Ns().Id_tmpl()) {	// clear cache (if template)
			app.Tmpl_result_cache().Clear();	
			page.Wiki().Cache_mgr().Free_mem_all();
		}
		if (!draft) {
			page.Data_preview_(ByteAry_.Empty);
			Xoa_page stack_page = history_mgr.Cur_page(page.Wiki());	// NOTE: must be to CurPage() else changes will be lost when going Bwd,Fwd
			stack_page.Data_raw_(page.Data_raw());						// NOTE: overwrite with "saved" changes
			stack_page.Wiki().ParsePage_root(page, true);				// NOTE: must reparse page if (a) Edit -> Read; or (b) "Options" save
			Exec_page_view(Xoh_wiki_article.Tid_view_read);
//				history_mgr.GoBwd();		// NOTE: must pop current version off stack else call below noops since page is already on stack; note that old page ref will still have old data
//				history_mgr.Add(page);
		}
		gui_wtr.Prog_one(GRP_KEY, "edit.save", "saved page ~{0}", String_.new_utf8_(page.Page_ttl().Full_txt_raw()));
		if (!draft)
			Exec_reload_imgs();
	}
	private void Exec_page_edit_rename() {
		if (ByteAry_.Eq(page.Page_ttl().Page_db(), Xoa_page.Bry_main_page)) {
			gui_wtr.Warn_many(GRP_KEY, "edit.rename.invalid_for_main_page", "The Main Page cannot be renamed");
			return;
		}
		if (cur_view_tid != Xoh_wiki_article.Tid_view_edit) {
			gui_wtr.Warn_many(GRP_KEY, "edit.rename.invalid_unless_edit", "Rename cannot occur unless in edit mode");
			return;
		}
		byte[] new_text = ByteAry_.new_utf8_(Eval_elem_value(Id_xowa_edit_rename_box));
		if (ByteAry_.Len_eq_0(new_text)) return;
		new_text = Xoa_ttl.Replace_spaces(new_text);	// ttls cannot have spaces; only underscores
		Xoa_ttl new_ttl = Xoa_ttl.parse_(page.Wiki(), new_text);
		if (new_ttl.Ns().Id() != Xow_ns_.Id_main) {
			gui_wtr.Warn_many(GRP_KEY, "edit.rename.invalid_for_ns", "The new page name must remain in the same namespace");
			return;
		}
		page.Wiki().Db_mgr().Save_mgr().Data_rename(page, new_text);
		page.Page_ttl_(Xoa_ttl.parse_(page.Wiki(), ByteAry_.Add(page.Page_ttl().Ns().Name_db_w_colon(), new_text)));
		Exec_page_view(Xoh_wiki_article.Tid_view_read);
		gui_wtr.Prog_one(GRP_KEY, "edit.rename.done", "renamed page to {0}", String_.new_utf8_(page.Page_ttl().Full_txt_raw()));
	}
	private void Exec_page_edit_preview() {
		if (cur_view_tid != Xoh_wiki_article.Tid_view_edit) return;
		byte[] new_raw = Eval_elem_value_edit_box_bry();
		Xow_wiki wiki = page.Wiki();
		Xoa_page new_page = new Xoa_page(wiki, page.Page_ttl()).Page_id_(page.Page_id());	// NOTE: page_id needed for sqlite (was not needed for xdat)
		new_page.Data_raw_(new_raw);
		wiki.ParsePage_root(new_page, true);		// refresh html
		ByteAryBfr tmp_bfr = app.Utl_bry_bfr_mkr().Get_m001();
		Xou_output_wkr wkr = wiki.Html_mgr().Output_mgr().Wkr(Xoh_wiki_article.Tid_view_read);
		wkr.Page_(new_page);
		wkr.XferAry(tmp_bfr, 0);
		byte[] new_htm = tmp_bfr.Mkr_rls().XtoAryAndClear();
		if (page.Page_ttl().Ns().Id_tmpl()) {	// clear cache (if template)
			app.Tmpl_result_cache().Clear();	
			wiki.Cache_mgr().Defn_cache().Free_mem_all();
		}
		new_page.Data_preview_(new_htm);
		page = new_page;
		Exec_page_view(Xoh_wiki_article.Tid_view_edit);
		Exec_html_box_select_by_id(Html_id_first_heading);
		Exec_reload_imgs();
	}
	private void Exec_url_box_paste_and_go() {
		String s = ClipboardAdp_.GetText();
		url_box.Text_(s);
		Exec_url_exec(s);
	}
	boolean find_box_dir_fwd = true, find_box_case_match = false, find_box_wrap_find = true;
	boolean Exec_find_box_find() {return html_box.Html_doc_find(cur_view_tid == Xoh_wiki_article.Tid_view_read ? Gfui_html.Elem_id_body : Id_xowa_edit_data_box, find_box.Text(), find_box_dir_fwd, find_box_case_match, find_box_wrap_find);}
	public void Exec_url_exec(String s) {
		byte[] bry = ByteAry_.new_utf8_(s);
		byte[] fmt = app.Url_alias_mgr().Fmt_or_null(bry);
		if (fmt != null) bry = fmt;
		Xoa_url tmp_url = new Xoa_url();
		Xoa_url_parser.Parse_url(tmp_url, app, page.Wiki(), bry, 0, bry.length);
		Exec_url_exec(tmp_url);
	}
	private void Exec_cancel_wait() {
		while (reload_imgs_thread.IsAlive()) {
			ThreadAdp_.Sleep(10);
		}
		page.File_queue().Clear();
		GfoInvkAble_.InvkCmd(sync_cmd, Invk_cancel_restart);
	}
	private void Exec_cancel_restart() {
		Exec_url_exec(restart_url);		
	}	private Xoa_url restart_url;
	private void Exec_url_exec(Xoa_url url) {			
		if (reload_imgs_thread.IsAlive()) {
			restart_url = url;
			gui_wtr.Canceled_y_();
			app.File_mgr().Download_mgr().Download_wkr().Download_xrg().Prog_cancel_y_();
			ThreadAdp_.invk_(this, Invk_cancel_wait).Start();
			return;
		}
		app.Log_wtr().Queue_enabled_(true);
		app.Utl_bry_bfr_mkr().Clear();
		app.Gui_wtr().Clear();
		app.Gui_mgr().Search_suggest_mgr().Cancel();
		if (Env_.System_memory_free() < app.Sys_cfg().Free_mem_when()) app.Free_mem(false);			
		Xoa_page old_page = page;
		Xoa_page new_page = Xoa_page.Null;			
		if (url.Anchor_str() != null && url.Eq_page(old_page.Url()) && url.Args().length == 0)	// skip page load and just jump to anchor if (a) new.anchor != null (has anchor) (b) new.page == old.page (same page) (c) url.Args() == 0 (no args; needed for Category:A?from=b#mw-pages)
			GfoInvkAble_.InvkCmd_val(async_cmd, Invk_html_box_select_by_id, url.Anchor_str());			
		else {
			Xow_wiki wiki = url.Wiki();
			wiki.Init_assert();	// NOTE: must assert that wiki is inited before parsing; occurs b/c lang (with lang-specific ns) are only loaded on init, and parse Xoa_ttl.parse_ will fail below; EX:pt.wikipedia.org/wiki/Wikipedia:Página principal
			wiki.View_data().Clear();
			Xoa_ttl ttl = Xoa_ttl.parse_(wiki, url.Page_bry());
			gui_wtr.Prog_one(GRP_KEY, "view.load", "loading: ~{0}", String_.new_utf8_(ttl.Raw()));
			try {
				new_page = wiki.GetPageByTtl(url, ttl);
				wiki.Ctx().Page_(new_page);
			}
			catch (Exception e) {
				Xoa_page fail_page = wiki.Data_mgr().Get_page(ttl, false);
				this.html_box.Html_doc_html_(String_.new_utf8_(fail_page.Data_raw()));
				gui_wtr.Prog_many(GRP_KEY, "view.load.fail", "fatal error: ~{0} ~{1}", String_.new_utf8_(url.Raw()), Err_.Message_gplx_brief(e));
				app.Log_wtr().Queue_enabled_(false);
				return;
			}
			if (new_page.Missing()) {
				if (ByteAry_.Eq(url.Page_bry(), Xoa_page.Bry_main_page)) {	// HACK: handle missing Main Page; EX: nl.wikivoyage.org
					url.Page_bry_(wiki.Props().Main_page());
					ttl = Xoa_ttl.parse_(wiki, url.Page_bry());
					new_page = wiki.GetPageByTtl(url, ttl);
					wiki.Ctx().Page_(new_page);
				}
				if (new_page.Missing()) {
					if (wiki.Db_mgr().Save_mgr().Create_enabled()) {
						new_page = Xoa_page.blank_page_(wiki, ttl);
						cur_view_tid = Xoh_wiki_article.Tid_view_edit;
						history_mgr.Add(new_page);	// NOTE: must put new_page on stack b/c pressing back will pop whatever's last
						Exec_show_wiki_page(new_page, false);
					}
					else {
						if (new_page.Redirect_list().Count() > 0)
							gui_wtr.Prog_many(GRP_KEY, "view.find.fail", "could not find: ~{0} (redirected from ~{1})", String_.new_utf8_(new_page.Url().Page_bry()), String_.new_utf8_((byte[])new_page.Redirect_list().FetchAt(0)));
						else
							gui_wtr.Prog_one(GRP_KEY, "view.find.fail", "could not find: ~{0}", String_.new_utf8_(url.Raw()));
					}
					app.Log_wtr().Queue_enabled_(false);
					return;
				}
			}
			if (!new_page.Url_redirected()) new_page.Url_(url);	// NOTE: handle redirect from commons
			if (new_page.Page_ttl().Anch_bgn() != ByteAry_.NotFound) new_page.Url().Anchor_bry_(new_page.Page_ttl().Anch_txt());	// NOTE: occurs when page is a redirect to an anchor; EX: w:Duck race -> Rubber duck#Races
			history_mgr.Add(new_page);
			Exec_show_wiki_page(new_page, true);
			if (page.Wiki().Cfg_history().Enabled()) app.User().History_mgr().Add(new_page);
			gui_wtr.Prog_none(GRP_KEY, "view.render.bgn", "rendering html");
			Exec_reload_imgs();
		}
	}	ThreadAdp reload_imgs_thread = ThreadAdp.Null;
	public void Exec_reload_imgs() {
		reload_imgs_thread = ThreadAdp_.invk_(this, Invk_page_reload_imgs).Start();
	}
	private void Exec_page_dbg(byte view_tid) {
		Xow_wiki wiki = page.Wiki();
//			if (ByteAry_.Eq(wiki.Key_bry(), Xow_wiki_type_.Key_home_bry)) {	// OBSOLETE: code to quickly switch to en.wikipedia.org for testing; should probably remove, but may be useful for future testing
//				wiki = app.Wiki_mgr().Default_wiki();
//			}
		wiki.Ctx().Defn_trace().Clear(); // FUTURE: moveme
		wiki.Ctx().Defn_trace_(Xot_defn_trace_dbg._);
		Xoa_ttl ttl = page.Page_ttl();
		Xoa_page new_page = new Xoa_page(page.Wiki(), ttl);
		byte[] data = Eval_elem_value_edit_box_bry();
		new_page.Data_raw_(data);
		wiki.ParsePage_root(new_page, true);
		ByteAryBfr bfr = app.Utl_bry_bfr_mkr().Get_m001();
		bfr.Add(new_page.Root().Root_src());
		wiki.Ctx().Defn_trace().Print(data, bfr);
		new_page.Data_raw_(bfr.Mkr_rls().XtoAryAndClear());
		byte old = cur_view_tid;
		cur_view_tid = view_tid;
		Exec_show_wiki_page(new_page, false);
		history_mgr.Add(new_page);
		cur_view_tid = old;
	}		
	private void Exec_app_exec_cfg() {
		GfsCore._.ExecText(Eval_elem_value_edit_box_str());
		gui_wtr.Prog_none(GRP_KEY, "cfg.end", "applied cfg");
	}
	public void Exec_page_reload() {
		page = history_mgr.Cur_page(page.Wiki());	// NOTE: must be to CurPage() else changes will be lost when going Bwd,Fwd
		page.Wiki().ParsePage_root(page, true);		// NOTE: must reparse page if (a) Edit -> Read; or (b) "Options" save
		Exec_page_refresh();
	}
	public void Exec_page_view(byte v) {
		if (cur_view_tid == Xoh_wiki_article.Tid_view_edit && v == Xoh_wiki_article.Tid_view_read && !page.Missing())
			Exec_page_reload(); // NOTE: if moving from "Edit" to "Read", reload page (else Preview changes will still show)
		cur_view_tid = v;
		if (page.Missing()) return;
		Exec_show_wiki_page(page, false);
	}
	public void Exec_page_stack(Xoa_page new_page) {
		if (new_page.Missing()) return;
		boolean new_page_is_same = ByteAry_.Eq(page.Page_ttl().Full_txt(), new_page.Page_ttl().Full_txt());
		Exec_show_wiki_page(new_page, true, new_page_is_same);
		Exec_reload_imgs();
	}
	private void Exec_show_wiki_page(Xoa_page new_page, boolean reset_to_read) {Exec_show_wiki_page(new_page, reset_to_read, false);}
	private void Exec_show_wiki_page(Xoa_page new_page, boolean reset_to_read, boolean new_page_is_same) {
		if (reset_to_read) cur_view_tid = Xoh_wiki_article.Tid_view_read;
		if (new_page.Url().Action_is_edit()) cur_view_tid = Xoh_wiki_article.Tid_view_edit;
		if (page != null && !new_page_is_same) {	// if new_page_is_same, don't update DocPos; will "lose" current position
			page.DocPos_(html_box.Html_window_vpos());
			history_mgr.Update_html_doc_pos(page);	// HACK: old_page is already in stack, but need to update its hdoc_pos
		}
		gui_wtr.Prog_none(GRP_KEY, "show.locate", "locating images");
		if (!Exec_html_box_show_text(new_page)) return;
		page = new_page;
		url_box.Text_(app.Url_parser().Build_str(new_page.Url()));
		win.Text_(String_.new_utf8_(ByteAry_.Add(new_page.Page_ttl().Full_txt(), Xowa_titleBar_suffix)));
		Xol_font_info lang_font = page.Wiki().Lang().Gui_font();
		if (lang_font.Name() == null) lang_font = app.Gui_mgr().Win_opts().Font();
		Exec_window_font_changed(lang_font);
		html_box.Focus();
		gui_wtr.Prog_none(GRP_KEY, "show.end", "");
		if (cur_view_tid == Xoh_wiki_article.Tid_view_read)
			GfoInvkAble_.InvkCmd(async_cmd, Invk_html_box_focus_previous_or_firstHeading);
	}
	public void Exec_window_font_changed(Xol_font_info itm_font) {
		FontAdp gui_font = url_box.TextMgr().Font();
		if (!itm_font.Eq(gui_font)) {
			FontAdp new_font = itm_font.XtoFontAdp();
			url_box.TextMgr().Font_(new_font);
			find_box.TextMgr().Font_(new_font);
			prog_box.TextMgr().Font_(new_font);
		}
	}
	public void Exec_sync(String cmd) {GfoInvkAble_.InvkCmd(sync_cmd, cmd);}
	public void Exec_async(String cmd) {GfoInvkAble_.InvkCmd(async_cmd, cmd);}
	public void Exec_async_arg(String cmd, String arg) {GfoInvkAble_.InvkCmd_val(async_cmd, cmd, arg);}
	boolean Exec_html_box_show_text(Xoa_page page) {
		byte[] bry = page.Wiki().Html_mgr().Output_mgr().Gen(page, cur_view_tid);
		html_box.Html_doc_html_(String_.new_utf8_(bry));
		html_box.Html_doc_body_focus();
		if (cur_view_tid == Xoh_wiki_article.Tid_view_read)
			page.Root().Data_htm_(bry);
		return true;
	}
	public void Init(Gfui_kit kit) {
		win = kit.New_win_app("win");
		win.Focus_able_(false);
		redirect_mgr.Bind("Xog_win_dynamic", win, this);
//			kit.Main_win_(win);
		async_cmd = win.Kit().New_cmd_async(this);
		sync_cmd = win.Kit().New_cmd_sync(this);

		Io_url img_dir = app.User().Fsys_mgr().App_img_dir().GenSubDir_nest("window", "chrome");
		FontAdp ui_font = app.Gui_mgr().Win_opts().Font().XtoFontAdp();
		go_bwd_btn			= new_btn(kit, win, img_dir, "go_bwd_btn", "go_bwd.png", Xol_msg_itm_.Id_xowa_window_go_bwd_btn_tooltip);
		go_fwd_btn			= new_btn(kit, win, img_dir, "go_fwd_btn", "go_fwd.png", Xol_msg_itm_.Id_xowa_window_go_fwd_btn_tooltip);
		url_box				= new_txt(kit, win, ui_font, "url_box", Xol_msg_itm_.Id_xowa_window_url_box_tooltip, true);
		url_exec_btn		= new_btn(kit, win, img_dir, "url_exec_btn", "url_exec.png", Xol_msg_itm_.Id_xowa_window_url_btn_tooltip);
		search_box			= new_txt(kit, win, ui_font, "search_box", Xol_msg_itm_.Id_xowa_window_search_box_tooltip, true);
		search_exec_btn		= new_btn(kit, win, img_dir, "search_exec_btn", "search_exec.png", Xol_msg_itm_.Id_xowa_window_search_btn_tooltip);
		html_box			= kit.New_html_box("html_box", win);
		find_close_btn		= new_btn(kit, win, img_dir, "find_close_btn", "find_close.png", Xol_msg_itm_.Id_xowa_window_find_close_btn_tooltip);
		find_box			= new_txt(kit, win, ui_font, "find_box", Xol_msg_itm_.Id_xowa_window_find_box_tooltip, true);
		find_fwd_btn		= new_btn(kit, win, img_dir, "find_fwd_btn", "find_fwd.png", Xol_msg_itm_.Id_xowa_window_find_fwd_btn_tooltip);
		find_bwd_btn		= new_btn(kit, win, img_dir, "find_bwd_btn", "find_bwd.png", Xol_msg_itm_.Id_xowa_window_find_bwd_btn_tooltip);
		prog_box			= new_txt(kit, win, ui_font, "prog_box", Xol_msg_itm_.Id_xowa_window_prog_box_tooltip, false);
		note_box			= new_txt(kit, win, ui_font, "note_box", Xol_msg_itm_.Id_xowa_window_info_box_tooltip, false);

		html_box.Html_js_enabled_(app.Gui_mgr().Html_mgr().Javascript_enabled());
		html_box.Html_js_cbks_add("xowa_exec", js_cbk);
		gui_wtr.Ini(kit, this);

		html_box.Html_invk_src_(this);
		GfoEvMgr_.SubSame(this, Gfui_html.Evt_location_changed, this);
		GfoEvMgr_.SubSame(this, Gfui_html.Evt_location_changing, this);
		GfoEvMgr_.SubSame(this, Gfui_html.Evt_link_hover, this);
		GfoEvMgr_.SubSame(win, Gfui_html.Evt_win_resized, this);
		GfoEvMgr_.Sub(app.Gui_mgr().Win_opts().Font(), Xol_font_info.Font_changed, this, Invk_window_font_changed);

		win_mgr.Init(this, app, win, url_box, prog_box, find_box, html_box);
		app.Usr_dlg().Ui_wkr_(new Gfo_usr_dlg_ui_swt(kit, prog_box, note_box, note_box, app.Gui_mgr().Win_opts()));
		this.gui_wtr = new Gfo_usr_dlg_xowa();
		gui_wtr.Ini(kit, this);
		gui_wtr.Ui_wkr_(app.Usr_dlg().Ui_wkr());
	}	private Xog_win_view_adp win_mgr = new Xog_win_view_adp();
	private void Exec_context_copy() {
		win.Kit().Clipboard().Copy(Html_doc_selected_get());
	}
	private void Exec_context_find() {
		app.Gui_mgr().Layout().Find_show();
		find_box.Text_(Html_doc_selected_get());
	}
	String Html_doc_selected_get() {return html_box.Html_doc_selected_get(page.Wiki().Key_str(), String_.new_utf8_(page.Page_ttl().Page_txt()));}
	public Xog_win_view_adp Win_adp() {return win_mgr;}
	GfuiBtn new_btn(Gfui_kit kit, GfuiWin win, Io_url img_dir, String id, String file, int tiptext_id) {
		GfuiBtn rv = kit.New_btn(id, win);
		rv.Btn_img_(kit.New_img_load(img_dir.GenSubFil(file))).TipText_(new_tiptext(tiptext_id));
		return rv;
	}
	GfuiTextBox new_txt(Gfui_kit kit, GfuiWin win, FontAdp ui_font, String id, int tiptext_id, boolean border_on) {
		GfuiTextBox rv = kit.New_text_box(id, win, KeyVal_.new_(GfuiTextBox.CFG_border_on_, border_on));
		rv.TextMgr().Font_(ui_font);
		rv.TipText_(new_tiptext(tiptext_id));
		return rv;
	}
	String new_tiptext(int id) {
		return String_.new_utf8_(app.User().Lang().Msg_mgr().Val_by_id(app.User().Wiki(), id));
	}
	public void Launch() {win_mgr.Launch();}
	public byte[] Exec_app_retrieve_by_url(String url_str, String output_str) {
		boolean output_html = String_.Eq(output_str, "html");
		Xoa_url url = new Xoa_url();
		byte[] url_bry = ByteAry_.new_utf8_(url_str);
		Xow_wiki home_wiki = app.User().Wiki();
		Xoa_ttl ttl = Xoa_ttl.parse_(home_wiki, Xoa_page.Bry_main_page);
		page = Xoa_page.blank_page_(home_wiki, ttl);
		Xoa_url_parser.Parse_url(url, app, page.Wiki(), url_bry, 0, url_bry.length);
		return Exec_app_retrieve_core(url, output_html);
	}
	byte[] Exec_app_retrieve_by_href(String href, boolean output_html) {return Exec_app_retrieve_core(Xto_url(href), output_html);}
	byte[] Exec_app_retrieve_core(Xoa_url url, boolean output_html) {
		if (url == null) return ByteAry_.new_ascii_("missing");
		Xoa_ttl ttl = Xoa_ttl.parse_(url.Wiki(), url.Page_bry());
		Xoa_page new_page = url.Wiki().GetPageByTtl(url, ttl);
		if (new_page.Missing()) {return ByteAry_.Empty;}
		page = new_page;
		history_mgr.Add(new_page);			
		return output_html
			? url.Wiki().Html_mgr().Output_mgr().Gen(new_page, cur_view_tid)
			: new_page.Data_raw()
			;
	}
	public Xoa_url Xto_url(String href) {
		if (href == null) return null;	// text is not link; return;
		byte[] href_bry = ByteAry_.new_utf8_(href);
		app.Href_parser().Parse(tmp_href, href_bry, page.Wiki(), page.Page_ttl().Page_url());
		switch (tmp_href.Tid()) {
			case Xoh_href.Tid_null: return null;
			case Xoh_href.Tid_xowa: {
				try {
					app.Gfs_mgr().Run_str(String_.new_utf8_(tmp_href.Page()));
				}
				catch (Exception e) {	// NOTE: must catch exception else it will bubble to SWT browser and raise secondary exception of xowa is not a registered protocol
					app.Gui_mgr().Kit().Ask_ok(GRP_KEY, "xto_url.fail", Err_.Message_gplx_brief(e));
				}
				return null;
			}
			case Xoh_href.Tid_xcmd: {
				byte[] xowa_href_bry = tmp_href.Page();
				int xowa_href_bry_len = xowa_href_bry.length;
				int slash_pos = ByteAry_.FindFwd(xowa_href_bry, Byte_ascii.Slash); if (slash_pos == ByteAry_.NotFound) slash_pos = xowa_href_bry_len;
				byte[] xowa_cmd_bry = ByteAry_.Mid(xowa_href_bry, 0, slash_pos);
				String xowa_cmd_str = String_.new_utf8_(xowa_cmd_bry);
				GfoMsg m = GfoMsg_.new_cast_(xowa_cmd_str);
				if (String_.Eq(xowa_cmd_str, Invk_eval))
					m.Add("cmd", String_.new_utf8_(xowa_href_bry, slash_pos + 1, xowa_href_bry_len));
				this.Invk(GfsCtx.new_(), 0, xowa_cmd_str, m);
				return null;
			}
			case Xoh_href.Tid_http: {
				app.Fsys_mgr().App_mgr().Exec_view_web(tmp_href.Raw());
				return null;
			}
			case Xoh_href.Tid_anchor: {
				Exec_html_box_select_by_id(String_.new_utf8_(tmp_href.Anchor()));
				return null;
			}
			case Xoh_href.Tid_file: {
				href_bry = app.Url_converter_url().Decode(href_bry);
				String url_str = Io_url.parse_http_file(String_.new_utf8_(href_bry), Op_sys.Cur().Tid_is_wnt());
				Io_url href_url = Io_url_.new_fil_(url_str);
				String xowa_ttl = page.Wiki().Gui_mgr().Cfg_browser().Content_editable()
					? html_box.Html_active_atr_get_str(gplx.xowa.html.Xoh_html_tag.Nde_xowa_title_str, null)
					: Xoh_dom_.Title_by_href(app.Url_converter_comma(), app.Utl_bry_bfr_mkr().Get_b512().Mkr_rls(), href_bry, ByteAry_.new_utf8_(html_box.Html_doc_html()));
				if (!Io_mgr._.ExistsFil(href_url)) {
					Xof_xfer_itm xfer_itm = new Xof_xfer_itm();
					byte[] title = app.Url_converter_url().Decode(ByteAry_.new_utf8_(xowa_ttl));
					xfer_itm.Atrs_by_lnki(Xop_lnki_type.Id_none, -1, -1, -1, -1).Atrs_by_ttl(title, ByteAry_.Empty);
					page.Wiki().File_mgr().Find_meta(xfer_itm);
					page.File_queue().Clear();
					page.File_queue().Add(xfer_itm);	// NOTE: set elem_id to "impossible" number, otherwise it will auto-update an image on the page with a super-large size; [[File:Alfred Sisley 062.jpg]]
					page.Wiki().File_mgr().Repo_mgr().Xfer_mgr().Force_orig_y_();
					page.File_queue().Exec(Xof_exec_tid.Tid_viewer_app, gui_wtr, page.Wiki());
					page.Wiki().File_mgr().Repo_mgr().Xfer_mgr().Force_orig_n_();
				}
				if (Io_mgr._.ExistsFil(href_url)) {
					ProcessAdp media_player = app.Fsys_mgr().App_mgr().App_by_ext(href_url.Ext());
					media_player.Run(href_url);
				}
				return null;
			}
		}
		// HACK: code to reparse href in wiki; EX: (1) goto w:Anything; (2) click on "anything" in wikt; "anything" will be parsed by en.wiki's rules, not en.wikt; DATE:2013-01-30
		Xow_wiki tmp_href_wiki = app.Wiki_mgr().Get_by_key_or_make(tmp_href.Wiki());
		tmp_href_wiki.Init_assert();
		app.Href_parser().Parse(tmp_href, href_bry, tmp_href_wiki, page.Page_ttl().Page_url());
		Xoa_url rv = new Xoa_url();
		app.Url_parser().Parse(rv, href_bry);
		rv.Wiki_bry_(tmp_href.Wiki());
		if (!rv.Redirect_force()) {					// HACK: the href's parser page is accurate. it will pick up "en.wiki.org" and "en.wiki.org/wiki" as references to main page; however it does not handle query parameters, and will return "A?redirect=no" as page name; the url parser handles query params
//				if (!page.Page_ttl().Ns().Id_ctg()) {	
			Xoa_ttl tmp_ttl = Xoa_ttl.parse_(tmp_href_wiki, tmp_href.Page());	// HACK: make ttl to get "real title" with subpage; else links like en.wikipedia.org/wiki/Page:A/B will not work  DATE:2013-06-22
			rv.Page_bry_(tmp_ttl.Full_txt_wo_qarg());	// HACK: Category has args like ?from=; do not set Page else Page will be "Category:A?from=B" (needs to fix this hack); DATE:2013-05-01; DATE:2013-06-22
//				}
//				else {									// HACK: remove anchor from args
				for (int i = 0; i < rv.Args().length; i++) {
					Gfo_url_arg arg = rv.Args()[i];
					int anchor_pos = ByteAry_.FindBwd(arg.Val_bry(), Byte_ascii.Hash);	// NOTE: must .FindBwd to handle Category args like de.wikipedia.org/wiki/Kategorie:Begriffskl%C3%A4rung?pagefrom=#::12%20PANZERDIVISION#mw-pages; DATE:2013-06-18
					if (anchor_pos != ByteAry_.NotFound)
						arg.Val_bry_(ByteAry_.Mid(arg.Val_bry(), 0, anchor_pos));
				}
//				}
			if (tmp_href.Anchor() != null) {
				byte[] anchor_bry = app.Url_converter_id().Encode(tmp_href.Anchor());	// reencode for anchors (which use . encoding, not % encoding); EX.WP: Enlightenment_Spain#Enlightened_despotism_.281759%E2%80%931788.29
				rv.Anchor_bry_(anchor_bry);
			}
		}
		rv.Wiki_(app.Wiki_mgr().Get_by_key_or_make(tmp_href.Wiki()));
		return rv;
	}	private Xoh_href tmp_href = new Xoh_href();
	byte[] Eval_elem_value_edit_box_bry() {return ByteAry_.new_utf8_(Eval_elem_value_edit_box_str());}
	String Eval_elem_value_edit_box_str() {return html_box.Html_elem_atr_get_str(Id_xowa_edit_data_box, Gfui_html.Atr_value);}
	String Eval_elem_value(String elem_id) {return html_box.Html_elem_atr_get_str(elem_id, Gfui_html.Atr_value);}		
	private GfoInvkAble async_cmd, sync_cmd;
	private byte cur_view_tid = Xoh_wiki_article.Tid_view_read;
	public static final String Id_xowa_edit_data_box = "xowa_edit_data_box", Id_xowa_edit_rename_box = "xowa_edit_rename_box", Html_id_first_heading = "firstHeading";
	public static final byte[] Xowa_titleBar_suffix = ByteAry_.new_ascii_(" - XOWA");
	static final String GRP_KEY = "xowa.gui.win";
}
class Xog_win_utl_ {
	public static String Parse_evt_location_changing(String v) { // EX: about:blank#anchor -> anchor
		int pos = String_.FindFwd(v, gplx.xowa.html.Xoh_html_tag.Const_anchor); if (pos == ByteAry_.NotFound) return null;
		return String_.Mid(v, pos + 1);
	}
}
