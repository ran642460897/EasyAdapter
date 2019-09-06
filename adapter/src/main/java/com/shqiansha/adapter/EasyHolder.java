package com.shqiansha.adapter;

import android.util.SparseArray;
import android.view.View;

import com.shqiansha.adapter.listener.OnRecyclerViewClickListener;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EasyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private SparseArray<View> views;
    private SparseArray<OnRecyclerViewClickListener> clickListeners;
    public EasyHolder(@NonNull View itemView) {
        super(itemView);
        this.views=new SparseArray<>();
        this.clickListeners=new SparseArray<>();
    }
    @SuppressWarnings("unchecked")
    public <T extends View> T getView(@IdRes int viewId) {
        View view = views.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            views.put(viewId, view);
        }
        return (T) view;
    }
    protected EasyHolder addOnRecyclerViewClickListener(@IdRes int viewId,OnRecyclerViewClickListener listener){
        View view=getView(viewId);
        view.setOnClickListener(this);
        clickListeners.put(viewId,listener);
        return this;
    }
    protected EasyHolder addOnRecyclerViewClickListeners(SparseArray<OnRecyclerViewClickListener> listeners){
        for(int i=0;i<listeners.size();i++){
            int key=listeners.keyAt(i);
            addOnRecyclerViewClickListener(key,listeners.get(key));
        }
        return this;
    }

    @Override
    public void onClick(View view) {
        OnRecyclerViewClickListener listener=clickListeners.get(view.getId());
        if(listener!=null) listener.onClick(view,getAdapterPosition());
    }
}
