package jombie.Model;

import java.util.ArrayList;
// discret Location from User class 
public class User implements Comparable<User> {
	private Location location;
	private int hp;
	private String userName;
	private boolean isJombie;
	private boolean isDead;
	private final float possiblityToBeJombie = 0.5f;
	private static final int basicDamage = 20;
	
	// attackableUser List�� nearEnemy�� ����
	// �ΰ�, ���� ��� �ڱ� ��ó attack���� ������ �ִ� ���� �� �� �ֵ��� ����
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
		if (this.getUserLocation().getLocation_y() == user.getUserLocation().getLocation_y()) {
			return this.getUserLocation().getLocation_x() - user.getUserLocation().getLocation_x();
		}
		return this.getUserLocation().getLocation_y() - user.getUserLocation().getLocation_y();
	}

	public User(Location location, String userName) {
		this.location = location;
		this.userName = userName;
		this.hp = 100;
		this.isJombie = (Math.random() < possiblityToBeJombie) ? true : false;
		this.isDead = false;
		this.nearEnemy = new ArrayList<>(); 
	}
	
	// discret Version of User class 
	public User(String userName){
		this.userName = userName;
		this.hp = 100;
		this.isJombie = (Math.random() < possiblityToBeJombie) ? true : false;
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

	// �̰� ���� �� �ʿ䰡 ��������
	// ���߿� ���ھ �ű�ٰ� ���� ���� ���⼭ ó���ؾ��� �� ���⵵�ϴ�.
	// ���� ���� �׿����� ���� ������ ���ؼ��� attack�� game���� �������� �ʰ� User���� ���� �� �ڿ� Game������
	// User�� Kill/Death�� �޾ƿ��� ������ �귯���� ���� ������

	// ����� hp�� �� �޾��� ��� ���� �Ǵ� ��쵵 ����س��ƾ� �� �� ����.
	public void attack(User user) {
		// �ڽ��� �����̰� ��밡 ����� ���
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

	public void printUserStatus() {
		System.out.println("Name\t :  " + userName);
		System.out.println("Position : (" + location.getLocation_y() + ", " + location.getLocation_x() + ")");
		System.out.println("Hp\t :  " + hp);
		System.out.println(isJombie ? "Jombie" : "Person");
		System.out.println(isDead ? "Dead\n" : "Alive\n");
	}
	
	public void printUserStatus(boolean isLoginUserJombie) {
		System.out.println("Name\t :  " + userName);
		if(!(isLoginUserJombie ^ isJombie))
			System.out.println("Position : (" + location.getLocation_y() + ", " + location.getLocation_x() + ")");
		else
			System.out.println("Position : YOU CAN'T SEE IT");
		System.out.println("Hp\t :  " + hp);
		System.out.println(isJombie ? "Jombie" : "Person");
		System.out.println(isDead ? "Dead\n" : "Alive\n");
	}
}
