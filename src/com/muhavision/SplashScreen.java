package com.muhavision;

//Beautiful imports
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class SplashScreen extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		new SplashScreen();
	}

	// Very nice loading screen
	public SplashScreen() {

		// Some graphic magic I don't want to know what it does.
		setUndecorated(true);
		setSize(600, 200);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width / 2 - getSize().width / 2, dim.height / 2
				- getSize().height / 2);
		add(new JLabel(new ImageIcon("./res/logo.png")));
		setVisible(true);
		try {
			Thread.sleep(8000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		setVisible(false);
	}

}
// Done with graphics (for now).
