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
package gplx.gfml; import gplx.*;
class GfmlTypeCompiler_fxt {
	public GfmlTyp_mok typ_() {return GfmlTyp_mok.new_();}
	public GfmlNde_mok nde_() {return GfmlNde_mok.new_();}
	public GfmlFld_mok fld_() {return GfmlFld_mok.new_();}
	public GfmlTypRegy Regy() {return typBldr.TypeRegy();}
	public void ini_Typ(ListAdp typs, GfmlTyp_mok typ) {typs.Add(typ);}
	@gplx.Internal protected void run_InitPragma(GfmlTypRegy regy, GfmlPragma pragma) {
		GfmlTypeMakr makr = GfmlTypeMakr.new_();
		GfmlType[] typeAry = pragma.MakePragmaTypes(makr);
		regy.Add_ary(typeAry);
	}
	public void tst_Type(GfmlTyp_mok expd, GfmlTyp_mok actl) {
		TfdsTstr_fxt tstr = TfdsTstr_fxt.new_();
		tst(tstr, expd, actl);
		tstr.tst_Equal("typ");
	}
	public void tst_Compile(GfmlNde_mok nde, GfmlTyp_mok expd) {
		GfmlNde gnde = run_Resolve(this.Regy(), "_type/type", nde);
		OrderedHash list = OrderedHash_.new_();
		GfmlType actlType = GfmlTypeCompiler.Compile(gnde, GfmlType_.Root, this.Regy(), list);
		GfmlTyp_mok actl = GfmlTyp_mok.type_(actlType);
		TfdsTstr_fxt tstr = TfdsTstr_fxt.new_();
		tst(tstr, expd, actl);
		tstr.tst_Equal("typ");
	}
	public void tst_Parse(String raw, GfmlNde_mok... expdAry) {
		GfmlDoc gdoc = GfmlDoc_.parse_any_eol_(raw);
		GfmlNde_mok expd = GfmlNde_mok.new_();
		for (GfmlNde_mok itm : expdAry)
			expd.Subs_(itm);
		TfdsTstr_fxt tstr = TfdsTstr_fxt.new_();
		GfmlTypeResolver_fxt.tst_Nde(tstr, expd, GfmlNde_mok.gfmlNde_(gdoc.RootNde()));
		tstr.tst_Equal("parse");
	}
	public static void tst(TfdsTstr_fxt tstr, GfmlTyp_mok expd, GfmlTyp_mok actl) {
		if (expd.Name() != null) tstr.Eq_str(expd.Name(), actl.Name(), "name");
		if (expd.Key() != null) tstr.Eq_str(expd.Key(), actl.Key(), "key");
		int max = tstr.List_Max(expd.Subs(), actl.Subs());
		for (int i = 0; i < max; i++) {
			GfmlFld_mok expdFld = (GfmlFld_mok)tstr.List_FetchAtOrNull(expd.Subs(), i);
			GfmlFld_mok actlFld = (GfmlFld_mok)tstr.List_FetchAtOrNull(actl.Subs(), i);
			tstr.SubName_push(Int_.XtoStr(i) + " fld");
			tst(tstr, expdFld, actlFld);
			tstr.SubName_pop();
		}
	}
	static void tst(TfdsTstr_fxt tstr, GfmlFld_mok expd, GfmlFld_mok actl) {
		expd = NullObj(expd); actl = NullObj(actl);
		if (expd.Name() != null) tstr.Eq_str(expd.Name(), actl.Name(), "name");
		if (expd.TypeKey() != null) tstr.Eq_str(expd.TypeKey(), actl.TypeKey(), "typekey");
		if (expd.DefaultTkn() != null) tstr.Eq_str(expd.DefaultTknRaw(), actl.DefaultTknRaw(), "default");
	}
	static GfmlFld_mok NullObj(GfmlFld_mok v) {return (v == null) ? GfmlFld_mok.new_().ini_ndk_(String_.NullStr, "") : v;}
	public void tst_Resolve(GfmlTyp_mok typ, GfmlNde_mok nde, GfmlNde_mok expd) {
		typBldr.TypeRegy().Add(typ.XtoGfmlType());
		tst_Resolve(nde, expd);
	}
	public GfmlNde run_Resolve(GfmlTypRegy regy, String typKey, GfmlNde_mok nde) {
		GfmlNde gfmlNde = nde.XtoGfmlItm(regy);
		typBldr.OverridePool(regy.FetchOrNull(typKey));
		tst_ResolveNde(gfmlNde, typKey);
		return gfmlNde;
	}
	public void tst_Resolve(GfmlNde_mok nde, GfmlNde_mok expd) {
		GfmlNde gfmlNde = nde.XtoGfmlItm(typBldr.TypeRegy());
		tst_ResolveNde(gfmlNde, GfmlType_.AnyKey);
		TfdsTstr_fxt tstr = TfdsTstr_fxt.new_();
		GfmlTypeResolver_fxt.tst_Nde(tstr, expd, GfmlNde_mok.gfmlNde_(gfmlNde));
		tstr.tst_Equal("test");
	}
	void tst_ResolveNde(GfmlNde nde, String ownerKey) {
		typBldr.NdeBgn(nde, ownerKey);
		for (int i = 0; i < nde.SubObjs_Count(); i++) {
			GfmlItm itm = (GfmlItm)nde.SubObjs_GetAt(i);
			if (itm.ObjType() == GfmlObj_.Type_nde)
				tst_ResolveNde((GfmlNde)itm, nde.Type().Key());
			else
				typBldr.AtrExec(nde, (GfmlAtr)itm);
		}
		typBldr.NdeEnd();
	}
	GfmlTypeMgr typBldr = GfmlTypeMgr.new_();
	public static GfmlTypeCompiler_fxt new_() {return new GfmlTypeCompiler_fxt();} GfmlTypeCompiler_fxt() {}
}
