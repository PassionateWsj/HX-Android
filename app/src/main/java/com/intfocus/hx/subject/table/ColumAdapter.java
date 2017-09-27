package com.intfocus.hx.subject.table;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.intfocus.hx.R;
import com.intfocus.hx.listen.OnItemClickListener;
import com.intfocus.hx.subject.table.bean.Head;
import com.intfocus.hx.subject.table.bean.TableChart;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

/**
 * Created by CANC on 2017/4/19.
 */

public class ColumAdapter extends SwipeMenuAdapter<ColumAdapter.ColumViewHolder> {

    private SwipeMenuRecyclerView mMenuRecyclerView;
    private TableChart tableChart;
    private OnItemClickListener mOnItemClickListener;
    private ColumnListener listener;

    public ColumAdapter(SwipeMenuRecyclerView menuRecyclerView, TableChart tableChart, ColumnListener listener) {
        this.mMenuRecyclerView = menuRecyclerView;
        this.tableChart = tableChart;
        this.listener = listener;
    }

    public void setDatas(TableChart tableChart) {
        this.tableChart = tableChart;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_column, parent, false);
    }

    @Override
    public ColumViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        ColumViewHolder viewHolder = new ColumViewHolder(realContentView);
        viewHolder.mOnItemClickListener = mOnItemClickListener;
        viewHolder.mMenuRecyclerView = mMenuRecyclerView;
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ColumViewHolder holder, final int position) {
        final Head head = tableChart.table.head.get(position);
        holder.tvColumn.setText(head.getValue());
        holder.ivCheck.setImageResource(head.isShow() ? R.drawable.btn_selected : R.drawable.btn_unselected);
        holder.ivMarker.setImageResource(head.isKeyColumn ? R.drawable.btn_pushpin : R.drawable.btn_pushpin2);
        holder.tvKeyColumn.setVisibility(head.isKeyColumn ? View.VISIBLE : View.GONE);
        //关键列无法取消选中状态,非关键列才可取消
        holder.ivCheck.setEnabled(!head.isKeyColumn);

        holder.ivCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.checkClick(position, head.getValue());
            }
        });
        holder.ivMarker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.markerClick(position, head.getValue());
            }
        });
    }

    @Override
    public int getItemCount() {
        return (tableChart == null || tableChart.table == null || tableChart.table.head == null) ? 0 : tableChart.table.head.size();
    }

    static class ColumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnTouchListener {
        ImageView ivCheck;
        TextView tvColumn;
        TextView tvKeyColumn;
        ImageView ivMarker;
        OnItemClickListener mOnItemClickListener;
        SwipeMenuRecyclerView mMenuRecyclerView;

        public ColumViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ivCheck = (ImageView) itemView.findViewById(R.id.iv_check);
            tvColumn = (TextView) itemView.findViewById(R.id.tv_column);
            tvKeyColumn = (TextView) itemView.findViewById(R.id.tv_key_column);
            ivMarker = (ImageView) itemView.findViewById(R.id.iv_marker);
            itemView.findViewById(R.id.iv_check).setOnTouchListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition());
            }
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN: {
                    mMenuRecyclerView.startDrag(this);
                    break;
                }
            }
            return false;
        }
    }

    public interface ColumnListener {
        void checkClick(int position, String info);

        void markerClick(int position, String info);
    }

}
