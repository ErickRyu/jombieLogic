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

	private final int possibleAttackRange = 2;

	private int maxJombie = 2;
	private int jombieCount;
	private int personCount;
	private int joinedUser;

	String nameDic[] = { "Erick", "John", "Clock", "Cook", "Norm", "Von", "Harris", "Fransis" };
	// mapSize 변수들이 지금 Game쪽이랑 중복되고있음

	private User currentLoginUser;

	private Map map = null;
	private Scanner sc;
	// 좀비를 몇 명 만들겠습니까? 를 먼저 만들고
	// 좀비들은 알아서 움직임.
	// 좀비를 공격하는 아이템 구현
	// 아이템의 좌표는 어디에 저장하고, map에는 어떻게 띄울 것인가?
	// 나는 계속 피해다님

	public Game() {
		userList = new ArrayList<>();
		map = new Map(mapSize_y, mapSize_x);
		sc = new Scanner(System.in);
		jombieCount = 0;
		personCount = 0;
	}

	public Game(ArrayList<User> userList, Map map) {
		this.userList = userList;
		this.map = map;
		sc = new Scanner(System.in);
		jombieCount = 0;
		personCount = 0;
		joinedUser = 0;
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
			System.out.println("1.맵 보기 \n2.위치이동\n3.유저정보\n4.맵보기(치트모드)\n5.종료");
			System.out.println("**********");
			res = sc.nextInt();
			switch (res) {
			case 1:
				map.drawMap(userList, currentLoginUser.isJombie(), currentLoginUser.attackableUsers);
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
/*	No more Use
	private void makeNewUser() {
		// 방금 map size오버해서 집어넣었는데 들어가졌음
		// 어디서 에러난 건지 찾아서 고칠것

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
		// 다시 위치정보를 물어가면서 중복된 위치 찾진 않음
		for (User user : userList) {
			boolean isDuplicated = user.getUserLocation().getLocation_y() == y
					&& user.getUserLocation().getLocation_x() == x;
			if (isDuplicated)
				return;
		}
		Location loc = new Location(y, x);

		User user = new User(loc, userName, makeJombie());
		userList.add(user);
		// 정렬
		Collections.sort(userList, new CustomComparator());

		map.drawMap(userList);
	}
*/
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
		// 정렬
		Collections.sort(userList, new CustomComparator());

		// map.drawMap(userList);

		return true;
	}
/* No more Use
	private void moveUsers() {
		// 중복되는 위치를 가지지 않도록 변경하려고 함.
		// 유저를 만들경우에도 중복되는위치 수정해야함ㄴ
		for (User user : userList) {
			// 죽은 유저의 경우 이도을 묻지 않는다.
			if (user.isDead())
				continue;
			int direction = -1;
			while (direction < 1 || direction > 9) {
				direction = askDirection(user);
				// 이동 가능한 위치로 입력받은 경우 이동시키고 반복문 종료
				if (user.setUserLocation_8D(userList, direction - 1))
					break;
				// 중복되거나 이동 불가능한 위치로 입력된 경우 반복문 다시 실행
				else
					direction = -1;
			}

		}
		// 정렬
		Collections.sort(userList, new CustomComparator());

		// isAttakable();
		// 8방위 공격으로 변경했음.
		isAttakable_8D();
		// 다시 맵에 그리기
		map.drawMap(userList);
	}
*/
	// 로그인 유저만 위치이동
	private void moveUser() {
		int direction = -1;
		while (direction < 1 || direction > 9) {
			direction = askDirection(currentLoginUser);
			// 이동 가능한 위치로 입력받은 경우 이동시키고 반복문 종료
			if (currentLoginUser.setUserLocation_8D(userList, direction - 1))
				break;
			// 중복되거나 이동 불가능한 위치로 입력된 경우 반복문 다시 실행
			else
				direction = -1;
		}
	}

	// 이동 범위 묻기
	private int askDirection(User user) {
		System.out.println(user.getUserName() + "님을 어디로 이동 시키겠습니까?");
		System.out.println("1.↖   2.↑  3.↗");
		System.out.println("4.← 5.■  6.→");
		System.out.println("7.↙   8.↓  9.↘");

		return sc.nextInt();
	}

	// 유저정보 띄우기
	public void showUsers() {
		System.out.println("===================");
		for (User user : userList) {
//			user.printUserStatus();
			user.printUserStatus(currentLoginUser.isJombie());
		}
		System.out.println("===================");
	}
/* No more Use
	private void isAttakable() {
		// 일단 맵에 겹치는 경우는 제외한다.
		// 수평으로 붙어있는(x좌표 1차이) 경우만 따지고 공격을 실행한다.
		User lastUser = null;
		User currentUser = null;
		for (int i = 1; i < userList.size(); i++) {
			lastUser = userList.get(i - 1);
			currentUser = userList.get(i);

			// 붙어 있는 사람이 죽은 사람은 아닌지 확인한다.
			if (lastUser.isDead() || currentUser.isDead())
				continue;

			// 붙어있는 유저들이 있는지 찾는다.
			boolean isNear = lastUser.getUserLocation().getLocation_y() == currentUser.getUserLocation().getLocation_y()
					&& lastUser.getUserLocation().getLocation_x() + 1 == currentUser.getUserLocation().getLocation_x();
			if (isNear) {
				// 사람과 좀비의 관계인지 찾는다.
				boolean isFirstJombie = lastUser.isJombie();
				boolean isSecondJombie = currentUser.isJombie();
				if (isFirstJombie || isSecondJombie) {
					// 공격한다.
					attack(lastUser, currentUser);
				}
			}
		}
	}
*/
	// 8방위 공격이 가능한지 살핀다.
	public void isAttakable_8D() {
		// 기본 아이디어
		// 사용자별로 처음 사용자부터 가져와서 검색한다.
		// y축으로 비교했을 경우 y가 같으면 계속 탐색한다.
		// otherY - d <= y <- otherY +1 이라면 계속 탐색한다.
		// y축이 1 초과로 차이가 난다면 탐색을 멈춘다.
		
		for (User user : userList) {
			//attackableUsers 초기화
			user.attackableUsers = new ArrayList<>();
			// 죽은 사용자는 넘김
			if (user.isDead())
				continue;
			for (User other : userList) {
				// 죽은 사용자, 비교 대상이 자신일 경우 넘김
				if (other.isDead() || user == other)
					continue;

				int diff_y = other.getUserLocation().getLocation_y() - user.getUserLocation().getLocation_y();
				if (diff_y >= -possibleAttackRange && diff_y <= possibleAttackRange) {
					int diff_x = other.getUserLocation().getLocation_x() - user.getUserLocation().getLocation_x();
					if (diff_x >= -possibleAttackRange && diff_x <= possibleAttackRange)
						// 중복되는 공격을 막기 위해서 user가 좀비인가를 기준으로만 attack을 호출한다.
						if (user.isJombie()){
							// 공격 가능한 유저를 attackableUser에 집어넣음
							user.attackableUsers.add(other);
							attack(user, other);
						}

				}
			}
		}
	}

	private void attack(User user1, User user2) {
		// 사라질 놈을 여기서 결정할까?
		// hp를 파악해서 0 이하일 경우 죽인다.

		// user1이 좀비이고 user2가 사람일 경우
		if (user1.isJombie() && !user2.isJombie()) {
			user2.beAttacked();
			if (user2.getUserHP() <= 0)
				user2.setDead(true);
		}
		// user1이 사람이고 user2가 좀비인 경우
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
			personCount++;
		}
		return isJombie;
	}
