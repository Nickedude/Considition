package considition.api.helpers;

import java.lang.reflect.Type;

import com.google.gson.*;

import considition.api.models.response.*;

public class ApiResponseDeserializer implements JsonDeserializer<ApiResponse> {

	public ApiResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		
		JsonObject jsonObject = json.getAsJsonObject();

        JsonElement jsonType = jsonObject.get("type");
        String type = jsonType.getAsString();

        ApiResponse model = null;     

        if (type.equals("Error")) {
        	model = new ErrorApiResponse();
        }
        else if (type.equals("GameError")) {
        	model = new GameErrorApiResponse();
        }
        else if (type.equals("GetGame")) {
        	model = new GetGameApiResponse(); 
        }
        else if (type.equals("GameCreated")) {
        	model = new GameCreatedApiResponse();
        }
        else if (type.equals("GameCompleted")) {
        	model = new GameCompletedApiResponse();
        }

        model = context.deserialize(json, model.getClass());
        
        return model;
	}
	
}
