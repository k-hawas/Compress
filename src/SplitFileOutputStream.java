import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 *  File Output Stream that split file into chunks with max size.
 *
 *  This class provide an implementation to OutputStream that writes to a sequence of files with
 *  maximum specified size.
 *  Note that here there is no concept for "filename", as this stream writes to multiple OS files.
 *  Note that unlike FileOutputStream, which throws FileNotFound exception on creation only, this
 *  class can throw this exception on write, if for some reason it cannot open the part's file.
 *  It's the responsibility of the caller to make sure that part names do not conflict with path's
 *  current content.
 *  It is usually better to specify an empty folder for path.
 */

public class SplitFileOutputStream extends OutputStream {

    private int index = 0;
    private long totalWritten = 0;
    private String path;
    private String prefix;
    private long size;
    private FileOutputStream current;


    /** Creates an instance that writes to specified path.
     *
     * @param path      Path to generated files
     */
    public SplitFileOutputStream(String path) {
        this(path, 1024, "filepart");
    }

    public SplitFileOutputStream(String path, long size) {
        this(path, size, "filepart");
    }


    /** Creates a instance with the specified path and prefix
     *
     * Files are created using the names (prefix).001, (prefix).002, etc.
     * Note that unlike FileOutputStream, no files are actually opened or accessed in the constructor.
     * @param path      Path to generated files
     * @param size      Size of File part.
     * @param prefix    Prefix used in file names.
     */
    public SplitFileOutputStream(String path, long size, String prefix) {
        this.path = path;
        this.size = size * 1024;
        this.prefix = prefix;
    }


    private String getNextFilename() {
        String name = String.format("%s/%s.%03d", path, prefix, index);
        index++;
        return name;
    }


    @Override
    public void write(int b) throws IOException {
        if (totalWritten % size == 0) {
            if(current != null)
                current.close();
            current = new FileOutputStream(getNextFilename());
        }
        current.write(b);
        totalWritten++;
    }


    public void close() throws IOException {
        if(current != null)
            current.close();
    }
}
