package com.muhavision.control;

/**
 * Created by ziga on 21.8.2014.
 */
public class FlightMode {

	public enum eMode {

		NORMAL_MODE(0), MUHA_MODE(1), TAG_MODE(2);

		int mode = 0;

		eMode(int mode) {
			this.mode = mode;
		}

	}

	public static String getFlightModeName(eMode mode) {
		if (mode == eMode.NORMAL_MODE)
			return "NORMAL";
		if (mode == eMode.MUHA_MODE)
			return "MUHA";
		if (mode == eMode.TAG_MODE)
			return "TAG";
		return "UNKNOWN";
	}

	private eMode mode = eMode.NORMAL_MODE;

	public eMode getMode() {
		return mode;
	}

	public void setMode(eMode mode) {
		this.mode = mode;
		mode.toString();
	}

}
