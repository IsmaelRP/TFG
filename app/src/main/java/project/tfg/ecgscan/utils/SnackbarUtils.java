package project.tfg.ecgscan.utils;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;

/**
 * Utility class for Snackbar.
 */
public class SnackbarUtils {

    private SnackbarUtils() {
    }

    public static void snackbar(View v, String message, int length){
        Snackbar.make(v, message, length).show();
    }
}
