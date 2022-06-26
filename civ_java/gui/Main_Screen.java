
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
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import javax.swing.*;

import ressource.*;
import ruler.*;
import state.*;


/**
 * @author roudoudou
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */



public class Main_Screen extends Civ_Panel
		implements	
			Civ_PanelEventListener,	
			ActionListener,
			KeyListener  
		
{
	
	Civ_Main civ_main;
	//protected Civ_Ressource ressource;
	//protected Civ_State state;
	
	
	//BufferedImage minimap = null;
	
	private Font small_font, standard_font;
		
	
	
	/* Creates a new instance of Civ_Draw */
	public Main_Screen( Civ_Main civ_main
			,int wscreen, int hscreen ) {
	
		super( civ_main.state,civ_main.ressource, 
				wscreen,hscreen, civ_main.my_id,
				true); 

		this.civ_main = civ_main;
	

		addKeyListener(this);
		
		addCiv_PanelEventListener(this);

		//Uncomment this if you wish to turn off focus
		//traversal. The focus subsystem consumes
		//focus traversal keys, such as Tab and Shift Tab.
		//If you uncomment the following line of code, this
		//disables focus traversal and the Tab events
		//become available to the key event listener.
		//setFocusTraversalKeysEnabled(false);

		/*
		 * # Make sure the component's isFocusable method returns true. This
		 * allows the component to receive the focus. For example, you can
		 * enable keyboard focus for a JLabel by calling setFocusable(true) on
		 * the label. # Make sure the component requests the focus when
		 * appropriate. For custom components, you'll probably need to implement
		 * a mouse listener that calls the requestFocusInWindow method when the
		 * component is clicked.
		 */
		//this.setFocusable(true);
	}
	
	/** Handle the key typed event from the text field. */
	public void keyTyped(KeyEvent e) {
		//displayInfo(e, "KEY TYPED: ");

	}
	/** Handle the key pressed event from the text field. */
	public void keyPressed(KeyEvent e) {
		//displayInfo(e, "KEY PRESSED: ");
		switch (e.getKeyCode()) {
			case KeyEvent.VK_ESCAPE :
				//System.exit(0);
				quitter();
				break;

			case KeyEvent.VK_W : // wait
				switch_to_next_unit();
				break;
				
			case KeyEvent.VK_F12 : // console
				civ_main.afficher_ecran("console");
				break;
				
			case KeyEvent.VK_NUMPAD1 :	current_unit_move(6);	break;
			case KeyEvent.VK_DOWN 	:
			case KeyEvent.VK_NUMPAD2 :	current_unit_move(5);	break;
			case KeyEvent.VK_NUMPAD3 :	current_unit_move(4);	break;
			case KeyEvent.VK_LEFT 	:
			case KeyEvent.VK_NUMPAD4 :	current_unit_move(7);	break;
			case KeyEvent.VK_RIGHT 	:
			case KeyEvent.VK_NUMPAD6 :	current_unit_move(3);	break;
			case KeyEvent.VK_NUMPAD7 :	current_unit_move(8);	break;
			case KeyEvent.VK_UP 	:
			case KeyEvent.VK_NUMPAD8 :	current_unit_move(1);	break;
			case KeyEvent.VK_NUMPAD9 :	current_unit_move(2);	break;
			
		
			
			
				
			case KeyEvent.VK_R : current_unit_build_road(); break;
			case KeyEvent.VK_M : current_unit_build_mine(); break;
			case KeyEvent.VK_I : current_unit_build_irrigation(); break;
			case KeyEvent.VK_B : current_unit_build_city(); break;
			
			case KeyEvent.VK_SPACE :
				if (current_unit==-1)
				{
					end_of_turn();
				}
				else
				{
					current_unit_wait();
				}
				break;	
				
			case KeyEvent.VK_ENTER : 
				if (current_unit==-1)
				{
					end_of_turn();
				}
				break;	
				
		}
	}
	
	
	
	

	/** Handle the key released event from the text field. */
	public void keyReleased(KeyEvent e) {
		//displayInfo(e, "KEY RELEASED: ");
	}

	void displayInfo(KeyEvent e, String s) {

		String keyString;

		//You should only rely on the key char if the event
		//is a key typed event.
		int id = e.getID();
		if (id == KeyEvent.KEY_TYPED) {
			char c = e.getKeyChar();
			keyString = "key character = '" + c + "'";
		} else {
			int keyCode = e.getKeyCode();
			keyString = "key code = " + keyCode + " ("
					+ KeyEvent.getKeyText(keyCode) + ")";
		}
		//Display information about the KeyEvent...
		System.out.println(s + keyString);

	}
	
	//// Game function ------------------------------
	
	public boolean check_current_unit()
	{
		if (current_unit < 0) 
			return false;
		
		Unit u = state.find_unit_by_id(current_unit) ;
	  	if (u == null)
	  	{	current_unit=-1;return false; }
	  	
	  	return true;
	}
	
	public void simple_redraw_all()
	{
		Unit u = state.find_unit_by_id(current_unit) ;
	  	if (u != null)
	  	{
	  		if (state.compute_movement_available(u)<=0)
	  		{
	  			switch_to_next_unit();
	  		}
	  	}
	  	else
	  	{
	  		switch_to_next_unit();
	  	}
		
	  		
		super.simple_redraw_all();
		
		if (panel_right!=null) 
			panel_right.update();
	}
	
	public void current_unit_build_road( )
	{
	
		if (check_current_unit()==false) 
			return;
			
		civ_main.ruler.process(
				new Civ_CommandUnitAction(current_unit, Civ_CommandUnitAction.BUILD_ROAD ));
		
		simple_redraw_all();
		
	}
	
	public void current_unit_build_mine( )
	{
		if (check_current_unit()==false) 
			return;
		
		civ_main.ruler.process(
				new Civ_CommandUnitAction(current_unit, Civ_CommandUnitAction.BUILD_MINE ));
		
		simple_redraw_all();
		
	}
	
	public void current_unit_build_irrigation( )
	{
		if (check_current_unit()==false) 
			return;
			
		civ_main.ruler.process(
				new Civ_CommandUnitAction(current_unit, Civ_CommandUnitAction.BUILD_IRRIGATION ));
		
		simple_redraw_all();
		
	}
	
	public void current_unit_build_city( )
	{
		if (check_current_unit()==false) 
			return;
		
		civ_main.ruler.process(
				new Civ_CommandUnitAction(current_unit, Civ_CommandUnitAction.BUILD_CITY ));
		
		simple_redraw_all();
		
	}
	
	
	public void current_unit_move( int dir )
	{
		if (check_current_unit()==false) 
			return;
			
		civ_main.ruler.process(
				new Civ_CommandMoveUnit(current_unit, dir));
		
		Unit u = state.find_unit_by_id(current_unit);
		set_view_if_needed( u.x*64, u.y*32 );
		
		simple_redraw_all();
		
	}

	public void current_unit_wait(  )
	{
		if (check_current_unit()==false) 
			return;
			
		civ_main.ruler.process(
				new Civ_CommandMoveUnit(current_unit, 0));
				
		simple_redraw_all();
		
	}
	
	void switch_to_next_unit() {

		System.out.println("switch_to_next_unit");

		current_unit = state.find_next_unit( my_id, current_unit );
		System.out.println("current_unit=" + current_unit);
		
		// switch
		switch_to_unit(state.find_unit_by_id(current_unit));
		
	}

	public void switch_to_unit(Unit u) {
		if (u != null)
		{
			System.out.println("switching to unit : " + u);
			set_view_if_needed((u.x) * 64, (u.y) * 32);
		}
		
		if (panel_right!=null) panel_right.update();
	}
	
	public void end_of_turn()
	{
		civ_main.ruler.process(
			new Civ_CommandEndOfTurn(my_id));
		
		simple_redraw_all();
	}
	
	 class Menu_Quitter 
	 	implements ActionListener {
	 	
	 	 private JDialog dialog;
	 	 private My_Popup popup;
	 	 private My_Menu menu;
		 
		 public Menu_Quitter()
		 {
		 	popup = new My_Popup(civ_main.ressource);
		 	popup.swing_worker_construct();	 	
		 	//popup.setLocation( 0, 0);
			//popup.setSize( 367, 191 );
		 	popup.basic_init( "Oh non!", this );	
		 	
		 	JLabel label = new JLabel("Voulez-vous vraiment quitter la partie?");
		 	//label.setHorizontalAlignment(JLabel.LEFT);
		 	label.setFont(  ressource.font.deriveFont(Font.PLAIN, 15)	);	 	
		 	popup.add(label, BorderLayout.PAGE_START);
		 	 	
		 	menu = new My_Menu( civ_main.ressource ) ;		
			menu.swing_worker_construct();					
			menu.addActionListener(this);	
			menu.add(Box.createRigidArea(new Dimension(0,15)));
			menu.add("Non, pas vraiment.");
			menu.add("Oui, sur-le-champ !");
			popup.add(menu, BorderLayout.CENTER);
			
			dialog = new JDialog(civ_main.frame, true); 
	 		//dialog.setLocation( 328, 102);
	 		//dialog.setSize( 367, 191 );	 		
	 		dialog.setUndecorated(true);
	 		dialog.setResizable(false);	 		
	 		dialog.setContentPane(popup);
	 		dialog.pack();
	 		dialog.setLocationRelativeTo(civ_main.frame);//centers on screen
	 		 
		 }
		 
	 	public void actionPerformed(ActionEvent e) {
	 	   	   
	 	   		String txt = e.getActionCommand();
	 	   		System.out.println("menu_quitter : action_performed : " + txt);
	 	   	    
	 	   		if ("button_o".equals(txt))
	 	   		{
	 	   			menu.validate_choice();
	 	   			setVisible(false);	
	 	   		}
	 	    	else if ("button_x".equals(txt))
	 		  	{			 	    	
	 	    		setVisible(false);
	 		  	}
	 	    	else if ("Oui, sur-le-champ !".equals(txt))
			  	{
		    		civ_main.afficher_ecran("exit");
			  	}
		    	else if ("Non, pas vraiment.".equals(txt))
			  	{
		    		setVisible(false);
			  	}
		    	
		    	
	    	
	 	   } 
	 	
	 	
	 	public void setVisible(boolean b)
	 	{
	 		enable_mousemotion = !b;
	 		dialog.setVisible(b);	 		
	 	}
	 	 
	 }
	 
	 
	public void quitter()
	{
		(new Menu_Quitter()).setVisible(true);
		 	
	}
	
	
	/*
	 * private void maybeShowPopup(MouseEvent e) { if (e.isPopupTrigger()) {
	 * popup.show(e.getComponent(), e.getX(), e.getY()); } }
	 */

	public void display_popup_at( Civ_PanelEvent evt ) {
		
		City c = state.search_city(evt.tx, evt.ty);
		java.util.List units = state.search_units(evt.tx, evt.ty);

		JPopupMenu menu = new JPopupMenu("Infos terrain");// declare and create
		// new menu

		JMenuItem menu_item = new JMenuItem("exit now !"); // create new menu item
		menu_item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		menu.add(menu_item); // add the menu item to the menu

		
		menu_item = new JMenuItem("end of turn"); // create new menu item
		menu_item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				end_of_turn();
				
			}
		});
		menu.add(menu_item); // add the menu item to the menu
		
		
		menu.addSeparator();

		if (c != null) {
			JMenu submenu = new JMenu(c.name);
			submenu.add("Info");
			submenu.add("Build");
			menu.add(submenu);
		}

		JMenu submenu_proto = new JMenu(
				"Voir l'article de la Civilopédia pour ");

		for (Iterator i = units.iterator(); i.hasNext();) {
			Unit u = (Unit) i.next();
			Proto p = state.proto[u.prto];

			String s = (p.name) + " (" + p.attack + "." + (p.defense) + "."
					+ Math.max(0, ((state.compute_movement_available(u) + 1) / 3))
					+ "/" + (p.movement) + ")";

			//in if (is_my_unit state u)

			JMenu submenu = new JMenu(s);
			//submenu.add("Activer");			
			final Unit u0 = u;
			menu_item = new JMenuItem("Activer"); // create new menu item
			menu_item.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					switch_to_unit(u0) ;
				}
			});
			submenu.add(menu_item);
			
			submenu.add("Contact");
			menu.add(submenu);

			submenu_proto.add(p.name);
		}

		if (submenu_proto.getSubElements().length > 0)
			menu.add(submenu_proto);

		menu.show(this, evt.mx, evt.my);

	}
	
	////////////
	
	
	public void civ_PanelEventOccurred(Civ_PanelEvent evt)
	{
		switch (evt.id) {
			case Civ_PanelEvent.ID_PRESSED_LEFT :			
				on_mouse_left_button(evt);			
				break;

			case Civ_PanelEvent.ID_PRESSED_RIGHT :
				display_popup_at(evt);			
				break;
				
			case Civ_PanelEvent.ID_DOUBLE_CLICK :
				on_double_click(evt);			
				break;
		}
	}
	
	//	 click bouton gauche
	void on_mouse_left_button( Civ_PanelEvent evt ) {
		/*
		 * with_explored_mouse ref mx my action = with_select_tile ref mx my $
		 * \(tx,ty) -> when_explored ref (tx,ty) $ action ref (tx,ty)
		 */
		// = with_explored_mouse ref mx my $ \ref (tx,ty) -> do
		
		java.util.List units = state.search_units(evt.tx, evt.ty);
		if (units.isEmpty())
			return;

		Unit u1 = (Unit) units.get(0);

		switch_to_unit(u1);
	}
	
	void on_double_click(  Civ_PanelEvent evt ){
		
		City c  = state.search_city(evt.tx, evt.ty);
		if (c==null)
			return;
			
		System.out.println("double-click on city " + c.name);
		
		City_Screen city_screen = new City_Screen(  c, 
				civ_main,
				wscreen, hscreen ); 
		
		
		civ_main.frame.afficher_ecran( city_screen );
	}

	public String info_left;
	public String info_middle;
	public String info_right;
	
	void display_info_bar(){
		//		 info bar	
		int wscreen = getWidth();
		int hscreen = getHeight();
		int b = 16;
		int size = 48; //64;
		int y = hscreen-size;
		int w,h;
		
		//		 couleurs civ 3		
		/*Color creme = new Color( 255, 247, 222 );
		Color orange = new Color( 255, 189, 107 );
		Color gris = new Color( 173, 173, 148 );
		Color noir = new Color( 0, 0, 0 );
		*/
		
		g2d.setFont(ressource.font);
		g2d.setBackground(Color.black);
		g2d.clearRect( 0, y, wscreen, size);	
		
		
		FontMetrics fm = g2d.getFontMetrics();		
		h = fm.getHeight();
		// center vertically
		y = y + size/2 ; //- h/2;
		
		g2d.setColor(Color.white);
		g2d.drawString(info_left, b, y );
		
		w = fm.stringWidth(info_middle);
		g2d.drawString(info_middle, (wscreen-w)/2, y );
		
		w = fm.stringWidth(info_right);
		g2d.drawString(info_right, wscreen-b-w, y );
		
	}
	
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);
		
		boolean display_fps = true;
		if (display_fps) {
			//System.out.printf("fps = %d\n" , fps );
			this.g2d = (Graphics2D) g;
			g2d.setFont(ressource.font);
			java.text.DecimalFormat df = new java.text.DecimalFormat("00");
			//print_boxed( ("fps = " + df.format(fps)), 0, 0 );
			String s = ("cpu_usage = " + cpu_usage + " %");
			info_left = "press F1 for help";
			info_middle = s;
			info_right = "-";
			//print_boxed(s, 0, 0);
		}
		
	
		
	
		
		// info bar	
		//display_info_bar();

		/*if (minimap==null){		
			minimap = Civ_MiniMap.render( state );
		}
		g.setClip(null);
		g.drawImage(minimap, 0, 0, null);
		*/
		
		//g2d.drawImage(menu_buttons, null, 100, 400);
		//g2d.drawImage(box_right, null, wscreen-box_right.getWidth() , hscreen-box_right.getHeight());
		//g2d.drawImage(box_left, null, 0 , hscreen-box_left.getHeight());
		
	}
	
	
	
	///////////
	
	 public void actionPerformed(ActionEvent e) {
	   	   
	   		String txt = e.getActionCommand();
	   		System.out.println("action_performed : " + txt);
	   	
	    	
	    	
	   } 
	 
	 
	 private Menu_Principal menu_principal =  null;
	 
	 class Menu_Principal 
	 	implements ActionListener {
	 	
	 	 private JDialog dialog;
	 	 private My_Popup popup;
	 	 private My_Menu menu;
		 
		 public Menu_Principal()
		 {
		 	popup = new My_Popup(civ_main.ressource);
		 	popup.swing_worker_construct();
		 	//this.setBounds( 328, 102, 367, 302 );
		 	popup.setLocation( 0, 0);
		 	//popup.setSize( 367, 302 );
		 	popup.basic_init( "Menu principal", this );	
		 	
		 	
		 	menu = new My_Menu( civ_main.ressource ) ;		
			menu.swing_worker_construct();					
			menu.addActionListener(this);	
			
			
								
			menu.add(		"Carte", true );
			menu.add(		"Charger partie (Ctrl-L)", false );
			menu.add(		"Nouvelle partie (Ctrl-Maj-Q)",  false );
			menu.add(		"Préférences (Ctrl-P)",  false );
			menu.add(		"Abdiquer (Ctrl-Q)",  false );
			menu.add(		"Sauvegarder partie (Ctrl-S)",  false );
			menu.add(		"Quitter partie (Echap)",  true );
						
			popup.add(menu, BorderLayout.CENTER);
			
			dialog = new JDialog(civ_main.frame, true); 
	 		//dialog.setLocation( 328, 102);
	 		//dialog.setSize( 367, 302 );	 		
	 		dialog.setUndecorated(true);
	 		dialog.setResizable(false);	 		
	 		dialog.setContentPane(popup);
	 		dialog.pack();
	 		dialog.setLocationRelativeTo(civ_main.frame);
			
		 }
		 
	 	public void actionPerformed(ActionEvent e) {
	 	   	   
	 	   		String txt = e.getActionCommand();
	 	   		System.out.println("menu_principal : action_performed : " + txt);
	 	   	    
	 	   		if ("button_o".equals(txt))
	 	   		{
	 	   			menu.validate_choice();
	 	   			setVisible(false);	
	 	   		}
	 	    	else if ("button_x".equals(txt))
	 		  	{			 	    	
	 	    		setVisible(false);
	 		  	}
	 	    	else if ("Quitter partie (Echap)".equals(txt))
			  	{
	 	    		setVisible(false);
		    		quitter();
			  	}
		    	else if ("Carte".equals(txt))
			  	{
		    		setVisible(false);
			  	}
		    	
		    	
	    	
	 	   } 
	 	
	 	
	 	public void setVisible(boolean b)
	 	{
	 		enable_mousemotion = !b;
	 		dialog.setVisible(b);	 		
	 	}
	 	 
	 }
	 
	 
	
	 private Menu_Buttons menu_buttons =  null;
	 
	  class Menu_Buttons extends Civ_Screen
	  		implements ActionListener {
	  	
	  	private BufferedImage menu_buttons = null;
	  	
	  	
	  	public Menu_Buttons(  )
	  	{
	  		super();
	  		this.setSize(200,100);
	  		this.setOpaque(false);
	  	}
	    
	  	public void load_menu_buttons()
	    {
	  		menu_buttons =  My_Toolkit.load_and_convert_alpha(
      				ressource.civ3("art\\interface\\menuButtons.pcx"));
    	
	  		BufferedImage menu_buttons_alpha = My_Toolkit.load_and_convert(
  				ressource.civ3("art\\interface\\menuButtonsAlpha.pcx"));
    	  	
	  		menu_buttons = My_Toolkit.convert_to_direct( menu_buttons );
    	
	  		int w = menu_buttons_alpha.getWidth();
	  		int h = menu_buttons_alpha.getHeight();
	  		
	  		
			int[] src_data = ((DataBufferInt)(menu_buttons.getRaster().getDataBuffer())).getData();  
	             
	        byte[] alpha_data = ((DataBufferByte)(menu_buttons_alpha.getRaster().getDataBuffer())).getData();
	               				              
	        int len = w*h;
	        int offset = 60 * w;
	        	       
	  		My_Toolkit.reset_alpha( src_data, 0 );
	        
	  		My_Toolkit.merge_alpha( src_data, alpha_data, 0*offset, len );
	        
	  		My_Toolkit.merge_alpha( src_data, alpha_data, 1*offset, len );
	        
	  		My_Toolkit.merge_alpha( src_data, alpha_data, 2*offset, len );
	        
	  		menu_buttons = My_Toolkit.convert_to_compatible( menu_buttons );
	    	
	    }
	  	
	  	 public void actionPerformed(ActionEvent e) {
		   	   
		   		String txt = e.getActionCommand();
		   		System.out.println("menu_buttons : action_performed : " + txt);
		   	    
		   		if ("menu principal".equals(txt))
		   		{	   			
		   			if (menu_principal==null)
			 			menu_principal = new Menu_Principal();

			 		menu_principal.setVisible(true);
		   		}
		   		else if ("conseillers".equals(txt))
		   		{	   			
		   			civ_main.afficher_ecran( "domestic_screen" );
		   		}
		    	
		   } 
	  	 
	  	 /////////// Civ_Screen
	 	
	  	 
	  	 public void swing_worker_construct()
		    {
	  		// l'image
	  		load_menu_buttons();
	  		
	  		//les trois boutons
			My_Button bouton_menu = new My_Button( menu_buttons, 0, 0, 35, 30, 0, 60);
			bouton_menu.setLocation(21,12);
			bouton_menu.setToolTipText("menu principal");
			bouton_menu.setActionCommand("menu principal");
			bouton_menu.addActionListener(this);
			bouton_menu.setFocusable(false);    
			this.add(bouton_menu);
			
			My_Button bouton_civilopedia = new My_Button( menu_buttons, 37, 0, 35, 30, 0, 60 );
			bouton_civilopedia.setLocation(57,12);
			bouton_civilopedia.setToolTipText("Civilopédia");
			bouton_civilopedia.setActionCommand("Civilopédia");
			bouton_civilopedia.addActionListener(this);
			bouton_civilopedia.setFocusable(false);  
			bouton_civilopedia.setEnabled(false);
			this.add(bouton_civilopedia);
			
			My_Button bouton_conseiller = new My_Button( menu_buttons, 74, 0, 35, 30, 0, 60 );
			bouton_conseiller.setLocation(94,12);
			bouton_conseiller.setToolTipText("conseillers");
			bouton_conseiller.setActionCommand("conseillers");
			bouton_conseiller.addActionListener(this);
			bouton_conseiller.setFocusable(false);  
			this.add(bouton_conseiller);
		    }
		    
	  	 public void flush(){ // BufferedImage.flush()
	    	
	    	menu_buttons.flush();
	  	 }
	    
	  }
	  	
		private Panel_Right panel_right;
		
	    class Panel_Right extends My_Panel implements ActionListener {
	    
	    	
		    private JLabel icon_unit , 
							proto_name,
							unit_numbers,
							city_name,
							info1, info2, info3;
		    
			public Panel_Right()
		  	{
				super();
				
		  	}
	    
	    
		
	    /////////// Civ_Screen
			
			private int count1 = 0, count2 = 0;
			
			
			public void timer_run(){
		    	// javax.swing.Timer
		        	
					count1 ++;
					if (count1<10)
						return;
					count1=0;
					
					count2 ++;
					if (current_unit==-1)
					{
						proto_name.setVisible((count2&1)==0);
					}
		    }

	  	
	    public void swing_worker_construct()
	    {
	    	BufferedImage box_right = My_Toolkit.load_2pcx_with_alpha( 
	  				ressource.civ3("art\\interface\\box right color.pcx"),
	  				ressource.civ3("art\\interface\\box right alpha.pcx") );
	    	
	    	   	
	    	init(box_right);
	    	
	    	
	    	////
	    	
	    	BufferedImage nextturn_states = My_Toolkit.load_2pcx_with_alpha( 
	  				ressource.civ3("art\\interface\\nextturn states color.pcx"),
	  				ressource.civ3("art\\interface\\nextturn states alpha.pcx") );
	    	
	    	My_Button button_nextturn = new My_Button(nextturn_states, 0, 0, 47, 28 );
	    	button_nextturn.setLocation(0,0);
	    	button_nextturn.setActionCommand("button_nextturn");
	    	button_nextturn.addActionListener(this);	    	
	    	this.add(button_nextturn);
	    	
	    	
	    	
	    	
	    	///////
	    	
	    	JPanel pane1 = new JPanel();
	    	pane1.setLayout(new GridLayout(3,1));
	    	pane1.setBounds(42, 22, 223, 40);
	    	pane1.setOpaque(false);
	    	this.add(pane1);
			
			
			
			proto_name = new JLabel();
			proto_name.setHorizontalAlignment(JLabel.RIGHT);
			proto_name.setFont(standard_font);
			pane1.add(proto_name);
			

			unit_numbers = new JLabel();
			unit_numbers.setHorizontalAlignment(JLabel.RIGHT);
			unit_numbers.setFont(standard_font);
			pane1.add(unit_numbers);
			
			city_name = new JLabel();
			city_name.setHorizontalAlignment(JLabel.RIGHT);
			city_name.setFont(standard_font);
			pane1.add(city_name);
			
		   	///////
	    	
	    	JPanel pane2 = new JPanel();
	    	pane2.setLayout(new GridLayout(3,1));
	    	pane2.setBounds( 42, 82, 223, 40);
	    	pane2.setOpaque(false);
	    	this.add(pane2);
			
			
			
			info1 = new JLabel();
			info1.setHorizontalAlignment(JLabel.CENTER);
			info1.setFont(standard_font);
			pane2.add(info1);
			
			info2 = new JLabel();
			info2.setHorizontalAlignment(JLabel.CENTER);
			info2.setFont(standard_font);
			pane2.add(info2);
			
			info3 = new JLabel();
			info3.setHorizontalAlignment(JLabel.CENTER);
			info3.setFont(standard_font);
			pane2.add(info3);
			
			///////
	    	icon_unit = new JLabel();	
			icon_unit.setHorizontalAlignment(JLabel.CENTER);
			icon_unit.setVerticalAlignment(JLabel.CENTER);
			icon_unit.setSize(240,240);    	
			icon_unit.setLocation(10,-50);	
			this.add(icon_unit);
			
	    }
	    
	    
	    public void update()
	    {
	    	
	    	Leader l = state.leader[my_id - 1];
			Race r = state.race[l.civ];
			Government g = state.government[l.government];
      		
			//info1.setText("Germany - Democracy (4.6.0)");
			info1.setText(r.civilization_name + " - " + g.name 
					+ " ("+l.tax_rate+"."+l.science_rate+"."+l.luxury_rate()+")");
			
			//info2.setText("1260 ap. J.C. 703 0r (+60 par tour)");
			info2.setText(
					state.game.compute_time(state.turn) + " " 
					+ " " + l.gold + " 0r"
					+ " (+60 par tour)");
			
			
			info3.setText("Industrialization (5 tours)");
			
	    	/////////////////
	    	Unit u = state.find_unit_by_id(current_unit);
	    	if (u==null)
	    	{
	    		icon_unit.setIcon( null );
	    		//proto_name.setText( "Veuillez patienter ..." );
	    		proto_name.setText( "Tour suivant : ENTREE ou ESPACE" );
	    		unit_numbers.setText( null );
	    		city_name.setText( null );
	    		return;
	    	}
	    	
	    	
	    	Proto p = state.proto[u.prto];
			
			Icon icone =  ressource.icon_units_large( p, my_id );
			
			icon_unit.setIcon( icone );
			
			
			
			proto_name.setText( p.name );
			
			
			String text =  p.attack 
				+ (p.bombard_strength > 0 ? ("(" + p.bombard_strength + ")"): "" )
				+"." + p.defense 
				+ "." + ((state.compute_movement_available(u)+2)/3) 
				+ "/" + p.movement;
			
			
			unit_numbers.setText( text );
			
			City c = state.search_city(u.x,u.y);
			if (c==null)
				city_name.setText("");
			else
				city_name.setText(c.name);
			
			
			
			
	    }
	    
	    
	    public void actionPerformed(ActionEvent e) {
		   	   
		   		String txt = e.getActionCommand();
		   		System.out.println("panel_right : action_performed : " + txt);
		   	    
		   		if ("button_nextturn".equals(txt))
		   		{	   			
		   			
			 		end_of_turn();
					
		   		}
		    	
		    	
		   } 
	    
	    }
 
		private Panel_Left panel_left;
		
	    class Panel_Left extends My_Panel {
	    
	    	
		    
			public Panel_Left()
		  	{
				super();
				
		  	}
	    
	    
		
	    /////////// Civ_Screen

	  	
	    public void swing_worker_construct()
	    {

			
	    	BufferedImage box_left = My_Toolkit.load_2pcx_with_alpha( 
				ressource.civ3("art\\interface\\box left color.pcx"),
				ressource.civ3("art\\interface\\box left alpha.pcx") ); 									
	
	
	    	init(box_left);

		
			
	    }
	    
	
	    }
 
  
	/////////// Civ_Screen

	  	
	    public void swing_worker_construct()
	    {
	    	
	    	
	    	small_font = ressource.font.deriveFont(Font.PLAIN, 10);            
            standard_font = ressource.font.deriveFont(Font.PLAIN, 12);
            
	    	 
	    	
	    	//les trois boutons
            menu_buttons = new Menu_Buttons();
            menu_buttons.swing_worker_construct();
            menu_buttons.setLocation(0,0);
            this.add(menu_buttons);
		
			
			//// box_left
			
			
            panel_left = new Panel_Left();
            panel_left.swing_worker_construct();
            panel_left.setLocation(0 , hscreen-panel_left.getHeight());
            panel_left.setVisible(false);
            this.add(panel_left);
            
			//// box_right
			
			panel_right = new Panel_Right();
			panel_right.swing_worker_construct();
			panel_right.setLocation(wscreen-panel_right.getWidth() , hscreen-panel_right.getHeight());
			this.add(panel_right);
			
			///
			switch_to_next_unit();

	    }
	    
	    
		
	
	    public void timer_run(){
	    	// javax.swing.Timer
	        	
	    	super.timer_run();
	    	
	    	panel_right.timer_run();
	    }
	    
	    public void flush(){ // BufferedImage.flush()
	    	
	    	menu_buttons.flush();
	    	panel_left.flush();
	    	panel_right.flush();
	    }
	    
		/**********************************/
		
			
		
			
			
			
		public static void main(String[] args) throws IOException {
			
	
			String filename = "demo.biq";
					
			if (args.length>=1){
				filename = args[0];
			}
			
			Civ_Main civ_main = new Civ_Main();
			civ_main.fast_init( filename );

			Main_Screen main_screen = new Main_Screen(  civ_main,
					civ_main.config.wscreen, civ_main.config.hscreen ); 
			
			main_screen.center_view();
			
			civ_main.main_screen = main_screen;
			civ_main.afficher_ecran( "main_screen" );
			
			System.out.println("bye bye");
			
			
		}
			
			
		    
}
