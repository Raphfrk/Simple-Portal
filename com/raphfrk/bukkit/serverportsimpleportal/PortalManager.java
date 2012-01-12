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
		
		String filename = new File(p.pluginDirectory, "portals.txt").toString();
		
		String[] portalStrings = MiscUtils.fileToString(filename);
		
		portals = new HashMap<IntLocation, String>();
		
		int count = 0;
		
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

					try {
						new ServerPortLocationMirror(target);
					} catch (IllegalArgumentException iae) {
						SimplePortal.log("Invalid portal target read from file");
						continue;
					}
					
					portals.put(loc, target);
					count ++;
				}
				
			}
		} else {
			SimplePortal.log("Unable to read portals file");
		}
		
		SimplePortal.log("Loaded " + count + " portal start blocks");
		
	}
	
	public void writePortals() {
		ArrayList<String> lines = new ArrayList<String>();
		
		int count = 0;
		
		for(IntLocation loc : portals.keySet()) {
			String target = portals.get(loc);
			if(target != null) {
				lines.add(loc + ":" + target);
				count ++;
			}
		}
		
		SimplePortal.log("Saving " + count + " portal start blocks");
		
		String filename = new File(p.pluginDirectory, "portals.txt").toString();
		
		MiscUtils.stringToFile(lines, filename);
	}
	
}
