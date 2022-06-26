/*
 * SLOC.java
 *
 * Created on 30 mars 2004, 18:03
 * 
 * civ_java : civilization game toolkit.
 * Copyright (C) 2003-2005  julien eyries (julien.eyries@9online.fr)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 */

/**
 *
 * @author  roudoudou
 */
 
package state;
import java.io.*;

public class SLOC implements Serializable {
    

	public int owner_type;
	public int owner; 
	public int x; 
	public int y;
        
        /* SLOC SECTION

  4	char		"SLOC"

  4	long		number of starting locations



  For each starting location:

    4	long		length of starting location data (16)

    4	long		owner type (0=None, 1=Barbarians, 2=Civ, 3=Player)

    4	long		owner (RACE ID, Player# (0=Player1 and so on) or Barbarian Tribe)

    4	long		X

    4	long		Y
         *
         */

    /** Creates a new instance of SLOC */
    public SLOC() {
        owner_type=0;
	owner=0; 
	x=0; 
	y=0;
    }
    
    public String toString(){
	return  "SLOC" 
			+ " owner_type=" +  (owner_type) 
			+ " owner=" +  (owner)
			+ " x=" +  (x)	
			+ " y=" +  (y);	
    }
    
}
