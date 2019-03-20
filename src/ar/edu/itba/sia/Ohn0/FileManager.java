package ar.edu.itba.sia.Ohn0;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

public class FileManager {

    public State readStateFromFile(Path path) throws IOException {
        Scanner sc = new Scanner(path);
        if (!sc.hasNextLine()) throw new IllegalStateException("No lines to read.");
        String firstLine = sc.nextLine().trim();
        if (!firstLine.matches("\\d+")) throw  new IllegalStateException("First line must be the size.");
        int size = Integer.parseInt(firstLine);
        Cell cells[][] = new Cell[size][size];
        for (int i = 0; i < size; i++) {
            if (!sc.hasNext()) throw new IllegalStateException();
            String[] row = sc.nextLine().trim().split(" ");
            if (row.length != size) throw new IllegalStateException("Column size do not match decalred size.");
            int j = 0;
            for (String value: row) {
                cells[i][j++] = createCell(value);
            }
        }
        return new State(cells, size);
    }

    private Cell createCell(String value) {
        if (value.matches("\\d+")){
            return new Cell(true, Integer.parseInt(value), Color.BLUE);
        }
        switch (value) {
            case ".":
                return new Cell(false, 0, Color.WHITE);
            case "X":
                return new Cell(true, 0, Color.RED);
            case "O":
                return new Cell(true, 0, Color.BLUE);
            default:
                throw new IllegalStateException("Invalid board symbol.");
        }
    }

}
