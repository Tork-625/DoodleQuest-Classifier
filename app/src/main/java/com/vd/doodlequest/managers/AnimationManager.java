package com.vd.doodlequest.managers;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.vd.doodlequest.R;

public class AnimationManager {

    public interface Callback { void run(); }

    public static void animateBadge(View badge) {
        ObjectAnimator sx = ObjectAnimator.ofFloat(badge, "scaleX", 1f, 1.3f, 1f);
        ObjectAnimator sy = ObjectAnimator.ofFloat(badge, "scaleY", 1f, 1.3f, 1f);

        AnimatorSet set = new AnimatorSet();
        set.playTogether(sx, sy);
        set.setDuration(300);
        set.start();
    }

    public static void playFadeClearTransition(
            View overlay,
            TextView hintText,
            Runnable clearCanvas,
            String newHint,
            Callback onEnd
    ) {
        overlay.setBackgroundColor(0xFFFFFFFF);
        overlay.setAlpha(0f);
        overlay.setVisibility(View.VISIBLE);

        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(overlay, "alpha", 0f, 1f);
        fadeIn.setDuration(300);

        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(overlay, "alpha", 1f, 0f);
        fadeOut.setDuration(300);

        AnimatorSet anim = new AnimatorSet();
        anim.playSequentially(fadeIn, fadeOut);

        fadeIn.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                if (clearCanvas != null) clearCanvas.run();

                hintText.setAlpha(0f);
                hintText.setText(newHint);
                hintText.animate().alpha(1f).setDuration(300).start();
            }
        });

        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                overlay.setVisibility(View.GONE);
                if (onEnd != null) onEnd.run();
            }
        });

        anim.start();
    }

    public static void playZoomFlashTransition(
            View overlay,
            Callback onMidway,
            Callback onEnd
    ) {
        overlay.setBackgroundColor(0xFFFFFFFF);
        overlay.setAlpha(0f);
        overlay.setScaleX(1f);
        overlay.setScaleY(1f);
        overlay.setVisibility(View.VISIBLE);

        ObjectAnimator alphaIn = ObjectAnimator.ofFloat(overlay, "alpha", 0f, 1f);
        ObjectAnimator zoomX = ObjectAnimator.ofFloat(overlay, "scaleX", 1f, 1.2f);
        ObjectAnimator zoomY = ObjectAnimator.ofFloat(overlay, "scaleY", 1f, 1.2f);

        alphaIn.setDuration(200);
        zoomX.setDuration(200);
        zoomY.setDuration(200);

        ObjectAnimator alphaOut = ObjectAnimator.ofFloat(overlay, "alpha", 1f, 0f);
        ObjectAnimator shrinkX = ObjectAnimator.ofFloat(overlay, "scaleX", 1.2f, 1f);
        ObjectAnimator shrinkY = ObjectAnimator.ofFloat(overlay, "scaleY", 1.2f, 1f);

        alphaOut.setDuration(400);
        shrinkX.setDuration(400);
        shrinkY.setDuration(400);

        AnimatorSet first = new AnimatorSet();
        first.playTogether(alphaIn, zoomX, zoomY);

        AnimatorSet second = new AnimatorSet();
        second.playTogether(alphaOut, shrinkX, shrinkY);

        alphaIn.addUpdateListener(anim -> {
            if (anim.getAnimatedFraction() > 0.7f && onEnd != null) {
                onEnd.run();
            }
        });

        first.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (onMidway != null) onMidway.run();
                second.start();
            }
        });

        second.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                overlay.setVisibility(View.GONE);
            }
        });

        first.start();
    }

    public static void playWrongCardAnimation(
            Context context,
            FrameLayout overlayLayer,
            Runnable onDone
    ) {
        overlayLayer.removeAllViews();
        overlayLayer.setVisibility(View.VISIBLE);

        View dim = new View(context);
        dim.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));
        dim.setBackgroundColor(0x88000000);
        dim.setAlpha(0f);
        overlayLayer.addView(dim);

        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(dim, "alpha", 0f, 1f);
        fadeIn.setDuration(400);
        fadeIn.start();

        View wrongCard = LayoutInflater.from(context)
                .inflate(R.layout.card_wrong_guess, overlayLayer, false);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
        );
        wrongCard.setLayoutParams(params);

        overlayLayer.addView(wrongCard);

        wrongCard.setScaleX(0.5f);
        wrongCard.setScaleY(0.5f);
        wrongCard.setAlpha(0f);

        AnimatorSet appear = new AnimatorSet();
        appear.playTogether(
                ObjectAnimator.ofFloat(wrongCard, "scaleX", 0.5f, 1f),
                ObjectAnimator.ofFloat(wrongCard, "scaleY", 0.5f, 1f),
                ObjectAnimator.ofFloat(wrongCard, "alpha", 0f, 1f)
        );
        appear.setDuration(400);
        appear.start();

        appear.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                AnimatorSet exit = new AnimatorSet();
                exit.playTogether(
                        ObjectAnimator.ofFloat(wrongCard, "alpha", 1f, 0f),
                        ObjectAnimator.ofFloat(dim, "alpha", 1f, 0f)
                );
                exit.setDuration(400);
                exit.start();

                exit.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        overlayLayer.removeAllViews();
                        overlayLayer.setVisibility(View.GONE);
                        if (onDone != null) onDone.run();
                    }
                });
            }
        });
    }


    public static void playCorrectCardReveal(View card, Runnable onReadyToFly) {
        card.setScaleX(0f);
        card.setScaleY(0f);
        card.setRotationY(0f);

        AnimatorSet reveal = new AnimatorSet();
        reveal.playTogether(
                ObjectAnimator.ofFloat(card, "scaleX", 0f, 1.2f, 1f),
                ObjectAnimator.ofFloat(card, "scaleY", 0f, 1.2f, 1f),
                ObjectAnimator.ofFloat(card, "rotationY", 0f, 360f)
        );
        reveal.setDuration(1000);
        reveal.start();

        reveal.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {

                AnimatorSet pulse = new AnimatorSet();
                pulse.playTogether(
                        ObjectAnimator.ofFloat(card, "scaleX", 1f, 1.05f, 1f),
                        ObjectAnimator.ofFloat(card, "scaleY", 1f, 1.05f, 1f)
                );
                pulse.setDuration(350);
                pulse.start();

                card.setOnClickListener(v -> onReadyToFly.run());
            }
        });
    }

    public static void animateCardToLibrary(
            View card,
            ImageButton libraryBtn,
            Runnable onEnd
    ) {
        float targetX = libraryBtn.getX() + libraryBtn.getWidth() / 2f - card.getWidth() / 2f;
        float targetY = libraryBtn.getY() + libraryBtn.getHeight() / 2f - card.getHeight() / 2f;

        AnimatorSet anim = new AnimatorSet();
        anim.playTogether(
                ObjectAnimator.ofFloat(card, "x", card.getX(), targetX),
                ObjectAnimator.ofFloat(card, "y", card.getY(), targetY),
                ObjectAnimator.ofFloat(card, "rotationY", 0f, 270f),
                ObjectAnimator.ofFloat(card, "scaleX", 1f, 0.1f),
                ObjectAnimator.ofFloat(card, "scaleY", 1f, 0.1f)
        );
        anim.setDuration(600);
        anim.start();

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (onEnd != null) onEnd.run();
            }
        });
    }
}
