package com.muhavision.control;

import java.util.List;

import com.codeminders.ardrone.NavData;
import com.codeminders.ardrone.Point;
import com.codeminders.ardrone.VisionTag;

//A class that should follow the marker but it doesn't.

public class MarkerControl {

	public static EulerAngles getControlDataAndPictureDataBasedOnNavData(
			NavData nav) {

		if(nav!=null)
			if (nav.isVisionEnabled()) {
				List<VisionTag> tags = nav.getVisionTags();
				if(tags!=null)
					if (!tags.isEmpty()) {
						VisionTag tag = tags.get(0);
						if (tag != null) {
							EulerAngles angles = new EulerAngles();
							Point point = tag.getPosition();
							angles.picX = point.getX();
							angles.picY = point.getY();
							angles.dist = tag.getDistance();
							angles.hasAngles = true;
							return angles;
						}
					}
			}

		return new EulerAngles();
	}

}
