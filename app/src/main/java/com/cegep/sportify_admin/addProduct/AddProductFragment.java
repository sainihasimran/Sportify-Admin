package com.cegep.sportify_admin.addProduct;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.cegep.sportify_admin.R;
import com.esafirm.imagepicker.features.ImagePickerConfig;
import com.esafirm.imagepicker.features.ImagePickerLauncher;
import com.esafirm.imagepicker.features.ImagePickerLauncherKt;
import com.esafirm.imagepicker.features.ImagePickerMode;
import com.esafirm.imagepicker.features.ImagePickerSavePath;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public class AddProductFragment extends Fragment {

    private ImagePickerLauncher imagepickerLauncher = null;

    private ViewPager viewPager;
    private WormDotsIndicator dotsIndicator;

    private ImageView addImageBackground;
    private ImageView addImagePlaceholder;
    private TextView addImageText;
    private TextView addImageDirectionsText;

    private List<Image> images;
    private Set<String> colorSet = new HashSet<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);
        viewPager = view.findViewById(R.id.viewPager);
        dotsIndicator = view.findViewById(R.id.dots_indicator);

        addImageBackground = view.findViewById(R.id.add_image_background);
        addImagePlaceholder = view.findViewById(R.id.add_image_placeholder);
        addImageText = view.findViewById(R.id.add_image_text);
        addImageDirectionsText = view.findViewById(R.id.add_image_directions_text);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupChooseProductImages(view);
        setupCategories(view);
        setupSubCategories(view);
        setupColorPicker(view);
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

    private void setupChooseProductImages(View view) {
        View.OnClickListener onClickListener = v -> {
            pickImage();
        };
        addImageBackground.setOnClickListener(onClickListener);
        addImagePlaceholder.setOnClickListener(onClickListener);
        addImageText.setOnClickListener(onClickListener);
        addImageDirectionsText.setOnClickListener(onClickListener);
    }

    public void setupCategories(View view) {
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
        });
    }

    public void setupSubCategories(View view) {
        List<String> subCategories = new ArrayList<>();
        subCategories.add("Men's");
        subCategories.add("Womens'");

        ArrayAdapter<String> subCategoriesAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1,
                                                                       subCategories);
        AutoCompleteTextView subCategoryTextView = view.findViewById(R.id.sub_category_textView);
        subCategoryTextView.setAdapter(subCategoriesAdapter);

        subCategoryTextView.setOnItemClickListener((parent, view1, position, id) -> {
            String subCategory = subCategories.get(position);
        });
    }

    public void setupColorPicker(View view) {
        ChipGroup chipGroup = view.findViewById(R.id.available_color_group);
        view.findViewById(R.id.add_color_chip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        if (!colorSet.contains(hexColor)) {
                            int numOfChildren = chipGroup.getChildCount();
                            chipGroup.addView(chip, numOfChildren - 1);
                            colorSet.add(hexColor);
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
            }
        });
    }

    private void pickImage() {
        ImagePickerConfig config = new ImagePickerConfig(ImagePickerMode.MULTIPLE, "Folder", "Tap to select", "DONE", 0, 4, 0, true, false, false,
                                                         false, false,
                                                         new ArrayList<Image>(), new ArrayList<File>(), new ImagePickerSavePath("Camera", true),
                                                         ReturnMode.NONE, false, true);
        imagepickerLauncher.launch(config);

    }

    private void setChooseImageVisibility(int visibility) {
        addImageBackground.setVisibility(visibility);
        addImagePlaceholder.setVisibility(visibility);
        addImageText.setVisibility(visibility);
        addImageDirectionsText.setVisibility(visibility);
    }
}
