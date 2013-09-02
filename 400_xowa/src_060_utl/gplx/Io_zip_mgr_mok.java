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
package gplx;
public class Io_zip_mgr_mok implements Io_zip_mgr {
	public void Zip_fil(Io_url src_fil, Io_url trg_fil) {
		byte[] src_bry = Io_mgr._.LoadFilBry(src_fil);
		byte[] zip_bry = Zip_bry(src_bry, 0, src_bry.length);
		Io_mgr._.SaveFilByt(trg_fil, zip_bry);
	}
	public void Zip_dir(Io_url src_dir, Io_url trg_fil) {}
	public byte[] Zip_bry(byte[] src, int bgn, int len)			{return ByteAry_.Add(Bry_zipped, ByteAry_.Mid(src, bgn, len));}
	public byte[] Unzip_bry(byte[] src, int bgn, int len)		{
		if (src == ByteAry_.Empty) return src;
		byte[] section = ByteAry_.Mid(src, bgn, bgn + len);
		if (!ByteAry_.HasAtBgn(section, Bry_zipped, 0, section.length)) throw Err_.new_("src not zipped: " + String_.new_utf8_(section));
		return ByteAry_.Mid(section, Bry_zipped.length, section.length);
	}
	public void Unzip_to_dir(Io_url src_fil, Io_url trg_dir) {}
	private static final byte[] Bry_zipped = ByteAry_.new_utf8_("zipped:");
}
