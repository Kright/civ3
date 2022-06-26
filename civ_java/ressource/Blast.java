/*
 * Blast.java
 *
 * Created on 12 avril 2004, 19:58
 *  
 * part of civ_java : civilization game toolkit.
 * ported from C to Java by julien eyries (julien.eyries@9online.fr)
 * see original author's comment below
 * 
 */

/* blast.c
 * Copyright (C) 2003 Mark Adler
 * For conditions of distribution and use, see copyright notice in blast.h
 * version 1.1, 16 Feb 2003
 *
 * blast.c decompresses data compressed by the PKWare Compression Library.
 * This function provides functionality similar to the explode() function of
 * the PKWare library, hence the name "blast".
 *
 * This decompressor is based on the excellent format description provided by
 * Ben Rudiak-Gould in comp.compression on August 13, 2001.  Interestingly, the
 * example Ben provided in the post is incorrect.  The distance 110001 should
 * instead be 111000.  When corrected, the example byte stream becomes:
 *
 *    00 04 82 24 25 8f 80 7f
 *
 * which decompresses to "AIAIAIAIAIAIA" (without the quotes).
 */

/*
 * Change history:
 *
 * 1.0	12 Feb 2003	- First version
 * 1.1	16 Feb 2003	- Fixed distance check for > 4 GB uncompressed data
 *
 * modified by Julien Eyries on 9 Jul 2003
 * converted to Java on 12 april 2004
 */

package ressource;
import java.io.*;
/*import java.util.LinkedList;
import java.lang.Runtime;
import java.lang.AssertionError;*/

/**
 *
 * @author  roudoudou
 */

   /*
 * Huffman code decoding tables.  count[1..MAXBITS] is the number of symbols of
 * each length, which for a canonical code are stepped through in order.
 * symbol[] are the symbol values in canonical order, where the number of
 * entries is the sum of the counts in count[].  The decoding process can be
 * seen in the function decode() below.
 */
class huffman {
    
     /* maximum code length */
    private static final int MAXBITS = 13;
    
    /* short ?? */
    public int[] count;	/* number of symbols of each length */
    public int[] symbol;	/* canonically ordered symbols */

    public huffman( int count_size, int symbol_size){
        this.count= new int[count_size];
        this.symbol= new int[symbol_size];
    }
    
/*
 * Given a list of repeated code lengths rep[0..n-1], where each byte is a
 * count (high four bits + 1) and a code length (low four bits), generate the
 * list of code lengths.  This compaction reduces the size of the object code.
 * Then given the list of code lengths length[0..n-1] representing a canonical
 * Huffman code for n symbols, construct the tables required to decode those
 * codes.  Those tables are the number of codes of each length, and the symbols
 * sorted by length, retaining their original order within each length.  The
 * return value is zero for a complete code set, negative for an over-
 * subscribed code set, and positive for an incomplete code set.  The tables
 * can be used if the return value is zero or positive, but they cannot be used
 * if the return value is negative.  If the return value is zero, it is not
 * possible for decode() using that table to return an error--any stream of
 * enough bits will resolve to a symbol.  If the return value is positive, then
 * it is possible for decode() using that table to return an error for received
 * codes past the end of the incomplete lengths.
 */
    public int construct( int[] rep )
    {
        int n = rep.length;
        int symbol_;		/* current symbol when stepping through length[] */
        int len;		/* current length when stepping through h->count[] */
        int left;		/* number of possible codes left of current length */
        int[] offs = new int[MAXBITS+1]; /*short offs[MAXBITS+1];*/	/* offsets in symbol table for each length */
        int[] length = new int[256]; /*short length[256];*/	/* code lengths */


        /* convert compact repeat counts into symbol bit length list */
        symbol_ = 0;
        for (int i=0;i<n;i++)
        {
            len = rep[i];
            left = (len >> 4) + 1;
            len &= 15;
            for (int j=0;j<left;j++)
            {
                length[symbol_++] = len;
            } 
        } 
        n = symbol_;

        /* count number of codes of each length */
        for (len = 0; len <= MAXBITS; len++)
            count[len] = 0;
        for (symbol_ = 0; symbol_ < n; symbol_++)
            (count[length[symbol_]])++;	/* assumes lengths are within bounds */
        if (count[0] == n)		/* no codes! */
            return 0;			/* complete, but decode() will fail */

        /* check for an over-subscribed or incomplete set of lengths */
        left = 1;				/* one possible code of zero length */
        for (len = 1; len <= MAXBITS; len++) {
            left <<= 1;			/* one more bit, double codes left */
            left -= count[len];		/* deduct count from possible codes */
            if (left < 0) return left;	/* over-subscribed--return negative */
        }					/* left > 0 means incomplete */

        /* generate offsets into symbol table for each length for sorting */
        offs[1] = 0;
        for (len = 1; len < MAXBITS; len++)
            offs[len + 1] = offs[len] + count[len];

        /*
         * put symbols in table sorted by length, by symbol order within each
         * length
         */
        for (symbol_ = 0; symbol_ < n; symbol_++)
            if (length[symbol_] != 0)
                symbol[offs[length[symbol_]]++] = symbol_;


            /*printf("****construct\n"); 
            printf("count = [");
            for (len = 0; len <= MAXBITS; len++)
                    printf("%d,", h->count[len] );
            printf("]\n\n");	
            printf("symbol = [");	     
            for (symbol = 0; symbol < n; symbol++)
                    printf("%d,", h->symbol[symbol] );
             printf("]\n\n");*/

        /* return zero for complete set, positive for incomplete set */
        return left;
    }


}
    
