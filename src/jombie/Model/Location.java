package jombie.Model;
import java.util.ArrayList;

import jombie.Control.Game;
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

	public boolean moveDirect(ArrayList<User> userList, int move) {
		int next_y = location_y + direction_y[move];
		int next_x = location_x + direction_x[move];
		// userList를 다 탐색하는게 좋을지
		// 나중에 map이 몇십만이상으로 커질 경우 배열은 부적절하지 않을까 생각
		// user는 일단 10명정도로 예상하고 있으므로 유저를 모두 탐색하는게 메모리나 속도 전체에서 가장 효율적이지 않을까 생각
		boolean canGo = next_y >= 0 && next_y <= Game.mapSize_y-1 && next_x >= 0 && next_x <= Game.mapSize_x-1; 
		boolean isDuplicated = false;
		if(canGo){
			for(User user : userList){
				isDuplicated = user.getUserLocation().location_y == next_y && user.getUserLocation().location_x == next_x; 
				if(isDuplicated){
					return false;
				}
			}
			location_y = next_y;
			location_x = next_x;
			return true;
		}
		return false;
	}
	public int getLocation_y() {
		return this.location_y;
	}
	public int getLocation_x() {
		return this.location_x;
	}
}
