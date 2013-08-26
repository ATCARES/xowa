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
package gplx.xowa.xtns.scores; import gplx.*; import gplx.xowa.*; import gplx.xowa.xtns.*;
public class Xow_xtn_score implements Xow_xtn_itm {
	public byte[] Xtn_key() {return KEY;} public static final byte[] KEY = ByteAry_.new_ascii_("score");
	public boolean Enabled() {return enabled;} private boolean enabled = true;
	public void Xtn_init_by_app(Xoa_app app) {}
	public void Xtn_init_by_wiki(Xow_wiki wiki) {}
	public ByteAryFmtr Html_img() {return html_img;} ByteAryFmtr html_img = ByteAryFmtr.new_(String_.Concat_lines_nl
		(	""
		,	"<p>"
		,	"  <a id=\"~{a_id}\" href=\"~{a_href}\" xowa_title=\"~{a_xowa_title}\">"
		,	"    <img id=\"~{img_id}\" src=\"~{img_src}\"  />"
		,	"  </a>"
		,	"</p>"
		), "a_id", "a_href", "a_xowa_title", "img_id", "img_src", "img_alt");
	public ByteAryFmtr Html_txt() {return html_txt;} ByteAryFmtr html_txt = ByteAryFmtr.new_(String_.Concat_lines_nl
		(	""
		,	"<div id=\"~{div_id}\" class=\"~{div_class}\">"
		,	"  <pre style=\"overflow:auto\">~{code}"
		,	"</pre>"
		,	"</div>"
		), "div_id", "div_class", "code");
	public ByteAryFmtr Lilypond_fmtr() {return lilypond_fmtr;}
		ByteAryFmtr lilypond_fmtr = ByteAryFmtr.new_(String_.Concat_lines_nl
		(	"\\header {"
		,	"  tagline = ##f"
		,	"}"
		,	"\\paper {"
		,	"  raggedright = ##t"
		,	"  raggedbottom = ##t"
		,	"  indent = 0\\mm"
		,	"}"
		,	"\\version \"~{version}\""
		,	"\\score {"
		,	"  ~{code}"
		,	"  \\layout { }"
		,	"  \\midi {"
		,	"    \\context {"
		,	"      \\Score"
		,	"      tempoWholesPerMinute = #(ly:make-moment 100 4)"
		,	"    }"
		,	"  }"
		,	"}"), "version", "code");
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_enabled))			return Yn.XtoStr(enabled);
		else if	(ctx.Match(k, Invk_enabled_))			enabled = m.ReadYn("v");
		else if	(ctx.Match(k, Invk_html_img))			return html_img.Fmt();
		else if	(ctx.Match(k, Invk_html_img_))			html_img.Fmt_(m.ReadBry("v"));
		else if	(ctx.Match(k, Invk_html_txt))			return html_txt.Fmt();
		else if	(ctx.Match(k, Invk_html_txt_))			html_txt.Fmt_(m.ReadBry("v"));
		else if	(ctx.Match(k, Invk_lilypond_fmt))		return lilypond_fmtr.Fmt();
		else if	(ctx.Match(k, Invk_lilypond_fmt_))		lilypond_fmtr.Fmt_(m.ReadBry("v"));
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}	private static final String Invk_enabled = "enabled", Invk_enabled_ = "enabled_", Invk_html_img = "html_img", Invk_html_img_ = "html_img_", Invk_html_txt = "html_txt", Invk_html_txt_ = "html_txt_", Invk_lilypond_fmt = "lilypond_fmt", Invk_lilypond_fmt_ = "lilypond_fmt_";
	public static byte[] Lilypond_version = null;
}