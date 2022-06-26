/*
 * Civ_Loader.java
 *
 * Created on 30 mars 2004, 18:07
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
 */

/**
 *
 * @author  roudoudou
 */

package ressource;

import java.io.*;
import java.util.LinkedList;
import java.lang.Runtime;
import java.lang.AssertionError;

import state.Building;
import state.Citizen;
import state.City;
import state.Civ_State;
import state.Culture;
import state.Game;
import state.Good;
import state.Government;
import state.Leader;
import state.Proto;
import state.Race;
import state.SLOC;
import state.Tech;
import state.Terrain;
import state.Tile;
import state.Unit;



class Section_VER { 
	
	public static final int MAJOR_PTW = 11;
	public static final int MAJOR_C3C = 12;
	
	int major, minor ;
	String description,title ;
	
	public int hashcode()
	{
		return (major<<8) | (minor&255);
	}
	
	public String toString(){
		return   "VER#" 
		+ " major=" + (major) 
		+ " minor=" + (minor)
		+ " description=" + (description)
		+ " title=" + (title) ;
	}
	
	public boolean is_PTW()
	{ return (major >= Section_VER.MAJOR_PTW ); }
	
	public boolean is_C3C()
	{ return (major >= Section_VER.MAJOR_C3C ); }
	
}


class Section_WMAP { 
	int width ;
	int height;
	
	public String toString(){
		return   "WMAP#" 
		+ " width=" + (width) 
		+ " height=" + (height);
	}
} 



// data Section_UNKNOW = 	Section_UNKNOW (String)	
// utiliser "null"	



public class Civ_Loader {
	
	
	final int number_era = 4;
	
	
	
	Section_VER section_ver;
	Section_WMAP section_wmap;

	
	Tile[] section_tile;
	Proto[] section_prto;
	Race[] section_race;
	Unit[] section_unit;
	Leader[] section_lead;
	City[] section_city;
	Terrain[] section_terr;
	Building[] section_bldg;
	Tech[] section_tech;
	SLOC[] section_sloc;
	Good[] section_good;
	Citizen[] section_ctzn;
	Culture[] section_cult;
	Government[] section_govt;
	Game game;
	
	
	private InputStream in;
	private int current_pos = 0;
	public boolean verbose = false;
	
	/** Creates a new instance of Civ_Loader */
	public Civ_Loader() {
		
	}
	
	
	public byte anyChar() {
		//  int c;
		// while ((c = in.read()) != -1)
		current_pos ++;
		try {
			return (byte)in.read();
		}
		catch (IOException e) {
			// Gestion des exceptions d'entrée/sortie
			return (byte)-1;
		}         
	}
	
	void read( byte[] a ) { 
		for (int i=0;i<a.length;i++){
			a[i]=anyChar();
		}
	}
	
	String read( int n ) { 
		byte[] a = new byte[n];
		read(a);
		return (new String(a));
	}
	
	
	void skip( int n ) { 
		
		if (n<0)
		{ 	error("error : skip < 0 !");}
		
		for (int i=0;i<n;i++){
			anyChar();
		}
		/*try {
		 this.in.skip(n);
		 }
		 catch (IOException e) {
		 // Gestion des exceptions d'entrée/sortie
		  } */ 
	}
	
	void search ( byte[] s ) { 
		byte c;
		int i=0;
		int n=s.length;
		do {
			c = anyChar();
			if (c==s[i])
				i++;   
		} while(i<n);
	}
	
	
	/*myseek ref (n :: Int) = do
	 state <- readIORef ref
	 hSeek (input state) RelativeSeek (fromIntegral n)
	 return ()*/
	
	void error( String s ){
		System.out.println(s);
		System.out.println( "version : " + section_ver);
		System.exit(1);
	}    
	
	void string4x( String[] names ){
		String s = read(4);
		for (int i=0;i<names.length;i++)
		{
			if (s.equals(names[i]))
				return;
		}
		error( "wanted " + names + ", found " + s );
	} 
	
	
	
	int uint32() {
		int s1 = uint16();
		int s2 = uint16();
		return ( (s1) | (s2 << 16));
	}
	
	int uint16() {
		int s1 = uint8();
		int s2 = uint8();
		return ( (s1) | (s2 << 8));
	}
	
