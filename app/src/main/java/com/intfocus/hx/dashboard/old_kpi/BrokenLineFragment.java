package com.intfocus.hx.dashboard.old_kpi;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.intfocus.hx.R;
import com.intfocus.hx.base.BaseModeFragment;
import com.intfocus.hx.constant.Colors;
import com.intfocus.hx.dashboard.kpi.bean.MererEntity;
import com.intfocus.hx.dashboard.kpi.bean.MeterClickEventEntity;
import com.intfocus.hx.view.MeterCursor;
import com.jonas.jgraph.graph.NChart;
import com.jonas.jgraph.models.NExcel;

import org.greenrobot.eventbus.EventBus;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 折线图
 */
public class BrokenLineFragment extends BaseModeFragment {
    private MererEntity entity;

    private View rootView;

    private int[] colors = Colors.INSTANCE.getColorsRGY();

    @ViewInject(R.id.ll_ragment_broken)
    private View ll_ragmentlayout;

    @ViewInject(R.id.tv_title_vpitem)
    private TextView tv_title;
    @ViewInject(R.id.tv_number_vpitem)
    private TextView tv_number;
    @ViewInject(R.id.tv_compare_vpitem)
    private TextView tv_compare;
    @ViewInject(R.id.tv_vpitem_unit)
    private TextView tv_unit;

    @ViewInject(R.id.img_vpitem)
    private MeterCursor img_cursor;

    @ViewInject(R.id.nchart_linefragment)
    private NChart mChart;

    DecimalFormat df = new DecimalFormat("#.00");

    public BrokenLineFragment() {
    }

    public static BrokenLineFragment newInstance(MererEntity entity) {
        BrokenLineFragment fragment = new BrokenLineFragment();
        Bundle args = new Bundle();
        args.putSerializable("Entity", entity);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            entity = (MererEntity) getArguments().getSerializable("Entity");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_brokenline, container, false);
            x.view().inject(this, rootView);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    init();
                }
            }, 100);
        }
        return rootView;
    }

    private void init() {
        if (entity.is_stick()) {
            mChart.setInterceptAnimate(true);
//            rootView.post(new Runnable() {
//                @Override
//                public void run() {
//                    int vpWidth = rootView.getWidth();
//                    int height = vpWidth*2;
//                    ViewGroup.LayoutParams lp = ll_ragmentlayout.getLayoutParams();
//                    lp.height = height;
//                    ll_ragmentlayout.setLayoutParams(lp);
//                }
//            });
        } else {

        }


        tv_title.setText(entity.getTitle());
        double number = entity.getData().getHigh_light().getNumber();
        if (number==0)
            tv_number.setText("0");
        else
            tv_number.setText(formatNumber(String.valueOf(df.format(number))));
        tv_unit.setText(entity.getUnit());

        MererEntity.LineEntity.HighLight high_light = entity.getData().getHigh_light();
        if (high_light.getCompare() != 0) {//显示百分比
            float compare = (float) ((high_light.getNumber() - high_light.getCompare()) / high_light.getCompare() * 100);
            if (high_light.getNumber()-high_light.getCompare()>0) {//上箭头
                tv_compare.setText("+" + df.format(compare) + "%");
            } else {
                tv_compare.setText("" + df.format(compare) + "%");
            }
        }

        if (high_light.getArrow() >= 0) {
            img_cursor.setVisibility(View.VISIBLE);
            tv_compare.setTextColor(colors[high_light.getArrow()]);
            tv_number.setTextColor(colors[high_light.getArrow()]);
            img_cursor.setCursorState(high_light.getArrow());
        }
        else
            img_cursor.setVisibility(View.GONE);


        int[] chart_data = entity.getData().getChart_data();
        int size = chart_data.length;

        if (size > 0) {
            List<NExcel> nExcelList = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                nExcelList.add(new NExcel(chart_data[i], ""));
            }
            mChart.setScrollAble(false);
            mChart.setFixedWidth(7);
            mChart.setActivationColor(colors[high_light.getArrow()]);
            mChart.setNormalColor(Color.parseColor("#4EB5D7"));
            mChart.setTextPrintColor(Color.parseColor("#4EB5D7"));
            mChart.setDrawTextUnit(entity.getUnit());
            mChart.setBarStanded(7);
            mChart.setBarAniStyle(NChart.ChartAniStyle.LINE_RIPPLE);//TODO 动画效果
            mChart.setChartStyle(NChart.ChartStyle.CLOSELINE);//TODO 曲线图
            mChart.setLinePointRadio(16);//设置折线转则点圆点半径
            mChart.setHCoordinate(10);
            mChart.setAbove(10);
            mChart.setInterceptTouchEvent(true);
            mChart.cmdFill(nExcelList);
        }

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MeterClickEventEntity(entity));
            }
        });
    }
}
