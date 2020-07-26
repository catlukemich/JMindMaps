package main;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class StatusBar extends JPanel {

    public StatusBar() {
        this.text_label = new JLabel();
        this.add(this.text_label);
        this.setPreferredSize(new Dimension(Integer.MIN_VALUE, 24));
        Border border = BorderFactory.createLineBorder(Color.GRAY);
        this.setBorder(border);
        this.setText("Select mode.");
    }

    JLabel text_label;


    public void setText(String text) {
        this.text_label.setText(text);
    }

}
