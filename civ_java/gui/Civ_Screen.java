/*
 * Created on 4 févr. 2005
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
import javax.swing.JPanel;


public class Civ_Screen extends JPanel
{
	public boolean has_been_constructed = false;
	
	public Civ_Screen()
	{
		super(null);
	}
    
    public void swing_worker_construct_if_needed() // SwingWorker.construct()
    {
    	if (has_been_constructed == true)
    		return ;
    	
    	swing_worker_construct();
    	
    	has_been_constructed = true;
    }
    
    public void swing_worker_construct() // SwingWorker.construct()
    {
    	
    }
     
    public void timer_run() // javax.swing.Timer
    {
    	
    }
    
    public void flush(){
    	// BufferedImage.flush()
    }
    
    public void finalize()
    { 
    	flush();
    }
}


    
    
    
    