/*
 * Civ_Draw.java
 *
 * Created on 4 avril 2004, 17:48
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
import java.awt.image.*;
import javax.swing.*;

import ressource.Civ_Ressource;
import ressource.My_Toolkit;
import state.Civ_State;

import java.awt.*;
import java.awt.event.*;


/**
 * 
 * @author roudoudou
 */

public class Civ_Panel extends Civ_Screen
		implements
			MouseListener,
			MouseMotionListener
			 {

	final boolean debug = true;

	final int timer_tick = 100; // multiple de 25 ms
	final boolean do_animation = true;

	public int wscreen, hscreen;
	public int my_id;
	public final int wtile = 128;
	public final int htile = 64;

	protected Civ_Ressource ressource;
	protected Civ_State state;
	protected Civ_Draw draw;
	
	public int current_unit = -1;
	
	private int viewx;
	private int viewy;

	Graphics2D g2d = null;

	private BufferedImage background = null;
	
	public boolean need_redraw = true;
	private boolean need_redraw_background = true;
	private boolean need_redraw_animation = true;
	

	int mouse_x, mouse_y;
	int mouse_startx, mouse_starty;
	int view_startx, view_starty;

	private int time = 0;
	
	public boolean  enable_mousemotion = false, enable_tilevalue = false,
					enable_city_border = false;
	

	/* Creates a new instance of Civ_Draw */
	public Civ_Panel(Civ_State state, Civ_Ressource ressource
			,int wscreen, int hscreen, int my_id, boolean enable_mousemotion) {

		super(); 

		this.state = state;
		this.ressource = ressource;
		this.wscreen = wscreen;
		this.hscreen = hscreen;
		this.my_id = my_id;		
		this.enable_mousemotion = enable_mousemotion;
		

		//////////
		draw = new Civ_Draw();
		draw.configure_input(this.ressource, this.state, viewx,
				viewy, wscreen, hscreen,
				current_unit, my_id, this.time);

		setOpaque(true);
		setSize(wscreen, hscreen);

		//Register for mouse events.
		addMouseListener(this);
		
		if (enable_mousemotion)
		{
			addMouseMotionListener(this);
		}

		mouse_x = wscreen / 2;
		mouse_y = hscreen / 2;

	
		

	}

	
	private int counter1 = 0;
	
		
	public void timer_run() {


		
		// check mouse
		counter1 ++;
		if (counter1 >= 1)
		{
			counter1 = 0;
			mouse_check_border();
		}

		// redraw everything needed
		if (do_animation) {
			if (need_redraw)
				simple_redraw_all();
			else
				simple_redraw_animation();
			need_redraw = false;
		}


		// increment time
		time += timer_tick;

	
	}

	public void mouseClicked(MouseEvent e) {
		
		int mx = e.getX();
		int my = e.getY();
		
		int clicks = e.getClickCount();
		
		// filtrage double-clics
		if (clicks<2)
			return;
		
		Point pos = draw.select_tile(mx, my);
		
	
		
		
		Civ_PanelEvent evt = new Civ_PanelEvent(this);
		evt.mx = mx;
		evt.my = my;
		evt.tx = pos.x;
		evt.ty = pos.y;
		evt.id = Civ_PanelEvent.ID_DOUBLE_CLICK;
		
		fireCiv_PanelEvent( evt );
	}
	
	
	public void mouseEntered(MouseEvent e) {
	}
	
	public void mouseExited(MouseEvent e) {
	}
	//   public void mousePressed(MouseEvent e) {}
	//   public void mouseReleased(MouseEvent e) {}
	//   public void mouseDragged(MouseEvent e) {}
	public void mouseMoved(MouseEvent e) {
		
		mouse_x = e.getX();
		mouse_y = e.getY();
	}
	
	
	public void mouse_check_border() {
		
		if (!enable_mousemotion)
		{
			return;
		}
		
		int mx = mouse_x;
		int my = mouse_y;
		int w = this.getWidth();
		int h = this.getHeight();
		
		int dx = 0;
		int dy = 0;
		
		/////////////// border 1
		int b = 16;
		int step = 8;
		
		if (mx<b)
			dx = -step;
		else if (mx>(w-b))
			dx = step;	
		if (my<b)
			dy = -step;
		else if (my>(h-b))
			dy = step;	
		
		/////////////// border 2
		b = 4;
		step = 64;
		
		if (mx<b)
			dx = -step;
		else if (mx>(w-b))
			dx = step;	
		if (my<b)
			dy = -step;
		else if (my>(h-b))
			dy = step;	
		
		
		
		if (dx!=0 || dy!=0)
		{
			set_view(viewx + dx, viewy + dy);
		}
		
	}

	/*
	 * public void mouseMoved(MouseEvent e) { System.out.println("Mouse moved " + " (" +
	 * e.getX() + "," + e.getY() + ")"); mouse_x = e.getX(); mouse_y = e.getY(); }
	 */

	private int last_dx = 0, last_dy = 0;
	private boolean mode_scroll = false;
	private boolean enable_drag = true;
	
	public void mouseDragged(MouseEvent e) {
		
		if (!enable_drag)
		{
			return;
		}
		
		if (!enable_mousemotion)
		{
			return;
		}
		
		// System.out.println("Mouse dragged" + " (" + e.getX() + "," + e.getY()
		// + ")");
		mouse_x = e.getX();
		mouse_y = e.getY();

		/*
		 * if ( e.getButton()!=1 ) return;
		 */
		if (mode_scroll == false)
			return;

		/*
		 * try { (new java.awt.Robot()).mouseMove(Civ_Main.wscreen/2,
		 * Civ_Main.hscreen/2); } catch (Exception ex) {}
		 */

		

		int v1 = 16;
		int v2 = 256;

		int dx, dy;

		dx = mouse_x - mouse_startx;
		dy = mouse_y - mouse_starty;

		int dist = (dx - last_dx) * (dx - last_dx) + (dy - last_dy)
				* (dy - last_dy);
		if (dist < 4 * 4)
			return;

		last_dx = dx;
		last_dy = dy;

		dx = dx * 8;
		dy = dy * 8;

		set_view(view_startx + dx, view_starty + dy);

	}

	public void mousePressed(MouseEvent e) {
		// System.out.println("Mouse pressed" + " (" + e.getX() + "," + e.getY()
		// + ")");
		mouse_x = e.getX();
		mouse_y = e.getY();

		/*
		 * System.out.println("# of clicks: " + e.getClickCount());
		 * 
		 * System.out.println("button :" + e.getButton());
		 */

		Point pos = draw.select_tile(mouse_x, mouse_y);
		
		
		Civ_PanelEvent evt = new Civ_PanelEvent(this);
		evt.mx = mouse_x;
		evt.my = mouse_y;
		evt.tx = pos.x;
		evt.ty = pos.y;
		
		mode_scroll = false;
		switch (e.getButton()) {
			case 1 :
				if (enable_mousemotion)
				{					
					view_startx = viewx;
					view_starty = viewy;
					mouse_startx = mouse_x;
					mouse_starty = mouse_y;
					setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
					mode_scroll = true;			
				}
				evt.id = Civ_PanelEvent.ID_PRESSED_LEFT;
				fireCiv_PanelEvent( evt );
				break;

			case 2 :
			case 3 :
				evt.id = Civ_PanelEvent.ID_PRESSED_RIGHT;
				fireCiv_PanelEvent( evt );
				break;

		}
	}

	

	

	public void mouseReleased(MouseEvent e) {

		if (!enable_mousemotion)
		{
			return;
		}
		
		mode_scroll = false;
		switch (e.getButton()) {
			case 1 :
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				break;

		}
	}

	int compute_fps(long t) {
		int fps;
		if (t <= 0)
			fps = 99;
		else
			fps = (int) Math.round(1e9 / (double) (t));
		if (fps > 99)
			fps = 99;
		return fps;
	}

	int compute_cpu_usage(long t_nanosec) {
		int cpu;
		cpu = (int) Math.round(1e-6 * 100.0 * (double) (t_nanosec)
				/ (double) (timer_tick));
		return cpu;
	}

	void print_boxed(String s, int x, int y) {
		FontMetrics fm = g2d.getFontMetrics();
		int w = fm.stringWidth(s);
		int h = fm.getHeight();
		g2d.clearRect(x, y, w, h + fm.getDescent());
		g2d.drawString(s, x, y + h);
	}

	public int fps = 0;
	public int cpu_usage = 0;
	
	void center_view() {
		// centre de l'ecran
		set_view (
				((state.world_w) * 64) / 2 ,
				((state.world_h) * 32) / 2 ) ;
	}
	
	
	public void set_view(int nvx, int nvy) {

		/*
		 * viewx = nvx % (64*(world_w)); viewy = nvy % (32*(world_h));
		 */
		viewx = Civ_State.mod(nvx, (64 * (state.world_w)));
		viewy = Civ_State.mod(nvy, (32 * (state.world_h)));
		//    System.out.println("viewx=" + viewx + " viewy=" + viewy);

		need_redraw = true;
		//civ_panel.simple_redraw_all();
		//frame.repaint();
	}
	
	
	
	public void set_view_if_needed(int nvx, int nvy) {
		
		int dist = 0;
		
		
		dist  = Math.abs(nvx - viewx) ;
		if ( dist > (wscreen/2-64) ){
			set_view(nvx, nvy);
			return;
		}
		
		dist  = Math.abs(nvy - viewy) ;
		if ( dist > (hscreen/2-64) ){
			set_view(nvx, nvy);
			return;
		}
		
	}

	
	protected void paintComponent(Graphics g) {

		super.paintComponent(g);
		//System.out.println("civ_draw.paintComponent");

		draw.configure_input(this.ressource, this.state, viewx,
				viewy, wscreen, hscreen,
				current_unit, my_id, this.time);

		if (background == null) {
			background = (BufferedImage) createImage(wscreen,
					hscreen);
			//background = new BufferedImage(civ_main.wscreen,
			// civ_main.hscreen, BufferedImage.TYPE_INT_RGB);
			System.out.println("background=" + background);
		}
		
		//if (!enable_timer)
		{
			//need_redraw_background = true;
			need_redraw_animation = true;
		}

		if (need_redraw_background) {
			this.g2d = (Graphics2D) background.createGraphics();
			draw.configure_output(this.g2d);
			long t1 = My_Toolkit.nanoTime();
			draw.redraw_background();
			if (enable_city_border) draw.dessine_limite_production(1);
			long t2 = My_Toolkit.nanoTime();
			fps = compute_fps(t2 - t1);
			cpu_usage = compute_cpu_usage(t2 - t1);
			//System.out.printf("redraw_background : %d ms\n" ,
			// Math.round((double)(t2-t1)*1e-6) );
			need_redraw_background = false;
			g2d.dispose();
		}

		if (background != null) {
			this.g2d = (Graphics2D) g;
			long t1 = My_Toolkit.nanoTime();
			g2d.drawImage(background, 0, 0, null);
			long t2 = My_Toolkit.nanoTime();
			//System.out.printf("blit : %d ms\n" ,
			// Math.round((double)(t2-t1)*1e-6) );
		}

		if (need_redraw_animation) {
			this.g2d = (Graphics2D) g;
			draw.configure_output(this.g2d);
			long t1 = My_Toolkit.nanoTime();
			draw.redraw_animation();
			long t2 = My_Toolkit.nanoTime();
			//System.out.printf("redraw_animation : %d ms\n" ,
			// Math.round((double)(t2-t1)*1e-6) );
			need_redraw_animation = false;
		}

		
		
		
	}
	
	
	
	
	

	public void simple_redraw_all() {

		need_redraw_background = true;
		need_redraw_animation = true;
		this.repaint();
	}

	public void simple_redraw_animation() {

		need_redraw_background = false;
		need_redraw_animation = true;
		this.repaint();

	}
	
	public Point to_screen(int tx, int ty) 
	{
		//Point p = draw.to_screen(tx,ty);
		int dstx = tx * 64 - viewx + (wscreen / 2);
		int dsty = ty * 32 - viewy + (hscreen / 2);
		Point p =  (new Point(dstx, dsty));
		
		//p.x -= viewx;
		//p.y -= viewy;
		return p;
	}
	

	//////////////////////// Civ_PanelEvent
//	 Add the event registration and notification code to a class.
 
        // Create the listener list
        protected javax.swing.event.EventListenerList listenerList =
            new javax.swing.event.EventListenerList();
    
        // This methods allows classes to register for MyEvents
        public void addCiv_PanelEventListener(Civ_PanelEventListener listener) {
            listenerList.add(Civ_PanelEventListener.class, listener);
        }
    
        // This methods allows classes to unregister for MyEvents
        public void removeCiv_PanelEventListener(Civ_PanelEventListener listener) {
            listenerList.remove(Civ_PanelEventListener.class, listener);
        }
    
        // This private class is used to fire MyEvents
        void fireCiv_PanelEvent(Civ_PanelEvent evt) {
            Object[] listeners = listenerList.getListenerList();
            // Each listener occupies two elements - the first is the listener class
            // and the second is the listener instance
            for (int i=0; i<listeners.length; i+=2) {
                if (listeners[i]==Civ_PanelEventListener.class) {
                    ((Civ_PanelEventListener)listeners[i+1]).civ_PanelEventOccurred(evt);
                }
            }
        }
    
	

}