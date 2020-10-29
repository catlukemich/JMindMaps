package map;

import java.awt.*;

public class LinkingState {
    public Node  link_start_node;
    public Point link_current_position = null;
    public Node  link_end_node;

    public void setStartNode(Node node) {
        this.link_start_node = node;
    }
}
