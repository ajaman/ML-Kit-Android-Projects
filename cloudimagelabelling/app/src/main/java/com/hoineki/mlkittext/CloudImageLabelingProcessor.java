package com.hoineki.mlkittext;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.label.FirebaseVisionCloudImageLabelerOptions;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionImageLabeler;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.hoineki.mlkittext.utility.FrameMetadata;
import com.hoineki.mlkittext.utility.GraphicOverlay;

import java.util.ArrayList;
import java.util.List;

/**
 * Cloud Label Detector Demo.
 */
public class CloudImageLabelingProcessor
        extends VisionProcessorBase<List<FirebaseVisionImageLabel>> {
    private static final String TAG = "CloudImgLabelProc";

    private final FirebaseVisionImageLabeler detector;

    public CloudImageLabelingProcessor() {
        FirebaseVisionCloudImageLabelerOptions.Builder optionsBuilder =
                new FirebaseVisionCloudImageLabelerOptions.Builder();

        detector = FirebaseVision.getInstance().getCloudImageLabeler(optionsBuilder.build());
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
        Log.d(TAG, "cloud label size: " + labels.size());
        List<String> labelsStr = new ArrayList<>();
        for (int i = 0; i < labels.size(); ++i) {
            FirebaseVisionImageLabel label = labels.get(i);
            Log.d(TAG, "cloud label: " + label);
            if (label.getText() != null) {
                labelsStr.add((label.getText()));
            }
        }
        CloudLabelGraphic cloudLabelGraphic = new CloudLabelGraphic(graphicOverlay, labelsStr);
        graphicOverlay.add(cloudLabelGraphic);
        graphicOverlay.postInvalidate();
    }

    @Override
    protected void onFailure(@NonNull Exception e) {
        Log.e(TAG, "Cloud Label detection failed " + e);
    }
}