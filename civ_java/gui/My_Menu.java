/*
 * Created on 13 févr. 2005
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

import ressource.Civ_Ressource;

import java.util.Enumeration;

/**
 * @author roudoudou
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class My_Menu extends Civ_Screen
		 {
	
	
	private Civ_Ressource ressource = null;
	
	private ButtonGroup group ;
	private Icon icon0, icon1, icon2;
	private Font font;
	//private int count_option = 0;
	
	//private int selected_index = -1;
	
	
	public My_Menu() 
	{
		super();	
		setOpaque(false);
	}
	
	public My_Menu( Civ_Ressource ressource ) {
		
			super(); 			
			setOpaque(false);		
			this.ressource = ressource;
	
	}
	     
	public void initialize( 
				My_Button buttons, //BufferedImage buttons,
				Font font
				) 
		{
	     
			
			this.icon0 = buttons.getIcon();  
			this.icon1 = buttons.getRolloverIcon(); 
			this.icon2 = buttons.getPressedIcon(); 
			this.font = font;
			
		  	// Group the radio buttons.
			this.group = new ButtonGroup();	    
		  
			// Put the radio buttons in a column in a panel.
	        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		 	
		}
	
	
	public void add( String[] options )
	{		
		for (int i=0;i<options.length;i++)
			add( options[i] );
		
	}
	
	
	public void add( String options )	
	{
		add( options, true );
	}
	
	public void add( String options, boolean enabled )
	{
		  
		  	int i = group.getButtonCount();	
		  		  	
		  	
		  	JRadioButton my_button = new JRadioButton(options);
		  	//my_button.setMnemonic(KeyEvent.VK_B);
		  	//my_button.setActionCommand(options[i]);
		  	if (i==0) my_button.setSelected(true);
		  	
		  	int w = 200;
		  	int h = 30; //20;
		  	
		  	//my_button.setSize(180, 32);	
		  	//my_button.setSize(w, h);	
		  	//my_button.setPreferredSize(new Dimension(w, h));	
			my_button.setFocusPainted(false);
			my_button.setBorderPainted(false);
			my_button.setRolloverEnabled(true);
			my_button.setContentAreaFilled(false);
			my_button.setIcon(icon0);  
			my_button.setRolloverIcon(icon1); 
			my_button.setSelectedIcon(icon2); 
			my_button.setPressedIcon(icon2); 
			my_button.setEnabled(enabled);
		  	
			my_button.setFont(font);
			//			 Set up Anti-Aliasing for text rendering
			//		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
			//                          RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			
		  	
		  	my_button.setFocusable(false);
		  	 //Register a listener for the radio buttons.
		  	//my_button.addActionListener(this);
		  	
		  	
			// action on double_click 
		  	final int index = i;	
		  	final String txt = my_button.getText();
		  	
    		MouseListener mouseListener = new MouseAdapter() {
    		    public void mouseClicked(MouseEvent e) {
    		    	
    		    	//if (e.getClickCount() >= 1) {
    		    	//	selected_index = index;
    		    	//}
    		    	
    		        if (e.getClickCount() >= 2) {
    		        	//fireActionEvent( new ActionEvent(this, selected_index,"double-click" ));    		        	
    		        	fireActionEvent( new ActionEvent(this, 0, txt ));    		        	
       		         
    		        }
    		    }
    		};
    		my_button.addMouseListener(mouseListener);
		  	
		  	
		  	this.add(my_button);
		  	
		  	group.add(my_button);
		  	
		 
	}
	
	
	public int getSelectedIndex(){
		//return selected_index;
		
		
		int i = 0;
		for (Enumeration e = group.getElements() ; e.hasMoreElements() ;) {
	      	JRadioButton my_button = (JRadioButton)e.nextElement();      	
	      	if (my_button.isSelected())
	      		return i;
	      	i ++;
		}
		return -1;
		
	}
	
	public String getSelectedValue(){
		
		
		for (Enumeration e = group.getElements() ; e.hasMoreElements() ;) {
	      	JRadioButton my_button = (JRadioButton)e.nextElement();      	
	      	if (my_button.isSelected())
	      		return my_button.getText();
		}
		
		return null;
	}
	
	public void validate_choice()
	{
		//System.out.println( "validate_choice : " + getSelectedValue() );
		fireActionEvent( new ActionEvent(this, 0, getSelectedValue() ));    		        	
	         
	}
	
	
	
////////////////////////Civ_PanelEvent
//	 Add the event registration and notification code to a class.

       // Create the listener list
       protected javax.swing.event.EventListenerList listenerList =
           new javax.swing.event.EventListenerList();
   
       // This methods allows classes to register for MyEvents
       public void addActionListener(ActionListener listener) {
           listenerList.add(ActionListener.class, listener);
       }
   
       // This methods allows classes to unregister for MyEvents
       public void removeActionListener(ActionListener listener) {
           listenerList.remove(ActionListener.class, listener);
       }
   
       // This private class is used to fire MyEvents
       void fireActionEvent(ActionEvent evt) {
           Object[] listeners = listenerList.getListenerList();
           // Each listener occupies two elements - the first is the listener class
           // and the second is the listener instance
           for (int i=0; i<listeners.length; i+=2) {
               if (listeners[i]==ActionListener.class) {
                   ((ActionListener)listeners[i+1]).actionPerformed(evt);
               }
           }
       }
       
/////////// Civ_Screen
		
		  public JPanel get_content_pane()
		  {
		  	return this;
		  }
		    
		    public void swing_worker_construct()
		    {
		    	if (ressource==null)
		    		return;
		    	
	    		Font font = ressource.font.deriveFont(Font.PLAIN, 15);	
	 	       
		        BufferedImage buttons = ressource.load_image(
						ressource.civ3("art\\buttonsFINAL.pcx"));
				
		        My_Button temp = new My_Button( buttons, 0, 36, 21, 21 );
						
		        initialize( temp, font );
				
		    }

		    public void timer_run(){
		    	// javax.swing.Timer
		        
		    }
		    
		    public void flush(){ // BufferedImage.flush()
		    }

}
	
	
