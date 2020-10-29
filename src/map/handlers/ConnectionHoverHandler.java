package map.handlers;

import map.Connection;
import map.View;
import map.ViewModel;

import java.awt.*;
import java.awt.event.MouseEvent;

public class ConnectionHoverHandler {

    public void mouseMoved(View view, MouseEvent event) {
        Point mouse = new Point(event.getX(), event.getY());
        ViewModel model = view.getModel();

        if (view.hover_state.hover_node == null || !(view.buttons_menu.containsPoint(mouse) || view.hover_state.hover_node.containsPoint(mouse))) {
            Connection new_hover_connection = null;
            for(Connection connection : model.connections) {
                Point position = view.screenToPosition(mouse);
                boolean is_near = connection.isPointNear(position);
                if(is_near) {
                    new_hover_connection = connection;
                }
            }

            if (new_hover_connection != view.hover_state.hover_connection) {
                if (view.hover_state.hover_connection != null) {
                    view.hover_state.hover_connection.onMouseOut();
                }

                if(new_hover_connection != null) {
                    new_hover_connection.onMouseOver();
                }
                view.hover_state.hover_connection = new_hover_connection;
            }
        }

        view.repaint();
    }
}