	int uint8 () {
		return ((int)(anyChar()) & 0xff);
	}
	
	boolean bool8() {
		return (uint8()!=0);
	}
	
	String civ_string( int n ){
		byte[] a = new byte[n];
		read(a);
		/*public String(byte bytes[], int offset, int length, String charsetName)
		 throws UnsupportedEncodingException*/
		int len=0;
		for (int i=0;i<n;i++){
			// $ map to_unicode $ to_cstring s	
			if (a[i]==0){
				len=i;
				break;
			}
		}
		String s="";
		try {
			s = new String(a,0,len,"ISO-8859-1");
		} catch(UnsupportedEncodingException e) {}
		return s;
	}
	
	private int[] array_uint32( ) {              
		int number = uint32();
		return array_uint32( number );
	}
	
	private int[] array_uint32( int number ) {         
		int[] tab;    
		tab = new int[number];
		for (int k=0;k<number;k++)
			tab[k] =  uint32();
		return tab;
	}
	
	private String[] array_civ_string( int len ) {              
		int number = uint32();
		return array_civ_string( number, len ) ;
	}
	
	private String[] array_civ_string( int number, int len ) {         
		String[] tab;       
		tab = new String[number];
		for (int k=0;k<number;k++)
			tab[k] =  civ_string(len);
		return tab;
	}
	
	
	private void parser_bic( int maxlen )   {
		
		
		// string4 ref "BIC "
		// try (string4 ref "BICX") <|> string4 ref "BIC "
		// skip ref 4
		//string4x ref ["BICX", "BIC "]
		String[] h=new String[2];
		h[0]="BICX";
		h[1]="BIC ";
		string4x( h );
		
		
		if ( maxlen > 0 )
		{
			//manyx ref parse_section (maxlen-4)
			
		}
		else
		{
			//manyy ref parse_section
			int n ;
			do {
				parse_section();                       
				try {
					n = this.in.available();                           
				} catch (IOException  e) { n=0; }
			} while (n>0);      
			
			
		}
		
		
	}
	
	int end_pos;
	
	private void section_begin( ) {
		int len = uint32(); 
		end_pos = current_pos + len;
	}
	
	private void section_end(  ) {
		if (current_pos > end_pos)
			error( "section overrun (" + (current_pos - end_pos) + " bytes)");
		skip( end_pos - current_pos );                     
	}
	
	private  void  parse_section( ) {
		
		
		
		String name = read(4);
		System.out.println("parse_section : " + name);
		
		if (name.equals("VER#"))
			parser_section_ver();
		else if (name.equals("WMAP"))
			parser_section_wmap();
		else if (name.equals("TILE"))
			parser_section_tile();
		else if (name.equals("CULT"))
			parser_section_cult();
		else if (name.equals("PRTO"))
			parser_section_prto();
		else if (name.equals("RACE"))
			parser_section_race();
		else if (name.equals("TERR"))
			parser_section_terr();
		else if (name.equals("BLDG"))
			parser_section_bldg();
		else if (name.equals("TECH"))
			parser_section_tech();
		else if (name.equals("GAME"))
			parser_section_game();
		else if (name.equals("SLOC"))
			parser_section_sloc();    
		else if (name.equals("FLAV"))
			parser_section_flav();
		else if (name.equals("GOOD"))
			parser_section_good();
		else if (name.equals("CITY"))
			parser_section_city();
		else if (name.equals("LEAD"))
			parser_section_lead();
		else if (name.equals("UNIT"))
			parser_section_unit();
		else if (name.equals("CTZN"))
			parser_section_ctzn();
		else if (name.equals("GOVT"))
			parser_section_govt();
		else //  -- $ Section_UNKNOW name
		{
			if (verbose) System.out.println("warning : unknow section " + name);
			parser_section_std();     
			//error("xxxx");
		}
		
		
	}
	
	
	
	
	private  void  parser_section_ver( ) {
		
		Section_VER s = new Section_VER(); // une seule section ver       
		int number = uint32();            
		//assert (number==1);
		
		//for (int i=0;i<number;i++)
		{
			
			section_begin();           
			skip(8);        
			s.major = uint32();
			s.minor = uint32();  
			s.description = civ_string(640);
			s.title = civ_string(64);         
			section_end();                        
			if (true) System.out.println(s);
		}
		
		section_ver = s;
	}
	
