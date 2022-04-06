import java.io.Serializable;

public class Tuple<K, V> implements Serializable {
    private K key;

    private V value;

    public Tuple(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }



}