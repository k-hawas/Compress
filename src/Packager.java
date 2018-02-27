import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;

public class PackingInputStream {

    File root;
    public PackingInputStream(String folder) throws FileNotFoundException {
        root = new File(folder);
        if(!root.exists())
            throw new FileNotFoundException();
    }



}
