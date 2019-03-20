package ar.edu.itba.sia.Ohn0;

import java.io.IOException;
import java.nio.file.Paths;


public class Main {

    public static void main(String[] args) throws IOException {
        FileManager fm = new FileManager();
        State state = fm.readStateFromFile(Paths.get("board"));
        System.out.println(state.getRepresentation());
    }

}
