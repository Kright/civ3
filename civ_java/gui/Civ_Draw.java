
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
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.util.HashMap;
import java.util.Iterator;

import ressource.Civ_Animation;
import ressource.Civ_Ressource;
import state.City;
import state.Civ_State;
import state.Tile;
import state.Unit;


/**
 * @author roudoudou
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class Civ_Draw {

	final boolean debug = false;

	/* source */
	private Civ_Ressource ressource;
	private Civ_State state;
	private final int wtile = 128;
	private final int htile = 64;
	private int viewx, viewy; // centre de l'ecran
	private int wscreen = 800;
	private int hscreen = 600;
	private int current_unit = 0;
	private int my_id = 1;
	private int time = 0;

	/* destination */
	private Graphics2D g2d;

	public Civ_Draw() {
	}

	public void configure_input(Civ_Ressource ressource, Civ_State state,
			int viewx, int viewy, int wscreen, int hscreen, int current_unit,
			int my_id, int time) {

		this.ressource = ressource;
		this.state = state;
		this.viewx = viewx;
		this.viewy = viewy;
		this.wscreen = wscreen;
		this.hscreen = hscreen;
		this.current_unit = current_unit;
		this.my_id = my_id;
		this.time = time;
	}

	public void configure_output(Graphics2D g2d) {

		this.g2d = g2d;
	}

	static int getbit(int x, int n) {
		return ((x >> n) & 1);
	}

	public static void blitSurface4(BufferedImage src, int sx, int sy, int sw, int sh,
			Graphics2D dst, int dx, int dy) {
		
		//dst.setClip(dx, dy, sw, sh);
		Shape tmp = dst.getClip();			
		dst.clipRect(dx, dy, sw, sh);			
		dst.drawImage(src, dx - sx, dy - sy, null);
		dst.setClip(tmp);

	}

	public static void blitSurface5(BufferedImage src, Graphics2D dst, int dx, int dy) {
		//blitSurface4( src, 0, 0, src.getWidth(), src.getHeight(), dst,dx,dy);
		//dst.setClip(null);
		dst.drawImage(src, dx, dy, null);
	}

	// affichage du terrain de base
	void redraw_base(int x, int y) {
		//= do -- when_explored ref (x,y+1) $ do

		Tile nord = state.readTile(x, (y - 1));
		Tile est = state.readTile((x + 1), y);
		Tile sud = state.readTile(x, (y + 1));
		Tile ouest = state.readTile((x - 1), y);

		Tile k = sud;

		int dstx = x * 64 - viewx + (wscreen / 2) - (wtile / 2);
		int dsty = y * 32 - viewy + (hscreen / 2) - (htile / 2);

		int srcx = wtile * ((k.image) % 9);
		int srcy = htile * ((k.image) / 9);

		int img = (k.file);

		blitSurface4((ressource.surf[img]), srcx, srcy, wtile, htile, this.g2d,
				dstx, dsty);

	}

	
	// affichage du terrain réel
	void redraw_real(int x, int y) {
		//  do -- when_explored ref (x,y) $ do

		int img;
		int srcx, srcy;

		Tile k = state.readTile(x, y);

		int centerx = x * 64 - viewx + (wscreen / 2);
		int centery = y * 32 - viewy + (hscreen / 2);
		int dstx = centerx - (wtile / 2);
		int dsty = centery - (htile / 2);

		Tile no = state.readTile((x - 1), (y - 1));
		Tile ne = state.readTile((x + 1), (y - 1));
		Tile so = state.readTile((x - 1), (y + 1));
		Tile se = state.readTile((x + 1), (y + 1));

		int river_so = getbit(k.river, 5);
		int river_se = getbit(k.river, 3);
		int river_ne = getbit(k.river, 1);
		int river_no = getbit(k.river, 7);

		// 1 si connection
		int _no = (no.is_mh()) & (1 - river_no);
		int _ne = (ne.is_mh()) & (1 - river_ne);
		int _se = (se.is_mh()) & (1 - river_se);
		int _so = (so.is_mh()) & (1 - river_so);

		switch (k.terrain_real()) {

			case Tile.TERRAIN_FOREST : // forest

				switch (k.terrain_base()) {
					case Tile.TERRAIN_PLAINS :
						img = Civ_Ressource.id_plains_forests;
						break;
					case Tile.TERRAIN_GRASSLAND :
						img = Civ_Ressource.id_grassland_forests;
						break;
					case Tile.TERRAIN_TUNDRA :
						img = Civ_Ressource.id_tundra_forests;
						break;
					default :
						img = Civ_Ressource.id_plains_forests;
						break;
				}
				srcx = 1;
				srcy = 6;
				blitSurface4((ressource.surf[img]), (128 * srcx), (88 * srcy),
						128, 88, this.g2d, dstx, (dsty - 12));
				break;

			case Tile.TERRAIN_GRASSLAND :

				img = (((k.bonuses) & 0x0001) != 0) ? Civ_Ressource.id_tnt  // bonus grassland
						: Civ_Ressource.id_xtgc;
				srcx = 0;
				srcy = 3;
				if (img != Civ_Ressource.id_xtgc)
					blitSurface4((ressource.surf[img]), (128 * srcx),
							(64 * srcy), 128, 64, this.g2d, dstx, dsty);
				break;

			case Tile.TERRAIN_MOUNTAINS :

				if (((k.bonuses) & 0x0010) != 0)
					img = Civ_Ressource.id_Mountains_snow ; // snow-capped mts
				else if (((k.bonuses) & 0x0100) != 0)
					img = Civ_Ressource.id_mountain_forests ; // pine forest
				else
					img = Civ_Ressource.id_Mountains;
				srcx = 2 * _ne + _no;
				srcy = 2 * _se + _so;
				blitSurface4((ressource.surf[img]), (128 * srcx), (88 * srcy),
						128, 88, this.g2d, dstx, (dsty - 24));
				break;

			case Tile.TERRAIN_HILLS  :

				srcx = 2 * _ne + _no;
				srcy = 2 * _se + _so;
				blitSurface4((ressource.surf[Civ_Ressource.id_xhills]), (128 * srcx), (72 * srcy),
						128, 72, this.g2d, dstx, (dsty - 8));
				break;
		} //  fin de switch( k.terrain_real() )

		// irrigation	
		if (k.irrigation()==1)
		{	    
			switch (k.terrain_base()) {
				case Tile.TERRAIN_DESERT :
					img = Civ_Ressource.id_irrigation_DESERT;
					break;
				case Tile.TERRAIN_PLAINS :
					img = Civ_Ressource.id_irrigation_PLAINS;
					break;
				case Tile.TERRAIN_TUNDRA :
					img = Civ_Ressource.id_irrigation_TUNDRA;
					break;
				default :
					img = Civ_Ressource.id_irrigation;
					break;
			}
			_no = no.irrigation() ;
			_ne = ne.irrigation() ;
			_se = se.irrigation() ;
			_so = so.irrigation() ;
			srcx = 2 * _ne + _no;
			srcy = 2 * _se + _so;
			blitSurface4((ressource.surf[img]), 
					(128 * srcx), (64 * srcy),
					128, 64, this.g2d, dstx, dsty);
		}	
		
		
		
		
		
		/*-- StartLoc		
		 -- when_ ((resource k)>=0) $
		 --	do	
		 --		sdl_BlitSurface3 (surf!22) 0 0 128 64 background dstx dsty 128 64
		 */

	}

	

	// affichage des rivieres
	void redraw_river(int x, int y) {
		// = when_explored ref (x,y+1) $ do

		Tile nord = state.readTile(x, (y - 1));
		Tile est = state.readTile((x + 1), y);
		Tile sud = state.readTile(x, (y + 1));
		Tile ouest = state.readTile((x - 1), y);

		Tile k = sud;

		int dstx = x * 64 - viewx + (wscreen / 2) - (wtile / 2);
		int dsty = y * 32 - viewy + (hscreen / 2) - (htile / 2);

		int river_no = getbit(nord.river, 5) | getbit(ouest.river, 1);

		int river_ne = getbit(nord.river, 3) | getbit(est.river, 7);

		int river_se = getbit(sud.river, 1) | getbit(est.river, 5);

		int river_so = getbit(sud.river, 7) | getbit(ouest.river, 3);

		// "floodplains.pcx" -- 20

		int flood_no = river_no & (nord.is_flood() | ouest.is_flood());

		int flood_ne = river_ne & (nord.is_flood() | est.is_flood());

		int flood_se = river_se & (sud.is_flood() | est.is_flood());

		int flood_so = river_so & (sud.is_flood() | ouest.is_flood());

		int srcx = flood_no + 2 * flood_ne;
		int srcy = flood_so + 2 * flood_se;
		int flood_val = srcx + 4 * srcy;

		if (flood_val != 0)
			blitSurface4((ressource.surf[Civ_Ressource.id_floodplains]), 
					(128 * srcx), (64 * srcy),
					wtile, htile, this.g2d, dstx, dsty);

		// "deltaRivers.pcx", -- 18
		// "mtnRivers.pcx" -- 19

		srcx = river_no + 2 * river_ne;
		srcy = river_so + 2 * river_se;
		int river_val = srcx + 4 * srcy;

		int sum_cso = nord.is_cso() | est.is_cso() | sud.is_cso() | ouest.is_cso();

		int sum_mh = nord.is_mh() | est.is_mh() | sud.is_mh() | ouest.is_mh();

		int img;
		if (((sum_cso == 1) || (flood_val > 0)) && (sum_mh == 0))
			img = Civ_Ressource.id_deltaRivers ;
		else
			img = Civ_Ressource.id_mtnRivers;

		// affichage des rivieres
		if (river_val != 0)
			blitSurface4((ressource.surf[img]), (128 * srcx), (64 * srcy),
					wtile, htile, this.g2d, dstx, dsty);

	}

	// affichage du "brouillard de guerre"

	int is_exp(Tile t) {
		return getbit(t.explored, my_id);
	}

	int is_fow(Tile t) {
		return getbit(t.fow, my_id);
	}

	void redraw_fog_of_war(int x, int y) { // when_explored ref (x,y+1) $ do

		/*
		 * if ((client_flags state) .&. 0x00000001)==1 then return () else do
		 */

		Tile nord = state.readTile(x, (y - 1));
		Tile est = state.readTile((x + 1), y);
		Tile sud = state.readTile(x, (y + 1));
		Tile ouest = state.readTile((x - 1), y);

		Tile k = sud;

		int dstx = x * 64 - viewx + (wscreen / 2) - (wtile / 2);
		int dsty = y * 32 - viewy + (hscreen / 2) - (htile / 2);

		int code_n = is_exp(nord) + is_fow(nord);
		int code_e = is_exp(est) + is_fow(est);
		int code_s = is_exp(sud) + is_fow(sud);
		int code_o = is_exp(ouest) + is_fow(ouest);

		int srcx = code_n + code_o * 3;
		int srcy = code_e + code_s * 3;
		
		//System.out.println("src : " + srcx + " " + srcy);

		if ((code_n + code_e + code_s + code_o)==8 )
			return;  // optimisation des cases complétement visibles
			
		blitSurface4((ressource.surf[Civ_Ressource.id_FogOfWar]), (128 * srcx),
				(64 * srcy), 128, 64, this.g2d, dstx, dsty);
	}

	// affichage des routes
	void redraw_roads(int x, int y) {
		// when_explored ref (x,y) $ do

		int img;
		int srcx, srcy;

		Tile k = state.readTile(x, y);

		if (k.road() == 0)
			return;

		int centerx = x * 64 - viewx + (wscreen / 2);
		int centery = y * 32 - viewy + (hscreen / 2);
		int dstx = centerx - (wtile / 2);
		int dsty = centery - (htile / 2);

		Tile k0, k1, k2, k3;
		
		k0 = state.readTile((x + 1), (y - 1)); // ne
		k1 = state.readTile((x + 2), (y)); // e
		k2 = state.readTile((x + 1), (y + 1)); // se
		k3 = state.readTile((x), (y + 2)); // s

		srcx = (k0.road()) + (k1.road()) * 2 + (k2.road()) * 4
				+ (k3.road()) * 8;
		
		k0 = state.readTile((x - 1), (y + 1)); // so
		k1 = state.readTile((x - 2), (y)); // o
		k2 = state.readTile((x - 1), (y - 1)); // no
		k3 = state.readTile((x), (y - 2)); // n

		srcy = (k0.road()) + (k1.road()) * 2 + (k2.road()) * 4
				+ (k3.road()) * 8;
		

		blitSurface4((ressource.surf[Civ_Ressource.id_roads]), (128 * srcx),
				(64 * srcy), 128, 64, this.g2d, dstx, dsty);

	}
	
	void redraw_ressources(int x, int y) {
		//  do -- when_explored ref (x,y) $ do

		int img;
		int srcx, srcy;

		Tile k = state.readTile(x, y);

		int centerx = x * 64 - viewx + (wscreen / 2);
		int centery = y * 32 - viewy + (hscreen / 2);
		int dstx = centerx - (wtile / 2);
		int dsty = centery - (htile / 2);

		// mines
		if (k.mine()==1)
		{	
			img = Civ_Ressource.id_TerrainBuildings;
			srcx = 2;
			srcy = 1;
			blitSurface4((ressource.surf[img]), 
					(128 * srcx), (64 * srcy),
					128, 64, this.g2d, dstx, dsty);
		}
		
		// resource
		if ((k.resource) >= 0) {
			img = Civ_Ressource.id_resources;
			int icon = state.good[k.resource].icon;
			srcx = (icon) % 6;
			srcy = (icon) / 6;
			blitSurface4((ressource.surf[img]), (50 * srcx), (50 * srcy), 50,
					50, this.g2d, (centerx - 25), (centery - 25));
		}
		
		
		
	}
	
	

	void redraw_tile(int x, int y) {

		
		redraw_base(x, (y + 1));

		redraw_real(x, y);

		redraw_river(x, (y + 1));

		redraw_roads(x, y);
		
		redraw_ressources(x, y);

		// redraw_fog_of_war( x, (y+1) );

	}

	
	
	void redraw_city(City c) {
		// when_explored ref (city_x c,city_y c) $ do
		// putStr $ "drawing " ++ (city_name c) ++ "\n"

		// coord
		int dstx = c.x * 64 - viewx + (wscreen / 2);
		int dsty = c.y * 32 - viewy + (hscreen / 2);

		/*int city_size;
		if (c.size<=6)
			city_size = 0;
		else if (c.size<=12)
			city_size = 1;
		else 
			city_size = 2;	
		*/
		int walls = (c.has_walls ? 1 : 0) ;
		//if (c.size>6) walls = 0;
		
		int srcx = c.city_level; // de 0 à 2 : city_level	
		int srcy = 0; // de 0 à 3 : l'epoque

		int culture_group = state.race[state.leader[c.owner - 1].civ].culture_group;
		int img = Civ_Ressource.id_rAMER + culture_group*2 + walls;

		// city
		blitSurface4((ressource.surf[img]), (167 * srcx), (95 * srcy), 167, 95,
				this.g2d, (dstx - 83), (dsty - 47));

		// city name

		g2d.setFont(ressource.font);
		g2d.setColor(Color.BLACK);
		print_center_align(c.name, dstx, dsty + 40 + (c.has_walls ? 8 : 0));

	}

	void print_center_align(String s, int x, int y) {
		int w = g2d.getFontMetrics().stringWidth(s);
		int h = g2d.getFontMetrics().getHeight();
		g2d.drawString(s, x - w / 2, y);
	}

	public void redraw_animation() {
		
		// redraw des units
		// note de julien : on redessine seulement les unités visibles,
		// et au plus 1 unité par case

		if ( state.unit == null )
			return;
		
		HashMap tmp = new HashMap(100);

		
		// dessine l'unité courante	
		{
			Unit u = state.find_unit_by_id(current_unit);	
			if (u!=null)
			{
				//System.out.println( "current_unit : " + u );
	
				if (clipping(unit2rect(u)))
				{
					Point pos = new Point(u.x, u.y);
					tmp.put(pos, null);
					redraw_unit(u);
				}
			}
		}
			
		

		for (Iterator i= state.unit.iterator();i.hasNext();)
	  	{
	  		Unit u = (Unit)i.next();

			if (!clipping(unit2rect(u)))
				continue;

			Point pos = new Point(u.x, u.y);

			if (tmp.containsKey(pos))
				continue;

			tmp.put(pos, null/* u */);

			redraw_unit(u);

		}

	}

	void redraw_unit(Unit u) {
		// when_explored ref (unit_x u,unit_y u) $ do

		// coord
		int dstx = u.x * 64 - viewx + (wscreen / 2);
		int dsty = u.y * 32 - viewy + (hscreen / 2);

		// animation
		//if (debug) System.out.println( "redraw_unit " + u );

		// curseur
		if (u.id == current_unit) {
			//if (debug) System.out.println( "*** curseur" );
			Civ_Animation animation = ressource.anim_cursor;

			int n = animation.calc_num_frame(0, time);
			//int n = 0;

			BufferedImage image = animation.images[n];

			Rectangle rect = animation.calc_rect_frame(dstx, dsty);
			int dx = rect.x;
			int dy = rect.y;

			blitSurface5(image, this.g2d, dx, dy);
		}

		// unit
		Civ_Animation animation = ressource.anim_cache
				.lookup_animation(state.proto[u.prto]);

		int n = animation.calc_num_frame(2, time);
		//int n = 0;

		BufferedImage image = animation.images[n];

		// changement palette
		image = change_palette(image, ressource.palv[u.owner]);

		Rectangle rect = animation.calc_rect_frame(dstx, dsty);

		int dx = rect.x;
		int dy = rect.y;

		blitSurface5(image, this.g2d, dx, dy);

	}

	public static BufferedImage change_palette(BufferedImage image,
			BufferedImage palv) {
		//	sdl_SetColors image (colors pal) 0 32
		WritableRaster raster = image.getRaster();

		IndexColorModel cm_image = (IndexColorModel) image.getColorModel();
		IndexColorModel cm_palv = (IndexColorModel) palv.getColorModel();

		byte[] r = new byte[256];
		byte[] g = new byte[256];
		byte[] b = new byte[256];
		byte[] a = new byte[256];
		cm_image.getReds(r);
		cm_image.getGreens(g);
		cm_image.getBlues(b);
		cm_image.getAlphas(a);

		byte[] r_palv = new byte[256];
		byte[] g_palv = new byte[256];
		byte[] b_palv = new byte[256];
		byte[] a_palv = new byte[256];
		cm_palv.getReds(r_palv);
		cm_palv.getGreens(g_palv);
		cm_palv.getBlues(b_palv);
		cm_palv.getAlphas(a_palv);

		System.arraycopy(r_palv, 0, r, 0, 32);
		System.arraycopy(g_palv, 0, g, 0, 32);
		System.arraycopy(b_palv, 0, b, 0, 32);
		System.arraycopy(a_palv, 0, a, 0, 32);

		/*
		 * for (int i=0;i <32;i++) { r[i]=r_palv[i]; g[i]=g_palv[i];
		 * b[i]=b_palv[i]; a[i]=a_palv[i]; }
		 */

		ColorModel cm2 = new IndexColorModel(8, 256, r, g, b, a);

		return new BufferedImage(cm2, raster, false, null);
	}

	Point select_tile(int mx, int my) {

		// position relative de la souris
		int x1 = (mx) - (wscreen / 2) + viewx;
		int y1 = (my) - (hscreen / 2) + viewy;

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

		return new Point(tx, ty);
	}

	boolean clipping(Rectangle r) {

		return (((r.x + r.width) >= 0) && ((r.y + r.height) >= 0)
				&& (r.x < wscreen) && (r.y < hscreen));
	}

	public Point to_screen(int tx, int ty) {

		int dstx = tx * 64 - viewx + (wscreen / 2);
		int dsty = ty * 32 - viewy + (hscreen / 2);
		return (new Point(dstx, dsty));
	}

	Rectangle city2rect(City c) {
		Point dst = to_screen(c.x, c.y);
		int x = dst.x - 100;
		int y = dst.y - 100;
		int w = 200;
		int h = 200;
		return (new Rectangle(x, y, w, h));
	}

	Rectangle unit2rect(Unit u) {
		Point dst = to_screen(u.x, u.y);
		int x = dst.x - 64;
		int y = dst.y - 64;
		int w = 128;
		int h = 128;
		return (new Rectangle(x, y, w, h));
	}

	Rectangle tile2rect(int tx, int ty) {
		Point dst = to_screen(tx, ty);
		int x = dst.x - 64;
		int y = dst.y - 32;
		int w = 128;
		int h = 64 + 32;
		return (new Rectangle(x, y, w, h));
	}
	
	

	public void redraw_background() {

		g2d.clearRect(0, 0, wscreen, hscreen);

		// coordonnées des tiles potentiellement 'visibles'
		/*
		 * let coord1 = [ (2*x+(y `mod` 2),y) | y <-[-14..12], x <-[-5..4] ]
		 * addVec (dx,dy) (sx,sy) = (sx+dx,sy+dy) (viewx,viewy) = view state
		 * (tx,ty) = select_tile (viewx,viewy) (wscreen `div` 2) (hscreen `div`
		 * 2) coord2 = map (addVec (tx,ty)) coord1
		 */

		// redraw des tiles
		/*
		 * mapM_ (redraw_tile ref) $ filter (clipping . (tile2rect (view
		 * state))) coord2
		 */

		Point p = select_tile((wscreen / 2), (hscreen / 2));
		int tx = p.x;
		int ty = p.y;

		int count_tile = 0;

		for (int y = -14; y <= 14; y += 1) {
			for (int x = -5; x <= 5; x += 1) {

				int coord1_x = (2 * x + (y % 2));
				int coord1_y = y;
				int coord2_x = coord1_x + tx;
				int coord2_y = coord1_y + ty;

				if (clipping(tile2rect(coord2_x, coord2_y))) {
					redraw_tile(coord2_x, coord2_y);
					count_tile ++;
				}
			}

		}

		if (debug)
			System.out.println(count_tile + " redraw_tile");

		// fog of war
		/*
		 * mapM_ (\(x,y) -> redraw_fog_of_war ref x (y+1)) $ filter (clipping .                 
		 * (tile2rect (view state))) coord2
		 */
		
		/*Point p = select_tile((wscreen / 2), (hscreen / 2));
		int tx = p.x;
		int ty = p.y;

		int count_tile = 0;
		*/
		
		for (int y = -14; y <= 14; y += 1) {
			for (int x = -5; x <= 5; x += 1) {

				int coord1_x = (2 * x + (y % 2));
				int coord1_y = y;
				int coord2_x = coord1_x + tx;
				int coord2_y = coord1_y + ty;

				if (clipping(tile2rect(coord2_x, coord2_y))) {
						
					redraw_fog_of_war(coord2_x, coord2_y+1);
			
				}
			}

		}

		// redraw des city
		if (state.city!=null)
		{
			for (Iterator i=state.city.iterator();i.hasNext();)
		  	{
	    		City c = (City)i.next();
				if (clipping(city2rect(c)))
					redraw_city(c);
			}
		}

	}
	
	//////////// autres
	
		
		
		class Tortue {
			
			public int x = 0, y =0;
			public int stepx = 64, stepy = 32;
			public boolean enable = true;
	
			
		    void deplace( int dx, int dy )
		    {	    	
		    	int x0 = x+dx*stepx;
		    	int y0 = y+dy*stepy;
		    	if (enable) g2d.drawLine(x, y, x0, y0 );
				x = x0;
		    	y = y0;
		    }

		    void border_0()
		    {
		    	enable = false;
		    	deplace( -3, 0 );
		    	enable = true;
				deplace( 3, -3 );
				deplace( 3, 3 );
				deplace( -3, 3 );
				deplace( -3, -3 );
		    }
		    
		    void border_1()
		    {
		    	enable = false;
		    	deplace( -3, 0 );
		    	enable = true;
				deplace( -1, -1 );
				deplace( 3, -3 );
				deplace( 1, 1 );
				deplace( 1, -1 );
				deplace( 3, 3 );
				deplace( -1, 1 );
				deplace( 1, 1 );
				deplace( -3, 3 );
				deplace( -1, -1 );
				deplace( -1, 1 );
				deplace( -3, -3 );
				deplace( 1, -1 );
		    }
		}

		public void dessine_limite_production( int border_level )
		{
			int cx = wscreen / 2;
			int cy = hscreen / 2;		
			

			g2d.setColor(Color.WHITE);
			
			Tortue t = new Tortue();
			
			t.x = cx;
			t.y = cy;
			t.stepx = 64;
			t.stepy = 32;
			
			if (border_level==0)
			{	
				t.border_0();
			}
			else
			{
				t.border_1();
			}
			
			
		}
	
	

}