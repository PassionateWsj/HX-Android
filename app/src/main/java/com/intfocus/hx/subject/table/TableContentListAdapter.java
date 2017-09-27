package com.intfocus.hx.subject.table;

import android.content.Context;
import android.support.v7.widget.RecyclerView;


import com.intfocus.hx.R;
import com.intfocus.hx.subject.table.bean.Head;
import com.intfocus.hx.subject.table.bean.MainData;

import java.util.List;

/**
 * Created by CANC on 2017/4/19.
 */

public class TableContentListAdapter extends ContentCommonAdapter<List<MainData>> {
    private int rowHeight;//1行,2行,3行
    private List<Head> heads;
    private TableContentItemAdapter.ContentItemListener listener;

    public TableContentListAdapter(Context context, List<Head> heads, List<List<MainData>> datas, int rowHeight,
                                   TableContentItemAdapter.ContentItemListener listener) {
        super(context, datas);
        this.layoutId = R.layout.item_table_main;
        this.rowHeight = rowHeight;
        this.listener = listener;
        this.heads = heads;
    }

    public void updateDatas(int rowHeight) {
        this.rowHeight = rowHeight;
        notifyDataSetChanged();
    }

    @Override
    public void convert(boolean isNew, RecyclerView recyclerView, List<MainData> mainData) {
        if (isNew) {
            TableContentItemAdapter adapter = new TableContentItemAdapter(context, heads, mainData, rowHeight, listener);
            recyclerView.setAdapter(adapter);
        }
        else {
            TableContentItemAdapter adapter = (TableContentItemAdapter) recyclerView.getAdapter();
            adapter.setData(heads, mainData, rowHeight);
        }

    }
}
