package com.hoineki.mlkittext;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.google.firebase.ml.vision.document.FirebaseVisionDocumentText;
import com.hoineki.mlkittext.utility.GraphicOverlay;

/**
 * Graphic instance for rendering TextBlock position, size, and ID within an associated graphic
 * overlay view.
 */
public class CloudDocumentTextGraphic extends GraphicOverlay.Graphic {
    private static final int TEXT_COLOR = Color.WHITE;
    private static final float TEXT_SIZE = 54.0f;
    private static final float STROKE_WIDTH = 4.0f;

    private final Paint rectPaint;
    private final Paint textPaint;
    private final FirebaseVisionDocumentText.Symbol symbol;
    private final GraphicOverlay overlay;

    CloudDocumentTextGraphic(GraphicOverlay overlay, FirebaseVisionDocumentText.Symbol symbol) {
        super(overlay);

        this.symbol = symbol;
        this.overlay = overlay;

        rectPaint = new Paint();
        rectPaint.setColor(TEXT_COLOR);
        rectPaint.setStyle(Paint.Style.STROKE);
        rectPaint.setStrokeWidth(STROKE_WIDTH);

        textPaint = new Paint();
        textPaint.setColor(TEXT_COLOR);
        textPaint.setTextSize(TEXT_SIZE);
    }

    /** Draws the text block annotations for position, size, and raw value on the supplied canvas. */
    @Override
    public void draw(Canvas canvas) {
        if (symbol == null) {
            throw new IllegalStateException("Attempting to draw a null text.");
        }

        Rect rect = symbol.getBoundingBox();
        canvas.drawRect(rect, rectPaint);
        canvas.drawText(symbol.getText(), rect.left, rect.bottom, textPaint);
    }
}