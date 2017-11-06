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

    public List<Vertex<E>> computePath(E from, E to) {
        init(from);
        PriorityQueue<Node> q = new PriorityQueue<>(new NodeComparator());
        q.add(new Node(from, 0, 0));
        while (!q.isEmpty()) {
            Node wrapper = q.poll();            // Get the data of the wrapped node
            E n = wrapper.node;                 // Get the node we're processing
            if (n == to)
                break;

            if (!visited.contains(n)) {
                visited.add(n);

                for (Vertex<E> nv : graph.nodes.get(n)) {
                    E e = nv.getTo();

                    int wait = graph.getWait(nv, wrapper.time);
                    int time = wrapper.time + wait + nv.distance;
                    int cost = costs.get(n) + nv.distance + wait;

                    if (!visited.contains(e) && costs.get(e) > cost) {
                        costs.put(e, cost);
                        predecessor.put(e, nv);
                        q.add(new Node(e, cost, time));
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

    public Iterator<E> getPath() {
        List<E> list = new ArrayList<>();

        for (Vertex<E> v : path) {
            list.add(v.to);
        }

        return list.iterator();
    }

    public int getPathLength() {
        if (path != null) {
            int i = 0;
            for (Vertex<E> v : path) {
                i += v.distance;
            }
            return i;
        }
        return -1;
    }

    public class Node {
        final int cost; // Current shortest path to this node.
        final int time; // Time that you arrive at this node.
        final E node;

        Node(E n, int c, int t) {
            node = n;
            cost = c;
            time = t;
        }
    }

    public class NodeComparator implements Comparator<Node> {
        public int compare(Node a, Node b) {
            return a.cost - b.cost;
        }
    }
}
