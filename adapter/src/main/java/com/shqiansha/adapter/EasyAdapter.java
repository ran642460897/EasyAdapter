package com.shqiansha.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;


import com.shqiansha.adapter.listener.OnRecyclerViewClickListener;
import com.shqiansha.adapter.model.AdapterSetting;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public abstract class EasyAdapter<E> extends RecyclerView.Adapter<EasyHolder> {
    private List<E> listData=new ArrayList<>();
    private int footerItemType=ITEM_TYPE_FOOT_END;
    private Context context;
    private boolean showFooter=true;//是否展示页脚
    private SparseArray<OnRecyclerViewClickListener> clickListeners;
    private OnRecyclerViewClickListener onTopReloadListener,onBottomReloadListener;

    private int currentState=STATE_DEFAULT;

    private int layoutResId;

    private AdapterSetting setting;

    private static final int STATE_DEFAULT=0;//初始状态
    private static final int STATE_WAIT_FOR_LOADING=1;//等待加载状态
    private static final int STATE_LOADING=2;//加载中状态
    private static final int STATE_LOADING_FAILED=3;//加载失败状态
    private static final int STATE_END=4;//完结状态


    private static final int ITEM_TYPE_LIST=20;
    private static final int ITEM_TYPE_FOOT_END=30;
    private static final int ITEM_TYPE_FOOT_LOADING_BOTTOM=31;
    private static final int ITEM_TYPE_FOOT_LOADING_TOP=32;
    private static final int ITEM_TYPE_FOOT_EMPTY=33;
    private static final int ITEM_TYPE_FOOT_NET_ERROR_BOTTOM=34;
    private static final int ITEM_TYPE_FOOT_NET_ERROR_TOP=35;



    public EasyAdapter(Context context,int layoutResId,AdapterSetting setting){
        this.context=context;
        this.layoutResId=layoutResId;
        this.setting=setting;
        clickListeners=new SparseArray<>();
    }
    public EasyAdapter(Context context,int layoutResId){
        this(context,layoutResId,new AdapterSetting());
    }

    /**
     * 更新数据
     * @param data 数据源
     * @param clear 是否清空以前列表数据
     * @param end 是否结束更多加载
     */
    public void updateData(List<E> data,boolean clear,boolean end){
        if(clear) listData.clear();
        if(data!=null) listData.addAll(data);
        notifyDataSetChanged();
        setCurrentState(end?STATE_END:STATE_WAIT_FOR_LOADING);
    }
    public void updateData(List<E> data,boolean clear){
        updateData(data,clear,true);
    }
    public void updateData(List<E> data){
        updateData(data,true,true);
    }
    public void updateData(E item,int position){
        if(position<listData.size()&&position>-1&&item!=null){
            listData.set(position,item);
            notifyItemChanged(position);
        }
    }
    public void addData(E item,int position){
        if(position<=listData.size()&&position>-1&&item!=null){
            if(listData.size()==0) setFooterItemType(ITEM_TYPE_FOOT_END);
            listData.add(position,item);
            notifyItemInserted(position);
        }
    }
    //    public void addData(E item){
//        if(item!=null){
//            if(listData.size()==0) setFooterItemType(ITEM_TYPE_FOOT_END);
//            listData.add(item);
//            notifyItemInserted(listData.size()-1);
//        }
//    }
    public E getItem(int position){
        return listData.get(position);
    }
    public void removeItem(int position){
        if(position<listData.size()&&position>-1) {
            listData.remove(position);
            notifyItemRemoved(position);
            if(listData.size()==0) setFooterItemType(ITEM_TYPE_FOOT_EMPTY);
        }
    }

    /**
     * 页脚数据变更
     * @param type 页脚类型
     */
    private void setFooterItemType(int type){
        if(!showFooter) return;

        this.footerItemType=type;
        notifyItemChanged(listData.size());
    }

    /**
     * 设置当前状态
     * @param state 状态
     */
    public void setCurrentState(int state){
        this.currentState=state;
        switch (state){
            case STATE_LOADING:case STATE_WAIT_FOR_LOADING:
                setFooterItemType(listData.size()==0?ITEM_TYPE_FOOT_LOADING_TOP:ITEM_TYPE_FOOT_LOADING_BOTTOM);
                break;
            case STATE_LOADING_FAILED:
                setFooterItemType(listData.size()==0?ITEM_TYPE_FOOT_NET_ERROR_TOP:ITEM_TYPE_FOOT_NET_ERROR_BOTTOM);
                break;
            case STATE_END:
                setFooterItemType(listData.size()==0?ITEM_TYPE_FOOT_EMPTY:ITEM_TYPE_FOOT_END);
                break;
        }
    }

    /**
     * 加载失败
     */
    public void notifyLoadingFailed(){
        setCurrentState(STATE_LOADING_FAILED);
    }

    /**
     * 加载中
     */
    public void notifyLoading(){
        setCurrentState(STATE_LOADING);
    }

    public boolean isWaitingForLoading(){
        return this.currentState==STATE_WAIT_FOR_LOADING;
    }

    @Override
    public int getItemCount() {
        if(showFooter) return listData.size() + 1;
        else return listData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position+1==getItemCount()&&showFooter){
            return footerItemType;
        }
        else{
            return ITEM_TYPE_LIST;
        }
    }



        @NonNull
    @Override
    public EasyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==ITEM_TYPE_LIST) {
            return new EasyHolder(LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false)).addOnRecyclerViewClickListeners(clickListeners);
        }else {
            switch (viewType){
                case ITEM_TYPE_FOOT_END:
                    return new EasyHolder(LayoutInflater.from(parent.getContext()).inflate(setting.getEndLayoutResId(), parent, false));
                case ITEM_TYPE_FOOT_LOADING_TOP:
                    return new EasyHolder(LayoutInflater.from(parent.getContext()).inflate(setting.getTopLoadingLayoutResId(), parent, false));
                case ITEM_TYPE_FOOT_LOADING_BOTTOM:
                    return new EasyHolder(LayoutInflater.from(parent.getContext()).inflate(setting.getBottomLoadingLayoutResId(), parent, false));
                case ITEM_TYPE_FOOT_EMPTY:
                    return new EasyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer_empty, parent, false));
                case ITEM_TYPE_FOOT_NET_ERROR_TOP:
                    EasyHolder topErrorHolder=new EasyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer_net_error_top, parent, false));
                    if(onTopReloadListener!=null) topErrorHolder.addOnRecyclerViewClickListener(R.id.item_footer_error_reload,onTopReloadListener);
                    return topErrorHolder;
                case ITEM_TYPE_FOOT_NET_ERROR_BOTTOM:
                    EasyHolder bottomErrorHolder=new EasyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_footer_net_error_bottom, parent, false));
                    if(onBottomReloadListener!=null) bottomErrorHolder.addOnRecyclerViewClickListener(R.id.item_footer_error_reload,onBottomReloadListener);
                    return bottomErrorHolder;
                default:
                    return null;
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull EasyHolder holder, int position) {
        int viewType=holder.getItemViewType();
        switch (viewType){
            case ITEM_TYPE_FOOT_END:
//                ((TextView)holder.getView(R.id.item_footer_end_text)).setText(endHint);
                break;
            case ITEM_TYPE_FOOT_EMPTY:
//                ((TextView)holder.getView(R.id.item_footer_empty_text)).setText(emptyHint);
//                if(emptyImgRes>0) ((ImageView)holder.getView(R.id.item_footer_empty_image)).setImageResource(emptyImgRes);
                break;
            case ITEM_TYPE_LIST:
                onBind(holder,position);
                break;
        }
    }
    public EasyAdapter<E> addOnRecyclerViewClickListener(@IdRes int viewId, OnRecyclerViewClickListener listener){
        clickListeners.put(viewId,listener);
        return this;
    }
    public abstract void onBind(@NonNull EasyHolder holder, int position);

    public Context getContext() {
        return context;
    }

    public List<E> getListData() {
        return listData;
    }

    public void setOnTopReloadListener(OnRecyclerViewClickListener onTopReloadListener) {
        this.onTopReloadListener = onTopReloadListener;
    }

    public void setOnBottomReloadListener(OnRecyclerViewClickListener onBottomReloadListener) {
        this.onBottomReloadListener = onBottomReloadListener;
    }
}
