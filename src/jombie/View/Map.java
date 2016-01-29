package jombie.View;
import java.util.ArrayList;

import jombie.Model.User;
public class Map {
	protected int mapSize_y;
	protected int mapSize_x;
	
	public Map(int y, int x){
		mapSize_y = y;
		mapSize_x = x;
	}
	public void drawMap(ArrayList<User> userList){
		// sparseMatrix ���ø��� �� �� ����.
		// ���߿� �ٽ� user�� y, x������ �����ؾ߰���.
		int last_y = 0;
		int last_x = 0;
		// �� ��� ������ : ���� ��ǥ�� �ߺ� �� ��� �ߺ��ؼ� ��� �����.
		for(User user : userList){
			int currentUserPosition_y = user.getUserLocation().getLocation_y();
			int currentUserPosition_x = user.getUserLocation().getLocation_x();
			
			for(; last_y < currentUserPosition_y; last_y++, last_x = 0){
				
				for(; last_x < mapSize_x; last_x++){
					//���  println ȣ���ϴ°� ������
					System.out.print(". ");
				}
				System.out.println();
			}
			
			for(; last_x < currentUserPosition_x; last_x++){
				System.out.print(". ");
			}
			System.out.print("* ");
			++last_x;
		}
		for(; last_y < mapSize_y; last_y++, last_x = 0){
			for(; last_x < mapSize_x; last_x++){
				System.out.print(". ");
			}
			System.out.println();			
		}
	}
	public void drawUser(User user){
		
	}
}
