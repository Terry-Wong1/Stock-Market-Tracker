package persistence;

/* Utilizes json-simple written by Yidong Fang.
   Obtained from https://code.google.com/archive/p/json-simple/ */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

// A writer that will write portfolio and watchlist data to a JSON file
public class Writer {
    // Fields
    private PrintWriter printWriter;

    // EFFECTS: constructs writer that will write data to file
    public Writer(File file) throws FileNotFoundException {
        printWriter = new PrintWriter(file);
    }

    // MODIFIES: this
    // EFFECTS: writes string data to file
    public void write(String data) {
        printWriter.write(data);
    }

    // MODIFIES: this
    // EFFECTS: flushes the print writer stream
    public void flush() {
        printWriter.flush();
    }

    // MODIFIES: this
    // EFFECTS: close print writer
    public void close() {
        printWriter.close();
    }
}
