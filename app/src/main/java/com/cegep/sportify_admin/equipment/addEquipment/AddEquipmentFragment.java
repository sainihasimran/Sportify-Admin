package com.cegep.sportify_admin.equipment.addEquipment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.cegep.sportify_admin.SportifyAdminApp;
import com.cegep.sportify_admin.Utils;
import com.cegep.sportify_admin.gallery.ImageAdapter;
import com.cegep.sportify_admin.model.Equipment;
import com.cegep.sportify_admin.model.SportWithTeams;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public class AddEquipmentFragment extends Fragment {

    private ViewPager viewPager;
    private WormDotsIndicator dotsIndicator;
    private ImageView addImageBackground;
    private ImageView addImagePlaceholder;
    private TextView addImageText;
    private TextView addImageDirectionsText;

    private ImagePickerLauncher imagepickerLauncher = null;

    private List<Image> images = new ArrayList<>();

    private final Equipment equipment = new Equipment();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_equipment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupImagePager(view);
        setupChooseEquipmentImages(view);
        setupProductNameInput(view);
        setupProductPriceInput(view);
        setupSaleInput(view);
        setupSportInput(view);
        setupProductDescriptionInput(view);
        setupStockInput(view);
        setupAddButtonClick(view);
    }

    @Override
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

    private void setupImagePager(View view) {
        viewPager = view.findViewById(R.id.viewPager);
        dotsIndicator = view.findViewById(R.id.dots_indicator);
    }

    private void setupChooseEquipmentImages(View view) {
        addImageBackground = view.findViewById(R.id.add_image_background);
        addImagePlaceholder = view.findViewById(R.id.add_image_placeholder);
        addImageText = view.findViewById(R.id.add_image_text);
        addImageDirectionsText = view.findViewById(R.id.add_image_directions_text);

        View.OnClickListener onClickListener = v -> pickImage();
        addImageBackground.setOnClickListener(onClickListener);
        addImagePlaceholder.setOnClickListener(onClickListener);
        addImageText.setOnClickListener(onClickListener);
        addImageDirectionsText.setOnClickListener(onClickListener);
    }

    private void setupProductNameInput(View view) {
        EditText nameEditText = view.findViewById(R.id.name_editText);
        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                equipment.setEquipmentName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupProductPriceInput(View view) {
        EditText priceEditText = view.findViewById(R.id.price_editText);
        priceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                equipment.setEquipmentPrice(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupSaleInput(View view) {
        EditText saleEditText = view.findViewById(R.id.sale_editText);
        saleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    equipment.setSale(0);
                } else {
                    equipment.setSale(Integer.parseInt(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupSportInput(View view) {
        EditText sportEditText = view.findViewById(R.id.sport_editText);
        sportEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                equipment.setSport(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupProductDescriptionInput(View view) {
        EditText descriptionEditText = view.findViewById(R.id.description_editText);
        descriptionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                equipment.setDescription(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupStockInput(View view) {
        EditText stockEditText = view.findViewById(R.id.stock_text);
        stockEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    equipment.setStock(0);
                } else {
                    equipment.setStock(Integer.parseInt(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupAddButtonClick(View view) {
        Button addEquipmentButton = view.findViewById(R.id.add_product_button);
        addEquipmentButton.setOnClickListener(v -> {
            if (equipment.isValid(requireContext())) {
                if (images.isEmpty()) {
                    addEquipment();
                } else {
                    uploadImages();
                }
            }
        });
    }

    private void pickImage() {
        ImagePickerConfig config = new ImagePickerConfig(ImagePickerMode.MULTIPLE, "Folder", "Tap to select", "DONE", 0, 4, 0, true, false, false,
                                                         false, false,
                                                         new ArrayList<>(), new ArrayList<>(), new ImagePickerSavePath("Camera", true),
                                                         ReturnMode.NONE, false, true);
        imagepickerLauncher.launch(config);

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

            equipment.setImages(imageUrls);
            addEquipment();
        }).addOnFailureListener(e -> Toast.makeText(requireContext(), "Failed to upload images", Toast.LENGTH_SHORT).show());
    }

    private void addEquipment() {
        String currentSport = equipment.getSport().toLowerCase();
        DatabaseReference sportsReference = Utils.getSportWithTeamsReference().child(currentSport);
        sportsReference.get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Toast.makeText(requireContext(), "Failed to add an equipment", Toast.LENGTH_SHORT).show();
            } else {
                Object response = null;
                DataSnapshot result = task.getResult();
                if (result != null && result.getValue() != null) {
                    response = result.getValue();
                }

                boolean updateSports = response == null;

                DatabaseReference equipmentsReference = Utils.getEquipmentsReference();
                String equipmentId = equipmentsReference.push().getKey();
                DatabaseReference productReference = equipmentsReference.child(equipmentId);
                equipment.setEquipmentId(equipmentId);
                equipment.setCreatedAt(System.currentTimeMillis());
                equipment.setAdminId(SportifyAdminApp.admin.adminId);
                if (equipment.isOnSale()) {
                    float salePrice = equipment.getPrice() - ((equipment.getPrice() * equipment.getSale()) / 100);
                    equipment.setSalePrice(salePrice);
                }
                productReference.setValue(equipment);
                if (updateSports) {
                    SportWithTeams sportWithTeams = new SportWithTeams();
                    sportWithTeams.setSport(currentSport);
                    sportWithTeams.setTeams(Collections.emptyList());
                    sportsReference.setValue(sportWithTeams);
                }
                requireActivity().finish();
            }
        });
    }

    private void setChooseImageVisibility(int visibility) {
        addImageBackground.setVisibility(visibility);
        addImagePlaceholder.setVisibility(visibility);
        addImageText.setVisibility(visibility);
        addImageDirectionsText.setVisibility(visibility);
    }

    private String getUniqueId() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