	private  void  parser_section_wmap( ) {     
		
		Section_WMAP s = new Section_WMAP(); // une seule section WMAP
		int number = uint32();            
		//assert (number==1);
		
		//for (int i=0;i<number;i++)
		{
			
			section_begin();           
			int number_of_resources = uint32();
			// resource_occurance <- count number_of_resources $ long ref
			skip(4*number_of_resources);
			skip(4);
			s.height = uint32();
			skip(16);
			s.width = uint32();   
			section_end();                        
			if (verbose) System.out.println(s);
		}
		
		section_wmap = s;
	}
	
	
	
	
	private  void  parser_section_bldg( ) {
		
		int number = uint32();   
		Building[] tab=new Building[number];
		
		if (verbose) System.out.println("number of buildings : " + number );
		
		
		for (int i=0;i<number;i++)
		{
			section_begin(); 
			// place specific section code here ...
			Building x =new Building();
	
			//x.description = civ_string(64);
			skip(64);
			x.name = civ_string(32);
	        x.civilopedia_entry = civ_string(32);
	        x.doubles_happiness_of = uint32();
	        x.gain_in_every_city = uint32();
	        x.gain_in_every_city_on_continent = uint32();
	        x.required_building = uint32();
	        x.cost = uint32();
	        x.culture = uint32();
	        x.bombardment_defense = uint32();
	        x.naval_bombardment_defense = uint32();
	        x.defense_bonus = uint32();
	        x.naval_defense_bonus = uint32();
	        x.maintenance_cost = uint32();
	        x.happy_faces_all_cities = uint32();
	        x.happy_faces = uint32();
	        x.unhappy_faces_all_cities = uint32();
	        x.unhappy_faces = uint32();
	        x.number_of_required_buildings = uint32();
	        x.air_power = uint32();
	        x.naval_power = uint32();
	        x.pollution = uint32();
	        x.production = uint32();
	        x.required_government = uint32();
	        x.spaceship_part = uint32();
	        x.required_advance = uint32();
	        x.rendered_obsolete_by = uint32();
	        x.required_resource_1 = uint32();
	        x.required_resource_2 = uint32();
	        
	        if (section_ver.is_PTW() )
			{
		        x.improvements = uint32(); 
		        x.other_characteristics = uint32();
		        x.small_wonders = uint32();
		        x.wonders = uint32();
		        x.number_of_armies_required = uint32();
			}
	        
	        if (section_ver.is_C3C() )
			{
			    x.flavors = uint32();
			    x.unknow1 = uint32();
			    x.unit_produced = uint32();
			    x.unit_frequency = uint32();
			}
	        
			section_end();      
			if (verbose) System.out.println(i + " : " + x);
			tab[i]=x;
		}
		
		section_bldg=tab;
	}
	
	private  void  parser_section_ctzn( ) {
		
		int number = uint32();   
		Citizen[] tab=new Citizen[number];
		
		if (verbose) System.out.println("number of citizen : " + number );
		
		
		for (int i=0;i<number;i++)
		{
			section_begin(); 
			// place specific section code here ...
			Citizen x =new Citizen();
			x.default_citizen = uint32();
			x.name = civ_string(32);
			x.civilopedia_entry = civ_string(32);
			x.plural_name = civ_string(32);
			x.prerequisite = uint32();
			x.luxuries = uint32();
			x.research = uint32();
			x.taxes = uint32();
			if (section_ver.is_C3C())
			{
				x.corruption = uint32();
				x.construction = uint32();
			}
			section_end();      
			if (verbose) System.out.println(x);
			tab[i] = x ;
		}
		
		section_ctzn=tab;
	}
	
	
	
