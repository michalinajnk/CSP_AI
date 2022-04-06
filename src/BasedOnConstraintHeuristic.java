public class BasedOnConstraintHeuristic implements Heuristic {


    @Override
    public Indices getNextField(Grid grid) {
        int gridSize = grid.getGridSize();
        Integer intensityIndex = null;
        Indices selectedField = null;

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
               int counter = 0;
               int allConstr = 0;
               Indices ind = new Indices(i, j);

               if((grid.getClass() == BinaryGrid.class)) {
                   for(BinaryConstraint constraint : ((BinaryGrid) grid).getConstraints()) {
                       allConstr++;
                       if((constraint.getLeftSide() !=null && ind == constraint.getLeftSide()) || ( constraint.getRightSide() !=null && ind == constraint.getRightSide())) { counter++; }
                   }

               }

                else if((grid.getClass() == FutoshikiGrid.class)) {
                    for(FutoshikiConstraint constraint : ((FutoshikiGrid) grid).getConstraints()) {
                        allConstr++;
                        if((constraint.getBigger() !=null && ind == constraint.getBigger()) || ( constraint.getBigger() !=null && ind == constraint.getSmaller())) { counter++; }
                    }
                }

                Field field = grid.getFieldForCoordinates(ind);

                int numberOfPossibleValues;
                boolean isEligible = true;
                if (field != null) {
                    numberOfPossibleValues = field.getNumberOfPossibleValues();
                    if (numberOfPossibleValues <= 1) {
                        isEligible = false;
                    }
                } else {
                    numberOfPossibleValues = grid.getPossibleValues(ind).size();
                    if (numberOfPossibleValues < 1) {
                        isEligible = false;
                    }
                }

                if (isEligible) {
                    int currentIntensityIndex = (counter) / allConstr; //minimizing choice & maximizing system response
                    if (intensityIndex == null || currentIntensityIndex > intensityIndex) {
                        intensityIndex = currentIntensityIndex;
                        selectedField = ind;
                    }
                }
            }
        }
        return selectedField;
    }

    @Override
    public String getName() {
        return "BasedOnConstraintHeuristic";
    }
}
