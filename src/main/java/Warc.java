import org.archive.io.ArchiveRecord;
import org.archive.io.ArchiveRecordHeader;

import java.io.Serializable;
import java.util.Iterator;

public class Warc implements Serializable {
    private String fileName;
    private ArchiveRecordHeader warcInfoHeader;
    private Iterator<ArchiveRecord> pages;

    public Warc(String fileName, ArchiveRecordHeader warcInfoHeader, Iterator<ArchiveRecord> pages) {
        this.fileName = fileName;
        this.warcInfoHeader = warcInfoHeader;
        this.pages = pages;
    }

    public Warc(Warc copy) {
        this.fileName = copy.getFileName();
        this.warcInfoHeader = copy.getWarcInfoHeader();
        this.pages = copy.getPages();
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ArchiveRecordHeader getWarcInfoHeader() {
        return warcInfoHeader;
    }

    public void setWarcInfoHeader(ArchiveRecordHeader warcInfoHeader) {
        this.warcInfoHeader = warcInfoHeader;
    }

    public Iterator<ArchiveRecord> getPages() {
        return pages;
    }

    public void setPages(Iterator<ArchiveRecord> pages) {
        this.pages = pages;
    }
}
