package com.vd.doodlequest.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FallingShapesView extends View {

    private final List<Shape> shapes = new ArrayList<>();
    private final Random random = new Random();
    private final int shapeCount = 200;
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final int[] colors = {
            Color.parseColor("#388E3C"),
            Color.parseColor("#D32F2F"),
            Color.parseColor("#0097A7"),
            Color.parseColor("#FFA000"),
            Color.parseColor("#C2185B")
    };

    public FallingShapesView(Context context) {
        super(context);
        init();
    }

    public FallingShapesView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FallingShapesView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < shapeCount; i++) {
            shapes.add(new Shape());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        for (Shape s : shapes) {
            if (!s.initialized) {
                s.reset(width, height);
                s.initialized = true;
            }
        }

        for (Shape s : shapes) {
            s.y += s.speed;
            if (s.y > height) s.reset(width, height);
            drawShape(canvas, s);
        }

        postInvalidateOnAnimation();
    }

    private void drawShape(Canvas canvas, Shape s) {
        paint.setColor(s.color);

        switch (s.type) {
            case STAR:
                drawStar(canvas, s.x, s.y, 20);
                break;
            case HEART:
                drawHeart(canvas, s.x, s.y, 20);
                break;
            case CIRCLE:
                canvas.drawCircle(s.x, s.y, 20, paint);
                break;
            case TRIANGLE:
                drawTriangle(canvas, s.x, s.y, 30);
                break;
            case SQUARE:
                canvas.drawRect(s.x - 20, s.y - 20, s.x + 20, s.y + 20, paint);
                break;
        }
    }

    private void drawStar(Canvas canvas, float cx, float cy, float radius) {
        Path path = new Path();
        for (int i = 0; i < 5; i++) {
            double angle = Math.toRadians(i * 72 - 90);
            float x = (float) (cx + radius * Math.cos(angle));
            float y = (float) (cy + radius * Math.sin(angle));
            if (i == 0) path.moveTo(x, y);
            else path.lineTo(x, y);
        }
        path.close();
        canvas.drawPath(path, paint);
    }

    private void drawTriangle(Canvas canvas, float cx, float cy, float size) {
        Path path = new Path();
        path.moveTo(cx, cy - size / 2);
        path.lineTo(cx - size / 2, cy + size / 2);
        path.lineTo(cx + size / 2, cy + size / 2);
        path.close();
        canvas.drawPath(path, paint);
    }

    private void drawHeart(Canvas canvas, float x, float y, float size) {
        Path path = new Path();
        float half = size / 2;
        path.moveTo(x, y + half);
        path.cubicTo(x - size, y - half, x - half, y - size, x, y - half);
        path.cubicTo(x + half, y - size, x + size, y - half, x, y + half);
        path.close();
        canvas.drawPath(path, paint);
    }

    private enum ShapeType { STAR, HEART, CIRCLE, TRIANGLE, SQUARE }

    private class Shape {
        float x;
        float y;
        float speed;
        int color;
        ShapeType type;
        boolean initialized = false;

        Shape() {
            speed = 2 + random.nextInt(6);
            color = colors[random.nextInt(colors.length)];
            type = ShapeType.values()[random.nextInt(ShapeType.values().length)];
        }

        void reset(int screenWidth, int screenHeight) {
            x = random.nextInt(screenWidth);
            y = -50 - random.nextInt(screenHeight / 2);
            speed = 2 + random.nextInt(6);
        }
    }
}
