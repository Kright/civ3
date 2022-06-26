/*
 * Good.java
 *
 * Created on 15 avril 2004, 16:03
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
import java.io.Serializable;
 
public class Good implements Serializable {
   
	public String name;
	public String civilopedia_entry;
	public int type;
	public int appearance_ratio;
	public int disappearance_probability;
	public int icon;
	public int prerequisite;
	public int food_bonus;
	public int shields_bonus;
	public int commerce_bonus;

           
    
    /*TOP GOOD SECTION (Natural Resources)

  4	char		"GOOD"

  4	long		number of natural resources


  For each resource:

    4	long		length of resource data (88)

    24	string		natural resource name

    32	string		civilopedia entry

    4	long		type

    4	long		appearance ratio

    4	long		disappearance probability

    4	long		icon

    4	long		prerequisite

    4	long		food bonus

    4	long		shields bonus

    4	long		commerce bonus
     */
    
    /** Creates a new instance of Good */
    public Good() {
    }

    public String toString(){
	return  "good"
			+ " name=" + (name) 
			+ " civilopedia_entry=" + (civilopedia_entry) 
			+ " type=" + (type)
			+ " appearance_ratio=" + (appearance_ratio)
			+ " disappearance_probability=" + (disappearance_probability)
			+ " icon=" + (icon)
			+ " prerequisite=" + (prerequisite)
			+ " food_bonus=" + (food_bonus)
			+ " shields_bonus=" + (shields_bonus)
			+ " commerce_bonus=" +  (commerce_bonus);
    }
    
}
