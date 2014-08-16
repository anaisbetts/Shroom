package org.paulbetts.shroom.core;

import android.support.v7.widget.RecyclerView;

import rx.Subscription;

/**
 * Created by paul on 8/12/14.
 */
public abstract class ReactiveListAdapter<T> extends RecyclerView.Adapter<ModelViewHolder<T>> implements Subscription {
    private final ReactiveArrayList<T> source;
    private Subscription inner;

    public ReactiveListAdapter(ReactiveArrayList<T> source) {
        this.source = source;

        inner = source.getChanged().subscribe(x -> {
            // TODO: Range changes
            switch (x.type) {
            case ADD:
                this.notifyItemInserted(x.newStartingIndex);
                break;
            case REMOVE:
                this.notifyItemRemoved(x.newStartingIndex);
                break;
            case REPLACE:
                this.notifyItemChanged(x.newStartingIndex);
                break;
            case RESET:
                this.notifyDataSetChanged();
                break;
            }
        });

        this.notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ModelViewHolder<T> modelViewHolder, int i) {
        modelViewHolder.bindModel(source.get(i));
    }

    @Override
    public int getItemCount() {
        return source.size();
    }

    public void unsubscribe() {
        if (inner.isUnsubscribed()) return;
        inner.unsubscribe();
    }

    public boolean isUnsubscribed() {
        return inner.isUnsubscribed();
    }
}
