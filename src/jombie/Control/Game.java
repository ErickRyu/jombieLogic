package jombie.Control;

import java.util.ArrayList;
import java.util.Scanner;

import jombie.Model.Location;
import jombie.Model.User;
import jombie.View.Map;

public class Game {
	static public final int mapSize_y = 10;
	static public final int mapSize_x = 10;
	private ArrayList<User> userList = null;
	private Map map = null;
	
	public Game(){
		userList = new ArrayList<>();
		map = new Map(mapSize_y, mapSize_x);
	}
	public void setUserLocation(User user) {
		
	}

	public void getUserLocation(User user) {

	}

	public void setUserHP(User user) {

	}

	public void getUserHP(User user) {

	}
	
	public void drawMap() {

	}

	public void playGame() {
		Scanner sc = new Scanner(System.in);
		int res;
		while (true) {
			System.out.println("1.�� ���� \n2.��ġ�̵�\n3.����");
			res = sc.nextInt();
			switch (res) {
			case 1:
				System.out.print("Input user Nmae : ");
				String userName = sc.next();
				int x = -1, y = -1;
				
				while(y < 0 || x >= mapSize_y){
					System.out.print("Input user Location X(0�̻� " + (mapSize_y - 1) + "����) : ");
					y = sc.nextInt();
				}
				while(x < 0 || x >= mapSize_x){
					System.out.print("Input user Location X(0�̻� " + (mapSize_x - 1) + "����) : ");
					x = sc.nextInt();
				}
				Location loc = new Location(y, x);
				User user = new User(loc, userName);
				userList.add(user);
				map.drawMap(userList);
				
				break;
			case 2:
				break;
			case 3:
				System.out.println("������ ����˴ϴ�.");
				return;
			default:
				System.out.println("�߸� �Է��ϼ̽��ϴ�.\n User choice : " + res);
				break;
			}
		}

	}
}
