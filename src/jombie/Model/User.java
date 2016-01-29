package jombie.Model;

public class User {
	private Location location;
	private int hp;
	private String userName;
	private boolean isJombie;
	private static final int basicDamage = 20;
	
	public User(Location location, String userName){
		this.location = location;
		this.hp = 100;
		this.isJombie = false;
	}
	public Location getUserLocation(){
		return location;
	}
	public void setUserLocation(){
		
	}
	public void attack(User user){
		// 자신이 좀비이고 상대가 사람일 경우
		if(this.isJombie && !user.isJombie){
			
		}
	}
	public void beAttacked(User user){
		setUserHPbyDamaging(basicDamage);
	}
	public void setUserHP(int hp){
		this.hp = hp;
	}
	public void setUserHPbyDamaging(int damage){
		this.hp -= damage;
	}
	public int getUserHP(){
		return this.hp;
	}
	

}
