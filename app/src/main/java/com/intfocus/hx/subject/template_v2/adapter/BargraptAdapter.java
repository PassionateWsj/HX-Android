package com.intfocus.hx.subject.template_v2.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.intfocus.hx.R;
import com.intfocus.hx.subject.template_v2.entity.BargraphComparator;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.DecimalFormat;
import java.util.LinkedList;

/**
 * Created by zbaoliang on 17-5-15.
 */
public class BargraptAdapter extends BaseAdapter {
    private Context ctx;
    private LinkedList<BargraphComparator> ltdata;
    private DecimalFormat dataFormat;
    private Drawable herearrow;
    private int selectItemIndex = 0;
    private int defauteColor;
    private int selectColor;

    public BargraptAdapter(Context ctx, LinkedList<BargraphComparator> ltdata) {
        this.ctx = ctx;
        setData(ltdata);
        herearrow = ctx.getResources().getDrawable(R.drawable.icon_herearrow);
        herearrow.setBounds(0, 0, herearrow.getMinimumWidth(),
                herearrow.getMinimumHeight());
        defauteColor = ctx.getResources().getColor(R.color.co3);
        selectColor = ctx.getResources().getColor(R.color.co14);
    }

    private void setData(LinkedList<BargraphComparator> ltdata) {
        if (ltdata == null)
            return;
        this.ltdata = ltdata;
        dataFormat = new DecimalFormat(".##%");
    }

    public void updateData(LinkedList<BargraphComparator> ltdata) {
        setData(ltdata);
        selectItemIndex = 0;
        notifyDataSetChanged();
    }

    public void setSelectItem(int index) {
        selectItemIndex = index;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (ltdata == null)
            return 0;
        return ltdata.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.item_mdrpunit_bargraph, parent, false);
            viewHolder = new ViewHolder();
            x.view().inject(viewHolder, convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (selectItemIndex == position) {
            viewHolder.tv_name.setTextColor(selectColor);
            viewHolder.tv_percentage.setTextColor(selectColor);
            viewHolder.img_cursor.setImageResource(R.drawable.icon_herearrow);
        } else {
            viewHolder.tv_name.setTextColor(defauteColor);
            viewHolder.tv_percentage.setTextColor(defauteColor);
            viewHolder.img_cursor.setImageResource(0);
        }

        viewHolder.tv_name.setText(ltdata.get(position).name);
        float percentage = ltdata.get(position).data;
        viewHolder.tv_percentage.setText(dataFormat.format(percentage));
        return convertView;
    }

    static class ViewHolder {
        @ViewInject(R.id.tv_name)
        TextView tv_name;
        @ViewInject(R.id.tv_percentage)
        TextView tv_percentage;
        @ViewInject(R.id.img_cursor)
        ImageView img_cursor;
    }
}
