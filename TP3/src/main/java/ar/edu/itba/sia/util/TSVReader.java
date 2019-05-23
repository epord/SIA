package ar.edu.itba.sia.util;

import ar.edu.itba.sia.Items.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class TSVReader {
    public static final String files[] = {"botas.tsv", "guantes.tsv", "pecheras.tsv", "cascos.tsv", "armas.tsv"};
    private static final int BOOT = 0;
    private static final int GLOVE = 1;
    private static final int PLATEBODY = 2;
    private static final int HELMET = 3;
    private static final int WEAPON = 4;

    public static List<Item> loadItems(int itemNumber) throws IOException {
        if(itemNumber < 0 || itemNumber > 4) {
            throw new IllegalArgumentException("");
        }

        BufferedReader TSVFile = new BufferedReader(new FileReader("TP3/src/main/" + files[itemNumber]));

        // Read first line with names
        String line = TSVFile.readLine();
        String stats[];
        line = TSVFile.readLine();
        List<Item> items = new ArrayList<>();

        while (line != null){
            stats = line.split("\t");
            createItem(stats, itemNumber, items);
            line = TSVFile.readLine(); // Read next line of data.
        }

        // Close the file once all data has been read.
        TSVFile.close();
        return items;
    }

    private static void createItem(String stats[], int itemNumber, List<Item> items) {
//    for(String i: stats) {
//        System.out.println(Double.parseDouble(stats[1]));
//
//    }
                switch (itemNumber) {
            case BOOT:
                items.add(new Boot(Double.parseDouble(stats[1]), Double.parseDouble(stats[2]),
                                    Double.parseDouble(stats[3]), Double.parseDouble(stats[4]),
                                    Double.parseDouble(stats[5])));
                break;

            case GLOVE:
                items.add(new Gloves(Double.parseDouble(stats[1]), Double.parseDouble(stats[2]),
                        Double.parseDouble(stats[3]), Double.parseDouble(stats[4]),
                        Double.parseDouble(stats[5])));
                break;

            case PLATEBODY:
                items.add(new Platebody(Double.parseDouble(stats[1]), Double.parseDouble(stats[2]),
                        Double.parseDouble(stats[3]), Double.parseDouble(stats[4]),
                        Double.parseDouble(stats[5])));
                break;

            case HELMET:
                items.add(new Helmet(Double.parseDouble(stats[1]), Double.parseDouble(stats[2]),
                        Double.parseDouble(stats[3]), Double.parseDouble(stats[4]),
                        Double.parseDouble(stats[5])));
                break;

            case WEAPON:
                items.add(new Weapon(Double.parseDouble(stats[1]), Double.parseDouble(stats[2]),
                        Double.parseDouble(stats[3]), Double.parseDouble(stats[4]),
                        Double.parseDouble(stats[5])));
                break;
        }
    }


}
