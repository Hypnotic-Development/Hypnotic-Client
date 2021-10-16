package badgamesinc.hypnotic.waypoint;

import java.util.List;

import com.google.common.collect.Lists;

public class WaypointManager {

	public static WaypointManager INSTANCE = new WaypointManager();
	
	public List<Waypoint> waypoints = Lists.newArrayList();
	
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
}
