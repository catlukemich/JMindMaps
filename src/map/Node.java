package map;

import main.AppImage;
import utils.Debug;
import utils.MathUtils;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

public class Node extends Widget implements Focusable {

    static private int PADDING = 10;
    static private int SPACING = 10;

    public Node() {
        this.node_image        = new NullNodeImage(this);
        this.title_entry       = new TitleEntry(this);
        this.description_entry = new NullDescriptionEntry(this);
        this.node_image.recalculateSize();
        this.title_entry.recalculateSize();
        this.description_entry.recalculateSize();
        this.recalculateSize();
        this.updatePositions();
    }

    NodeImage        node_image;
    TitleEntry       title_entry;
    DescriptionEntry description_entry;

    boolean has_focus;
    boolean is_selected;
    Widget focus_widget;


    /// Painting logic ///
    public void paint(Graphics2D graphics) {
        // Paint the border and backgroundL
        Rectangle rectangle = this.calcBounds();
        graphics.setColor(new Color(250, 250, 250));
        graphics.fillRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        if (this.is_selected) {
            graphics.setColor(Color.RED);
        }
        else {
            graphics.setColor(Color.GRAY);
        }
        graphics.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);

        this.node_image.paint(graphics);
        this.title_entry.paint(graphics);
        this.description_entry.paint(graphics);
    }


    public void setView(View view) {
        super.setView(view);
        this.node_image.setView(view);
        this.title_entry.setView(view);
        this.description_entry.setView(view);
    }


    public View getView() {
        return this.view;
    }

    /// Setters and getters for node image, title and description ///

    public void addImage() {
        this.node_image = new NodeImage(this);
        this.node_image.setView(this.view);
        this.recalculateSize();
    }



    public void removeImage() {
        this.node_image = new NullNodeImage(this);
        this.node_image.setView(this.view);
        this.recalculateSize();
    }

    public void setImage(AppImage image) {
        this.node_image.setImage(image);
        this.node_image.recalculateSize();
        this.recalculateSize();
        this.updatePositions();
    }


    public AppImage getImage() {
        return this.node_image.image;
    }


    public void setTitle(String title) {
        this.title_entry.setText(title);
        this.recalculateSize();
        this.updatePositions();
    }


    public String getTitle() {
        return this.title_entry.text;
    }


    public void setSelected(boolean selected) {
        this.is_selected = selected;
    }


    public boolean hasDescription() {
        return !(this.description_entry instanceof NullDescriptionEntry);
    }


    public void addDescription() {
        this.description_entry = new DescriptionEntry(this);
        this.description_entry.setView(this.view);
        this.description_entry.recalculateSize();
        this.recalculateSize();
        this.updatePositions();
    }



    public void removeDescription() {
        this.description_entry = new NullDescriptionEntry(this);
        this.description_entry.setView(this.view);
        this.recalculateSize();
        this.updatePositions();
    }




    public String getDescription() {
        return this.description_entry.text;
    }


    public void setDescription(String description) {
        this.description_entry.setText(description);
        this.recalculateSize();
        this.updatePositions();
    }


    /// Code for positioning node and delegated methods. ///
    public void setPosition(Point position) {
        super.setPosition(position);
        this.updatePositions(); // Update positions of the contained widgets
    }


    public void updatePositions() {
        Point image_position = this.calcImagePosition();
        this.node_image.setPosition(image_position);

        Point title_entry_position = this.calcTitleEntryPosition();
        this.title_entry.setPosition(title_entry_position);

        Point description_entry_position = this.calcDescriptionEntryPosition();
        this.description_entry.setPosition(description_entry_position);
    }



    private Point calcImagePosition() {
        int top = this.position.y - this.getHeight() / 2;
        int position_y = top + PADDING + this.node_image.getHeight() / 2;
        return new Point(this.position.x, position_y);
    }


    private Point calcTitleEntryPosition() {
        int top = this.position.y - this.getHeight() / 2 + this.node_image.getHeight() + SPACING ;
        int position_y = top + this.title_entry.getHeight() / 2;
        return new Point(this.position.x, position_y);
    }


    private Point calcDescriptionEntryPosition() {
        int top = this.position.y - this.getHeight() / 2 + this.node_image.getHeight() + SPACING + this.title_entry.getHeight() + SPACING ;
        int position_y = top  + this.description_entry.getHeight() / 2;
        return new Point(this.position.x, position_y);
    }


    /// Width calculation methods ///
    public void recalculateSize() {
        this.size.width  = this.calcTotalWidth();
        this.size.height = this.calcTotalHeight();
    }


    private int calcTotalWidth() {
        return this.calcWidth() + 2 * PADDING;
    }

    private int calcWidth() {
        int width = MathUtils.max(
            this.node_image.getWidth(),
            this.title_entry.getWidth(),
            this.description_entry.getWidth()
        );
        return width;
    }

    private int calcTotalHeight() {
        return this.calcHeight() + 2 * PADDING;
    }

    private int calcHeight() {
        int height = 0;
        height += this.node_image.getHeight();
        if (!(this.node_image instanceof NullNodeImage)){
            height += SPACING;
        }
        height += this.title_entry.getHeight();
        if (!(this.description_entry instanceof NullDescriptionEntry)) {
            height += SPACING;
        }
        height += this.description_entry.getHeight();
        return height;
    }


    /// Event handlinge methods ///
    public void onClick(MouseEvent event) {
        Point mouse = new Point(event.getX(), event.getY());
        this.focus_widget = null;
        if (this.title_entry.containsPoint(mouse)){
            this.description_entry.loseFocus();
            this.title_entry.onClick(event);
            this.focus_widget = this.title_entry;
            this.title_entry.gainFocus();
        }
        else if (this.description_entry.containsPoint(mouse)){
            this.title_entry.loseFocus();
            this.description_entry.onClick(event);
            this.focus_widget = this.description_entry;
            this.description_entry.gainFocus();
        }
        else {
            this.title_entry.loseFocus();
            this.description_entry.loseFocus();
        }
        this.view.repaint();
    }



    public void focusOnTitleEntry() {
        this.description_entry.loseFocus();
        this.title_entry.gainFocus();
        this.focus_widget = this.title_entry;
    }


    public void gainFocus() {
        this.has_focus = true;
    }


    public void loseFocus() {
        this.title_entry.loseFocus();
        this.description_entry.loseFocus();
        this.has_focus = false;
    }


    public boolean keyPressed(KeyEvent event) {
        if (this.focus_widget != null) {
            this.focus_widget.keyPressed(event);
            return true;
        }
        return false;
    }


    public boolean keyReleased(KeyEvent event) {
        if (this.focus_widget != null) {
            this.focus_widget.keyReleased(event);
            return true;
        }
        return false;
    }


    public boolean keyTyped(KeyEvent event) {
        if (this.focus_widget != null) {
            this.focus_widget.keyTyped(event);
            return true;
        }
        return false;
    }



    public void onMouseOver(MouseEvent event) {
    }

    public void onMouseOut(MouseEvent event) {
    }


    public String toString() {
        if (this.getTitle().isEmpty()) return "<NODE>";
        else return this.getTitle();
    }
}

