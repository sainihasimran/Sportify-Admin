package com.cegep.sportify_admin.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.cegep.sportify_admin.R;
import com.esafirm.imagepicker.model.Image;
import org.jetbrains.annotations.NotNull;

public class ImageFragment extends Fragment {

    private static final String KEY_IMAGE = "KEY_IMAGE";

    private ImageView imageView;

    public static ImageFragment newInstance(Image image) {
        ImageFragment imageFragment = new ImageFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_IMAGE, image);
        imageFragment.setArguments(bundle);
        return imageFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        imageView = view.findViewById(R.id.imageView);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Image image = getArguments().getParcelable(KEY_IMAGE);

        Glide.with(this)
                .load(image.getUri())
                .into(imageView);
    }
}
