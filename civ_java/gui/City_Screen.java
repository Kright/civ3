/*
 * Created on 2 déc. 2004
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
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import javax.swing.event.*;

import ressource.Civ_Ressource;
import state.Building;
import state.City;
import state.CityValue;
import state.Civ_State;
import state.Proto;
import state.TileValue;
import state.Unit;




/**
 * @author roudoudou
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */







public class City_Screen extends My_Panel
	implements  KeyListener, 
				ActionListener,
				Civ_PanelEventListener
				
	{

		private City city;
		private int wscreen;
		private int hscreen;
	
		private Civ_Main civ_main;
		private Civ_Ressource ressource;
		private Civ_State state;
		//private Civ_Draw draw;
		
		// ressources
		private BufferedImage city_icons, units_32, pop_heads,
				img_prod_button, buildings_large, buildings_small,
				production_queue_box, x_and_view, img_draft_button, img_hurry_button;
		
		private Font small_font,standard_font,big_font;
        
		private JPanel production_queue;
		
		public CityValue city_value ;
		
		private Civ_Panel civ_panel; 
	    
		private My_Button prod_button;
		private JLabel prod_button_icon, prod_button_text;
	
		
	    /* Creates a new instance of Civ_Ecran */
	    public City_Screen( City city, 
	    		Civ_Main civ_main,	
	    		int wscreen, int hscreen ) {
	      
	            super( civ_main.ressource, 
	            		civ_main.ressource.civ3("art\\City Screen\\background.pcx")); 
	            
	               
				
	            ///////////////////////////////////
	            
	            this.civ_main = civ_main;
	            

	            this.city = city;
	            this.wscreen = wscreen ;
	            this.hscreen = hscreen ;
	            
	        	
	    		this.state = civ_main.state;
	    		this.ressource = civ_main.ressource;
	    		 
	    }
	    
	    public void initialize_all( ) {
	    		
	    		//////
	    		
	    		System.out.println("City_Screen : " + city);
	    		  
	    		city_value = new CityValue(state);
	    		city_value.compute( city );
	    		System.out.println(city_value);
	    		
	            // ressources
	    		chargement_ressources();
	    		initialize_building_icons();
	    		
				
	            setOpaque(true); 
	            setSize(new java.awt.Dimension(wscreen,hscreen));
	            
	           
	    		
	    		// Register for keyboard events.
	            addKeyListener(this);
	            
	          
	        
	    		
	    	
	    		
	    		
	    		// liste buildings
	    		
    	      	initialize_building_list();

	    		
    	      	//liste production
	    	
    	      	
    	    	initialize_production_queue();
    	      	
    	      	// view : 71x43 at (871,22)
    	      	
	      		
    	      	
    	      	My_Button view_button = new My_Button( x_and_view, 0, 0, 72, 43 );	
    	      	view_button.setLocation(871, 22);
    	      	view_button.setFocusable(false); 
	    		this.add(view_button);
	    		
	    		
	    		// eXit : 36x44 at (956,22) 
	    		
    	      	My_Button x_button = new My_Button( x_and_view, 0, 44, 37, 43 );
    	      	x_button.setLocation(956, 22);
    	    	x_button.setFocusable(false); 
    	    	x_button.addActionListener(this);	    	   
	    		x_button.setToolTipText("Exit");
	    		//x_button.setMnemonic(KeyEvent.VK_ESCAPE);
	    		x_button.setActionCommand("exit");
    	    	this.add(x_button);
	    	
	    	   
    	    	
    	      	
	    		// prod : 115x95 at (905,516)
 
    	    	
    	    	initialize_prod_button();
    	    	
	    		// hurry     		
	    		My_Button hurry_button = new My_Button( 
	    				 img_hurry_button,				
						0, 0, 29, 29 );
	      		
	    		hurry_button.setLocation(860, 520);
	    		hurry_button.setFocusable(false); 
	    		this.add(hurry_button);
	    		
	    		// draft
	    		My_Button draft_button = new My_Button( 
	    				 img_draft_button,				
						0, 0, 20, 20);
	      		
	    		draft_button.setLocation(843, 524);
	    		draft_button.setFocusable(false); 
	    		this.add(draft_button);
	    		
	      		
	    		//main panel 
	    		int viewx = city.x * 64 ;
	    		int viewy = city.y * 32 ;

	    		civ_panel = new Civ_Panel( state,ressource, 
	    				1024,412,(city.owner),
	    				false); 
	    		
	    		
	    		civ_panel.set_view(viewx, viewy);
	    		civ_panel.enable_city_border = true;
	    		civ_panel.setLocation(0,94);
	    		civ_panel.addCiv_PanelEventListener(this);
	    		
	    		this.add(civ_panel);
	    		
	    		//	  POPULATION
	    		
	    		
	    		initialize_population();
	    		
	    }   
	    
	    
	    public static JLabel simple_label( Icon icon )
	    {
	    	JLabel label = new JLabel(icon);		
	    	label.setHorizontalAlignment(JLabel.CENTER);
	    	label.setVerticalAlignment(JLabel.CENTER);
	    	label.setVerticalTextPosition(JLabel.BOTTOM);
	    	label.setHorizontalTextPosition(JLabel.CENTER);
	    	label.setBounds(0,0,icon.getIconWidth(),icon.getIconHeight());
	    	return label;
	    }
	    
	    
	    public static int max(int a, int b) { return (a>b ? a : b); }
	    public static int min(int a, int b) { return (a<b ? a : b); }
	    
	      
	    static int next_pow10( int x )
	    {
	    	int b=1;
	    	while (b<=x) b *= 10;
	    	return b;
	    }
	    
	   
	    // ressources
	    private void chargement_ressources()
	    {
	    	
    		small_font = ressource.font.deriveFont(Font.PLAIN, 10);            
            standard_font = ressource.font.deriveFont(Font.PLAIN, 12);
            big_font = ressource.font.deriveFont(Font.BOLD, 24);	
            	       
      		 
      		city_icons = ressource.load_image(
      				ressource.civ3("art\\City Screen\\CityIcons.pcx"));
      		
      		units_32 = ressource.load_image(
      				ressource.civ3_use_version("art\\units\\units_32.pcx", state.version));
      		
      		units_32 = Civ_Draw.change_palette(units_32, ressource.palv[city.owner]  );
      		
      		pop_heads = ressource.load_image(
      				ressource.civ3("art\\SmallHeads\\popHeads.pcx"));
			
      		production_queue_box = ressource.load_image(
      				ressource.civ3("art\\City Screen\\ProductionQueueBox.pcx"));
      		      		
			buildings_small = ressource.load_image(
	  				ressource.civ3_use_version("art\\City Screen\\buildings-small.pcx", state.version));
							
			img_prod_button = ressource.load_image(
					ressource.civ3("art\\City Screen\\ProdButton.pcx"));

			buildings_large = ressource.load_image(
					ressource.civ3_use_version("art\\City Screen\\buildings-large.pcx", state.version));
		
			x_and_view = ressource.load_image(
      				ressource.civ3("art\\City Screen\\XandView.pcx"));
			
			img_draft_button = ressource.load_image(
    	      		ressource.civ3("art\\City Screen\\draftButton.pcx"));	
			
			img_hurry_button = ressource.load_image(
    	      		ressource.civ3("art\\City Screen\\HurryButton.pcx"));
	    }
	    
	    
	    //	  forme
	    private void initialize_building_list()
	    {
	      			
    		JList liste = new JList(city.buildings.toArray()); 
    		liste.setFont(small_font);		    			
    		BuildingListRenderer renderer = new BuildingListRenderer();
    		renderer.setPreferredSize(new Dimension(150, 32));
    		liste.setCellRenderer(renderer);
    		liste.setOpaque(false);
    		liste.setVisibleRowCount(7);
    		liste.setFixedCellHeight(32);
    	
    		
    		
    		JScrollPane scrollpane = new JScrollPane(liste);
    		scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    		scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    		scrollpane.setBounds(0,528,150,7*32);	
    		//scrollpane.setLocation(0,528);
    		scrollpane.setOpaque(false);
    		scrollpane.getViewport().setOpaque(false); 
    		scrollpane.setBorder(BorderFactory.createEmptyBorder());
    		//scrollpane.getVerticalScrollBar().setUnitIncrement(32);
    		//scrollpane.getVerticalScrollBar().setBlockIncrement(32);
    		this.add(scrollpane);
    		

    		
    		
    		this.setFocusable(true); 
    		liste.setFocusable(false); 
    		scrollpane.setFocusable(false); 
    		
    		
    		// add popup menu
			final JPopupMenu popupMenu = new JPopupMenu();
						
		    JMenuItem sellMenuItem = new JMenuItem("Sell");
		    //sellMenuItem.addActionListener(actionListener);
		    popupMenu.add(sellMenuItem);

		  
		    JMenuItem civilopediaMenuItem = new JMenuItem("Civilopedia");
		    //civilopediaMenuItem.addActionListener(actionListener);
		    popupMenu.add(civilopediaMenuItem);
		    
	    	liste.setComponentPopupMenu(popupMenu);
	    	
	    	
	    }
	  
	    private void initialize_production_queue()
		{
	    	int w = 203 ; 
	    	int h = 360 ;
			production_queue = new JPanel(null);
			production_queue.setBounds(815,130,w,h);	      	
			production_queue.setFocusable(false); 
			
			this.setOpaque(false);
			
			JLabel label = new JLabel( new My_Icon( production_queue_box)  );  
			label.setBounds(0,0,w,h);	 
			label.setOpaque(false);
			production_queue.add(label);
			
			LinkedList prod = new LinkedList();		
			prod.addAll( state.available_proto(city) );
			prod.addAll( state.available_building(city) );
			

			
		 	int row_count = 8;
		 	int cell_height = 43;
		 	final JList liste = new JList(prod.toArray()); 
			liste.setFont(small_font);		    			
			ProductionListRenderer renderer = new ProductionListRenderer();
			renderer.setPreferredSize(new Dimension(200, cell_height));
			liste.setCellRenderer(renderer);
			liste.setOpaque(false);
			liste.setVisibleRowCount(row_count);
			liste.setFixedCellHeight(cell_height);
			
			
			int border = 5;
			JScrollPane scrollpane = new JScrollPane(liste);
    		scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    		scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    		scrollpane.setBounds(border,border,w-2*border, row_count*cell_height/*h-2*border*/ );	
    		scrollpane.setOpaque(false);
    		scrollpane.getViewport().setOpaque(false); 
    		scrollpane.setBorder(BorderFactory.createEmptyBorder());
    		//scrollpane.getVerticalScrollBar().setUnitIncrement(32);
    		//scrollpane.getVerticalScrollBar().setBlockIncrement(32);
    		production_queue.add(scrollpane,0);
    		
			
    		
    		// on double_click : close production queue
    		MouseListener mouseListener = new MouseAdapter() {
    		    public void mouseClicked(MouseEvent e) {
    		        if (e.getClickCount() >= 2) {
    		            //int index = liste.locationToIndex(e.getPoint());
    		            //System.out.println("Double clicked on Item " + index);
    		            production_queue.setVisible(false);
    		         }
    		    }
    		};
    		liste.addMouseListener(mouseListener);
    		
    		final City_Screen city_screen = this;
    		
    		// on selection : update city.build and prod_button
    		ListSelectionListener selectionListener = new ListSelectionListener() {
	    		public void valueChanged(ListSelectionEvent e) {
	    		    if (e.getValueIsAdjusting() == false) {	
	    		        if (liste.getSelectedValue() != null) {	    		     
	    		        	state.update_city_build( city, (City.Build)liste.getSelectedValue() );
	    		        	initialize_prod_button();
	    		        	city_screen.update_value();
	    		        }
	    		    }
	    		}
    		};
    		liste.addListSelectionListener(selectionListener);
    		

    		production_queue.setFocusable(false); 
    		liste.setFocusable(false); 
    		scrollpane.setFocusable(false); 
    		
    		production_queue.setVisible(false);
    		this.add(production_queue);
			
		}
	    
	
	    private void initialize_prod_button(){
	    	
	    	if (prod_button==null)
	    	{
	    		int w = 116;
	    		int h = 95;
	    		
				prod_button = new My_Button( img_prod_button, 0, 0, w, h );
				prod_button.setLocation(905, 516);
					prod_button.setFocusable(false);       		
					prod_button.addActionListener(this);	    	   
				prod_button.setActionCommand("prod_button");
				prod_button.setLayout(null);
				this.add(prod_button);
				
				prod_button_icon = new JLabel();		
				prod_button_icon.setHorizontalAlignment(JLabel.CENTER);
				prod_button_icon.setVerticalAlignment(JLabel.CENTER);
				prod_button_icon.setVerticalTextPosition(JLabel.BOTTOM);
				prod_button_icon.setHorizontalTextPosition(JLabel.CENTER);
				prod_button_icon.setBounds(0,0,w,h);
				prod_button_icon.setFont(small_font);
				prod_button.add(prod_button_icon);
				
				prod_button_text = new JLabel();		
				prod_button_text.setHorizontalAlignment(JLabel.CENTER);
				prod_button_text.setVerticalAlignment(JLabel.TOP);
				prod_button_text.setVerticalTextPosition(JLabel.BOTTOM);
				prod_button_text.setHorizontalTextPosition(JLabel.CENTER);
				prod_button_text.setBounds(10,68,w-20,h);
				prod_button_text.setFont(small_font);
				prod_button.add(prod_button_text,0);
				
	    	}
			
	    	Icon icon=null;
	    	String text=null;
	    	
	    		
			switch( city.build.type )
			{
				case City.Build.TYPE_UNIT:
					//icon = icon_units_32(city.build.id);		
					icon = icon_units_large(city.build.id);					
					text = state.proto[city.build.id].name ;
					prod_button_icon.setIcon(icon);	
					prod_button_icon.setLocation(0,10);
					prod_button_icon.setVerticalAlignment(JLabel.CENTER);
					prod_button_text.setText(text);
					
					break;
				
				case City.Build.TYPE_BUILDING:
					icon = icon_buildings_large(city.build.id);					
					text = state.building[city.build.id].name ;
					prod_button_icon.setIcon(icon);	
					prod_button_icon.setLocation(0,20);
					prod_button_icon.setVerticalAlignment(JLabel.TOP);
					prod_button_text.setText(text);
					break;
			
			}
			
			
	    	
		
			
		
	
	    }
	    
	    void initialize_population()
	     {
		 	civ_panel.removeAll();
		 	
			//int cx = civ_panel.getWidth() / 2;
			//int cy = civ_panel.getHeight() / 2;		
		
			
			int head_x;
			int stepx = 800 / city.size ; 
			stepx = min(stepx,50);
			head_x = ( 1024 - city.size*stepx) / 2 ;	   		 
  		 	
  		 	
  		 	for (Iterator i = city.citizens.iterator(); i.hasNext(); )
  		 	{
  		 		City.Citizen c = (City.Citizen)i.next();
	 	
  		 		// tête du citoyen
  		 		ajoute_icone_citoyen( c, head_x , 435-94 );
				head_x += stepx;
  		 	
  		 		// production du citoyen
  		 		if (c.profession!= City.Citizen.WORKER)
  		 			continue;
  		 		
  		 		ajoute_icone_tilevalue( c.x, c.y );
  		 	
  	  		}
  		 	
  		 	// case de la ville
  		 	ajoute_icone_tilevalue( 0, 0 );
  		 	
  		 	civ_panel.validate();
	     	
	     }
	    
	    void ajoute_icone_citoyen( City.Citizen c, int x , int y )
	    {
	    	int epoque = 0;	    
	 		JLabel head_icon = simple_label(icon_pop_heads(c, epoque));		  		 		
	 		head_icon.setLocation(x , y);
	 		head_icon.setFont(small_font);
	 		head_icon.setToolTipText(
	 				state.citizen[c.profession].name 
	 				+ " (" + state.race[c.race].civilization_name + ") " 
				+ c.humeur_string() 
				);
	 		
	 		civ_panel.add(head_icon);		
	    }
		
	    void ajoute_icone_tilevalue( int cx, int cy)
	    {
	    	TileValue v = new TileValue(state);			
	    	v.compute( city, cx, cy );	 		
		 	CityIcon icon  = new CityIcon( v );
		 	JLabel label = new JLabel(icon);		
	    	label.setHorizontalAlignment(JLabel.CENTER);
	    	label.setVerticalAlignment(JLabel.CENTER);
	    	
	    	
	    	Point p = civ_panel.to_screen( city.x + cx, city.y + cy);	 		
	    	label.setBounds(p.x-64,p.y-32,128,64);
    	 		
		 	civ_panel.add( label  );
	    }
	
		
		void draw_icon( Graphics2D g2d, int id, int x, int y )
		{
			// 25 icones 32x32		
			Civ_Draw.blitSurface4( city_icons, id*31, 0, 32, 32, g2d, x, y );
		}
		
		void draw_unit( Graphics2D g2d, int id, int x, int y )
		{
			// 14x6 icones 32x32		
			int sx = (id % 14)*33;
			int sy = (id / 14)*33;
			Civ_Draw.blitSurface4( units_32, sx, sy, 32, 32, g2d, x, y );
		}
		
		Icon icon_units_32( int id )
		{
			// lookup icon_index
			id = state.proto[id].icon_index ;
			// 14x6 icones 32x32		
			int sx = (id % 14)*33;
			int sy = (id / 14)*33;
			return new My_Icon(units_32, sx, sy, 32, 32);
		}
		
		Icon icon_units_large( int id )
		{
			
			Proto p = state.proto[id];
			/*
			Proto p = new Proto();
			p.name = "Artillerie";
			p.civilopedia_entry = "PRTO_Artillery";
			*/
			
			return ressource.icon_units_large( p, city.owner );
			
		
		}
		
		
		
		private void initialize_building_icons()
		{		
			Civ_State civ3mod = ressource.load_civ3mod();
			
			for (int i=0;i<state.building.length; i++)
			{				
		    	Building b = state.building[i];	     			
				int id = civ3mod.search_building( b.civilopedia_entry );
				if (id<0) continue;
				
				b.icon = id;			
			}
		}
		
		Icon icon_buildings_small( int id )
		{
			
			id = state.building[id].icon;
			
			////
			int x = 0;
			int y = id;			
			return new My_Icon( buildings_small, 32+32*x, 33+33*y, 32, 32 );
		}
		
		Icon icon_buildings_large( int id )
		{
			
			id = state.building[id].icon;
			
			////
			int sw = 52;
			int sh = 41;
			int sx = 32 + 0*sw;
			int sy = 32 + id*sh;									
			return new My_Icon( buildings_large, sx, sy, sw, sh );			
		}
		
		
		
		Icon icon_pop_heads( City.Citizen citizen , int epoque )
		{
			/*System.out.println("draw_head"
					+ " citizen=" + citizen
					+ " epoque=" + epoque
					+ " x=" + x
					+ " y=" + y);*/
			
			// 10x16 icones 50x50	
			// citoyens :
			// horizontalement : 2(sexe) x 5(culture_group) 
			// sexe : 0 = homme, 1 = femme
			// culture_group 
			// verticalement : 4(epoques) x 4(humeur)
			// humeur : 0 = statisfait, 1 = heureux, 2 = opposant, 3 = malheureux
			// professionels :
			// horizontalement : 2(sexe) x 4(epoque) 
			// verticalement : 3(profession)
			
			int sx,sy ;
			if (citizen.profession==0)
			{
				sx = 5*citizen.sexe + state.race[citizen.race].culture_group;
				sy = 4*epoque + citizen.humeur;
			}
			else
			{
				sx = 5*citizen.sexe + epoque;
				sy = 16 + citizen.profession - 1;
			}
			//Civ_Draw.blitSurface4( pop_heads, sx*50, sy*50, 50, 50, g2d, x, y );
			return new My_Icon( pop_heads, sx*50, sy*50, 50, 50 );		
		}
		
		
		
		int calcule_step( int w, int n ) //  w peut etre négatif
		{
			if (n==0) return 0;
			int step;
			step = (w<<8) / n ; 
			step = min( Math.abs(step), (32<<8));		
			if (w<0) step = -step;
			//step &= ~255;
			return step;
		}
		
		void dessine_rangee_icones( Graphics2D g2d, int id, int n,
				int x, int y, int step )
		{
			x <<= 8;
			for (int i=0; i<n; i++ )
			{
				int x0 = (x+128)>>8;
	    		draw_icon(g2d, id, x0, y );
	    		x += step;
			}		
			
		}
		
		/*void dessine_icones_production( Graphics2D g2d, int food, int shield, int gold,
				int cx, int cy )
		{
			int x,y;
			int step;
			int w = 128; int n = food + shield + gold ;
			if (n<=0) return;
			step = (w<<8) / n ; 
			step = min( step, 16);		
			
			
			y = cy - 16;
			x = (cx<<8) - (n*step)/2;
			
			for (int i=0; i<food; i++)
				draw_icon(g2d, 15, (x+i*step)>>8, y );
			
			for (int i=0; i<shield; i++)
				draw_icon(g2d, 13, (x+(i+food)*step)>>8, y );
			
			for (int i=0; i<gold; i++)
				draw_icon(g2d, 14, (x+(i+food+shield)*step)>>8, y );
			
		}
		*/
		
		void dessine_reserve0( Graphics2D g2d, 
				int id0, int n, int w, int h,
				int x, int y, int stepx, int stepy )
		{			
			for (int j=0; j<h; j++ )
			{
				int y0 = y + stepy*j;
				for (int i=0; i<w; i++ )
				{		
					int x0 = x + stepx*i;
		    		if (n>0) draw_icon(g2d, id0 , x0, y0 );
		    		n --;
				}		
			}
		}
		
		void dessine_reserve( Graphics2D g2d, 
					int id0, int id1, int n, int w, int h,
					int x, int y, int stepx, int stepy )
		{	
			dessine_reserve0( g2d, id0, w*h, w, h, x, y, stepx, stepy );
			dessine_reserve0( g2d, id1,   n, w, h, x, y, stepx, stepy );		
		}
		
		public static String nb_tours_toString( int x )
		{			
			return (x > 0 ? Integer.toString(x) : "--") 
				+ (x <= 1 ? " tour" : " tours");
		}
		
		void dessine_production( Graphics2D g2d )
		{
			
			///////
			
			int w;
			int step;		
			w = 540 ; 
			
			step = calcule_step( w,  (city_value.shield)) ;
			
			int y = 517;
			
			dessine_rangee_icones( g2d, 5, city_value.shield_loss, 282, y, step );
			
				
			dessine_rangee_icones( g2d, 4, city_value.shield_income, 806, y, -step );
			
			
    		
			
    		
    	
			int width = 8*14;
			int height = 10*14 ;
			w = city_value.prod_nb_rangees;
			int h = 10;
			int stepx = width / w ;
			int stepy = height / h ;
			
			stepx = min(stepx,14);
			stepy = min(stepy,14);
			
    		dessine_reserve( g2d, 
    				8, 4, city.shield, w, h, 
    				894 + (width - stepx*w)/2 , 611, stepx, stepy );
    		
    		
    		
    		
			g2d.setFont(small_font);
    		g2d.drawString((city_value.shield + " par tour"), 380, 522); 
    		g2d.drawString(("Achevé en " + nb_tours_toString(city_value.prod_nb_tours)), 917, 620); 
    		g2d.setFont(standard_font);		
    		
    	
    		
		}

		void dessine_nourriture( Graphics2D g2d )
		{
		
			
			int step;
			
			if (city_value.food_loss > 0)
			{
				step = calcule_step( 190,  city_value.food_loss) ;
				dessine_rangee_icones( g2d, 7, city_value.food_loss, 286, 564,  step );
			}
			
			if (city_value.food_income > 0)
			{
				step = calcule_step(  -200 ,  city_value.food_income ) ;	
				dessine_rangee_icones( g2d, 6, city_value.food_income, 765-32, 564, step);
			}
			

    		
    		
			g2d.setFont(small_font);
    		g2d.drawString((city_value.food_income + " par tour"), 380, 568); 
    		
    		if ( city_value.food_income < 0 )
    			g2d.drawString("Pénurie", 790, 568); 
    		else if ( city_value.food_income == 0 )	
    			g2d.drawString("Aucune croissance", 790, 568); 
    		else	
    			g2d.drawString(("Croiss. en " + nb_tours_toString(city_value.food_nb_tours)), 790, 568); 
    		
    		g2d.setFont(standard_font);
    		
    		
    		int width = 6*18;
			int height = 10*18 ;
			int w = city_value.food_nb_rangees;
			int h = 10;
			int stepx = width / w ;
			int stepy = height / h ;
			
			stepx = min(stepx,18);
			stepy = min(stepy,18);
			

    		dessine_reserve( g2d, 
    				9, 6, city.food, w, h,
    				774 + (width - stepx*w)/2, 572, stepx, stepy ); 
    		
    		
		}
		
		
		
		void dessine_commerce( Graphics2D g2d )
		{
			
			///////// impôts
			
			int step;
			step = calcule_step( -190,  city_value.tax) ;		
			dessine_rangee_icones( g2d, 2, city_value.tax, 670-32, 620, step );
			
			step = calcule_step( 150,  city_value.gold_loss) ;	
			dessine_rangee_icones( g2d, 3, city_value.gold_loss, 300, 650, step );
			
			step = calcule_step( -190,  city_value.science) ;	
			dessine_rangee_icones( g2d, 2, city_value.science, 670-32, 650, step );
			
			
			draw_icon(g2d,24,691,622);
			draw_icon(g2d,1,691,652);
			draw_icon(g2d,12,691,686);
			
			
			g2d.setFont(small_font);
			g2d.drawString((city_value.gold + " par tour"), 370, 618); 	
    		g2d.drawString((city_value.tax + " (" + (city_value.tax_rate*10)+"%)"), 720, 646); 
    		g2d.drawString((city_value.science + " (" + (city_value.science_rate*10)+"%)"), 720, 670);    		
    		g2d.drawString((city_value.luxury + " (" + (city_value.luxury_rate*10)+"%)"), 720, 704);    		
    		g2d.drawString((city_value.gold_loss + ""), 302, 697);    		
    		g2d.setFont(standard_font);
	
    		
		}
		
		class CityIcon implements Icon {
			
				private int[] data; // id
				private int step;
				
				public CityIcon( TileValue v )
				{		
					data = new int[6];
					data[0] = 15;
					data[1] = v.food;
					data[2] = 13;
					data[3] = v.shield;
					data[4] = 14;
					data[5] = v.gold;	
					
					int n = compute_length(); 		
					
					//step = 128 / n;
					step = 0;
					if (n>1) step = (128 - 32) / (n-1);
	   		 		step = min( step, 14);	 		
				}
				
				private int compute_length()
				{
					int sum = 0;
					for (int i=0;i<data.length; i+=2)
					{
						sum += data[i+1];
					}
					return sum;
				}
			
			
				public void paintIcon(Component c,
		                  Graphics g,
		                  int dx,
		                  int dy) {
					// TODO Auto-generated method stub
					//System.out.println("paintIcon");
					Graphics2D g2d = (Graphics2D)g;
					
					int x = dx ; int y = dy;
					
					for (int i=0;i<data.length; i+=2)
					{
						int id = data[i+0];
						int number = data[i+1];
						
						if (id<0) continue;
						
						for (int k=0;k<number;k++)
		   		 		{
		   		 			draw_icon(g2d, id, x, y );
		   		 			x += step;
		   		 		}
						
					}
					

				}

				
				public int getIconWidth() {
					// TODO Auto-generated method stub
					int n = compute_length();
					//return ( n * step);
					return ( (n-1) * step + 32);
				}

				public int getIconHeight() {
					// TODO Auto-generated method stub
					return 32;
				}
		}
		
		
		
		 
		 public void update_value()
		 {
		 	city_value.compute( city );
		 	
		 	repaint();
		 }
		
	     protected void paintComponent(Graphics g) {
	            
	         
	            super.paintComponent(g);          
	         
	           	Graphics2D g2d = (Graphics2D)g;
	            
	           	//System.out.println("City_Screen : paintComponent");
	           	
	            // background
	            //g2d.drawImage(background, 0, 0, null );        
	           
	            
	            // main panel : y de 94 à 506 (412 pix)	
	            /*java.awt.geom.AffineTransform transform = g2d.getTransform();
	            Shape shape = g2d.getClip();
	            
	    		g2d.translate(0,94);
	    		g2d.clipRect(0,0,1024,412);	
	    		
	    		
				dessine_limite_production(g2d);
			
				
				g2d.setTransform(transform);
				g2d.setClip(shape);
				*/
				
				
				
	            
	        	// city name	
	    		g2d.setFont(big_font);
	    		g2d.setColor(Color.BLACK);
	    		print_center_align(g2d, city.name, 512, 28);  
	    		g2d.setFont(standard_font);
	    		g2d.setColor(Color.BLACK);
	    		
	     		//CULTURE	 
	    		int y0=16;
	    		g2d.drawString("CULTURE", 713, y0); 
	    		g2d.setFont(small_font);
	    		int culture_next = max(10,next_pow10(city.culture));
	    		g2d.drawString("Total : " + city.culture 
	    				 + " / " + culture_next, 718, 68); 
	    		g2d.setFont(standard_font);
	    		g2d.setColor(new Color(222,198,82));	    		
	    		int w = (104*city.culture)/culture_next ;
	    		g2d.fillRect(720,39,w,12);
	    		g2d.setColor(Color.BLACK);
	    		
	    		//EXIT AND VIEW
	    		g2d.drawString("PRESS ESC TO CLOSE", 844, y0); 	    
	    		
	    		//x_and_view
	    		
	    		//RESSOURCES STRATEGIQUES
	    		g2d.drawString("RESSOURCES STRATEGIQUES", 16, y0); 
	    		
	    		//AMELIORATIONS
	    		int y1 = 522;
	    		g2d.drawString("AMELIORATIONS", 10, y1); 
	    		
	    		//PRODUITS DE LUXE
	    		int x0 = 160;
	    		g2d.drawString("PRODUITS DE LUXE", x0, y1); 
	    		
	    		//PRODUCTION
	    		int x1 = 292;
	    		g2d.drawString("PRODUCTION", x1, y1); 
	    		dessine_production(g2d);
	    		
	    		//NOURRITURE
	    		g2d.drawString("NOURRITURE", x1, 568); 
	    		dessine_nourriture(g2d);
	    		
	    		//COMMERCE
	    		g2d.drawString("COMMERCE", x1, 620); 
	    		dessine_commerce(g2d);
	    		
	    		//GARNISON
	    		int y2 = 726;
	    		g2d.drawString("GARNISON", x1, y2); 
	    		java.util.List liste = state.search_units(city.x,city.y);
	    		int x = 300;
	    		for (Iterator i=liste.iterator();i.hasNext();)
			  	{
		    		Unit u = (Unit)i.next();
		    		draw_unit(g2d,state.proto[u.prto].icon_index,x,730);
		    		x += 32;
		    		if (x>730) break;
			  	}
				
	    		//POLLUTION
	    		g2d.drawString("POLLUTION", x0, y2); 	
	    		   		
	    		int step;
	    		step = calcule_step( 120,  city_value.pollution) ;	
	    		dessine_rangee_icones( g2d, 11, city_value.pollution, 160, 734, step );
				
	  
	   		 	
	    		
	        }
	     
	     
	        
	     void print_center_align(Graphics2D g2d , String s, int x, int y) {
			int w = g2d.getFontMetrics().stringWidth(s);
			int h = g2d.getFontMetrics().getHeight();
			g2d.drawString(s, x - w / 2, y);
		}
	     
	     /////////////// listeners //////////////////
	     
	     
	   // action   
	     public void actionPerformed(ActionEvent e) {
	    	//System.out.println(e);
	        if ("exit".equals(e.getActionCommand())) {
	        	civ_main.afficher_ecran("main_screen");
	        } 
	        else if ("prod_button".equals(e.getActionCommand())) {
	        	production_queue.setVisible(!production_queue.isVisible());
	        
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
					civ_main.afficher_ecran("main_screen");
					break;

			
			}
		}
		
		public void modify_production_area( int tx, int ty )
		{
			int d = city.distance( tx, ty );
			System.out.println("distance=" + d);
			
			if (d > city.prod_level())
				return;
			
			// case de la ville
			if (tx==city.x && ty==city.y)
			{
				System.out.println("gouverneur du travail");
				state.city_governor(city);
				initialize_population();
				update_value();
				return;
			}
			
			
			// citoyens
			City.Citizen c  = city.find_worker_at( tx, ty);
			
			if (c==null)
			{
				
				c = city.find_citizen_not_worker();
				if (c!=null)
				{
					c.profession = City.Citizen.WORKER;
					c.x = tx - city.x;
					c.y = ty - city.y;
					initialize_population();
					update_value();
				}
				System.out.println("adding worker " + c);
			}
			else
			{
				System.out.println("removing worker " + c);
				c.profession = City.Citizen.WORKER + 1;
				c.x = 0;
				c.y = 0;
				initialize_population();
				update_value();
			}
			
		}
		
		public void civ_PanelEventOccurred(Civ_PanelEvent evt)
		{
			System.out.println("click on " + evt);
			
			switch (evt.id) {
				case Civ_PanelEvent.ID_PRESSED_LEFT :			
					modify_production_area( evt.tx, evt.ty );
					break;

				case Civ_PanelEvent.ID_PRESSED_RIGHT :						
					break;
					
				case Civ_PanelEvent.ID_DOUBLE_CLICK :
					break;
			}
		}
		
		/**********************************/
		
		class BuildingListRenderer extends JLabel
			implements ListCellRenderer {
	
		
			
			
			public BuildingListRenderer() {
			
				
				setOpaque(false);
				setHorizontalAlignment(LEFT);
				setVerticalAlignment(CENTER);
				
			
			}
	
			/*
			* This method finds the image and text corresponding
			* to the selected value and returns the label, set up
			* to display the text and image.
			*/
			public Component getListCellRendererComponent(
			                JList list,
			                Object value,
			                int index,
			                boolean isSelected,
			                boolean cellHasFocus) {
			//Get the selected index. (The index param isn't
			//always valid, so just use the value.)
			int id = ((Integer)value).intValue();
			
			if (isSelected) {
				setOpaque(true);
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setOpaque(false);
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}
			//System.out.println("painting building cell " + id);
			//Set the icon and text.  
						
			Icon icon = icon_buildings_small( id );
			String text = state.building[id].name;
			
			setIcon(icon);	
			setText(text);
			setFont(list.getFont());

			return this;
			}
	
		}
		
		/**********************************/
		
		class ProductionListRenderer extends JLabel
			implements ListCellRenderer {

	
	
		public ProductionListRenderer() {
		
			
			setOpaque(false);
			setHorizontalAlignment(LEFT);
			setVerticalAlignment(CENTER);

		
		}

		/*
		* This method finds the image and text corresponding
		* to the selected value and returns the label, set up
		* to display the text and image.
		*/
		public Component getListCellRendererComponent(
		                JList list,
		                Object value,
		                int index,
		                boolean isSelected,
		                boolean cellHasFocus) {
		//Get the selected index. (The index param isn't
		//always valid, so just use the value.)
		City.Build build = (City.Build)value;
		
		
		if (isSelected) {
			setOpaque(true);
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setOpaque(false);
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}
		//System.out.println("painting building cell " + id);
		//Set the icon and text.  
		
		Icon icon = null;
		String text = null;
		
		int cost =  state.compute_cost_city_build( build );
		
		int nb_tours =  city_value.compute_prod_nb_tours( cost );   
		
		switch( build.type )
		{
			
			case City.Build.TYPE_UNIT:	
				icon = icon_units_32( build.id );
				Proto p = state.proto[build.id];
				text = p.name
					+ " " + p.attack 
					+ (p.bombard_strength > 0 ? ("(" + p.bombard_strength + ")"): "" )
					+"." + p.defense 
					+ "." + p.movement
					+ " " + nb_tours_toString(nb_tours);
				break;
				
			case City.Build.TYPE_BUILDING:		
				Building b = state.building[build.id];
				icon = icon_buildings_small( build.id );				
				text = b.name
					+ " " + nb_tours_toString(nb_tours);
				break;
		
		}
		
		
		setIcon(icon);	
		setText(text);
		setFont(list.getFont());
		

		return this;
		}

	}
		
		/////////// Civ_Screen
		
		 
		    
		    public void swing_worker_construct()
		    {
		    	// SwingWorker.construct()
		    	
		    	super.swing_worker_construct();
		    	initialize_all();
		    }
		    
		   
		    
		   
		    
		    public void flush(){ // BufferedImage.flush()
		    	super.flush();
		    	
    	
		    	city_icons.flush();
				units_32.flush();
				pop_heads.flush();
				img_prod_button.flush();
				buildings_large.flush();
				buildings_small.flush();
				production_queue_box.flush(); 
				x_and_view.flush();
				img_draft_button.flush(); 
				img_hurry_button.flush();
	
		    }
		    
		 
		    
	/**********************************/
	
		
	
		
		
		
		public static void main(String[] args) throws IOException {
			
			
			
			String filename = "demo.biq";
			
			if (args.length>=1){
				filename = args[0];
			}
			
			Civ_Main civ_main = new Civ_Main();				
			civ_main.fast_init( filename );

			City city = (City)civ_main.state.city.get(0);
			System.out.println(city);
			
////////////debug /////////////////
            
			city.size = 2;
            city.food = 6*(2 + 5);
			city.shield = 5*8 + 6;
			city.build.type = City.Build.TYPE_BUILDING;
			city.build.id = 0;		
			//for (int i=0;i<10;i++) city.buildings.add( new Integer(i) ); 
			
			civ_main.state.check_city(city);
			
   			
////////////debug /////////////////
			
			
			

			City_Screen city_screen = new City_Screen(  city, 
					civ_main,
					civ_main.config.wscreen, civ_main.config.hscreen ); 
			
			
			
			civ_main.frame.afficher_ecran( city_screen );
			
			System.out.println("bye bye");
			
			
		}
		
		
	    
}
















