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
package gplx.intl; import gplx.*;
public class Gfo_num_fmt_mgr implements GfoInvkAble {
	public Gfo_num_fmt_mgr() {this.Clear();}
	public boolean Standard() {return standard;} private boolean standard = true;
	public byte[] Dec_dlm() {return dec_dlm;} public Gfo_num_fmt_mgr Dec_dlm_(byte[] v) {this.dec_dlm = v; raw_trie.Add_bry_byte(v, Raw_tid_dec); return this;} private byte[] dec_dlm = Dec_dlm_default;
	public byte[] Raw(byte[] src) {
		int src_len = src.length;
		for (int i = 0; i < src_len; i++) {
			byte b = src[i];
			Object o = raw_trie.MatchAtCur(src, i, src_len);
			if (o == null)
				tmp.Add_byte(b);
			else {
				if (((ByteVal)o).Val() == Raw_tid_dec)
					tmp.Add_byte(Byte_ascii.Dot);
				i = raw_trie.Match_pos() - 1; // NOTE: handle multi-byte delims
			}
		}
		return tmp.XtoAryAndClear();
	}
	public byte[] Fmt(int val) {return Fmt(ByteAry_.new_ascii_(Int_.XtoStr(val)));}
	public byte[] Fmt(byte[] src) {
		int src_len = src.length;
		int num_bgn = -1; int dec_pos = -1;
		for (int i = 0; i < src_len; i++) {
			byte b = src[i];
			switch (b) {
				case Byte_ascii.Num_0: case Byte_ascii.Num_1: case Byte_ascii.Num_2: case Byte_ascii.Num_3: case Byte_ascii.Num_4:
				case Byte_ascii.Num_5: case Byte_ascii.Num_6: case Byte_ascii.Num_7: case Byte_ascii.Num_8: case Byte_ascii.Num_9:						
					if (dec_pos == -1) {
						if (num_bgn == -1) num_bgn = i;
					}
					else
						tmp.Add_byte(b);
					break;
				default:
					if (num_bgn != -1) {
						Gfo_num_fmt_wkr wkr = GetOrNew(i - num_bgn);
						wkr.Fmt(src, num_bgn, i, tmp);
						num_bgn = -1;
						dec_pos = -1;
						if (ByteAry_.HasAtBgn(src, dec_dlm, i, src_len)) {
						//if (src[i] == Byte_ascii.Dot) {	// ASSUME: all "." in raw are interpreted as decimals; EX: Float_.Xto_str()
							dec_pos = i;
							i += dec_dlm.length - 1;
							tmp.Add(dec_dlm);
							continue;
						}
					}
					tmp.Add_byte(b);
					break;
			}
		}
		if (num_bgn != -1) {
			Gfo_num_fmt_wkr wkr = GetOrNew(src_len - num_bgn);
			wkr.Fmt(src, num_bgn, src_len, tmp);
		}
		return tmp.XtoAryAndClear();
	}
	Gfo_num_fmt_wkr GetOrNew(int src_len) {
		Gfo_num_fmt_wkr rv = null;
		if (src_len < cache_len) {
			rv = cache[src_len];
			if (rv != null) return rv;
		}
		rv = new Gfo_num_fmt_wkr(grp_ary, grp_ary_len, src_len);
		if (src_len < cache_len) cache[src_len] = rv;
		return rv;
	}
	public Gfo_num_fmt_grp Grps_get_last() {return grp_ary[grp_ary_len - 1];}
	public Gfo_num_fmt_grp Grps_get(int i) {return grp_ary[i];}
	public int Grps_len() {return grp_ary_len;}
	public void Grps_add(Gfo_num_fmt_grp dat_itm) {
		standard = false;
		this.grp_ary = (Gfo_num_fmt_grp[])Array_.Resize(grp_ary, grp_ary_len + 1);
		grp_ary[grp_ary_len] = dat_itm;
		grp_ary_len = grp_ary.length;
		for (int i = 0; i < grp_ary_len; i++) {
			Gfo_num_fmt_grp itm = grp_ary[i];
			byte[] itm_dlm = itm.Dlm();
			Object o = raw_trie.MatchAtCurExact(itm_dlm, 0, itm_dlm.length);	// check for existing Object
			if (o == null)
				raw_trie.Add_bry_byte(itm_dlm, Raw_tid_grp);
		}
	}
	public Gfo_num_fmt_mgr Clear() {
		this.grp_ary = Gfo_num_fmt_grp.Ary_empty;
		grp_ary_len = 0;
		cache = new Gfo_num_fmt_wkr[cache_len];
		raw_trie.Clear();
		return this;
	}
	public Object Invk(GfsCtx ctx, int ikey, String k, GfoMsg m) {
		if		(ctx.Match(k, Invk_dec_dlm_))		this.Dec_dlm_(m.ReadBry("v"));	// NOTE: must call mutator
		else if	(ctx.Match(k, Invk_clear))			this.Clear();
		else if	(ctx.Match(k, Invk_grps_add))		this.Grps_add(new Gfo_num_fmt_grp(m.ReadBry("dlm"), m.ReadInt("digits"), m.ReadYn("repeat")));
		else	return GfoInvkAble_.Rv_unhandled;
		return this;
	}
	public static final String Invk_dec_dlm_ = "dec_dlm_", Invk_clear = "clear", Invk_grps_add = "grps_add";
	static final byte Raw_tid_dec = 0, Raw_tid_grp = 1;
	ByteTrieMgr_fast raw_trie = ByteTrieMgr_fast.cs_(); 
	Gfo_num_fmt_grp[] grp_ary = Gfo_num_fmt_grp.Ary_empty; int grp_ary_len;
	Gfo_num_fmt_wkr[] cache; int cache_len = 16;
	ByteAryBfr tmp = ByteAryBfr.new_();
	private static final byte[] Dec_dlm_default = new byte[] {Byte_ascii.Dot};
	public static final byte[] Grp_dlm_default = new byte[] {Byte_ascii.Comma};
}
class Gfo_num_fmt_wkr {
	public void Fmt(byte[] src, int bgn, int end, ByteAryBfr bb) {
		if (itm_max == 0) {bb.Add_mid(src, bgn, end); return;}; // NOTE: small numbers (<=3) will have a 0-len ary
		int cur_idx = itm_max - 1;
		Gfo_num_fmt_bldr cur = itm_ary[cur_idx];
		int cur_pos = cur.Pos();
		for (int i = bgn; i < end; i++) {
			if (i == cur_pos + bgn) {
				cur.Gen(bb);
				if (cur_idx > 0) cur = itm_ary[--cur_idx];
				cur_pos = cur.Pos();
			}
			bb.Add_byte(src[i]);
		}
	}
	public Gfo_num_fmt_wkr(Gfo_num_fmt_grp[] grp_ary, int grp_ary_len, int src_len) {
		itm_ary = new Gfo_num_fmt_bldr[src_len];					// default to src_len; will resize below;
		int src_pos = src_len, dat_idx = 0, dat_repeat = -1;
		while (true) {
			if (dat_idx == grp_ary_len) dat_idx = dat_repeat;	// no more itms left; return to repeat
			Gfo_num_fmt_grp dat = grp_ary[dat_idx];
			src_pos -= dat.Digits();
			if (src_pos < 1) break;								// no more digits needed; stop
			byte[] dat_dlm = dat.Dlm();
			itm_ary[itm_max++] = dat_dlm.length == 1 ? new Gfo_num_fmt_bldr_one(src_pos, dat_dlm[0]) : (Gfo_num_fmt_bldr)new Gfo_num_fmt_bldr_many(src_pos, dat_dlm);
			if (dat.Repeat() && dat_repeat == -1) dat_repeat = dat_idx;
			++dat_idx;
		}
		itm_ary = (Gfo_num_fmt_bldr[])Array_.Resize(itm_ary, itm_max);
	}
	Gfo_num_fmt_bldr[] itm_ary; int itm_max;
}
interface Gfo_num_fmt_bldr {
	int Pos();
	void Gen(ByteAryBfr bb);
}
class Gfo_num_fmt_bldr_one implements Gfo_num_fmt_bldr {
	public int Pos() {return pos;} private int pos;
	public void Gen(ByteAryBfr bb) {bb.Add_byte(b);}
	public Gfo_num_fmt_bldr_one(int pos, byte b) {this.pos = pos; this.b = b;} private byte b;
}
class Gfo_num_fmt_bldr_many implements Gfo_num_fmt_bldr {
	public int Pos() {return pos;} private int pos;
	public void Gen(ByteAryBfr bb) {bb.Add(ary);}
	public Gfo_num_fmt_bldr_many(int pos, byte[] ary) {this.pos = pos; this.ary = ary;} private byte[] ary;
}
