package com.shqiansha.adapter;

import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import com.shqiansha.adapter.listener.OnItemChildClickListener;
import com.shqiansha.adapter.listener.OnItemClickListener;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EasyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private SparseArray<View> views;
    private SparseArray<OnItemChildClickListener> clickListeners;
    private OnItemClickListener onItemClickListener;
    public EasyHolder(@NonNull View itemView) {
        super(itemView);
        this.views=new SparseArray<>();
        this.clickListeners=new SparseArray<>();

        itemView.setOnClickListener(this);
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
    protected EasyHolder addOnItemChildClickListener(@IdRes int viewId, OnItemChildClickListener listener){
        View view=getView(viewId);
        view.setOnClickListener(this);
        clickListeners.put(viewId,listener);
        return this;
    }
    protected EasyHolder addOnItemChildClickListeners(SparseArray<OnItemChildClickListener> listeners){
        for(int i=0;i<listeners.size();i++){
            int key=listeners.keyAt(i);
            addOnItemChildClickListener(key,listeners.get(key));
        }
        return this;
    }

    protected EasyHolder setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        return this;
    }

    @Override
    public void onClick(View view) {
        OnItemChildClickListener onItemChildClickListener=clickListeners.get(view.getId());
        if(onItemChildClickListener!=null) {
            onItemChildClickListener.onClick(view,getAdapterPosition());
        }
        else if(onItemClickListener!=null) {
            onItemClickListener.onClick(view,getAdapterPosition());
        }
    }
}
