package com.litesnap.open.load;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class BaseHolder<T> extends RecyclerView.ViewHolder {
    protected int viewType;
    public BaseHolder(@NonNull View itemView) {
        super(itemView);
        viewType = -1;
    }

    public BaseHolder(@NonNull View itemView, int viewType) {
        super(itemView);
        this.viewType = viewType;
    }

    public int getViewType() {
        return viewType;
    }

    public abstract void bindHolder(T bean, Context context);
}
