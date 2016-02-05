package jombie.Control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import jombie.Control.Game.CustomComparator;
import jombie.Model.User;
import jombie.View.Map;

public class GameTest {
	String nameDic[] = { "Erick", "John", "Clock", "Cook", "Norm", "Von", "Harris", "Fransis" };
	// mapSize 변수들이 지금 Game쪽이랑 중복되고있음
	private final int mapSize_y = 10;
	private final int mapSize_x = 10;
	private ArrayList<User> userList;
	private Map map;
	private Game game;

	public GameTest(int maxJombie, int maxUser) {
		userList = new ArrayList<>();
		map = new Map(mapSize_y, mapSize_x);
		game = new Game(userList, map);
		for (int i = 0; i < maxUser; i++) {
			if (!game.makeNewUser(nameDic[i], (int) (Math.random() * game.mapSize_y),
					(int) (Math.random() * game.mapSize_x))) {
				--i;
			}
		}
		map.drawMap(userList);
		game.showUsers();
	}
	public void moveRandom() {
		for(User user : userList){
			user.setUserLocation(userList, (int)(Math.random()*4));
		}
		Collections.sort(userList, new CustomComparator());
		// 공격 호출 안하고 있었다.
		game.isThereAttack_8D();
		map.drawMap(userList);
		game.showUsers();
	}
	//Game class와 중복됨
	public class CustomComparator implements Comparator<User> {
		@Override
		public int compare(User u1, User u2) {
			return u1.compareTo(u2);
		}
	}
	
}
