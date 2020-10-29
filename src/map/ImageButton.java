package map;

import main.App;
import main.AppImage;
import main.ImageRepository;
import utils.Resources;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class ImageButton extends Button {

    // The add image and remove image - images:
    static private BufferedImage add_image    = Resources.loadImage("/add_image_button.png");
    static private BufferedImage remove_image = Resources.loadImage("/remove_image_button.png");


    public ImageButton(NodeMenu buttons) {
        super(buttons, add_image);
    }

    public void onClick(MouseEvent event) {
        super.onClick(event);

        if (this.buttons.node.getImage() == null) {
            App.instance.side_panel.setNode(this.buttons.node);

            AppImage image = ImageRepository.browseForImage();
            if (image != null) {
                App.instance.side_panel.updateImagesPanel();

                // Update the node with the image:
                this.buttons.node.addImage();
                this.buttons.node.setImage(image);
                this.buttons.updatePositions();

                // Show the node information in the node panel:
                App.instance.side_panel.displayNodePanel();
                App.instance.side_panel.updateNodePanel();

                this.update();
            }
        }
        else {
            App.instance.side_panel.setNode(this.buttons.node);

            this.buttons.node.removeImage();
            this.buttons.node.updatePositions();
            this.buttons.updatePositions();

            App.instance.side_panel.updateNodePanel();

            this.update();
        }
    }


    public void update() {
        if (this.buttons.node.getImage() == null) {
            this.setImage(add_image);
        }
        else {
            this.setImage(remove_image);
        }
    }
}
