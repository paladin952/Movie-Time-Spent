package com.clpstudio.tvshowtimespent.bussiness.login;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.clpstudio.tvshowtimespent.R;
import com.clpstudio.tvshowtimespent.presentation.mainscreen.MainActivity;

import javax.inject.Inject;

/**
 * Class responsible for creating notifications
 */
public final class NotificationBuilder {

    @Inject
    public NotificationBuilder() {
    }

    /**
     * Creating a builder for user's inactivity notification
     *
     * @param context The context
     * @return the NotificationCompat.Builder
     */
    public NotificationCompat.Builder getWeMissYouBuilder(Context context) {
        String title = context.getResources().getString(R.string.we_miss_you_notification_titles);
        String message = context.getResources().getString(R.string.we_miss_you_notification_messages);


        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setTicker(message)
                        .setOnlyAlertOnce(true)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(message));

        if (Build.VERSION.SDK_INT >= 21) {
            builder.setPriority(Notification.PRIORITY_MAX);
            builder.setVibrate(new long[100]);
        }

        Intent resultIntent = new Intent(context, MainActivity.class);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        return builder;
    }
}
