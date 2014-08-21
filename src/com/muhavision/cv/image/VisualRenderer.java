package com.muhavision.cv.image;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.codeminders.ardrone.NavData;
import com.muhavision.Main;
import com.muhavision.control.EulerAngles;
import com.muhavision.control.FlightMode;
import com.muhavision.cv.QuadrantFlowSpeed;

public class VisualRenderer extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public BufferedImage image = null;
	long prevData = 0;
	float avgFps;
	float filtT = 10;

	QuadrantFlowSpeed speed = new QuadrantFlowSpeed();

	NavData fdata = null;
	EulerAngles angles = null;

	public Main global_main = null;

	public VisualRenderer(Main main) {
		global_main = main;
	}

	@Override
	public void paintComponent(Graphics g_panel) {
		BufferedImage bimage = new BufferedImage(320, 240,
				BufferedImage.TYPE_INT_BGR);

		Graphics g = bimage.getGraphics();

		if (image != null)
			g.drawImage(image, 0, 0, this);
		else
			g.drawImage(new ImageIcon("./res/nosignal.png").getImage(), 0, 0,
					320, 240, this);

		g.setFont(new Font("Arial", Font.PLAIN, 10));

		if (fdata != null) {
			g.drawString("Battery: " + fdata.getBattery(), 250, 30);
		}
		if (angles != null) {
			g.drawString("Mark. dist.: " + angles.dist, 250, 38);
			g.fillRect(angles.picX, angles.picY, 4, 4);
		}

		g.drawString("Roll: " + (int) global_main.roll, 30, 30);
		g.drawString("Pitch: " + (int) global_main.pitch, 30, 38);
		g.drawString("Yaw: " + (int) global_main.yaw, 30, 46);
		g.drawString("Height: " + (int) global_main.height, 30, 54);

		g.drawOval(156, 116, 5, 5);

		if (speed != null) {
			g.drawLine(156, 116, 156 + (speed.rx - speed.lx), 116);
		}

		if (global_main != null) {
			g.drawString(FlightMode.getFlightModeName(global_main.flightMode
					.getMode()), 138, 80);
		}

		g_panel.drawImage(bimage, 0, 0, getWidth(), getHeight(), this);
	}

	public void reloadDatas(BufferedImage image, QuadrantFlowSpeed speed,
			NavData fdata, EulerAngles angles) {
		this.image = image;
		this.speed = speed;
		this.fdata = fdata;
		this.angles = angles;
		repaint();
	}

}
