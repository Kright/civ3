/*
 * Proto.java
 *
 * Created on 30 mars 2004; 15:48
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
 
 
public class Proto implements Serializable {
    
		
	
	public int id=0;
	
    //char name[32]; 
	//char civilopedia_entry[32]; 
    public String name;
    public String civilopedia_entry;
    
    public int bombard_strength;
    public int bombard_range;
    public int capacity;
    public int shield_cost;
    public int defense;
    public int icon_index;
    public int attack; 
    public int operational_range;
    public int population_cost;
    public int rate_of_fire;
    public int movement;
    public int required;	
    public int upgrade_to;
    public int required_resource_1;
    public int required_resource_2;
    public int required_resource_3;	
    public int abilities;
    public int strategy;
    public int available_to;
    public int orders;
    public int air_missions;
    public int class_;
	
    public int other_strategy;
    public int hp_bonus; 
    public int ptw_standard_orders;
    public int ptw_special_actions; 
    public int ptw_worker_actions;
    public int ptw_air_missions;
    public int ptw_actions_mix;
      
        
        /* PRTO SECTION (Units)

  4	char		"PRTO"

  4	long		number of units



  For each unit:

    4	long		length of unit data

    4	long		zone of control

    32	long		unit name

    32	long		civilopedia entry

    4	long		bombard strength

    4   long		bombard range

    4   long		capacity

    4   long		shield cost

    4   long		defense

    4   long		icon index

    4   long		attack

    4   long		operational range

    4   long		population cost

    4   long		rate of fire

    4   long		movement

    4   long		required

    4   long		upgrade to

    4   long		required resource #1

    4   long		required resource #2

    4   long		required resource #3

    4   long		unit abilities (binary):

							00000000

			wheeled				.......1

			foot soldier			......1.

			blitz				.....1..

			cruise missile			....1...

			all terrain as roads		...1....

			radar				..1.....

			amphibious unit			.1......

			submarine			1.......



			aircraft carrier		.......1

			draft				......1.

			immobile			.....1..

			sink in sea			....1...

			sink in ocean			...1....

			UNUSED (was mounted)		..1.....

			carry foot units only		.1......

			starts golden age		1.......



			nuclear	weapon			.......1

			hidden nationality		......1.

			army				.....1..

			leader				....1...

			ICBM				...1....

			stealth				..1.....

			can see	submarines		.1......

			tactical missile		1.......



			can carry tactical missiles	.......1

			ranged attack animations	......1.

			turn to	attack			.....1..

    4	long		AI-strategy (binary):

							00000000

			offense				.......1

			defense				......1.

			artillery			.....1..

			explore				....1...

			army				...1....

			cruise missile			..1.....

			air bombard			.1......

			air defense			1.......



			naval power			.......1

			air transport			......1.

			naval transport			.....1..

			naval carrier			....1...

			terraform			...1....

			settle				..1.....

			leader				.1......

			tactical nuke			1.......



			ICBM				.......1

			naval missile transport		......1.

    4   long		available to (binary)

    4   long		standard orders/special actions

							00000000

			skip turn			.......1

			wait				......1.

			fortify				.....1..

			disband				....1...

			go to				...1....

			load				..1.....

			unload				.1......

			airlift				1.......



			pillage				.......1

			bombard				......1.

			airdrop				.....1..

			build army			....1...

			finish improvement		...1....

			upgrade				..1.....

			build colony			.1......

			build city			1.......



			build road			.......1

			build railroad			......1.

			build fort			.....1..

			build mine			....1...

			irrigate			...1....

			clear forest			..1.....

			clear jungle			.1......

			plant forest			1.......



			clear pollution			.......1

			automate			......1.

			join city			.....1..

    4	long		air missions (binary):

			meaning			duped from6	00000000

			bomb					.......1

			recon					......1.

			intercept				.....1..

			re-base					....1...

			precision bombing			...1....

			(unused) 				..1.....

			bombard			orders		.1......

			build road		orders		1.......



			build railroad		orders		.......1

			build fort		orders		......1.

			build mine		orders		.....1..

			irrigate		orders		....1...

			clear forest		orders		...1....

			clear jungle		orders		..1.....

			plant forest		orders		.1......

			clear pollution 	orders		1.......



			terraform		AI-strategy 	.......1

			immobile		abilities	......1.

			precision bombing	air missions	.....1..

    4   long		class 0=land, 1=sea, 2=air

    4   long		other strategy for unit (-1 if first strategy/not multiplie strategies)

    4   long		HP bonus 

    4   long		PTW standard orders (binary)

							00000000

			skip turn			.......1

			wait				......1.

			fortify				.....1..

			disband				....1...

			go to				...1....

			explore				..1..... 

 			sentry				.1...... 

    4   long		PTW special actions (binary)

							00000000 (first byte)

  			load				.......1 

  			unload				......1. 

  			airlift				.....1.. 

  			pillage				....1... 

  			bombard				...1.... 

  			airdrop				..1..... 

  			build army			.1...... 

  			finish improvements		1....... 



 							00000000 (second byte)

  			upgrade unit			.......1 

  			capture				......1. 

    4   long		PTW worker actions (binary)

							00000000 (first byte)

  			build colony			.......1 

  			build city			......1. 

  			build road			.....1..

  			build railroad			....1...

  			build fortress			...1....

  			build mine			..1..... 

  			irrigate			.1...... 

  			clear forest			1....... 



							00000000 (second byte)

   			clear jungle			.......1 

   			plant forest 			......1. 

   			clear pollution 		.....1.. 

   			automate 			....1... 

   			join city 			...1.... 

   			build airfield 			..1..... 

   			build radar tower 		.1...... 

   			build outpost 			1....... 

    4   long		PTW air missions (binary)

    							00000000 (first byte)

   			bombing				.......1

			recon				......1.

			intercept			.....1..

			re-base				....1...

			precision bombing		...1....

    2   short		PTW "actions mix" (binary)

    		meaning			duped from8	00000000 

    		sentry			std orders		.......1

 			bombard 		special actions	......1. 

 			build colony OR

 			build road 		worker actions	.....1..

 			build road		worker actions	....1...

 			build railroad	worker actions		...1....

 			build road		worker actions	..1.....

 			irrigate		worker actions	.1......

 			clear forest	worker actions		1.......



 			clear jungle	worker actions		.......1

 			clear pollution	worker actions		......1.

			automate		worker actions	.....1..

			automate		worker actions	....1...

			bombing			air missions	...1....

			precision bombing	air missions	..1....

			automate		worker actions	.1......

			goto			std orders OR	

			rebase			air missions	1.......

    2   short		??? (1)

    4   long		bombard effects



    For each terrain (PTW: 12 C3C: 14)   

      1 byte		ignore movment cost

    4   long		require support

    4   long		???

    4   long		???

    4   long		???

    4   long		???

    4   long		enslave results

    4   long		???

    4   long		number of stealth targets



    For each stealth target   

      4 long		stealth target (PRTO ref)

    4   long		???

    4   long		???

    1   byte		create craters

    4   float		worker strength

    4   long		???

    4   long		air defense


        */
        
    /** Creates a new instance of Proto */
    public Proto() {
		name = ""; 
		civilopedia_entry = "";
		defense = 0;
		attack = 0; 
		movement = 0;
		required = 0;
		class_ = 0;
		available_to = 0;
		shield_cost = 0;
		abilities = 0;
		strategy = 0;
		orders = 0;
		} 
	
	  public int hashCode(){
    	return id;
	}
	
   public boolean equals(Object obj) {
	Proto p = (Proto)obj;
	return (this.id==p.id);
			
   }	
		
    public String toString(){
	return  "proto"
			+ " name=" + (name) 
			+ " civilopedia_entry=" + (civilopedia_entry) 
			+ " shield_cost=" + (shield_cost)
			+ " defense=" + (defense)
			+ " attack=" + (attack)	
			+ " movement=" + (movement)
			+ " required=" + (required)			
			+ " abilities=" + My_Toolkit.show_hex32 (abilities)
			+ " strategy=" + My_Toolkit.show_hex32 (strategy)
			+ " available_to=" + My_Toolkit.show_hex32 (available_to)
			+ " orders=" + My_Toolkit.show_hex32 (orders)
			+ " air_missions=" + My_Toolkit.show_hex32 (air_missions)
			+ " class=" + (class_)
			+ " ptw_worker_actions=" + My_Toolkit.show_hex32 (ptw_worker_actions)
			;
	
	
    }
	
    public static int get_field( int x, int start, int len) { 
    	int mask = ((1<<len)-1);
    	return ((x>>start) & mask); 
    }
    
    public static int set_field( int x, int start, int len, int z) { 
    	int mask = ((1<<len)-1);
    	return ((x & (~(mask<<start))) | ((z&mask)<<start)); 
    }
    
    public static int get_bit( int x, int start) { 
    	return get_field( x, start, 1) ; 
    }
    
    public static int set_bit( int x, int start, int z) { 	
    	return set_field( x, start, 1, z); 
    }

    public int build_colony() { return get_bit(ptw_worker_actions, 0); }
    public int build_city() { return get_bit(ptw_worker_actions, 1); }
    public int build_road() { return get_bit(ptw_worker_actions, 2); }
    public int build_railroad() { return get_bit(ptw_worker_actions, 3); }
    public int build_fortress() { return get_bit(ptw_worker_actions, 4); }
    public int build_mine() { return get_bit(ptw_worker_actions, 5); }
    public int irrigate() { return get_bit(ptw_worker_actions, 6); }
    public int clear_forest() { return get_bit(ptw_worker_actions, 7); }
    

	
    
    /////////////////////////////////////
		
    public static void main(String[] args) throws Exception {
        System.out.println("Hello World!"); 
        Proto p1 = new Proto();
        p1.name ="proto_xyz";
        System.out.println(p1);
        
        {
        FileOutputStream out = new FileOutputStream("proto.dat");
		ObjectOutputStream s = new ObjectOutputStream(out);
		s.writeObject(p1);
		s.flush();
		}
		
		{
		FileInputStream in = new FileInputStream("proto.dat");
		ObjectInputStream s = new ObjectInputStream(in);
		Proto p2 = (Proto)s.readObject();
		System.out.println(p2);
    	}
    }

	

		
}
