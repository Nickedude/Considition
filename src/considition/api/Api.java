package considition.api;

import java.util.*;

import com.google.gson.*;

import java.io.*;
import java.net.*;

import considition.api.helpers.*;
import considition.api.models.*;
import considition.api.models.objectives.*;
import considition.api.models.response.*;

public class Api {
	
	private static final String DOMAIN = "http://theconsidition.se";
	private static String _apiKey;
	private static boolean _silenced;
	
	private static void log(String message) {
		if (!_silenced) {
			System.out.println("API: " + message);
		}
	}
	
	private static String get(String path) {
		try {
			URL url = new URL(DOMAIN + "/" + path);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("GET");
			con.setRequestProperty("X-ApiKey", _apiKey);
			con.setRequestProperty("Accept", "application/json");
			
			if (con.getResponseCode() != 200) {
				log(con.getResponseCode() + ": " + con.getResponseMessage());
				return null;
			}
			
			BufferedReader br = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			
			String data = "";
			String chunk;
			while ((chunk = br.readLine()) != null) {
				data += chunk;
			}
			
			con.disconnect();
			return data;
		}
		catch (IOException ex) {
			log("Exception: " + ex.getMessage());
			ex.printStackTrace();
		}
		return null;
	}
	
	private static String post(String path, Object data) {
		try {
			URL url = new URL(DOMAIN + "/" + path);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setDoOutput(true);
			con.setRequestMethod("POST");
			con.setRequestProperty("X-ApiKey", _apiKey);
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Accept", "application/json");
			
			Gson gson = new Gson();
			String jsonData = gson.toJson(data);

			OutputStream os = con.getOutputStream();
			os.write(jsonData.getBytes());
			os.flush();
			
			if (con.getResponseCode() != 200) {
				log(con.getResponseCode() + ": " + con.getResponseMessage());
				return null;
			}
			
			BufferedReader br = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			
			String received = "";
			String chunk;
			while ((chunk = br.readLine()) != null) {
				received += chunk;
			}
			
			con.disconnect();
			return received;
		}
		catch (IOException ex) {
			log("Exception: " + ex.getMessage());
			ex.printStackTrace();
		}
		return null;
	}

	private static void handleApiResponse(ApiResponse response) {
		if (response instanceof ErrorApiResponse) {
			ErrorApiResponse error = (ErrorApiResponse)response;
			String message = "An error occured: " + error.message;
			log(message);
			System.exit(1);
		}
		else if (response instanceof GameErrorApiResponse) {
			GameErrorApiResponse error = (GameErrorApiResponse)response;
			String message = "Your solution had an error: " + error.error;
			log(message);
			System.exit(1);
		}
	}
	
	public static void setApiKey(String apiKey) {
		_apiKey = apiKey;
	}
	
	public static void silence() {
		_silenced = true;
	}
	
	public static void unsilence() {
		_silenced = false;
	}
	
	public static void initGame() {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(ApiResponse.class, new ApiResponseDeserializer());
		Gson gson = builder.create();
		String json = get("considition/initgame");
		ApiResponse response = gson.fromJson(json, ApiResponse.class);
		handleApiResponse(response);
		GameCreatedApiResponse gameResponse =
				(GameCreatedApiResponse)response;
		log("Created new game with ID " + gameResponse.gameId);
	}
	
	public static GameState getMyLastGame() {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(ApiResponse.class, new ApiResponseDeserializer());
		builder.registerTypeAdapter(GameObjective.class, new GameObjectiveDeserializer());
		Gson gson = builder.create();
		String json = get("considition/getgame");
		ApiResponse response = gson.fromJson(json, ApiResponse.class);
		handleApiResponse(response);
		GetGameApiResponse gameResponse =
				(GetGameApiResponse)response;
		log("Retrieved game with ID " + gameResponse.gameState.id);
		return gameResponse.gameState;
	}
	
	public static GameState getGame(int gameStateId) {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(ApiResponse.class, new ApiResponseDeserializer());
		builder.registerTypeAdapter(GameObjective.class, new GameObjectiveDeserializer());
		Gson gson = builder.create();
		String json = get("considition/getgame?gameStateId=" + gameStateId);
		ApiResponse response = gson.fromJson(json, ApiResponse.class);
		handleApiResponse(response);
		GetGameApiResponse gameResponse =
				(GetGameApiResponse)response;
		log("Retrieved game with ID " + gameResponse.gameState.id);
		return gameResponse.gameState;
	}
	
	public static int submitSolution(List<String> solution, int gameStateId) {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(ApiResponse.class, new ApiResponseDeserializer());
		Gson gson = builder.create();
		String json = post("considition/submit?gameStateId=" + gameStateId, solution);
		ApiResponse response = gson.fromJson(json, ApiResponse.class);
		handleApiResponse(response);
		GameCompletedApiResponse gameResponse =
				(GameCompletedApiResponse)response;
		log("Your solution gave " + gameResponse.points + " points.");
		return gameResponse.points;
	}
	
}
