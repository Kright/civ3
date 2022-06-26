/*
 * 
 * part of civ_java : civilization game toolkit.
 * ported from C to Java by julien eyries (julien.eyries@9online.fr)
 * see original author's comment below
 * 
 */


/*
SDL_flic - renders FLIC animations
Copyright (C) 2003 Andre de Leiradella

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

For information about SDL_flic contact leiradella@bigfoot.com

Version 1.0: first public release.
Version 1.1: fixed bug to set *error to FLI_OK when returning successfully from FLI_Open
             added function FLI_Reset to reset the animation to the first frame
Version 1.2: added function FLI_Skip to skip the current frame without rendering
             FLI_Animation->surface is now correctly locked and unlocked
             the rwops stream is now part of the FLI_Animation structure and is closed inside FLI_Close
             renamed FLI_Reset to FLI_Rewind
             added function FLI_Version that returns the library version
*/

package ressource;
import javax.imageio.*;
import java.io.*;
import java.awt.image.*;
import javax.imageio.spi.*;
import java.util.Iterator;
import java.awt.Point;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.IndexColorModel;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

class FLI_Frame{
    public int size, type, numchunks;
} 

class FLI_Chunk {
	public int size, type, index;
}


public class My_FLIC {
	
		final boolean debug = false;
		
        public int format, numframes, width, height, depth, delay;
        public int offframe1, nextframe, offnextframe;
        
        /* CIV III custom FLC by j.eyries*/
        public int      total_direction,frames_per_direction;
        public int      left_offset,top_offset;
        public int      original_width,original_height;
        public int      animation_time;
        
