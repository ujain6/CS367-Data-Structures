import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * SetTesterADT implementation using an ArrayList as the data structure.
 *
 * DO NOT MODIFY THIS CLASS.
 *
 * @author CS367
 */
public class ArrayListSetTester<K extends Comparable<K>> implements SetTesterADT<K> {
    
    List<K> listSet;
    
    public ArrayListSetTester() {
        listSet = new ArrayList<K>();
    }

    @Override
    public void add(K item) {
        listSet.add(item);
    }

    @Override
    public boolean contains(K item) {
        return listSet.contains(item);
    }

    @Override
    public List<K> subSet(K minValue, K maxValue) {
        List<K> result = new ArrayList<>();
        for(K elem : listSet){
            if(elem.compareTo(minValue) > 0 && elem.compareTo(maxValue) < 0){
                result.add(elem);
            }
        }
        return result;
    }

    @Override
    public void clear() {
        listSet.clear();
        
    }

    @Override
    public int size(){
        return listSet.size();
    }

    @Override
    public void displayTree(int maxDisplayLevels){
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        return listSet.iterator();
    }
}
