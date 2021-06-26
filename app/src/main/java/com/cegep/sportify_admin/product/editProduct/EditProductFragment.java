package com.cegep.sportify_admin.product.editProduct;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.cegep.sportify_admin.R;
import com.cegep.sportify_admin.gallery.ImageAdapter;
import com.cegep.sportify_admin.home.ProductsListFragment;
import com.cegep.sportify_admin.model.Product;
import com.esafirm.imagepicker.features.ImagePickerConfig;
import com.esafirm.imagepicker.features.ImagePickerLauncher;
import com.esafirm.imagepicker.features.ImagePickerLauncherKt;
import com.esafirm.imagepicker.features.ImagePickerMode;
import com.esafirm.imagepicker.features.ImagePickerSavePath;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class EditProductFragment extends Fragment {

    private ViewPager viewPager;
    private WormDotsIndicator dotsIndicator;
    private ImageView addImageBackground;
    private ImageView addImagePlaceholder;
    private TextView addImageText;
    private TextView addImageDirectionsText;

    private EditText nameEditText;
    private EditText priceEditText;
    private EditText saleEditText;
    private EditText descriptionEditText;
    private EditText xSmallEditText;
    private EditText smallEditText;
    private EditText mediumEditText;
    private EditText largeEditText;
    private EditText xLargeEditText;
    FirebaseDatabase fdb;

    private Button editProductButton;

    private ImagePickerLauncher imagepickerLauncher = null;
    private List<Image> images = new ArrayList<>();
    private final Product product = new Product(ProductsListFragment.selectedProduct);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupImagePager(view);
        setupChooseProductImages(view);
        editPriceInput(view);
        editSaleEditText(view);
        editDescription(view);
        editSizesInput(view);
        setupColorPicker(view);
        editButtonClick(view);
    }

    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        imagepickerLauncher = ImagePickerLauncherKt.registerImagePicker(this, images -> {
            if (images == null || images.isEmpty()) {
                return null;
            }

            this.images = images;

            ImageAdapter viewPagerAdapter = new ImageAdapter(getChildFragmentManager(), images);
            viewPager.setAdapter(viewPagerAdapter);
            dotsIndicator.setViewPager(viewPager);

            viewPager.setVisibility(View.VISIBLE);
            dotsIndicator.setVisibility(View.VISIBLE);
            setChooseImageVisibility(View.GONE);
            return null;
        });
    }

    private void pickImage() {
        ImagePickerConfig config = new ImagePickerConfig(ImagePickerMode.MULTIPLE, "Folder", "Tap to select", "DONE", 0, 4, 0, true, false, false,
                false, false,
                new ArrayList<>(), new ArrayList<>(), new ImagePickerSavePath("Camera", true),
                ReturnMode.NONE, false, true);
        imagepickerLauncher.launch(config);

    }

    private void setupImagePager(View view) {
        viewPager = view.findViewById(R.id.viewPager);
        dotsIndicator = view.findViewById(R.id.dots_indicator);
    }

    private void setupChooseProductImages(View view) {
        addImageBackground = view.findViewById(R.id.add_image_background);
        addImagePlaceholder = view.findViewById(R.id.add_image_placeholder);
        addImageText = view.findViewById(R.id.edit_image_text);
        addImageDirectionsText = view.findViewById(R.id.add_image_directions_text);

        View.OnClickListener onClickListener = v -> pickImage();
        addImageBackground.setOnClickListener(onClickListener);
        addImagePlaceholder.setOnClickListener(onClickListener);
        addImageText.setOnClickListener(onClickListener);
        addImageDirectionsText.setOnClickListener(onClickListener);
    }


    private void uploadImages() {
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        List<String> imageUrls = new ArrayList<>();
        List<String> uuids = new ArrayList<>();

        List<UploadTask> uploadTasks = new ArrayList<>();

        for (Image image : images) {
            try {
                String uuid = getUniqueId();
                uuids.add(uuid);

                InputStream stream = new FileInputStream(new File(image.getPath()));
                StorageReference child = storageReference.child(uuid);

                UploadTask uploadTask = child.putStream(stream);
                uploadTasks.add(uploadTask);
            } catch (Exception e) {
                Toast.makeText(requireContext(), "Failed to upload images", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        Task<List<Task<?>>> uploadTask = Tasks.whenAllSuccess(uploadTasks);
        uploadTask.continueWithTask(task -> {
            List<Task<Uri>> downloadUrlTasks = new ArrayList<>();
            for (String uuid : uuids) {
                StorageReference child = storageReference.child(uuid);
                downloadUrlTasks.add(child.getDownloadUrl());
            }

            return Tasks.whenAllSuccess(downloadUrlTasks);
        }).addOnSuccessListener(objects -> {
            for (Object object : objects) {
                imageUrls.add(object.toString());
            }

            product.setImages(imageUrls);
            editProduct();
        }).addOnFailureListener(e -> Toast.makeText(requireContext(), "Failed to upload images", Toast.LENGTH_SHORT).show());
    }

}
