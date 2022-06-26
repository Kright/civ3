
/*
 * Created on 7 mars 2005
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
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import ressource.Civ_Ressource;

/**
 * @author roudoudou
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Advisor_Tab extends JPanel
{


	  public JRadioButton domestic ;
	  public JRadioButton trade ;
	  public JRadioButton military ;
	  public JRadioButton foreign ;
	  public JRadioButton culture ;
	  public JRadioButton science ;
      
	
	
	
	  /* Creates a new instance of Advisor_Tab */
 public Advisor_Tab( Civ_Ressource ressource, ActionListener listener ) {
   
         
         super(null);
         
         // Put the radio buttons in a column in a panel.
	      //this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
	      
         this.setSize( 100, 768 );
         this.setOpaque(false);
         
         ///////////////////////////////////
         //this.ressource = ressource;
         
         BufferedImage advisor_tab = ressource.load_image( 
         		ressource.civ3("art\\SmallHeads\\advisor_tab.pcx"));
      
         
         int sx = 0;
         int sw = 60;
         int sh = 56;
         int stepx = 56;

         
         domestic 	= transform(new My_Button(advisor_tab, sx, sh*0, sw, sh, stepx, 0 ));
         trade 		= transform(new My_Button(advisor_tab, sx, sh*1, sw, sh, stepx, 0 ));
         military 	= transform(new My_Button(advisor_tab, sx, sh*2, sw, sh, stepx, 0 ));
         foreign 	= transform(new My_Button(advisor_tab, sx, sh*3, sw, sh, stepx, 0 ));
         culture 	= transform(new My_Button(advisor_tab, sx, sh*4, sw, sh, stepx, 0 ));
         science 	= transform(new My_Button(advisor_tab, sx, sh*5, sw, sh, stepx, 0 ));
         
         this.add(domestic);
         this.add(trade);
         this.add(military);
         this.add(foreign);
         this.add(culture);
         this.add(science);
         
         
         ButtonGroup group = new ButtonGroup();	 
         group.add(domestic);
         group.add(trade);
         group.add(military);
         group.add(foreign);
         group.add(culture);
         group.add(science);
                 
         
         domestic.setActionCommand("domestic");
         trade.setActionCommand("trade");
         military.setActionCommand("military");
         foreign.setActionCommand("foreign");
         culture.setActionCommand("culture");
         science.setActionCommand("science");
         
         domestic.setToolTipText("Intérieur");
         trade.setToolTipText("Commerce");
         military.setToolTipText("Militaire");
         foreign.setToolTipText("Diplomatique");
         culture.setToolTipText("Culturel");
         science.setToolTipText("Scientifique");
         
         domestic.addActionListener(listener);
         trade.addActionListener(listener);
         military.addActionListener(listener);
         foreign.addActionListener(listener);
         culture.addActionListener(listener);
         science.addActionListener(listener);
         
         
         int x = 0;
         int y = 244;
         int h = 70 ;
         domestic.setLocation(x, y+h*0);
         trade.setLocation(x, y+h*1);
         military.setLocation(x, y+h*2);
         foreign.setLocation(x, y+h*3);
         culture.setLocation(x, y+h*4);
         science.setLocation(x, y+h*5);
         
         
 		
 		this.setFocusable(true); 
 }
 
 JRadioButton transform( JButton buttons )
 {
 	Icon icon0, icon1, icon2;
 	
 	
 	icon0 = buttons.getIcon();  
	icon1 = buttons.getRolloverIcon(); 
	icon2 = buttons.getPressedIcon(); 
	
 	JRadioButton my_button = new JRadioButton();
  	//my_button.setMnemonic(KeyEvent.VK_B);
	
  	int w = buttons.getWidth();
  	int h = buttons.getHeight();
  	
  	//my_button.setSize(56, 56);	
  	my_button.setSize(w, h);	
  	my_button.setPreferredSize(new Dimension(w, h));	
	my_button.setFocusPainted(false);
	my_button.setBorderPainted(false);
	my_button.setRolloverEnabled(true);
	my_button.setContentAreaFilled(false);
	my_button.setIcon(icon0);  
	my_button.setRolloverIcon(icon1); 
	my_button.setSelectedIcon(icon2); 
	my_button.setPressedIcon(icon2); 
  	
	
  	
  	my_button.setFocusable(false);
  	
  	return my_button;
 }


 
 

	    

	
}



