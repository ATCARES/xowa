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
package gplx.xowa; import gplx.*;
import gplx.gfui.*;
public class Xof_xfer_itm_ {
	public static void Calc_xfer_size(Int_2_ref rv, int thumb_default_w, int file_w, int file_h, int lnki_w, int lnki_h, boolean lnki_thumb, double lnki_upright) {Calc_xfer_size(rv, thumb_default_w, file_w, file_h, lnki_w, lnki_h, lnki_thumb, lnki_upright, true);}
	public static void Calc_xfer_size(Int_2_ref rv, int thumb_default_w, int file_w, int file_h, int lnki_w, int lnki_h, boolean lnki_thumb, double lnki_upright, boolean thumb_width_must_be_lt_file_width) {
		int rv_w = lnki_w, rv_h = lnki_h;
		if (lnki_w < 1 && lnki_h < 1) {
			if (lnki_thumb)		rv_w = thumb_default_w;		// do not default to thumb if only height is set; EX: x900px should have w=0 h=900
			else				rv_w = file_w;
		}
		if (lnki_upright > 0)
			rv_w = Xof_url_.Width_adjust_by_upright(rv_w, lnki_upright);
		if (file_w < 1)				rv.Val_all_(rv_w, rv_h);
		else						Xof_xfer_itm_.Calc_view(rv, rv_w, rv_h, file_w, file_h, thumb_width_must_be_lt_file_width);
	}
	@gplx.Internal protected static void Calc_view(Int_2_ref rv, int lnki_w, int lnki_h, int file_w, int file_h, boolean thumb_width_must_be_lt_file_width) {// SEE:NOTE_1 for proc source/layout
		if (lnki_w == -1) lnki_w = file_w;						// lnki_w missing >>> use file_w; REF.MW:Linker.php|makeImageLink2|$hp['width'] = $file->getWidth( $page );				
		if (lnki_h != -1) {										// height exists; REF.MW:Generic.php|normaliseParams|if ( isset( $params['height'] ) && $params['height'] != -1 ) {
			if (lnki_w * file_h > lnki_h * file_w)  {			// lnki ratio > file ratio; SEE:NOTE_2;
				lnki_w = Calc_w(file_w, file_h, lnki_h);
			}
		}
		lnki_h = Scale_height(file_w, file_h, lnki_w);
		if (lnki_w > file_w && thumb_width_must_be_lt_file_width) {	// do not allow lnki_w > file_w; REF.MW:Generic.php|normaliseParams
			lnki_w = file_w; lnki_h = file_h;
		}
		rv.Val_all_(lnki_w, lnki_h);
	}
	public static int Calc_w(int file_w, int file_h, int lnki_h) {
		double ideal_w = (double)file_w * (double)lnki_h / (double)file_h;
		double ideal_w_ceil = Math_.Ceil(ideal_w);
		return Math_.Round(ideal_w_ceil * file_h / file_w, 0) > lnki_h
			? (int)Math_.Floor(ideal_w)
			: (int)ideal_w_ceil;
	}
	public static int Scale_height(int file_w, int file_h, int lnki_w) {
		return file_w == 0												// REF.MW:File.php|scaleHeight
			? 0
			: (int)Math_.Round(((double)lnki_w * file_h) / file_w, 0);	// NOTE: (double) needed else result will be int and decimal will be automatically truncated
	}
}
/*
NOTE_1:proc source/layout
MW calls the falling procs
. Linker.php|makeImageLink2
. Linker.php|makeThumbLink2
. File.php|transform
. Bitmap.php|normaliseParams
. Generic.php|normaliseParams
. File.php|scaleHeight
Note that this proc is a selective culling of the w,h setting code in the above (the procs do a lot of other checks/building)
also, MW's if branching can be combined. for now, emulating MW and not enforcing matching if/else 

NOTE_2: view ratio > file ratio
REF.MW:ImageFunctions.php|wfFitBoxWidth
don't know why this logic exists; for now, articulating example

consider file of 200,100 (2:1)
EX_1: view is 120,40 (3:1)
- (a) 120,80 or (b) 80,40
- (b) 80,40

EX_2: view is 120,80 (1.5:1)
- (a) 120,60 or (b) 160,80
- (a) 120,60
*/