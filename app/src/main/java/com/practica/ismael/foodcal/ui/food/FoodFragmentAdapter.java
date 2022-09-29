package com.practica.ismael.foodcal.ui.food;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.practica.ismael.foodcal.R;
import com.practica.ismael.foodcal.data.model.Food;

import java.util.ArrayList;
import java.util.List;

public class FoodFragmentAdapter extends ListAdapter<Food, FoodFragmentAdapter.ViewHolder> implements Filterable {

    private final FoodFragmentViewModel vm;
    private List<Food> listFoods;
    private List<Food> listFoodsFiltered;
    private Food data;

    FoodFragmentAdapter(FoodFragmentViewModel vm) {
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
        this.vm = vm;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.fragment_list_foods, parent, false), vm);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
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

    void restoreItem() {
        vm.addFood(data);
        data = null;
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
        private final ImageView imgPopupMenu;
        private final FoodFragmentViewModel vm;

        ViewHolder(View itemView, FoodFragmentViewModel vm) {
            super(itemView);
            lblFoodName = ViewCompat.requireViewById(itemView, R.id.lblFoodName);
            imgPopupMenu = ViewCompat.requireViewById(itemView, R.id.imgPopupMenuFoodList);
            this.vm = vm;
            imgPopupMenu.setOnClickListener(this::showPopup);
        }

        private void showPopup(View v) {
            PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
            popupMenu.inflate(R.menu.popup_menu);
            popupMenu.setOnMenuItemClickListener(this::onMenuItemClick);
            popupMenu.show();
        }

        void bind(Food food) {
            lblFoodName.setText(food.getNombrecomida());
        }

        private boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.mnuEdit:
                    vm.setFoodEditTrigger(getItem(getAdapterPosition()));
                    break;
                case R.id.mnuDelete:
                    data = getItem(getAdapterPosition());
                    vm.setFoodDeletedLiveData(getItem(getAdapterPosition()));
                    break;
                default:
                    return false;
            }
            return true;
        }
    }

    private boolean includeInFilter(Food item, String filterString) {
        return item.getNombrecomida().toLowerCase().contains(filterString.toLowerCase());
    }
}


