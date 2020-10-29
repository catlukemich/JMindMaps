package main.commands;

import main.App;
import map.Connection;
import map.ViewModel;

public class NewConnectionCommand extends Command {

    public NewConnectionCommand(Connection connection) {
        this.connection = connection;
    }

    Connection connection;


    public void undo() {
        ViewModel model = App.instance.map_view.getModel();
        model.removeConnection(this.connection);
        App.instance.map_view.repaint();
    }

    public void redo() {
        ViewModel model = App.instance.map_view.getModel();
        model.addConnection(this.connection);
        App.instance.map_view.repaint();
    }
}
