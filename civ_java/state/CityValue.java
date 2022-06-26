

/*
 * Created on 3 févr. 2005
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
public class CityValue
{	
	private City city;
	
	private Civ_State state;
	
	public int 	food, shield, gold,		
				food_loss, shield_loss, gold_loss,					
				food_income, shield_income, gold_income;	
				
		
	
	public int tax_rate, science_rate, luxury_rate;	
	public int tax_basic, science_basic, luxury_basic;	
	public int tax, science, luxury; 
	
	public int prod_nb_rangees, prod_nb_tours;	
	public int food_nb_rangees, food_nb_tours;
	
	public int pollution = 0; 	
	public int maintenance = 0;	
	public int corruption = 0; // en pourcentage
	
	public String toString(){
		return   "CityValue" 
		 		+ " food=" + food + "/" + food_loss + "/" + food_income
				+ " shield=" + shield + "/" + shield_loss + "/" + shield_income
				+ " gold=" + gold + "/" + gold_loss + "/" + gold_income
				+ " tax=" + tax_rate + "/" + tax_basic + "/" + tax
				+ " science=" + science_rate + "/" + science_basic + "/" + science
				+ " luxury=" + luxury_rate + "/" + luxury_basic + "/" + luxury
				;
		

		
	}
	
	public CityValue( Civ_State state )
    {
	 		this.state = state;
    }
	
	public void compute( City city )
      {
      	
		this.city = city;
		
		TileValue tmp = new TileValue(state);
		
		// case de la ville
		tmp.compute( city, 0, 0);
 		
 		food = tmp.food;
 		shield = tmp.shield;
 		gold = tmp.gold;	
		
		// citoyens
		for (Iterator i = city.citizens.iterator(); i.hasNext(); )
		{
		 		City.Citizen c = (City.Citizen)i.next();
		 		
		 		if (c.profession!= City.Citizen.WORKER)
   		 			continue;
		 		
		 		tmp.compute( city, c.x, c.y );
		 		
		 		food += tmp.food;
		 		shield += tmp.shield;
		 		gold += tmp.gold;	
		}
	
		
		// food
		food_loss = 2*city.size;
		food_income = food - food_loss;
		
		// shield	
		int waste = 0; // en pourcentage
		shield_loss = (waste*shield)/100;
		shield_income = shield - shield_loss;
		
		//gold
		
		Leader l = state.leader[city.owner-1];	
		tax_rate = l.tax_rate;
		science_rate = l.science_rate;
		luxury_rate = l.luxury_rate(); 
		
		
		
		
		gold_loss = (gold*corruption + 50)/100;
		gold_income = gold - gold_loss;
		
		int improvements = state.city_has_improvement(city,2);
		//System.out.println("science_improvement="+improvements);
		science_basic = (gold_income*science_rate + 9)/10;
		science = (science_basic*(2+improvements))/2;
		

		
		// tax
		int rate = tax_rate+luxury_rate;
		tax_basic = gold_income-science_basic;
		if (rate==0)	tax_basic = 0;
		else			tax_basic = (tax_basic*tax_rate + (rate-1))/(rate);
		tax = (tax_basic*(2+state.city_has_improvement(city,4)))/2;
		
		
		//luxury
		luxury_basic  = gold_income-science_basic-tax_basic;
		luxury = (luxury_basic*(2+state.city_has_improvement(city,3)))/2;
		
		
		
		
		
		
		/////////// production
		
		int cost =  state.compute_cost_city_build( city.build );
		
		prod_nb_rangees = cost / 10;
		
		prod_nb_tours = compute_prod_nb_tours( cost );   		
		
		//// nourriture
		
		if (city.size<=6)	food_nb_rangees = 2;
		else				food_nb_rangees = Civ_State.min(6,city.size);
		
		food_nb_tours = compute_food_nb_tours();
		
		// maintenance
		for (Iterator i = city.buildings.iterator(); i.hasNext(); )
		{
				int id = ((Integer)i.next()).intValue();	 		
				maintenance += state.building[id].maintenance_cost ;
		}
		
		
      }
	
		public int compute_food_nb_tours()
		{
			int cost = food_nb_rangees*10;
			
			if (city.food >= cost)
				return 1;
			
			if (food_income<=0)
				return -1;
			
			int val = 1 + (cost - city.food - 1) / food_income;
		
			return val;
		}
	
		public int compute_prod_nb_tours( int cost )
		{
			if (city.shield >= cost)
				return 1;
			
			if (shield_income<=0)
				return -1;
			
			int val = 1 + (cost - city.shield - 1) / shield_income ;
		
			return val;
		}
}
