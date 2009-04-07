import processing.core.PApplet;
import processing.core.PFont;
import org.json.*;
import mpe.client.*;
import java.util.ArrayList;

public class simpleClick extends PApplet{
	UDPClient client;
	boolean start = false;
	boolean setFlg = true; // set class or not
	final int ID = 1; // set up mpe ID
	int times;// refresh timer
	//
	Brownian[] showCircle = new Brownian[100];
	// Article variable
	String body, title, byline, url, date;
	// Comments variable
	String[] twUser = new String[100];
	String[] twText = new String[100];
	String[] twBiUser = new String[100];
	String[] twBiText = new String[100];
	String[] twTiUser = new String[100];
	String[] twTiText = new String[100];
	String[] nytUser = new String[100];
	String[] nytText = new String[100];
	
	PFont font15, font30, font125;
	int totalLength, nytLength, twLength, twbiLength, twtiLength;
	
	float xPos[] = new float[100];
	float yPos[] = new float[100];
	
public static void main(String args[]) {
    PApplet.main(new String[] { "simpleClick" });
}
//This is triggered by the client whenever a new frame should be rendered
public void frameEvent(UDPClient c){
	//make the class based on server data.
	if (c.messageAvailable() && setFlg == true) {
		String[] msg = c.getDataMessage();
		for (int i = 0; i < msg.length; i++) {
			String[] data = msg[i].split(",");
			float x = Float.parseFloat(data[0]); // x position
			float y = Float.parseFloat(data[1]); // y position
			String text = data[2]; // text data
			System.out.println (text);
            showCircle[i] = new Brownian(this, client, text, x, y, 30, 0.95f, font15);
		}
		//
		System.out.println ("done");
		setFlg = false;
	}
	  start = true;
	  redraw();
	}

public void setup(){
	//basic set up for Most pixel ever library
	client = new UDPClient(sketchPath("mpe"+ ID +".ini"),this);
	size(client.getLWidth(), client.getLHeight());
	client.start();
	noLoop();
	//basic set up for drawing
	font15 = loadFont("HelveticaNeue-Bold-15.vlw");
	font30 = loadFont("HelveticaNeue-Bold-30.vlw");
	font125 = loadFont("HelveticaNeue-Bold-125.vlw");
	textFont(font15,15);
	smooth();
	noStroke();
	
	
	String[] lines = loadStrings("commentsNew.txt"); 
	try {
		JSONObject Data = new JSONObject(join(lines, "")); 
		JSONArray entry = Data.getJSONArray("results");
		JSONObject ob1 = entry.getJSONObject(0);
		
		//get the Article data 
		body = ob1.getString("body");
		title = ob1.getString("title");
		byline = ob1.getString("byline");
		url = ob1.getString("url");
		date = ob1.getString("date");
		
		//get twitter data
		JSONArray twitter = ob1.getJSONArray("twitter_comments");
		for (int i = 0; i < twitter.length(); i++) {
			JSONObject twiObject = twitter.getJSONObject(i);
			twUser[i] = twiObject.getString("user");//put user
			twText[i] = twiObject.getString("text");//put text
			
		//	System.out.println(twUser[i]);
		}
		//get twitter data
		JSONArray bitwitter = ob1.getJSONArray("twitter_bitly_comments");
		for (int i = 0; i < bitwitter.length(); i++) {
			JSONObject twiBiObject = bitwitter.getJSONObject(i);
		//	twBiUser[i] = twiBiObject.getString("user");//put user
			twBiText[i] = twiBiObject.getString("text");//put text
		//	System.out.println(twBiUser[i]);
		}
		//get twitter_tinyurl data
		JSONArray titwitter = ob1.getJSONArray("twitter_tinyurl_comments");
		for (int i = 0; i < titwitter.length(); i++) {
			JSONObject twiTiObject = titwitter.getJSONObject(i);
		//	twTiUser[i] = twiTiObject.getString("user");//put user
			twTiText[i] = twiTiObject.getString("text");//put text
			//System.out.println(twTiUser[i]);
		}
		//get NYT data
		JSONArray nytComments = ob1.getJSONArray("nyt_comments");
		for (int i = 0; i < nytComments.length(); i++) {
			JSONObject nytObject = nytComments.getJSONObject(i);
		//	nytUser[i] = nytObject.getString("user");//put user
			nytText[i] = nytObject.getString("text");//put text
			//System.out.println(nytUser[i]);
		}
		nytLength = nytComments.length();
		twLength = twitter.length();
		twbiLength = bitwitter.length();
		twtiLength = titwitter.length();
		// put total number of the comments
		totalLength = twitter.length() + bitwitter.length() + titwitter.length() + nytComments.length();
		
		System.out.println ("title = "+title); 
		System.out.println ("total comments number = " + totalLength);
		//
		// Only if we are client 0, get the data from json file!!!
		if (ID == 0) {
		for (int i = 0; i < 7; i++) {
			 xPos[i] = random(width*2-100, width*3-50);
			 yPos[i] = random(0, 700);
		}
		}
	}  
	catch (JSONException e) {  
	  System.out.println (e.toString());  
	} 
	
}

public void draw(){
	//timer redraw everything
	/*
	times++;
	if(times > 700){
		setFlg = true;
		times = 0;
	}
	*/
	//start most pixel ever drawing
	if (start) {
		client.placeScreen();
		background(0);
		

		textFont(font125,120);
		textAlign(LEFT);
		text(title, 75, height/3, width-75, height);
		text(totalLength, width + width/2, height/2);
		
		
		
			for (int i = 0; i < 7; i++) {
				//
				if (ID == 0 && frameCount % 30 == 0) {
					client.broadcast(xPos[i] + "," + yPos[i] + "," + nytText[i]);
				}
			}
		//
		if(setFlg == false){
			for (int i = 0; i < 7; i++) {
				textAlign(LEFT);
				textFont(font15,15);
				showCircle[i].update(); 
			}
			
		}
		client.done();
	}
}
}