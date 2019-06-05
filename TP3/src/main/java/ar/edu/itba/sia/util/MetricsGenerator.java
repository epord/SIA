package ar.edu.itba.sia.util;

import ar.edu.itba.sia.GeneticOperators.Interfaces.EndCondition;
import ar.edu.itba.sia.GeneticOperators.Mutations.GeneralMutation;
import ar.edu.itba.sia.Items.*;
import ar.edu.itba.sia.Warriors.Warrior;

import java.util.*;
import java.util.stream.Collectors;
import java.util.AbstractMap.SimpleEntry;

public class MetricsGenerator {
    private static List<Double> bestFitnesses = new ArrayList<>();
    private static List<Double> meanFitnesses = new ArrayList<>();
    private static List<Double> meanHeights = new ArrayList<>();
    private static List<List<Warrior>> generations = new ArrayList<>();
    private static List<Integer> biodiversityIndex = new ArrayList<>();
    private static List<Double> simpsonIndex = new ArrayList<>();
    private static List<Double> mutationProbability = new ArrayList<>();

    public static void addGeneration(List<Warrior> warriors) {
        if (warriors.size() == 0) return;
        generations.add(warriors);
        bestFitnesses.add(warriors.stream().max(Comparator.comparingDouble(Warrior::getFitness)).get().getFitness());
        meanFitnesses.add(warriors.stream().mapToDouble(Warrior::getFitness).sum() / warriors.size());
        meanHeights.add(warriors.stream().mapToDouble(Warrior::getHeight).sum() / warriors.size());

        // Calculate biodiversity
        Set<Warrior> nonRepeatedWarriors = new HashSet<>();
        nonRepeatedWarriors.addAll(warriors);
        biodiversityIndex.add(nonRepeatedWarriors.size());

        // Calculate Simpson index
        HashMap<Warrior, Integer> warriorCounter = new HashMap<>();
        WarriorUtils.loadMap(warriorCounter, warriors);
        Double accumulatedRelativeDiversity = 0.0;
        for (Map.Entry<Warrior, Integer> entry : warriorCounter.entrySet()) {
            accumulatedRelativeDiversity += Math.pow(entry.getValue().doubleValue() / warriors.size(), 2);
        }
        simpsonIndex.add(1 - accumulatedRelativeDiversity);

        // Add mutation probability
        mutationProbability.add(GeneralMutation.getProbability());
    }

    public static void clearData() {
        bestFitnesses.clear();
        meanFitnesses.clear();
        meanHeights.clear();
        generations.clear();
    }

    private static Map.Entry<Item, Long> mostRepeatedItem(List<Warrior> warriors, ItemType itemType) {
        Map<Item, Long> itemCounter;
        switch (itemType) {
            case HELMET:
                itemCounter = warriors.stream().collect(Collectors.groupingBy(Warrior::getHelmet, Collectors.counting()));
                break;
            case PLATEBODY:
                itemCounter = warriors.stream().collect(Collectors.groupingBy(Warrior::getPlatebody, Collectors.counting()));
                break;
            case GLOVES:
                itemCounter = warriors.stream().collect(Collectors.groupingBy(Warrior::getGloves, Collectors.counting()));
                break;
            case WEAPON:
                itemCounter = warriors.stream().collect(Collectors.groupingBy(Warrior::getWeapon, Collectors.counting()));
                break;
            case BOOTS:
                itemCounter = warriors.stream().collect(Collectors.groupingBy(Warrior::getBoots, Collectors.counting()));
                break;
            default:
                throw new IllegalArgumentException("Invalid item type");
        }
        Item mostCommonItem = null;
        long maxRepetition = -1;
        for (Map.Entry<Item, Long> entry : itemCounter.entrySet()) {
            if (maxRepetition < entry.getValue()) {
                maxRepetition = entry.getValue();
                mostCommonItem = entry.getKey();
            }
        }
        return new SimpleEntry<>(mostCommonItem, maxRepetition);
    }

