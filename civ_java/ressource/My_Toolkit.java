/*
 * My_Toolkit.java
 *
 * Created on 30 mars 2004, 15:56
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
import java.awt.*;
import java.awt.image.*;

import javax.imageio.ImageIO;




/**
 *
 * @author  roudoudou
 */
public class My_Toolkit {
    
    /** Creates a new instance of My_Toolkit */
    public My_Toolkit() {
    }
    
    public static void error( String s ){
        System.out.println(s);
        System.exit(1);
    }   
  
  
    public static char to_hex( int c ){
       return (char)(c < 10 ? '0'+c : 'A'+c-10);
    }
    
   /* public static String sprintf(String format, Object... args) 
    {
    	ByteArrayOutputStream st1 = new ByteArrayOutputStream();
		PrintStream st2 = new PrintStream(st1);			
		st2.printf(format,args);
		return st1.toString();	 	
    }
    */
   
    
   
    
    public static long nanoTime() 
    {
    	
		return System.currentTimeMillis() * 1000000;	 	
    }
    
    public static String coord(int x, int y){
    	return ("(" + x + "," + y +")");
    }
    
    public static String array_int(int[] tab){
    	
    	if (tab==null)
    		return "null";
    	
    	StringBuffer buf = new StringBuffer();
    	
    	buf.append("[");   	
    	for (int i=0;i<tab.length;i++)
    	{
    		buf.append( Integer.toString(tab[i]) );
    		buf.append(",");
    	}
    	buf.append("]");
    	
    	return buf.toString();
    }
    
    
    
    public static String show_hex32( int x ) {
        char[] a = new char[8];
        int i;
        int c;
        for (i=0;i<8;i++){
            c = (x >> (4*(7-i))) & 0xf;
            a[i]=to_hex(c) ;
        }
        return (new String(a));
    }
	
    public static String show_hex8( int x ) {
        char[] a = new char[2];
        int i;
        int c;
        for (i=0;i<2;i++){
            c = (x >> (4*(1-i))) & 0xf;
            a[i]=to_hex(c) ;
        }
        return (new String(a));
    }
    
	public static void lookup_pixel( int[] dst_data, byte[] src_data, int[] src_rgb )
    {
    	for (int i=0;i<dst_data.length;i++)
    	{
    			dst_data[i]=src_rgb[src_data[i]&255];
    	} 	
    } 
    
	public static BufferedImage convert_to_direct(BufferedImage src) 
    {
    	/* conversion d'une image indexé en image "directe" */
        BufferedImage dst ;    
		dst = new BufferedImage(src.getWidth(),src.getHeight(), BufferedImage.TYPE_INT_ARGB);    
	
		IndexColorModel src_cm = (IndexColorModel)(src.getColorModel());
		int[] src_rgb = new int[256];
		src_cm.getRGBs(src_rgb);
			
        WritableRaster src_raster = src.getRaster();
        byte[] src_data = ((DataBufferByte)(src_raster.getDataBuffer())).getData();
        
        WritableRaster dst_raster = dst.getRaster();	      
        int[] dst_data = ((DataBufferInt)(dst_raster.getDataBuffer())).getData();  
        				              
        lookup_pixel( dst_data, src_data, src_rgb );         
        return dst;      
    }
    
    public static BufferedImage convert_to_compatible(BufferedImage src) 
    {
    	GraphicsConfiguration gc;
    	gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
      
        BufferedImage dst ;     
        dst = gc.createCompatibleImage(src.getWidth(),src.getHeight(),src.getColorModel().getTransparency());
                
        Graphics2D g2d = dst.createGraphics();
        g2d.drawImage(src,0,0,null);
        g2d.dispose();
				
		return dst;
    }
 
	
	
    public static BufferedImage load_and_convert ( String filename ) 
    {
	
		System.out.println("loading image " + filename ); 		
		BufferedImage image2 = My_PCX.load( filename );
		
		if (image2==null) 
			error( "file not found !");	
		
		return image2; 
	}   
	
	
	
    public static BufferedImage load_and_convert_alpha ( String filename )
    {       
        return convert_alpha(
        		load_and_convert(filename) );
    }
 
    
    public static BufferedImage convert_alpha( BufferedImage image )
    {
    	WritableRaster raster = image.getRaster();
        IndexColorModel cm = (IndexColorModel)image.getColorModel();
        
        byte[] r = new byte[256];
        byte[] g = new byte[256];
        byte[] b = new byte[256];
        cm.getReds(r);
        cm.getGreens(g);
        cm.getBlues(b);
        
        byte[] a = new byte[256];      	 
        for (int i=0;i<256;i++)
        	a[i] = (byte)255;   
        a[254]=(byte)0;
        a[255]=(byte)0;

        ColorModel cm2 = new IndexColorModel( 8, 256, r, g, b, a);
    
        return new BufferedImage( cm2,  raster, false, null);
    }
    
    public static BufferedImage convert_alpha_shadow( BufferedImage image )
    {
    	WritableRaster raster = image.getRaster();
        IndexColorModel cm = (IndexColorModel)image.getColorModel();
        
        byte[] r = new byte[256];
        byte[] g = new byte[256];
        byte[] b = new byte[256];
        cm.getReds(r);
        cm.getGreens(g);
        cm.getBlues(b);
        
        byte[] a = new byte[256];      	 
        for (int i=0;i<256;i++)
        	a[i] = (byte)255;   
        	
        for (int i=240;i<=255;i++)
        {
        	r[i] = (byte)0; 
        	g[i] = (byte)0; 
        	b[i] = (byte)0; 
        	a[i] = (byte)(((255-i)*16)&255);  
        }

        ColorModel cm2 = new IndexColorModel( 8, 256, r, g, b, a);
    
        return new BufferedImage( cm2,  raster, false, null);
    }
   
  
    
