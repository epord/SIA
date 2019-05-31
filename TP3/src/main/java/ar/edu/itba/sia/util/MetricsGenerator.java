package ar.edu.itba.sia.util;

import ar.edu.itba.sia.Warriors.Warrior;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MetricsGenerator {
    private static List<Double> bestFitnesses = new ArrayList<>();
    private static List<Double> meanFitnesses = new ArrayList<>();
    private static List<Double> meanHeights = new ArrayList<>();

    public static void addGeneration(List<Warrior> warriors) {
        if (warriors.size() == 0) return;
        bestFitnesses.add(warriors.stream().max(Comparator.comparingDouble(Warrior::getFitness)).get().getFitness());
        meanFitnesses.add(warriors.stream().mapToDouble(Warrior::getFitness).sum() / warriors.size());
        meanHeights.add(warriors.stream().mapToDouble(Warrior::getHeight).sum() / warriors.size());
    }

    public static void clearData() {
        bestFitnesses.clear();
        meanFitnesses.clear();
        meanHeights.clear();
    }

    public static String getOctaveCode() {
        StringBuilder sb = new StringBuilder();

        sb.append("bestFitnesses = [");
        bestFitnesses.forEach(f -> sb.append(f).append(' '));
        sb.append("];\n");
        sb.append("subplot(2, 1, 1);\n");
        sb.append("plot([1:").append(bestFitnesses.size()).append("], bestFitnesses, \"color\", \"red\");\n");
        sb.append("hold on;\n");

        sb.append("meanFitnesses = [");
        meanFitnesses.forEach(f -> sb.append(f).append(' '));
        sb.append("];\n");
        sb.append("subplot(2, 1, 1);\n");
        sb.append("plot([1:").append(meanFitnesses.size()).append("], meanFitnesses, \"color\", \"blue\");\n");

        sb.append("title(\"Fitness over generations\", 'fontsize',14);\n");
        sb.append("xlabel(\"generations\");\n");
        sb.append("ylabel(\"fitness\");\n");
        sb.append("legend(\"best fitness\", \"mean fitness\");\n");

        sb.append("meanHeights = [");
        meanHeights.forEach(f -> sb.append(f).append(' '));
        sb.append("];\n");
        sb.append("subplot(2, 1, 2);\n");
        sb.append("plot([1:").append(meanHeights.size()).append("], meanHeights, \"color\", \"blue\");\n");

        sb.append("title(\"Mean height over generations\", 'fontsize',14);\n");
        sb.append("xlabel(\"generations\");\n");
        sb.append("ylabel(\"height (m)\");\n");

        return sb.toString();
    }


}

