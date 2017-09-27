package com.intfocus.hx.subject.table;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.intfocus.hx.R;

import java.util.List;

/**
 * Created by liuruilin on 2017/5/15.
 */

public abstract class ContentCommonAdapter <T> extends BaseAdapter {
    protected Context context;
    protected List<T> datas;
    protected LayoutInflater inflater;
    protected int layoutId;

    public ContentCommonAdapter(Context context, List<T> datas) {
        this.context = context;
        this.datas = datas;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public T getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(layoutId, parent, false);
            holder = new ViewHolder();
            holder.mRecyclerView = (RecyclerView) convertView.findViewById(R.id.recycler_view);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
            mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            holder.mRecyclerView.setLayoutManager(mLayoutManager);
            convertView.setTag(holder);
            convert(true, holder.mRecyclerView, getItem(position));
        }
        else {
            holder = (ViewHolder) convertView.getTag();
            convert(false, holder.mRecyclerView, getItem(position));
        }

        return convertView;
    }

    public void setData(List<T> datas){
        this.datas=datas;
        this.notifyDataSetChanged();
    }

    public abstract void convert(boolean isNew, RecyclerView recyclerView, T t);

    //清理数据
    public void onDestroy(){

    }

    static class ViewHolder {
        RecyclerView mRecyclerView;
    }
}
