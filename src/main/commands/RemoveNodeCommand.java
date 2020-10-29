package main.commands;

import main.App;
import map.Connection;
import map.Node;
import map.ViewModel;

import java.util.ArrayList;

public class RemoveNodeCommand extends Command {

    public RemoveNodeCommand(Node node, ArrayList<Connection> connections) {
        this.node = node;
        this.connections = connections;
    }

    Node node;
    ArrayList<Connection> connections;


    public void undo() {
        ViewModel model = App.instance.map_view.getModel();
        model.addNode(this.node);
        for (Connection connection: this.connections) {
            model.addConnection(connection);
        }

        App.instance.map_view.repaint();
    }

    public void redo() {
        ViewModel model = App.instance.map_view.getModel();
        model.removeNode(this.node);
        for (Connection connection: this.connections) {
            model.removeConnection(connection);
        }

        App.instance.map_view.repaint();
    }
}
