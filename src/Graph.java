import java.util.*;

class Graph<E> {
    HashMap<E,List<Vertex<E>>> nodes;
    HashMap<Vertex<E>, TreeSet<Integer>> timetable;

    Graph () {
        nodes = new HashMap<>();
        timetable = new HashMap<>();
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

    void addDepartures(Vertex<E> v, Collection<Integer> times) {
        timetable.putIfAbsent(v, new TreeSet<>());
        timetable.get(v).addAll(times);
    }

    int getWait(Vertex<E> v,  int time) {
        if(timetable.containsKey(v)) {
            return timetable.get(v).ceiling(time);
        }
        return 0;
    }

    int getDistance(E from, E to) {
        return 0;
    }

    boolean contains (E node) {
        return nodes.containsKey(node);
    }

}
