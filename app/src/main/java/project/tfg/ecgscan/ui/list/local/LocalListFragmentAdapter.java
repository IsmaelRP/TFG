package project.tfg.ecgscan.ui.list.local;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import project.tfg.ecgscan.R;
import project.tfg.ecgscan.data.local.model.Electro;

public class LocalListFragmentAdapter extends ListAdapter<Electro, LocalListFragmentAdapter.ViewHolder> implements Filterable {

    private final LocalListFragmentViewModel vm;
    private List<Electro> listElectros;
    private List<Electro> listElectrosFiltered;


    LocalListFragmentAdapter(LocalListFragmentViewModel vm) {
        super(new DiffUtil.ItemCallback<Electro>() {
            @Override
            public boolean areItemsTheSame(@NonNull Electro oldItem, @NonNull Electro newItem) {
                return oldItem.getIndividualId() == newItem.getIndividualId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Electro oldItem, @NonNull Electro newItem) {
                return TextUtils.equals(oldItem.getName(), newItem.getName());
            }
        });
        this.vm = vm;
    }

    @NonNull
    public LocalListFragmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LocalListFragmentAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.image_electro, parent, false), vm);
    }

    @Override
    public void onBindViewHolder(@NonNull LocalListFragmentAdapter.ViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getIndividualId();
    }

    @Override
    public Electro getItem(int position) {
        return super.getItem(position);
    }


    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String filterString = constraint.toString();
                if (filterString.isEmpty()) {
                    listElectrosFiltered = listElectros;
                } else {
                    List<Electro> list = new ArrayList<>();
                    for (Electro item : listElectros) {
                        if (includeInFilter(item, filterString)) {
                            list.add(item);
                        }
                    }
                    listElectrosFiltered = list;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listElectrosFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listElectrosFiltered = (List<Electro>) results.values;
                updateList(listElectrosFiltered);
            }
        };
    }

    /*
    void restoreItem() {
        vm.addImage(data);
        data = null;
    }
*/


    private void updateList(List<Electro> list) {
        super.submitList(list);
    }


    @Override
    public void submitList(@Nullable List<Electro> list) {
        listElectros = list;
        listElectrosFiltered = list;
        super.submitList(listElectros);

    }





    //  VIEWHOLDER

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView lblImageName;
        private final TextView lblImageDate;
        private final ImageView imgElectro;
        private final Button btnDiagnose;
        private final Button btnDelete;
        private final LocalListFragmentViewModel vm;

        ViewHolder(View itemView, LocalListFragmentViewModel vm) {
            super(itemView);
            lblImageName = ViewCompat.requireViewById(itemView, R.id.txtName);
            lblImageDate = ViewCompat.requireViewById(itemView, R.id.txtDate);
            btnDiagnose = ViewCompat.requireViewById(itemView, R.id.btnDiagnose);
            btnDelete = ViewCompat.requireViewById(itemView, R.id.btnDelete);
            imgElectro = ViewCompat.requireViewById(itemView, R.id.imgElectro);
            this.vm = vm;

            // TODO: implementar métodos abajo
            btnDelete.setOnClickListener(v -> vm.deleteImgFromLocal(getItem(getAdapterPosition())));
            //btnDiagnose.setOnClickListener(v -> vm.diagnoseImg(((BitmapDrawable)imgElectro.getDrawable()).getBitmap()));
        }



        /*
        private void showPopup(View v) {
            PopupMenu popupMenu = new PopupMenu(v.getContext(), v);
            popupMenu.inflate(R.menu.popup_menu);
            popupMenu.setOnMenuItemClickListener(this::onMenuItemClick);
            popupMenu.show();
        }
         */

        void bind(Electro elec) {
            lblImageName.setText(elec.getName());
            lblImageDate.setText(elec.getDate());
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

    private boolean includeInFilter(Electro item, String filterString) {
        return item.getName().toLowerCase().contains(filterString.toLowerCase());
    }


}
