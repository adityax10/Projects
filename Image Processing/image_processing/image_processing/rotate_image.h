void IMAGE::rotate_image() {
 int iAngle = 180;     
 createTrackbar("Angle", window, &iAngle, 360);
 int iImageHieght = img.rows / 2;     
 int iImageWidth = img.cols / 2;
 while (true)     {     
	 Mat matRotation = getRotationMatrix2D( Point(iImageWidth, iImageHieght), (iAngle - 180), 1 ); 
      // Rotate the image          
	 Mat imgRotated;          
	 warpAffine( img, imgRotated, matRotation, img.size() );
     imshow( window, imgRotated );
     int iRet = waitKey(30);          
	 if ( iRet == 27 )          {     
		 break;          
	 }     
 }

}