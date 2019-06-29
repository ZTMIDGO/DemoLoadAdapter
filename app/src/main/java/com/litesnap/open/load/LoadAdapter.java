package com.litesnap.open.load;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public abstract class LoadAdapter<T, B extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<BaseHolder> {
    public static final int VIEW_FOOTER = -99;

    protected abstract BaseHolder createHolder(ViewGroup viewGroup, int viewType);
    protected abstract void bindHolder(BaseHolder holder, int position);

    protected boolean mIsLoad;
    protected boolean mCanLoad;

    private OnLoadingListener listener;
    protected List<T> mDataList;
    protected LayoutInflater mInflater;
    private RecyclerView mRecyclerView;
    protected View mLoadView;
    protected Context mContext;

    public LoadAdapter(Context context, List<T> list){
        mDataList = list;
        mInflater = LayoutInflater.from(context);
        mCanLoad = true;
        mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1){
            return VIEW_FOOTER;
        }

        if (getItemType(position) == VIEW_FOOTER){
            throw new RuntimeException("too more footer");
        }
        return getItemType(position);
    }

    @NonNull
    @Override
    public BaseHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == VIEW_FOOTER){
            return new LoadHolder(getLoadView(), viewType);
        }
        return createHolder(viewGroup, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position) {
        if (holder.getViewType() == VIEW_FOOTER){
            holder.bindHolder(null, mContext);
            return;
        }
        bindHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return mDataList.size() + 1;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        mRecyclerView = recyclerView;

        final RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        if (layoutManager instanceof GridLayoutManager){
            ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int i) {
                    switch (getItemViewType(i)){
                        case VIEW_FOOTER:
                            return ((GridLayoutManager) layoutManager).getSpanCount();
                        default:
                            return 1;
                    }
                }
            });
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    if (!mCanLoad){
                        return;
                    }

                    if (mIsLoad){
                        return;
                    }

                    if (getItemCount() <= mRecyclerView.getChildCount()){
                        mIsLoad = true;
                        return;
                    }

                    int index = getItemCount();

                    if (layoutManager instanceof LinearLayoutManager){
                        index = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    }else if (layoutManager instanceof GridLayoutManager){
                        index = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                    }else if (layoutManager instanceof StaggeredGridLayoutManager){

                    }

                    if (index >= getItemCount()){
                        mIsLoad = true;
                    }

                    if (listener != null){
                        listener.onLoad();
                    }
                }
            }
        });
    }

    protected int getItemType(int position){
        return 0;
    }

    public void addAll(T[] array){
        addAll(Arrays.asList(array));
    }

    public void addAll(List<T> list){
        if (list == null || list.size() == 0){
            return;
        }

        int index = mDataList.size();
        mDataList.addAll(index, list);
        notifyItemRangeInserted(index, list.size());
    }

    public void replaceAll(Collection<T> datas){
        mDataList.clear();
        mDataList.addAll(datas);
        notifyDataSetChanged();
    }

    public void replaceAll(T[] datas){
        replaceAll(new ArrayList<>(Arrays.asList(datas)));
    }

    public void setLoadView(View view) {
        mLoadView = view;
    }

    public View getLoadView() {
        return mLoadView;
    }

    public List<T> getDataList() {
        return mDataList;
    }

    public boolean isLoad(){
        return mIsLoad;
    }

    public void setCanLoad(boolean canLoad){
        mCanLoad = canLoad;
    }

    public boolean isCanLoad() {
        return mCanLoad;
    }

    public void setLoad(boolean isLoad){
        mIsLoad = isLoad;
    }

    public void setListener(OnLoadingListener listener) {
        this.listener = listener;
    }

    private class LoadHolder extends BaseHolder {
        public LoadHolder(@NonNull View itemView, int viewType) {
            super(itemView, viewType);
        }

        @Override
        public void bindHolder(Object bean, Context context) {
            if (mRecyclerView.getChildCount() >= getItemCount() - 1){
                itemView.setVisibility(View.GONE);
                return;
            }else {
                itemView.setVisibility(View.VISIBLE);
            }
        }
    }

    public interface OnLoadingListener{
        void onLoad();
    }
}
