package map;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AutoScroller implements ActionListener {

    public AutoScroller() {
        this.view = view;
    }

    private View view;
    private Timer timer;
    private Node target;

    float x;
    float y;
    float step_dx;
    float step_dy;
    int step;


    public void scrollTo(View view, Node node) {
        this.view = view;
        if (this.timer != null) this.timer.stop();
        this.target = node;

        Point center = view.getCenter();
        this.x = center.x;
        this.y = center.y;

        Point node_position = node.getPosition();
        int dx = node_position.x - center.x;
        int dy = node_position.y - center.y;

        this.step_dx = dx / (float) 30;
        this.step_dy = dy / (float) 30;

        this.step = 0;

        this.timer = new Timer(16, this);
        this.timer.start();
    }

    public void actionPerformed(ActionEvent e) {
        this.x += this.step_dx;
        this.y += this.step_dy;
        this.step += 1;

        Point current_pos = new Point((int)this.x, (int)this.y);
        this.view.setCenter(current_pos);

        if (this.step == 30) {
            Point target_pos = new Point(this.target.getPosition());
            this.view.setCenter(target_pos);
            this.timer.removeActionListener(this);
        }
        this.view.repaint();
    }
}
