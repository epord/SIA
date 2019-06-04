package ar.edu.itba.sia.util;

import ar.edu.itba.sia.Items.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TSVReader {
    public static final String[] files = {"botas.tsv", "guantes.tsv", "pecheras.tsv", "cascos.tsv", "armas.tsv"};
    private static final Path DEFAULT_DATA_DIR = Paths.get("Data");

    /**
     * Equivalent to {@link #loadItems(ItemType, Path, boolean)} with the data dir specified in settings, or {@link #DEFAULT_DATA_DIR}
     * if not specified.
     *
     * @param type        Item type to load.
     * @param useFullData Whether to read from the full or test data subdirectory.
     * @return The loaded items
     * @throws IOException On I/O errors.
     */
    public static List<Item> loadItems(ItemType type, boolean useFullData) throws IOException {
        String specifiedPath = Settings.get(Constants.DATA_DIR);
        Path path = specifiedPath == null || specifiedPath.isEmpty() ? DEFAULT_DATA_DIR : Paths.get(specifiedPath);

        return loadItems(type, path, useFullData);
    }

    public static List<Item> loadItems(ItemType type, Path dataDir, boolean useFullData) throws IOException {
        Path fullPath = dataDir.resolve(Paths.get(useFullData ? "Full" : "Test", files[type.ordinal()]));
        BufferedReader TSVFile = new BufferedReader(new FileReader(fullPath.toFile()));

        // Read first line with names
        //noinspection UnusedAssignment, we use this to skip the first line
        String line = TSVFile.readLine();
        String[] stats;
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

    private static void createItem(String[] stats, ItemType type, List<Item> items) {

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
