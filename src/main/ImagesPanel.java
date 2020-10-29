package main;

import main.commands.ImageChangeCommand;
import map.Node;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ImagesPanel extends JPanel implements ActionListener {

    public ImagesPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
        this.update();
    }

    Node node;


    public void setNode(Node node) {
        this.node = node;
    }


    public void update() {
        Component[] components = this.getComponents();
        for (Component component : components) {
            this.remove(component);
        }

        ArrayList<AppImage> images = ImageRepository.getImages();
        for (AppImage image : images) {
            JButton button = new JButton(image.file.getName());
            ImageIcon icon = new ImageIcon(image.scaled);
            button.setIcon(icon);
            button.setHorizontalTextPosition(SwingConstants.CENTER);
            button.setVerticalTextPosition(SwingConstants.BOTTOM);
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMaximumSize(new Dimension(260, 500));
            this.add(button);

            button.putClientProperty("image", image);

            this.add(Box.createRigidArea(new Dimension(10,10)));

            button.addActionListener(this);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (this.node == null) return;

        JButton source = (JButton) e.getSource();
        AppImage image = (AppImage) source.getClientProperty("image");

        if (this.node.getImage() == null) {
            this.node.addImage();
        }

        AppImage previous_image = this.node.getImage();
        this.node.setImage(image);

        App.instance.history.addCommand(new ImageChangeCommand(this.node, previous_image, image));

        App.instance.side_panel.updateNodePanel();
        App.instance.map_view.buttons_menu.updatePositions();
        App.instance.map_view.buttons_menu.update();

    }
}
