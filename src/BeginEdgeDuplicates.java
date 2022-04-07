public class BeginEdgeDuplicates extends BinaryConstraint {

    public BeginEdgeDuplicates(Indices ind, int forbiddenValue) {
        super(ind, forbiddenValue, true);
    }

    @Override
    public boolean checkConstraints(Grid grid, BinaryConstraint constr) {
        Indices ind = constr.getRightSide();
        Field nextToDuplicates = grid.getFieldForCoordinates(ind);
        if (nextToDuplicates != null && nextToDuplicates.getSingleValue() == constr.getForbiddenValue()) {
            return false;
        }
        return true;
    }
}
