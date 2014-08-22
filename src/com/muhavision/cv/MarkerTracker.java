package com.muhavision.cv;

import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_highgui.*;

import java.awt.GridLayout;
import java.awt.image.BufferedImage;

import org.bytedeco.javacpp.helper.opencv_core.AbstractIplImage;
import org.bytedeco.javacpp.opencv_core.IplImage;

import com.muhavision.control.EulerAngles;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_video.*;

import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.bytedeco.javacpp.*;
import org.bytedeco.javacpp.opencv_core.CvPoint2D32f;
import org.bytedeco.javacpp.opencv_core.CvSize;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.helper.opencv_core.*;
import org.bytedeco.javacpp.helper.*;
import org.bytedeco.javacv.*;

public class MarkerTracker {

	//red
	//H:3.0 S:209.0 V:203.0 X:0.0
	//H:8.0 S:211.0 V:174.0 X:0.0
	//H:0.0 S:182.0 V:80.0 X:0.0
	//H:2.0 S:181.0 V:203.0 X:0.0
	//H:2.0 S:234.0 V:196.0 X:0.0
	//H:4.0 S:232.0 V:181.0 X:0.0
	//H:8.0 S:244.0 V:181.0 X:0.0
	
	//green
	//H:62.0 S:179.0 V:124.0 X:0.0
	//H:63.0 S:167.0 V:127.0 X:0.0
	//H:63.0 S:139.0 V:145.0 X:0.0
	//H:67.0 S:204.0 V:80.0 X:0.0
	//H:70.0 S:202.0 V:77.0 X:0.0
	//H:63.0 S:166.0 V:106.0 X:0.0
	//H:57.0 S:180.0 V:102.0 X:0.0
	//H:71.0 S:183.0 V:131.0 X:0.0
	//H:72.0 S:115.0 V:120.0 X:0.0
	
	static CanvasFrame canvas = new CanvasFrame("Quad Cam Live");
	
	static int aa, bb, cc, dd, hh, ii;
	
	static CvScalar green_min = cvScalar(156, 120, 27, 0);
	static CvScalar green_max = cvScalar(180, 255, 255, 0);
	//static CvScalar green_min = cvScalar(aa,bb,cc, 0);
	//static CvScalar green_max = cvScalar(dd,hh,ii, 0);
				
	static CvScalar red_min   = cvScalar(75,107,86, 0);
	static CvScalar red_max =   cvScalar(138,255,255, 0);
	
	static JSlider sl11 = new JSlider(0, 180, 0);
	static JSlider sl21 = new JSlider(0, 255, 0);
	static JSlider sl31 = new JSlider(0, 255, 0);
	static JSlider sl12 = new JSlider(0, 180, 0);
	static JSlider sl22 = new JSlider(0, 255, 0);
	static JSlider sl32 = new JSlider(0, 255, 0);
	
	public static void load(){
		JFrame d = new JFrame();
		d.setLayout(new GridLayout(6,2));
		final JLabel l11 = new JLabel("");
		final JLabel l21 = new JLabel("");
		final JLabel l31 = new JLabel("");
		final JLabel l12 = new JLabel("");
		final JLabel l22 = new JLabel("");
		final JLabel l32 = new JLabel("");
		
		d.add(sl11);
		d.add(l11);
		d.add(sl21);
		d.add(l21);
		d.add(sl31);
		d.add(l31);
		
		d.add(sl12);
		d.add(l12);
		d.add(sl22);
		d.add(l22);
		d.add(sl32);
		d.add(l32);
		
		d.setSize(400, 400);
		d.setVisible(true);
		/*sl11.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				l11.setText("H min: "+sl11.getValue());
				aa = sl11.getValue();
				red_min = cvScalar(aa,bb,cc, 0);
				red_max = cvScalar(dd,hh,ii, 0);
			}
		});
		sl21.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				l21.setText("S min: "+sl21.getValue());
				bb = sl21.getValue();
				red_min = cvScalar(aa,bb,cc, 0);
				red_max = cvScalar(dd,hh,ii, 0);
			}
		});
		sl31.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				l31.setText("V min: "+sl31.getValue());
				cc = sl31.getValue();
				red_min = cvScalar(aa,bb,cc, 0);
				red_max = cvScalar(dd,hh,ii, 0);
			}
		});
		sl12.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				l12.setText("H max: "+sl12.getValue());
				dd = sl12.getValue();
				red_min = cvScalar(aa,bb,cc, 0);
				red_max = cvScalar(dd,hh,ii, 0);
			}
		});
		sl22.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				l22.setText("S max: "+sl22.getValue());
				hh = sl22.getValue();
				red_min = cvScalar(aa,bb,cc, 0);
				red_max = cvScalar(dd,hh,ii, 0);
			}
		});
		sl32.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				l32.setText("V max: "+sl32.getValue());
				ii = sl32.getValue();
				red_min = cvScalar(aa,bb,cc, 0);
				red_max = cvScalar(dd,hh,ii, 0);
			}
		});*/
	}
	
