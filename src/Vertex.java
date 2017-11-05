public class Vertex <E> {
    E from;
    E to;
    Integer cost;

    public Vertex (E f, E t, int c) {
        from = f;
        to = t;
        cost = c;
    }

    public E getTo() {
        return to;
    }

}
