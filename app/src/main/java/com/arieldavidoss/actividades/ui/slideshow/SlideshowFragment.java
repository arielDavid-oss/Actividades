package com.arieldavidoss.actividades.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.arieldavidoss.actividades.Database.SQLiteHelper;
import com.arieldavidoss.actividades.R;
import com.arieldavidoss.actividades.databinding.FragmentSlideshowBinding;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    private EditText nombreTarea ;
    private EditText descripcionTarea ;
    private EditText fechaTerminacion ;
    private Button crearButton ;
    private SQLiteHelper dbHelper ;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);

        nombreTarea = root.findViewById(R.id.etnombre);
        descripcionTarea = root.findViewById(R.id.etdescripcion);
        fechaTerminacion = root.findViewById(R.id.edttxtfecha);
        crearButton = root.findViewById(R.id.btnCrear);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        root = binding.getRoot();

        final TextView textView = binding.textSlideshow;

        crearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = nombreTarea.getText().toString();
                String descripcion = descripcionTarea.getText().toString();
                String fecha = fechaTerminacion.getText().toString();
                // Insertar la nueva tarea en la base de datos
                boolean insertado = dbHelper.addActividad(nombre, descripcion, fecha);
                if (insertado) {
                    Toast.makeText(getContext(), "Tarea creada exitosamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Error al crear la tarea", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}