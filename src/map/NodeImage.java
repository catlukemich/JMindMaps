package map;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class NodeImage extends Widget {

    public NodeImage(Node node) {
        this.node = node;
    }

    Node node;
    BufferedImage image;


    public void setImage(BufferedImage image){
        this.image = image;
        this.size.width  = image.getWidth();
        this.size.height = image.getHeight();
        this.view.repaint();
    }



    public void paint(Graphics2D graphics) {
        Rectangle bounds = this.calcBounds();
        graphics.drawImage(this.image, bounds.x, bounds.y, null);
    }

    public void onClick(MouseEvent event) {

    }

    public void gainFocus() {

    }

    public void loseFocus() {

    }

    public void keyPressed(KeyEvent event) {

    }

    public void keyReleased(KeyEvent event) {

    }

    public void keyTyped(KeyEvent event) {

    }
}
