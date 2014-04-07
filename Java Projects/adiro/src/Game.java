import java.awt.Color;
import java.awt.DisplayMode;

import javax.swing.*;

public class Game {
	
	public static void main(String args[])
	{
		JFrame window = new JFrame("Adiro");
		//window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		//Setting the pane in the window with gamepanel
		window.setContentPane(new GamePanel(600,600));

		window.setResizable(false);
		//window.setUndecorated(true);
		window.setVisible(true);
		window.pack();
	}
	

}
