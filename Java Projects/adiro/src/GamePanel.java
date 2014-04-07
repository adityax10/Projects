import java.awt.Color;
import java.awt.Dimension;
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
	static int WIDTH ;
	static int HEIGHT;
	Thread t;
	Boolean isRunning;
	
	//Player & Bullets
	Player p ;
    static ArrayList<Bullet> bullets;
    //Enemy
    static ArrayList<Enemy> enemies;
    
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
	public GamePanel(int w,int h) {
		// calling the super class constructor
		super();
		WIDTH = w;
		HEIGHT =h;
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
		//player and bullets
		p = new Player();
		bullets = new ArrayList<>();
		//enemies
		enemies = new ArrayList<>();
		//initalising enemy =>
		for(int i=0;i<10;i++)
		{
			enemies.add(new Enemy(1));
		}
		
		// setting the canvas
		canvas = new BufferedImage(WIDTH, HEIGHT,
				BufferedImage.TYPE_INT_RGB);
		// setting the graphics by aallocating it to the canvas
		g = (Graphics2D) canvas.getGraphics();
		//for graphics
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		//for text
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

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
			//System.out.println("Total time"+totalTime+"\n URDTIme :"+URDTime+"\n WaitTime : "+waitTime);
			
			frameCount++;
			if (frameCount == maxframeCount) {
				averageFPS = ((double)totalTime / frameCount);
				frameCount = 0;
				totalTime = 0;
			}
		}
	}

	public void gameUpdate() {
		//For Player
		p.Update();
		
		//for Bullets check for remval
		for(int i=0;i<bullets.size();i++)
		{
			boolean remove = bullets.get(i).Update();
			if(remove)
			{
			bullets.remove(i);
			}
		}
		//for Enemy
		for(int i=0;i<enemies.size();i++)
		{
			enemies.get(i).Update();
		}
		
		Bullet.checkCollision();

	}

	public void gameRender() {
		// drawing to an offline screen(off screen)
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		g.setColor(Color.BLACK);
		
		//g.drawString("FPS : "+averageFPS, 20, 20);
		g.drawString("Bullets : "+bullets.size()+"\n Enimies "+enemies.size(), 20, 20);
		
		//Drawing player
		p.draw(g);
		//Drawing Bullets
		for(int i=0;i<bullets.size();i++)
		{
			bullets.get(i).draw(g);
		}
		//Drawing Enemy
		for(int i=0;i<enemies.size();i++)
		{
			enemies.get(i).draw(g);
		}
	}

	public void gameDraw() {
		// drawing offline screen to the onScreen(canvas)
		Graphics g2 = this.getGraphics();
		g2.drawImage(canvas, 0, 0, this);
		g2.dispose();

	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		switch(e.getKeyCode())
		{
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
			p.setFiring = true;
			break;
		}
		
		
		//System.out.println("Key Pressed"+e.getKeyCode());
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
