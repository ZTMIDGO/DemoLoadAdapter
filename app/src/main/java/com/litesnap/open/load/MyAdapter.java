package com.litesnap.open.load;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class MyAdapter extends LoadAdapter {
    public MyAdapter(Context context, List<Object> list) {
        super(context, list);
    }

    @Override
    protected BaseHolder createHolder(ViewGroup viewGroup, int viewType) {
        View view = mInflater.inflate(R.layout.button_layout, viewGroup, false);
        return new MyHolder(view, viewType);
    }

    @Override
    protected void bindHolder(BaseHolder holder, int position) {

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

    }

    public class MyHolder extends BaseHolder {
        public MyHolder(@NonNull View itemView, int viewType) {
            super(itemView, viewType);
        }

        @Override
        public void bindHolder(Object bean, Context context) {

        }
    }
}
