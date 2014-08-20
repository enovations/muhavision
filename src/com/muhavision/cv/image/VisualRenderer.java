package com.muhavision.cv.image;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class VisualRenderer extends JPanel{
	
	public BufferedImage image = null;
	long prevData = 0;
	float avgFps;
	float filtT = 10;
	
	ImageData data = null;
	
	public VisualRenderer(JFrame frame) {
		data = ImageHelper.getScaledData(frame);
	}
	
	public void reloadDatas(BufferedImage image){
		this.image = image;
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g){
		System.out.println(data.w+":"+data.h);
		float diff = (((float)System.nanoTime() - prevData))/1000000000.0f;
		prevData = System.nanoTime();
		float fps = 1.0f/diff;
		avgFps = (avgFps*filtT+fps)/(filtT+1);
		if(image!=null)
			g.drawImage(image, data.x, data.y, data.w, data.h, this);
		else
			g.drawImage(new ImageIcon("./res/nosignal.png").getImage(), 0, 0, getWidth(), getHeight(), this);
		g.drawString("FPS: "+(int)avgFps, 30, 30);
	}

}
