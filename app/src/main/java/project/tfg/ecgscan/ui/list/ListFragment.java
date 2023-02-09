package project.tfg.ecgscan.ui.list;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    private final long ONE_MEGABYTE = 1024 * 1024;
    private ListFragmentAdapter listAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static ListFragment newInstance() {
        return new ListFragment();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupViews(view);
        loadItems();

        //loadList();

    }

    private void loadItems() {
        storageRef.listAll()
                .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                    @Override
                    public void onSuccess(ListResult listResult) {

                        for (StorageReference item : listResult.getItems()) {
                            loadImage(item);
                        }
                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Uh-oh, an error occurred!
                    }
                });
    }

    private void loadImage(StorageReference item) {
        item.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                //TODO mandar imagen a recyclerview

                ElectroImage e = new ElectroImage(BitmapFactory.decodeByteArray(bytes, 0, bytes.length), item.getName());

                secondVM.addImage(e);

                /*Task<StorageMetadata> p = item.getMetadata();
                p.addOnSuccessListener(storageMetadata -> {
                    StorageMetadata xdwa = storageMetadata;
                });

                 */

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


    private void setupViews(View view) {
        secondVM = ViewModelProviders.of(requireActivity()).get(SecondActivityViewModel.class);

        updateStorage(secondVM.getFirebaseStorage().getValue());
        updateStorageReference(secondVM.getStorageReference().getValue());

        secondVM.setListElectros(new ArrayList<>());

        secondVM.getFirebaseStorage().observe(getViewLifecycleOwner(), this::updateStorage);
        secondVM.getStorageReference().observe(getViewLifecycleOwner(), this::updateStorageReference);

        secondVM.getListElectros().observe(getViewLifecycleOwner(), this::updateList);

        listAdapter = new ListFragmentAdapter(secondVM);

        b.lstList.setHasFixedSize(true);
        b.lstList.setLayoutManager(new GridLayoutManager(requireContext(), 1));
        b.lstList.setItemAnimator(new DefaultItemAnimator());
        b.lstList.setAdapter(listAdapter);
    }

    private void updateList(Event<List<ElectroImage>> listEvent) {
        List<ElectroImage> list = listEvent.peekContent();
        Collections.sort(list);
        listAdapter.submitList(list);
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
