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
		System.out.print("Input user Nmae : ");
		String userName = sc.next();
		int x = -1, y = -1;

		while (y < 0 || x >= mapSize_y) {
			System.out.print("Input user Location X(0�̻� " + (mapSize_y - 1) + "����) : ");
			y = sc.nextInt();
		}
		while (x < 0 || x >= mapSize_x) {
			System.out.print("Input user Location X(0�̻� " + (mapSize_x - 1) + "����) : ");
			x = sc.nextInt();
		}
		Location loc = new Location(y, x);
		User user = new User(loc, userName);
		userList.add(user);
		// ����
		Collections.sort(userList, new CustomComparator());

		map.drawMap(userList);
	}

	private void moveUser() {
		for (User user : userList) {
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

		// �ٽ� �ʿ� �׸���
		map.drawMap(userList);
	}

	private void showUsers() {
		System.out.println("===================");
		for (User user : userList) {
			System.out.println("Name : " + user.getUserName());
			System.out.println("Position : (" + user.getUserLocation().getLocation_y() + ", "
					+ user.getUserLocation().getLocation_x() + ")");
			System.out.println("Hp : " + user.getUserHP() + "\n");

		}
		System.out.println("===================");
	}

	private void isThereAttack() {
		Location userLoc[] = new Location[userList.size()];
		for (int i = 0; i < userList.size(); i++) {
			userLoc[i] = userList.get(i).getUserLocation();
		}
		// �پ��ִ� �������� �ִ��� ã�´�.

		// ����� ������ �������� ã�´�.

		// �����Ѵ�.
	}

	public class CustomComparator implements Comparator<User> {
		@Override
		public int compare(User u1, User u2) {
			return u1.compareTo(u2);
		}
	}
}
