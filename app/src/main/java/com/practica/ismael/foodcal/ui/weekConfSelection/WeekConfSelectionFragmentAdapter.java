package com.practica.ismael.foodcal.ui.weekConfSelection;

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
import com.practica.ismael.foodcal.data.model.Week;
import com.practica.ismael.foodcal.ui.main_activity.MainActivityViewModel;

import java.util.ArrayList;
import java.util.List;

public class WeekConfSelectionFragmentAdapter extends ListAdapter<Week, WeekConfSelectionFragmentAdapter.ViewHolder> implements Filterable {

    private List<Week> listWeeks;
    private List<Week> listWeeksFiltered;
    private final WeekConfSelectionViewModel vm;

    WeekConfSelectionFragmentAdapter(MainActivityViewModel mainVM, WeekConfSelectionViewModel vm) {
        super(new DiffUtil.ItemCallback<Week>() {
            @Override
            public boolean areItemsTheSame(@NonNull Week oldItem, @NonNull Week newItem) {
                return oldItem.getIdSemana() == newItem.getIdSemana();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Week oldItem, @NonNull Week newItem) {
                return TextUtils.equals(oldItem.getNombreSemana(), newItem.getNombreSemana());
            }
        });
        MainActivityViewModel mainVM1 = mainVM;
        this.vm = vm;
    }

    @NonNull
    @Override
    public WeekConfSelectionFragmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeekConfSelectionFragmentAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.fragment_list_selection_calendars, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WeekConfSelectionFragmentAdapter.ViewHolder holder, int position) {
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

        private final TextView lblWeekName;

        ViewHolder(View itemView) {
            super(itemView);
            lblWeekName = ViewCompat.requireViewById(itemView, R.id.lblWeekName);
            itemView.setOnClickListener(v -> showCalendarOptions());
        }

        private void showCalendarOptions() {
            vm.setWeekSelected(getItem(getAdapterPosition()));
        }

        void bind(Week week) {
            lblWeekName.setText(week.getNombreSemana());
        }
    }

    private boolean includeInFilter(Week item, String filterString) {
        return item.getNombreSemana().toLowerCase().contains(filterString.toLowerCase());
    }
}
