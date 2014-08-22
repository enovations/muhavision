package com.muhavision.control;

import com.muhavision.Main;
import com.muhavision.pid.PID;

public class MarkerCalculator {
	
	static PID yaw = new PID(0.25f, 0, 0, 0, PID.Direction.NORMAL);
	static PID pitch = new PID(0.005f, 0, 0.0003f, 0, PID.Direction.NORMAL);
	
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
			
			float pxdist = (float)Math.sqrt(dx*dx+dy*dy);
			
			if(pxdist<80){
				
				float pitchout = (pitch.calculate(pxdist, 40))/2;
				
				if(Math.abs(pitchout)<9){
					main.pitch = pitchout*-1;
				}
								
			}
						
			main.reloadControls();
		
		}
	}

}
