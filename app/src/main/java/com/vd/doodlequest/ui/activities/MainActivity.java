package com.vd.doodlequest.ui.activities;

import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.vd.doodlequest.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.setNavigationBarColor(getResources().getColor(android.R.color.black));

        setContentView(R.layout.activity_main);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        Button playButton = findViewById(R.id.play_button);

        playButton.setOnClickListener(v -> {
            AnimatorSet clickAnim = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.play_button_animator);
            clickAnim.setTarget(playButton);
            clickAnim.start();

            clickAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(android.animation.Animator animation) {
                    startActivity(new Intent(MainActivity.this, DrawingActivity.class));
                }
            });
        });
    }

    public void openDrawingScreen(View view) {
        startActivity(new Intent(this, DrawingActivity.class));
    }
}
