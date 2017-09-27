package com.intfocus.hx.subject.table;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.intfocus.hx.R;
import com.intfocus.hx.subject.table.bean.FilterItem;

import java.util.List;

/**
 * Created by CANC on 2017/4/6.
 */

public class TableFilterItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<FilterItem> datas;
    private final LayoutInflater inflater;
    private FilterItemLisenter lisenter;
    private boolean allChecked;


    public TableFilterItemAdapter(Context context, List<FilterItem> datas, FilterItemLisenter lisenter, boolean allChecked) {
        this.context = context;
        this.datas = datas;
        this.inflater = LayoutInflater.from(context);
        this.lisenter = lisenter;
        this.allChecked = allChecked;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View contentView = inflater.inflate(R.layout.item_table_filter_item, parent, false);
        return new TableFilterHolder(contentView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        TableFilterHolder viewHolder = (TableFilterHolder) holder;
        final FilterItem filterItem = datas.get(position);
        viewHolder.tvFilterName.setText(filterItem.getValue());
        viewHolder.rlFilterItem.setSelected(filterItem.isSelected);
        viewHolder.rlCheckAll.setSelected(allChecked);
        if (position == 0) {
            viewHolder.rlCheckAll.setVisibility(View.VISIBLE);
        } else {
            viewHolder.rlCheckAll.setVisibility(View.GONE);
        }
        viewHolder.rlFilterItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterItem.isSelected = !filterItem.isSelected;
                lisenter.updateFilter();
            }
        });
        viewHolder.rlCheckAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (FilterItem filterItem1 : datas) {
                    filterItem1.isSelected = !allChecked;
                }
                lisenter.updateFilter();
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas == null ? 0 : datas.size();
    }

    public class TableFilterHolder extends RecyclerView.ViewHolder {
        RelativeLayout rlFilterItem;
        RelativeLayout rlCheckAll;
        TextView tvFilterName;

        public TableFilterHolder(View itemView) {
            super(itemView);
            rlFilterItem = (RelativeLayout) itemView.findViewById(R.id.rl_filter_item);
            rlCheckAll = (RelativeLayout) itemView.findViewById(R.id.rl_check_all);
            tvFilterName = (TextView) itemView.findViewById(R.id.tv_filter_name);
        }
    }

    public interface FilterItemLisenter {
        void updateFilter();
    }
}
