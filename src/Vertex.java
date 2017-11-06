public class Vertex <E> {
    E from;
    E to;
    int distance;

    public Vertex (E f, E t, int d) {
        from = f;
        to = t;
        distance = d;
    }

    public E getTo() {
        return to;
    }

}
