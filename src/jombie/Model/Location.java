package jombie.Model;

import java.util.ArrayList;

import jombie.Control.Game;

public class Location {
	static protected final int direction_8D_y[] = { -1, -1, -1, 0, 0, 0, 1, 1, 1 };
	static protected final int direction_8D_x[] = { -1, 0, 1, -1, 0, 1, -1, 0, 1 };

	private int location_y;
	private int location_x;

	public Location(int y, int x) {
		this.location_y = y;
		this.location_x = x;
	}
	// userList 받아와서 중복 체크해야하는데 호출 하는 곳이 User 클래스여서 다시 건드려야할 필요성있음
	// game클래스에서 setLocation날리는 걸로 변경
	public boolean setLocation(int y, int x) {
		if (canGo(y, x)) {
			// if (isDuplicated(userList, y, x)) return false;
			this.location_y = y;
			this.location_x = x;
			 return true;
		} else
			return false;

	}

//	public boolean moveDirect(ArrayList<User> userList, int move) {
//		int next_y = location_y + direction_y[move];
//		int next_x = location_x + direction_x[move];
//		// userList를 다 탐색하는게 좋을지
//		// 나중에 map이 몇십만이상으로 커질 경우 배열은 부적절하지 않을까 생각
//		// user는 일단 10명정도로 예상하고 있으므로 유저를 모두 탐색하는게 메모리나 속도 전체에서 가장 효율적이지 않을까 생각
//		
//		if (canGo(next_y, next_x) && !isDuplicated(user, userList, next_y, next_x)) {
//			location_y = next_y;
//			location_x = next_x;
//			return true;
//		}
//		return false;
//	}
	
	public boolean moveDirect_8D(User user, ArrayList<User> userList, int move) {
		int next_y = location_y + direction_8D_y[move];
		int next_x = location_x + direction_8D_x[move];
		
		if (canGo(next_y, next_x) && !isDuplicated(user, userList, next_y, next_x)) {
			location_y = next_y;
			location_x = next_x;
			return true;
		}
		return false;
	}
	
	
	// 이동 가능한 범위인지 체크
	public boolean canGo(int y, int x) {
		return y >= 0 && y <= Game.mapSize_y - 1 && x >= 0 && x <= Game.mapSize_x - 1;
	}
	// 다른 유저와 중복된 위치를 가지진 않았는지 체크
	public boolean isDuplicated(User me, ArrayList<User> userList, int y, int x) {
		for (User user : userList) {
			if(user == me)continue;
			if (user.getUserLocation().location_y == y && user.getUserLocation().location_x == x) {
				return true;
			}
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
