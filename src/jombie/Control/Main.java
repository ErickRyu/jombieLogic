package jombie.Control;

public class Main {
	public static void main(String[] args) {
//		 Game game = new Game();
//		 game.playGame();

		GameTest gameTest = new GameTest(2, 5);
		
		// 30번 자동 이동하도록 테스트를 돌리니까 이용자들이 이상한 곳으로 움직여서 나가버림
		// 왜저러지?
		for (int i = 0; i < 100; i++)
			gameTest.moveRandom();
	}
}
