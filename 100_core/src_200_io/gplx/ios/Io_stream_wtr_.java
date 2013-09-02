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
package gplx.ios; import gplx.*;
public class Io_stream_wtr_ {
	public static Io_stream_wtr bzip2_(Io_url url)		{return new Io_stream_wtr_bzip2().Trg_url_(url);}
	public static Io_stream_wtr gzip_(Io_url url)		{return new Io_stream_wtr_gzip().Trg_url_(url);}
	public static Io_stream_wtr zip_(Io_url url)		{return new Io_stream_wtr_zip().Trg_url_(url);}
	public static Io_stream_wtr file_(Io_url url)		{return new Io_stream_wtr_file().Trg_url_(url);}
	public static Io_stream_wtr new_by_url_(Io_url url) {
		String ext = url.Ext();
		if		(String_.Eq(ext, Io_stream_.Ext_zip)) 	return gplx.ios.Io_stream_wtr_.zip_(url);
		else if	(String_.Eq(ext, Io_stream_.Ext_gz)) 	return gplx.ios.Io_stream_wtr_.gzip_(url);
		else if	(String_.Eq(ext, Io_stream_.Ext_bz2)) 	return gplx.ios.Io_stream_wtr_.bzip2_(url);
		else 											return gplx.ios.Io_stream_wtr_.file_(url);
	}
	public static Io_stream_wtr new_by_mem(ByteAryBfr bfr, byte tid) {
		Io_stream_wtr wtr = new_by_tid_(tid).Trg_url_(Io_url_.Null);
		wtr.Trg_bfr_(bfr);
		return wtr;
	}
	public static Io_stream_wtr new_by_tid_(byte v) {
		switch (v) {
			case gplx.ios.Io_stream_.Tid_file	: return new Io_stream_wtr_file();
			case gplx.ios.Io_stream_.Tid_zip	: return new Io_stream_wtr_zip();
			case gplx.ios.Io_stream_.Tid_gzip	: return new Io_stream_wtr_gzip();
			case gplx.ios.Io_stream_.Tid_bzip2	: return new Io_stream_wtr_bzip2();
			default								: throw Err_.unhandled(v);
		}
	}
	public static void Save_all(Io_url url, byte[] bry, int bgn, int end) {
		Io_stream_wtr wtr = new_by_url_(url);
		try {
			wtr.Open();
			wtr.Write(bry, bgn, end);
		}
		finally {wtr.Rls();}
	}
}
abstract class Io_stream_wtr_base implements Io_stream_wtr {
	java.io.OutputStream zip_stream;
	public Io_url Trg_url() {return trg_url;} public Io_stream_wtr Trg_url_(Io_url v) {trg_url = v; trg_bfr = null; return this;} Io_url trg_url;
	public void Trg_bfr_(ByteAryBfr v) {trg_bfr = v;} ByteAryBfr trg_bfr; java.io.ByteArrayOutputStream mem_stream;
	@SuppressWarnings("resource") // rely on OutputStream to close bry_stream
	public Io_stream_wtr Open() {
		java.io.OutputStream bry_stream = null;
		if (trg_bfr == null) {
			if (!Io_mgr._.ExistsFil(trg_url)) Io_mgr._.SaveFilStr(trg_url, "");			
			try {bry_stream = new java.io.FileOutputStream(trg_url.Raw());}
			catch (Exception exc) {throw Err_.new_fmt_("open failed: trg_url={0}", trg_url.Raw());}		
		}
		else {
			mem_stream = new java.io.ByteArrayOutputStream();
			bry_stream = mem_stream;
		}
		zip_stream = Wrap_stream(bry_stream);
		return this;
	}
	public void Write(byte[] bry, int bgn, int len) {
		try {zip_stream.write(bry, bgn, len);}
		catch (Exception exc) {throw Err_.new_fmt_("write failed: bgn={0} len={1}", bgn, len);}
	}
	public void Flush() {
		if (trg_bfr != null) {
			try {zip_stream.close();} catch (Exception exc) {throw Err_.new_fmt_("flush failed");}	// must close zip_stream to flush all bytes
			trg_bfr.Add(mem_stream.toByteArray());
		}
	}
	public void Rls() {
		try {
			if (zip_stream != null) zip_stream.close();
			if (mem_stream != null) mem_stream.close();
		}
		catch (Exception e) {throw Err_.new_fmt_("close failed: trg_url={0}", trg_url.Raw());}
	}
	public abstract java.io.OutputStream Wrap_stream(java.io.OutputStream stream);
}
class Io_stream_wtr_bzip2 extends Io_stream_wtr_base {
	@Override public byte Tid() {return Io_stream_.Tid_bzip2;}
	@Override public java.io.OutputStream Wrap_stream(java.io.OutputStream stream) {
		try {return new org.apache.commons.compress.compressors.bzip2.BZip2CompressorOutputStream(stream);}
		catch (Exception exc) {throw Err_.new_fmt_("failed to open bzip2 stream");}
	}
	static final byte[] Bz2_header = new byte[] {Byte_ascii.Ltr_B, Byte_ascii.Ltr_Z};
}
class Io_stream_wtr_gzip extends Io_stream_wtr_base {
	@Override public byte Tid() {return Io_stream_.Tid_gzip;}
	@Override public java.io.OutputStream Wrap_stream(java.io.OutputStream stream) {
		try {return new java.util.zip.GZIPOutputStream(stream);}
		catch (Exception exc) {throw Err_.new_fmt_("failed to open gz stream");}
	}
}
class Io_stream_wtr_zip implements Io_stream_wtr {
	java.util.zip.ZipOutputStream zip_stream;
	
