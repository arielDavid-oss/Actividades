package com.arieldavidoss.actividades.ui.home;

import android.database.Cursor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.arieldavidoss.actividades.Database.SQLiteHelper;
import com.arieldavidoss.actividades.R;
import java.util.ArrayList;
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

            } while (cursor.moveToNext());
        }
            cursor.close();
    }



}
