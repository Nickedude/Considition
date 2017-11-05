import java.util.Comparator;
import java.util.HashSet;

public class Node <E> {
    HashSet<Vertex<E>> neighbours;

    public Node () {
        neighbours = new HashSet<>();
    }

    public Node (Node n, int costToN, int costFromN) {
        neighbours = new HashSet<>();
        addNeighbour(n, costToN);
        n.addNeighbour(this, costFromN);
    }

    public void addNeighbour(Node n, int costToN) {
        if(n != null && costToN > -1) {
            neighbours.add(new Vertex(this, n, costToN));
        }
    }


}
