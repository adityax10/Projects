
IplImage* ThresholdedImage(IplImage* imgHSV){               
	IplImage* imgThresh=cvCreateImage(cvGetSize(imgHSV),IPL_DEPTH_8U, 1);       
	cvInRangeS(imgHSV, cvScalar(160,160,160), cvScalar(180,256,256), imgThresh);        
	return imgThresh; 
} 

/*
void IMAGE::detect_object () {
	VideoCapture cap(0);

	if (!cap.isOpened()) {
		cout << "camera is not working properly.." << endl;
		return ;
	}

	 cvNamedWindow("Video");
	 cvNamedWindow("Object");
	 while(true) {
		Mat frame ;
		bool success = cap.read(frame);
		if(success == false) {
			break;
		} 
//		frame = frame.clone();				//  ??
		GaussianBlur(frame,frame,Size(3,3),0,0);
		Mat frame_copy(frame);
		cvtColor(frame,frame_copy,CV_BGR2HSV,1);
		cvtColor(frame,frame,CV_HSV2BGR);
		frame_copy = GetThresholdedImage(frame_copy);							// ??
		GaussianBlur(frame_copy,frame_copy,Size(3,3),0,0);
		imshow("Video",frame);
		imshow("Object",frame_copy);
		if (waitKey(30)==27) {
			break;
		}
	 }
}
*/
void IMAGE::detect_object() {
	CvCapture* capture =0;       
    capture = cvCaptureFromCAM(0);      
	if(!capture){       
		printf("Capture failure\n");            
		return ;      
	}            
	IplImage* frame = 0 ;
	cvNamedWindow("Video");            
	cvNamedWindow("Ball");
      //iterate through each frames of the video            
	while(true){
            frame = cvQueryFrame(capture);                        
			if(!frame) break;
            frame=cvCloneImage(frame);             
			cvSmooth(frame, frame, CV_GAUSSIAN,3,3); //smooth the original image using Gaussian kernel
            IplImage* imgHSV = cvCreateImage(cvGetSize(frame), IPL_DEPTH_8U, 3);             
			cvCvtColor(frame, imgHSV, CV_BGR2HSV); //Change the color format from BGR to HSV            
			IplImage* imgThresh = ThresholdedImage(imgHSV);                      
			cvSmooth(imgThresh, imgThresh, CV_GAUSSIAN,3,3); //smooth the binary image using Gaussian kernel                        
			cvShowImage("Ball", imgThresh);                        
			cvShowImage("Video", frame);
			


			//Clean up used images            
			cvReleaseImage(&imgHSV);            
			cvReleaseImage(&imgThresh);                        
			cvReleaseImage(&frame);
            //Wait 10mS            
			int c = cvWaitKey(10);            //If 'ESC' is pressed, break the loop            
			if((char)c==27 ) break;      
	}
}