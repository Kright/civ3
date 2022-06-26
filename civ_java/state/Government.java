/*
 * Created on 1 févr. 2005
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

import java.io.Serializable;

import ressource.My_Toolkit;

public class Government implements Serializable {

	
	
	public int default_type;
	public int transition_type;
	public int requires_maintenance;
	public int unknow_0;
	public int standard_tile_penalty;
	public int standard_trade_bonus;
	public String name;
	public String civilopedia_entry;
	public String male_ruler_title_1;
	public String female_ruler_title_1;
	public String male_ruler_title_2;
	public String female_ruler_title_2;
	public String male_ruler_title_3;
	public String female_ruler_title_3;
	public String male_ruler_title_4;
	public String female_ruler_title_4;	
	public int corruption_and_waste;
	public int immune_to;
	public int diplomats_are;
	public int spies_are;
	public Modifier[] modifier;
	public int hurrying;
	public int assimilation_chance;
	public int draft_limit;
	public int military_police_limit;
	public int ruler_title_pairs_used;
	public int prerequisite_technology_index;
	public int science_rate_cap;
	public int worker_rate;
	public int unknow_1;
	public int unknow_2;
	public int unknow_3;
	public int free_units;
	public int free_units_per_town;
	public int free_units_per_city;
	public int free_units_per_metropolis;
	public int cost_unit;
	public int war_weariness;
	public int xenophobic;
	public int force_resettle;
    
    
    	  
			  
    	  	public static class Modifier
			{	
				
				public int can_bribe;
				public int bribery;
				public int resistance;
				
				public Modifier(){
				}	
				
				public String toString(){
					return   "Government.Modifier" 
							+ " can_bribe=" + (can_bribe) 
					 		+ " bribery=" + (bribery) 
							+ " resistance=" + (resistance) ;
				}
			}
	
	
	/*
	 * 
	 * GOVT SECTION (Governments)

  4	char		"GOVT"

  4	long		number of government types



  For each government type:

    4	long		length of government data

    4	long		default type

    4	long		transition type

    4	long		requires maintenance

    4	long		??? (0 for Rebublic/Democracy, 1 for rest)

    4	long		standard tile penalty

    4	long		standard trade bonus

    64	string		government name

    32	string		civilopedia entry

    32	string		male ruler title #1

    32	string		female ruler title #1

    32	string		male ruler title #2

    32	string		female ruler title #2

    32	string		male ruler title #3

    32	string		female ruler title #3

    32	string		male ruler title #4

    32	string		female ruler title #4

    4	long		corruption and waste

    4	long		immune to

    4	long		diplomats are

    4	long		spies are

    4	long		number of governments



    For each government type:

    (performance of above goverment type versus each of the others)

      4	long		can bribe this government type? (0 = no, 1 = yes)

      4	long		bribery modifier vs.

      4	long		resistance modifier vs.



    4	long		hurrying

    4	long		assimilation chance

    4	long		draft limit

    4	long		military police	limit

    4	long		# ruler title pairs used (max is 4)

    4	long		prerequisite technology index #

    4	long		science rate cap (10 = 100% of gold can go to science)

    4	long		worker rate

    4	long		??? (-1 for Despotism/Communism, 0 for Anarchy/Monarchy, 

			1 for Republic/Democracy)

    4	long		??? (1 for Republic/Democracy, 0 for the rest)

    4	long		???

    4	long		free units  (-1 = all, >=0 number of free units)

    4	long		free units per town

    4	long		free units per city

    4	long		free units per metropolis

    4	long		cost/unit

    4	long		war weariness

    4	long		xenophobic

    4	long		force resettle
	 */
	
	
	 	public Government() {
	    }

	 	
	 	
	    
	    public String toString(){
		return  "Government"
		+ " default_type=" + default_type
		+ " transition_type=" + transition_type
		+ " requires_maintenance=" + requires_maintenance
		+ " unknow_0=" + My_Toolkit.show_hex32(unknow_0)
		+ " standard_tile_penalty=" + standard_tile_penalty
		+ " standard_trade_bonus=" + standard_trade_bonus
		+ " name=" + name
		+ " civilopedia_entry=" + civilopedia_entry
		+ " male_ruler_title_1=" + male_ruler_title_1
		+ " female_ruler_title_1=" + female_ruler_title_1
		+ " male_ruler_title_2=" + male_ruler_title_2
		+ " female_ruler_title_2=" + female_ruler_title_2
		+ " male_ruler_title_3=" + male_ruler_title_3
		+ " female_ruler_title_3=" + female_ruler_title_3
		+ " male_ruler_title_4=" + male_ruler_title_4
		+ " female_ruler_title_4=" + female_ruler_title_4
		+ " corruption_and_waste=" + corruption_and_waste
		+ " immune_to=" + immune_to
		+ " diplomats_are=" + diplomats_are
		+ " spies_are=" + spies_are
		+ " modifier=" + modifier
		+ " hurrying=" + hurrying
		+ " assimilation_chance=" + assimilation_chance
		+ " draft_limit=" + draft_limit
		+ " military_police_limit=" + military_police_limit
		+ " ruler_title_pairs_used=" + ruler_title_pairs_used
		+ " prerequisite_technology_index=" + prerequisite_technology_index
		+ " science_rate_cap=" + science_rate_cap
		+ " worker_rate=" + worker_rate
		+ " unknow_1=" + My_Toolkit.show_hex32(unknow_1)
		+ " unknow_2=" + My_Toolkit.show_hex32(unknow_2)
		+ " unknow_3=" + My_Toolkit.show_hex32(unknow_3)
		+ " free_units=" + free_units
		+ " free_units_per_town=" + free_units_per_town
		+ " free_units_per_city=" + free_units_per_city
		+ " free_units_per_metropolis=" + free_units_per_metropolis
		+ " cost_unit=" + cost_unit
		+ " war_weariness=" + war_weariness
		+ " xenophobic=" + xenophobic
	    + " force_resettle=" + force_resettle;
	    }
	    
}
