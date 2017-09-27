package com.intfocus.hx.subject.table;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.intfocus.hx.R;
import com.intfocus.hx.subject.table.bean.TableBarChart;
import com.intfocus.hx.util.Utils;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by CANC on 2017/4/6.
 */

public class TableBarChartAdapter extends CommonAdapter<TableBarChart> {
    private int screenWidth;
    private double maxValue;
    private TableBarChartLisenter lisenter;
    private int rowHeight;

    public TableBarChartAdapter(Context context, List<TableBarChart> datas, double maxValue, int rowHeight, TableBarChartLisenter lisenter) {
        super(context, datas);
        this.layoutId = R.layout.item_table_bar_chart;
        this.maxValue = maxValue;
        this.screenWidth = Utils.getScreenWidth(context) - Utils.dpToPx(context, 120) - 20;
        this.rowHeight = rowHeight;
        this.lisenter = lisenter;
    }

    public void updateRowHeight(int rowHeight) {
        this.rowHeight = rowHeight;
        this.notifyDataSetChanged();
    }

    @Override
    public void convert(ViewHolder holder, TableBarChart tableBarChart) {
        LinearLayout llLayout = holder.getView(R.id.ll_layout);
        TextView tvNumber = holder.getView(R.id.tv_number);
        View viewBar = holder.getView(R.id.view_bar);
        String color = tableBarChart.getColor();
        llLayout.getLayoutParams().height = Utils.dpToPx(context, 25 * rowHeight);
        tvNumber.setText(tableBarChart.getData() + "");
        tvNumber.setTextColor(Color.parseColor(color));
        String mainDataStr = tableBarChart.getData();
        if (mainDataStr.contains(",")) {
            mainDataStr = mainDataStr.replace(",", "");
        }
        if (mainDataStr.contains("%")) {
            mainDataStr = mainDataStr.replace("%", "");
        }
        viewBar.setVisibility(View.VISIBLE);
        BigDecimal bigDecimal = new BigDecimal(mainDataStr);
        double mainData = bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        viewBar.setBackgroundColor(Color.parseColor(color));

        double a = 0;
        if (maxValue > 0 && mainData >0) {
            a = mainData/maxValue;
        }
        else if (maxValue <=0 && mainData <=0){
            a =  maxValue/mainData;
        }
        else if (maxValue >0 && mainData <=0){
            a = 0;
        }
        LayoutParams layoutParams = (LayoutParams) viewBar.getLayoutParams();
        layoutParams.width = (int) (screenWidth * 3 / 4 * a);
        viewBar.setLayoutParams(layoutParams);
        llLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lisenter.barChartClick();
            }
        });
    }

    public interface TableBarChartLisenter {
        void barChartClick();
    }
}
