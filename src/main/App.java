package main;

import map.Node;
import map.View;
import map.ViewModel;
import utils.Resources;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

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

    public View map_view;
    public History history;
    public SidePanel side_panel;
    public StatusBar status_bar;

    public File file; // The current file the app is working with,

    public void start() {
        ImageRepository.readRepository();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        MenuBar menu_bar = new MenuBar();
        this.setJMenuBar(menu_bar);

        JPanel content_pane = new JPanel();
        content_pane.setLayout(new BorderLayout());
        Border pane_border = BorderFactory.createLineBorder(Color.GRAY, 1);
        content_pane.setBorder(pane_border);
        this.setContentPane(content_pane);

        this.map_view = new View();
        content_pane.add(this.map_view, BorderLayout.CENTER);
        Globals.graphics = (Graphics2D) this.map_view.getGraphics();

        this.history = new History();

        Node node = new Node();
        ViewModel model = this.map_view.getModel();
        node.setView(this.map_view);
        model.addNode(node);
        BufferedImage image = Resources.loadImage("/building.png");
        node.setTitle("Node no 1");

        this.side_panel = new SidePanel();
        content_pane.add(this.side_panel, BorderLayout.LINE_END);
        this.side_panel.setPreferredSize(new Dimension(260, 600));
        this.side_panel.setMaximumSize(new Dimension(260, Integer.MAX_VALUE));


        this.status_bar = new StatusBar();
        content_pane.add(this.status_bar, BorderLayout.PAGE_END);


        this.map_view.repaint();

        this.setSize(800, 600);
    }

}
