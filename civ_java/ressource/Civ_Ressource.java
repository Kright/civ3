/*
 * Created on 4 juil. 2004
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

package ressource;

import gui.My_AnimationIcon;

import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.Icon;

import state.Civ_State;
import state.Proto;
import state.Unit;

/**
 * @author roudoudou
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Civ_Ressource {

    public BufferedImage[] surf;
    public BufferedImage[] palv;
    public Font font=null;
    public Civ_Animation anim_cursor;
    public AnimationCache anim_cache;
    public HashMap image_cache = new HashMap();
	
    
    public String civ3_dir = null;
    public int civ3_version;
    
    private PrintStream copy_log = null;
    
    
    public static String addslashes( String txt )
    { return (" \"" + txt +  "\""); }
    
    public void build_copy_log( String filename) 
    { 	
    	String temp = "C:\\jeux\\temp";
    	if (copy_log==null)
    	{
    		try {
    			
    			copy_log = new PrintStream("copy_log.bat");
    		} catch (FileNotFoundException e) {}
    		
    		copy_log.println("rmdir /S /Q " + addslashes(temp));
    		
    		copy_log.println("mkdir " + addslashes(temp));
    		
    		copy_log.println("xcopy /T /E "
    							+ " " + addslashes(civ3_dir)
								+ " " + addslashes(temp));
    		   		
    		copy_log.println("xcopy /Y /E"
    				+ " " + addslashes(civ3_dir + "\\art\\units") 
					+ " " + addslashes(temp + "\\art\\units") );
    		
    		
        	
    	}    	
    	copy_log.println("copy /Y"
    				+ " " + addslashes(civ3_dir + "\\" + filename)
					+ " " + addslashes(temp +  "\\" + filename));
    }
    
    public String civ3( String filename) { 
  	
    	//build_copy_log(filename);
		
    	return (civ3_dir + "\\" + filename);
    }
    
    
	public String civ3_ptw(String filename) {
		return (civ3("CIV3PTW\\" + filename));
	}
	
	public String civ3_c3c(String filename) {
		return (civ3("CIV3PTW\\" + filename));
	}
	
	public String civ3_use_version(String filename, int num ) 
	{
		// solution de repli
		if (num > civ3_version) 
			num = civ3_version;
		
		switch( num )
		{
			case Civ_State.VERSION_VANILLA:
				return civ3(filename);
			case Civ_State.VERSION_PTW:
				return civ3_ptw(filename);
			case Civ_State.VERSION_C3C:
				return civ3_c3c(filename);
		}
		return null;
	}
	
    public Civ_Ressource( String civ3_dir, int civ3_version ){
 
    		this.civ3_dir = civ3_dir;
    		this.civ3_version = civ3_version;
    }
    
    
    
    public void load_civ3_font( ){
    	

    	
    	//chargement fonte civ 3
		//font <- ttf_OpenFont (civ3 "LSANS.TTF") 14
		// note : copier le fichier LSANS.TTF dans le répertoire %windir%\Fonts
		//font = new Font( "Lucida Sans", Font.PLAIN, 14);
		// encore mieux :
		try {
			File file = new File(civ3("LSANS.TTF"));
			FileInputStream fis = new FileInputStream(file);
			font = Font.createFont(Font.TRUETYPE_FONT, fis);
			font = font.deriveFont(Font.PLAIN, 14);
			fis.close();
		} catch (Exception e) {}
		System.out.println("font : " + font);
    	
    }
    
    public void load_from_disk(  ){
    	
    
    	
    	
		//  chargement fonte civ 3
    	if (font==null)
    		load_civ3_font();
    	
    	
    	// chargement civ3mod
    	load_civ3mod();
		
		//		 chargement images
        {
        long t1 = System.currentTimeMillis();	
		chargement_images();
        long t2 = System.currentTimeMillis();	
        System .out.println("chargement images : " + (t2-t1) +" ms");
        //System.gc();
        }
	
     	// scan animation
	  	{
	  	long t1 = System.currentTimeMillis();
		anim_cache = new AnimationCache(civ3_dir, civ3mod);
		long t2 = System.currentTimeMillis();	
	        System .out.println("scan animation : " + (t2-t1) +" ms");
		//System.gc();
		}
	
	     
       
        
	
	
	// Fog Of War 
	surf[id_FogOfWar] = My_Toolkit.convert_fow(	
							My_Toolkit.load_and_convert( 
								civ3("art\\Terrain\\FogOfWar.pcx") ) );
	
	
	// chargement palettes  
	
	    palv = new BufferedImage[32];
	    
	    for (int i=0;i<32;i++)
	    {	
	    	java.text.DecimalFormat df = new java.text.DecimalFormat("00");	
	    	
			String filename = civ3 (
				 "art\\units\\Palettes\\"  + "ntp" + df.format(i) + ".pcx" );
				 
			palv[i] = My_Toolkit.load_and_convert( filename );
		}
							 		 

		
	
	// chargement animation curseur
	anim_cursor =new Civ_Animation( 
						civ3( "art\\Animations\\Cursor\\Cursor.flc" ) );
	

    
    }
    
    // art/Terain
    public static final int id_xtgc = 0;
    public static final int id_xpgc = 1;
    public static final int id_xdgc = 2;
    public static final int id_xdpc = 3;
    public static final int id_xdgp = 4;
    public static final int id_xggc = 5;
    public static final int id_wcso = 6;
    public static final int id_wsss = 7;
    public static final int id_wooo = 8;
    public static final int id_Mountains = 9;
    public static final int id_xhills = 10;
    public static final int id_Mountains_snow = 11;
    public static final int id_mountain_forests = 12;
    public static final int id_mountain_jungles = 13;
    public static final int id_tnt = 14;
    public static final int id_plains_forests = 15;
    public static final int id_grassland_forests = 16;
    public static final int id_tundra_forests = 17;
    public static final int id_deltaRivers = 18;
    public static final int id_mtnRivers = 19;
    public static final int id_floodplains = 20;
    public static final int id_resources = 21;
    public static final int id_StartLoc = 22;
    public static final int id_buttonsFinal = 24;
    public static final int id_FogOfWar = 25;
    public static final int id_roads = 26;
    public static final int id_irrigation = 27;
    public static final int id_irrigation_DESERT = 28;
    public static final int id_irrigation_PLAINS = 29;
    public static final int id_irrigation_TUNDRA = 30;
    public static final int id_TerrainBuildings = 31;
    
    
   
    
    // art/Cities 
    public static final int id_rAMER = 128;
    public static final int id_AMERWALL = 129;
    public static final int id_rEURO = 130;
    public static final int id_EUROWALL = 131;
    public static final int id_rROMAN = 132;
    public static final int id_ROMANWALL = 133;
    public static final int id_rMIDEAST = 134;
    public static final int id_MIDEASTWALL = 135;
    public static final int id_rASIAN = 136;
    public static final int id_ASIANWALL = 137;
    
    
    
      
  
    

 
    
    void chargement_image( int id, String s ){
        
        
        
    	BufferedImage img1 = My_Toolkit.load_and_convert_alpha(civ3(s));
 
        surf[id] = img1;
        
      
        // tempo pour laisser le système respirer ...
        try {  Thread.sleep(0); } 
        catch(InterruptedException e) {}
    }
        
    void chargement_images(){
    /*tmp <- (flip mapM) (zip [1..] art_filelist) 
			$ \(ix,(id_,s)) -> do
					ptr <- load_and_convert_alpha $ civ3 s
					let val = (100*ix) `div` (length art_filelist) 
					    txt = "loading pictures"
					-- progress_bar ref val txt
					return (id_,ptr)			
	*/
        
       
        
           surf = new BufferedImage[256];
          
           
        chargement_image(	id_xtgc ,             	"art\\Terrain\\xtgc.pcx"); 
        chargement_image(	id_xpgc ,              	"art\\Terrain\\xpgc.pcx"); 
        chargement_image(	id_xdgc ,              	"art\\Terrain\\xdgc.pcx");
        chargement_image(	id_xdpc ,              	"art\\Terrain\\xdpc.pcx"); 
        chargement_image(	id_xdgp ,              	"art\\Terrain\\xdgp.pcx"); 
        chargement_image(	id_xggc ,              	"art\\Terrain\\xggc.pcx");
        chargement_image(	id_wcso ,              	"art\\Terrain\\wcso.pcx");  
        chargement_image(	id_wsss ,            	"art\\Terrain\\wsss.pcx"); 
        chargement_image(	id_wooo ,              	"art\\Terrain\\wooo.pcx");  
        chargement_image(	id_Mountains ,         	"art\\Terrain\\Mountains.pcx"); 
        chargement_image(	id_xhills ,           	"art\\Terrain\\xhills.pcx"); 
        chargement_image(	id_Mountains_snow ,   	"art\\Terrain\\Mountains-snow.pcx");
        chargement_image(	id_mountain_forests , 	"art\\Terrain\\mountain forests.pcx"); 
        chargement_image(	id_mountain_jungles , 	"art\\Terrain\\mountain jungles.pcx"); 
        chargement_image(	id_tnt ,             	"art\\Terrain\\tnt.pcx"); // bonus grassland
        chargement_image(	id_plains_forests ,   	"art\\Terrain\\plains forests.pcx"); 
        chargement_image(	id_grassland_forests ,	"art\\Terrain\\grassland forests.pcx"); 
        chargement_image(	id_tundra_forests,   	"art\\Terrain\\tundra forests.pcx"); 
        chargement_image(	id_deltaRivers ,      	"art\\Terrain\\deltaRivers.pcx");
        chargement_image(	id_mtnRivers,        	"art\\Terrain\\mtnRivers.pcx"); 
        chargement_image(	id_floodplains ,      	"art\\Terrain\\floodplains.pcx"); 
        chargement_image(	id_resources,        	"art\\resources.pcx"); 
        //chargement_image(	id_StartLoc ,         	"art\\Terrain\\StartLoc.pcx"); 	
        chargement_image(	id_buttonsFinal ,    	"art\\buttonsFinal.pcx");
        //chargement_image(	id_FogOfWar ,		"art\\Terrain\\FogOfWar.pcx");
        chargement_image(	id_roads ,           	"art\\Terrain\\roads.pcx");
        chargement_image(	id_irrigation ,         "art\\Terrain\\irrigation.pcx");
        chargement_image(	id_irrigation_DESERT,   "art\\Terrain\\irrigation DESETT.pcx");
        chargement_image(	id_irrigation_PLAINS,   "art\\Terrain\\irrigation PLAINS.pcx");
        chargement_image(	id_irrigation_TUNDRA,   "art\\Terrain\\irrigation TUNDRA.pcx");
        chargement_image(	id_TerrainBuildings,    "art\\Terrain\\TerrainBuildings.PCX");
        
       
        // art\Cities 
        chargement_image(	id_rAMER ,           	"art\\Cities\\rAMER.PCX"); 
        chargement_image(	id_AMERWALL ,          	"art\\Cities\\AMERWALL.PCX"); 
        chargement_image(	id_rEURO ,           	"art\\Cities\\rEURO.PCX"); 
        chargement_image(	id_EUROWALL ,          	"art\\Cities\\EUROWALL.PCX"); 
        chargement_image(	id_rROMAN ,           	"art\\Cities\\rROMAN.PCX"); 
        chargement_image(	id_ROMANWALL ,          "art\\Cities\\ROMANWALL.PCX"); 
        chargement_image(	id_rMIDEAST ,           "art\\Cities\\rMIDEAST.PCX"); 
        chargement_image(	id_MIDEASTWALL ,        "art\\Cities\\MIDEASTWALL.PCX"); 
        chargement_image(	id_rASIAN ,           	"art\\Cities\\rASIAN.PCX"); 
        chargement_image(	id_ASIANWALL ,          "art\\Cities\\ASIANWALL.PCX"); 
        

	
    }
    
    void prechargement_animations( Civ_State state )
    {
    	if (state.unit==null)
    		return;
    	
    	for (Iterator i= state.unit.iterator();i.hasNext();)
	  	{
	  		Unit u = (Unit)i.next();
				
			Civ_Animation animation = this.anim_cache.lookup_animation(
				state.proto[u.prto] );
	        	
		}
    }
    
    
    /////
    
    
    private Civ_State civ3mod = null;
	
	
    public Civ_State load_civ3mod()
	{
		if (civ3mod!=null) return civ3mod;
		civ3mod = load_civ3mod(civ3("civ3mod.bic"));
		return civ3mod;
	}
	
    public static Civ_State load_civ3mod( String filename )
	{	
		Civ_Loader tmp = new Civ_Loader();
		tmp.bicload(filename);
		return tmp.get_state();
	}
	
    //////////
    
    public Icon icon_units_large( Proto p, int owner )
	{
		
		
		Civ_Animation animation = this.anim_cache
					.lookup_animation(p);
	
		
		return new My_AnimationIcon( animation, 
				2*animation.frames_per_direction,
				this.palv[owner] );
	}
    
    public BufferedImage load_image( String filename )
    {
    	BufferedImage img;
	 	
    	img = (BufferedImage)image_cache.get( filename );
		if (img!=null){
			System.out.println("cache hit : " + filename);
			return img;
		}
				
		img = My_Toolkit.load_and_convert_alpha(filename);
		image_cache.put( filename , img );	
			
    	return img;
    }
    
}
