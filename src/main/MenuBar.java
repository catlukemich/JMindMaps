package main;

import map.Connection;
import map.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import utils.Debug;
import utils.FileUtils;
import utils.Resources;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class MenuBar extends JMenuBar implements ActionListener {

    public MenuBar() {
        // Create file menu:
        JMenu file_menu = new JMenu("File");
        this.add(file_menu);


        this.new_item = new JMenuItem("New");
        this.new_item.addActionListener(this);
        file_menu.add(this.new_item);

        this.open_item = new JMenuItem("Open...");
        this.open_item.addActionListener(this);
        file_menu.add(this.open_item);

        this.save_as_item = new JMenuItem("Sava as...");
        this.save_as_item.addActionListener(this);
        KeyStroke save_as_stroke = KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK);
        this.save_as_item.setAccelerator(save_as_stroke);
        file_menu.add(this.save_as_item);

        this.save_item = new JMenuItem("Save");
        KeyStroke save_stroke = KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK);
        this.save_item.setAccelerator(save_stroke);
        this.save_item.addActionListener(this);
        file_menu.add(this.save_item);

        file_menu.addSeparator();

        this.exit_item = new JMenuItem("Exit");
        this.exit_item.addActionListener(this);
        file_menu.add(this.exit_item);

        // Create edit menu:
        JMenu edit_menu = new JMenu("Edit");
        this.add(edit_menu);

        this.undo_item = new JMenuItem("Undo");
        this.undo_item.addActionListener(this);
        KeyStroke undo_stroke = KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK);
        this.undo_item.setAccelerator(undo_stroke);
        edit_menu.add(this.undo_item);

        this.redo_item = new JMenuItem("Redo");
        this.redo_item.addActionListener(this);
        KeyStroke redo_stroke = KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK);
        this.redo_item.setAccelerator(redo_stroke);
        edit_menu.add(this.redo_item);

    }

    JMenuItem new_item;
    JMenuItem open_item;
    JMenuItem save_as_item;
    JMenuItem save_item;
    JMenuItem exit_item;

    JMenuItem undo_item;
    JMenuItem redo_item;

    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == this.new_item) {
            int confirmation = JOptionPane.showConfirmDialog(this, "Create a new mind map? This will clear the current canvas.", "New", JOptionPane.YES_NO_OPTION);
            // If yes:
            if (confirmation == 0)  {
                App.instance.map_view.clear();
                App.instance.history = new History();
                App.instance.file = null;
            }
        }
        if (source == this.exit_item) {
            System.exit(0);
        }
        else if (source == this.save_as_item) {
            this.saveAs();
        }
        else if(source == this.save_item) {
            File current_file = App.instance.file;
            if (current_file == null) {
                this.saveAs();
            }
            else {
                this.writeSave(current_file);
            }
        }
        else if (source == this.open_item) {
            this.open();
        }
        else if (source == this.undo_item) {
            App.instance.history.undo();
        }
        else if (source == this.redo_item) {
            App.instance.history.redo();
        }
    }



    private void saveAs() {
        // Ask the user for the file:
        JFileChooser chooser = new JFileChooser();
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileFilter(new FileFilter() {
            public boolean accept(File file) {
                String extension = FileUtils.getFileExtension(file);
                return extension.equals("smm");
            }

            public String getDescription() {
                return "Simple Mind Map (SMM)";
            }
        });
        int result = chooser.showSaveDialog(App.instance);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            String extension = FileUtils.getFileExtension(file);
            if (!extension.equals("smm")) {
                file = new File(file.getAbsolutePath() + ".smm"); // Add the file extension if it wasn't written by the user.
            }
            boolean exists = false;
            try {
                exists = !file.createNewFile();
            } catch (IOException e) {
                System.err.println(e);
            }

            this.writeSave(file);

            App.instance.file = file;
        }
    }


    private void writeSave(File file) {
        ArrayList<Node> nodes = App.instance.map_view.getModel().getNodes();
        ArrayList<Connection> connections = App.instance.map_view.getModel().getConnections();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }

        // Create the document along with it's root node:
        Document document = builder.newDocument();
        Element mindmap = document.createElement("mindmap");
        document.appendChild(mindmap);

        // Create nodes element that is a child of the main "mindmap" element:
        Element nodes_elem = document.createElement("nodes");
        mindmap.appendChild(nodes_elem);

        for(Node node : nodes) {
            int hash = node.hashCode();
            String hash_code = String.valueOf(hash);

            // Set the hash attribute:
            Element node_elem = document.createElement("node");
            node_elem.setAttribute("hash", hash_code);

            // Set the position element:
            Element position_elem = document.createElement("position");
            Point position = node.getPosition();
            String x = String.valueOf(position.x);
            String y = String.valueOf(position.y);
            position_elem.setAttribute("x", x);
            position_elem.setAttribute("y", y);
            node_elem.appendChild(position_elem);

            // Add the image node elem with path to the image if the node has an image:
            AppImage image = node.getImage();
            if (image != null) {
                String image_path = image.file.getAbsolutePath();
                Element image_elem = document.createElement("image");
                image_elem.appendChild(document.createTextNode(image_path));
                node_elem.appendChild(image_elem);
            }

            // Add the title element with the text of the title:
            String title = node.getTitle();
            Element title_elem = document.createElement("title");
            title_elem.appendChild(document.createTextNode(title));
            node_elem.appendChild(title_elem);

            // Add the description element if the node contains description:
            if(node.hasDescription()) {
                String description = node.getDescription();
                Element description_elem = document.createElement("description");
                description_elem.appendChild(document.createTextNode(description));
                node_elem.appendChild(description_elem);
            }
            nodes_elem.appendChild(node_elem);
        }

        // Create connections element that is direct child of the root node "mindmaps":
        Element connections_elem = document.createElement("connections");
        mindmap.appendChild(connections_elem);

        for (Connection connection : connections) {
            Element connection_elem = document.createElement("connection");
            String node_1_hashcode = String.valueOf(connection.node_1.hashCode());
            String node_2_hashcode = String.valueOf(connection.node_2.hashCode());
            connection_elem.setAttribute("node_1", node_1_hashcode);
            connection_elem.setAttribute("node_2", node_2_hashcode);
            connections_elem.appendChild(connection_elem);
        }

        TransformerFactory transformer_factory = TransformerFactory.newInstance();
        Transformer transformer = null;
        try {
            transformer = transformer_factory.newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(file);
        try {
            transformer.transform(source, result);
        } catch (TransformerException e) {
            e.printStackTrace();
        }

    }


    private void open() {
        // Ask the user for the file:
        JFileChooser chooser = new JFileChooser();
        chooser.setAcceptAllFileFilterUsed(false);
        chooser.setFileFilter(new FileFilter() {
            public boolean accept(File file) {
                String extension = FileUtils.getFileExtension(file);
                return extension.equals("smm");
            }

            public String getDescription() {
                return "Simple Mind Map (SMM)";
            }
        });
        int result = chooser.showOpenDialog(App.instance);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            App.instance.map_view.clear();
            this.openFile(file);

            App.instance.file = file;
        }
    }


    private void openFile(File file) {
        HashMap<Integer, Node> nodes = new HashMap<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document document = null;
        try {
            document = builder.parse(file);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        // Read the nodes and add them to the map view:
        NodeList node_list = document.getElementsByTagName("node");
        for (int i = 0; i < node_list.getLength(); i++) {
            org.w3c.dom.Node node_item = node_list.item(i);

            Node node = new Node();
            node.setView(App.instance.map_view);

            org.w3c.dom.Node hash_attribute = node_item.getAttributes().getNamedItem("hash");
            int node_hash = Integer.valueOf(hash_attribute.getTextContent());

            NodeList child_nodes = node_item.getChildNodes();

            for (int j = 0; j < child_nodes.getLength(); j++) {
                org.w3c.dom.Node node_data_item = child_nodes.item(j);
                if (node_data_item.getNodeName().equals("position")) {
                    NamedNodeMap data_attributes = node_data_item.getAttributes();
                    int x = Integer.valueOf(data_attributes.getNamedItem("x").getTextContent());
                    int y = Integer.valueOf(data_attributes.getNamedItem("y").getTextContent());
                    node.setPosition(new Point(x, y));
                }

                if (node_data_item.getNodeName().equals("image")) {
                    String image_path = node_data_item.getTextContent();
                    try {
                        AppImage image = Resources.loadAppImage(image_path);
                        node.addImage();
                        node.setImage(image);
                    }
                    catch (RuntimeException e) {}
                }

                if (node_data_item.getNodeName().equals("title")){
                    String title = node_data_item.getTextContent();
                    node.setTitle(title);
                }

                if (node_data_item.getNodeName().equals("description")) {
                    String description = node_data_item.getTextContent();
                    node.addDescription();
                    node.setDescription(description);
                }
            }

            App.instance.map_view.getModel().addNode(node);
            nodes.put(node_hash, node);
        }


        // Read the connections and add them to the map view:
        NodeList connections_list = document.getElementsByTagName("connection");
        for (int i = 0; i < connections_list.getLength(); i++) {
            org.w3c.dom.Node connection_item = connections_list.item(i);

            NamedNodeMap connection_item_attributes = connection_item.getAttributes();
            int node_1_hashcode = Integer.valueOf(connection_item_attributes.getNamedItem("node_1").getTextContent());
            int node_2_hashcode = Integer.valueOf(connection_item_attributes.getNamedItem("node_2").getTextContent());

            Node node_1 = nodes.get(node_1_hashcode);
            Node node_2 = nodes.get(node_2_hashcode);

            Connection connection = new Connection(node_1, node_2);
            connection.setView(App.instance.map_view);

            App.instance.map_view.getModel().addConnection(connection);
        }

        App.instance.side_panel.setNode(null);
        App.instance.side_panel.updateNodePanel();
        App.instance.side_panel.updateNodesPanel();

    }

}
