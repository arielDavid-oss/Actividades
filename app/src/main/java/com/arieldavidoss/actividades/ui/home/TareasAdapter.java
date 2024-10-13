package com.arieldavidoss.actividades.ui.home;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.arieldavidoss.actividades.Database.SQLiteHelper;
import com.arieldavidoss.actividades.Notifications.NotificationReceiver;
import com.arieldavidoss.actividades.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TareasAdapter extends RecyclerView.Adapter<TareasAdapter.TareaViewHolder> {

    private List<Tarea> tareaList;
    private SQLiteHelper dbHelper;

    public TareasAdapter(List<Tarea> tareaList,Context context) {
        this.tareaList = tareaList;
        this.dbHelper = new SQLiteHelper(context);
    }

    @NonNull
    @Override
    public TareaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tarea, parent, false);
        return new TareaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TareaViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Tarea tarea = tareaList.get(position);
        holder.tvNombreTarea.setText(tarea.getNombre());
        holder.tvDescripcionTarea.setText(tarea.getDescripcion());
        holder.tvFechaTerminacion.setText(tarea.getFechaTerminacion());
        if(tarea.getCompletada() == 1){
            holder.cardView.setCardBackgroundColor(Color.GREEN);
        }
        setNotificationForTask(holder.itemView.getContext(), tarea);

        holder.btnEliminarTarea.setOnClickListener(v ->{

            new AlertDialog.Builder(holder.itemView.getContext())
                    .setTitle("Eliminar tarea")
                    .setMessage("¿Estás seguro de que deseas eliminar esta tarea?")
                    .setPositiveButton("Sí", (dialog,which) -> {

                        int tareaId = tareaList.get(position).getId();


                        if (tareaId != 0) { // Verifica que el ID no sea nulo o inválido
                            dbHelper.deleteActividad(tareaId);
                            tareaList.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, tareaList.size());
                        } else {
                            Toast.makeText(holder.itemView.getContext(),
                                    "No se puede eliminar, tarea sin ID válido",
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        holder.btnCompletarTarea.setOnClickListener(v -> {
            // Cambiar el color de fondo de la tarjeta
           new AlertDialog.Builder(holder.itemView.getContext())
                   .setTitle("Completar tarea")
                     .setMessage("¿Estás seguro de que deseas completar esta tarea?")
                     .setPositiveButton("Sí", (dialog,which) -> {

                              holder.cardView.setCardBackgroundColor(Color.GREEN);
                              dbHelper.updateTareaCompletada(tarea.getId(), 1);
                              Toast.makeText(holder.itemView.getContext(), "Tarea completada", Toast.LENGTH_SHORT).show();

                     })
                     .setNegativeButton("No", null)
                     .show();
        });
    }
    private void setNotificationForTask(Context context, Tarea tarea) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date fechaTerminacion = dateFormat.parse(tarea.getFechaTerminacion());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(fechaTerminacion);
            calendar.add(Calendar.DAY_OF_YEAR, -1);  // Un día antes

            Intent intent = new Intent(context, NotificationReceiver.class);
            intent.putExtra("task_name", tarea.getNombre());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, tarea.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public int getItemCount() {
        return tareaList.size();
    }
    public static class TareaViewHolder extends RecyclerView.ViewHolder {
        TextView tvId,tvNombreTarea, tvDescripcionTarea, tvFechaTerminacion;
        ImageButton btnEliminarTarea, btnCompletarTarea;
        CardView cardView;

        public TareaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreTarea = itemView.findViewById(R.id.tvNombreTarea);
            tvDescripcionTarea = itemView.findViewById(R.id.tvDescripcionTarea);
            tvFechaTerminacion = itemView.findViewById(R.id.tvFechaTerminacion);
            btnEliminarTarea = itemView.findViewById(R.id.btnEliminarTarea);
            btnCompletarTarea = itemView.findViewById(R.id.btnCompletarTarea);
            cardView = (CardView) itemView;
        }
    }
}
