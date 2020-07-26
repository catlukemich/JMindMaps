package main;

import org.w3c.dom.*;
import utils.Resources;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;

public class ImageRepository {

    // All the images loaded from cache file and added to the cache after loading:
    static private ArrayList<AppImage> images = new ArrayList<>();


    static public ArrayList<AppImage> getImages() {
        return images;
    }


    static public void addImage(AppImage image) {
        // Check if image with the specified path is not loaded into the cache:
        boolean already_exists = false;
        for (AppImage cached_image : images) {
            if (cached_image.path.equals(image.path)) {
                already_exists = true;
            }
        }
        if (!already_exists) images.add(image);
    }

    // Read the cache from the cache file:
    static public void readRepository(){
        File cache_file = getCacheFile();
        if (cache_file.exists()) {
            try {
                // Read the cache acutally
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document document = builder.parse(cache_file);
                NodeList images_nodes = document.getElementsByTagName("image");
                for (int i = 0; i < images_nodes.getLength(); i++) {
                    Node item = images_nodes.item(i);
                    String image_path = item.getTextContent();
                    File image_file = new File(image_path);
                    if (image_file.exists()){
                        AppImage image = Resources.loadAppImage(image_path);
                        images.add(image);
                    }
                }
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    };


    static public void writeRepository() {
        try {
            File cache_dir = getCacheDirectory();
            File cache_file = getCacheFile();
            if (!cache_dir.exists()) {
                cache_dir.mkdirs();
                if (!cache_file.exists()) {
                    cache_file.createNewFile();
                }
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document document = builder.newDocument();
            Element root = document.createElement("images");
            document.appendChild(root);
            for(AppImage image : images) {
                Element image_elem = document.createElement("image");
                Text path_elem = document.createTextNode(image.path);
                image_elem.appendChild(path_elem);
                root.appendChild(image_elem);
            }

            TransformerFactory transformer_factory = TransformerFactory.newInstance();
            Transformer transformer = transformer_factory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(cache_file);
            transformer.transform(source, result);
        }
        catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }


    static private File getCacheFile() {
        String home_dir_name = System.getProperty("user.home");
        File cache_file = new File(home_dir_name + File.separator + "MindMaps" + File.separator + "images.xml");
        return cache_file;
    }


    static private File getCacheDirectory() {
        String home_dir_name = System.getProperty("user.home");
        File cache_dir = new File(home_dir_name + File.separator + "MindMaps");
        return cache_dir;
    }


    public static void main(String[] args) {
        readRepository();
    }
}


