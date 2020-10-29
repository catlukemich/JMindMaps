package map.handlers;

import map.View;

import java.awt.*;
import java.awt.event.MouseEvent;

public class ViewScrollHandler {

    private boolean is_scrolling;
    Point drag_point;

    public void mousePressed(View view, MouseEvent event) {
        if (event.getButton() == 2) {
            // Middle mouse button pressed:
            this.drag_point = new Point(event.getX(), event.getY());
            this.is_scrolling = true;
        }
    }

    public boolean mouseDragged(View view, MouseEvent event) {
        // Scrolling handling:
        if(this.is_scrolling) {
            int dx = event.getX() - this.drag_point.x;
            int dy = event.getY() - this.drag_point.y;

            view.position_state.center.x -= dx;
            view.position_state.center.y -= dy;

            this.drag_point = new Point(event.getX(), event.getY());
            view.repaint();
            return true;
        }
        else {
            return false;
        }
    }

    public void mouseReleased(View view, MouseEvent event) {
        if (event.getButton() == 2) {
            this.is_scrolling = false;
        }
    }
}