	@Override public byte Tid() {return Io_stream_.Tid_zip;}
	public Io_url Trg_url() {return trg_url;} public Io_stream_wtr Trg_url_(Io_url v) {trg_url = v; trg_bfr = null; return this;} Io_url trg_url = Io_url_.Null;
	public void Trg_bfr_(ByteAryBfr v) {trg_bfr = v;} ByteAryBfr trg_bfr; java.io.ByteArrayOutputStream mem_stream;
	@SuppressWarnings("resource") // rely on zip_stream to close bry_stream 
	public Io_stream_wtr Open() {
		java.io.OutputStream bry_stream;
		if (trg_bfr == null) {
			if (!Io_mgr._.ExistsFil(trg_url)) Io_mgr._.SaveFilStr(trg_url, "");	// create file if it doesn't exist
			try {bry_stream = new java.io.FileOutputStream(trg_url.Xto_api());}
			catch (Exception exc) {throw Err_.new_fmt_("open failed: trg_url={0}", trg_url.Raw());}
		}
		else {
			mem_stream = new java.io.ByteArrayOutputStream();
			bry_stream = mem_stream;
		}
		zip_stream = new java.util.zip.ZipOutputStream(bry_stream);
		java.util.zip.ZipEntry entry = new java.util.zip.ZipEntry("file");
		try {zip_stream.putNextEntry(entry);}
		catch (Exception exc) {throw Err_.new_fmt_("open failed: trg_url={0}", trg_url.Raw());}
		return this;
	}
	public void Write(byte[] bry, int bgn, int len) {
		try {zip_stream.write(bry, bgn, len);}
		catch (Exception exc) {throw Err_.new_fmt_("write failed: trg_url={0} bgn={1} len={2}", trg_url.Raw(), bgn, len);}
	}
	public void Flush() {
		try {
			zip_stream.flush();
			if (trg_bfr != null)
				trg_bfr.Add(mem_stream.toByteArray());
		}
		catch (Exception e) {throw Err_.new_fmt_("flush failed: trg_url={0}", trg_url.Raw());}
	}
	public void Rls() {
		try {
			if (zip_stream != null) zip_stream.close();
			if (mem_stream != null) mem_stream.close();
		}
		catch (Exception e) {throw Err_.new_fmt_("close failed: trg_url={0}", trg_url.Raw());}
	}
}
class Io_stream_wtr_file implements Io_stream_wtr {
	IoStream bry_stream; 
	@Override public byte Tid() {return Io_stream_.Tid_file;}
	public Io_url Trg_url() {return trg_url;} public Io_stream_wtr Trg_url_(Io_url v) {trg_url = v; return this;} Io_url trg_url;
	public void Trg_bfr_(ByteAryBfr v) {trg_bfr = v;} ByteAryBfr trg_bfr; java.io.ByteArrayOutputStream mem_stream;
	public Io_stream_wtr Open() {
		try {
			if (trg_bfr == null)
				bry_stream = Io_mgr._.OpenStreamWrite(trg_url);
		}
		catch (Exception exc) {throw Err_.new_fmt_("open failed: trg_url={0}", trg_url.Raw());}
		return this;
	}
	public void Write(byte[] bry, int bgn, int len) {
		if (trg_bfr == null) {
			try {bry_stream.Write(bry, bgn, len);}
			catch (Exception exc) {throw Err_.new_fmt_("write failed: trg_url={0} bgn={1} len={2}", trg_url.Raw(), bgn, len);}
		}
		else
			trg_bfr.Add_mid(bry, bgn, bgn + len);
	}
	public void Flush() {
		if (trg_bfr == null)
			bry_stream.Flush();
	}
	public void Rls() {
		try {
			if (trg_bfr == null)
				bry_stream.Rls();
		}
		catch (Exception e) {throw Err_.new_fmt_("close failed: trg_url={0}", trg_url.Raw());}
	}
}
