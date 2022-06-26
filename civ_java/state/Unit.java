/*
 * unit.java
 *
 * Created on 30 mars 2004, 13:51
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
		
public class Unit implements Serializable,Comparable  {
    
        // paramètres venant du BIC 
        public String name;
        public int owner_type;
        public int experience_level;
        public int owner;
        public int prto;
        public int ai_strategy;
        public int x; 
        public int y;
        public String ptw_name;
        public int use_civilization_king;
        
        // paramètres venant du SAV 
    	public int id;
    	public int moved;
    	public int rank;
    	public int hits;
     
        /*
         UNIT SECTION

  4	char		"UNIT"

  4	long		number of units



  For each unit:

    4	long		length of unit data 

    32	string		name

    4	long		owner type (0=None, 1=Barbarians, 2=Civ, 3=Player)

    4	long		experience level

    4	long		owner (RACE ID, Player# (0=Player1 and so on) or Barbarian Tribe)

    4	long		PRTO#

    4	long		AI strategy

    4	long		X

    4	long		Y

    57	string		PTW name (replaces name)

    4	long		use civilization king
    */
        
    /** Creates a new instance of unit */
    public Unit() {
		id = -1;
		owner = 0; 
		prto = 0;
		x = 0; 
		y = 0;
		moved = 0;
		rank = 0;
		hits = 0;
	}
    
     public String toString(){
	return  "unit" 
			+ " id=" +  (id) 
			+ " owner=" +  (owner)
			+ " proto=" +  (prto)
			+ " x=" +  (x)
			+ " y=" +  (y)
			+ " moved=" +  (moved)
			+ " rank=" +  (rank)
			+ " hits=" +  (hits);
    }
    
    public int hashCode() {
        return id;
    }
    
    public int compareTo(Object obj){
        return (this.hashCode() - obj.hashCode());
    }
    
    public boolean equals(Object obj){
        return (this.hashCode() == obj.hashCode());
    }
    
     public static void main(String[] args) {
        System.out.println("Hello World!"); 
        Unit u1 = new Unit();
        Unit u2 = new Unit();
        u1.id = 1000;
        u2.id = 500;
        System.out.println(u1);
        System.out.println(u1.hashCode());
        System.out.println(u2);
        System.out.println(u2.hashCode());
        System.out.println((u1.equals(u2)));
        System.out.println((u1.compareTo(u2)));
        System.out.println((u1==u2));
    }
    
}
