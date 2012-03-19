/**
*	WeaponTypes.java
*
*	@author Johan
*/

package constants;

public enum WeaponType {
	RIFLE(100, 20, false, false), GRENADE(4, 50, false, false), 
	SWORD(true, true), PORTALGUN(true, false);
	
	private final int startingAmmo;
	private final int damage;
	private final boolean hasInfiniteAmmo;
	private final boolean hasInfiniteDamage;
	
	
	WeaponType(	final int startingAmmo, 
				final int damagePerAttack, 
				final boolean hasInfiniteAmmo, 
				final boolean hasInfiniteDamage){
		
		this.startingAmmo = startingAmmo;
		this.damage = damagePerAttack;
		this.hasInfiniteAmmo = hasInfiniteAmmo;
		this.hasInfiniteDamage = hasInfiniteDamage;
	}
	
	
	WeaponType(final boolean hasInfiniteAmmo, final boolean hasInfiniteDamage){
		this.startingAmmo = this.damage = 0; 	// Have to initialize these
		this.hasInfiniteAmmo = hasInfiniteAmmo;
		this.hasInfiniteDamage = hasInfiniteDamage;
	}
	
	public int getStartingAmmo(){
		return this.startingAmmo;
	}
	
	public int getDamage(){
		return this.damage;
	}
	
	public boolean hasInfiniteAmmo(){
		return this.hasInfiniteAmmo;
	}
	
	public boolean hasInfiniteDamage(){
		return this.hasInfiniteDamage;
	}
	
}