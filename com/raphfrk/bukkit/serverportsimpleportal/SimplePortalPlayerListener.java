/*******************************************************************************
 * Copyright (C) 2012 Raphfrk
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 ******************************************************************************/
package com.raphfrk.bukkit.serverportsimpleportal;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.raphfrk.bukkit.serverportcoreapi.ServerPortLocation;

public class SimplePortalPlayerListener extends PlayerListener {
	
	SimplePortal p;
	
	SimplePortalPlayerListener(SimplePortal p) {
		this.p = p;
	}
	
	public HashMap<Integer,IntLocation> oldPositions = new HashMap<Integer,IntLocation>();
	
	@Override
	public void onPlayerMove(PlayerMoveEvent event) {
		
		Player player = event.getPlayer();
		
		int id = player.getEntityId();
		
		IntLocation oldLocation = oldPositions.get(id);
		
		Location to = event.getTo();
		
		IntLocation newLocation = new IntLocation(to.getBlockX(), to.getBlockY(), to.getBlockZ(), to.getWorld().getName());
		
		if(oldLocation == null) {
			oldPositions.put(id, newLocation);
			return;
		}
		
		if(!oldLocation.getWorld().getName().equals(newLocation.getWorld().getName())) {
			oldPositions.put(id, newLocation);
			return;
		}
		
		if(oldLocation.equals(newLocation)) return;
		
		oldPositions.put(id, newLocation);
		
		String target = p.portalManager.portals.get(newLocation);
		
		if (target != null) {
			ServerPortLocation dest;
			try {
				dest = new ServerPortLocationMirror(target);
			} catch (IllegalArgumentException iae) {
				player.sendMessage("[SimplePortal] Illegal portal target: " + target);
				return;
			}
			p.serverPortCore.teleport(player.getName(), dest);
		}
		
	}
	
}
