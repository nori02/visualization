import processing.core.PApplet;
import processing.core.PFont;

public class textClass {
		simpleText parent;
		PFont font;
		String str, str2;
		float x, y;
		int time;
		boolean changeText = true;
		float alphaVal = 50;
		int alphaSpeed = 5;
		boolean colFlg = true;
		float r = 0, g = 0, b = 0;
	
	textClass (simpleText p, String _str, String _str2,PFont _font, int _time) {  
		parent = p; 
		str = _str;
		str2 = _str2;
		font = _font;
		time = _time;
		alphaVal = parent.random(50,150);
		} 
	    //
	
		void update(float _x, float _y) {
		   x = _x;
		   y = _y;
		   //
		   time--;
		   if(time < 0){
			   changeText = false;
			   time = 1;
		   }
		   //
		   if(changeText == true){
			   alphaVal = alphaVal + alphaSpeed;
			   if(alphaVal > 250){
				//   alphaVal = 25;
				   alphaSpeed = -alphaSpeed;
			   }
			   if(alphaVal < 5){
				 //  alphaVal = 200;
				   alphaSpeed = -alphaSpeed;
			   }
			   //
			   parent.fill(alphaVal);
			   parent.text(str, x, y);
			   
		   }else if(changeText == false){
			   /*
			   if(colFlg == true){
			   r = parent.random(0,250);
			   g =  parent.random(0,250);
			   b =  parent.random(0,250);
			   colFlg = false;
			   }
			   parent.fill(r,g,b);
			   */
			 //  parent.fill(255,255,255);
			   parent.text(str2, x, y);
		   }
		} 
}
