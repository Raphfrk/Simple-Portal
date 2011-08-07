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
		
		if(oldLocation==null || !oldLocation.getWorld().getName().equals(newLocation.getWorld().getName())) {
			return;
		}
		
		if(oldLocation.equals(newLocation)) return;
		
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
