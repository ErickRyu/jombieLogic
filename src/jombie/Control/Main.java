package jombie.Control;

public class Main {
	public static void main(String[] args) {
//		 Game game = new Game();
//		 game.playGame();

		GameTest gameTest = new GameTest(2, 5);
		
		// 30�� �ڵ� �̵��ϵ��� �׽�Ʈ�� �����ϱ� �̿��ڵ��� �̻��� ������ �������� ��������
		// ��������?
		for (int i = 0; i < 100; i++)
			gameTest.moveRandom();
	}
}
