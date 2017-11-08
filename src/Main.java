//package considition;

import java.util.*;

import considition.api.*;
import considition.api.models.*;

public class Main {
	
	// TODO: Enter your API key
	static final String API_KEY = "3abea427-dd03-4706-b621-a89a093b2771";
	
	static List<String> solve(GameState game) {
		/*
         * --- Available commands ---
         * TRAVEL [NORTH|SOUTH|WEST|EAST]
         * [BUS|TRAIN|FLIGHT] {CityName}
         * SET_PRIMARY_TRANSPORTATION [CAR|BIKE]
         */

        // TODO: Implement your solution

        HashMap<String, Integer> costs = new HashMap<>();
        HashMap<String, City> cityCoords = new HashMap<>();
        for(TransportationMethod t :  game.transportation) {
            double time = 1.0 / t.speed;
            double pollution = time * t.pollutions;
            int cost = (int) (time * 1000000) + (int) (pollution * 1000000);
            costs.put(t.name, cost);
        }


        Graph<MapNode> g = new Graph<>();
        //Parse cities as nodes
        for (int y = 0; y < game.map.length; y++) {
            for (int x = 0; x < game.map[y].length; x++) {
                g.addNode(new MapNode(x, y, game.map[y][x]));
            }
        }

        for(int y = 0; y < game.map.length; y++) {
            for(int x = 0; x < game.map[y].length; x++) {
                if(game.map[y][x] == 'W') {
                    if(y > 0) {
                        g.addEdge(new MapNode(x, y, 'B'), new MapNode(x, y-1, 'B'), costs.get("Boat"), "Boat");
                    }
                    if(x > 0) {
                        g.addEdge(new MapNode(x, y, 'B'), new MapNode(x-1, y, 'B'), costs.get("Boat"), "Boat");
                    }
                    if(x < game.map[y].length - 1) {
                        g.addEdge(new MapNode(x, y, 'B'), new MapNode(x + 1, y, 'B'), costs.get("Boat"), "Boat");
                    }
                    if(y < game.map.length - 1) {
                        g.addEdge(new MapNode(x, y, 'B'), new MapNode(x, y + 1, 'B'), costs.get("Boat"), "Boat");
                    }
                } else {
                    if(y > 0) {
                        g.addEdge(new MapNode(x, y, 'B'), new MapNode(x, y-1, 'B'), costs.get("Bike"), "Bike");
                        g.addEdge(new MapNode(x, y, 'B'), new MapNode(x, y-1, 'B'), costs.get("Car"), "Car");
                    }
                    if(x > 0) {
                        g.addEdge(new MapNode(x, y, 'B'), new MapNode(x-1, y, 'B'), costs.get("Bike"), "Bike");
                        g.addEdge(new MapNode(x, y, 'B'), new MapNode(x-1, y, 'B'), costs.get("Car"), "Car");
                    }
                    if(x < game.map[y].length - 1) {
                        g.addEdge(new MapNode(x, y, 'B'), new MapNode(x + 1, y, 'B'), costs.get("Bike"), "Bike");
                        g.addEdge(new MapNode(x, y, 'B'), new MapNode(x + 1, y, 'B'), costs.get("Car"), "Car");
                    }
                    if(y < game.map.length - 1) {
                        g.addEdge(new MapNode(x, y, 'B'), new MapNode(x, y + 1, 'B'), costs.get("Car"), "Car");
                        g.addEdge(new MapNode(x, y, 'B'), new MapNode(x, y + 1, 'B'), costs.get("Bike"), "Bike");
                    }
                }
            }
        }

        for(City c : game.cities) {
            cityCoords.put(c.name, c);
        }

        for(City c : game.cities) {
            for(String s : c.hasBusTo) {
                City c2 = cityCoords.get(s);
                double dist = Math.sqrt((c.x-c2.x)^2 + (c.y - c2.y)^2);
                g.addEdge(new MapNode(c.x, c.y), new MapNode(c2.x, c2.y), (int) (costs.get("Bus") * dist), "Bus");
            }

            for(String s : c.hasFlightTo) {
                City c2 = cityCoords.get(s);
                double dist = Math.sqrt((c.x-c2.x)^2 + (c.y - c2.y)^2);
                g.addEdge(new MapNode(c.x, c.y), new MapNode(c2.x, c2.y), (int) (costs.get("Flight") * dist), "Flight");
            }

            for(String s : c.hasTrainTo) {
                City c2 = cityCoords.get(s);
                double dist = Math.sqrt((c.x-c2.x)^2 + (c.y - c2.y)^2);
                g.addEdge(new MapNode(c.x, c.y), new MapNode(c2.x, c2.y), (int) (costs.get("Train") * dist), "Train");
            }
        }

        AStar<MapNode> aStar = new AStar<>(g);

        aStar.computePath(new MapNode(game.start), new MapNode(game.end));


        // Example solution
		List<String> solution = new ArrayList<String>();
		int x = game.start.x;
		int y = game.start.y;
		while (x < game.end.x) {
			x++;
			solution.add("TRAVEL EAST");
		}
		while (y < game.end.y) {
			y++;
			solution.add("TRAVEL SOUTH");
		}
		
		return solution;
	}
	
	public static void main(String[] args) {
		Api.setApiKey(API_KEY);
		Api.initGame();
		GameState game = Api.getMyLastGame();
		List<String> solution = solve(game);
		Api.submitSolution(solution, game.id);
	}

	static class MapNode extends  Location {
	    char type;

	    MapNode(int x, int y, char type) {
	        this(x,y);
            this.type = type;
        }

        MapNode(int x, int y) {
	        this.x = x;
	        this.y = y;
        }

        MapNode(Location l) {
            x = l.x;
            y = l.y;
        }

        @Override
        public int hashCode() {
            return y * 10000000 + x;
        }
    }
	
}
