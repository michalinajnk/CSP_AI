public class Backtracking implements SolverAlgorithm{

    @Override
    public Grid loadGrid(String fileName) throws Exception {
        return GridReader.initializeFromFile(fileName);
    }

    @Override
    public Grid move(Grid grid, Indices ind, Integer val) {
        grid.move(ind, val);
        return grid;
    }

    @Override
    public String getSolverName() {
        return "backtracking";
    }

}


