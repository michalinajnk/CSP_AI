public class Solution {
    private final Grid grid;
    private final int numberOfMoves;
    private final long endTime;



    public Solution(Grid grid, int coutner, long time){
        this.grid = grid;
        numberOfMoves = coutner;
        endTime = time;
    }

    public Grid getGrid() {
        return grid;
    }

    public int getNumberOfMoves() {
        return numberOfMoves;
    }

    public float getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return "Solution{" +
                "grid=" + grid.toString() +
                ", numberOfMoves=" + numberOfMoves +
                ", endTime=" + endTime +
                '}';
    }

}
