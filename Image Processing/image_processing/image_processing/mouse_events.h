

void CallBackFunc(int events,int x,int y,int flags , void *userData) {

	if (flags == (EVENT_FLAG_CTRLKEY + EVENT_FLAG_LBUTTON))				cout << "control key plus left button clicked (" << x << "," << y << ")" << endl;
	else if (flags == (EVENT_FLAG_RBUTTON + EVENT_FLAG_SHIFTKEY))		cout << "shift key plus right button clicked (" << x << "," << y << ")" << endl;
	else if (events == EVENT_MOUSEMOVE && flags == EVENT_FLAG_ALTKEY)	cout << "alt key plus mouse move (" << x << "," << y << ")" << endl;
	else if (events == EVENT_LBUTTONDOWN)								cout << "left button down (" << x << "," << y << ")" << endl;
	else if (events == EVENT_RBUTTONDOWN)								cout << "right button down (" << x << "," << y << ")" << endl;
	else if (events == EVENT_MBUTTONDOWN)								cout << "middle button down (" << x << "," << y << ")" << endl;
	else if (events == EVENT_LBUTTONUP)									cout << "left button up (" << x << "," << y << ")" << endl;
	else if (events == EVENT_RBUTTONUP)									cout << "right button up(" << x << "," << y << ")" << endl;
	else if (events == EVENT_MBUTTONUP)									cout << "middle button up (" << x << "," << y << ")" << endl;
	else if (events == EVENT_LBUTTONDBLCLK)								cout << "left button clicked (" << x << "," << y << ")" << endl;
	else if (events == EVENT_RBUTTONDBLCLK)								cout << "right button clicked (" << x << "," << y << ")" << endl;
	else if (events == EVENT_MBUTTONDBLCLK)								cout << "middle button clicked (" << x << "," << y << ")" << endl;
}

void IMAGE::mouse_events () {
	
	setMouseCallback(window,CallBackFunc,NULL) ;
}