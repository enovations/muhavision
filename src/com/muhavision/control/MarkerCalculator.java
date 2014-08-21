package com.muhavision.control;

import com.muhavision.Main;
import com.muhavision.pid.PID;

public class MarkerCalculator {
	
	static PID yaw = new PID(1, 0, 0, 0, PID.Direction.NORMAL);
	
	public static void calculateAndControl(EulerAngles angles, Main main){
		
		int x1 = angles.picX;
		int x2 = angles.picX2;
		int y1 = angles.picY;
		int y2 = angles.picY2;
		
		int dx = Math.abs(x1-x2);
		int dy = Math.abs(y1-y2);
		
		if(dy!=0){
			
			int angle = (int)Math.toDegrees(Math.atan(dx/dy)) - 90;
			
			if(Math.abs(angle)<10){
				
				int yawControl = (x1+x2)/2;
				int offset = 160 - yawControl;
				
				float out = yaw.calculate(offset, 0);
				
				main.yaw = out*0.025f;
				
			}
			
			main.reloadControls();
		
		}
	}

}
