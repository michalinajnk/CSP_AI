public class QueueHeuristic implements Heuristic {

    @Override
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


    @Override
    public String getName() {
        return "QueueHeuristic";
    }
}
