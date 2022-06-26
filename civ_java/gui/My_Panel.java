
/*
 * Created on 15 févr. 2005
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
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import ressource.Civ_Ressource;

/**
 * @author roudoudou
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class My_Panel extends Civ_Screen {
	
	private String filename = null;
	private Civ_Ressource ressource = null;
	
	private BufferedImage background = null;
	
	public My_Panel() {
		
			super(); 
			
	}
	
	public My_Panel( BufferedImage background ) {
	
		super(); 
		init(background);
		
		
	}
	
	public void init( BufferedImage background ) {
		
			
			this.background = background;
			
			setSize( background.getWidth(), background.getHeight());
			
			setOpaque(false);
		}
		
	
	public My_Panel( Civ_Ressource ressource, String filename ) {
		
		super(); 
		
		this.ressource = ressource;
		this.filename = filename;
		
		setSize( 1024, 768 );
		
		setOpaque(true);
	}
	
	
	
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
		
		if (background!=null)			
			g2d.drawImage(background, null, 0 , 0);
		
	}
	
	/////////// Civ_Screen
	
	
	    
	    public void swing_worker_construct()
	    {
	    	if (filename==null)
	    		return;
	    				
	    	background = ressource.load_image(
		 			filename );
			
			setSize( background.getWidth(), background.getHeight());
			
			setOpaque(false);
	    }

	  
	    public void flush(){ // BufferedImage.flush()
	    	background.flush();
	    }
	    
		/**********************************/
	
}
