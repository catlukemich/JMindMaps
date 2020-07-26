package map;

import main.App;
import main.AppImage;
import main.ImageRepository;
import utils.FileUtils;
import utils.Resources;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class ImageButton extends Button {

    // The add image and remove image - images:
    static private BufferedImage add_image    = Resources.loadImage("/add_image_button.png");
    static private BufferedImage remove_image = Resources.loadImage("/remove_image_button.png");


    public ImageButton(NodeButtons buttons) {
        super(buttons, add_image);
    }

    public void onClick(MouseEvent event) {
        super.onClick(event);

        if (this.buttons.node.getImage() == null) {
            App.instance.side_panel.setNode(this.buttons.node);

            // If there is no image ask the user to choose image from file:
            JFileChooser file_chooser = new JFileChooser();
            file_chooser.setAcceptAllFileFilterUsed(false);
            file_chooser.addChoosableFileFilter(new FileFilter() {
                public boolean accept(File file) {
                    if (file.isDirectory()) return true;
                    String extension = FileUtils.getFileExtension(file);
                    return extension.equals("png");
                }

                public String getDescription() {
                    return "Portable Network Graphics (PNG)";
                }
            });
            int ret_value = file_chooser.showOpenDialog(App.instance);
            if (ret_value == JFileChooser.APPROVE_OPTION) {
                // Read the selected file and do scaling on it:
                File file = file_chooser.getSelectedFile();
                String path = file.getAbsolutePath();

                // Create the image and add it to cache:
                AppImage app_image = Resources.loadAppImage(path);
                ImageRepository.addImage(app_image);
                ImageRepository.writeRepository();

                // Update the node with the image:
                this.buttons.node.addImage();
                this.buttons.node.setImage(app_image);
                this.buttons.updatePositions();

                // Show the node information in the node panel:
                App.instance.side_panel.displayNodePanel();
                App.instance.side_panel.updateNodePanel();

                // Display this button image for removing the image:
                this.setImage(remove_image);
            }
        }
        else {
            App.instance.side_panel.setNode(this.buttons.node);

            this.buttons.node.removeImage();
            this.buttons.node.updatePositions();
            this.buttons.updatePositions();

            App.instance.side_panel.updateNodePanel();

            this.setImage(add_image);
        }

    }
}
