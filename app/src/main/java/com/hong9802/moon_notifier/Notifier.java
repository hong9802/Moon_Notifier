package com.hong9802.moon_notifier;

import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Color;
import android.os.AsyncTask;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.concurrent.ExecutionException;

public class Notifier extends ContextWrapper {
    public Notifier(Context base) {
        super(base);
    }

    public void createNotification(String address) throws ExecutionException, InterruptedException {
        AsyncTask<String, Void, String> parse = new Parser().execute(address);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "default");
        String[] notidata = parse.get().split(";");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(notidata[0] + " 달 관찰시간");
        builder.setContentText(notidata[1]);
        builder.setColor(Color.GREEN);
        builder.setAutoCancel(false);
        NotificationManager notificationManager = (NotificationManager)this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    public void removeNotification()
    {
        NotificationManagerCompat.from(this).cancel(1);
    }
}
