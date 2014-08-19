/**
 * 
 * @author Klemen
 * 
 * Basic variation of PID controller. Supports P, I and D.
 * 
 */

package com.muhavision.pid;

public class PID {
	
	float p,i,d;
	float err;
	long dt = -1;
	float prevErr;
	float integration;
	int direction = 1;
	
	public PID(int p, int i, int d, int direction) {
		this.p = p;
		this.i = i;
		this.d = d;
		this.direction = direction;
	}
	
	public float calculate(float input, float setpoint){
		
		if(dt!=-1){
			
			dt = System.currentTimeMillis() - dt;
		
			float err = setpoint - input;
			float diff = (err - prevErr)/dt;
			integration += err*dt;
		
			return (err*p + integration*i + diff*d)*direction;
		
		}else{
			dt = System.currentTimeMillis();
			return 0;
		}

	}
	
	public void resetPID(){
		prevErr = 0;
		integration = 0;
		dt = -1;
	}
	
	public static interface Direction {
		public static int NORMAL = 1;
		public static int REVERSED = -1;
	}

}
