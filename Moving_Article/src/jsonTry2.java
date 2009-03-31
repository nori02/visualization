import processing.core.PApplet;
import processing.core.PFont;
import org.json.*;

import processing.xml.XMLElement;
//
public class jsonTry2 extends PApplet{
	String url1;
	String byline, abs, dateline, head;
	Kinetic [] showHead = new Kinetic[20];
	String[] headlines = new String[20];
	int num = 0;
	PFont font;
	int hLength;
	int timer = 2500;
	
public static void main(String args[]) {
    PApplet.main(new String[] { "jsonTry2" });
}

public void setup(){
	size(1100,700);
	//set up for text
	font = loadFont("HelveticaNeue-Bold-30.vlw");
	textFont(font,30);
	textLeading(24);
	textAlign(CENTER);
	///////get data by json
	String[] lines = loadStrings("http://api.nytimes.com/svc/news/v2/all/last24hours.json?api-key=1ce0a4c3cbe8bd589b69a199ff000bd1:14:57909004"); 
	try {
	JSONObject Data = new JSONObject(join(lines, "")); 
	JSONArray entries = Data.getJSONArray("results");
	hLength = entries.length();//put length of data
	for (int i = 0; i < entries.length(); i++) {
	JSONObject entry = entries.getJSONObject(i);
		headlines[i] = entry.getString("headline");//put headlines
		float RandomRotate = random(-0.5f, 0.5f);
		float xPos = random(100, width-100);
		float yPos = random(100, height-100);
		int time = i*100+50;
		showHead[i] = new Kinetic(this, headlines[i], xPos, yPos, RandomRotate, font, time);
	  }
	}  
	catch (JSONException e) {  
	  System.out.println (e.toString());  
	} 
}

public void draw(){
	background(50);
	for(int i = 0; i<hLength; i++){
	showHead[i].update();
	}
	//
	timer--;
	//if the timer is end, search article and draw letters again
	if(timer < 5){
		timer = 2500;
		///////get data by json
		String[] lines = loadStrings("http://api.nytimes.com/svc/news/v2/all/last24hours.json?api-key=1ce0a4c3cbe8bd589b69a199ff000bd1:14:57909004"); 
		try {
		JSONObject Data = new JSONObject(join(lines, "")); 
		JSONArray entries = Data.getJSONArray("results");
		hLength = entries.length();//put length of data
		for (int i = 0; i < entries.length(); i++) {
		JSONObject entry = entries.getJSONObject(i);
			headlines[i] = entry.getString("headline");//put headlines
			float RandomRotate = random(-0.5f, 0.5f);
			float xPos = random(100, width-100);
			float yPos = random(100, height-100);
			int time = i*100+50;
			showHead[i] = new Kinetic(this, headlines[i], xPos, yPos, RandomRotate, font, time);
		  }
		}  
		catch (JSONException e) {  
		  System.out.println (e.toString());  
		} 
	}
}
//end 
}