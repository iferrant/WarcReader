import java.io.File;

public class BalanceCorpusManager {

    private File spamFolder;
    private File hamFolder;
    private int numSpam;
    private int numHam;

    public BalanceCorpusManager(File spamFolder, File hamFolder, int numSpam, int numHam) {
        this.spamFolder = spamFolder;
        this.hamFolder = hamFolder;
        this.numSpam = numSpam;
        this.numHam = numHam;
    }

    public void balance() {
        CustomWarcWriter writeSpam = new CustomWarcWriter(this.spamFolder, numSpam);
        writeSpam.writeWarcs();
        CustomWarcWriter writeHam = new CustomWarcWriter(this.hamFolder, numHam);
        writeHam.writeWarcs();
    }

    public void read() {
        CustomWarcReader customSpamWarcReader = new CustomWarcReader(spamFolder);
        customSpamWarcReader.read();
        CustomWarcReader customHamWarcReader = new CustomWarcReader(hamFolder);
        customHamWarcReader.read();
    }
}
