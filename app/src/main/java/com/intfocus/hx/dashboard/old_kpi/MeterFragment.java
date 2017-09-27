package com.intfocus.hx.dashboard.old_kpi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.intfocus.hx.R;
import com.intfocus.hx.base.BaseSwipeHomeFragment;
import com.intfocus.hx.dashboard.DashboardActivity;
import com.intfocus.hx.dashboard.kpi.bean.MererEntity;
import com.intfocus.hx.dashboard.kpi.bean.MeterClickEventEntity;
import com.intfocus.hx.dashboard.kpi.bean.MeterRequestResult;
import com.intfocus.hx.dashboard.old_kpi.adapter.MeterVPAdapter;
import com.intfocus.hx.dashboard.old_kpi.adapter.SaleDataAdapter;
import com.intfocus.hx.listen.CustPagerTransformer;
import com.intfocus.hx.mode.MeterMode;
import com.intfocus.hx.subject.SubjectActivity;
import com.intfocus.hx.subject.TableActivity;
import com.intfocus.hx.subject.metrics.HomeTricsActivity;
import com.intfocus.hx.util.DisplayUtil;
import com.intfocus.hx.util.FileUtil;
import com.intfocus.hx.util.K;
import com.intfocus.hx.util.ToastUtils;
import com.intfocus.hx.util.URLs;
import com.intfocus.hx.view.WidthHeightLinearLayou;
import com.zbl.lib.baseframe.core.Subject;
import com.zbl.lib.baseframe.utils.StringUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import sumimakito.android.advtextswitcher.AdvTextSwitcher;
import sumimakito.android.advtextswitcher.Switcher;

/**
 * 主页 - 仪表盘
 */
public class MeterFragment extends BaseSwipeHomeFragment {

    Context ctx;

    View rootView;

    JSONObject user;

    ArrayList<String> gropsNameList;

    ArrayList<ArrayList<MererEntity>> entityList;

    @ViewInject(R.id.tv_realtime_title_meter)
    AdvTextSwitcher tv_realtime_title;

    @ViewInject(R.id.anim_loading)
    RelativeLayout mAnimLoading;

//    @ViewInject(R.id.whl_vp_meter)
    WidthHeightLinearLayou mWHllayout;
    @ViewInject(R.id.vp_meter)
    ViewPager vp_meter;
    MeterVPAdapter vpAdapter;
    ArrayList<MererEntity> topDatas = new ArrayList<>();

    HashMap<String, ArrayList<MererEntity>> gropsName = new HashMap();

    @ViewInject(R.id.meter_group)
    LinearLayout ll_meterGroupContainer;

