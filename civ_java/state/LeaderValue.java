

/*
 * Created on 8 mars 2005
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

package state;

import java.util.Iterator;

/**
 * @author roudoudou
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class LeaderValue {

	private Leader leader;
	
	private Civ_State state;
	
	// revenus
	public int 	revenus, revenus_villes, revenus_percepteurs,		
				revenus_civ, revenus_interets;	
				
	// depenses
	public int 	depenses, depenses_science, depenses_distractions,		
				depenses_corruption, depenses_maintenance, depenses_unites,
				depenses_civ;	
	
	public int gain;
	
	public int science_nb_tours;
	
	public int free_units;
	
	public String toString(){
		return   "LeaderValue" 
		 		+ " revenus=" + revenus
				+ " revenus_villes=" + revenus_villes 
				+ " revenus_percepteurs=" + revenus_percepteurs
				+ " revenus_civ=" + revenus_civ
				+ " revenus_interets=" + revenus_interets 
				+ " depenses=" + depenses
				+ " depenses_science=" + depenses_science
				+ " depenses_distractions=" + depenses_distractions
				+ " depenses_corruption=" + depenses_corruption
				+ " depenses_maintenance=" + depenses_maintenance
				+ " depenses_unites=" + depenses_unites
				+ " depenses_civ=" + depenses_civ
				+ " gain=" + gain
				+ " science_nb_tours=" + science_nb_tours
				+ " free_units=" + free_units				
				;
	}
	
	public LeaderValue( Civ_State state )
    {
	 		this.state = state;
	 		init();
    }
	
	public void init( )
    {
		revenus = 0;
		revenus_villes = 0;
		revenus_percepteurs = 0;		
		revenus_civ = 0;
		revenus_interets = 0;	
		
		depenses = 0;
		depenses_science = 0;
		depenses_distractions = 0;		
		depenses_corruption = 0;
		depenses_maintenance = 0; 
		depenses_unites  = 0;
		depenses_civ = 0;
		
		gain = 0;
		
		science_nb_tours = 0;
		
		free_units = 0;
    }
	
	
	
	
	
	 public void compute( int player_id )
	  {
	 	leader = state.leader[player_id-1];
	 	Government gov = state.government[leader.government];	
		Race race = state.race[leader.civ];
		
		init();
		
		free_units = gov.free_units;
		
			
	 	//	  traitement des villes					
	  	CityValue v = new CityValue(state);  	
		for (Iterator i=state.city.iterator();i.hasNext();)
		{
			City c = (City)i.next();
			if ( c.owner!=player_id )
				continue;
			
			v.compute( c );
			
			revenus_villes += v.gold;
		 	depenses_corruption += v.gold_loss;
		 	depenses_maintenance += v.maintenance;
		 	depenses_science += v.science_basic;

  		  
  		    if ( c.size <= 6 )
  		    	depenses_unites -= gov.free_units_per_town;
  		    else if ( c.size <= 12  )
  		    	depenses_unites -= gov.free_units_per_city;
  		    else
  		    	depenses_unites -= gov.free_units_per_metropolis;
  			
		}	
			
		//	traitement des unités	
		int nb_units = 0;
		for (Iterator i=state.unit.iterator();i.hasNext();)
		{
			Unit u = (Unit)i.next();
			if ( u.owner!=player_id )
				continue;
				
			nb_units ++;
		}	
		
		depenses_unites = (nb_units - free_units)*gov.cost_unit;
		if (depenses_unites<0)
			depenses_unites = 0;
		
		
		//////////////////////////
		
		
		revenus = revenus_villes + revenus_percepteurs + revenus_civ + revenus_interets ;
		
		depenses = depenses_science + depenses_distractions + depenses_corruption
					+ depenses_maintenance + depenses_unites + depenses_civ;
		
		gain = revenus - depenses;
	 	
	  }
	
}
