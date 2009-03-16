import processing.core.PApplet;
import processing.core.PFont;
import org.json.*;
import mpe.client.*;
import java.util.ArrayList;

public class simpleClick extends PApplet{
	UDPClient client;
	boolean start = false;
	float[] xPos = new float[20];
	float[] yPos = new float[20];
	float[] eSize = new float[20];
	String[] headlines = new String[20];
	points [] pClass = new points[20];
	final int ID = 0; // set up mpe ID
	boolean setFlg = true; // set class or not
	int times;// refresh timer
	
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
			float x = Float.parseFloat(data[0]); //circle x position
            float y = Float.parseFloat(data[1]); //circle y position
            float x2 = Float.parseFloat(data[2]); //next circle x position
            float y2 = Float.parseFloat(data[3]); // next circle y position
            float size = Float.parseFloat(data[4]); // circle size
            String headline = data[5]; // headline text
            int time = i*100+50; // timer for drawing objects
            pClass[i] = new points(this, client, headline, time, x, y, x2, y2, size);
		}
		//
		//System.out.println("done");
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
	//set up for draw
	PFont font = loadFont("LinotypeUnivers-BasicBold-48.vlw");
	textFont(font,25);
	smooth();
	ellipseMode(CENTER);
	String[] lines = loadStrings("data/nyt.txt"); 
	try {
	  JSONObject Data = new JSONObject(join(lines, ""));
	   for (int i = 0; i < 6; i++) {
		// Only if we are client 0!!!
		if (ID == 0) {
		   // data from json file
		   JSONArray entries1 = Data.getJSONArray("entry_point");
		   JSONObject entry2 = entries1.getJSONObject(i);
		   headlines[i] = entry2.getString("name");
		   //pick the random data 
		   xPos[i] = i*300 + 200;
		   xPos[6] = 6*300 + 200;
		   yPos[i] = random(50,400);
		   yPos[6] = random(50,400);
		   eSize[i] = random(10,90);
		}
	   }
	}  
	catch (JSONException e) {  
	  System.out.println (e.toString());  
	} 
}

public void draw(){
	//timer redraw everything
	times++;
	if(times > 700){
		setFlg = true;
		times = 0;
	}
	//start most pixel ever drawing
	if (start) {
	client.placeScreen();
		background(0);
		//send the data for all clients
		for (int i = 0; i < 6; i++) {
			if (ID == 0 && frameCount % 30 == 0) {
				client.broadcast(xPos[i] + "," + yPos[i] + "," + xPos[i+1] + "," + yPos[i+1] + "," + eSize[i] + "," + headlines[i]);
			}
		}
		//
		if(setFlg == false){
			for (int i = 0; i < 6; i++) {
				pClass[i].update(); 
			}
		}
		client.done();
	}
}
}