/*
 * Building.java
 *
 * Created on 30 mars 2004, 17:59
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
	
public class Building implements Serializable {
    
		//String description;
        public String name;
        public String civilopedia_entry;
        public int doubles_happiness_of;
        public int gain_in_every_city;
        public int gain_in_every_city_on_continent;
        public int required_building;
        public int cost;
        public int culture;
        public int bombardment_defense;
        public int naval_bombardment_defense;
        public int defense_bonus;
        public int naval_defense_bonus;
        public int maintenance_cost;
        public int happy_faces_all_cities;
        public int happy_faces;
        public int unhappy_faces_all_cities;
        public int unhappy_faces;
        public int number_of_required_buildings;
        public int air_power;
        public int naval_power;
        public int pollution;
        public int production;
        public int required_government;
        public int spaceship_part;
        public int required_advance;
        public int rendered_obsolete_by;
        public int required_resource_1;
        public int required_resource_2;
        public int improvements; 
        public int other_characteristics;
        public int small_wonders;
        public int wonders;
        public int number_of_armies_required;
        public int flavors;
        public int unknow1;
        public int unit_produced;
        public int unit_frequency;
	    
	    
	    /// julien
        public int icon = 0;
        
        /*
         *BLDG SECTION (Improvements and Wonders) 2

  4	char		"BLDG"

  4	long		number of building types



  For each building type:

    4	long		length of building data (266)

    64	string		description (unused) 3

    32	string		building name

    32	string		civilopedia entry

    4   long		doubles happiness of 4

    4   long		gain in every city

    4   long		gain in every city on continent

    4   long		required building

    4   long		cost

    4   long		culture

    4   long		bombardment defense

    4   long		naval bombardment defense

    4   long		defense bonus

    4   long		naval defense bonus

    4   long		maintenance cost

    4   long		happy faces (all cities)

    4   long		happy faces

    4   long		unhappy faces (all cities)

    4   long		unhappy faces

    4   long		number of required buildings

    4   long		air power

    4   long		naval power

    4   long		pollution

    4   long		production

    4   long		required government

    4   long		spaceship part

    4   long		required advance

    4   long		rendered obsolete by

    4   long		required resource #1

    4   long		required resource #2

    4	long		improvements (binary) 5:

								00000000

			center of empire			.......1

			veteran	ground units			......1.

			+50% research output			.....1..

			+50% luxury output			....1...

			+50% tax output				...1....

			removes pop. pollution			..1.....

			reduces bldg. pollution			.1......

			resistant to bribery			1.......



			reduces	corruption			.......1

			doubles	city growth rate		......1.

			increases luxury trade			.....1..

			allows city size level 2		....1...

			allows city size level 3		...1....

			replaces other buildings		..1.....

			must be	near water			.1......

			must be	near a river			1.......



			can explode or meltdown			.......1

			veteran	sea units			......1.

			veteran	air units			.....1..

			capitalization				....1...

			allows water trade			...1....

			allows air trade			..1.....

			reduces	war weariness			.1......

			increases shields in water		1.......



			increases food in water			.......1

    4	long		other characteristics (binary):

								00000000

                        coastal	installation			.......1

                        military installation			......1.

                        wonder					.....1..

                        small wonder				....1...

                        continental mood effects		...1....

                        research installation			..1.....

                        trade installation			.1......

                        exploration installation		1.......



			place of worship			.......1

			construction installation		......1.

    4	long		small wonders (binary):

								00000000

			increases chance of leader appearance	.......1

			build armies without leader		......1.

			build larger armies			.....1..

			treasury earns 5%			....1...

			build spaceship parts			...1....

			reduces corruption			..1.....

			decreases success of missile attacks	.1......

			allows spy missions			1.......



			allows healing in enemy territory	.......1

			required goods must be in city radius	......1.

			requires victorious army		.....1..

    4	long		wonders (binary):

								00000000

			safe sea travel				.......1

			gain any advances owned by 2 civs	......1.

			double combat strength vs. barbarians	.....1..

			+1 ship movement			....1...

			doubles research output			...1....

			+1 trade in each trade-producing tile	..1.....

			halves unit upgrade cost		.1......

			pays maintenance for trade insts.	1.......



			allows all civs to build nuclears	.......1

			city growth causes +2 citizens		......1.

			+2 free advances			.....1..

			reduces war weariness in all cities	....1...

			doubles city defenses			...1....

			allows diplomatic victory		..1.....

    4	long		number of armies required

    4	long		flavors (binary)

    4	long		???

    4	long		Unit produced (PRTO ref)

    4	long		Unit frequency
         *
         **/
        
        
    /** Creates a new instance of Building */
    public Building() {
		name = "";
		cost = 0;
		required_advance = 0; 
		} 


   
   
    
    public String toString(){
    	
	return    "building" 
			//+ " description=" +  (description) 
			+ " name=" +  (name) 
			+ " civilopedia_entry=" +  (civilopedia_entry) 
			+ " doubles_happiness_of=" +  (doubles_happiness_of) 
			+ " gain_in_every_city=" +  (gain_in_every_city) 
			+ " gain_in_every_city_on_continent=" +  (gain_in_every_city_on_continent) 
			+ " required_building=" +  (required_building) 
			+ " cost=" +  (cost) 
			+ " culture=" +  (culture) 
			+ " bombardment_defense=" +  (bombardment_defense) 
			+ " naval_bombardment_defense=" +  (naval_bombardment_defense) 
			+ " defense_bonus=" +  (defense_bonus) 
			+ " naval_defense_bonus=" +  (naval_defense_bonus) 
			+ " maintenance_cost=" +  (maintenance_cost) 
			+ " happy_faces_all_cities=" +  (happy_faces_all_cities) 
			+ " happy_faces=" +  (happy_faces) 
			+ " unhappy_faces_all_cities=" +  (unhappy_faces_all_cities) 
			+ " unhappy_faces=" +  (unhappy_faces) 
			+ " number_of_required_buildings=" +  (number_of_required_buildings) 
			+ " air_power=" +  (air_power) 
			+ " naval_power=" +  (naval_power) 
			+ " pollution=" +  (pollution) 
			+ " production=" +  (production) 
			+ " required_government=" +  (required_government) 
			+ " spaceship_part=" +  (spaceship_part) 
			+ " required_advance=" +  (required_advance) 
			+ " rendered_obsolete_by=" +  (rendered_obsolete_by) 
			+ " required_resource_1=" +  (required_resource_1) 
			+ " required_resource_2=" +  (required_resource_2) 
			+ " improvements=" +  My_Toolkit.show_hex32(improvements) 
			+ " other_characteristics=" +  My_Toolkit.show_hex32(other_characteristics) 
			+ " small_wonders=" +  (small_wonders) 
			+ " wonders=" +  (wonders) 
			+ " number_of_armies_required=" +  (number_of_armies_required) 
			+ " flavors=" +  (flavors) 
			//+ " unknow1=" +  (unknow1) 
			+ " unit_produced=" +  (unit_produced) 
			+ " unit_frequency=" +  (unit_frequency) 
			;
	
	   
    }

}
