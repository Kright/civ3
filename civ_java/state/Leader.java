/*
 * Leader.java
 *
 * Created on 30 mars 2004; 17:24
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
 
	
public class Leader implements Serializable {
    
	public static class StartUnit implements Serializable {
		public int number;
		public int type;
        public StartUnit() {}
    }
   
    // paramètres venant du BIC 
    public int custom_civ_data;
    public int human_player;
    public String name;
    public StartUnit[] start_unit;
    public int gender;
    public int[] technology;
    public int difficulty;
    public int initial_era;
    public int gold;
    public int government;
    public int civ;
    public int color;
    
    // paramètres venant du SAV 
    public int id;
    public int race;
    public int capitale;
    public int tax_rate, science_rate;
    
    public int luxury_rate(){
    	return (10 - tax_rate - science_rate);
    }
    
/*    LEAD SECTION

  4	char		"LEAD"

  4	long		number of players



  For each player:

    4	long		length of player data

    4	long		custom civ data (1=use, 0=don't use)

    4	long 		human player (1=yes, 0=no)

    32	string		leader name

    4	long 		???

    4	long 		???

    4	long 		number of different start unit

    for each type of start units

      4 long		number of start units of the type

      4 long		type of start unit

    4	long 		gender of leader name

    4	long 		number of starting technologies

    for each starting technology

      4 long		technology

    4	long 		difficulty

    4	long 		initial era

    4	long 		start cash

    4	long 		government

    4	long 		civ (-2=random, -3=any)

    4	long 		color

    4	long 		skip first turn

	4	long 		???

	1	byte 		start embassies

*/
        
    /** Creates a new instance of Leader */
    public Leader() {
                        id = -1;
			race = -1;
			capitale = -1;
			tax_rate = 5;
			science_rate = 5;
			technology = new int[0];
			} 
    
    
    
    public boolean has_technology( int n )
    {
    	for (int i=0;i<technology.length;i++)
    	{
    		if (technology[i]==n)
    			return true;
    	}
    	return false;
    }
    
    

    public String toString(){
	return   "leader"
                // paramètres venant du BIC   
                + " name=" + (name) 
                + " custom_civ_data=" + (custom_civ_data) 
                + " human_player=" + (human_player) 
                //+ " start_unit=" + (start_unit) 
                + " gender=" + (gender) 
                + " technology=" + My_Toolkit.array_int(technology) 
                + " difficulty=" + (difficulty) 
                + " initial_era=" + (initial_era) 
                + " start_cash=" + (gold) 
                + " government=" + (government) 
                + " civ=" + (civ) 
                + " color=" + (color) 
                // paramètres venant du SAV 
		+ " id=" + (id) 
		+ " race=" + (race) 
		+ " capitale=" + (capitale)
		+ " tax_rate=" + (tax_rate) 
		+ " science_rate=" + (science_rate)	
		;
    }
    
}
