import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

public class Bullet {
	// VARIABLES
	int x;
	int y;
	int dx, dy;
	double rad;
	int speed;
	int r;
	boolean isAlive;

	// CONSTRUCTOR
	public Bullet(int x, int y, double angle) {
		this.x = x;
		this.y = y;
		r = 10;
		rad = Math.toRadians(angle);
		dx = (int) Math.cos(rad);
		dy = (int) Math.sin(rad);
		speed = 10;
		isAlive = true;
	}
	
	//RemoveBullet
	void removeBullet()
	{
		isAlive = false;
	}

	// FUNCTIONS

	int getX() {
		return x;
	};

	int getY() {
		return y;
	};

	public boolean Update() {
		if(isAlive)
		{
		x += dx * speed;
		y += dy * speed;

		// check out of bounds
		if ((x < 0 || x > GamePanel.WIDTH) || (y < 0 || y > GamePanel.HEIGHT)) {
			return true;
		}
		}
		else
			return true;
		
		return false;
	}

	public void draw(Graphics2D g) {
		g.setColor(Color.gray);
		g.fillOval(x-r/2, y, r, r);
		g.setColor(Color.RED);
		g.setStroke(new BasicStroke(2));
		g.drawOval(x-r/2, y, r, r);
	}
}
