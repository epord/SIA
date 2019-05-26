package ar.edu.itba.sia.util;

import ar.edu.itba.sia.Items.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TSVReader {
    public static final String files[] = {"botas.tsv", "guantes.tsv", "pecheras.tsv", "cascos.tsv", "armas.tsv"};

    public static List<Item> loadItems(ItemType type) throws IOException {

// May be different in other systems
        BufferedReader TSVFile = new BufferedReader(new FileReader("TP3/src/main/" + files[type.ordinal()]));
//        BufferedReader TSVFile = new BufferedReader(new FileReader("src/main/" + files[type.ordinal()]));

        // Read first line with names
        String line = TSVFile.readLine();
        String stats[];
        line = TSVFile.readLine();
        List<Item> items = new ArrayList<>();

        while (line != null){
            stats = line.split("\t");
            createItem(stats, type, items);
            line = TSVFile.readLine(); // Read next line of data.
        }

        // Close the file once all data has been read.
        TSVFile.close();
        return items;
    }

    private static void createItem(String stats[], ItemType type, List<Item> items) {

        switch (type) {
            case BOOTS:
                items.add(new Boots(Double.parseDouble(stats[1]), Double.parseDouble(stats[2]),
                                    Double.parseDouble(stats[3]), Double.parseDouble(stats[4]),
                                    Double.parseDouble(stats[5])));
                break;

            case GLOVES:
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
