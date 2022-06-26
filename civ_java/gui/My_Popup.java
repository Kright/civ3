
/*
 * Created on 16 févr. 2005
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
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.*;

import ressource.Civ_Ressource;

/**
 * @author roudoudou
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class My_Popup extends Civ_Screen
		 {

		private Civ_Ressource ressource = null;
		
		private BufferedImage popupborders = null, 
								popup_and_menu_background= null;
		
		public My_Popup( BufferedImage popupborders,
				BufferedImage popup_and_menu_background ) {
		
			super(); 
			
			setOpaque(false);
			
			this.popupborders = popupborders;
			this.popup_and_menu_background = popup_and_menu_background;
	
		}
		
		public My_Popup( Civ_Ressource ressource ) {
		
			super(); 
			
			setOpaque(false);
			
			this.ressource = ressource;
	
		}
		
		public void basic_init( String title, ActionListener listener )
		{		 	
		 	this.setBorder(	 			
		 			BorderFactory.createTitledBorder( 
		 					BorderFactory.createEmptyBorder(24,16,24,16),					
		 					title, 
							TitledBorder.CENTER,
							TitledBorder.ABOVE_TOP, 
							ressource.font.deriveFont(Font.BOLD, 24)
					) 
		 	);	
		 	
		 	this.setLayout(new BorderLayout());
		 	
			BufferedImage x_o = ressource.load_image(
					ressource.civ3("art\\X-o_ALLstates-sprite.pcx"));
			
			My_Button button_o = new My_Button( x_o,  0, 0, 20, 20, 36, 0);	
			button_o.setActionCommand("button_o");
			button_o.addActionListener(listener);
			
			My_Button button_x = new My_Button( x_o, 20, 0, 15, 20, 36, 0);
			button_x.setActionCommand("button_x");
			button_x.addActionListener(listener);
			
			JPanel temp = new JPanel();
			temp.setOpaque(false);
			temp.setLayout(new BoxLayout(temp, BoxLayout.X_AXIS));	 	
			temp.add(Box.createHorizontalGlue());
			temp.add(button_o);
			temp.add(Box.createRigidArea(new Dimension(10, 0)));
			temp.add(button_x);
			this.add(temp, BorderLayout.PAGE_END);
			
			// debug
		/*	temp.setOpaque(true);			
			temp.setBackground(Color.GREEN);
			button_o.setBorderPainted(true);
			button_x.setBorderPainted(true);
			*/
		}
		
		private void draw( int sx, int sy, Graphics2D g2d, int dx, int dy )
		{
			Civ_Draw.blitSurface4( popupborders, 250+sx*62, sy*45, 62, 45, g2d, dx, dy );				
		}
		
		
		
		protected void paintComponent(Graphics g) {

			super.paintComponent(g);
			
			Graphics2D g2d = (Graphics2D) g;
			
			//g2d.drawImage(background, null, 0 , 0);
			
			int w = this.getWidth();
			int h = this.getHeight();
			
			
			
			
			int x,y;
			
			int w0 = popup_and_menu_background.getWidth();
			int h0 = popup_and_menu_background.getHeight();
			
			for (y=0; y<h; y+=h0){		
				for (x=0; x<w; x+=w0){	
					g2d.drawImage(popup_and_menu_background, null, x, y);
				}
			}
			
			
			// en X : 25O, 312, 374, 436
			// en Y : 0, 45, 90, 135
			
			/*
			for (y=44; y<h-44; y+=44){		
				for (x=61; x<w-61; x+=61){	
					draw( 1, 1, g2d, x, y );
				}
			}
			*/
			for (x=0; x<w; x+=61){				
				draw( 1, 0, g2d, x, 0 );
				draw( 1, 2, g2d, x, h-45 );			
			}
			
			for (y=0; y<h; y+=44){				
				draw( 0, 1, g2d, 0, y );
				draw( 2, 1, g2d, w-62, y );			
			}
			
			
			draw( 0, 0, g2d, 0, 0 );
			draw( 2, 0, g2d, w-62, 0 );
			draw( 2, 2, g2d, w-62, h-45 );
			draw( 0, 2, g2d, 0, h-45 );
				
		}
		
/////////// Civ_Screen
		
		
		    
		    public void swing_worker_construct()
		    {
		    	if (ressource==null)
		    		return;
		    	
		    	popupborders = ressource.load_image(
			 			ressource.civ3("art\\popupborders.pcx"));
			 	
			 	popup_and_menu_background = ressource.load_image(
			 			ressource.civ3("art\\PopUpandMenuBackground.pcx"));
				
		    }

		   
		    
		    public void flush(){ // BufferedImage.flush()
		    	popupborders.flush();
		    	popup_and_menu_background.flush();
		    }
		
		
		/////////:
		
		
		
		public static void main(String[] args) throws IOException {
			
			 final String[] options = { 							
					"Carte", 
					"Charger partie (Ctrl-L)",
					"Nouvelle partie (Ctrl-Maj-Q)",
					"Préférences (Ctrl-P)",
					"Abdiquer (Ctrl-Q)",
					"Sauvegarder partie (Ctrl-S)",
					"Quitter partie (Echap)",
			};
			
		 	Civ_Main civ_main = new Civ_Main();
			civ_main.fast_init2(  );
			
			My_Panel panel = new My_Panel( 
					civ_main.ressource,           		
					civ_main.ressource.civ3("art\\title.pcx") );
		 
			
		 	
		 	My_Popup popup = new My_Popup(civ_main.ressource);
		 	popup.swing_worker_construct();		 	
		 	popup.setBounds( 328, 102, 367, 302 );		 	
		 	popup.basic_init( "Menu principal", 
		 			
		 			new ActionListener() {
		 		
				 		 public void actionPerformed(ActionEvent e) {
				 	   	   
				 	   		String txt = e.getActionCommand();
				 	   		System.out.println("action_performed : " + txt);
				 	   	    
					 	   	if ("button_x".equals(txt))
				 	   		{
				 	   			System.exit(1);	
				 	   		}
				 	   	
				 	   } 
		 			}
		 	
		 	
		 	);	 
		 	panel.add(popup);
		 	
		 
		 	My_Menu menu = new My_Menu( civ_main.ressource ) ;		
			menu.swing_worker_construct();			
			menu.add(options);	
			popup.add(menu, BorderLayout.CENTER);
			
		
			// debug
			menu.setOpaque(true);	
			menu.setBackground(Color.BLUE);
			/////
			
			civ_main.frame.afficher_ecran( panel );
			
			
			
		 }
		
	
}
