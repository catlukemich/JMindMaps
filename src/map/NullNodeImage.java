package map;

public class NullNodeImage extends NodeImage {

    public NullNodeImage(Node node) {
        super(node);
    }

    int getWidth() {
        return 0;
    }

    int getHeight() {
        return 0;
    }
}
