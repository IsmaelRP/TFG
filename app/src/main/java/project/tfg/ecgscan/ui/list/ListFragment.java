package project.tfg.ecgscan.ui.list;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import project.tfg.ecgscan.R;
import project.tfg.ecgscan.data.ElectroImage;
import project.tfg.ecgscan.data.Event;
import project.tfg.ecgscan.databinding.FragmentListBinding;
import project.tfg.ecgscan.ui.secondActivity.SecondActivityViewModel;

public class ListFragment extends Fragment {

    private NavController navController;
    private FragmentListBinding b;
    private SecondActivityViewModel secondVM;

    private FirebaseStorage storage;
    private StorageReference storageRef;

    private Toolbar toolbar;

    private final long ONE_MEGABYTE = 1024 * 1024;
    private ListFragmentAdapter listAdapter;

    private int aux = 0;

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews();
        secondVM.clearImages();

    }


    @Override
    public void onResume() {
        super.onResume();
        loadItems();
    }

    private void loadItems() {
        secondVM.clearImages();
        storageRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {

                        for (StorageReference item : listResult.getItems()) {
                            loadImage(item);
                        }
                    }
                });
    }

    private void loadImage(StorageReference item) {
        item.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                item.getMetadata().addOnSuccessListener(new OnSuccessListener<StorageMetadata>() {
                    @Override
                    public void onSuccess(StorageMetadata storageMetadata) {
                        Date date = new Date(storageMetadata.getCreationTimeMillis());
                        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy");

                        ElectroImage img = new ElectroImage(BitmapFactory.decodeByteArray(bytes, 0, bytes.length), storageMetadata.getName(), df.format(date));
                        secondVM.addImage(img);

                    }
                }).addOnCompleteListener(new OnCompleteListener<StorageMetadata>() {
                    @Override
                    public void onComplete(@NonNull Task<StorageMetadata> task) {
                        secondVM.setListElectros();
                    }
                });



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                //TODO error
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        b = FragmentListBinding.inflate(inflater, container, false);
        return b.getRoot();
    }


    private void setupViews() {
        secondVM = ViewModelProviders.of(requireActivity()).get(SecondActivityViewModel.class);

        updateStorage(secondVM.getFirebaseStorage().getValue());
        updateStorageReference(secondVM.getStorageReference().getValue());

        secondVM.getFirebaseStorage().observe(getViewLifecycleOwner(), this::updateStorage);
        secondVM.getStorageReference().observe(getViewLifecycleOwner(), this::updateStorageReference);

        secondVM.getListElectros().observe(getViewLifecycleOwner(), this::updateList);

        listAdapter = new ListFragmentAdapter(secondVM);

        b.lstList.setHasFixedSize(true);
        b.lstList.setLayoutManager(new LinearLayoutManager(requireContext()));
        b.lstList.setItemAnimator(new DefaultItemAnimator());
        b.lstList.setAdapter(listAdapter);


        toolbar = Objects.requireNonNull(requireActivity().findViewById(R.id.main_toolbar));

        SearchView searchView = (SearchView) toolbar.getMenu().findItem(R.id.menu_search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                listAdapter.getFilter().filter(newText);
                return true;
            }
        });
    }

    private void updateList(Event<List<ElectroImage>> listEvent) {
        List<ElectroImage> list = listEvent.peekContent();
        Collections.sort(list);
        listAdapter.submitList(list);
        listAdapter.notifyDataSetChanged();
        b.lblEmptyList.setVisibility(list.isEmpty() ? View.VISIBLE : View.INVISIBLE);
        //b.swRefreshFoods.setRefreshing(false);
    }

    private void updateStorageReference(Event<StorageReference> storageReferenceEvent) {
        storageRef = storageReferenceEvent.peekContent();
    }

    private void updateStorage(Event<FirebaseStorage> firebaseStorageEvent) {
        storage = firebaseStorageEvent.peekContent();
    }


}
