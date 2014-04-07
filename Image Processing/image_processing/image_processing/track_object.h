#define CRITICAL_DISPLACEMENT 100
#define RC 1
#define LC 2
#define UC 3
#define DC 4
#define CLICKED 8
#define KEYBOARD 1
#define MOUSE 2
#define KEYMOUSE 3

int clicked = 0 ,click_count = 0 ,last_click_count = 0 , hold_count = 0 ;
int screenX = 1366;
int lastx=0 , lasty=0 ,x,y;
int cnt  = 0 , leftcnt = 0;
int rightx = 0, righty = 0 ;
int ht = 320 , wt = 240 ;
// this is to get the info about the screen 
int screenx = GetSystemMetrics(SM_CXSCREEN);
int screeny = GetSystemMetrics(SM_CYSCREEN);
bool hold = false ;

void checkGesture(int curx,int cury)
{
	cout << lastx <<" " << curx << " ";
	cout << lasty << " " << cury << endl;

	if ((!lastx && !lasty) || hold ) {
		lastx = curx  , lasty = cury ;
		return ;
	}

	if( lastx - curx > CRITICAL_DISPLACEMENT && clicked != RC )
		// PRESS RIGHT ARROW
	{
		hold = true ;
		clicked = RC ;
		click_count  = 0;
		cout<<" MOVED RIGHT  " <<endl;
		keybd_event(VK_RIGHT,0,KEYEVENTF_EXTENDEDKEY|0,0); 
		keybd_event(VK_RIGHT,0,KEYEVENTF_EXTENDEDKEY|KEYEVENTF_KEYUP,0);
	}	

	else if ( lasty - cury > CRITICAL_DISPLACEMENT && clicked != UC)
	// PRESS UP KEY
	{	
		cout << "UP CLICKED \n " ;
		hold = true ;
		clicked = UC ;
		click_count  = 0;
		keybd_event(VK_UP,0,KEYEVENTF_EXTENDEDKEY|0,0); 
		keybd_event(VK_UP,0,KEYEVENTF_EXTENDEDKEY|KEYEVENTF_KEYUP,0);
	}

	else if (cury - lasty > CRITICAL_DISPLACEMENT && clicked != DC)
		// PRESS DOWN KEY
	{
	cout << "DOWN CLICKED \n" ;
		hold = true ;
		clicked = DC ;
		click_count  = 0;
		keybd_event(VK_DOWN,0,KEYEVENTF_EXTENDEDKEY|0,0); 
		keybd_event(VK_DOWN,0,KEYEVENTF_EXTENDEDKEY|KEYEVENTF_KEYUP,0);
	}

	else if (curx - lastx > CRITICAL_DISPLACEMENT && clicked != LC)
		// PRESS LEFT KEY
	{		
		cout  << "LEFT CLICKED \n" ;
		hold = true ;
		clicked = LC ;
		click_count  = 0;
		keybd_event(VK_LEFT,0,KEYEVENTF_EXTENDEDKEY|0,0); 
		keybd_event(VK_LEFT,0,KEYEVENTF_EXTENDEDKEY|KEYEVENTF_KEYUP,0);
	}
//	lastx = curx  , lasty = cury ;
}

int getX (int x) {
	return 1910- 3*x ;
}
int getY (int y) {
	return 2*y ;
}


void keyboard (int recX ,int recY) {
//	x = getX(recX) , y = getY (recY);
	checkGesture(recX,recY);
} 

void mouse_pointers (int recX,int recY){ 
	recX = getX(recX) , recY = getY(recY) ;
	
	if (gui == KEYMOUSE)
	if (abs(lastx-recX)>CRITICAL_DISPLACEMENT || abs(lasty-recY)>CRITICAL_DISPLACEMENT )
		keyboard(recX,recY) ;
//	return  ;

	if (abs(lastx-recX)<10 && abs(lasty-recY)<10)
			leftcnt ++;
	else
		leftcnt = 0;

	if ( ((abs(rightx-recX))>10&&(abs(righty-recY))<50) || ((abs(rightx-recX))<50&&(abs(righty-recY))>10) )
		cnt ++ ;
	else
		cnt = 0 ,rightx = recX , righty = recY;
	if (leftcnt>10) {
		mouse_event(MOUSEEVENTF_LEFTDOWN,0,0,0,0);
		mouse_event(MOUSEEVENTF_LEFTUP,0,0,0,0);
//		mouse_event(MOUSEEVENTF_LEFTDOWN,0,0,0,0);
//		mouse_event(MOUSEEVENTF_LEFTUP,0,0,0,0);
		leftcnt = 0 ;
	}
 		//check for click
	else if (cnt > 40) {
		mouse_event(MOUSEEVENTF_RIGHTDOWN,0,0,0,0);
		mouse_event(MOUSEEVENTF_RIGHTUP,0,0,0,0);
		cnt = 0;
		rightx = righty = 0 ;
	}
	//Resetting the previous coords
/*	if (abs(lastx-recX)<2||abs(lasty-recY)<2) {
		x = lastx;
		y = lasty;
	}
	else{
		x = recX;
		y = recY;
		lastx = recX ;
		lasty = recY ;
	}
*/
		x = lastx = recX ;
		y = lasty = recY ;
	SetCursorPos(x,y);
//	cout << recX << " " << recY << endl;
}

