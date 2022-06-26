
package biqdecompressor;

import java.io.*;
/**
 * 
 * @author chiefpaco
 */
public class Decom {

    private static final int MAXDICT = 4096;
    private static final int BUFFER = 4096;


    private int header;
    private int dictionary_bits;
    private int dictionary_size;

    private char[] queue = new char[MAXDICT];
    private int queue_head;
    private int queue_tail;
    private int queue_count;

    private char[] bitstream = new char[BUFFER*8];
//    private byte[] bBuffer = new byte[BUFFER];
    private byte[] buffer = new byte[BUFFER];
    private int inbuffer;
    private int buffer_offset;
    private BufferedInputStream d_bin; // Buffered version of in
    private BufferedOutputStream d_bout;  // Buffered version of out

/*    public void Decom ( File cFile) {
        decompress(cFile);
    }
*/

//    #define get_bit(x, n)   (((x) >> (n)) & 1)
    private int get_bit(int x, int n)
    {
        return ((x >> n) & 1);
    }

//    #define set_bit(x, n)   ((x) |= (1 << (n)))
    private int set_bit(int x, int n)
    {
        return (x |= (1 << n));
    }


    public void decompress(String comFile, String outputFile) {
        int p, x;
        int  length, offset;
        //char ch;
        char ch;
        try{
        //in = new FileInputStream (comFile);
        d_bin = new BufferedInputStream(new FileInputStream(comFile));

        //out = new FileOutputStream ( "temp");
        d_bout = new BufferedOutputStream(new FileOutputStream(outputFile));

        inbuffer = 0;
        queue_init();

        header = getbits(8);
        dictionary_bits = getbits(8);
        dictionary_size = 64 << dictionary_bits;

        if (header != 0) {
            System.err.println("error: lookup table for literal bytes not yet supported :(\n");
            d_bin.close();
//            in.close();
            d_bout.close();
//            out.close();
            return;
        }

        if (dictionary_size > MAXDICT) {
            System.err.println("error: dictionary too large (%d). increase MAXDICT (%d) and recompile\n");
            d_bin.close();
//            in.close();
            d_bout.close();
//            out.close();
            return;
        }

        for (;;) {
            x = getbits(1);

            if (x == 0) {
                ch = (char)getbits(8);

                //fputc(ch, stdout);
                try {
                d_bout.write(ch);
                queue_push(ch);
                } catch (IOException e) {
                    System.err.println("IOException" + e.getMessage());
                    return;
                }
            }
            else {
                length = getcopylength();

                if (length == 519)
                    break;

                if (length != 2)
                {
                    offset = getcopyoffsethighorder() << dictionary_bits;
                    offset |= getbits(dictionary_bits);
                }

                else
                {
                    offset = getcopyoffsethighorder() << 2;
                    offset |= getbits(2);
                }

                offset %= queue_count;

                if (queue_tail >= offset)
                    offset = queue_tail - offset;
                else
                    offset = dictionary_size - (offset - queue_tail);

                while (length-- != 0) {
                    //fputc(queue[offset], stdout);
                    try{
                    d_bout.write(queue[offset]);
                    queue_push(queue[offset++]);
                    } catch (IOException e) {
                        System.err.println("IOException" + e.getMessage());
                        return;
                    }

                    if (offset == dictionary_size)
                        offset = 0;
                }
            }
        }
        d_bin.close();
//        in.close();
        d_bout.close();
//        out.close();
    } catch (FileNotFoundException e) {
        System.err.println("FileNotFountException" + e.getMessage());
        return;
    }
      catch (IOException e) {
        System.err.println("IOException" + e.getMessage());
        return;
    }

    }

    private void queue_init() {
        queue_head = 0; queue_tail = -1; queue_count = 0;
    }

    private void queue_push(char key) {
        if (queue_count == dictionary_size)
            queue_pop();

        if (++queue_tail == dictionary_size)
            queue_tail = 0;

        queue[queue_tail] = key; queue_count++;
    }

    private void queue_pop() {
        queue_head++; queue_count--;

        if (queue_head == dictionary_size)
            queue_head = 0;
    }

    private int getbits(int n) {
        int i, j, x;

        if (inbuffer == 0) {
            //x = fread(buffer, 1, BUFFER, stdin);
            try{
            x = d_bin.read(buffer, 0, BUFFER);
            } catch (IOException e) {
                System.err.println("IOException" + e.getMessage());
                return 0xFF;
            }

            if (x == 0)
                return (0xFFFF);

        /*    fprintf(stderr, ":: read = %d\n", x);*/
/*            for (int bufcnt = 0; bufcnt < BUFFER; bufcnt++)
            {
                buffer[bufcnt] = (char)bBuffer[bufcnt];
            }
*/
            for (i=0; i<x; i++)
                for (j=0; j<8; j++) {
                    bitstream[(i << 3) + j] = (char)((int)buffer[i] & 1);
                    buffer[i] >>= 1;
                }

            inbuffer = (x << 3); buffer_offset = 0;
        }

        if (inbuffer < n) {
            i = inbuffer;

            x = getbits(i);

            return(x | (getbits(n-i) << i));
        }

        x = 0;

        for (i=0; i<n; i++)
            if (bitstream[buffer_offset + i] != 0)
                x = set_bit(x, i);

        inbuffer -= n; buffer_offset += n;

        return (x);
    }

    private int getreversebits(int n) {
        int i;
        int x, r = 0;

        x = getbits(n);

        for (i=0; i<n; i++)
            if (get_bit(x, n-i-1) != 0 )
                r = set_bit(r, i);

        return (r);
    }

    private int getcopylength() {
        if (getbits(1) != 0) {
            if (getbits(1) != 0) return 3;
            else            return ((getbits(1) != 0) ? 2 : 4);
        }

        if (getbits(1) != 0) {
            if (getbits(1) != 0) return 5;
            else            return ((getbits(1) != 0) ? 6 : 7);
        }

        if (getbits(1) != 0) {
            if (getbits(1) != 0) return 8;
            else            return ((getbits(1) != 0)? 9 : 10 + getbits(1));
        }

        if (getbits(1) != 0) {
            if (getbits(1) != 0) return (12 + getbits(2));
            else                return (16 + getbits(3));
        }

        if (getbits(1) != 0) {
            if (getbits(1) != 0) return (24 + getbits(4));
            else                return (40 + getbits(5));
        }

        if (getbits(1) != 0)
            return (72 + getbits(6));

        if (getbits(1) != 0)
            return (136 + getbits(7));

        return (264 + getbits(8));
    }

    private int getcopyoffsethighorder() {
        int x;

        if (getbits(1) != 0) {
            if (getbits(1) != 0) return 0x00;

            if (getbits(1) != 0) return (0x02 - getbits(1));
            else            return (0x06 - getreversebits(2));
        }

        if (getbits(1) != 0) {
            x = getreversebits(4);

            if (x != 0) return (0x16 - x);
            else   return (0x17 - getbits(1));
        }

        if (getbits(1) != 0)
            return (0x27 - getreversebits(4));

        if (getbits(1) != 0)
            return (0x2F - getreversebits(3));

        return (0x3F - getreversebits(4));
    }
}