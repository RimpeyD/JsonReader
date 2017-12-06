import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.lang.Object;
import java.net.HttpURLConnection;
import java.util.Base64;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JsonReader {

    public static String returnInfo(String urlGiven, String username, String password) {
    	
    	String jsonLine = "";
    	
    	URL url;
		try {
			String newUrl = urlGiven;
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
    
    @SuppressWarnings({ "unchecked" })
	public static String getPhenoData(String var1, String var2, String var3){
    String results = "";
	try{		
		JSONParser parser = new JSONParser();
	
		String line = returnInfo(var1, var2, var3);
		
		Object obj = parser.parse(line);
		JSONObject jsonObject = (JSONObject) obj;
		JSONArray patientSummaries = (JSONArray) jsonObject.get("patientSummaries"); 
		String id;
			
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
    	
    	Scanner scanner = new Scanner(System.in);
		System.out.flush();
		String var1 = scanner.nextLine();
		String var2 = scanner.nextLine();
		String var3 = scanner.nextLine();
		
		System.out.println(getPhenoData(var1, var2, var3));
 	}

}