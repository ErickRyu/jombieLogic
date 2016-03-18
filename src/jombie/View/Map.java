package jombie.View;

import java.io.PrintWriter;
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
			int currentUserPosition_y = user.getUserLocation().getLocationY();
			int currentUserPosition_x = user.getUserLocation().getLocationX();
			// 중복되는 경우 출력하지 않으려고 했음
			// 제대로 처리했는지는 잘 모르겠는데, 일단 화면에 중복되지는 않는다.
			// 문제가 있을거라고 생각했는데, 아니네..
			if (last_y == currentUserPosition_y && last_x == currentUserPosition_x) {
				continue;
			} else {
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
//			System.out.print(user.isDead()? ". " : "* ");
			
			// 누구인지 이름의 첫 글자를 표시
			System.out.print(user.isDead()? ". " : user.getUserName().charAt(0) + " ");
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

	/**
	 * User에 맞게 맵을 보여줌
	 * 좀비는 좀비만 볼 수 있고, 사람은 사람만 볼 수 있음.
	 */
	public void drawMap(ArrayList<User> userList, boolean isJombie, ArrayList<User> nearEnemy) {
		int last_y = -1;
		int last_x = -1;
		String dot;
		
		for (User user : userList) {
			int currentUserPosition_y = user.getUserLocation().getLocationY();
			int currentUserPosition_x = user.getUserLocation().getLocationX();
			// 중복되는 경우 출력하지 않으려고 했음
			// 제대로 처리했는지는 잘 모르겠는데, 일단 화면에 중복되지는 않는다.
			// 문제가 있을거라고 생각했는데, 아니네..
			if (last_y == currentUserPosition_y && last_x == currentUserPosition_x) {
				continue;
			} else {
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
//			System.out.print(user.isDead()? ". " : "* ");
			
			// 누구인지 이름의 첫 글자를 표시
			// 좀비는 좀비의 위치만, 사람은 사람의 위치만 볼 수 있음.
			// 죽은 경우는 맵에서 띄우지 않음.
			// LoginUser가 좀비일 때, 공격 가능 위치에 있는 사람은 맵에 띄우게 했음.
			// 치트모드로 동작하고있음
			if(!user.isDead() && (!user.isJombie()^isJombie || nearEnemy.contains(user))){
				System.out.print(user.getUserName().charAt(0) + " ");
			}else{
				System.out.print(". ");
			}
			
//			System.out.print(user.isDead()? ". " : !(user.isJombie()^isJombie)? (user.getUserName().charAt(0) + " ") : ". ");
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
	
	public void drawMapToWriter(PrintWriter writer, ArrayList<User> userList, boolean isJombie, ArrayList<User> nearEnemy){
		int last_y = -1;
		int last_x = -1;
		String dot;
		
		for (User user : userList) {
			int currentUserPosition_y = user.getUserLocation().getLocationY();
			int currentUserPosition_x = user.getUserLocation().getLocationX();
			// 중복되는 경우 출력하지 않으려고 했음
			// 제대로 처리했는지는 잘 모르겠는데, 일단 화면에 중복되지는 않는다.
			// 문제가 있을거라고 생각했는데, 아니네..
			if (last_y == currentUserPosition_y && last_x == currentUserPosition_x) {
				continue;
			} else {
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
				writer.println(dot);
			}
			dot = "";
			for (; last_x < currentUserPosition_x; last_x++) {
				dot += ". ";
			}
			writer.print(dot);
//			writer.print(user.isDead()? ". " : "* ");
			
			// 누구인지 이름의 첫 글자를 표시
			// 좀비는 좀비의 위치만, 사람은 사람의 위치만 볼 수 있음.
			// 죽은 경우는 맵에서 띄우지 않음.
			// LoginUser가 좀비일 때, 공격 가능 위치에 있는 사람은 맵에 띄우게 했음.
			// 치트모드로 동작하고있음
			if(!user.isDead() && (!user.isJombie()^isJombie || nearEnemy.contains(user))){
				writer.print(user.getUserName().charAt(0) + " ");
			}else{
				writer.print(". ");
			}
			
//			writer.print(user.isDead()? ". " : !(user.isJombie()^isJombie)? (user.getUserName().charAt(0) + " ") : ". ");
		}
		++last_x;
		for (; last_y < mapSize_y; last_y++, last_x = 0) {
			dot = "";
			for (; last_x < mapSize_x; last_x++) {
				dot += ". ";
			}
			writer.println(dot);
		}
		writer.println();
		writer.flush();
	}
	
}