	private  void    parser_section_prto( ) {
		
		int number = uint32();   
		section_prto=new Proto[number];
		
		if (verbose) System.out.println("number of proto : " + number );
		
		
		for (int i=0;i<number;i++)
		{
			section_begin(); 
			// place specific section code here ...
			Proto x =new Proto();
			skip(4);
			x.id = i;
			x.name = civ_string(32);
			x.civilopedia_entry  = civ_string(32);
			x.bombard_strength = uint32();
			x.bombard_range = uint32();
			x.capacity = uint32();
			x.shield_cost = uint32();   
			x.defense  = uint32(); 
			x.icon_index = uint32();
			x.attack  = uint32(); 
			x.operational_range = uint32();
			x.population_cost = uint32();
			x.rate_of_fire = uint32();
			x.movement  = uint32(); 
			x.required  = uint32();          
			x.upgrade_to = uint32();
			x.required_resource_1 = uint32();
			x.required_resource_2 = uint32();
			x.required_resource_3 = uint32();
			x.abilities = uint32(); 
			x.strategy  = uint32(); 
			x.available_to  = uint32(); 
			x.orders  = uint32(); 
			x.air_missions  = uint32(); 
			x.class_  = uint32(); 
			
			
			x.other_strategy = uint32();
			x.hp_bonus = uint32(); 
			
			if (section_ver.is_PTW() )
			{
				x.ptw_standard_orders = uint32();
				x.ptw_special_actions = uint32(); 
				x.ptw_worker_actions = uint32();
				x.ptw_air_missions = uint32();
				x.ptw_actions_mix = uint32();
			}
			
			section_end();      
			//if (verbose) System.out.println(x);
			section_prto[i] = x;
		}
		
		
	}
	
	
	
	private  void    parser_section_terr( ) {
		
		int number = uint32();   
		Terrain[] t=new Terrain[number];
		
		if (verbose) System.out.println("number of terrains : " + number );
		
		
		for (int i=0;i<number;i++)
		{
			section_begin(); 
			// place specific section code here ...
			t[i]=new Terrain();
			// skip_ ref 7
			// long		resflag			nres	number of possible resources in bits ( val/8 rounded up)
			// byte-flag x ciel([nres]/8) possible resources (binary) 
			int nres = uint32();
			//possible_resources <- skip ref $ (nres+7) `div` 8
			skip((nres+7)/8);
			t[i].name = civ_string(32);
			t[i].civilopedia_entry = civ_string(32);
			t[i].irrigation = uint32(); // (food) bonus
			t[i].mining = uint32(); // (shields) bonus
			t[i].road = uint32(); // (commerce) bonus  
			t[i].defense = uint32(); // bonus	
			t[i].movement_cost = uint32();
			t[i].food = uint32();
			t[i].shields = uint32();
			t[i].commerce = uint32();    
			t[i].worker_job = uint32();
			t[i].pollution_effect = uint32();  
			t[i].allow_cities = bool8();
			t[i].allow_colonies = bool8();
			
			// Conquest : VER# major=12 minor=6 
			if (section_ver.is_C3C())
			{
				t[i].impassable = bool8();
				t[i].impassable_by_wheeled = bool8();
				t[i].allow_airfields = bool8();
				t[i].allow_forts = bool8();
				t[i].allow_outposts = bool8();
				t[i].allow_radar_towers = bool8();                 
				skip(4);  
				t[i].landmark_enabled = bool8();
				t[i].landmark_food = uint32();
				t[i].landmark_shields = uint32();
				t[i].landmark_commerce = uint32();
				t[i].landmark_irrigation = uint32();   
				t[i].landmark_mining = uint32();
				t[i].landmark_road = uint32();
				t[i].landmark_movement_cost = uint32();
				t[i].landmark_defense_bonus = uint32();
				t[i].landmark_name = civ_string(32);
				t[i].landmark_civilopedia_entry = civ_string(32);
				skip(4);
				t[i].terrain_flags = uint32();
				t[i].disease_strength = uint32();
			}
			section_end();      
			if (verbose) System.out.println(t[i]);
		}
		section_terr=t;
	}
	
