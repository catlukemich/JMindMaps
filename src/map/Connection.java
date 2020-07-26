package map;

import java.awt.*;
import java.util.Collection;

public class Connection implements Focusable {

    public Connection(Node node_1, Node node_2) {
        this.node_1 = node_1;
        this.node_2 = node_2;
    }

    View view;
    public Node node_1;
    public Node node_2;

    private float width = 0.5f;
    private boolean has_focus = false;


    public void setView(View view) {
        this.view = view;
    }

    public void paint(Graphics2D graphics) {
        Point point_1 = this.view.positionToScreen(node_1.getPosition());
        Point point_2 = this.view.positionToScreen(node_2.getPosition());

        graphics.setColor(Color.BLACK);
        Stroke previous_stroke = graphics.getStroke();
        graphics.setStroke(new BasicStroke(this.width));
        graphics.drawLine(point_1.x, point_1.y, point_2.x, point_2.y);
        graphics.setStroke(previous_stroke);
    }


    public boolean isPointNear(Point point) {
        Point pos_1 = this.node_1.getPosition();
        Point pos_2 = this.node_2.getPosition();

        int range_h_start = pos_1.x < pos_2.x ? pos_1.x : pos_2.x - 2;
        int range_h_end = pos_1.x > pos_2.x ? pos_1.x : pos_2.x + 2;

        int range_v_start = pos_1.y < pos_2.y ? pos_1.y : pos_2.y - 2;
        int range_v_end = pos_1.y > pos_2.y ? pos_1.y : pos_2.y + 2;

        boolean is_horizontal_on = point.x >= range_h_start && point.x <= range_h_end;
        boolean is_vertical_on   = point.y >= range_v_start && point.y <= range_v_end;


        return this.distanceToPoint(point) < 4 && is_horizontal_on && is_vertical_on;
    }


    private float distanceToPoint(Point point) {
        Point start = this.node_1.getPosition();
        Point end   = this.node_2.getPosition();

        float AB = distance(point, start);
        float BC = distance(start, end);
        float AC = distance(point, end);

        float s = (AB + BC + AC) / 2;
        float area = (float) Math.sqrt((s * (s - AB) * (s - BC) * (s - AC)));
        float AD = (2 * area) / BC;
        return AD;
    }


    static private float distance(Point a, Point b) {
        float dx = b.x - a.x;
        float dy = b.y - a.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }


    public void onMouseOver() {
        if (!this.has_focus) {
            this.width = 2;
        }
    }

    public void onMouseOut() {
        if (!this.has_focus) {
            this.width = 0.5f;
        }
    }


    public void gainFocus() {
        this.has_focus = true;
        this.width = 2;
    }


    public void loseFocus() {
        this.has_focus = false;
        this.width = 0.5f;
    }

}
