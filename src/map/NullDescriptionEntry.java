package map;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

public class NullDescriptionEntry extends DescriptionEntry {
    public NullDescriptionEntry(Node node) {
        super(node);
    }

    public void paint(Graphics2D graphics) {}

    public void setText(String text) {}

    public void recalculateSize() {}

    public int getLineHeight() {
        return 0;
    }

    int getWidth() {
        return 0;
    }

    int getHeight() {
        return 0;
    }

    public void onClick(MouseEvent event) {}

    public boolean keyPressed(KeyEvent event) {
        return false;
    }


    public boolean keyReleased(KeyEvent event) {
        return false;
    }


    public boolean keyTyped(KeyEvent event) {
        return false;
    }

    protected Point getCaretPosition() {
        return null;
    }
}
