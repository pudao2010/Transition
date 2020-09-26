package com.hencoder.hencoderpracticedraw1.anim;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.transition.AutoTransition;
import android.support.transition.ChangeBounds;
import android.support.transition.TransitionManager;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeClipBounds;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.hencoder.hencoderpracticedraw1.R;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class TransitionActivity extends AppCompatActivity implements View.OnClickListener {

    private FrameLayout mFlContent;
    private ImageView mIvImage;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);

        mFlContent = (FrameLayout) findViewById(R.id.fl_content);
        mIvImage = (ImageView) findViewById(R.id.iv_image);

        findViewById(R.id.btn_autotransition).setOnClickListener(this);
        findViewById(R.id.btn_changeBounds).setOnClickListener(this);
        findViewById(R.id.btn_changeClipBounds).setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void transition(ViewGroup target) {
        TransitionManager.beginDelayedTransition(target, new AutoTransition());
        if (pos % 2 == 0) {
            target.setPadding(100, 100, 100, 100);
        } else {
            target.setPadding(0, 0, 0, 0);
        }
        pos++;

    }

    private void changeClipBounds(ViewGroup target) {
        Rect rect = new Rect(50, 150, 200, 350);
        ChangeClipBounds changeClipBounds = new ChangeClipBounds();
        android.transition.TransitionManager.beginDelayedTransition(target, changeClipBounds);
        if (pos % 2 == 0) {
            ViewCompat.setClipBounds(mIvImage, rect);
        } else {
            ViewCompat.setClipBounds(mIvImage, null);
        }
        pos++;
    }

    private void changbounds(ViewGroup target) {
        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setDuration(2000);
        TransitionManager.beginDelayedTransition(target, changeBounds);
        target.setPadding(100, 100, 100, 100);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_autotransition:
                transition(mFlContent);
                break;
            case R.id.btn_changeBounds:
                changbounds(mFlContent);
                break;
            case R.id.btn_changeClipBounds:
                changeClipBounds(mFlContent);
                break;
        }
    }
}
