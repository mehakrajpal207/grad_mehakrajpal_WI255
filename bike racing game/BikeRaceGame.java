import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BikeRaceGame {

    private static final int TOTAL_RACERS = 10;
    private static final int RACE_DISTANCE = 1000;
    private static final int CHECKPOINT_INTERVAL = 100;
    
    private static final Vector<RacerResult> leaderBoard = new Vector<>();
    private static final CyclicBarrier startBarrier = new CyclicBarrier(TOTAL_RACERS);
    private static final CountDownLatch finishLatch = new CountDownLatch(TOTAL_RACERS);

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(TOTAL_RACERS);
        String[] bikeNames = {
            "B1", "B2", "B3", "B4", "B5", 
            "B6", "B7", "B8", "B9", "B10"
        };


        System.out.println("--- The Race is about to start! ---");

        for (int i = 0; i < TOTAL_RACERS; i++) {
            executor.execute(new Racer(bikeNames[i]));
        }

        try {
            finishLatch.await();
            System.out.println("\n--- RACE FINISHED! LEADERBOARD ---");
            displayLeaderBoard();
        } catch (Exception  e) {
            System.out.println(e);
        } finally {
            executor.shutdown();
        }
    }

    static class Racer implements Runnable {
        private final String name;
        private int distanceCovered = 0;
        private int lastNotifiedCheckpoint = 0;

        public Racer(String name) {
            this.name = name;
        }

        @Override
        public void run() {
            try {
                startBarrier.await();
                long startTime = System.currentTimeMillis();

                while (distanceCovered < RACE_DISTANCE) {
                    int move = (int)(Math.random() * 50) + 30;
                    distanceCovered += move;
                    if (distanceCovered > RACE_DISTANCE) distanceCovered = RACE_DISTANCE;
                    checkAndPrintProgress();
                    Thread.sleep((long)(Math.random() * 1000) + 500);
                }

                long finishTime = System.currentTimeMillis() - startTime;
                System.out.println( name + " finished in " + (finishTime / 1000.0) + "s");

                leaderBoard.add(new RacerResult(name, finishTime));
                finishLatch.countDown();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void checkAndPrintProgress() {
            int currentCheckpoint = (distanceCovered / CHECKPOINT_INTERVAL) * CHECKPOINT_INTERVAL;
            if (currentCheckpoint > lastNotifiedCheckpoint && currentCheckpoint <= RACE_DISTANCE) {
                System.out.println(name + " has reached " + currentCheckpoint + " meters.");
                lastNotifiedCheckpoint = currentCheckpoint;
            }
        }
    }

    static class RacerResult {
        String name;
        long time;

        RacerResult(String name, long time) {
            this.name = name;
            this.time = time;
        }
    }

    private static void displayLeaderBoard() {
        System.out.printf("%-5s | %-10s | %-12s%n", "Pos", "Name", "Time (s)");
        System.out.println("---------------------------------------");
        for (int i = 0; i < leaderBoard.size(); i++) {
            RacerResult res = leaderBoard.get(i);
            System.out.printf("%-5d | %-10s | %-12.2f%n", (i + 1), res.name, (res.time / 1000.0));
        }
    }
}