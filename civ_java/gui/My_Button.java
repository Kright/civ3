
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
import java.awt.image.BufferedImage;
import java.awt.Dimension;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 * @author roudoudou
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

class My_Button extends JButton /*implements ChangeListener*/ {
	
	/*
		public void stateChanged(ChangeEvent e){
			// Invoked when the target of the listener has changed its state.
			System.out.println("stateChanged :" + e);
		}
			*/
	

		public My_Button( BufferedImage img )
		{
			super();	 
			
			int sw = img.getWidth()/3;
			int sh = img.getHeight();
			  	
			init( img, 0, 0, sw, sh, sw, 0 );
		}
	
	
		public My_Button( BufferedImage img, int sx, int sy, int sw, int sh )
		{
	  		super();	 
  			  	
		  	init( img, sx, sy, sw, sh, sw, 0);
		}
		
		public My_Button( BufferedImage img, int sx, int sy, int sw, int sh,
				int stepx, int stepy)
		{
	  		super();	 
  		
	  		init( img, sx, sy, sw, sh, stepx, stepy);  			  	
		}
		
		public My_Button( Icon icon0, Icon icon1, Icon icon2 )
		{
			super();	
			
			init( icon0, icon1, icon2 );		
		}
		
		private void init( BufferedImage img, int sx, int sy, int sw, int sh,
				int stepx, int stepy)
		{
			Icon icon0 = new My_Icon( img, sx+stepx*0, sy+stepy*0, sw, sh );  
		  	Icon icon1 = new My_Icon( img, sx+stepx*1, sy+stepy*1, sw, sh ); 
		  	Icon icon2 = new My_Icon( img, sx+stepx*2, sy+stepy*2, sw, sh ); 
		  	
		  	init( icon0, icon1, icon2 );	
		}
		
		
		private void init( Icon icon0, Icon icon1, Icon icon2 )
		{		
			int sw = icon0.getIconWidth();
			int sh = icon0.getIconHeight();
			
			this.setFocusable(false);
			this.setSize(sw,sh);		
			this.setFocusPainted(false);
			this.setBorderPainted(false);
			this.setRolloverEnabled(true);
			this.setContentAreaFilled(false);
			this.setIcon(icon0);  
			this.setRolloverIcon(icon1); 
			this.setPressedIcon(icon2); 
			//this.setSelectedIcon(icon2); 
			
			//ROLLOVER_ICON_CHANGED_PROPERTY
	  		//this.addChangeListener(this);
	  		//this.getPropertyChangeListeners()
			
			
			this.setPreferredSize(new Dimension(sw,sh));	
		}
	  	
}

