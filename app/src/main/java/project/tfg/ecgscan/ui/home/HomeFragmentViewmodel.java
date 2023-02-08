package project.tfg.ecgscan.ui.home;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.tflite.client.TfLiteInitializationOptions;

import org.tensorflow.lite.task.gms.vision.TfLiteVision;


public class HomeFragmentViewmodel extends ViewModel {


    public void initializeTensorflowlite(Context context){

        TfLiteInitializationOptions options = TfLiteInitializationOptions.builder()
                .setEnableGpuDelegateSupport(true)
                .build();

        TfLiteVision.initialize(context, options).addOnSuccessListener(unused -> {
            //  initialized
            String modelName = "name";
        }).addOnFailureListener(e -> {
            //  error
        });
    }

}
