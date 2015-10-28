package com.clpstudio.movetimespent.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import com.clpstudio.movetimespent.R;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by lclapa on 10/28/2015.
 */
public class FacebookUtils {
    /**
     * Tell others how much time have you spent
     * Share a link to the app
     *
     * @param context     The context
     * @param shareDialog The dialog
     */
    public static void shareLinkOnFacebook(Context context, ShareDialog shareDialog, String days, String hours, String minutes) {


        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle("I've spent " + days + " days, " + hours + " hours, " + minutes + "minutes" + " watching tv series!")
                    .setContentDescription(context.getString(R.string.facebook_description))
                    .setContentUrl(Uri.parse(context.getString(R.string.google_play_app_link)))
                    .build();
            shareDialog.show(linkContent);
        }
    }

    /**
     * Get the current hash key DEBUG mode
     *
     * @param context
     * @return
     */
    public static String getKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }
        return key;
    }
}
