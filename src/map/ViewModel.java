package map;

import java.util.ArrayList;

public class ViewModel {
    public ArrayList<Connection> connections = new ArrayList<>();
    public ArrayList<Node> nodes = new ArrayList<>();


    public void clear() {
        this.connections.clear();
        this.nodes.clear();
    }

    public void addNode(Node node) {
        this.nodes.add(node);

    }

    public void removeNode(Node node) {
        this.nodes.remove(node);
    }

    public void addConnection(Connection connection) {
        this.connections.add(connection);
    }

    public void removeConnection(Connection connection) {
        this.connections.remove(connection);
    }

    public ArrayList<Connection> getConnections() {
        return this.connections;
    }

    public ArrayList<Node> getNodes() {
        return this.nodes;
    }
}
