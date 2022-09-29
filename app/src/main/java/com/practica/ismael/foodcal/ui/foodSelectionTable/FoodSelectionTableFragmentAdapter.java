package com.practica.ismael.foodcal.ui.foodSelectionTable;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.practica.ismael.foodcal.R;
import com.practica.ismael.foodcal.data.model.Food;
import com.practica.ismael.foodcal.ui.main_activity.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class FoodSelectionTableFragmentAdapter extends ListAdapter<Food, FoodSelectionTableFragmentAdapter.ViewHolder> implements Filterable {

    private List<Food> listFoods;
    private List<Food> listFoodsFiltered;
    private final MainActivityViewModel mainVM;

    FoodSelectionTableFragmentAdapter(MainActivityViewModel mainVM) {
        super(new DiffUtil.ItemCallback<Food>() {
            @Override
            public boolean areItemsTheSame(@NonNull Food oldItem, @NonNull Food newItem) {
                return oldItem.getIdcomida() == newItem.getIdcomida();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Food oldItem, @NonNull Food newItem) {
                return TextUtils.equals(oldItem.getNombrecomida(), newItem.getNombrecomida());
            }
        });
        this.mainVM = mainVM;
    }

    @NonNull
    @Override
    public FoodSelectionTableFragmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FoodSelectionTableFragmentAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.fragment_list_selection_foods, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FoodSelectionTableFragmentAdapter.ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getIdcomida();
    }

    @Override
    public Food getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String filterString = constraint.toString();
                if (filterString.isEmpty()) {
                    listFoodsFiltered = listFoods;
                } else {
                    List<Food> list = new ArrayList<>();
                    for (Food item : listFoods) {
                        if (includeInFilter(item, filterString)) {
                            list.add(item);
                        }
                    }
                    listFoodsFiltered = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listFoodsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                //noinspection unchecked
                listFoodsFiltered = (List<Food>) results.values;
                updateList(listFoodsFiltered);
            }
        };
    }

    private void updateList(List<Food> list) {
        super.submitList(list);
    }

    @Override
    public void submitList(@Nullable List<Food> list) {
        listFoods = list;
        listFoodsFiltered = list;
        super.submitList(list);
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView lblFoodName;

        ViewHolder(View itemView) {
            super(itemView);
            lblFoodName = ViewCompat.requireViewById(itemView, R.id.lblFoodName);
            itemView.setOnClickListener(v -> saveFoodSelection());
        }

        private void saveFoodSelection() {
            mainVM.setFoodTableSelected(getItem(getAdapterPosition()));
        }

        void bind(Food food) {
            lblFoodName.setText(food.getNombrecomida());
        }
    }

    private boolean includeInFilter(Food item, String filterString) {
        return item.getNombrecomida().toLowerCase().contains(filterString.toLowerCase());
    }


}
