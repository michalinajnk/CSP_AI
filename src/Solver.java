import java.util.ArrayList;
import java.util.List;

public class Solver {

    private Indices currField;
    private List<Integer> valuesToCheck;
    private SolverAlgorithm algo;
    private Grid grid;
    private Solutions solutions;

    public Solver(SolverAlgorithm algo, String fileName) throws Exception {
        this.algo = algo;
        this.grid = algo.loadGrid(fileName);
        this.solutions = new Solutions(algo.getSolverName(), fileName);
        System.out.println(grid.toString());
        start();
    }

    public Solver(Solver previousSolver, int parentVal){
        this.algo= previousSolver.algo;
        this.grid = previousSolver.grid.copy();
        this.solutions = previousSolver.solutions;
        System.out.println(grid.toString());
        algo.move(this.grid, previousSolver.currField, parentVal);
        this.solutions.increaseMovementCounter();
        start();
    }

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


    private void start(){
        if(currField == null ) {
            currField = getNextField(grid);
            if (currField != null) {
                valuesToCheck = grid.getPossibleValues(currField);
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