/*	일단 놔둠
	 public void moveRandom() {
	 for (User user : userList) {
	 user.setUserLocation(userList, (int) (Math.random() * 4));
	 }
	 Collections.sort(userList, new CustomComparator()); // 공격 호출 안하고 있었다.
	 // isAttakable_8D();
	 map.drawMap(userList);
	 showUsers();
	 }
*/
	public void moveRandom_ExceptMe() {
		// 로그인 유저(주인공) 제외하고 모든 유저 이동
		for (User user : userList) {
			if (user == currentLoginUser)
				continue;
			user.setUserLocation_8D(userList, (int) (Math.random() * 9));
		}
		moveUser();

		Collections.sort(userList, new CustomComparator());
		isAttakable_8D();
//		map.drawMap(userList);
//		showUsers();
	}

	public void showLogin(){
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
		// 다시 위치정보를 물어가면서 중복된 위치 찾진 않음
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

	// item 생성 메소드
	private int[] makeItem() {
		// 맵 상에 어떻게 띄울까를 고민중
		// 맵에 사람과 겹칠경우 어떻게 하지?
		// 사람을 그냥 띄워놔야겠지?
		// 언제 아이템을 생성하고 언제 먹지?
		// 이동 후에 생성하고 현재 위치에서 겹쳐지면 바로 먹을까?
		// 이동 후에 생성 -> 아이템과 유저 위치 확인

		int[] itemLocation = new int[2];

		// 10%의 확률로 아이템 생성
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
