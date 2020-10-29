package main.commands;

import main.App;
import map.Node;
import map.ViewModel;

public class NewNodeCommand extends Command {

    public NewNodeCommand(Node node) {
        this.node = node;
    }

    Node node;


    public void undo() {
        ViewModel model = App.instance.map_view.getModel();
        model.removeNode(this.node);
        App.instance.map_view.repaint();
    }

    public void redo() {
        ViewModel model = App.instance.map_view.getModel();
        model.addNode(this.node);
        App.instance.map_view.repaint();
    }

}
