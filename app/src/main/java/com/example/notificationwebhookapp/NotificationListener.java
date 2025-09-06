package com.example.notificationwebhookapp;

import android.app.Notification;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.util.HashSet;
import java.util.Set;

public class NotificationListener extends NotificationListenerService {

    private static final String TAG = "NotificationListener";
    private static final String PREFS_NAME = "NotificationWebhookPrefs";
    private static final String SELECTED_APPS_KEY = "SelectedApps";

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.d(TAG, "Notification posted: " + sbn.getPackageName());

        String packageName = sbn.getPackageName();

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Set<String> selectedApps = sharedPreferences.getStringSet(SELECTED_APPS_KEY, new HashSet<>());

        Log.d(TAG, "Selected apps: " + selectedApps);

        if (selectedApps.contains(packageName)) {
            Log.d(TAG, "Notification is from a selected app: " + packageName);

            Notification notification = sbn.getNotification();
            if (notification != null) {
                // Create an Intent with all notification data
                Intent intent = new Intent(this, MainActivity.class);
                intent.setAction("com.example.SEND_WEBHOOK");
                
                // Package notification data
                Bundle notificationData = new Bundle();
                
                // Basic StatusBarNotification data
                notificationData.putString("package", packageName);
                notificationData.putLong("postTime", sbn.getPostTime());
                notificationData.putInt("id", sbn.getId());
                notificationData.putString("tag", sbn.getTag());
                notificationData.putString("key", sbn.getKey());
                
                // Notification properties
                if (notification.extras != null) {
                    Bundle extras = notification.extras;
                    
                    // Standard notification fields
                    notificationData.putString("title", extras.getString(Notification.EXTRA_TITLE));
                    notificationData.putString("text", extras.getString(Notification.EXTRA_TEXT));
                    notificationData.putString("subText", extras.getString(Notification.EXTRA_SUB_TEXT));
                    notificationData.putString("bigText", extras.getString(Notification.EXTRA_BIG_TEXT));
                    notificationData.putString("summaryText", extras.getString(Notification.EXTRA_SUMMARY_TEXT));
                    notificationData.putString("infoText", extras.getString(Notification.EXTRA_INFO_TEXT));
                    
                    // Additional extras that might be available
                    try {
                        if (extras.containsKey(Notification.EXTRA_CONVERSATION_TITLE)) {
                            notificationData.putString("conversationTitle", extras.getString(Notification.EXTRA_CONVERSATION_TITLE));
                        }
                        if (extras.containsKey(Notification.EXTRA_CHANNEL_ID)) {
                            notificationData.putString("channelId", extras.getString(Notification.EXTRA_CHANNEL_ID));
                        }
                    } catch (Exception e) {
                        Log.w(TAG, "Error extracting additional extras", e);
                    }
                }
                
                // Notification metadata
                notificationData.putString("category", notification.category);
                notificationData.putInt("priority", notification.priority);
                notificationData.putInt("visibility", notification.visibility);
                notificationData.putInt("flags", notification.flags);
                
                // Actions (if any)
                if (notification.actions != null && notification.actions.length > 0) {
                    String[] actionTitles = new String[notification.actions.length];
                    for (int i = 0; i < notification.actions.length; i++) {
                        if (notification.actions[i] != null && notification.actions[i].title != null) {
                            actionTitles[i] = notification.actions[i].title.toString();
                        }
                    }
                    notificationData.putStringArray("actions", actionTitles);
                }
                
                // Group information
                notificationData.putString("group", notification.getGroup());
                notificationData.putString("sortKey", notification.getSortKey());
                
                // Pass all data to MainActivity
                intent.putExtra("notificationData", notificationData);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                Log.d(TAG, "Complete notification data sent to MainActivity");
            } else {
                Log.e(TAG, "Notification is null for package: " + packageName);
            }
        } else {
            Log.d(TAG, "Notification is not from a selected app: " + packageName);
        }
    }
}
