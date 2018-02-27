import java.io.*;
import java.util.zip.DeflaterInputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class Compress {

    public static void main(String[] args) {

        try {
//            Packager packager = new Packager("F:/Work/Compress - Copy", new FileOutputStream("F:/Work/package"));
            OutputStream os = new DeflaterOutputStream(new FileOutputStream("F:/Work/package"));
            Packager packager = new Packager("F:/Work/Compress - Copy", os);
            packager.pack();
            packager.close();

//            Unpackager unpackager = new Unpackager("F:/Work/Compress_Copy", new FileInputStream("F:/Work/package"));
            Unpackager unpackager = new Unpackager("F:/Work/Compress_Copy", new InflaterInputStream(new FileInputStream("F:/Work/package")));
            unpackager.unpack();


        }catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }


}
