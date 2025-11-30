package com.vd.doodlequest.managers;

import android.widget.ToggleButton;

import com.vd.doodlequest.ui.views.SketchView;

public class ToolManager {

    private final SketchView sketchView;
    private final ToggleButton pencilBtn;
    private final ToggleButton eraserBtn;

    public ToolManager(
            SketchView sketchView,
            ToggleButton pencilBtn,
            ToggleButton eraserBtn
    ) {
        this.sketchView = sketchView;
        this.pencilBtn = pencilBtn;
        this.eraserBtn = eraserBtn;

        setupListeners();
    }

    private void setupListeners() {
        pencilBtn.setOnClickListener(v -> enablePencil());
        eraserBtn.setOnClickListener(v -> enableEraser());
    }

    private void enablePencil() {
        sketchView.setMode(SketchView.Mode.PENCIL);
        pencilBtn.setChecked(true);
        eraserBtn.setChecked(false);
    }

    private void enableEraser() {
        sketchView.setMode(SketchView.Mode.ERASER);
        pencilBtn.setChecked(false);
        eraserBtn.setChecked(true);
    }
}
