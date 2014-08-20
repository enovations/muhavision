package com.muhavision.cv.image;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.codeminders.ardrone.NavData;
import com.codeminders.ardrone.Point;
import com.muhavision.Main;
import com.muhavision.cv.QuadrantFlowSpeed;

public class VisualRenderer extends JPanel{
	
	public BufferedImage image = null;
	long prevData = 0;
	float avgFps;
	float filtT = 10;
	
	ImageData data = new ImageData();
	QuadrantFlowSpeed speed = new QuadrantFlowSpeed();
	
	NavData fdata = null;
	
	Main global_main = null;
	
	public VisualRenderer(Main main) {
		this.global_main = main;
	}
	
	public void setDataProps(JFrame frame){
		data = ImageHelper.getScaledData(frame);
	}
	
	public void reloadDatas(BufferedImage image, QuadrantFlowSpeed speed, NavData fdata){
		this.image = image;
		this.speed = speed;
		this.fdata = fdata;
		repaint();
	}
	
	public void reloadNoData(){
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
		g.setFont(new Font("Arial", Font.PLAIN, 10));
		if(fdata!=null){
			g.drawString("Battery: "+(int)fdata.getBattery(), 250, 30);
			g.drawString("Speed: "+(int)fdata.getVx(), 250, 30);
		}
		g.drawString("Roll: "+(int)global_main.roll, 30, 30);
		g.drawString("Pitch: "+(int)global_main.pitch, 30, 38);
		g.drawString("Yaw: "+(int)global_main.yaw, 30, 46);
		g.drawString("Height: "+(int)global_main.height, 30, 54);
		//g.setColor(Color.yellow);
		//g.drawLine(140, 50, 140, 190);
		//g.drawLine(180, 50, 180, 190);
		g.setColor(Color.red);
		if(speed!=null){
			g.drawLine(80, 80, 80+(speed.lx*2), 80);
			g.drawLine(80, 81, 80+(speed.lx*2), 81);
			g.drawOval(80, 80, 5, 5);
			
			g.drawLine(200, 80, 200+(speed.rx*2), 80);
			g.drawLine(200, 81, 200+(speed.rx*2), 81);
			g.drawOval(200, 80, 5, 5);
			
			g.setColor(Color.pink);
			
			g.drawLine(140, 140, 140+((speed.rx-speed.lx)), 140);
			g.drawLine(141, 141, 141+((speed.rx-speed.lx)), 141);
			g.drawOval(140, 140, 5, 5);
		}
		Graphics2D g2d = (Graphics2D) g;
		if(fdata!=null){
			int roll = (int) fdata.getRoll();
			int pitch = (int) fdata.getPitch();
			g2d.setColor(Color.white);
			g.drawOval(156, 116, 8, 8);
			g2d.rotate(Math.toRadians(roll), 160, 120);
			g2d.drawLine(90, 120, 230, 120);
			g2d.drawString("MUHA", 150, 80);
			g.drawLine(120, 50, 120, 190);
			g.drawLine(200, 50, 200, 190);
			g.drawOval(116, 116+(int)((float)(pitch)*1.3), 8, 8);
			g.drawOval(196, 116+(int)((float)(pitch)*1.3), 8, 8);
			
		}
		/*	g.setColor(Color.green);
			int cnt = 0;
			for (Point p : speed.tmp2) {
				g.drawOval(p.getX(), p.getY(), 3, 3);
				cnt++;
			}
			g.setColor(Color.yellow);
			for (int i = 0; i < cnt; i++) {
				g.drawLine(speed.tmp1.get(i).getX(), speed.tmp1.get(i).getY(), speed.tmp2.get(i).getX(), speed.tmp2.get(i).getY());
			}
			
		}*/
		g_panel.drawImage(bimage, 0, 0, getWidth(), getHeight(), this);
	}

}
