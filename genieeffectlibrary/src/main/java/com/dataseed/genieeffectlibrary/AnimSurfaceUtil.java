package com.dataseed.genieeffectlibrary;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mikezhu on 7/27/17.
 */

public class AnimSurfaceUtil {
    public static void startAnimation(Activity context, final AnimSurface animSurface, final View animView, final boolean reverse) {
        if (!animSurface.isShown()) {
            ((ViewGroup) context.getWindow().getDecorView().findViewById(android.R.id.content)).addView(animSurface);
        }

        final int[] endPosition = new int[2];
        animView.getLocationOnScreen(endPosition);
        animSurface.post(new Runnable() {
            @Override
            public void run() {
                animSurface.setIsDebug(true);
                if (animSurface.startAnimation(animView, endPosition[0] + animView.getWidth() + 50, endPosition[1] + animView.getHeight() / 2, reverse)) {
                }
            }
        });
    }
}
