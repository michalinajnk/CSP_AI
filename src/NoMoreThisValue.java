public class NoMoreThisValue extends BinaryConstraint {
    public NoMoreThisValue(Indices ind, int forbiddenValue) {
        super(ind, forbiddenValue, false);
    }

    @Override
    public boolean checkConstraints(Grid grid, BinaryConstraint constr) {
        return grid.isCorrectNumberOfValuesInSequence() && grid.areSequencesUnique();

    }


    }
