package map.handlers;

import main.App;
import main.commands.NodeDragCommand;
import map.Node;
import map.View;
import map.ViewModel;

import java.awt.*;
import java.awt.event.MouseEvent;

public class NodeDragHandler {
    Node drag_node  = null;
    Point drag_start_point = new Point();
    Point drag_anchor_point = new Point();

    Point drag_start_position = new Point();

    public void mousePressed(View view, MouseEvent event) {
        Point mouse = new Point(event.getX(), event.getY());
        if (event.getButton() == 1) {
            Node drag_node = null;
            ViewModel model = view.getModel();

            // Else start dragging node under the cursor:
            for (Node node : model.nodes) {
                if (node.containsPoint(mouse)) {
                    drag_node = node;
                    break;
                }
            }
            if (drag_node != null) {
                // Set the drag node and the point where drag started on screen:
                this.drag_start_position = new Point(drag_node.getPosition());
                this.drag_start_point = new Point(mouse);
                this.drag_node = drag_node;
                this.drag_anchor_point = mouse;
            }
        }
    }


    public void mouseDraged(View view, MouseEvent event) {
        // Node dragging logic:
        if (this.drag_node != null) {
            Point mouse = new Point(event.getX(), event.getY());
            Point node_position = new Point(this.drag_node.getPosition());
            int dx = mouse.x - this.drag_anchor_point.x;
            int dy = mouse.y - this.drag_anchor_point.y;
            node_position.x += dx;
            node_position.y += dy;
            this.drag_node.setPosition(node_position);
            view.buttons_menu.updatePositions();
            this.drag_anchor_point = mouse;
            view.buttons_menu.setVisible(false);
            view.repaint();
        }
    }

    public void mouseReleased(View view, MouseEvent event) {
        if (event.getButton() == 1 && this.drag_node != null) {
            Point mouse = new Point(event.getX(), event.getY());
            Point drag_end_position = new Point(this.drag_node.getPosition());
            int dx = mouse.x - this.drag_anchor_point.x;
            int dy = mouse.y - this.drag_anchor_point.y;
            drag_end_position.x += dx;
            drag_end_position.y += dy;

            App.instance.history.addCommand(new NodeDragCommand(this.drag_node, this.drag_start_position, drag_end_position));
            this.drag_node = null;
            view.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

}
