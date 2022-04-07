public class InsideDuplicates extends BinaryConstraint {


    public InsideDuplicates(Indices previousInd, Indices nextInd, int forbiddenValue) {
        super(previousInd, nextInd, forbiddenValue);
    }

    @Override
    public boolean checkConstraints(Grid grid, BinaryConstraint constr) {
        Field leftSideField = grid.getFieldForCoordinates(constr.getLeftSide());
        Field rightSideField = grid.getFieldForCoordinates(constr.getRightSide());
        if (leftSideField != null && rightSideField != null) {
            if (rightSideField.hasOneValue() && leftSideField.hasOneValue()) {
                if (leftSideField.getSingleValue() != rightSideField.getSingleValue() || leftSideField.getSingleValue() == constr.getForbiddenValue()) {
                    return false;
                }
            }
        }
        return true;
    }
}
