/*
 * Civ_Main.java
 *
 * Created on 4 avril 2004, 18:10
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
import javax.imageio.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.spi.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;


import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.net.*;
import java.util.zip.*;

import ressource.*;
import ruler.*;
import state.*;

/**
 * 
 * @author roudoudou
 */





public class Civ_Main {

	public Civ_Frame frame;
	
	
	// les differents "écrans"	

	Civ_Console civ_console;
	Main_Screen main_screen;
	// ------------------------
	
	

	public Civ_State state;

	//-------

	public Civ_Config config;
	public Civ_Ressource ressource;
	public Civ_Ruler ruler;

	public int my_id = 1; // attention : 0= les barbares

	/* à trier ... */
	
	public int time, time1;
	public int countdown;
	public int ms_per_redraw;
	public int request_end;
	public int need_redraw;
	public int client_state;
	public int current_player;
	public int master_id; // le maître du jeu
	public String my_username;

	

	public java.util.List my_units() {
		return state.my_units(my_id);
	}

	public java.util.List my_cities() {
		return state.my_cities(my_id);
	}

	/** Creates a new instance of Civ_Main */
	public Civ_Main() {
		//civ_panel = new civ_panel(this);
		// civ_event = new Civ_Event(this);
		ressource = null;
		config = new Civ_Config();
		ruler = new Civ_RulerClassic();
	}

	
	
	

