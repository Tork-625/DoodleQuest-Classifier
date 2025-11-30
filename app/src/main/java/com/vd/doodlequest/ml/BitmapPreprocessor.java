package com.vd.doodlequest.ml;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import androidx.core.graphics.BitmapCompat;

public class BitmapPreprocessor {

    public static Bitmap process(Bitmap bitmap) {
        int padding = 150;
        int targetSize = 28;

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int minX = width, minY = height, maxX = 0, maxY = 0;

        // Find bounding box
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = bitmap.getPixel(x, y);
                if (!isWhite(pixel)) {
                    if (x < minX) minX = x;
                    if (y < minY) minY = y;
                    if (x > maxX) maxX = x;
                    if (y > maxY) maxY = y;
                }
            }
        }

        if (minX > maxX || minY > maxY) {
            minX = minY = 0;
            maxX = width - 1;
            maxY = height - 1;
        }

        int cropWidth = maxX - minX + 1;
        int cropHeight = maxY - minY + 1;
        int squareSize = Math.max(cropWidth, cropHeight) + padding;

        // Create padded square
        Bitmap squareBitmap = Bitmap.createBitmap(squareSize, squareSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(squareBitmap);
        canvas.drawColor(Color.WHITE);

        int dx = (squareSize - cropWidth) / 2;
        int dy = (squareSize - cropHeight) / 2;

        Bitmap tightCrop = Bitmap.createBitmap(bitmap, minX, minY, cropWidth, cropHeight);
        canvas.drawBitmap(tightCrop, dx, dy, null);

        // Invert
        Paint invertPaint = new Paint();
        android.graphics.ColorMatrix cm = new android.graphics.ColorMatrix(new float[]{
                -1, 0, 0, 0, 255,
                0,-1, 0, 0, 255,
                0, 0,-1, 0, 0,
                0, 0, 0, 1,   0
        });
        invertPaint.setColorFilter(new android.graphics.ColorMatrixColorFilter(cm));

        Bitmap inverted = Bitmap.createBitmap(squareBitmap.getWidth(), squareBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        new Canvas(inverted).drawBitmap(squareBitmap, 0, 0, invertPaint);

        Rect src = new Rect(0, 0, inverted.getWidth(), inverted.getHeight());

        return BitmapCompat.createScaledBitmap(
                inverted,
                targetSize,
                targetSize,
                src,
                false
        );
    }

    private static boolean isWhite(int pixel) {
        int r = Color.red(pixel);
        int g = Color.green(pixel);
        int b = Color.blue(pixel);
        return r > 240 && g > 240 && b > 240;
    }
}
