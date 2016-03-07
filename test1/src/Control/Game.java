package Control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import Model.Location;
import Model.User;
import View.Map;

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
	// mapSize �������� ���� Game���̶� �ߺ��ǰ�����

	private User currentLoginUser;

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
			System.out.println("1.�� ���� \n2.��ġ�̵�\n3.��������\n4.�ʺ���(ġƮ���)\n5.����");
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
				System.out.println("������ ����˴ϴ�.");
				return;
			default:
				System.out.println("�߸� �Է��ϼ̽��ϴ�.\nUser choice : " + res);
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
		// ����
		Collections.sort(userList, new CustomComparator());

		// map.drawMap(userList);

		return true;
	}

	// �α��� ������ ��ġ�̵�
	private void moveUser() {
		int direction = -1;
		while (direction < 1 || direction > 9) {
			direction = askDirection(currentLoginUser);
			// �̵� ������ ��ġ�� �Է¹��� ��� �̵���Ű�� �ݺ��� ����
			if (currentLoginUser.setUserLocation_8D(userList, direction - 1))
				break;
			// �ߺ��ǰų� �̵� �Ұ����� ��ġ�� �Էµ� ��� �ݺ��� �ٽ� ����
			else
				direction = -1;
		}
	}

	// �̵� ���� ����
	private int askDirection(User user) {
		System.out.println(user.getUserName() + "���� ���� �̵� ��Ű�ڽ��ϱ�?");
		System.out.println("1.��   2.��  3.��");
		System.out.println("4.�� 5.��  6.��");
		System.out.println("7.��   8.��  9.��");

		return sc.nextInt();
	}

	// �������� ����
	public void showUsers() {
		System.out.println("===================");
		for (User user : userList) {
//			user.printUserStatus();
			user.printUserStatus(currentLoginUser.isJombie());
		}
		System.out.println("===================");
	}

	// �ΰ� �ֺ����� ���� �ִ����� ���Ǳ� ���� ���ݰ��ɹ����� ���� �ִ��� Ž������ ���� (8����)
	public void isNearEnemy() {
		// �⺻ ���̵��
		// ����ں��� ó�� ����ں��� �����ͼ� �˻��Ѵ�.
		// y������ ������ ��� y�� ������ ��� Ž���Ѵ�.
		// otherY - d <= y <- otherY +1 �̶�� ��� Ž���Ѵ�.
		// y���� 1 �ʰ��� ���̰� ���ٸ� Ž���� �����.
		
		for (User user : userList) {
			// nearEnemy �ʱ�ȭ
			user.resetNearEnemy();
			// ���� ����ڴ� ����
			if (user.isDead())
				continue;
			for (User other : userList) {
				// ���� �����, �� ����� �ڽ��� ��� �ѱ�
				if (other.isDead() || user == other)
					continue;

				int diff_y = other.getUserLocation().getLocation_y() - user.getUserLocation().getLocation_y();
				if (diff_y >= -possibleAttackRange && diff_y <= possibleAttackRange) {
					int diff_x = other.getUserLocation().getLocation_x() - user.getUserLocation().getLocation_x();
					if (diff_x >= -possibleAttackRange && diff_x <= possibleAttackRange)
						// �ߺ��Ǵ� ������ ���� ���ؼ� user�� �����ΰ��� �������θ� attack�� ȣ���Ѵ�.
						// �ߺ��Ǵ� ��찡 �ִ��� �ٽ� �����غ��߰���
						if (user.isJombie() ^ other.isJombie()){
							// ���� ������ ������ attackableUser�� �������
							user.addNearEnemy(other);
							// user�� ������ ���� �ٷ� ����
							// �ֺ��� �� �� �����ϵ��� �����ؾ� �� �� ����.
							if(user.isJombie())
								attack(user, other);
						}
				}
			}
		}
	}
	
	// UserControl, MapControl ���� ������ �� �� ����
	private void attack(User user1, User user2) {
		// ����� ���� ���⼭ �����ұ�?
		// hp�� �ľ��ؼ� 0 ������ ��� ���δ�.

		// user1�� �����̰� user2�� ����� ���
		if (user1.isJombie() && !user2.isJombie()) {
			user2.beAttacked();
			if (user2.getUserHP() <= 0)
				user2.setDead(true);
		}
		// user1�� ����̰� user2�� ������ ���
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
/*	�ϴ� ����
	 public void moveRandom() {
	 for (User user : userList) {
	 user.setUserLocation(userList, (int) (Math.random() * 4));
	 }
	 Collections.sort(userList, new CustomComparator()); // ���� ȣ�� ���ϰ� �־���.
	 // isAttakable_8D();
	 map.drawMap(userList);
	 showUsers();
	 }
*/
	public void moveRandom_ExceptMe() {
		// �α��� ����(���ΰ�) �����ϰ� ��� ���� �̵�
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
			System.out.print("Input user Location Y(0�̻� " + (mapSize_y - 1) + "����) : ");
			y = sc.nextInt();
		}
		while (x < 0 || x >= mapSize_x) {
			System.out.print("Input user Location X(0�̻� " + (mapSize_x - 1) + "����) : ");
			x = sc.nextInt();
		}
		// �ٽ� ��ġ������ ����鼭 �ߺ��� ��ġ ã�� ����
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

	// item ���� �޼ҵ�
	private int[] makeItem() {
		// �� �� ��� ��� �����
		// �ʿ� ����� ��ĥ��� ��� ����?
		// ����� �׳� ������߰���?
		// ���� �������� �����ϰ� ���� ����?
		// �̵� �Ŀ� �����ϰ� ���� ��ġ���� �������� �ٷ� ������?
		// �̵� �Ŀ� ���� -> �����۰� ���� ��ġ Ȯ��

		int[] itemLocation = new int[2];

		// 10%�� Ȯ���� ������ ����
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
