import processing.core.PApplet;
import processing.core.PFont;
import mpe.client.UDPClient;

public class points {
	simpleClick parent;
	UDPClient client;
	PFont font;
	String str;
	float x, y, x2, y2;
	float rx, rx2, lastPos, fadeSpeed = 2.5f, eSize;
	int time,bright = 0;

	points(simpleClick p, UDPClient _c, String _str, int _time, float _x, float _y, float _x2, float _y2, float _eSize){
		parent = p;
		client = _c;
		str = _str;// headline text
		time = _time;// timer set
		//line position
		x = _x;
		y = _y;
		x2 = _x2;
		y2 = _y2;
		eSize = _eSize;//circle size
		// put position of start and end
		rx = _x;
		rx2 = _x2;
		lastPos = _x2;
	}
	
	void update(){
		//fill black square and make line fade
		parent.stroke(255);
		parent.line(x, y, x2, y2);
		parent.fill(0);
		parent.noStroke();
		parent.rect(rx,0,rx2,768);
		//
		time--;//counting time
		//when timer is end, the program start drawing  
		if(time < 5){
			//calculating the position of fade position
			rx = rx + fadeSpeed;
			rx2 = rx2 - fadeSpeed;
			if(rx > lastPos){
				rx = 0;
				rx2 = 0;
				fadeSpeed = 0;
			}
			//for circle fade in
			bright = bright + 5;
			if(bright > 255){
				bright = 255;
			}
			//draw circle
			parent.fill(bright);
			parent.noStroke();
			parent.ellipse(x,y,eSize,eSize);
		}
		
		//for showing text
		float textW = parent.textWidth(str);// make the text center
		parent.text(str, x - textW/2, y + eSize+10);
	}
}
