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
package gplx.gfui;
import gplx.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
class Swt_html implements Gxw_html, Swt_control, FocusListener {
	private Swt_html_lnr_location lnr_location; private Swt_html_lnr_status lnr_status;
	public Swt_html(Swt_kit kit, GxwElem ownerElem, KeyValHash ctorArgs) {
		this.kit = kit;
		lnr_location = new Swt_html_lnr_location(this);
		lnr_status = new Swt_html_lnr_status(this);
		Composite owner = ((Swt_win)ownerElem).UnderShell();
		int browser_type = Swt_html.Browser_tid_none;
		Object browser_type_obj = ctorArgs.FetchValOr(Swt_kit.Cfg_Html_BrowserType, null);
		if (browser_type_obj != null) browser_type = Int_.cast_(browser_type_obj);
		browser = new Browser(owner, browser_type);
		core = new Swt_core_cmds_html(this, browser);
		browser.addKeyListener(new Swt_KeyLnr(this));
		browser.addMouseListener(new Swt_html_lnr_mouse(this, browser, kit));
		browser.addLocationListener(lnr_location);
		browser.addProgressListener(new Swt_html_lnr_progress(this));
		browser.addStatusTextListener(lnr_status);
		browser.addFocusListener(this);
		browser.addTitleListener(new Swt_html_lnr_title(this));
//		browser.addTraverseListener(new Swt_html_lnr_Traverse(this));
	}
	public Swt_kit Kit() {return kit;} private Swt_kit kit;
	@Override public Control Under_control() {return browser;} private Browser browser;
	public String 		Html_doc_html() 												{return Eval_script_as_str(kit.Html_cfg().Doc_html());}
	public void 		Html_doc_html_(String s) 										{browser.setText(s);}	// DBG: Io_mgr._.SaveFilStr(Io_url_.new_fil_("C:\\temp.txt"), s) 
	public String 		Html_doc_selected_get(String site, String page) 				{return Eval_script_as_str(kit.Html_cfg().Doc_selected_get(site, page));}
	public void 		Html_doc_body_focus() 											{Eval_script_as_exec(kit.Html_cfg().Doc_body_focus());}
	public String 		Html_elem_atr_get_str(String elem_id, String atr_key) 			{return Eval_script_as_str(kit.Html_cfg().Elem_atr_get(elem_id, atr_key));}
	public boolean 		Html_elem_atr_get_bool(String elem_id, String atr_key) 			{return Bool_.parse_((String)Eval_script(kit.Html_cfg().Elem_atr_get_toString(elem_id, atr_key)));}
	public Object 		Html_elem_atr_get_obj(String elem_id, String atr_key) 			{return Eval_script(kit.Html_cfg().Elem_atr_get(elem_id, atr_key));}
	public boolean 		Html_elem_atr_set(String elem_id, String atr_key, String atr_val){return Eval_script_as_exec(kit.Html_cfg().Elem_atr_set(elem_id, atr_key, Escape_quotes(atr_val)));}
	public boolean 		Html_elem_atr_set_append(String elem_id, String atr_key, String atr_val)
																						{return Eval_script_as_exec(kit.Html_cfg().Elem_atr_set_append(elem_id, atr_key, Escape_quotes(atr_val)));}
	public boolean 		Html_elem_delete(String elem_id) 								{return Eval_script_as_exec(kit.Html_cfg().Elem_delete(elem_id));}
	public boolean 		Html_elem_replace_html(String id, String html) 					{return Eval_script_as_exec(kit.Html_cfg().Elem_replace_html(id, html));}
	public boolean 		Html_gallery_packed_exec() 										{return Eval_script_as_exec(kit.Html_cfg().Gallery_packed_exec());}
	public boolean 		Html_elem_focus(String elem_id) 								{return Eval_script_as_exec(kit.Html_cfg().Elem_focus(elem_id));}
	public boolean 		Html_elem_scroll_into_view(String id) 							{return Eval_script_as_bool(kit.Html_cfg().Elem_scroll_into_view(Escape_quotes(id)));}
	public String 		Html_window_vpos() 												{return Eval_script_as_str(kit.Html_cfg().Window_vpos());}
	public boolean 		Html_window_print_preview()										{return Eval_script_as_bool(kit.Html_cfg().Window_print_preview());}
	public void 		Html_js_enabled_(boolean v) 									{browser.setJavascriptEnabled(v);}
	public void 		Html_js_cbks_add(String func_name, GfoInvkAble invk) 			{new Swt_html_func(browser, func_name, invk);}
	public String 		Html_js_eval_script(String script) 								{return Eval_script_as_str(script);}
	public boolean Html_elem_img_update(String elem_id, String elem_src, int elem_width, int elem_height) {
		elem_src = Escape_quotes(elem_src);
		return Eval_script_as_bool(kit.Html_cfg().Elem_img_update(elem_id, elem_src, elem_width, elem_height));
	}
	public String Html_active_atr_get_str(String atr_key, String or) {
		Object rv_obj = Eval_script(kit.Html_cfg().Active_atr_get_str(atr_key));
		String rv = (String)rv_obj;
		return rv == null || !eval_rslt.Result_pass() ? or : rv;
	}
	public void Html_js_eval_proc(String proc, String... args) {
		ByteAryFmtr fmtr = kit.Html_cfg().Js_scripts_get(proc);
		String script = fmtr.Bld_str_many(args);
		Eval_script(script);
	}
	public boolean Html_window_vpos_(String v) {
		Gfui_html_cfg.Html_window_vpos_parse(v, scroll_top, node_path);
		return Eval_script_as_exec(kit.Html_cfg().Window_vpos_(node_path.Val(), scroll_top.Val()));
	}	private StringRef scroll_top = StringRef.null_(), node_path = StringRef.null_();
	public boolean Html_doc_find(String elem_id, String find, boolean dir_fwd, boolean case_match, boolean wrap_find) {
		if (String_.Eq(find, String_.Empty)) return false;
		find = String_.Replace(find, "\\", "\\\\");	// escape \ -> \\
		find = String_.Replace(find, "'", "\\'");	// escape ' -> \'; NOTE: \\' instead of \'
		boolean search_text_is_diff = !String_.Eq(find, prv_find_str);
		prv_find_str = find;
		String script = String_.Eq(elem_id, Gfui_html.Elem_id_body)
			? kit.Html_cfg().Doc_find_html(find, dir_fwd, case_match, wrap_find, search_text_is_diff, prv_find_bgn)
			: kit.Html_cfg().Doc_find_edit(find, dir_fwd, case_match, wrap_find, search_text_is_diff, prv_find_bgn);
		Object result_obj = Eval_script(script);
		try  				{prv_find_bgn = (int)Double_.cast_(result_obj);}
		catch (Exception e) {Err_.Noop(e); return false;}
		return true;
	}	private String prv_find_str = ""; private int prv_find_bgn;
	public void Html_invk_src_(GfoEvObj invk) {lnr_location.Host_set(invk); lnr_status.Host_set(invk);}
	private String Escape_quotes(String v) {return String_.Replace(String_.Replace(v, "'", "\\'"), "\"", "\\\"");}
	@Override public GxwCore_base Core() {return core;} GxwCore_base core;
	@Override public GxwCbkHost Host() {return host;} @Override public void Host_set(GxwCbkHost host) {this.host = host;} GxwCbkHost host;
	@Override public String TextVal() {return browser.getText();}
	@Override public void TextVal_set(String v) {browser.setText(v);}
	@Override public void EnableDoubleBuffering() {}
	@Override public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {return GfoInvkAble_.Rv_unhandled;}
	private boolean Eval_script_as_bool(String script) 	{
		Object result_obj = Eval_script(script);
		return eval_rslt.Result_pass() && Bool_.cast_or_(result_obj, false);
	}
	private boolean Eval_script_as_exec(String script) 	{Eval_script(script); return eval_rslt.Result_pass();}
	private String Eval_script_as_str(String script) 	{return (String)Eval_script(script);}
	public Object Eval_script(String script) {
		eval_rslt.Clear();
		try 				{
			eval_rslt.Result_set(browser.evaluate(script));
			return eval_rslt.Result();
		}
		catch (Exception e) {eval_rslt.Error_set(e.getMessage()); 				return eval_rslt.Error();}
	}	private Swt_html_eval_rslt eval_rslt = new Swt_html_eval_rslt();
	@Override public void focusGained(FocusEvent arg0) {
//		if (!focus_acquired && Swt_kit.Html_box_focus_automatically) {
//			browser.forceFocus();
//			focus_acquired = true;
//			HtmlBox_focus();
//		}
	}	//boolean focus_acquired = false;
	@Override public void focusLost(FocusEvent arg0) {
//		focus_acquired = false;
	}
	public static final int
	  Browser_tid_none 		= SWT.NONE
	, Browser_tid_mozilla 	= SWT.MOZILLA
	, Browser_tid_webKit	= SWT.WEBKIT
	;	
}
class Swt_core_cmds_html extends Swt_core_cmds {
	public Swt_core_cmds_html(Swt_html html_box, Control control) {super(control); this.html_box = html_box;} Swt_html html_box;
	@Override public void Focus() {
		if (Focus_able())
			control.forceFocus();
	}
	@Override public void Select_exec() {
		this.Focus();
	}
}
class Swt_html_eval_rslt {
	public void Clear() {error = null; result = null;}
	public boolean Result_pass() {return error == null;}
	public Object Result() {return result;} public void Result_set(Object v) 	{result = v; error = null;} Object result;
	public String Error () {return error;} 	public void Error_set(String v) 	{error = v; result = null;} String error;
}
class Swt_html_lnr_Traverse implements TraverseListener {
	public Swt_html_lnr_Traverse(Swt_html html_box) {this.html_box = html_box;} Swt_html html_box;
	@Override public void keyTraversed(TraverseEvent arg0) {}
}
class Swt_html_lnr_title implements TitleListener {
	public Swt_html_lnr_title(Swt_html html_box) {this.html_box = html_box;} Swt_html html_box;
	@Override public void changed(TitleEvent ev) {
		try {UsrDlg_._.Note(ev.title);}		
		catch (Exception e) {html_box.Kit().Ask_ok("xowa.swt.html_box", "title.fail", Err_.Message_gplx_brief(e));}	// NOTE: must catch error or will cause app to lock; currently called inside displaySync 
	}
}
class Swt_html_func extends BrowserFunction {    
    public Swt_html_func(Browser browser, String name, GfoInvkAble invk) {
        super (browser, name);
        this.browser = browser;
        this.invk = invk;
    }	Browser browser; GfoInvkAble invk;
    public Object function (Object[] args) {
    	try {
    		return gplx.gfui.Gfui_html.Js_args_exec(invk, args);
    	}
    	catch (Exception e) {
    		return Err_.Message_gplx_brief(e);
    	}
    }
}
class Swt_html_lnr_status implements StatusTextListener {
	public Swt_html_lnr_status(Swt_html html_box) {this.html_box = html_box;} Swt_html html_box;
	public void Host_set(GfoEvObj host) {this.host = host;} GfoEvObj host;
	@Override public void changed(StatusTextEvent ev) {
		String ev_text = ev.text;
//		if (String_.Has(ev_text, "Loading [MathJax]")) return;	// suppress MathJax messages; // NOTE: disabled for 2.1 (which no longer outputs messages to status); DATE:2013-05-03
		try {if (host != null) GfoEvMgr_.PubObj(host, Gfui_html.Evt_link_hover, "v", ev_text);}
		catch (Exception e) {html_box.Kit().Ask_ok("xowa.gui.html_box", "status.fail", Err_.Message_gplx_brief(e));}	// NOTE: must catch error or will cause app to lock; currently called inside displaySync 
	}
}
class Swt_html_lnr_progress implements ProgressListener {
	public Swt_html_lnr_progress(Swt_html html_box) {this.html_box = html_box;} Swt_html html_box;
	@Override public void changed(ProgressEvent arg0) {}
	@Override public void completed(ProgressEvent arg0) {
//		UsrDlg_._.Note("done");
	}
}
class Swt_html_lnr_location implements LocationListener {
	public Swt_html_lnr_location(Swt_html html_box) {this.html_box = html_box;} Swt_html html_box;
	public void Host_set(GfoEvObj host) {this.host = host;} GfoEvObj host;
	@Override public void changed(LocationEvent arg) 	{Pub_evt(arg, Gfui_html.Evt_location_changed);}
	@Override public void changing(LocationEvent arg) 	{Pub_evt(arg, Gfui_html.Evt_location_changing);}
	void Pub_evt(LocationEvent arg, String evt) {		
		String location = arg.location;
		if (String_.Eq(location, "about:blank")) return;	// location changing event fires once when page is loaded; ignore
		try {
			GfoEvMgr_.PubObj(host, evt, "v", location);
			arg.doit = false; // cancel navigation event, else there will be an error when trying to go to invalid location
		}
		catch (Exception e) {html_box.Kit().Ask_ok("xowa.gui.html_box", evt, Err_.Message_gplx_brief(e));}	// NOTE: must catch error or will cause app to lock; currently called inside displaySync 		
	}
}
class Swt_html_lnr_mouse implements MouseListener {
	public Swt_html_lnr_mouse(GxwElem elem, Browser browser, Swt_kit kit) {this.elem = elem; this.browser = browser; this.kit = kit;} GxwElem elem; Browser browser; Swt_kit kit;
	@Override public void mouseDown(MouseEvent ev) {
		if (Is_at_scrollbar_area()) return;
		elem.Host().MouseDownCbk(XtoMouseData(ev));
	}
	@Override public void mouseUp(MouseEvent ev) {
		if (Is_at_scrollbar_area()) return;
		elem.Host().MouseUpCbk(XtoMouseData(ev));
	}
	boolean Is_at_scrollbar_area() {
		// WORKAROUND.SWT: SEE:NOTE_1:browser scrollbar and click  
		Point browser_size = browser.getSize();
		Point click_pos = kit.Swt_display().getCursorLocation();		
		return click_pos.x >= browser_size.x - 12;
	}
	@Override public void mouseDoubleClick(MouseEvent ev) {}
	IptEvtDataMouse XtoMouseData(MouseEvent ev) {
		IptMouseBtn btn = null;
		switch (ev.button) {
			case 1: btn = IptMouseBtn_.Left; break;
			case 2: btn = IptMouseBtn_.Middle; break;
			case 3: btn = IptMouseBtn_.Right; break;
			case 4: btn = IptMouseBtn_.X1; break;
			case 5: btn = IptMouseBtn_.X2; break;
		}
		return IptEvtDataMouse.new_(btn, IptMouseWheel_.None, ev.x, ev.y);		
	}
}
/*
NOTE_1:browser scrollbar and click
a click in the scrollbar area will raise a mouse-down/mouse-up event in content-editable mode
. a click should be consumed by the scrollbar and not have any effect elsewhere on the window
. instead, a click event is raised, and counted twice
  1) for the scroll bar this will scroll the area.
  2) for the window. if keyboard-focus is set on a link, then it will activate the link.

swt does not expose any scrollbar information (visible, width), b/c the scrollbar is controlled by the underlying browser
so, assume:
. scrollbar is always present
. scrollbar has arbitrary width (currently 12)
. and discard if click is in this scrollbar area

two issues still occur with the workaround
1) even if the scrollbar is not present, any click on the right-hand edge of the screen will be ignored
2) click -> hold -> move mouse over to left -> release; the mouse up should be absorbed, but it is not due to position of release   
*/
