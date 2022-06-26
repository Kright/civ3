/*
 * Created on 13 févr. 2005
 *
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
import java.awt.image.*;
import javax.swing.*;



import java.io.IOException;

/**
 * @author roudoudou
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Title_Screen extends My_Panel
	implements  KeyListener
				,ActionListener
				
	{
	
	private Civ_Main civ_main;
	
	private Font font;
	
	private My_Menu menu;


	

	
	   
	  /* Creates a new instance of Civ_Ecran */
    public Title_Screen( Civ_Main civ_main ) {
      
            super( civ_main.ressource,
            		civ_main.ressource.civ3("art\\title.pcx") ); 
            
            ///////////////////////////////////
            this.civ_main = civ_main;
         
    		addKeyListener(this); 
    		
    		this.setFocusable(true); 
    }
 
    
   
    
   
    
    /////////////// listeners //////////////////
    
    
    // action   
    public void actionPerformed(ActionEvent e) {
   	   
   		String txt = e.getActionCommand();
   		//System.out.println("action_performed : " + txt);
   	    
   		
    	if ("Quitter".equals(txt))
	  	{
    		civ_main.quitter();
	  	}
    	else if ("Nouvelle partie".equals(txt))
	  	{
    		civ_main.nouvelle_partie();   		
    	}
   } 
    
    
    public void keyTyped(KeyEvent e) {
		//System.out.println("KEY TYPED: " + e);
	}

	
	public void keyPressed(KeyEvent e) {
		//System.out.println("KEY PRESSED: " + e);
	}
	
	public void keyReleased(KeyEvent e) {
		//System.out.println("KEY RELEASED: " + e);
		
		switch (e.getKeyCode()) {
			case KeyEvent.VK_ESCAPE :
				civ_main.afficher_ecran("previous");
				break;

		
		}
	}
	
    
    
    /////////// Civ_Screen
	
	 
	    
	    public void swing_worker_construct()
	    {
	    	// SwingWorker.construct()
	    	super.swing_worker_construct();
	    	
	    	// le reste ...
	    	

			menu = new My_Menu( civ_main.ressource ) ;		
			menu.swing_worker_construct();			
			menu.setSize(180, 320);
			menu.setLocation( 840, 150 );
			menu.addActionListener(this);
			
			
			
			menu.add("Nouvelle partie", true );
			menu.add("Démarrage rapide", false );
			menu.add("Didacticiel", false );
			menu.add("Charger une partie", false );
			menu.add("Charger un scénario", false );
			menu.add("Panthéon", false );
			menu.add("Préférences", false );
			menu.add("Préférences audio", false );
			menu.add("Crédits", false );
			menu.add("Quitter", true );
			 
			
			
			
			this.add(menu);
 
	    	
	    }
	    
	   
	    
	   
	
	/**********************************/
	
		
	
		
		
		
		public static void main(String[] args) throws IOException {
			
			
			String filename = "demo.biq";
			
			if (args.length>=1){
				filename = args[0];
			}
			
			Civ_Main civ_main = new Civ_Main();				
			civ_main.fast_init( filename );

			
			civ_main.afficher_ecran( "title_screen" );
			

			
			
		}

}
