public class Vertex <E> {
    Node<E> from;
    Node<E> to;
    Integer cost;

    public Vertex (Node<E> f, Node<E> t, int c) {
        from = f;
        to = t;
        cost = c;
    }

    public Node<E> getTo() {
        return to;
    }

}
