package com.arieldavidoss.actividades.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper  extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "actividades.db";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_ACTIVIDADES = "actividades";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOMBRE = "nombreActividad";
    private static final String COLUMN_DESCRIPCION = "descripcion";
    private static final String COLUMN_HORA_FIN = "horaFin";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE " + TABLE_ACTIVIDADES + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOMBRE + " TEXT, " +
                COLUMN_DESCRIPCION + " TEXT, " +
                COLUMN_HORA_FIN + " TEXT" +
                ")";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVIDADES);
        onCreate(db);
    }

    public boolean addActividad(String nombreActividad,String descripcion, String horaFin) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, nombreActividad);
        values.put(COLUMN_DESCRIPCION, descripcion);
        values.put(COLUMN_HORA_FIN, horaFin);
       long result = db.insert(TABLE_ACTIVIDADES, null, values);
        db.close();

        return result != -1;
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

    // Actualizar una actividad
    public void updateActividad(int id, String nombreActividad,String descripcion, String horaFin) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOMBRE, nombreActividad);
        values.put(COLUMN_DESCRIPCION, descripcion);
        values.put(COLUMN_HORA_FIN, horaFin);
        db.update(TABLE_ACTIVIDADES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}
