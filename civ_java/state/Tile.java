/*
 * Tile.java
 *
 * Created on 30 mars 2004; 13:56
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
	
public class Tile implements Serializable {
    

	
	public static final int TERRAIN_DESERT =  0 ;
	public static final int TERRAIN_PLAINS =  1 ;
	public static final int TERRAIN_GRASSLAND =  2 ;
	public static final int TERRAIN_TUNDRA =  3;
	public static final int TERRAIN_FLOODPLAINS =  4 ;
	public static final int TERRAIN_HILLS =  5 ;
	public static final int TERRAIN_MOUNTAINS =  6 ;
	public static final int TERRAIN_FOREST =  7 ;
	public static final int TERRAIN_COAST =  11 ;
	public static final int TERRAIN_SEA =  12 ;
	public static final int TERRAIN_OCEAN =  13 ;
	
    public int file; 
 	public int river; 
 	public int resource;
 	public int image; 
 	public int overlays;
 	public int terrain; 
 	public int bonuses;
 	public int extra;
 	//
 	public int explored;
 	public int fow;
        
        
    /*    TILE SECTION

  4	char		"TILE"

  4	long		number of tiles (= width/2 * height)



  For each tile:

    4	long		length of tile data 

    1	byte		river connection info  (binary)

					00000000

			river in north	.......1

			river in west	......1.

			river in east	.....1..

			river in south	....1...

    1	byte		border

    4	long		resource

    1	byte		image

    1	byte		file:	0 = xtgc.pcx, 1 = xgpc.pcx, 2 = xdgc.pcx,

				3 = xdpc.pcx, 4 = xdgp.pcx, 5 = xggc.pcx,

				6 = wcso.pcx, 7 = wsss.pcx, 8 = wooo.pcx

    2	short		??? (0)

    1	byte		overlays (binary):

					00000000

			road		.......1

			railroad	......1.

			mine		.....1..

			irrigation	....1...

			fortress	...1....

			goody huts	..1.....

			pollution	.1......

			barbarian camp	1.......

    

    1	nibble		base terrain (PTW, 0 in C3C)

    1	nibble		real terrain (PTW, 0 in C3C)

    1	byte		bonuses (binary):

					00000000

			bonus grassland .......1

			player start    ....1...

			snow-capped mts ...1....

			pine forest     ..1.....

    1	byte		river crossing data (binarry):

					00000000

			crossing N	.......1

			crossing NE	......1.

			crossing E	.....1..

			crossing SE	....1...

			crossing S	...1....

			crossing SW	..1.....

			crossing W	.1......

			crossing NW	1.......

    2	short		barbarian tribe (-1 = none, 75 = random)

    2	short		colony (CLNY ref)

    2	short		city (CITY ref)

    2	short		continent (CONT ref)

    1	byte		???

    2	short		victory point location (0 = VPL, -1 = non VPL)

    4	long		ruin

    4	long		C3C overlays (binary)

	1	byte		???

	1	nibble		C3C base terrain

    1	nibble		C3C real terrain

    2	short		???

    2	short		fog of war

	4	long		C3C bonuses (binary)

	2	short		???   
        */
    /** Creates a new instance of Tile */
    public Tile() {
		file =0; 
		river =0; 
		resource =0;
		image =0; 
		overlays = 0;
		terrain =0; 
		bonuses =0;
		extra =0;
		explored =0;
		fow = 0;
		} 
    
   
    public int terrain_real() { return (terrain >> 4); }
    public int terrain_base() { return (terrain & 0x0f); }
    //public int road() { return (overlays & 1); }
    
    
    
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

    public int road() { return get_bit(overlays, 0); }
    public void road(int z) { overlays = set_bit(overlays, 0, z); }
    
    public int railroad() { return get_bit(overlays, 1); }
    public void railroad(int z) { overlays = set_bit(overlays, 1, z); }
    
    public int mine() { return get_bit(overlays, 2); }
    public void mine(int z) { overlays = set_bit(overlays, 2, z); }
    
    public int irrigation() { return get_bit(overlays, 3); }
    public void irrigation(int z) { overlays = set_bit(overlays, 3, z); }
    
    public int fortress() { return get_bit(overlays, 4); }
    public void fortress(int z) { overlays = set_bit(overlays, 4, z); }
    
    public int goody_huts() { return get_bit(overlays, 5); }
    public void goody_huts(int z) { overlays = set_bit(overlays, 5, z); }
    
    public int pollution() { return get_bit(overlays, 6); }
    public void pollution(int z) { overlays = set_bit(overlays, 6, z); }
    
    public int barbarian_camp() { return get_bit(overlays, 7); }
    public void barbarian_camp(int z) { overlays = set_bit(overlays, 7, z); }
    
    public int bonus_grassland() { return get_bit(bonuses, 0); }
    public void bonus_grassland(int z) { overlays = set_bit(bonuses, 0, z); }
    
    public int player_start() { return get_bit(bonuses, 3); }
    public void player_start(int z) { overlays = set_bit(bonuses, 3, z); }
    
    public int snow_capped_mts() { return get_bit(bonuses, 4); }
    public void snow_capped_mts(int z) { overlays = set_bit(bonuses, 4, z); }
    
    public int pine_forest() { return get_bit(bonuses, 5); }
    public void pine_forest(int z) { overlays = set_bit(bonuses, 5, z); }
    
 
    
    public int is_flood() {
		return ((terrain_real() == TERRAIN_FLOODPLAINS) ? 1 : 0);
	}

    public int is_cso() {
		final int[] tab_cso = {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0, 0, 0, 0};
		return (tab_cso[terrain_real()]);
	}

	
    public int is_mh() {
		final int[] tab_mh = {0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		return (tab_mh[terrain_real()]);
	}
	
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
    
  	public boolean is_sea(){
  		int t = terrain_real();
  		//return ((t==9) || (t==10) || (t==11)); // Civ3, PTW ?
  		return (t>=10); // Conquest
  	}
  	
  	public boolean is_land(){
  		return (is_sea()==false);
  	}
  	
  	
    public String toString(){
	return  "tile" 
		+ " file=" +  (file ) 
		+ " river=" +  (river )
		+ " resource=" +  (resource )
		+ " image=" +  (image )
		+ " overlays=" +  My_Toolkit.show_hex32 (overlays )
		+ " terrain=" +  My_Toolkit.show_hex8 (terrain )
		+ " bonuses=" +  (bonuses )
		+ " extra=" +  (extra )
		+ " explored=" + My_Toolkit.show_hex32 (explored )
		+ " fow=" + My_Toolkit.show_hex32 (fow );
    }
    
   
    
     public static void main(String[] args) {
        System.out.println("Hello World!"); 
        Tile t1 = new Tile();
        System.out.println(t1);
    }
    
}
