/*
 * Tech.java
 *
 * Created on 30 mars 2004, 17:40
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

import ressource.My_Toolkit;
	
public class Tech implements Serializable {
    
  
	public String name;
	public String civilopedia_entry;
	public int cost;
	public int era;
	public int icon;
	public int x;
	public int y;
	public int prerequisite_1;
	public int prerequisite_2;
	public int prerequisite_3;
	public int prerequisite_4;
	public int flags;
	//public int flavors;
    
    
    /* TECH SECTION (Civilization Advances)

  4	char		"TECH"

  4	long		number of advances



  For each advance:

    4	long		length of advance data (104)

    32	string		advance name

    32	string		civilopedia entry

    4	long		cost

    4	long		era

    4	long		advance icon

    4	long		x

    4	long		y

    4	long		prerequisite #1

    4	long		prerequisite #2

    4	long		prerequisite #3

    4	long		prerequisite #4

    4	long		flags (binary):

								00000000

			enables diplomats			.......1

			enables irrigation without fresh water	......1.

			enables bridges				.....1..

			disables diseases from flood plains	....1...

			enables conscription of units		...1....

			enables mobilization levels		..1.....

			enables recycling			.1......

			enables precision bombing		1.......



			enables mutual protection pacts		.......1

			enables right of passage treaties	......1.

			enables military alliances		.....1..

			enables trade embargoes			....1...

			doubles effect of (wealth) improvement	...1....

			enables trade over sea tiles		..1.....

			enables trade over ocean tiles		.1......

			enables map trading			1.......



			enables communication trading		.......1

			not required for era advancement	......1.

			doubles work rate (of workers)		.....1..

    4	long		flavors (binary)


        **/
    
    
    /** Creates a new instance of Tech */
    public Tech() { 
		name = "";
		cost = 0 ;
		} 

    public String toString(){
	return     "tech" 
			+ " name=" +  (name) 
			+ " civilopedia_entry=" +  (civilopedia_entry) 
			+ " cost=" +  (cost)
			+ " era=" +  (era)
			+ " icon=" +  (icon)
			+ " x=" +  (x)
			+ " y=" +  (y)
			+ " prerequisite_1=" +  (prerequisite_1)			
			+ " prerequisite_2=" +  (prerequisite_2)
			+ " prerequisite_3=" +  (prerequisite_3)
			+ " prerequisite_4=" +  (prerequisite_4)
			+ " flags=" +  My_Toolkit.show_hex32(flags)
			;
	

    }



	 
}
