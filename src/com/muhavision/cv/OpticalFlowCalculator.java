package com.muhavision.cv;

import java.awt.image.BufferedImage;
import java.util.Vector;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.opencv_core.IplImage;

import com.codeminders.ardrone.Point;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_video.*;
import static org.bytedeco.javacpp.opencv_highgui.*;

public class OpticalFlowCalculator {
	
	BufferedImage prev = null;
	
	private static final int MAX_CORNERS = 190;
	
	public BufferedImage Equalize(BufferedImage bufferedimg)
    {
        IplImage iploriginal = IplImage.createFrom(bufferedimg);

        IplImage srcimg = IplImage.create(iploriginal.width(), iploriginal.height(), IPL_DEPTH_8U, 1);
        //IplImage destimg = IplImage.create(iploriginal.width(), iploriginal.height(), IPL_DEPTH_8U, 1);

        cvCvtColor(iploriginal, srcimg, CV_BGR2GRAY);

        //cvEqualizeHist( srcimg, destimg );

        BufferedImage eqimg = srcimg.getBufferedImage();

        return eqimg;
    }
	
	public QuadrantFlowSpeed getFlowData(BufferedImage curr){
		
		//first time
		if(prev==null){
			prev=curr;
			return new QuadrantFlowSpeed();
		}
		
		IplImage current_color  = IplImage.createFrom(curr);
		IplImage previous_color = IplImage.createFrom(prev);
				
		IplImage current = IplImage.create(current_color.width(), current_color.height(), IPL_DEPTH_8U, 1);
		IplImage previous = IplImage.create(previous_color.width(), previous_color.height(), IPL_DEPTH_8U, 1);
		
		cvCvtColor(current_color, current, CV_BGR2GRAY);
		cvCvtColor(previous_color, previous, CV_BGR2GRAY);
		
		CvSize velikost = cvGetSize(current);
		
		IplImage eig_image = cvCreateImage(velikost, IPL_DEPTH_32F, 1);
        IplImage tmp_image = cvCreateImage(velikost, IPL_DEPTH_32F, 1);
        
        IntPointer corner_count = new IntPointer(1).put(MAX_CORNERS);
        CvPoint2D32f cornersA = new CvPoint2D32f(MAX_CORNERS);

        CvArr mask = null;
        cvGoodFeaturesToTrack(current, eig_image, tmp_image, cornersA,
                corner_count, 0.01, 5.0, mask, 3, 0, 0.04);
        
        int win_size = 5;
        
        cvFindCornerSubPix(current, cornersA, corner_count.get(),
                cvSize(win_size, win_size), cvSize(-1, -1),
                cvTermCriteria(CV_TERMCRIT_ITER | CV_TERMCRIT_EPS, 20, 0.03));
        
        //Lucas Kande filter
        
        BytePointer features_found = new BytePointer(MAX_CORNERS);
        FloatPointer feature_errors = new FloatPointer(MAX_CORNERS);
        
        CvSize pyr_sz = cvSize(current.width() + 8, previous.height() / 3);

        IplImage pyrA = cvCreateImage(pyr_sz, IPL_DEPTH_32F, 1);
        IplImage pyrB = cvCreateImage(pyr_sz, IPL_DEPTH_32F, 1);

        CvPoint2D32f cornersB = new CvPoint2D32f(MAX_CORNERS);
        cvCalcOpticalFlowPyrLK(current, previous, pyrA, pyrB, cornersA, cornersB,
                corner_count.get(), cvSize(win_size, win_size), 5,
                features_found, feature_errors,
                cvTermCriteria(CV_TERMCRIT_ITER | CV_TERMCRIT_EPS, 20, 0.3), 0);
        
        Vector<VectorData> left = new Vector<VectorData>();
    	Vector<VectorData> right = new Vector<VectorData>();
        
    	float avgsum_left = 0;
    	float left_number = 0;
    	float avgsum_right = 0;
    	float right_number = 0;
    	
        for (int i = 0; i < corner_count.get(); i++) {
            if (features_found.get(i) == 0 || feature_errors.get(i) > 550) {
                continue;
            }
            cornersA.position(i);
            cornersB.position(i);
            int ax = (int) cornersA.x();
            int ay = (int) cornersA.y();
            int bx = (int) cornersB.x();
            int by = (int) cornersB.y();
            if(ax<100){
            	left.add(new VectorData(ax, ay, bx, by));
            	int dx = Math.abs(ax-bx);
            	int dy = Math.abs(ay-by);
            	float length = (float) Math.sqrt(((dx*dx) + (dy*dy)));
            	avgsum_left += length;
            	left_number++;
            }else if(ax>220){
            	right.add(new VectorData(ax, ay, bx, by));
            	int dx = Math.abs(ax-bx);
            	int dy = Math.abs(ay-by);
            	float length = (float) Math.sqrt(((dx*dx) + (dy*dy)));
            	avgsum_right += length;
            	right_number++;
            }
        }
        
        float avgl = avgsum_left/left_number;
        float avgr = avgsum_right/right_number;
        
        int avglx1_sum = 0;
        int avgly1_sum = 0;
        int avglx2_sum = 0;
        int avgly2_sum = 0;
        
        int vecltime = 0;
        
        if(left_number>0)
        	for(VectorData point : left){
        		int dx = Math.abs(point.x1 - point.x2);
        		int dy = Math.abs(point.y1 - point.y2);
        		float length = (float) Math.sqrt(((dx*dx) + (dy*dy)));
        		if((length/avgl)<5){
        			avglx1_sum += point.x1;
        			avglx2_sum += point.x2;
        			avgly1_sum += point.y1;
        			avgly2_sum += point.y2;
        			vecltime++;
        		}
        	}
        
        int avgl_x1 = avglx1_sum / vecltime;
        int avgl_x2 = avglx2_sum / vecltime;
        int avgl_y1 = avgly1_sum / vecltime;
        int avgl_y2 = avgly2_sum / vecltime;
        
        int vec_l_x = avgl_x1 - avgl_x2;
        int vec_l_y = avgl_y1 - avgl_y2;
		
        QuadrantFlowSpeed speed = new QuadrantFlowSpeed();
        
        speed.x = vec_l_x;
        speed.y = vec_l_y;
        
        //speed.tmp1 = tmp1;
        //speed.tmp2 = tmp2;
        
        prev=curr;
        
		return speed;
	}

}
