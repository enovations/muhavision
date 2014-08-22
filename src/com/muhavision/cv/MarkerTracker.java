package com.muhavision.cv;

import static org.bytedeco.javacpp.opencv_core.cvCreateImage;
import static org.bytedeco.javacpp.opencv_core.cvGetSize;
import static org.bytedeco.javacpp.opencv_core.cvInRangeS;
import static org.bytedeco.javacpp.opencv_core.cvScalar;
import static org.bytedeco.javacpp.opencv_imgproc.CV_BGR2HSV;
import static org.bytedeco.javacpp.opencv_imgproc.cvCvtColor;
import static org.bytedeco.javacpp.opencv_imgproc.cvErode;
import static org.bytedeco.javacpp.opencv_imgproc.cvGetCentralMoment;
import static org.bytedeco.javacpp.opencv_imgproc.cvGetSpatialMoment;
import static org.bytedeco.javacpp.opencv_imgproc.cvMoments;

import java.awt.GridLayout;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.bytedeco.javacpp.opencv_core.CvScalar;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_imgproc.CvMoments;
import org.bytedeco.javacpp.helper.opencv_core.AbstractIplImage;

import com.muhavision.control.LocationData;

public class MarkerTracker {

	// static CanvasFrame canvas = new CanvasFrame("Quad Cam Live");

	static int aa, bb, cc, dd, hh, ii;

	static CvScalar green_min = cvScalar(156, 120, 27, 0);
	static CvScalar green_max = cvScalar(180, 255, 255, 0);

	static CvScalar red_min = cvScalar(75, 107, 86, 0);
	static CvScalar red_max = cvScalar(138, 255, 255, 0);

	static JSlider sl11 = new JSlider(0, 180, 0);
	static JSlider sl21 = new JSlider(0, 255, 0);
	static JSlider sl31 = new JSlider(0, 255, 0);
	static JSlider sl12 = new JSlider(0, 180, 0);
	static JSlider sl22 = new JSlider(0, 255, 0);
	static JSlider sl32 = new JSlider(0, 255, 0);

	public static LocationData getMarkerData(BufferedImage bufferedimg) {

		if (bufferedimg != null) {

			IplImage sourceRGB = AbstractIplImage.createFrom(bufferedimg);

			IplImage source = cvCreateImage(cvGetSize(sourceRGB), 8, 3);

			cvCvtColor(sourceRGB, source, CV_BGR2HSV);

			IplImage thrs_green = hsvThreshold(source, green_min, green_max);
			IplImage thrs_red = hsvThreshold(source, red_min, red_max);

			cvErode(thrs_green, thrs_green, null, 1);
			cvErode(thrs_red, thrs_red, null, 2);

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

			LocationData angle = new LocationData();

			angle.picX = posX_green;
			angle.picY = posY_green;

			angle.picX2 = posX_red;
			angle.picY2 = posY_red;

			return angle;

		}

		return new LocationData();
	}

	static IplImage hsvThreshold(IplImage orgImg, CvScalar min, CvScalar max) {
		// CvScalar s=cvGet2D(orgImg,120,160);
		// System.out.println( "H:"+ s.val(0) + " S:" + s.val(1) + " V:" +
		// s.val(2) + " X:" + s.val(3));//Print values
		IplImage imgThreshold = cvCreateImage(cvGetSize(orgImg), 8, 1);
		cvInRangeS(orgImg, min, max, imgThreshold);
		// cvReleaseImage(orgImg);
		// cvSmooth(imgThreshold, imgThreshold);
		return imgThreshold;
	}

	public static void load() {
		JFrame d = new JFrame();
		d.setLayout(new GridLayout(6, 2));
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
		sl11.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				l11.setText("H min: " + sl11.getValue());
				aa = sl11.getValue();
				red_min = cvScalar(aa, bb, cc, 0);
				red_max = cvScalar(dd, hh, ii, 0);
			}
		});
		sl21.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				l21.setText("S min: " + sl21.getValue());
				bb = sl21.getValue();
				red_min = cvScalar(aa, bb, cc, 0);
				red_max = cvScalar(dd, hh, ii, 0);
			}
		});
		sl31.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				l31.setText("V min: " + sl31.getValue());
				cc = sl31.getValue();
				red_min = cvScalar(aa, bb, cc, 0);
				red_max = cvScalar(dd, hh, ii, 0);
			}
		});
		sl12.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				l12.setText("H max: " + sl12.getValue());
				dd = sl12.getValue();
				red_min = cvScalar(aa, bb, cc, 0);
				red_max = cvScalar(dd, hh, ii, 0);
			}
		});
		sl22.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				l22.setText("S max: " + sl22.getValue());
				hh = sl22.getValue();
				red_min = cvScalar(aa, bb, cc, 0);
				red_max = cvScalar(dd, hh, ii, 0);
			}
		});
		sl32.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				l32.setText("V max: " + sl32.getValue());
				ii = sl32.getValue();
				red_min = cvScalar(aa, bb, cc, 0);
				red_max = cvScalar(dd, hh, ii, 0);
			}
		});
	}

}
