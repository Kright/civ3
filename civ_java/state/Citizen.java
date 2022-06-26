

/*
 * Created on 12 déc. 2004
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

package state;
import java.io.Serializable;

/**
 * @author roudoudou
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */



public class Citizen  implements Serializable {
	
	
	 public int default_citizen;
	 public String name;
	 public String civilopedia_entry;
	 public String plural_name;
	 public int prerequisite;
	 public int luxuries;
	 public int research;
	 public int taxes;
	 public int corruption;
	 public int construction;
		
	 
	 public String toString(){
		return   "citizen" 
		 		+ " default_citizen=" + (default_citizen) 
				+ " name=" + (name) 
				+ " civilopedia_entry=" + (civilopedia_entry) 
				+ " plural_name=" + (plural_name) 
				+ " prerequisite=" + (prerequisite) 
				+ " luxuries=" + (luxuries) 
				+ " research=" + (research) 
				+ " taxes=" + (taxes)
				+ " corruption=" + (corruption)	
				+ " construction=" + (construction)	;
	 }
		
	/*
	 * CTZN SECTION (Citizens)

  4	char		"CTZN"

  4	long		number of citizen types



  For each citizen type:

    4	long		length of citizen data (124)

    4	long		default citizen

    32	string		citizens (singular) name

    32	string		civilopedia entry

    32	string		plural name

    4	long		prerequisite

    4	long		luxuries

    4	long		research

    4	long		taxes

    4	long		corruption

    4	long		construction
	 */
	 
	   /** Creates a new instance of Citizen */
	    public Citizen() {
		
			} 

}
