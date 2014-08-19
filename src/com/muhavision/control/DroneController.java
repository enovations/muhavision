package com.muhavision.control;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.UnknownHostException;

import com.codeminders.ardrone.ARDrone;
import com.codeminders.ardrone.NavData;
import com.codeminders.ardrone.NavDataListener;
import com.codeminders.ardrone.util.BufferedImageVideoListener;
import com.muhavision.pid.PID;

public class DroneController {
	
	PID roll = new PID(1, 1, 0, PID.Direction.NORMAL);
	
	ARDrone drone = null;
	
	public DroneController() {
		System.out.println("Drone controller loading...");
		try {
			
			drone = new ARDrone();
			drone.connect();

			drone.addImageListener(new BufferedImageVideoListener() {
				
				@Override
				public void imageReceived(BufferedImage image) {
					try {
						drone.sendAllNavigationData();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			
			drone.addNavDataListener(new NavDataListener() {
				
				@Override
				public void navDataReceived(NavData data) {
					
				}
			});
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ARDrone getDrone(){
		return drone;
	}

}