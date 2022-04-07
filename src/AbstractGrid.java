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
        int numberOfPossibleValues = field.getNumberOfPossibleValues();
        boolean moreThanOneValLeft = field.removeForbiddenValues(getForbiddenValues(ind));
        int newNumber = field.getNumberOfPossibleValues();
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
    @Override
    public boolean isCorrectNumberOfValuesInSequence() {
        for(int i=0; i< getGridSize(); i++) {
            List<Integer> checkingCol =  getValuesAlreadyInSeq(getColumn(i));
            List<Integer> checkingRow =  getValuesAlreadyInSeq(getRow(i));
            int size = getGridSize();
            Tuple<Integer, Integer> valuesInColumn = getNumberOfValues(checkingCol);
            boolean isFull = (valuesInColumn.getKey() + valuesInColumn.getValue() == getGridSize());

            if (isFull  &&  (valuesInColumn.getKey() != getGridSize() / 2 ||  valuesInColumn.getValue() != getGridSize() / 2)) {
                System.out.println("IS FULL AND Not correct number of val in column, numer of column: " + i);
                System.out.println("Values in column of 0: " + valuesInColumn.getKey() + " of column: " + i);
                System.out.println("Values in column of 1: " + valuesInColumn.getValue() + " of column: " + i);

                System.out.println("IS FULL AND Not correct number of val in column, numer of column: " + i);
                return false;
            }
            else if(valuesInColumn.getKey() > getGridSize() / 2 || valuesInColumn.getValue() > getGridSize() / 2) {
                System.out.println("ONE OF VAL IS MORE THAN THE HALS SIZE OF GRID in column");
                return false;
            }

            Tuple<Integer, Integer> valuesInRow = getNumberOfValues(checkingRow);
            isFull = (valuesInRow.getKey() + valuesInRow.getValue() == getGridSize());

            if (isFull  &&  (valuesInRow.getKey() != getGridSize() / 2 || valuesInRow.getValue() != getGridSize() / 2)) {
                System.out.println("IS FULL AND Not correct number of val in row");
                return false;
            }
            else if(valuesInRow.getKey() > getGridSize() / 2 || valuesInRow.getValue() > getGridSize() / 2) {
                System.out.println("ONE OF VAL IS MORE THAN THE HALS SIZE OF GRID in row");
                return false;
            }
        }
        return true;
    }

    protected Tuple<Integer, Integer> getNumberOfValues(List<Integer> checkingSeq){
        int ones = (int) checkingSeq.stream().filter(ele -> ele == 1).count();
        int zeros = (int) checkingSeq.stream().filter(ele -> ele == 0).count();
        return new Tuple(ones, zeros);
    }

    public boolean areUnique(List<List<Integer>> seques){
        HashSet unique = new HashSet();
        for(List<Integer> seq: seques ) {
            if(seq.size() == getGridSize())
                unique.add(seq);
        }
        return unique.size() == seques.size();
    }

    @Override
    public boolean areSequencesUnique(){
        List<List<Integer>> rows = new ArrayList<>();
        List<List<Integer>> cols = new ArrayList<>();
        for(int i= 0 ; i < getGridSize(); i++) {
            cols.add(getValuesAlreadyInSeq(getColumn(i)));
            rows.add(getValuesAlreadyInSeq(getRow(i)));
        }
        return areUnique(cols) && areUnique(rows);
    }


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













