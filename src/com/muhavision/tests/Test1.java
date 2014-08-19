package com.muhavision.tests;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_highgui.*;

import org.bytedeco.javacv.CanvasFrame;

public class Test1 {

	
	public static void main(String[] args) {
        
	     //Load image img1 as IplImage
	        final IplImage image = cvLoadImage("img.png");
	         
	        //create canvas frame named 'Demo'
	        final CanvasFrame canvas = new CanvasFrame("Demo");
	         
	        //Show image in canvas frame
	        canvas.showImage(image);
	         
	        //This will close canvas frame on exit
	        canvas.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);        
	 }   
	
}