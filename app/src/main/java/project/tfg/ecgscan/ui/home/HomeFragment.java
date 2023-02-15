package project.tfg.ecgscan.ui.home;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import project.tfg.ecgscan.R;
import project.tfg.ecgscan.data.Event;
import project.tfg.ecgscan.databinding.FragmentHomeBinding;
import project.tfg.ecgscan.ui.secondActivity.SecondActivityViewModel;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding b;
    private Bitmap image;
    private SecondActivityViewModel secondVM;
    private NavController navController;
    private boolean cloud;

    private FirebaseStorage storage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = FragmentHomeBinding.inflate(inflater, container, false);
        return b.getRoot();
    }


    private void startImageFunctionality(Bitmap image) {
        b.imageView.setImageBitmap(image);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews(view);
    }


    private void setupViews(View view) {
        navController = Objects.requireNonNull(Navigation.findNavController(view));
        secondVM = ViewModelProviders.of(requireActivity()).get(SecondActivityViewModel.class);
        secondVM.getNavigateToList().observe(getViewLifecycleOwner(), this::navigateToTabs);
        b.btnScan.setOnClickListener(v -> dispatchTakePictureIntent(MediaStore.ACTION_IMAGE_CAPTURE));
        b.btnUpload.setOnClickListener(v -> dispatchSelectPictureIntent());

        //updateStorage(secondVM.getFirebaseStorage().getValue());
        secondVM.getFirebaseStorage().observe(getViewLifecycleOwner(), this::updateStorage);
    }

    private void updateStorage(Event<FirebaseStorage> firebaseStorageEvent) {
        storage = firebaseStorageEvent.peekContent();
    }

    private void navigateToTabs(Event<Boolean> booleanEvent) {
        if (booleanEvent.getContentIfNotHandled() != null){
            navController.navigate(HomeFragmentDirections.desHomeToTabs());
        }
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

                    if (result.getData().getExtras() != null) {
                        image = (Bitmap) result.getData().getExtras().get("data");
                        startImageFunctionality(image);
                        launchYesNoDialog(image);
                    } else {
                        try {
                            image = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), result.getData().getData());
                            startImageFunctionality(image);
                            launchYesNoDialog(image);
                        } catch (IOException e) {
                            System.out.println("Error handling media files");
                        }
                    }
                }
            });

    private final DialogInterface.OnClickListener dialogClickListener = (dialog, which) -> {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                launchNameTextDialog();
                break;

            case DialogInterface.BUTTON_NEGATIVE:
                //  nothing for now
                break;
        }
    };

    private void launchYesNoDialog(Bitmap image) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        String msg;
        cloud = secondVM.getPreferencesCloud().getValue().peekContent();

        if (cloud){
            msg = "Do you want to upload the image to the cloud?";
        }else{
            msg = "Do you want to save the image on your phone?";
        }
        builder.setMessage(msg)
                .setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener)
                .show();
    }

    private void launchNameTextDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Fill in the name of the electrocardiogram");

        final EditText input = new EditText(requireContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> saveImage(input.getText().toString()));
        builder.setNegativeButton("Cancel", (dialog, which) -> diagnoseImg(dialog));

        builder.show();
    }

    private void diagnoseImg(DialogInterface dialog) {
        //TODO: llamar al viewmodel con la img y mostrar diagnóstico en la pantalla home de manera "temporal"
        // image
        dialog.cancel();
    }

    private void saveImage(String filename){

        if (cloud){
            uploadImageToFirebase(filename);
        }else{
            loadToLocal(filename);
        }
    }

    private void loadToLocal(String filename) {
        //TODO: almacenar imagen en local
    }

    private void uploadImageToFirebase(String fileName) {
        StorageReference storageRef = storage.getReference("imgs/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + fileName);

        secondVM.setFirebaseStorage(storage);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageMetadata meta = new StorageMetadata.Builder()
                .setContentType("image/jpg")
                .setCustomMetadata("name", fileName)
                .setCustomMetadata("date", DateTimeFormatter.ofPattern("dd/MM/uuuu").format(LocalDate.now()))
                .setCustomMetadata("description", "DESCRIPTION TEST")
                .build();

        UploadTask uploadTask = storageRef.putBytes(data, meta);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
            }
        });

    }


}
