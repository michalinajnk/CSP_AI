import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FutoshikiGrid extends AbstractGrid{
    List<FutoshikiConstraint> constraints;

    FutoshikiGrid(int size) {
        super(size);
        constraints = new ArrayList<>();
    }


    public FutoshikiGrid(FutoshikiGrid grid) {
        super(grid);
        this.constraints = grid.constraints;
    }


    public List<FutoshikiConstraint> getConstraints(){
        return constraints;
    }

    public ArrayList<Integer> getGridDomain(){
        ArrayList<Integer> gridDomain = new ArrayList<>();
        for(int i=1; i <= grid.size(); i++){
            gridDomain.add(i);
        }
        return gridDomain;
    }

    @Override
    public ArrayList<Integer> getPossibleValues(Indices ind){
        Field field = getFieldForCoordinates(ind);
        if(field != null) {
            if (field.hasMoreThanOnePossibility())
                return new ArrayList<>(field.getPossibleVals());
            else
                return new ArrayList<>();
        }
        ArrayList<Integer> newPossibleVals = getGridDomain();
        newPossibleVals.removeAll(getForbiddenValues(ind));
        return newPossibleVals;

    }

    @Override
    public Grid copy() {
        return new FutoshikiGrid(this);
    }


    private boolean areOnlyUniqueInSequence(){
        for(int i = 0; i < grid.size();i++){
            List<Integer> row = getRow(i).stream().filter(Objects::nonNull).filter(Field::hasOneValue).map(Field::getSingleValue).collect(Collectors.toList());
            int rowSize = row.size();
            if (row.stream().distinct().collect(Collectors.toList()).size() != rowSize) {
                return false;
            }
            List<Integer> col = getColumn(i).stream().filter(Objects::nonNull).filter(Field::hasOneValue).map(Field::getSingleValue).collect(Collectors.toList());
            int colSize = col.size();
            if (col.stream().distinct().collect(Collectors.toList()).size() != colSize) {
                return false;
            }

        }
        return true;
    }

    public List<Integer> getForbiddenValues(Indices ind){
        List<Field> checkingRow = getRow(ind.getNumRow());

        //checking values without this val
        checkingRow.set(ind.getNumCol(), null);

        List<Integer> valAlreadyIn = checkingRow.stream()
                .filter(Objects::nonNull)
                .filter(Field::hasOneValue)
                .map(Field::getSingleValue)
                .collect(Collectors.toList());

        List<Field> checkingColumn = getColumn(ind.getNumCol());
        //checking values without this val
        checkingColumn.set(ind.getNumRow(), null);
        valAlreadyIn.addAll(checkingColumn.stream()
                .filter(Objects::nonNull)
                .filter(Field::hasOneValue)
                .map(Field::getSingleValue)
                .collect(Collectors.toList()));

        valAlreadyIn.stream().distinct().collect(Collectors.toList());
        List<Integer> collect = valAlreadyIn.stream().distinct().collect(Collectors.toList());
        return collect;
    }




    @Override
    protected boolean checkConstraints() {
        if(areOnlyUniqueInSequence()) {
            for (FutoshikiConstraint constr : constraints) {
                Field smallerField = getFieldForCoordinates(constr.getSmaller());
                Field biggerField = getFieldForCoordinates(constr.getBigger());
                if (smallerField != null && biggerField != null) {
                    if (biggerField.hasOneValue() && smallerField.hasOneValue()) {
                        if (biggerField.getSingleValue() < smallerField.getSingleValue()) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    @Override
    protected String displayConstraints() {
        StringBuilder builder = new StringBuilder();
        for (FutoshikiConstraint constraint : constraints) {
            builder.append("\n").append(constraint);
        }
        return builder.toString();
    }


}
