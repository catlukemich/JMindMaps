package map.handlers;

import map.Node;
import map.View;
import map.ViewModel;

import java.awt.*;
import java.awt.event.MouseEvent;

public class NodeMenuHandler {


    public void mousePressed(View view, MouseEvent event) {
        if (event.getButton() == 1) {
            // Left mouse button pressed:
            Point mouse = new Point(event.getX(), event.getY());
            if (view.buttons_menu.containsPoint(mouse) && view.buttons_menu.isVisible()) {
                // If the press occured on the node buttons, forward it:
                view.buttons_menu.onMousePress(event);
            }
        }
    }

    public void mouseMoved(View view, MouseEvent event) {
        Point mouse = new Point(event.getX(), event.getY());
        Node new_hover_node = null;

        ViewModel model = view.getModel();

        for (Node node : model.nodes) {
            if (node.containsPoint(mouse)) {
                new_hover_node = node;
                break;
            }
        }

        if(new_hover_node != view.hover_state.hover_node) {
            if (view.hover_state.hover_node != null) {
                view.hover_state.hover_node.onMouseOut(event);
            }
            if (new_hover_node != null) {
                new_hover_node.onMouseOver(event);
                view.hover_state.hover_node = new_hover_node;
                view.buttons_menu.setNode(new_hover_node);
                view.repaint();
            }

            if (new_hover_node != null) {
                view.buttons_menu.setVisible(true);
            }
            else {
                view.buttons_menu.setVisible(false);
            }
        }

        if(view.hover_state.hover_node != null) {
            if (view.buttons_menu.containsPoint(mouse) || view.hover_state.hover_node.containsPoint(mouse)) {
                view.buttons_menu.setVisible(true);
                view.buttons_menu.onMouseMove(event);
            }
            else {
                view.buttons_menu.setVisible(false);
            }
        }
    }
}
