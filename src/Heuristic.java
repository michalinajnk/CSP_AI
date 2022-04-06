public interface Heuristic {
    Indices getNextField(Grid grid);
    String getName();
}
