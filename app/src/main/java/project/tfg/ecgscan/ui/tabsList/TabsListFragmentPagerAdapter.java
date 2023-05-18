package project.tfg.ecgscan.ui.tabsList;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.NavController;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import project.tfg.ecgscan.ui.list.cloud.CloudListFragment;
import project.tfg.ecgscan.ui.list.local.LocalListFragment;

class TabsListFragmentPagerAdapter extends FragmentStateAdapter {

    private static final int FRAGMENT_COUNT = 2;
    private NavController navController;

    public TabsListFragmentPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, NavController navController) {
        super(fragmentManager, lifecycle);
        this.navController = navController;
    }


    @NonNull
    public Fragment getItem(int position) {
        if (position == 0) {
            return CloudListFragment.newInstance(navController);
        } else {
            return CloudListFragment.newInstance(navController);
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
            return CloudListFragment.newInstance(navController);
        } else {
            return LocalListFragment.newInstance(navController);
        }
    }
}