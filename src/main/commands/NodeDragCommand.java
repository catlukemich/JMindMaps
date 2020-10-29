package main.commands;

import main.App;
import map.Node;

import java.awt.*;

public class NodeDragCommand extends Command {

    public NodeDragCommand(Node node, Point start_point, Point end_point) {
        this.node = node;
        this.start_point = start_point;
        this.end_point = end_point;
    }

    Node node;
    Point start_point;
    Point end_point;


    public void undo() {
        this.node.setPosition(this.start_point);
        App.instance.map_view.repaint();
    }


    public void redo() {
        this.node.setPosition(this.end_point);
        App.instance.map_view.repaint();
    }
}


