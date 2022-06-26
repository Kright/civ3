
/*
 * Created on 28 févr. 2005
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
import javax.swing.*;
import javax.swing.plaf.metal.MetalSliderUI;
import javax.swing.event.*;
import javax.swing.table.*;

import ressource.Civ_Ressource;
import state.City;
import state.CityValue;
import state.Civ_State;
import state.Leader;
import state.LeaderValue;



/**
 * @author roudoudou
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */



public class Domestic_Screen extends My_Panel
implements  KeyListener
			,ActionListener
			,ChangeListener 
{

	private Civ_Frame frame;	
	private Civ_Main civ_main;
	private Civ_Ressource ressource;
	private Civ_State state;
	private Leader leader;
	
	private LeaderValue leader_value ;
	
	// ressources
	private BufferedImage city_icons, units_32, pop_heads,
			  buildings_small   ;
	
	private Font small_font,standard_font,big_font;
	
	private JSlider slider_science, slider_luxury;
	
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
	
	
	private City[] my_cities;	    
	private CityValue my_values[];    
	          
		
	
	  /* Creates a new instance of Civ_Ecran */
	public Domestic_Screen( Civ_Main civ_main ) {
    
          super( civ_main.ressource,
          		civ_main.ressource.civ3("art\\advisors\\domestic.pcx") ); 
          
          ///////////////////////////////////
          	this.civ_main = civ_main;
      		this.ressource = civ_main.ressource;
      		this.frame = civ_main.frame;	
      		this.state = civ_main.state;
      		this.leader = state.leader[civ_main.my_id-1];
      		
      		
      		
  		addKeyListener(this); 
  		
  		this.setFocusable(true); 
  }
  
	public String format( int x )
	{
		if (x < 0)
			return format_neg(-x);
		else
			return format_pos(x);
	}
	
	public String format_neg( int x )
	{
		return "-" + Integer.toString(x);
	}
	
	public String format_pos( int x )
	{
		return "+" + Integer.toString(x);
	}
	
  public void update_value()
	 {
  		
  		System.out.println( "leader : " + leader );
	 	leader_value.compute( civ_main.my_id );
	 	System.out.println( "leader_value : " + leader_value );
	 	
	 	revenus.setText("Revenus: " + leader_value.revenus);
	 	revenus_villes.setText("Des villes: " + format_pos(leader_value.revenus_villes));
	 	revenus_percepteurs.setText("Des percepteurs: " + format_pos(leader_value.revenus_percepteurs));
	 	revenus_civ.setText("Des autres civs: " + format_pos(leader_value.revenus_civ));
	 	revenus_interets.setText("Des intérêts: " + format_pos(leader_value.revenus_interets));
	 	
	 	
	 	depenses.setText("Dépenses: " + leader_value.depenses);
	 	depenses_science.setText(format_neg(leader_value.depenses_science) + ": Science" );
	 	depenses_distractions.setText(format_neg(leader_value.depenses_distractions) + ": Distractions" );
	 	depenses_corruption.setText(format_neg(leader_value.depenses_corruption) + ": Corruption" );
	 	depenses_maintenance.setText(format_neg(leader_value.depenses_maintenance) + ": Maintenance" );
	 	depenses_unites.setText(format_neg(leader_value.depenses_unites) + ": Coût des unités" );
	 	depenses_civ.setText(format_neg(leader_value.depenses_civ) + ": Aux autres civs" );
	 	
	 	
	 	gain.setText("Gain: " + format(leader_value.gain) );
	 	tresor.setText("Trésor: " + leader.gold + " Or" );
	 	

		
	 	

	 	repaint();
	 }
  
  public void update_slider()
  {
  	slider_science.setValue(leader.science_rate);
  	slider_luxury.setValue(leader.luxury_rate());
 
  	label_science.setText( (leader.science_rate*10) + "%" );
    label_luxury.setText( (leader.luxury_rate()*10) + "%" );
  }
  
  /////////////// listeners //////////////////
  
 
  
  public void stateChanged(ChangeEvent e) {
    JSlider source = (JSlider)e.getSource();
    int val = (int)source.getValue();
    
    
    
    
    if (source == slider_science)
    {
    	int luxury_rate = leader.luxury_rate();
		leader.science_rate = val;
		leader.tax_rate = 10 - luxury_rate - leader.science_rate; 
		if (leader.tax_rate<0)
		{
			leader.tax_rate = 0;
		}
    }
	else if (source == slider_luxury)
    {
		
		leader.tax_rate = 10 - val - leader.science_rate; 
		if (leader.tax_rate<0)
		{
			leader.tax_rate = 0;
			leader.science_rate = 10 - val ;
		}
    }
	
  
    update_slider();
    
    
    if (!source.getValueIsAdjusting()) { //done adjusting
        // textField.setValue(new Integer(fps)); //update ftf value
    	
    	update_value();
     	
     } else { //value is adjusting; just set the text
         //textField.setText(String.valueOf(fps));
     }
     

  }
  
  // action   
  public void actionPerformed(ActionEvent e) {
 	   
 		String txt = e.getActionCommand();
 		System.out.println("domestic_advisor, action_performed : " + txt);
 	    
 		if ("exit".equals(txt))
   		{	   			
   			civ_main.afficher_ecran("main_screen");
   		}
 		else if  ("science".equals(txt))
   		{	   			
   			civ_main.afficher_ecran("science_screen");
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
			System.out.println("Domestic_Advisor : " + leader); 		  
			leader_value = new LeaderValue(state);
			leader_value.compute( civ_main.my_id );
	    	
	    	// le reste ...
	    	Advisor_Tab advisor_tab = new Advisor_Tab( civ_main.ressource, this );    	
	    	advisor_tab.domestic.setSelected(true);
	    	advisor_tab.setLocation(0,0);
	    	this.add(advisor_tab);
	    	
	    	// slider
	    	final Icon science_icon = icon_city_icons(1);
	    	final Icon luxury_icon = icon_city_icons(12);
	    	
	    	
	    	slider_science = new JSlider(JSlider.HORIZONTAL, 0, 10, 5);
	    	slider_science.addChangeListener(this);
	    	slider_science.setPaintTicks(false);
	    	slider_science.setPaintTrack(false);
	    	slider_science.setPaintLabels(false);
	    	slider_science.setSnapToTicks(true);
	    	slider_science.setBounds( 560, 85, 204, 32 );
	    	slider_science.setOpaque(false);
	    	slider_science.setUI(new my_SliderUI(science_icon));	    	
	    	this.add(slider_science);
	    	
	    	slider_luxury = new JSlider(JSlider.HORIZONTAL, 0, 10, 5);
	    	slider_luxury.addChangeListener(this);
	    	slider_luxury.setPaintTicks(false);
	    	slider_luxury.setPaintTrack(false);
	    	slider_luxury.setPaintLabels(false);
	    	slider_luxury.setSnapToTicks(true);
	    	slider_luxury.setBounds( 560, 128, 204, 32 );
	    	slider_luxury.setOpaque(false);
	    	slider_luxury.setUI(new my_SliderUI(luxury_icon));
	    	this.add(slider_luxury);
	    	
	    	
	    	Color violet = new Color( 156, 24, 255 );
			
	    	label_science = new JLabel();
	    	label_science.setBounds( 758, 92, 26, 16 );
	    	label_science.setFont(small_font);
	    	label_science.setHorizontalAlignment(JLabel.RIGHT);
	    	label_science.setForeground(violet);
	    	this.add(label_science);
	    	
	    	label_luxury = new JLabel();
	    	label_luxury.setBounds( 758, 138, 26, 16 );
	    	label_luxury.setFont(small_font);
	    	label_luxury.setHorizontalAlignment(JLabel.RIGHT);
	    	label_luxury.setForeground(violet);
	    	this.add(label_luxury);
	    	
	    	
	    	
	    	// Revenus
	    	
	    	revenus = new JLabel();
	    	revenus.setHorizontalAlignment(JLabel.LEFT);
	    	revenus.setBounds( 246, 100, 120, 20 );
	    	revenus.setFont(standard_font);
			this.add(revenus);
			
	    	JPanel pane1 = new JPanel();
	    	pane1.setLayout(new GridLayout(4,1));
	    	pane1.setBounds(83, 95, 128, 68);
	    	pane1.setOpaque(false);
	    	this.add(pane1);
			
			
	   
		
	    	revenus_villes = new JLabel();
	    	revenus_villes.setHorizontalAlignment(JLabel.RIGHT);
	    	revenus_villes.setFont(standard_font);
			pane1.add(revenus_villes);
			

			revenus_percepteurs = new JLabel();
			revenus_percepteurs.setHorizontalAlignment(JLabel.RIGHT);
			revenus_percepteurs.setFont(standard_font);
			pane1.add(revenus_percepteurs);
			
			revenus_civ = new JLabel();
			revenus_civ.setHorizontalAlignment(JLabel.RIGHT);
			revenus_civ.setFont(standard_font);
			pane1.add(revenus_civ);
			
			revenus_interets = new JLabel();
			revenus_interets.setHorizontalAlignment(JLabel.RIGHT);
			revenus_interets.setFont(standard_font);
			pane1.add(revenus_interets);
			
			
			

			//	depenses
			
			depenses = new JLabel();
			depenses.setHorizontalAlignment(JLabel.LEFT);
			depenses.setBounds( 246, 152, 120, 20 );
			depenses.setFont(standard_font);
			this.add(depenses);
			
	    	JPanel pane2 = new JPanel();
	    	pane2.setLayout(new GridLayout(6,1));
	    	pane2.setBounds( 380, 84, 160, 100 );
	    	pane2.setOpaque(false);
	    	this.add(pane2);
		
	    	depenses_science = new JLabel();
	    	depenses_science.setHorizontalAlignment(JLabel.LEFT);
	    	depenses_science.setFont(standard_font);
	    	pane2.add(depenses_science);
			
	    	depenses_distractions = new JLabel();
	    	depenses_distractions.setHorizontalAlignment(JLabel.LEFT);
	    	depenses_distractions.setFont(standard_font);
	    	pane2.add(depenses_distractions);
	    	
	    	depenses_corruption = new JLabel();
	    	depenses_corruption.setHorizontalAlignment(JLabel.LEFT);
	    	depenses_corruption.setFont(standard_font);
	    	pane2.add(depenses_corruption);
	    	
	    	depenses_maintenance = new JLabel();
	    	depenses_maintenance.setHorizontalAlignment(JLabel.LEFT);
	    	depenses_maintenance.setFont(standard_font);
	    	pane2.add(depenses_maintenance);
			
	    	depenses_unites = new JLabel();
	    	depenses_unites.setHorizontalAlignment(JLabel.LEFT);
	    	depenses_unites.setFont(standard_font);
	    	pane2.add(depenses_unites);
	    	
	    	depenses_civ = new JLabel();
	    	depenses_civ.setHorizontalAlignment(JLabel.LEFT);
	    	depenses_civ.setFont(standard_font);
	    	pane2.add(depenses_civ);
	    	
	    	
	    	// gain, tresor
	    	gain = new JLabel();
	    	gain.setHorizontalAlignment(JLabel.LEFT);
	    	gain.setBounds( 246, 192, 120, 20 );
	    	gain.setFont(standard_font);
			this.add(gain);
			
			tresor = new JLabel();
			tresor.setHorizontalAlignment(JLabel.CENTER);
			tresor.setBounds( 82, 192, 154, 20 );
			tresor.setFont(standard_font);
			this.add(tresor);
	    	
			// titre
			JLabel label;
			label = new JLabel("CONSEILLER INTERIEUR");
			label.setHorizontalAlignment(JLabel.CENTER);
			label.setBounds( 0, 24, 1024, 25 );
			label.setFont(big_font);
			//label.setFont(new Font("monospaced", Font.BOLD, 24));	
			
			//label.setFont(civ_main.ressource.font.deriveFont(Font.BOLD, 24)	);
			this.add(label);
			
			
			// exit
			
			My_Button exit = new My_Button(
					ressource.load_image(
      				ressource.civ3("art\\exitBox-backgroundStates.pcx")));
			exit.setLocation(950, 718);
			exit.setActionCommand("exit");
			exit.addActionListener(this);
			this.add(exit);
			
			
			// table
			
			initialize_city_table();
			
			
			
			
			///////////////////
			update_value();
			update_slider();
	    }
	    
	    
	    
	    Icon icon_city_icons( int id  )
		{
			// 25 icones 32x32		
			return new My_Icon(city_icons, id*31, 0, 32, 32);
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
      		
      		pop_heads = ressource.load_image(
      				ressource.civ3("art\\SmallHeads\\popHeads.pcx"));
			
	      		
			buildings_small = ressource.load_image(
	  				ressource.civ3_use_version("art\\City Screen\\buildings-small.pcx", state.version));
		
		
	    }
	    
	    
	 
	    
	    JTableHeader header;
	    
	    private void initialize_city_table()
	    {
	    	//java.util.List collection = state.my_cities(civ_main.my_id);
	    	java.util.List collection = state.city;
			
			my_cities = (City[])(collection.toArray(new City[collection.size()] ));
      		
			my_values = new CityValue[my_cities.length];
			for( int i=0;i<my_cities.length;i++)
			{
				my_values[i] = new CityValue(state);
				my_values[i].compute(my_cities[i]);
			}
			
			TableColumnModel tableColumnModel = new DefaultTableColumnModel();

			TableCellRenderer renderer = new MyTableCellRenderer();
			
			TableColumn[] column = new TableColumn[10];
			
			for (int i=0;i<10;i++)
    		{
				column[i]= new TableColumn(i,45);
				column[i].setCellRenderer(renderer);
				//column[i].setHeaderRenderer(renderer);
				//column[i].setResizable(false);		
    			tableColumnModel.addColumn(column[i]);
    		}
			
			column[0].setPreferredWidth(130);
			column[8].setPreferredWidth(250);
			column[9].setPreferredWidth(100);
			
		
			
			
    		JTable table = new JTable(new MyTableModel(), tableColumnModel); 
    		table.setShowGrid(false);
   			//table.setShowHorizontalLines(false);
    		
    		
    
    		table.setPreferredScrollableViewportSize(new Dimension(890, 9*45));
    		table.setOpaque(false);
    		table.setRowHeight(45);
    	
    	
    		
    		
    		JScrollPane scrollpane = new JScrollPane(table);
    		scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    		scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    		
    		scrollpane.setBounds( 85, 297, 890, 9*45 );
    		//scrollpane.setBounds( 85, 280, 890, 9*45 );
    		scrollpane.setOpaque(false);
    		scrollpane.getViewport().setOpaque(false); 
    		scrollpane.setBorder(BorderFactory.createEmptyBorder());
    		//scrollpane.getVerticalScrollBar().setUnitIncrement(32);
    		//scrollpane.getVerticalScrollBar().setBlockIncrement(32);
    		
    		//JViewport vp  = scrollpane.getColumnHeader();
    		System.out.println("scrollpane.getColumnHeader=" + scrollpane.getColumnHeader());
    		//JViewport vp  = scrollpane.getColumnHeader();
    		System.out.println("scrollpane.getRowHeader=" + scrollpane.getRowHeader());
    		
    		
    		
    		
    		/*JPanel scrollpane = new JPanel(new FlowLayout());
    		//table.setPreferredSize( new Dimension (890, 9*45));
    		scrollpane.setBounds( 85, 297, 890, 9*45 );
    		scrollpane.add(table);
    		*/
    		
    		/*header = table.getTableHeader();
    		header.setOpaque(false);
    		header.setBackground(Color.GREEN);
    		header.setResizingAllowed(false);
    		header.setReorderingAllowed(false);
    		*/
    		
    		
    		this.add(scrollpane);
    		this.setFocusable(true); 
    		//liste.setFocusable(false); 
    		scrollpane.setFocusable(false); 

    		////////////////////////////////
    		
    		int y = 238;
    		int x = 230;
    		int h = 50;
    		
    		JLabel label;
    		label = new JLabel("Villes");  
    		label.setBounds( 85, y, 900, h );
    		label.setHorizontalAlignment(JLabel.LEFT);
    		label.setFont(standard_font);
    		this.add(label);
    		
    		
    		
    		label = new JLabel(icon_city_icons(6));
    		label.setBounds( x+8, y+6, 900, h );
    		label.setHorizontalAlignment(JLabel.LEFT);
    		this.add(label);
    		
    		label = new JLabel(icon_city_icons(7));
    		label.setBounds( x, y, 900, h );
    		label.setHorizontalAlignment(JLabel.LEFT);
    		this.add(label);
    		
    		label = new JLabel(icon_city_icons(4));
    		label.setBounds( x+47+8, y+6, 900, h );
    		label.setHorizontalAlignment(JLabel.LEFT);
    		this.add(label);
    		
    		label = new JLabel(icon_city_icons(5));
    		label.setBounds( x+47, y, 900, h );
    		label.setHorizontalAlignment(JLabel.LEFT);
    		this.add(label);
    		
    		label = new JLabel(icon_city_icons(2));
    		label.setBounds( x+47*2+8, y+6, 900, h );
    		label.setHorizontalAlignment(JLabel.LEFT);
    		this.add(label);
    		
    		label = new JLabel(icon_city_icons(3));
    		label.setBounds( x+47*2, y, 900, h );
    		label.setHorizontalAlignment(JLabel.LEFT);
    		this.add(label);
    		
    		x  = 398;
    		
    		label = new JLabel(icon_city_icons(23));
    		label.setBounds( x, y, 900, h );
    		label.setHorizontalAlignment(JLabel.LEFT);
    		this.add(label);
    		
    		label = new JLabel(icon_city_icons(12));
    		label.setBounds( x+47-6, y, 900, h );
    		label.setHorizontalAlignment(JLabel.LEFT);
    		this.add(label);
    		
    		label = new JLabel(icon_city_icons(19));
    		label.setBounds( x+47+6, y, 900, h );
    		label.setHorizontalAlignment(JLabel.LEFT);
    		this.add(label);
    		
    		label = new JLabel(icon_city_icons(1));
    		label.setBounds( x+47*2, y, 900, h );
    		label.setHorizontalAlignment(JLabel.LEFT);
    		this.add(label);
    		
    		label = new JLabel(icon_city_icons(24));
    		label.setBounds( x+47*3, y, 900, h );
    		label.setHorizontalAlignment(JLabel.LEFT);
    		this.add(label);
    		
    		label = new JLabel("Population");
    		label.setFont(standard_font);
    		label.setBounds( 600, y, 250, h );
    		label.setHorizontalAlignment(JLabel.CENTER);
    		this.add(label);
    		
    		label = new JLabel("Prod. en cours");
    		label.setFont(standard_font);
    		label.setBounds( 850, y, 110, h );
    		label.setHorizontalAlignment(JLabel.RIGHT);
    		this.add(label);
    		
	    }
	    
	  
	    public class MyTableCellRenderer extends JPanel
        	implements TableCellRenderer {

	    	private String[] columnNames = {"Villes",
                    "food","shield","gold",
                    "prod","luxury","science","tax",
					"Population","Prod. en cours"};
	    	
	    	JLabel label;
	    	
				public MyTableCellRenderer() {
					super(new FlowLayout());
					//super(null);
					
					//setLocation(0,0);
					//setSize(45,90);
					//setPreferredSize(new Dimension(45,90));					
					setOpaque(false);
					//setBackground(Color.GREEN);
					
					
					label = new JLabel();
					label.setForeground(Color.RED);
					//label.setPreferredSize(new Dimension(45,45));
					//label.setLocation(0,0);
					//label.setSize(45,45);
					label.setFont(standard_font);
					label.setText( "?" );
					this.add(label);
					
					
				}

			public Component getTableCellRendererComponent(
			         JTable table, Object value,
			         boolean isSelected, boolean hasFocus,
			         int row, int col) {
				
				//String txt = (String)value ;
				//String txt =  My_Toolkit.coord(col,row);
				
				//label.setText( txt );
				label.setHorizontalAlignment(JLabel.CENTER);
				label.setVerticalAlignment(JLabel.CENTER);
				
				/*if ( row < 0 ) // header
				{
					//System.out.println("row=" + row);
					
					Container cont = header.getParent();
		    		//System.out.println("cont="+cont);
		    		JViewport vp = (JViewport)cont;
		    		vp.setOpaque(false);
		    		
					label.setText(columnNames[col]); 
					return this;
				}*/
				
				City c = my_cities[row];
				CityValue v = my_values[row];
				
				
				
				switch( col )
				{
					
				
				
				case 0: label.setText(c.name); label.setHorizontalAlignment(JLabel.LEFT); break;
				case 1: label.setText(v.food_loss + "." + v.food_income); break;
				case 2: label.setText(v.shield_loss + "." + v.shield_income); break;
				case 3: label.setText(v.gold_loss + "." + v.gold_income); break;
	    		
				case 4: label.setText(Integer.toString(0)); break;
				case 5: label.setText(Integer.toString(v.luxury)); break;
				case 6: label.setText(Integer.toString(v.science)); break;
				case 7: label.setText(Integer.toString(v.tax)); break;
				case 8: label.setText(Integer.toString(c.size)); break;
					 
						
				case 9: 		
				switch( c.build.type )
				{
					case City.Build.TYPE_UNIT:		
						label.setText( state.proto[c.build.id].name );		
						break;
					
					case City.Build.TYPE_BUILDING:	
						label.setText( state.building[c.build.id].name );
						break;
				
				}
				break;
				}
	    	
	    		
	    		
				return label;
				//return this;
			}
		}
	    
	    class MyTableModel extends AbstractTableModel {
	       

	        public int getColumnCount() {
	            return 10;
	        }

	        public int getRowCount() {
	            return my_cities.length;
	        }

	        public String getColumnName(int col) {
	            return null; //("header " + col);	        	
	        }

	        public Object getValueAt(int row, int col) {
	            //return My_Toolkit.coord(col,row);
	        	return null;
	        }

	        

	       

	    }
	    
	    
	/**********************************/
	
		
	
		
		
		
		public static void main(String[] args) throws IOException {
			
			
			String filename = "demo.biq";
			
			if (args.length>=1){
				filename = args[0];
			}
			
			Civ_Main civ_main = new Civ_Main();				
			civ_main.fast_init( filename );

			
			civ_main.afficher_ecran( "domestic_screen" );
			

			
			
		}
	
}


 class my_SliderUI extends MetalSliderUI 
 {
 	
 	Icon icon;
 	
 	public my_SliderUI(Icon icon)
 	{
 		this.icon = icon;
 	}
	
 	
 	protected  int getThumbOverhang() {
 		return 0; //icon.getIconWidth();
 	}
	
	protected Dimension getThumbSize(){
		return new Dimension(icon.getIconWidth(),icon.getIconHeight());
	}
	
	public void paintThumb(Graphics g) {
		
		//System.out.println("MetalSliderUI - paintThumb");
		//System.out.println("thumbRect " + thumbRect);
	
		icon.paintIcon(null,g,thumbRect.x,thumbRect.y);
		

		
	
	}
	
}





