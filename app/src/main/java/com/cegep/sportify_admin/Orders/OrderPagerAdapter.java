package com.cegep.sportify_admin.Orders;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class OrderPagerAdapter extends FragmentPagerAdapter {
    public OrderPagerAdapter(FragmentManager supportFragmentManager) {
        super(supportFragmentManager);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new PendingOrdersFragment();
        }

        if (position == 1) {
            return new AcceptedOrdersFragment();
        }

       return new CancelledOrdersFragment();
    }

    @Override
    public int getCount() {
        return 3;
    }
}
