package com.vd.doodlequest.managers;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.vd.doodlequest.R;
import com.vd.doodlequest.ml.DoodleClassifier;

public class PredictionManager {

    public interface Callback {
        void onCorrectGuess();
        void onWrongGuess();
        void onCardSavedToLibrary();
    }

    private final Context context;
    private final FrameLayout overlayContainer;
    private final Callback callback;

    public PredictionManager(Context context, FrameLayout overlayContainer, Callback callback) {
        this.context = context;
        this.overlayContainer = overlayContainer;
        this.callback = callback;
    }

    public void processPrediction(
            DoodleClassifier.Prediction[] top5,
            String target,
            int drawableRes,
            String displayLabel,
            Runnable onProgressUpdate,
            FrameLayout wrongPredictionOverlay
    ) {
        if (target == null) return;

        boolean match = isPredictionCorrect(top5, target);

        if (match) {
            if (onProgressUpdate != null) onProgressUpdate.run();
            showCorrectCard(drawableRes, displayLabel);
            callback.onCorrectGuess();
        } else {
            AnimationManager.playWrongCardAnimation(
                    context,
                    wrongPredictionOverlay,
                    callback::onWrongGuess
            );
        }
    }

    public boolean isPredictionCorrect(DoodleClassifier.Prediction[] top5, String target) {
        if (top5 == null || top5.length == 0 || target == null) return false;

        for (int i = 0; i < Math.min(2, top5.length); i++) {
            if (top5[i].label.equals(target)) return true;
        }
        return false;
    }

    public void showCorrectCard(int drawableRes, String displayLabel) {
        View card = LayoutInflater.from(context)
                .inflate(R.layout.card_correct_guess, overlayContainer, false);

        ImageView cardImage = card.findViewById(R.id.card_image);
        cardImage.setImageResource(drawableRes);

        TextView cardLabel = card.findViewById(R.id.card_label);
        cardLabel.setText(displayLabel);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                dpToPx(200),
                dpToPx(300)
        );
        params.gravity = Gravity.CENTER;
        card.setLayoutParams(params);

        overlayContainer.addView(card);

        AnimationManager.playCorrectCardReveal(card, () -> sendCardToLibrary(card));
    }

    private void sendCardToLibrary(View card) {
        ImageButton libraryBtn =
                ((AppCompatActivity) context).findViewById(R.id.button_library);

        AnimationManager.animateCardToLibrary(
                card,
                libraryBtn,
                () -> {
                    overlayContainer.removeView(card);
                    callback.onCardSavedToLibrary();
                }
        );
    }

    private int dpToPx(int dp) {
        return Math.round(dp * context.getResources().getDisplayMetrics().density);
    }
}
