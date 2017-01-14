package com.clpstudio.tvshowtimespent.bussiness.login;


import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Controller responsible for showing notifications for different states
 */
@Singleton
public final class NotificationController {
    private Context context;
    private NotificationBuilder builder;
    private NotificationManager notificationManager;

    public static final int NOTIFICATION_LED_TURN_ON = 500;

    @Inject
    public NotificationController(Context context, NotificationBuilder notificationBuilder) {
        this.context = context;
        this.builder = notificationBuilder;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void showWeMissYouNotification(Context context) {
        Notification notification = getNotificationWithCustomLed(builder.getWeMissYouBuilder(context).build());
        notificationManager.notify(1, notification);
    }

    /**
     * Set the colors of the led
     *
     * @param notification The notification to modify
     * @return The new modification with custom led color
     */
    private Notification getNotificationWithCustomLed(Notification notification) {
        notification.ledARGB = 0xffff00; //Yellow
        notification.flags = Notification.FLAG_SHOW_LIGHTS;
        notification.ledOnMS = NOTIFICATION_LED_TURN_ON;
        notification.ledOffMS = NOTIFICATION_LED_TURN_ON;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        return notification;
    }

    /**
     * Clear a notification, if visible, by id
     *
     * @param id The id of the notification
     */
    public void clearNotification(int id) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
    }

}