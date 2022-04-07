import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class BinaryConstraint implements Constraint{

    private Indices nextInd;
    private Indices previousInd;
    private int forbiddenValue;
    private boolean getNext = true;

    public BinaryConstraint(Indices ind,int forbiddenValue, boolean next){
        this.forbiddenValue = forbiddenValue;
        this.getNext = next;

        if(getNext){
            //this.type = BinaryConstraintsType.beginEdgeDuplicates;
            this.nextInd = ind;
            this.previousInd =null;


        }else{
           // this.type = BinaryConstraintsType.endEdgeDuplicates;
            this.nextInd = null;
            this.previousInd = ind;

        }

    }

    public BinaryConstraint(Indices previousInd, Indices nextInd, int forbiddenValue){
        this.forbiddenValue = forbiddenValue;
        this.previousInd = previousInd;
        this.nextInd = nextInd;
       // this.type = BinaryConstraintsType.insideDuplicates;


    }

    public abstract boolean checkConstraints(Grid grid, BinaryConstraint constr);

    public Indices getLeftSide(){
        return previousInd;
    }

    public Indices getRightSide(){
        return nextInd;
    }

    public int getForbiddenValue(){
        return forbiddenValue;
    }

  /* public BinaryConstraintsType type(){
        return type;
    }
*/

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append( "Previous Indices : " + Utilities.coalesce(previousInd,"edge"));
        sb.append( " Forbidden Val : " + forbiddenValue);
        sb.append(" Next Indices : " + Utilities.coalesce(nextInd,"edge"));
        return sb.toString();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BinaryConstraint that = (BinaryConstraint) o;
        return forbiddenValue == that.forbiddenValue && getNext == that.getNext  && Objects.equals(nextInd, that.nextInd) && Objects.equals(previousInd, that.previousInd);
    }

    @Override
    public int hashCode() {
        return Objects.hash( nextInd, previousInd, forbiddenValue, getNext);
    }
}










