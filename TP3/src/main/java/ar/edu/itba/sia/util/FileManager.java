package ar.edu.itba.sia.util;

import java.io.FileWriter;
import java.io.IOException;

public class FileManager {

    public void writeStringToFile(String path, String string) throws IOException{
        writeStringToFile(path, string, false);
    }

    public void writeStringToFile(String path, String string, Boolean append) throws IOException {
        FileWriter fw = new FileWriter(path, append);
        fw.write(string);
        fw.close();
    }

}
