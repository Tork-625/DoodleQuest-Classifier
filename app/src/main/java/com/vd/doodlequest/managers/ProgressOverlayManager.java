package com.vd.doodlequest.managers;

import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

public class ProgressOverlayManager {

    private final View overlay;
    private final ImageView[] dots;
    private Handler handler;
    private Runnable runnable;

    public ProgressOverlayManager(View overlay, ImageView... dots) {
        this.overlay = overlay;
        this.dots = dots;
    }

    public void show() {
        overlay.setVisibility(View.VISIBLE);

        handler = new Handler();
        runnable = new Runnable() {
            int index = 0;

            @Override
            public void run() {
                for (ImageView dot : dots) dot.setVisibility(View.INVISIBLE);
                dots[index].setVisibility(View.VISIBLE);

                index = (index + 1) % dots.length;
                handler.postDelayed(this, 300);
            }
        };

        handler.post(runnable);
    }

    public void hide() {
        overlay.setVisibility(View.GONE);

        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }

        for (ImageView dot : dots) {
            dot.setVisibility(View.INVISIBLE);
        }
    }
}
