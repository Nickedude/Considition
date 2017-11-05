import Lab3Help.*;
import java.util.*;

/**
*	Wrapper class needed for testing.
**/
public class DijkstraStringPath implements Path<String> {
 	
	AStar<String> d;

	@SuppressWarnings("unchecked")
 	public DijkstraStringPath(List<BStop> stops, List<BLineTable> lineTable) {
 		Graph<String> graph = new Graph<>();					//Create the graph
		Iterator<BStop> bstopitr = stops.iterator();			//Iterator for all the stops
		while(bstopitr.hasNext()) {	
			BStop temp = bstopitr.next();						//Get the next BStop
			graph.addNode(temp.getName());						//Add it to the graph
			graph.addEdge(temp.getName(),temp.getName(),0);		//Add an edge from the BStop to itself
		}
		for(BLineTable table: lineTable) {						//For each line table
			BLineStop[] stopsVisited = table.getStops();		//Get the stops on the line
			for(int i = 0; i < stopsVisited.length-1; i++) {	//Iterates through each stop of the line
				graph.addEdge(stopsVisited[i].getName(),stopsVisited[i+1].getName(),stopsVisited[i+1].getTime());	//Add the edge to the graph (from,to,cost)
			}
		}
		d = new AStar<>(graph);	//Create an object of DijkstrasAlgorithm to calculate the paths
	}
	/**
	*	Computes the shortest paths between two nodes in the graph
	**/
	@SuppressWarnings("unchecked")
	public void computePath(String from, String to) {
		d.computePath(from,to);
	}

	/**
	*	Returns the path starting in "from" and ending in "to" as an iterator. The iterator is null 
	*	if no such path exists or if it hasn't been calculated.
	*	@return An iterator representing the path.
	**/
	@SuppressWarnings("unchecked")
	public Iterator<String> getPath() {
		return d.getPath();
	}

	/**
	*	Returns the length of the path starting in "from" and ending in "to".
	*	@return The length of the path. Is infinity if no such path exists or if it hasn't been calculated.
	**/
	public int getPathLength() {
		return d.getPathLength();
	}
}