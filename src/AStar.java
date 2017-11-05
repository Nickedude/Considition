import java.util.*;

class AStar<E> {    // :-(

    private final Graph<E> graph;
    private HashMap<E, Vertex<E>> predecessor;
    private HashMap<E, Integer> costs;
    private HashSet<E> visited;
    private List<Vertex<E>> path;

    AStar(Graph<E> g) {
        graph = g;
    }

    private void init(E from) {
        costs = new HashMap<>();
        visited = new HashSet<>();
        predecessor = new HashMap<>();

        for (E n : graph.nodes.keySet()) {  //Init costs to inf
            costs.put(n, Integer.MAX_VALUE);
            predecessor.put(n, null);
        }

        costs.put(from, 0);            //Cost for from is 0
        predecessor.put(from, null);   //Predecessor to from is from
    }

    List<Vertex<E>> computePath(E from, E to) {
        init(from);
        PriorityQueue<Node> q = new PriorityQueue<>(new NodeComparator());
        q.add(new Node(from, 0, 0, 0, 0));
        while (!q.isEmpty()) {
            Node wrapper = q.poll();            // Get the data of the wrapped node
            E n = wrapper.node;                 // Get the node we're processing
            if (n == to)
                break;

            if (!visited.contains(n)) {
                visited.add(n);

                for (Vertex<E> nv : graph.nodes.get(n)) {
                    E e = nv.getTo();
                                                // Needs to take into account waiting in score comparison.
                    if (!visited.contains(e) && costs.get(e) > costs.get(n) + (nv.cost)) {
                        costs.put(e, costs.get(n) + nv.cost);
                        predecessor.put(e, nv);
                        q.add(new Node(e, costs.get(e), 0, wrapper.time + wrapper.waiting + wrapper.cost, 0));
                    }
                }
            }
        }
        List<Vertex<E>> ans = new LinkedList<>();

        Vertex<E> v = predecessor.get(to);

        while (v != null) {
            ans.add(v);
            v = predecessor.get(v.from);
        }

        ans.add(new Vertex<>(from, from, 0));
        Collections.reverse(ans);
        path = ans;
        return ans;
    }

    Iterator<E> getPath() {
        List<E> list = new ArrayList<>();

        for (Vertex<E> v : path) {
            list.add(v.to);
        }

        return list.iterator();
    }

    int getPathLength() {
        if (path != null) {
            int i = 0;
            for (Vertex<E> v : path) {
                i += v.cost;
            }
            return i;
        }
        return -1;
    }

    class Node {
        final int cost;
        final int distance;
        final int time;
        final int waiting;

        final E node;

        Node(E n, int c, int d, int t, int w) {
            cost = c;
            node = n;
            distance = d;
            time = t;
            waiting = w;
        }
    }

    public class NodeComparator implements Comparator<Node> {
        public int compare(Node a, Node b) {
            return (a.cost + a.distance + a.waiting) - (b.cost + b.distance + b.waiting);
        }
    }
}
