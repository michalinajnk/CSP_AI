public class DensityHeuristic implements Heuristic{

    @Override
    public Indices getNextField(Grid grid) {
        int gridSize = grid.getGridSize();
        Integer intensityIndex = null;
        Indices selectedField = null;
        for (int i = 0; i < gridSize; i++) {
            int rowElems = grid.getRow(i).stream().mapToInt(field -> field == null ? 1 : field.getNumberOfPossibleValues()).sum();

            for (int j = 0; j < gridSize; j++) {
                int colElems = grid.getColumn(j).stream().mapToInt(field -> field == null ? 1 : field.getNumberOfPossibleValues()).sum();

                Indices indices = new Indices(i, j);
                Field field = grid.getFieldForCoordinates(indices);

                int numberOfPossibleValues;
                boolean isEligible = true;
                if (field != null) {
                    numberOfPossibleValues = field.getNumberOfPossibleValues();
                    if (numberOfPossibleValues <= 1) {
                        isEligible = false;
                    }
                } else {
                    numberOfPossibleValues = grid.getPossibleValues(indices).size();
                    if (numberOfPossibleValues < 1) {
                        isEligible = false;
                    }
                }

                if (isEligible) {
                    int currentIntensityIndex = (rowElems + colElems) / numberOfPossibleValues; //minimizing choice & maximizing system response
                    if (intensityIndex == null || currentIntensityIndex > intensityIndex) {
                        intensityIndex = currentIntensityIndex;
                        selectedField = indices;
                    }
                }
            }
        }
        return selectedField;
    }

    @Override
    public String getName() {
        return "DensityHeuristic";
    }
}
