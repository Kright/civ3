/*
 * Created on 4 juil. 2004
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
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import javax.swing.*;

/**
 * @author roudoudou
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Civ_Frame extends JFrame
	implements ActionListener, KeyListener {

	public boolean exit_on_escape = true;
	public final boolean verbose = true;
	int wscreen;
	int hscreen;
	boolean isUndecorated = false;
	
	private Civ_Screen current_screen=null;
	
	public javax.swing.Timer timer;
	final int timer_tick = 100; // multiple de 25 ms
	boolean enable_timer = true;
	public int time = 0;
	
	public Civ_Frame( int wscreen, int hscreen, boolean fullscreen){
		super(); 
		
	
		
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.wscreen = wscreen;
		this.hscreen = hscreen;

		
		this.setSize(wscreen, hscreen);
		this.setTitle("civ_java");
		this.setResizable(false);

		GraphicsEnvironment env;
		GraphicsDevice device;
		env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		device = env.getDefaultScreenDevice();
		DisplayMode oldDisplayMode = device.getDisplayMode();

		if (oldDisplayMode.getWidth() == wscreen
				&& oldDisplayMode.getHeight() == hscreen) {
			isUndecorated = true;
			
		}
		this.setUndecorated(isUndecorated);

		
		if (fullscreen) {
			// switch to fullscreen
			switch_to_fullscreen();
			//System.exit(0);
		}

		//this.getContentPane().setLayout(new java.awt.FlowLayout());
		this.getContentPane().setLayout(null);
	
		
		/*Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension windowSize = this.getSize();
		this.setLocation(Math.max(0,(screenSize.width -windowSize.width)/2), 
		                   Math.max(0,(screenSize.height-windowSize.height)/2));
		*/
	
		/*JPanel ecran_null = new JPanel();
		ecran_null.setPreferredSize(new Dimension(wscreen, hscreen));
		this.getContentPane().add(ecran_null);	
		*/
		
		//this.validate();
		//this.pack();
		this.setVisible(true);
		
		
		if (verbose) System.out.println("size = " + this.getSize());
		  
		if ( isUndecorated==false )
		{
		    Insets insets = this.getInsets();
		    if (verbose) System.out.println("insets = " + insets);
		    this.setSize(	this.getWidth() + insets.left + insets.right,
		    				this.getHeight() + insets.top + insets.bottom); 	
		    if (verbose) System.out.println("size = " + this.getSize());
		}
		
		
		if (enable_timer)
		{
			timer = new javax.swing.Timer(timer_tick, this);
			timer.start();
		}
		
	}
	
	String to_string(DisplayMode dm) {
		return "DisplayMode " + dm.getWidth() + "x" + dm.getHeight() + " "
				+ dm.getBitDepth() + " bpp" + " " + dm.getRefreshRate() + " Hz";
	}
	
	void switch_to_fullscreen() {
		// switch to fullscreen

		GraphicsEnvironment env;
		GraphicsDevice device;

		env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		device = env.getDefaultScreenDevice();

		DisplayMode oldDisplayMode = device.getDisplayMode();

		System.out.println("current display mode : "
				+ to_string(oldDisplayMode));

		if (!device.isFullScreenSupported()) {
			System.out.println("warning : full-screen not supported.");
			return;
		}

		this.setUndecorated(true);
		device.setFullScreenWindow(this);

		// on ne change rien si la taille est correcte
		if ((oldDisplayMode.getWidth() == wscreen)
				&& (oldDisplayMode.getHeight() == hscreen))
			return;

		if (!device.isDisplayChangeSupported()) {
			System.out.println("warning : display change not supported.");
			return;
		}

		DisplayMode newDisplayMode = null;
		DisplayMode[] dm = device.getDisplayModes();
		for (int i = 0; i < dm.length; i++) {
			if ((dm[i].getWidth() != wscreen) || (dm[i].getHeight() != hscreen)
					|| (dm[i].getBitDepth() < 15)) {
				continue;
			}

			System.out.println("possible mode : " + to_string(dm[i]));

			if (newDisplayMode == null) {
				newDisplayMode = dm[i];
				continue;
			}

			if (dm[i].getRefreshRate() > newDisplayMode.getRefreshRate()) {
				newDisplayMode = dm[i];
				continue;
			}
		}

		if (newDisplayMode == null)
			return;

		System.out.println("switching to : " + to_string(newDisplayMode));
		device.setDisplayMode(newDisplayMode);

	}
	
	
	///////////////////////////////////
	


	
	
	
	  private void afficher_ecran_(final Civ_Screen screen ) {
	  	
	  	
				
		screen.setOpaque(true);
		setContentPane(screen);
		
		
		repaint();
		validate();
						
		setVisible(true);		
		screen.requestFocusInWindow();
		
		if (current_screen!=null)
			current_screen.flush(); // free VRAM ?
		
		current_screen = screen;
	  }
	
	  public void afficher_ecran(final Civ_Screen screen ) {
		
		if (screen == null) 
		{
			System.out.println("null screen, exitting.");
			System.exit(1);
			return;
		}
		final Civ_Frame _this_ = this;
		
		if (screen.has_been_constructed)
		{
			//Schedule a job for the event-dispatching thread:
		    //creating and showing this application's GUI.
		    javax.swing.SwingUtilities.invokeLater(new Runnable() {
		        public void run() {
		        	afficher_ecran_( screen );
		        }
		    });
			
		}
		else
		{
		    final SwingWorker worker = new SwingWorker() {
		        
	
		        public Object construct() {
		            //screen.swing_worker_construct_if_needed();
		          	if (screen.has_been_constructed)
		        		return null;
		        	
		          	screen.swing_worker_construct();
		        	
		          	screen.has_been_constructed = true;
		        	
		          	screen.addKeyListener(_this_);
		          	
		            return null; //return value not used by this program
		        }
	
		        //Runs on the event-dispatching thread.
		        public void finished() {	        	
		        	afficher_ecran_( screen );	
		        }		    };
	    	    
		    worker.start(); 
		}
	}
	
	public void actionPerformed(ActionEvent evt) {
		//...Update the progress bar...

		if (!enable_timer)
		{
			return;
		}
		
		if (current_screen!=null)
			current_screen.requestFocusInWindow();
		
	

		// redraw everything needed
		if (current_screen!=null)			
			current_screen.timer_run();

		// increment time
		time += timer_tick;

		/* thread is done */
		/*if (false) {
			timer.stop();
			//...Update the GUI...
		}*/
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
				//civ_main.afficher_ecran("previous");
				if (exit_on_escape) {
					System.out.println("exit_on_escape");
					//System.exit(1);
					this.setVisible(false);
					this.dispose();
				}
				
				break;

		
		}
	}
	
	/**********************************/
	
	public static void main(String[] args) throws IOException {
		
		
		
		Civ_Frame frame ;
		frame = new Civ_Frame( 1024, 768, false );

		//frame.show();
		
		
		System.out.println("bye bye");
		
		
	}
	
}
