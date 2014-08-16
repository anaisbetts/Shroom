package org.paulbetts.shroom.core;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by paul on 8/12/14.
 */
public abstract class ModelViewHolder<TModel> extends RecyclerView.ViewHolder {
    public ModelViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bindModel(TModel item);
}
