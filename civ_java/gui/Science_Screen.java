/*
 * Created on 2 oct. 2004
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
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import ressource.*;
import ressource.parser.ParseException;
import ressource.parser.PediaIcons;
import state.*;




/**
 * @author roudoudou
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */



public class Science_Screen extends My_Panel
implements  KeyListener
			,ActionListener
			
{

	private Civ_Frame frame;	
	private Civ_Main civ_main;
	private Civ_Ressource ressource;
	private Civ_State state;
	private Leader leader;
	
	
	// ressources
	private BufferedImage city_icons, units_32, pop_heads,
			  buildings_small, techboxes   ;
	
	private Font small_font,standard_font,big_font;
	
	
	private JLabel label_science, label_luxury;
    
	//	 revenus
	private JLabel 	revenus, revenus_villes, revenus_percepteurs,		
				revenus_civ, revenus_interets;	
				
	// depenses
	private JLabel 	depenses, depenses_science, depenses_distractions,		
				depenses_corruption, depenses_maintenance, depenses_unites,
				depenses_civ;	
	
	private JLabel gain, tresor;
	
	private JLabel science_nb_tours;
	
	PediaIcons pedia_icons;
	
		
	
	  /* Creates a new instance of Civ_Ecran */
	public Science_Screen( Civ_Main civ_main ) {
    
          super( civ_main.ressource,
          		civ_main.ressource.civ3("art\\advisors\\science_ancient.pcx") ); 
          
          ///////////////////////////////////
          	this.civ_main = civ_main;
      		this.ressource = civ_main.ressource;
      		this.frame = civ_main.frame;	
      		this.state = civ_main.state;
      		this.leader = state.leader[civ_main.my_id-1];
      		
      		
      		
  		addKeyListener(this); 
  		
  		this.setFocusable(true); 
  }
  
	
  public void update_value()
	 {
  		
  		System.out.println( "leader : " + leader );
	 	
	 
		
	 	

	 	repaint();
	 }
  
  
  
  /////////////// listeners //////////////////
  
 
  
  
  // action   
  public void actionPerformed(ActionEvent e) {
 	   
 		String txt = e.getActionCommand();
 		System.out.println("science_advisor, action_performed : " + txt);
 	    
 		if ("exit".equals(txt))
   		{	   			
   			civ_main.afficher_ecran("main_screen");
   		}
 		else if  ("domestic".equals(txt))
   		{	   			
   			civ_main.afficher_ecran("domestic_screen");
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
	    	
	    	// ressources
			chargement_ressources();
			
			// calcul leader_value
			System.out.println("Science_Advisor : " + leader); 		  
			
	    	// le reste ...
	    	Advisor_Tab advisor_tab = new Advisor_Tab( civ_main.ressource, this );    	
	    	advisor_tab.science.setSelected(true);
	    	advisor_tab.setLocation(0,0);
	    	this.add(advisor_tab);
	    	
	    
			
	   
	    	
	    	
	    	
	    	
			// titre
			JLabel label;
			label = new JLabel("SCIENTIFIQUE");
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setBounds( 0, 24, 1024, 25 );
			label.setFont(big_font);
			this.add(label);
			
			
			// exit
			
			My_Button exit = new My_Button(
					ressource.load_image(
      				ressource.civ3("art\\exitBox-backgroundStates.pcx")));
			exit.setLocation(950, 718);
			exit.setActionCommand("exit");
			exit.addActionListener(this);
			this.add(exit);
			
			
			// les sciences
			
			for (int i =0; i<state.tech.length; i ++)
			{
				
				
				ajoute_science( i );

			}
			
			
			
			///////////////////
			update_value();

	    }
	    
	    
	    private void ajoute_science( int n )
	    {
	    	Tech t = state.tech[n];
			
			if (t.era != 0)
				return;
			
			System.out.println("adding " + t);
			
			/*
			 * en y (83,190,273,355,..) :
			 * ancient, middle ages, industrial, modern
			 * en x (189) : 
			 * researched, researching, available, unavailable
			 */
			
			
			
			int x = 2;
			int y = 9;
			int w = 189;
			int h = 83;
			int sw = 100;
			int sh = 64;
			
			My_Icon icon0 = new My_Icon( techboxes, x+w*0, y, sw, sh );
			My_Icon icon1 = new My_Icon( techboxes, x+w*1, y, sw, sh );
			My_Icon icon2 = new My_Icon( techboxes, x+w*2, y, sw, sh );
			My_Icon icon3 = new My_Icon( techboxes, x+w*3, y, sw, sh );
			
			JButton button  = new JButton();
			button.setLocation(t.x,t.y);
			button.setSize(sw, sh);
			button.setFont(small_font);
			//button.setText(t.name);
			
			//button.setBorder(null);
			button.setFocusable(false);
			//button.setSize(sw,sh);		
			button.setFocusPainted(false);
			button.setBorderPainted(false);
			button.setRolloverEnabled(true);			
			button.setContentAreaFilled(false);
			button.setDisabledSelectedIcon(icon0);  //researched
			button.setPressedIcon(icon1); 			//researching
			button.setIcon(icon2); 					//available
			button.setDisabledIcon(icon3);			//unavailable
			//button.setPreferredSize(new Dimension(sw,sh));	
			
			if (leader.has_technology(n))
			{
				button.setEnabled(false);
				button.setSelected(true);
			}
			else
			{
				button.setEnabled(true);
				button.setSelected(false);
			}
			
			button.setLayout(null);
			JLabel label = new JLabel(t.name);
			label.setFont(small_font);
			label.setBounds(6,6,86,17);
			button.add(label);
			
			
			String filename = pedia_icons.get( t.civilopedia_entry );
	    	System.out.println( t.civilopedia_entry + " => " + filename );
			if (filename!=null)
			{
		    	Icon icon = new My_Icon( ressource.load_image(
		    			ressource.civ3(filename)));
		    	
		    	label = new JLabel(icon);
				label.setBounds(12,24,32,32);  	
		    	button.add(label);
			}
			
			
			this.add(button);
	    }
	   
	    
	    // ressources
	    private void chargement_ressources()
	    {
	    	
    		small_font = civ_main.ressource.font.deriveFont(Font.PLAIN, 10);            
            standard_font = civ_main.ressource.font.deriveFont(Font.PLAIN, 12);
            big_font = civ_main.ressource.font.deriveFont(Font.BOLD, 24);	
            	       
      		 
      		city_icons = ressource.load_image(
      				ressource.civ3("art\\City Screen\\CityIcons.pcx"));
      		
      		units_32 = ressource.load_image(
      				ressource.civ3_use_version("art\\units\\units_32.pcx", state.version));
      		
      		units_32 = Civ_Draw.change_palette(units_32, ressource.palv[civ_main.my_id]  );
      		
      	
	      		
			buildings_small = ressource.load_image(
	  				ressource.civ3_use_version("art\\City Screen\\buildings-small.pcx", state.version));
		
			techboxes = ressource.load_image(
      				ressource.civ3("art\\advisors\\techboxes.pcx"));
      		
			
		
			
			// PediaIcons
			
			InputStream stream=null; 
			
	  		try { 
	  		stream = new BufferedInputStream( 
	  				new FileInputStream(
	  						ressource.civ3("text\\PediaIcons.txt")
	  				
	  				));	
	  		}catch (FileNotFoundException e) {}
		  	
		  	
		    pedia_icons = new PediaIcons(stream);
		    
		    try {
		    	pedia_icons.Input();
		    } catch(ParseException e) {
		    	System.out.println(e);
		    	}
		    
		    /*System.out.println("\n\n\n********* testing result *********");
		    
		    final String test_key[] = { 
		    	"TECH_Masonry", 
		    	"TECH_Metallurgy" };
		    
		    for (int i=0;i<test_key.length;i++)
		    {
		    
		   		String s = pedia_icons.get( test_key[i] );
		    	System.out.println( test_key[i] + "\n  => " + s );
		    	
		    }*/
		    
	    }
	    
	    
	    
	    
	    
	    
	/**********************************/
	
		
	
		
		
		
		public static void main(String[] args) throws IOException {
			
			
			//String filename = "demo.biq";
			String filename = "Earth (Standard).bic";
			
			if (args.length>=1){
				filename = args[0];
			}
			
			Civ_Main civ_main = new Civ_Main();				
			civ_main.fast_init( filename );

			
			civ_main.afficher_ecran( "science_screen" );
			

			
			
		}
	
}