    public static BufferedImage convert_fow ( BufferedImage image )
    {
		WritableRaster raster = image.getRaster();
        IndexColorModel cm = (IndexColorModel)image.getColorModel();
        
        byte[] r = new byte[256];
        byte[] g = new byte[256];
        byte[] b = new byte[256];
        cm.getReds(r);
        cm.getGreens(g);
        cm.getBlues(b);
        
        byte[] a = new byte[256];      	 
        
        /*for (int i=0;i<16;i++)
	    {
        	a[i] = (byte)255;
	    } 
        */
        
        for (int i=0;i<16;i++)
	    {
			a[i] = (byte)(255-r[i] );
        	r[i] = 0;
			g[i] = 0;
			b[i] = 0;
	    } 
        
        for (int i=16;i<256;i++)
	    {
			a[i] = (byte)0;
			r[i] = 0;
			g[i] = 0;
			b[i] = 0;
			
	    } 

        ColorModel cm2 = new IndexColorModel( 8, 256, r, g, b, a);
    
        BufferedImage out = new BufferedImage( cm2,  raster, false, null);
        
        //return out;
        return convert_to_direct(out);
    }
    
    
    public static BufferedImage convert_fow_transparent ( BufferedImage image )
    {
		WritableRaster raster = image.getRaster();
        IndexColorModel cm = (IndexColorModel)image.getColorModel();
        
        byte[] r = new byte[256];
        byte[] g = new byte[256];
        byte[] b = new byte[256];
        cm.getReds(r);
        cm.getGreens(g);
        cm.getBlues(b);
        
        byte[] a = new byte[256];      	 
        
        for (int i=0;i<16;i++)
	    {
        	a[i] = (byte)255;
	    } 
        
        
        for (int i=16;i<256;i++)
	    {
			a[i] = (byte)0;
			r[i] = 0;
			g[i] = 0;
			b[i] = 0;
			
	    } 

        ColorModel cm2 = new IndexColorModel( 8, 256, r, g, b, a);
    
        BufferedImage out = new BufferedImage( cm2,  raster, false, null);
        
        return out;
    }
    
    

    
    
    public static void merge_alpha( int[] src_data, byte[] alpha_data,
    		int offset, int len )
    {
    	for (int i=0;i<len;i++)
     	{
    			src_data[offset+i] = (src_data[offset+i] & 0x00ffffff) 
									| ((255 - (int)alpha_data[i]) << 24);
     	} 	
    }
    
    public static void reset_alpha( int[] src_data, int alpha )
    {
    	alpha = alpha << 24;
    	for (int i=0;i<src_data.length;i++)
     	{
    			src_data[i] = (src_data[i] & 0x00ffffff) 
									| alpha;
     	} 	
    }
    
    public static BufferedImage load_2pcx_with_alpha( 
  			String filename_color, String filename_alpha )
  	{
  		BufferedImage color =  My_Toolkit.load_and_convert_alpha(filename_color);
	
  		BufferedImage alpha = My_Toolkit.load_and_convert(filename_alpha);
	  	
  		color = My_Toolkit.convert_to_direct( color );
    	
  		int w = alpha.getWidth();
  		int h = alpha.getHeight();
  			  		
		int[] src_data = ((DataBufferInt)(color.getRaster().getDataBuffer())).getData();  
             
        byte[] alpha_data = ((DataBufferByte)(alpha.getRaster().getDataBuffer())).getData();
               				               	       
  		My_Toolkit.reset_alpha( src_data, 0 );
        
  		My_Toolkit.merge_alpha( src_data, alpha_data, 0, w*h );
  		
  		return color;
  		//return My_Toolkit.convert_to_compatible( color );		
  	}
    
    
    ////////////////////////////////
    
    
    
    public static void main(String[] args) throws IOException {
    	
 
	  	String civ3_dir = "D:\\jeux\\Civilization III";
	  	
	  	// generation image
	  
	  	int w = 1152;
	  	int h = 576;
	  	BufferedImage image =new BufferedImage(w,
	  			 h, BufferedImage.TYPE_INT_RGB);
	  	
		BufferedImage fow  = My_Toolkit.convert_fow(	
				My_Toolkit.load_and_convert( 
					(civ3_dir + "\\" + "art\\Terrain\\FogOfWar.pcx") ) );
		
		
		
		Graphics2D g2d = (Graphics2D) image.createGraphics();
		
		
		g2d.setBackground(Color.green);
		g2d.clearRect(0, 0, w, h);
		g2d.setClip(null);
		g2d.drawImage(fow, 0, 0, null);
		
	  	ImageIO.write(image, "png", new File("d:\\temp\\fow.png"));


	  	System.out.println("done.");
    }
    
    
    
    ////////////////////////
    
    public static int get_field( int x, int start, int len) { 
    	int mask = ((1<<len)-1);
    	return ((x>>start) & mask); 
    }
    
    public static int set_field( int x, int start, int len, int z) { 
    	int mask = ((1<<len)-1);
    	return ((x & (~(mask<<start))) | ((z&mask)<<start)); 
    }
    
    public static int get_bit( int x, int start) { 
    	return get_field( x, start, 1) ; 
    }
    
    public static int set_bit( int x, int start, int z) { 	
    	return set_field( x, start, 1, z); 
    }
    
    
    
}
