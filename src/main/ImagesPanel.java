package main;

import javax.swing.*;
import java.util.ArrayList;

public class ImagesPanel extends JPanel {

    public ImagesPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        ArrayList<AppImage> images = ImageRepository.getImages();

        for (AppImage image : images) {
            JLabel image_label = new JLabel();
            ImageIcon icon = new ImageIcon(image.scaled);
            image_label.setIcon(icon);
            this.add(image_label);
        }


    }
}
