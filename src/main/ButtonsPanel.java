package main;

import map.Mode;
import utils.Resources;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonsPanel extends JPanel implements ActionListener {
    public ButtonsPanel() {
        this.setBackground(new Color(250, 250, 250));
        Border border = BorderFactory.createLineBorder(Color.GRAY);
        this.setBorder(border);

        this.add_node_button = this.createButton( "Add node", "/add_button.png");
        this.add_node_button.addActionListener(this);
        this.add(this.add_node_button);

        this.select_node_button = this.createButton("Select node", "/select_button.png");
        this.select_node_button.addActionListener(this);
        this.add(this.select_node_button);

    }


    private JButton createButton(String text, String image_path) {
        JButton button = new JButton(text, Resources.loadIcon(image_path));
        button.setPreferredSize(new Dimension(100, 80));
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        return button;
    }

    JButton add_node_button;
    JButton select_node_button;

    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource();

        if (source == this.add_node_button) {
            App.instance.map_view.setMode(Mode.ADD_NODE);
            App.instance.status_bar.setText("Add node mode.");
        }
        if (source == this.select_node_button) {
            App.instance.map_view.setMode(Mode.SELECT_NODE);
            App.instance.status_bar.setText("Select mode.");
        }
    }
}
