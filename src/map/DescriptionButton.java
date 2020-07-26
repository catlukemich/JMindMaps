package map;

import main.App;
import utils.Debug;
import utils.Resources;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class DescriptionButton extends Button {

    static private BufferedImage add_description_image = Resources.loadImage("/add_description_button.png");
    static private BufferedImage remove_description_image = Resources.loadImage("/remove_description_button.png");


    public DescriptionButton(NodeButtons buttons) {
        super(buttons, add_description_image);
    }


    public void onClick(MouseEvent event) {
        if (!this.buttons.node.hasDescription()) {
            this.buttons.node.addDescription();
            this.buttons.updatePositions();
        }
        else {
            this.buttons.node.removeDescription();
            this.buttons.updatePositions();
        }

        App.instance.side_panel.updateNodePanel();
    }
}
