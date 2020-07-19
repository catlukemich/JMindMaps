package utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Resources {
    final int var = 0;

    static public BufferedImage loadImage(String path) {
        InputStream image_stream = Resources.class.getResourceAsStream(path);
        try {
            BufferedImage image = ImageIO.read(image_stream);
            return image;
        } catch (IOException e) {
            System.out.println("Can't load image: \"" + path + "\"");
            e.printStackTrace();
        }
        return null;
    }

}