    private static Item meanItem(List<Warrior> warriors, ItemType itemType) {
        Double accumulatedStrength = 0.0;
        Double accumulatedAgility = 0.0;
        Double accumulatedExpertise = 0.0;
        Double accumulatedResitance = 0.0;
        Double accumulatedHitPoints = 0.0;

        for (Warrior warrior: warriors) {
            Item item = warrior.getItem(itemType);
            accumulatedStrength += item.getStrength();
            accumulatedAgility += item.getAgility();
            accumulatedExpertise += item.getExpertise();
            accumulatedResitance += item.getResistance();
            accumulatedHitPoints += item.getHitPoints();
        }

        accumulatedStrength /= warriors.size();
        accumulatedAgility /= warriors.size();
        accumulatedExpertise /= warriors.size();
        accumulatedResitance /= warriors.size();
        accumulatedHitPoints /= warriors.size();

        switch (itemType) {
            case HELMET:
                return new Helmet(accumulatedStrength, accumulatedAgility, accumulatedExpertise, accumulatedResitance, accumulatedHitPoints);
            case PLATEBODY:
                return new Platebody(accumulatedStrength, accumulatedAgility, accumulatedExpertise, accumulatedResitance, accumulatedHitPoints);
            case GLOVES:
                return new Gloves(accumulatedStrength, accumulatedAgility, accumulatedExpertise, accumulatedResitance, accumulatedHitPoints);
            case WEAPON:
                return new Weapon(accumulatedStrength, accumulatedAgility, accumulatedExpertise, accumulatedResitance, accumulatedHitPoints);
            case BOOTS:
                return new Boots(accumulatedStrength, accumulatedAgility, accumulatedExpertise, accumulatedResitance, accumulatedHitPoints);
            default:
                throw new IllegalArgumentException("Invalid item type");
        }
    }

    public static String getVisualizationData() {
        StringBuilder sb = new StringBuilder();

        generations.forEach(generation -> {

            // Warriors classification by fitness (in percentage)
            final double masterRaceFitness = 38.129314598705854; /// TODO: this is hardcoded
            int divisions = 10; // step percentage to classifier
            List<Integer> classesCount = new ArrayList<>(Collections.nCopies(divisions, 0));
            generation.forEach(warrior -> {
                int belongingClass = (int) Math.min(Math.floor(warrior.getFitness() * divisions / masterRaceFitness), divisions - 1);
                classesCount.set(belongingClass, classesCount.get(belongingClass) + 1);
            });
            List<Integer> normalizedClassesCount = classesCount.parallelStream().map(c -> (int) Math.round((100.0 * c) / generation.size())).collect(Collectors.toList());
            for (Integer count: normalizedClassesCount) {
                sb.append(count).append(' ');
            }
            sb.append('\n');


            // Find most common items
            Map.Entry<Item, Long> commonHelmet = mostRepeatedItem(generation, ItemType.HELMET);
            Map.Entry<Item, Long> commonPlatebody = mostRepeatedItem(generation, ItemType.PLATEBODY);
            Map.Entry<Item, Long> commonGloves = mostRepeatedItem(generation, ItemType.GLOVES);
            Map.Entry<Item, Long> commonWeapon = mostRepeatedItem(generation, ItemType.WEAPON);
            Map.Entry<Item, Long> commonBoots = mostRepeatedItem(generation, ItemType.BOOTS);

            @SuppressWarnings("unchecked")
            Map.Entry<Item, Long>[] commonItems = new Map.Entry[] {commonHelmet, commonPlatebody, commonGloves, commonWeapon, commonBoots};

            for (Map.Entry<Item, Long> commonItem: commonItems) {
                sb.append(commonBoots.getValue()).append(' ')
                        .append(commonItem.getKey().getStrength()).append(' ')
                        .append(commonItem.getKey().getAgility()).append(' ')
                        .append(commonItem.getKey().getExpertise()).append(' ')
                        .append(commonItem.getKey().getResistance()).append(' ')
                        .append(commonItem.getKey().getHitPoints()).append('\n');
            }


            // Find mean items
            Helmet meanHelmet = (Helmet) meanItem(generation, ItemType.HELMET);
            Platebody meanPlatebody = (Platebody) meanItem(generation, ItemType.PLATEBODY);
            Gloves meanGloves = (Gloves) meanItem(generation, ItemType.GLOVES);
            Weapon meanWeapon = (Weapon) meanItem(generation, ItemType.WEAPON);
            Boots meanBoots = (Boots) meanItem(generation, ItemType.BOOTS);

            Item[] meanItems = new Item[]{meanHelmet, meanPlatebody, meanGloves, meanWeapon, meanBoots};
            for (Item item: meanItems) {
                sb.append(item.getStrength()).append(' ')
                        .append(item.getAgility()).append(' ')
                        .append(item.getExpertise()).append(' ')
                        .append(item.getResistance()).append(' ')
                        .append(item.getHitPoints()).append('\n');
            }
        });

        return sb.toString();
    }

