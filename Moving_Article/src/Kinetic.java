import processing.core.PApplet;
import processing.core.PFont;

public class Kinetic {
	PApplet parent;
	PFont font;
	float textX, textY, rotation;
	float Tsize = 1; //minimum size
	float TsizeChange = 3;// max size
	int time;
	String sentence;
	
	Kinetic(PApplet p, String _sentence, float _textX, float _textY, float _rotation, PFont _font, int _time){
		parent = p;
		textX = _textX;
		textY = _textY;
		rotation = _rotation;
		sentence = _sentence;
		font = _font;
		time = _time;
		}
	void update(){
		 parent.fill(255);
		 time--;
		 //if time is end, show the text
		 if(time < 5){	
		  parent.pushMatrix();
		  parent.translate(textX, textY);
		  //easing for max size
		  Tsize = Tsize+(TsizeChange-Tsize)/8;
		  if(Tsize > 2.9){
		    TsizeChange = 1;
		  }
		  //
		  parent.scale(Tsize);
		  parent.rotate(rotation);
		  parent.text(sentence, 0, 0);
		  parent.popMatrix();
		}  
	}
}
