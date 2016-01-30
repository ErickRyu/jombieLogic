package jombie.View;

import java.util.ArrayList;

import jombie.Model.User;

public class Map {
	protected int mapSize_y;
	protected int mapSize_x;

	public Map(int y, int x) {
		mapSize_y = y;
		mapSize_x = x;
	}

	public void drawMap(ArrayList<User> userList) {
		// sparseMatrix ���ø��� �� �� ����.
		int last_y = -1;
		int last_x = -1;
		// �� ��� ������ : ���� ��ǥ�� �ߺ� �� ��� �ߺ��ؼ� ��� �����.
		String dot;
		for (User user : userList) {
			int currentUserPosition_y = user.getUserLocation().getLocation_y();
			int currentUserPosition_x = user.getUserLocation().getLocation_x();
			// �ߺ��Ǵ� ��� ������� �������� ����
			// ����� ó���ߴ����� �� �𸣰ڴµ�, �ϴ� ȭ�鿡 �ߺ������� �ʴ´�.
			// ������ �����Ŷ�� �����ߴµ�, �ƴϳ�..
			if (last_y == currentUserPosition_y && last_x == currentUserPosition_x)
				continue;
			else {
				last_x++;
			}
			if (last_y == -1) {
				last_y = 0;
			}

			for (; last_y < currentUserPosition_y; last_y++, last_x = 0) {
				dot = "";
				for (; last_x < mapSize_x; last_x++) {
					dot += ". ";
				}
				System.out.println(dot);
			}
			dot = "";
			for (; last_x < currentUserPosition_x; last_x++) {
				dot += ". ";
			}
			System.out.print(dot);
			System.out.print(user.isDead()? ". " : "* ");
		}
		++last_x;
		for (; last_y < mapSize_y; last_y++, last_x = 0) {
			dot = "";
			for (; last_x < mapSize_x; last_x++) {
				dot += ". ";
			}
			System.out.println(dot);
		}
		System.out.println();
	}

	public void drawUser(User user) {

	}
}
