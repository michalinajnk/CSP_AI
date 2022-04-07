import java.util.List;

public interface Grid {

    boolean validate();

    boolean isPoorlyCompleted();

    List<Integer> getPossibleValues(Indices indices);

    boolean move(Indices indices, int value);

    boolean removeAllForbiddenValues();

    boolean isCorrectNumberOfValuesInSequence();

    boolean areSequencesUnique();

    int getGridSize();

    List<Integer> getGridDomain();

    Field getFieldForCoordinates(Indices ind);

    void setField(Field field, Indices ind);

    void initializeWithPossibleValues();

    Grid copy();

    List<Field> getRow(int index);
    List<Field> getColumn(int index);

}