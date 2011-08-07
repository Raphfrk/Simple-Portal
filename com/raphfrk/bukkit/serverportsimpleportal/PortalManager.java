package com.raphfrk.bukkit.serverportsimpleportal;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class PortalManager {
	
	public HashMap<IntLocation, String> portals = new HashMap<IntLocation, String>();

	SimplePortal p;
	
	PortalManager(SimplePortal p) {
		this.p = p;
	}
	
	public void readPortals() {
		
		String filename = new File(p.getDataFolder(), "portals.txt").toString();
		
		String[] portalStrings = MiscUtils.fileToString(filename);
		
		portals = new HashMap<IntLocation, String>();
		
		if(portalStrings != null) {
			for(String portalString : portalStrings) {
				
				String[] split1 = portalString.split(":", 2);
				
				if (split1.length == 2) {
					String location = split1[0];
					IntLocation loc = null;
					if(IntLocation.isIntLocation(location)) {
						loc = IntLocation.getIntLocation(location);
					} else {
						SimplePortal.log("Invalid portal location read from file");
						continue;
					}
					String target = split1[1];
					portals.put(loc, target);
					try {
						new ServerPortLocationMirror(target);
					} catch (IllegalArgumentException iae) {
						SimplePortal.log("Invalid portal target read from file");
						continue;
					}
					System.out.println("Read: " + loc + " -> " + target);
				}
				
			}
		} else {
			SimplePortal.log("Unable to read portals file");
		}
		
	}
	
	public void writePortals() {
		ArrayList<String> lines = new ArrayList<String>();
		
		for(IntLocation loc : portals.keySet()) {
			String target = portals.get(loc);
			if(target != null) {
				lines.add(loc + ":" + target);
			}
		}
		
		String filename = new File(p.getDataFolder(), "portals.txt").toString();
		
		MiscUtils.stringToFile(lines, filename);
	}
	
}
