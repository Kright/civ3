/*
 * Created on 4 févr. 2005
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
package gui;
import java.util.EventObject;

import ressource.My_Toolkit;
/**
 * @author roudoudou
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Civ_PanelEvent extends EventObject {
		
		public static final int ID_PRESSED_LEFT = 1;
		public static final int ID_PRESSED_RIGHT = 2;
		public static final int ID_DOUBLE_CLICK = 3;
		
		public int id;
		public int mx,my;
		public int tx,ty;
		
        public Civ_PanelEvent(Object source) {
            super(source);
        }
        
       
        public String toString(){
        	return "Civ_PanelEvent" 
					+ " id=" + id
					+ " mouse=" + My_Toolkit.coord(mx,my)
					+ " tile=" + My_Toolkit.coord(tx,ty);
        }
}


