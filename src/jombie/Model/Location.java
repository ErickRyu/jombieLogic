package jombie.Model;
import jombie.Control.*;
public class Location {
	static protected final int direction_y[] = { -1, 0, 1, 0 };
	static protected final int direction_x[] = { 0, 1, 0, -1};
	
	private int location_y;
	private int location_x;
	
	public Location(int y, int x) {
		this.location_y = y;
		this.location_x = x;
	}

	public void setLocation(int y, int x) {
		this.location_y = y;
		this.location_x = x;
	}

	public void moveDirect(int move) {
		int next_y = location_y + direction_y[move];
		int next_x = location_x + direction_x[move]; 
		if(next_y >= 0 && next_y <= Game.mapSize_y)
			location_y = next_y;
		if(next_x >= 0 && next_x <= Game.mapSize_x)
			location_x += direction_x[move];
		
	}
	public int getLocation_y() {
		return this.location_y;
	}
	public int getLocation_x() {
		return this.location_x;
	}
}
