import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;

class Graph<E> {
    HashMap<E,List<Vertex<E>>> nodes;
    HashMap<E, SortedSet<Integer>> timetable;

    Graph () {
        nodes = new HashMap<>();
    }

    void addNode (E n) {
        if(n != null && !nodes.containsKey(n)) {
            nodes.put(n, new ArrayList<>());
        }
    }

    void addEdge (E from, E to, int cost) {
        if(nodes.containsKey(from) && nodes.containsKey(to)) {
            nodes.get(from).add(new Vertex<>(from, to, cost));
        }
    }

    boolean contains (E node) {
        return nodes.containsKey(node);
    }

    int getWait(Vertex<E> v,  int time) {
        return 0;
    }

    int getDistance(E from, E to) {
        return 0;
    }

}
