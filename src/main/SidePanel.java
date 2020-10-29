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

        // Setup the node panel:
        this.node_panel = new NodePanel();
        this.tools_panel.addTab("Node", this.node_panel);

        // Setup the images panel:
        this.images_panel = new ImagesPanel();
        JScrollPane images_scroll_pane = new JScrollPane(this.images_panel);
        images_scroll_pane.setPreferredSize(new Dimension(240, 400));
        this.tools_panel.addTab("Images", images_scroll_pane);

        // Setup the nodes panel:
        this.nodes_panel = new NodesPanel();
        this.tools_panel.addTab("Nodes", this.nodes_panel);
    }

    ButtonsPanel buttons_panel;

    JTabbedPane  tools_panel;
    NodePanel    node_panel;
    ImagesPanel  images_panel;
    NodesPanel   nodes_panel;


    public void setNode(Node node) {
        this.node_panel.setNode(node);
        this.node_panel.update();
        this.images_panel.setNode(node);
    }


    /// Node panel display logic: ///
    public void updateNodePanel() {
        this.node_panel.update();
    }

    public void displayNodePanel() {
        this.tools_panel.setSelectedIndex(0);
    }


    /// Images panel display logic: ///
    public void updateImagesPanel() {
        this.images_panel.update();
        this.images_panel.revalidate();
        this.revalidate();
    }

    public void displayImagesPanel() {
        this.tools_panel.setSelectedIndex(1);
    }


    public void updateNodesPanel() {
        this.nodes_panel.clear();
        this.nodes_panel.populate();
    }
}