	private  void    parser_section_tech( ) {
		
		int number = uint32();   
		section_tech =new Tech[number];
		
		if (verbose) System.out.println("number of technologies : " + number );
		
		for (int i=0;i<number;i++)
		{
			section_begin(); 
			// place specific section code here ...
			Tech x =new Tech();
			x.name = civ_string(32);
			x.civilopedia_entry = civ_string(32);
			x.cost = uint32();
			x.era = uint32();
			x.icon = uint32();
			x.x = uint32();
			x.y = uint32();
			x.prerequisite_1 = uint32();
			x.prerequisite_2 = uint32();
			x.prerequisite_3 = uint32();
			x.prerequisite_4 = uint32();
			x.flags = uint32();
			

			section_end();      
			if (verbose) System.out.println(x);
			section_tech[i]=x;
		}
		
	}
	
	
	private  void    parser_section_race( ) {
		
		int number = uint32();   
		Race[] r=new Race[number];
		
		if (verbose) System.out.println("number of races : " + number );
		
		
		for (int i=0;i<number;i++)
		{
			//if (verbose) System.out.println("section_begin " + i + " @ " + current_pos );
			section_begin(); 
			// place specific section code here ...
			r[i]=new Race();
			r[i].city_name = array_civ_string(24);
			r[i].hero_name =  array_civ_string(32);
			r[i].leader_name = civ_string(32);	
			r[i].leader_title = civ_string(24);	
			r[i].civilopedia_entry = civ_string(32);	
			r[i].adjective = civ_string(40);	
			r[i].civilization_name = civ_string(40);	
			r[i].noun = civ_string(40);	           
			r[i].forward_filename = array_civ_string( number_era, 260 );
			r[i].reverse_filename = array_civ_string( number_era, 260 );
			r[i].culture_group = uint32();
			r[i].leader_gender = uint32();
			r[i].civilization_gender = uint32();
			r[i].aggression_level = uint32();
			r[i].unique_civilization_counter = uint32();
			r[i].shunned_government = uint32();
			r[i].favorite_government = uint32();
			r[i].default_color = uint32();
			r[i].unique_color = uint32();
			r[i].free_tech_1 = uint32();
			r[i].free_tech_2 = uint32();
			r[i].free_tech_3 = uint32();
			r[i].free_tech_4 = uint32();
			r[i].bonuses = uint32();
			r[i].governor_settings = uint32(); 
			r[i].build_never = uint32();
			r[i].build_often = uint32();
			r[i].plurality = uint32();
			/* seulement pour CONQUESTS */
			/*
			 r[i].king_unit = uint32();
			 r[i].flavors = uint32();
			 skip(4);
			 r[i].diplomacy_text_index = uint32();
			 r[i].scientific_leader_name = array_civ_string( 32 );   */
			section_end();      
			if (verbose) System.out.println(r[i]);
		}
		section_race=r;
	}
	
	private  void    parser_section_sloc( ) {
		
		int number = uint32();   
		SLOC[] s=new SLOC[number];
		
		if (verbose) System.out.println("number of SLOC : " + number );
		
		
		for (int i=0;i<number;i++)
		{
			section_begin(); 
			// place specific section code here ...
			s[i]=new SLOC();
			s[i].owner_type = uint32();
			s[i].owner = uint32();
			s[i].x = uint32();
			s[i].y = uint32();
			section_end();      
			if (verbose) System.out.println(s[i]);
		}
		section_sloc=s;
	}
	
	
	private Tile parser_bic_tile(){
		Tile t = new Tile();
		t.river = uint8();
		skip(1);
		t.resource  = uint32();
		t.image  = uint8();
		t.file  = uint8();
		skip(2);
		t.overlays  = uint8();
		t.terrain  = uint8();
		t.bonuses  = uint8();
		return t;
	}
	
	
	private  void    parser_section_tile( ) {
		
		int number = uint32();   
		Tile[] t=new Tile[number];       
		if (verbose) System.out.println("number of tiles : " + number);
		
		for (int i=0;i<number;i++)
		{
			section_begin(); 
			// place specific section code here ...
			t[i]= parser_bic_tile();      
			section_end();      
			//if (verbose) System.out.println(t[i]);
		}
		section_tile=t;
	}
	
