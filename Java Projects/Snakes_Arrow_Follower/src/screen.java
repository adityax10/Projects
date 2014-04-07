import java.awt.*;
import javax.swing.*;

public class screen extends JFrame{

	public JFrame window;
	// gives access to the video card on the device
	public GraphicsDevice videocard;

	public screen() {
		GraphicsEnvironment env = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		videocard = env.getDefaultScreenDevice();
	}
	

	public void setFullScreen(JFrame window, DisplayMode dm) {
		
		window.setUndecorated(true);
		window.setResizable(false);
		videocard.setFullScreenWindow(window);

		if (dm != null && videocard.isDisplayChangeSupported()) {
			try{
			videocard.setDisplayMode(dm);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	

	public Window getFullScreenWindow() {
		return videocard.getFullScreenWindow();
	}

	public void disposeWindow() {
		Window w = videocard.getFullScreenWindow();
		if (w != null) {
			w.dispose();
		}
		videocard.setFullScreenWindow(null);
	}
	
}
