import java.util.function.Supplier;
import static java.util.Arrays.asList;

public class Utilities {
    public static <T> T coalesce2(Supplier<T>... ts) {
        return asList(ts)
                .stream()
                .map(t -> t.get())
                .filter(t -> t != null)
                .findFirst()
                .orElse(null);
    }

    public static <T> T coalesce(T one, T two)
    {
        return one != null ? one : two;
    }

    public static int avoidZero(int one, int two){return one!= 0 ? one : two; }
}
