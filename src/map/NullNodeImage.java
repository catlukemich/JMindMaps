package map;

import java.awt.*;

public class NullNodeImage extends NodeImage {

    public NullNodeImage(Node node) {
        super(node);
    }

    public void recalculateSize() {
    }

    int getWidth() {
        return 0;
    }

    int getHeight() {
        return 0;
    }


    public void paint(Graphics2D graphics) {}
}
