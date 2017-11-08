package considition.api.models;

import java.util.List;

public class City extends Location {
	public String name;
	public List<String> hasFlightTo;
	public List<String> hasTrainTo;
	public List<String> hasBusTo;
}
