import java.util.ArrayList;
import java.util.List;

public class Solutions {
    private final List<Solution> allSolutions;
    private final String algorithm;
    private final String fileName;
    private int moveCounter;
    private final long startTimeInterval;
    private long endTime;

    public Solutions(String name, String fileName) {
        this.fileName = fileName;
        this.algorithm = name;
        this.allSolutions = new ArrayList<>();
        this.moveCounter = 0;
        this.startTimeInterval = System.currentTimeMillis();
    }
    public void addSolution(Solution solution) {
        this.allSolutions.add(solution);
    }



    public int getMovementCounter() {
        return moveCounter;
    }

    public void increaseMovementCounter() {
        moveCounter++;
    }

    public List<Solution> getSolutions() {
        return allSolutions;
    }

    public double getTotalTime() {
        return ((double) endTime - startTimeInterval) / 1000;
    }

    public double getFirstTime() {
        return ((double) allSolutions.get(0).getEndTime() - startTimeInterval) / 1000;
    }

    public void setEndTime() {
        endTime = System.currentTimeMillis();
    }

    public String getAlg() {
        return algorithm;
    }


    public String getFileName() {
        return fileName;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(algorithm).append(": ").append(fileName).append("\n")
                .append(String.format("%30s", "Total number of solutions: ")).append(allSolutions.size()).append("\n");

                //.append(String.format("%30s", "Moves for first: ")).append(allSolutions.get(0).getNumberOfMoves()).append("\n")
               //.append(String.format("%30s", "Total moves: ")).append(moveCounter).append("\n")
                //.append(String.format("%30s", "Time for first: ")).append(getFirstTime()).append(" s").append("\n")
               //.append(String.format("%30s", "Total end time: ")).append(getTotalTime()).append(" s");
        return builder.toString();



    }


}
