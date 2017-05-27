package br.com.postero.ifrnmessenger.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import br.com.postero.ifrnmessenger.R;
import br.com.postero.ifrnmessenger.activities.DisciplinaActivity;
import br.com.postero.ifrnmessenger.models.Disciplina;

/**
 * Created by felipe on 27/05/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        RemoteMessage.Notification notification = remoteMessage.getNotification();

        showNotification(notification, remoteMessage.getData());
    }

    private void showNotification(RemoteMessage.Notification notification, Map<String, String> data) {

        String title = notification.getTitle();
        String message = notification.getBody();

        Intent intent = new Intent(this, DisciplinaActivity.class);
        intent.putExtra("disciplina", Disciplina.findById(Disciplina.class, Long.valueOf(data.get("disciplina"))));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        Notification nativeNotification = builder.setContentTitle(title)
                .setSmallIcon(R.drawable.ic_message_black_24dp)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, nativeNotification);

    }
}
