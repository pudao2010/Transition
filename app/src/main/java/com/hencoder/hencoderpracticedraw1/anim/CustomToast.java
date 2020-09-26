package com.hencoder.hencoderpracticedraw1.anim;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hencoder.hencoderpracticedraw1.BuildConfig;
import com.hencoder.hencoderpracticedraw1.R;

import java.lang.ref.WeakReference;

/**
 * Created by pucheng on 2020-09-26.
 */
public final class CustomToast {
    private static final String NEW_LINE = System.getProperty("line.separator");
    private static final String TAG = "CustomToast";
    private static WeakReference<Toast> sToastRef;
    private static final int LIMIT_TIME_INTERVAL = 2000;
    private static long lastShowTimeStamp;
    private static String lastContent;
    private static final Handler MHANDLER = new Handler(Looper.myLooper());

    private CustomToast() throws Exception {
        throw new Exception();
    }

    public static void showToast(Context context, @StringRes int resId) {
        if (context == null) {
            return;
        }
        String msg = context.getString(resId);
        showToast(context, msg, false);
    }

    public static void showToast(Context context, @StringRes int resId, boolean lengthLong) {
        if (context == null) {
            return;
        }
        context = context.getApplicationContext();
        String msg = context.getString(resId);
        showToast(context, msg, lengthLong);
    }

    public static void showToast(Context context, String msg) {
        if (context == null) {
            return;
        }
        context = context.getApplicationContext();
        showToast(context, msg, false);
    }

    public static void showToast(Context context, final String msg, final boolean lengthLong) {
        if (context == null) {
            return;
        }
        printToastInfo(context, msg);
        toastDefault(context, msg, lengthLong);
    }

    public static void showToast(Context context, String msg, Drawable leftIcon) {
        if (context == null) {
            return;
        }
        printToastInfo(context, msg);
        showToastInternal(context, msg, ToastLevel.DEFAULT, false, leftIcon);
    }

    public static void toastDefault(Context context, String strMsg, boolean toastLong) {
        showToastInternal(context, strMsg, ToastLevel.DEFAULT, toastLong);
    }

    public static void toastDefault(Context context, @StringRes int strResId, boolean toastLong) {
        toastDefault(context, context.getString(strResId), toastLong);
    }

    public static void toastLoading(Context context, @StringRes int strResId, boolean toastLong) {
        toastLoading(context, context.getString(strResId), toastLong);
    }

    public static void toastLoading(Context context, String strMsg, boolean toastLong) {
        showToastInternal(context, strMsg, ToastLevel.LOADING, toastLong);
    }

    public static void toastSuccess(Context context, @StringRes int strResId, boolean toastLong) {
        toastSuccess(context, context.getString(strResId), toastLong);
    }

    public static void toastSuccess(Context context, String strMsg, boolean toastLong) {
        showToastInternal(context, strMsg, ToastLevel.SUCCESS, toastLong);
    }

    public static void toastSuccess(Context context, String strMsg) {
        showToastInternal(context, strMsg, ToastLevel.SUCCESS, false);
    }

    public static void toastSuccess(Context context, @StringRes int strResId) {
        showToastInternal(context, context.getString(strResId), ToastLevel.SUCCESS, false);
    }

    public static void toastException(Context context, @StringRes int strResId, boolean toastLong) {
        toastException(context, context.getString(strResId), toastLong);
    }

    public static void toastException(Context context, String strMsg, boolean toastLong) {
        showToastInternal(context, strMsg, ToastLevel.EXCEPTION, toastLong);
    }

    public static void toastException(Context context, @StringRes int strResId) {
        showToastInternal(context, context.getString(strResId), ToastLevel.EXCEPTION, false);
    }

    public static void toastException(Context context, String strMsg) {
        showToastInternal(context, strMsg, ToastLevel.EXCEPTION, false);
    }

    private static void showToastInternal(final Context context,
                                          final String strMsg, @ToastLevel final int toastLevel,
                                          final boolean toastLong) {
        showToastInternal(context, strMsg, toastLevel, toastLong, null);
    }

