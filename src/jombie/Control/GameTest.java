package jombie.Control;

import java.util.ArrayList;

import jombie.Model.User;
import jombie.View.Map;

public class GameTest {
	String nameDic[] = {
			"Erick", "John", "Clock", "Cook", "Norm", "Von", "Harris", "Fransis"
	};
	//지금 Game쪽이랑 중복되고있음
	private final int mapSize_y = 10;
	private final int mapSize_x = 10;
	
	public GameTest(int maxJombie, int maxUser){
		ArrayList<User> userList = new ArrayList<>();
		Map map = new Map(mapSize_y, mapSize_x);
		Game game = new Game(userList, map);
		for (int i = 0; i < maxUser; i++){
			if(!game.makeNewUser(nameDic[i], (int) (Math.random() * game.mapSize_y), (int) (Math.random() * game.mapSize_x))){
				--i;
			}
		}
		map.drawMap(userList);
		game.showUsers();
	}
}
