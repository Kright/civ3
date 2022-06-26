/*
 * Created on 28 mars 2005
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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author roudoudou
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public
class INI_File {
	
	final boolean debug = false;

	public int id = -1;
	public String name ="";
	public String path ="";
	public int normal_speed=0;
	public int fast_speed=0;
	public String s_blank="";
	public String s_default="";
	public double t_blank=0.0;
	public double t_default=0.0;
		
	private BufferedReader in = null;
	
	static Pattern p_section = Pattern.compile("\\[(.*)\\]") ;
	static Pattern p_parameter = Pattern.compile("(.*)=([ \\S]*).*");

	public INI_File( String filename ) {
        
    
     	try {
            this.in = new BufferedReader(
                            new FileReader(filename) );        
        }
        catch (FileNotFoundException e) {           
             System.err.println( "file not found: " + filename);
             System.exit(0);
        }  
   
		parse_section( "Speed" ); 
		normal_speed = parse_parameter_int( "Normal Speed" );
		fast_speed = parse_parameter_int( "Fast Speed" );
	
		parse_section( "Animations"  ); 
		s_blank = parse_parameter_string( "BLANK" );
		s_default = parse_parameter_string( "DEFAULT" );
	
		parse_section( "Timing" ); 
		t_blank = parse_parameter_double( "BLANK" );
		t_default = parse_parameter_double( "DEFAULT" );
		
	   	try {  in.close(); }
        catch (IOException e) {}

	}
	
	private String readLine()
	{
		String s="";
		try	{ s = in.readLine(); }
		catch (IOException e) {}
		if (debug) System.out.println("ligne : "+ s); 
		return s;
	}
	  
	private void parse_section( String name ){
	
	/*many (noneOf "[")
	char '['
	string name
	char ']'
	end_of_line  */
		
		//Pattern p = Pattern.compile("\\["+name+"\\]");
		
		while(true)
		{
			String s=readLine();
			Matcher m = p_section.matcher(s);
			if (m.matches())
			{	
				if (debug) System.out.println("parse_section, trouve : "+  m.group(1)); 
				if (name.equals(m.group(1)))
					return;
			}
		}
	
		
	}
	  
	private int parse_parameter_int( String name ){
	
	/*	string name
	char '='
	x <- parser_int
	end_of_line
	return x	  */
	
		//Pattern p = Pattern.compile(name+"=([0-9]+)");	

		return Integer.parseInt(
				parse_parameter_string(name));	
	}
	
	private double parse_parameter_double( String name ){
	
	/*	string name
	char '='
	x <- parser_double
	end_of_line
	return x  */
	
		//Pattern p = Pattern.compile(name+"=([\\+\\-0-9.]+)");	
		
		return Double.parseDouble(
				parse_parameter_string(name));
	

	}
	
	private String parse_parameter_string( String name ){
	
	/*	string name
	char '='
	s <- many (noneOf "\n")
	end_of_line
	return s	  */
	
		//Pattern p = Pattern.compile(name+"=(.*)");	
		
		while(true)
		{
			String s=readLine();
			Matcher m = p_parameter.matcher(s);
			if (m.matches())
			{
				if (debug) System.out.println("parse_parameter, trouve :"
								+ " param=" + m.group(1)
								+ " value=" + m.group(2)); 
				if (name.equals(m.group(1)))
					return m.group(2);	
			}
			
		}
		
		//return "";
	}
	
	public String toString()
	{
		return "INI_File"
			+ " id=" + id
			+ " name=" + name 
			+ " path=" + path 
			+ " normal_speed=" + normal_speed
			+ " fast_speed=" + fast_speed
			+ " s_blank=" + s_blank
			+ " s_default=" + s_default
			+ " t_blank=" + t_blank
			+ " t_default=" + t_default;
			
	}
	
	/**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        
        String filename = "D:\\jeux\\Civilization III\\art\\units\\Guerriers\\Guerriers.ini";
     
        System.out.println("loading : " + filename);
     
       
        INI_File ini= null;
        
        for (int i=0;i<10;i++)
        {   
        	System.gc();
        	long t1 = My_Toolkit.nanoTime(); 
            ini = new INI_File( filename );
            long t2 = My_Toolkit.nanoTime();       
            //System.out.printf("time = %.3f ms\n" , ((double)(t2-t1) * 1e-6) ); 
                
        }
        
        
        System.out.println("ini : " + ini);
    }	

}


