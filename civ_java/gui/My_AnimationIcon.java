
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

import ressource.Civ_Animation;


/**
 * @author roudoudou
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class My_AnimationIcon implements Icon {
	
	private Civ_Animation animation;	
	private BufferedImage image;
	private int numframe;
	
	// tout est fait comme si on avait une icone de taille
	// original_width x original_height

	
	public My_AnimationIcon( Civ_Animation animation, int numframe, BufferedImage palv )
	{		
		//System.out.println(animation);
		
		this.animation = animation;
		this.numframe = numframe;		
	
		image = animation.images[numframe];

		// changement palette
		if (palv!=null)
			image = Civ_Draw.change_palette(image, palv );

	}
	
		public void paintIcon(Component c,
                  Graphics g,
                  int dx,
                  int dy) {
			// TODO Auto-generated method stub
			Graphics2D g2d = (Graphics2D)g;
	
			//g2d.setColor(Color.darkGray);
			//g2d.fillRect(dx, dy, getIconWidth(), getIconHeight());

			
			dx = dx + (getIconWidth()/2) - (animation.original_width / 2) + (animation.left_offset);
			dy = dy + (getIconHeight()/2) - (animation.original_height / 2) + (animation.top_offset);   
			
			//g2d.setColor(Color.green);
			//g2d.fillRect(dx, dy, image.getWidth(), image.getHeight());
			
			
			g2d.drawImage(image, dx, dy, null);
	
		}

		
		public int getIconWidth() {
			// TODO Auto-generated method stub
			return (animation.original_width);		
		}

		public int getIconHeight() {
			// TODO Auto-generated method stub
			return (animation.original_height);
		}
}