public class Blast extends InputStream {
    
       /* maximum code length */
    private static final int MAXBITS = 13;
    private static final int MAXWIN = 4096;


    private static final boolean debug = false;

    
    /* input state */
    private InputStream input;
    private int input_cnt;
    //unsigned left;		/* available input at in */
    private int bitbuf;			/* bit buffer */
    private int bitcnt;			/* number of bits in bit buffer */
    
    private int left(){
        int n=0;
        try{ n = input.available(); }
        catch (IOException e) {}
        return n;
    }

    /* input limit error return state for bits() and decode() */
    //jmp_buf env;
    
    /* work area */
    private int lit;		/* true if literals are coded */
    private int dict;		/* log2(dictionary size) - 6 */

	

    /* output state */
    /*private FileOutputStream output;*/
    private int next;		/* index of next write location in out[] */
    private boolean first;			/* true to check distances (for first 4K) */
    private byte[] out; /*unsigned char out[MAXWIN];*/	/* output buffer and sliding window */
	private int output_available = 0 ;  
	private int output_pos = 0 ;
	private byte[] output_buffer = null;
    private boolean output_eof = false;
    
    
 
/*
 * Return need bits from the input stream.  This always leaves less than
 * eight bits in the buffer.  bits() works properly for need == 0.
 *
 * Format notes:
 *
 * - Bits are stored in bytes from the least significant bit to the most
 *   significant bit.  Therefore bits are dropped from the bottom of the bit
 *   buffer, using shift right, and new bytes are appended to the top of the
 *   bit buffer, using shift left.
 */


    // retourne un bit
    private int getdata(){
	
	if (bitcnt==0){
                        int c=0;
                        try {
                            c= input.read();
                        } catch (IOException e) {}
                        if (debug) {
							if (((input_cnt+1) & 1023)==0){
								System.out.print(".");
								System.out.flush();
							}
                    	}
                        bitbuf = c;
                        bitcnt=8;
			input_cnt= input_cnt+1; 
        }

    	int s1 = bitbuf & 1;
	bitbuf = bitbuf >> 1;
        bitcnt = bitcnt -1;
	return s1;
    }
 

    private int bits( int need ){
        int b=0;
        for (int i=0;i<need;i++){
            //b = getdata() + 2*b;
            b |=  getdata()<<i;
        }
        return b;
    }
    
    

