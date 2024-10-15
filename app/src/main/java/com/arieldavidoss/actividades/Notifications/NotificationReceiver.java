package com.arieldavidoss.actividades.Notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.arieldavidoss.actividades.MainActivity;
import com.arieldavidoss.actividades.R;
import com.arieldavidoss.actividades.ui.home.HomeFragment;

public class NotificationReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "mi_canal";

    @Override
    public void onReceive(Context context, Intent intent) {
        String taskName = intent.getStringExtra("task_name");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID) // Usa CHANNEL_ID aquí
                .setSmallIcon(android.R.drawable.ic_notification_overlay)  // Usa un ícono predeterminado de Android
                .setContentTitle("Recordatorio de tarea")
                .setContentText("La tarea \"" + taskName + "\" está a punto de vencer.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        // Crear un intent para cuando se presione la notificación
        Intent notificationIntent = new Intent(context, MainActivity.class); // Cambia a la actividad principal
        notificationIntent.putExtra("show_fragment", "home");
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Añadir flags para manejar la tarea
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        builder.setContentIntent(contentIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(intent.getIntExtra("notification_id", 0), builder.build());
    }
}
