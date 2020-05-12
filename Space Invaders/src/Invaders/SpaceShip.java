/**
 * 
 */
package Invaders;

import framework.Model;

/**
 * @author grv5783
 *
 */
public class SpaceShip extends Model {

	/**
	 * @param name
	 */
	public SpaceShip(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see framework.Model#updateLocation(long)
	 */
	@Override
	public void updateLocation(long interval) {
		// V = U + AT
		setHorizontalSpeed(getHorizontalSpeed() + getHorizontalAcceleration() * interval);
		setVerticalSpeed(getVerticalSpeed() + getVerticalAcceleration() * interval);
		
		setLocX(getLocX() + interval * getHorizontalSpeed() / 1000.0f);
		setLocY(getLocY() + interval * getVerticalSpeed() / 1000.0f);
	}

}
