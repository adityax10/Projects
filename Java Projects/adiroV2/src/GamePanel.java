import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.colorchooser.ColorChooserComponentFactory;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.plaf.ColorUIResource;

public class GamePanel extends JPanel implements Runnable, KeyListener {

	// VARIABLES
	static int WIDTH;
	static int HEIGHT;
	static Thread t;
	Boolean isRunning;
	int enemyWaveNumber[] = {4, 8, 12, 16, 32,64 , 128, 256};
	static int score;

	// Player & Bullets
	Player p;
	static ArrayList<Bullet> bullets;
	// Enemy
	static ArrayList<Enemy> enemies;

	// enemyWaves
	boolean waveStart;
	int waveNumber;
	long waveStartTimer;
	int waveDelay;
	boolean isProcessingDelay;

	// canvas
	BufferedImage canvas;
	// brush
	static Graphics2D g;

	// FPS
	int FPS = 30;
	double averageFPS;
	int frameCount = 0;
	int maxframeCount = FPS;

	// Time
	long startTime;
	long waitTime;
	// total time actually taken by the complete gameloop
	long totalTime;
	// time actually taken by the gameloop for URD
	long URDTime;
	// desired time interval b/w frames for drawing the FPS in 1 sec
	long targetTime = 1000 / FPS;

	// CONSTRUCTOR
	public GamePanel(int w, int h) {
		// calling the super class constructor
		super();
		WIDTH = w;
		HEIGHT = h;

		// enemyNumber=100;
		waveDelay = 2000;
		waveNumber = 0;
		waveStart = true;
		waveStartTimer = 0;
		isProcessingDelay = true;
		score = 0;
		// Settting the Panel Size
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		// allow panel to get in focus
		setFocusable(true);
		// request for focus
		requestFocusInWindow();
	}

	/*
	 * Notifies this component that it now has a parent component. When this
	 * method is invoked, the chain of parent components is set up with
	 * KeyboardAction event listeners. This method is called by the toolkit
	 * internally and should not be called directly by programs
	 */
	public void addNotify() {
		super.addNotify();
		if (t == null)
			t = new Thread(this);
		t.start();
		addKeyListener(this);
	}

	// Thread Run Method
	public void run() {
		// TODO Auto-generated method stub
		isRunning = true;
		totalTime = 0;
		frameCount = 0;
		// player and bullets
		p = new Player();
		bullets = new ArrayList<>();

		// enemies
		enemies = new ArrayList<>();

		// setting the canvas
		canvas = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
		// setting the graphics by aallocating it to the canvas
		g = (Graphics2D) canvas.getGraphics();
		// for graphics
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		// for text
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		while (isRunning) {

			startTime = System.currentTimeMillis();
			gameUpdate();
			gameRender();
			gameDraw();

			URDTime = (System.currentTimeMillis() - startTime);
			waitTime = targetTime - URDTime;

			// now sleep the thread for waitTime
			try {
				t.sleep(waitTime);
			} catch (Exception e) {

			}
			totalTime += System.currentTimeMillis() - startTime;
			// System.out.println("Total time"+totalTime+"\n URDTIme :"+URDTime+"\n WaitTime : "+waitTime);

			frameCount++;
			if (frameCount == maxframeCount) {
				averageFPS = ((double) totalTime / frameCount);
				frameCount = 0;
				totalTime = 0;
			}
		}
	}

	public void addEnemy(int x) {
		for (int i = 0; i < x; i++) {
			enemies.add(new Enemy(1));
		}
	}

	public void gameUpdate() {

		// Creating enemyWave
		if (waveStart == true && enemies.size() == 0) {
			// waveStartTimer = System.currentTimeMillis();
			addWave(waveNumber);
			waveNumber++;
		} else {
			waveStart = false;
			if (enemies.size() == 0 && isProcessingDelay) {
				waveStartTimer = System.currentTimeMillis();
				isProcessingDelay = false;
			}

			long waveWaitTime = System.currentTimeMillis() - waveStartTimer;

			if (waveWaitTime > waveDelay && enemies.size() == 0) {
				waveStart = true;
				isProcessingDelay = true;
			}
		}

		// For Player
		p.Update();

		// for Bullets check for remval
		for (int i = 0; i < bullets.size(); i++) {
			boolean remove = bullets.get(i).Update();
			if (remove) {
				bullets.remove(i);
			}
		}

		// for Enemy
		for (int i = 0; i < enemies.size(); i++) {
			enemies.get(i).Update();
		}

		checkBulletEnemyCollision();

		if (checkPlayerEnemyCollison() && !p.isRecovering) {
			p.lives--;
			p.recovery();
		}
	}

