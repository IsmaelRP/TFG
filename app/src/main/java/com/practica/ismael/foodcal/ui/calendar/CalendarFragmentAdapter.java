package com.practica.ismael.foodcal.ui.calendar;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.Filter;
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
import com.practica.ismael.foodcal.data.model.Calendar;

import java.util.ArrayList;
import java.util.List;

public class CalendarFragmentAdapter extends ListAdapter<Calendar, CalendarFragmentAdapter.ViewHolder> implements Filterable {

    private final CalendarFragmentViewModel vm;
    private List<Calendar> listCalendars;
    private List<Calendar> listCalendarsFiltered;
    private Calendar data;

    CalendarFragmentAdapter(CalendarFragmentViewModel vm) {
        super(new DiffUtil.ItemCallback<Calendar>() {
            @Override
            public boolean areItemsTheSame(@NonNull Calendar oldItem, @NonNull Calendar newItem) {
                return oldItem.getIdCalendario() == newItem.getIdCalendario()
                        && oldItem.getIdUsuario().equals(newItem.getIdUsuario());
            }

            @Override
            public boolean areContentsTheSame(@NonNull Calendar oldItem, @NonNull Calendar newItem) {
                return TextUtils.equals(oldItem.getNombreCalendario(), newItem.getNombreCalendario());
            }
        });
        this.vm = vm;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.fragment_list_calendars, parent, false), vm);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getIdCalendario();
    }

    @Override
    public Calendar getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String filterString = constraint.toString();
                if (filterString.isEmpty()) {
                    listCalendarsFiltered = listCalendars;
                } else {
                    List<Calendar> list = new ArrayList<>();
                    for (Calendar item : listCalendars) {
                        if (includeInFilter(item, filterString)) {
                            list.add(item);
                        }
                    }
                    listCalendarsFiltered = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listCalendarsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                //noinspection unchecked
                listCalendarsFiltered = (List<Calendar>) results.values;
                updateList(listCalendarsFiltered);
            }
        };
    }

    void restoreItem() {
        //vm.addFood(data);
        data = null;
    }

    private void updateList(List<Calendar> list) {
        super.submitList(list);
    }

    @Override
    public void submitList(@Nullable List<Calendar> list) {
        listCalendars = list;
        listCalendarsFiltered = list;
        super.submitList(list);
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView lblCalendarName;
        private final ImageView imgPopupMenu;
        private final CalendarFragmentViewModel vm;

        ViewHolder(View itemView, CalendarFragmentViewModel vm) {
            super(itemView);
            lblCalendarName = ViewCompat.requireViewById(itemView, R.id.lblCalendarName);
            imgPopupMenu = ViewCompat.requireViewById(itemView, R.id.imgPopupMenuCalendarList);
            this.vm = vm;
            imgPopupMenu.setOnClickListener(this::showPopup);
            itemView.setOnClickListener(v -> navigateToCalendarTable());
        }

        private void navigateToCalendarTable() {
            vm.setCalendarTrigger(getItem(getAdapterPosition()));
        }

        private void showPopup(View v) {
            PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
            popupMenu.inflate(R.menu.delete_menu);
            popupMenu.setOnMenuItemClickListener(this::onMenuItemClick);
            popupMenu.show();
        }

        void bind(Calendar calendar) {
            lblCalendarName.setText(calendar.getNombreCalendario());
        }

        private boolean onMenuItemClick(MenuItem menuItem) {
            //noinspection SwitchStatementWithTooFewBranches
            switch (menuItem.getItemId()) {
                case R.id.mnuDelete:
                    data = getItem(getAdapterPosition());
                    vm.setCalendarDeletedTrigger(getItem(getAdapterPosition()));
                    break;
                default:
                    return false;
            }
            return true;
        }
    }

    private boolean includeInFilter(Calendar item, String filterString) {
        return item.getNombreCalendario().toLowerCase().contains(filterString.toLowerCase());
    }
}


