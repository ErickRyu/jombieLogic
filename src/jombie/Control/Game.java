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
	private Map map = null;
	Scanner sc;

	public Game() {
		userList = new ArrayList<>();
		map = new Map(mapSize_y, mapSize_x);
		sc = new Scanner(System.in);
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

		while (y < 0 || x >= mapSize_y) {
			System.out.print("Input user Location X(0이상 " + (mapSize_y - 1) + "이하) : ");
			y = sc.nextInt();
		}
		while (x < 0 || x >= mapSize_x) {
			System.out.print("Input user Location X(0이상 " + (mapSize_x - 1) + "이하) : ");
			x = sc.nextInt();
		}
		Location loc = new Location(y, x);
		User user = new User(loc, userName);
		userList.add(user);
		// 정렬
		Collections.sort(userList, new CustomComparator());

		map.drawMap(userList);
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

	private void showUsers() {
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
	public class CustomComparator implements Comparator<User> {
		@Override
		public int compare(User u1, User u2) {
			return u1.compareTo(u2);
		}
	}
}
