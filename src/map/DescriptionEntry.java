package map;

import main.Globals;
import utils.MathUtils;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class DescriptionEntry extends Widget {

    static int MAX_WIDTH = 300;


    public DescriptionEntry(Node node) {
        this.node = node;
        this.font = new Font("Arial", 0, 20);
    }

    Node   node;
    Font   font;
    String text = "";

    boolean has_focus = false;
    int caret_index;

    private ArrayList<Line> lines = new ArrayList<>();

    public void paint(Graphics2D graphics) {
        Rectangle bounds = this.calcBounds();

        // Draw the text:
        graphics.setFont(this.font);
        int text_width = this.calcTextWidth();
        int current_x = bounds.x;
        int current_y = bounds.y;
        for (Line line : this.lines) {
            current_x = bounds.x  + (text_width - line.width) / 2;
            int y = current_y + this.getLineHeight() - this.getFontDescent();
            graphics.drawString(line.text, current_x, y);
            current_y += this.getLineHeight();
        }


        // Draw the caret if this widget has focus:
        if (this.has_focus) {
            Point caret_position = this.getCaretPosition();
            graphics.drawRect(caret_position.x, caret_position.y, 2, this.getStringHeight(this.text) );
            System.out.println(caret_position);
        }
    }


    public void setText(String text) {
        this.text = text;
        this.createLines();
        this.recalculateSize();
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


    private void recalculateSize() {
        this.size.width  = this.calcTextWidth();
        this.size.height = this.calcTextHeight();
    }


    private int calcTextWidth() {
        int width = 0;
        for (Line line : this.lines) {
            width = Math.max(line.width, width);
        }
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

    }

    public void gainFocus() {
        this.has_focus = true;
    }

    public void loseFocus() {
        this.has_focus = false;
    }

    public void keyPressed(KeyEvent event) {
        if (event.getKeyCode() == 37) {
            // Left arrow was pressed:
            this.caret_index -= 1;
            this.view.repaint();

        }
        else if(event.getKeyCode() == 39) {
            // Right arrow was pressed:
            this.caret_index += 1;
            this.view.repaint();
        }

        if (this.caret_index < 0) this.caret_index = 0;
        if (this.caret_index > this.text.length()) this.caret_index = this.text.length();
    }

    public void keyReleased(KeyEvent event) {

    }

    public void keyTyped(KeyEvent event) {

    }

    private Point getCaretPosition() {
        Rectangle bounds = this.calcBounds();
        int current_y = bounds.y;

        int char_index = 0;
        for(Line line : this.lines) {

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
        return null;
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
