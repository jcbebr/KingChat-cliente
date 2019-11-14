package control;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author José Carlos
 */
public class Defs {

    private String path;
    private int port;
    private static Defs instance;

    public Defs() {
        try (InputStream input = new FileInputStream("./defs.properties")) {
            Properties prop = new Properties();
            prop.load(input);

            this.path = prop.getProperty("path");
            this.port = Integer.parseInt(prop.getProperty("port"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static Defs getInstance() {
        if (instance == null) {
            instance = new Defs();
        }
        return instance;
    }

    public String getPath() {
        return path;
    }

    public int getPort() {
        return port;
    }

}
