package project.tfg.ecgscan.ui.tabsList;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import project.tfg.ecgscan.ui.list.ListFragment;

class TabsListFragmentPagerAdapter extends FragmentStateAdapter {

    private static final int FRAGMENT_COUNT = 2;

    public TabsListFragmentPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }


    @NonNull
    public Fragment getItem(int position) {
        if (position == 0) {
            return ListFragment.newInstance();
        } else {
            return ListFragment.newInstance();
        }
    }

    @Override
    public int getItemCount() {
        return FRAGMENT_COUNT;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
            return ListFragment.newInstance();
        } else {
            return ListFragment.newInstance();
        }
    }
}