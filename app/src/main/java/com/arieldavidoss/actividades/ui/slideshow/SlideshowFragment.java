package com.arieldavidoss.actividades.ui.slideshow;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.arieldavidoss.actividades.Database.SQLiteHelper;
import com.arieldavidoss.actividades.databinding.FragmentSlideshowBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SlideshowFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    private EditText nombreTarea;
    private EditText descripcionTarea;
    private EditText fechaTerminacion;
    private Button crearButton;
    private SQLiteHelper dbHelper;
    private EditText horaTerminacion;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Usando ViewBinding para inflar la vista
        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Inicializar SQLiteHelper
        dbHelper = new SQLiteHelper(getContext());

        // Inicializar los elementos de la UI
        nombreTarea = binding.etnombre;
        descripcionTarea = binding.etdescripcion;
        fechaTerminacion = binding.edttxtfecha;
        crearButton = binding.btnCrear;
        horaTerminacion = binding.edttxthora;

        // Configurar el selector de fecha
        fechaTerminacion.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getContext(),
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String selectedDate = String.format("%02d/%02d/%04d", selectedDay, selectedMonth + 1, selectedYear);
                        fechaTerminacion.setText(selectedDate);
                    },
                    year, month, day);
            datePickerDialog.show();
        });

        // Acciones al hacer clic en el botón "Crear"
        crearButton.setOnClickListener(v -> {
            String nombre = nombreTarea.getText().toString();
            String descripcion = descripcionTarea.getText().toString();
            String fecha = fechaTerminacion.getText().toString();
            String hora = horaTerminacion.getText().toString();

            // Validar que los campos no estén vacíos
            if (nombre.isEmpty() || descripcion.isEmpty() || fecha.isEmpty() || hora.isEmpty()) {
                Toast.makeText(getContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }
            String fechaHoraCombinada = fecha + " " + hora;
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            try {
                // Verificar si la fecha y hora están en el formato correcto
                Date fechaHora = dateFormat.parse(fechaHoraCombinada);

                // Insertar la nueva tarea en la base de datos
                boolean insertado = dbHelper.addActividad(nombre, descripcion, dateFormat.format(fechaHora));
                if (insertado) {
                    Toast.makeText(getContext(), "Tarea creada exitosamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Error al crear la tarea", Toast.LENGTH_SHORT).show();
                }
            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Error al procesar la fecha y hora", Toast.LENGTH_SHORT).show();
            }
        });

        // Configurar el selector de hora
        horaTerminacion.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    getContext(),
                    (view, hourOfDay, minute1) -> {
                        String selectedTime = String.format("%02d:%02d", hourOfDay, minute1);
                        horaTerminacion.setText(selectedTime);
                    },
                    hour, minute, true);
            timePickerDialog.show();
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
