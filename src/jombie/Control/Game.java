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
	// ���� �� �� ����ڽ��ϱ�? �� ���� �����
	// ������� �˾Ƽ� ������.
	// ���� �����ϴ� ������ ����
		// �������� ��ǥ�� ��� �����ϰ�, map���� ��� ��� ���ΰ�?
	// ���� ��� ���شٴ�
	
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
			System.out.println("1.�� ���� \n2.��ġ�̵�\n3.��������\n4.����");
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
				System.out.println("������ ����˴ϴ�.");
				return;
			default:
				System.out.println("�߸� �Է��ϼ̽��ϴ�.\nUser choice : " + res);
				break;
			}
		}
	}

	private void makeNewUser() {
		// ��� map size�����ؼ� ����־��µ� ������
		// ��� ������ ���� ã�Ƽ� ��ĥ��
		
		
		System.out.print("Input user Nmae : ");
		String userName = sc.next();
		int x = -1, y = -1;

		while (y < 0 || y >= mapSize_y) {
			System.out.print("Input user Location Y(0�̻� " + (mapSize_y - 1) + "����) : ");
			y = sc.nextInt();
		}
		while (x < 0 || x >= mapSize_x) {
			System.out.print("Input user Location X(0�̻� " + (mapSize_x - 1) + "����) : ");
			x = sc.nextInt();
		}
		Location loc = new Location(y, x);
		
		//max���� �ѱ��� �ʰ� jombie�� ����
		boolean isJombie = (Math.random() > 0.5)? (jombieCount > maxJombie? true : false) : false;
		User user = new User(loc, userName, isJombie);
		userList.add(user);
		// ����
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
		// ����
		Collections.sort(userList, new CustomComparator());
		
//		map.drawMap(userList);
		
		return true;
	}
	
	private void moveUser() {
		for (User user : userList) {
			//���� ������ ��� �̵��� ���� �ʴ´�.
			if(user.isDead()) continue;
			
			System.out.println(user.getUserName() + "���� ���� �̵� ��Ű�ڽ��ϱ�?");
			int direction = -1;

			while (direction < 1 || direction > 4) {
				System.out.println("1. ����");
				System.out.println("2. ����");
				System.out.println("3. ����");
				System.out.println("4. ����");
				direction = sc.nextInt();
			}
			user.setUserLocation(direction - 1);
		}
		// ����
		Collections.sort(userList, new CustomComparator());
		isThereAttack();
		// �ٽ� �ʿ� �׸���
		map.drawMap(userList);
	}

	public void showUsers() {
		System.out.println("===================");
		for (User user : userList) {
			// print User Status��� ���� �׳� ȣ��
			user.printUserStatus();
		}
		System.out.println("===================");
	}

	private void isThereAttack() {
		// ��� �ؾ����� ���� ������...
		
		/*
		 * int maxDuplicatedPosition = 3; int map[][][] = new
		 * int[mapSize_y][mapSize_x][maxDuplicatedPosition];
		 * 
		 * //map�� -1�� ä��. for(int[][] m : map) for(int[] m2 : m) Arrays.fill(m2,
		 * -1);
		 */
		
		// ���� �� �ɰ��� �����غ���...
		// �ϴ� �ʿ� ��ġ�� ���� �����Ѵ�.
		// �������� �پ��ִ�(x��ǥ 1����) ��츸 ������ ������ �����Ѵ�.
		User lastUser = null;
		User currentUser = null;
		for (int i = 1; i < userList.size(); i++) {
			lastUser = userList.get(i-1);
			currentUser = userList.get(i);
			
			// �پ� �ִ� ����� ���� ����� �ƴ��� Ȯ���Ѵ�.
			if(lastUser.isDead() || currentUser.isDead()) continue;
			
			// �پ��ִ� �������� �ִ��� ã�´�.
			boolean isNear = lastUser.getUserLocation().getLocation_y() == currentUser.getUserLocation().getLocation_y() && lastUser.getUserLocation().getLocation_x() + 1 == currentUser.getUserLocation().getLocation_x(); 
			if(isNear){
				// ����� ������ �������� ã�´�.
				boolean isFirstJombie = lastUser.isJombie();
				boolean isSecondJombie = currentUser.isJombie();
				if(isFirstJombie || isSecondJombie){
					// �����Ѵ�.
					attack(lastUser, currentUser);
				}
			}
		}
	}
	
	private void attack(User user1, User user2){
		// ����� ���� ���⼭ �����ұ�?
		// hp�� �ľ��ؼ� 0 ������ ��� ���δ�.
		
		// user1�� �����̰� user2�� ����� ���
		if(user1.isJombie() && !user2.isJombie()){
			user2.beAttacked();
			if(user2.getUserHP() <= 0)
				user2.setDead(true);
		}
		// user1�� ����̰� user2�� ������ ���
		else if(!user1.isJombie() && user2.isJombie()){
			user1.beAttacked();
			if(user1.getUserHP() <= 0)
				user1.setDead(true);
		}
	}
	
	// item ���� �޼ҵ�
	private int[] makeItem(){
		// �� �� ��� ��� �����
		// �ʿ� ����� ��ĥ��� ��� ����?
		// ����� �׳� ������߰���?
		// ���� �������� �����ϰ� ���� ����?
		// �̵� �Ŀ� �����ϰ� ���� ��ġ���� �������� �ٷ� ������?
		// �̵� �Ŀ� ���� -> �����۰� ���� ��ġ Ȯ��
		
		int[] itemLocation = new int[2];
		
		// 10%�� Ȯ���� ������ ����
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
