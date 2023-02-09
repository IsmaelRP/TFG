package project.tfg.ecgscan.ui.list;


import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import project.tfg.ecgscan.R;
import project.tfg.ecgscan.data.ElectroImage;
import project.tfg.ecgscan.ui.secondActivity.SecondActivityViewModel;

public class ListFragmentAdapter extends ListAdapter<ElectroImage, ListFragmentAdapter.ViewHolder> /* implements Filterable */ {

    private final SecondActivityViewModel vm;
    private List<ElectroImage> listElectros;
    private List<ElectroImage> listElectrosFiltered;
    private ElectroImage img;

    ListFragmentAdapter(SecondActivityViewModel vm) {
        super(new DiffUtil.ItemCallback<ElectroImage>() {
            @Override
            public boolean areItemsTheSame(@NonNull ElectroImage oldItem, @NonNull ElectroImage newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull ElectroImage oldItem, @NonNull ElectroImage newItem) {
                return TextUtils.equals(oldItem.getName(), newItem.getName());
            }
        });
        this.vm = vm;
    }

    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.image_electro, parent, false), vm);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    @Override
    public ElectroImage getItem(int position) {
        return super.getItem(position);
    }

    /*
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

     */

    private void updateList(List<ElectroImage> list) {
        super.submitList(list);
    }

    @Override
    public void submitList(@Nullable List<ElectroImage> list) {
        listElectros = list;
        listElectrosFiltered = list;
        super.submitList(list);
    }







    //  VIEWHOLDER

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView lblImageName;
        private final ImageView imgElectro;
        private final SecondActivityViewModel vm;

        ViewHolder(View itemView, SecondActivityViewModel vm) {
            super(itemView);
            lblImageName = ViewCompat.requireViewById(itemView, R.id.textViewName);
            imgElectro = ViewCompat.requireViewById(itemView, R.id.imageView);
            this.vm = vm;
            //imgPopupMenu.setOnClickListener(this::showPopup);
        }

        /*
        private void showPopup(View v) {
            PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
            popupMenu.inflate(R.menu.popup_menu);
            popupMenu.setOnMenuItemClickListener(this::onMenuItemClick);
            popupMenu.show();
        }
         */

        void bind(ElectroImage elec) {
            lblImageName.setText(elec.getName());
            imgElectro.setImageBitmap(elec.getImage());
        }

        /*
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

         */
    }

}


