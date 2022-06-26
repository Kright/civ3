
/*
 * Created on 1 déc. 2004
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

/**
 * @author roudoudou
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Civ_CommandUnitAction implements Civ_Command {

	public static final int BUILD_ROAD = 1;
	public static final int BUILD_MINE = 2;
	public static final int BUILD_IRRIGATION = 3;
	public static final int BUILD_CITY = 4;
	
	
	
	int id;
	int action;
	
	public Civ_CommandUnitAction( int id, int action ){
		this.id = id ;
		this.action = action ;
		
	}
	
	public String toString()
	{
		return "unit " + id + " action " + action ;
	}
	
	
}
