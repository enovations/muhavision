package com.muhavision;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.codeminders.ardrone.NavData;
import com.muhavision.control.FlightMode;
import com.muhavision.control.LocationData;

//Some more graphics.
//They provide nice hub that lags the shit out of your computer.

public class VisualRenderer extends JPanel {

	private static final long serialVersionUID = 1L;
	public BufferedImage image = null;
	long prevData = 0;
	float avgFps;
	float filtT = 10;

	LocationData speed = new LocationData();

	NavData fdata = null;
	LocationData angles = null;

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
			g.setColor(Color.orange);
			g.drawString("Battery: " + fdata.getBattery(), 230, 30);
			g.setColor(Color.red);
			g.drawString("Flying: " + fdata.isFlying(), 230, 200);
			g.setColor(Color.gray);
			g.drawString("Height: " + fdata.getAltitude(), 230, 220);
			g.setColor(Color.white);
		}

		if (angles != null) {
			if (angles.dist != -1)
				g.drawString("Mark. dist.: " + (int) angles.dist, 230, 38);
			g.setColor(Color.yellow);
			g.fillRect(angles.picX, angles.picY, 8, 8);
			g.setColor(Color.green);
			g.fillRect(angles.picX2, angles.picY2, 8, 8);
			g.setColor(Color.white);
		}

		g.setColor(Color.green);

		g.drawString("Roll: " + (int) global_main.roll, 30, 30);
		g.drawString("Pitch: " + (int) global_main.pitch, 30, 38);
		g.drawString("Yaw: " + (int) global_main.yaw, 30, 46);
		g.drawString("Height: " + (int) global_main.height, 30, 54);

		g.setColor(Color.white);

		g.drawOval(156, 116, 5, 5);

		if (global_main.flightMode.getMode() == FlightMode.eMode.MUHA_MODE) {

			g.setColor(Color.yellow);

			g.drawOval(156, 186, 5, 5);
			g.drawLine(158, 187, 158 + (speed.rx - speed.lx), 187);
			g.drawLine(158, 188, 158 + (speed.rx - speed.lx), 188);
			g.drawLine(158, 189, 158 + (speed.rx - speed.lx), 189);
			g.drawLine(100, 140, 100 + speed.lx, 140);
			g.drawLine(220, 140, 220 + speed.rx, 140);

			g.setColor(Color.white);

		}

		if (global_main != null) {
			g.setColor(Color.red);
			g.drawString(FlightMode.getFlightModeName(global_main.flightMode
					.getMode()), 138, 80);
			g.setColor(Color.white);
		}

		Graphics2D g2d = (Graphics2D) g;

		int roll = 0, pitch = 0;

		if (fdata != null) {
			roll = (int) fdata.getRoll();
			pitch = (int) fdata.getPitch();
		}

		g.drawLine(100, 50, 100, 190);
		g.drawLine(220, 50, 220, 190);
		g.drawOval(96, 116 + (int) ((pitch) * 1.3), 8, 8);
		g.drawOval(216, 116 + (int) ((pitch) * 1.3), 8, 8);
		if (fdata != null && fdata.isEmergency()) {
			g.setColor(Color.red);
			g.setFont(new Font("Arial", Font.PLAIN, 15));
			g.drawString("EMERGENCY", 112, 220);
		}
		g2d.rotate(Math.toRadians(roll), 160, 120);
		g2d.drawLine(90, 120, 230, 120);

		g_panel.drawImage(bimage, 0, 0, getWidth(), getHeight(), this);
	}

	public void reloadDatas(BufferedImage image, LocationData speed,
			NavData fdata, LocationData angles) {
		this.image = image;
		this.speed = speed;
		this.fdata = fdata;
		this.angles = angles;
		repaint();
	}

}
