package com.muhavision.cv.image;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.codeminders.ardrone.Point;
import com.muhavision.cv.QuadrantFlowSpeed;

public class VisualRenderer extends JPanel{
	
	public BufferedImage image = null;
	long prevData = 0;
	float avgFps;
	float filtT = 10;
	
	ImageData data = new ImageData();
	QuadrantFlowSpeed speed = new QuadrantFlowSpeed();
	
	public VisualRenderer() {
		
	}
	
	public void setDataProps(JFrame frame){
		data = ImageHelper.getScaledData(frame);
	}
	
	public void reloadDatas(BufferedImage image, QuadrantFlowSpeed speed){
		this.image = image;
		this.speed = speed;
		repaint();
	}
	
	@Override
	public void paintComponent(Graphics g_panel){
		BufferedImage bimage = new BufferedImage(320, 240, BufferedImage.TYPE_INT_RGB);
		Graphics g = bimage.getGraphics();
		float diff = (((float)System.nanoTime() - prevData))/1000000000.0f;
		prevData = System.nanoTime();
		float fps = 1.0f/diff;
		avgFps = (avgFps*filtT+fps)/(filtT+1);
		if(image!=null)
			g.drawImage(image, 0, 0, this);
		else
			g.drawImage(new ImageIcon("./res/nosignal.png").getImage(), 0, 0, 320, 240, this);
		g.drawString("FPS: "+(int)avgFps, 30, 30);
		g.setColor(Color.red);
		if(speed!=null){
			for (Point p : speed.tmp1) {
				g.drawOval(p.getX(), p.getY(), 3, 3);
			}
			g.setColor(Color.green);
			int cnt = 0;
			for (Point p : speed.tmp2) {
				g.drawOval(p.getX(), p.getY(), 3, 3);
				cnt++;
			}
			g.setColor(Color.yellow);
			for (int i = 0; i < cnt; i++) {
				g.drawLine(speed.tmp1.get(i).getX(), speed.tmp1.get(i).getY(), speed.tmp2.get(i).getX(), speed.tmp2.get(i).getY());
			}
			
		}
		g_panel.drawImage(bimage, 0, 0, getWidth(), getHeight(), this);
	}

}
