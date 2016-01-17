package com.example.peter.carentry;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Random;

/**
 * Created by Peter on 12/14/2015.
 */
public class SimpleWidgetProvider extends AppWidgetProvider {
    public static final String KEYLESS_ENTRY_LOCK_COMMAND = "lock dis shit reeeel tite";
    public static final String KEYLESS_ENTRY_UNLOCK_COMMAND = "nuthn to steel hur";
    public static final String KEYLESS_ENTRY_PANIC_COMMAND = "oh fuck oh fuck oh fuck oh fuck oh fuck oh fuck oh fuck oh fuck";

    private static TalkToCar coms;

    /* The only real reason to override this is to have the phone unlock the car and not need
     * to hit the button. Stupid, maybe....
     *
     * Other reason would be to have the bluetooth connect automatically so that the unlock
     * is instintanious.
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.example_appwidget);
        ComponentName watchWidget = new ComponentName(context, SimpleWidgetProvider.class);

        remoteViews.setOnClickPendingIntent(R.id.lock_button, getPendingSelfIntent(context, KEYLESS_ENTRY_LOCK_COMMAND));
        remoteViews.setOnClickPendingIntent(R.id.unlock_button, getPendingSelfIntent(context, KEYLESS_ENTRY_UNLOCK_COMMAND));
        remoteViews.setOnClickPendingIntent(R.id.panic_button, getPendingSelfIntent(context, KEYLESS_ENTRY_PANIC_COMMAND));
        appWidgetManager.updateAppWidget(watchWidget, remoteViews);

    }

    @Override
    public void onEnabled(Context context) {
        coms = new TalkToCar();
    }

    private PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (coms == null)
            coms = new TalkToCar();
        if (KEYLESS_ENTRY_LOCK_COMMAND.equals(intent.getAction()))
            coms.lockCar();
        else if (KEYLESS_ENTRY_UNLOCK_COMMAND.equals(intent.getAction()))
            coms.unlockCar();
        else if (KEYLESS_ENTRY_PANIC_COMMAND.equals(intent.getAction()))
            coms.panic();
    }
}
