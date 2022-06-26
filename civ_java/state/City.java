/*
 * City.java
 *
 * Created on 30 mars 2004; 17:36
 * 
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
import java.util.Iterator;
import java.util.LinkedList;

import ressource.My_Toolkit;



public class City implements Serializable {
   
	/*public static final int BUILD_TYPE_BUILDING = 1; 
	public static final int BUILD_TYPE_UNIT = 2; 
	*/
	
        // paramètres venant du BIC 
        public boolean has_walls;
        public boolean has_palace;
        public String name;
        public int owner_type;
        // int building[256]; 
        /*int number_of_buildings;
        int[] building_id;*/
        public LinkedList buildings; // <Integer>		
        public int culture;
        public int owner;
        public int size;
        public int x;
        public int y;
        public int city_level;
        public int border_level;
        public int use_auto_name;
        
        // paramètres venant du SAV 
        public int id;
	public int shield;
	public int food;
	/*public int build_id;
	public int build_type;
	*/
	public Build build;
	public LinkedList citizens; 
	
	public static class Build
	{	
		public static final int TYPE_BUILDING = 1; 
		public static final int TYPE_UNIT = 2; 
		
		public int type;
		public int id;
		
		public Build( int type, int id ){
			this.id= id;
			this.type = type;
		}	
		
		public String toString(){
			return   "city.build" 
			 		+ " id=" + (id) 
					+ " type=" + (type) ;
		}
	}
	
	
		

 /*        CITY SECTION

  4	char		"CITY"

  4	long		number of cities



  For each city:

    4	long		length of city data (variable)

    1	byte		has walls

    1	byte		has palace

    24	string		city name

    4	long		owner type (0=None, 1=Barbarians, 2=Civ, 3=Player)

    4	long		number of buildings

    for each building

      4 long		builning ID

    4	long		culture

    4	long		owner (RACE ID, Player# (0=Player1 and so on) or Barbarian Tribe)

    4	long		size

    4	long		X

    4	long		Y

    4	long		city level

    4	long		border level

    4	long		use auto name

*/
    /** Creates a new instance of City */
    public City() {
		id = -1;
		x = 0; 
		y = 0;
		name = "";
		owner = 0;
		shield = 0;
		food = 0;
		build = new Build( Build.TYPE_UNIT, 0  ); // default : build settler
		buildings = new LinkedList();
		citizens = new LinkedList();
		}   
    
    
    public int prod_level()
    {
    	if (border_level>0)
    		return 2;
    	else
    		return 1;
    }
    
    public int distance( int tx, int ty )
    {
    	return Civ_State.distance( tx-this.x, ty-this.y );	
    }
		
    public String toString(){
	return   "city" 			
			// paramètres venant du BIC      
            + " has_walls="  + (has_walls)
            + " has_palace="  + (has_palace)
            + " name="  + (name)
            + " owner_type="  + (owner_type)
            //+ " buildings=" + (buildings)
            + " culture="  + (culture)
            + " owner="  + (owner)
            + " size="  + (size)                       
            + " x=" + (x)
			+ " y=" + (y)
            + " city_level="  + (city_level)
            + " border_level="  + (border_level)
            + " use_auto_name="  + (use_auto_name)
            // paramètres venant du SAV 
            + " id=" + (id) 
			+ " shield=" + (shield)
			+ " food=" + (food)
			+ " build.id="  + (build.id)
			+ " build.type="  + (build.type);

        
    }
    
    
    ///////////////////////////////
    
    public static class Citizen
	{	
		// sexe
		public static final int MALE = 0; 
		public static final int FEMALE = 1; 		
		
		// humeur
		public static final int SATISFAIT = 0; 
		public static final int HAPPY = 1; 	
		public static final int OPPOSANT = 2; 
		public static final int UNHAPPY = 3; 
		
		// profession
		public static final int WORKER = 0; 
		
		public int sexe;
		public int race;
		public int humeur;
		public int profession;
		public int x,y;
		
		public Citizen( int sexe, int race, int humeur, int profession, int x, int y )
		{
			this.sexe= sexe;
			this.race = race;
			this.humeur = humeur;
			this.profession = profession;
			this.x = x;
			this.y = y;
		}	
		
		public static final String[] humeur_tab = {
				"satisfait", 
				"heureux",	
				"opposant", 
				"malheureux", 
		};
		
		public String humeur_string()
		{
			return humeur_tab[this.humeur&3];
		}
		
		public String toString(){
			return   "city.citizen" 
			 		+ " sexe=" + (sexe) 
					+ " race=" + (race) 
					+ " humeur=" + (humeur) 
					+ " profession=" + (profession)
					+ " pos=" + My_Toolkit.coord(x,y);
		}
	}

	
 	public City.Citizen find_worker_at(int x, int y)
	{
		x -= this.x;
		y -= this.y;
		
  		for (Iterator i = this.citizens.iterator(); i.hasNext(); )
		 	{
		 		City.Citizen c = (City.Citizen)i.next();
		 	
		 		if (c.profession!= City.Citizen.WORKER)
		 			continue;

		 		if (c.x==x && c.y==y){
		 			return c;
		 		}
		 		
		 	}
  		return null;
	}
    
 	public City.Citizen find_citizen_not_worker()
	{
		
		
  		for (Iterator i = this.citizens.iterator(); i.hasNext(); )
		 	{
		 		City.Citizen c = (City.Citizen)i.next();
		 	
		 		if (c.profession != City.Citizen.WORKER)
		 			return c;
		 		
		 	}
  		return null;
	}
 	
 	
 	public boolean has_building( int b )
	{	
  		for (Iterator i = buildings.iterator(); i.hasNext(); )
		 	{		 		
		 		int id = ((Integer)i.next()).intValue();
						 		
		 		if (id == b)
		 			return true;
		 		
		 	}
  		return false;
	}
 	
 	public void add_building( int b )
	{	
 		buildings.add(new Integer(b));
	}
 	
 	public static final int size_limit1 = 6;
 	public static final int size_limit2 = 12;
 	
 	public boolean is_town()
 	{
 		return (size <= size_limit1);
 	}
 	
 	public boolean is_city()
 	{
 		return ((size > size_limit1) && (size <= size_limit2));
 	}
 	
 	public boolean is_metropolis()
 	{
 		return (size > size_limit2);
 	}
	  
 	
 	
}


