package org.paulbetts.shroom.core;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

import rx.subjects.PublishSubject;

/**
 * Created by paul on 8/10/14.
 */
public class ReactiveArrayList<T> implements List<T>, Serializable, RandomAccess {
    private ArrayList<T> innerList = new ArrayList<>();
    private final PublishSubject<ListChangedNotification<T>> changed = PublishSubject.create();

    public ReactiveArrayList() {
    }

    @Override
    public void add(int i, T t) {

    }

    @Override
    public boolean add(T t) {
        this.add(innerList.size(), t);
        return true;
    }

    @Override
    public boolean addAll(int i, Collection<? extends T> ts) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> ts) {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public boolean contains(Object o) {
        return innerList.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> objects) {
        return innerList.containsAll(objects);
    }

    @Override
    public T get(int i) {
        return innerList.get(i);
    }

    @Override
    public int indexOf(Object o) {
        return innerList.indexOf(o);
    }

    @Override
    public boolean isEmpty() {
        return innerList.isEmpty();
    }

    @NonNull
    @Override
    public Iterator<T> iterator() {
        // XXX: Remove!
        return innerList.iterator();
    }

    @Override
    public int lastIndexOf(Object o) {
        return innerList.lastIndexOf(o);
    }

    @NonNull
    @Override
    public ListIterator<T> listIterator() {
        // XXX: Remove! Add!
        return innerList.listIterator();
    }

    @NonNull
    @Override
    public ListIterator<T> listIterator(int i) {
        // XXX: Remove! Add!
        return innerList.listIterator(i);
    }

    @Override
    public T remove(int i) {
        return null;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> objects) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> objects) {
        return false;
    }

    @Override
    public T set(int i, T t) {
        return null;
    }

    @Override
    public int size() {
        return innerList.size();
    }

    @NonNull
    @Override
    public List<T> subList(int i, int i2) {
        return innerList.subList(i, i2);
    }

    @NonNull
    @Override
    public Object[] toArray() {
        return innerList.toArray();
    }

    @NonNull
    @Override
    public <T1> T1[] toArray(T1[] t1s) {
        return innerList.toArray(t1s);
    }

    private void writeObject(java.io.ObjectOutputStream out)
            throws IOException {
        // Don't serialize the Subject, only the actual data
        out.writeObject(innerList);
    }

    private void readObject(java.io.ObjectInputStream in)
            throws IOException, ClassNotFoundException {
        // Don't serialize the Subject, only the actual data
        innerList = (ArrayList<T>)in.readObject();
    }

    public enum ListChangedType {
        ADD, REMOVE, REPLACE, MOVE, RESET,
    }

    public final class ListChangedNotification<TList> {
        public ListChangedType type;

        public int newStartingIndex;
        public Iterable<TList> newItems;

        public int oldStartingIndex;
        public Iterable<TList> oldItems;

        public ListChangedNotification(ListChangedType type, int newStartingIndex, Iterable<TList> newItems, int oldStartingIndex, Iterable<TList> oldItems) {
            this.type = type;
            this.newStartingIndex = newStartingIndex;
            this.newItems = newItems;
            this.oldStartingIndex = oldStartingIndex;
            this.oldItems = oldItems;
        }
    }

    Iterable<T> justIterable(T item) {
        return new Iterable<T>() {
            boolean hasNext = false;

            @Override
            public Iterator<T> iterator() {
                return new Iterator<T>() {
                    @Override
                    public boolean hasNext() {
                        return hasNext;
                    }

                    @Override
                    public T next() {
                        hasNext = false;
                        return item;
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    Iterable<T> empty = null;
    Iterable<T> emptyIterable() {
        empty = (empty != null ? empty : new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return new Iterator<T>() {
                    @Override
                    public boolean hasNext() {
                        return false;
                    }

                    @Override
                    public T next() {
                        throw new UnsupportedOperationException();
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        });

        return empty;
    }
}
