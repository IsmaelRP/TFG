package project.tfg.ecgscan.ui.home;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.facebook.FacebookSdk.getCacheDir;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.model.AspectRatio;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import project.tfg.ecgscan.R;
import project.tfg.ecgscan.base.MsgDialog;
import project.tfg.ecgscan.base.YesNoDialog;
import project.tfg.ecgscan.data.ElectroImage;
import project.tfg.ecgscan.data.Event;
import project.tfg.ecgscan.data.RepositoryImpl;
import project.tfg.ecgscan.data.local.AppDatabase;
import project.tfg.ecgscan.data.local.model.Electro;
import project.tfg.ecgscan.databinding.FragmentHomeBinding;
import project.tfg.ecgscan.ui.secondActivity.SecondActivityViewModel;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding b;
    private Bitmap image;
    private SecondActivityViewModel secondVM;
    private HomeFragmentViewmodel vm;
    private NavController navController;
    private boolean cloud;
    private ProgressDialog progressDialog;

    private FirebaseStorage storage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = FragmentHomeBinding.inflate(inflater, container, false);
        return b.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews(view);
    }


    private void setupViews(View view) {
        b.txtDiagnosisData.setMovementMethod(new ScrollingMovementMethod());
        navController = Objects.requireNonNull(Navigation.findNavController(view));

        secondVM = ViewModelProviders.of(requireActivity()).get(SecondActivityViewModel.class);
        secondVM.getNavigateToList().observe(getViewLifecycleOwner(), this::navigateToTabs);
        b.btnScan.setOnClickListener(v -> dispatchTakePictureIntent(MediaStore.ACTION_IMAGE_CAPTURE));
        b.btnUpload.setOnClickListener(v -> dispatchSelectPictureIntent());

        secondVM.getFirebaseStorage().observe(getViewLifecycleOwner(), this::updateStorage);
        secondVM.getCrop().observe(getViewLifecycleOwner(), v -> {
            if(v.getContentIfNotHandled() != null){
                startImageFunctionality(v.peekContent());
            }

        });

        vm = ViewModelProviders.of(this,
                        new HomeFragmentViewModelFactory(requireActivity().getApplication(), new RepositoryImpl(
                                AppDatabase.getInstance(requireContext().getApplicationContext()).electroDao())))
                .get(HomeFragmentViewmodel.class);

        //vm.getInsertResult().observe(getViewLifecycleOwner(), v -> startImageFunctionality(image));

        secondVM.getElectroObservable().observe(getViewLifecycleOwner(), v -> {
            updateUI(v);
            startImageFunctionality(v.peekContent().getImage());
        });

        vm.getDiagnoseResponse().observe(getViewLifecycleOwner(), v -> updateDiagnose(v));

        //String url = "http://IP_EXTERNA:PUERTO/API/";
        String url = "http://10.0.2.2:9910/Test/";

        // PARA INTERCEPTAR Y TESTEAR
        /*
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        // Configurar nivel de logging deseado
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Añadir interceptor a OkHttp
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.addInterceptor(logging);
        OkHttpClient client = httpClientBuilder.build();
         */
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                //.client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        vm.setApiService(retrofit);
    }

    private void updateDiagnose(Event<String> diag) {
        if (!diag.hasBeenHandled()) {
            b.txtDiagnosisData.setText(diag.getContentIfNotHandled());
            progressDialog.dismiss();
            //b.imageView.setImageBitmap(image);
        }

    }


    private void startImageFunctionality(Bitmap image) {
        this.image = image;
        progressDialog = new ProgressDialog(requireContext());
        progressDialog.setMessage("Diagnosing the image...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        vm.diagnoseImage(image);
    }


    private void updateUI(Event<ElectroImage> electro) {
        electro.getContentIfNotHandled();

        image = electro.peekContent().getImage();

        b.txtDiagnosis.setVisibility(View.VISIBLE);
        b.txtDiagnosisData.setVisibility(View.VISIBLE);
        b.txtEmptyDiag.setVisibility(View.INVISIBLE);

        if (!electro.peekContent().getName().isEmpty()) {
            b.txtName.setVisibility(View.VISIBLE);
            b.txtDate.setVisibility(View.VISIBLE);

            b.txtNameData.setVisibility(View.VISIBLE);
            b.txtDateData.setVisibility(View.VISIBLE);

            b.txtNameData.setText(electro.peekContent().getName());
            b.txtDateData.setText(electro.peekContent().getDate());
        }

    }

    private void updateStorage(Event<FirebaseStorage> firebaseStorageEvent) {
        storage = firebaseStorageEvent.peekContent();
    }

    private void navigateToTabs(Event<Boolean> booleanEvent) {
        if (booleanEvent.getContentIfNotHandled() != null) {

            if (FirebaseAuth.getInstance().getCurrentUser().isAnonymous()) {
                Toast.makeText(getContext(), "Error, anonymous users cannot access to lists", Toast.LENGTH_LONG).show();
            } else {
                navController.navigate(HomeFragmentDirections.desHomeToTabs());
            }

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
                    Uri uriCrop = null;

                    if (result.getData().getExtras() != null) {
                        image = (Bitmap) result.getData().getExtras().get("data");

                        FileOutputStream outputStream = null;

                        try {
                            File internalStorageDir = requireActivity().getFilesDir();

                            String fileName = "crop.jpg";
                            File tempFile = new File(internalStorageDir, fileName);

                            outputStream = new FileOutputStream(tempFile);

                            // Comprime y guarda el bitmap en el archivo
                            image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

                            uriCrop = Uri.fromFile(tempFile);
                        } catch (FileNotFoundException e) {
                            Toast.makeText(getContext(), "Error cropping image (file exception)", Toast.LENGTH_LONG).show();
                        }finally {
                            // Cierra el FileOutputStream
                            if (outputStream != null){
                                try {
                                    outputStream.close();
                                } catch (IOException e) {
                                    Toast.makeText(getContext(), "Error cropping image (file exception)", Toast.LENGTH_LONG).show();
                                }
                            }
                        }


                        if (!FirebaseAuth.getInstance().getCurrentUser().isAnonymous()) {
                            launchYesNoDialog();
                        }

                    } else {
                        try {
                            image = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), result.getData().getData());
                            uriCrop = result.getData().getData();

                            if (!FirebaseAuth.getInstance().getCurrentUser().isAnonymous()) {
                                launchYesNoDialog();
                            }

                        } catch (IOException e) {
                            System.out.println("Error handling media files");
                        }
                    }

                    startCropActivity(uriCrop);

                    Date date = new Date();
                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");
                    //secondVM.setElectroObservable(new ElectroImage(image, "", df.format(date)));
                    updateUI(new Event(new ElectroImage(image, "", df.format(date))));
                }
            });


    private void startCropActivity(Uri sourceUri) {
        String destinationFileName = "cropped_image.jpg"; // Nombre del archivo de imagen recortada

        // Obtener la ruta de los archivos internos de la aplicación
        File internalStorageDir = requireActivity().getFilesDir();
        File destinationFile = new File(internalStorageDir, destinationFileName);
        Uri destinationUri = Uri.fromFile(destinationFile);

        UCrop.Options options = new UCrop.Options();
        options.setCompressionQuality(80); // Configura la calidad de compresión de la imagen recortada (0-100)
        options.setToolbarColor(ContextCompat.getColor(requireContext(), R.color.splashbg)); // Personaliza el color de la barra de herramientas de UCrop
        options.setFreeStyleCropEnabled(true);

        options.setAspectRatioOptions(0,
                new AspectRatio("10:2", 10.0f, 2.0f),
                new AspectRatio("20:2", 20.0f, 2.0f),
                new AspectRatio("30:2", 30.0f, 2.0f),
                new AspectRatio("40:2", 40.0f, 2.0f),
                new AspectRatio("50:2", 50.0f, 2.0f));


        UCrop.of(sourceUri, destinationUri)
                .withOptions(options)
                .start(requireActivity());
    }


    private void launchYesNoDialog() {
        String msg;
        cloud = secondVM.getPreferencesCloud().getValue().peekContent();

        if (cloud) {
            msg = "Do you want to upload the image to the cloud?";
        } else {
            msg = "Do you want to save the image on your phone?";
        }


        YesNoDialog dialog = YesNoDialog.newInstance(msg);
        dialog.setOnClickListener(new OnOkClickListener() {
            @Override
            public void onOkClick() {
                launchNameTextDialog();
            }
        });
        dialog.show(getChildFragmentManager(), "YesNoDialog");
    }


    private void launchNameTextDialog() {
        MsgDialog dialog = MsgDialog.newInstance();
        dialog.setOnClickListener(new OnButtonClickListener() {
            @Override
            public void onButtonClick(String title) {
                saveImage(title);
            }
        });
        dialog.show(getChildFragmentManager(), "MsgDialog");


    }

    private void saveImage(String filename) {
        if (cloud) {
            uploadImageToFirebase(filename);
        } else {
            saveToLocal(filename);
        }
    }

    private void saveToLocal(String name) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();

        Bitmap aux = image;

        vm.insertElectro(new Electro(0, image, name, dtf.format(now)));
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
