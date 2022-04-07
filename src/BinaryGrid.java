import java.util.*;

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

    public List<BinaryConstraint> getConstraints(){
        return constraints;
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

        Field field = getFieldForCoordinates(ind);
        if(field == null) {
            setField(Field.createForSingleValue(ind, value), ind);
            return true;
        } else {
            return field.setSingleValue(value);
        }
    }


    public void rebuildConstraints(){
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
                        add(new BeginEdgeDuplicates(ind.getNextIndicesInRow(), previousField.getSingleValue()));
                        setField(Field.createForSingleValue(ind.getNextIndicesInRow(), Math.abs(previousField.getSingleValue() - 1)), ind.getNextIndicesInRow());
                    }
                   else if(ind.getNumCol() == getGridSize() - 1) {
                        add(new EndEdgeDuplicates(previousFieldInRow.getPreviousIndicesInRow(), previousField.getSingleValue()));
                        setField(Field.createForSingleValue(previousFieldInRow.getPreviousIndicesInRow(), Math.abs(previousField.getSingleValue() - 1)), previousFieldInRow.getPreviousIndicesInRow());
                    }
                   else{
                        add(new InsideDuplicates(previousFieldInRow.getPreviousIndicesInRow(), ind.getNextIndicesInRow(), previousField.getSingleValue()));
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
                        add(new BeginEdgeDuplicates(ind.getNextIndicesInColumn(), previousField.getSingleValue()));
                        setField(Field.createForSingleValue(ind.getNextIndicesInColumn(), Math.abs(previousField.getSingleValue() - 1)), ind.getNextIndicesInColumn());
                    }
                    else if(ind.getNumRow() == getGridSize() - 1) {
                        add(new EndEdgeDuplicates(previousFieldInCol.getPreviousIndicesInColumn(), previousField.getSingleValue()));
                        setField(Field.createForSingleValue(previousFieldInCol.getPreviousIndicesInColumn(), Math.abs(previousField.getSingleValue() - 1)), previousFieldInCol.getPreviousIndicesInColumn());
                    }
                    else {
                        add(new InsideDuplicates(previousFieldInCol.getPreviousIndicesInColumn(), ind.getNextIndicesInColumn(), previousField.getSingleValue()));
                        setField(Field.createForSingleValue(ind.getNextIndicesInColumn(), Math.abs(previousField.getSingleValue() - 1)), ind.getNextIndicesInColumn());
                        setField(Field.createForSingleValue(previousFieldInCol.getPreviousIndicesInColumn(), Math.abs(previousField.getSingleValue() - 1)), previousFieldInCol.getPreviousIndicesInColumn());
                    }
                }
            }
        }
    }

    private void addConstraintOnNumberOfValues(Tuple<Integer, Integer> zerosOnes, int i) {
        if(zerosOnes.getKey() >= getGridSize()){
            for(int j = 0 ; j < getGridSize(); j++) {
                Indices  ind = new Indices(j, i);
                Field field = getFieldForCoordinates(ind);
                add(new NoMoreThisValue(ind, 1));
                setField(Field.createForSingleValue(ind, 0), ind);

            }
        } else if(zerosOnes.getValue() >= getGridSize()){
            for(int j = 0 ; j < getGridSize(); j++) {
                Indices  ind = new Indices(j, i);
                Field field = getFieldForCoordinates(ind);
                add(new NoMoreThisValue(ind, 0));
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



    //it based on the possible values for for each indices left
    @Override
    protected boolean checkConstraints() {
        boolean iNSEQ = isCorrectNumberOfValuesInSequence();
        boolean result =true;
        rebuildConstraints();
            for (BinaryConstraint constr : constraints) {
                 result = constr.checkConstraints(this, constr);
            }
        return result && iNSEQ;
    }


    @Override
    protected String displayConstraints() {
        StringBuilder builder = new StringBuilder();
        for (BinaryConstraint constraint : constraints) {
            builder.append("\n").append(constraint);
        }
        return builder.toString();
    }











}





