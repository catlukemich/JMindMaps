package map;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class Button extends Widget {

    public Button(NodeMenu buttons, BufferedImage image) {
        this.image = image;
        this.buttons = buttons;
        this.recalculateSize();
    }

    NodeMenu buttons;
    BufferedImage image;

    boolean is_pressed = false;

    public void paint(Graphics2D graphics) {
        Rectangle bounds = this.calcBounds();
        // Draw background:
        if (this.is_pressed) {
            graphics.setColor(new Color(240, 240, 240));
        }
        else {
            graphics.setColor(Color.WHITE);
        }
        graphics.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);

        // Draw the button image:
        graphics.drawImage(this.image, bounds.x, bounds.y, null);


        graphics.setColor(Color.LIGHT_GRAY);
        // Draw the border:
        graphics.drawRect(bounds.x, bounds.y, bounds.width, bounds.height);

    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    private void recalculateSize() {
        this.size.width  = this.image.getWidth();
        this.size.height = this.image.getHeight();
    }


    public void onMousePress(MouseEvent event) {
        this.is_pressed = true;
    }


    public void onMouseRelease(MouseEvent event) {
        this.is_pressed = false;
    }
}
