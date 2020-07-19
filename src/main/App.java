package main;

import map.Node;
import map.View;
import utils.Resources;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;

public class App extends JFrame {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                UIManager.getSystemLookAndFeelClassName()
            );
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        App main = new App();
        main.start();
    }


    public App() {
        super("JMindMap");
    }


    public void start() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        JPanel content_pane = new JPanel();
        content_pane.setLayout(new BorderLayout());
        Border pane_border = BorderFactory.createLineBorder(Color.GRAY, 1);
        content_pane.setBorder(pane_border);
        this.setContentPane(content_pane);

        this.map_view = new View();
        content_pane.add(this.map_view, BorderLayout.CENTER);
        Globals.graphics = (Graphics2D) this.map_view.getGraphics();


        this.buttons_panel = new ButtonPanel();
        content_pane.add(this.buttons_panel, BorderLayout.LINE_END);

        Node node = new Node();
        this.map_view.addNode(node);
        BufferedImage image = Resources.loadImage("/building.png");
        node.setImage(image);
        node.setTitle("Helllo world, thi is me fucker");
        node.setDescription("This might be the description which might be  be the description whspans on many lines, is editable and just works (hopefully)");

        this.map_view.repaint();

        this.setSize(800, 600);
    }


    View map_view;
    ButtonPanel buttons_panel;
}
