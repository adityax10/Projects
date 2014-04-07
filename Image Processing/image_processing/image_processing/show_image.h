void IMAGE::show_image () {

	if(check_image()) {
		namedWindow ("Image",CV_WINDOW_AUTOSIZE);
		imshow ("Image",this->img);
		waitKey(0);
	}
}