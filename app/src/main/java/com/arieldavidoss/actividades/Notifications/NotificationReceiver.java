package com.arieldavidoss.actividades.Notifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "TareasChannel";

    @Override
    public void onReceive(Context context, Intent intent) {
        String taskName = intent.getStringExtra("task_name");
        int taskId = intent.getIntExtra("task_id", 0); // Asumiendo que envías un ID de tarea también.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Recordatorio de Tarea";
            String description = "Notificación para recordarte que tienes una tarea pendiente";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID) // Usa CHANNEL_ID aquí
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Recordatorio de tarea")
                .setContentText("Tienes una tarea pendiente: " + taskName)
                .setPriority(NotificationCompat.PRIORITY_HIGH) // Cambiar a PRIORITY_HIGH
                .setAutoCancel(true);

        // Usar el ID de tarea para la notificación
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(taskId, builder.build()); // Usa el ID de la tarea
    }
}
