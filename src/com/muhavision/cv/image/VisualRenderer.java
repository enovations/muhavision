package com.muhavision.cv.image;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class VisualRenderer extends JPanel{
	
	public BufferedImage image = null;
	
	public VisualRenderer() {
		
	}
	
	public void reloadDatas(BufferedImage image){
		this.image = image;
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g){
		g.setColor(Color.black);
		if(image!=null)
			g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
		else
			g.drawImage(new ImageIcon("./res/nosignal.png").getImage(), 0, 0, getWidth(), getHeight(), this);
	}

}
