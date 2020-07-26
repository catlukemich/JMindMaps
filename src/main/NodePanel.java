package main;

import map.Node;
import map.View;
import utils.Debug;
import utils.Resources;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.awt.image.BufferedImage;

public class NodePanel extends JPanel implements DocumentListener, KeyListener {

    static public BufferedImage NO_IMAGE = Resources.loadImage("/no_image.png");


    public NodePanel () {
        // Setup the layout:
        BoxLayout layout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
        this.setLayout(layout);
        this.setBorder(new CompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(6,6,6,6)
        ));

        // Setup the controls:
        this.label = new JLabel("Selected node:");
        this.label.setFont(new Font("Arial", 0 , 20));
        this.label.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(this.label);

        this.add(Box.createRigidArea(new Dimension(10,10)));

        this.image_label = new JLabel("Image:");
        this.image_label.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(this.image_label);

        this.image_field = new JLabel();
        this.image_field.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(this.image_field);

        this.add(Box.createRigidArea(new Dimension(10,10)));

        this.title_label = new JLabel("Title:");
        this.title_label.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(this.title_label);

        this.title_field = new JTextField("<NODE NAME>");
        this.title_field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        this.title_field.getDocument().addDocumentListener(this);
        this.add(this.title_field);

        this.add(Box.createRigidArea(new Dimension(10,10)));

        this.description_label = new JLabel("Description:");
        this.description_label.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(this.description_label);

        this.description_field = new JEditorPane();
        this.description_field.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        this.add(this.description_field);
        this.description_field.addKeyListener(this);
//        this.description_field.getDocument().addDocumentListener(this);

        this.empty_description_label = new JLabel("No description");
        this.empty_description_label.setFont(new Font("Arial", 0 , 24));
        this.empty_description_label.setForeground(Color.LIGHT_GRAY);
        this.empty_description_label.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.vertical_glue = Box.createVerticalGlue();
        this.add(this.vertical_glue);

    }

    Node node; // The current node, which has te focus on the map.

    JLabel label;
    JLabel      image_label;
    JLabel      image_field;
    JLabel      title_label;
    JTextField  title_field;
    JLabel      description_label;
    JEditorPane description_field;
    JLabel      empty_description_label;
    Component   vertical_glue;


    public void setNode(Node node) {
        this.node = node;
    }


    // Update the panel so it displays current node data:
    public void update() {
        if (this.node == null) return;

        // Display the image:
        AppImage image = this.node.getImage();
        if (image != null) {
            this.image_field.setText("");
            ImageIcon icon = new ImageIcon(image.scaled);
            this.image_field.setIcon(icon);
        }
        else {
            ImageIcon no_image = new ImageIcon(NO_IMAGE);
            this.image_field.setIcon(no_image);
        }

        // Read the node text into title's textfield:
        String title = this.node.getTitle();
        this.title_field.setText(title);

        // Read the node description into description textarea:
        boolean has_description = this.node.hasDescription();

        if (!has_description) {
            this.remove(this.description_field);
            this.remove(this.vertical_glue);
            this.add(this.empty_description_label);
            this.add(this.vertical_glue);
        }
        else {
            this.remove(this.empty_description_label);
            this.remove(this.vertical_glue);
            this.add(this.description_field);
            this.add(this.vertical_glue);
            String description = this.node.getDescription();
            this.description_field.setText(description);
        }

        this.revalidate();
    }


    /// Document listener event's handling ///

    public void insertUpdate(DocumentEvent e) {
        this.updateNodeTitle();
    }

    public void removeUpdate(DocumentEvent e) {
        this.updateNodeTitle();
    }

    public void changedUpdate(DocumentEvent e) {}

    private void updateNodeTitle() {
        String title = this.title_field.getText();
        this.node.setTitle(title);
        this.node.recalculateSize();
        View view = this.node.getView();
        view.repaint();
    }


    private void updateNodeDescription() {
        String description = this.description_field.getText();
        this.node.setDescription(description);
        View view = this.node.getView();
        view.repaint();
    }

    public void keyTyped(KeyEvent e) {

    }

    public void keyPressed(KeyEvent e) {
        System.out.println(e.getKeyCode());
        if (e.getKeyCode() == 10) {
            this.updateNodeDescription();
        }
        e.consume();
    }

    public void keyReleased(KeyEvent e) {

    }
}