	void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
		}
	}
	
	public void fast_init( String filename )
	{		
		Civ_Loader sav_file = new Civ_Loader();
		sav_file.bicload(  filename );	
		
		
		state = sav_file.get_state();

		
		ruler.reset(this.state);
	
		config = new Civ_Config();
		config.load();

		
		ressource = new Civ_Ressource( config.civ3_dir, config.civ3_version );
		ressource.load_civ3_font();
		ressource.load_from_disk();
		
		chargement_finalisation();
		
		
		frame = new Civ_Frame(config.wscreen, config.hscreen,
				config.fullscreen);
	}
	
	public void fast_init2()
	{				
		config = new Civ_Config();
		config.load();

		
		ressource = new Civ_Ressource( config.civ3_dir, config.civ3_version );
		ressource.load_civ3_font();
		//ressource.load_from_disk();
		
		
		frame = new Civ_Frame(config.wscreen, config.hscreen,
				config.fullscreen);
	}
	
	void prechargement() {
		final SwingWorker worker = new SwingWorker() {
			public Object construct() {
				//...code that might take a while to execute is here...

				// attente 1 s avant de démarrer
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
				}

				System.out.println("préchargement commencé");
				long t1 = System.currentTimeMillis();

				initialisation_niveau_2(1);

				long t2 = System.currentTimeMillis();
				System.out.println("préchargement fini");
				System.out.println("durée= " + (t2 - t1) + " ms");

				return null;
			}
		};
		worker.start(); //required for SwingWorker 3
	}

	
	

	void initialisation_niveau_1(int option) {

		// chargement config
		
		config.load();
		config.save();
		
		// recherche civ3
		//config.civ3_dir = null;
		
		/*if (!config.check_civ3())
		{
			//Create a file chooser
			JFileChooser fc = new JFileChooser();		
			fc.setDialogTitle( "please locate the Civilization III folder on your disk" );
			fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			
			int returnVal = fc.showOpenDialog(null);		
			if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //This is where a real application would open the file.
                System.out.println("Opening: " + file.getAbsolutePath());
                config.civ3_dir = file.getAbsolutePath();
                config.save();
            } 
		}*/
		
		if (!config.check_civ3())
		{
			JOptionPane.showMessageDialog(null,
					"<html>Error : Civilization 3 directory not found !<br>"
					+ "please edit civ3_dir in civ_java.conf</html>",
					"",JOptionPane.INFORMATION_MESSAGE);
			
			System.out.println("exitting.");
			System.exit(1);
		}
		
		
		// initialisation niveau 1
		
			
		frame = new Civ_Frame(config.wscreen, config.hscreen,
				config.fullscreen);
		
		frame.exit_on_escape = false;
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				terminaison();
			}
		});
		
		
		
		
		// console
		ressource = new Civ_Ressource( config.civ3_dir, config.civ3_version );
		ressource.load_civ3_font();
		civ_console = new Civ_Console(this, 
				ressource.font
				,config.wscreen, config.hscreen);
		
		frame.afficher_ecran( civ_console );
		
		

	}

	void initialisation_niveau_2(int option) {

		// initialisation niveau 2
		
		
		ressource.load_from_disk();
		//ressource.prechargement_animations(state);


	}

	void game_init() {

		ruler.reset(this.state);
		
		

		
	}

	// on nettoie tout
	void terminaison() {

		System.out.println("exitting ..");
		System.out.println("bye bye.");
		
		if (frame!=null)
		{
			frame.setVisible(false);
			frame.dispose();
		}
		
		System.exit(0);
	}

	
	

	InputStream open_url(String name) {
		String base = "http://localhost/";

		URL page = null;
		try {
			page = new URL(base + name);
		} catch (MalformedURLException e) {
			System.out.println("open_url(): MalformedURLException");
			System.exit(1);
		}

		System.out.println("connecting to : " + page);

		InputStream in = null;
		try {
			in = page.openStream();
		} catch (IOException e) {
			System.out.println("open_url(): IOException");
			System.exit(1);
		}
		return in;
	}

	void chargement_internet() {
		InputStream in = open_url("servlet/DataServlet");

		System.out.println("loading data ... ");
		try {
			GZIPInputStream gz_in = new GZIPInputStream(in);
			ObjectInputStream obj_in = new ObjectInputStream(gz_in);
			state = (Civ_State) obj_in.readObject();
			obj_in.close();
		} catch (Exception e) {
		}

		System.out.println("state=" + state);
	}

	void chargement_URL( URL scenario )  {
	
		System.out.println( "chargement URL " + scenario );	

		InputStream in = null;
		try {
			in= scenario.openStream();
		} catch(IOException e) {
			System.out.println(e);
			terminaison();
			}

		// note : on n'autorise que le chargement de fichier compressés
		// à partir d'une URL
		Civ_Loader loader = new Civ_Loader();	
		loader.verbose = config.verbose; 
		loader.bicload_compressed( in );
		state = loader.get_state();
		
		chargement_finalisation();
	}
	
	void chargement_fichier( String scenario )  {

		System.out.println( "chargement fichier " + scenario );	
	
		Civ_Loader loader = new Civ_Loader();	
		loader.verbose = config.verbose; 	
		loader.bicload( scenario );
		state = loader.get_state();
		
		chargement_finalisation();
	}
	
		
	void chargement_finalisation()  {

		//chargement_internet();

		/*
		 * si pas de proto => on charge les informations "statiques" 
		 * à partir d'un fichier de reference
		 * (civ3ptw "civ3X.bix")
		 * (civ3 "civ3mod.bic") 
		 */
		
		if ( (state.proto == null) )
		{
			Civ_Loader loader = new Civ_Loader();	
			loader.verbose = config.verbose; 
			loader.bicload( ressource.civ3("civ3mod.bic") );
			Civ_State state0 = loader.get_state();
			
			if (state.proto == null)  	state.proto = state0.proto;		
			if (state.race == null) 	state.race = state0.race;
			if (state.terrain == null)  state.terrain = state0.terrain;
			if (state.building == null) state.building = state0.building;
			if (state.tech == null)  	state.tech = state0.tech;
			if (state.good == null)  	state.good = state0.good;
			if (state.leader == null)  	state.leader = state0.leader;	  
			
			if (state.unit == null)  	state.unit = state0.unit;	
			if (state.city == null)  	state.city = state0.city;	
			
			if (state.citizen == null)  	state.citizen = state0.citizen;	
			if (state.culture == null)  	state.culture = state0.culture;	
			if (state.government == null)  	state.government = state0.government;	
			
			
		}
		
	
		/*
		 * -- reveal_map ref
		 *  -- section game state <- readIORef ref let x = (game state) {
		 * game_tech= (game_tech init_game) // (zip [0..9] (repeat (-1))) }
		 * modifyIORef ref $ \st -> st { game = x }
		 *  
		 */

		state.startup_init();
		
		state.reset_fow();
		//state.reveal_map();
		
		
		
	}

	public void nouvelle_partie() {
		
		main_screen = new Main_Screen(this
				,config.wscreen, config.hscreen );
		
		main_screen.center_view();

		afficher_ecran( "main_screen" );
		
	}
	
	private Civ_Screen previous_screen=null;
	private Civ_Screen current_screen=null;
	
	
	public void afficher_ecran( String txt )
	{
		Civ_Screen next_screen = null;
		
		System.out.println("next_screen = " + txt);
		if ("previous".equals(txt))
		{
			next_screen = previous_screen;
		}
	    else if ("main_screen".equals(txt))
	    {
	    	next_screen = main_screen;
		}
	    else if ("domestic_screen".equals(txt))
	    {
	    	next_screen = new Domestic_Screen( this ); 
		}
	    else if ("console".equals(txt))
	    {
	    	next_screen = civ_console ;
		}
	    else if ("title_screen".equals(txt))
	    {
	    	next_screen = new Title_Screen(  this );
		} 
	    else if ("science_screen".equals(txt))
	    {
	    	next_screen = new Science_Screen( this ); 
		}
	    


		
	    if (next_screen==null)	{
			System.out.println("null screen, exitting.");
			//System.exit(1);
			quitter();
	   	}
	    
	    previous_screen = current_screen;
	    current_screen = next_screen;
	    
	    frame.afficher_ecran( current_screen );
		
	}
	
	public void quitter() {
		
		terminaison();
	}
		

	
	public void main_(String filename, int option) throws Exception {

		// initialisation
		// creation de la fenêtre
		// minimum vital pour afficher des informations
		initialisation_niveau_1(option);

		

		// le reste

		// config.scenario = "null";
		
		
		
		
		if ("select".equals(config.scenario))
		{
			//Create a file chooser
			JFileChooser fc = new JFileChooser(config.civ3_dir);		
			fc.setDialogTitle( "select a Civilization III scénario" );
			
			
			ScenarioFilter filter = new ScenarioFilter();
		    fc.setFileFilter(filter);
			
			int returnVal = fc.showOpenDialog(null);		
			if (returnVal != JFileChooser.APPROVE_OPTION) {
				System.exit(1);
			}
			
            File file = fc.getSelectedFile();
               
    		chargement_fichier( file.getAbsolutePath() );
    	
			
		}
		else if ("built-in".equals(config.scenario))
		{
			System.out.println("using built-in scenario");
			chargement_URL( Civ_Main.class.getResource("../demo.biq") );
		}
		else
		{
			chargement_fichier( config.scenario );

		}
		
	    

		
		
		
		initialisation_niveau_2(option);

		/*
		 * -- initialisation reseau sdlNet_Init
		 */

		// ecran reseau
		game_init();

		// boucle des evenements
		
		
		afficher_ecran( "title_screen" );
		
		//nouvelle_partie();
	

		// la première tâche principale va se terminer,
		// mais les autres tâches vont prendre le relais
		// notamment la tâche des évenements Swing

	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main0(final String filename, final int option)
			throws Exception {

		(new Civ_Main()).main_(filename, option);

		/*
		 * javax.swing.SwingUtilities.invokeLater(new Runnable() { public void
		 * run() { (new Civ_Main()).main_(filename,option); } });
		 */

	}

	public static void main(String[] args) throws Exception {

		/*System.setProperty("sun.java2d.translaccel","true");
		System.setProperty("sun.java2d.accthreshold", "0");
		System.setProperty("sun.java2d.ddscale","true");
		System.setProperty("sun.java2d.ddforcevram", "true"); 
		*/
		
		/*System.setProperty("sun.java2d.opengl","True");
		System.setProperty("sun.java2d.translaccel", "true");
		System.setProperty("sun.java2d.ddforcevram", "true");
		System.setProperty("sun.java2d.trace","log,timestamp,count,verbose");
		*/

		//String filename = "D:\\julien\\dev_java\\civ_java\\Rome.bic";
		String filename = "demo.biq";
		int option = 0;

		if (args.length >= 1)
			filename = args[0];
		if (args.length >= 2)
			option = Integer.parseInt(args[1]);

		//(new Civ_Main()).main_(filename,option);

		main0(filename, option);

	}

}

