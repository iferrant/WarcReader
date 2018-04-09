import java.io.*;
import java.text.ParseException;

public class Main {

    public static void main(String[] args) throws IOException, ParseException {
        WarcWriterManager warcWriterManager = new WarcWriterManager(new File("./_ham_"));
        warcWriterManager.writeWarcs();

        WarcReaderManager warcReaderManager = new WarcReaderManager(new File("./_ham_copy"));
        warcReaderManager.read();
    }
}
