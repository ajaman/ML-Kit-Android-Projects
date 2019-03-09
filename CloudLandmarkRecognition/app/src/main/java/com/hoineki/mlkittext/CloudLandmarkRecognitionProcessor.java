package com.hoineki.mlkittext;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions;
import com.google.firebase.ml.vision.cloud.landmark.FirebaseVisionCloudLandmark;
import com.google.firebase.ml.vision.cloud.landmark.FirebaseVisionCloudLandmarkDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.hoineki.mlkittext.utility.FrameMetadata;
import com.hoineki.mlkittext.utility.GraphicOverlay;

import java.util.List;

/**
 * Cloud Landmark Detector Demo.
 */
public class CloudLandmarkRecognitionProcessor
        extends VisionProcessorBase<List<FirebaseVisionCloudLandmark>> {
    private static final String TAG = "CloudLmkRecProc";

    private final FirebaseVisionCloudLandmarkDetector detector;

    public CloudLandmarkRecognitionProcessor() {
        super();
        FirebaseVisionCloudDetectorOptions options =
                new FirebaseVisionCloudDetectorOptions.Builder()
                        .setMaxResults(10)
                        .setModelType(FirebaseVisionCloudDetectorOptions.STABLE_MODEL)
                        .build();

        detector = FirebaseVision.getInstance().getVisionCloudLandmarkDetector(options);
    }

    @Override
    protected Task<List<FirebaseVisionCloudLandmark>> detectInImage(FirebaseVisionImage image) {
        return detector.detectInImage(image);
    }

    @Override
    protected void onSuccess(
            @Nullable Bitmap originalCameraImage,
            @NonNull List<FirebaseVisionCloudLandmark> landmarks,
            @NonNull FrameMetadata frameMetadata,
            @NonNull GraphicOverlay graphicOverlay) {
        graphicOverlay.clear();
        Log.d(TAG, "cloud landmark size: " + landmarks.size());
        for (int i = 0; i < landmarks.size(); ++i) {
            FirebaseVisionCloudLandmark landmark = landmarks.get(i);
            Log.d(TAG, "cloud landmark: " + landmark);
            CloudLandmarkGraphic cloudLandmarkGraphic = new CloudLandmarkGraphic(graphicOverlay,
                    landmark);
            graphicOverlay.add(cloudLandmarkGraphic);
        }
        graphicOverlay.postInvalidate();
    }

    @Override
    protected void onFailure(@NonNull Exception e) {
        Log.e(TAG, "Cloud Landmark detection failed " + e);
    }
}