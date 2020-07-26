package utils;

import java.io.File;

public class FileUtils {

    static public String getFileExtension(File file) {
        String name = file.getName();
        int last_dot = name.lastIndexOf(".");
        if (last_dot == -1) {
            return "";
        }
        String extension =  name.substring(last_dot + 1);
        return extension;
    }

}
