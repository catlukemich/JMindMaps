package main;

import java.awt.image.BufferedImage;
import java.io.File;


// App image allows usage of original sized images and scaled down
// images to allow correct display in both the map view and the
// side panel node frame.
public class AppImage {

    public AppImage(File file, BufferedImage original, BufferedImage scaled) {
        this.file = file;
        this.original = original;
        this.scaled   = scaled;
    }

    File file;
    final public BufferedImage original;
    final public BufferedImage scaled;
}
