public class DensityHeuristic implements Heuristic{

    @Override
    public Indices getNextField(Grid grid) {
        int gridSize = grid.getGridSize();
        Integer intensityIndex = null;
        Indices selectedField = null;
        for (int rowNum = 0; rowNum < gridSize; rowNum++) {
            int rowElems = grid.getRow(rowNum).stream().mapToInt(field -> field == null ? 1 : field.getNumberOfPossibleValues()).sum();

            for (int colNum = 0; colNum < gridSize; colNum++) {
                int colElems = grid.getColumn(colNum).stream().mapToInt(field -> field == null ? 1 : field.getNumberOfPossibleValues()).sum();

                Indices indices = new Indices(rowNum, colNum);
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