    // envoie un octet   
    private void putdata(int c) { 
    	 
        out[next] = (byte)(c&255);       
        if ((next+1)==MAXWIN){
            first = false; // 1er 4K passé
            // on vide le buffer de sortie
            // c'est 10x plus rapide que de sortir les octets un par un
            /*try {
            output.write(out);
            } catch (IOException e) {}*/
 
            flush_output( MAXWIN );
 
	    	
        }
        next =  ((next)+1) & (MAXWIN-1);	       
    }

    /*
 * Decode a code from the stream s using huffman table h.  Return the symbol or
 * a negative value if there is an error.  If all of the lengths are zero, i.e.
 * an empty code, or if the code is incomplete and an invalid code is received,
 * then -9 is returned after reading MAXBITS bits.
 *
 * Format notes:
 *
 * - The codes as stored in the compressed data are bit-reversed relative to
 *   a simple integer ordering of codes of the same lengths.  Hence below the
 *   bits are pulled from the compressed data one at a time and used to
 *   build the code value reversed from what is in the stream in order to
 *   permit simple integer comparisons for decoding.
 *
 * - The first code for the shortest length is all ones.  Subsequent codes of
 *   the same length are simply integer decrements of the previous code.  When
 *   moving up a length, a one bit is appended to the code.  For a complete
 *   code, the last code of the longest length will be all zeros.  To support
 *   this ordering, the bits pulled during decoding are inverted to apply the
 *   more "natural" ordering starting with all zeros and incrementing.
 */
    
 
        
        huffman litcode ;
        huffman lencode ;
        huffman distcode ;
        
    
       private void construct_all()
        {      
            System.out.println("Blast.construct_all()");
            
 
            
  //  static short litcnt[MAXBITS+1], litsym[256];	/* litcode memory */
 //   static short lencnt[MAXBITS+1], lensym[16];		/* lencode memory */
 //   static short distcnt[MAXBITS+1], distsym[64];	/* distcode memory */
 //   static struct huffman litcode = {litcnt, litsym};	/* length code */
 //   static struct huffman lencode = {lencnt, lensym};	/* length code */
 //   static struct huffman distcode = {distcnt, distsym};/* distance code */
              
              /* set up decoding tables (once--might not be thread-safe) */

                  /* bit lengths of literal codes */         
    final int[] litlen = {	
        11, 124, 8, 7, 28, 7, 188, 13, 76, 4, 10, 8, 12, 10, 12, 10, 8, 23, 8,
	9, 7, 6, 7, 8, 7, 6, 55, 8, 23, 24, 12, 11, 7, 9, 11, 12, 6, 7, 22, 5,
	7, 24, 6, 11, 9, 6, 7, 22, 7, 11, 38, 7, 9, 8, 25, 11, 8, 11, 9, 12,
	8, 12, 5, 38, 5, 38, 5, 11, 7, 5, 6, 21, 6, 10, 53, 8, 7, 24, 10, 27,
	44, 253, 253, 253, 252, 252, 252, 13, 12, 45, 12, 45, 12, 61, 12, 45,
	44, 173};
        
        
        /* bit lengths of length codes 0..15 */
    final int[] lenlen = {2, 35, 36, 53, 38, 23};
        /* bit lengths of distance codes 0..63 */
    final int[] distlen = {2, 20, 53, 230, 247, 151, 248};
    
      //  static short litcnt[MAXBITS+1], litsym[256];	/* litcode memory */
 //   static short lencnt[MAXBITS+1], lensym[16];		/* lencode memory */
 //   static short distcnt[MAXBITS+1], distsym[64];	/* distcode memory */
    
            litcode  = new huffman( MAXBITS+1,256 );
            lencode  = new huffman( MAXBITS+1,16 );
            distcode  = new huffman( MAXBITS+1,64  );
    
            litcode.construct( litlen );
            lencode.construct( lenlen );
            distcode.construct( distlen );

        }
       
