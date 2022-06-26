/*
 * Game.java
 *
 * Created on 30 mars 2004; 17:28
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

import ressource.Civ_Loader;
import ressource.My_Toolkit;
 
public class Game implements Serializable {
    
    
	
	public int default_game_rules;
	public int default_victory_conditions;
	public int[] playable_civ;
	public int rules_set;	
	public int place_capture_units;
	public int auto_place_kings;
	public int auto_place_victory_locations;
	public int debug_mode;
	public int use_time_limit;
	public int base_time_unit;	
	public int start_month;
	public int start_week;
	public int start_year;
	public int minute_time_limit;
	public int turn_time_limit;
	public int[] turns_per_timescalepart;
	public int[] timeunits_per_turn;
	public String scenario_search_folders;
    
        /* GAME SECTION

  4	char		"GAME"

  4	long		number of scenario properties (1)



  For each scenario property:

    4	long		length of scenario property

    4	long		default game rules (1=use, 0=don't use)

    4	long 		default victory consitions (1=use, 0=don't use)

    4	long		number of playable civs (0=all playable)

    for each playable civ

      4 long		id of playable civ

    4	long		victory conditions and rules set (binarry):

							00000000

			domination victory enabled	.......1

			space race victory enabled	......1.

			diplomatic victory enabled	.....1..

			victory by conquest enabled	....1...

			cultural victory enabled	...1....

			civ specific abilities enabled	..1.....

			culturaly linked start		.1......

			restart players			1.......



			preserve random seed		.......1

			accelerated production		......1.

			elimination enabled		.....1..

			regicide enabled		....1...

			mass regicide enabled		...1....

			victory locations enabled	..1.....

			capture the flag enabled	.1......

			allow cultural conversions	1.......

    4	long		place capture units

    4	long		auto place kings

    4	long		auto place victory locations

    4	long		debug mode

    4	long		use time limit

    4	long		base time unit (0-Years, 1-Months, 2-Weeks)

    4	long		start month (1-January ... 12-December)

    4	long		start week (1-52)

    4	long		start year (negative if "BC")

    4	long		minute time limit

    4	long		turn time limit

    28	long[7]		number of turns per timescalepart

    28	long[7]		nuber of timeunits per turn

    5200	string		scenario search folders

    (4	long		map visible, BIX >= 11.19 ONLY, not BIQ (major=12))



    for each playable civ

	   4 long		alliance (0-4, 0 = none)

    4	long		victory point limit

    4	long		city elimination count

    4	long		one city cultutre win

    4	long		all cities cultutre win

    4	long		domination terrain

    4	long		domination population

    4	long		wonder cost

    4	long		defeating opposing unit cost

    4	long		advancement cost 

    4	long		city conquest population 

    4	long		victory point scoring

    4	long		capturing special unit

    4	long		???

    1	byte		???

    256	string		alliance name #0 (blank/unallied)

    256	string		alliance name #1

    256	string		alliance name #2

    256	string		alliance name #3

    256	string		alliance name #4



    For each alliance (0 to 4):

       4 long		war with alliance #0 ("unallied")

       4 long		war with alliance #1

       4 long		war with alliance #2

       4 long		war with alliance #3

       4 long		war with alliance #4

    4	long		alliance victory type

    260	string		plauge name

    1	byte		permit plauges

    4	long 		plauge earliest strat

    4	long 		plauge variation

    4	long 		plauge duration

    4	long 		plauge strength

    4	long 		plauge grace period

    4	long 		plauge max occurance

    4	long		???

    260	string		??? ("Unknown")

    4	long		respawn flag units

    1	byte		capture any flag

    4	long		gold for capture

    1	byte		map visible

    1	byte		retain culture

    4	long		???

    4	long		eruption period

    4	long		mp basetime

    4	long		mp city time

    4	long		mp unit time

    

	


         **/
        
    /** Creates a new instance of Game */
    public Game() {

    }
    
    					
    public String toString(){
	return   "game"
			+ " default_game_rules=" + (default_game_rules)
			+ " default_victory_conditions=" + (default_victory_conditions)
			+ " playable_civ=" + My_Toolkit.array_int(playable_civ)
			+ " rules_set=" + (rules_set)
			+ " place_capture_units=" + (place_capture_units)
			+ " auto_place_kings=" + (auto_place_kings)
			+ " auto_place_victory_locations=" + (auto_place_victory_locations)
			+ " debug_mode=" + (debug_mode)
			+ " use_time_limit=" + (use_time_limit)
			+ " base_time_unit=" + (base_time_unit)
			+ " start_month=" + (start_month)
			+ " start_week=" + (start_week)
			+ " start_year=" + (start_year)
			+ " minute_time_limit=" + (minute_time_limit)
			+ " turn_time_limit=" + (turn_time_limit)
			+ " turns_per_timescalepart=" + My_Toolkit.array_int(turns_per_timescalepart)
			+ " timeunits_per_turn=" +  My_Toolkit.array_int(timeunits_per_turn)
			+ " scenario_search_folders=" + (scenario_search_folders)
			;

    }
    
    public String compute_time( int turn )
    {
    	int month = start_month;
    	int week = start_week;
    	int year = start_year;
    	
    	for (int k =0; k<turns_per_timescalepart.length; k++)
    	{
    		int n = turns_per_timescalepart[k];
    		int t = timeunits_per_turn[k];
    		if (turn > n)
    		{   			
    			year += n*t;
    			turn -= n;
    			continue;
    		}
    		else
    		{   			
    			year += turn*t;
    			turn = 0;
    			break;
    		}
    		
    	}
    	
    	String txt ;
    	
    	if (year < 0)
    		txt = Integer.toString(-year) + " av J.C.";
    	else
    		txt = Integer.toString(year) + " ap J.C.";
    		
    	return txt;	
    }
    
    
    public static void main(String[] args) {
        System.out.println("Hello World!"); 
        //Game g = new Game();
        
        Game g = new Civ_Loader().get_state( "demo.biq" ).game;
		
		
        System.out.println(g);
        
        for (int i=0;i<10;i++)
        {
        	System.out.println("i=" + i + " : " + "t=" + g.compute_time(i));      
        }
        
        
    }
}
