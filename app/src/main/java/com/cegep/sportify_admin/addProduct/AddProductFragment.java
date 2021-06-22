package com.cegep.sportify_admin.addProduct;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
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
import com.cegep.sportify_admin.model.AddProductRequest;
import com.esafirm.imagepicker.features.ImagePickerConfig;
import com.esafirm.imagepicker.features.ImagePickerLauncher;
import com.esafirm.imagepicker.features.ImagePickerLauncherKt;
import com.esafirm.imagepicker.features.ImagePickerMode;
import com.esafirm.imagepicker.features.ImagePickerSavePath;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public class AddProductFragment extends Fragment {

    private ImagePickerLauncher imagepickerLauncher = null;

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

    private Button addProductButton;

    private List<Image> images = new ArrayList<>();
    private final AddProductRequest addProductRequest = new AddProductRequest();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupImagePager(view);
        setupChooseProductImages(view);
        setupProductNameInput(view);
        setupPriceInput(view);
        setupSaleEditText(view);
        setupCategories(view);
        setupSubCategories(view);
        setupDescription(view);
        setupSizesInput(view);
        setupColorPicker(view);
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

    private void setupChooseProductImages(View view) {
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
        nameEditText = view.findViewById(R.id.name_editText);
        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addProductRequest.setProductName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupPriceInput(View view) {
        priceEditText = view.findViewById(R.id.price_editText);
        priceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addProductRequest.setProductPrice(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupSaleEditText(View view) {
        saleEditText = view.findViewById(R.id.sale_editText);
        saleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    addProductRequest.setSale(0);
                } else {
                    addProductRequest.setSale(Integer.parseInt(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupCategories(View view) {
        List<String> categories = new ArrayList<>();
        categories.add("Footwear");
        categories.add("Tops");
        categories.add("Bottoms");
        categories.add("Swimwear");
        categories.add("Outerwear");
        categories.add("Headwear");

        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, categories);
        AutoCompleteTextView categoryTextView = view.findViewById(R.id.category_textView);
        categoryTextView.setAdapter(categoriesAdapter);

        categoryTextView.setOnItemClickListener((parent, view1, position, id) -> {
            String category = categories.get(position);
            addProductRequest.setCategory(category);
        });
    }

    private void setupSubCategories(View view) {
        List<String> subCategories = new ArrayList<>();
        subCategories.add("Men's");
        subCategories.add("Womens'");

        ArrayAdapter<String> subCategoriesAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1,
                                                                       subCategories);
        AutoCompleteTextView subCategoryTextView = view.findViewById(R.id.sub_category_textView);
        subCategoryTextView.setAdapter(subCategoriesAdapter);

        subCategoryTextView.setOnItemClickListener((parent, view1, position, id) -> {
            String subCategory = subCategories.get(position);
            addProductRequest.setSubCategory(subCategory);
        });
    }

    private void setupDescription(View view) {
        descriptionEditText = view.findViewById(R.id.description_editText);
        descriptionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                addProductRequest.setDescription(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupSizesInput(View view) {
        xSmallEditText = view.findViewById(R.id.x_small_text);
        xSmallEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    addProductRequest.setxSmallSize(0);
                } else {
                    addProductRequest.setxSmallSize(Integer.parseInt(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        smallEditText = view.findViewById(R.id.small_text);
        smallEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    addProductRequest.setSmallSize(0);
                } else {
                    addProductRequest.setSmallSize(Integer.parseInt(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mediumEditText = view.findViewById(R.id.medium_text);
        mediumEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    addProductRequest.setMediumSize(0);
                } else {
                    addProductRequest.setMediumSize(Integer.parseInt(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        largeEditText = view.findViewById(R.id.large_text);
        largeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    addProductRequest.setLargeSize(0);
                } else {
                    addProductRequest.setLargeSize(Integer.parseInt(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        xLargeEditText = view.findViewById(R.id.x_large_text);
        xLargeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    addProductRequest.setxLargeSize(0);
                } else {
                    addProductRequest.setxLargeSize(Integer.parseInt(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupColorPicker(View view) {
        ChipGroup chipGroup = view.findViewById(R.id.available_color_group);
        view.findViewById(R.id.add_color_chip).setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
            builder.setTitle(R.string.choose_color);
            builder.setMessage("Enter hex color in the form #RRGGBB");

            View view1 = LayoutInflater.from(requireContext()).inflate(R.layout.color_input_layout, null, false);
            EditText inputEditText = view1.findViewById(R.id.input_text);
            builder.setView(view1);

            builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
                String hexColor = "#" + inputEditText.getText().toString();
                Chip chip = (Chip) LayoutInflater.from(requireContext()).inflate(R.layout.color_chip, chipGroup, false);
                try {
                    int color = Color.parseColor(hexColor);
                    chip.setChipBackgroundColor(ColorStateList.valueOf(color));
                    if (!addProductRequest.hasColor(hexColor)) {
                        int numOfChildren = chipGroup.getChildCount();
                        chipGroup.addView(chip, numOfChildren - 1);
                        addProductRequest.addColor(hexColor);
                    } else {
                        Toast.makeText(requireContext(), "Color already added", Toast.LENGTH_SHORT).show();
                    }
                } catch (IllegalArgumentException e) {
                    Toast.makeText(requireContext(), "Invalid color", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton(android.R.string.cancel, null);
            AlertDialog dialog = builder.create();
            Window window = dialog.getWindow();
            if (window != null) {
                window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
            }
            dialog.show();
        });
    }

    private void setupAddButtonClick(View view) {
        addProductButton = view.findViewById(R.id.add_product_button);
        addProductButton.setOnClickListener(v -> {
            if (addProductRequest.isValid(requireContext())) {
                if (images.isEmpty()) {
                    addProduct();
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

            addProductRequest.setImages(imageUrls);
            addProduct();
        }).addOnFailureListener(e -> Toast.makeText(requireContext(), "Failed to upload images", Toast.LENGTH_SHORT).show());
    }

    private void addProduct() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference productsReference = databaseReference.child("Brand").child("Products");
        String productId = productsReference.push().getKey();
        DatabaseReference productReference = productsReference.child(productId);
        productReference.setValue(addProductRequest);
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
