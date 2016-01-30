package jombie.View;

import java.util.ArrayList;

import jombie.Model.User;

public class Map {
	protected int mapSize_y;
	protected int mapSize_x;

	public Map(int y, int x) {
		mapSize_y = y;
		mapSize_x = x;
	}

	public void drawMap(ArrayList<User> userList) {
		// sparseMatrix 떠올리면 될 것 같음.
		int last_y = -1;
		int last_x = -1;
		// 이 방식 문제점 : 유저 좌표가 중복 될 경우 중복해서 계속 출력함.
		String dot;
		for (User user : userList) {
			int currentUserPosition_y = user.getUserLocation().getLocation_y();
			int currentUserPosition_x = user.getUserLocation().getLocation_x();
			// 중복되는 경우 출력하지 않으려고 했음
			// 제대로 처리했는지는 잘 모르겠는데, 일단 화면에 중복되지는 않는다.
			// 문제가 있을거라고 생각했는데, 아니네..
			if (last_y == currentUserPosition_y && last_x == currentUserPosition_x)
				continue;
			else {
				last_x++;
			}
			if (last_y == -1) {
				last_y = 0;
			}

			for (; last_y < currentUserPosition_y; last_y++, last_x = 0) {
				dot = "";
				for (; last_x < mapSize_x; last_x++) {
					dot += ". ";
				}
				System.out.println(dot);
			}
			dot = "";
			for (; last_x < currentUserPosition_x; last_x++) {
				dot += ". ";
			}
			System.out.print(dot);
			System.out.print(user.isDead()? ". " : "* ");
		}
		++last_x;
		for (; last_y < mapSize_y; last_y++, last_x = 0) {
			dot = "";
			for (; last_x < mapSize_x; last_x++) {
				dot += ". ";
			}
			System.out.println(dot);
		}
		System.out.println();
	}

	public void drawUser(User user) {

	}
}
