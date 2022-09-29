package com.practica.ismael.foodcal.ui.configuration;

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
import com.practica.ismael.foodcal.data.model.Week;

import java.util.ArrayList;
import java.util.List;

public class ConfigurationFragmentAdapter extends ListAdapter<Week, ConfigurationFragmentAdapter.ViewHolder> implements Filterable {

    private final ConfigurationFragmentViewModel vm;
    private List<Week> listWeeks;
    private List<Week> listWeeksFiltered;
    private Week data;

    ConfigurationFragmentAdapter(ConfigurationFragmentViewModel vm) {
        super(new DiffUtil.ItemCallback<Week>() {
            @Override
            public boolean areItemsTheSame(@NonNull Week oldItem, @NonNull Week newItem) {
                return oldItem.getIdSemana() == newItem.getIdSemana() &&
                        oldItem.getIdUsuario().equals(newItem.getIdUsuario());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Week oldItem, @NonNull Week newItem) {
                return TextUtils.equals(oldItem.getNombreSemana(), newItem.getNombreSemana());
            }
        });
        this.vm = vm;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.fragment_list_week, parent, false), vm);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getIdSemana();
    }

    @Override
    public Week getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String filterString = constraint.toString();
                if (filterString.isEmpty()) {
                    listWeeksFiltered = listWeeks;
                } else {
                    List<Week> list = new ArrayList<>();
                    for (Week item : listWeeks) {
                        if (includeInFilter(item, filterString)) {
                            list.add(item);
                        }
                    }
                    listWeeksFiltered = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listWeeksFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                //noinspection unchecked
                listWeeksFiltered = (List<Week>) results.values;
                updateList(listWeeksFiltered);
            }
        };
    }

    void restoreItem() {
        vm.addWeek(data.getIdUsuario(), data.getNombreSemana());
        data = null;
    }

    private void updateList(List<Week> list) {
        super.submitList(list);
    }

    @Override
    public void submitList(@Nullable List<Week> list) {
        listWeeks = list;
        listWeeksFiltered = list;
        super.submitList(list);
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView lblCalendarName;
        private final ImageView imgPopupMenu;
        private final ConfigurationFragmentViewModel vm;

        ViewHolder(View itemView, ConfigurationFragmentViewModel vm) {
            super(itemView);
            lblCalendarName = ViewCompat.requireViewById(itemView, R.id.lblWeekName);
            imgPopupMenu = ViewCompat.requireViewById(itemView, R.id.imgPopupMenuWeekList);
            this.vm = vm;
            imgPopupMenu.setOnClickListener(this::showPopup);
            itemView.setOnClickListener(v -> showWeek());
        }

        private void showWeek() {
            vm.setWeekTrigger(getItem(getAdapterPosition()));
        }

        private void showPopup(View v) {
            PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
            popupMenu.inflate(R.menu.popup_menu);
            popupMenu.setOnMenuItemClickListener(this::onMenuItemClick);
            popupMenu.show();
        }

        void bind(Week week) {
            lblCalendarName.setText(week.getNombreSemana());
        }

        private boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.mnuEdit:
                    vm.setWeekEditTrigger(getItem(getAdapterPosition()));
                    break;
                case R.id.mnuDelete:
                    data = getItem(getAdapterPosition());
                    vm.setWeekDeletedLiveData(getItem(getAdapterPosition()));
                    break;
                default:
                    return false;
            }
            return true;
        }
    }

    private boolean includeInFilter(Week item, String filterString) {
        return item.getNombreSemana().toLowerCase().contains(filterString.toLowerCase());
    }
}


