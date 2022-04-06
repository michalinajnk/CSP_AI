public interface SolverAlgorithm {
    Grid loadGrid(String fileName) throws Exception;
    Grid move(Grid grid, Indices ind, Integer val);
    String getSolverName();

}
