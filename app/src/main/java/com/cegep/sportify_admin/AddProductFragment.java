package com.cegep.sportify_admin;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import java.util.List;

class AddProductFragment extends Fragment {

    private static final String[] REQUIRED_PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    private static final int PERMISSION_REQUEST_CODE = 123;

    private static final int CHOOSE_IMAGE_REQUEST_CODE = 343;

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
        setupChooseProductImages(view);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            pickImage();
        } else {
            Toast.makeText(getContext(), R.string.storage_permission_declined, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == CHOOSE_IMAGE_REQUEST_CODE && data != null) {
            List<String> selectionData = Matisse.obtainPathResult(data);
            if (selectionData == null || selectionData.isEmpty()) {
                Toast.makeText(getContext(), R.string.choose_image_error, Toast.LENGTH_SHORT).show();
            } else {

            }
        } else {
            Toast.makeText(getContext(), R.string.choose_image_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void setupChooseProductImages(View view) {
        View.OnClickListener onClickListener = v -> {
            String permission = REQUIRED_PERMISSIONS[0];
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                pickImage();
            } else if (ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED) {
                pickImage();
            } else {
                requestPermissions(REQUIRED_PERMISSIONS, PERMISSION_REQUEST_CODE);
            }


        };
        view.findViewById(R.id.add_image_background).setOnClickListener(onClickListener);
        view.findViewById(R.id.add_image_placeholder).setOnClickListener(onClickListener);
        view.findViewById(R.id.add_image_directions_text).setOnClickListener(onClickListener);
    }

    private void pickImage() {
        Matisse.from(this)
                .choose(MimeType.ofImage())
                .maxSelectable(4)
                .forResult(CHOOSE_IMAGE_REQUEST_CODE);
    }
}
