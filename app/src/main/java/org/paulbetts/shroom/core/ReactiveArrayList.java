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
        innerList.add(i, t);
        changed.onNext(ListChangedNotification.add(i, t));
    }

    @Override
    public boolean add(T t) {
        this.add(innerList.size(), t);
        return true;
    }

    @Override
    public boolean addAll(int i, Collection<? extends T> ts) {
        boolean ret = innerList.addAll(i, ts);
        if (!ret) return ret;

        changed.onNext(new ListChangedNotification<>(ListChangedType.ADD, i, (Iterable<T>)ts, i, IterableEx.empty()));
        return ret;
    }

    @Override
    public boolean addAll(Collection<? extends T> ts) {
        return this.addAll(innerList.size(), ts);
    }

    @Override
    public void clear() {
        innerList.clear();
        changed.onNext(ListChangedNotification.reset());
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
        T ret = innerList.remove(i);
        changed.onNext(ListChangedNotification.remove(i, ret));
        return ret;
    }

    @Override
    public boolean remove(Object o) {
        int index = innerList.indexOf(o);
        if (index < 0) return false;

        this.remove(index);
        return true;
    }

    @Override
    public boolean removeAll(Collection<?> objects) {
        boolean ret = false;

        // XXX: Replace this with range at some point
        for(Object obj: objects) {
            ret |= this.remove(obj);
        }

        return ret;
    }

    @Override
    public boolean retainAll(Collection<?> objects) {
        return false;
    }

    @Override
    public T set(int i, T t) {
        T old = innerList.get(i);

        innerList.set(i, t);
        changed.onNext(new ListChangedNotification<T>(ListChangedType.REPLACE, i, IterableEx.just(old), i, IterableEx.just(t)));
        return old;
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
}
