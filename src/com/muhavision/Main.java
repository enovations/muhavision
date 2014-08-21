/**
 * 
 * @author Klemen
 * 
 * 
 * Main class file for the whole project. UI Thread is located here.
 * 
 * 
 */

package com.muhavision;

//Imports (obvious)
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.muhavision.control.DroneController;
import com.muhavision.control.ExpoController;
import com.muhavision.control.FlightMode;
import com.muhavision.cv.image.VisualRenderer;

//This is a class
public class Main {
	// Graphics (don't know what it does)
	JFrame controlTowerFrame = new JFrame("Muha Mission Planner");

	private final static int PACKETSIZE = 1024;

	// Let's start this and mami!!!
	public static void main(String[] args) {
		new Main();
	}

	public float roll, pitch, yaw, height;
	VisualRenderer visual = new VisualRenderer(this);

	DroneController controller = new DroneController(visual);

	public static final boolean DEBUG = true;

	public FlightMode flightMode = new FlightMode();

	public Main() {
		// More graphics
		if (!DEBUG)
			new SplashScreen();

		JPanel commands = new JPanel();

		commands.setBackground(Color.black);

		JButton takeoff = new JButton("Take off");
		takeoff.setBackground(Color.black);
		takeoff.setForeground(Color.white);
		takeoff.setFont(new Font("Arial", Font.PLAIN, 17));
		takeoff.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					controller.getDrone().takeOff();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		takeoff.setFocusable(false);
		commands.add(takeoff);

		// main panel
		JPanel visualHolder = new JPanel(new GridLayout());
		visualHolder.add(visual);
		controlTowerFrame.add("Center", visualHolder);

		JButton land = new JButton("Land");
		land.setBackground(Color.black);
		land.setForeground(Color.white);
		land.setFont(new Font("Arial", Font.PLAIN, 17));
		land.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					controller.getDrone().land();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		land.setFocusable(false);
		commands.add(land);

		JButton trim = new JButton("Flat trim");
		trim.setBackground(Color.black);
		trim.setForeground(Color.white);
		trim.setFont(new Font("Arial", Font.PLAIN, 17));
		trim.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					controller.getDrone().trim();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		trim.setFocusable(false);
		commands.add(trim);

		controlTowerFrame.add("North", commands);

		// controlTowerFrame.setResizable(false);
		// controlTowerFrame.setSize(700, 500);

		// controlTowerFrame.setUndecorated(true);

		// Now graphics may be visible.
		controlTowerFrame.setVisible(true);
		controlTowerFrame.setExtendedState(Frame.MAXIMIZED_BOTH);
		// controlTowerFrame.setFocusable(true);
		// controlTowerFrame.setFocusableWindowState(true);
		controlTowerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Now lets listen for mami!!!
		try {

			final DatagramSocket socket = new DatagramSocket(1234);

			Thread mami_listener = new Thread() {

				@Override
				public void run() {

					// On port 1234 mami sends x,y,z,a axis.
					while (true) {
						// x;y;z
						DatagramPacket packet = new DatagramPacket(
								new byte[PACKETSIZE], PACKETSIZE);

						try {
							socket.receive(packet);
						} catch (IOException e) {
							e.printStackTrace();
						}

						String[] mami_array = new String(packet.getData())
								.split("\\;");

						if (mami_array.length > 1) {

							pitch = (float) (ExpoController.getExpo(Integer
									.parseInt(mami_array[1].trim())));
							roll = (float) (ExpoController.getExpo(Integer
									.parseInt(mami_array[0].trim())));
							
							if (visual.global_main.flightMode.getMode() != FlightMode.eMode.TAG_MODE)
								yaw = (float) (ExpoController.getExpo((Integer
									.parseInt(mami_array[2].trim()))) / 4.7);// reduce
																				// response
							
							
							int mamih = Integer.parseInt(mami_array[3].trim());
							if (mamih == 2)
								height = -0.2f;
							else if (mamih == -1)
								height = 0.2f;
							else
								height = 0;

							reloadControls();

						}
					}
				}

			};

			mami_listener.start();

		} catch (SocketException e1) {
			e1.printStackTrace();
		}

		// Lets listen for some more mami!!!
		try {

			final DatagramSocket socket = new DatagramSocket(2345);

			// On port 2345 mami sends button presses.
			Thread mami_listener = new Thread() {

				@Override
				public void run() {
					while (true) {
						// x;y;z
						DatagramPacket packet = new DatagramPacket(
								new byte[PACKETSIZE], PACKETSIZE);
						try {
							socket.receive(packet);
						} catch (IOException e) {
							e.printStackTrace();
						}

						String[] mami_array = new String(packet.getData())
								.split("\\;");

						if (mami_array.length > 1) {

							if (Integer.parseInt(mami_array[0].trim()) == 1) {
								try {
									controller.getDrone().sendEmergencySignal();
								} catch (IOException e) {
									e.printStackTrace();
								}
							}

							if (Integer.parseInt(mami_array[3].trim()) == 1) {
								try {
									controller.getDrone().land();
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}

							else if (Integer.parseInt(mami_array[2].trim()) == 1) {
								try {
									controller.getDrone().takeOff();
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}

							else if (Integer.parseInt(mami_array[10].trim()) == 1) {
								flightMode
										.setMode(FlightMode.eMode.NORMAL_MODE);
							}

							else if (Integer.parseInt(mami_array[8].trim()) == 1) {
								flightMode.setMode(FlightMode.eMode.MUHA_MODE);
							}

							else if (Integer.parseInt(mami_array[6].trim()) == 1) {
								flightMode.setMode(FlightMode.eMode.TAG_MODE);
							}

							else if (Integer.parseInt(mami_array[1].trim()) == 1) {
								try {
									controller.getDrone().trim();
								} catch (IOException e1) {
									e1.printStackTrace();
								}
							}

							else if (Integer.parseInt(mami_array[4].trim()) == 1) {
								controller.getDrone().setCombinedYawMode(true);
							}

							else if (Integer.parseInt(mami_array[5].trim()) == 1) {
								controller.getDrone().setCombinedYawMode(false);
							}

						}
					}
				}

			};

			mami_listener.start();

		} catch (SocketException e1) {
			e1.printStackTrace();
		}

		visual.reloadDatas(null, null, null, null);
		
		flightMode.setMode(FlightMode.eMode.TAG_MODE);

	}

    //And the commands have been sent. Nothing else to do but hope that it works
	protected void reloadControls() {
		try {
			controller.getDrone().move(roll, pitch, height, yaw);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
