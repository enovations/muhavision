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

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.UIManager;

import com.muhavision.control.DroneController;

public class Main {
	
	JFrame controlTowerFrame = new JFrame("Muha Mission Planner");
	
	DroneController controller = new DroneController();
	
	public Main() {
		
		JToolBar commands = new JToolBar();
		
		JButton takeoff = new JButton("Take off");
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
		
		JButton land = new JButton("Land");
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
		
		controlTowerFrame.add("North", commands);
		
		controlTowerFrame.setResizable(false);
		controlTowerFrame.setSize(700, 500);
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		controlTowerFrame.setLocation(dim.width/2-controlTowerFrame.getSize().width/2, 
					dim.height/2-controlTowerFrame.getSize().height/2);		
		controlTowerFrame.setVisible(true);
		controlTowerFrame.setFocusable(true);
		controlTowerFrame.setFocusableWindowState(true);
		controlTowerFrame.addKeyListener(new KeyListener() {	
			@Override public void keyTyped(KeyEvent arg0) {}
			@Override public void keyReleased(KeyEvent arg0) {}
			@Override
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyChar()=='\n')
					try {
						controller.getDrone().sendEmergencySignal();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		});
	}
	
	public static void main(String[] args) {
		try{
			UIManager.setLookAndFeel(
		            UIManager.getSystemLookAndFeelClassName());
		}catch(Exception e){}
		new Main();
	}

}
