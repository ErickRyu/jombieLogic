package jombie.Model;

public class User implements Comparable<User> {
	private Location location;
	private int hp;
	private String userName;
	private boolean isJombie;
	private boolean isDead;
	private static final int basicDamage = 20;

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
		this.isJombie = (Math.random() > 0.5) ? true : false;
		this.isDead = false;
	}
	
	public User(Location location, String userName, boolean isJombie) {
		this.location = location;
		this.userName = userName;
		this.hp = 100;
		this.isJombie = isJombie;
		this.isDead = false;
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

	public void setUserLocation(int direction) {
		this.location.moveDirect(direction);
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
	public void printUserStatus(){
		System.out.println("Name\t :  " + userName);
		System.out.println("Position : (" + location.getLocation_y() + ", " + location.getLocation_x() + ")");
		System.out.println("Hp\t :  " + hp);
		System.out.println(isJombie? "Jombie" : "Person");
		System.out.println(isDead? "Dead\n" : "Alive\n");
	}
}
