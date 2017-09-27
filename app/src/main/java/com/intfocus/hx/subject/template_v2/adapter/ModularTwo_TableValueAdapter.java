package com.intfocus.hx.subject.template_v2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TableRow;
import android.widget.TextView;

import com.intfocus.hx.R;
import com.intfocus.hx.subject.template_v2.entity.ModularTwo_UnitTableEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 表格第一列名字列表适配器
 * Created by zbaoliang on 17-5-15.
 */
public class ModularTwo_TableValueAdapter extends BaseAdapter {
    private Context ctx;
    private List<ModularTwo_UnitTableEntity.TableRowEntity> ltdata;
    private ArrayList<Integer> al_HeaderLenght;

    LayoutInflater inflate;

    private int defauteColor;
    private int hasSubColor;

    public ModularTwo_TableValueAdapter(Context ctx, List<ModularTwo_UnitTableEntity.TableRowEntity> ltdata, ArrayList<Integer> al_HeaderLenght) {
        this.ctx = ctx;
        this.al_HeaderLenght = al_HeaderLenght;
        inflate = LayoutInflater.from(ctx);
        defauteColor = ctx.getResources().getColor(R.color.co3);
        hasSubColor = ctx.getResources().getColor(R.color.co14);
        setData(ltdata);
    }

    private void setData(List<ModularTwo_UnitTableEntity.TableRowEntity> ltdata) {
        if (ltdata == null)
            return;
        this.ltdata = ltdata;
    }

    public void updateData(List<ModularTwo_UnitTableEntity.TableRowEntity> ltdata) {
        setData(ltdata);
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
        convertView = inflate.inflate(R.layout.item_tabledatavalue_row, parent, false);
        ModularTwo_UnitTableEntity.TableRowEntity entity = ltdata.get(position);
        String[] main_data = entity.main_data;
        int length = main_data.length;
        for (int i = 1; i < length; i++) {
            View v = inflate.inflate(R.layout.item_table_value, null);
            TextView tv = (TextView) v.findViewById(R.id.tv_tableData_value);
            tv.setText(main_data[i]);
            tv.getLayoutParams().width = al_HeaderLenght.get(i - 1);
            ((TableRow) convertView).addView(v);
        }
        return convertView;
    }
}
