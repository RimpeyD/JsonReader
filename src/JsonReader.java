import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.lang.Object;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Base64;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonReader {

    int indexOfKey = 0;
    int nextIndexOfSpace = 0;
    int lastIndexOfKey = 0;
    static String key = "\"id\":";
    
    public static String returnInfo(String urlGiven, String username, String password) {
    	
    	String jsonLine = "";
    	
    	URL url;
		try {
			String newUrl = urlGiven;
//			String newUrl = formerUrl + "/" + id;
			String authorization = username + ":" + password;
			
			url = new URL(newUrl);
			String encoding = Base64.getEncoder().encodeToString((authorization).getBytes("UTF-8"));
			
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.setRequestProperty ("Authorization", "Basic " + encoding);
			InputStream content = (InputStream)connection.getInputStream();
			BufferedReader in = new BufferedReader (new InputStreamReader (content));
			
			jsonLine = in.readLine();
			
		} catch (Exception e) {
			System.out.println("ERROR MESAGASDA");
			e.printStackTrace();
			
		}
    	
    	return jsonLine;
    }
    
    public static String returnJson(String urlGiven, String username, String password, String id) {
    	
    	String jsonLine = "";
    	
    	URL url;
		try {
			
			String formerUrl = urlGiven;
			String newUrl = formerUrl + "/" + id;
			String line;
			
			String authorization = username + ":" + password;
			
			url = new URL(newUrl);
			String encoding = Base64.getEncoder().encodeToString((authorization).getBytes("UTF-8"));
			
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.setRequestProperty ("Authorization", "Basic " + encoding);
			InputStream content = (InputStream)connection.getInputStream();
			BufferedReader in = new BufferedReader (new InputStreamReader (content));
			
//			while ((line = in.readLine()) != null) {
			jsonLine = in.readLine();
//			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return jsonLine;
    }
    
    public static String getName(String jsonExtracted) {
    	
    	String patientName = "";
    	
    	JSONParser parser = new JSONParser();
    	
    	Object obj;
		try {
			obj = parser.parse(jsonExtracted);
			JSONObject patientData = (JSONObject) obj;
			JSONObject patientIdentifier = (JSONObject) patientData.get("patient_name");			
			String firstName = (String) patientIdentifier.get("first_name");			
			String lastName = (String) patientIdentifier.get("last_name");
			
			patientName = firstName + " " + lastName;
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
    	
    	return patientName;
    }
    
    @SuppressWarnings({ "unchecked", "resource" })
	public static String getPhenoData(String var1, String var2, String var3){
    String results = "";
	try{
		Scanner scanner = new Scanner(System.in);
//		System.out.flush();
//		String var1 = scanner.nextLine();
//		String var2 = scanner.nextLine();
//		String var3 = scanner.nextLine();
		
		JSONParser parser = new JSONParser();
	
		String line = returnInfo(var1, var2, var3);
		
//		while ((line = in.readLine()) != null) {
		
//		if(line != null) {
			
			Object obj = parser.parse(line);
			JSONObject jsonObject = (JSONObject) obj;
			JSONArray patientSummaries = (JSONArray) jsonObject.get("patientSummaries"); 
			
			String id;
			String eid;
			
			int temp = 0;
			
			try {
			
				while (((JSONObject) patientSummaries.get(temp)) != null) {
					temp++;
				}
			}catch (Exception e) {
				
			}finally {
				
				JSONArray jArray = new JSONArray();
				
				for(int i = 0; i < temp; i++) {
					JSONObject array = ((JSONObject) patientSummaries.get(i));
					JSONObject json = new JSONObject();
					id = (String) array.get("id");
					eid = (String) array.get("eid");
					json.put("id", id);
					
					String jsonOutput = returnJson(var1, var2, var3, id);
					String name = getName(jsonOutput);
					json.put("name", name);

					jArray.add(json);
				}
				results = jArray.toString();
			}
	}
	catch (Exception e){
		e.printStackTrace();
	}
	
		return results;
   }
    
    public static void main(String[] args){
//    	System.out.println(getPhenoData("http://localhost:9090/rest/patients", "Admin", "admin"));
    	System.out.println(getPhenoData("https://c4r.ccm.sickkids.ca/rest/patients", "FBernier", "Ber60ahs"));
 	}

}