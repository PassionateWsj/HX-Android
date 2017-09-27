package com.intfocus.hx.subject.table;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by CANC on 2017/4/19.
 */

public abstract class CommonAdapter <T> extends BaseAdapter {
    protected Context context;
    protected List<T> datas;
    protected LayoutInflater inflater;
    protected int layoutId;

    public CommonAdapter(Context context, List<T> datas) {
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

            holder = ViewHolder.get(context, convertView, parent, layoutId, position);

            convert(holder, getItem(position));

            convertView = holder.getConvertView();

        return convertView;
    }

    public void setData(List<T> datas){
        this.datas=datas;
        this.notifyDataSetChanged();
    }

    public abstract void convert(ViewHolder holder, T t);

    //清理数据
    public void onDestroy(){

    }

}
