package jombie.Model;

public class User implements Comparable<User> {
	private Location location;
	private int hp;
	private String userName;
	private boolean isJombie;
	private static final int basicDamage = 20;
	
	@Override
	public int compareTo(User user) {
		if(this.getUserLocation().getLocation_y() == user.getUserLocation().getLocation_y()){
			return this.getUserLocation().getLocation_x() - user.getUserLocation().getLocation_x();
		}
		return this.getUserLocation().getLocation_y() - user.getUserLocation().getLocation_y();
	}

	public User(Location location, String userName) {
		this.location = location;
		this.userName = userName;
		this.hp = 100;
		this.isJombie = false;
	}

	public Location getUserLocation() {
		return location;
	}
	
	public void setUserLocation(int direction){
		this.location.moveDirect(direction);
	}

	public String getUserName() {
		return userName;
	}

	public void attack(User user) {
		// 자신이 좀비이고 상대가 사람일 경우
		if (this.isJombie && !user.isJombie) {

		}
	}

	public void beAttacked(User user) {
		setUserHPbyDamaging(basicDamage);
	}

	public void setUserHP(int hp) {
		this.hp = hp;
	}

	public void setUserHPbyDamaging(int damage) {
		this.hp -= damage;
	}

	public int getUserHP() {
		return this.hp;
	}

}
