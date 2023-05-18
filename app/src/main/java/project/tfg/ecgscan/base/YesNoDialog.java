package project.tfg.ecgscan.base;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import project.tfg.ecgscan.ui.home.OnOkClickListener;

public class YesNoDialog extends DialogFragment {


    private OnOkClickListener listener;
    private String title;

    public YesNoDialog(String title) {
        this.title = title;
    }

    public static YesNoDialog newInstance(String title) {
        YesNoDialog fragment = new YesNoDialog(title);
        Bundle args = new Bundle();


        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onOkClick();
                        dismiss();
                    }
                })
                .setNegativeButton("Cancel", null);
        return builder.create();
    }

    public void setOnClickListener(OnOkClickListener listener) {
        this.listener = listener;
    }



}
