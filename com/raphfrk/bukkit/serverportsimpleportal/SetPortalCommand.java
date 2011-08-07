package com.raphfrk.bukkit.serverportsimpleportal;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetPortalCommand implements CommandExecutor {

	SimplePortal p;

	public SetPortalCommand(SimplePortal p) {
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
		
		Player player = (Player)sender;
	
		IntLocation source = new IntLocation(player.getLocation());
		
		String target = args[0];
		
		try {
			new ServerPortLocationMirror(target);
		} catch (IllegalArgumentException iae) {
			sender.sendMessage("[SimplePortal] Unable to parse target location");
			return true;
		}
		
		p.portalManager.portals.put(source, target);
		
		sender.sendMessage("[SimplePortal] added link");
		sender.sendMessage("FROM: " + source);
		sender.sendMessage("TO: " + target);
		
		p.portalManager.writePortals();
		
		return true;
		
	}

}

