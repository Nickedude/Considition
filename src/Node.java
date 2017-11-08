public class Node <E> {
    final int cost; // Current lowest cost to this node.
    final int time; // Time that you arrive at this node.
    final int distance; // Distance to goal.
    final E node;

    Node(E n, int c, int d, int t) {
        node = n;
        cost = c;
        distance = d;
        time = t;
    }
}