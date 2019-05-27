package ar.edu.itba.sia;

import ar.edu.itba.sia.Warriors.Warrior;
import ar.edu.itba.sia.Warriors.WarriorType;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class BestWarriorTest {

    @Test
    public void shouldNeverExceedMaximum() throws IOException {
        Warrior bestWarrior = MasterRaceFinder.find(WarriorType.ARCHER);
        final double MAX_PERFORMANCE = bestWarrior.getPerformance();
        final int NUM_TESTS = 1000;
        System.out.println("Maximum performance: " + MAX_PERFORMANCE);

        for (int i = 0; i < NUM_TESTS; i++) {
            System.out.format("Test run %d/%d...", i+1, NUM_TESTS);
            Warrior warrior = BestWarriorFinder.findBestWarrior();
            double performance = warrior.getPerformance();
            assertTrue(String.format("\nBest performance exceeded: %g > %g\nOffending warrior: %s", performance, MAX_PERFORMANCE, warrior), MAX_PERFORMANCE > performance);
            System.out.println("OK");
        }
    }
}
