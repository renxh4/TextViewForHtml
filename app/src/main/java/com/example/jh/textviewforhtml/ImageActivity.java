package com.example.jh.textviewforhtml;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ImageActivity extends AppCompatActivity implements View.OnClickListener {
    private CollapsingToolbarLayoutState state;

    private enum CollapsingToolbarLayoutState {
        EXPANDED,
        COLLAPSED,
        INTERNEDIATE
    }
    private String Html5="Once I was part of many posts, of which my popularity shone. \\n\\nMany a thread friend I had, alas they were gone. \\n\\nAlone I felt, not realizing the error of my ways. \\n\\nFor I was on the staging server, and all the posts were a forum away.<br /><span class=\"lia-inline-image-display-wrapper lia-image-align-inline\"><img src=\"http://precerpapi.lenovo.com/cerp/proxyimage?path=http://lnv.stage.lithium.com/lnv/attachments/lnv/ll07_en/1/1/Family_guy_bird_is_the_word_by_tehinvisible-d48dzo9.jpg\" /></span>";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
        boolean scro = intent.getBooleanExtra("scro", false);
        ImageView imageView = (ImageView) findViewById(R.id.backdrop);
        imageView.setOnClickListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        AppBarLayout  app_bar=(AppBarLayout)findViewById(R.id.appbar);
        app_bar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (verticalOffset == 0) {
                    if (state != CollapsingToolbarLayoutState.EXPANDED) {
                        state = CollapsingToolbarLayoutState.EXPANDED;//修改状态标记为展开
                        collapsingToolbarLayout.setTitle("EXPANDED");//设置title为EXPANDED
                    }
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (state != CollapsingToolbarLayoutState.COLLAPSED) {
                        collapsingToolbarLayout.setTitle("hello");//设置title不显示
                       // playButton.setVisibility(View.VISIBLE);//隐藏播放按钮
                        state = CollapsingToolbarLayoutState.COLLAPSED;//修改状态标记为折叠
                    }
                } else {
                    if (state != CollapsingToolbarLayoutState.INTERNEDIATE) {
                        if(state == CollapsingToolbarLayoutState.COLLAPSED){
                           // playButton.setVisibility(View.GONE);//由折叠变为中间状态时隐藏播放按钮
                        }
                        collapsingToolbarLayout.setTitle("INTERNEDIATE");//设置title为INTERNEDIATE
                        state = CollapsingToolbarLayoutState.INTERNEDIATE;//修改状态标记为中间
                    }
                }
            }
        });
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        MainAdapter hha = new MainAdapter(this, Html5);
        recyclerView.setAdapter(hha);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backdrop:
                Toast.makeText(this,"点击了",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
