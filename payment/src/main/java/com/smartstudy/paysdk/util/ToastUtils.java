package com.smartstudy.paysdk.util;

import android.content.Context;
import android.widget.Toast;

import com.smartstudy.paysdk.R;


public class ToastUtils {

    private static Toast mToast;

    /**
     * 非阻塞试显示Toast,防止出现连续点击Toast时的显示问题
     */
    public static void showToast(Context context, CharSequence text, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, duration);
        } else {
            mToast.setText(text);
            mToast.setDuration(duration);
        }
        mToast.show();
    }

    public static void shortToast(Context context, CharSequence text) {
        showToast(context, text, Toast.LENGTH_SHORT);
    }

    public static void longToast(Context context, CharSequence text) {
        showToast(context, text, Toast.LENGTH_LONG);
    }

    public static void showNotNull(Context context, CharSequence text) {
        showToast(context, String.format(context.getString(R.string.not_null), text), Toast.LENGTH_SHORT);
    }
}
