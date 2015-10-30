package com.clpstudio.tvshowtimespent.Utils;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.clpstudio.tvshowtimespent.R;

public class SnackBarUtils {

    /**
     * creates an snackbar with red background
     * @param context The context
     * @param coordinatorLayout The coordinator layout
     * @param message The message
     */
    public static void snackError(Context context, CoordinatorLayout coordinatorLayout, String message){
        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT);
        View view = snackbar.getView();
        view.setBackgroundColor(context.getResources().getColor(R.color.snackbar_red));
        TextView textView = (TextView) view.findViewById(R.id.snackbar_text);
        textView.setTextColor(context.getResources().getColor(android.R.color.white));
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        snackbar.show();
    }

    /**
     * creates a standard snake bar with grey background
     * @param coordinatorLayout The coordinator layout
     * @param message The message
     */
    public static void snackStandard(CoordinatorLayout coordinatorLayout, String message){
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }

}
