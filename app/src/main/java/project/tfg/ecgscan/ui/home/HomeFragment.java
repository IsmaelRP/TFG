package project.tfg.ecgscan.ui.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

import project.tfg.ecgscan.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding b;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = FragmentHomeBinding.inflate(inflater, container, false);
        return b.getRoot();
    }


    private void saveImage(Bitmap image) {
        b.imageView.setImageBitmap(image);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews();
        FirebaseAuth.getInstance().signOut();   //TODO: CUIDADO BORRAR PELIGRO
    }

    private void setupViews() {
        b.btnScan.setOnClickListener(v -> dispatchTakePictureIntent(MediaStore.ACTION_IMAGE_CAPTURE));
        b.btnUpload.setOnClickListener(v -> dispatchSelectPictureIntent());
    }

    private void dispatchSelectPictureIntent() {
        Intent selectPictureIntent = new Intent();
        selectPictureIntent.setType("image/*");
        selectPictureIntent.setAction(Intent.ACTION_GET_CONTENT);

        if (selectPictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            imageCallback.launch(selectPictureIntent);
        }
    }


    private void dispatchTakePictureIntent(String action_code) {
        Intent takePictureIntent = new Intent(action_code);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            imageCallback.launch(takePictureIntent);
        }
    }

    private ActivityResultLauncher<Intent> imageCallback = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    if (result.getData().getExtras() != null){
                        saveImage((Bitmap) result.getData().getExtras().get("data"));
                    }else{
                        try {
                            saveImage(MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), result.getData().getData()));
                        } catch (IOException e) {
                            System.out.println("Error handling media files");
                        }
                    }

                }
            });

}
