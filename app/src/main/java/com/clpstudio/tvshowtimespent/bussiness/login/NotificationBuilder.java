package com.clpstudio.tvshowtimespent.bussiness.login;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
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
        PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        return builder;
    }

    /**
     * Default design for DealDash notifications
     *
     * @param context      The context
     * @param intentAction Intent action for click events
     * @param title        The tile
     * @param subtitle     The subtitle
     * @param message      The message
     * @return A new builder for creating the notification
     */
    public NotificationCompat.Builder getGeneralDealDashNotificationBuilder(Context context, Intent intentAction, String title,
                                                                            String subtitle, String message) {
        int uniqueId = (int) (System.currentTimeMillis() / 1000);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        final PendingIntent pendingIntent = PendingIntent.getBroadcast(context, uniqueId, intentAction, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setTicker(message)
                .setContentText(subtitle)
                .setSubText(message)
                .setContentIntent(pendingIntent)
                .setSound(soundUri);

        return builder;
    }

}