    public static String getOctaveCode() {
        StringBuilder sb = new StringBuilder();

        sb.append("bestFitnesses = [");
        bestFitnesses.forEach(f -> sb.append(f).append(' '));
        sb.append("];\n");
        sb.append("subplot(5, 1, 1);\n");
        sb.append("plot([1:").append(bestFitnesses.size()).append("], bestFitnesses, \"color\", \"red\");\n");
        sb.append("hold on;\n");

        sb.append("meanFitnesses = [");
        meanFitnesses.forEach(f -> sb.append(f).append(' '));
        sb.append("];\n");
        sb.append("subplot(5, 1, 1);\n");
        sb.append("plot([1:").append(meanFitnesses.size()).append("], meanFitnesses, \"color\", \"blue\");\n");
        sb.append("title(\"Fitness over generations\", 'fontsize',14);\n");
        sb.append("xlabel(\"generations\");\n");
        sb.append("ylabel(\"fitness\");\n");
        sb.append("legend(\"best fitness\", \"mean fitness\");\n");

        sb.append("meanHeights = [");
        meanHeights.forEach(f -> sb.append(f).append(' '));
        sb.append("];\n");
        sb.append("subplot(5, 1, 2);\n");
        sb.append("plot([1:").append(meanHeights.size()).append("], meanHeights, \"color\", \"blue\");\n");
        sb.append("title(\"Mean height over generations\", 'fontsize',14);\n");
        sb.append("xlabel(\"generations\");\n");
        sb.append("ylabel(\"height (m)\");\n");

        sb.append("diversity = [");
        biodiversityIndex.forEach(f -> sb.append(f).append(' '));
        sb.append("];\n");
        sb.append("subplot(5, 1, 3);\n");
        sb.append("plot([1:").append(biodiversityIndex.size()).append("], diversity, \"color\", \"blue\");\n");
        sb.append("title(\"Biodiversity over generations\", 'fontsize',14);\n");
        sb.append("xlabel(\"generations\");\n");
        sb.append("ylabel(\"biodiversity index\");\n");

        sb.append("simpson = [");
        simpsonIndex.forEach(f -> sb.append(f).append(' '));
        sb.append("];\n");
        sb.append("subplot(5, 1, 4);\n");
        sb.append("plot([1:").append(simpsonIndex.size()).append("], simpson, \"color\", \"blue\");\n");
        sb.append("title(\"Simpson index over generations\", 'fontsize',14);\n");
        sb.append("xlabel(\"generations\");\n");
        sb.append("ylabel(\"Simpson index\");\n");

        sb.append("mutation = [");
        mutationProbability.forEach(f -> sb.append(f).append(' '));
        sb.append("];\n");
        sb.append("subplot(5, 1, 5);\n");
        sb.append("plot([1:").append(mutationProbability.size()).append("], mutation, \"color\", \"blue\");\n");
        sb.append("title(\"Mutation Probability\", 'fontsize',14);\n");
        sb.append("xlabel(\"generations\");\n");
        sb.append("ylabel(\"Mutation probability\");\n");

        return sb.toString();
    }


}

