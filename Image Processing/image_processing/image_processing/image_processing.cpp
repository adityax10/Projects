// image_processing.cpp : Defines the entry point for the console application.
//

#include "stdafx.h"
#include "main_header.h"
#include "user_defined_headers.h"

using namespace std;
using namespace cv;

string name = "MyPic.jpg";

int _tmain(int argc, _TCHAR* argv[]) {
	
	cout << "Hi ! Welcome.. " << endl;
	cout << "Select Interface :" << endl;
	cout << "Press...\n" ;
	cout << 1 << " for Keyboard \n";
	cout << 2 <<  " for Mouse \n" ;
	cout << 3 << " for Keyboard and Mouse both \n" ;
	cin >>  gui ;

	IMAGE image(name);
//	image.show_image();
	image.track_object () ;
	image.add_track_bar();
//	image.rotate_image();
//	image.mouse_events();
//	image.show_image() ;
//	SetCursorPos(1366,768);
	
	return 0;
}