/*
 * Created on 3 févr. 2005
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

import java.io.*;
import java.util.zip.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Random;
import java.awt.Point;

import ressource.Civ_Loader;
import ressource.My_Toolkit;



public class Civ_State implements Serializable {

	public static final int VERSION_VANILLA = 1;
	public static final int VERSION_PTW 	= 2;
	public static final int VERSION_C3C 	= 3;
	
	public int version;
	
    public int world_w;
    public int world_h;
    public int turn; 
    
    public Tile[] world;
    public Proto[] proto;
    public Race[] race;
    public Terrain[] terrain ;
    public Building[] building;
    public Tech[] tech ;
    public Game game;
    //public Unit[] unit;
    public LinkedList unit;
    //public City[] city;
    public LinkedList city;
    public Good[] good;
    public Leader[] leader;
    public Citizen[] citizen;
    public Culture[] culture;
    public Government[] government;
    
    public Civ_State() {
    	world_w = 0;
    	world_h = 0;
    	turn = 0; 
    	
    	unit = new LinkedList();
    	city = new LinkedList();
    	
    }
    

    public String toString(){
	return  "Civ_State"
			+ " size " + (world_w) + "x" + (world_h)+ "\n"
			+ " " + (world.length) + " tile\n" 
			+ " " + (proto.length) + " proto\n" 
			+ " " + (race.length) + " race\n" 
			+ " " + (terrain.length) + " terrain\n" 
			+ " " + (building.length) + " building\n" 
			+ " " + (tech.length) + " tech\n" 
			+ " " + (unit.size()) + " unit\n" 
			+ " " + (city.size()) + " city\n" 
			+ " " + (good.length) + " good\n" 
			+ " " + (leader.length) + " leader\n" 
			+ " " + (citizen.length) + " citizen" 
			;
    }
    
    public int search_building( String civilopedia_entry )
	{		
    	for (int i=0;i<building.length; i++)
    	{
    		Building b = building[i];
     		if ( civilopedia_entry.equals(b.civilopedia_entry) )
     			return i;
     	}	
		return -1;
	}
    
    
    
    public City search_city( int tx, int ty )
	{	
    	if (city==null)	return null;   	
    	for (Iterator i=city.iterator();i.hasNext();)
	  	{
    		City c = (City)i.next();
     		if ( c.x==tx && c.y==ty )
     			return c;
     	}	
		return null;
	}
    
    public Unit find_unit_by_id( int id )
	{
    	if (id<0) return null;
    	if (unit==null)	return null;
	  	for (Iterator i=unit.iterator();i.hasNext();)
	  	{
	  		Unit u = (Unit)i.next();
	  		if ( u.id==id )
     			return(u);
     	}	
		return null;
	}
	
    public int max_unit_id()
	{
    	int max = 0;
    	if (unit==null)	return max;
	  	for (Iterator i=unit.iterator();i.hasNext();)
	  	{
	  		Unit u = (Unit)i.next();
	  		if ( u.id>max )
     			max=u.id;
     	}	
		return max;
	}
	
	public java.util.List search_units( int tx, int ty )
	{
		java.util.List tmp = new LinkedList();
		if (unit==null)	return tmp;
     	for (Iterator i=unit.iterator();i.hasNext();)
     	{
     		Unit u = (Unit)i.next();
     		if ( u.x==tx && u.y==ty )
     			tmp.add(u);
     	}	
		return tmp;
	}
	
	public Unit search_unit( int tx, int ty )
	{
		if (unit==null)	return null;
		for (Iterator i=unit.iterator();i.hasNext();)
     	{
     		Unit u = (Unit)i.next();
     		if ( u.x==tx && u.y==ty )
     			return(u);
     	}	
		return null;
	}
	
	
	public java.util.List my_units( int my_id )
	{
		java.util.List tmp = new LinkedList();
		if (unit==null)	return tmp;
		for (Iterator i=unit.iterator();i.hasNext();)
     	{
     		Unit u = (Unit)i.next();
     		if ( u.owner==my_id  )
     			tmp.add(u);
     	}	
		return tmp;
	}
	
	public java.util.List my_cities( int my_id )
	{
		java.util.List tmp = new LinkedList();
		if (city==null) return tmp;
		for (Iterator i=city.iterator();i.hasNext();)
	  	{
    		City c = (City)i.next();
     		if ( c.owner==my_id )
     			tmp.add(c);
     	}	
		return tmp;
	}
	
   
    public static int distance( int x0, int y0 )
    {
       //Distance = floor(max(x,y) + 0.5*min(x,y))
    	
    	int x,y;
    	//System.out.println("distance: p0=" + My_Toolkit.coord(x0,y0));
    	x = x0 - y0;
    	y = x0 + y0;
    	//x = Math.abs(x);
    	//y = Math.abs(y);
    	if (x<0) x= -x;
    	if (y<0) y= -y;
    	//System.out.println("distance: p=" + My_Toolkit.coord(x,y));
    	
    	int max, min;
    	if ( x > y )
    	{ max = x; min = y; }
    	else
    	{ max = y; min = x; }
    	
    	//System.out.println("distance: mm=" + My_Toolkit.coord(max,min));
    	
    	int d = (2*max+min)>>2;	
    	return d;   	
    }
    
    
    public static int mod( int a, int b )
    {
        // c = a `mod` b; // on suppose b > 0
        int c;
        c = a % b;
        if (c<0) c = c + b;
        return c;
    }
     
     
    public int ixTile( int x, int y ) {
		int xx,yy,k;
		/* xx = x % world_w;
		yy = y % world_h;*/
		xx = mod(x , world_w);
		yy = mod(y , world_h);
		k = (yy*world_w + xx) / 2;
		return k;
    }
                
                
      public Tile readTile( int x, int y ){
          return world[ixTile(x,y)];
      }
    
      public void writeTile( int x, int y, Tile t ){
          world[ixTile(x,y)]=t;
      }
      

      public static final int[] delta_tile = 
      { 
        //////////// level 0
        0,0,
		//////////// level 1
        0,-2,	// n
		1,-1, // ne
		2,0, 	// e
		1,1,	// se
		0,2,	// s
		-1,1,	// so
		-2,0,	// o
		-1,-1, // no
		////////////level 2
		-1,-3,
		1,-3,
		2,-2,
		3,-1,
		3,1,
		2,2,
		1,3,
		-1,3,
		-2,2,
		-3,1,
		-3,-1,
		-2,-2,
      };
      
      static final int[] dir_xy = 
      { 
         0,-2,	// n
		1,-1, // ne
		2,0, 	// e
		1,1,	// se
		0,2,	// s
		-1,1,	// so
		-2,0,	// o
		-1,-1, // no		
      };
      
      
      
      void update_explored( Unit u ) 
      {
      	update_explored( u.x, u.y, (1<<u.owner), 1 );	
      }
	  	    		
      void update_explored( City c ) 
      {
      	update_explored( c.x, c.y, (1<<c.owner), c.border_level );	
      }
      
      Point[] dtile = null;
      int[] dtile_len = null;
      
      boolean recherche_point( int len, int x, int y )
      {
      		for (int i=0;i<len;i++)
      		{
      			int x0 = dtile[i].x;
    			int y0 = dtile[i].y;
      			if (x0==x && y0==y)
      				return true;
      		}
      		return false;
      }
      
      public static int max(int a, int b) { return (a>b ? a : b); }
      public static int min(int a, int b) { return (a<b ? a : b); }
      
      private void build_dtile( int level ) {
      	
      	dtile = new Point[261];
      	dtile_len = new int[11]; // level 0-10
      	
      	
      	dtile[0] = new Point(0,0); 	
    	dtile_len[0] = 1;
    	
    	
    	int len2 = 0;
    	int len1 = 0;
    	int len0 = 1;
    	
    	for( int current_level = 1; current_level<=level; current_level++ )
    	{
    		//System.out.println("build_dtile level " + current_level);
        	
    		len2 = len1;
    		len1 = len0;
    		//len0 = len0;
    		
    		
    		
    		for ( int i= len2; i< len1; i++ )
    		{
    			int x0 = dtile[i].x;
    			int y0 = dtile[i].y;
    			
    			
    			for (int k=1; k<9; k ++)
    			{  
    				int x = x0 + delta_tile[k*2+0];
    				int y = y0 + delta_tile[k*2+1];
    				
    				int cost = max(Math.abs(x),Math.abs(y));
        			if (cost > current_level+1 )
        					continue;
    				
    				if (recherche_point( len0, x, y ))
    					continue;
    				
    				//System.out.println(x + "," + y + ",");
    				dtile[len0] = new Point(x,y); 	
    		    	
    				//dtile[len0].x = x;
    				//dtile[len0].y = y;
    				len0 ++;
    			}
    		}
    		
    		dtile_len[current_level] = len0;
    		
    		//System.out.println("len= " + len0);
        	
    	}
    	
      }
      
      
      
      void update_explored( int x, int y, int mask, int level  ) 
      {
      	mask = -1; // debug : pour tous les joueurs
      	//      update explored
      	
      	if (dtile==null)
      		build_dtile(10) ;
      	
      	level = min(level,10);
      	
      	
      	int len = dtile_len[level];    
      	 
      	
    	for (int i=0;i<len;i++){
    		int dx = dtile[i].x;
    		int dy = dtile[i].y;
    		
    		Tile t = readTile(x+dx,y+dy);
    		
    		t.explored |= mask;
			t.fow |= mask ;
    		
    	}
      }
      
      void reveal_map()
      {
      	for (int i=0;i<world.length;i++){
      		world[i].explored = -1;
      		world[i].fow = -1;
      	}
      }
      
      void hide_map()
      {
      	for (int i=0;i<world.length;i++){
      		world[i].explored = 0;
      		world[i].fow = 0;
      	}
      }
      
      public void reset_fow()
      {
      	hide_map();
      	
      	for (Iterator i=unit.iterator();i.hasNext();)
     	{
     		Unit u = (Unit)i.next();
    		update_explored( u );
     	}
      	
      	for (Iterator i=city.iterator();i.hasNext();)
	  	{
    		City c = (City)i.next();
    		update_explored( c );
     	}
      	
      }
	
      public LinkedList available_proto( City c )
      {
      		LinkedList out = new LinkedList();
      		Leader l = this.leader[c.owner - 1];
      		
      		
			for (int i=0;i<proto.length;i++)
			{
				Proto p = this.proto[i];
				
				if ( !l.has_technology( p.required ) )
					continue;
				
				
				if ( My_Toolkit.get_bit( p.available_to, l.civ ) == 0 )
					continue;
				
				out.add( new City.Build( City.Build.TYPE_UNIT, i ) );
			}
			
			return out;
      }
	  
      public boolean is_coastal_tile( int x, int y )
      {
      	for (int pos=0;pos<=8;pos++){
 
	 		Tile t = readTile( 
	 				x + delta_tile[2*pos+0], 
	 				y + delta_tile[2*pos+1]);
	 		
      		if ( t.is_sea() )
      			return true;
      	}
      	return false;
      }
      
      public LinkedList available_building( City c )
      {
      		LinkedList out = new LinkedList();
      		Leader l = this.leader[c.owner - 1];
 			
			for (int i=0;i<building.length;i++)
			{
				Building b = this.building[i];
				
				if ( !l.has_technology( b.required_advance ) )
					continue;
				
				if ( c.has_building(i) )
					continue;
				
				if ( b.required_building>=0 ){
					if ( !c.has_building(b.required_building) )
						continue;
				}
				
				// coastal	installation
				if (My_Toolkit.get_bit(b.other_characteristics, 0)!=0)
				{
					//System.out.println("b = " + b.name);					
					//System.out.println("is_coastal_tile = " + is_coastal_tile(c.x, c.y));
					if (!is_coastal_tile(c.x, c.y))
						continue;
				}
				
				//System.out.println("adding " + b.name);									
				out.add( new City.Build( City.Build.TYPE_BUILDING, i ) );
			}
			
			return out;
      }
      
      
      public int government_default()
      {
      	for (int i=0; i<government.length; i++)
      	{
      		if (government[i].default_type!=0)
      			return i;
      	}
      	return 0;
      }
      
      public int government_transition()
      {
      	for (int i=0; i<government.length; i++)
      	{
      		if (government[i].transition_type!=0)
      			return i;
      	}
      	return 0;
      }
      
      public void startup_init()
      {
      	
    	if (proto == null)  	proto = new Proto[0];		
		if (race == null) 	race = new Race[0];	
		if (terrain == null)  terrain = new Terrain[0];	
		if (building == null) building = new Building[0];	
		if (tech == null)  	tech = new Tech[0];	
		if (good == null)  	good = new Good[0];	
		//if (leader == null)  	leader = new Leader[0];	
		
		if (unit == null)  	unit = new LinkedList();	
		if (city == null)  	city = new LinkedList();	
		
		if (citizen == null)  		citizen = new Citizen[0];	
		if (culture == null)  		culture = new Culture[0];	
		if (government == null)  	government = new Government[0];	
		
	
		
		
		if (leader == null){
			
			int num_players = 4;
			leader = new Leader[num_players];	
			
			int g = government_default();
			
			for (int i=0;i<num_players;i++)
	      	{
	      		Leader l = new Leader();
	      		Race r = race[i+1];
	      		
	      		l.civ = i+1;
	      		l.name = r.leader_name ;
	      		l.government = g;
	      		
	      		leader[i] = l;
	      	}
			
		}

      	// start technology
      	/*for (int i=0;i<leader.length;i++)
      	{
      		Race r = race[leader[i].civ];
      		int[] tab = new int[4];
      		leader[i].technology = new int[4];
      	}
      	*/
      	
      	
      	// check cities
      	for (Iterator i=city.iterator();i.hasNext();)
	  	{
    		City c = (City)i.next();
    		check_city( c );
     	}
      	
      	// leader
      	for (int i=0;i<leader.length;i++)
      	{
      		Leader l = leader[i];
      		if (l.name.length()==0)
      		{
      			l.name = race[l.civ].leader_name ;
      		}	
      	}
      	
      }
      
      public void city_governor( City city )
      {
      	// citoyens
  		
  		int pos = 1;
  		
  		for (Iterator i = city.citizens.iterator(); i.hasNext(); )
		 {
		 	City.Citizen c = (City.Citizen)i.next();
		 			
			int cx = delta_tile[2*pos+0];
	 		int cy = delta_tile[2*pos+1];
	 		pos ++;
	 		
	 		c.profession = City.Citizen.WORKER;
	 		c.x = cx;
	 		c.y = cy;
	 		
	 	}
      }
	  
      public void add_one_citizen( City city )
      {
      	int race = this.leader[city.owner - 1].civ;		
      	
      	City.Citizen c = new City.Citizen(
				0, race, City.Citizen.HAPPY, City.Citizen.WORKER+1,
				0, 0) ;	
		city.citizens.add( c );
		city.size ++;
      }
      
      public void check_city( City city )
      {
      	
      		// citoyens
      		
      		int num = city.size - city.citizens.size();   
      		city.size = city.citizens.size();
			for (int i=0;i<num ;i++)
		 	{				
				add_one_citizen( city );		
		 	}
			
			// route
			Tile t = readTile(city.x, city.y);
			t.road(1);
			
			// gouverneur
			city_governor( city );
			
      }
      
     
    
      
      
     
      
  	
  	
  		public int compute_cost_city_build( City.Build build )
  		{
  			int cost = 0;
  			switch( build.type )
			{
				case City.Build.TYPE_UNIT:		
					cost = proto[build.id].shield_cost ;		
					break;
				
				case City.Build.TYPE_BUILDING:	
					cost = building[build.id].cost*10 ;
					break;
			
			}
  			return cost;
  		}
  		
  		public void update_city_build( City city, City.Build b )
  		{
  			city.build = b;
  			
  			// les boucliers supplémentaires sont perdus .
  			int cost = compute_cost_city_build( b );
  			if (city.shield > cost ) city.shield = cost;
  			
  		}
      
      ////////////////////
  		
  		public int find_next_unit( int player_id, int current_unit ) {

  			
  			System.out.println("find_next_unit");

  			
  			// filtrage
  			int state = (current_unit >=0) ? 0 : 1;
  			Iterator i = unit.iterator();
  			Unit u ;
  			int count = 0;
  			
  			while( true )
  			{
  				if (!i.hasNext()) 
  				{ 
  					if (state==0) break;
  					if (count>=1) break; 					
  					i = unit.iterator(); 
  					count = 1;
  				}
  				u = (Unit) i.next();
  			
  				switch( state )
				{
  					case 0:			// recherche unité courante			
  						if (u.id== current_unit )
  						{		
  							state = 1;
  							System.out.println("find_next_unit :" + u);

  						}
  						break;
  					
  					case 1:			// recherche prochaine unité
  						if (u.id== current_unit )
  						{
  							return -1;
  						}
  						if ((u.owner == player_id)
  							&& (compute_movement_available(u) > 0))
  						{
  							return u.id;
  						}
  						break;
  							
  						
				}
  			
  			}
  			
  			return -1;
  		}
      
  		public int compute_movement_cost( Proto p, int x, int y )
  		{
  	 	// class 0=land, 1=sea, 2=air
	  	// terrains :
	  	// 0 = désert
	  	// 1 = plaine (plains)
	  	// 2 = prairie (grassland)
	  	// 3 = toundra
	  	// 6 = mountain
	  	// 9 = côte (coast)
	  	// 10 = mer (sea)
	  	// 11 = ocean (ocean)
  		Tile t = readTile (x, y);
  			
	 	City c = search_city(x,y);
	 	if (c!=null)
	 		return 1;
	
	 	
	  	if (p.class_==0 && t.is_sea())
	  		return -1;
  	
	  	if (p.class_==1 && t.is_land())
	  		return -1;
	  		
	  	Terrain terr = terrain[t.terrain_real()];
	  	
	  	int	movement_cost = t.road()==1 ? 1 : 3*terr.movement_cost;
	  	return movement_cost;
  		}
  		
  		 public int compute_movement_available( Unit u )
  		{
  			Proto p = proto[u.prto];
  			return compute_movement_available(u,p);
  		}
  		 
  		public int compute_movement_available( Unit u, Proto p )
  		{
  			int	val = 3*(p.movement) - (u.moved);
  			return val;
  		}
      
	  public void move_unit( int id, int dx, int dy )
	  {

	  	// putStr $ "move_current_unit " ++ show (dx,dy) ++ "\n"	

	  	Unit u = find_unit_by_id(id) ;
	  	if (u == null)
	  		return;
	  	
	  	
	  	
	  	int x = mod( (u.x+dx) , world_w );
	  	int y = mod( (u.y+dy) , world_h );
	  		
	  		
	  	Proto p = proto[u.prto];
	  	
	  
						
	  	if (dx==0 && dy==0) // wait
	  	{
	  		u.moved = 3*p.movement;
	  		return;
	  	}
		
	  	int	movement_available = compute_movement_available( u, p );
	  	
	  	
	  	if (movement_available<=0)
	  		return;
	  	
	  	int	movement_cost = compute_movement_cost( p, x, y );
	  	if (movement_cost<0)
	  	{		// passage impossible
	  		return;
	  	}
	  	
	  				
	  	// putStr $ "moving unit : " ++ show (u3) ++ "\n"	
	  	u.x = x;
	  	u.y = y;
	  	u.moved = min ( 3*p.movement , (u.moved + movement_cost) );
	  	
	  
		
	  	
	  	//check for ennemy unit on destination tile
	  
	  
	 	int player_id = (u.owner);  	
	  	Unit ennemy = search_unit(x, y);
		if (ennemy!=null) 
	  		if (ennemy.owner != player_id)
	  			combat( u.id, ennemy.id );

	  	
	  	// on previent les autres joueurs du mouvement
	  	
/*
	  		network_broadcast( "movement " +  (u.id) 
	  			+ " " +  (u.x) 
	  			+ " " +  (u.y) );
	  		network_answer( u.owner , "unit" + " " +  u );
*/	  				

	  	// modification fow
	  	update_explored(u);
	  }
	  
	  private Random randNumber = null;
	  
	  public int rollDice( int n )
	  {
	  		if (randNumber==null)
	  				randNumber = new Random(0);  
	  		return randNumber.nextInt(n)+1;
	  }
	  

	    
	  int combat_hit( int id )
	  {
	  	
	  	Unit u = find_unit_by_id(id) ;
	  	
	  	
	  	int new_hits = u.hits + 1;
	  	
	  	
	  	    
	  	// network_broadcast ref $ "hit " ++ show (id_)  		
	      
	  	if (new_hits > u.rank)
	  	{
	  			unit.remove(u);
	  			//network_broadcast ref $ "delete_unit " ++ show (id_)  		
	  			return 1;
	  	}
	  	else 
	  	{
	  			u.hits = new_hits;
	  			//network_broadcast ref $ "unit " ++ show (new_u) 
	  			return 0;
	  	}
	  }
	  			
	  
	  
	  void combat_round( int id_attack, int id_defense )
	  {
		  	Unit u_attack = find_unit_by_id( id_attack );
		  	Unit u_defense = find_unit_by_id( id_defense );
	 
	  	    Proto p_attack = proto[u_attack.prto];
	  	    Proto p_defense = proto[u_defense.prto];
	  	    
	  	    int att = p_attack.attack ;
	  	    int def = p_defense.defense;

	  	System.out.println( "combat_round" );
	  	System.out.println( "attack  = "  + u_attack + " / " + p_attack );
	  	System.out.println( "defense = "  + u_defense + " / " + p_defense );	    
	  	
	  	int r = rollDice (att+def);
	  	
	  	int id_loser =  (att>=r) ? id_defense : id_attack ;
	  	
	  	int ret = combat_hit( id_loser );
	  	System.out.println( "combat_hit " +  ret );
	  	
	  	// jusqu'à la destruction d'une des unités		
	  	if (ret==0) 	
	  		combat_round(  id_attack,  id_defense );		
	 
	  	}

	  void combat( int id_attack, int id_defense )
	  {
	
	  	// network_broadcast ref $ "combat " ++ show (id_attack) ++ " " ++ show (id_defense) 		
	  	combat_round ( id_attack, id_defense );
	  }

	  /////////////////////
	  
	  public void build_road( int id )
	  {
	  	Unit u = find_unit_by_id(id) ;
	  	if (u == null)
	  		return;
	  	
	  	// seulemet les unités autorisées
	  	if (proto[u.prto].build_road()==0)
	  		return;
	  	 	
	  	Tile t = readTile (u.x, u.y);
	  	
	  	t.road(1);	
	  }
	  
	  public void build_mine( int id )
	  {
	  	Unit u = find_unit_by_id(id) ;
	  	if (u == null)
	  		return;
	  	
	  	// seulemet les unités autorisées
	  	if (proto[u.prto].build_mine()==0)
	  		return;
	  	
	  	Tile t = readTile (u.x, u.y);
	  	
	  	t.mine(1);	
	  }
	  
	  public void build_irrigation( int id )
	  {
	  	Unit u = find_unit_by_id(id) ;
	  	if (u == null)
	  		return;
	  	
	  	// seulemet les unités autorisées
	  	if (proto[u.prto].irrigate()==0)
	  		return;
	  	
	  	Tile t = readTile (u.x, u.y);
	  	
	  	// pas d'irrigation sur : montagne, colline, forêt
	  	switch (t.terrain_real()) {
	  	
	  	case Tile.TERRAIN_FOREST :
	  	case Tile.TERRAIN_MOUNTAINS :
	  	case Tile.TERRAIN_HILLS :
	  		break;
	  		
	  	default :
	  	case Tile.TERRAIN_GRASSLAND :
	  		t.irrigation(1);
	  		break;
	  	
	  	}
	  	
	  	
	  }
	  
	  public void build_city( int id )
	  {
	  	Unit u = find_unit_by_id(id) ;
	  	if (u == null)
	  		return;
	  	
	  	// seulemet les unités autorisées
	  	if (proto[u.prto].build_city()==0)
	  		return;
	  	
	  	// pas de ville au même endroit
	  	if (search_city(u.x,u.y)!=null)
	  		return;
	  	
	  	Race race0 = race[leader[u.owner - 1].civ];
	  	//System.out.println( "build_city " + race0 ) ;
		
	  	//String name = "Turlututu";
	  	String name = race0.city_name[race0.city_name_idx % race0.city_name.length];
	  	
	  	race0.city_name_idx ++;
	  	
	  	if ( race0.city_name_idx >= race0.city_name.length )
	  		name = "Nouveau " + name;
		
	  	build_city( name, u.x, u.y, u.owner );
	  	
	  	//on efface l'unité 
	  	unit.remove(u);
		
	  }
	  
	 
	  
	  public void build_city( String name, int x, int y, int owner )
	  {
	  	City c = new City();
	  	c.x = x;
	  	c.y = y;
	  	c.name = name;
	  	c.size = 1;
	  	c.city_level = 0;
	  	c.owner = owner;
	  	city.add(c);
	  	
	  	Tile t = readTile (c.x, c.y); 	
	  	t.road(1);	
	  }
	  
	  public void add_new_unit( Unit u )
	  {
	  		u.id = max_unit_id() + 1;
	  		this.unit.addLast(u);
	  }
	  
	  public void next_turn_unit( Unit u )
	  {
	  		if (u.moved==0)
	  		{
	  			u.hits = max(0, u.hits-1);
	  		}
	  	
	  		u.moved = 0;
	  }
	  
	  public int city_has_improvement( City c, int n )
		{	
	 		int sum = 0;
	  		for (Iterator i = c.buildings.iterator(); i.hasNext(); )
			 	{		 		
			 		int id = ((Integer)i.next()).intValue();
							 	
			 		Building b = this.building[id];
			 		
			 		
			 		int improvements = My_Toolkit.get_bit(b.improvements,n);
			 		sum += improvements;	
			 		
			 		/*if ( improvements!=0 )
			 		{
			 			System.out.println("improvement : " + b.name);
			 		}*/
			 	}
	  		return sum;
		}
	  
	  public void next_turn_city( City c )
	  {
	  	Leader l = leader[c.owner-1];
	  	
	  	CityValue v = new CityValue(this);
	  	v.compute( c );
	  	//System.out.println(city_value);
	  	
	  	c.food += v.food_income;
	  	c.shield += v.shield_income;	
	  	l.gold +=  v.gold;
	  	
	  	//croissance population
	  	if (c.food >= v.food_nb_rangees*10)
	  	{
	  		//allows city size level 2
	  		//if (city_has_improvement(c,11);
	  		c.food = 0;
	  		add_one_citizen(c);
	  		city_governor( c );
	  	}
	  	
	  	//construction
	  	int cost = compute_cost_city_build( c.build );
	  	if (c.shield >= cost ){
	  		c.shield = 0;
	  		
	  		switch( c.build.type )
			{
	  			
				case City.Build.TYPE_UNIT:	
					Unit u = new Unit();
					u.x = c.x;
					u.y = c.y;
					u.experience_level = 3 + city_has_improvement(c,1) ;
					u.prto = c.build.id;
					u.owner = c.owner;
					add_new_unit(u);
					break;
				
				case City.Build.TYPE_BUILDING:	
					c.add_building( c.build.id );
					break;
				
			}
	  		
	  	}
	  }
	  
	  public void next_turn( int player_id )
	  {
	  	Leader l = leader[player_id-1];
	  	
	  	
	  	
	  	System.out.println( "player " + player_id 
	  			+ " (" + l.name + ")"
	  			+ " : " + "end of turn " + turn );
	  	
	  	
	  	turn ++;
	  	
	  	
	  	// traitement des villes			
		for (Iterator i=city.iterator();i.hasNext();)
		{
			City c = (City)i.next();
			if ( c.owner!=player_id )
				continue;
				
  			next_turn_city(c);
		}	
			
		//	traitement des unités			
		for (Iterator i=unit.iterator();i.hasNext();)
		{
			Unit u = (Unit)i.next();
			if ( u.owner!=player_id )
				continue;
				
  			next_turn_unit(u);
		}	
	  	
	  }
      
      //////////////////////
      
    public static void main(String[] args) throws Exception {
        
    	Civ_State st = new Civ_State(); 
    	st.build_dtile( 10 );
    	System.exit(0);
    	      
        String filename = "demo.biq";
        
        if (args.length>0)
        	filename = args[0];
        
        // on charge un BIC
       	Civ_Loader sav_file = new Civ_Loader();
       	sav_file.bicload(  filename );      
        Civ_State p1 = sav_file.get_state();
        System.out.println("p1=" + p1);	     
   
   		// sauvegarde
       	GZIPOutputStream out = new GZIPOutputStream(
       								new FileOutputStream("civ_state.gz"));
		ObjectOutputStream s1 = new ObjectOutputStream(out);
		s1.writeObject(p1);
		s1.flush();
		out.close();
		
		
		// on recharge
		/*GZIPInputStream in = new GZIPInputStream(
								new FileInputStream("civ_state.gz"));
		ObjectInputStream s2 = new ObjectInputStream(in);
		Civ_State p2 = (Civ_State)s2.readObject();
		System.out.println("p2=" + p2);	
		in.close();
    	*/
    }

}
    
    