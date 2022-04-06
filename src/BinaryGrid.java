import java.util.*;
import java.util.stream.Collectors;

public class BinaryGrid extends AbstractGrid {
    List<BinaryConstraint> constraints;

    protected BinaryGrid(int size) {
        super(size);
        constraints = new ArrayList<>();
    }

    public BinaryGrid(BinaryGrid grid) {
        super(grid);
        this.constraints = grid.constraints;
    }

    @Override
    public Grid copy() {
       return new BinaryGrid(this);
    }

    public ArrayList<Integer> getGridDomain() {
        ArrayList<Integer> domain = new ArrayList<>();
        domain.add(0);
        domain.add(1);
        return domain;

    }

    private void add(BinaryConstraint constr){
        if(!constraints.contains(constr)){
            constraints.add(constr);
        }
    }

    @Override
    public List<Integer> getForbiddenValues(Indices ind) {
        for (BinaryConstraint constr : constraints) {
            if(constr.getLeftSide() == ind || constr.getRightSide() == ind)
                return List.of(constr.getForbiddenValue());
        }
        return new ArrayList<>();
    }

    public void addConstraints(Indices ind){

        addConstraintsForRow( ind);
        addConstraintsForColumn(ind);
    }

    @Override
    public ArrayList<Integer> getPossibleValues(Indices ind){
        Field field = getFieldForCoordinates(ind);
        if(field != null) {
            if (field.hasMoreThanOnePossibility())
                return new ArrayList<>(getGridDomain());
            else
                return new ArrayList<>();
        }
        ArrayList<Integer> newPossibleVals = getGridDomain();
        newPossibleVals.removeAll(getForbiddenValues(ind));
        return newPossibleVals;

    }


    @Override
    public boolean move(Indices ind, int value){
        addInitialConstraints();
        Field field = getFieldForCoordinates(ind);
        if(field == null) {
            setField(Field.createForSingleValue(ind, value), ind);
            return true;
        } else {
            return field.setSingleValue(value);
        }
    }


    public void addInitialConstraints(){
        constraints.clear();
        for(int i=0; i < getGridSize(); i++) {
            for (int j = 0; j < getGridSize(); j++) {
                Indices ind = new Indices(i, j);
                addConstraints(ind);
            }
        }
        addAllConstraintOnNumberOfValues();
    }

    private void addConstraintsForRow(Indices ind){
        if (ind.getNumCol() != 0 ) {
            Indices previousFieldInRow = new Indices(ind.getNumRow(), ind.getNumCol()-1);
            Field previousField = getFieldForCoordinates(previousFieldInRow);
            Field currentField = getFieldForCoordinates(ind);
            if(previousField != null && currentField!=null) {
                if (previousField.hasOneValue() && currentField.hasOneValue() && (previousField.getSingleValue() == currentField.getSingleValue())) {
                    if (ind.getNumCol() == 1) {
                        add(new BinaryConstraint(ind.getNextIndicesInRow(), previousField.getSingleValue(), true));
                        setField(Field.createForSingleValue(ind.getNextIndicesInRow(), Math.abs(previousField.getSingleValue() - 1)), ind.getNextIndicesInRow());
                    }
                   else if(ind.getNumCol() == getGridSize() - 1) {
                        add(new BinaryConstraint(previousFieldInRow.getPreviousIndicesInRow(), previousField.getSingleValue(), false));
                        setField(Field.createForSingleValue(previousFieldInRow.getPreviousIndicesInRow(), Math.abs(previousField.getSingleValue() - 1)), previousFieldInRow.getPreviousIndicesInRow());
                    }
                   else{
                        add(new BinaryConstraint(previousFieldInRow.getPreviousIndicesInRow(), ind.getNextIndicesInRow(), previousField.getSingleValue()));
                        setField(Field.createForSingleValue(previousFieldInRow.getPreviousIndicesInRow(), Math.abs(previousField.getSingleValue() - 1)), previousFieldInRow.getPreviousIndicesInRow());
                        setField(Field.createForSingleValue(ind.getNextIndicesInRow(), Math.abs(previousField.getSingleValue() - 1)), ind.getNextIndicesInRow());
                    }
                }
            }
        }

    }



