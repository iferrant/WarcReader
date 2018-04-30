import java.io.*;

public class Main {

    public static void main(String[] args) {
        // TODO: Improve spam/ham files input
        if (args.length != 2) {
            System.err.println("Introduce the spam and ham folder's path as arguments");
        } else {
            File spam = new File(args[0]);
            File ham = new File(args[1]);
            BalanceCorpusManager balanceCorpusManager = new BalanceCorpusManager(spam, ham, 3, 3);
            balanceCorpusManager.balance();
            balanceCorpusManager.read();
        }
    }
}
