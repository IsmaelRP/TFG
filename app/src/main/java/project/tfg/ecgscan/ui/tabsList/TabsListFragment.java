package project.tfg.ecgscan.ui.tabsList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

import project.tfg.ecgscan.R;
import project.tfg.ecgscan.databinding.FragmentTabslistBinding;
import project.tfg.ecgscan.ui.secondActivity.SecondActivityViewModel;

public class TabsListFragment extends Fragment {

    private FragmentTabslistBinding b;
    private SecondActivityViewModel vm;

    private NavController navController;

    @Override
    public void onResume() {
        super.onResume();
        requireActivity().setTitle(getString(R.string.tabs_fragment_title));
    }


    private final int[] iconResIds = {R.drawable.ic_baseline_cloud_queue_24, R.drawable
            .ic_baseline_cloud_off_24};

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        b = DataBindingUtil.inflate(inflater, R.layout.fragment_tabslist, container, false);    //  Databinding enabled
        return b.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViews(requireView());
    }

    private void setupViews(View view) {
        navController = Objects.requireNonNull(Navigation.findNavController(view));

        vm = ViewModelProviders.of(requireActivity()).get(SecondActivityViewModel.class);     //  To obtain ViewModel

        ((AppCompatActivity) requireActivity()).setSupportActionBar(b.tabsToolbar);
        setupViewPager(view);
    }

    private void setupViewPager(View view) {
        ViewPager2 viewPager = ViewCompat.requireViewById(view, R.id.viewPager);
        TabLayout tabLayout = ViewCompat.requireViewById(view, R.id.tabLayout);

        TabsListFragmentPagerAdapter adapter = new TabsListFragmentPagerAdapter(requireFragmentManager(), getLifecycle(), navController);
        viewPager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setIcon(iconResIds[position])).attach();
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

        });
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    /*
    private void changeToolbarScrollBehavior(boolean scrollable) {
        AppBarLayout.LayoutParams params =
                (AppBarLayout.LayoutParams) b.tabsToolbar.getLayoutParams();
        if (scrollable) {
            params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        } else {
            params.setScrollFlags(0);
        }
    }

     */


}
