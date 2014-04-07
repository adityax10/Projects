// main image class

#include "main_header.h"
using namespace cv;
using namespace std;
#define window "Image"
int gui;
class IMAGE {

	
	public:

		IMAGE () {}
		IMAGE (string image) {
			img = imread(image);
		}
		
		void show_image() ;
		void brightness (int val , bool inc = true);
		void contrast (double val );
		void equalise_grayscale () ;
		void equalise_color() ;
		void add_track_bar();
		void filter_image(double contrast,int brightness);
		void mouse_events () ;
		void rotate_image() ;
		void detect_object() ;
		void track_object (); 
		void mouse_pointers(int,int) ;
		int d_o();

	private :
		Mat img;

		bool check_image() {
			if (this->img.empty()) {
					cerr << "unable to read image" << endl;
					return false;
				}
				return true ;
			}

		Mat GetThresholdedImage (Mat frame) {
			Mat imgThresh(500, 1000, CV_8UC1, Scalar(0,0,0));
			inRange(frame, Scalar(160,200,200), Scalar(175,256,256), imgThresh);
			return imgThresh;
		}

};


void IMAGE::brightness (int val  , bool inc) {
	if (inc == false)	val *= -1;
	this->img  = this->img + Scalar(val,val,val); //increase the brightness by val units     
	//img.convertTo(img, -1, 1, val);
}

void IMAGE::contrast (double val) {
	 img.convertTo(img, -1, val, 0); //increase the contrast (val)
}

void IMAGE::equalise_grayscale() {
	 cvtColor(img, img, CV_BGR2GRAY); //change the color image to grayscale image
     equalizeHist(img, img); //equalize the histogram
}

void IMAGE::equalise_color () {

	vector<Mat> channels;
	cvtColor(img, img, CV_BGR2YCrCb); //change the color image from BGR to YCrCb format
	split(img,channels); //split the image into channels
	equalizeHist(channels[0], channels[0]); //equalize histogram on the 1st channel (Y)
	merge(channels,img); //merge 3 channels including the modified 1st channel into one image
	cvtColor(img, img, CV_YCrCb2BGR); //change the color image from YCrCb to BGR format (to display image properly)
}


void IMAGE::filter_image (double contrast,int brightness) {
	img.convertTo(img, -1, contrast, brightness); 
}