	private  void  parser_section_game( ) {
		// bidon ?
		//parser_section_std();
		
		game = new Game();
		skip(4);
		section_begin(); 
		
		game.default_game_rules = uint32();
		game.default_victory_conditions = uint32();
		game.playable_civ = array_uint32();
		game.rules_set = uint32();	
		
		if (section_ver.is_PTW())
		{
		game.place_capture_units = uint32();
		game.auto_place_kings = uint32();
		game.auto_place_victory_locations = uint32();
		game.debug_mode = uint32();
		game.use_time_limit = uint32();
		game.base_time_unit = uint32();	
		game.start_month = uint32();
		game.start_week = uint32();
		game.start_year = uint32();
		game.minute_time_limit = uint32();
		game.turn_time_limit = uint32();
		game.turns_per_timescalepart = array_uint32(7);
		game.timeunits_per_turn = array_uint32(7);
		game.scenario_search_folders = civ_string(5200);
		}
		
		section_end();   
		
		if (verbose) System.out.println(game);
	}
	
	
	private  void  parser_section_cult( ) {
		
		if (section_ver.major<4) skip (8);
		//parser_section_std();
		
		int number = uint32();   
		Culture[] tab=new Culture[number];
		
		if (verbose) System.out.println("number of culture : " + number );
		
		
		for (int i=0;i<number;i++)
		{
			section_begin(); 
			// place specific section code here ...
			Culture x =new Culture();
			x.name = civ_string(64);
			x.chance_of_successful_propaganda = uint32();
			x.culture_ratio_percentage = uint32();
			x.culture_ratio_denominator = uint32();
			x.culture_ratio_numerator = uint32();
			x.initial_resistance_chance = uint32();
			x.continued_resistance_chance = uint32();
			section_end();      
			if (verbose) System.out.println(x);
			tab[i] = x ;
		}
		
		section_cult=tab;
		
	}
	
	
	
	private  void  parser_section_std( ) {
		
		int number = uint32();                  
		for (int i=0;i<number;i++)
		{
			section_begin(); 
			// place specific section code here ...
			section_end();                    
		}
		
	}
	
	private  void  parser_section_flav( ) {
		
		
		
		/*     TOP FLAV SECTION (Flavors)
		 4	char		"FLAV"
		 4	long		number of flavorgroups (1)  
		 For each flavorgroup:
		 4	long		number of flavors
		 For each flavor:
		 4   long		???
		 256 string		flavor name
		 4 long		number of flavors
		 For each flavor:
		 4 long		relation*/
		
		int number_flavorgroups = uint32();   
		for (int i=0;i<number_flavorgroups;i++)
		{
			int number_flavors = uint32();  
			for (int j=0;j<number_flavors;j++)
			{
				skip(4);
				skip(256);
				int number_xx = uint32();
				skip(4*number_xx);
			}       
		}
		
		
	}
	
	private  void  parser_section_good( ) {
		
		int number = uint32();   
		Good[] x=new Good[number];
		
		if (verbose) System.out.println("number of goods : " + number );
		
		
		for (int i=0;i<number;i++)
		{
			section_begin(); 
			// place specific section code here ...
			x[i]=new Good();
			x[i].name = civ_string(24);
			x[i].civilopedia_entry = civ_string(32);
			x[i].type = uint32();
			x[i].appearance_ratio = uint32();
			x[i].disappearance_probability = uint32();
			x[i].icon = uint32();
			x[i].prerequisite = uint32();
			x[i].food_bonus = uint32();
			x[i].shields_bonus = uint32();
			x[i].commerce_bonus = uint32();
			section_end();      
			if (verbose) System.out.println(x[i]);
		}
		section_good=x;
	}
	
	private  void  parser_section_city( ) {
		
		int number = uint32();   
		City[] x=new City[number];
		
		if (verbose) System.out.println("number of cities : " + number );
		
		
		for (int i=0;i<number;i++)
		{
			section_begin(); 
			// place specific section code here ...
			x[i]=new City();
			x[i].has_walls = bool8();
			x[i].has_palace= bool8();
			x[i].name= civ_string(24);
			x[i].owner_type= uint32();          
			int number_of_buildings = uint32();
			for (int j=0;j<number_of_buildings;j++)
			{
				int building_id= uint32();
				x[i].buildings.add(new Integer(building_id)); 
			}
			x[i].culture= uint32();
			x[i].owner= uint32();
			x[i].size= uint32();
			x[i].x= uint32();
			x[i].y= uint32();
			x[i].city_level= uint32();
			x[i].border_level= uint32();
			x[i].use_auto_name= uint32();
			section_end();      
			if (verbose) System.out.println(x[i]);
		}
		section_city=x;
	}
	
