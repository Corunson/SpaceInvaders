/**
 * 
 */
package Invaders;

import static framework.GameConstants.*;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JPanel;

import framework.View;

/**
 * @author grv5783
 *
 */
public class SIView extends View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean startingScreen = false;
	private boolean showEasyScreen = false;
	private boolean showMediumScreen = false;
	private boolean showHardScreen = false;
	private boolean showHellScreen = false;
	private boolean showLoseScreen = false;
	Rectangle rect = new Rectangle(305, 450, 100, 50);
	private Canvas canvas;
	
	/**
	 * 
	 */
	public SIView() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param title
	 */
	public SIView(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}

	public void startingScreen(boolean state) {
		startingScreen = state;
	}

	public void showEasyScreen(boolean state) {
		showEasyScreen = state;
	}

	public void showMediumScreen(boolean state) {
		showMediumScreen = state;
	}

	public void showHardScreen(boolean state) {
		showHardScreen = state;
	}

	public void showHellScreen(boolean state) {
		showHellScreen = state;
	}

	public void showLoseScreen(boolean state) {
		showLoseScreen = state;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see framework.View#preRender(java.awt.Graphics2D)
	 */
	@Override
	protected void preRender(Graphics2D gc) {
		gc.setColor(Color.BLACK);
		gc.fillRect(0, 0, ViewWidth, ViewHeight);
		if (startingScreen) {
			canvas();
			painter(gc);
			renderCanvas();
		}
	}

	private void canvas() {
		setSize(getWidth(), getHeight());
		JPanel graphicsPanel = new JPanel();
		add(graphicsPanel);

		canvas = new Canvas();

		graphicsPanel.add(canvas);
	}

	private void renderCanvas() {
		Graphics gc = canvas.getGraphics();
		gc.dispose();
	}

	private void painter(Graphics2D gc) {

		super.paint(gc);
		Color easy = new Color(255, 255, 102);
		Color medium = new Color(255, 92, 51);
		Font mediumFont = new Font("Courier New", Font.PLAIN, 26);
		Color hard = new Color(230, 0, 0);
		Color hell = new Color(102, 0, 0);

		gc.setColor(Color.BLACK);
		gc.fillRect(0, 0, ViewWidth, ViewHeight);
		gc.setColor(easy);
		gc.fillRect(305, 450, 100, 50);
		gc.setColor(medium);
		gc.setFont(mediumFont);
		gc.fillRect(455, 450, 100, 50);
		gc.setColor(hard);
		gc.fillRect(605, 450, 100, 50);
		gc.setColor(hell);
		gc.fillRect(755, 450, 100, 50);

		displayInboxMessage(new Rectangle(305, 450, 100, 50), gc, Color.ORANGE, "easy");
		displayInboxMessage1(new Rectangle(455, 450, 100, 50), gc, Color.ORANGE, "medium");
		displayInboxMessage(new Rectangle(605, 450, 100, 50), gc, Color.ORANGE, "hard");
		displayInboxMessage(new Rectangle(755, 450, 100, 50), gc, Color.ORANGE, "hell!");

	}

	private void displayInboxMessage(Rectangle box, Graphics2D gc, Color color, String message) {
		Color oldColor = gc.getColor();
		Font oldFont = gc.getFont();
		Font font = new Font("Courier New", Font.PLAIN, 26);
		gc.setFont(font);
		gc.setColor(color);
		int stringWidth = gc.getFontMetrics().stringWidth(message);
		int stringHeight = gc.getFontMetrics().getAscent();
		gc.drawString(message, box.x + box.width / 2 - stringWidth / 2, box.y + stringHeight + 5);
		gc.setColor(oldColor);
		gc.setFont(oldFont);

	}

	private void displayInboxMessage1(Rectangle box, Graphics2D gc, Color color, String message) {
		Color oldColor = gc.getColor();
		Font oldFont = gc.getFont();
		Font font = new Font("Courier New", Font.PLAIN, 24);
		gc.setFont(font);
		gc.setColor(color);
		int stringWidth = gc.getFontMetrics().stringWidth(message);
		int stringHeight = gc.getFontMetrics().getAscent();
		gc.drawString(message, box.x + box.width / 2 - stringWidth / 2, box.y + stringHeight + 5);
		gc.setColor(oldColor);
		gc.setFont(oldFont);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see framework.View#postRender(java.awt.Graphics2D)
	 */
	@Override
	protected void postRender(Graphics2D gc) {
		if (showEasyScreen) {
			gc.setColor(Color.YELLOW);
			gc.fillOval(-25, -25, 75, 75);
			gc.drawLine(65, 25, 175, 75);
			gc.drawLine(50, 45, 150, 150);
			gc.drawLine(20, 60, 70, 135);
			displayMessage("GET OUT OF EASY MODE, YOU CAN'T BE GOOD IF YOU ONLY PLAY EASYMODE :D!", gc, Color.PINK, 0);
			displayMessage("ESC to Exit!", gc, Color.YELLOW, 30);
		}
		if (showMediumScreen) {
			displayMessage("Hmmm... improvment has been made!", gc, Color.BLUE, 0);
			displayMessage("ESC to Exit!", gc, Color.YELLOW, 30);

		}
		if (showHardScreen) {
			displayMessage("NOW WE ARE GETTING PLACES!", gc, Color.WHITE, 0);
			displayMessage("ESC to Exit!", gc, Color.YELLOW, 30);

		}
		if (showHellScreen) {
			displayMessage("YOU HAVE THE POWER OF GOD....", gc, Color.GREEN, 0);
			displayMessage("ESC to Exit!", gc, Color.YELLOW, 30);

		}
		if (showLoseScreen) {
			displayMessage("GAME OVER, GOD YOU'RE AWFUL!", gc, Color.RED, 0);
			displayMessage("ESC to Exit!", gc, Color.YELLOW, 30);
		}

	}

}
