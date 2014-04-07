import java.awt.Color;
import java.awt.DisplayMode;

import javax.swing.*;

public class Game {
	
	public static void main(String args[])
	{
		screen s = new screen();
		JFrame window = new JFrame("Adiro");
		//window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		DisplayMode dm = new DisplayMode(1366,768,32,
				DisplayMode.REFRESH_RATE_UNKNOWN);
		
		
		//Setting the pane in the window with gamepanel
		window.setContentPane(new GamePanel(1366,768));

		window.setResizable(false);
		window.setUndecorated(true);
		//window.setVisible(true);
		//window.pack();
		s.setFullScreen(window, dm);
	}
	

}
