package main;

import map.Node;
import map.View;
import utils.Resources;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class NodePanel extends JPanel implements DocumentListener, KeyListener, ActionListener {

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
        // For the selected node panel:
        this.node_selected_panel = new JPanel();
        this.node_selected_panel.setLayout(new BoxLayout(this.node_selected_panel, BoxLayout.PAGE_AXIS));

        this.label = new JLabel("Selected node:");
        this.label.setFont(new Font("Arial", 0 , 20));
        this.label.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.node_selected_panel.add(this.label);

        this.node_selected_panel.add(Box.createRigidArea(new Dimension(10,10)));

        this.image_label = new JLabel("Image:");
        this.image_label.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.node_selected_panel.add(this.image_label);

        this.image_field = new JLabel();
        this.image_field.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.node_selected_panel.add(this.image_field);

        this.node_selected_panel.add(Box.createRigidArea(new Dimension(10,4)));

        this.browse_button = new JButton("Browse");
        this.browse_button.addActionListener(this);
        this.browse_button.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.node_selected_panel.add(this.browse_button);


        this.node_selected_panel.add(Box.createRigidArea(new Dimension(10,10)));

        this.title_label = new JLabel("Title:");
        this.title_label.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.node_selected_panel.add(this.title_label);

        this.title_field = new JTextField("<NODE NAME>");
        this.title_field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        this.title_field.getDocument().addDocumentListener(this);
        this.node_selected_panel.add(this.title_field);

        this.node_selected_panel.add(Box.createRigidArea(new Dimension(10,10)));

        this.description_label = new JLabel("Description:");
        this.description_label.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.node_selected_panel.add(this.description_label);

        this.description_field = new JEditorPane();
        this.description_field.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        this.description_field.addKeyListener(this);
        this.node_selected_panel.add(this.description_field);

        this.empty_description_label = new JLabel("No description");
        this.empty_description_label.setFont(new Font("Arial", 0 , 24));
        this.empty_description_label.setForeground(Color.LIGHT_GRAY);
        this.empty_description_label.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.vertical_glue = Box.createVerticalGlue();
        this.node_selected_panel.add(this.vertical_glue);


        // For the node not selected panel:
        this.node_not_selected_panel = new JPanel();

        this.no_node_selected_label = new JLabel("No node selected");
        this.no_node_selected_label.setForeground(Color.LIGHT_GRAY);
        this.no_node_selected_label.setFont(new Font("Arial",0 , 24));
        this.node_not_selected_panel.add(this.no_node_selected_label);

        this.update();
    }

    Node node; // The current node, which is selected on the map view.

    /// The node selected panel and it's components, displayed when a node is selected:
    JPanel       node_selected_panel;
    JLabel      label;
    JLabel      image_label;
    JLabel      image_field;
    JButton     browse_button;
    JLabel      title_label;
    JTextField  title_field;
    JLabel      description_label;
    JEditorPane description_field;
    JLabel      empty_description_label;
    Component   vertical_glue;

    // Tho node NOT selected panel and it's components displayed when no node is selected:
    JPanel node_not_selected_panel;
    JLabel no_node_selected_label;


    public void setNode(Node node) {
        this.node = node;
    }


    // Update the panel so it displays current node data:
    public void update() {
        if (this.node != null) {
            this.remove(this.node_not_selected_panel);
            this.add(this.node_selected_panel);

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
                this.node_selected_panel.remove(this.description_field);
                this.node_selected_panel.remove(this.vertical_glue);
                this.node_selected_panel.add(this.empty_description_label);
                this.node_selected_panel.add(this.vertical_glue);
            }
            else {
                this.node_selected_panel.remove(this.empty_description_label);
                this.node_selected_panel.remove(this.vertical_glue);
                this.node_selected_panel.add(this.description_field);
                this.node_selected_panel.add(this.vertical_glue);
                String description = this.node.getDescription();
                this.description_field.setText(description);
            }
        }
        else {
            this.remove(this.node_selected_panel);
            this.add(this.node_not_selected_panel);
        }

        this.revalidate();
        this.node_selected_panel.revalidate();
        this.node_not_selected_panel.revalidate();
        this.repaint();
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

    public void actionPerformed(ActionEvent e) {
        // Called only when browse button was clicked:
        AppImage image = ImageRepository.browseForImage();
        if (image != null) {
            this.image_field.setIcon(new ImageIcon(image.scaled));
            if (this.node.getImage() == null) {
                this.node.addImage();
            }
            this.node.setImage(image);

            App.instance.map_view.buttons_menu.updatePositions();
            App.instance.map_view.buttons_menu.update();
        }
    }
}
