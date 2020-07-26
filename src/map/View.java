package map;

import main.App;
import utils.Debug;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;

public class View extends JPanel implements MouseMotionListener, MouseListener, KeyListener, FocusListener {

    public View() {
        this.setBackground(Color.WHITE);
        Border border = BorderFactory.createLineBorder(Color.GRAY);
        this.setBorder(border);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addKeyListener(this);
        this.addFocusListener(this);

        this.node_buttons = new NodeButtons();
        this.node_buttons.setView(this);
    }


    NodeButtons node_buttons;

    Mode mode = Mode.SELECT_NODE;

    ArrayList<Connection> connections = new ArrayList<>();
    ArrayList<Node> nodes = new ArrayList<>();

    Point center = new Point(0, 0);
    boolean is_scrolling = false;
    int last_click_time;  // Last click time in milliseconds

    Node       hover_node       = null;
    Connection hover_connection = null;
    Focusable  focus_object     = null;


    Node  drag_node  = null;
    Point drag_point = new Point();

    Node  link_start_node;
    Point link_current_position = null;
    Node  link_end_node;



    public void paint(Graphics graphics) {
        super.paint(graphics);
        Graphics2D graphics2d = (Graphics2D) graphics;

        RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2d.setRenderingHints(hints);

        if (this.link_start_node != null && this.link_current_position != null) {
            graphics2d.setColor(Color.BLACK);
            Stroke previous_stroke = graphics2d.getStroke();
            graphics2d.setStroke(new BasicStroke(2));
            Point screen_start = this.positionToScreen(this.link_start_node.getPosition());
            Point screen_end   = this.link_current_position;
            graphics2d.drawLine(screen_start.x, screen_start.y, screen_end.x, screen_end.y);
            graphics2d.setStroke(previous_stroke);
        }

        for(Connection connection : this.connections) {
            connection.paint(graphics2d);
        }

        for (Node node: this.nodes) {
            node.paint(graphics2d);
        }

        if(this.node_buttons.isVisible()) {
            this.node_buttons.paint(graphics2d);
        }

    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void addNode(Node node) {
        this.nodes.add(node);
        node.setView(this);
    }


    public void removeNode(Node node) {
        this.nodes.remove(node);
        node.setView(null);
    }


    public void removeConnection(Connection connection) {
        this.connections.remove(connection);
    }



    // Map position from view coordinates to screen coordinates:
    public Point positionToScreen(Point point) {
        int x = point.x;
        int y = point.y;

        x -= this.center.x;
        y -= this.center.y;

        Dimension size = this.getSize();

        x += size.width  / 2;
        y += size.height / 2;

        return new Point(x, y);
    }


    // Map position from screen coordinates to view coordinates:
    public Point screenToPosition(Point screen_position) {
        Point position = new Point(new Point(screen_position));
        position.x -= this.getWidth()  / 2;
        position.y -= this.getHeight() / 2;
        position.x += this.center.x;
        position.y += this.center.y;
        return position;
    }


    public void mouseMoved(MouseEvent event) {
        Point mouse = new Point(event.getX(), event.getY());
        Node new_hover_node = null;

        for (Node node : this.nodes) {
            if (node.containsPoint(mouse)) {
                new_hover_node = node;
                break;
            }
        }

        if(new_hover_node != this.hover_node) {
            if (this.hover_node != null) {
                this.hover_node.onMouseOut(event);
            }
            if (new_hover_node != null) {
                new_hover_node.onMouseOver(event);
                this.hover_node = new_hover_node;
                this.node_buttons.setNode(new_hover_node);
                this.repaint();
            }

            if (new_hover_node != null) {
                this.node_buttons.setVisible(true);
            }
            else {
                this.node_buttons.setVisible(false);
            }
        }

        if(this.hover_node != null) {
            if (this.node_buttons.containsPoint(mouse) || this.hover_node.containsPoint(mouse)) {
                this.node_buttons.setVisible(true);
                this.node_buttons.onMouseMove(event);
            }
            else {
                this.node_buttons.setVisible(false);
            }
        }

        if (this.hover_node == null || (!this.node_buttons.containsPoint(mouse) || !this.hover_node.containsPoint(mouse))) {
            Connection new_hover_connection = null;
            for(Connection connection : this.connections) {
                Point position = this.screenToPosition(mouse);
                boolean is_near = connection.isPointNear(position);
                if(is_near) {
                    new_hover_connection = connection;
                }
            }

            if (new_hover_connection != this.hover_connection) {
                if (this.hover_connection != null) {
                    this.hover_connection.onMouseOut();
                }

                if(new_hover_connection != null) {
                    new_hover_connection.onMouseOver();
                }
                this.hover_connection = new_hover_connection;
            }
        }

        this.repaint();

    }



    public void mousePressed(MouseEvent event) {
        if (event.getButton() == 2) {
            // Middle mouse button pressed:
            this.drag_point = new Point(event.getX(), event.getY());
            this.is_scrolling = true;
        }
        else if (event.getButton() == 1) {
            // Left mouse button pressed:
            Point mouse = new Point(event.getX(), event.getY());
            if (this.node_buttons.containsPoint(mouse) && this.node_buttons.isVisible()) {
                // If the press occured on the node buttons, forward it:
                this.node_buttons.onMousePress(event);
            }
            else {
                Node drag_node = null;
                // Else start dragging node under the cursor:
                for (Node node : this.nodes) {
                    if (node.containsPoint(mouse)) {
                        drag_node = node;
                        break;
                    }
                }
                if (drag_node != null) {
                    this.drag_node = drag_node;
                    this.drag_point = mouse;
                }
            }
        }
    }


    public void mouseDragged(MouseEvent event) {
        // Node dragging logic:
        if (this.drag_node != null) {
            Point mouse = new Point(event.getX(), event.getY());
            Point node_position = new Point(this.drag_node.getPosition());
            int dx = mouse.x - this.drag_point.x;
            int dy = mouse.y - this.drag_point.y;
            node_position.x += dx;
            node_position.y += dy;
            this.drag_node.setPosition(node_position);
            this.node_buttons.updatePositions();
            this.drag_point = mouse;
            this.node_buttons.setVisible(false);
        }

        // New connection creation handling:
        if(this.link_start_node != null) {
            Point mouse = new Point(event.getX(), event.getY());
            this.link_current_position = mouse;

            for(Node node : this.nodes) {
                if (node.containsPoint(mouse) && node != this.link_start_node) {
                    Node end_node = node;
                    this.link_current_position = this.positionToScreen(end_node.getPosition());
                    this.link_end_node = node;
                    break;
                }
            }
        }

        // Scrolling handling:
        if(this.is_scrolling) {
            int dx = event.getX() - this.drag_point.x;
            int dy = event.getY() - this.drag_point.y;
            this.center.x -= dx;
            this.center.y -= dy;
            this.drag_point = new Point(event.getX(), event.getY());
            this.repaint();
        }
        this.repaint();
    }

    public void mouseReleased(MouseEvent event) {
        // Drag node release code:
        this.drag_node = null;

        if(this.link_start_node != null && this.link_end_node != null) {
            // Check if a connection already exists:
            boolean connection_exists = false;
            for(Connection connection : this.connections) {
                if(connection.node_1 == this.link_start_node && connection.node_2 == this.link_end_node) {
                    connection_exists = true;
                }
                if(connection.node_1 == this.link_end_node && connection.node_2 == this.link_start_node) {
                    connection_exists = true;
                }
            }
            if (!connection_exists) {
                // Make the connection:
                Connection new_connection = new Connection(this.link_start_node, this.link_end_node);
                new_connection.setView(this);
                this.connections.add(new_connection);
            }
            this.link_start_node = null;
            this.link_end_node = null;
        }
        if (event.getButton() == 2) {
            this.is_scrolling = false;
        }
        else if (event.getButton() == 1) {
            this.node_buttons.onMouseRelease(event);
        }


        // Invoke the mouse moved event, so that we regain the hover node and the interface to the correct state:
        this.mouseMoved(event);

    }


    public void mouseClicked(MouseEvent event) {
        this.grabFocus();
        if (this.mode == Mode.SELECT_NODE) {
            Point mouse = new Point(event.getX(), event.getY());
            boolean node_clicked = false;
            for(Node node : this.nodes) {
                if (node.containsPoint(mouse)) {
                    if (node != this.focus_object) {
                        if (this.focus_object != null) {
                            this.focus_object.loseFocus();
                        }
                        this.focus_object = node;
                        App.instance.side_panel.displayNodePanel();
                        App.instance.side_panel.setNode(node);
                        this.focus_object.gainFocus();
                    }
                    node.onClick(event);
                    node_clicked = true;
                    break;
                }
            }


            if (this.node_buttons.containsPoint(mouse)) {
                this.node_buttons.onClick(event);
            }

            if(this.hover_connection != null) {
                Focusable new_focus_object = this.hover_connection;
                if (new_focus_object != this.focus_object) {
                    if (this.focus_object != null) {
                        this.focus_object.loseFocus();
                    }
                    if (new_focus_object != null) {
                        new_focus_object.gainFocus();
                    }
                }
                this.focus_object = new_focus_object;
            }

            if (!node_clicked) {
                int click_time = (int) (System.nanoTime() / 1_000_000);
                if (click_time - this.last_click_time < 250) {
                    this.onDoubleClick(event);
                }
                this.last_click_time = click_time;
            }
        }
        else if (this.mode == Mode.ADD_NODE) {
            Point mouse = new Point(event.getX(), event.getY());
            Point position = this.screenToPosition(mouse);
            this.insertNewNode(position);
        }
    }


    private void onDoubleClick(MouseEvent event) {
        Point mouse = new Point(event.getX(), event.getY());
        Point position = this.screenToPosition(mouse);
        this.insertNewNode(position);
    }


    private Node insertNewNode(Point position) {
        Node node = new Node();
        this.addNode(node);
        node.setPosition(position);
        return node;
    }


    public void keyTyped(KeyEvent event) {
        if (this.focus_object != null) {
            if (this.focus_object instanceof Node) {
                Node focus_node = (Node) this.focus_object;
                boolean consumed = focus_node.keyTyped(event);
            }
        }
    }

    public void keyPressed(KeyEvent event) {
        if(this.focus_object != null && this.focus_object instanceof Node) {
            Node focus_node = (Node) this.focus_object;

            // Forward the event to the focus node, and check if it was consumed:
            boolean consumed = focus_node.keyPressed(event);

            // If the event wasn't consumed - the active node, not it's controls received the event,
            // so process the event locally:
            if (!consumed && event.getKeyCode() == 127) {
                // Remove a node if delete key was pressed:
                this.removeNode(focus_node);
                // Remove connections that were associated with the removed node and remove it:
                Iterator<Connection> iterator = this.connections.iterator();
                while(iterator.hasNext()) {
                    Connection connection = iterator.next();
                    if (connection.node_1 == focus_node || connection.node_2 == focus_node) {
                        iterator.remove();
                    }
                }
                Node node = this.node_buttons.getNode();
                if (node == focus_node) {
                    this.node_buttons.setVisible(false);
                }

                this.hover_node = null;
                this.focus_object = null;


                this.repaint();
            }
        }
        else if (this.focus_object != null && this.focus_object instanceof Connection) {
            // Remove the connection between two nodes if delete key was pressed:
            if (event.getKeyCode() == 127) {
                Connection connection = (Connection) this.focus_object;
                this.removeConnection(connection);

                this.repaint();
            }
        }
    }

    public void keyReleased(KeyEvent event) {
        if (this.focus_object != null) {
            if (this.focus_object instanceof Node) {
                Node focus_node = (Node) this.focus_object;
                focus_node.keyReleased(event);
            }
        }
    }


    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}



    public void setStartNode(Node node) {
        this.link_start_node = node;
    }


    public void focusGained(FocusEvent e) {
        if (this.focus_object != null) {
            this.focus_object.gainFocus();
        }
        this.repaint();
    }

    public void focusLost(FocusEvent e) {
        if(this.focus_object != null) {
            this.focus_object.loseFocus();
        }
        this.repaint();
    }

}
