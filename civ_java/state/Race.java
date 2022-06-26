/*
 * Race.java
 *
 * Created on 30 mars 2004; 16:01
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
 
public class Race implements Serializable {
    
		public int city_name_idx = 0; // julien
	
        public String[] city_name;   
        public String[] hero_name;
	public String leader_name;
	public String leader_title;
	public String civilopedia_entry;
	public String adjective;
	public String civilization_name; 
	public String noun;
        public String[] forward_filename;
        public String[] reverse_filename;
        public int culture_group;
        public int leader_gender;
        public int civilization_gender;
        public int aggression_level;
        public int unique_civilization_counter;
        public int shunned_government;
        public int favorite_government;
        public int default_color;
        public int unique_color;
        public int free_tech_1;
        public int free_tech_2;
        public int free_tech_3;
        public int free_tech_4;
        public int bonuses;
        public int governor_settings; 
        public int build_never;
        public int build_often;
        public int plurality;
        public int king_unit;
        public int flavors;
        public int diplomacy_text_index;
        public String[] scientific_leader_name;
        
      
        /*
        RACE SECTION (Civilizations)

      
  4	char		"RACE"

  4	long		number of civilizations



  For each civilization:

    4	long		length of civilization data

    4	long		number of city names



    For each cityname:

      24 string		city name



    4	long		number of leaders



    For each great leader:

      32 string		great leader name



    32	string		leader name

    24	string		leader title

    32	string		civilopedia entry

    40	string		adjective

    40	string		civilization name

    40	string		noun



    For each era:

      260 string	forward filename



    For each era:

      260 string	reverse filename



    4	long		culture group

    4	long		leader gender

    4	long		civilization gender

    4	long		aggression level (-2 to 2)

    4	long		unique civilization counter

    4	long		shunned government

    4	long		favorite government

    4	long		default color

    4	long		unique color

    4	long		free tech #1 index

    4	long		free tech #2 index

    4	long		free tech #3 index

    4	long		free tech #4 index

    4	long		bonuses (binary):

					00000000

			militaristic	.......1

			commercial	......1.

			expansionist	.....1..

			scientific	....1...

			religious	...1....

			industrious	..1.....

    4	long		governor settings (binary):

						00000000

			manage citizens		.......1

			emphasize food		......1.

			emphasize shields	.....1..

			emphasize trade		....1...

			manage production	...1....

			no wonders		..1.....

			no small wonders	.1......

    4	long		build never (binary):

						00000000

			off. land units		.......1

			def. land units		......1.

			artillery land units	.....1..

			settlers		....1...

			workers			...1....

			naval units		..1.....

			air units		.1......

			growth			1.......



			production		.......1

			happiness		......1.

			science			.....1..

			wealth			....1...

			trade			...1....

			explore			..1.....

			culture			.1......

    4	long		build often (binary, same as above)

    4	long		plurality

    4	long		king unit (PRTO ref)

	4	long		flavors (binary)

	4	long		???

	4	long		diplomacy text index

	4	long		number of scientific leaders



    For each scientific leader:

      32 string		scientific leader name
      
      */
        
    /** Creates a new instance of Race */
    public Race() {
                city_name = null;
                hero_name = null;
		leader_name = "";
		leader_title = "";
		civilopedia_entry = "";
		adjective = "";	
		civilization_name = ""; 
		noun = "";
		} 
		
    public String toString(){
	return  "race" 
		+ " leader_name=" + (leader_name)
		+ " leader_title=" + (leader_title)
		+ " civilopedia_entry=" + (civilopedia_entry)
		+ " adjective=" + (adjective)
		+ " civilization_name=" + (civilization_name)
		+ " noun=" + (noun)
		//+ " city_name="  + (city_name)
		//+ " hero_name="  + (hero_name)
                + " culture_group=" + (culture_group)
				+ " free_tech=" + free_tech_1 + "," + free_tech_2 + "," + free_tech_3 + "," + free_tech_4
                ;
    }

    public static void main(String[] args) {
        System.out.println("Hello World!"); 
        Race r1 = new Race();
        System.out.println(r1);
    }
		

}
