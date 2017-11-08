package considition.api.models;

import java.util.List;
import considition.api.models.objectives.*;

public class GameState {
	public int id;
	public List<City> cities;
	public List<TransportationMethod> transportation;
	public List<GameObjective> objectives;
	public Location start;
	public Location end;
	public char[][] map;
}