    @SuppressLint("MissingPermission")
    private static void showToastInternal(final Context context,
                                          String msg, @ToastLevel final int toastLevel,
                                          final boolean toastLong, final Drawable leftIcon) {
        if (context == null || TextUtils.isEmpty(msg)) {
            Log.w(TAG, logWarnMsg(toastLevel, context, msg));
            return;
        }
        final String strMsg = msg;
        printToastInfo(context, strMsg);
        final long time = SystemClock.elapsedRealtime();
        if (!TextUtils.isEmpty(lastContent) && lastContent.equals(strMsg)) {
            if (time - lastShowTimeStamp < LIMIT_TIME_INTERVAL) {
                //重复弹出
                Log.e(TAG, "this is repeatedly toast show, ignore it");
                return;
            }
        }
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                cancelToast();
                Toast toast = new Toast(context);
                View toastView = LayoutInflater.from(context).inflate(R.layout.view_toast, null);
                toast.setView(toastView);
                //设置toast的位置
                toast.setGravity(Gravity.CENTER, 0, -320);
                toast.setDuration(toastLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
                //TODO 没有 toast.getWindowParams();方法，暂时new出WindowManager.LayoutParams();未发现问题
                WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();
                windowParams.windowAnimations = R.style.CustomToast;
                //WindowManager.LayoutParams windowParams = toast.getWindowParams();
                windowParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                if (Build.VERSION.SDK_INT >= 26) {
//                    windowParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
                    windowParams.type = WindowManager.LayoutParams.TYPE_TOAST;
                } else {
                    windowParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
                }
                TextView tvMsg = (TextView) toastView.findViewById(R.id.tv_msg);
                tvMsg.setText(strMsg);

                ImageView ivIcon = (ImageView) toastView.findViewById(R.id.iv_icon_level);
                ivIcon.clearAnimation();
                if (leftIcon != null) {
                    ivIcon.setImageDrawable(leftIcon);
                } else {
                    int iconVis = View.VISIBLE;
                    switch (toastLevel) {
                        case ToastLevel.DEFAULT:
                            iconVis = View.GONE;
                            break;
                        case ToastLevel.LOADING:
                            Animation anim = AnimationUtils.loadAnimation(context, R.anim.rotate);
                            anim.setRepeatCount(Animation.INFINITE);
                            anim.setRepeatMode(Animation.RESTART);
                            ivIcon.startAnimation(anim);
                            break;
                        case ToastLevel.EXCEPTION:
                        case ToastLevel.SUCCESS:
                        default:
                            break;
                    }
                    ivIcon.setVisibility(iconVis);
                    ivIcon.setImageLevel(toastLevel);
                }
                sToastRef = new WeakReference<>(toast);
                toast.show();
                lastShowTimeStamp = time;
                lastContent = strMsg;
            }
        };
        MHANDLER.post(runnable);
    }

    public static void cancelToast() {
        WeakReference<Toast> ref = sToastRef;
        if (ref == null)
            return;
        Toast toast = ref.get();
        if (toast == null)
            return;
        toast.cancel();
        ref.clear();
    }

    private static void printToastInfo(Context context, String msg) {
        if (BuildConfig.DEBUG) {
            StackTraceElement[] stackTrace = new Throwable().getStackTrace();
            String stack = "";
            if (stackTrace != null) {
                for (StackTraceElement element : stackTrace) {
                    if (element == null) {
                        continue;
                    }
                    String content = element.toString();
                    if (content.contains(context.getPackageName())) {
                        stack = "at " + content;
                    }
                }
            }
            Log.d(TAG, "SHOW TOAST: "
                    + NEW_LINE + "location: [" + stack + "]"
                    + NEW_LINE + "package:  [" + context.getPackageName() + "]"
                    + NEW_LINE + "content:  [" + msg + "]"
            );
        }
    }

    private static String logWarnMsg(int toastLevel, Context context, String msg) {
        return "Toast level = " + toastLevel + " [ context = " + context + " , msg = " + msg + " ]";
    }


}
