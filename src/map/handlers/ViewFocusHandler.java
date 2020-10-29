package map.handlers;

import main.App;
import main.commands.NewNodeCommand;
import map.*;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.MouseEvent;

public class ViewFocusHandler {

    public boolean mousePressed(View view, MouseEvent event) {
        view.buttons_menu.onMousePress(event);
        return false;
    }

    public void mouseClicked(View view, MouseEvent event) {
        ViewModel model = view.getModel();
        if (view.mode == Mode.SELECT_NODE) {
            if (event.getButton() != 1) return;

            Point mouse = new Point(event.getX(), event.getY());
            boolean node_clicked = false;
            for(Node node : model.nodes) {
                if (node.containsPoint(mouse)) {
                    view.setSelectedNode(node, true);

                    if (node != view.focus_state.focus_object) {
                        if (view.focus_state.focus_object != null) {
                            view.focus_state.focus_object.loseFocus();
                        }
                        view.focus_state.focus_object = node;
                        App.instance.side_panel.displayNodePanel();
                        App.instance.side_panel.setNode(node);
                        view.focus_state.focus_object.gainFocus();
                    }
                    node.onClick(event);
                    node_clicked = true;
                    break;
                }
            }


            if (view.buttons_menu.containsPoint(mouse)) {
                view.buttons_menu.onClick(event);
            }

            if(view.hover_state.hover_connection != null) {
                Focusable new_focus_object = view.hover_state.hover_connection;
                if (new_focus_object != view.focus_state.focus_object) {
                    if (view.focus_state.focus_object != null) {
                        view.focus_state.focus_object.loseFocus();
                    }
                    if (new_focus_object != null) {
                        new_focus_object.gainFocus();
                    }
                }
                view.focus_state.focus_object = new_focus_object;
            }

            if (!node_clicked) {
                view.setSelectedNode(null, false);
                int click_time = (int) (System.nanoTime() / 1_000_000);
                if (click_time - view.last_click_time < 250) {
                    this.onDoubleClick(view, event);
                }
                view.last_click_time = click_time;
            }
        }
        else if (view.mode == Mode.ADD_NODE) {
            Point mouse = new Point(event.getX(), event.getY());
            Point position = view.screenToPosition(mouse);
            Node node = view.insertNewNode(position);

            App.instance.history.addCommand(new NewNodeCommand(node));

            // Update the nodes panel with new node:
            App.instance.side_panel.updateNodesPanel();
        }
    }

    public void onDoubleClick(View view, MouseEvent event) {
        Point mouse = new Point(event.getX(), event.getY());
        Point position = view.screenToPosition(mouse);
        Node node = view.insertNewNode(position);

        App.instance.history.addCommand(new NewNodeCommand(node));

        // Update the nodes panel with new node:
        App.instance.side_panel.updateNodesPanel();
    }

    public void focusGained(View view, FocusEvent e) {
        Focusable focus_object = view.focus_state.focus_object;
        if (focus_object != null) {
            focus_object.gainFocus();
        }
        view.repaint();
    }

    public void focusLost(View view, FocusEvent e) {
        Focusable focus_object = view.focus_state.focus_object;
        if(focus_object != null) {
            focus_object.loseFocus();
        }
        view.repaint();
    }
}
