public class ForwardChecking implements SolverAlgorithm {
   
    @Override
    public Grid loadGrid(String fileName) throws Exception {
        Grid grid = GridReader.initializeFromFile(fileName);
        grid.initializeWithPossibleValues();
        return grid;
    }



    @Override
    public Grid move(Grid grid, Indices ind, Integer value) {
        grid.move(ind, value);
        grid.removeAllForbiddenValues();
        return grid;
    }

    @Override
    public String getSolverName() {
        return "forward checking";
    }


}