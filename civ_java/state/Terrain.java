/*
 * Terrain.java
 *
 * Created on 30 mars 2004; 18:02
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
	
public class Terrain implements Serializable {
    
  
	public String name; //32
	public String civilopedia_entry; //32
	public int	irrigation; // (food) bonus
	public int	mining; // (shields) bonus
	public int	road; // (commerce) bonus  
	public int	defense; // bonus	
	public int movement_cost; 
	public int food; 
	public int shields; 
	public int commerce;
	public int worker_job;
	public int pollution_effect;
	public boolean allow_cities;
	public boolean allow_colonies;
	public boolean impassable;
	public boolean impassable_by_wheeled;
	public boolean allow_airfields;
	public boolean allow_forts;
	public boolean allow_outposts;
	public boolean allow_radar_towers;
	// skip 4
	public boolean landmark_enabled;
	public int landmark_food;
	public int landmark_shields;
	public int landmark_commerce;
	public int	landmark_irrigation; // (food) bonus
	public int	landmark_mining; // (shields) bonus
	public int landmark_road; // (commerce) bonus
	public int landmark_movement_cost;
	public int landmark_defense_bonus;
	public String landmark_name; //32
	public String landmark_civilopedia_entry; //32
    //  skip 4
	public int terrain_flags;
	public int disease_strength;    
	
    /*
     *
     *TERR SECTION (Terrain)

  4	char		"TERR"

  4	long		number of terrains



  For each terrain:

	4	long		length of terrain data (113)

	4	long		number of possible resources

	*	byte[]		possible resources (binary)

			1 for the corresponding indexes of the resources

    32	string		terrain name

    32	string		civilopedia entry

    4	long		irrigation (food) bonus

    4	long		mining (shields) bonus

    4	long		road (commerce) bonus

    4	long		defense bonus

    4	long		movement cost

    4	long		food

    4	long		shields

    4	long		commerce

    4	long		worker job

    4	long		pollution effect

    1	byte		allow cities

    1	byte		allow colonies

    1	byte		impassable

    1	byte		impassable by wheeled

    1	byte		allow airfields

    1	byte		allow forts

    1	byte		allow outposts

    1	byte		allow radar towers

    4	long		???

    1	byte		landmark enabled

    4	long		landmark food

    4	long		landmark shields

    4	long		landmark commerce

    4	long		landmark irrigation (food) bonus

    4	long		landmark mining (shields) bonus

    4	long		landmark road (commerce) bonus

    4	long		landmark movement cost

    4	long		landmark defense bonus

    32	string		landmark name

    32	string		landmark civilopedia entry

    4	long		???

    4	long		terrain flags

    4	long		disease strength

    
     *
     **/    
        
    /** Creates a new instance of Terrain */
    public Terrain() {
		name = "";
		movement_cost = 0; 
		food = 0;
		shields = 0; 
		commerce = 0;
		} 
				
    public String toString(){
	return   "terrain" 
			+ " name=" + (name) 
			+ " civilopedia_entry=" + (civilopedia_entry) 
			+ " irrigation=" + (irrigation) 
			+ " mining=" + (mining) 
			+ " road=" + (road) 
			+ " defense=" + (defense) 
			+ " movement_cost=" + (movement_cost)
			+ " food=" + (food)	
			+ " shields=" + (shields)	
			+ " commerce=" + (commerce)
			+ "\n worker_job=" + My_Toolkit.show_hex32(worker_job) 
			+ " pollution_effect=" +  My_Toolkit.show_hex32(pollution_effect) 
			+ " allow_cities=" + (allow_cities) 
			+ " allow_colonies=" + (allow_colonies) 
			+ " impassable=" + (impassable) 
			+ " impassable_by_wheeled=" + (impassable_by_wheeled) 
			+ " allow_airfields=" + (allow_airfields) 
			+ " allow_forts=" + (allow_forts) 
			+ " allow_outposts=" + (allow_outposts) 
			+ " allow_radar_towers=" + (allow_radar_towers) 
			+ "\n landmark_enabled=" + (landmark_enabled) 
			+ " landmark_food=" + (landmark_food) 
			+ " landmark_shields=" + (landmark_shields) 
			+ " landmark_commerce=" + (landmark_commerce) 
			+ " landmark_irrigation=" + (landmark_irrigation) 
			+ " landmark_mining=" + (landmark_mining) 
			+ " landmark_road=" + (landmark_road) 
			+ " landmark_movement_cost=" + landmark_movement_cost
			+ " landmark_defense_bonus=" + landmark_defense_bonus
			+ " landmark_name=" + landmark_name
			+ " landmark_civilopedia_entry=" + landmark_civilopedia_entry
			+ " terrain_flags=" + My_Toolkit.show_hex32(terrain_flags)
			+ " disease_strength=" + (disease_strength);

    }
}
