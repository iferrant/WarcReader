import org.archive.io.ArchiveRecordHeader;

import org.archive.io.ArchiveRecord;
import org.archive.io.warc.WARCReader;
import org.archive.io.warc.WARCReaderFactory;

import java.io.*;
import java.text.ParseException;
import java.util.*;

public class WarcWriterManager {
    private final String WARC_VERSION = "WARC/1.0";
    private final String HEADER_WARCINFO_ALL_LANGUAGE = "WARC-All-Language";
    private final String HEADER_WARCINFO_ALL_CONTENT_TYPE = "WARC-All-Content_type";
    private final String HEADER_LANGUAGE = "WARC-language";
    private final String HEADER_CONTENT_TYPE = "Content-Type";
    private final String HEADER_CONTENT_LENGTH = "Content-Length";
    private final String[] warcinfoheaderkeys = {"WARC-Type", "WARC-Date", "WARC-Record-ID",
            "WARC-All-Language", "WARC-All-Content_type", "Content-Type", "Content-Length"};
    private final String[] responseheaderkeys = {"WARC-Type", "WARC-Record-ID", "WARC-Date",
            "WARC-Target-URI", "WARC-language", "Content-Type", "Content-Length"};

    private Map<String, List<CustomHeader>> customHeaders = new HashMap<>();

    private File[] files;

    public WarcWriterManager(File folder) {
        files = getFilesOfFolder(folder);
    }

    public void writeWarcs() throws IOException, ParseException {
        List<Warc> warcsList = buildListOfPages(files);
        List<Warc> auxWarcsList = buildListOfPages(files);

        warcsList.forEach(this::setCustomHeaders);

        customHeaders.forEach((k, v) -> {
            System.out.println("\nFile: " + k );
            v.forEach(System.out::println);
        });

        auxWarcsList.forEach(this::writeWarcFile);
    }

    private File[] getFilesOfFolder(File folder) {
        File[] noFiles = {};
        return folder.isDirectory()? folder.listFiles(): noFiles;
    }

    private List<Warc> buildListOfPages(File[] files) throws IOException, ParseException {
        List<Warc> warcsList = new ArrayList<>();
        for (File item: files) {
            warcsList.add(getPagesOfWarc(item));
        }

        return warcsList;
    }

    private Warc getPagesOfWarc(File warc) throws IOException, ParseException {
        WARCReader warcReader = WARCReaderFactory.get(new File(warc.getAbsolutePath()));
        String warcFileName = warc.getName();
        ArchiveRecordHeader warcinfo = warcReader.get().getHeader();

        return new Warc(warcFileName, warcinfo, warcReader.iterator());
    }

    private void writeWarcFile(Warc warc) {
        File file = new File("./_ham_copy/" + warc.getFileName());
        try (FileOutputStream os = new FileOutputStream(file)) {
            writeWarcInfoHeader(warc.getFileName(), warc.getWarcInfoHeader(), os);
            writeResponses(warc.getPages(), os);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void writeWarcInfoHeader(String fileName, ArchiveRecordHeader warcHeader, OutputStream os)
            throws IOException {
        String version = WARC_VERSION + System.lineSeparator();
        os.write(version.getBytes());
        for (String header: warcinfoheaderkeys) {
            switch (header) {
                case HEADER_WARCINFO_ALL_LANGUAGE:
                    os.write(buildCustomHeader(fileName, header, customHeaders).getBytes());
                    break;
                case HEADER_WARCINFO_ALL_CONTENT_TYPE:
                    os.write(buildCustomHeader(fileName, header, customHeaders).getBytes());
                    break;
                default:
                    os.write(buildHeader(
                            header,
                            warcHeader.getHeaderValue(header))
                            .getBytes());
                    break;
            }
        }
    }

    private void writeResponseHeader(ArchiveRecordHeader warcHeader, OutputStream os) throws IOException {
        String version = System.lineSeparator() + WARC_VERSION + System.lineSeparator();
        os.write(version.getBytes());
        for (String header: responseheaderkeys) {
            os.write(buildHeader(
                            header,
                            warcHeader.getHeaderValue(header))
                            .getBytes());
        }
    }

    private String buildCustomHeader(String fileName, String headerKey, Map<String, List<CustomHeader>> values) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, List<CustomHeader>> entry: values.entrySet()) {
            if (entry.getKey().equals(fileName)) {
                for (CustomHeader ch: entry.getValue()) {
                    if (ch.getKey().equals(headerKey)) {
                        for (String l : ch.getValues()) {
                            if (sb.length() != 0) {
                                sb.append(",");
                            }
                            sb.append(l);
                        }
                    }
                }
            }
        }
        return headerKey + ": " + sb.toString() + System.lineSeparator();
    }

    private String buildHeader(String headerKey, Object headerValue) {
        String h = headerKey + ": ";
        if (headerValue != null) {
            h += headerValue.toString() + System.lineSeparator();
        } else {
            h += System.lineSeparator();
        }
        return h;
    }


    private void writeResponses(Iterator<ArchiveRecord> records, OutputStream os) throws IOException {
        os.write(System.lineSeparator().getBytes());
        while (records.hasNext()) {
            ArchiveRecord record = records.next();
            writeResponseHeader(record.getHeader(), os);
            record.dump(os);
        }
    }

    private void setCustomHeaders(Warc warc) {
        CustomHeader languages = new CustomHeader(HEADER_WARCINFO_ALL_LANGUAGE, new HashSet<>());
        CustomHeader contentTypes = new CustomHeader(HEADER_WARCINFO_ALL_CONTENT_TYPE, new HashSet<>());
        Warc aux = new Warc(warc);
        while (aux.getPages().hasNext()) {
            ArchiveRecordHeader recordHeader = aux.getPages().next().getHeader();
            Object lang = recordHeader.getHeaderValue(HEADER_LANGUAGE);
            Object cont = recordHeader.getHeaderValue(HEADER_CONTENT_TYPE);
            if (lang != null) {
                languages.getValues().add(lang.toString());
            }
            if (cont != null) {
                contentTypes.getValues().add(cont.toString());
            }
        }
        List<CustomHeader> headers = new ArrayList<>();
        headers.add(languages);
        headers.add(contentTypes);
        customHeaders.put(warc.getFileName(), headers);
    }
}
