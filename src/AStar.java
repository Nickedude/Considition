import java.util.*;

public class AStar<E> {    // :-(

    final Graph<E> graph;
    HashMap<E,Vertex<E>> predecessor;
    HashMap<E,Integer> costs;
    HashSet<E> visited;
    public List<Vertex<E>> path;

    public AStar (Graph g) {
        graph = g;
    }

    public void init(E from) {
        costs = new HashMap<>();
        visited = new HashSet<>();
        predecessor = new HashMap<>();

        for(E n : graph.nodes.keySet()) {                                 //Init costs to inf
            costs.put(n, Integer.MAX_VALUE);
        }
        costs.put(from, 0);                                 //Cost for from is 0
        predecessor.put(from, null);   //Predecessor to from is from
    }

    public List<Vertex<E>> computePath(E from, E to) {
        init(from);
        PriorityQueue<Node> q = new PriorityQueue<>(new NodeComparator());
        q.add(new Node(from, 0));
        visited.add(from);
        while(!q.isEmpty()) {
            Node n = q.poll();               //Get the node we're processing

            if(!visited.contains(n.node)) {
                visited.add(n.node);

                for(Vertex<E> nv : graph.nodes.get(n.node)) {
                    E e = nv.getTo();

                    if(!visited.contains(e) && costs.get(e) > costs.get(n.node) + nv.cost) {
                        costs.put(e, costs.get(n.node)+nv.cost);
                        predecessor.put(e,nv);
                        q.add(new Node(e, costs.get(e)));
                    }
                }
            }
        }
        List<Vertex<E>> ans = new LinkedList<>();

        Vertex<E> v = predecessor.get(to);
        ans.add(v);
        while((v = predecessor.get(v.from)) != null) {
            ans.add(v);
        }

        Collections.reverse(ans);
        path = ans;
        return ans;
    }

    public Iterator<E> getPath() {
        List<E> list = new ArrayList<>();

        for(Vertex<E> v : path) {
            list.add(v.to);
        }

        return list.iterator();
    }

    public int getPathLength() {
        if(path != null) {
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
        final E node;

        Node (E n, int c) {
            cost = c;
            node = n;
        }
    }

    public class NodeComparator implements Comparator<Node> {

        public int compare(Node a, Node b) {
            return 0;
        }
    }
}
