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
package gplx.fsdb.tbls; import gplx.*; import gplx.fsdb.*;
public class Fsdb_fil_itm {
	public Fsdb_fil_itm(int id, int owner, int ext_id, String name) {this.id = id; this.owner = owner; this.ext_id = ext_id; this.name = name;}
	public int Id() {return id;} public Fsdb_fil_itm Id_(int v) {id = v; return this;} private int id;
	public int Owner() {return owner;} public Fsdb_fil_itm Owner_(int v) {owner = v; return this;} private int owner;
	public int Ext_id() {return ext_id;} public Fsdb_fil_itm Ext_id_(int v) {ext_id = v; return this;} private int ext_id;
	public String Name() {return name;} public Fsdb_fil_itm Name_(String v) {name = v; return this;} private String name;
	public static final Fsdb_fil_itm Null = new Fsdb_fil_itm(0, 0, 0, "");
}