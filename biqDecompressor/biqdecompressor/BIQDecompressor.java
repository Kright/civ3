
package biqdecompressor;

/*
 *
This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 *
 **/

import java.io.File;
import java.io.IOException;

/**
 * Puts a command-line interface on Chief Paco's decompression code.
 * This program can be used stand-alone, or called via the command line from
 * other programs.
 * 
 * @author Quintillus
 */
public class BIQDecompressor
{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        if (args.length == 0)
        {
            System.out.println("Usage Information");
            System.out.println("  java -jar BIQDecompressor [inFile] [outFile]");
            System.out.println("  ");
            System.out.println(" inFile (REQ)  - The BIQ file that needs to be decompressed");
            System.out.println(" outFile (OPT) - The output for the uncompressed file.  Should not be the same"
                             + "                 as inFile.  If not provided, ._tmp.biq will be used.");
            return;
        }
        
        File file = new File(args[0]);
        String outFile = "._tmp.biq";
        if (args.length == 2)
            outFile = args[1];
        Decom d = new Decom();
        try{
            d.decompress(file.getCanonicalPath(), outFile);
            System.out.println("Decompression successful");
        }
        catch(java.lang.StackOverflowError e)
        {
            //logger.error("Stack overflow error on decompression",e);
            //JOptionPane.showMessageDialog(null, "Stack overflow error on decompression.  This likely indicates a corrupt BIQ file.", "Stack Overflow",JOptionPane.ERROR_MESSAGE);
            //return false;
        }
        catch(IOException e)
        {
            
        }
    }
}
