import java.util.ArrayList;
import java.util.List;

public class Field {
    private List<Integer> possibleVals;
    private final Indices indices;
    private static boolean DEFAULT_VAL_SET = true;



   public Field(Field field){
       this.indices = field.indices;
       this.possibleVals = new ArrayList<>(field.possibleVals);

   }

   public Field(Indices ind, Grid grid){
       this.indices = ind;
       this.possibleVals=grid.getGridDomain();
   }

    public Field(Indices ind) {
       this.indices = ind;
       this.possibleVals = new ArrayList<>();

    }

    public static Field createForSize(Indices ind, int size) {
        Field field = new Field(ind);
        for (int i = 1; i <= size; i++) {
            field.possibleVals.add(i);
        }

        return field;
    }
    public static Field createForAvailableValues(Indices indices, List<Integer> possibleVals) {
        Field result = new Field(indices);
        result.possibleVals = new ArrayList<>(possibleVals);
        return result;
    }

    public static Field createForSingleValue(Indices indices, int value) {
        DEFAULT_VAL_SET = false;
        Field result = new Field(indices);
        result.possibleVals.add(value);
        return result;
    }



    public boolean hasOneValue() {
        return possibleVals.size() == 1;
    }

    public Integer getSingleValue() {
        if (this.possibleVals.size() != 1) {
            throw new IllegalStateException("Field does not have exactly one value");
        }
        return this.possibleVals.get(0);
    }

    public boolean setSingleValue(int value) {  //reset ad set
        if (possibleVals.contains(value)) {
            possibleVals.clear();

            possibleVals.add(value);
            DEFAULT_VAL_SET = false;
            return true;
        }
        return false;
    }

    public boolean hasNoValues() {
        return possibleVals.size() == 0;
    }

    public List<Integer> getPossibleVals() {
        return this.possibleVals;
    }

    public int getNumberOfpossibleVals() {
        return possibleVals.size();
    }

    public boolean hasMoreThanOnePossibility(){
       return getNumberOfpossibleVals() > 1;
    }

    public boolean removeForbiddenValues(List<Integer> forbidenValues) {
        possibleVals.removeAll(forbidenValues);
        return possibleVals.size() >= 1;
    }
    public boolean notDefault_value(){
       return !DEFAULT_VAL_SET;
    }

    public Indices getIndices() {
        return indices;
    }

    @Override
    public String toString() {
        String result;
        if (possibleVals.size() == 1) {
            result = possibleVals.get(0).toString();
        } else if (possibleVals.size() < 1) {
            result = "!";
        } else result = "-";
        return String.format("%4s", result);
    }
}
