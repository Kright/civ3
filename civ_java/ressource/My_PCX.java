/*
 * part of civ_java : civilization game toolkit.
 * ported from C to Java by julien eyries (julien.eyries@9online.fr)
 * see original author's comment below
 */

/*
    SDL_image:  An example image loading library for use with SDL
    Copyright (C) 1999, 2000, 2001  Sam Lantinga

    This library is free software; you can redistribute it and/or
    modify it under the terms of the GNU Library General Public
    License as published by the Free Software Foundation; either
    version 2 of the License, or (at your option) any later version.

    This library is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
    Library General Public License for more details.

    You should have received a copy of the GNU Library General Public
    License along with this library; if not, write to the Free
    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

    Sam Lantinga
    slouken@libsdl.org
*/

/* $Id: IMG_pcx.c,v 1.6 2001/12/14 13:02:16 slouken Exp $ */

/*
 * PCX file reader:
 * Supports:
 *  1..4 bits/pixel in multiplanar format (1 bit/plane/pixel)
 *  8 bits/pixel in single-planar format (8 bits/plane/pixel)
 *  24 bits/pixel in 3-plane format (8 bits/plane/pixel)
 *
 * (The <8bpp formats are expanded to 8bpp surfaces)
 *
 * Doesn't support:
 *  single-planar packed-pixel formats other than 8bpp
 *  4-plane 32bpp format with a fourth "intensity" plane
 */



package ressource;
import javax.imageio.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.spi.*;
import java.util.Iterator;
import java.awt.Point;

import java.awt.image.Raster;
import java.awt.image.WritableRaster;




class PCXheader {
    
    /*Uint8 Manufacturer;
	Uint8 Version;
	Uint8 Encoding;
	Uint8 BitsPerPixel;
	Sint16 Xmin, Ymin, Xmax, Ymax;
	Sint16 HDpi, VDpi;
	Uint8 Colormap[48];
	Uint8 Reserved;
	Uint8 NPlanes;
	Sint16 BytesPerLine;
	Sint16 PaletteInfo;
	Sint16 HscreenSize;
	Sint16 VscreenSize;
	Uint8 Filler[54];*/
    
        int Manufacturer;
	int Version;
	int Encoding;
	int BitsPerPixel;
	int Xmin, Ymin, Xmax, Ymax;
	int HDpi, VDpi;
	byte[] Colormap;
	
	int NPlanes;
	int BytesPerLine;
	int PaletteInfo;
	int HscreenSize;
	int VscreenSize;
	
        /* 128 octets */
        
        private InputStream src;
        
        public PCXheader() {
           
        
        }
        
      
        int sint16() {
            return ( (uint16()<<16)>>16 );
        }
        
        int uint16() {
            int s1 = uint8();
            int s2 = uint8();
            return ( (s1) | (s2 << 8));
        }

        int uint8 () {
            int c=-1;
            try {
                c=src.read();
            } catch (IOException e) { }     
            return (c & 0xff);
        }
    
        void skip (int len)
        {
            for (int i=0;i<len;i++)
                uint8();        
        }
    
        byte[] read( int len )
        {
            byte[] b= new byte[len];
            try {
                src.read(b);
            } catch (IOException e) { }    
            return b;
        }
        
        public void load(  InputStream src )
        {
            this.src = src ;
            
           
           
            
            Manufacturer = uint8();
            Version = uint8();
            Encoding = uint8();
            BitsPerPixel = uint8();
            Xmin = sint16();
            Ymin = sint16();
            Xmax = sint16();
            Ymax = sint16();
            HDpi = sint16();
            VDpi = sint16();
            Colormap = read(48);
            skip(1);
            NPlanes = uint8();
            BytesPerLine = sint16();
            PaletteInfo = sint16();
            HscreenSize = sint16();
            VscreenSize = sint16();
            skip(54);
        
	
        }
         
      
	
}

public class My_PCX {
    
    final boolean debug = false;
    
      public My_PCX() {
           
        
        }
      
    private InputStream src;
    
    private int read()
    {
        int ch;
        int c=-1;
        try {
            c=src.read();
        } catch (IOException e) { }     
        ch =(int) (c & 0xff);

        if(c==-1) {
                System.out.println( "file truncated" );
                System.exit(1);
        }
        return ch;
    }
    
