package com.muhavision;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class SplashScreen extends JFrame{

	public SplashScreen() {
		setUndecorated(true);
		setSize(600, 200);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation(dim.width/2-getSize().width/2, 
					dim.height/2-getSize().height/2);	
		add(new JLabel(new ImageIcon("./res/logo.png")));
		setVisible(true);
		for (int i = 0; i < 5; i++) {
			setLocation(
					(dim.width/2-getSize().width/2)+((int)(Math.random()*1000)-500), 
					(dim.height/2-getSize().height/2)+((int)(Math.random()*1000)-500));
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		setLocation(dim.width/2-getSize().width/2, 
					dim.height/2-getSize().height/2);	
		add(new JLabel(new ImageIcon("./res/logo.png")));
	}
	
	public static void main(String[] args) {
		new SplashScreen();
	}
	
}
