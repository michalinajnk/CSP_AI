import java.util.Objects;

public class Indices {
    int numRow;
    int numCol;

    public Indices(int i , int j ){
        numRow = i;
        numCol = j;
    }

    public int getNumCol() {
        return numCol;
    }

    public int getNumRow() {
        return  numRow;
    }

    public Indices getPreviousIndicesInRow(){
        return new Indices(getNumRow(), getNumCol()-1);
    }

    public Indices getPreviousIndicesInColumn(){
        return new Indices(getNumRow() -1, getNumCol());
    }

    public Indices getNextIndicesInRow(){
        return new Indices(getNumRow(), getNumCol()+1);
    }

    public Indices getNextIndicesInColumn(){
        return new Indices(getNumRow() +1, getNumCol());
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Indices Indices = (Indices) o;
        return numRow == Indices.numRow &&
                numCol == Indices.numCol;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numRow, numCol);
    }

    @Override
    public String toString() {
        return String.valueOf((char) (((int) 'A') + numRow)) + numCol;
    }
}