    private byte read_byte()
    {   
        return (byte)read();
    }
                                

    
    /* See if an image is contained in a data source */
    public static boolean IMG_isPCX(String filename)
    {
            boolean is_PCX;
            final int ZSoft_Manufacturer = 10;
            final int PC_Paintbrush_Version = 5;
            final int PCX_RunLength_Encoding = 1;
            PCXheader pcxh = new PCXheader();

            is_PCX = false;
            
            FileInputStream src = null;
            try {
                src = new FileInputStream(filename);        
            }
            catch (FileNotFoundException e) {           
                 System.err.println( "file not found: " + filename);
                 return false;
            }           
        
            pcxh.load(src);
         
            if ( (pcxh.Manufacturer == ZSoft_Manufacturer) &&
                 (pcxh.Version == PC_Paintbrush_Version) &&
                 (pcxh.Encoding == PCX_RunLength_Encoding) ) {
                    is_PCX = true;
            }
            
            try {  src.close(); }
            catch (IOException e) {}
            
            return(is_PCX);
    }

    public static BufferedImage load(String filename)
    {
        return (new My_PCX()).load_(filename);
    }
    
    private BufferedImage load_(String filename)
    {
        
        this.src = null;
        
    
          try {
                this.src = new BufferedInputStream(
                                new FileInputStream(filename) );        
            }
            catch (FileNotFoundException e) {           
                 System.err.println( "file not found: " + filename);
                 return null;
            }    
        
        BufferedImage img = IMG_LoadPCX();
        
        try {  src.close(); }
        catch (IOException e) {}
        
        return img;
    }
        
    
    /* Load a PCX type image  */
    private BufferedImage IMG_LoadPCX()
    {
	PCXheader pcxh = new PCXheader();
	int Rmask;
	int Gmask;
	int Bmask;
	int Amask;
	BufferedImage surface = null;
	int width, height;
	int y, bpl;
	//byte[] row, buf = null;	
	int bits, src_bits;
	
       
	if ( src==null ) {
		System.out.println( "src is null" );
		return null;
	}

        pcxh.load(src);
	if ( pcxh==null ) {
                System.out.println( "file truncated" );
		return null;
	}
        
	
	/* Create the surface of the appropriate type */
	width = (pcxh.Xmax - pcxh.Xmin) + 1;
	height = (pcxh.Ymax - pcxh.Ymin) + 1;
	Rmask = Gmask = Bmask = Amask = 0;
	src_bits = pcxh.BitsPerPixel * pcxh.NPlanes;
        bits = 0;
	if((pcxh.BitsPerPixel == 1 && pcxh.NPlanes >= 1 && pcxh.NPlanes <= 4)
	   || (pcxh.BitsPerPixel == 8 && pcxh.NPlanes == 1)) {
		bits = 8;
	} else if(pcxh.BitsPerPixel == 8 && pcxh.NPlanes == 3) {
		bits = 24;
                System.out.println( "24 bits - unsupported PCX format" );
		return null;
	} else {
		System.out.println( "unsupported PCX format" );
		return null;
	}
        
    
       
	

	bpl = pcxh.NPlanes * pcxh.BytesPerLine;
        
     byte[] pixels = new byte[width*height];
        
    /*    WritableRaster raster = Raster.createPackedRaster(DataBuffer.TYPE_BYTE,width,
                height, 1, 8, new Point(0,0) );
        */
        if ( pixels == null ){
		System.out.println( "not enough memory for surface" );
		return null;
	}

	byte[] buf = new byte[bpl];
	
	
	//row = surface->pixels;
	for ( y=0; y<height; ++y ) {
            
                if (debug) { System.out.print(".");
                    System.out.flush(); }
		/* decode a scan line to a temporary buffer first */
		int i, count = 0;
		int ch = 0;
		//Uint8 *dst = (src_bits == 8) ? row : buf;
		for(i = 0; i < bpl; i++) {
			if(count==0) {
                            
                                ch = read();
                                
                                
				if( (ch & 0xc0) == 0xc0) {
					count = ch & 0x3f;
                                        
                                         ch =  read () ;
		
				} else
					count = 1;
			}
                       
                     
                       
                         
			buf[i] = (byte)(ch);
                        
			count--;
		}

		if(src_bits <= 4) {
                        System.out.println( "<= 4 bits - unsupported PCX format" );
                        return null;
			/* expand planes to 1 byte/pixel */
			/*Uint8 *src = buf;
			int plane;
			for(plane = 0; plane < pcxh.NPlanes; plane++) {
				int i, j, x = 0;
				for(i = 0; i < pcxh.BytesPerLine; i++) {
					Uint8 byte = *src++;
					for(j = 7; j >= 0; j--) {
						unsigned bit = (byte >> j) & 1;
						row[x++] |= bit << plane;
					}
                                        
				}
			}*/
 		} 
                else if( src_bits == 8) {
                    
                    /*int x,  s;                    
                    for(x = 0; x < width; x++) {                       
                        s = ((int)buf[x]) & 255;
                        raster.setSample(x, y, 0, s);
                    }*/
                                    
                    /*raster.setDataElements(0,y, width, 1,  buf );*/
                    
                    /* solution la plus rapide !!! */
                    /*byte[] dst = ((DataBufferByte)(raster.getDataBuffer())).getData();      
                    System.arraycopy( buf,0,dst, y*width, width );*/
					System.arraycopy( buf,0,pixels, y*width, width );

                }
                else if(src_bits == 24) {
                
                        System.out.println( "24 bits - unsupported PCX format" );
                        return null;
			/* de-interlace planes */
			/*Uint8 *src = buf;
			int plane;
			for(plane = 0; plane < pcxh.NPlanes; plane++) {
				int x;
				dst = row + plane;
				for(x = 0; x < width; x++) {
					*dst = *src++;
					dst += pcxh.NPlanes;
				}
			}*/
		}

		//row += surface->pitch;
	}
        
        if (debug) System.out.println("");
                    
        
        ///ColorModel cm = null;
        
        byte[] r = null;
        byte[] g = null;
        byte[] b = null;
        
	if (bits == 8) {           
		int nc = 1 << src_bits;
		int i;
                r = new byte[nc];
                g = new byte[nc];
                b = new byte[nc];
                
		if(src_bits == 8) {
			byte ch;
			/* look for a 256-colour palette */
			do {
				
                                ch = read_byte() ;
			} while ( ch != 12 );

			for(i = 0; i < 256; i++) {
                                r[i] = read_byte()  ;
                                g[i] = read_byte()  ;
                                b[i] = read_byte()  ;
                               /* r[i] = (byte)(255-r[i])  ;
                                g[i] = (byte)(255-g[i])  ;
                                b[i] = (byte)(255-b[i])  ;*/
			}
		} else {
			for(i = 0; i < nc; i++) {
				r[i] = pcxh.Colormap[i * 3];
				g[i] = pcxh.Colormap[i * 3 + 1];
				b[i] = pcxh.Colormap[i * 3 + 2];
			}
		}
                
            //    cm = new IndexColorModel( bits, nc, r, g, b);
                
             
	}

        //surface = new BufferedImage( cm,  raster, false, null);
        surface = build_image_256_color( pixels, width, height,
    									r, g, b );
         
        if ( surface == null ){
		System.out.println( "not enough memory for surface" );
		return null;
	}
        
	return(surface);
    }
    
   
    public static BufferedImage build_image_256_color( byte[] pixels, int width, int height,
    									byte[] r, byte[] g, byte[] b) {
     
    
            // Create a data buffer using the byte buffer of pixel data.
            // The pixel data is not copied; the data buffer uses the byte buffer array.
            DataBuffer dbuf = new DataBufferByte(pixels, width*height, 0);
    
            // The number of banks should be 1
            int numBanks = dbuf.getNumBanks(); // 1
    
            // Prepare a sample model that specifies a storage 4-bits of
            // pixel datavd in an 8-bit data element
            
            int[] bandOffsets = new int[]{0};
             
            SampleModel sampleModel = new PixelInterleavedSampleModel(
            		DataBuffer.TYPE_BYTE, width, height, 1, width, bandOffsets) ;
            
            /*
            int bitMasks[] = new int[]{(byte)0xf};
            SampleModel sampleModel = new SinglePixelPackedSampleModel(
                DataBuffer.TYPE_BYTE, width, height, bitMasks);
    		*/
    		
            // Create a raster using the sample model and data buffer
            WritableRaster raster = Raster.createWritableRaster(sampleModel, dbuf, null);
    
    		IndexColorModel   cm = new IndexColorModel( 8, 256, r, g, b);
    		  
    		  
            // Combine the color model and raster into a buffered image
            return new BufferedImage(cm, raster, false, null);//new java.util.Hashtable());
        }


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        
        String filename = "D:\\jeux\\Civilization III\\art\\title.pcx";
     
        System.out.println("loading : " + filename);
     
        BufferedImage img=null ;
        
        for (int i=0;i<10;i++)
        {
            System.gc();
            long t1 = System.currentTimeMillis();    
            img = My_PCX.load( filename );
            long t2 = System.currentTimeMillis();
            System.out.println("time = " + (t2-t1) + " ms");           
        }
        
        File f = new File("D:\\temp\\myimage.png");
        ImageIO.write(img, "png", f);
        
    }
}

