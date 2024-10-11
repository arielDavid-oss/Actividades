package com.arieldavidoss.actividades.Notifications;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;


public class NotificationHelper {

    // Método para programar la notificación antes de que la actividad termine (por ejemplo, 10 minutos antes)
    public static void scheduleNotification(Context context, String nombreActividad, Calendar horaFin) {
        // Restar 10 minutos a la hora de fin
        horaFin.add(Calendar.MINUTE, -10);

        // Configurar AlarmManager para disparar la notificación
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.putExtra("nombreActividad", nombreActividad);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, horaFin.getTimeInMillis(), pendingIntent);
        }
    }


}