	private  void  parser_section_lead( ) {
		
		int number = uint32();   
		section_lead=new Leader[number];
		
		if (verbose) System.out.println("number of leaders : " + number );
		
		
		for (int i=0;i<number;i++)
		{
			section_begin(); 
			// place specific section code here ...
			Leader x =new Leader();           
			x.custom_civ_data = uint32();
			x.human_player= uint32();
			x.name = civ_string(32);
			skip(2*4);
			int number_of_different_start_unit = uint32();
			x.start_unit = new Leader.StartUnit[number_of_different_start_unit];
			for (int j=0;j<number_of_different_start_unit;j++)
			{
				x.start_unit[j] = new Leader.StartUnit();              
				x.start_unit[j].number= uint32();
				x.start_unit[j].type = uint32();               
			}            
			x.gender= uint32();			
			x.technology = array_uint32();
			x.difficulty= uint32();
			x.initial_era= uint32();
			x.gold= uint32();
			x.government= uint32();
			x.civ= uint32();
			x.color= uint32();   
			section_end();      
			if (verbose) System.out.println(x);
			section_lead[i]=x;
		}
		
	}
	
	private  void  parser_section_unit( ) {
		
		int number = uint32();   
		section_unit =new Unit[number];
		
		if (verbose) System.out.println("number of units : " + number );
		
		for (int i=0;i<number;i++)
		{
			section_begin(); 
			// place specific section code here ...
			Unit x=new Unit();
			x.id = i;
			x.name = civ_string(32);
			x.owner_type= uint32();
			x.experience_level= uint32();
			x.owner= uint32();
			x.prto= uint32();
			x.ai_strategy= uint32();
			x.x= uint32();
			x.y= uint32();
			if (section_ver.is_PTW())
				x.ptw_name = civ_string(57);
			if (section_ver.is_C3C())
				x.use_civilization_king= uint32();
			section_end();      
			//if (verbose) System.out.println(x);
			section_unit[i]=x;
		}
		
	}
	
	private  void  parser_section_govt( ) {
		
		int number = uint32();   
		section_govt=new Government[number];
		
		if (verbose) System.out.println("number of government : " + number );
		
		
		for (int i=0;i<number;i++)
		{
			section_begin(); 
			// place specific section code here ...
			Government x =new Government();
			
			
			x.default_type = uint32();
			x.transition_type = uint32();
			x.requires_maintenance = uint32();
			x.unknow_0 = uint32();
			x.standard_tile_penalty = uint32();
			x.standard_trade_bonus = uint32();
			
			x.name = civ_string(64);
			x.civilopedia_entry = civ_string(32);
			x.male_ruler_title_1 = civ_string(32);
			x.female_ruler_title_1 = civ_string(32);
			x.male_ruler_title_2 = civ_string(32);
			x.female_ruler_title_2 = civ_string(32);
			x.male_ruler_title_3 = civ_string(32);
			x.female_ruler_title_3 = civ_string(32);
			x.male_ruler_title_4 = civ_string(32);
			x.female_ruler_title_4 = civ_string(32);	
			
			x.corruption_and_waste = uint32();
			x.immune_to = uint32();
			x.diplomats_are = uint32();
			x.spies_are = uint32();
			
			int number2 = uint32();
			x.modifier = new Government.Modifier[number2];
			for (int j=0;j<number2;j++)
			{
				Government.Modifier y =new Government.Modifier();
				y.can_bribe = uint32();
				y.bribery = uint32();
				y.resistance = uint32();
				x.modifier[j]=y;
			}
		    
		    
		    x.hurrying = uint32();
		    x.assimilation_chance = uint32();
		    x.draft_limit = uint32();
		    x.military_police_limit = uint32();
		    x.ruler_title_pairs_used = uint32();
		    x.prerequisite_technology_index = uint32();
		    x.science_rate_cap = uint32();
		    x.worker_rate = uint32();
		    x.unknow_1 = uint32();
		    x.unknow_2 = uint32();
		    x.unknow_3 = uint32();
		    x.free_units = uint32();
		    x.free_units_per_town = uint32();
		    x.free_units_per_city = uint32();
		    x.free_units_per_metropolis = uint32();
		    x.cost_unit = uint32();
		    x.war_weariness = uint32();
		    
		    if (section_ver.is_C3C())
		    {
		    	x.xenophobic = uint32();
		    	x.force_resettle = uint32();
		    }
		    

			section_end();      
			if (verbose) System.out.println(x);
			section_govt[i] = x ;
		}
		
	
	}
	
	
	/////////////////////////////////////////////////////////////////
	
