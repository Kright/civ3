/*
 * Created on 15 sept. 2004
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
import javax.imageio.*;

import ressource.Civ_Loader;
import state.Civ_State;
import state.Tile;

import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.awt.Point;

import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

/**
 * @author roudoudou
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Civ_MiniMap {

	public static int build_rgb( int r, int g, int b)
	{
		int c;
		c = (r << 16) | (g<<8) | (b);
		return c;	
	}
	
	
	public static BufferedImage render( Civ_State state ) {
		
		/* image "directe" */
		
		/*int w = 200;
		int h = 100;
		*/
		
		int w = state.world_w/2;
		int h = state.world_h;
		
   
		int[] palette = new int[256];
				
		for (int i=0;i<256;i++){
			palette[i] = build_rgb( i,i,i );
		}
		
		//int plain = build_rgb( 132,105,66 ); 
		//int coast = build_rgb( 41,105,107 );
		
		// terrains de base
		int toundra = build_rgb( 222,234,238 );
		int desert = build_rgb( 246,242,189 );
		int plain = build_rgb( 213,170,82 );
		int grassland = build_rgb( 139,145,41 );	
		int coast = build_rgb( 32,157,164 );	
		int sea = build_rgb( 24,125,131 );
		int ocean = build_rgb( 0,72,82 );
		
		palette[0] = plain; //toundra; 	// xtgc
		palette[1] = plain; //grassland;	// xgpc
		palette[2] = plain; //desert;	// xdgc
		palette[3] = plain; //desert;	// xdpc
		palette[4] = plain; //grassland;	// xdgp
		palette[5] = plain;		// xggc
		palette[6] = coast;		// wcso
		palette[7] = sea; 		// wsss
		palette[8] = ocean;		// wooo
	
		int x,y;
		
		int[] precalc = new int[w*h];
		
		for (y=0;y<h;y++){
			for (x=0;x<w;x++){
				

			    Tile t = state.readTile( 2*x, y );
			    int c  = t.file  ; // entre 0 et 8
					
				//img.setRGB( x,y, palette[c&255] );
			    precalc[x+y*w] = palette[c&255];
			    
			}
		}
		
		int w2 = 4 * 230;
		int h2 = 4 * 110;		
		int x2, y2;
		
        
        BufferedImage img = new BufferedImage(w2, h2, BufferedImage.TYPE_INT_RGB);    
	
		
		double x1, y1;
		double step_x1, step_y1;
		
	
		step_x1 = (double)w  / (double)w2 ;
		step_y1 = (double)h  / (double)h2 ;
		
		for (y2=0;y2<h2;y2++){			
			for (x2=0;x2<w2;x2++){
				
				x1 = (double)x2 * step_x1;
				y1 = (double)y2 * step_y1;
				
				x = (int)(x1 * 64.0);
				y = (int)(y1 * 32.0);
				
			/*	// la partie entiere
				int xq = (x + 32) / 64;
				int yq = (y + 16) / 32;
				
				// le reste
				int xr = x - xq * 64;
				int yr = y - yq * 32;
				
				// sur une frontiere
				int is_x = (xq + yq) % 2;*/
				
				int k = select_tile(x, y, w) ;
				
				int c = 0;
				
				if (k<(w*h))
					c = precalc[k];
				
				
				img.setRGB( x2,y2, c );
				
			}
		}
		
		return img;
	}
	
	
	//Point select_tile(int mx, int my) {
	static int select_tile(int x1, int y1, int wtx) {

		// position relative de la souris
		/*int x1 = (mx) - (wscreen / 2) + viewx;
		int y1 = (my) - (hscreen / 2) + viewy;*/

		// la partie entiere
		int x2 = (x1 + 32) / 64;
		int y2 = (y1 + 16) / 32;

		// 4 quadrants
		/*
		 * f1 (x,y) | (x >= 0) && (y >= 0) = (1,0) | (x <= 0) && (y >= 0) =
		 * (0,-1) | (x <= 0) && (y <= 0) = (-1,0) | otherwise = (0,1)
		 */

		// rotation -45°
		//f2 (x,y) = f1 (x-y,x+y)
		// le reste
		int x3 = x1 - x2 * 64;
		int y3 = y1 - y2 * 32;

		// sur une frontiere
		int is_x = (x2 + y2) % 2;

		// correction
		//(dx,dy) = f2 (x3,-y3)
		int dx, dy;
		int ax, ay;
		int bx, by;
		int cx, cy;
		ax = x3;
		ay = -y3;
		bx = ax - ay;
		by = ax + ay;
		if ((bx >= 0) & (by >= 0)) {
			dx = 1;
			dy = 0;
		} else if ((bx <= 0) & (by >= 0)) {
			dx = 0;
			dy = -1;
		} else if ((bx <= 0) & (by <= 0)) {
			dx = -1;
			dy = 0;
		} else {
			dx = 0;
			dy = 1;
		}

		// selected tile
		int tx = x2 + is_x * dx;
		int ty = y2 + is_x * dy;

		//return new Point(tx, ty);
		return( tx + wtx*ty );
	}
	
	
	   /**********************************/
    
    public static void main(String[] args) throws IOException {
	
    		 
        //String filename = "demo.biq";
    	
    	String filename = "D:\\jeux\\Civilization III\\scenarios\\Terre (Gigantesque).bic";
    
        if (args.length>=1){
        	filename = args[0];
        }
        
        Civ_Loader sav_file = new Civ_Loader();               
        sav_file.bicload(  filename );      
        Civ_State state = sav_file.get_state();
        
        System.out.println("*********************");
        System.out.println("Minimap rendering ...");
        long t1 = System.currentTimeMillis();      
        BufferedImage minimap = Civ_MiniMap.render( state );       
        long t2 = System.currentTimeMillis();  	
        System.out.println("t= " + (t2-t1) + " ms");
        
        File f = new File("D:\\temp2\\minimap.png");
        ImageIO.write(minimap, "png", f);
        
        System.out.println("bye bye");
    }
}
