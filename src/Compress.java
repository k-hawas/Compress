import java.io.IOException;

public class Compress {

    public static void main(String[] args) {
        SplitFileOutputStream f = new SplitFileOutputStream("F:/Work/Compress/file", 100);

        try {
            for (int i = 0; i < 1024 * 1024; i++) {
                f.write('X');
            }
        }catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }


}
