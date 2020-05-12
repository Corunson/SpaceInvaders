/**
 * 
 */
package Invaders;

import static framework.GameConstants.*;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import framework.CollisionEvent;
import framework.Controller;
import framework.GameEvent;
import framework.Model;
import framework.OutOfBoundsEvent;
import framework.SoundFX;
import framework.View;

/**
 * @author grv5783
 *
 */
public class SIController extends Controller {

	private final short FIRE_KEY = 0x0010;
	private final short LEFT_KEY = 0x0004;
	private final short RIGHT_KEY = 0x0001;
	private short ActiveKeys = 0x0000;

	private boolean ayylienLeft = false, ayylienRight = false, shipLeft = false, shipRight = false,
			ayylienBottom = false;

	private boolean hellAlienLeft = false, hellAlienRight = false;

	private boolean startEasy, startMedium, startHard, startHell;

	private boolean spaceAndWinder = false;
	
	private boolean gameOver = false;

	private SpaceShip spaceShip;

	private float firingDelay = 0L;
	private float ayylienSpeed = 0.0f;
	private float hellAlienSpeed = 0.0f;
	private float shipSpeed = 0.0f;
	private long ignitionTime;
	private int ayylienCount = 1;
	private int hellAyyliens = 1;
	private int alienBossHp = 1;

	private Rectangle rectEasy = new Rectangle(305, 450, 100, 50);
	private Rectangle rectMedium = new Rectangle(455, 450, 100, 50);
	private Rectangle rectHard = new Rectangle(605, 450, 100, 50);
	private Rectangle rectHell = new Rectangle(755, 450, 100, 50);
	
	private sideWinder sideWinder;
	private Missile missile;
	private Alien[] horde;
	private HellAlien hellAlien;

	/**
	 * 
	 */
	public SIController() {
		super(new SIView());
		((SIView) getView()).startingScreen(true);
		// initEntityList();
	}

	private void initEntityList() {
		// Add entities
		spaceShip = new SpaceShip("ship");
		addEntity(spaceShip, "ship-3.png", 1, 5);
		spaceShip.setActive(true);
		spaceShip.setGhost(false);
		spaceShip.setHorizontalSpeed(0.0f);
		spaceShip.setLocation(ViewWidth / 2, ViewHeight - 80);
		if (startEasy) {
			SoundFX.UKULELE.play();
			int rows = 5, cols = 5;
			firingDelay = 300L;
			ayylienSpeed = 250.0f;
			shipSpeed = 350.0f;
			ayylienCount = rows * cols;
			addAliens(rows, cols);
		} else if (startMedium) {
			int rows = 7, cols = 7;
			firingDelay = 450L;
			ayylienSpeed = 300.0f;
			shipSpeed = 300.0f;
			ayylienCount = rows * cols;
			addAliens(rows, cols);

		} else if (startHard) {
			int rows = 9, cols = 9;
			firingDelay = 600L;
			ayylienSpeed = 350.0f;
			shipSpeed = 250.0f;
			ayylienCount = rows * cols;
			addAliens(rows, cols);

		} else if (startHell) {
			SoundFX.HELL.play();
			alienBossHp = 100;
			firingDelay = 750L;
			hellAlienSpeed = 400.0f;
			ayylienCount = 100000;
			hellAyyliens = 0;
			shipSpeed = 200.0f;
			addHellBoss();

		}

	}

	private void addHellBoss() {
		hellAlien = new HellAlien("Hellboy");
		hellAlien.setActive(true);
		hellAlien.setLocation(30, 50);
		hellAlien.setHorizontalSpeed(hellAlienSpeed);
		summonSideWinders();
		addEntity(hellAlien, "alienBoss-3.png", 4, 7);
		
		

	}

	private void callTheHellHorde() {
		int rows = 5, cols = 5;
		ayylienSpeed = 500.0f;
		hellAyyliens = rows * cols;
		addAliens(rows, cols);
	}

