import java.util.Iterator;
import java.util.List;

/**
 * SetTester ADT specifies the operations needed to test different
 * implementations of a set storing key values. It is not a full specification
 * of a set operations, e.g., delete is not included.
 *
 * DO NOT CHANGE THIS INTERFACE.
 *
 * @author CS367
 */

public interface SetTesterADT<K> extends Iterable<K> {

    /**
     * Add the key to the set.
     *
     * @param key the key to add
     * @throws IllegalArgumentException if the key is null
     * @throws DuplicateKeyException if the key is a duplicate
     */
    public void add(K key);

    /**
     * Return true iff the set contains the key.
     *
     * @param key the key to search
     * @return true iff the tester contains the key
     * @throws IllegalArgumentException if the key is null
     */
    public boolean contains(K key);

    /**
     * Returns the sorted list of keys in the tree that are in the specified
     * range (inclusive of minValue, exclusive of maxValue).
     *
     * @param minValue the minimum value of the desired range (inclusive)
     * @param maxValue the maximum value of the desired range (exclusive)
     * @return the sorted list of keys in the specified range
     * @throws IllegalArgumentException if either minValue or maxValue is
     * null, or minValue is larger than maxValue
     */
    public List<K> subSet(K minValue, K maxValue);

    /**
     * Clears all the keys in the set.
     */
    public void clear();

    /**
     * Returns the number of keys in the set.
     *
     * @return the number of keys
     */
    int size();

    /**
     * Displays the set but only maxNumLevels from the top of the data
     * structure used by the implementation.
     *
     * @param maxDisplayLevels number of levels to display
     */
    public void displayTree(int maxDisplayLevels);

    /**
     * Returns an iterator for the set.
     *
     * @return the iterator
     */
    public Iterator<K> iterator();
}
