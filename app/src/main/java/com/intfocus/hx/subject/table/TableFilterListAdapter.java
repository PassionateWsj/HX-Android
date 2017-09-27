package com.intfocus.hx.subject.table;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.TextView;

import com.intfocus.hx.R;
import com.intfocus.hx.subject.table.bean.Filter;

import java.util.List;


/**
 * Created by CANC on 2017/4/24.
 */

public class TableFilterListAdapter extends CommonAdapter<Filter> {
    private TableFilterItemAdapter.FilterItemLisenter lisenter;

    public TableFilterListAdapter(Context context, List<Filter> datas, TableFilterItemAdapter.FilterItemLisenter lisenter) {
        super(context, datas);
        this.layoutId = R.layout.item_table_filter;
        this.lisenter = lisenter;
    }

    public void updateDatas(List<Filter> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public void convert(ViewHolder holder, final Filter filter) {
        TextView tvFilterName = holder.getView(R.id.tv_filter_name);
        SwitchCompat switchCompat = holder.getView(R.id.swipe_content);
        tvFilterName.setText(filter.name);
        switchCompat.setChecked(filter.isSelected);
        final RecyclerView recyclerView = holder.getView(R.id.recycler_view);
        TableFilterItemAdapter adapter = new TableFilterItemAdapter(context, filter.items, lisenter, filter.isAllcheck);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(filter.isSelected ? View.VISIBLE : View.GONE);
        switchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter.isSelected = !filter.isSelected;
                recyclerView.setVisibility(filter.isSelected ? View.VISIBLE : View.GONE);
            }
        });
    }
}
