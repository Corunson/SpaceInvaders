package Invaders;

import framework.Model;

public class HellHordeAliens extends Model {

	public HellHordeAliens(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void updateLocation(long interval) {
		setHorizontalSpeed(getHorizontalSpeed() + getHorizontalAcceleration() * interval);
		setVerticalSpeed(getVerticalSpeed() + getVerticalAcceleration() * interval);
		
		setLocX(getLocX() + interval * getHorizontalSpeed() / 1000.0f);
		setLocY(getLocY() + interval * getVerticalSpeed() / 1000.0f);



	}

}
