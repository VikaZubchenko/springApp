package pz.sptingapp.MyList;

interface IMyList<T> extends Iterable<T> {
    void add(T e);
    void clear();
    boolean remove(T o);
    Object[] toArray();
    int size();
    boolean contains(T o);
    String toString();
}
