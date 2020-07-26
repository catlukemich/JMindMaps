package main;

import map.Node;

import javax.swing.*;
import java.awt.*;

public class SidePanel extends JPanel {

    public SidePanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        this.buttons_panel = new ButtonsPanel();
        this.buttons_panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
        this.add(this.buttons_panel);

        this.tools_panel = new JTabbedPane();
        this.add(this.tools_panel);

        this.node_panel = new NodePanel();
        this.tools_panel.addTab("Node", this.node_panel);

        this.images_panel = new ImagesPanel();
        this.tools_panel.addTab("Images", this.images_panel);
    }

    ButtonsPanel buttons_panel;

    JTabbedPane  tools_panel;
    ImagesPanel  images_panel;
    NodePanel    node_panel;





    /// Node panel display logic: ///

    public void setNode(Node node) {
        this.node_panel.setNode(node);
        this.node_panel.update();
    }


    public void updateNodePanel() {
        this.node_panel.update();
    }


    public void displayNodePanel() {
        this.tools_panel.setSelectedIndex(0);
//        this.add(this.node_panel);
    }

    // Images panel display logic: ///
    public void displayImagesPanel() {

    }
}
