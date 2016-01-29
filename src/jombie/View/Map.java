package jombie.View;
import java.util.ArrayList;

import jombie.Model.User;
public class Map {
	protected int mapSize_y;
	protected int mapSize_x;
	
	public Map(int y, int x){
		mapSize_y = y;
		mapSize_x = x;
	}
	public void drawMap(ArrayList<User> userList){
		// sparseMatrix 떠올리면 될 것 같음.
		// 나중에 다시 user는 y, x순으로 정렬해야겠음.
		int last_y = 0;
		int last_x = 0;
		// 이 방식 문제점 : 유저 좌표가 중복 될 경우 중복해서 계속 출력함.
		for(User user : userList){
			int currentUserPosition_y = user.getUserLocation().getLocation_y();
			int currentUserPosition_x = user.getUserLocation().getLocation_x();
			
			for(; last_y < currentUserPosition_y; last_y++, last_x = 0){
				
				for(; last_x < mapSize_x; last_x++){
					//계속  println 호출하는게 더러움
					System.out.print(". ");
				}
				System.out.println();
			}
			
			for(; last_x < currentUserPosition_x; last_x++){
				System.out.print(". ");
			}
			System.out.print("* ");
			++last_x;
		}
		for(; last_y < mapSize_y; last_y++, last_x = 0){
			for(; last_x < mapSize_x; last_x++){
				System.out.print(". ");
			}
			System.out.println();			
		}
	}
	public void drawUser(User user){
		
	}
}
