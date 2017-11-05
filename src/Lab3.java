	import Lab3Help.*;
import java.util.*;
import java.io.*;

public class Lab3 {
	// Hållplatser, linjer, start, stopp
	@SuppressWarnings("unchecked")
	public static void main (String[] args) {
		for(String s : args) {
			System.out.println(s);
		}
		try {
			Lab3File reader = new Lab3File();						//An object of Lab3File for reading the files
			List<BStop> stops = reader.readStops(args[0]);			//Read the bus stops and save them in a list
			Graph<String> graph = new Graph<>();					//Create the graph 
			Iterator<BStop> bstopitr = stops.iterator();			//Iterator for all the stops
			while(bstopitr.hasNext()) {	
				BStop temp = bstopitr.next();						//Get the next BStop
				graph.addNode(temp.getName());						//Add it to the graph
				graph.addEdge(temp.getName(),temp.getName(),0);		//Add an edge from the BStop to itself
			}

			List<BLineTable> lineTable = reader.readLines(args[1]);	//Read the line tables to a list
			for(BLineTable table: lineTable) {						//For each line table
				BLineStop[] stopsVisited = table.getStops();		//Get the stops on the line
				for(int i = 0; i < stopsVisited.length-1; i++) {	//Iterates through each stop of the line
					graph.addEdge(stopsVisited[i].getName(),stopsVisited[i+1].getName(),stopsVisited[i+1].getTime());	//Add the edge to the graph (from,to,cost)
				}
			}

			AStar<String> d = new AStar<>(graph);	//Create an object of DijkstrasAlgorithm to calculate the paths
			if(!graph.contains(args[2]) || !graph.contains(args[3])) {
				System.out.println("Atleast one of the stops doesn't exist!");
				return;
			}
			d.computePath(args[2],args[3]);							//Calculate the length of the path between the stops
			int pathLength = d.getPathLength();
			if(pathLength == Integer.MAX_VALUE) 								//No path exists between the stops
				System.out.println("There is no path between these stops.");
			else {
				System.out.println(pathLength);						//Print the length of the path
				Iterator<String> itr = d.getPath();					//Get the path
				while(itr.hasNext()) {
					System.out.println(itr.next());					//Print the next stop
				}
			}

		}
		catch(MalformedData e) {
			System.out.println("Data is malformed.");
		}
		catch(IOException e) {
			System.out.println("IO exception occured.");
		}

	}
}