package map;

import main.App;
import map.handlers.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;

public class View extends JPanel implements MouseMotionListener, MouseListener, KeyListener, FocusListener {

    public View() {
        this.setBackground(Color.WHITE);
        Border border = BorderFactory.createLineBorder(Color.GRAY);
        this.setBorder(border);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addKeyListener(this);
        this.addFocusListener(this);

        this.buttons_menu = new NodeMenu();
        this.buttons_menu.setView(this);
    }

    public static int NEW_NODE_DISTANCE = 100; // The distance at which, when using the connector - a new node will be created.

    public Mode mode = Mode.SELECT_NODE;
    public int last_click_time;  // Last click time in milliseconds



    final public NodeMenu buttons_menu;

    private ViewModel model = new ViewModel();

    private ViewScrollHandler drag_handler = new ViewScrollHandler();
    private NodeMenuHandler         node_menu_handler = new NodeMenuHandler();
    private ConnectionHoverHandler  connection_hover_handler = new ConnectionHoverHandler();
    private NodeDragHandler         node_drag_handler = new NodeDragHandler();
    private NodeLinkingHandler      linking_handler = new NodeLinkingHandler();
    private ViewKeyHandler          key_handler = new ViewKeyHandler();
    private ViewFocusHandler        focus_handler = new ViewFocusHandler();

    public PositionState position_state = new PositionState();
    public FocusState focus_state = new FocusState();
    public HoverState hover_state = new HoverState();
    public LinkingState linking_state = new LinkingState();


    private ViewDrawer drawer = new ViewDrawer();


    AutoScroller   autoscroller   = new AutoScroller();
    ButtonScroller buttonscroller = new ButtonScroller();


    public void paint(Graphics graphics) {
        super.paint(graphics);
        Graphics2D graphics2d =  (Graphics2D) graphics;
        this.drawer.draw(this, graphics2d);

    }

    // Map position from view coordinates to screen coordinates:
    public Point positionToScreen(Point point) {
        int x = point.x;
        int y = point.y;

        x -= this.position_state.center.x;
        y -= this.position_state.center.y;

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
        position.x += this.position_state.center.x;
        position.y += this.position_state.center.y;
        return position;
    }


    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public ViewModel getModel() {
        return this.model;
    }

    public void clear() {
        this.model.clear();
    }

    public void scrollToNode(Node node) {
        this.autoscroller.scrollTo(this, node);
    }


    public void mouseMoved(MouseEvent event) {
        this.node_menu_handler.mouseMoved(this, event);
        this.connection_hover_handler.mouseMoved(this, event);
    }

    public void mousePressed(MouseEvent event) {
        this.node_menu_handler.mousePressed(this, event);
        this.drag_handler.mousePressed(this, event);
        this.node_drag_handler.mousePressed(this, event);
    }


    public void mouseDragged(MouseEvent event) {
        boolean consumed = this.drag_handler.mouseDragged(this, event);
        if (!consumed) {
            this.node_drag_handler.mouseDraged(this, event);
        }
        this.linking_handler.mouseDragged(this, event);
    }

    public void mouseReleased(MouseEvent event) {
        this.drag_handler.mouseReleased(this, event);
        this.node_drag_handler.mouseReleased(this, event);
        this.linking_handler.mouseReleased(this, event);
    }

    public void mouseClicked(MouseEvent event) {
        this.grabFocus();
        this.focus_handler.mouseClicked(this, event);
    }

    public void keyTyped(KeyEvent event) {
        this.key_handler.keyTyped(this, event);
    }

    public void keyPressed(KeyEvent event) {
        this.key_handler.keyPressed(this, event);
    }

    public void keyReleased(KeyEvent event) {
        this.key_handler.keyReleased(this, event);
    }


    public void focusGained(FocusEvent e) {
        this.focus_handler.focusGained(this, e);
    }

    public void focusLost(FocusEvent e) {
        this.focus_handler.focusLost(this, e);
    }

    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}



    public void setSelectedNode(Node node, boolean switch_tab) {
        Node previous_node = this.focus_state.selected_node;
        if (previous_node != null) {
            this.focus_state.selected_node.setSelected(false);
        }
        if (node != null) {
            node.setSelected(true);
        }
        this.focus_state.selected_node = node;

        if (switch_tab) {
            App.instance.side_panel.displayNodePanel();
        }
        App.instance.side_panel.setNode(node);
    }

    public Point getCenter() {
        return this.position_state.center;
    }

    public void setCenter(Point center) {
        this.position_state.center = center;
    }



    public Node insertNewNode(Point position) {
        Node node = new Node();
        node.setView(this);
        this.model.addNode(node);
        node.setPosition(position);
        return node;
    }









}
