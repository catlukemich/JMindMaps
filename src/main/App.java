package main;

import map.Node;
import map.View;
import utils.Resources;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;

public class App extends JFrame {

    static public App instance;

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

        App app = new App();
        instance = app;
        app.start();
    }


    public App() {
        super("JMindMap");
    }


    public void start() {
        ImageRepository.readRepository();

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


        this.side_panel = new SidePanel();
        content_pane.add(this.side_panel, BorderLayout.LINE_END);
        this.side_panel.setMaximumSize(new Dimension(200, Integer.MAX_VALUE));


        this.status_bar = new StatusBar();
        content_pane.add(this.status_bar, BorderLayout.PAGE_END);

        Node node = new Node();
        this.map_view.addNode(node);
        BufferedImage image = Resources.loadImage("/building.png");
        node.setTitle("Hello");

        this.map_view.repaint();

        this.setSize(800, 600);
    }


    public View map_view;
    public SidePanel side_panel;
    public StatusBar status_bar;
}
