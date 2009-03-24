import processing.core.PApplet;
import processing.core.PFont;
import org.json.*;
import processing.xml.XMLElement;
//
public class jsonTry extends PApplet{

	String url1;
	String byline, abs, dateline, head;
	
public static void main(String args[]) {
    PApplet.main(new String[] { "jsonTry" });
}

public void setup(){
	size(800,600);
	//we don't need font for thi code
	/*
	PFont font = loadFont("LinotypeUnivers-BasicBold-48.vlw");
	textFont(font,20);
	textLeading(24);
	*/
	//
	String[] lines = loadStrings("http://api.nytimes.com/svc/community/v2/comments/recent.json?api-key=e9a9a2c57ba09d052376815d7a67a964:12:57909004"); 
	try {
	  //First, we call recent comment
	  JSONObject Data = new JSONObject(join(lines, ""));
	  JSONObject Data2 = Data.getJSONObject("results");
	  JSONArray firstArray = Data2.getJSONArray("comments");
	  JSONObject Obs = firstArray.getJSONObject(0);
	  url1 = Obs.getString("articleURL");
	  System.out.println (url1);
	  //call exact URL comment
	  String url2 = "http://api.nytimes.com/svc/community/v2/comments/url/exact-match.json?url=" + url1 +"&api-key=e9a9a2c57ba09d052376815d7a67a964:12:57909004";
	  String[] lines2 = loadStrings(url2);
	  //check comment number
	  try {
	  	JSONObject DataComment = new JSONObject(join(lines2, ""));
	 	JSONObject DataComment2 = DataComment.getJSONObject("results");
	 	int totalNum = DataComment2.getInt("totalCommentsReturned");
	  	System.out.println ("total number = "+totalNum);
	  	//if number is grater than 10 call another API
	  		if(totalNum >= 10){
	  			System.out.println ("Call API !!");
	  			String url3 = "NYTLabAPI;
	  			String[] lines3 = loadStrings(url3); 
	  			System.out.println (url3);
	  			try {
	  				JSONObject DataResult = new JSONObject(join(lines3, ""));
	  				JSONObject DataResult2 = DataResult.getJSONObject("data");
	  				byline = DataResult2.getString("bylline");
	  				abs = DataResult2.getString("abstract");
	  				dateline = DataResult2.getString("dateline");
	  				head = DataResult2.getString("headline");
	  				//print out results
	  				System.out.println ("bylline = "+ byline);
	  				System.out.println ("abstract = "+ abs);
	  				System.out.println ("dateline = "+ dateline);
	  				System.out.println ("headline = "+ head);
	  			}catch (JSONException e) {  
	  				System.out.println (e.toString()); 
	  			}
	  		}
	  	//
	  	}catch (JSONException e) {
		  System.out.println (e.toString());  
		} 
	  //---------	
	}  
	catch (JSONException e) {  
	  System.out.println (e.toString());  
	} 
}

public void draw(){
	background(255);
	fill(0);
}

//end 
}