	public void gameRender() {

		if (p.lives < 0) {
			gameOver(g);
		} else {
			// drawing to an offline screen(off screen)
			g.setColor(Color.WHITE);
			g.fillRect(0, 0, WIDTH, HEIGHT);
			g.setColor(Color.BLACK);

			// g.drawString("FPS : "+averageFPS, 20, 20);
			// g.drawString("Bullets : "+bullets.size()+"\n Enimies "+enemies.size(),
			// 20, 20);

			// draw Score
			g.setColor(Color.BLACK);
			g.setFont(new Font(Font.SERIF, Font.BOLD + Font.ITALIC, 20));
			g.drawString("Score : " + score, 20, 20);

			// draw lives
			for (int i = 0, x = 20, y = 40; i < p.lives; i++) {
				int r = 10;
				g.setStroke(new BasicStroke(3));
				g.setColor(Color.GRAY.brighter());
				g.fillOval(x, y, 2 * r, 2 * r);
				g.setStroke(new BasicStroke(4));
				g.setColor(Color.black);
				g.drawOval(x, y, 2 * r, 2 * r);
				g.setStroke(new BasicStroke(4));
				g.setColor(Color.white);
				g.fillOval(x + r / 2, y + r / 2, r, r);
				x += 30;
			}

			// drawWaveNumber
			if (enemies.size() == 0 && isProcessingDelay == false) {
				String s = "- " + " W A V E    N U M B E R  " + waveNumber
						+ " -";

				g.setColor(Color.BLACK);
				g.setFont(new Font("Curlz MT", Font.BOLD + Font.ITALIC, 30));
				g.drawString(s, WIDTH / 2 - s.length() * 8, HEIGHT / 2 + 10);
			}

			// Drawing player
			p.draw(g);
			// Drawing Bullets
			for (int i = 0; i < bullets.size(); i++) {
				bullets.get(i).draw(g);
			}
			// Drawing Enemy
			for (int i = 0; i < enemies.size(); i++) {
				enemies.get(i).draw(g);
			}
		}

	}

	public void gameDraw() {
		// drawing offline screen to the onScreen(canvas)
		Graphics g2 = this.getGraphics();
		g2.drawImage(canvas, 0, 0, this);
		g2.dispose();
	}

	private void addWave(int wavNo) {
		// TODO Auto-generated method stub
		if (wavNo < enemyWaveNumber.length) {
			addEnemy(enemyWaveNumber[wavNo]);
		}
	}

	// bullet enemy collision
	public static void checkBulletEnemyCollision() {
		for (int i = 0; i < GamePanel.bullets.size(); i++) {
			Bullet b = GamePanel.bullets.get(i);

			for (int j = 0; j < GamePanel.enemies.size(); j++) {
				Enemy e = GamePanel.enemies.get(j);
					double dx = e.x - b.x;
					double dy = e.y - b.y;
					double dist = Math.sqrt(dx * dx + dy * dy);
					if (dist < 2*e.r) {
						System.out.println("Killing :!" + j);
						e.kill();
						GamePanel.enemies.remove(j);
						GamePanel.bullets.get(i).removeBullet();
						GamePanel.score += 10;
					}
			}
		}
	}

	// player Enemy Collision
	public boolean checkPlayerEnemyCollison() {
		for (int i = 0; i < enemies.size(); i++) {
			Enemy e = enemies.get(i);
			double dx = e.x - p.x;
			double dy = e.y - p.y;
			double dist = Math.sqrt(dx * dx + dy * dy);
			if (dist <= 2 * p.r)
				return true;
		}

		return false;
	}

	public void gameOver(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.setFont(new Font("Curlz MT", Font.BOLD + Font.ITALIC, 30));
		g.drawString("G A M E    O V E R !", WIDTH / 2 - 100, HEIGHT / 2 + 10);
		g.drawString("Y o u r   S c o r e : " + score, WIDTH / 2 - 100,HEIGHT / 2 + 50);
		isRunning = false;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			p.setBottom(true);
			break;
		case KeyEvent.VK_DOWN:
			p.setTop(true);
			break;
		case KeyEvent.VK_LEFT:
			p.setLeft(true);
			break;
		case KeyEvent.VK_RIGHT:
			p.setRight(true);
			break;
		case KeyEvent.VK_F:
			if (!p.isRecovering)
				p.setFiring = true;
			break;
		}
		// System.out.println("Key Pressed"+e.getKeyCode());
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		p.setBottom(false);
		p.setLeft(false);
		p.setRight(false);
		p.setTop(false);
		p.setFiring = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

}
