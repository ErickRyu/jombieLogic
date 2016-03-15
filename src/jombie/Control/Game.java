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
	// 占쌀쇽옙占쏙옙 0.1 -> 1
	// 占쏙옙占쏙옙占쏙옙 占쌉뤄옙占쏙옙占쌍는걸뤄옙 占쏙옙占쏙옙.
	// 
	private final int possibleAttackRange = 2;

	private int maxJombie = 2;
	private int jombieCount;

	String nameDic[] = { "Erick", "John", "Clock", "Cook", "Norm", "Von", "Harris", "Fransis" };
	// mapSize 占쏙옙占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙 Game占쏙옙占싱띰옙 占쌩븝옙占실곤옙占쏙옙占쏙옙
	
	
	private User currentLoginUser;

	private Map map = null;
	private Scanner sc;
	// 占쏙옙占쏙옙 占쏙옙 占쏙옙 占쏙옙占쏙옙黴占쏙옙歐占�? 占쏙옙 占쏙옙占쏙옙 占쏙옙占쏙옙占�
	// 占쏙옙占쏙옙占쏙옙占� 占싯아쇽옙 占쏙옙占쏙옙占쏙옙.
	// 占쏙옙占쏙옙 占쏙옙占쏙옙占싹댐옙 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙
	// 占쏙옙占쏙옙占쏙옙占쏙옙 占쏙옙표占쏙옙 占쏙옙占� 占쏙옙占쏙옙占싹곤옙, map占쏙옙占쏙옙 占쏘떻占쏙옙 占쏙옙占� 占쏙옙占싸곤옙?
	// 占쏙옙占쏙옙 占쏙옙占� 占쏙옙占쌔다댐옙

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
		// 占쏙옙占쏙옙
		Collections.sort(userList, new CustomComparator());

		// map.drawMap(userList);

		return true;
	}

	// 占싸깍옙占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙치占싱듸옙
	private void moveUser() {
		int direction = -1;
		while (direction < 1 || direction > 9) {
			direction = askDirection(currentLoginUser);
			// 占싱듸옙 占쏙옙占쏙옙占쏙옙 占쏙옙치占쏙옙 占쌉력뱄옙占쏙옙 占쏙옙占� 占싱듸옙占쏙옙키占쏙옙 占쌥븝옙占쏙옙 占쏙옙占쏙옙
			if (currentLoginUser.setUserLocation_8D(userList, direction - 1))
				break;
			// 占쌩븝옙占실거놂옙 占싱듸옙 占쌀곤옙占쏙옙占쏙옙 占쏙옙치占쏙옙 占쌉력듸옙 占쏙옙占� 占쌥븝옙占쏙옙 占쌕쏙옙 占쏙옙占쏙옙
			else
				direction = -1;
		}
	}

	// 占싱듸옙 占쏙옙占쏙옙 占쏙옙占쏙옙
	private int askDirection(User user) {
		System.out.println(user.getUserName() + "占쏙옙占쏙옙 占쏙옙占쏙옙 占싱듸옙 占쏙옙키占쌘쏙옙占싹깍옙?");
		System.out.println("1.占쏙옙   2.占쏙옙  3.占쏙옙");
		System.out.println("4.占쏙옙 5.占쏙옙  6.占쏙옙");
		System.out.println("7.占쏙옙   8.占쏙옙  9.占쏙옙");

		return sc.nextInt();
	}

	// 占쏙옙占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙
	public void showUsers() {
		System.out.println("===================");
		for (User user : userList) {
//			user.printUserStatus();
			user.printUserStatus(currentLoginUser.isJombie());
		}
		System.out.println("===================");
	}

	// 占싸곤옙 占쌍븝옙占쏙옙占쏙옙 占쏙옙占쏙옙 占쌍댐옙占쏙옙占쏙옙 占쏙옙占실깍옙 占쏙옙占쏙옙 占쏙옙占쌥곤옙占심뱄옙占쏙옙占쏙옙 占쏙옙占쏙옙 占쌍댐옙占쏙옙 탐占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙 (8占쏙옙占쏙옙)
	public void isNearEnemy() {
		// 占썩본 占쏙옙占싱듸옙占�
		// 占쏙옙占쏙옙謎占쏙옙占� 처占쏙옙 占쏙옙占쏙옙謎占쏙옙占� 占쏙옙占쏙옙占싶쇽옙 占싯삼옙占싼댐옙.
		// y占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占� y占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占� 탐占쏙옙占싼댐옙.
		// otherY - d <= y <- otherY +1 占싱띰옙占� 占쏙옙占� 탐占쏙옙占싼댐옙.
		// y占쏙옙占쏙옙 1 占십곤옙占쏙옙 占쏙옙占싱곤옙 占쏙옙占쌕몌옙 탐占쏙옙占쏙옙 占쏙옙占쏙옙占�.
		
		for (User user : userList) {
			// nearEnemy 占십깍옙화
			user.resetNearEnemy();
			// 占쏙옙占쏙옙 占쏙옙占쏙옙渼占� 占쏙옙占쏙옙
			if (user.isDead())
				continue;
			for (User other : userList) {
				// 占쏙옙占쏙옙 占쏙옙占쏙옙占�, 占쏙옙 占쏙옙占쏙옙占� 占쌘쏙옙占쏙옙 占쏙옙占� 占싼깍옙
				if (other.isDead() || user == other)
					continue;

				int diff_y = other.getUserLocation().getLocation_y() - user.getUserLocation().getLocation_y();
				if (diff_y >= -possibleAttackRange && diff_y <= possibleAttackRange) {
					int diff_x = other.getUserLocation().getLocation_x() - user.getUserLocation().getLocation_x();
					if (diff_x >= -possibleAttackRange && diff_x <= possibleAttackRange)
						// 占쌩븝옙占실댐옙 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙 占쏙옙占쌔쇽옙 user占쏙옙 占쏙옙占쏙옙占싸곤옙占쏙옙 占쏙옙占쏙옙占쏙옙占싸몌옙 attack占쏙옙 호占쏙옙占싼댐옙.
						// 占쌩븝옙占실댐옙 占쏙옙李� 占쌍댐옙占쏙옙 占쌕쏙옙 占쏙옙占쏙옙占쌔븝옙占쌩곤옙占쏙옙
						if (user.isJombie() ^ other.isJombie()){
							// 占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙 attackableUser占쏙옙 占쏙옙占쏙옙占쏙옙占�
							user.addNearEnemy(other);
							// user占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙 占쌕뤄옙 占쏙옙占쏙옙
							// 占쌍븝옙占쏙옙 占쏙옙 占쏙옙 占쏙옙占쏙옙占싹듸옙占쏙옙 占쏙옙占쏙옙占쌔억옙 占쏙옙 占쏙옙 占쏙옙占쏙옙.
							if(user.isJombie())
								attack(user, other);
						}
				}
			}
		}
	}
	
	// UserControl, MapControl 占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙 占쏙옙 占쏙옙占쏙옙
	private void attack(User user1, User user2) {
		// 占쏙옙占쏙옙占� 占쏙옙占쏙옙 占쏙옙占썩서 占쏙옙占쏙옙占쌀깍옙?
		// hp占쏙옙 占식억옙占쌔쇽옙 0 占쏙옙占쏙옙占쏙옙 占쏙옙占� 占쏙옙占싸댐옙.

		// user1占쏙옙 占쏙옙占쏙옙占싱곤옙 user2占쏙옙 占쏙옙占쏙옙占� 占쏙옙占�
		if (user1.isJombie() && !user2.isJombie()) {
			user2.beAttacked();
			if (user2.getUserHP() <= 0)
				user2.setDead(true);
		}
		// user1占쏙옙 占쏙옙占쏙옙隔占� user2占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占�
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
/*	占싹댐옙 占쏙옙占쏙옙
	 public void moveRandom() {
	 for (User user : userList) {
	 user.setUserLocation(userList, (int) (Math.random() * 4));
	 }
	 Collections.sort(userList, new CustomComparator()); // 占쏙옙占쏙옙 호占쏙옙 占쏙옙占싹곤옙 占쌍억옙占쏙옙.
	 // isAttakable_8D();
	 map.drawMap(userList);
	 showUsers();
	 }
*/
	public void moveRandom_ExceptMe() {
		// 占싸깍옙占쏙옙 占쏙옙占쏙옙(占쏙옙占싸곤옙) 占쏙옙占쏙옙占싹곤옙 占쏙옙占� 占쏙옙占쏙옙 占싱듸옙
		for (User user : userList) {
			if (user == currentLoginUser)
				continue;
			user.setUserLocation_8D(userList, (int) (Math.random() * 9));
		}
		moveUser();

		Collections.sort(userList, new CustomComparator());
		isNearEnemy();
//		map.drawMap(userList);
//		showUsers();
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
		// 占쌕쏙옙 占쏙옙치占쏙옙占쏙옙占쏙옙 占쏙옙占쏘가占썽서 占쌩븝옙占쏙옙 占쏙옙치 찾占쏙옙 占쏙옙占쏙옙
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
//		map.drawMap(userList);
//		showUsers();
	}

	// item 占쏙옙占쏙옙 占쌨소듸옙
	private int[] makeItem() {
		// 占쏙옙 占쏙옙 占쏘떻占쏙옙 占쏙옙占쏘를 占쏙옙占쏙옙占�
		// 占십울옙 占쏙옙占쏙옙占� 占쏙옙칠占쏙옙占� 占쏘떻占쏙옙 占쏙옙占쏙옙?
		// 占쏙옙占쏙옙占� 占쌓놂옙 占쏙옙占쏙옙占쏙옙煞占쏙옙占�?
		// 占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙占싹곤옙 占쏙옙占쏙옙 占쏙옙占쏙옙?
		// 占싱듸옙 占식울옙 占쏙옙占쏙옙占싹곤옙 占쏙옙占쏙옙 占쏙옙치占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙占쏙옙 占쌕뤄옙 占쏙옙占쏙옙占쏙옙?
		// 占싱듸옙 占식울옙 占쏙옙占쏙옙 -> 占쏙옙占쏙옙占쌜곤옙 占쏙옙占쏙옙 占쏙옙치 확占쏙옙

		int[] itemLocation = new int[2];

		// 10%占쏙옙 확占쏙옙占쏙옙 占쏙옙占쏙옙占쏙옙 占쏙옙占쏙옙
		if (Math.random() > 0.9) {
			itemLocation[0] = (int) (Math.random() * mapSize_y);
			itemLocation[1] = (int) (Math.random() * mapSize_x);
		} else {
			itemLocation[0] = -1;
			itemLocation[1] = -1;

		}
		return itemLocation;
	}

	public class CustomComparator implements Comparator<User> {
		@Override
		public int compare(User u1, User u2) {
			return u1.compareTo(u2);
		}
	}
}