    private int decode( huffman h )
    {
        int len;		/* current number of bits in code */
        int code;		/* len bits being decoded */
        int first;		/* first code of length len */
        int count;		/* number of codes of length len */
        int index;		/* index of first code of length len in symbol table */
        //int bitbuf;		/* bits from stream */
        int left;		/* bits left in next or left to process */
        int next;            /* next number of codes */

 
        code = first = index = 0;
        len = 1;
        //next = h->count + 1;
        next = 0 + 1;
       /* while (true)*/ {
            while (true) {               
                code |= getdata() ^ 1;	/* invert code */
                count = h.count[next];
                next ++;
                if (code < first + count) {	/* if length len, return symbol */                  
                    return h.symbol[index + (code - first)];
                }
                index += count;		/* else update for next length */
                first += count;
                first <<= 1;
                code <<= 1;
                len ++;
            }
           /* left = (MAXBITS+1) - len;
            if (left == 0) break;
            if (left > 8) left = 8;*/
        }
        //return -9;				/* ran out of codes */
    }

/*
 * Decode PKWare Compression Library stream.
 *
 * Format notes:
 *
 * - First byte is 0 if literals are uncoded or 1 if they are coded.  Second
 *   byte is 4, 5, or 6 for the number of extra bits in the distance code.
 *   This is the base-2 logarithm of the dictionary size minus six.
 *
 * - Compressed data is a combination of literals and length/distance pairs
 *   terminated by an end code.  Literals are either Huffman coded or
 *   uncoded bytes.  A length/distance pair is a coded length followed by a
 *   coded distance to represent a string that occurs earlier in the
 *   uncompressed data that occurs again at the current location.
 *
 * - A bit preceding a literal or length/distance pair indicates which comes
 *   next, 0 for literals, 1 for length/distance.
 *
 * - If literals are uncoded, then the next eight bits are the literal, in the
 *   normal bit order in th stream, i.e. no bit-reversal is needed. Similarly,
 *   no bit reversal is needed for either the length extra bits or the distance
 *   extra bits.
 *
 * - Literal bytes are simply written to the output.  A length/distance pair is
 *   an instruction to copy previously uncompressed bytes to the output.  The
 *   copy is from distance bytes back in the output stream, copying for length
 *   bytes.
 *
 * - Distances pointing before the beginning of the output data are not
 *   permitted.
 *
 * - Overlapped copies, where the length is greater than the distance, are
 *   allowed and common.  For example, a distance of one and a length of 518
 *   simply copies the last byte 518 times.  A distance of four and a length of
 *   twelve copies the last four bytes three times.  A simple forward copy
 *   ignoring whether the length is greater than the distance or not implements
 *   this correctly.
 */
 
    private void flush_output( int len )
    {
    	if (output_available>0)
        {
        	System.out.println("\nbuffer overflow !");
        	System.exit(1);          	
        }
    	System.arraycopy( out,0, output_buffer, 0, len );
		output_available = len ;	
		output_pos = 0;  	
    }
    
    public int read() throws IOException
    {  
    	int ret=0;
    	
    	
    	
    	while ( output_available == 0 )
    	{
    		if (output_eof)
    		{
    			return -1;	/* end of file */
    		}
    		
    		ret = decomp();	
    	
	    	if (ret<=0) /* error - end of file */	
	    	{
	    		/* on vide ce qui reste dans le buffer */	
	    		flush_output( next );
	    		output_eof = true;
	    		break;
	    	}	
	    		    	
	    
    	} 
    	
    	int c = -1; 
    	
    	if ( output_available > 0 )
    	{
    		c = (int)(output_buffer[output_pos] & 255);	
    		output_available --;	
    		output_pos ++;
    	}
   	 	
    	return c; 
    } 
    
    
    public int available() throws IOException
    {
    	if (output_eof 
    			&& output_available==0)
    		return 0;	
    	else
    		return 1;
    }
              
              
    public void close() throws IOException
    {
    	input.close();	
    }
           
           
  
	    	  	     
	    	  	         
