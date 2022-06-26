
/*
 * Created on 12 sept. 2004
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

package ruler;
import state.Civ_State;

/**
 * @author roudoudou
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Civ_RulerClassic implements Civ_Ruler {

	
	Civ_State state;
	
	
	public Civ_RulerClassic()
	{
		
		
	}
	
	/* (non-Javadoc)
	 * @see Civ_Ruler#process(Civ_Command)
	 */
	public void process(Civ_Command c) {
		// TODO Auto-generated method stub
		
		System.out.println( "RulerClassic : " + c);
		
		if (c instanceof Civ_CommandMoveUnit) {
			
			Civ_CommandMoveUnit cmd = (Civ_CommandMoveUnit)c;
	
			
			//Unit u = state.find_unit_by_id(cmd.id) ;
		
			/*int dx=0,dy=0;
			switch( cmd.dir )
			{
				case 1: dx= -1; dy =  1; break;
				case 2: dx=  0; dy =  2; break;
				case 3: dx=  1; dy =  1; break;
				case 4: dx= -2; dy =  0; break;
				case 6: dx=  2; dy =  0; break;
				case 7: dx= -1; dy = -1; break;
				case 8: dx=  0; dy = -2; break;
				case 9: dx=  1; dy = -1; break;
			}*/
			int dx = Civ_State.delta_tile[2*cmd.dir+0];
			int dy = Civ_State.delta_tile[2*cmd.dir+1];
			
			/*u.x += dx;
			u.y += dy;
			
			u.x  = Civ_State.mod(u.x , state.world_w);
			u.y  = Civ_State.mod(u.y , state.world_h);
			*/
			
			state.move_unit(cmd.id, dx, dy);
		}
		else if(c instanceof Civ_CommandEndOfTurn){
			
			Civ_CommandEndOfTurn cmd = (Civ_CommandEndOfTurn)c;
			
			state.next_turn( cmd.player_id );
			
		}
		else if(c instanceof Civ_CommandUnitAction){
			
			Civ_CommandUnitAction cmd = (Civ_CommandUnitAction)c;
			
			switch( cmd.action )
			{
				case Civ_CommandUnitAction.BUILD_ROAD :
					state.build_road(cmd.id); break;
					
				case Civ_CommandUnitAction.BUILD_MINE :
					state.build_mine(cmd.id); break;
					
				case Civ_CommandUnitAction.BUILD_IRRIGATION :
					state.build_irrigation(cmd.id); break;
					
				case Civ_CommandUnitAction.BUILD_CITY :
					state.build_city(cmd.id); break;
				
				
			}
			
			
			
		}
		
		

	}

	/* (non-Javadoc)
	 * @see Civ_Ruler#reset(Civ_State)
	 */
	public void reset(Civ_State s) {
		// TODO Auto-generated method stub
		this.state = s;
	}

}