    private void addConstraintsForColumn(Indices ind){
        if (ind.getNumRow() != 0 && ind.getNumRow() != getGridSize() - 1) {
            Indices previousFieldInCol = new Indices(ind.getNumRow()-1, ind.getNumCol());
            Field previousField = getFieldForCoordinates(previousFieldInCol);
            Field currentField = getFieldForCoordinates(ind);
            if(previousField != null && currentField!=null) {
                if (previousField.hasOneValue() && currentField.hasOneValue() && (previousField.getSingleValue() == currentField.getSingleValue())) {
                    if (ind.getNumRow() == 1) {
                        add(new BinaryConstraint(ind.getNextIndicesInColumn(), previousField.getSingleValue(), true));
                        setField(Field.createForSingleValue(ind.getNextIndicesInColumn(), Math.abs(previousField.getSingleValue() - 1)), ind.getNextIndicesInColumn());
                    }
                    else if(ind.getNumRow() == getGridSize() - 1) {
                        add(new BinaryConstraint(previousFieldInCol.getPreviousIndicesInColumn(), previousField.getSingleValue(), false));
                        setField(Field.createForSingleValue(previousFieldInCol.getPreviousIndicesInColumn(), Math.abs(previousField.getSingleValue() - 1)), previousFieldInCol.getPreviousIndicesInColumn());
                    }
                    else {
                        add(new BinaryConstraint(previousFieldInCol.getPreviousIndicesInColumn(), ind.getNextIndicesInColumn(), previousField.getSingleValue()));
                        setField(Field.createForSingleValue(ind.getNextIndicesInColumn(), Math.abs(previousField.getSingleValue() - 1)), ind.getNextIndicesInColumn());
                        setField(Field.createForSingleValue(previousFieldInCol.getPreviousIndicesInColumn(), Math.abs(previousField.getSingleValue() - 1)), previousFieldInCol.getPreviousIndicesInColumn());
                    }
                }
            }
        }
    }

    private void addConstraintOnNumberOfValues(Tuple<Integer, Integer> zerosOnes, int i) {
        if(zerosOnes.getKey() >= getGridSize()){
            for(int j = 0 ;j < getGridSize(); j++) {
                Indices  ind = new Indices(j, i);
                Field field = getFieldForCoordinates(ind);
                add(new BinaryConstraint(ind, 1, false));
                setField(Field.createForSingleValue(ind, 0), ind);

            }
        } else if(zerosOnes.getValue() >= getGridSize()){
            for(int j = 0 ;j < getGridSize(); j++) {
                Indices  ind = new Indices(j, i);
                Field field = getFieldForCoordinates(ind);
                add(new BinaryConstraint(new Indices(j, i), 0, false));
                setField(Field.createForSingleValue(ind, 1), ind);
            }
        }

    }

    private void addAllConstraintOnNumberOfValues(){
        for(int i= 0 ; i < getGridSize(); i++){
            Tuple<Integer, Integer> zerosOnes = getNumberOfValues(getValuesAlreadyInSeq(getColumn(i)));
            Tuple<Integer, Integer> zerosOnesRow = getNumberOfValues(getValuesAlreadyInSeq(getRow(i)));
            addConstraintOnNumberOfValues(zerosOnes, i);
            addConstraintOnNumberOfValues(zerosOnesRow, i);
        }

    }

    private boolean areSequencesUnique(){
        List<List<Integer>> rows = new ArrayList<>();
        List<List<Integer>> cols = new ArrayList<>();
        for(int i= 0 ; i < getGridSize(); i++) {
           cols.add(getValuesAlreadyInSeq(getColumn(i)));
           rows.add(getValuesAlreadyInSeq(getRow(i)));
        }
        return areUnique(cols) && areUnique(rows);
    }


    @Override
    protected boolean checkConstraints() {
        if(isCorrectNumberOfValuesInSequence() || areSequencesUnique()) {
            for (BinaryConstraint constr : constraints) {
                switch(constr.type()) {
                    case insideDuplicates:
                        Field leftSideField = getFieldForCoordinates(constr.getLeftSide());
                        Field rightSideField = getFieldForCoordinates(constr.getRightSide());
                        if (leftSideField != null && rightSideField != null) {
                            if (rightSideField.hasOneValue() && leftSideField.hasOneValue()) {
                                if (leftSideField.getSingleValue() != rightSideField.getSingleValue() || leftSideField.getSingleValue() == constr.getForbiddenValue()) {
                                    return false;
                                }
                            }
                        }
                        break;

                    case beginEdgeDuplicates:
                        Indices ind = constr.getRightSide();
                        Field nextToDuplicates = getFieldForCoordinates(ind);
                        if(nextToDuplicates != null && nextToDuplicates.getSingleValue() == constr.getForbiddenValue()){
                            return false;
                        }
                        break;

                    case endEdgeDuplicates:
                        Indices ind2 = constr.getLeftSide();
                        Field previousDupl = getFieldForCoordinates(ind2);
                        if(previousDupl != null && previousDupl.getSingleValue() == constr.getForbiddenValue()){
                            return false;
                        }
                        break;

                    default:
                        break;
                }
            }
            return true;
        }
        return false;
    }


    @Override
    protected String displayConstraints() {
        StringBuilder builder = new StringBuilder();
        for (BinaryConstraint constraint : constraints) {
            builder.append("\n").append(constraint);
        }
        return builder.toString();
    }



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

    private Tuple<Integer, Integer> getNumberOfValues(List<Integer> checkingSeq){
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


}





