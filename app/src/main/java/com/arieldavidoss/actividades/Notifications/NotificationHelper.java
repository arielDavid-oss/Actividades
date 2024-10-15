package com.arieldavidoss.actividades.Notifications;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class NotificationHelper {

    public final Context context;

    public NotificationHelper(Context context) {
        this.context = context;
        crearCanalNotificaciones();
    }

    private void crearCanalNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "mi_canal",  // Asegúrate de que este ID coincida en NotificationReceiver
                    "Recordatorios de Tareas",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Canal para notificaciones de recordatorios de tareas");

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    public void scheduleNotification(long triggerAtMillis, int notificationId, String taskName) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("task_name", taskName);  // Pasa el nombre de la tarea
        intent.putExtra("notification_id", notificationId);  // Pasa un ID único para la notificación

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, notificationId, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerAtMillis, pendingIntent);
        }
    }


}
