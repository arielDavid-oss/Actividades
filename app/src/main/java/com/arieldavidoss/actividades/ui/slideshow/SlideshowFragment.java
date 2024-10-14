package com.arieldavidoss.actividades.ui.slideshow;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
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
        fechaTerminacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener la fecha actual
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // Crear un DatePickerDialog y mostrarlo
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(),
                        new DatePickerDialog.OnDateSetListener(){
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                // Formatear la fecha seleccionada y mostrarla en el EditText
                                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                                fechaTerminacion.setText(selectedDate);
                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });
        // Acciones al hacer clic en el botón "Crear"
        crearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = nombreTarea.getText().toString();
                String descripcion = descripcionTarea.getText().toString();
                String fecha = fechaTerminacion.getText().toString();
                String hora = horaTerminacion.getText().toString();

                // Validar que los campos no estén vacíos
                if (nombre.isEmpty() || descripcion.isEmpty() || fecha.isEmpty()) {
                    Toast.makeText(getContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                    return;
                }
                String fechaHoraCombinada = fecha + " " + hora;

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                try {
                    // Verificar si la fecha y hora están en el formato correcto
                    Date fechaHora = dateFormat.parse(fechaHoraCombinada);

                    // Insertar la nueva tarea en la base de datos con la fecha y hora combinadas
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
            }

        });


        horaTerminacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        getContext(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String selectedTime = String.format("%02d:%02d", hourOfDay, minute);
                                horaTerminacion.setText(selectedTime);
                            }
                        },
                        hour, minute, true);
                timePickerDialog.show();
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
