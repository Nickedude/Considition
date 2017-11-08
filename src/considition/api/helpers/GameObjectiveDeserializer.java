package considition.api.helpers;

import java.lang.reflect.Type;

import com.google.gson.*;

import considition.api.models.objectives.*;
import considition.api.models.response.ApiResponse;
import considition.api.models.response.ErrorApiResponse;
import considition.api.models.response.GameCompletedApiResponse;
import considition.api.models.response.GameCreatedApiResponse;
import considition.api.models.response.GameErrorApiResponse;
import considition.api.models.response.GetGameApiResponse;

public class GameObjectiveDeserializer implements JsonDeserializer<GameObjective> {
	
	public GameObjective deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		
		JsonObject jsonObject = json.getAsJsonObject();

        JsonElement jsonType = jsonObject.get("type");
        String type = jsonType.getAsString();

        GameObjective model = null;
        
        if (type.equals("long_bike_ride")) {
        	model = new BikeDistanceObjective();
        }
        else if (type.equals("curvy_road")) {
        	model = new CurvyRoadObjective();
        }
        else if (type.equals("far_from_city")) {
        	model = new FarFromCityObjective();
        }
        else if (type.equals("far_from_land")) {
        	model = new FarFromLandObjective();
        }
        else if (type.equals("long_flight")) {
        	model = new LongFlightObjective();
        }
        else if (type.equals("nearby_land")) {
        	model = new NearbyLandObjective();
        }
        else if (type.equals("quick_far_city_visit")) {
        	model = new QuickFarCityVisitObjective();
        }
        else if (type.equals("return_to_city")) {
        	model = new ReturnToCityObjective();
        }
        else if (type.equals("train_both_ways")) {
        	model = new TrainBothWaysObjective();
        }
        else if (type.equals("unique_path")) {
        	model = new UniquePathObjective();
        }
        else if (type.equals("useful_transport_methods")) {
        	model = new UsefulTransportMethodsObjective();
        }
        else if (type.equals("visit_city")) {
        	model = new VisitCityObjective();
        }
        else if (type.equals("visit_many_cities")) {
        	model = new VisitManyCitiesObjective();
        }
        else if (type.equals("visit_small_island")) {
        	model = new VisitSmallIslandObjective();
        }

        model = context.deserialize(json, model.getClass());
        
        return model;
	}
	
}
