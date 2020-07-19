package map;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class View extends JPanel implements MouseMotionListener, MouseListener, KeyListener {

    public View() {
        this.setBackground(Color.WHITE);
        this.setBorder(new ViewBorder());
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addKeyListener(this);
    }

    Point center = new Point(0, 0);
    ArrayList<Node> nodes = new ArrayList<>();
    Point drag_point = new Point();

    Node focus_node = null;
    boolean is_scrolling = false;


    public void addNode(Node node) {
        this.nodes.add(node);
        node.setView(this);
    }


    public void removeNode(Node node) {
        this.nodes.remove(node);
        node.setView(null);
    }


    public void paint(Graphics graphics) {
        Graphics2D graphics2d = (Graphics2D) graphics;
        super.paint(graphics2d);
        for (Node node: this.nodes) {
            node.paint(graphics2d);
        }
    }


    public Point map(Point point) {
        int x = point.x;
        int y = point.y;

        x -= this.center.x;
        y -= this.center.y;

        Dimension size = this.getSize();

        x += size.width  / 2;
        y += size.height / 2;

        return new Point(x, y);
    }


    public void mousePressed(MouseEvent event) {
        if (event.getButton() == 2) {
            this.drag_point = new Point(event.getX(), event.getY());
            this.is_scrolling = true;
        }
    }


    public void mouseDragged(MouseEvent event) {
        if(this.is_scrolling) {
            int dx = event.getX() - this.drag_point.x;
            int dy = event.getY() - this.drag_point.y;
            this.center.x -= dx;
            this.center.y -= dy;
            this.drag_point = new Point(event.getX(), event.getY());
            this.repaint();
        }
    }

    public void mouseReleased(MouseEvent e) {
        this.is_scrolling = false;
    }


    public void mouseClicked(MouseEvent event) {
        this.grabFocus();
        if(this.focus_node != null) this.focus_node.loseFocus();
        Point mouse = new Point(event.getX(), event.getY());
        for(Node node : this.nodes) {
            if (node.containsPoint(mouse)) {
                node.gainFocus();
                this.focus_node = node;
                node.onClick(event);
                System.out.println(node);
                break;
            }
        }
    }


    public void keyTyped(KeyEvent event) {
        if (this.focus_node != null) {
            this.focus_node.keyTyped(event);
        }
    }

    public void keyPressed(KeyEvent event) {
        if(this.focus_node != null) {
            this.focus_node.keyPressed(event);
        }
    }

    public void keyReleased(KeyEvent event) {
        if (this.focus_node != null) {
            this.focus_node.keyReleased(event);
        }
    }


    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {}


    private class ViewBorder extends AbstractBorder {
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            super.paintBorder(c, g, x, y, width, height);
            g.setColor(Color.GRAY);
            g.fillRect(width - 1, y, width, height);
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(0,0,0,1);
        }
    }



}
