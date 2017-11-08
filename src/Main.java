//package considition;

import java.util.*;

import considition.api.*;
import considition.api.models.*;
import considition.api.models.objectives.GameObjective;
import considition.api.models.objectives.QuickFarCityVisitObjective;
import considition.api.models.objectives.VisitCityObjective;

public class Main {
	
	// TODO: Enter your API key
	static final String API_KEY = "3abea427-dd03-4706-b621-a89a093b2771";
	public static GameState gameState;
    static HashSet<City> notVisited = new HashSet<>();


    static List<String> solve(GameState game) {
	    gameState = game;
		/*
         * --- Available commands ---
         * TRAVEL [NORTH|SOUTH|WEST|EAST]
         * [BUS|TRAIN|FLIGHT] {CityName}
         * SET_PRIMARY_TRANSPORTATION [CAR|BIKE]
         */

        // TODO: Implement your solution

        HashMap<String, Integer> costs = new HashMap<>();
        HashMap<String, City> cityCoords = new HashMap<>();
        HashMap<Location, City> cityLocation = new HashMap<>();
        Location[][] locations = new Location[game.map.length][game.map[0].length];

        int citiesToVisit = 0;

        for(TransportationMethod t :  game.transportation) {
            double time = 1.0 / t.speed;
            double pollution = time * t.pollutions;
            int cost = (int) (pollution * 10000000);
            costs.put(t.name, cost);
        }


        Graph<Location> g = new Graph<>();
        //Parse cities as nodes
        for (int y = 0; y < game.map.length; y++) {
            for (int x = 0; x < game.map[y].length; x++) {
                Location l = new Location(x,y);
                locations[y][x] = l;
                g.addNode(l);
            }
        }

        for(int y = 0; y < game.map.length; y++) {
            for(int x = 0; x < game.map[y].length; x++) {
                Location from = locations[y][x];
                if(game.map[y][x] == 'W') {
                    if(y > 0) {
                        g.addEdge(from, locations[y-1][x], costs.get("Boat"), "Boat");
                    }
                    if(x > 0) {
                        g.addEdge(from, locations[y][x-1], costs.get("Boat"), "Boat");
                    }
                    if(x < game.map[y].length - 1) {
                        g.addEdge(from, locations[y][x+1], costs.get("Boat"), "Boat");
                    }
                    if(y < game.map.length - 1) {
                        g.addEdge(from, locations[y+1][x], costs.get("Boat"), "Boat");
                    }
                } else {
                    if(y > 0) {
                        g.addEdge(from, locations[y-1][x], costs.get("Bike"), "Bike");
                        g.addEdge(from, locations[y-1][x], costs.get("Car"), "Car");
                    }
                    if(x > 0) {
                        g.addEdge(from, locations[y][x-1], costs.get("Bike"), "Bike");
                        g.addEdge(from, locations[y][x-1], costs.get("Car"), "Car");
                    }
                    if(x < game.map[y].length - 1) {
                        g.addEdge(from,  locations[y][x+1], costs.get("Bike"), "Bike");
                        g.addEdge(from,  locations[y][x+1], costs.get("Car"), "Car");
                    }
                    if(y < game.map.length - 1) {
                        g.addEdge(from, locations[y+1][x], costs.get("Car"), "Car");
                        g.addEdge(from, locations[y+1][x], costs.get("Bike"), "Bike");
                    }
                }
            }
        }

        for(City c : game.cities) {
            notVisited.add(c);
            cityCoords.put(c.name, c);
            cityLocation.put(locations[c.y][c.x], c);
            for(Vertex<Location> v : g.ingoing.get(locations[c.y][c.x])) {
                v.distance =  v.distance / 1000;
            }
        }

        for(City c : game.cities) {
            for(String s : c.hasBusTo) {
                City c2 = cityCoords.get(s);
                double dist = Math.sqrt((c.x-c2.x)^2 + (c.y - c2.y)^2);
                Location from = locations[c.y][c.x];
                Location to = locations[c2.y][c2.x];

                g.addEdge(from,to, (int) (costs.get("Bus") * dist), "Bus");
            }

            for(String s : c.hasFlightTo) {
                City c2 = cityCoords.get(s);
                Location from = locations[c.y][c.x];
                Location to = locations[c2.y][c2.x];
                double dist = Math.sqrt((c.x-c2.x)^2 + (c.y - c2.y)^2);
                g.addEdge(from, to, (int) (costs.get("Flight") * dist), "Flight");
            }

            for(String s : c.hasTrainTo) {
                City c2 = cityCoords.get(s);
                Location from = locations[c.y][c.x];
                Location to = locations[c2.y][c2.x];

                double dist = Math.sqrt((c.x-c2.x)^2 + (c.y - c2.y)^2);
                g.addEdge(from, to, (int) (costs.get("Train") * dist), "Train");
            }
        }

        for(GameObjective go : game.objectives) {
            if (go instanceof VisitCityObjective) {
                City c = cityCoords.get(((VisitCityObjective) go).x);
                for(Vertex<Location> v : g.ingoing.get(locations[c.y][c.x])) {
                    v.distance =  v.distance - go.points * 100000;
                }
            }

            /*if(go instanceof ) {

            }*/

            if(go instanceof QuickFarCityVisitObjective) {
                List<City> candidates = new LinkedList<>();
                for(City c : cityCoords.values()) {
                    c.hasFlightTo.isEmpty();
                    candidates.add(c);
                }

                for(int i = 0; i < candidates.size(); i++) {
                    for(int j = 0; j < candidates.size(); j++) {
                        if(j != i) {
                            City c1 = candidates.get(i);
                            City c2 = candidates.get(j);
                            double distance = Math.sqrt((c1.x - c2.x)^2 + (c1.y - c2.y)^2);
                            if(distance > ((QuickFarCityVisitObjective) go).x) {
                                for(Vertex<Location> v : g.ingoing.get(locations[c1.y][c1.x])) {
                                    v.distance = v.distance - go.points * 100000;
                                }
                                for(Vertex<Location> v : g.ingoing.get(locations[c2.y][c2.x])) {
                                    v.distance = v.distance - go.points * 100000;
                                }
                            }
                        }
                    }
                }
            }
        }

        AStar<Location> aStar = new AStar<>(g);
        List<String> solution = new ArrayList<String>();
        int xs = game.start.x;
        int ys = game.start.y;
        int xe = game.end.x;
        int ye = game.end.y;

        List<Vertex<Location>> ans =  aStar.computePath(locations[ys][xs], locations[ye][xe]);

        boolean car = true;

        for(Vertex<Location> v : ans) {
            switch (v.type) {
                case "Car":
                    if(!car) {
                        solution.add("SET_PRIMARY_TRANSPORTATION CAR");
                        car = true;
                    }
                    solution.add("TRAVEL " + getDirection(v.from, v.to));
                    break;
                case "Bike":
                case "Boat":
                    if(car) {
                        solution.add("SET_PRIMARY_TRANSPORTATION BIKE");
                        car = false;
                    }
                    solution.add("TRAVEL " + getDirection(v.from, v.to));
                    break;
                case "Flight":
                    solution.add("FLIGHT " + cityLocation.get(v.to).name);
                    break;
                case "Bus":
                    solution.add("BUS " + cityLocation.get(v.to).name);
                    break;
                case "Train":
                    solution.add("TRAIN " + cityLocation.get(v.to).name);
                    break;
            }
        }


		return solution;
	}

	static String getDirection (Location from, Location to) {
	    if(from.x > to.x) {
	        return "WEST";
        } else if(from.y > to.y) {
	        return "NORTH";
        } else if (from.x < to.x) {
	        return "EAST";
        } else {
	        return "SOUTH";
        }
    }
	
	public static void main(String[] args) {
		Api.setApiKey(API_KEY);
		//Api.initGame();
		GameState game = Api.getGame(4952);
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
