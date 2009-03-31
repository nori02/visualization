import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import processing.core.PApplet;
import processing.core.PFont;

public class move extends PApplet{
	Brownian[] showCircle = new Brownian[20];
	String[] headlines = new String[20];
	PFont font;
	int hLength;
	//
	public static void main(String args[]) {
		PApplet.main(new String[] { "move" });
	}
	public void setup(){
		size(1100,700);
		//basick set up
		font = loadFont("HelveticaNeue-Bold-15.vlw");
		smooth();
		textFont(font,15);
		textLeading(24);
		textAlign(CENTER);
		noStroke();
		///////get data by json
		String[] lines = loadStrings("http://api.nytimes.com/svc/news/v2/all/last24hours.json?api-key=1ce0a4c3cbe8bd589b69a199ff000bd1:14:57909004"); 
		try {
			JSONObject Data = new JSONObject(join(lines, "")); 
			JSONArray entries = Data.getJSONArray("results");
			hLength = entries.length();//put length of data
		for (int i = 0; i < entries.length(); i++) {
			//put variables
			float x = random(0, width);
			float y = random(0, height-50);
			float rd = random(20, 50);
			float fric = 0.95f;
			//
			JSONObject entry = entries.getJSONObject(i);
			headlines[i] = entry.getString("headline");//put headlines
			showCircle[i] = new Brownian(this, headlines[i] ,x, y, rd, fric, font);
		  }
		}
		catch (JSONException e) {  
		  System.out.println (e.toString());  
		}
	}
	public void draw(){
		background(50);
		for(int i = 0; i<hLength; i++){
			showCircle[i].update();	
		}
	}
}