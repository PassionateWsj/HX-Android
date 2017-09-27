package com.intfocus.hx.subject.template_v2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.intfocus.hx.R;
import com.intfocus.hx.subject.template_v2.entity.ModularTwo_UnitTableEntity;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * 表格第一列名字列表适配器
 * Created by zbaoliang on 17-5-15.
 */
public class ModularTwo_TableNameAdapter extends BaseAdapter {
    private Context ctx;
    private List<ModularTwo_UnitTableEntity.TableRowEntity> ltdata;

    private int defauteColor;
    private int hasSubColor;

    public ModularTwo_TableNameAdapter(Context ctx, List<ModularTwo_UnitTableEntity.TableRowEntity> ltdata) {
        this.ctx = ctx;
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
        String tableName = ltdata.get(position).main_data[0];
        return tableName;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.item_table_name, parent, false);
            viewHolder = new ViewHolder();
            x.view().inject(viewHolder, convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        ModularTwo_UnitTableEntity.TableRowEntity entity = ltdata.get(position);

        if (entity.sub_data == null) {
            viewHolder.img_dot.setVisibility(View.INVISIBLE);
            viewHolder.tv_name.setTextColor(defauteColor);
        } else {
        viewHolder.tv_name.setTextColor(hasSubColor);
        viewHolder.img_dot.setVisibility(View.VISIBLE);
    }
        viewHolder.tv_name.setText(entity.main_data[0]);
        return convertView;
    }

    static class ViewHolder {
        @ViewInject(R.id.tv_tableName_value)
        TextView tv_name;
        @ViewInject(R.id.img_tableName_dot)
        ImageView img_dot;
    }
}
