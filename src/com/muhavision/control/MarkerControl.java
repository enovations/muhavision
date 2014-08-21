package com.muhavision.control;

import java.util.ArrayList;
import java.util.List;

import com.codeminders.ardrone.NavData;
import com.codeminders.ardrone.Point;
import com.codeminders.ardrone.VisionTag;

public class MarkerControl {
	
	public static EulerAngles getControlDataAndPictureDataBasedOnNavData(NavData nav){
		
		if(nav.isVisionEnabled()){
			List<VisionTag> tags = (ArrayList<VisionTag>) nav.getVisionTags();
			for (VisionTag tag : tags) {
				EulerAngles angles = new EulerAngles();
				Point point = tag.getPosition();
				angles.picX = point.getX();
				angles.picY = point.getY();
				angles.dist = tag.getDistance();
				angles.hasAngles = true;
				return angles;
			}
		}
		
		return new EulerAngles();
	}

}
