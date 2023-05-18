package project.tfg.ecgscan.base;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import project.tfg.ecgscan.R;
import project.tfg.ecgscan.ui.home.OnButtonClickListener;

public class MsgDialog extends DialogFragment {


    private EditText titleName;
    private OnButtonClickListener listener;

    public MsgDialog() {
    }

    public static MsgDialog newInstance() {
        MsgDialog fragment = new MsgDialog();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog, null);

        titleName = view.findViewById(R.id.input_text);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Fill in the name of the electrocardiogram")
                .setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Obtener el texto introducido en el EditText
                        String title = titleName.getText().toString();

                        listener.onButtonClick(title);
                        dismiss();
                    }
                })
                .setNegativeButton("Cancel", null);
        return builder.create();
    }

    public void setOnClickListener(OnButtonClickListener listener) {
        this.listener = listener;
    }


}
