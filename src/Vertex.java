public class Vertex <E> {
    E from;
    E to;
    int distance;
    String type;

    public Vertex (E f, E t, int d, String type) {
        from = f;
        to = t;
        distance = d;
        this.type = type;
    }

    public E getTo() {
        return to;
    }

}
