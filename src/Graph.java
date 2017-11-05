import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Graph<E> {
    HashMap<E,List<Vertex<E>>> nodes;

    public Graph () {
        nodes = new HashMap<>();
    }

    public void addNode (E n) {
        if(n != null && !nodes.containsKey(n)) {
            nodes.put(n, new ArrayList<>());
        }
    }

    public void addEdge (E from, E to, int cost) {
        if(nodes.containsKey(from) && nodes.containsKey(to)) {
            nodes.get(from).add(new Vertex<>(from, to, cost));
        }
    }

    public boolean contains (E node) {
        return nodes.containsKey(node);
    }

    public HashMap<E, List<Vertex<E>>> getGraph () {
        return nodes;
    }
}
