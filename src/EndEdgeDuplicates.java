public class EndEdgeDuplicates extends BinaryConstraint {

    public EndEdgeDuplicates(Indices ind, int forbiddenValue) {
        super(ind, forbiddenValue, false);
    }

    @Override
    public boolean checkConstraints(Grid grid, BinaryConstraint constr) {
        Indices ind2 = constr.getLeftSide();
        Field previousDupl = grid.getFieldForCoordinates(ind2);
        if (previousDupl != null && previousDupl.getSingleValue() == constr.getForbiddenValue()) {
            return false;
        }
        return true;
    }
}
