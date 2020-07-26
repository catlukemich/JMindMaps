package main;

import java.awt.image.BufferedImage;


// App image allows usage of original sized images and scaled down
// images to allow correct display in both the map view and the
// side panel node frame.
public class AppImage {

    public AppImage(String path, BufferedImage original, BufferedImage scaled) {
        this.path = path;
        this.original = original;
        this.scaled   = scaled;
    }

    String path;
    final public BufferedImage original;
    final public BufferedImage scaled;
}
