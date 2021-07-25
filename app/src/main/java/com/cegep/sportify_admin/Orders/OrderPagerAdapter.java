package com.cegep.sportify_admin.Orders;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class OrderPagerAdapter extends FragmentPagerAdapter {

    public OrderPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
           // return new AcceptedOrder();
        }
        return null;
      //  return new CancelledOrder();
    }

    @Override
    public int getCount() {
        return 2;
    }
}
