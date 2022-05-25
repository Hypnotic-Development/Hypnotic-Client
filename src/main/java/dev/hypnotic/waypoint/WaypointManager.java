/*
* Copyright (C) 2022 Hypnotic Development
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package dev.hypnotic.waypoint;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.google.common.collect.Lists;

import dev.hypnotic.Hypnotic;

public class WaypointManager {

	public static WaypointManager INSTANCE = new WaypointManager();
	public List<Waypoint> waypoints = Lists.newArrayList();
	
	// The file to save and load waypoints
	private File waypointsFile = new File(Hypnotic.hypnoticDir, "waypoints.txt");
	
	public WaypointManager() {
		
	}
	
	public Waypoint getWaypointByName(String string) {
		for (Waypoint waypoint : waypoints) {
			if (waypoint.getName().equalsIgnoreCase(string)) {
				return waypoint;
			}
		}
		return null;
	}
	
	/**
	 * Saves all of the waypoints
	 * @throws IOException
	 */
	public void save() throws IOException {
		// Create the file if it doesn't exist
		if (!waypointsFile.exists()) waypointsFile.createNewFile();
		
		PrintWriter pw = new PrintWriter(waypointsFile);
		
		// Save all of the waypoint names, locations, colors, and enabled status
		for (Waypoint waypoint : waypoints) {
			pw.println(waypoint.getName() + ":" + waypoint.getX() + ":" + waypoint.getY() + ":" + waypoint.getZ() + ":" + waypoint.getColor().getRGB() + ":" + waypoint.isEnabled());
		}
		
		pw.close();
	}
	
	/**
	 * Loads all of the waypoints
	 * @throws IOException
	 */
	public void load() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(waypointsFile));
		
		// Loop through all of the lines in the file and read them
		String line = "";
		while ((line = reader.readLine()) != null) {
			// try-catch this just in case something is wrong with the line
			try {
				// Get the data of the waypoint separated by a colon
				String[] data = line.split(":");
				Waypoint waypoint = new Waypoint(data[0], Double.parseDouble(data[1]), Double.parseDouble(data[2]), Double.parseDouble(data[3]), new Color(Integer.parseInt(data[4])));
				waypoints.add(waypoint);
				waypoint.setEnabled(Boolean.parseBoolean(data[5]));
			} catch (Exception e) {
				Hypnotic.LOGGER.error("Something went wrong reading the waypoints file");
				e.printStackTrace();
			}
		}
		
		reader.close();
	}
}
