import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class Graph<E> {
    HashMap<E,List<Vertex<E>>> nodes;
    HashMap<E, List> timetable;

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

    public int getWait(E from, E to,  int time) {
        return 0;
    }

    HashMap<E, List<Vertex<E>>> getGraph () {
        return nodes;
    }
}
