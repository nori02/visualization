import processing.core.PApplet;
import processing.core.PFont;
import org.json.*;
import mpe.client.*;

public class simpleText extends PApplet{
	UDPClient client;
	boolean start = false;
	String[] headlines = new String[20];
	String[] timeStamp = new String[20];
	float[] textWidths = new float[21];
	int hLength;
	final int ID = 1; // set up mpe ID
	//
	textClass [] headClass = new textClass[20];
	
public static void main(String args[]) {
    PApplet.main(new String[] { "simpleText" });
}

public void setup(){
	//basic set up for Most pixel ever library
	client = new UDPClient(sketchPath("mpe"+ ID +".ini"),this);
	size(client.getLWidth(), client.getLHeight());
	client.start();
	noLoop();
	//set up for draw
	PFont font = loadFont("LinotypeUnivers-BasicBold-48.vlw");
	textFont(font,25);
	smooth();
	///////get data by json
	String[] lines = loadStrings("http://api.nytimes.com/svc/news/v2/all/last24hours.json?api-key=1ce0a4c3cbe8bd589b69a199ff000bd1:14:57909004"); 
	try {
	  JSONObject Data = new JSONObject(join(lines, "")); 
	  JSONArray entries = Data.getJSONArray("results");
	  hLength = entries.length();//put length of data
	  for (int i = 0; i < entries.length(); i++) {
		    JSONObject entry = entries.getJSONObject(i);
		    String trans = entry.getString("created");
		    headlines[i] = entry.getString("headline");//put headlines
		    timeStamp[i] =  trans.substring(12,19);//put time stamp
		    headClass[i]  = new textClass(this, timeStamp[i], headlines[i], font, i*60);//make text class
		  }
	}  
	catch (JSONException e) {  
	  System.out.println (e.toString());  
	} 	
	/////////
}

public void frameEvent(UDPClient c){
	  start = true;
	  redraw();
	}

public void draw(){
	float[] xPos = new float[20];
	float[] yPos = new float[20];
	int margin = 50;
	int headBetween = 50;
	float blength  = 0;
	float dy = 45;
	//
	if (start) {
	client.placeScreen();
	background(0);
	///////get the X, Y position of each headlines//////////
	for (int i = 0; i < hLength; i++) {
		textWidths[i] = textWidth(headlines[i]);
		xPos[i] = blength;
		yPos[i] = dy;
		//
		blength = blength + textWidths[i] + headBetween; 
			if(blength + textWidths[i+1]> client.getMWidth() - margin ){
				dy = dy + 50;
				blength = 0;
			}
	}
	///////updatae text class//////////
	for (int i = 0; i < hLength; i++) {
		headClass[i].update(xPos[i] + margin, yPos[i]);
	}
	client.done();
	}
}
//end 
}