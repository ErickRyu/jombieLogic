package jombie.Control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import jombie.Model.Location;
import jombie.Model.User;
import jombie.View.Map;

public class Game {
	static public final int mapSize_y = 10;
	static public final int mapSize_x = 10;
	private ArrayList<User> userList = null;
	
	private int maxJombie;
	private int jombieCount;
	private int personCount;
	
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
	}

	public void playGame() {

		int res;
		while (true) {
			System.out.println("**********");
			System.out.println("1.새 유저 \n2.위치이동\n3.유저정보\n4.종료");
			System.out.println("**********");
			res = sc.nextInt();
			switch (res) {
			case 1:
				makeNewUser();
				break;
			case 2:
				moveUser();
				break;
			case 3:
				showUsers();
				break;
			case 4:
				System.out.println("게임이 종료됩니다.");
				return;
			default:
				System.out.println("잘못 입력하셨습니다.\nUser choice : " + res);
				break;
			}
		}
	}

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
		Location loc = new Location(y, x);
		
		//max좀비를 넘기지 않게 jombie를 생성
		boolean isJombie = (Math.random() > 0.5)? (jombieCount > maxJombie? true : false) : false;
		User user = new User(loc, userName, isJombie);
		userList.add(user);
		// 정렬
		Collections.sort(userList, new CustomComparator());

		map.drawMap(userList);
	}

	
	public boolean makeNewUser(String name, int y, int x) {
		if(y < 0 || y >= mapSize_y || x < 0 || x >= mapSize_x)	return false;
		boolean isJombie;
		if(isJombie = (Math.random() > 0.5)? (jombieCount > maxJombie? true : false) : false){
			personCount++;
		}else{
			jombieCount++;
		}
		User user = new User(new Location(y, x), name, isJombie);
		userList.add(user);
		// 정렬
		Collections.sort(userList, new CustomComparator());
		
//		map.drawMap(userList);
		
		return true;
	}
	
	private void moveUser() {
		for (User user : userList) {
			//죽은 유저의 경우 이도을 묻지 않는다.
			if(user.isDead()) continue;
			
			System.out.println(user.getUserName() + "님을 어디로 이동 시키겠습니까?");
			int direction = -1;

			while (direction < 1 || direction > 4) {
				System.out.println("1. 북쪽");
				System.out.println("2. 동쪽");
				System.out.println("3. 남쪽");
				System.out.println("4. 서쪽");
				direction = sc.nextInt();
			}
			user.setUserLocation(direction - 1);
		}
		// 정렬
		Collections.sort(userList, new CustomComparator());
		isThereAttack();
		// 다시 맵에 그리기
		map.drawMap(userList);
	}

	public void showUsers() {
		System.out.println("===================");
		for (User user : userList) {
			// print User Status라는 것을 그냥 호출
			user.printUserStatus();
		}
		System.out.println("===================");
	}

	private void isThereAttack() {
		// 어떻게 해야할지 감이 안잡힘...
		
		/*
		 * int maxDuplicatedPosition = 3; int map[][][] = new
		 * int[mapSize_y][mapSize_x][maxDuplicatedPosition];
		 * 
		 * //map을 -1로 채움. for(int[][] m : map) for(int[] m2 : m) Arrays.fill(m2,
		 * -1);
		 */
		
		// 조금 더 쪼개서 생각해보자...
		// 일단 맵에 겹치는 경우는 제외한다.
		// 수평으로 붙어있는(x좌표 1차이) 경우만 따지고 공격을 실행한다.
		User lastUser = null;
		User currentUser = null;
		for (int i = 1; i < userList.size(); i++) {
			lastUser = userList.get(i-1);
			currentUser = userList.get(i);
			
			// 붙어 있는 사람이 죽은 사람은 아닌지 확인한다.
			if(lastUser.isDead() || currentUser.isDead()) continue;
			
			// 붙어있는 유저들이 있는지 찾는다.
			boolean isNear = lastUser.getUserLocation().getLocation_y() == currentUser.getUserLocation().getLocation_y() && lastUser.getUserLocation().getLocation_x() + 1 == currentUser.getUserLocation().getLocation_x(); 
			if(isNear){
				// 사람과 좀비의 관계인지 찾는다.
				boolean isFirstJombie = lastUser.isJombie();
				boolean isSecondJombie = currentUser.isJombie();
				if(isFirstJombie || isSecondJombie){
					// 공격한다.
					attack(lastUser, currentUser);
				}
			}
		}
	}
	
	private void attack(User user1, User user2){
		// 사라질 놈을 여기서 결정할까?
		// hp를 파악해서 0 이하일 경우 죽인다.
		
		// user1이 좀비이고 user2가 사람일 경우
		if(user1.isJombie() && !user2.isJombie()){
			user2.beAttacked();
			if(user2.getUserHP() <= 0)
				user2.setDead(true);
		}
		// user1이 사람이고 user2가 좀비인 경우
		else if(!user1.isJombie() && user2.isJombie()){
			user1.beAttacked();
			if(user1.getUserHP() <= 0)
				user1.setDead(true);
		}
	}
	
	// item 생성 메소드
	private int[] makeItem(){
		// 맵 상에 어떻게 띄울까를 고민중
		// 맵에 사람과 겹칠경우 어떻게 하지?
		// 사람을 그냥 띄워놔야겠지?
		// 언제 아이템을 생성하고 언제 먹지?
		// 이동 후에 생성하고 현재 위치에서 겹쳐지면 바로 먹을까?
		// 이동 후에 생성 -> 아이템과 유저 위치 확인
		
		int[] itemLocation = new int[2];
		
		// 10%의 확률로 아이템 생성
		if(Math.random() > 0.9){
			itemLocation[0] = (int)(Math.random() * mapSize_y);
			itemLocation[1] = (int)(Math.random() * mapSize_x);
		}else{
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
