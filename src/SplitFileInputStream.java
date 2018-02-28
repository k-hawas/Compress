import java.io.*;

public class SplitFileInputStream extends InputStream {

    String prefix;
    int index;
    File root;
    String path;
    InputStream input;

    public SplitFileInputStream(String path) throws FileNotFoundException {
        root = new File(path);
        if(!root.exists())
            throw new FileNotFoundException();
        if(!root.isDirectory())
            throw new IllegalArgumentException("Path must be a directory.");

        this.path = path + "/";

        String[] files = root.list();

        if(files.length < 1)
            throw new FileNotFoundException();

        String first = files[0];
        int idx = first.indexOf('.');
        prefix = first.substring(0, idx);
        input = new BufferedInputStream(new FileInputStream(this.path + prefix + ".000"));
    }


    @Override
    public int read() throws IOException {
        int c = input.read();
        if(c == -1) {
            input.close();
            String next = getNextFileName();
            try {
                input = new BufferedInputStream(new FileInputStream(next));
                System.out.println("<<< " + next + " >>>");
            }catch (Exception e) {
                return -1;
            }
            return(input.read());
        }
        else
            return c;
    }

    private String getNextFileName() {
        index++;
        return String.format("%s%s.%03d", path, prefix, index);
    }

}
