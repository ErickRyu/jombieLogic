package jombie.Control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Scanner;

import jombie.Model.Location;
import jombie.Model.User;
import jombie.View.Map;

public class Game {
	public static final int mapSize_y = 20;
	public static final int mapSize_x = 20;
	private ArrayList<User> userList;
	private final int possibleAttackRange = 2;

	private int maxJombie = 2;
	private int jombieCount;

	String nameDic[] = { "Erick", "John", "Clock", "Cook", "Norm", "Von", "Harris", "Fransis" };

	private User currentLoginUser;

	private Map map;
	private Scanner sc;

	public Game() {
		userList = new ArrayList<>();
		map = new Map(mapSize_y, mapSize_x);
		sc = new Scanner(System.in);
		jombieCount = 0;
		// personCount = 0;
	}

	public Game(ArrayList<User> userList, Map map) {
		this.userList = userList;
		this.map = map;
		sc = new Scanner(System.in);
		jombieCount = 0;
		// personCount = 0;
		// joinedUser = 0;
	}

	public void playSingleGame(int maxUser) {
		showLogin();
		makeOtherUsers(maxUser - 1);
		askMe();
	}

	public void askMe() {
		int res;
		while (true) {
			System.out.println("**********");
			System.out.println("1.맵 보기 \n2.위치이동\n3.유저정보\n4.맵보기(치트모드)\n5.종료");
			System.out.println("**********");
			res = sc.nextInt();
			switch (res) {
			case 1:
				map.drawMap(userList, currentLoginUser.isJombie(), currentLoginUser.getNearEnemy());
				break;
			case 2:
				moveRandom_ExceptMe();
				break;
			case 3:
				showUsers();
				break;
			case 4:
				map.drawMap(userList);
				break;
			case 5:
				System.out.println("게임이 종료됩니다.");
				return;
			default:
				System.out.println("잘못 입력하셨습니다.\nUser choice : " + res);
				break;
			}
		}
	}

	public boolean makeNewUser(String name, int y, int x) {
		if (y < 0 || y >= mapSize_y || x < 0 || x >= mapSize_x) {
			return false;
		}
		for (User user : userList) {
			boolean isDuplicated = user.getUserLocation().getLocationY() == y
					&& user.getUserLocation().getLocationX() == x;
			if (isDuplicated) {
				return false;
			}
		}
		User user = new User(new Location(y, x), name, makeJombie());
		userList.add(user);
		Collections.sort(userList, new CustomComparator());

		return true;
	}

	private void moveUser() {
		int direction = -1;
		while (direction < 1 || direction > 9) {
			direction = askDirection(currentLoginUser);
			if (currentLoginUser.setUserLocation_8D(userList, direction - 1)) {
				break;
			} else {
				direction = -1;
			}
		}
	}

	private int askDirection(User user) {
		System.out.println(user.getUserName() + "님을 어디로 이동 시키겠습니까?");
		System.out.println("1.↖   2.↑  3.↗");
		System.out.println("4.← 5.■  6.→");
		System.out.println("7.↙   8.↓  9.↘");

		return sc.nextInt();
	}

	public void showUsers() {
		System.out.println("===================");
		for (User user : userList) {
			user.printUserStatus(currentLoginUser.isJombie());
		}
		System.out.println("===================");
	}

	public void isNearEnemy() {
		for (User user : userList) {
			user.resetNearEnemy();
			if (user.isDead()) {
				continue;
			}
			for (User other : userList) {
				if (other.isDead() || user == other) {
					continue;
				}
				int diff_y = other.getUserLocation().getLocationY() - user.getUserLocation().getLocationY();
				if (diff_y >= -possibleAttackRange && diff_y <= possibleAttackRange) {
					int diff_x = other.getUserLocation().getLocationX() - user.getUserLocation().getLocationX();
					if (diff_x >= -possibleAttackRange && diff_x <= possibleAttackRange && user.isJombie() ^ other.isJombie()) {
						user.addNearEnemy(other);
						if (user.isJombie()) {
							attack(user, other);
						}
					}
				}
			}
		}
	}
	public void isNearEnemy(HashMap<String, User> userMap) {
		
		for(User user : userMap.values()){
			user.resetNearEnemy();
			if (user.isDead()) {
				continue;
			}
			for(User other : userMap.values()){
				if (other.isDead() || user == other) {
					continue;
				}
				int diff_y = other.getUserLocation().getLocationY() - user.getUserLocation().getLocationY();
				if (diff_y >= -possibleAttackRange && diff_y <= possibleAttackRange) {
					int diff_x = other.getUserLocation().getLocationX() - user.getUserLocation().getLocationX();
					if (diff_x >= -possibleAttackRange && diff_x <= possibleAttackRange && user.isJombie() ^ other.isJombie()) {
						user.addNearEnemy(other);
						if (user.isJombie()) {
							attack(user, other);
						}
					}
				}
			}
		}
	}
	private void attack(User user1/*<< login user*/, User user2) {
		if (user1.isJombie() && !user2.isJombie()) {
			user2.beAttacked();
			if (user2.getUserHP() <= 0) {
				user2.setDead(true);
			}
		} else if (!user1.isJombie() && user2.isJombie()) {
			user1.beAttacked();
			if (user1.getUserHP() <= 0) {
				user1.setDead(true);
			}
		}
	}

	private boolean makeJombie() {
		boolean isJombie = false;
		if (isJombie = (Math.random() > 0.5) ? (jombieCount < maxJombie) : false) {
			jombieCount++;
		}
		return isJombie;
	}

	public void moveRandom_ExceptMe() {
		for (User user : userList) {
			if (user == currentLoginUser) {
				continue;
			}
			user.setUserLocation_8D(userList, (int) (Math.random() * 9));
		}
		moveUser();

		Collections.sort(userList, new CustomComparator());
		isNearEnemy();
	}

	public void showLogin() {
		System.out.print("Input user Nmae : ");
		String userName = sc.next();
		int x = -1, y = -1;

		while (y < 0 || y >= mapSize_y) {
			System.out.print("Input user Location Y(0이상 " + (mapSize_y - 1) + "이하) : ");
			y = sc.nextInt();
		}
		while (x < 0 || x >= mapSize_x) {
			System.out.print("Input user Location X(0이상 " + (mapSize_x - 1) + "이하) : ");
			x = sc.nextInt();
		}
		for (User user : userList) {
			boolean isDuplicated = user.getUserLocation().getLocationY() == y
					&& user.getUserLocation().getLocationX() == x;
			if (isDuplicated) {
				return;
			}
		}
		Location loc = new Location(y, x);

		User user = new User(loc, userName, makeJombie());
		userList.add(user);

		logIn(user);
	}

	public void logIn(User loginUser) {
		currentLoginUser = loginUser;
	}

	public void makeOtherUsers(int maxUser) {
		for (int i = 0; i < maxUser; i++) {
			if (!makeNewUser(nameDic[i], (int) (Math.random() * mapSize_y), (int) (Math.random() * mapSize_x))) {
				--i;
			}
		}
	}

	public class CustomComparator implements Comparator<User> {
		@Override
		public int compare(User u1, User u2) {
			return u1.compareTo(u2);
		}
	}
}
