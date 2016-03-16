package jombie.Control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import jombie.Model.Location;
import jombie.Model.User;
import jombie.View.Map;

public class Game {
	static public final int mapSize_y = 20;
	static public final int mapSize_x = 20;
	private ArrayList<User> userList = null;
	// 
	private final int possibleAttackRange = 2;

	private int maxJombie = 2;
	private int jombieCount;

	String nameDic[] = { "Erick", "John", "Clock", "Cook", "Norm", "Von", "Harris", "Fransis" };
	
	
	private User currentLoginUser;

	private Map map = null;
	private Scanner sc;

	public Game() {
		userList = new ArrayList<>();
		map = new Map(mapSize_y, mapSize_x);
		sc = new Scanner(System.in);
		jombieCount = 0;
//		personCount = 0;
	}

	public Game(ArrayList<User> userList, Map map) {
		this.userList = userList;
		this.map = map;
		sc = new Scanner(System.in);
		jombieCount = 0;
//		personCount = 0;
//		joinedUser = 0;
	}

	public void playSingleGame(int maxUser) {
		showLogin();
		makeOtherUsers(maxUser-1);
		askMe();
	}

	public void askMe() {
		int res;
		while (true) {
			System.out.println("**********");
			System.out.println("1.占쏙옙 占쏙옙占쏙옙 \n2.占쏙옙치占싱듸옙\n3.占쏙옙占쏙옙占쏙옙占쏙옙\n4.占십븝옙占쏙옙(치트占쏙옙占�)\n5.占쏙옙占쏙옙");
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
				System.out.println("占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙絳求占�.");
				return;
			default:
				System.out.println("占쌩몌옙 占쌉뤄옙占싹셨쏙옙占싹댐옙.\nUser choice : " + res);
				break;
			}
		}
	}

	public boolean makeNewUser(String name, int y, int x) {
		if (y < 0 || y >= mapSize_y || x < 0 || x >= mapSize_x)
			return false;
		for (User user : userList) {
			boolean isDuplicated = user.getUserLocation().getLocation_y() == y
					&& user.getUserLocation().getLocation_x() == x;
			if (isDuplicated)
				return false;
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
			if (currentLoginUser.setUserLocation_8D(userList, direction - 1))
				break;
			else
				direction = -1;
		}
	}

	private int askDirection(User user) {
		System.out.println(user.getUserName() + "占쏙옙占쏙옙 占쏙옙占쏙옙 占싱듸옙 占쏙옙키占쌘쏙옙占싹깍옙?");
		System.out.println("1.占쏙옙   2.占쏙옙  3.占쏙옙");
		System.out.println("4.占쏙옙 5.占쏙옙  6.占쏙옙");
		System.out.println("7.占쏙옙   8.占쏙옙  9.占쏙옙");

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
			if (user.isDead())
				continue;
			for (User other : userList) {
				if (other.isDead() || user == other)
					continue;

				int diff_y = other.getUserLocation().getLocation_y() - user.getUserLocation().getLocation_y();
				if (diff_y >= -possibleAttackRange && diff_y <= possibleAttackRange) {
					int diff_x = other.getUserLocation().getLocation_x() - user.getUserLocation().getLocation_x();
					if (diff_x >= -possibleAttackRange && diff_x <= possibleAttackRange)
						if (user.isJombie() ^ other.isJombie()){
							user.addNearEnemy(other);
							if(user.isJombie())
								attack(user, other);
						}
				}
			}
		}
	}
	
	private void attack(User user1, User user2) {
		if (user1.isJombie() && !user2.isJombie()) {
			user2.beAttacked();
			if (user2.getUserHP() <= 0)
				user2.setDead(true);
		}
		else if (!user1.isJombie() && user2.isJombie()) {
			user1.beAttacked();
			if (user1.getUserHP() <= 0)
				user1.setDead(true);
		}
	}

	private boolean makeJombie() {
		boolean isJombie = false;
		if (isJombie = (Math.random() > 0.5) ? (jombieCount < maxJombie ? true : false) : false) {
			jombieCount++;
		} else {
//			personCount++;
		}
		return isJombie;
	}
	public void moveRandom_ExceptMe() {
		for (User user : userList) {
			if (user == currentLoginUser)
				continue;
			user.setUserLocation_8D(userList, (int) (Math.random() * 9));
		}
		moveUser();

		Collections.sort(userList, new CustomComparator());
		isNearEnemy();
	}

	public void showLogin(){
		System.out.print("Input user Nmae : ");
		String userName = sc.next();
		int x = -1, y = -1;

		while (y < 0 || y >= mapSize_y) {
			System.out.print("Input user Location Y(0占싱삼옙 " + (mapSize_y - 1) + "占쏙옙占쏙옙) : ");
			y = sc.nextInt();
		}
		while (x < 0 || x >= mapSize_x) {
			System.out.print("Input user Location X(0占싱삼옙 " + (mapSize_x - 1) + "占쏙옙占쏙옙) : ");
			x = sc.nextInt();
		}
		for (User user : userList) {
			boolean isDuplicated = user.getUserLocation().getLocation_y() == y
					&& user.getUserLocation().getLocation_x() == x;
			if (isDuplicated)
				return;
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
