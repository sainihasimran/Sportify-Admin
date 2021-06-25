package com.cegep.sportify_admin.gallery;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.esafirm.imagepicker.model.Image;
import java.util.List;

public class ImageAdapter extends FragmentPagerAdapter {

    private final List<Image> images;

    public ImageAdapter(@NonNull FragmentManager fm, List<Image> images) {
        super(fm);
        this.images = images;
    }

    @NonNull

    @Override
    public Fragment getItem(int position) {
        return ImageFragment.newInstance(images.get(position));
    }

    @Override
    public int getCount() {
        return images.size();
    }
}
