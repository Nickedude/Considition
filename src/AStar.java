import java.util.*;

public class AStar<E> {    // :-(

    final Graph<E> graph;
    HashMap<Node<E>,Vertex<E>> predecessor;
    HashMap<Node<E>,Integer> costs;
    HashSet<Node<E>> visited;

    public AStar (Graph g) {
        graph = g;
    }

    public void init(Node from) {
        costs = new HashMap<>();
        visited = new HashSet<>();
        predecessor = new HashMap<>();

        for(Node n : graph.nodes) {                                 //Init costs to inf
            costs.put(n, Integer.MAX_VALUE);
        }
        costs.put(from, 0);                                 //Cost for from is 0
        predecessor.put(from, null);   //Predecessor to from is from
    }

    public List<Vertex<E>> shortestDijkstraPath(Node<E> from, Node<E> to) {
        init(from);
        PriorityQueue<Node<E>> q = new PriorityQueue<>(new NodeComparator());
        q.add(from);
        visited.add(from);
        while(!q.isEmpty()) {
            Node<E> n = q.poll();               //Get the node we're processing

            if(!visited.contains(n)) {
                visited.add(n);

                for(Vertex<E> nv : n.neighbours) {
                    Node<E> w = nv.getTo();

                    if(!visited.contains(w) && costs.get(w) > costs.get(n) + nv.cost) {
                        costs.put(w, costs.get(n)+nv.cost);
                        predecessor.put(w,nv);
                        q.add(w);
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
        return ans;
    }

    public class NodeComparator implements Comparator<Node<E>> {

        public int compare(Node<E> a, Node<E> b) {
            return 0;
        }
    }
}