	public static EulerAngles getMarkerData(BufferedImage bufferedimg){
		
		if(bufferedimg!=null){
		
			IplImage sourceRGB = AbstractIplImage.createFrom(bufferedimg);
		
			IplImage source = cvCreateImage(cvGetSize(sourceRGB), 8, 3);
			
			cvCvtColor(sourceRGB, source, CV_BGR2HSV);
			
			IplImage thrs_green = hsvThreshold(source, green_min, green_max);
			IplImage thrs_red = hsvThreshold(source, red_min, red_max);
			
			cvErode(thrs_green, thrs_green, null, 1);
			cvErode(thrs_red, thrs_red, null, 2);
			
			canvas.showImage(thrs_red);
					
			CvMoments moments_green = new CvMoments();
			CvMoments moments_red = new CvMoments();
		
			cvMoments(thrs_green, moments_green, 1);
        	cvMoments(thrs_red, moments_red, 1);
        
        	double mom10_green = cvGetSpatialMoment(moments_green, 1, 0);
        	double mom01_green = cvGetSpatialMoment(moments_green, 0, 1);
        	double area_green = cvGetCentralMoment(moments_green, 0, 0);
        	int posX_green = (int) (mom10_green / area_green);
        	int posY_green = (int) (mom01_green / area_green);
        
        	double mom10_red = cvGetSpatialMoment(moments_red, 1, 0);
        	double mom01_red = cvGetSpatialMoment(moments_red, 0, 1);
        	double area_red = cvGetCentralMoment(moments_red, 0, 0);
        	int posX_red = (int) (mom10_red / area_red);
        	int posY_red = (int) (mom01_red / area_red);
						
			/*ObjectFinder.Settings sett = new ObjectFinder.Settings();
			
			//sett.setThresholdWindowMax(150);
			//sett.setThresholdKBlackMarkers(0.8);
			
			ObjectFinder detector = new ObjectFinder(sett);
			
			IplImage current_color = AbstractIplImage.createFrom(bufferedimg);

			IplImage current = AbstractIplImage.create(current_color.width(),
					current_color.height(), IPL_DEPTH_8U, 1);

			cvCvtColor(current_color, current, CV_BGR2GRAY);

			CvSize velikost = cvGetSize(current);

			IplImage eig_image = cvCreateImage(velikost, IPL_DEPTH_32F, 1);
			IplImage tmp_image = cvCreateImage(velikost, IPL_DEPTH_32F, 1);
			
			IplImage image = cvLoadImage("marker.png",CV_LOAD_IMAGE_GRAYSCALE);
			
			sett.setObjectImage(image);
			sett.setUseFLANN(true);
			
			double[] dst_corners = detector.find(current);
			
			for (int i = 0; i < dst_corners.length; i++) {
				System.out.print(dst_corners+" : ");
			}
			System.out.println("-");
			
			//Marker[] markers = detector.detect(sourceRGB, false);
			
			//canvas.showImage(detector.getThresholdedImage());
			
			//for (int i = 0; i < markers.length; i++) {
			//	System.out.println("markerck");
			//}*/
        	
        	EulerAngles angle = new EulerAngles();
        	
        	angle.picX = posX_green;
        	angle.picY = posY_green;
        	
        	angle.picX2 = posX_red;
        	angle.picY2 = posY_red;
        	
        	return angle;
        
		}
		
		return new EulerAngles();
	}
	
	static IplImage hsvThreshold(IplImage orgImg, CvScalar min, CvScalar max) {
        //IplImage imgHSV = cvCreateImage(cvGetSize(orgImg), 8, 3);
        
        CvScalar s=cvGet2D(orgImg,120,160);               
        System.out.println( "H:"+ s.val(0) + " S:" + s.val(1) + " V:" + s.val(2) + " X:" + s.val(3));//Print values
        
        IplImage imgThreshold = cvCreateImage(cvGetSize(orgImg), 8, 1);
        cvInRangeS(orgImg, min, max, imgThreshold);
        //cvReleaseImage(orgImg);
        //cvSmooth(imgThreshold, imgThreshold);
        return imgThreshold;
    }
	
	//hsv h deliš z dve, s in v pomnožiš z 255
	
		//green
		//85.0 S:149.0 V:192.0 X:0.0
		//H:82.0 S:146.0 V:206.0 X:0.0
		//H:83.0 S:101.0 V:192.0 X:0.0
		//H:83.0 S:156.0 V:188.0 X:0.0
		//H:83.0 S:160.0 V:178.0 X:0.0
		//H:85.0 S:133.0 V:167.0 X:0.0
		//
		//H:82.0 S:111.0 V:206.0 X:0.0
		//H:83.0 S:136.0 V:142.0 X:0.0
		//H:76.0 S:106.0 V:199.0 X:0.0
		
		//red
		//H:0.0 S:128.0 V:160.0 X:0.0
		//H:2.0 S:172.0 V:203.0 X:0.0
		//H:2.0 S:172.0 V:203.0 X:0.0
		//H:0.0 S:178.0 V:196.0 X:0.0
		//H:2.0 S:172.0 V:203.0 X:0.0
		//H:179.0 S:190.0 V:203.0 X:0.0
		//
		//H:167.0 S:127.0 V:203.0 X:0.0
		//H:175.0 S:118.0 V:203.0 X:0.0
		//H:167.0 S:127.0 V:203.0 X:0.0
		
		//static CvScalar green_min = cvScalar(76, 105, 120, 0);
		//static CvScalar green_max = cvScalar(87, 183, 216, 0);
			
		//static CvScalar red_min = cvScalar(0, 128, 160, 0);
		//static CvScalar red_max = cvScalar(2, 190, 203, 0);
	
}
