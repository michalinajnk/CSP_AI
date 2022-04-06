import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractGrid implements Grid {
    protected final List<List<Field>> grid;

    protected AbstractGrid(int size){
        grid = initializeEmptyGrid(size); //by nulls
    }

    protected AbstractGrid(Grid grid){
        int size = grid.getGridSize();
        this.grid = initializeEmptyGrid(size);
        for(int i=0; i <size; i++){
            for(int j=0; j < size; j++){
                Indices nextInd = new Indices(i,j);
                Field field = grid.getFieldForCoordinates(nextInd);
                if(field != null) {
                    setField(new Field(field), nextInd);
                }
            }
        }
    }

    public abstract Grid copy();
    protected abstract boolean checkConstraints();
    protected abstract String displayConstraints();

    protected  List<List<Field>> initializeEmptyGrid(int size){
        List<List<Field>> grid = new ArrayList<>();
        for(int i=0; i < size; i++){
            List<Field> row = new ArrayList<>();
            for(int j=0; j < size; j++){
                    row.add(j, null);
            }
            grid.add(i, row);
        }
        return grid;
    }

    public void setField(Field field, Indices ind){
       grid.get(ind.getNumRow()).set(ind.getNumCol(), field);
    }

    public int getGridSize(){
        return grid.size();
    }
    public abstract  ArrayList<Integer> getGridDomain();


    public boolean move(Indices ind, int value){
        Field field = getFieldForCoordinates(ind);
        if(field == null) {
            setField(Field.createForSingleValue(ind, value), ind);
            return true;
        } else {
            return field.setSingleValue(value);
        }
    }


    public abstract ArrayList<Integer> getPossibleValues(Indices ind);

    public abstract List<Integer> getForbiddenValues(Indices ind);



    List<Integer> getValuesAlreadyInSeq(List<Field> checkingSeq){
        List<Field> res = new ArrayList<>(checkingSeq);
        return res
                .stream()
                .filter(Objects::nonNull)
                .filter(Field::hasOneValue)
                .map(Field::getSingleValue)
                .collect(Collectors.toList());
    }


    private Tuple<Boolean, Boolean> removeForbiddenValues(Indices ind) {
        Field field = getFieldForCoordinates(ind);
        int numberOfPossibleValues = field.getNumberOfpossibleVals();
        boolean moreThanOneValLeft = field.removeForbiddenValues(getForbiddenValues(ind));
        int newNumber = field.getNumberOfpossibleVals();
        boolean possibleValuesChanged = (numberOfPossibleValues != newNumber);
        return new Tuple<>(moreThanOneValLeft, possibleValuesChanged);
    }


    public boolean removeAllForbiddenValues(){
        boolean valuesChanged = true;
        boolean result = true;

        while (valuesChanged && result) {
            valuesChanged = false;
            for (int i = 0; i < grid.size(); ++i)  {
                for (int j = 0; j < grid.size(); ++j) {
                    Indices ind = new Indices(i, j);
                    if (getFieldForCoordinates(ind) != null) {
                        Tuple<Boolean, Boolean> afterDeleteState = removeForbiddenValues(ind);
                        boolean moreThanOneValLeft = afterDeleteState.getKey();
                        boolean possibleValuesChanged = afterDeleteState.getValue();

                        if (!moreThanOneValLeft) {
                            result = false;
                        }
                        if (possibleValuesChanged) {
                            valuesChanged = true;
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public Field getFieldForCoordinates(Indices ind) {
        return this.grid.get(ind.getNumRow()).get(ind.getNumCol());
    }


    public void initializeWithPossibleValues(){
        for(int i=0; i < grid.size(); i++) {
            for (int j = 0; j < grid.size(); j++) {
               Indices ind = new Indices(i,j);
                if (getFieldForCoordinates(ind) == null) {
                    setField(Field.createForAvailableValues(ind, getPossibleValues(ind)), ind);
                }
            }
        }
    }

/*
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

*/

    public boolean validate() {
        for(int i = 0; i < getGridSize();i++) {
            if (getRow(i).stream().filter(Objects::nonNull).anyMatch(Field::hasNoValues) ||
                    getColumn(i).stream().filter(Objects::nonNull).anyMatch(Field::hasNoValues)) {
                System.out.println("Number of row and column: " + i);
                return false;
            }
        }
     return checkConstraints();

    }


    public boolean isPoorlyCompleted() {
        for (int i = 0; i < grid.size(); i++) {
            for (int j = 0; j < grid.size(); j++) {
                Indices ind = new Indices(i, j);
                Field field = getFieldForCoordinates(ind);
                if (field == null || !field.hasOneValue()) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<Field> getRow(int rowNum) {
        return new ArrayList<>(grid.get(rowNum));
    }

    public List<Field> getColumn(int colNum) {
        List<Field> result = new ArrayList<>();
        for (List<Field> fields : grid) {
            result.add(fields.get(colNum));
        }
        return result;
    }

    /*

    public List<Field> getSequence(int index, String name){
        List<Field> sequence = new ArrayList<>();
        switch(name){
            case "row":
                sequence.addAll(grid.get(index));
                break;
            case "col":
                List<Field> col = new ArrayList<>();
                for(List<Field> row: grid){
                    sequence.add(row.get(index));
                }
                break;
            default:
                break;

        }
        return sequence;

    }

     */

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(int i=0; i < grid.size(); i++) {
            for (int j = 0; j < grid.size(); j++) {
                Field field = getFieldForCoordinates(new Indices(i, j));
                if (field != null) {
                    builder.append(field);
                } else {
                    builder.append(String.format("%4s", "-"));
                }
            }
            builder.append("\n");
        }
        builder.append("\nConst:");
        builder.append(displayConstraints());
        builder.append("\n\nisValid: ").append(validate());
        return builder.toString();
    }




}













