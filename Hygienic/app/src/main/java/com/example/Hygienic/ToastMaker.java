package com.example.Hygienic;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.Toast;

public class ToastMaker {
    /**
     * Method that displays a Toast letting user know no result found if API replies with no results.
     * Put in a method to prevent duplication of code as the same code is required twice.
     */
    public static void showToast(String toastText, TableLayout tableLayout, Context context){
        // tableLayout only applicable for  Activity. MainActivity sends null for tableLayout
        if (tableLayout != null){
            tableLayout.removeAllViews();
            tableLayout.setVisibility(View.INVISIBLE);
        }

        Toast toast = Toast.makeText(context,toastText, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
