package map;

import utils.MathUtils;

import java.awt.*;
import java.awt.event.MouseEvent;

public class NodeMenu extends Widget {

    static private int PADDING = 10;
    static private int SPACING = 4;


    public NodeMenu() {
        this.add_remove_image_button = new ImageButton(this);
        this.link_button = new LinkButton(this);
        this.add_remove_description_button = new DescriptionButton(this);
        this.recalculateSize();
    }

    Node node;
    boolean is_visible = false;

    ImageButton add_remove_image_button;
    LinkButton link_button;
    DescriptionButton add_remove_description_button;

    Widget hover_widget;

    public void paint(Graphics2D graphics) {
        if(this.node != null) {
            // Draw the buttons:
            this.add_remove_image_button.paint(graphics);
            this.link_button.paint(graphics);
            this.add_remove_description_button.paint(graphics);
        }
    }


    public void setView(View view) {
        super.setView(view);
        this.add_remove_image_button.setView(view);
        this.link_button.setView(view);
        this.add_remove_description_button.setView(view);
    }


    public void setNode(Node node) {
        this.node = node;
        this.recalculateSize();
        this.updatePosition();
        this.updateButtonsPositions();
        this.update();
    }


    public void update() {
        this.add_remove_image_button.update();
        this.add_remove_description_button.update();
    }


    public Node getNode() {
        return this.node;
    }

    public void updatePositions() {
        this.updatePosition();
        this.updateButtonsPositions();
    }


    public void setVisible(boolean visible) {
        this.is_visible = visible;
    }


    public boolean isVisible() {
        return this.is_visible;
    }


    private void recalculateSize() {
        this.size.width  = this.calcWidth();
        this.size.height = this.calcHeight();
    }

    private int calcWidth() {
        int width = 0;
        width += PADDING;
        width += this.add_remove_image_button.getWidth();
        width += SPACING;
        width += this.link_button.getWidth();
        width += SPACING;
        width += this.add_remove_description_button.getWidth();
        width += PADDING;
        return width;
    }


    private int calcHeight() {
        int height = 0;
        height += PADDING;
        height = MathUtils.max(
                this.add_remove_image_button.getHeight(),
                this.link_button.getHeight(),
                this.add_remove_description_button.getHeight()
        );
        height += PADDING;
        return height;
    }



    // Update position after node is sete for this buttons container:
    private void updatePosition() {
        Point node_position = this.node.getPosition();
        int position_x = node_position.x;
        int node_height = this.node.getHeight();
        int height = this.calcHeight();
        int position_y = node_position.y + node_height / 2 + height / 2;

        Point position = new Point(position_x, position_y);
        this.setPosition(position);
    }


    private void updateButtonsPositions() {
        Point image_button_position = this.calcImageButtonPosition();
        this.add_remove_image_button.setPosition(image_button_position);

        Point link_button_position = this.calcLinkButtonPosition();
        this.link_button.setPosition(link_button_position);

        Point description_button_position = this.calcDescriptionButtonPosition();
        this.add_remove_description_button.setPosition(description_button_position);
    }



    private Point calcImageButtonPosition() {
        int left = this.position.x - this.getWidth() / 2 + PADDING;
        int position_x = left + this.add_remove_image_button.getWidth() / 2;
        return new Point(position_x, this.position.y);
    }


    private Point calcLinkButtonPosition() {
        int left = this.position.x - this.getWidth() / 2 + PADDING + this.add_remove_image_button.getWidth() + SPACING;
        int position_x = left + this.link_button.getWidth() / 2;
        return new Point(position_x, this.position.y);
    }


    private Point calcDescriptionButtonPosition(){
        int left = this.position.x - this.getWidth() / 2 + PADDING + this.add_remove_image_button.getWidth() + SPACING + this.link_button.getWidth() + SPACING;
        int position_x = left + this.add_remove_description_button.getWidth() / 2;
        return new Point(position_x, this.position.y);
    }


    public void onMouseMove(MouseEvent event) {
        super.onMouseMove(event);
        Point mouse = new Point(event.getX(), event.getY());
        Widget new_hover_widget = null;
        Widget[] widgets = {this.add_remove_image_button, this.link_button, this.add_remove_description_button};
        for(Widget widget : widgets) {
            if (widget.containsPoint(mouse)) {
                new_hover_widget = widget;
                break;
            }
        }

        if (new_hover_widget != this.hover_widget) {
            if (this.hover_widget != null) {
                this.hover_widget.onMouseOut(event);
            }
            if (new_hover_widget != null) {
                new_hover_widget.onMouseOver(event);
            }
            this.hover_widget = new_hover_widget;
        }
        this.view.repaint();
    }


    public void onMousePress(MouseEvent event) {
        super.onMousePress(event);
        if (this.hover_widget != null) {
            this.hover_widget.onMousePress(event);
        }
        this.view.repaint();
    }


    public void onMouseRelease(MouseEvent event) {
        super.onMousePress(event);
        if (this.hover_widget != null) {
            this.hover_widget.onMouseRelease(event);
        }
        this.view.repaint();
    }


    public void onClick(MouseEvent event) {
        super.onClick(event);
        if (this.hover_widget != null) {
            this.hover_widget.onClick(event);
        }
    }
}
