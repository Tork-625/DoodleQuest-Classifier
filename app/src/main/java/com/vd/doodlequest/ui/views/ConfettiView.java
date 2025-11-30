package com.vd.doodlequest.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ConfettiView extends View {

    private static class Particle {
        float x, y, size, speedY, speedX;
        int color;
    }

    private final List<Particle> particles = new ArrayList<>();
    private final Paint paint = new Paint();
    private final Random random = new Random();
    private boolean isRunning = true;
    private int particleCount = 100;

    public ConfettiView(Context context, int particleCount) {
        super(context);
        this.particleCount = particleCount;
        paint.setStyle(Paint.Style.FILL);
        initialize();
    }

    public ConfettiView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setStyle(Paint.Style.FILL);
        initialize();
    }

    private void initialize() {
        addOnLayoutChangeListener(new OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (getWidth() > 0 && getHeight() > 0 && particles.isEmpty()) {
                    initParticles(getWidth(), getHeight());
                    removeOnLayoutChangeListener(this);

                    post(updateRunnable);
                }
            }
        });
    }


    private void initParticles(int width, int height) {
        particles.clear();
        for (int i = 0; i < particleCount; i++) {
            Particle p = new Particle();
            p.x = random.nextInt(width);
            p.y = random.nextInt(height);
            p.size = 10 + random.nextInt(15);
            p.speedY = 5 + random.nextInt(8);
            p.speedX = random.nextFloat() * 4 - 2;
            p.color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            particles.add(p);
        }
    }

    private final Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isRunning || particles.isEmpty()) return;

            int width = getWidth();
            int height = getHeight();

            for (Particle p : particles) {
                p.y += p.speedY;
                p.x += p.speedX;

                if (p.y > height) p.y = -p.size;
                if (p.x > width) p.x = 0;
                if (p.x < 0) p.x = width;
            }

            invalidate();
            postDelayed(this, 16); // ~60fps
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Particle p : particles) {
            paint.setColor(p.color);
            canvas.drawCircle(p.x, p.y, p.size, paint);
        }
    }

    public void stop() {
        isRunning = false;
    }
}
