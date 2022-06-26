
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

import java.util.Iterator;

import ressource.My_Toolkit;

/**
 * @author roudoudou
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TileValue
{			
	private Civ_State state;
	
	public int food=0;
	public int shield=0;
	public int gold=0; 		
	
			
	public String toString(){
		return   "TileValue" 
		 		+ " food=" + food
				+ " shield=" + shield 
				+ " gold=" + gold;
	}
	
	 public TileValue( Civ_State state )
      {
	 		this.state = state;
      }
	 
	 public void compute(  int x, int y )
	 	      {
			Tile t = state.readTile( x, y );
			Terrain terr = state.terrain[t.terrain_real()];
			
			food = terr.food + t.irrigation()*terr.irrigation;
			shield = terr.shields + t.mine()*terr.mining;
			gold = terr.commerce + t.road()*terr.road; 
			
			if (t.river!=0) gold += 1;
			
			if (t.bonus_grassland()!=0)	shield += 1;
			
			if (t.resource>0) {
				Good g = state.good[t.resource];
				food += g.food_bonus;
				shield += g.shields_bonus;
				gold += g.commerce_bonus;
			}

      }
	 
	 public void compute( City city, int dx, int dy )
      {
	 	int x, y;
	 	
	 	x = city.x+dx;
	 	y = city.y+dy;
		
	 	compute( x , y );
	 	
		Leader leader = state.leader[city.owner - 1];		
		Government gov = state.government[leader.government];	
		Race race = state.race[leader.civ];
		
		if (food>=3) food -= gov.standard_tile_penalty;
		if (shield>=3) shield -= gov.standard_tile_penalty;
		if (gold>=3) gold -= gov.standard_tile_penalty;
		
		if (gold>0) gold += gov.standard_trade_bonus;
		
		if (dx==0 && dy==0)
		{
			// bonus de la ville
			food += 1;
			if (shield==0) shield = 1;
		}
		
		// bonus des batiments
		
		Tile t = state.readTile( x, y );
		Terrain terr = state.terrain[t.terrain_real()];
		
		for (Iterator i = city.buildings.iterator(); i.hasNext(); )
	 	{		 		
	 		int id = ((Integer)i.next()).intValue();		
	 		Building b = state.building[id];
	 		
	 		//increases shields in water		1.......
	 		if (My_Toolkit.get_bit(b.improvements, 23)!=0
	 				&& t.is_sea())	
	 		{
	 			shield += 1;
	 		}
	 		
	 		//increases food in water			.......1
	 		if (My_Toolkit.get_bit(b.improvements, 24)!=0
	 				&& t.is_sea())	
	 		{
	 			food += 1;
	 		}
	 		
	 		
	 	}
		
	 }
	 
}


