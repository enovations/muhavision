package com.muhavision.cv.image;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

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
		g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
	}

}
