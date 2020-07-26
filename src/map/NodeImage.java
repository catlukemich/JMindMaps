package map;

import main.AppImage;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class NodeImage extends Widget {

    public NodeImage(Node node) {
        this.node = node;
    }

    Node node;
    AppImage image;


    public void setImage(AppImage image){
        this.image = image;
        this.recalculateSize();
        this.view.repaint();
    }


    public void recalculateSize() {
        if (this.image != null) {
            this.size.width  = this.image.original.getWidth();
            this.size.height = this.image.original.getHeight();
        }
        else {
            this.size.width  = 0;
            this.size.height = 0;
        }
    }


    public void paint(Graphics2D graphics) {
        Rectangle bounds = this.calcBounds();
        if (this.image != null) {
            graphics.drawImage(this.image.original, bounds.x, bounds.y, null);
        }
    }

    public void onClick(MouseEvent event) {

    }

    public void gainFocus() {

    }

    public void loseFocus() {

    }

    public boolean keyPressed(KeyEvent event) {
        return false;
    }

    public boolean keyReleased(KeyEvent event) {
        return false;
    }

    public boolean keyTyped(KeyEvent event) {
        return false;
    }
}
