/**
*	WeaponTypes.java
*
*	@author Johan
*/

package constants;

public enum WeaponType {
	RIFLE(100, 20), GRENADE(4, 50), SWORD(-1, 100), PORTALGUN(2, 0);
	
	private final int startingAmmo;
	private final int damage;
	
	WeaponType(final int startingAmmo, final int damagePerAttack){
		this.startingAmmo = startingAmmo;
		this.damage = damagePerAttack;
	}
	
	public int getStartingAmmo(){
		return this.startingAmmo;
	}
	
	public int getDamage(){
		return this.damage;
	}
	
}
