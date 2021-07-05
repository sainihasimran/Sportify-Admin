package com.cegep.sportify_admin.product.addProduct;

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
import com.cegep.sportify_admin.Constants;
import com.cegep.sportify_admin.R;
import com.cegep.sportify_admin.SportifyAdminApp;
import com.cegep.sportify_admin.Utils;
import com.cegep.sportify_admin.gallery.ImageAdapter;
import com.cegep.sportify_admin.model.Product;
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
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.jetbrains.annotations.NotNull;

public class AddProductFragment extends Fragment {

    private ViewPager viewPager;
    private WormDotsIndicator dotsIndicator;
    private ImageView addImageBackground;
    private ImageView addImagePlaceholder;
    private TextView addImageText;
    private TextView addImageDirectionsText;

    private ImagePickerLauncher imagepickerLauncher = null;
    private List<Image> images = new ArrayList<>();
    private final Product product = new Product();

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
        setupSportInput(view);
        setupTeamInput(view);
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
        EditText nameEditText = view.findViewById(R.id.name_editText);
        nameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                product.setProductName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupPriceInput(View view) {
        EditText priceEditText = view.findViewById(R.id.price_editText);
        priceEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                product.setProductPrice(s.toString());
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
                product.setSport(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupTeamInput(View view) {
        EditText teamEditText = view.findViewById(R.id.team_editText);
        teamEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                product.setTeam(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupSaleEditText(View view) {
        EditText saleEditText = view.findViewById(R.id.sale_editText);
        saleEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    product.setSale(0);
                } else {
                    product.setSale(Integer.parseInt(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupCategories(View view) {
        List<String> categories = Arrays.asList(Constants.CATEGORIES);

        ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, categories);
        AutoCompleteTextView categoryTextView = view.findViewById(R.id.category_textView);
        categoryTextView.setAdapter(categoriesAdapter);

        categoryTextView.setOnItemClickListener((parent, view1, position, id) -> {
            String category = categories.get(position);
            product.setCategory(category);
        });
    }

    private void setupSubCategories(View view) {
        List<String> subCategories = Arrays.asList(Constants.SUB_CATEGORIES);

        ArrayAdapter<String> subCategoriesAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1,
                                                                       subCategories);
        AutoCompleteTextView subCategoryTextView = view.findViewById(R.id.sub_category_textView);
        subCategoryTextView.setAdapter(subCategoriesAdapter);

        subCategoryTextView.setOnItemClickListener((parent, view1, position, id) -> {
            String subCategory = subCategories.get(position);
            product.setSubCategory(subCategory);
        });
    }

    private void setupDescription(View view) {
        EditText descriptionEditText = view.findViewById(R.id.description_editText);
        descriptionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                product.setDescription(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupSizesInput(View view) {
        EditText xSmallEditText = view.findViewById(R.id.x_small_text);
        xSmallEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    product.setxSmallSize(0);
                } else {
                    product.setxSmallSize(Integer.parseInt(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        EditText smallEditText = view.findViewById(R.id.small_text);
        smallEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    product.setSmallSize(0);
                } else {
                    product.setSmallSize(Integer.parseInt(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        EditText mediumEditText = view.findViewById(R.id.medium_text);
        mediumEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    product.setMediumSize(0);
                } else {
                    product.setMediumSize(Integer.parseInt(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        EditText largeEditText = view.findViewById(R.id.large_text);
        largeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    product.setLargeSize(0);
                } else {
                    product.setLargeSize(Integer.parseInt(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        EditText xLargeEditText = view.findViewById(R.id.x_large_text);
        xLargeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    product.setxLargeSize(0);
                } else {
                    product.setxLargeSize(Integer.parseInt(s.toString()));
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
                    if (!product.hasColor(hexColor)) {
                        int numOfChildren = chipGroup.getChildCount();
                        chipGroup.addView(chip, numOfChildren - 1);
                        product.addColor(hexColor);
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
        Button addProductButton = view.findViewById(R.id.add_product_button);
        addProductButton.setOnClickListener(v -> {
            if (product.isValid(requireContext())) {
                if (images.isEmpty()) {
                    checkSportExistence();
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

            product.setImages(imageUrls);
            checkSportExistence();
        }).addOnFailureListener(e -> Toast.makeText(requireContext(), "Failed to upload images", Toast.LENGTH_SHORT).show());
    }

    private void checkSportExistence() {
        if (product.hasSport()) {
            final String currentSport = product.getSport().toLowerCase();
            final String currentTeam = product.getTeam().toLowerCase();
            DatabaseReference sportWithTeamsReference = Utils.getSportWithTeamsReference().child(currentSport);
            sportWithTeamsReference.get().addOnCompleteListener(task -> {
                if (!task.isSuccessful()) {
                    Toast.makeText(requireContext(), "Failed to add product", Toast.LENGTH_SHORT).show();
                } else {
                    DataSnapshot result = task.getResult();
                    SportWithTeams sportWithTeams = null;

                    if (result != null) {
                        sportWithTeams = result.getValue(SportWithTeams.class);
                    }

                    boolean shouldSetValue = true;
                    if (sportWithTeams == null) {
                        sportWithTeams = new SportWithTeams();
                        List<String> teams = new ArrayList<>();
                        teams.add(currentTeam);

                        sportWithTeams.setSport(currentSport);
                        sportWithTeams.setTeams(teams);
                    } else {

                        List<String> teams = sportWithTeams.getTeams();
                        if (teams == null) {
                            teams = Collections.singletonList(currentTeam);
                            sportWithTeams.setTeams(teams);
                        } else if (teams.isEmpty()) {
                            teams.add(currentTeam);
                        } else {
                            if (!teams.contains(currentTeam)) {
                                teams.add(currentTeam);
                            } else {
                                shouldSetValue = false;
                            }
                        }
                    }

                    if (shouldSetValue) {
                        sportWithTeamsReference.setValue(sportWithTeams).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                addProduct();
                            } else {
                                Toast.makeText(requireContext(), "Failed to add product", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        addProduct();
                    }
                }
            });
        } else {
            addProduct();
        }
    }

    private void addProduct() {
        DatabaseReference productsReference = Utils.getProductsReference();
        String productId = productsReference.push().getKey();
        DatabaseReference productReference = productsReference.child(productId);
        product.setProductId(productId);
        product.setCreatedAt(System.currentTimeMillis());
        product.setAdminId(SportifyAdminApp.admin.adminId);
        if (product.isOnSale()) {
            float salePrice = product.getPrice() - ((product.getPrice() * product.getSale()) / 100);
            product.setSalePrice(salePrice);
        }
        productReference.setValue(product);
        requireActivity().finish();
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
