package com.vd.doodlequest.ui.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.vd.doodlequest.managers.PredictionManager;
import com.vd.doodlequest.managers.ProgressOverlayManager;
import com.vd.doodlequest.R;
import com.vd.doodlequest.ui.views.SketchView;
import com.vd.doodlequest.managers.ToolManager;
import com.vd.doodlequest.models.UnlockState;
import com.vd.doodlequest.data.DoodleProgressRepository;
import com.vd.doodlequest.managers.AnimationManager;
import com.vd.doodlequest.managers.ConfettiManager;
import com.vd.doodlequest.ml.DoodleClassifier;

public class DrawingActivity extends AppCompatActivity {

    private FrameLayout progressOverlay;
    private ImageView dot1, dot2, dot3;
    private TextView badge;
    private TextView hintText;
    private int unseenCount = 0;
    ToggleButton changeHintBtn;
    String[] classes;
    private String currentTargetItem;
    private DoodleProgressRepository progress;
    private SketchView sketchView;
    private DoodleClassifier model;
    private ProgressOverlayManager progressOverlayManager;
    private PredictionManager predictionManager;
    private ConfettiManager confettiManager;
    private ToolManager toolManager;
    private FrameLayout confettiContainer;
    private ImageView congratsBanner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawing);

        if (getSupportActionBar() != null) getSupportActionBar().hide();
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.setNavigationBarColor(getResources().getColor(android.R.color.black));

        badge = findViewById(R.id.badge_count);
        progressOverlay = findViewById(R.id.progressOverlay);
        dot1 = findViewById(R.id.dot1);
        dot2 = findViewById(R.id.dot2);
        dot3 = findViewById(R.id.dot3);

        updateBadge();

        progressOverlayManager = new ProgressOverlayManager(
                progressOverlay, dot1, dot2, dot3
        );

        FrameLayout container = findViewById(R.id.draw_container);
        sketchView = new SketchView(this);
        container.addView(sketchView);

        ToggleButton pencilToggle = findViewById(R.id.toggle_pencil);
        ToggleButton eraserToggle = findViewById(R.id.toggle_eraser);

        toolManager = new ToolManager(sketchView, pencilToggle, eraserToggle);

        hintText = findViewById(R.id.text_hint);
        changeHintBtn = findViewById(R.id.toggle_change);

        progress = new DoodleProgressRepository(this);
        classes = progress.getAllItemsArray();

        changeHintBtn.setOnClickListener(v -> {
            String nextItem = progress.getRandomUndiscoveredItem();
            String hint = nextItem != null ? progress.getHintForItem(nextItem) : "";

            currentTargetItem = nextItem;

            View overlay = findViewById(R.id.sweep_overlay);
            AnimationManager.playFadeClearTransition(
                    overlay,
                    hintText,
                    () -> sketchView.clear(),
                    hint,
                    null
            );

            changeHintBtn.setChecked(false);
        });

        ImageButton refreshButton = findViewById(R.id.button_refresh);
        refreshButton.setOnClickListener(v -> {
            ObjectAnimator fadeOut = ObjectAnimator.ofFloat(sketchView, "alpha", 1f, 0f);
            fadeOut.setDuration(300);
            fadeOut.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    sketchView.clear();
                    ObjectAnimator.ofFloat(sketchView, "alpha", 0f, 1f)
                            .setDuration(300).start();
                }
            });
            fadeOut.start();
        });

        model = new DoodleClassifier(this, "Doodle_Recognizer_CNN_Model_1.pt");

        showNextHint();

        FrameLayout drawContainer = findViewById(R.id.draw_container);

        predictionManager = new PredictionManager(
                this,
                drawContainer,
                new PredictionManager.Callback() {
                    @Override public void onCorrectGuess() {}

                    @Override public void onWrongGuess() {}

                    @Override
                    public void onCardSavedToLibrary() {
                        unseenCount++;
                        updateBadge();
                        AnimationManager.animateBadge(badge);

                        View overlay = findViewById(R.id.sweep_overlay);
                        AnimationManager.playZoomFlashTransition(
                                overlay,
                                () -> showNextHint(),
                                () -> sketchView.clear()
                        );
                    }
                }
        );

        ImageButton tickButton = findViewById(R.id.button_tick);
        tickButton.setOnClickListener(v -> {
            progressOverlayManager.show();

            new Thread(() -> {
                if (model == null) {
                    Log.e("predict", "MODEL NULL");
                    runOnUiThread(() -> progressOverlayManager.hide());
                    return;
                }

                Bitmap processedBitmap = sketchView.getProcessedBitmap();
                DoodleClassifier.Prediction[] top5 =
                        model.getTopFivePredictions(processedBitmap, classes);

                runOnUiThread(() -> {
                    progressOverlayManager.hide();
                    handlePrediction(top5);
                });
            }).start();
        });

        ImageButton libraryButton = findViewById(R.id.button_library);
        libraryButton.setOnClickListener(v -> {
            unseenCount = 0;
            updateBadge();
            startActivity(new Intent(this, LibraryActivity.class));
        });

        confettiContainer = findViewById(R.id.confettiContainer);
        congratsBanner = findViewById(R.id.congrats_banner);

        confettiManager = new ConfettiManager(
                this,
                progress,
                () -> {
                    currentTargetItem = progress.getRandomUndiscoveredItem();
                    if (currentTargetItem != null)
                        hintText.setText(progress.getHintForItem(currentTargetItem));
                }
        );

        confettiManager.initViews(confettiContainer, congratsBanner);
    }

    private void handlePrediction(DoodleClassifier.Prediction[] top5) {
        if (currentTargetItem == null) return;

        int drawableRes = getDrawableRes(currentTargetItem);
        String label = capitalizeWords(currentTargetItem);

        FrameLayout wrongOverlay = findViewById(R.id.top_overlay_container);

        predictionManager.processPrediction(
                top5,
                currentTargetItem,
                drawableRes,
                label,
                () -> {
                    progress.addDiscovered(currentTargetItem);
                    int idx = getIndexOfClass(currentTargetItem);
                    if (idx != -1) UnlockState.unlock(idx);
                },
                wrongOverlay
        );
    }

    private void updateBadge() {
        if (unseenCount > 0) {
            badge.setText(String.valueOf(unseenCount));
            badge.setVisibility(View.VISIBLE);
        } else {
            badge.setVisibility(View.GONE);
        }
    }

    private void showNextHint() {
        currentTargetItem = progress.getRandomUndiscoveredItem();

        if (currentTargetItem == null) {
            confettiManager.start();
            hintText.setText("You’ve discovered everything! 🎉");
            progress.markEverythingDiscovered();
            return;
        }

        hintText.setText(progress.getHintForItem(currentTargetItem));
    }

    private int getDrawableRes(String label) {
        String drawableName = label.toLowerCase().replace(" ", "_");
        return getResources().getIdentifier(drawableName, "drawable", getPackageName());
    }

    private int getIndexOfClass(String item) {
        for (int i = 0; i < classes.length; i++) {
            if (classes[i].equals(item)) return i;
        }
        return -1;
    }

    private String capitalizeWords(String text) {
        String[] words = text.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String w : words) {
            if (w.length() > 0) {
                sb.append(Character.toUpperCase(w.charAt(0)))
                        .append(w.substring(1))
                        .append(" ");
            }
        }
        return sb.toString().trim();
    }
}
