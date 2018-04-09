import org.archive.io.ArchiveRecord;
import org.archive.io.warc.WARCReader;
import org.archive.io.warc.WARCReaderFactory;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Iterator;

public class WarcReaderManager {

    private File[] files;

    public WarcReaderManager(File folder) {
        files = getFilesOfFolder(folder);
    }

    public void read() throws IOException, ParseException {
        printWarcs(files);
    }

    private File[] getFilesOfFolder(File folder) {
        File[] noFiles = {};
        return folder.isDirectory()? folder.listFiles(): noFiles;
    }

    private void printWarcs(File[] files) throws IOException, ParseException {
        for (File item: files) {
            WARCReader warcReader = WARCReaderFactory.get(item);
            Iterator<ArchiveRecord> it = warcReader.iterator();
            while (it.hasNext()) {
                it.next().dump();
            }
        }
    }
}
