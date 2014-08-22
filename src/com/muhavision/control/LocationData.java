package com.muhavision.control;

//Again: JAVA WHY YOU NOT HAVE A STRUCT

public class LocationData {

	public float roll, pitch, yaw;

	public int picX, picY;
	public int picX2, picY2;

	public float dist;

	public boolean hasAngles = false;

	public int x1, x2, y1, y2;

	public int rx, ry, lx, ly;

	public LocationData() {
	}

	public LocationData(int x1, int y1, int x2, int y2) {
		this.x1 = x1;
		this.x2 = x2;
		this.y1 = y1;
		this.y2 = y2;
	}

}
