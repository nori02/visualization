import processing.core.PApplet;
import processing.core.PFont;
import org.json.*;
import mpe.client.*;
import java.util.ArrayList;

public class For3Screens extends PApplet{
	UDPClient client;
	boolean start = false;
	boolean setFlg = true; // set class or not
	final int ID = 1; // set up mpe ID
	int times;// refresh timer
	//
	Brownian[] showText = new Brownian[300];
	// Article variable
	String body, title, byline, url, date, section;
	// Comments variable
	String[] twUser = new String[300];
	String[] twText = new String[300];
	String[] twBiUser = new String[300];
	String[] twBiText = new String[300];
	String[] twTiUser = new String[300];
	String[] twTiText = new String[300];
	String[] nytUser = new String[300];
	String[] nytText = new String[300];
	String[] nytDate = new String[300];
	String[] nytLocate = new String[300];
	
	PFont font, font30, font120;
	PFont mainheadline;
	int totalLength, nytLength, twLength, twbiLength, twtiLength;
	
	float xPos[] = new float[300];
	float yPos[] = new float[300];
	float newXPos[] = new float[300];
	float newYPos[] = new float[300];
	//
	float vx[] = new float[300];
	float vy[] = new float[300];
	float bx[] = new float[300];
	float by[] = new float[300];
	float friction = 0.95f;
	int getRid = 1;
	
public static void main(String args[]) {
    PApplet.main(new String[] { "For3Screens" });
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
			float desX =Float.parseFloat(data[2]);
			float desY = Float.parseFloat(data[3]);
			String text = data[4]; // text data
			String text2 = data[5]; // text data
			System.out.println (text2);
			int timer = i*100 + 15;
			int resetTimer = nytLength*100;
			showText[i] = new Brownian(this, client, text, timer ,resetTimer ,x, y, 70, desX, desY);
		}
		//
		System.out.println ("done");
		setFlg = false;
	}
	
	if (c.messageAvailable() && setFlg == false){
		getRid++;//get rid of first value
		String[] brownianMsg = c.getDataMessage();
		for (int i = 0; i < brownianMsg.length; i++) {
			if(getRid > 2){//start after throw value
				String[] browniandata = brownianMsg[i].split(",");
				bx[i] = Float.parseFloat(browniandata[0]); // x position
				by[i] = Float.parseFloat(browniandata[1]); // y position
			}
		}
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
	frameRate(60);
	font = loadFont("HelveticaNeue-Bold-15.vlw");
	mainheadline = loadFont("Helvetica-Bold-120.vlw");
	textFont(font,15);
	textLeading(16);
	smooth();
	noStroke();
	
	
	String[] lines = loadStrings("comments0408.json"); 
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
		section = ob1.getString("section");
		
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
			nytUser[i] = nytObject.getString("user");//put user
			nytText[i] = nytObject.getString("text");//put text
			nytDate[i] = nytObject.getString("created");//put date
			nytLocate[i] = nytObject.getString("location");//put locate
		}
		nytLength = nytComments.length();
		nytLength = 40;
		twLength = twitter.length();
		twbiLength = bitwitter.length();
		twtiLength = titwitter.length();
		// put total number of the comments
		totalLength = twitter.length() + bitwitter.length() + titwitter.length() + nytComments.length();
		
		System.out.println ("title = "+ title);
		System.out.println ("byline = "+ byline); 
		System.out.println ("date = "+ date); 
		System.out.println ("url = "+ url); 
		System.out.println ("section = "+ section); 
		System.out.println ("total comments number = " + totalLength);
		//
		// Only if we are client 0, put first X,Y position for comments
		if (ID == 0) {
			for (int i = 0; i < nytLength; i++) {
				xPos[i] = random(width-100, width*3-50);
				yPos[i] = random(0, 700);
				newXPos[i] = random(width-100, width*3-50);
				newYPos[i] = random(0, 700);
			}
		}
	}  
	catch (JSONException e) {  
	  System.out.println (e.toString());  
	} 
	
}

public void draw(){
	//timer redraw everything
	
	//start most pixel ever drawing
	if (start) {
		client.placeScreen();
		background(0);
		textAlign(LEFT);
		
		//for first screen text
		int leftMargin = 100;
		 fill(180, 172, 165, 150);
		  textFont (mainheadline, 30);
		//  String sectionname = "U.S. / POLITICS";
		  text(section, leftMargin, 100);
		  
		  fill(220, 200, 190);
		  textFont (mainheadline);
	//	  String mainhead = "Gay Rights Groups Celebrate Victories in Marriage Push";
		  textLeading(115);
		  text(title, leftMargin, 130, width-200, 400);
		  
		  fill(180, 172, 65, 150);
		  textFont (mainheadline, 20);
	//	  String author = "By ABBY GOODNOUGH";
		  text(byline, leftMargin, 500);
		  
		  float timeXpos = textWidth(byline) + 15;
		  fill(150, 15, 15, 180);
		  textFont (mainheadline, 20);
		  String timestamp = "DATE:" + date;
		  text(timestamp, leftMargin + timeXpos, 500);

		  fill(206, 188, 20);
		  textFont (mainheadline, 40);
		  String commentnums = nytLength + " COMMENTS";
		  text(commentnums, leftMargin, 550);
		  
		///broadcast valuabes for first position and text data
			for (int i = 0; i < nytLength; i++) {
				if (ID == 0 && frameCount % 60 == 0 && setFlg == true) {
					client.broadcast(xPos[i] + "," + yPos[i] + "," + xPos[i] + "," + yPos[i] + "," + nytText[i] + "," + nytUser[i]);
				}
			}
		//broadcast brownian motion valuabes for all clients
			for (int i = 0; i < nytLength; i++) {
				if (ID == 0 && setFlg == false) {
					vx[i] += Math.random() * 0.2 - 0.1;
					vy[i] += Math.random() * 0.2 - 0.1;
					vx[i] *= friction;
					vy[i] *= friction;
					client.broadcast(vx[i] + "," + vy[i]);
				}
			}
		//update text movement
		if(setFlg == false){
			for (int i = 0; i < nytLength; i++) {
				textAlign(LEFT);
				textFont(font,15);
				showText[i].update(bx[i],by[i]); 
			}
		}
		client.done();
	}
}
}