	private void addAliens(int rows, int cols) {
		horde = new Alien[rows * cols];

		for (int i = 0; i < cols; i++) {
			for (int j = 0; j < rows; j++) {
				if (startHell == true) {
					horde[i * cols + j] = new Alien("ayys" + i + j);
					horde[i * cols + j].setHorizontalSpeed(ayylienSpeed);
					horde[i * cols + j].setActive(true);
					horde[i * cols + j].setLocation((i * 100 + 100), (j * 50 + 300));
					addEntity(horde[i * cols + j], "alien-3.png", 1, 7);
					System.out.println(ayylienCount);
				} else {
					horde[i * cols + j] = new Alien("ayys" + i + j);
					horde[i * cols + j].setHorizontalSpeed(ayylienSpeed);
					horde[i * cols + j].setActive(true);
					horde[i * cols + j].setLocation((i * 100 + 100), (j * 50 + 20));
					addEntity(horde[i * cols + j], "alien-3.png", 1, 7);
					System.out.println(ayylienCount);

				}
			}
		}

	}

	/**
	 * @param view
	 */
	public SIController(View view) {
		super(view);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_A:
		case KeyEvent.VK_LEFT:
			setActiveKeys(LEFT_KEY, true);
			break;

		case KeyEvent.VK_D:
		case KeyEvent.VK_RIGHT:
			setActiveKeys(RIGHT_KEY, true);
			break;

		case KeyEvent.VK_F1:
		case KeyEvent.VK_SPACE:
			setActiveKeys(FIRE_KEY, true);
			break;
		case KeyEvent.VK_ESCAPE:
			if (gameOver)
				System.exit(0);
			break;
		default:

		}
	}

	private void setActiveKeys(short val, boolean state) {
		if (state)
			ActiveKeys |= val;
		else
			ActiveKeys &= ~val;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int keyCode = e.getKeyCode();

		switch (keyCode) {
		case KeyEvent.VK_A:
		case KeyEvent.VK_LEFT:
			setActiveKeys(LEFT_KEY, false);
			break;

		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_D:
			setActiveKeys(RIGHT_KEY, false);
			break;

		case KeyEvent.VK_F1:
		case KeyEvent.VK_SPACE:
			setActiveKeys(FIRE_KEY, false);
			break;
		default:
		}
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.WindowListener#windowClosing(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.WindowListener#windowIconified(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.WindowListener#windowDeiconified(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.WindowListener#windowActivated(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.WindowListener#windowDeactivated(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */

	@Override
	public void mouseClicked(MouseEvent e) {
		double mouseX = e.getX();
		double mouseY = e.getY();
		System.out.println(e.getButton());
		System.out.println(e.getX() + " " + e.getY());
		switch (e.getButton()) {
		case MouseEvent.BUTTON1:
			if (rectEasy.contains(mouseX, mouseY)) {
				((SIView) getView()).startingScreen(false);
				rectEasy = rectMedium = rectHard = rectHell = null;
				startEasy = true;
				initEntityList();
			} else if (rectMedium.contains(mouseX, mouseY)) {
				((SIView) getView()).startingScreen(false);
				rectEasy = rectMedium = rectHard = rectHell = null;
				startMedium = true;
				initEntityList();
			} else if (rectHard.contains(mouseX, mouseY)) {
				((SIView) getView()).startingScreen(false);
				rectEasy = rectMedium = rectHard = rectHell = null;
				startHard = true;
				initEntityList();
			} else if (rectHell.contains(mouseX, mouseY)) {
				((SIView) getView()).startingScreen(false);
				rectEasy = rectMedium = rectHard = rectHell = null;
				startHell = true;
				initEntityList();
			}
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see framework.Controller#preprocess()
	 */
	@Override
	protected void preprocess() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see framework.Controller#postprocess()
	 */
	@Override
	protected void postprocess() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see framework.Controller#resetAllFlags()
	 */
	@Override
	protected void resetAllFlags() {
		shipLeft = shipRight = ayylienLeft = ayylienRight = hellAlienLeft = hellAlienRight = false;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see framework.Controller#onMessage(framework.GameEvent)
	 */
	@Override
	protected void onMessage(GameEvent e) {
		if (e instanceof OutOfBoundsEvent) {
			OutOfBoundsEvent event = (OutOfBoundsEvent) e;
			Model m = event.getModel();
			String message = event.getMessage();

			switch (message) {
			case "top":
				if (m instanceof Missile) {
					removeEntity(m);
				}
				break;
			case "bottom":
				if (m instanceof sideWinder)
					removeEntity(m);
				if (m instanceof Alien)
					ayylienBottom = true;
				alienHitBottom();
				break;
			case "left":
				if (m instanceof SpaceShip)
					shipLeft = true;
				if (m instanceof Alien)
					ayylienLeft = true;
				if (m instanceof HellAlien)
					hellAlienLeft = true;

				break;
			case "right":
				if (m instanceof SpaceShip)
					shipRight = true;
				if (m instanceof Alien)
					ayylienRight = true;
				if (m instanceof HellAlien)
					hellAlienRight = true;
				break;

			}
		}
	}

	private void alienHitBottom() {
		if (ayylienBottom) {
			((SIView) getView()).showLoseScreen(true);
			gameOver = true;
			deactivateAll();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see framework.Controller#updateInstanceParameters()
	 */
	@Override
	protected void updateInstanceParameters() {
		if (ayylienCount <= 0 || alienBossHp <= 0) {
			spaceShip.setHorizontalSpeed(0);
			gameOver = true;
			endingEffects();
			return;
		}

		if ((ActiveKeys & LEFT_KEY) != 0) {
			if (shipLeft) {
				spaceShip.setHorizontalSpeed(0);
				// spaceShit.setHorizontalAccel(0);
			} else {
				spaceShip.setHorizontalSpeed(-shipSpeed);
				// spaceShit.setHorizontalAccel(1.0f);
			}
		}

		if ((ActiveKeys & RIGHT_KEY) != 0) {
			if (shipRight) {
				spaceShip.setHorizontalSpeed(0);
				// spaceShit.setHorizontalAccel(0);
			} else {
				spaceShip.setHorizontalSpeed(shipSpeed);
				// spaceShit.setHorizontalAccel(1.0f);
			}
		}

		if (ayylienLeft || ayylienRight) {
			for (int i = 0; i < horde.length; i++) {

				if (ayylienLeft) {

					horde[i].setLocX(horde[i].getLocX() + 5);

				} else {
					horde[i].setLocX(horde[i].getLocX() - 5);
				}
				horde[i].setLocY(horde[i].getLocY() + 20);
				horde[i].setHorizontalSpeed(-horde[i].getHorizontalSpeed());
			}
		}

		if (hellAlien != null) {
			processHellAlienLogic();
		}
		
		

		if (ActiveKeys == 0 || (ActiveKeys == FIRE_KEY)) {
			spaceShip.setHorizontalSpeed(0);
		}

		if ((ActiveKeys & FIRE_KEY) != 0) {
			fire();
		}

	}

	private void processHellAlienLogic() {
		if ((hellAlienLeft || hellAlienRight) && (hellAyyliens <= 0)) {
			hellAlien.setHorizontalSpeed(-hellAlien.getHorizontalSpeed() * 1.1f);
			System.out.println(hellAlien.getHorizontalSpeed());

		} else if (((hellAlienLeft || hellAlienRight) && (hellAlien.getHorizontalSpeed() >= 800)
				|| (hellAlien.getHorizontalSpeed()) <= -800) && hellAyyliens <= 0) {
			hellAlien.setHorizontalSpeed(hellAlienSpeed);
			System.out.println("listen to me once");
			callTheHellHorde();

		} else if ((hellAlienLeft || hellAlienRight)) {
			hellAlien.setHorizontalSpeed(-hellAlien.getHorizontalSpeed());
			System.out.println("listen to me");
		}		
	}

	private void endingEffects() {
		if (startEasy == true) {
			((SIView) getView()).showEasyScreen(true);
			SoundFX.UKULELE.stop();
			SoundFX.YAY.play();
			startEasy = false;
		}
		if (startMedium == true) {
			((SIView) getView()).showMediumScreen(true);
			SoundFX.MEDIUM.play();
			startMedium = false;
		}
		if (startHard == true) {
			((SIView) getView()).showHardScreen(true);
			SoundFX.MEDIUM.play();
			startHard = false;
		}
		if (startHell == true) {
			((SIView) getView()).showHellScreen(true);
			SoundFX.HELL.stop();
			SoundFX.MEDIUM.play();
			SoundFX.YAY.play();
			startHell = false;
		}
	}

	private synchronized void fire() {
		long currentTime = System.currentTimeMillis();
		if (currentTime - ignitionTime < firingDelay) {
			return;
		}

		missile = new Missile("missile");
		missile.setActive(true);
		missile.setGhost(false);
		addEntity(missile, "missile-3.png", 1, 4);

		Dimension dim = spaceShip.getImageDstDimension();
		float x = spaceShip.getLocX();
		float y = spaceShip.getLocY();

		x += (float) dim.width / 2.6f;
		missile.setLocation((int) x, (int) y - 30);
		missile.setVerticalSpeed(-50);
		missile.setVerticalAcceleration(-1.0f);
		ignitionTime = currentTime;
		SoundFX.SHOOT.play();

	}

	private void summonSideWinders() {
		Timer sideWinderFalling = new Timer(500, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dropSideWinder();

			}
		});
		sideWinderFalling.start();
	}

	private void dropSideWinder() {
		if(spaceAndWinder) {
			return;
		}
		sideWinder = new sideWinder("sideWinder");
		sideWinder.setActive(true);
		sideWinder.setGhost(false);

		Random rand = new Random();
		int randomX = rand.nextInt(5 + (ViewWidth - 10));
		
		addEntity(sideWinder, "sidewinder-4.png", .2f, 7);
		
		sideWinder.setLocation(randomX, 15);
		sideWinder.setVerticalSpeed(-20);
		sideWinder.setVerticalAcceleration(2.0f);
		
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see framework.Controller#processCollisions(framework.CollisionEvent[])
	 */

	@Override
	protected void processCollisions(CollisionEvent[] ce) {
		for (CollisionEvent event : ce) {
			Model m1 = event.getM1();
			Model m2 = event.getM2();

			if (m1.isGhost() || m2.isGhost())
				return;

			if ((m1 instanceof Missile && m2 instanceof Alien) || (m2 instanceof Missile && m1 instanceof Alien)) {
				removeEntity(m1);
				removeEntity(m2);
				m1.setGhost(true);
				m2.setGhost(true);
				ayylienCount--;
				SoundFX.HIT.play();
				if (startHell) {
					removeEntity(m1);
					removeEntity(m2);
					m1.setGhost(true);
					m2.setGhost(true);
					hellAyyliens--;
					SoundFX.HIT.play();
				}
			}

			if ((m1 instanceof Alien && m2 instanceof SpaceShip) || (m1 instanceof SpaceShip && m2 instanceof Alien)) {
				removeEntity(m1);
				removeEntity(m2);
				deactivateAll();
				((SIView) getView()).showLoseScreen(true);
				gameOver = true;
				SoundFX.HIT.play();
			}
			
			if ((m1 instanceof sideWinder && m2 instanceof SpaceShip) || (m1 instanceof SpaceShip && m2 instanceof sideWinder)) {
				removeEntity(m1);
				removeEntity(m2);
				deactivateAll();
				((SIView) getView()).showLoseScreen(true);
				gameOver = true;
				SoundFX.HIT.play();
			}

			if ((m1 instanceof Missile && m2 instanceof HellAlien)
					|| (m1 instanceof HellAlien && m2 instanceof Missile)) {
				removeEntity(m2);
				alienBossHp = alienBossHp - 5;
				System.out.println(alienBossHp);
				System.out.println(hellAyyliens);
				SoundFX.HIT.play();
			}

			if ((hellAyyliens > 0) && ((m1 instanceof Missile && m2 instanceof HellAlien))
					|| ((hellAyyliens > 0) && (m1 instanceof HellAlien && m2 instanceof Missile))) {
				removeEntity(m2);
				alienBossHp = alienBossHp + 5;
				System.out.println(alienBossHp);
				SoundFX.INVINCIBLE.play();
			}

			if (alienBossHp <= 0) {
				removeEntity(hellAlien);
				deactivateAll();
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new SIController().start();
			}
		});
	}

}
