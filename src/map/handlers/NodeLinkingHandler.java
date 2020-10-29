package map.handlers;

import main.App;
import main.commands.NewConnectionCommand;
import map.Connection;
import map.Node;
import map.View;
import map.ViewModel;
import utils.MathUtils;

import java.awt.*;
import java.awt.event.MouseEvent;

public class NodeLinkingHandler {

    public void mouseDragged(View view, MouseEvent event) {

        // New connection creation handling:
        if(view.linking_state.link_start_node != null) {
            Point mouse = new Point(event.getX(), event.getY());
            view.linking_state.link_current_position = mouse;

            ViewModel model = view.getModel();
            for(Node node : model.nodes) {
                if (node.containsPoint(mouse) && node != view.linking_state.link_start_node) {
                    Node end_node = node;
                    view.linking_state.link_current_position = view.positionToScreen(end_node.getPosition());
                    view.linking_state.link_end_node = node;
                    break;
                }
            }
            Point start_position   = view.linking_state.link_start_node.getPosition();
            Point current_position = view.screenToPosition(event.getPoint());
            float distance = MathUtils.distance(start_position, current_position);
            if (distance < View.NEW_NODE_DISTANCE) {
                view.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            else {
                view.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
            }
            view.repaint();
        }
    }


    public void mouseReleased(View view, MouseEvent event) {
        ViewModel model = view.getModel();

        // Restore default cursor (which might have changed in mouse move and drag events):
        view.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

        if(view.linking_state.link_start_node != null && view.linking_state.link_end_node != null) {
            // Check if a connection already exists:
            boolean connection_exists = false;
            for(Connection connection : model.connections) {
                if(connection.node_1 == view.linking_state.link_start_node && connection.node_2 == view.linking_state.link_end_node) {
                    connection_exists = true;
                }
                if(connection.node_1 == view.linking_state.link_end_node && connection.node_2 == view.linking_state.link_start_node) {
                    connection_exists = true;
                }
            }
            if (!connection_exists) {
                // Make the connection:
                Connection new_connection = new Connection(view.linking_state.link_start_node, view.linking_state.link_end_node);
                new_connection.setView(view);
                App.instance.history.addCommand(new NewConnectionCommand(new_connection));
                model.addConnection(new_connection);
            }
            view.linking_state.link_start_node = null;
            view.linking_state.link_end_node = null;
        }
        else if (view.linking_state.link_start_node != null) {
            // If the linking was occuring and the distance from start node is greater than
            // the new node connection distance - create a new node and connect it to the
            // node from which the connection beginned:
            Point start_position   = view.linking_state.link_start_node.getPosition();
            Point current_position = view.screenToPosition(event.getPoint());
            float distance = MathUtils.distance(start_position, current_position);

            if (distance > View.NEW_NODE_DISTANCE) {
                Node node = view.insertNewNode(current_position);
                // Add the connection:
                Connection connection = new Connection(view.linking_state.link_start_node, node);
                connection.setView(view);
                model.addConnection(connection);
                view.setSelectedNode(node, true);
                if (view.focus_state.focus_object != null) view.focus_state.focus_object.loseFocus();
                view.focus_state.focus_object = node;
                node.setSelected(true);
                node.focusOnTitleEntry();

                // Update the nodes panel with new node:
                App.instance.side_panel.updateNodesPanel();
            }
        }
    }
}