    @Override
    public Subject setSubject() {
        ctx = act.getApplicationContext();
        return new MeterMode(ctx);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.meter_fragment, container, false);
            x.view().inject(this, rootView);
            initView();
            getModel().requestData();
        }
        gropsNameList = new ArrayList<>();
        entityList = new ArrayList<>();
        mAnimLoading = (RelativeLayout) rootView.findViewById(R.id.anim_loading);
        String userConfigPath = String.format("%s/%s", FileUtil.basePath(ctx), K.kUserConfigFileName);
        if ((new File(userConfigPath)).exists()) {
            user = FileUtil.readConfigFile(userConfigPath);
        }
        initSwipeLayout(rootView);
        return rootView;
    }

    /**
     * 初始化View
     */
    private void initView() {
        initAffiche();

        initViewPager();

//        initRecycleView();
    }

    /**
     * 初始化公告控件
     */
    private void initAffiche() {
        tv_realtime_title.setVisibility(View.GONE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String[] texts = getModel().getMessage();
                if (texts != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv_realtime_title.setVisibility(View.VISIBLE);
                            tv_realtime_title.setTexts(texts);
                            tv_realtime_title.setCallback((DashboardActivity) getActivity());
                            new Switcher().attach(tv_realtime_title).setDuration(3000).start();
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 初始化滑动区域模块
     */
    private void initViewPager() {
        vp_meter.setPageTransformer(false, new CustPagerTransformer(act.getApplicationContext()));
        vp_meter.setOffscreenPageLimit(3);
        vpAdapter = new MeterVPAdapter(ctx, getActivity().getSupportFragmentManager(), topDatas);
        vp_meter.setAdapter(vpAdapter);
        vp_meter.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                vp_meter.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    /**
     * 数据请求回调方法
     *
     * @param result
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MeterRequestResult result) {
        if (!result.isSuccess() || result == null) {
            ToastUtils.INSTANCE.show(ctx, "数据请求失败，errorCode:" + result.getStateCode());
            return;
        }
        topDatas.clear();
        if (!result.getTopDatas().isEmpty()) {
            topDatas.addAll(result.getTopDatas());
        } else {
            mWHllayout.setVisibility(View.GONE);
        }
        vpAdapter.notifyDataSetChanged();

        gropsName.clear();
        ll_meterGroupContainer.removeAllViews();

        Iterator<MererEntity> iterator = result.getBodyDatas().iterator();
        while (iterator.hasNext()) {
            MererEntity entity = iterator.next();
            String group_name = entity.getGroup_name();
            if (!StringUtil.isEmpty(group_name)) {
                ArrayList<MererEntity> list = gropsName.get(group_name);
                if (list == null || list.isEmpty()) {
                    list = new ArrayList();
                    gropsNameList.add(group_name);
                    entityList.add(list);
                    gropsName.put(group_name, list);
                }
                list.add(entity);
            }
        }

        Set<String> set = gropsName.keySet();
        for (int i = 0; i < set.size(); i++) {
            ArrayList<MererEntity> list = entityList.get(i);
            LayoutInflater inflater = LayoutInflater.from(ctx);
            View view = inflater.inflate(R.layout.item_meter_group, null);
            TextView tv_title = (TextView) view.findViewById(R.id.tv_item_meter_group);
            tv_title.setText(list.get(0).getGroup_name());
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_meter);
            initRecycleView(recyclerView, list);
            ll_meterGroupContainer.addView(view);
        }

        // 关闭加载动画
        mAnimLoading.setVisibility(View.GONE);
        mSwipeLayout.setRefreshing(false);

        rootView.invalidate();
    }

    /**
     * 初始化销售数据模块
     */
    private void initRecycleView(RecyclerView recyclerView, ArrayList<MererEntity> bodyDatas) {
        int offset = DisplayUtil.dip2px(ctx, 3);
        recyclerView.setPadding(offset, offset, offset, offset);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
//        layoutManager.setAutoMeasureEnabled(false);
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);
        //设置Adapter
        SaleDataAdapter recycleAdapter = new SaleDataAdapter(ctx, getActivity().getSupportFragmentManager(), bodyDatas);
        recyclerView.setAdapter(recycleAdapter);
        //设置分隔线
        recyclerView.addItemDecoration(new MarginDecoration(ctx));
        //设置增加或删除条目的动画
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    /**
     * 图表点击事件统一处理方法
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MeterClickEventEntity entity) {
        if (entity != null) {
            String link = "/" + entity.getEntity().getTarget_url();
            String bannerName = entity.getEntity().getTitle();

            if (link.indexOf("template") > 0 && link.indexOf("group") > 0) {
                try {
                    String templateID = TextUtils.split(link, "/")[6];
                    int groupID = user.getInt(URLs.kGroupId);
                    String reportID = TextUtils.split(link, "/")[8];
                    String urlString = link;
                    Intent intent;

                    switch (templateID) {
                        case "-1":
                        case "2":
                        case "4":
                            intent = new Intent(getActivity(), SubjectActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.putExtra(URLs.kBannerName, bannerName);
                            intent.putExtra(URLs.kLink, link);
                            intent.putExtra(URLs.kObjectId, 1);
                            intent.putExtra(URLs.kObjectType, 1);
                            intent.putExtra("groupID", groupID);
                            intent.putExtra("reportID", reportID);
                            startActivity(intent);
                            break;

                        case "3":
                            intent = new Intent(ctx, HomeTricsActivity.class);
                            urlString = String.format("%s/api/v1/group/%d/template/%s/report/%s/json",
                                    K.kBaseUrl, groupID, templateID, reportID);
                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.putExtra(URLs.kBannerName, bannerName);
                            intent.putExtra(URLs.kObjectId, 1);
                            intent.putExtra(URLs.kObjectType, 1);
                            intent.putExtra("groupID", groupID);
                            intent.putExtra("reportID", reportID);
                            intent.putExtra("urlString", urlString);
                            startActivity(intent);
                            break;

                        case "5":
                            intent = new Intent(ctx, TableActivity.class);
                            urlString = String.format("%s/api/v1/group/%d/template/%s/report/%s/json",
                                    K.kBaseUrl, groupID, templateID, reportID);
                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent.putExtra(URLs.kBannerName, bannerName);
                            intent.putExtra(URLs.kObjectId, 1);
                            intent.putExtra(URLs.kObjectType, 1);
                            intent.putExtra("groupID", groupID);
                            intent.putExtra("reportID", reportID);
                            intent.putExtra("urlString", urlString);
                            startActivity(intent);
                            break;

                        default:
                            showTemplateErrorDialog();
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Intent intent = new Intent(getActivity(), SubjectActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra(URLs.kBannerName, bannerName);
                intent.putExtra(URLs.kLink, link);
                intent.putExtra(URLs.kObjectId, 1);
                intent.putExtra(URLs.kObjectType, 1);
                startActivity(intent);
            }
        } else {
            ToastUtils.INSTANCE.show(ctx, "数据实体为空");
        }
    }

    void showTemplateErrorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("温馨提示")
                .setMessage("当前版本暂不支持该模板, 请升级应用后查看")
                .setPositiveButton("前去升级", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(K.kPgyerUrl));
                        startActivity(browserIntent);
                    }
                })
                .setNegativeButton("稍后升级", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 返回 LoginActivity
                    }
                });
        builder.show();
    }
}
