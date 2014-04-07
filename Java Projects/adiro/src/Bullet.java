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

	// CONSTRUCTOR
	public Bullet(int x, int y, double angle) {
		this.x = x;
		this.y = y;
		r = 10;
		rad = Math.toRadians(angle);
		dx = (int) Math.cos(rad);
		dy = (int) Math.sin(rad);
		speed = 5;
	}

	// FUNCTIONS

	int getX() {
		return x;
	};

	int getY() {
		return y;
	};

	// Collison with enemy
	public static void checkCollision() {
		for (int i = 0; i < GamePanel.bullets.size(); i++) {
			Bullet b = GamePanel.bullets.get(i);

			for (int j = 0; j < GamePanel.enemies.size(); j++) {
				Enemy e = GamePanel.enemies.get(j);
				{
					if ((b.x >= e.x )&&( b.x <= (e.x + e.r))&&(b.y >= e.y)
							&& (b.y <= (e.y + e.r))){
						System.out.println("Killing :!");
						e.kill();
						GamePanel.enemies.remove(j);
						GamePanel.bullets.remove(i);
					}
				}
			}
		}
	}

	public boolean Update() {
		x += dx * speed;
		y += dy * speed;

		// check out of bounds
		if ((x < 0 || x > GamePanel.WIDTH) || (y < 0 || y > GamePanel.HEIGHT)) {
			return true;
		}
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
