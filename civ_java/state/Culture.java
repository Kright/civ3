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

/**
 * @author roudoudou
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package state;
import java.io.*;

public class Culture  implements Serializable {

	
	    public String name;
	    public int chance_of_successful_propaganda;
	    public int culture_ratio_percentage;
	    public int culture_ratio_denominator;
	    public int culture_ratio_numerator;
	    public int initial_resistance_chance;
	    public int continued_resistance_chance;
	    
	    public String toString(){
			return   "culture" 
					+ " name=" + (name) 
					+ " chance_of_successful_propaganda=" + (chance_of_successful_propaganda) 
					+ " culture_ratio_percentage=" + (culture_ratio_percentage) 
					+ " culture_ratio_denominator=" + (culture_ratio_denominator) 
					+ " culture_ratio_numerator=" + (culture_ratio_numerator) 
					+ " initial_resistance_chance=" + (initial_resistance_chance) 
					+ " continued_resistance_chance=" + (continued_resistance_chance);
		 }
		
	/*
	 * 

TOP CULT SECTION (Culture)

  4	char		"CULT"

  4	long		number of culture opinions



  For each culture opinion:

    4	long		length of culture data (88)

    64	string		culture opinion name

    4	long		chance of successful propaganda

    4	long		culture ratio percentage (3:1 = 300, 3:4 = 75)

    4	long		culture ratio denominator (e.g. the 1 in 3:1)

    4	long		culture ratio numerator (e.g. the 3 in 3:1)

    4	long		initial resistance chance

    4	long		continued resistance chance
	 */
	    
	    /** Creates a new instance of Culture */
	    public Culture() {
		
			} 
	
}
