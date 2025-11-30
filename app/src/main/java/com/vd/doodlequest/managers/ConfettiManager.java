package com.vd.doodlequest.managers;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.vd.doodlequest.ui.views.ConfettiView;
import com.vd.doodlequest.data.DoodleProgressRepository;

public class ConfettiManager {

    public interface Callback {
        void onResetProgressAndShowNextHint();
    }

    private final Context context;
    private final Callback callback;

    private FrameLayout confettiContainer;
    private ConfettiView confettiView;
    private ImageView banner;

    private DoodleProgressRepository progressManager;

    public ConfettiManager(Context context,
                           DoodleProgressRepository progressManager,
                           Callback callback) {

        this.context = context;
        this.progressManager = progressManager;
        this.callback = callback;
    }

    public void initViews(FrameLayout container, ImageView bannerView) {
        this.confettiContainer = container;
        this.banner = bannerView;
    }

    public void start() {
        if (confettiContainer == null || banner == null) return;

        confettiContainer.setAlpha(0f);
        banner.setAlpha(0f);

        confettiContainer.setVisibility(View.VISIBLE);
        banner.setVisibility(View.VISIBLE);

        confettiContainer.post(() -> {
            if (confettiView == null) {
                confettiView = new ConfettiView(context, 100);
                confettiContainer.addView(confettiView);
            }
        });

        confettiContainer.animate().alpha(1f).setDuration(500).start();
        banner.animate().alpha(1f).setDuration(500).start();

        View.OnClickListener stop = v -> stopConfetti();
        confettiContainer.setOnClickListener(stop);
        banner.setOnClickListener(stop);
    }

    public void stopConfetti () {
        if (confettiView != null) confettiView.stop();

        confettiContainer.animate().alpha(0f).setDuration(500)
                .withEndAction(() -> {
                    confettiContainer.removeAllViews();
                    confettiContainer.setVisibility(View.GONE);
                    confettiView = null;

                    progressManager.resetDiscoveredItems();

                    if (callback != null) {
                        callback.onResetProgressAndShowNextHint();
                    }
                }).start();

        banner.animate().alpha(0f).setDuration(500)
                .withEndAction(() -> banner.setVisibility(View.GONE))
                .start();
    }
}
