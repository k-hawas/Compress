import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Packager {

    File root;
    String rootName;
    OutputStream output;
    public Packager(String folder, OutputStream output) throws FileNotFoundException {
        root = new File(folder);
        if(!root.exists())
            throw new FileNotFoundException();
        if(!root.isDirectory())
            throw new IllegalArgumentException("Argument must be a directory.");

        this.output = output;
        this.rootName = root.getAbsolutePath();
    }




    public void pack() throws IOException{
        packFolder(root);
    }


    String getRelativeName(File file) {
        String s = file.getAbsolutePath();
        if(s.equals(rootName))
            return ("");
        s = s.substring(rootName.length() + 1);
        if(file.isDirectory())
            s += "/";
        s = s.replace('\\', '/');
        return s;
    }

    public void packFolder (File folder) throws IOException {
        if(folder != root)
            outputName(folder);
        String[] children = folder.list();
        for (String s : children) {
            File file = new File(String.format("%s/%s", folder.getAbsolutePath(), s));
            if(file.isDirectory())
                packFolder(file);
            else
                packFile(file);
        }
    }

    public void packFile (File file) throws IOException{
        outputName(file);
        outputData(file);
    }


    public void outputName (File file) throws IOException {
        String s = getRelativeName(file);
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        short sh = (short)bytes.length;
        outputShort(sh);
        output.write(bytes);
    }

    public void outputData (File file) throws IOException {
        int l = (int)file.length();
        outputInt(l);
        BufferedInputStream input = new BufferedInputStream(new FileInputStream(file));
        while (true) {
            int c = input.read();
            if(c == -1) {
                input.close();
                break;
            }
            output.write(c);
        }
    }

    public void outputInt(int i) throws IOException {
        byte[] b = new byte[4];
        b[0] = (byte)(i & 0xFF);
        b[1] = (byte)((i >> 8) & 0xFF);
        b[2] = (byte)((i >> 16) & 0xFF);
        b[3] = (byte)((i >> 24) & 0xFF);
        output.write(b);
    }

    public void outputShort(short s) throws IOException {
        byte[] b = new byte[2];
        b[0] = (byte)(s & 0xFF);
        b[1] = (byte)((s >> 8) & 0xFF);
        output.write(b);
    }

    public void close() throws IOException {
        output.flush();
        output.close();
    }


}
