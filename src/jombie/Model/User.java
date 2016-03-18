package jombie.Model;

import java.util.ArrayList;

import javax.swing.JTextArea;
/** Discrete Location from User class. */
public class User implements Comparable<User> {
	private Location location;
	private int hp;
	private String userName;
	private boolean isJombie;
	private boolean isDead;
	private final float possiblityToBeJombie = 0.5f;
	private static final int basicDamage = 20;
	
	/**
	 * AttackableUser List를 nearEnemy로 변경
	 * 인간, 좀비 모두 자기 근처 attack가능 범위에 있는 적을 볼 수 있도록 변경.
	 */
	private ArrayList<User> nearEnemy;
	
	public ArrayList<User> getNearEnemy(){
		return nearEnemy;
	}
	public void resetNearEnemy(){
		nearEnemy = new ArrayList<>();
	}
	public void addNearEnemy(User user){
		nearEnemy.add(user);
	}
	

	@Override
	public int compareTo(User user) {
		if (getUserLocation().getLocationY() == user.getUserLocation().getLocationY()) {
			return getUserLocation().getLocationX() - user.getUserLocation().getLocationX();
		}
		return getUserLocation().getLocationY() - user.getUserLocation().getLocationY();
	}

	public User(String userName, Location location) {
		this.location = location;
		this.userName = userName;
		this.hp = 100;
		this.isJombie = Math.random() < possiblityToBeJombie;
		this.isDead = false;
		this.nearEnemy = new ArrayList<>(); 
	}
	
	/** Discret Version of User class. */
	public User(String userName){
		this.userName = userName;
		this.hp = 100;
		this.isJombie = Math.random() < possiblityToBeJombie;
		this.isDead = false;
		this.nearEnemy = new ArrayList<>();
	}
	public User(Location location, String userName, boolean isJombie) {
		this.location = location;
		this.userName = userName;
		this.hp = 100;
		this.isJombie = isJombie;
		this.isDead = false;
		this.nearEnemy = new ArrayList<>(); 
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}

	public boolean isDead() {
		return this.isDead;
	}

	public Location getUserLocation() {
		return location;
	}

	public boolean setUserLocation_8D(ArrayList<User> userList, int direction) {
		return this.location.moveDirect_8D(this, userList, direction);
	}

	public boolean setUserLocation(int y, int x) {
		return this.location.setLocation(y, x);
	}

	public String getUserName() {
		return userName;
	}

	/**
	 * 이걸 여기 둘 필요가 없으려나
	 * 나중에 스코어를 매긴다고 했을 때는 여기서 처리해야할 것 같기도하다.
	 * 누가 나를 죽였는지 같은 정보를 위해서는 attack을 game에서 진행하지 않고 User에서 진행 한 뒤에 Game에서는
	 * User별 Kill/Death를 받아오는 식으로 흘러가야 하지 않을까

	 * 사람이 hp가 다 달았을 경우 좀비가 되는 경우도 고려해놓아야 할 것 같다.
	 */
	public void attack(User user) {
		// 자신이 좀비이고 상대가 사람일 경우
		if (this.isJombie && !user.isJombie) {

		}
	}

	public boolean isJombie() {
		return this.isJombie;
	}

	public void beAttacked() {
		this.hp -= basicDamage;
	}

	public void setUserHP(int hp) {
		this.hp = hp;
	}

	public int getUserHP() {
		return this.hp;
	}

	public void printUserStatus(JTextArea incoming) {
		incoming.append("Name\t :  " + userName + "\n");
		incoming.append("Position : (" + location.getLocationY() + ", " + location.getLocationX() + ")\n");
		incoming.append("Hp\t :  " + hp + "\n");
		incoming.append(isJombie ? "Jombie\n" : "Person\n");
		incoming.append(isDead ? "Dead\n\n" : "Alive\n\n");
	}
	
	public void printUserStatus(boolean isLoginUserJombie) {
		System.out.println("Name\t :  " + userName);
		if(!(isLoginUserJombie ^ isJombie)) {
			System.out.println("Position : (" + location.getLocationY() + ", " + location.getLocationX() + ")");
		} else {
			System.out.println("Position : YOU CAN'T SEE IT");
		}
		System.out.println("Hp\t :  " + hp);
		System.out.println(isJombie ? "Jombie" : "Person");
		System.out.println(isDead ? "Dead\n" : "Alive\n");
	}
}
