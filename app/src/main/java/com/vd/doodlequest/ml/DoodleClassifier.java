package com.vd.doodlequest.ml;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;

import org.pytorch.IValue;
import org.pytorch.Module;
import org.pytorch.Tensor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DoodleClassifier {

    private Module module;

    public DoodleClassifier(Context context, String modelAssetName) {
        try {
            String path = assetFilePath(context, modelAssetName);
            module = Module.load(path);
        } catch (IOException e) {}
    }

    private String assetFilePath(Context context, String assetName) throws IOException {
        File file = new File(context.getFilesDir(), assetName);
        if (file.exists() && file.length() > 0) {
            return file.getAbsolutePath();
        }
        try (InputStream is = context.getAssets().open(assetName);
             FileOutputStream os = new FileOutputStream(file)) {
            byte[] buffer = new byte[4096];
            int read;
            while ((read = is.read(buffer)) != -1) {
                os.write(buffer, 0, read);
            }
            os.flush();
            return file.getAbsolutePath();
        }
    }

    private Tensor preprocess(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float[] floatValues = new float[width * height];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = bitmap.getPixel(x, y);
                int r = Color.red(pixel);
                int g = Color.green(pixel);
                int b = Color.blue(pixel);

                float gray = (r + g + b) / 3f / 255f;

                float binary = gray > 0f ? 1.0f : 0.0f;

                floatValues[y * width + x] = binary;
            }
        }
        return Tensor.fromBlob(floatValues, new long[]{1, 1, height, width});
    }

    public Prediction[] getTopFivePredictions(Bitmap bitmap, String[] classes) {
        Tensor inputTensor = preprocess(bitmap);

        Tensor outputTensor = module.forward(IValue.from(inputTensor)).toTensor();

        float[] logits = outputTensor.getDataAsFloatArray();

        float sumExp = 0f;
        for (float l : logits) sumExp += Math.exp(l);
        float[] probs = new float[logits.length];
        for (int i = 0; i < logits.length; i++) {
            probs[i] = (float)(Math.exp(logits[i]) / sumExp);
        }

        Prediction[] topFivePredictions = new Prediction[5];
        for (int i = 0; i < 5; i++) topFivePredictions[i] = new Prediction("", 0f);

        for (int i = 0; i < probs.length; i++) {
            for (int j = 0; j < 5; j++) {
                if (probs[i] > topFivePredictions[j].probability) {
                    for (int k = 4; k > j; k--) topFivePredictions[k] = topFivePredictions[k - 1];
                    topFivePredictions[j] = new Prediction(classes[i], probs[i]);
                    break;
                }
            }
        }

        return topFivePredictions;
    }


    public static class Prediction {
        public String label;
        public float probability;
        public Prediction(String label, float probability) {
            this.label = label;
            this.probability = probability;
        }
    }
}
