import java.io.*;
import java.nio.charset.StandardCharsets;

public class Unpackager {

    String pathName;
    File path;
    InputStream input;

    public Unpackager(String path, InputStream input) throws FileNotFoundException {
        this.input = input;
        this.pathName = path.replace('\\', '/');

        if(!this.pathName.endsWith("/"))
            this.pathName = this.pathName + "/";
        this.path = new File(pathName);
        if(!this.path.exists())
            throw new FileNotFoundException();
        if(!this.path.isDirectory())
            throw new IllegalArgumentException("Path must be a directory.");
    }


    public void unpack() throws IOException {
        while(true) {
            String s = inputName();
            if(s == null)
                break;
            System.out.println(s);
            if (s.endsWith("/"))  // directory
                unpackFolder(s);
            else
                unpackFile(s);
        }
    }


    private void unpackFolder(String name) throws IOException {
        String absoluteName = pathName + name;
        File dir = new File(absoluteName);
        dir.mkdir();
    }

    private void unpackFile(String name) throws IOException {
        String absoluteName = pathName + name;
        BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(absoluteName));
        copyData(output);
        output.flush();
        output.close();
    }

    private String inputName() throws IOException {
        short s = inputShort();
        if(s == 0) // end of archive
            return null;
        byte[] b = new byte[s];
        for(int i = 0 ; i < s ; i++)
            b[i] = (byte)input.read();
        return (new String(b , StandardCharsets.UTF_8));
    }


    private void copyData(OutputStream output) throws IOException {
        int total = inputInt();
        for(int i = 0 ; i < total ; i++)
            output.write(input.read());
    }


    private int inputInt() throws IOException {
        byte[] b = new byte[4];
    //    input.read(b);
        b[0] = (byte)input.read();
        b[1] = (byte)input.read();
        b[2] = (byte)input.read();
        b[3] = (byte)input.read();
        int i = 0;
        i |= (int)b[0] & 0xFF;
        i |= ((int)b[1] << 8) & 0xFF00;
        i |= ((int)b[2] << 16) & 0xFF0000;
        i |= ((int)b[3] << 24) & 0xFF000000;
        return i;
    }

    private short inputShort() throws IOException {
        byte[] b = new byte[2];
        b[0] = (byte)input.read();
        b[1] = (byte)input.read();
        short i = 0;
        i += b[0];
        i += b[1] << 8;
        return i;
    }



}
