package map;

import java.awt.*;

public class ViewDrawer {

    public void draw(View view, Graphics2D graphics) {
        Graphics2D graphics2d =  (Graphics2D) graphics;

        RenderingHints hints = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphics2d.setRenderingHints(hints);

        if (view.linking_state.link_start_node != null && view.linking_state.link_current_position != null) {
            graphics2d.setColor(Color.BLACK);
            Stroke previous_stroke = graphics2d.getStroke();
            graphics2d.setStroke(new BasicStroke(2));
            Point screen_start = view.positionToScreen(view.linking_state.link_start_node.getPosition());
            Point screen_end   = view.linking_state.link_current_position;
            graphics2d.drawLine(screen_start.x, screen_start.y, screen_end.x, screen_end.y);
            graphics2d.setStroke(previous_stroke);
        }

        ViewModel model = view.getModel();

        for(Connection connection : model.connections) {
            connection.paint(graphics2d);
        }

        for (Node node: model.nodes) {
            node.paint(graphics2d);
        }

        if(view.buttons_menu.isVisible()) {
            view.buttons_menu.paint(graphics2d);
        }
    }
}
