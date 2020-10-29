package main.commands;

import main.App;
import main.AppImage;
import map.Node;

import java.awt.image.BufferedImage;

public class ImageChangeCommand extends Command {

    public ImageChangeCommand(Node node, AppImage previous_image, AppImage current_image) {
        this.node = node;
        this.previous_image = previous_image;
        this.current_image  = current_image;
    }

    Node node;
    AppImage previous_image;
    AppImage current_image;

    public void undo() {
        if (this.previous_image == null) {
            this.node.removeImage();
        }
        else {
            node.setImage(this.previous_image);
        }
        this.node.recalculateSize();
        this.node.updatePositions();
        App.instance.map_view.repaint();
    }

    public void redo() {
        this.node.setImage(this.current_image);
        this.node.recalculateSize();
        this.node.updatePositions();
        App.instance.map_view.repaint();
    }
}
