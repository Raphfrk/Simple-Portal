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

