package map.handlers;

import main.App;
import main.commands.RemoveConnectionCommand;
import main.commands.RemoveNodeCommand;
import map.*;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;

public class ViewKeyHandler {

    public void keyPressed(View view, KeyEvent event) {
        ViewModel model = view.getModel();

        if(view.focus_state.focus_object != null && view.focus_state.focus_object instanceof Node) {
            Node focus_node = (Node) view.focus_state.focus_object;

            // Forward the event to the focus node, and check if it was consumed:
            boolean consumed = focus_node.keyPressed(event);

            // If the event wasn't consumed - the active node, not it's controls received the event,
            // so process the event locally:
            if (!consumed && event.getKeyCode() == 127) {
                // Remove a node if delete key was pressed:
                model.removeNode(focus_node);
                // Remove connections that were associated with the removed node and remove it:

                Iterator<Connection> iterator = model.connections.iterator();
                ArrayList<Connection> removed_connections = new ArrayList<>();
                while(iterator.hasNext()) {
                    Connection connection = iterator.next();
                    if (connection.node_1 == focus_node || connection.node_2 == focus_node) {
                        removed_connections.add(connection);
                        iterator.remove();
                    }
                }

                App.instance.history.addCommand(new RemoveNodeCommand(focus_node, removed_connections));

                Node node = view.buttons_menu.getNode();
                if (node == focus_node) {
                    view.buttons_menu.setVisible(false);
                }

                view.hover_state.hover_node = null;
                view.focus_state.focus_object = null;

                App.instance.side_panel.updateNodesPanel();

                view.repaint();
            }
        }
        else if (view.focus_state.focus_object != null && view.focus_state.focus_object instanceof Connection) {
            // Remove the connection between two nodes if delete key was pressed:
            if (event.getKeyCode() == 127) {
                Connection connection = (Connection) view.focus_state.focus_object;
                App.instance.history.addCommand(new RemoveConnectionCommand(connection));
                model.removeConnection(connection);
                view.repaint();
            }
        }
    }

    public void keyReleased(View view, KeyEvent event) {
        if (view.focus_state.focus_object != null) {
            if (view.focus_state.focus_object instanceof Node) {
                Node focus_node = (Node) view.focus_state.focus_object;
                focus_node.keyReleased(event);
            }
        }
    }

    public void keyTyped(View view, KeyEvent event) {
        Focusable focus_object = view.focus_state.focus_object;
        if (focus_object != null) {
            if (focus_object instanceof Node) {
                Node focus_node = (Node) focus_object;
                boolean consumed = focus_node.keyTyped(event);
            }
        }
    }

}
