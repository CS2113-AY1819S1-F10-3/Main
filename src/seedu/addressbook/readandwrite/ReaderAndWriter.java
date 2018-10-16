package seedu.addressbook.readandwrite;

import java.io.*;

public class ReaderAndWriter {

    public File fileToUse (String pathName){
        File file = new File(pathName);
        return file;
    }

    public BufferedReader openReader (File file) throws FileNotFoundException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        return br;
    }

    public PrintWriter openWriter (File file) throws IOException {
        PrintWriter pw = new PrintWriter(file);
        return pw;
    }

    public PrintWriter openTempWriter (File file) throws IOException {
        PrintWriter pw = new PrintWriter(new FileWriter(file));
        return pw;
    }

}
