
/*
 * Created on 26 janv. 2005
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
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.Icon;

/**
 * @author roudoudou
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
class My_Icon implements Icon {
	
	private BufferedImage img;	
	private int sx, sy, sw, sh;
	
	public My_Icon( BufferedImage img )
	{		
		this.img = img;
		this.sx = 0;
		this.sy = 0;
		this.sw = img.getWidth();
		this.sh = img.getHeight();
	}
	
	public My_Icon( BufferedImage img, int sx, int sy, int sw, int sh )
	{
		this.img = img;
		this.sx = sx;
		this.sy = sy;
		this.sw = sw;
		this.sh = sh;
	}
	
		public void paintIcon(Component c,
                  Graphics g,
                  int dx,
                  int dy) {
			// TODO Auto-generated method stub
			//System.out.println("paintIcon");
			Graphics2D g2d = (Graphics2D)g;
			
			Civ_Draw.blitSurface4( img, sx, sy, sw, sh, g2d, dx, dy );
			
			
		}

		
		public int getIconWidth() {
			// TODO Auto-generated method stub
			return sw;
		}

		public int getIconHeight() {
			// TODO Auto-generated method stub
			return sh;
		}
}


