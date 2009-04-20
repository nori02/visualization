import mpe.client.UDPClient;
import processing.core.PApplet;
import processing.core.PFont;

public class Brownian {
	UDPClient client;
	PApplet parent;
	String str;
	float vx,vy;
	float x,y;
	float alpha;
	float r,g,b;
	int time, restTime, time2 = 100;
	float Tsize = 1, TsizeChange = 1;
	float yDesPos = 960/2, newYPos, newXPos;
	boolean brFlg = true, fadeFlg = false;
	//
	Brownian(PApplet p, UDPClient _c, String _str ,int _time, int _rest, float _x, float _y, float _alpha, float _newXPos, float _newYPos){
		parent = p;
		client = _c;
		time = _time;
		restTime = _rest;
		alpha = _alpha;
		str = _str;
		r = 255;
		g = 255;
		b = 255;
		x = _x;
		y = _y;
		newXPos = _newXPos;
		newYPos = _newYPos;
		
	}
	//
	void update(float dx, float dy){
		parent.pushMatrix();
		//transit timer
		time--;
		//show and animation text
		if(time < 5){
			time = 0;
			time2--;
			alpha = 255;
			TsizeChange = 3.5f;
			y = y + ( yDesPos - y )/5; // position to center
			brFlg = false;
		}
		//reset text
		if(time2 < 1){
			time2 = 100;
			time = restTime;
			alpha = 70;
			TsizeChange = 1;
			//put new position
		//	newYPos = parent.random(0, parent.height-50);
		//	newXPos = parent.random(0, parent.width);
			fadeFlg = true;
		}
		
		//go to new position
		if(fadeFlg == true){
			y = y + (newYPos - y)/5;
			x = x + (newXPos - x)/5;
			if( y == newYPos && x == newXPos){
				fadeFlg = false;
				brFlg = true;// start brownian motion again
			}
		}
		
		parent.translate(x, y);
		Tsize = Tsize + (TsizeChange - Tsize)/5; // easing size
		parent.scale(Tsize);
		parent.fill(r,g,b,alpha);
		parent.text(str, -170, -90, 240, 180);
	//	parent.text(str, -90, -75, 18 0, 150);
	//	parent.text(str, -60, -45, 120, 90);
		//Brownian motion 
		if(brFlg == true){
			x += dx;
			y += dy;
		}
		parent.popMatrix();
	//
	}
}
