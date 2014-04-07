
void MyCallbackForBrightness(int iValueForBrightness, void *userData) {     
		int iValueForContrast = *( static_cast<int*>(userData) );
		//Calculating brightness and contrast value     
		int iBrightness = iValueForBrightness - 50;     
		double dContrast = iValueForContrast / 50.0;
		//Calculated contrast and brightness value    
	  //adjust the brightness and contrast     
//		img.convertTo(img, -1, dContrast, iBrightness); 
	}

void MyCallbackForContrast(int iValueForContrast, void *userData) {    
		int iValueForBrightness = *( static_cast<int*>(userData) );
		int iBrightness = iValueForBrightness - 50;     
		double dContrast = iValueForContrast / 50.0;
//		image.filter_image(dContrast,iBrightness);
}


void IMAGE::add_track_bar () {
	
	if (check_image()) {
		 int Brightness = 50;    
		 int Contrast = 50;
		 Mat temp ;
		 //Create track bar to change brightness    
		 createTrackbar("Brightness", window, &Brightness, 100);
         //Create track bar to change contrast    
//		  createTrackbar("Contrast", window, &Contrast, 100, MyCallbackForContrast, &Brightness);  
		 createTrackbar("Contrast", window, &Contrast, 100);
		 while(true) {
			int b = Brightness -50 ;
			double c = Contrast /50.0 ;
			img.convertTo(temp,-1,c,b);
			imshow(window,temp);
			if (waitKey(30)==27) {
				return ;
			}
		 }
	}
}