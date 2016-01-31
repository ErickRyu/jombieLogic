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

	// 이걸 여기 둘 필요가 없으려나
	// 나중에 스코어를 매긴다고 했을 때는 여기서 처리해야할 것 같기도하다.
	// 누가 나를 죽였는지 같은 정보를 위해서는 attack을 game에서 진행하지 않고 User에서 진행 한 뒤에 Game에서는
	// User별 Kill/Death를 받아오는 식으로 흘러가야 하지 않을까
	
	// 사람이 hp가 다 달았을 경우 좀비가 되는 경우도 고려해놓아야 할 것 같다.
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
	public void printUserStatus(){
		System.out.println("Name\t :  " + userName);
		System.out.println("Position : (" + location.getLocation_y() + ", " + location.getLocation_x() + ")");
		System.out.println("Hp\t :  " + hp);
		System.out.println(isJombie? "Jombie" : "Person");
		System.out.println(isDead? "Dead\n" : "Alive\n");
	}
}
