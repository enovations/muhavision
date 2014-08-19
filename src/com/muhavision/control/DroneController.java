package com.muhavision.control;

import com.muhavision.pid.PID;

public class DroneController {
	
	PID roll = new PID(1, 1, 0, PID.Direction.NORMAL);
	
	public DroneController() {
		
	}
	
	

}
