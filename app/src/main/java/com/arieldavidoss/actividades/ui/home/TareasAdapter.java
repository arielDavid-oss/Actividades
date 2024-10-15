package com.arieldavidoss.actividades.ui.home;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
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

    private final List<Tarea> tareaList;
    private final SQLiteHelper dbHelper;

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
            holder.cardView.setCardBackgroundColor(Color.parseColor("#388E3C"));
            holder.tvNombreTarea.setPaintFlags(holder.tvNombreTarea.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        setNotificationForTask(holder.itemView.getContext(), tarea);

        holder.btnEliminarTarea.setOnClickListener(v -> new AlertDialog.Builder(holder.itemView.getContext())
                .setTitle("Eliminar tarea")
                .setMessage("¿Estás seguro de que deseas eliminar esta tarea?")
                .setPositiveButton("Sí", (dialog,which) -> {

                    int tareaId = tareaList.get(position).getId();


                    if (tareaId != 0) { // Verifica que el ID no sea nulo o inválido
                        dbHelper.deleteActividad(tareaId);
                        tareaList.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, tareaList.size());
                        cancelNotification(holder.itemView.getContext(), tarea.getId());
                    } else {
                        Toast.makeText(holder.itemView.getContext(),
                                "No se puede eliminar, tarea sin ID válido",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show());

        holder.btnCompletarTarea.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
            builder.setTitle("Completar tarea");
            builder.setMessage("¿Estás seguro de que deseas completar esta tarea?");
            builder.setPositiveButton("Sí", (dialog, which) -> {

                // Transición suave de color a verde
                int colorFrom = ((CardView) holder.itemView).getCardBackgroundColor().getDefaultColor();
                int colorTo = Color.parseColor("#388E3C");
                ObjectAnimator colorAnimation = ObjectAnimator.ofObject(holder.cardView, "cardBackgroundColor", new ArgbEvaluator(), colorFrom, colorTo);
                colorAnimation.setDuration(500); // Duración de la transición
                colorAnimation.start();

                // Efecto de tachado en el nombre de la tarea
                holder.tvNombreTarea.setPaintFlags(holder.tvNombreTarea.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                // Actualizar en la base de datos
                dbHelper.updateTareaCompletada(tarea.getId(), 1);
                Toast.makeText(holder.itemView.getContext(), "Tarea completada", Toast.LENGTH_SHORT).show();
                cancelNotification(holder.itemView.getContext(), tarea.getId());

            });
            builder.setNegativeButton("No", null);
            builder.show();
        });
    }
    @SuppressLint("ScheduleExactAlarm")
    private void setNotificationForTask(Context context, Tarea tarea) {
        if (tarea.getCompletada() == 1) {
            // Si la tarea ya está completada, no se programa ninguna notificación
            cancelNotification(context, tarea.getId());  // Por si acaso la notificación ya fue programada anteriormente
            return;
        }
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat =  new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            Date fechaTerminacion = dateFormat.parse(tarea.getFechaTerminacion());
            Calendar calendar = Calendar.getInstance();
            assert fechaTerminacion != null;
            calendar.setTime(fechaTerminacion);
            calendar.add(Calendar.DAY_OF_YEAR, -1);  // Un día antes

            Intent intent = new Intent(context, NotificationReceiver.class);
            intent.putExtra("task_name", tarea.getNombre());
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, tarea.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);


            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cancelNotification(Context context, int taskId) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, taskId, intent, PendingIntent.FLAG_UPDATE_CURRENT| PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }

    @Override
    public int getItemCount() {
        return tareaList.size();
    }
    public static class TareaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreTarea, tvDescripcionTarea, tvFechaTerminacion;
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
