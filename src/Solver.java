import java.util.ArrayList;
import java.util.List;

public class Solver {

    private Indices currFieldIndices;
    private List<Integer> valuesToCheck;
    private SolverAlgorithm algo;
    private final Heuristic heuristic;
    private Grid grid;
    private Solutions solutions;

    public Solver(Heuristic heur, SolverAlgorithm algo, String fileName) throws Exception {
        this.heuristic = heur;
        this.algo = algo;
        this.grid = algo.loadGrid(fileName);
        this.solutions = new Solutions(heur.getName(), algo.getSolverName(), fileName);
        System.out.println(grid.toString());
        start();
    }

    public Solver(Solver previousSolver, int parentVal){
        this.heuristic = previousSolver.heuristic;
        this.algo= previousSolver.algo;
        this.grid = previousSolver.grid.copy();
        this.solutions = previousSolver.solutions;
        System.out.println(grid.toString());
        algo.move(this.grid, previousSolver.currFieldIndices, parentVal);
        this.solutions.increaseMovementCounter();
        start();
    }
/*
    public Indices getNextField(Grid grid) {
        int gridSize = grid.getGridSize();
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                Indices ind = new Indices(i, j);
                Field field = grid.getFieldForCoordinates(ind);
                if (field == null)
                    return ind;
                else if (field.hasMoreThanOnePossibility())
                    return field.getIndices();
            }
        }
        return null;
    }

*/
    private void start(){
        if(currFieldIndices == null) {
            currFieldIndices = heuristic.getNextField(grid);
            if (currFieldIndices != null) {
                valuesToCheck = grid.getPossibleValues(currFieldIndices);
            }
            else {
                valuesToCheck = new ArrayList<>();
            }
        }
    }

    public Solutions run(){
        if(!grid.validate()){
            System.out.println("MATRIX IS NOT VALIDATE");
            return this.solutions;
        } else if(grid.isPoorlyCompleted()) {
            System.out.println("MATRIX IS POORLY COMPLETED");
            this.solutions.addSolution(new Solution(grid, solutions.getMovementCounter(),  System.currentTimeMillis()));
            return solutions;
        } else {
            for (Integer value : valuesToCheck) {
                System.out.println("RECURRENCY");
                new Solver(this, value).run();
            }
        }
        this.solutions.setEndTime();
        return solutions;
        }
    }