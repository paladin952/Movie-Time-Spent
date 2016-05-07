package com.clpstudio.tvshowtimespent.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CLP STUDIO
 */
public class Utils {

    /**
     * Open the store for this app
     * @param activity The activity to start from
     */
    public static void openStore(Activity activity) {
        final String appPackageName = activity.getPackageName(); // getPackageName() from Context or Activity object
        try {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    /**
     * Get a link to the app
     * @param activity The activity
     * @return The link to the app
     */
    public static String getAppLink(Activity activity){
        final String appPackageName = activity.getPackageName();
            return "https://play.google.com/store/apps/details?id=" + appPackageName;
    }

    /**
     * Converting a list of integers into one of strings
     * @param integers The list of ingers to be converter
     * @return A new list of strings
     */
    public static List<String> convertListOfIntegersToString(List<Integer> integers){
        List<String> strings = new ArrayList<>();

        for(Integer integer: integers){
            strings.add(String.valueOf(integer));
        }

        return strings;
    }

}
