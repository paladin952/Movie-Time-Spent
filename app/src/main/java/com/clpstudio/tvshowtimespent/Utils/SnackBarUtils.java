package com.clpstudio.tvshowtimespent.Utils;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.clpstudio.tvshowtimespent.R;

/**
 * Created by lclapa on 10/29/2015.
 */
public class SnackBarUtils {

    public static void snackError(Context context, CoordinatorLayout coordinatorLayout, String message){
        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT);
        View view = snackbar.getView();
        view.setBackgroundColor(context.getResources().getColor(R.color.snackbar_red));
        TextView textView = (TextView) view.findViewById(R.id.snackbar_text);
        textView.setTextColor(context.getResources().getColor(android.R.color.white));
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        snackbar.show();
    }

    public static void snackStandard(CoordinatorLayout coordinatorLayout, String message){
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_SHORT).show();
    }

}
