package com.vd.doodlequest.ui.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

import com.vd.doodlequest.R;
import com.vd.doodlequest.ml.BitmapPreprocessor;

public class SketchView extends View {

    private Bitmap bitmap;
    private Canvas bitmapCanvas;
    private Paint paint;
    public enum Mode { PENCIL, ERASER }
    private Mode currentMode = Mode.PENCIL;
    private float lastX = -1;
    private float lastY = -1;
    private Bitmap pencilCursor;
    private Bitmap eraserCursor;

    public SketchView(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(8f);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);

        pencilCursor = BitmapFactory.decodeResource(getResources(), R.drawable.ic_pencil_cursor);
        eraserCursor = BitmapFactory.decodeResource(getResources(), R.drawable.ic_eraser_cursor);

        int size = 130;
        pencilCursor = resize(pencilCursor, size, size);
        eraserCursor = resize(eraserCursor, size, size);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmap);
        bitmapCanvas.drawColor(Color.WHITE);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(bitmap, 0, 0, null);

        if (lastX >= 0 && lastY >= 0) {
            if (currentMode == Mode.PENCIL && pencilCursor != null) {
                float left = lastX - pencilCursor.getWidth() / 4.7f;
                float top  = lastY - pencilCursor.getHeight() * 3.5f / 4f;
                canvas.drawBitmap(pencilCursor, left, top, null);

            } else if (currentMode == Mode.ERASER && eraserCursor != null) {
                float left = lastX - eraserCursor.getWidth() / 2f;
                float top  = lastY - eraserCursor.getHeight() / 2f;
                canvas.drawBitmap(eraserCursor, left, top, null);
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                lastX = x;
                lastY = y;
                drawPoint(x, y);
                break;

            case MotionEvent.ACTION_MOVE:
                drawLine(lastX, lastY, x, y);
                lastX = x;
                lastY = y;
                break;

            case MotionEvent.ACTION_UP:
                lastX = -1;
                lastY = -1;
                break;
        }

        invalidate();
        return true;
    }


    private void drawPoint(float x, float y) {
        if (currentMode == Mode.PENCIL) {
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(8f);

        } else if (currentMode == Mode.ERASER) {
            paint.setColor(Color.WHITE);
            paint.setStrokeWidth(100f);
        }

        bitmapCanvas.drawCircle(x, y, paint.getStrokeWidth() / 2, paint);
    }


    private void drawLine(float startX, float startY, float endX, float endY) {
        if (currentMode == Mode.PENCIL) {
            paint.setColor(Color.BLACK);
            paint.setStrokeWidth(8f);

        } else if (currentMode == Mode.ERASER) {
            paint.setColor(Color.WHITE);
            paint.setStrokeWidth(100f);
        }

        bitmapCanvas.drawLine(startX, startY, endX, endY, paint);
    }


    private Bitmap resize(Bitmap src, int newW, int newH) {
        return Bitmap.createScaledBitmap(src, newW, newH, true);
    }

    public void setMode(Mode mode) {
        currentMode = mode;
    }


    public Bitmap getProcessedBitmap() {
        return BitmapPreprocessor.process(bitmap);
    }

    public void clear() {
        if (bitmap != null && bitmapCanvas != null) {
            bitmapCanvas.drawColor(Color.WHITE);
            invalidate();
        }
    }
}
