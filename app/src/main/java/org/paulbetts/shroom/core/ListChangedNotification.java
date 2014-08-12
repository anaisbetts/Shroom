package org.paulbetts.shroom.core;

/**
* Created by paul on 8/12/14.
*/
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

    public static <T> ListChangedNotification<T> add(int index, T item) {
        return new ListChangedNotification<T>(ListChangedType.ADD, index, IterableEx.justIterable(item), index, IterableEx.emptyIterable());
    }

    public static <T> ListChangedNotification<T> remove(int index, T item) {
        return new ListChangedNotification<T>(ListChangedType.REMOVE, index, IterableEx.emptyIterable(), index, IterableEx.justIterable(item));
    }
}
