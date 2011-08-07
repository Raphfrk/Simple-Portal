package com.raphfrk.bukkit.serverportsimpleportal;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DelPortalCommand implements CommandExecutor {

	SimplePortal p;

	public DelPortalCommand(SimplePortal p) {
		this.p = p;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label,
			String[] args) {

		if (!sender.isOp()) {
			sender.sendMessage("[SimplePortal] Only ops may use this command");
			return true;
		}
		
		if(args.length != 1) {
			return false;
		}
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("[SimplePortal] Only players can set portal locations");
			return true;
		}
		
		int d = 0;
		if (MiscUtils.isInt(args[0])) {
			d = MiscUtils.getInt(args[0]);
		} else {
			sender.sendMessage("[SimplePortal] Unable to parse distance");
			return true;
		}
		
		Player player = (Player)sender;
		
		int x = player.getLocation().getBlockX();
		int y = player.getLocation().getBlockY();
		int z = player.getLocation().getBlockZ();
		String world = player.getWorld().getName();
	
		IntLocation source = new IntLocation(player.getLocation());
		
		String target = args[0];
		
		boolean updated = false;
		
		for(int xx = -d; xx <= d; xx++) {
			for(int yy = -d; yy <= d; yy++) {
				for(int zz = -d; zz <= d; zz++) {
					int px = x + xx;
					int py = y + yy;
					int pz = z + zz;
					IntLocation loc = new IntLocation(px, py, pz, world);
					if(p.portalManager.portals.remove(loc) != null) {
						player.sendMessage("[SimplePortal] Removed portal block at " + loc);
						updated = true;
					}
				}
			}
		}
		
		if(updated) {
			p.portalManager.writePortals();
		}
		
		p.portalManager.portals.put(source, target);
		
		return true;
		
	}
}
