package utils;

import main.AppImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Resources {

    static public AppImage loadAppImage(String path) {
        File file = new File(path);
        BufferedImage original = loadImage(file);
        BufferedImage scaled = scaleImage(original, 200, 400);
        return new AppImage(file, original, scaled);
    }


    static public BufferedImage loadImage(File file) {
        try {
            BufferedImage image = ImageIO.read(file);
            return image;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Can't load file image: " + file.getAbsolutePath());
        }
    }


    static public BufferedImage loadImage(String path) {
        InputStream image_stream = Resources.class.getResourceAsStream(path);
        try {
            BufferedImage image = ImageIO.read(image_stream);
            return image;
        } catch (IOException e) {
            System.out.println("Can't load image: \"" + path + "\"");
            throw new RuntimeException(e);
        }
    }


    static public BufferedImage scaleImage(BufferedImage image, int max_width, int max_height) {
        int img_width = image.getWidth();
        int img_height = image.getHeight();

        float horizontal_ratio = 1;
        float vertical_ratio    = 1;


        if(img_height > max_height) {
            vertical_ratio = (float)max_height / (float)img_height;
        }
        if(img_width > max_width) {
            horizontal_ratio = (float)max_width / (float)img_width;
        }

        float scale_ratio = 1;

        if (vertical_ratio < horizontal_ratio) {
            scale_ratio = vertical_ratio;
        }
        else if (horizontal_ratio < vertical_ratio) {
            scale_ratio = horizontal_ratio;
        }

        int dest_width  = (int) (img_width * scale_ratio);
        int dest_height = (int) (img_height * scale_ratio);

        BufferedImage scaled = new BufferedImage(dest_width, dest_height, image.getType());
        Graphics graphics = scaled.getGraphics();
        graphics.drawImage(image, 0, 0, dest_width, dest_height, null);
        graphics.dispose();

        return scaled;

    }


    static public Icon loadIcon(String path) {
        BufferedImage image = loadImage(path);
        ImageIcon icon = new ImageIcon(image);
        return icon;
    }

}