        public String toString(){
			return  "My_FLIC"
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
        
        /* rwops is where the animation is read from. */
        private InputStream in;
        private int pos = 0;
        
        private String error_name( int type )
    	{
    		switch (type)
    		{
    			case FLI_OK: return "OK";
    			case FLI_READERROR: return "READERROR";
    			case FLI_CORRUPTEDFILE: return "CORRUPTEDFILE";
    			case FLI_SDLERROR: return "SDLERROR";
    			case FLI_OUTOFMEMORY: return "OUTOFMEMORY";
    		}
    		return "UNKNOW" + " (" + type + ")" ;	
    	}
        
        private void error( int code )
        {
        	System.out.println("error : " + error_name(code));
        	System.exit(1);	
        }
        
        private void skip( int size) {  
        	/*pos += size;
      		try { in.skip(size); } 
      		catch (IOException e) {}*/
      		/* note de julien : on dirait que le in.skip est perturbé par les
      		retour à la ligne (caractère 13) */
        	for (int i=0;i<size;i++)
        		readu8();
		}
		
    	private int tell(){
			return pos;
		}
    	
		private void readbuffer( byte[] buffer) {			
			int n = 0;
			try { in.read(buffer); }
			catch (IOException e) {}
			pos += n;
     		if (n!= buffer.length)
                error(FLI_READERROR);
		}
		
		private int readu8() {    
			int c = 0;
			try { c= in.read(); }
			catch (IOException e) {}
			pos ++;
        	return (c & 255);
		}
		
		
		private int reads8() {    
			int c = readu8();
        	return ((c<<24)>>24);
		}
		
        private int readu16() {
            int c1 = readu8();
            int c2 = readu8();
            return ( (c1) | (c2 << 8));
        }
		
		private int readu32() {
            int c1 = readu16();
            int c2 = readu16();
            return ( (c1) | (c2 << 16));
        }
		
		
        
        /* surface is where the frames is rendered to. */      
        private byte[] dataArray=null;
        private byte[] color_r=null;
    	private byte[] color_g=null;
    	private byte[] color_b=null;
    	
    	private void read_pixel( int offset, int len )
		{
			for (int i=offset;i<offset+len;i++)
	        	dataArray[i] = (byte)readu8();
		}
		
		private void repeat_pixel( int offset, int len )
		{
			  byte c = (byte)readu8();
			  for (int i=offset;i<offset+len;i++)
	          	dataArray[i] = c;
		}
        
        /* Supported formats. */
		public static final int FLI_FLI = 0xAF11;
		public static final int FLI_FLC = 0xAF12;

		/* Error codes. */
		/* No error. */
		public static final int  FLI_OK            =0;
		/* Error reading the file. */
		public static final int  FLI_READERROR     =1;
		/* Invalid frame size (corrupted file). */
		public static final int  FLI_CORRUPTEDFILE =2;
		/* Error in SDL operation. */
		public static final int  FLI_SDLERROR      =3;
		/* Out of memory. */
		public static final int  FLI_OUTOFMEMORY   =4;
    
    
    	/* Library version. */
		private static final int FLI_MAJOR =1;
		private static final int FLI_MINOR =2;
		
		/* Chunk types. */
		private static final int FLI_COLOR256 =4;
		private static final int FLI_SS2      =7;
		private static final int FLI_COLOR    =11;
		private static final int FLI_LC       =12;
		private static final int FLI_BLACK    =13;
		private static final int FLI_BRUN     =15;
		private static final int FLI_COPY     =16;
		private static final int FLI_PSTAMP   =18;

      	public My_FLIC() {
        }
      
      
        
        /*
		Returns the library version in the format MAJOR << 16 | MINOR.
		*/
		public int FLI_Version() {
		        return FLI_MAJOR << 16 | FLI_MINOR;
		}
		
		/*
		Opens a FLIC animation and return a pointer to it. rwops is left at the same
		point it was before the the call. error receives the result of the call.
		*/
		public int FLI_Open(String filename) {
			
	        this.in = null;

	        try {
                this.in = new BufferedInputStream(
                                new FileInputStream(filename) );        
            }
            catch (FileNotFoundException e) {           
                 System.err.println( "file not found: " + filename);
                 return 0;
            }    
            
            /* Read the header. */
			readheader();
			if (debug) System.out.println("this : " + this );
			
            
			/* Create a buffer to hold the rendered frame. */
            dataArray = new byte[width*height];
      		color_r = new byte[256];
      		color_g = new byte[256];
      		color_b = new byte[256];
      		if (dataArray == null)
      			error(FLI_SDLERROR);
      		
      		/* Read the first frame. */
            offframe1 = tell();
           /* FLI_Frame frame = readframe();*/
            
            /* If it's a prefix frame, skip it. */
          /*  if (frame.type == 0xF100) {
                    skip( frame.size - 16);
                    offframe1 = tell();
                    numframes--;
            }
            */
            offnextframe = offframe1;
            nextframe = 1; 
  
      		return 0;
		}
		
	
		
		
		/*
		Closes the animation, closes the stream and frees all used memory.
		*/
		public void FLI_Close() {
			
		    try {  in.close(); }
	        catch (IOException e) {}
	        
		}
		
		/*
		Renders the next frame of the animation returning an int to indicate if it was
		successfull or not.
		*/
		public int FLI_NextFrame() {
		       	FLI_Frame frame;
		        FLI_Chunk chunk;
		        int    i;
		       
		        if (debug)
		        	System.out.println("FLI_NextFrame");
		        	
		        /* Seek to the current frame. */
		       // SDL_RWseek(flic->rwops, flic->offnextframe, SEEK_SET);
		        skip(offnextframe-tell());
		        
		        /* Read the current frame. */
		        frame =readframe();
		        
		        /* If it's a prefix frame, skip it. */
	            if (frame.type == 0xF100) {
	                    skip( frame.size - 16);
	                    frame =readframe();
	                   
	            }
		        
		        /* Read and process each of the chunks of this frame. */
		     
		     
		        for (i = frame.numchunks; i != 0; i--) {
		        		chunk = readchunk();
		            
		                switch (chunk.type) {
		                        case FLI_COLOR:
		                                handlecolor( chunk);
		                                break;
		                        case FLI_LC:
		                                handlelc( chunk);
		                                break;
		                        case FLI_BLACK:
		                                handleblack( chunk);
		                                break;
		                        case FLI_BRUN:
		                                handlebrun( chunk);
		                                break;
		                        case FLI_COPY:
		                                handlecopy( chunk);
		                                break;
		                        case FLI_COLOR256:
		                                handlecolor256( chunk);
		                                break;
		                        case FLI_SS2:
		                                handless2( chunk);
		                                break;
		                        case FLI_PSTAMP:
		                                /* Ignore this chunk. */
		                                break;
		                        default:
		                                error(FLI_CORRUPTEDFILE);
		                }
		        }
		      
		        /* Setup the number and position of next frame. If it wraps, go to the first one. */
		        if (++nextframe > numframes) {
		                offnextframe = offframe1;
		                nextframe = 1;
		        } else
		                offnextframe += frame.size;
		        
		        
		        return FLI_OK;
		}
		
		/*
		Rewinds the animation to the first frame.
		*/
		public int FLI_Rewind() {
			/*offnextframe = offframe1;
	        nextframe = 1;*/
	        return FLI_OK;
		}
		
		/*
		Skips the current frame of the animation without rendering it.
		*/
		public int FLI_Skip() {
			return 0;
		}


		private void readheader() {
			if (debug) System.out.println("readheader");
	        /* Skip size, we don't need it. */
	        skip( 4);
	        /* Read and check magic. */
	        format = readu16();
	        //if (flic->format != FLI_FLI && flic->format != FLI_FLC)
	        if (format != FLI_FLC)  
	                error(FLI_CORRUPTEDFILE);
	        /* Read number of frames, maximum is 4000 for FLI and FLC files. */
	        numframes = readu16();
	        if (numframes > 4000)
	                error(FLI_CORRUPTEDFILE);
	        /* Read width and height, must be 320x200 for FLI files. */
	        width = readu16();
	        height = readu16();
	        if (format == FLI_FLI && (width != 320 || height != 200))
	                error(FLI_CORRUPTEDFILE);
	        /* Read color depth, must be 8 for FLI and FLC files. */
	        depth = readu16();
	        if (depth != 8)
	                error(FLI_CORRUPTEDFILE);
	        /* Skip the flags, it doesn't look like it follows the specs. */
	        readu16();
	        /* Read the delay between frames. */
	        delay = (format == FLI_FLI) ? readu16() : readu32();
	        /* Skip rest of the header. */
	        // SDL_RWseek(flic->rwops, (flic->format == FLI_FLI) ? 110 : 108, SEEK_CUR);     
	        /* CIV III custom FLC by j.eyries*/      
	        skip(108-32);
	        total_direction= readu16();
	        frames_per_direction= readu16() + 1;
	        left_offset= readu16();
	        top_offset= readu16();
	        original_width= readu16();
	        original_height= readu16();
	        animation_time= readu16();
	        if (delay <= 0) delay = 20; // correction
	        if (total_direction > 0) // + ring frame
	        	numframes = total_direction * (frames_per_direction);  
	        skip(18);   
	}

	private int count_frame = 0;
	
	private FLI_Frame readframe(  ) {			
			if (debug)  System.out.println("readframe " + count_frame + " @ " + pos );
			count_frame ++;			
			FLI_Frame frame = new FLI_Frame();
	        /* Read the size of the frame, must be less than or equal to 64k in FLI files. */
	        frame.size = readu32();
	        if (format == FLI_FLI && frame.size > 65536)
	                error(FLI_CORRUPTEDFILE);
	        /* Read the type of the frame, must be 0xF1FA in FLI files or 0xF1FA or 0xF100 in FLC files. */
	        frame.type = readu16();
	        if (frame.type != 0xF1FA && (format == FLI_FLC && frame.type != 0xF100))
	                error(FLI_CORRUPTEDFILE);
	        /* Read the number of chunks in this frame. */
	        frame.numchunks = readu16();
	        /* Skip rest of the data. */
	        skip( 8);
	        if (debug)
	            System.out.println("frame : type=" + Integer.toString(frame.type,16) 
	            			+ " size=" + frame.size 
							+ " numchunks=" + frame.numchunks);       
	        return frame;
	}

	private String chunk_name( int type )
	{
		switch (type)
		{
			case FLI_COLOR256: return "COLOR256";
			case FLI_SS2: return "SS2";
			case FLI_COLOR: return "COLOR";
			case FLI_LC: return "LC";
			case FLI_BLACK: return "BLACK";
			case FLI_BRUN: return "BRUN";
			case FLI_COPY: return "COPY";
			case FLI_PSTAMP: return "PSTAMP";
		}
		return "UNKNOW"+ " (" + type + ")";	
	}
	
	
	

	

	private FLI_Chunk readchunk() {
		if (debug) System.out.println("readchunk");
		FLI_Chunk chunk = new FLI_Chunk();
        /* Read the chunk size. */
        chunk.size = readu32();
        /* Read the chunk type. */
        chunk.type = readu16();
        if (debug)
            System.out.println("chunk : type=" + chunk_name(chunk.type) + " size=" + chunk.size );   
        return chunk;
	}
    
	
	
	private void handlecolor( FLI_Chunk chunk ) {
        int       numpackets, index, count;

        /* Number of packets. */
        numpackets = readu16();
        /* Color index that will be changed. */
        index = 0;
        while (numpackets-- > 0) {
                /* Skip some colors. */
                index += readu8();
                /* And change some others. */
                count = readu8();
                if (count == 0)
                        count = 256;
                while (count-- > 0) {
                        /* r, g and b are in the range [0..63]. */
                        color_r[index] = (byte)( (readu8() * 255) / 63 );
                        color_g[index] = (byte)( (readu8() * 255) / 63 );
                        color_b[index] = (byte)( (readu8() * 255) / 63 );                     
                        index ++;
                }
        }
	}
	
	private void handlelc( FLI_Chunk chunk) {
        int    numlines, numpackets, size;
        int  line, p;
        byte[] buf = new byte[256];
    
        /* Skip lines at the top of the image. */
        line =  readu16() * width;
        /* numlines lines will change. */
        numlines = readu16();
        while (numlines-- > 0) {
                p = line;
                line += width;
                /* Each line has numpackets changes. */
                numpackets = readu8();
                while (numpackets-- > 0) {
                        /* Skip pixels at the beginning of the line. */
                        p += readu8();
                        /* size pixels will change. */
                        size = reads8();
                        if (size >= 0) {
                                /* Pixels follow. */
                        		read_pixel( p, size );
                        } else {
                                size = -size;
                                /* One pixel to be repeated follow. */
                                repeat_pixel( p, size );
                        }
                        p += size;
                }
        }
	}
	
	
	
	
	private void handleblack(FLI_Chunk chunk) {

        /* Fill the surface with color 0. */
		for (int i=0;i<dataArray.length;i++)
        	dataArray[i] = 0;
       
	}
	
	private void handlebrun(FLI_Chunk chunk) {
        int    numline, numcol, size;
        int  p, next;

        /* Begin at the top of the image. */
        p = 0;
        /* All lines will change. */
        for (numline=0; numline<height ; numline++) {
                /* The number of packages is ignored, packets run until the next line is reached. */
                readu8();
                // j.eyries : this is not correct !!
                // next = p + flic->surface->pitch;
               numcol = 0;
               while ( numcol < width ) {
                        /* size pixels will change. */
                        size = reads8();
                        if (size < 0) {
                                size = -size;
                                /* Pixels follow. */
                                read_pixel( p+numcol, size );                       
                        } else {
                                /* One pixel to be repeated follow. */
                        		repeat_pixel( p+numcol, size );
                        }
                        numcol += size;
                }
                p += width ;
        }
	}
	
	private void handlecopy(FLI_Chunk chunk) {      
        /* Read the entire image from the stream. */
		read_pixel( 0, width * height );
	}
	

	private void handlecolor256( FLI_Chunk chunk) {
	        int       numpackets, index, count;

	        if (format == FLI_FLI)
	                error( FLI_CORRUPTEDFILE);
	        /* Number of packets. */
	        numpackets = readu16();
	        /* Color index that will be changed. */
	        index = 0;
	        while (numpackets-- > 0) {
	                /* Skip some colors. */
	                index += readu8();
	                /* And change some others. */
	                count = readu8();
	                if (count == 0)
	                        count = 256;
	                while (count-- > 0) {
	                        /* r, g and b are in the range [0..255]. */
	                        color_r[index] = (byte)readu8();
	                        color_g[index] = (byte)readu8();
	                        color_b[index] = (byte)readu8();
	                        index ++;
	                }
	        }
	}
	
	private void handless2( FLI_Chunk chunk) {
        int   numlines, y, code, size;
        int p, c;


        if (format == FLI_FLI)
                error(FLI_CORRUPTEDFILE);
        /* numlines lines will change. */
        numlines = readu16();
        y = 0;
        while (numlines > 0) {
                /* Read the code. */
                code = readu16();
                switch ((code >> 14) & 0x03) {
                        case 0x00:
                                p =  width * y;
                                while (code-- > 0) {
                                        /* Skip some pixels. */
                                        p += readu8();
                                        size = reads8() * 2;
                                        if (size >= 0) {
                                                /* Pixels follow. */
                                                read_pixel( p,  size);
                                        } else {
                                                size = -size;
                                                readu8();
                                                /* One pixel to be repeated follow. */
                                                repeat_pixel(p, size);
                                        }
                                        p += size;
                                }
                                y++;
                                numlines--;
                                break;
                        case 0x01:
                                error(FLI_CORRUPTEDFILE);
                        case 0x02:
                                /* Last pixel of the line. */
                                p = width * (y + 1);
                                dataArray[p-1] = (byte)(code & 0xFF);
                                break;
                        case 0x03:
                                /* Skip some lines. */
                                y += (code ^ 0xFFFF) + 1;
                                break;
                }
        }
	}


	 
	 private BufferedImage build_BufferedImage()
	 {
        byte[] dst = new byte[width*height]; 
        if ( dst == null ){
 			System.out.println( "not enough memory for surface" );
 			System.exit(0);
 		}   		
        System.arraycopy( dataArray,0,dst, 0, width*height );

		return My_PCX.build_image_256_color( dst, width, height, color_r, color_g, color_b );
			
	 }
	 
	
	 
	 public BufferedImage[] load_(String filename)
	    {
	        
	 		FLI_Open(filename);
	 		
	 		BufferedImage[] img = new BufferedImage[numframes];
	 		
	 		for (int i=0;i<numframes;i++)
	 		{
	 			FLI_NextFrame();
	 			img[i] = build_BufferedImage();	
	 		}
	 		
	 		FLI_Close();
	 		 		
	        return img;
	    }
	 
	 public static BufferedImage[] load(String filename)
	    {
	        return (new My_FLIC()).load_(filename);
	    }
	        
    
   /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        
        //String filename = "D:\\jeux\\Civilization III\\art\\units\\Guerriers\\warriorAttackA.flc";
        //String filename = "D:\\jeux\\Civilization III\\art\\units\\Cavaliers\\HorsemanDefault.flc";
        String filename = "D:\\jeux\\Civilization III\\art\\units\\Hoplites\\hopliteDefault.flc";      	
       	//String filename = "D:\\jeux\\Civilization III\\art\\Animations\\Cursor\\Cursor.flc"; 
        
        System.out.println("loading : " + filename);
     
        BufferedImage[] img =null;
        
       
        for (int i=0;i<1;i++)
        {   
        	System.gc();
        	long t1 = System.currentTimeMillis();    
            img = My_FLIC.load( filename );
            long t2 = System.currentTimeMillis();
            System.out.println("time = " + (t2-t1) + " ms");           
        }
        
        for (int i=0;i<img.length;i++)
        {
        	File f = new File("D:\\temp\\img" + (10000+i) + ".png");          	
        	ImageIO.write(img[i], "png", f);
    	}
        
    }
}







