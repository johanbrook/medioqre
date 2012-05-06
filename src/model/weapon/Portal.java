/**
*	Portal.java
*
*	@author Johan
*/

package model.weapon;

import java.awt.Dimension;
import java.awt.Rectangle;

import model.CollidableObject;
import model.Entity;
import model.weapon.PortalGun.Mode;

public class Portal extends CollidableObject {

	private Portal otherPortal;
	private Mode mode;
	
	public Portal(Mode mode, Rectangle collBox, Dimension size, int xoffset, int yoffset) {
		super(collBox, size, xoffset, yoffset);
		this.mode = mode;
	}
	
	public Mode getMode() {
		return this.mode;
	}
	
	public void setSisterPortal(Portal p) {
		this.otherPortal = p;
	}

	
	public Portal getSisterPortal() {
		return this.otherPortal;
	}

}
