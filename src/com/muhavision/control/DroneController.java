package com.muhavision.control;

import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.IOException;
import java.net.UnknownHostException;

import com.codeminders.ardrone.ARDrone;
import com.codeminders.ardrone.NavData;
import com.codeminders.ardrone.NavDataListener;
import com.codeminders.ardrone.util.BufferedImageVideoListener;
import com.muhavision.cv.MarkerTracker;
import com.muhavision.cv.OpticalFlowCalculator;
import com.muhavision.cv.QuadrantFlowSpeed;
import com.muhavision.cv.image.VisualRenderer;
import com.muhavision.pid.PID;

//Controlling the quad copter based on enabled mode

public class DroneController {

	PID roll = new PID(1, 1, 0, 10, PID.Direction.NORMAL);

	OpticalFlowCalculator calc = new OpticalFlowCalculator();

	ARDrone drone = null;
	NavData data = null;
	
	BufferedImage quadImage = null;

	public DroneController(final VisualRenderer visual) {
		System.out.println("Drone controller loading...");
		try {

			drone = new ARDrone();
			drone.connect();

			drone.addImageListener(new BufferedImageVideoListener() {

				@Override
				public void imageReceived(BufferedImage image) {
					RescaleOp rescaleOp = new RescaleOp(1.8f, -20, null);
					rescaleOp.filter(image, image);
					quadImage = image;
				}
			});

			drone.addNavDataListener(new NavDataListener() {
				
				@Override
				public void navDataReceived(NavData fdata) {
					data = fdata;
				}
			});
			
			Thread t = new Thread(){
				
				public void run(){
					while(true){
						
						long millis = System.currentTimeMillis();
						
						QuadrantFlowSpeed speed = null;
						EulerAngles angle = null;

						if(visual.global_main.flightMode!=null)
							if (visual.global_main.flightMode.getMode() == FlightMode.eMode.MUHA_MODE)
								speed = calc.getFlowData(quadImage);
							else if (visual.global_main.flightMode.getMode() == FlightMode.eMode.TAG_MODE)
								angle = MarkerTracker.getMarkerData(quadImage);

						visual.reloadDatas(quadImage, speed, data, angle);
						
						millis = System.currentTimeMillis() - millis;
						
						try {
							if(millis<70)
								Thread.sleep(70-millis);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						
					}
				}
				
			};
			t.start();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ARDrone getDrone() {
		return drone;
	}

}