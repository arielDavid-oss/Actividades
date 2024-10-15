package com.arieldavidoss.actividades.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.arieldavidoss.actividades.Notifications.NotificationHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "actividades.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_ACTIVIDADES = "actividades";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOMBRE = "nombreActividad";
    private static final String COLUMN_DESCRIPCION = "descripcion";
    private static final String COLUMN_COMPLETADA = "completada";
    private static final String COLUMN_HORA_FIN = "horaFin";

    private final NotificationHelper notificationHelper;  // Añadir una instancia de NotificationHelper

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        notificationHelper = new NotificationHelper(context);  // Inicializar NotificationHelper
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE " + TABLE_ACTIVIDADES + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOMBRE + " TEXT, " +
                COLUMN_DESCRIPCION + " TEXT, " +
                COLUMN_COMPLETADA + " INTEGER DEFAULT 0 NOT NULL, " +
                COLUMN_HORA_FIN + " TEXT" +
                ")";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVIDADES);
        onCreate(db);
    }

    public boolean addActividad(String nombreActividad, String descripcion, String horaFin) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, nombreActividad);
        values.put(COLUMN_DESCRIPCION, descripcion);
        values.put(COLUMN_HORA_FIN, horaFin);
        long result = db.insert(TABLE_ACTIVIDADES, null, values);
        db.close();

        if (result != -1) {
            // Programar la notificación usando NotificationHelper
            programarNotificacion(nombreActividad, result, horaFin);
        }

        return result != -1;
    }

    private void programarNotificacion(String nombreActividad, long activityId, String horaFin) {
        // Convertir horaFin en formato de fecha y hora
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        try {
            Date fechaFin = format.parse(horaFin);

            // Calcular un día antes
            Calendar calendar = Calendar.getInstance();
            assert fechaFin != null;
            calendar.setTime(fechaFin);
            calendar.add(Calendar.DAY_OF_YEAR, -1);  // Restar un día

            // Programar la notificación utilizando NotificationHelper
            notificationHelper.scheduleNotification(calendar.getTimeInMillis(), (int) activityId, nombreActividad);

        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(notificationHelper.context, "Error al programar la notificación", Toast.LENGTH_SHORT).show();
        }
    }

    // Leer todas las actividades
    public Cursor getAllActividades() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ACTIVIDADES, null);
    }

    // Eliminar una actividad
    public void deleteActividad(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ACTIVIDADES, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void updateTareaCompletada(int tareaId, int completada) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_COMPLETADA, completada);
        db.update(TABLE_ACTIVIDADES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(tareaId)});
        db.close();
    }
}
