import java.io.*;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

public class Compress {

    public static void main(String[] args) {
        String op;
        String input;
        String output;
        int size = 0;

        if(args.length < 3) {
            printUsage();
            return;
        }

        op = args[0];
        input = args[1];
        output = args[2];

        if(op.equals("compress")) {

            if (args.length < 4) {
                printUsage();
                return;
            }

            try {
                size = Integer.parseInt(args[3]);
            }catch(NumberFormatException e) {
                System.out.println("Size must be numeric");
            }
        }

        if(op.equals("compress"))
            compress(input, output, size);
        else
            uncompress(input, output);

    }

    private static void compress(String input, String output, int size) {
        if(size < 1) {
            System.out.println("size must be >= 1");
            return;
        }
        File iroot = new File(input);
        if(!iroot.exists()) {
            System.out.println("Can't find input directory.");
            return;
        }

        File oroot = new File(output);
        if(!oroot.exists()) {
            System.out.println("Can't find output directory.");
        }

        OutputStream os = new DeflaterOutputStream(new SplitFileOutputStream(output, size));

        try {
            Packager p = new Packager(input, os);
            p.pack();
            p.close();
        }catch(Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }


    private static void uncompress(String input, String output) {
        File iroot = new File(input);
        if(!iroot.exists()) {
            System.out.println("Can't find input directory.");
            return;
        }

        File oroot = new File(output);
        if(!oroot.exists()) {
            System.out.println("Can't find output directory.");
        }

        try {
            InputStream is = new InflaterInputStream(new SplitFileInputStream(input));
            Unpackager p = new Unpackager(output, is);
            p.unpack();
        }catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }


    private static void printUsage() {
        System.out.println("Usage:");
        System.out.println("Compress compress <directory-to-compress> <output dir> <filesize in KB>");
        System.out.println("Compress uncompress <input dir> <output dir>");
    }


    private static void test() {
        try {
//            Packager packager = new Packager("F:/Work/Compress - Copy", new FileOutputStream("F:/Work/package"));
            OutputStream os = new DeflaterOutputStream(new SplitFileOutputStream("F:/Work/output", 10));
            Packager packager = new Packager("F:/Work/Compress - Copy", os);
            packager.pack();
            packager.close();

//            Unpackager unpackager = new Unpackager("F:/Work/Compress_Copy", new FileInputStream("F:/Work/package"));
            Unpackager unpackager = new Unpackager("F:/Work/Compress_Copy", new InflaterInputStream(new SplitFileInputStream("F:/Work/output")));
            unpackager.unpack();


        }catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }

    }

}
