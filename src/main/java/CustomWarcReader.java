import org.archive.io.ArchiveRecord;
import org.archive.io.warc.WARCReader;
import org.archive.io.warc.WARCReaderFactory;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class CustomWarcReader {

    private File[] files;

    /**
     * Reads the warc files that contains the <code>folder</code>
     * @param folder {@link File} folder with the .warc files
     */
    public CustomWarcReader(File folder) {
        files = getFilesOfFolder(folder);
    }

    /**
     * Reads and prints the warcs content
     */
    public void read() {
        printWarcs(files);
    }

    /**
     * Builds an array with the files on the <code>folder</code>
     * @param folder {@link File} folder with the .warc files
     * @return Array of warc {@link File}s
     */
    private File[] getFilesOfFolder(File folder) {
        File[] noFiles = {};
        return folder.isDirectory()? folder.listFiles(): noFiles;
    }

    /**
     * Prints on the default out the warc files content received as parameter
     * @param files Array of files
     */
    private void printWarcs(File[] files) {
        try {
            for (File item : files) {
                WARCReader warcReader = WARCReaderFactory.get(item);
                Iterator<ArchiveRecord> it = warcReader.iterator();
                while (it.hasNext()) {
                    ArchiveRecord ar = it.next();
                    System.out.println(ar.getHeader().toString());
                    ar.dump();
                }
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
        }
    }
}
