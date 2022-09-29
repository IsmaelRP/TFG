package com.practica.ismael.foodcal.utils;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * Utility class for SoftKeyboard.
 */
public class KeyboardUtils {

    private KeyboardUtils() {
    }

    public static void hideSoftKeyboard(Activity activity){
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Context
                        .INPUT_METHOD_SERVICE);
        if (imm != null && activity.getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

}
