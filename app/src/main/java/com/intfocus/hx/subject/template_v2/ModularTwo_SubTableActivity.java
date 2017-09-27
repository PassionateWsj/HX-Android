package com.intfocus.hx.subject.template_v2;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.intfocus.hx.R;
import com.intfocus.hx.subject.template_v2.entity.DataHolder;
import com.zbl.lib.baseframe.utils.StringUtil;

import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * 子Table页面
 */
public class ModularTwo_SubTableActivity extends AppCompatActivity {

    @ViewInject(R.id.tv_subtable_name)
    private TextView title;

    @ViewInject(R.id.fl_subtable_container)
    private FrameLayout fl_container;
    private String subData;

    @ViewInject(R.id.imgBtn_ColsPopupWindow_bannerInfo)
    private ImageButton imgBtn_Cols;
    public int suRootID;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modular_two_subtable);
        getSupportActionBar().hide();
        x.view().inject(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Slide().setDuration(500));
            getWindow().setExitTransition(new Slide().setDuration(500));
        }

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String titel = bundle.getString("Title");
//            subData = bundle.getString("Data");
            subData = DataHolder.getInstance().getData();
            suRootID = bundle.getInt("suRootID");
            title.setText(titel);
            if (StringUtil.isEmpty(subData))
                finish();

            ModularTwo_UnitTablesContModeFragment toFragment = ModularTwo_UnitTablesContModeFragment.newInstance(suRootID,subData);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.replace(R.id.fl_subtable_container, toFragment);
            ft.commit();
        }
    }

    @Event(R.id.imgBtn_ColsPopupWindow_bannerInfo)
    private void onViewClick(View view) {
        ActivityCompat.finishAfterTransition(this);
    }
}
