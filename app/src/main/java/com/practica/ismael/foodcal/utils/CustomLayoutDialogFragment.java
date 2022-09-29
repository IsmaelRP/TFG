package com.practica.ismael.foodcal.utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.practica.ismael.foodcal.R;

public class CustomLayoutDialogFragment extends DialogFragment {

    private static Listener listener;
    private static int title;
    private static int positive;
    private static int negative;

    @SuppressWarnings({"EmptyMethod", "UnusedParameters"})
    public interface Listener {
        void onAddClick(DialogFragment dialog);

        void onCancelClick(DialogFragment dialog);
    }

    @SuppressLint("InflateParams")
    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        setCancelable(true);
        AlertDialog.Builder b = new AlertDialog.Builder(requireActivity());
        b.setTitle(title);
        b.setView(LayoutInflater.from(requireActivity()).inflate(R.layout.food_dialog, null));
        b.setPositiveButton(positive,
                (dialog, which) -> listener.onAddClick(CustomLayoutDialogFragment.this));
        b.setNegativeButton(negative,
                (dialog, which) -> listener.onCancelClick(CustomLayoutDialogFragment.this));
        return b.create();
    }

    public static void setListener(Listener listener) {
        CustomLayoutDialogFragment.listener = listener;
    }

    public static void setData(int title, int positive, int negative){
        CustomLayoutDialogFragment.title = title;
        CustomLayoutDialogFragment.positive = positive;
        CustomLayoutDialogFragment.negative = negative;

    }

}