IplImage* imgTracking; 
int lastX = -1; 
int lastY = -1;

//This function threshold the HSV image and create a binary image 
IplImage* thresholdedImage(IplImage* imgHSV){           
	IplImage* imgThresh=cvCreateImage(cvGetSize(imgHSV),IPL_DEPTH_8U, 1);    
	cvInRangeS(imgHSV, cvScalar(0,160,160), cvScalar(22,256,256), imgThresh);     
	return imgThresh; 
}

void trackObject(IplImage* imgThresh){    
	// Calculate the moments of 'imgThresh'    
	CvMoments *moments = (CvMoments*)malloc(sizeof(CvMoments));    
	cvMoments(imgThresh, moments, 1);    
	double moment10 = cvGetSpatialMoment(moments, 1, 0);    
	double moment01 = cvGetSpatialMoment(moments, 0, 1);    
	double area = cvGetCentralMoment(moments, 0, 0);
     // if the area<1000, I consider that the there are no object in the image and it's because of the noise, the area is not zero     
	if(area>10){        
		// calculate the position of the ball        
		int posX = moment10/area;        
		int posY = moment01/area;                       
		if(lastX>=0 && lastY>=0 && posX>=0 && posY>=0)        {            
			// Draw a yellow line from the previous point to the current point            
			cvLine(imgTracking, cvPoint(posX, posY), cvPoint(lastX, lastY), cvScalar(0,0,255), 4);        
		}
//		int difX;
        lastX = posX;        
		lastY = posY;		
		if (gui == MOUSE || gui == KEYMOUSE) 
			mouse_pointers(lastX,lastY);
		else if (gui == KEYBOARD)
			keyboard(lastX,lastY);

//		keyboard(lastX,lastY);
	//	cout << lastX << " " << lastY << endl;
	}

    free(moments); 
}

void IMAGE::track_object() {
	CvCapture* capture =0;             
	capture = cvCaptureFromCAM(0);      
	if(!capture){         
		printf("Capture failure\n");         
		return ;      
	}            
	IplImage* frame=0;      
	frame = cvQueryFrame(capture);                 
	if(!frame) return ;        
	//create a blank image and assigned to 'imgTracking' which has the same size of original video     
	imgTracking=cvCreateImage(cvGetSize(frame),IPL_DEPTH_8U, 3);     
	cvZero(imgTracking); //covert the image, 'imgTracking' to black
//	cvNamedWindow("Video",WINDOW_AUTOSIZE );     
	cvNamedWindow("Ball");
      //iterate through each frames of the video           
	while(true){
            frame = cvQueryFrame(capture);                       
			if(!frame) break;            
			frame=cvCloneImage(frame);                         
			cvSmooth(frame, frame, CV_GAUSSIAN,3,3); //smooth the original image using Gaussian kernel
            IplImage* imgHSV = cvCreateImage(cvGetSize(frame), IPL_DEPTH_8U, 3);             
			cvCvtColor(frame, imgHSV, CV_BGR2HSV); //Change the color format from BGR to HSV            
			IplImage* imgThresh = thresholdedImage(imgHSV);                      
			cvSmooth(imgThresh, imgThresh, CV_GAUSSIAN,3,3); //smooth the binary image using Gaussian kernel                       
			//track the position of the object          
			trackObject(imgThresh);
            // Add the tracking image and the frame           
		//	cvAdd(frame, imgTracking, frame);
            cvShowImage("Ball", imgThresh);                      
//			cvShowImage("Video", frame);                      
			 //Wait 10mS            
			int c = cvWaitKey(10);            //If 'ESC' is pressed, break the loop         
			       //Clean up used images           
			cvReleaseImage(&imgHSV);           
			cvReleaseImage(&imgThresh);                       
			cvReleaseImage(&frame);
      
			click_count ++ ;
			if (click_count == CLICKED) {
				click_count = 0;
				clicked = 0 ;
			}

			if (hold == true)
				hold_count ++ ;
			else
				hold_count = 0 ;

			if (hold_count == 2*CLICKED ) {
				hold_count = 0 ;
				hold = false ;
			}
//			cout << c << endl;
//			cin.get() ;
			if((char)c==27 ) break;            
	}
}