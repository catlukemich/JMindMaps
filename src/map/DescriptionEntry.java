package map;

import main.App;
import main.Globals;
import utils.MathUtils;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class DescriptionEntry extends Widget {

    static int MAX_WIDTH = 300;
    static private String DESCRIPTION = "Description";


    public DescriptionEntry(Node node) {
        this.node = node;
        this.font = new Font("Arial", 0, 12);
    }

    Node   node;
    Font   font;
    String text = "";

    boolean has_focus = false;
    int caret_index;

    private ArrayList<Line> lines = new ArrayList<>();

    public void paint(Graphics2D graphics) {
        Rectangle bounds = this.calcBounds();

        graphics.setFont(this.font);

        if (this.text.isEmpty()) {
            graphics.setColor(Color.LIGHT_GRAY);
            int x = bounds.x;
            int y = bounds.y + this.getLineHeight() - this.getFontDescent();
            graphics.drawString(DESCRIPTION , x, y);
        }

        // Draw the text:
        graphics.setColor(Color.BLACK);
        int text_width = this.calcTextWidth();
        int current_y = bounds.y;
        for (Line line : this.lines) {
            int current_x = bounds.x  + (text_width - line.width) / 2;
            int y = current_y + this.getLineHeight() - this.getFontDescent();
            graphics.drawString(line.text, current_x, y);
            current_y += this.getLineHeight();
        }


        // Draw the caret if this widget has focus:
        if (this.has_focus) {
            Point caret_position = this.getCaretPosition();
            graphics.drawRect(caret_position.x, caret_position.y, 1, this.getStringHeight(this.text) );
        }
    }


    public void setText(String text) {
        this.text = text;
        this.createLines();
        this.recalculateSize();
        this.node.recalculateSize();
        this.node.updatePositions();
        this.view.repaint();
    }


    private void createLines() {
        this.lines = new ArrayList<>();
        String current_text = "";
        String[] words = this.text.split(" ");

        int line_start_index = 0;

        for(int i = 0; i < words.length; i++) {
            String word = words[i];
            current_text += word + " ";
            int width_next;
            try {
                String next_word = words[i + 1];
                width_next = this.getStringWidth(current_text + " " + next_word);
            }
            catch (ArrayIndexOutOfBoundsException e) {
                width_next = 0;
            }
            if (width_next > MAX_WIDTH || width_next == 0) {
                int line_width = this.getStringWidth(current_text);
                Line line = new Line(current_text, line_start_index, i, line_width);
                this.lines.add(line);
                line_start_index = i;
                current_text = "";
            }
        }
    }


    public void recalculateSize() {
        int width = this.calcTextWidth();
        int height = this.calcTextHeight();
        width = Math.max(width, this.getStringWidth(DESCRIPTION));
        height = Math.max(height, this.getStringHeight(DESCRIPTION));
        this.size.width  = width;
        this.size.height = height;
    }


    private int calcTextWidth() {
        int width = 0;
        for (Line line : this.lines) {
            width = Math.max(line.width, width);
        }

        int minimal_width = this.getStringWidth(DESCRIPTION);
        width = Math.max(width, minimal_width);
        return width;
    }


    public int getLineHeight() {
        return this.getStringHeight(this.text);
    }


    private int calcTextHeight() {
        int height = 0;
        for (Line line : this.lines) {
            height += this.getLineHeight();
        }
        return height;
    }


    private int getStringWidth(String string) {
        return Globals.graphics.getFontMetrics(this.font).stringWidth(string);
    }


    private int getStringHeight(String string) {
        return (int) this.font.getLineMetrics(string, Globals.graphics.getFontRenderContext()).getHeight();
    }

    private int getFontDescent() {
        return (int) this.font.getLineMetrics(this.text, Globals.graphics.getFontRenderContext()).getDescent();
    }


    public void onClick(MouseEvent event) {
        Point mouse = new Point(event.getX(), event.getY());
        this.caret_index = this.getCaretIndex(mouse);
        this.view.repaint();
    }


    public void gainFocus() {
        this.has_focus = true;
    }


    public void loseFocus() {
        this.has_focus = false;
    }


    public boolean keyPressed(KeyEvent event) {
        if (event.getKeyCode() == 37) {
            // Left arrow was pressed:
            this.caret_index -= 1;
        }
        else if(event.getKeyCode() == 39) {
            // Right arrow was pressed:
            this.caret_index += 1;
        }
        else if (event.getKeyCode() == 8 && this.caret_index >= 1) {
            // Backspace was pressed:
            String text_before = this.text.substring(0, this.caret_index - 1);
            String text_after  = this.text.substring(this.caret_index);
            String new_text    = text_before + text_after;
            this.setText(new_text);
            this.caret_index -= 1;
        }
        else if (event.getKeyCode() == 127 && this.caret_index < this.text.length()) {
            // Delete key was pressed:
            String text_before = this.text.substring(0, this.caret_index);
            String text_after  = this.text.substring(this.caret_index + 1);
            String new_text    = text_before + text_after;
            this.setText(new_text);
        }
        else if (event.getKeyCode() == 32) {
            // Space bar was pressed:
            String text_before = this.text.substring(0, this.caret_index);
            String text_after  = this.text.substring(this.caret_index);
            String new_text    = text_before + " " +  text_after;
            this.caret_index += 1;
            this.setText(new_text);
        }
        else if (event.getKeyCode() == 38) {
            // Up arrow was pressed:
            Point caret_position = this.getCaretPosition();
            caret_position.y = caret_position.y - this.getLineHeight() + this.getLineHeight() / 2;
            this.caret_index = this.getCaretIndex(caret_position);
        }
        else if (event.getKeyCode() == 40) {
            // Down arrow was pressed:
            Point caret_position = this.getCaretPosition();
            caret_position.y = caret_position.y + this.getLineHeight() + this.getLineHeight() / 2;
            this.caret_index = this.getCaretIndex(caret_position);
        }

        if (this.caret_index < 0) this.caret_index = 0;
        if (this.caret_index > this.text.length()) this.caret_index = this.text.length();

        this.createLines();
        this.recalculateSize();


        this.view.repaint();

        // At this point the node panel node is set to the current node, so we can call to update the current state:
        App.instance.side_panel.updateNodePanel();

        return true;
    }


    public boolean keyTyped(KeyEvent event) {
        char character = event.getKeyChar();
        if (!Character.isAlphabetic(character)) return false;
        String string_before = this.text.substring(0, this.caret_index);
        String string_after  = this.text.substring(this.caret_index);
        String new_text = string_before + character + string_after;
        this.setText(new_text);
        this.caret_index += 1;

        if (this.caret_index < 0) this.caret_index = 0;
        if (this.caret_index > this.text.length()) this.caret_index = this.text.length();

        // At this point the node panel node is set to the current node, so we can call to update the current state:
        App.instance.side_panel.updateNodePanel();

        return true;
    }


    public boolean keyReleased(KeyEvent event) {
        return false;
    }


    protected Point getCaretPosition() {
        Rectangle bounds = this.calcBounds();
        int current_y = bounds.y;

        int char_index = 0;
        Line line = null;
        for(int line_index = 0; line_index < this.lines.size(); line_index++) {
            line = this.lines.get(line_index);
            int current_x = bounds.x + (bounds.width - line.width) / 2;
            for(int i = 0; i < line.text.length(); i++) {
                if(char_index == this.caret_index) {
                    return new Point(current_x, current_y);
                }
                char character = line.text.charAt(i);
                current_x += this.getStringWidth(String.valueOf(character));
                char_index += 1;
            }

            current_y += this.getLineHeight();
        }
        if (line != null) {
            int last_line_end_x = bounds.x + (bounds.width - line.width) / 2 + line.width;
            int last_line_end_y = current_y - this.getLineHeight();
            return new Point(last_line_end_x, last_line_end_y);
        }
        else {
            return new Point(bounds.x, bounds.y);
        }
    }


    // Calculate the index of caret given a position, this function will return an approximate caret index used
    // when clicking on this widget, to position the caret under the mouse cursor.
    private int getCaretIndex(Point position) {
        Rectangle bounds = this.calcBounds();
        if(position.y < bounds.y) {
            return 0;
        }
        else if (position.y > bounds.y + bounds.height) {
            return this.text.length();
        }


        int current_y = bounds.y;

        int char_index = 0;

        for (Line line : this.lines) {
            int line_height = this.getLineHeight();
            if (position.y > current_y && position.y < current_y + line_height) {
                int left = bounds.x + (bounds.width - line.width) / 2 ;
                int right = left + line.width;
                if (position.x < left ) {
                    return char_index;
                }
                if (position.x > right) {
                    return char_index + line.text.length() - 1;
                }

                int caret_index = 0;
                int current_width = bounds.x + (bounds.width - line.width) / 2;
                while(true) {
                    try {
                        String character = String.valueOf(line.text.charAt(caret_index));
                        int character_width = this.getStringWidth(character);
                        current_width += character_width ;
                        if (current_width - character_width / 2 > position.x) return char_index + caret_index;;
                        caret_index += 1;

                        // If we are beyond the string return what's left:
                        if (caret_index == this.text.length()) {
                            return caret_index;
                        }
                    }
                    catch (StringIndexOutOfBoundsException e) {
                        break;
                    }
                }
            }

            current_y += this.getLineHeight();
            char_index += line.text.length();
        }
        return 0;
    }



    static private class Line {
        public Line(String text, int start_index, int end_index, int width){
            this.text = text;
            this.start_index = start_index;
            this.end_index   = end_index;
            this.width = width;
        }


        String text;
        int start_index;
        int end_index;
        int width;
    }


}
