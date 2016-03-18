package jombie.Model;

import java.util.ArrayList;

import jombie.Control.Game;

public class Location {
	protected static final int directionY[] = { -1, -1, -1, 0, 0, 0, 1, 1, 1 };
	protected static final int directionX[] = { -1, 0, 1, -1, 0, 1, -1, 0, 1 };

	private int location_y;
	private int location_x;

	public Location(int y, int x) {
		this.location_y = y;
		this.location_x = x;
	}
	/**
	 * UserList 받아와서 중복 체크해야하는데 호출 하는 곳이 User 클래스여서 다시 건드려야할 필요성있음
	 * game클래스에서 setLocation날리는 걸로 변경.
	 */
	public boolean setLocation(int y, int x) {
		if (canGo(y, x)) {
			// if (isDuplicated(userList, y, x)) return false;
			this.location_y = y;
			this.location_x = x;
			 return true;
		} else {
			return false;
		}

	}

	public boolean moveDirect_8D(User user, ArrayList<User> userList, int move) {
		int next_y = location_y + directionY[move];
		int next_x = location_x + directionX[move];
		
		if (canGo(next_y, next_x) && !isDuplicated(user, userList, next_y, next_x)) {
			location_y = next_y;
			location_x = next_x;
			return true;
		}
		return false;
	}
	
	/** 이동 가능한 범위인지 체크. */
	public boolean canGo(int y, int x) {
		return y >= 0 && y <= Game.mapSize_y - 1 && x >= 0 && x <= Game.mapSize_x - 1;
	}
	/** 다른 유저와 중복된 위치를 가지진 않았는지 체크. */
	public boolean isDuplicated(User me, ArrayList<User> userList, int y, int x) {
		for (User user : userList) {
			if(user == me) {
				continue;
			}
			if (user.getUserLocation().location_y == y && user.getUserLocation().location_x == x) {
				return true;
			}
		}
		return false;
	}

	public int getLocationY() {
		return this.location_y;
	}

	public int getLocationX() {
		return this.location_x;
	}
}
