package main;

import map.Node;
import utils.Debug;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class NodesPanel extends JPanel implements MouseListener {

    public NodesPanel() {
        this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

        this.add(Box.createRigidArea(new Dimension(10, 4)));

        this.model = new DefaultListModel();
        this.list = new JList(this.model);
        this.list.setPreferredSize(new Dimension(200, 400));
        this.list.setPrototypeCellValue("-----------------------------------------");
        this.list.addMouseListener(this);
        this.list.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        this.list.setMinimumSize(new Dimension(200, 100));
        this.add(this.list);

        this.clear();
        this.populate();
    }

    DefaultListModel model;
    JList list;


    public void clear() {
        this.model.clear();
    }


    public void populate() {
        ArrayList<Node> nodes = App.instance.map_view.getModel().getNodes();
        for(Node node : nodes) {
            this.model.addElement(node);
        }
        this.list.setModel(this.model);
        this.revalidate();
        this.list.revalidate();
        this.list.updateUI();
    }

    public void mouseClicked(MouseEvent event) {
        int item_index = this.list.locationToIndex(event.getPoint());
        Node node = (Node) this.model.get(item_index);
        App.instance.map_view.scrollToNode(node);
        App.instance.map_view.setSelectedNode(node, false);
        if (event.getClickCount() == 2) {
            App.instance.map_view.setSelectedNode(node, true);
        }
    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }
}
