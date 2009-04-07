import mpe.client.UDPClient;
import processing.core.PApplet;
import processing.core.PFont;

public class Brownian {
	PApplet parent;
	UDPClient client;
	PFont font;
	String str;
	float vx,vy;
	float x,y;
	float friction;
	float rd;
	//
	Brownian(PApplet p, UDPClient _c, String _str ,float _x, float _y, float _rd, float _friction, PFont _font){
		parent = p;
		client = _c;
		str = _str;
		font = _font;
		rd = _rd;
		friction = _friction;
		x = _x;
		y = _y;
	}
	//
	void update(){
		parent.fill(255);
		//parent.ellipse(x,y,rd,rd);
		parent.text(str, x, y + 40, 400, 200);
		//Brownian motion 
		vx += Math.random() * 0.2 - 0.1;
		vy += Math.random() * 0.2 - 0.1;
		x += vx;
		y += vy;
		vx *= friction;
		vy *= friction;
		
	}
	//
}
