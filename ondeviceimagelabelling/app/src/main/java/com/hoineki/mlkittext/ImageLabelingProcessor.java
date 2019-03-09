package com.hoineki.mlkittext;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.hoineki.mlkittext.utility.CameraImageGraphic;
import com.hoineki.mlkittext.utility.FrameMetadata;
import com.hoineki.mlkittext.utility.GraphicOverlay;

import java.io.IOException;
import java.util.List;

/**
 * Custom Image Classifier Demo.
 */
public class ImageLabelingProcessor extends VisionProcessorBase<List<FirebaseVisionImageLabel>> {

    private static final String TAG = "ImageLabelingProcessor";

    private final FirebaseVisionImageLabeler detector;

    public ImageLabelingProcessor() {
        detector = FirebaseVision.getInstance().getOnDeviceImageLabeler();
    }

    @Override
    public void stop() {
        try {
            detector.close();
        } catch (IOException e) {
            Log.e(TAG, "Exception thrown while trying to close Text Detector: " + e);
        }
    }

    @Override
    protected Task<List<FirebaseVisionImageLabel>> detectInImage(FirebaseVisionImage image) {
        return detector.processImage(image);
    }

    @Override
    protected void onSuccess(
            @Nullable Bitmap originalCameraImage,
            @NonNull List<FirebaseVisionImageLabel> labels,
            @NonNull FrameMetadata frameMetadata,
            @NonNull GraphicOverlay graphicOverlay) {
        graphicOverlay.clear();
        if (originalCameraImage != null) {
            CameraImageGraphic imageGraphic = new CameraImageGraphic(graphicOverlay, originalCameraImage);
            graphicOverlay.add(imageGraphic);
        }
        LabelGraphic labelGraphic = new LabelGraphic(graphicOverlay, labels);
        graphicOverlay.add(labelGraphic);
        graphicOverlay.postInvalidate();
    }

    @Override
    protected void onFailure(@NonNull Exception e) {
        Log.w(TAG, "Label detection failed." + e);
    }
}