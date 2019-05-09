import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

/**
 * SetTesterADT implementation using a Java's Treeset, which uses a Red-Black
 * tree as the data structure.
 *
 * DO NOT MODIFY THIS CLASS.
 *
 * @author CS367
 */

public class RBTreeSetTester<K extends Comparable<K>> implements SetTesterADT<K> {
    
    TreeSet<K> rbTree;
    
    public RBTreeSetTester() {
        rbTree = new TreeSet<K>();
    }

    @Override
    public void add(K item) {
        rbTree.add(item);
    }

    @Override
    public boolean contains(K item) {
        return rbTree.contains(item);
    }

    @Override
    public List<K> subSet(K minValue, K maxValue) {
        return new ArrayList<K>(rbTree.subSet(minValue, maxValue));
    }

    @Override
    public void clear() {
        rbTree.clear();
    }

    @Override
    public int size(){
        return rbTree.size();
    }

    @Override
    public void displayTree(int maxDisplayLevels){
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        return rbTree.iterator();
    }
}
