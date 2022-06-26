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

package ressource;
import java.io.*;


import java.util.List;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.HashMap;

import state.Civ_State;
import state.Proto;



public class AnimationCache {
	
	final boolean debug = false;
	
	private List ini_files = new LinkedList();
	
	
	private HashMap map_proto_ini = new HashMap();
	private HashMap map_ini_anim = new HashMap();
	
	public String civ3_dir = null;
    public String civ3( String filename) {  return (civ3_dir + "\\" + filename);  }
    
    
    private Proto[] proto_civ3 = null; 
    
	public AnimationCache( String civ3_dir, Civ_State civ3mod ) {
        
		this.civ3_dir = civ3_dir;
		
		// pour "Translate method"
					      
        this.proto_civ3 = civ3mod.proto;
        
        scan_all_animation();
    }
    
   public void scan_all_animation() {
		
		System.out.println( "scanning CIV3 units ... " );	
		scan_all_animation( civ3("art\\units") );	
		
		System.out.println(  "scanning CIV3PTW units ... " );
		scan_all_animation( civ3("CIV3PTW\\Art\\Units") );

	}
	
	public void scan_all_animation( String path ) {
		
		File dir = new File(path);  		
		if (!dir.isDirectory()) return;
				  
	    String[] files = dir.list();
        for (int i=0; i<files.length; i++) {
            // Get filename of file or directory
            scan_animation( path, files[i] );
        }
	   
    
	}
	
	void scan_animation( String path, String name) {
	
	
		if (name.startsWith(".")) return;
	
		/*File dir = new File(path + "\\" + name);  		
		if (!dir.isDirectory()) return;
			*/	  	

		String filename = (path+"\\"+name+"\\"+name+".ini");
		if ((new File(filename)).exists())
		{
			scan_animation_( path, name, filename );	
			return;
		}	
	
	
		filename = (path+"\\"+name+"\\"+name+".INI");
		if ((new File(filename)).exists())
		{
			scan_animation_( path, name, filename );	
			return;
		}	
		
	
	}
	
	String to_unicode( String in )
	{
		return in;	
	}
	
	
	

	
	void scan_animation_( String path, String name, String filename ) {
		
		System.out.println( "scanning unit : " + to_unicode(name) );

		INI_File ini = new INI_File( filename );
		
		if (debug) System.out.println( "ini : " + ini );
	
		// on memorise l'information	
		ini.id = 0 ;
		ini.name = name;
		ini.path = path;
		
		ini_files.add(ini);	    
			
	}
	

	String name_civ3ptw( Proto p ) {
		return p.civilopedia_entry.substring(5).replace('_',' ');
		/* (map convert_underscore $  
 				drop 5 (proto_civilopedia_entry p)) */
 	}
 				
	String name_civ3( Proto p ) {
		return p.name;
	}


	

	public Civ_Animation lookup_animation( Proto p ) {

		Civ_Animation anim;
		INI_File ini = lookup_ini( p );
		
		// recherche animation déjà chargée
		anim = (Civ_Animation)map_ini_anim.get( ini );
		if (anim!=null)
			return anim;
			
		// chargement animation
		String filename = ini.path + "\\"
			+ ini.name + "\\"
			+ ini.s_default;
		
		System.out.println("loading : " + filename);			
		anim = new Civ_Animation( filename );	
		if (debug) System.out.println("anim = " + anim);			
		
		
		// modification delay
		anim.delay = ini.normal_speed;
		anim.id = p.id ;
		
		
		map_ini_anim.put( ini , anim );		
		return anim;			
	}
	
	

	public INI_File lookup_ini( Proto p ) {

		INI_File ini;
		
		// recherche ini
		ini = (INI_File)map_proto_ini.get( p );
		if (ini!=null)
			return ini;
			
		// recherche ini	
		ini = lookup_ini_( p );	
		map_proto_ini.put( p , ini );		
		return ini;
	}
			
			
	private INI_File lookup_ini_( Proto p ) {
		
		INI_File ini;
		
		System.out.println( "*** " + p.civilopedia_entry + " (" + p.name + ")"); 
				
		// recherche unité type "CIV3PTW"
		System.out.println( "lookup animation with CIV3PTW method ... " );
		String name = name_civ3ptw(p);
		for ( Iterator i= ini_files.iterator(); i.hasNext(); )
		{
			ini = (INI_File)i.next(); 	
			if (name.compareToIgnoreCase(ini.name)==0)
				return ini;
		}
		
		
		
			
		// recherche unité type "CIV3"
		System.out.println( "lookup animation with CIV3 method ... "  );
		for ( Iterator i= ini_files.iterator(); i.hasNext(); )
		{
			ini = (INI_File)i.next(); 	
			if (p.name.compareToIgnoreCase(ini.name)==0)
				return ini;
		}
		
		
		// recherche en utilisant proto_civ3
		// si pas de proto => on charge les informations 
		// à partir d'un fichier de reference
		System.out.println( "lookup animation with Translate method ... " );	
	
        
        Proto good_proto = null;
       	for ( int i=0; i<proto_civ3.length; i++ )
		{
			Proto x = proto_civ3[i];
			//if (name_civ3ptw(x).compareToIgnoreCase(name_civ3ptw(p))==0)
			if (x.civilopedia_entry.equals(p.civilopedia_entry))
			{	good_proto = x; break; }
		}
	
		if (good_proto!=null)
		{
			INI_File good_ini = null;
			for ( Iterator i= ini_files.iterator(); i.hasNext(); )
			{
				ini = (INI_File)i.next(); 	
				if (good_proto.name.compareToIgnoreCase(ini.name)==0)
					return ini;
			}
		}
	
	
		
		// dernière solution	
		System.out.println( "lookup animation with Rescue method ... " );		
		return (INI_File)ini_files.get(0);		
		
	}
	

	
	/**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

    	String civ3_dir = "D:\\jeux\\Civilization III";
    	
    	Civ_State civ3mod = Civ_Ressource.load_civ3mod(civ3_dir+"\\"+"civ3mod.bic" );
		
    	
    	AnimationCache cache = new AnimationCache(civ3_dir, civ3mod);

        Civ_Animation civ_anim= null;
        
        Proto p =  new Proto();
        p.id = 100;
        p.name = "Guerriersxx";
        p.civilopedia_entry = "xxxxxxx";
        
        for (int i=0;i<1;i++)
        {   
        	System.gc();
        	long t1 = My_Toolkit.nanoTime(); 
            civ_anim = cache.lookup_animation(p);
            long t2 = My_Toolkit.nanoTime();       
            //System.out.printf("time = %.3f ms\n" , ((double)(t2-t1) * 1e-6) );           
        }
        
        
        System.out.println("civ_anim : " + civ_anim);
    }	
}   

