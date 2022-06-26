
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


package ressource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import state.Civ_State;

import com.ice.jni.registry.*;

/**
 * @author roudoudou
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */



public class Civ_Config {
	

	/************ configuration dans "civ_java.conf" **********/

	public String civ3_dir;
	public int wscreen,hscreen;
	public boolean fullscreen;
	public String scenario;
	public boolean verbose;
	public int civ3_version;
	    

	public Civ_Config() {
		//civ3_dir = ".";
		
		System.setProperty("swing.aatext", "true");
		
		civ3_dir = get_civ3_install_path();
		if (civ3_dir==null)
			civ3_dir = ".";
		
		wscreen = 1024; //800;
		hscreen = 768; //600;
		fullscreen = false;
		scenario = "built-in";
		verbose = false;
		
		check_civ3();
		civ3_version = Civ_State.VERSION_VANILLA;
		
		if (check_civ3_ptw())
			civ3_version = Civ_State.VERSION_PTW;
		
		/// debug
		System.out.println("autodetect config :" + this);
	}
	
	public String toString() {
		return "Civ_Config"
			+ " civ3_dir=" + civ3_dir
			+ " wscreen=" + wscreen
			+ " hscreen=" + hscreen
			+ " fullscreen=" + fullscreen
			+ " scenario=" + scenario
			+ " verbose=" + verbose
			+ " civ3_version=" + civ3_version
			;
	}

	public void load() {
		Properties applicationProps = new Properties();
		try {
			FileInputStream in = new FileInputStream("civ_java.conf");
			applicationProps.load(in);
			in.close();
		} catch (Exception e) {}
	
		String s;
		
		s = applicationProps.getProperty("civ3_dir");
		if (s!=null) civ3_dir = s;
			
		s = applicationProps.getProperty("wscreen");
		if (s!=null) wscreen = Integer.parseInt(s);
		
		s = applicationProps.getProperty("hscreen");
		if (s!=null) hscreen = Integer.parseInt(s);
		
		s = applicationProps.getProperty("fullscreen");
		if (s!=null) fullscreen = Boolean.parseBoolean(s);
		
		s = applicationProps.getProperty("scenario");
		if (s!=null) scenario = s;
		
		s = applicationProps.getProperty("verbose");
		if (s!=null) verbose = Boolean.parseBoolean(s);
		
		s = applicationProps.getProperty("civ3_version");
		if (s!=null) civ3_version = Integer.parseInt(s);
		
		
		/// debug
		System.out.println("loaded config : " + this);
		
	}
	
	
	
	public void save() {
		Properties applicationProps = new Properties();
		
		applicationProps.setProperty("civ3_dir", civ3_dir);
		applicationProps.setProperty("wscreen", Integer.toString(wscreen));
		applicationProps.setProperty("hscreen", Integer.toString(hscreen));
		applicationProps.setProperty("fullscreen", Boolean.toString(fullscreen));
		applicationProps.setProperty("scenario", scenario);
		applicationProps.setProperty("verbose", Boolean.toString(verbose));
		applicationProps.setProperty("civ3_version", Integer.toString(civ3_version));
		
		
		try {
			FileOutputStream out = new FileOutputStream("civ_java.conf");
			applicationProps.store(out, "--- this is the Civ_Java configuration file ---");
			out.close();
		} catch (Exception e) {}
	}

	
	
		// verifications
	
	public boolean check_civ3() {
		
		String file = "civ3mod.bic";
		
		// on recherche s'il y a un fichier "civ3mod.bic"
		// dans le répertoire civ3_dir
		boolean ret = ((new File( civ3_dir + "\\" + file)).exists());
		
		System.out.println("file " + file 
				+ (ret ? "" : " NOT") + " found in directory \""
				+ civ3_dir + "\"");
		
		return ret;
	}
	
	
	public boolean check_civ3_ptw() {
		
		String file = "CIV3PTW\\civ3X.bix";
		
		boolean ret = ((new File( civ3_dir + "\\" + file)).exists());
		
		System.out.println("file " + file 
				+ (ret ? "" : " NOT") + " found in directory \""
				+ civ3_dir + "\"");
		
		return ret;
	}
	
	
	public static String get_civ3_install_path()
	{
		String civ3_install_path = null;
		try {
		
			civ3_install_path = Registry.HKEY_LOCAL_MACHINE
				.openSubKey("SOFTWARE\\Infogrames Interactive\\Civilization III")
					.getStringValue("Install_Path");

			System.out.println("civ3_install_path=" + civ3_install_path);
		
		}
		catch (Exception e) { 
			System.out.println(e);
		}
		return civ3_install_path;
	}
	
	public static void main(String[] args) throws IOException {
	
		
		
		
		
		Civ_Config config = new Civ_Config();
		
		config.load();
			
		System.out.println(config);
		
		config.check_civ3();
		
	}
		
}