    private int decomp()
    {
  
	/* decode literals and length/distance pairs */
        int ret;
 	
        int select = bits(1);
        if (select==1)
            ret = decode_length_distance();
        else 
            ret = decode_literals();
                
        return ret;   		
	
    }
   
     /* decode literals and length/distance pairs */      
    private int decode_literals(){
        //System.out.print(".");
        /* get literal and write it */
        int symbol = (lit!=0) ? decode(litcode) : bits(8);
        //out[next++] = symbol;
        putdata( symbol );
    	return 1;
    }
        
        

   
    private int decode_length_distance(){
	//System.out.print("x");
	int symbol;		/* decoded symbol, extra bits for distance */
        int len;		/* length for copy */
        int dist;		/* distance for copy */
        
        // base for length codes 
        final int[] base = {  3, 2, 4, 5, 6, 7, 8, 9, 10, 12, 16, 24, 40, 72, 136, 264};

        // extra bits for length codes 
        final int[] extra = { 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8 };

                
	// get length
	symbol = decode ( lencode );
        len = (base[symbol]) + bits(extra[symbol]);       
        // putStr $ "len = " ++ show(len) ++ "\n"
        if (len == 519)  		// end code 
        {
                if (debug) System.out.println("end code !");
                return 0;
        }
        
	// get distance 
        symbol = len == 2 ? 2 : dict;
        dist = decode(distcode) << symbol;
        dist += bits(symbol);
        dist ++;
        if (first && (dist > next)){
            
            System.err.println("distance too far back");
            return -3;		/* distance too far back */
        }
        // putStr $ "dist = " ++ show(dist) ++ "\n" 
                

        //  copy length bytes from distance bytes back	  	
        for (int i=0;i<len;i++)
        {
            int ix = (MAXWIN + (next) - dist) & (MAXWIN-1);
            putdata( out[ix] );
        }
        return 1;
    }
                        

        
    /** Creates a new instance of Blast */
    public Blast( InputStream in ) {
    	
    	this.input = in;
    	
        construct_all();
        
        if (debug)   
            System.out.println( "blast_init()");
             
        /* initialize input state */
        this.bitbuf = 0;
        this.bitcnt = 0;

        /* initialize output state */
        this.next = 0;
        this.first = true;       
        this.out = new byte[MAXWIN];  
        this.output_buffer = new byte[MAXWIN];
        
        
        // read header
		lit = bits(8);	
    	dict = bits(8);
    	if (debug) {
    		System.out.println( "lit= " + lit );
    		System.out.println( "dict= " + dict );
    	}	

    }
    



    
  
    
   
    
    public static void unpack( String filename_in, String filename_out ) throws IOException
    {   
    	InputStream in;
    	OutputStream out;
    	
    	
    	
        in = new Blast (
        		new BufferedInputStream(
                  new FileInputStream(filename_in)));     
    
        
        out = new BufferedOutputStream(
        		new FileOutputStream(filename_out)); 
       

        int c;
        
        /*InputStream in2 = new BufferedInputStream(
                new FileInputStream("tmp0.bic"));
        int pos = 0;*/

        while ((c = in.read()) != -1)
        {
        	out.write(c);
        	/*int c2 = in2.read();
        	if (c!=c2)
        	{
        		System.out.println("error at pos " + pos);
        		System.exit(0);
        	}
        	pos ++;*/
        }

		
        in.close();
		out.close();        
      
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        
        
         
        String filename_in = "demo.biq";
        String filename_out = "tmp.bic";
        
        if (args.length>=2){
        	filename_in = args[0];
        	filename_out = args[1];
        }
        
        long t1 = System.currentTimeMillis(); 
        
        Blast.unpack( filename_in, filename_out );
        
    	long t2 = System.currentTimeMillis();
        System.out.println("t= " + (t2-t1) + " ms");
        
        
    }
    
}
