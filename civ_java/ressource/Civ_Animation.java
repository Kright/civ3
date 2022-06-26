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
import java.awt.image.*;
import java.awt.Point;
import java.awt.Rectangle;
    
public class Civ_Animation {

	final boolean debug = true;
	
	
	public int id;
	
	public int format, numframes, width, height, depth, delay;
    public int total_direction,frames_per_direction;
    public int left_offset,top_offset;
    public int original_width,original_height;
    public int animation_time;
        		
	public BufferedImage[] images ;
			
	public Civ_Animation( String filename ) 
	{
			
		My_FLIC flic = new My_FLIC();
			
		images=flic.load_(filename);
		
		for (int i=0;i<images.length;i++)
    	{
    		images[i] = My_Toolkit.convert_alpha_shadow( images[i] );
    		//images[i] = My_Toolkit.convert_to_direct( images[i] );
    	}
		
		format=flic.format;
		numframes=flic.numframes;
		width=flic.width;
		height=flic.height;
		depth=flic.depth;
		delay=flic.delay;
    	total_direction=flic.total_direction;
    	frames_per_direction=flic.frames_per_direction;
    	left_offset=flic.left_offset;
    	top_offset=flic.top_offset;
    	original_width=flic.original_width;
    	original_height=flic.original_height;
    	animation_time=flic.animation_time;
		
   	}   
   	
   	public int calc_num_frame( int dir, int time ) {
	
		int n = dir*frames_per_direction 
				+ ((time / delay) % frames_per_direction);
		return n;
	}

	public Rectangle calc_rect_frame( int x, int y ) {
		
		int dx = x - (original_width / 2) + (left_offset);
		int dy = y - (original_height / 2) + (top_offset);   
		
		return new Rectangle(dx,dy,width,height);
	}
		 
	    
      

	public String toString(){
		return  "Civ_Animation"
			+ " format=" + Integer.toString(format, 16) 
			+ " numframes=" + numframes
			+ " width=" + (width) 
			+ " height=" + (height)
			+ " depth=" + (depth)
			+ " delay=" + (delay)
			+ " total_direction=" + (total_direction)
			+ " frames_per_direction=" + (frames_per_direction)
			+ " left_offset=" + (left_offset)
			+ " top_offset=" + (top_offset)
			+ " original_width=" + (original_width)
			+ " original_height=" + (original_height)
			+ " animation_time=" + (animation_time);
    }
	
 	/**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

		// chargement animation
		String filename = "D:\\jeux\\Civilization III\\art\\units\\Guerriers\\warriorAttackA.flc";
			
		System.out.println("loading : " + filename);	
		Civ_Animation a = new Civ_Animation( filename );
		System.out.println("a : " + a);
    }	
}
