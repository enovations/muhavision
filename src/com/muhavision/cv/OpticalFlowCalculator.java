package com.muhavision.cv;

import java.awt.image.BufferedImage;

import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.opencv_core.IplImage;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_video.*;
import static org.bytedeco.javacpp.opencv_highgui.*;

public class OpticalFlowCalculator {
	
	BufferedImage prev = null;
	
	private static final int MAX_CORNERS = 500;
	
	public QuadrantFlowSpeed getFlowData(BufferedImage curr){
		
		//first time
		if(prev==null){
			prev=curr;
			return new QuadrantFlowSpeed();
		}
		
		IplImage current  = IplImage.createFrom(curr);
		IplImage previous = IplImage.createFrom(prev);
		
		CvSize velikost = cvGetSize(current);
		
		IplImage eig_image = cvCreateImage(velikost, IPL_DEPTH_32F, 1);
        IplImage tmp_image = cvCreateImage(velikost, IPL_DEPTH_32F, 1);
        
        IntPointer corner_count = new IntPointer(1).put(MAX_CORNERS);
        CvPoint2D32f cornersA = new CvPoint2D32f(MAX_CORNERS);

        CvArr mask = null;
        cvGoodFeaturesToTrack(current, eig_image, tmp_image, cornersA,
                corner_count, 0.05, 5.0, mask, 3, 0, 0.04);
        
        int win_size = 5;
        
        cvFindCornerSubPix(current, cornersA, corner_count.get(),
                cvSize(win_size, win_size), cvSize(-1, -1),
                cvTermCriteria(CV_TERMCRIT_ITER | CV_TERMCRIT_EPS, 20, 0.03));
        
        BytePointer features_found = new BytePointer(MAX_CORNERS);
        FloatPointer feature_errors = new FloatPointer(MAX_CORNERS);
		
		return new QuadrantFlowSpeed();
	}

}
