package com.arieldavidoss.actividades.ui.home;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.arieldavidoss.actividades.Database.SQLiteHelper;
import com.arieldavidoss.actividades.R;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewTareas;
    private SQLiteHelper dbHelper;
    private TareasAdapter tareasAdapter;
    private List<Tarea> listaTareas;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerViewTareas = root.findViewById(R.id.recyclerViewTareas);
        recyclerViewTareas.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHelper = new SQLiteHelper(getContext());

        // Cargar tareas desde la base de datos
        cargarTareas();

        tareasAdapter = new TareasAdapter(getContext(), listaTareas);
        recyclerViewTareas.setAdapter(tareasAdapter);

        return root;
    }

    private void cargarTareas() {
        listaTareas = new ArrayList<>();
        Cursor cursor = dbHelper.getAllActividades();

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String nombre = cursor.getString(cursor.getColumnIndex("nombreActividad"));
                @SuppressLint("Range") String descripcion = cursor.getString(cursor.getColumnIndex("descripcionActividad"));
                @SuppressLint("Range") String fecha = cursor.getString(cursor.getColumnIndex("horaFin"));
                listaTareas.add(new Tarea(nombre, descripcion, fecha));
            }
        }
        if (cursor != null) {
            cursor.close();
        }
    }
}