	public static InputStream my_fopen( String filename ) /*throws IOException*/ {
		InputStream in;
		try {
			//in = new FileInputStream(filename);
			in = new BufferedInputStream(
					new FileInputStream(filename) );     
			return in;    
		}
		catch (FileNotFoundException e) {
			// Gestion des exceptions de fichier non trouvé
			System.out.println("Une exception s'est produite !\n" 
					+ e.getMessage());
			/* System.out.println("\nAffichage de la pile :\n");
			 e.printStackTrace();*/
			System.exit(1);
			return null;
		}             
		
	}
	
	
	
	
	public static boolean is_compressed( String filename ) /*throws IOException*/ 
	{
		
		byte[] h = new byte[4];
		InputStream in ;     
		in = my_fopen(filename);
		try {
			in.read( h );
		}
		catch (IOException e) {
			// Gestion des exceptions d'entrée/sortie
		}       
		
		try {
			in.close();
		}
		catch (IOException e) {
			// Gestion des exceptions d'entrée/sortie
		}
		
		String header = new String(h);
		if ( header.substring(0,3).equals("BIC") )
		{
			System.out.println("detected uncompressed BIC");
			return false;
		}
		else
		{
			System.out.println("detected compressed BIC");			 
			return true;          
		}
		
		
	}
	
	
	
	public void bicload( String filename )  {
		
		
		System.out.println( "parsing file \"" + filename + "\"" );	
		
		if ( is_compressed(filename) )
		{
			bicload_compressed( my_fopen(filename) );
		}
		else
		{
			bicload_uncompressed( my_fopen(filename) );
		}	
		
	}
	
	
	
	public void bicload_compressed( InputStream in )  {
		
		bicload_uncompressed(  new Blast ( in ) );                  		 
	}
	
	public void bicload_uncompressed( InputStream in )  {
		
		this.in = in;
		
		
		
		parser_bic(-1);       
		
	}
	
	public LinkedList fromArray( Object[] tab )
	{
		LinkedList tmp = new LinkedList();
		if (tab==null) return tmp;
		for (int i=0;i<tab.length;i++)
		{
			tmp.add( tab[i] );		
		}
		return tmp;
	}
	
	public Civ_State get_state(String filename)  {
		bicload(filename);
		return get_state();
	}
		
		
	public Civ_State get_state()  {
		
		Civ_State state = new Civ_State();
		
		if (section_ver.is_C3C())
			state.version = Civ_State.VERSION_C3C;
		else if (section_ver.is_PTW())
			state.version = Civ_State.VERSION_PTW;
		else
			state.version = Civ_State.VERSION_VANILLA;
		
		// section game
		state.game = game;
		
		// section WMAP
		if (section_wmap!=null)
		{          
			state.world_w = section_wmap.width ;
			state.world_h = section_wmap.height;
		}

		// section world 
		state.world = section_tile;
		
		// other section	    	 			
		state.proto = section_prto;
		state.race = section_race;
		state.terrain= section_terr;
		state.building= section_bldg;
		state.tech= section_tech;		
		state.unit = fromArray(section_unit);
		state.city = fromArray(section_city);
		state.good = section_good;
		state.leader = section_lead;
		state.citizen = section_ctzn;
		state.culture = section_cult;
		state.government = section_govt;
		
		return state;
	}
	
	/**********************************/
	
	public static void main(String[] args) throws IOException {
		
		
		String civ3_dir  = "C:\\jeux\\civilization III\\";
		
		String filename = "demo.biq";
		//String filename = civ3_dir + "\\" + "civ3mod.bic";
		//String filename = civ3_dir + "\\" + "CIV3PTW\\civ3X.bix";
		
		
		
		if (args.length>=1){
			filename = args[0];
		}
		
		Civ_Loader sav_file = new Civ_Loader();
		sav_file.verbose = true;
		
		long t1 = System.currentTimeMillis(); 
		
		sav_file.bicload(  filename );
		
		long t2 = System.currentTimeMillis();
		
		System.out.println("t= " + (t2-t1) + " ms");
		
		/*Civ_State state = sav_file.get_state();
		 
		 for (int i=0;i<state.terrain.length;i++)
		 */
		
		System.out.println("bye bye");
		
		
	}
	
}
