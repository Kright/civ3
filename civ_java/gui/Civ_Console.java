/*
 * Created on 14 juil. 2004
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
import java.awt.Color;
import java.awt.Font;
import java.io.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.*;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JPanel;

import ressource.Civ_Config;
import ressource.Civ_Loader;
import ressource.Civ_Ressource;
import sun.io.ByteToCharConverter;


/**
 * @author roudoudou
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */



public class Civ_Console extends Civ_Screen 
	implements KeyListener {

	
	
	public PrintStream out;
		
	
	private int col = 0;
	private int lin = 0;
	// console_font :: (Ptr SDL_Surface),
	private Font font;
	//console_screen :: (Ptr SDL_Surface),
	private Graphics2D screen;
	private StringBuffer[] mem;

	// image "fonte" = 256 caractères sur 1 ligne
	
	private int ncol = 80;
	private int nlin = 25;

	//ncol = (wscreen) / wcol ;
	//nlin = (hscreen - 32) / hlin ;
	 	
	/*private int wcol = 6 ;
	private int hlin = 13 ;
	*/

	private int wcol = 10 ;
	private int hlin = 20 ;

	private Civ_Main civ_main;
	 
	public Civ_Console( 
			Civ_Main civ_main,
			Font font, 
			int wscreen, int hscreen ){
		
		super(); 
	
		this.civ_main = civ_main;
		this.font = font;
		
		
	
		setOpaque(true);
		setBounds(0,0,wscreen, hscreen);
		
		if (font==null)
		{
			font = new Font("Dialog",Font.PLAIN, 14);
		}
		
	
		FontMetrics fm = this.getFontMetrics(font);
		wcol = fm.charWidth('W') ;
		hlin = fm.getHeight(); 
		
	
		ncol = this.getWidth() / wcol;	
		nlin = this.getHeight() / hlin;
		
		
		mem = new StringBuffer[nlin];
		
	
		for (int i=0;i<mem.length; i++)
			mem[i] = new StringBuffer(ncol);
		
		
		addKeyListener(this);
		
		try { 
		out = new PrintStream(
				new Console_OutputStream(),
				true,
				"ISO-8859-1");
		} catch (UnsupportedEncodingException e) {}
	
		
		// test
		
		System.setOut( this.out );
		System.setErr( this.out );
		
		/*
		for (int i=0;i<100; i++){
			String s = "hello " + i + "\n";
			System.out.print(s);
		}
		*/
		
		
	}
	
	public void keyTyped(KeyEvent e) {
		//System.out.println("KEY TYPED: " + e);
	}

	
	public void keyPressed(KeyEvent e) {
		//System.out.println("KEY PRESSED: " + e);
	}
	
	public void keyReleased(KeyEvent e) {
		//System.out.println("KEY RELEASED: " + e);
		
		switch (e.getKeyCode()) {
			case KeyEvent.VK_ESCAPE :
				if (civ_main!=null)
					civ_main.afficher_ecran("previous");
				break;

		
		}
	}
	
	public void append( String s )
	{
		putStr(s);
		repaint();
	}
	
	static int min( int a, int b )
	{
		return (a<b?a:b);		
	}
	
	static int max( int a, int b )
	{
		return (a>b?a:b);		
	}
	
	void scroll()
	{
		StringBuffer temp = mem[0];
		
		for (int i=0;i<=(nlin-2);i++)
		{
			mem[i] = mem[i+1];
		}
				
		//mem[nlin-1]= new StringBuffer(ncol);
		temp.setLength(0);
		mem[nlin-1] = temp;
		
		
		lin = max( 0 , (lin-1) );

	}
	
	void newline(){
		
			//scroll if needed		
			if ((lin)==(nlin-1))
				scroll();
				
			// next line
			lin = lin + 1;		
	}
			
	
	void putChar( char c )
	{
		switch( c )
		{
			case '\r' : /*newline();*/ return;
			case '\n' : newline(); return;
			//case '\b' : backspace(); return;
			
		}
		
		//if ((c>=' ') && (c<='\127'))
		{

			
			
			mem[lin].append(c);
			
			col = min( (ncol-1), (col+1)  );
		}
		
	}

	
			
	void putStr( String s ){
		for (int i=0;i<s.length();i++)
			putChar(s.charAt(i));
		
	}
	

			
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);
		
		Graphics2D g2d = (Graphics2D) g;
		
		// couleurs civ 3		
		Color creme = new Color( 255, 247, 222 );
		Color orange = new Color( 255, 189, 107 );
		Color gris = new Color( 173, 173, 148 );
		Color noir = new Color( 0, 0, 0 );
		
		// fond		
		g2d.setBackground(creme);
		g2d.clearRect(0, 0, this.getWidth(), this.getHeight());

		// texte  	
		g2d.setColor(noir);
		g2d.setFont(this.font);
		for (int i=0; i<mem.length; i++ )
		{
			if (mem[i]==null) continue;
			int x = 10;
			int y = 10+i*hlin;
			g2d.drawString(mem[i].toString(), x, y );
		}
	}
	
	
	/////////// Civ_Screen
	
	  public JPanel get_content_pane()
	  {
	  	return this;
	  }
	    
	    public void swing_worker_construct()
	    {
	    	// SwingWorker.construct()
	    	
	    }
	    
	    private int count1 = 0;
	    private int count = 0;
	    
	    public void timer_run(){
	    	// javax.swing.Timer
	    	count1 ++;
	    	if (count1<10)	return;
	    	count1 = 0;	
	    	
	    	//System.out.println("tic " + count); count ++;
	    	
	    	this.repaint();
	    }
	    
	    public void flush(){ // BufferedImage.flush()
	    }
	
	/***********************/
	class Console_OutputStream extends OutputStream {

		
		private OutputStream log1=null;
		private PrintStream log2=null;
		
		
		
		public Console_OutputStream() {
			
			
			
			try {
				log1 = new FileOutputStream("log.txt");
			} catch (FileNotFoundException e) {}
			
			log2 = System.out;
			
		}

		public void write(int c) {
			
				if (log1!=null)
				{
					try {
					log1.write(c);
					} catch (IOException e) {}
				}
				
				if (log2!=null)
				{		
					log2.write(c);			
				}
				
				
				byte[] buf = new byte[1];
				buf[0] = (byte) c;
				String s = null;
				try { 
					s = new String(buf,"ISO-8859-1");
				} catch (UnsupportedEncodingException e) {}
				
				
				putStr(s);
				
		}
		
		public void flush() {
			
			try {
				log1.flush();
			} catch (IOException e) {}
			
			
			//repaint();	// voir timer_run
		}
			
		
		
	}



	///////////////////// Main
	
	
	public static void main(String[] args) throws IOException {
		
		
		
		String filename = "demo.biq";
		//String filename = "d:\\jeux\\civilization III\\civ3mod.bic";
		
		if (args.length>=1){
			filename = args[0];
		}
		
		
		
		Civ_Config config = new Civ_Config();
		//config.load();
		
		
		Civ_Ressource ressource ;
		ressource = new Civ_Ressource( config.civ3_dir, config.civ3_version );
		ressource.load_civ3_font();
		
		
		Civ_Frame frame ;
		frame = new Civ_Frame(config.wscreen, config.hscreen,
				config.fullscreen);

		frame.setVisible(true);
		

		Civ_Console console = new Civ_Console(  null, 
				ressource.font,
				config.wscreen, config.hscreen ); 
		
		
		
		frame.afficher_ecran( console );
		
		Civ_Loader loader = new Civ_Loader();
		loader.verbose = true;
		loader.bicload(  filename );	
		
		System.out.println("bye bye");
		
		
		
	}

	
	
}
