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
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
public class Swt_kit implements Gfui_kit {
	private String xulRunnerPath = null;
	private KeyValHash ctor_args = KeyValHash.new_(); private HashAdp kit_args = HashAdp_.new_();
	private KeyValHash nullArgs = KeyValHash.new_();
	public byte Tid() {return Gfui_kit_.Swt_tid;}
	public String Key() {return "swt";}
	public Gfui_clipboard Clipboard() {return clipboard;} private Swt_clipboard clipboard;
	public Display Swt_display() {return display;} private Display display;
	public Gfui_html_cfg Html_cfg() {return html_cfg;} private Gfui_html_cfg html_cfg = new Gfui_html_cfg();
	public void Cfg_set(String type, String key, Object val) {
		if 		(String_.Eq(type, Gfui_kit_.Cfg_HtmlBox)) {
			if (String_.Eq(key, "XulRunnerPath")) {
				xulRunnerPath = (String)val;
				return;
			}
		}
		KeyValHash typeCfg = (KeyValHash)kit_args.Fetch(type);
		if (typeCfg == null) {
			typeCfg = KeyValHash.new_();
			kit_args.Add(type, typeCfg);
		}
		typeCfg.AddReplace(key, val);
	}
	public boolean Kit_init_done() {return kit_init_done;} private boolean kit_init_done;  
	public void Kit_init(Gfo_usr_dlg gui_wtr) {
		this.gui_wtr = gui_wtr;
		usrMsgWkr_Stop = new Swt_UsrMsgWkr_Stop(this, gui_wtr);
		gui_wtr.Log_many("", "", "swt.kit_init.display");
		display = new Display();
		UsrDlg_._.Reg(UsrMsgWkr_.Type_Warn, GfoConsoleWin._);
		UsrDlg_._.Reg(UsrMsgWkr_.Type_Stop, usrMsgWkr_Stop);
		gui_wtr.Log_many("", "", "swt.kit_init.clipboard");
		clipboard = new Swt_clipboard(display);
		if (xulRunnerPath != null) System.setProperty("org.eclipse.swt.browser.XULRunnerPath", xulRunnerPath);
		kit_init_done = true; 
		gui_wtr.Log_many("", "", "swt.kit_init.done");
	}	private Gfo_usr_dlg gui_wtr;
	public void	Kit_term_cbk_(GfoInvkAbleCmd v) {this.term_cbk = v;} GfoInvkAbleCmd term_cbk = GfoInvkAbleCmd.Null;
	public void Kit_run() {
	    shell.addListener(SWT.Close, new Swt_lnr_shell_close(this));
		shell.open();
		Cursor cursor = new Cursor(display, SWT.CURSOR_ARROW);
		shell.setCursor(cursor);	// set cursor to hand else cursor defaults to Hourglass until mouse is moved; DATE: 2014-01-31
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		cursor.dispose();
		Kit_term();
	}
	public void Kit_term() {
		usrMsgWkr_Stop.Rls();
		clipboard.Rls();
		display.dispose();
	}	private Swt_UsrMsgWkr_Stop usrMsgWkr_Stop;
	public boolean Ask_yes_no(String grp_key, String msg_key, String fmt, Object... args) 		{
		Swt_dlg_msg dlg = (Swt_dlg_msg)New_dlg_msg(ask_fmtr.Bld_str_many(ask_bfr, fmt, args)).Init_btns_(Gfui_dlg_msg_.Btn_yes, Gfui_dlg_msg_.Btn_no).Init_ico_(Gfui_dlg_msg_.Ico_question);
		display.syncExec(dlg);
		return dlg.Ask_rslt == Gfui_dlg_msg_.Btn_yes;
	}
	public boolean Ask_ok_cancel(String grp_key, String msg_key, String fmt, Object... args) 	{
		Swt_dlg_msg dlg = (Swt_dlg_msg)New_dlg_msg(ask_fmtr.Bld_str_many(ask_bfr, fmt, args)).Init_btns_(Gfui_dlg_msg_.Btn_ok, Gfui_dlg_msg_.Btn_cancel).Init_ico_(Gfui_dlg_msg_.Ico_question);
		display.syncExec(dlg);
		return dlg.Ask_rslt == Gfui_dlg_msg_.Btn_ok;
	}	ByteAryFmtr ask_fmtr = ByteAryFmtr.new_().Fail_when_invalid_escapes_(false); ByteAryBfr ask_bfr = ByteAryBfr.new_();
	public int Ask_yes_no_cancel(String grp_key, String msg_key, String fmt, Object... args) 	{
		Swt_dlg_msg dlg = (Swt_dlg_msg)New_dlg_msg(ask_fmtr.Bld_str_many(ask_bfr, fmt, args)).Init_btns_(Gfui_dlg_msg_.Btn_yes, Gfui_dlg_msg_.Btn_no, Gfui_dlg_msg_.Btn_cancel).Init_ico_(Gfui_dlg_msg_.Ico_question);		
		display.syncExec(dlg);
		return dlg.Ask_rslt;
	}
	public void Ask_ok(String grp_key, String msg_key, String fmt, Object... args) 				{
		Swt_dlg_msg dlg = (Swt_dlg_msg)New_dlg_msg(ask_fmtr.Bld_str_many(ask_bfr, fmt, args)).Init_btns_(Gfui_dlg_msg_.Btn_ok).Init_ico_(Gfui_dlg_msg_.Ico_information);
		display.syncExec(dlg);
	}
	public GfuiInvkCmd New_cmd_sync(GfoInvkAble invk) 	{return new Swt_gui_cmd(gui_wtr, display, invk, Bool_.N);}
	public GfuiInvkCmd New_cmd_async(GfoInvkAble invk) 	{return new Swt_gui_cmd(gui_wtr, display, invk, Bool_.Y);}
	public GfuiWin New_win_utl(String key, GfuiWin owner, KeyVal... args) {return GfuiWin_.kit_(this, key, new Swt_win(shell), nullArgs);	}
	public GfuiWin New_win_app(String key, KeyVal... args) {
		Swt_win win = new Swt_win(display);
		this.shell = win.UnderShell();
		shell.setLayout(null);
		GfuiWin rv = GfuiWin_.kit_(this, key, win, nullArgs);
		main_win = rv;
		return rv;
	}	Shell shell; GfuiWin main_win;
	public GfuiBtn New_btn(String key, GfuiElem owner, KeyVal... args) {
		GfuiBtn rv = GfuiBtn_.kit_(this, key, new Swt_btn_no_border(owner.UnderElem(), ctor_args), ctor_args);
		owner.SubElems().Add(rv);
		return rv;
	}
	public Gfui_html New_html(String key, GfuiElem owner, KeyVal... args) {
		ctor_args.Clear();
		Object htmlBox_args_obj = kit_args.Fetch(Gfui_kit_.Cfg_HtmlBox);
		if (htmlBox_args_obj != null) {
			KeyValHash htmlBox_args = (KeyValHash)htmlBox_args_obj;
			KeyVal browser_type = htmlBox_args.FetchOrNull(Cfg_Html_BrowserType);
			if (browser_type != null) ctor_args.Add(browser_type);
		}
		Gfui_html rv = Gfui_html.kit_(this, key, new Swt_html(this, owner.UnderElem(), ctor_args), ctor_args);
		rv.Owner_(owner);
		return rv;
	}
	public GfuiTextBox New_text_box(String key, GfuiElem owner, KeyVal... args) {
		ctor_args.Clear();
		int args_len = args.length;
		for (int i = 0; i < args_len; i++)
			ctor_args.Add(args[i]);
		boolean border_on = Bool_.cast_(ctor_args.FetchValOr(GfuiTextBox.CFG_border_on_, true));
		GxwTextFld under = new Swt_text_w_border(owner.UnderElem(), New_color(border_on ? ColorAdp_.LightGray : ColorAdp_.White), ctor_args);
		GfuiTextBox rv = GfuiTextBox_.kit_(this, key, under, ctor_args);
		rv.Owner_(owner);
		ctor_args.Clear();
		return rv;
	}
	public GfuiStatusBox New_status_box(String key, GfuiElem owner, KeyVal... args) {
		ctor_args.Clear();
		GfuiStatusBox rv = GfuiStatusBox_.kit_(this, key, new Swt_text(owner.UnderElem(), ctor_args));
		rv.Owner_(owner);
		return rv;
	}
	public Gfui_dlg_file New_dlg_file(String msg) {return new Swt_dlg_file(shell).Init_msg_(msg);}
	public Gfui_dlg_msg New_dlg_msg(String msg) {return new Swt_dlg_msg(shell).Init_msg_(msg);}
	public ImageAdp New_img_load(Io_url url) {
		if (url == Io_url_.Null) return ImageAdp_.Null;
		Image img = new Image(display, url.Raw());
		Rectangle rect = img.getBounds();
		return new Swt_img(this, img, rect.width, rect.height).Url_(url);
	}
	public Color New_color(ColorAdp v) {return (Color)New_color(v.Alpha(), v.Red(), v.Green(), v.Blue());}
	public Object New_color(int a, int r, int g, int b) {return new Color(display, r, g, b);}
	public Gfui_mnu_grp New_mnu_popup(GfuiElem owner) 	{return Swt_popup_grp.new_popup(owner);}
	public Gfui_mnu_grp New_mnu_bar(GfuiWin owner) 		{return Swt_popup_grp.new_bar(owner);}
	public float Calc_font_height(GfuiElem elem, String s) {
		if (String_.Len_eq_0(s)) return 8;
		String old_text = elem.Text();
		elem.Text_(s);
		float rv = ((Swt_text_w_border)(elem.UnderElem())).text_elem.getFont().getFontData()[0].height;
		shell.setText(old_text);
		return rv;
	}
	public static final Swt_kit _ = new Swt_kit(); private Swt_kit() {}	// singleton b/c of following line "In particular, some platforms which SWT supports will not allow more than one active display" (http://help.eclipse.org/indigo/topic/org.eclipse.platform.doc.isv/reference/api/org/eclipse/swt/widgets/Display.html)
	public static final String Cfg_Html_BrowserType = "BrowserType";
	public static int Cfg_Html_BrowserType_parse(String v) {
		if		(String_.Eq(v, "mozilla"))	return Swt_html.Browser_tid_mozilla;
		else								return Swt_html.Browser_tid_none;
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(String_.Eq(k, Invk_Cfg_add)) {
			String type = m.ReadStrOr("type", "");
			String key = m.ReadStrOr("key", "");
			String val = m.ReadStrOr("val", "");
			if (ctx.Deny()) return this;			
			if (String_.Eq(type, Gfui_kit_.Cfg_HtmlBox)) {
				if 		(String_.Eq(key, "XulRunnerPath"))
					xulRunnerPath = val;
				else if (String_.Eq(key, Swt_kit.Cfg_Html_BrowserType))
					Cfg_set(type, Swt_kit.Cfg_Html_BrowserType, Cfg_Html_BrowserType_parse(val));
			}
		}
		else if	(String_.Eq(k, Invk_HtmlBox)) {return html_cfg;}
		else if	(String_.Eq(k, Invk_ask_file)) return this.New_dlg_file(m.Args_getAt(0).Val_to_str_or_empty()).Ask();
		return this;
	}	public static final String Invk_Cfg_add = "Cfg_add", Invk_HtmlBox = "HtmlBox", Invk_ask_file = "ask_file";	
	public static boolean Html_box_focus_automatically = false;
	public static FontAdp Control_font_get(Font font, GxwCore_base owner) {
		FontData fontData = font.getFontData()[0];
		FontAdp rv = FontAdp.new_(fontData.getName(), fontData.getHeight(), FontStyleAdp_.lang_(fontData.getStyle()));	// NOTE: swt style constants match swing
		rv.OwnerGxwCore_(owner);
		return rv;
	}
	public static void Control_font_set(FontAdp font, GxwCore_base owner, Control control) {
		font.OwnerGxwCore_(owner);
		FontData fontData = new FontData(font.Name(), (int)font.size, font.Style().Val());
		Font rv = new Font(control.getDisplay(), fontData);
		control.setFont(rv);
	}
}
class Swt_lnr_shell_close implements Listener {
	public Swt_lnr_shell_close(Swt_kit kit) {this.kit = kit;} private Swt_kit kit;
	@Override public void handleEvent(Event event) {		
		boolean rslt = Bool_.cast_(kit.term_cbk.Invk());
		if (!rslt) 
			event.doit = false;
	}
}
class Swt_UsrMsgWkr_Stop implements UsrMsgWkr, RlsAble {
	public Swt_UsrMsgWkr_Stop(Swt_kit kit, Gfo_usr_dlg gui_wtr) {this.kit = kit; this.gui_wtr = gui_wtr;} Swt_kit kit; Gfo_usr_dlg gui_wtr;
	@Override public void Rls() {this.kit = null;}
	public void ExecUsrMsg(int type, UsrMsg umsg) {
		String msg = umsg.XtoStr(); 
		kit.Ask_ok("xowa.gui", "stop", msg);
		gui_wtr.Log_many("", "", msg);
	}
}
class Swt_gui_cmd implements GfuiInvkCmd, Runnable {
	private Gfo_usr_dlg usr_dlg; private GfoInvkAble target; private Display display; private boolean async;	
	private GfsCtx invk_ctx; private int invk_ikey; private String invk_key; private GfoMsg invk_msg;	
	public Swt_gui_cmd(Gfo_usr_dlg usr_dlg, Display display, GfoInvkAble target, boolean async) {
		this.usr_dlg = usr_dlg; this.display = display; this.target = target; this.async = async;
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		this.invk_ctx = ctx; this.invk_ikey = ikey ; this.invk_key = k; this.invk_msg = m;
		if (async)
			display.asyncExec(this);
		else
			display.syncExec(this);
		return this;
	}	
	@Override public void run() {
		try {
			target.Invk(invk_ctx, invk_ikey, invk_key, invk_msg);
		}
		catch (Exception e) {
			usr_dlg.Warn_many("", "", "fatal error while running; key=~{0} err=~{1}", invk_key, Err_.Message_gplx_brief(e));
		}
	} 
	public void Rls() {
		usr_dlg = null; target = null; display = null;
		invk_ctx = null; invk_key = null; invk_msg = null;
	}
}
