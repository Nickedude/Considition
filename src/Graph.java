import java.util.HashSet;

public class Graph<E> {
    HashSet<Node<E>> nodes;

    public Graph () {
        nodes = new HashSet<>();
    }

    public void addNode (Node<E> n) {
        if(n != null) {
            nodes.add(n);
        }
    }
}
