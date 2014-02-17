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
package gplx.xowa.files.qrys.fs_roots; import gplx.*; import gplx.xowa.*; import gplx.xowa.files.*; import gplx.xowa.files.qrys.*;
import gplx.dbs.*;
import gplx.xowa.files.fsdb.*;
import gplx.xowa.files.wiki_orig.*;
public class Fs_root_wkr implements Xof_qry_wkr {
//		private Xow_wiki wiki;
	private ListAdp dirs = ListAdp_.new_();
	private OrderedHash fils = OrderedHash_.new_bry_();
	public Fs_root_wkr(Xow_wiki wiki) {}//this.wiki = wiki;}
	public byte Tid() {return Xof_qry_wkr_.Tid_dir;}
	public boolean Qry_file(Xof_fsdb_itm itm) {
		byte[] lnki_ttl = itm.Lnki_ttl();
		Orig_fil_itm fil = (Orig_fil_itm)fils.Fetch(lnki_ttl);	// search local hash
		if (fil == null) {								// not found
			fil = Find_in_dirs(lnki_ttl);				// query each dir
			if (fil != null)							// file found
				fils.Add(lnki_ttl, fil);				// add to local hash
		}
		if (fil == null) return false;					// file not found; return false;
		itm.Orig_size_(fil.Fil_w(), fil.Fil_h());
		return true;
	}
	private Orig_fil_itm Find_in_dirs(byte[] lnki_ttl) {
		int dirs_len = dirs.Count();
		for (int i = 0; i < dirs_len; i++) {
			Fs_root_dir qry_dir = (Fs_root_dir)dirs.FetchAt(i);
			Orig_fil_itm rv = (Orig_fil_itm)qry_dir.Get_by_ttl(lnki_ttl);
			if (rv != null) return rv;
		}
		return null;
	}
}