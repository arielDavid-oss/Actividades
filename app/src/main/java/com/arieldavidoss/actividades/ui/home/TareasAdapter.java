package com.arieldavidoss.actividades.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.arieldavidoss.actividades.R;
import java.util.List;

public class TareasAdapter extends RecyclerView.Adapter<TareasAdapter.TareaViewHolder> {

    private List<Tarea> tareas;
    private Context context;

    public TareasAdapter(Context context, List<Tarea> tareas) {
        this.context = context;
        this.tareas = tareas;
    }

    @NonNull
    @Override
    public TareaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tarea, parent, false);
        return new TareaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TareaViewHolder holder, int position) {
        Tarea tarea = tareas.get(position);
        holder.tvNombreTarea.setText(tarea.getNombre());
        holder.tvDescripcionTarea.setText(tarea.getDescripcion());
        holder.tvFechaTerminacion.setText(tarea.getFechaTerminacion());
    }

    @Override
    public int getItemCount() {
        return tareas.size();
    }

    public static class TareaViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombreTarea, tvDescripcionTarea, tvFechaTerminacion;

        public TareaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreTarea = itemView.findViewById(R.id.tvNombreTarea);
            tvDescripcionTarea = itemView.findViewById(R.id.tvDescripcionTarea);
            tvFechaTerminacion = itemView.findViewById(R.id.tvFechaTerminacion);
        }
    }
}
