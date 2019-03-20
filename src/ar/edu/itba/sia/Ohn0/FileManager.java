package ar.edu.itba.sia.Ohn0;

import java.io.FileReader;
import java.io.IOException;

public class FileManager {

    /// TODO: separador entre celdas para leer numeros de más de una cifra
    public State readStateFromFile(String path) throws IOException {
        FileReader fr = new FileReader(path);
        int size = readNumber(fr);
        if (size == -1) throw new IllegalStateException();
        Cell cells[][] = new Cell[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int value = fr.read();
                if (value == -1) throw new IllegalStateException();
                cells[i][j] = createCell(value);
            }
            fr.read();
        }
        return new State(cells, size);
    }

    private int readNumber(FileReader fr) throws IOException {
        int number = 0;
        int value;
        while ((value = fr.read()) != '\n') {
            number = number * 10 + value - '0';
        }
        return number;
    }

    private Cell createCell(int value) {
        switch (value) {
            case '.':
                return new Cell(false, 0, Color.WHITE);
            case 'X':
                return new Cell(true, 0, Color.RED);
            case 'O':
                return new Cell(true, 0, Color.BLUE);
            default:
                return new Cell(true, value - '0', Color.BLUE);
        }
    }

}
