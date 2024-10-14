package com.arieldavidoss.actividades.ui.home;

import android.database.Cursor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.arieldavidoss.actividades.Database.SQLiteHelper;
import com.arieldavidoss.actividades.Notifications.NotificationHelper;
import com.arieldavidoss.actividades.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    private SQLiteHelper dbHelper;
    private final List<Tarea> tareaList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView recyclerViewTareas = view.findViewById(R.id.recyclerViewTareas);
        recyclerViewTareas.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHelper = new SQLiteHelper(getContext());

        // Cargar tareas desde la base de datos
        cargarTareas();

        TareasAdapter tareaAdapter = new TareasAdapter(tareaList, getContext());
        recyclerViewTareas.setAdapter(tareaAdapter);

        return view;
    }

    private void cargarTareas() {
        tareaList.clear();

        Cursor cursor = dbHelper.getAllActividades();


        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombreActividad"));
                String descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"));
                int completada = cursor.getInt(cursor.getColumnIndexOrThrow("completada"));
                String horaFin = cursor.getString(cursor.getColumnIndexOrThrow("horaFin"));

                // Agrega la tarea a la lista
                tareaList.add(new Tarea(id,nombre, descripcion,completada ,horaFin));

              //  programarNotificacion(nombre, id, horaFin);
            } while (cursor.moveToNext());
        }
            cursor.close();
    }

//    private void programarNotificacion(String nombreActividad, int tareaId, String horaFin) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
//        try {
//            // Convertir la hora de fin a un objeto Date
//            Date fechaFin = dateFormat.parse(horaFin);
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(fechaFin);
//
//            // Restar un día para programar la notificación un día antes
//            calendar.add(Calendar.DAY_OF_MONTH, -1);
//
//            // Programar la notificación
//            NotificationHelper.scheduleNotification(getContext(), nombreActividad, calendar);
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//            Toast.makeText(getContext(), "Error al programar la notificación: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }

}
