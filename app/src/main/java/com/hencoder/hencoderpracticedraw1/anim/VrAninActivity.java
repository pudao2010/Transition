package com.hencoder.hencoderpracticedraw1.anim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.hencoder.hencoderpracticedraw1.R;

public class VrAninActivity extends AppCompatActivity {

    private Animator mCurrentAnimator;
    private static final int DURATION = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vr_anin);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final View sourceView = findViewById(R.id.fl_source);
        sourceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                zoomUp(sourceView);
//                CustomToast.showToast(VrAninActivity.this, "This is a custom toast!");
            }
        });

    }

    private void zoomUp(final View sourceView) {
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        final View resultView = findViewById(R.id.fl_result);

        final Rect startBounds = new Rect();
        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize(point);
        final Rect finalBounds = new Rect(0, 0, point.x, point.y);
        final Point globalOffset = new Point(0, 0);

        sourceView.getGlobalVisibleRect(startBounds);

        float startScale;
        if ((float) finalBounds.width() / finalBounds.height() > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        sourceView.setAlpha(0f);

        resultView.setVisibility(View.VISIBLE);

        resultView.setPivotX(0f);
        resultView.setPivotY(0f);

        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(resultView, View.X, startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(resultView, View.Y, startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(resultView, View.SCALE_X, startScale, 1f))
                .with(ObjectAnimator.ofFloat(resultView, View.SCALE_Y, startScale, 1f));

        set.setDuration(DURATION);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;


        final float startScaleFinal = startScale;
        resultView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their
                // original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator.ofFloat(resultView, View.X, startBounds.left))
                        .with(ObjectAnimator.ofFloat(resultView, View.Y, startBounds.top))
                        .with(ObjectAnimator.ofFloat(resultView, View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator.ofFloat(resultView, View.SCALE_Y, startScaleFinal));
                set.setDuration(DURATION);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        sourceView.setAlpha(1f);
                        resultView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        sourceView.setAlpha(1f);
                        resultView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;

            }
        });
    }
}
