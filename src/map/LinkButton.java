package map;

import utils.Resources;

import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class LinkButton extends Button {

    static private BufferedImage link_image = Resources.loadImage("/link_button.png");


    public LinkButton(NodeMenu buttons) {
        super(buttons, link_image);
    }


    public void onMousePress(MouseEvent event) {
        super.onMousePress(event);
        View view = this.buttons.node.view;
        Node start_node = this.buttons.node;
        view.linking_state.setStartNode(start_node);
    }


    public void onMouseRelease(MouseEvent event) {
        super.onMouseRelease(event);
        View view = this.buttons.node.view;
        view.linking_state.setStartNode(null);
    }


}
